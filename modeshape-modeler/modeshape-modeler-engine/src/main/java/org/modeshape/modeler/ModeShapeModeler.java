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
package org.modeshape.modeler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Session;

import org.modeshape.common.util.CheckArg;
import org.modeshape.jcr.JcrLexicon;
import org.modeshape.jcr.api.JcrTools;
import org.modeshape.jcr.api.ValueFactory;
import org.modeshape.jcr.api.sequencer.Sequencer;
import org.modeshape.modeler.extensions.DependencyProcessor;
import org.modeshape.modeler.extensions.Desequencer;
import org.modeshape.modeler.internal.Manager;
import org.modeshape.modeler.internal.ModelImpl;
import org.modeshape.modeler.internal.ModelTypeImpl;
import org.modeshape.modeler.internal.ModelerLexicon;
import org.modeshape.modeler.internal.Task;
import org.polyglotter.common.Logger;

/**
 * 
 */
public final class ModeShapeModeler implements Modeler {

    final Manager manager;

    /**
     * Uses a default ModeShape configuration.
     * 
     * @param repositoryStoreParentPath
     *        the path to the folder that should contain the ModeShape repository store
     */
    public ModeShapeModeler( final String repositoryStoreParentPath ) {
        this( repositoryStoreParentPath, DEFAULT_MODESHAPE_CONFIGURATION_PATH );
    }

    /**
     * @param repositoryStoreParentPath
     *        the path to the folder that should contain the ModeShape repository store
     * @param modeShapeConfigurationPath
     *        the path to a ModeShape configuration file
     */
    public ModeShapeModeler( final String repositoryStoreParentPath,
                             final String modeShapeConfigurationPath ) {
        manager = new Manager( repositoryStoreParentPath, modeShapeConfigurationPath );
    }

    String absolutePath( String path ) {
        if ( path == null ) return "/";
        path = path.trim();
        if ( path.isEmpty() ) return "/";
        if ( path.charAt( 0 ) == '/' ) return path;
        return '/' + path;
    }

