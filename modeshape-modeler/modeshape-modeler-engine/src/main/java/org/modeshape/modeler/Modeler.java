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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 
 */
public interface Modeler extends AutoCloseable {

    /**
     * The path to the default ModeShape configuration, which uses a file-based repository
     */
    String DEFAULT_CONFIGURATION_PATH = "jcr/modeShapeConfig.json";

    /**
     * @return the path to the configuration for the embedded repository supplied when this Modeler was instantiated.
     */
    String configurationPath();

    /**
     * @param model
     *        a workspace model
     * @param file
     *        a file to which the supplied model should be exported
     * @throws ModelerException
     *         if any problem occurs
     */
    void export( Model model,
                 File file ) throws ModelerException;

    /**
     * @param model
     *        a workspace model
     * @param stream
     *        an output stream to which the supplied model should be exported
     * @throws ModelerException
     *         if any problem occurs
     */
    void export( Model model,
                 OutputStream stream ) throws ModelerException;

    /**
     * @param model
     *        a workspace model
     * @param url
     *        a URL to which the supplied model should be exported
     * @throws ModelerException
     *         if any problem occurs
     */
    void export( Model model,
                 URL url ) throws ModelerException;

    /**
     * @param artifactPath
     *        the workspace path to an artifact; must not be empty.
     * @param modelPath
     *        the path where the model should be created
     * @return a new model using a metamodel determined by the artifact's content, and if the artifact is a file, its file
     *         extension; never <code>null</code>
     * @throws ModelerException
     *         if any problem occurs
     */
    Model generateDefaultModel( final String artifactPath,
                                final String modelPath ) throws ModelerException;

    /**
     * Creates a model with the name of the supplied file.
     * 
     * @param file
     *        the file to be imported. Must not be <code>null</code>.
     * @param modelFolder
     *        the parent path where the model should be created
     * @param metamodel
     *        the metamodel of model to be created for the supplied artifact; may be <code>null</code>.
     * @return a new model of the supplied metamodel; never <code>null</code>
     * @throws ModelerException
     *         if any problem occurs
     */
    Model generateModel( final File file,
                         final String modelFolder,
                         final Metamodel metamodel ) throws ModelerException;

    /**
     * Creates a model with the name of the supplied file.
     * 
     * @param file
     *        the file to be imported. Must not be <code>null</code>.
     * @param modelFolder
     *        the parent path where the model should be created
     * @param modelName
     *        the name of the model. If <code>null</code> or empty, the name of the supplied file will be used.
     * @param metamodel
     *        the metamodel of model to be created for the supplied artifact; may be <code>null</code>.
     * @return a new model of the supplied metamodel; never <code>null</code>
     * @throws ModelerException
     *         if any problem occurs
     */
    Model generateModel( final File file,
                         final String modelFolder,
                         final String modelName,
                         final Metamodel metamodel ) throws ModelerException;

    /**
     * @param stream
     *        the artifact's content to be imported. Must not be <code>null</code>.
     * @param modelPath
     *        the path where the model should be created
     * @param metamodel
     *        the metamodel of model to be created for the supplied artifact; may be <code>null</code>.
     * @return a new model of the supplied metamodel; never <code>null</code>
     * @throws ModelerException
     *         if any problem occurs
     */
    Model generateModel( final InputStream stream,
                         final String modelPath,
                         final Metamodel metamodel ) throws ModelerException;

    /**
     * @param artifactPath
     *        the workspace path to an artifact; must not be empty.
     * @param modelPath
     *        the path where the model should be created
     * @param metamodel
     *        the metamodel of model to be created for the supplied artifact; may be <code>null</code>.
     * @param persistArtifacts
     *        <code>true</code> if auto-imported dependency artifacts should be persisted
     * @return a new model of the supplied metamodel; never <code>null</code>
     * @throws ModelerException
     *         if any problem occurs
     */
    Model generateModel( final String artifactPath,
                         final String modelPath,
                         final Metamodel metamodel,
                         final boolean persistArtifacts ) throws ModelerException;

