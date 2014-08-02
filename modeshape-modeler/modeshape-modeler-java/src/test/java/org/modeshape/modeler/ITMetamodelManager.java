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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Test;
import org.modeshape.modeler.internal.ModelerImpl;
import org.modeshape.modeler.test.JavaIntegrationTest;

@SuppressWarnings( "javadoc" )
public class ITMetamodelManager extends JavaIntegrationTest {

    @Test
    public void shouldGetApplicableModelTypeManager() throws Exception {
        final Metamodel[] metamodels = metamodelManager().metamodelsForArtifact( modeler().importData( MODEL_FILE, null ) );
        assertThat( metamodels, notNullValue() );
        assertThat( metamodels.length == 0, is( false ) );
    }

    @Test
    public void shouldGetExistingRegisteredModelTypeRepositoriesIfRegisteringRegisteredUrl() throws Exception {
        final URL[] origRepos = metamodelManager().metamodelRepositories();
        final URL[] repos = metamodelManager().registerMetamodelRepository( INTEGRATION_TEST_METAMODEL_REPOSITORY_URL );
        assertThat( repos, notNullValue() );
        assertThat( repos, is( origRepos ) );
    }

    @Test
    public void shouldGetIntallableModelTypeCategories() throws Exception {
        assertThat( metamodelManager().installableMetamodelCategories().length == 0, is( false ) );
    }

    @Test
    public void shouldGetModelType() throws Exception {
        metamodel();
    }

    @Test
    public void shouldGetModelTypeCategories() throws Exception {
        assertThat( metamodelManager().metamodelCategories().length, is( 1 ) );
        assertThat( metamodelManager().metamodelCategories()[ 0 ], is( "java" ) );
    }

    @Test
    public void shouldIniitializeModelTypeRepositories() throws Exception {
        modeler().close();
        try ( final Modeler modeler = Modeler.Factory.instance( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_CONFIGURATION_PATH ) )
        {
            assertThat( modeler.metamodelManager().metamodelRepositories()[ 0 ], is( new URL(
                                                                                              MetamodelManager.JBOSS_METAMODEL_REPOSITORY ) ) );
            assertThat( modeler.metamodelManager().metamodelRepositories()[ 1 ], is( new URL(
                                                                                              MetamodelManager.MAVEN_METAMODEL_REPOSITORY ) ) );
        }
    }

    @Test
    public void shouldInstallModelTypes() throws Exception {
        final Metamodel[] modelTypes = metamodelManager().metamodels();
        assertThat( modelTypes, notNullValue() );
        assertThat( modelTypes.length, not( 0 ) );
    }

    @Test
    public void shouldMoveModelTypeRepositoryDown() throws Exception {
        modeler().close();
        try ( Modeler modeler = Modeler.Factory.instance( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_CONFIGURATION_PATH ) ) {
            final URL url = new URL( MetamodelManager.JBOSS_METAMODEL_REPOSITORY );
            final int size = modeler.metamodelManager().metamodelRepositories().length;
            assertThat( modeler.metamodelManager().metamodelRepositories()[ 0 ], is( url ) );
            modeler.metamodelManager().moveMetamodelRepositoryDown( url );
            assertThat( modeler.metamodelManager().metamodelRepositories()[ 1 ], is( url ) );
            assertThat( modeler.metamodelManager().metamodelRepositories().length, is( size ) );
        }
    }

    @Test
    public void shouldMoveModelTypeRepositoryUp() throws Exception {
        modeler().close();
        try ( Modeler modeler = Modeler.Factory.instance( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_CONFIGURATION_PATH ) ) {
            final URL url = new URL( MetamodelManager.MAVEN_METAMODEL_REPOSITORY );
            final int size = modeler.metamodelManager().metamodelRepositories().length;
            assertThat( modeler.metamodelManager().metamodelRepositories()[ 1 ], is( url ) );
            modeler.metamodelManager().moveMetamodelRepositoryUp( url );
            assertThat( modeler.metamodelManager().metamodelRepositories()[ 0 ], is( url ) );
            assertThat( modeler.metamodelManager().metamodelRepositories().length, is( size ) );
        }
    }

    @Test
    public void shouldNotInstallModelTypeCategoryIfAlreadyInstalled() throws Exception {
        assertThat( metamodelManager().metamodelCategories().length, is( 1 ) );
        assertThat( metamodelManager().metamodels().length, is( 2 ) );
        metamodelManager().install( CATEGORY );
        assertThat( metamodelManager().metamodelCategories().length, is( 1 ) );
        assertThat( metamodelManager().metamodels().length, is( 2 ) );
    }

    @Test
    public void shouldNotMoveModelTypeRepositoryDownIfUrlLast() throws Exception {
        modeler().close();
        try ( Modeler modeler = Modeler.Factory.instance( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_CONFIGURATION_PATH ) ) {
            final URL[] urls = modeler.metamodelManager().metamodelRepositories();
            final URL url = new URL( MetamodelManager.MAVEN_METAMODEL_REPOSITORY );
            modeler.metamodelManager().moveMetamodelRepositoryDown( url );
            assertThat( modeler.metamodelManager().metamodelRepositories(), is( urls ) );
        }
    }

    @Test
    public void shouldNotMoveModelTypeRepositoryUpIfUrlFirst() throws Exception {
        final URL[] urls = metamodelManager().metamodelRepositories();
        metamodelManager().moveMetamodelRepositoryUp( INTEGRATION_TEST_METAMODEL_REPOSITORY_URL );
        assertThat( metamodelManager().metamodelRepositories(), is( urls ) );
    }

    @Test
    public void shouldOnlyInstallModelTypeCategoryOnce() throws Exception {
        metamodelManager().install( "java" );
        assertThat( metamodelManager().metamodels().length == 0, is( false ) );
        final int size = metamodelManager().metamodels().length;
        metamodelManager().install( "java" );
        assertThat( metamodelManager().metamodels().length, is( size ) );
    }

    @Test
    public void shouldRestorePreviouslySavedState() throws Exception {
        modeler().close();
        try ( Modeler modeler = new ModelerImpl( TEST_REPOSITORY_STORE_PARENT_PATH ) ) {
            final MetamodelManager metamodelManager = modeler.metamodelManager();
            for ( final URL url : metamodelManager.metamodelRepositories() )
                metamodelManager.unregisterMetamodelRepository( url );
            metamodelManager.registerMetamodelRepository( INTEGRATION_TEST_METAMODEL_REPOSITORY_URL );
            metamodelManager.install( "java" );
        }
        try ( Modeler modeler = new ModelerImpl( TEST_REPOSITORY_STORE_PARENT_PATH ) ) {
            final MetamodelManager metamodelManager = modeler.metamodelManager();
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
