/*
 * Polyglotter (http://polyglotter.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * Polyglotter is free software. Unless otherwise indicated, all code in Polyglotter
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * Polyglotter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.modeshape.modeler.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.Value;

import org.infinispan.commons.util.ReflectionUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modeshape.common.util.CheckArg;
import org.modeshape.jcr.ExtensionLogger;
import org.modeshape.jcr.JcrLexicon;
import org.modeshape.jcr.api.JcrTools;
import org.modeshape.jcr.api.nodetype.NodeTypeManager;
import org.modeshape.jcr.api.sequencer.Sequencer;
import org.modeshape.modeler.Metamodel;
import org.modeshape.modeler.MetamodelManager;
import org.modeshape.modeler.Modeler;
import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.ModelerI18n;
import org.modeshape.modeler.ModelerLexicon;
import org.modeshape.modeler.internal.task.TaskWithResult;
import org.modeshape.modeler.internal.task.WriteSystemTask;
import org.modeshape.modeler.spi.metamodel.DependencyProcessor;
import org.modeshape.modeler.spi.metamodel.Exporter;
import org.modeshape.modeler.spi.metamodel.Importer;
import org.polyglotter.common.Logger;

/**
 * The default implementation of a metamodel modeler.
 */
final class MetamodelManagerImpl implements MetamodelManager {

    static final Logger LOGGER = Logger.getLogger( MetamodelManagerImpl.class );

    static final String MODESHAPE_GROUP = "org/modeshape";

    private static final String SEQUENCER_PREFIX = "modeshape-sequencer-";

    // pass in category, version, name
    private static final String SEQUENCER_PATH_PATTERN = MODESHAPE_GROUP + "/" + SEQUENCER_PREFIX + "%s/%s/%s";

    // pass in category, version
    private static final String SEQUENCER_ZIP_PATTERN = SEQUENCER_PREFIX + "%s-%s-module-with-dependencies.zip";

    private final MetamodelInstaller metamodelInstaller;
    final ModelerImpl modeler;
    final LinkedList< URL > metamodelRepositories = new LinkedList<>();
    final Set< Metamodel > metamodels = new HashSet<>();
    final LibraryClassLoader libraryClassLoader = new LibraryClassLoader();
    final Path library;

    MetamodelManagerImpl( final ModelerImpl modeler ) throws ModelerException {
        this.modeler = modeler;
        this.metamodelInstaller = new MetamodelInstaller();

        // setup classpath area for metamodel archives
        try {
            library = Files.createTempDirectory( null );
        } catch ( final IOException e ) {
            throw new ModelerException( e );
        }
        library.toFile().deleteOnExit();

        // load caches from MS repository
        modeler.run( this, new WriteSystemTask() {

            @Override
            public void run( final Session session,
                             final Node systemNode ) throws Exception {
                loadMetamodelRepositories( session, systemNode );
                loadCategories( session, systemNode );
            }
        } );
    }

    /**
     * @param category
     *        the category name whose node is being requested (cannot be <code>null</code>)
     * @param systemNode
     *        the system node (cannot be <code>null</code>)
     * @param create
     *        <code>true</code> if the category node should be created if not found
     * @return the category node or <code>null</code> if not found
     * @throws Exception
     *         if the categories parent node is not found or if another error occurs
     */
    Node categoryNode( final String category,
                       final Node systemNode,
                       final boolean create ) throws Exception {
        if ( !systemNode.hasNode( ModelerLexicon.METAMODEL_CATEGORIES ) ) {
            throw new ModelerException( ModelerI18n.metamodelCategoryParentNodeNotFound, systemNode.getPath() );
        }

        final Node categoriesNode = systemNode.getNode( ModelerLexicon.METAMODEL_CATEGORIES );
        Node categoryNode = null;

        if ( !categoriesNode.hasNode( category ) ) {
            if ( create ) {
                categoryNode = categoriesNode.addNode( category, ModelerLexicon.Metamodel.Category.NODE_TYPE );
                categoryNode.addNode( ModelerLexicon.Metamodel.Category.ARCHIVES, ModelerLexicon.Metamodel.Category.ARCHIVES );
                categoryNode.addNode( ModelerLexicon.Metamodel.Category.METAMODELS, ModelerLexicon.Metamodel.Category.METAMODELS );
                LOGGER.debug( "Created category node '%s'", category );
            }
        } else {
            categoryNode = categoriesNode.getNode( category );
            LOGGER.debug( "Found category node '%s'", category );
        }

        return categoryNode;
    }

