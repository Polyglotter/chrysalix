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
package org.modelspace;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Test;
import org.modelspace.Metamodel;
import org.modelspace.MetamodelManager;
import org.modelspace.Modelspace;
import org.modelspace.internal.ModelspaceImpl;
import org.modelspace.test.JavaIntegrationTest;

@SuppressWarnings( "javadoc" )
public class ITMetamodelManager extends JavaIntegrationTest {

    @Test
    public void shouldGetApplicableMetamodelManager() throws Exception {
        final Metamodel[] metamodels = metamodelManager().metamodelsForArtifact( modelspace().importData( MODEL_FILE, null ) );
        assertThat( metamodels, notNullValue() );
        assertThat( metamodels.length == 0, is( false ) );
    }

    @Test
    public void shouldGetExistingRegisteredMetamodelRepositoriesIfRegisteringRegisteredUrl() throws Exception {
        final URL[] origRepos = metamodelManager().metamodelRepositories();
        final URL[] repos = metamodelManager().registerMetamodelRepository( INTEGRATION_TEST_METAMODEL_REPOSITORY_URL );
        assertThat( repos, notNullValue() );
        assertThat( repos, is( origRepos ) );
    }

    @Test
    public void shouldGetIntallableMetamodelCategories() throws Exception {
        assertThat( metamodelManager().installableMetamodelCategories().length == 0, is( false ) );
    }

    @Test
    public void shouldGetMetamodel() throws Exception {
        metamodel();
    }

    @Test
    public void shouldGetMetamodelCategories() throws Exception {
        assertThat( metamodelManager().metamodelCategories().length, is( 1 ) );
        assertThat( metamodelManager().metamodelCategories()[ 0 ], is( "java" ) );
    }

    @Test
    public void shouldIniitializeMetamodelRepositories() throws Exception {
        modelspace().close();
        try ( final Modelspace modelspace = Modelspace.Factory.instance( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_CONFIGURATION_PATH ) )
        {
            assertThat( modelspace.metamodelManager().metamodelRepositories()[ 0 ], is( new URL(
                                                                                              MetamodelManager.JBOSS_METAMODEL_REPOSITORY ) ) );
            assertThat( modelspace.metamodelManager().metamodelRepositories()[ 1 ], is( new URL(
                                                                                              MetamodelManager.MAVEN_METAMODEL_REPOSITORY ) ) );
        }
    }

    @Test
    public void shouldInstallMetamodels() throws Exception {
        final Metamodel[] metamodels = metamodelManager().metamodels();
        assertThat( metamodels, notNullValue() );
        assertThat( metamodels.length, not( 0 ) );
    }

    @Test
    public void shouldMoveMetamodelRepositoryDown() throws Exception {
        modelspace().close();
        try ( Modelspace modelspace = Modelspace.Factory.instance( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_CONFIGURATION_PATH ) ) {
            final URL url = new URL( MetamodelManager.JBOSS_METAMODEL_REPOSITORY );
            final int size = modelspace.metamodelManager().metamodelRepositories().length;
            assertThat( modelspace.metamodelManager().metamodelRepositories()[ 0 ], is( url ) );
            modelspace.metamodelManager().moveMetamodelRepositoryDown( url );
            assertThat( modelspace.metamodelManager().metamodelRepositories()[ 1 ], is( url ) );
            assertThat( modelspace.metamodelManager().metamodelRepositories().length, is( size ) );
        }
    }

    @Test
    public void shouldMoveMetamodelRepositoryUp() throws Exception {
        modelspace().close();
        try ( Modelspace modelspace = Modelspace.Factory.instance( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_CONFIGURATION_PATH ) ) {
            final URL url = new URL( MetamodelManager.MAVEN_METAMODEL_REPOSITORY );
            final int size = modelspace.metamodelManager().metamodelRepositories().length;
            assertThat( modelspace.metamodelManager().metamodelRepositories()[ 1 ], is( url ) );
            modelspace.metamodelManager().moveMetamodelRepositoryUp( url );
            assertThat( modelspace.metamodelManager().metamodelRepositories()[ 0 ], is( url ) );
            assertThat( modelspace.metamodelManager().metamodelRepositories().length, is( size ) );
        }
    }

    @Test
    public void shouldNotInstallMetamodelCategoryIfAlreadyInstalled() throws Exception {
        assertThat( metamodelManager().metamodelCategories().length, is( 1 ) );
        assertThat( metamodelManager().metamodels().length, is( 2 ) );
        metamodelManager().install( CATEGORY );
        assertThat( metamodelManager().metamodelCategories().length, is( 1 ) );
        assertThat( metamodelManager().metamodels().length, is( 2 ) );
    }

    @Test
    public void shouldNotMoveMetamodelRepositoryDownIfUrlLast() throws Exception {
        modelspace().close();
        try ( Modelspace modelspace = Modelspace.Factory.instance( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_CONFIGURATION_PATH ) ) {
            final URL[] urls = modelspace.metamodelManager().metamodelRepositories();
            final URL url = new URL( MetamodelManager.MAVEN_METAMODEL_REPOSITORY );
            modelspace.metamodelManager().moveMetamodelRepositoryDown( url );
            assertThat( modelspace.metamodelManager().metamodelRepositories(), is( urls ) );
        }
    }

    @Test
    public void shouldNotMoveMetamodelRepositoryUpIfUrlFirst() throws Exception {
        final URL[] urls = metamodelManager().metamodelRepositories();
        metamodelManager().moveMetamodelRepositoryUp( INTEGRATION_TEST_METAMODEL_REPOSITORY_URL );
        assertThat( metamodelManager().metamodelRepositories(), is( urls ) );
    }

    @Test
    public void shouldOnlyInstallMetamodelCategoryOnce() throws Exception {
        metamodelManager().install( "java" );
        assertThat( metamodelManager().metamodels().length == 0, is( false ) );
        final int size = metamodelManager().metamodels().length;
        metamodelManager().install( "java" );
        assertThat( metamodelManager().metamodels().length, is( size ) );
    }

    @Test
    public void shouldRestorePreviouslySavedState() throws Exception {
        modelspace().close();
        try ( Modelspace modelspace = new ModelspaceImpl( TEST_REPOSITORY_STORE_PARENT_PATH ) ) {
            final MetamodelManager metamodelManager = modelspace.metamodelManager();
            for ( final URL url : metamodelManager.metamodelRepositories() )
                metamodelManager.unregisterMetamodelRepository( url );
            metamodelManager.registerMetamodelRepository( INTEGRATION_TEST_METAMODEL_REPOSITORY_URL );
            metamodelManager.install( "java" );
        }
        try ( Modelspace modelspace = new ModelspaceImpl( TEST_REPOSITORY_STORE_PARENT_PATH ) ) {
            final MetamodelManager metamodelManager = modelspace.metamodelManager();
            assertThat( metamodelManager.metamodelRepositories().length, is( 1 ) );
            assertThat( metamodelManager.metamodelCategories().length, is( 1 ) );
            assertThat( metamodelManager.metamodels().length, is( 2 ) );
        }
    }

    @Test
    public void shouldUninstall() throws Exception {

        assertThat( metamodelManager().metamodels().length, is( 2 ) ); // JavaFile, ClassFile
        metamodelManager().uninstall( "java" );
        assertThat( metamodelManager().metamodels().length, is( 0 ) );
    }
}