    /**
     * @param artifactUrl
     *        the URL of an artifact; must not be <code>null</code>.
     * @param modelFolder
     *        the parent path where the model should be created
     * @param metamodel
     *        the metamodel of model to be created for the supplied artifact; may be <code>null</code>.
     * @return a new model of the supplied metamodel; never <code>null</code>
     * @throws ModelerException
     *         if any problem occurs
     */
    Model generateModel( final URL artifactUrl,
                         final String modelFolder,
                         final Metamodel metamodel ) throws ModelerException;

    /**
     * @param artifactUrl
     *        the URL of an artifact; must not be <code>null</code>.
     * @param modelFolder
     *        the parent path where the model should be created
     * @param modelName
     *        the name of the model. If <code>null</code> or empty, the name of the supplied file will be used.
     * @param metamodel
     *        the metamodel of model to be created for the supplied artifact; may be <code>null</code>.
     * @return a new model of the supplied metamodel; never <code>null</code>
     * @throws ModelerException
     *         if any problem occurs
     */
    Model generateModel( final URL artifactUrl,
                         final String modelFolder,
                         final String modelName,
                         final Metamodel metamodel ) throws ModelerException;

    /**
     * @param stream
     *        the artifact's content to be imported. Must not be <code>null</code>.
     * @param workspacePath
     *        the path where the artifact should be imported
     * @return the workspace path the to imported artifact
     * @throws ModelerException
     *         if any problem occurs
     */
    String importArtifact( final InputStream stream,
                           final String workspacePath ) throws ModelerException;

    /**
     * @param url
     *        the name of the artifact as it should be stored in the workspace. Must not be empty.
     * @param workspaceFolder
     *        the parent path where the artifact should be imported
     * @return the workspace path the to imported artifact
     * @throws ModelerException
     *         if any problem occurs
     */
    String importArtifact( final URL url,
                           final String workspaceFolder ) throws ModelerException;

    /**
     * @param url
     *        the name of the artifact as it should be stored in the workspace. Must not be empty.
     * @param workspaceFolder
     *        the parent path where the artifact should be imported
     * @param workspaceName
     *        the name of the artifact in the workspace. If <code>null</code> or empty, the last segment of the supplied URL will be
     *        used.
     * @return the workspace path the to imported artifact
     * @throws ModelerException
     *         if any problem occurs
     */
    String importArtifact( final URL url,
                           final String workspaceFolder,
                           final String workspaceName ) throws ModelerException;

    /**
     * @param file
     *        the file to be imported. Must not be <code>null</code>.
     * @param workspaceFolder
     *        the parent path where the file should be imported
     * @return the workspace path the to imported artifact
     * @throws ModelerException
     *         if any problem occurs
     */
    String importFile( final File file,
                       final String workspaceFolder ) throws ModelerException;

    /**
     * @param file
     *        the file to be imported. Must not be <code>null</code>.
     * @param workspaceFolder
     *        the parent path where the file should be imported
     * @param workspaceName
     *        the name of the file in the workspace. If <code>null</code> or empty, the name of the supplied file will be used.
     * @return the workspace path the to imported artifact
     * @throws ModelerException
     *         if any problem occurs
     */
    String importFile( final File file,
                       final String workspaceFolder,
                       final String workspaceName ) throws ModelerException;

    /**
     * @return the metamodel manager
     * @throws ModelerException
     *         if any error occurs
     */
    MetamodelManager metamodelManager() throws ModelerException;

    /**
     * @param path
     *        a workspace path for a model
     * @return the model at the supplied path, or <code>null</code> if not found
     * @throws ModelerException
     *         if any error occurs
     */
    Model model( final String path ) throws ModelerException;

    /**
     * @return the path to the folder that should contain the repository store
     */
    String repositoryStoreParentPath();
}