    String absolutePath( String path,
                         final String name ) {
        path = absolutePath( path );
        return path.endsWith( "/" ) ? path + name : path + '/' + name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see AutoCloseable#close()
     */
    @Override
    public void close() throws ModelerException {
        manager.close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#export(Model, File)
     */
    @Override
    public void export( final Model model,
                        final File file ) throws ModelerException {
        CheckArg.isNotNull( model, "model" );
        CheckArg.isNotNull( file, "file" );
        try {
            export( model, new FileOutputStream( file ) );
        } catch ( final FileNotFoundException e ) {
            throw new ModelerException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#export(Model, OutputStream)
     */
    @Override
    public void export( final Model model,
                        final OutputStream stream ) throws ModelerException {
        CheckArg.isNotNull( model, "model" );
        CheckArg.isNotNull( stream, "stream" );

        final ModelType modelType = model.modelType();

        if ( modelType != null ) {
            final Desequencer desequencer = modelType.desequencer();

            if ( desequencer != null ) {
                desequencer.execute( model, stream );
            }
        }

        throw new ModelerException( ModelerI18n.modelExportDesequencerNotFound, model.name() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#export(Model, URL)
     */
    @Override
    public void export( final Model model,
                        final URL url ) throws ModelerException {
        CheckArg.isNotNull( model, "model" );
        CheckArg.isNotNull( url, "url" );
        try ( OutputStream stream = url.openConnection().getOutputStream() ) {
            export( model, stream );
        } catch ( final IOException e ) {
            throw new ModelerException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#generateDefaultModel(String, String)
     */
    @Override
    public Model generateDefaultModel( final String artifactPath,
                                       final String modelPath ) throws ModelerException {
        return generateModel( artifactPath, modelPath, null, true );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#generateModel(File, String, ModelType)
     */
    @Override
    public Model generateModel( final File file,
                                final String modelFolder,
                                final ModelType modelType ) throws ModelerException {
        return generateModel( file, modelFolder, null, modelType );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#generateModel(File, String, String, ModelType)
     */
    @Override
    public Model generateModel( final File file,
                                final String modelFolder,
                                final String modelName,
                                final ModelType modelType ) throws ModelerException {
        CheckArg.isNotNull( file, "file" );
        try {
            return generateModel( file.toURI().toURL(), modelFolder, modelName, modelType );
        } catch ( final MalformedURLException e ) {
            throw new ModelerException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#generateModel(InputStream, String, ModelType)
     */
    @Override
    public Model generateModel( final InputStream stream,
                                final String modelPath,
                                final ModelType modelType ) throws ModelerException {
        final String artifactPath = importArtifact( stream, ModelerLexicon.TEMP_FOLDER + "/file" );
        return generateModel( artifactPath, modelPath, modelType, false );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.Modeler#generateModel(java.lang.String, java.lang.String, org.modeshape.modeler.ModelType,
     *      boolean)
     */
    @Override
    public Model generateModel( final String artifactPath,
                                final String modelPath,
                                final ModelType modelType,
                                final boolean persistArtifact ) throws ModelerException {
        CheckArg.isNotEmpty( artifactPath, "artifactPath" );
        CheckArg.isNotEmpty( modelPath, "modelPath" );

        try {
            return manager.run( new Task< Model >() {

                @Override
                public Model run( final Session session ) throws Exception {
                    final Node artifactNode = manager.artifactNode( session, artifactPath );
                    ModelType type = modelType;
                    if ( modelType == null ) {
                        // If no model type supplied, use default model type if one exists
                        type = manager.modelTypeManager().defaultModelType( artifactNode,
                                                                            manager.modelTypeManager().modelTypes( artifactNode ) );
                        if ( type == null )
                            throw new IllegalArgumentException( ModelerI18n.unableToDetermineDefaultModelType.text( artifactPath ) );
                        throw new UnsupportedOperationException( "Not yet implemented" );
                    }
                    // Build the model
                    final ValueFactory valueFactory = ( ValueFactory ) session.getValueFactory();
                    final Calendar cal = Calendar.getInstance();
                    final ModelTypeImpl modelType = ( ModelTypeImpl ) type;
                    final Node modelNode = new JcrTools().findOrCreateNode( session, absolutePath( modelPath ) );
                    modelNode.addMixin( ModelerLexicon.Model.MODEL_MIXIN );
                    if ( artifactNode.hasProperty( ModelerLexicon.Model.EXTERNAL_LOCATION ) )
                        modelNode.setProperty( ModelerLexicon.Model.EXTERNAL_LOCATION,
                                               artifactNode.getProperty( ModelerLexicon.Model.EXTERNAL_LOCATION ).getString() );
                    final boolean save = modelType.sequencer().execute( artifactNode.getNode( JcrLexicon.CONTENT.getString() )
                                                                                    .getProperty( JcrLexicon.DATA.getString() ),
                                                                        modelNode,
                                                                        new Sequencer.Context() {

                                                                            @Override
                                                                            public Calendar getTimestamp() {
                                                                                return cal;
                                                                            }

                                                                            @Override
                                                                            public ValueFactory valueFactory() {
                                                                                return valueFactory;
                                                                            }
                                                                        } );
                    if ( save ) {
                        modelNode.setProperty( ModelerLexicon.Model.MODEL_TYPE, modelType.id() );
                        final ModelImpl model = new ModelImpl( manager, modelNode.getPath() );
                        session.save();
                        processDependencies( artifactPath, modelNode, model, persistArtifact );
                        return model;
                    }
                    throw new ModelerException( ModelerI18n.sessionNotSavedWhenCreatingModel, artifactPath );
                }
            } );
        } finally {
            if ( !persistArtifact ) {
                removeTemporaryArtifact( artifactPath );
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#generateModel(URL, String, ModelType)
     */
    @Override
    public Model generateModel( final URL artifactUrl,
                                final String modelFolder,
                                final ModelType modelType ) throws ModelerException {
        return generateModel( artifactUrl, modelFolder, null, modelType );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#generateModel(URL, String, String, ModelType)
     */
    @Override
    public Model generateModel( final URL artifactUrl,
                                final String modelFolder,
                                final String modelName,
                                final ModelType modelType ) throws ModelerException {
        final String artifactPath = importArtifact( artifactUrl, ModelerLexicon.TEMP_FOLDER );
        return generateModel( artifactPath, absolutePath( modelFolder, name( modelName, artifactUrl ) ), modelType, false );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importArtifact(InputStream, String)
     */
    @Override
    public String importArtifact( final InputStream stream,
                                  final String workspacePath ) throws ModelerException {
        CheckArg.isNotNull( stream, "stream" );
        CheckArg.isNotEmpty( workspacePath, "workspacePath" );
        return manager.run( new Task< String >() {

            @Override
            public String run( final Session session ) throws Exception {
                // Ensure the path is non-null, absolute, and ends with a slash
                final Node node = new JcrTools().uploadFile( session, absolutePath( workspacePath ), stream );
                // Add unstructured mix-in to allow node to contain anything else, like models created later
                node.addMixin( ModelerLexicon.UNSTRUCTURED_MIXIN );
                session.save();
                return node.getPath();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importArtifact(URL, String)
     */
    @Override
    public String importArtifact( final URL url,
                                  final String workspaceFolder ) throws ModelerException {
        return importArtifact( url, workspaceFolder, null );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importArtifact(URL, String, String)
     */
    @Override
    public String importArtifact( final URL url,
                                  final String workspaceFolder,
                                  final String workspaceName ) throws ModelerException {
        CheckArg.isNotNull( url, "url" );
        try {
            final String path = importArtifact( url.openStream(), absolutePath( workspaceFolder, name( workspaceName, url ) ) );
            saveExternalLocation( path, url.toString() );
            return path;
        } catch ( final FileNotFoundException e ) {
            throw new IllegalArgumentException( e );
        } catch ( final IOException e ) {
            throw new ModelerException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importFile(File, String)
     */
    @Override
    public String importFile( final File file,
                              final String workspaceFolder ) throws ModelerException {
        return importFile( file, workspaceFolder, null );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importFile(File, String, String)
     */
    @Override
    public String importFile( final File file,
                              final String workspaceFolder,
                              final String workspaceName ) throws ModelerException {
        CheckArg.isNotNull( file, "file" );
        try {
            return importArtifact( file.toURI().toURL(), workspaceFolder, workspaceName );
        } catch ( final MalformedURLException e ) {
            throw new ModelerException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#model(String)
     */
    @Override
    public Model model( final String path ) throws ModelerException {
        CheckArg.isNotEmpty( path, "path" );
        return manager.run( new Task< Model >() {

            @Override
            public Model run( final Session session ) throws Exception {
                try {
                    final String absPath = absolutePath( path );
                    final Node node = session.getNode( absPath );
                    if ( !node.isNodeType( ModelerLexicon.Model.MODEL_MIXIN ) )
                        throw new IllegalArgumentException( ModelerI18n.notModelPath.text( absPath ) );
                    return new ModelImpl( manager, absPath );
                } catch ( final PathNotFoundException e ) {
                    return null;
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#modelTypeManager()
     */
    @Override
    public ModelTypeManager modelTypeManager() throws ModelerException {
        return manager.modelTypeManager();
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#modeShapeConfigurationPath()
     */
    @Override
    public String modeShapeConfigurationPath() {
        return manager.modeShapeConfigurationPath;
    }

    private String name( String workspaceName,
                         final URL url ) {
        if ( workspaceName != null && !workspaceName.trim().isEmpty() ) return workspaceName;
        workspaceName = url.getPath();
        workspaceName = workspaceName.substring( workspaceName.lastIndexOf( '/' ) + 1 );
        return workspaceName;
    }

    void processDependencies( final String artifactPath,
                              final Node modelNode,
                              final ModelImpl model,
                              final boolean persistArtifacts ) throws Exception {
        if ( model.modelType() == null ) {
            Logger.getLogger( getClass() ).debug( "No model type found for model '%s'", modelNode.getName() );
            return;
        }

        final DependencyProcessor dependencyProcessor = model.modelType().dependencyProcessor();

        if ( dependencyProcessor == null ) {
            Logger.getLogger( getClass() ).debug( "No dependency processor found for model '%s'", modelNode.getName() );
        } else {
            dependencyProcessor.process( artifactPath, modelNode, this, persistArtifacts );
        }
    }

    private void removeTemporaryArtifact( final String artifactPath ) throws ModelerException {
        manager.run( new Task< Void >() {

            @Override
            public Void run( final Session session ) throws Exception {
                session.getNode( artifactPath ).remove();
                session.save();
                return null;
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#repositoryStoreParentPath()
     */
    @Override
    public String repositoryStoreParentPath() {
        return System.getProperty( Manager.REPOSITORY_STORE_PARENT_PATH_PROPERTY );
    }

    private void saveExternalLocation( final String path,
                                       final String location ) throws ModelerException {
        manager.run( new Task< Void >() {

            @Override
            public Void run( final Session session ) throws Exception {
                session.getNode( path ).setProperty( ModelerLexicon.Model.EXTERNAL_LOCATION, location );
                session.save();
                return null;
            }
        } );
    }
}
