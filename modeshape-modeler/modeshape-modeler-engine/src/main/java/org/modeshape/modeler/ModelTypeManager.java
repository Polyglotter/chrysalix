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

import java.net.URL;

/**
 * 
 */
public interface ModelTypeManager {

    /**
     * 
     */
    String JBOSS_MODEL_TYPE_REPOSITORY = "https://repository.jboss.org/nexus/content/groups/public-jboss";

    /**
     * 
     */
    String MAVEN_MODEL_TYPE_REPOSITORY = "http://repo1.maven.org/maven2";

    /**
     * @param artifactPath
     *        the repository path to an artifact
     * @return the default model type for the artifact at the supplied path
     * @throws ModelerException
     *         if any problem occurs
     */
    ModelType defaultModelType( final String artifactPath ) throws ModelerException;

    /**
     * @param category
     *        the name of an {@link #installableModelTypeCategories() installable model type category} from an on-line <a
     *        href="http://maven.apache.org">Maven</a> {@link #modelTypeRepositories() model type repository}
     * @return the collection of names of potential sequencer classes that could not be instantiated, usually due to missing
     *         dependencies.
     * @throws ModelerException
     *         if any problem occurs
     */
    String[] install( final String category ) throws ModelerException;

    /**
     * @return the installable {@link ModelType model type} categories from the {@link #modelTypeRepositories() registered
     *         repositories}
     * @throws ModelerException
     *         if any problem occurs
     */
    String[] installableModelTypeCategories() throws ModelerException;

    /**
     * @param id
     *        a ID of a model type
     * @return the model type with the supplied ID, or <code>null</code> if none exists.
     */
    ModelType modelType( String id );

    /**
     * @return the installed model type categories; never <code>null</code>.
     */
    String[] modelTypeCategories();

    /**
     * @return the {@link #registerModelTypeRepository(URL) registered} <a href="http://maven.apache.org">Maven</a> model type
     *         repository URLs, ordered by how they will searched when {@link #installableModelTypeCategories() retrieving} or
     *         {@link #install(String) installing} model type categories
     */
    URL[] modelTypeRepositories();

    /**
     * @return the available model types
     */
    ModelType[] modelTypes();

    /**
     * @param artifactPath
     *        the repository path to an artifact
     * @return the model types applicable to the artifact at the supplied path
     * @throws ModelerException
     *         if any problem occurs
     */
    ModelType[] modelTypesForArtifact( final String artifactPath ) throws ModelerException;

    /**
     * @param category
     *        an {@link #modelTypeCategories() installed model type category}
     * @return the available model types for the supplied category
     */
    ModelType[] modelTypesForCategory( String category );

    /**
     * @param repositoryUrl
     *        a URL to an on-line <a href="http://maven.apache.org">Maven</a> {@link #modelTypeRepositories() model type repository}
     * @return the {@link #registerModelTypeRepository(URL) registered} <a href="http://maven.apache.org">Maven</a>
     *         {@link #modelTypeRepositories() model type repository} URLs, ordered by how they will searched when
     *         {@link #installableModelTypeCategories() retrieving} or {@link #install(String) installing} model type categories
     * @throws ModelerException
     *         if any error occurs
     */
    URL[] moveModelTypeRepositoryDown( final URL repositoryUrl ) throws ModelerException;

    /**
     * @param repositoryUrl
     *        a URL to an on-line <a href="http://maven.apache.org">Maven</a> {@link #modelTypeRepositories() model type repository}
     * @return the {@link #registerModelTypeRepository(URL) registered} <a href="http://maven.apache.org">Maven</a>
     *         {@link #modelTypeRepositories() model type repository} URLs, ordered by how they will searched when
     *         {@link #installableModelTypeCategories() retrieving} or {@link #install(String) installing} model type categories
     * @throws ModelerException
     *         if any error occurs
     */
    URL[] moveModelTypeRepositoryUp( final URL repositoryUrl ) throws ModelerException;

    /**
     * @param repositoryUrl
     *        a URL to an on-line <a href="http://maven.apache.org">Maven</a> {@link #modelTypeRepositories() model type repository}
     * @return the registered <a href="http://maven.apache.org">Maven</a> {@link #modelTypeRepositories() model type repository}
     *         URLs, ordered by how they will searched when {@link #installableModelTypeCategories() retrieving} or
     *         {@link #install(String) installing} model type categories
     * @throws ModelerException
     *         if any error occurs
     */
    URL[] registerModelTypeRepository( final URL repositoryUrl ) throws ModelerException;

    /**
     * @param category
     *        the name of an {@link #installableModelTypeCategories() installed model type category}
     * @throws ModelerException
     *         if any problem occurs
     */
    void uninstall( final String category ) throws ModelerException;

    /**
     * @param repositoryUrl
     *        a URL to an on-line <a href="http://maven.apache.org">Maven</a> {@link #modelTypeRepositories() model type repository}
     * @return the {@link #registerModelTypeRepository(URL) registered} <a href="http://maven.apache.org">Maven</a>
     *         {@link #modelTypeRepositories() model type repository} URLs, ordered by how they will searched when
     *         {@link #installableModelTypeCategories() retrieving} or {@link #install(String) installing} model type categories
     * @throws ModelerException
     *         if any error occurs
     */
    URL[] unregisterModelTypeRepository( final URL repositoryUrl ) throws ModelerException;
}