    /**
     * @param fileNode
     *        the file node
     * @param metamodels
     *        the metamodels applicable to the supplied file node
     * @return the default metamodel for the supplied file node
     * @throws Exception
     *         if any problem occurs
     */
    public Metamodel defaultMetamodel( final Node fileNode,
                                       final Metamodel[] metamodels ) throws Exception {
        return metamodels.length == 0 ? null : metamodels[ 0 ];
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#defaultMetamodel(String)
     */
    @Override
    public Metamodel defaultMetamodel( final String filePath ) throws ModelerException {
        CheckArg.isNotEmpty( filePath, "filePath" );
        return modeler.run( new TaskWithResult< Metamodel >() {

            @Override
            public Metamodel run( final Session session ) throws Exception {
                final Node node = modeler.dataNode( session, filePath );
                final Metamodel metamodel = defaultMetamodel( node, metamodels( node ) );
                return metamodel == null ? null : metamodel;
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#install(String)
     */
    @Override
    public void install( final String category ) throws ModelerException {
        CheckArg.isNotEmpty( category, "category" );
        LOGGER.debug( "Installing category '%s'", category );
        modeler.run( this, new WriteSystemTask() {

            @Override
            public void run( final Session session,
                             final Node systemNode ) throws Exception {
                LOGGER.debug( "Installing importer for category '%s'", category );
                installImporter( category, session, systemNode );

                LOGGER.debug( "Installing metamodel for category '%s'", category );
                installMetamodel( category, session, systemNode );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#installableMetamodelCategories()
     */
    @Override
    public String[] installableMetamodelCategories() throws ModelerException {
        final Set< String > categories = new HashSet<>();
        for ( final URL repositoryUrl : metamodelRepositories ) {
            try {
                if ( repositoryUrl.getProtocol().startsWith( "file" ) ) {
                    final String path = ( repositoryUrl.getFile() + File.separatorChar + MODESHAPE_GROUP );
                    final File folder = new File( path );
                    final File[] files = folder.listFiles();

                    if ( files == null ) continue;

                    for ( final File file : files ) {
                        final String name = file.getName();
                        if ( name.contains( "sequencer-" ) )
                            categories.add( name.substring( name.indexOf( "sequencer-" ) + "sequencer-".length() ) );
                    }
                } else {
                    final Document doc = Jsoup.connect( path( repositoryUrl.toString(), MODESHAPE_GROUP ) ).get();
                    final Elements elements = doc.getElementsMatchingOwnText( "sequencer-" );
                    for ( final Element element : elements ) {
                        final String href = element.attr( "href" );
                        categories.add( href.substring( href.indexOf( "sequencer-" ) + "sequencer-".length(),
                                                        href.lastIndexOf( '/' ) ) );
                    }
                }
            } catch ( final IOException e ) {
                throw new ModelerException( e );
            }
        }
        return categories.toArray( new String[ categories.size() ] );
    }

    void installImporter( final String category,
                          final Session session,
                          final Node systemNode ) throws Exception {
        // don't install importer if already installed
        if ( categoryNode( category, systemNode, false ) != null ) {
            return;
        }

        final Node categoryNode = categoryNode( category, systemNode, true );
        final String archiveName = String.format( SEQUENCER_ZIP_PATTERN, category, version() );
        final Path archivePath = library.resolve( archiveName );
        final String sequencerArchivePath = String.format( SEQUENCER_PATH_PATTERN, category, version(), archiveName );
        boolean sequencerArchiveFound = false;

        // loop through repositories until we find the sequencer archive
        for ( final URL repositoryUrl : metamodelRepositories ) {
            final URL url = new URL( path( repositoryUrl.toString(), sequencerArchivePath ) );
            InputStream urlStream = null;
            IOException err = null;

            // copy archive over to library if found at this repository
            try {
                try {
                    urlStream = url.openStream();
                    sequencerArchiveFound = true;
                } catch ( final IOException e ) {
                    continue;
                }

                Files.copy( urlStream, archivePath );
            } catch ( final IOException e ) {
                err = e;
            } finally {
                if ( urlStream != null ) try {
                    urlStream.close();
                } catch ( final IOException e ) {
                    if ( err == null ) throw e;
                    err.addSuppressed( e );
                    throw err;
                }
            }

            try ( final ZipFile archive = new ZipFile( archivePath.toFile() ) ) {
                final Collection< String > potentialSequencerClassNames = new ArrayList<>();
                final Node archivesNode = categoryNode.getNode( ModelerLexicon.Metamodel.Category.ARCHIVES );

                for ( final Enumeration< ? extends ZipEntry > archiveIter = archive.entries(); archiveIter.hasMoreElements(); ) {
                    final ZipEntry archiveEntry = archiveIter.nextElement();
                    if ( archiveEntry.isDirectory() ) continue;

                    // see if archive should be ignored
                    String name = archiveEntry.getName().toLowerCase();

                    if ( !name.endsWith( ".jar" ) || name.endsWith( "-tests.jar" ) || name.endsWith( "-sources.jar" ) ) {
                        LOGGER.debug( "Ignoring Jar: %s", name );
                        continue;
                    }

                    // see if this jar has already been installed
                    final Path jarPath =
                        library.resolve( archiveEntry.getName().substring( archiveEntry.getName().lastIndexOf( '/' ) + 1 ) );

                    if ( jarPath.toFile().exists() ) {
                        LOGGER.debug( "Jar already installed: %s", jarPath );
                        continue;
                    }

                    // copy to library path
                    try ( final InputStream stream = archive.getInputStream( archiveEntry ) ) {
                        Files.copy( stream, jarPath );
                        jarPath.toFile().deleteOnExit();
                    }

                    // add to classpath
                    libraryClassLoader.addURL( jarPath.toUri().toURL() );

                    // add jar to category node in repository
                    try ( final InputStream stream = archive.getInputStream( archiveEntry ) ) {
                        new JcrTools().uploadFile( session,
                                                   archivesNode.getPath() + '/' + jarPath.getFileName().toString(),
                                                   stream );
                    }

                    // Iterate through entries looking for appropriate sequencer classes
                    if ( jarPath.getFileName().toString().startsWith( SEQUENCER_PREFIX ) )
                        try ( final ZipFile jar = new ZipFile( jarPath.toFile() ) ) {
                            for ( final Enumeration< ? extends ZipEntry > jarIter = jar.entries(); jarIter.hasMoreElements(); ) {
                                final ZipEntry jarEntry = jarIter.nextElement();
                                if ( jarEntry.isDirectory() ) continue;

                                name = jarEntry.getName();

                                // see if class is a possible sequencer
                                if ( name.endsWith( "Sequencer.class" ) ) {
                                    potentialSequencerClassNames.add( name.replace( '/', '.' ).substring( 0, name.length() - ".class".length() ) );
                                    LOGGER.debug( "Potential sequencer: %s", name );
                                }
                            }
                        }
                }

                final Node metamodelsNode = categoryNode.getNode( ModelerLexicon.Metamodel.Category.METAMODELS );

                // try and load each potential sequencer class that was found
                for ( final String sequencerClassName : potentialSequencerClassNames ) {
                    Class< ? > sequencerClass = null;

                    try {
                        sequencerClass = libraryClassLoader.loadClass( sequencerClassName );

                        if ( Sequencer.class.isAssignableFrom( sequencerClass )
                             && !Modifier.isAbstract( sequencerClass.getModifiers() ) ) {
                            String id = Modeler.class.getPackage().getName() + '.' + category + '.'
                                        + sequencerClass.getSimpleName();
                            id = id.endsWith( "Sequencer" ) ? id.substring( 0, id.length() - "Sequencer".length() ) : id;

                            // add metamodel to MS repository
                            final Node metamodelNode = metamodelsNode.addNode( id, ModelerLexicon.Metamodel.NODE_TYPE );
                            metamodelNode.setProperty( ModelerLexicon.Metamodel.IMPORTER_CLASS_NAME, sequencerClass.getName() );

                            // add to cache
                            final MetamodelImpl metamodel = new MetamodelImpl( category, id );
                            metamodel.setImporter( sequencerImporter( session, sequencerClass ) );
                            metamodels.add( metamodel );
                        }
                    } catch ( final NoClassDefFoundError | ClassNotFoundException ignored ) {
                        LOGGER.debug( "Potential importer class '%s' cannot be loaded", sequencerClass );
                    }
                }
            }

            archivePath.toFile().delete();
        }

        if ( !sequencerArchiveFound ) {
            throw new IllegalArgumentException( ModelerI18n.unableToFindMetamodelCategory.text( category ) );
        }
    }

    void installMetamodel( final String category,
                           final Session session,
                           final Node systemNode ) throws Exception {
        final Node categoryNode = categoryNode( category, systemNode, false );

        if ( metamodelInstaller.install( categoryNode,
                                         libraryClassLoader,
                                         library,
                                         metamodelRepositories,
                                         version(),
                                         metamodels ) ) {
            LOGGER.debug( "Installed extensions for category '%s'", category );
        } else {
            LOGGER.debug( "No extensions installed for category '%s'", category );
        }
    }

    void loadCategories( final Session session,
                         final Node systemNode ) throws Exception {
        if ( !systemNode.hasNode( ModelerLexicon.METAMODEL_CATEGORIES ) ) {
            systemNode.addNode( ModelerLexicon.METAMODEL_CATEGORIES );
            LOGGER.debug( "'%s' node created", ModelerLexicon.METAMODEL_CATEGORIES );
        } else {
            final Node categoriesNode = systemNode.getNode( ModelerLexicon.METAMODEL_CATEGORIES );

            for ( final NodeIterator iter = categoriesNode.getNodes(); iter.hasNext(); ) {
                final Node categoryNode = iter.nextNode();
                loadCategoryArchives( session, categoryNode );
                loadMetamodels( session, categoryNode );
            }
        }
    }

    void loadCategoryArchives( final Session session,
                               final Node categoryNode ) throws Exception {
        if ( !categoryNode.hasNode( ModelerLexicon.Metamodel.Category.ARCHIVES ) ) {
            categoryNode.addNode( ModelerLexicon.Metamodel.Category.ARCHIVES );
            LOGGER.debug( "'%s' node created", ModelerLexicon.Metamodel.Category.ARCHIVES );
        } else {
            final Node archivesNode = categoryNode.getNode( ModelerLexicon.Metamodel.Category.ARCHIVES );

            for ( final NodeIterator iter = archivesNode.getNodes(); iter.hasNext(); ) {
                final Node archiveNode = iter.nextNode();
                final Path archivePath = library.resolve( archiveNode.getName() );
                final Node contentNode = archiveNode.getNode( JcrLexicon.CONTENT.getString() );

                try ( InputStream stream = contentNode.getProperty( JcrLexicon.DATA.getString() ).getBinary().getStream() ) {
                    Files.copy( stream, archivePath );
                }

                archivePath.toFile().deleteOnExit();
                libraryClassLoader.addURL( archivePath.toUri().toURL() );
                LOGGER.debug( "Loaded archive: %s", archivePath );
            }
        }
    }

    void loadMetamodelRepositories( final Session session,
                                    final Node systemNode ) throws Exception {
        if ( !systemNode.hasProperty( ModelerLexicon.METAMODEL_REPOSITORIES ) ) {
            final Value[] vals = new Value[ 2 ];
            vals[ 0 ] = session.getValueFactory().createValue( JBOSS_METAMODEL_REPOSITORY );
            vals[ 1 ] = session.getValueFactory().createValue( MAVEN_METAMODEL_REPOSITORY );
            systemNode.setProperty( ModelerLexicon.METAMODEL_REPOSITORIES, vals );
        }

        for ( final String url : JcrUtil.values( systemNode, ModelerLexicon.METAMODEL_REPOSITORIES ) ) {
            metamodelRepositories.add( new URL( url ) );
        }
    }

    void loadMetamodels( final Session session,
                         final Node categoryNode ) throws Exception {
        if ( !categoryNode.hasNode( ModelerLexicon.Metamodel.Category.METAMODELS ) ) {
            categoryNode.addNode( ModelerLexicon.Metamodel.Category.METAMODELS );
            LOGGER.debug( "'%s' node created", ModelerLexicon.Metamodel.Category.METAMODELS );
        } else {
            final Node metamodelsNode = categoryNode.getNode( ModelerLexicon.Metamodel.Category.METAMODELS );
            final String category = categoryNode.getName();

            for ( final NodeIterator iter = metamodelsNode.getNodes(); iter.hasNext(); ) {
                final Node metamodelNode = iter.nextNode();
                final MetamodelImpl metamodel = new MetamodelImpl( category, metamodelNode.getName() );
                metamodels.add( metamodel );
                if ( metamodelNode.hasProperty( ModelerLexicon.Metamodel.IMPORTER_CLASS_NAME ) ) {
                    final String className = JcrUtil.value( metamodelNode,
                                                            ModelerLexicon.Metamodel.IMPORTER_CLASS_NAME );
                    metamodel.setImporter( sequencerImporter( session, libraryClassLoader.loadClass( className ) ) );
                }

                if ( metamodelNode.hasProperty( ModelerLexicon.Metamodel.EXPORTER_CLASS_NAME ) ) {
                    final String className = JcrUtil.value( metamodelNode,
                                                            ModelerLexicon.Metamodel.EXPORTER_CLASS_NAME );
                    metamodel.setExporter( ( Exporter ) libraryClassLoader.loadClass( className ).newInstance() );
                }

                if ( metamodelNode.hasProperty( ModelerLexicon.Metamodel.DEPENDENCY_PROCESSOR_CLASS_NAME ) ) {
                    final String className = JcrUtil.value( metamodelNode,
                                                            ModelerLexicon.Metamodel.DEPENDENCY_PROCESSOR_CLASS_NAME );
                    metamodel.setDependencyProcessor( ( DependencyProcessor ) libraryClassLoader.loadClass( className ).newInstance() );
                }
                LOGGER.debug( "Loaded metamodel: %s", metamodel.id() );
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#metamodel(String)
     */
    @Override
    public Metamodel metamodel( final String id ) {
        CheckArg.isNotEmpty( id, "id" );
        for ( final Metamodel metamodel : metamodels )
            if ( id.equals( metamodel.id() ) ) return metamodel;
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#metamodelCategories()
     */
    @Override
    public String[] metamodelCategories() {
        final Set< String > categories = new HashSet<>();
        for ( final Metamodel metamodel : metamodels )
            categories.add( metamodel.category() );
        return categories.toArray( new String[ categories.size() ] );
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#metamodelRepositories()
     */
    @Override
    public URL[] metamodelRepositories() {
        return metamodelRepositories.toArray( new URL[ metamodelRepositories.size() ] );
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#metamodels()
     */
    @Override
    public Metamodel[] metamodels() {
        return metamodels.toArray( new Metamodel[ metamodels.size() ] );
    }

    /**
     * @param fileNode
     *        the file node
     * @return the metamodels applicable to the supplied file node
     * @throws Exception
     *         if any problem occurs
     */
    public Metamodel[] metamodels( final Node fileNode ) throws Exception {
        final Set< Metamodel > applicableMetamodels = new HashSet<>();
        final String mimeType = JcrUtil.value( fileNode.getNode( JcrLexicon.CONTENT.getString() ),
                                               JcrLexicon.MIMETYPE.getString() );

        for ( final Metamodel metamodel : metamodels() ) {
            if ( ( ( MetamodelImpl ) metamodel ).importer().supports( mimeType ) ) applicableMetamodels.add( metamodel );
        }

        return applicableMetamodels.toArray( new Metamodel[ applicableMetamodels.size() ] );
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#metamodelsForArtifact(String)
     */
    @Override
    public Metamodel[] metamodelsForArtifact( final String filePath ) throws ModelerException {
        CheckArg.isNotEmpty( filePath, "filePath" );
        return modeler.run( new TaskWithResult< Metamodel[] >() {

            @Override
            public final Metamodel[] run( final Session session ) throws Exception {
                return metamodels( modeler.dataNode( session, filePath ) );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#metamodelsForCategory(String)
     */
    @Override
    public Metamodel[] metamodelsForCategory( final String category ) {
        CheckArg.isNotEmpty( category, "category" );
        final Set< Metamodel > metamodels = new HashSet<>();
        for ( final Metamodel metamodel : metamodels )
            if ( category.equals( metamodel.category() ) ) metamodels.add( metamodel );
        return metamodels.toArray( new Metamodel[ metamodels.size() ] );
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#moveMetamodelRepositoryDown(URL)
     */
    @Override
    public URL[] moveMetamodelRepositoryDown( final URL repositoryUrl ) throws ModelerException {
        CheckArg.isNotNull( repositoryUrl, "repositoryUrl" );
        final int ndx = metamodelRepositories.indexOf( repositoryUrl );
        if ( ndx < 0 ) throw new IllegalArgumentException( ModelerI18n.urlNotFound.text( repositoryUrl ) );
        metamodelRepositories.remove( ndx );
        metamodelRepositories.add( Math.min( ndx + 1, metamodelRepositories.size() ), repositoryUrl );
        saveMetamodelRepositories();
        return metamodelRepositories();
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#moveMetamodelRepositoryUp(URL)
     */
    @Override
    public URL[] moveMetamodelRepositoryUp( final URL repositoryUrl ) throws ModelerException {
        CheckArg.isNotNull( repositoryUrl, "repositoryUrl" );
        final int ndx = metamodelRepositories.indexOf( repositoryUrl );
        if ( ndx < 0 ) throw new IllegalArgumentException( ModelerI18n.urlNotFound.text( repositoryUrl ) );
        metamodelRepositories.remove( ndx );
        metamodelRepositories.add( Math.max( ndx - 1, 0 ), repositoryUrl );
        saveMetamodelRepositories();
        return metamodelRepositories();
    }

    private String path( final String prefix,
                         final String suffix ) {
        if ( prefix.charAt( prefix.length() - 1 ) == '/' )
            return suffix.charAt( 0 ) == '/' ? prefix + suffix.substring( 1 ) : prefix + suffix;
        return suffix.charAt( 0 ) == '/' ? prefix + suffix : prefix + '/' + suffix;
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#registerMetamodelRepository(URL)
     */
    @Override
    public URL[] registerMetamodelRepository( final URL repositoryUrl ) throws ModelerException {
        CheckArg.isNotNull( repositoryUrl, "repositoryUrl" );
        if ( !metamodelRepositories.contains( repositoryUrl ) ) {
            metamodelRepositories.addFirst( repositoryUrl );
            saveMetamodelRepositories();
        }
        return metamodelRepositories();
    }

    private void saveMetamodelRepositories() throws ModelerException {
        modeler.run( this, new WriteSystemTask() {

            @Override
            public void run( final Session session,
                             final Node systemNode ) throws Exception {
                final Value[] vals = new Value[ metamodelRepositories.size() ];
                int ndx = 0;
                for ( final URL url : metamodelRepositories )
                    vals[ ndx++ ] = session.getValueFactory().createValue( url.toString() );
                systemNode.setProperty( ModelerLexicon.METAMODEL_REPOSITORIES, vals );
            }
        } );
    }

    Importer sequencerImporter( final Session session,
                                final Class< ? > sequencerClass ) throws Exception {
        final Sequencer sequencer = ( Sequencer ) sequencerClass.newInstance();
        ReflectionUtil.setValue( sequencer, "logger", ExtensionLogger.getLogger( sequencerClass ) );
        ReflectionUtil.setValue( sequencer, "repositoryName",
                                 session.getRepository().getDescriptor( org.modeshape.jcr.api.Repository.REPOSITORY_NAME ) );
        ReflectionUtil.setValue( sequencer, "name", sequencerClass.getSimpleName() );
        sequencer.initialize( session.getWorkspace().getNamespaceRegistry(),
                              ( NodeTypeManager ) session.getWorkspace().getNodeTypeManager() );
        return new SequencerImporter( sequencer );
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#uninstall(String)
     */
    @Override
    public void uninstall( final String category ) throws ModelerException {
        CheckArg.isNotEmpty( category, "category" );

        // delete from cache all metamodels of that category
        boolean deleted = false;

        for ( final Iterator< Metamodel > iter = metamodels.iterator(); iter.hasNext(); ) {
            final Metamodel metamodel = iter.next();

            if ( category.equals( metamodel.category() ) ) {
                deleted = true;
                iter.remove();
                LOGGER.debug( "Uninstalled metamodel '%s'", metamodel.id() );
            }
        }

        if ( !deleted ) {
            throw new ModelerException( ModelerI18n.unableToFindMetamodelCategory, category );
        }

        // delete from MS repository
        modeler.run( this, new WriteSystemTask() {

            @Override
            public void run( final Session session,
                             final Node systemNode ) throws Exception {
                final Node categoryNode = categoryNode( category, systemNode, false );
                if ( categoryNode == null ) throw new ModelerException( ModelerI18n.unableToFindMetamodelCategory, category );

                // remove category archive paths from classpath
                if ( categoryNode.hasNode( ModelerLexicon.Metamodel.Category.ARCHIVES ) ) {
                    final Node archivesNode = categoryNode.getNode( ModelerLexicon.Metamodel.Category.ARCHIVES );

                    for ( final NodeIterator iter = archivesNode.getNodes(); iter.hasNext(); ) {
                        final Node archiveNode = iter.nextNode();
                        final Path archivePath = library.resolve( archiveNode.getName() );
                        if ( !archivePath.toFile().delete() ) LOGGER.debug( "Unable to delete jar: %s", archivePath );
                    }
                }

                // remove from MS repo now
                categoryNode.remove();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetamodelManager#unregisterMetamodelRepository(URL)
     */
    @Override
    public URL[] unregisterMetamodelRepository( final URL repositoryUrl ) throws ModelerException {
        CheckArg.isNotNull( repositoryUrl, "repositoryUrl" );
        if ( metamodelRepositories.remove( repositoryUrl ) ) saveMetamodelRepositories();
        return metamodelRepositories();
    }

    private String version() throws ModelerException {
        return modeler.repository().getDescriptor( Repository.REP_VERSION_DESC );
    }

    class LibraryClassLoader extends URLClassLoader {

        LibraryClassLoader() {
            super( new URL[ 0 ], LibraryClassLoader.class.getClassLoader() );
        }

        @Override
        protected void addURL( final URL url ) {
            super.addURL( url );
        }
    }
}
