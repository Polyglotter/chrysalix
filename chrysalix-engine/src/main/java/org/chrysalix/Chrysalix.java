/*
 * Chrysalix
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * Chrysalix is free software. Unless otherwise indicated, all code in Chrysalix
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * Chrysalix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.chrysalix;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.chrysalix.common.Logger;
import org.modelspace.Metamodel;
import org.modelspace.MetamodelManager;
import org.modelspace.Model;
import org.modelspace.Modelspace;
import org.modelspace.ModelspaceException;

/**
 * 
 */
public final class Chrysalix implements Modelspace {

    /**
     * 
     */
    public static final Logger LOGGER = Logger.logger( ChrysalixI18n.class );

    private final Modelspace modelspace;

    /**
     * The Chrysalix namespace. Value is {@value} .
     */
    public static final String NAMESPACE_URI = "chrysalix.org/1.0";

    /**
     * The Chrysalix namespace prefix. Value is {@value} .
     */
    public static final String NAMESPACE_PREFIX = "chryx";

    /**
     * Uses a default configuration.
     * 
     * @param repositoryStoreParentPath
     *        the file path to the folder that should contain the repository store
     */
    public Chrysalix( final String repositoryStoreParentPath ) {
        modelspace = Modelspace.Factory.instance( repositoryStoreParentPath );
    }

    /**
     * @param repositoryStoreParentPath
     *        the file path to the folder that should contain the repository store
     * @param configurationPath
     *        the file path to a configuration file
     */
    public Chrysalix( final String repositoryStoreParentPath,
                      final String configurationPath ) {
        modelspace = Modelspace.Factory.instance( repositoryStoreParentPath, configurationPath );
    }

    /**
     * {@inheritDoc}
     * 
     * @see AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        modelspace.close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#configurationPath()
     */
    @Override
    public String configurationPath() {
        return modelspace.configurationPath();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Modelspace#deleteModel(java.lang.String)
     */
    @Override
    public boolean deleteModel( final String path ) throws ModelspaceException {
        return modelspace.deleteModel( path );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Modelspace#export(org.modelspace.Model, java.io.File)
     */
    @Override
    public void export( final Model model,
                        final File file ) throws ModelspaceException {
        modelspace.export( model, file );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Modelspace#export(org.modelspace.Model, java.io.OutputStream)
     */
    @Override
    public void export( final Model model,
                        final OutputStream stream ) throws ModelspaceException {
        modelspace.export( model, stream );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Modelspace#export(org.modelspace.Model, java.net.URL)
     */
    @Override
    public void export( final Model model,
                        final URL url ) throws ModelspaceException {
        modelspace.export( model, url );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#generateModel(String, String)
     */
    @Override
    public Model generateModel( final String dataPath,
                                final String modelPath ) throws ModelspaceException {
        return modelspace.generateModel( dataPath, modelPath );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#generateModel(String, String, Metamodel)
     */
    @Override
    public Model generateModel( final String dataPath,
                                final String modelPath,
                                final Metamodel metamodel ) throws ModelspaceException {
        return modelspace.generateModel( dataPath, modelPath, metamodel );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#importData(File, String)
     */
    @Override
    public String importData( final File file,
                              final String workspaceFolder ) throws ModelspaceException {
        return modelspace.importData( file, workspaceFolder );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#importData(File, String, String)
     */
    @Override
    public String importData( final File file,
                              final String workspaceFolder,
                              final String workspaceName ) throws ModelspaceException {
        return modelspace.importData( file, workspaceFolder, workspaceName );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#importData(InputStream, String)
     */
    @Override
    public String importData( final InputStream stream,
                              final String workspacePath ) throws ModelspaceException {
        return modelspace.importData( stream, workspacePath );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#importData(URL, String)
     */
    @Override
    public String importData( final URL url,
                              final String workspaceFolder ) throws ModelspaceException {
        return modelspace.importData( url, workspaceFolder );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#importData(URL, String, String)
     */
    @Override
    public String importData( final URL url,
                              final String workspaceFolder,
                              final String workspaceName ) throws ModelspaceException {
        return modelspace.importData( url, workspaceFolder, workspaceName );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#importModel(File, String, Metamodel)
     */
    @Override
    public Model importModel( final File file,
                              final String modelFolder,
                              final Metamodel metamodel ) throws ModelspaceException {
        return modelspace.importModel( file, modelFolder, metamodel );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#importModel(File, String, String, Metamodel)
     */
    @Override
    public Model importModel( final File file,
                              final String modelFolder,
                              final String modelName,
                              final Metamodel metamodel ) throws ModelspaceException {
        return modelspace.importModel( file, modelFolder, modelName, metamodel );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#importModel(InputStream, String, Metamodel)
     */
    @Override
    public Model importModel( final InputStream stream,
                              final String modelPath,
                              final Metamodel metamodel ) throws ModelspaceException {
        return modelspace.importModel( stream, modelPath, metamodel );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#importModel(URL, String, Metamodel)
     */
    @Override
    public Model importModel( final URL dataUrl,
                              final String modelFolder,
                              final Metamodel metamodel ) throws ModelspaceException {
        return modelspace.importModel( dataUrl, modelFolder, metamodel );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#importModel(URL, String, String, Metamodel)
     */
    @Override
    public Model importModel( final URL dataUrl,
                              final String modelFolder,
                              final String modelName,
                              final Metamodel metamodel ) throws ModelspaceException {
        return modelspace.importModel( dataUrl, modelFolder, modelName, metamodel );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#metamodelManager()
     */
    @Override
    public MetamodelManager metamodelManager() throws ModelspaceException {
        return modelspace.metamodelManager();
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#model(String)
     */
    @Override
    public Model model( final String path ) throws ModelspaceException {
        return modelspace.model( path );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Modelspace#newModel(java.lang.String, java.lang.String)
     */
    @Override
    public Model newModel( final String modelPath,
                           final String metamodelId ) throws ModelspaceException {
        return modelspace.newModel( modelPath, metamodelId );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Modelspace#newModel(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public Model newModel( final String modelPath,
                           final String metamodelId,
                           final boolean override ) throws ModelspaceException {
        return modelspace.newModel( modelPath, metamodelId, override );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modelspace#repositoryStoreParentPath()
     */
    @Override
    public String repositoryStoreParentPath() {
        return modelspace.repositoryStoreParentPath();
    }
}
