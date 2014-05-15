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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modeshape.common.util.CheckArg;
import org.modeshape.jcr.JcrLexicon;
import org.modeshape.jcr.api.JcrTools;
import org.modeshape.jcr.api.sequencer.Sequencer;
import org.modeshape.modeler.ModeShapeModeler;
import org.modeshape.modeler.ModelType;
import org.modeshape.modeler.ModelTypeManager;
import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.ModelerI18n;
import org.modeshape.modeler.ModelerLexicon;
import org.polyglotter.common.Logger;

/**
 * The default implementation of a model type manager.
 */
public final class ModelTypeManagerImpl implements ModelTypeManager {

    static final Logger LOGGER = Logger.getLogger( ModelTypeManagerImpl.class );

    private static final String MODESHAPE_GROUP = "org/modeshape";

    // pass in category, version, name
    private static final String SEQUENCER_PATH_PATTERN = MODESHAPE_GROUP + "/modeshape-sequencer-%s/%s/%s";

    // pass in category, version
    private static final String SEQUENCER_ZIP_PATTERN = "modeshape-sequencer-%s-%s-module-with-dependencies.zip";

    private final ExtensionInstaller extensionInstaller;
    final Manager manager;
    final LinkedList< URL > modelTypeRepositories = new LinkedList<>();
    final Set< ModelType > modelTypes = new HashSet<>();
    final LibraryClassLoader libraryClassLoader = new LibraryClassLoader();
    final Path library;

