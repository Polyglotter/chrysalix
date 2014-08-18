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
package org.polyglotter;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.modeshape.modeler.Metamodel;
import org.modeshape.modeler.MetamodelManager;
import org.modeshape.modeler.Model;
import org.modeshape.modeler.Modeler;
import org.modeshape.modeler.ModelerException;

/**
 * 
 */
public final class Polyglotter implements Modeler {

    private final Modeler modeler;
    /**
     * The Polyglotter namespace. Value is {@value} .
     */
    public static final String NAMESPACE_URI = "www.redhat.com/polyglotter/1.0";
    /**
     * The Polyglotter namespace prefix. Value is {@value} .
     */
    public static final String NAMESPACE_PREFIX = "poly";

    /**
     * Uses a default configuration.
     * 
     * @param repositoryStoreParentPath
     *        the file path to the folder that should contain the repository store
     */
    public Polyglotter( final String repositoryStoreParentPath ) {
        modeler = Modeler.Factory.instance( repositoryStoreParentPath );
    }

    /**
     * @param repositoryStoreParentPath
     *        the file path to the folder that should contain the repository store
     * @param configurationPath
     *        the file path to a configuration file
     */
    public Polyglotter( final String repositoryStoreParentPath,
                        final String configurationPath ) {
        modeler = Modeler.Factory.instance( repositoryStoreParentPath, configurationPath );
    }

    /**
     * {@inheritDoc}
     * 
     * @see AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        modeler.close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#configurationPath()
     */
    @Override
    public String configurationPath() {
        return modeler.configurationPath();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.Modeler#export(org.modeshape.modeler.Model, java.io.File)
     */
    @Override
    public void export( final Model model,
                        final File file ) throws ModelerException {
        modeler.export( model, file );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.Modeler#export(org.modeshape.modeler.Model, java.io.OutputStream)
     */
    @Override
    public void export( final Model model,
                        final OutputStream stream ) throws ModelerException {
        modeler.export( model, stream );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.Modeler#export(org.modeshape.modeler.Model, java.net.URL)
     */
    @Override
    public void export( final Model model,
                        final URL url ) throws ModelerException {
        modeler.export( model, url );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#generateModel(String, String)
     */
    @Override
    public Model generateModel( final String dataPath,
                                final String modelPath ) throws ModelerException {
        return modeler.generateModel( dataPath, modelPath );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#generateModel(String, String, Metamodel)
     */
    @Override
    public Model generateModel( final String dataPath,
                                final String modelPath,
                                final Metamodel metamodel ) throws ModelerException {
        return modeler.generateModel( dataPath, modelPath, metamodel );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importData(File, String)
     */
    @Override
    public String importData( final File file,
                              final String workspaceFolder ) throws ModelerException {
        return modeler.importData( file, workspaceFolder );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importData(File, String, String)
     */
    @Override
    public String importData( final File file,
                              final String workspaceFolder,
                              final String workspaceName ) throws ModelerException {
        return modeler.importData( file, workspaceFolder, workspaceName );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importData(InputStream, String)
     */
    @Override
    public String importData( final InputStream stream,
                              final String workspacePath ) throws ModelerException {
        return modeler.importData( stream, workspacePath );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importData(URL, String)
     */
    @Override
    public String importData( final URL url,
                              final String workspaceFolder ) throws ModelerException {
        return modeler.importData( url, workspaceFolder );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importData(URL, String, String)
     */
    @Override
    public String importData( final URL url,
                              final String workspaceFolder,
                              final String workspaceName ) throws ModelerException {
        return modeler.importData( url, workspaceFolder, workspaceName );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importModel(File, String, Metamodel)
     */
    @Override
    public Model importModel( final File file,
                              final String modelFolder,
                              final Metamodel metamodel ) throws ModelerException {
        return modeler.importModel( file, modelFolder, metamodel );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importModel(File, String, String, Metamodel)
     */
    @Override
    public Model importModel( final File file,
                              final String modelFolder,
                              final String modelName,
                              final Metamodel metamodel ) throws ModelerException {
        return modeler.importModel( file, modelFolder, modelName, metamodel );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importModel(InputStream, String, Metamodel)
     */
    @Override
    public Model importModel( final InputStream stream,
                              final String modelPath,
                              final Metamodel metamodel ) throws ModelerException {
        return modeler.importModel( stream, modelPath, metamodel );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importModel(URL, String, Metamodel)
     */
    @Override
    public Model importModel( final URL dataUrl,
                              final String modelFolder,
                              final Metamodel metamodel ) throws ModelerException {
        return modeler.importModel( dataUrl, modelFolder, metamodel );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#importModel(URL, String, String, Metamodel)
     */
    @Override
    public Model importModel( final URL dataUrl,
                              final String modelFolder,
                              final String modelName,
                              final Metamodel metamodel ) throws ModelerException {
        return modeler.importModel( dataUrl, modelFolder, modelName, metamodel );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#metamodelManager()
     */
    @Override
    public MetamodelManager metamodelManager() throws ModelerException {
        return modeler.metamodelManager();
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#model(String)
     */
    @Override
    public Model model( final String path ) throws ModelerException {
        return modeler.model( path );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Modeler#repositoryStoreParentPath()
     */
    @Override
    public String repositoryStoreParentPath() {
        return modeler.repositoryStoreParentPath();
    }
}
