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
package org.modelspace;

import java.net.URL;

/**
 * 
 */
public interface MetamodelManager {

    /**
     * 
     */
    String JBOSS_METAMODEL_REPOSITORY = "https://repository.jboss.org/nexus/content/groups/public-jboss";

    /**
     * 
     */
    String MAVEN_METAMODEL_REPOSITORY = "http://repo1.maven.org/maven2";

    /**
     * @param dataPath
     *        the repository path to an data
     * @return the default metamodel for the data at the supplied path
     * @throws ModelspaceException
     *         if any problem occurs
     */
    Metamodel defaultMetamodel( final String dataPath ) throws ModelspaceException;

    /**
     * @param category
     *        the name of an {@link #installableMetamodelCategories() installable metamodel category} from an on-line <a
     *        href="http://maven.apache.org">Maven</a> {@link #metamodelRepositories() metamodel repository}
     * @throws ModelspaceException
     *         if any problem occurs
     */
    void install( final String category ) throws ModelspaceException;

    /**
     * @return the installable {@link Metamodel metamodel} categories from the {@link #metamodelRepositories() registered
     *         repositories}
     * @throws ModelspaceException
     *         if any problem occurs
     */
    String[] installableMetamodelCategories() throws ModelspaceException;

    /**
     * @param id
     *        a ID of a metamodel
     * @return the metamodel with the supplied ID, or <code>null</code> if none exists.
     */
    Metamodel metamodel( String id );

    /**
     * @return the installed metamodel categories; never <code>null</code>.
     */
    String[] metamodelCategories();

    /**
     * @return the {@link #registerMetamodelRepository(URL) registered} <a href="http://maven.apache.org">Maven</a> metamodel
     *         repository URLs, ordered by how they will searched when {@link #installableMetamodelCategories() retrieving} or
     *         {@link #install(String) installing} metamodel categories
     */
    URL[] metamodelRepositories();

    /**
     * @return the available metamodels
     */
    Metamodel[] metamodels();

    /**
     * @param dataPath
     *        the workspace path to data
     * @return the metamodels applicable to the data at the supplied path
     * @throws ModelspaceException
     *         if any problem occurs
     */
    Metamodel[] metamodelsForArtifact( final String dataPath ) throws ModelspaceException;

    /**
     * @param category
     *        an {@link #metamodelCategories() installed metamodel category}
     * @return the available metamodels for the supplied category
     */
    Metamodel[] metamodelsForCategory( String category );

    /**
     * @param repositoryUrl
     *        a URL to an on-line <a href="http://maven.apache.org">Maven</a> {@link #metamodelRepositories() metamodel repository}
     * @return the {@link #registerMetamodelRepository(URL) registered} <a href="http://maven.apache.org">Maven</a>
     *         {@link #metamodelRepositories() metamodel repository} URLs, ordered by how they will searched when
     *         {@link #installableMetamodelCategories() retrieving} or {@link #install(String) installing} metamodel categories
     * @throws ModelspaceException
     *         if any error occurs
     */
    URL[] moveMetamodelRepositoryDown( final URL repositoryUrl ) throws ModelspaceException;

    /**
     * @param repositoryUrl
     *        a URL to an on-line <a href="http://maven.apache.org">Maven</a> {@link #metamodelRepositories() metamodel repository}
     * @return the {@link #registerMetamodelRepository(URL) registered} <a href="http://maven.apache.org">Maven</a>
     *         {@link #metamodelRepositories() metamodel repository} URLs, ordered by how they will searched when
     *         {@link #installableMetamodelCategories() retrieving} or {@link #install(String) installing} metamodel categories
     * @throws ModelspaceException
     *         if any error occurs
     */
    URL[] moveMetamodelRepositoryUp( final URL repositoryUrl ) throws ModelspaceException;

    /**
     * @param repositoryUrl
     *        a URL to an on-line <a href="http://maven.apache.org">Maven</a> {@link #metamodelRepositories() metamodel repository}
     * @return the registered <a href="http://maven.apache.org">Maven</a> {@link #metamodelRepositories() metamodel repository}
     *         URLs, ordered by how they will searched when {@link #installableMetamodelCategories() retrieving} or
     *         {@link #install(String) installing} metamodel categories
     * @throws ModelspaceException
     *         if any error occurs
     */
    URL[] registerMetamodelRepository( final URL repositoryUrl ) throws ModelspaceException;

    /**
     * @param category
     *        the name of an {@link #installableMetamodelCategories() installed metamodel category}
     * @throws ModelspaceException
     *         if any problem occurs
     */
    void uninstall( final String category ) throws ModelspaceException;

    /**
     * @param repositoryUrl
     *        a URL to an on-line <a href="http://maven.apache.org">Maven</a> {@link #metamodelRepositories() metamodel repository}
     * @return the {@link #registerMetamodelRepository(URL) registered} <a href="http://maven.apache.org">Maven</a>
     *         {@link #metamodelRepositories() metamodel repository} URLs, ordered by how they will searched when
     *         {@link #installableMetamodelCategories() retrieving} or {@link #install(String) installing} metamodel categories
     * @throws ModelspaceException
     *         if any error occurs
     */
    URL[] unregisterMetamodelRepository( final URL repositoryUrl ) throws ModelspaceException;
}