    ModelTypeManagerImpl( final Manager manager ) throws ModelerException {
        this.manager = manager;
        this.extensionInstaller = new ExtensionInstaller();

        // setup classpath area for model type archives
        try {
            library = Files.createTempDirectory( null );
        } catch ( final IOException e ) {
            throw new ModelerException( e );
        }
        library.toFile().deleteOnExit();

        // load caches from MS repository
        manager.run( this, new SystemTask< Void >() {

            @Override
            public Void run( final Session session,
                             final Node systemNode ) throws Exception {
                loadModelTypeRepositories( session, systemNode );
                loadCategories( session, systemNode );
                session.save();

                return null;
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
        if ( !systemNode.hasNode( ModelerLexicon.MODEL_TYPE_CATEGORIES ) ) {
            throw new ModelerException( ModelerI18n.modelTypeCategoryParentNodeNotFound, systemNode.getPath() );
        }

        final Node categoriesNode = systemNode.getNode( ModelerLexicon.MODEL_TYPE_CATEGORIES );
        Node categoryNode = null;

        if ( !categoriesNode.hasNode( category ) ) {
            if ( create ) {
                categoryNode = categoriesNode.addNode( category, ModelerLexicon.Category.NODE_TYPE );
                categoryNode.addNode( ModelerLexicon.Category.ARCHIVES, ModelerLexicon.Category.ARCHIVES );
                categoryNode.addNode( ModelerLexicon.Category.MODEL_TYPES, ModelerLexicon.Category.MODEL_TYPES );
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
     * @param modelTypes
     *        the model types applicable to the supplied file node
     * @return the default model type for the supplied file node
     * @throws Exception
     *         if any problem occurs
     */
    public ModelType defaultModelType( final Node fileNode,
                                       final ModelType[] modelTypes ) throws Exception {
        final String ext = fileNode.getName().substring( fileNode.getName().lastIndexOf( '.' ) + 1 );
        for ( final ModelType type : modelTypes )
            for ( final String typeExt : type.sourceFileExtensions() )
                if ( typeExt.equals( ext ) ) return type;
        return modelTypes.length == 0 ? null : modelTypes[ 0 ];
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#defaultModelType(String)
     */
    @Override
    public ModelType defaultModelType( final String filePath ) throws ModelerException {
        CheckArg.isNotEmpty( filePath, "filePath" );
        return manager.run( new Task< ModelType >() {

            @Override
            public ModelType run( final Session session ) throws Exception {
                final Node node = manager.artifactNode( session, filePath );
                final ModelType type = defaultModelType( node, modelTypes( node ) );
                return type == null ? null : type;
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#install(String)
     */
    @Override
    public void install( final String category ) throws ModelerException {
        CheckArg.isNotEmpty( category, "category" );
        LOGGER.debug( "Installing category '%s'", category );

        try {
            manager.run( this, new SystemTask< Void >() {

                @Override
                public Void run( final Session session,
                                 final Node systemNode ) throws Exception {
                    LOGGER.debug( "Installing sequencer for category '%s'", category );
                    installSequencer( category, session, systemNode );

                    LOGGER.debug( "Installing extensions for category '%s'", category );
                    installExtensions( category, session, systemNode );

                    session.save();
                    LOGGER.debug( "Session saved" );

                    return null;
                }
            } );
        } catch ( final Exception e ) {
            // try to rollback session
            manager.run( this, new SystemTask< Void >() {

                /**
                 * {@inheritDoc}
                 * 
                 * @see org.modeshape.modeler.internal.SystemTask#run(javax.jcr.Session, javax.jcr.Node)
                 */
                @Override
                public Void run( final Session session,
                                 final Node systemNode ) {
                    try {
                        session.refresh( false );
                        LOGGER.debug( "*** Session rollback success ***" );
                    } catch ( final Exception err ) {
                        LOGGER.error( err, ModelerI18n.sessionRollbackFailed, category );
                    }

                    return null;
                }
            } );

            // session successfully rolled back
            if ( e instanceof RuntimeException ) throw e;
            if ( e instanceof ModelerException ) throw ( ModelerException ) e;
            throw new ModelerException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#installableModelTypeCategories()
     */
    @Override
    public String[] installableModelTypeCategories() throws ModelerException {
        final Set< String > categories = new HashSet<>();
        for ( final URL repositoryUrl : modelTypeRepositories ) {
            try {
                if ( repositoryUrl.getProtocol().startsWith( "file" ) ) {
                    final String path = ( repositoryUrl.getFile() + File.separatorChar + MODESHAPE_GROUP );
                    final File folder = new File( path );
                    for ( final File file : folder.listFiles() ) {
                        final String name = file.getName();
                        if ( name.contains( "sequencer-" ) )
                            categories.add( name.substring( name.indexOf( "sequencer-" ) + "sequencer-".length() ) );
                    }
                } else {
                    final Document doc = Jsoup.connect( path( repositoryUrl.toString(), MODESHAPE_GROUP ) ).get();
                    final Elements elements = doc.getElementsMatchingOwnText( "sequencer-" );
                    for ( final Element element : elements ) {
                        final String href = element.attr( "href" );
                        categories.add( href.substring( href.indexOf( "sequencer-" ) + "sequencer-".length(), href.lastIndexOf( '/' ) ) );
                    }
                }
            } catch ( final IOException e ) {
                throw new ModelerException( e );
            }
        }
        return categories.toArray( new String[ categories.size() ] );
    }

    void installExtensions( final String category,
                            final Session session,
                            final Node systemNode ) throws Exception {
        final Node categoryNode = categoryNode( category, systemNode, false );

        if ( extensionInstaller.install( categoryNode,
                                         libraryClassLoader,
                                         library,
                                         modelTypeRepositories,
                                         version(),
                                         modelTypes ) ) {
            LOGGER.debug( "Installed extensions for category '%s'", category );
        } else {
            LOGGER.debug( "No extensions installed for category '%s'", category );
        }
    }

    void installSequencer( final String category,
                           final Session session,
                           final Node systemNode ) throws Exception {
        // don't install sequencer if already installed
        if ( categoryNode( category, systemNode, false ) != null ) {
            return;
        }

        final Node categoryNode = categoryNode( category, systemNode, true );
        final String archiveName = String.format( SEQUENCER_ZIP_PATTERN, category, version() );
        final Path archivePath = library.resolve( archiveName );
        final String sequencerArchivePath = String.format( SEQUENCER_PATH_PATTERN, category, version(), archiveName );
        boolean sequencerArchiveFound = false;

        // loop through repositories until we find the sequencer archive
        for ( final URL repositoryUrl : modelTypeRepositories ) {
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
                final Node archivesNode = categoryNode.getNode( ModelerLexicon.Category.ARCHIVES );

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

                    // Iterate through entries looking for appropriate extension classes
                    try ( final ZipFile jar = new ZipFile( jarPath.toFile() ) ) {
                        for ( final Enumeration< ? extends ZipEntry > jarIter = jar.entries(); jarIter.hasMoreElements(); ) {
                            final ZipEntry jarEntry = jarIter.nextElement();
                            if ( jarEntry.isDirectory() ) continue;

                            name = jarEntry.getName();

                            // see if class is a possible sequencer
                            if ( jarPath.getFileName().toString().contains( "sequencer" )
                                 && name.endsWith( "Sequencer.class" ) ) {
                                potentialSequencerClassNames.add( name.replace( '/', '.' ).substring( 0, name.length() - ".class".length() ) );
                                LOGGER.debug( "Potential sequencer: %s", name );
                            }
                        }
                    }
                }

                final Node modelTypesNode = categoryNode.getNode( ModelerLexicon.Category.MODEL_TYPES );

                // try and load each potential sequencer class that was found
                for ( final String sequencerClassName : potentialSequencerClassNames ) {
                    Class< ? > sequencerClass = null;

                    try {
                        sequencerClass = libraryClassLoader.loadClass( sequencerClassName );

                        if ( Sequencer.class.isAssignableFrom( sequencerClass )
                             && !Modifier.isAbstract( sequencerClass.getModifiers() ) ) {
                            String id = ModeShapeModeler.class.getPackage().getName() + '.' + category + '.'
                                        + sequencerClass.getSimpleName();
                            id = id.endsWith( "Sequencer" ) ? id.substring( 0, id.length() - "Sequencer".length() ) : id;

                            // add model type to MS repository
                            final Node modelTypeNode = modelTypesNode.addNode( id, ModelerLexicon.ModelType.NODE_TYPE );
                            modelTypeNode.setProperty( ModelerLexicon.ModelType.SEQUENCER_CLASS_NAME, sequencerClass.getName() );

                            // add to cache
                            @SuppressWarnings( "unchecked" ) final ModelTypeImpl type =
                                new ModelTypeImpl( manager, category, id, ( Class< Sequencer > ) sequencerClass );
                            modelTypes.add( type );
                        }
                    } catch ( final NoClassDefFoundError | ClassNotFoundException ignored ) {
                        LOGGER.debug( "Potential sequencer class '%s' cannot be loaded", sequencerClass );
                    }
                }
            }

            archivePath.toFile().delete();
        }

        if ( !sequencerArchiveFound ) {
            throw new IllegalArgumentException( ModelerI18n.unableToFindModelTypeCategory.text( category ) );
        }
    }

    void loadCategories( final Session session,
                         final Node systemNode ) throws Exception {
        if ( !systemNode.hasNode( ModelerLexicon.MODEL_TYPE_CATEGORIES ) ) {
            systemNode.addNode( ModelerLexicon.MODEL_TYPE_CATEGORIES );
            LOGGER.debug( "'%s' node created", ModelerLexicon.MODEL_TYPE_CATEGORIES );
        } else {
            final Node categoriesNode = systemNode.getNode( ModelerLexicon.MODEL_TYPE_CATEGORIES );

            for ( final NodeIterator iter = categoriesNode.getNodes(); iter.hasNext(); ) {
                final Node categoryNode = iter.nextNode();
                loadCategoryArchives( session, categoryNode );
                loadModelTypes( session, categoryNode );
            }
        }
    }

    void loadCategoryArchives( final Session session,
                               final Node categoryNode ) throws Exception {
        if ( !categoryNode.hasNode( ModelerLexicon.Category.ARCHIVES ) ) {
            categoryNode.addNode( ModelerLexicon.Category.ARCHIVES );
            LOGGER.debug( "'%s' node created", ModelerLexicon.Category.ARCHIVES );
        } else {
            final Node archivesNode = categoryNode.getNode( ModelerLexicon.Category.ARCHIVES );

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

    void loadModelTypeRepositories( final Session session,
                                    final Node systemNode ) throws Exception {
        if ( !systemNode.hasProperty( ModelerLexicon.MODEL_TYPE_REPOSITORIES ) ) {
            final Value[] vals = new Value[ 2 ];
            vals[ 0 ] = session.getValueFactory().createValue( JBOSS_MODEL_TYPE_REPOSITORY );
            vals[ 1 ] = session.getValueFactory().createValue( MAVEN_MODEL_TYPE_REPOSITORY );
            systemNode.setProperty( ModelerLexicon.MODEL_TYPE_REPOSITORIES, vals );
        }

        for ( final String url : JcrUtil.values( systemNode, ModelerLexicon.MODEL_TYPE_REPOSITORIES ) ) {
            modelTypeRepositories.add( new URL( url ) );
        }
    }

    void loadModelTypes( final Session session,
                         final Node categoryNode ) throws Exception {
        if ( !categoryNode.hasNode( ModelerLexicon.Category.MODEL_TYPES ) ) {
            categoryNode.addNode( ModelerLexicon.Category.MODEL_TYPES );
            LOGGER.debug( "'%s' node created", ModelerLexicon.Category.MODEL_TYPES );
        } else {
            final Node modelTypesNode = categoryNode.getNode( ModelerLexicon.Category.MODEL_TYPES );
            final String category = categoryNode.getName();

            for ( final NodeIterator iter = modelTypesNode.getNodes(); iter.hasNext(); ) {
                final Node modelTypeNode = iter.nextNode();
                String sequencerClassName = null;
                String desequencerClassName = null;
                String dependencyProcessorClassName = null;

                if ( modelTypeNode.hasProperty( ModelerLexicon.ModelType.SEQUENCER_CLASS_NAME ) ) {
                    sequencerClassName = JcrUtil.value( modelTypeNode,
                                                        ModelerLexicon.ModelType.SEQUENCER_CLASS_NAME );
                }

                if ( modelTypeNode.hasProperty( ModelerLexicon.ModelType.DESEQUENCER_CLASS_NAME ) ) {
                    desequencerClassName = JcrUtil.value( modelTypeNode,
                                                          ModelerLexicon.ModelType.DESEQUENCER_CLASS_NAME );
                }

                if ( modelTypeNode.hasProperty( ModelerLexicon.ModelType.DEPENDENCY_PROCESSOR_CLASS_NAME ) ) {
                    dependencyProcessorClassName = JcrUtil.value( modelTypeNode,
                                                                  ModelerLexicon.ModelType.DEPENDENCY_PROCESSOR_CLASS_NAME );
                }

                final ModelTypeImpl modelType = new ModelTypeImpl( manager,
                                                                   category,
                                                                   modelTypeNode.getName(),
                                                                   sequencerClassName,
                                                                   desequencerClassName,
                                                                   dependencyProcessorClassName );

                if ( modelTypeNode.hasProperty( ModelerLexicon.ModelType.FILE_EXTENSIONS ) ) {
                    final String[] fileExtensions = JcrUtil.values( modelTypeNode, ModelerLexicon.ModelType.FILE_EXTENSIONS );
                    modelType.setSourceFileExtensions( fileExtensions );
                }

                modelTypes.add( modelType );
                LOGGER.debug( "Loaded modelType: %s", modelType.id() );
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#modelType(String)
     */
    @Override
    public ModelType modelType( final String id ) {
        CheckArg.isNotEmpty( id, "id" );
        for ( final ModelType type : modelTypes )
            if ( id.equals( type.id() ) ) return type;
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#modelTypeCategories()
     */
    @Override
    public String[] modelTypeCategories() {
        final Set< String > categories = new HashSet<>();
        for ( final ModelType type : modelTypes )
            categories.add( type.category() );
        return categories.toArray( new String[ categories.size() ] );
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#modelTypeRepositories()
     */
    @Override
    public URL[] modelTypeRepositories() {
        return modelTypeRepositories.toArray( new URL[ modelTypeRepositories.size() ] );
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#modelTypes()
     */
    @Override
    public ModelType[] modelTypes() {
        return modelTypes.toArray( new ModelType[ modelTypes.size() ] );
    }

    /**
     * @param fileNode
     *        the file node
     * @return the model types applicable to the supplied file node
     * @throws Exception
     *         if any problem occurs
     */
    public ModelType[] modelTypes( final Node fileNode ) throws Exception {
        final Set< ModelType > applicableModelTypes = new HashSet<>();
        final String mimeType = JcrUtil.value( fileNode.getNode( JcrLexicon.CONTENT.getString() ),
                                               JcrLexicon.MIMETYPE.getString() );

        for ( final ModelType type : modelTypes() ) {
            if ( ( ( ModelTypeImpl ) type ).sequencer().isAccepted( mimeType ) ) applicableModelTypes.add( type );
        }

        return applicableModelTypes.toArray( new ModelType[ applicableModelTypes.size() ] );
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#modelTypesForArtifact(String)
     */
    @Override
    public ModelType[] modelTypesForArtifact( final String filePath ) throws ModelerException {
        CheckArg.isNotEmpty( filePath, "filePath" );
        return manager.run( new Task< ModelType[] >() {

            @Override
            public final ModelType[] run( final Session session ) throws Exception {
                return modelTypes( manager.artifactNode( session, filePath ) );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#modelTypesForCategory(String)
     */
    @Override
    public ModelType[] modelTypesForCategory( final String category ) {
        CheckArg.isNotEmpty( category, "category" );
        final Set< ModelType > types = new HashSet<>();
        for ( final ModelType type : modelTypes )
            if ( category.equals( type.category() ) ) types.add( type );
        return types.toArray( new ModelType[ types.size() ] );
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#moveModelTypeRepositoryDown(URL)
     */
    @Override
    public URL[] moveModelTypeRepositoryDown( final URL repositoryUrl ) throws ModelerException {
        CheckArg.isNotNull( repositoryUrl, "repositoryUrl" );
        final int ndx = modelTypeRepositories.indexOf( repositoryUrl );
        if ( ndx < 0 ) throw new IllegalArgumentException( ModelerI18n.urlNotFound.text( repositoryUrl ) );
        modelTypeRepositories.remove( ndx );
        modelTypeRepositories.add( Math.min( ndx + 1, modelTypeRepositories.size() ), repositoryUrl );
        saveModelTypeRepositories();
        return modelTypeRepositories();
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#moveModelTypeRepositoryUp(URL)
     */
    @Override
    public URL[] moveModelTypeRepositoryUp( final URL repositoryUrl ) throws ModelerException {
        CheckArg.isNotNull( repositoryUrl, "repositoryUrl" );
        final int ndx = modelTypeRepositories.indexOf( repositoryUrl );
        if ( ndx < 0 ) throw new IllegalArgumentException( ModelerI18n.urlNotFound.text( repositoryUrl ) );
        modelTypeRepositories.remove( ndx );
        modelTypeRepositories.add( Math.max( ndx - 1, 0 ), repositoryUrl );
        saveModelTypeRepositories();
        return modelTypeRepositories();
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
     * @see ModelTypeManager#registerModelTypeRepository(URL)
     */
    @Override
    public URL[] registerModelTypeRepository( final URL repositoryUrl ) throws ModelerException {
        CheckArg.isNotNull( repositoryUrl, "repositoryUrl" );
        if ( !modelTypeRepositories.contains( repositoryUrl ) ) {
            modelTypeRepositories.addFirst( repositoryUrl );
            saveModelTypeRepositories();
        }
        return modelTypeRepositories();
    }

    private void saveModelTypeRepositories() throws ModelerException {
        manager.run( this, new SystemTask< Void >() {

            @Override
            public Void run( final Session session,
                             final Node systemNode ) throws Exception {
                final Value[] vals = new Value[ modelTypeRepositories.size() ];
                int ndx = 0;
                for ( final URL url : modelTypeRepositories )
                    vals[ ndx++ ] = session.getValueFactory().createValue( url.toString() );
                systemNode.setProperty( ModelerLexicon.MODEL_TYPE_REPOSITORIES, vals );
                session.save();
                return null;
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#uninstall(String)
     */
    @Override
    public void uninstall( final String category ) throws ModelerException {
        CheckArg.isNotEmpty( category, "category" );

        // delete from cache all model types of that category
        boolean deleted = false;

        for ( final Iterator< ModelType > iter = modelTypes.iterator(); iter.hasNext(); ) {
            final ModelType modelType = iter.next();

            if ( category.equals( modelType.category() ) ) {
                deleted = true;
                iter.remove();
                LOGGER.debug( "Uninstalled modelType '%s'", modelType.id() );
            }
        }

        if ( !deleted ) {
            throw new ModelerException( ModelerI18n.unableToFindModelTypeCategory, category );
        }

        // delete from MS repository
        manager.run( this, new SystemTask< Void >() {

            @Override
            public Void run( final Session session,
                             final Node systemNode ) throws Exception {
                final Node categoryNode = categoryNode( category, systemNode, false );
                if ( categoryNode == null ) throw new ModelerException( ModelerI18n.unableToFindModelTypeCategory, category );

                // remove category archive paths from classpath
                if ( categoryNode.hasNode( ModelerLexicon.Category.ARCHIVES ) ) {
                    final Node archivesNode = categoryNode.getNode( ModelerLexicon.Category.ARCHIVES );

                    for ( final NodeIterator iter = archivesNode.getNodes(); iter.hasNext(); ) {
                        final Node archiveNode = iter.nextNode();
                        final Path archivePath = library.resolve( archiveNode.getName() );
                        if ( !archivePath.toFile().delete() ) LOGGER.debug( "Unable to delete jar: %s", archivePath );
                    }
                }

                // remove from MS repo now
                categoryNode.remove();
                session.save();

                return null;
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTypeManager#unregisterModelTypeRepository(URL)
     */
    @Override
    public URL[] unregisterModelTypeRepository( final URL repositoryUrl ) throws ModelerException {
        CheckArg.isNotNull( repositoryUrl, "repositoryUrl" );
        if ( modelTypeRepositories.remove( repositoryUrl ) ) saveModelTypeRepositories();
        return modelTypeRepositories();
    }

    private String version() throws ModelerException {
        return manager.repository().getDescriptor( Repository.REP_VERSION_DESC );
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
