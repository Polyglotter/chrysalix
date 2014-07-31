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
import org.modeshape.modeler.internal.ModelTypeManagerImpl;
import org.modeshape.modeler.test.JavaIntegrationTest;

@SuppressWarnings( "javadoc" )
public class ITModelTypeManager extends JavaIntegrationTest {

    @Test
    public void shouldGetApplicableModelTypeManager() throws Exception {
        final ModelType[] types = modelTypeManager().modelTypesForArtifact( modeler().importFile( MODEL_FILE, null ) );
        assertThat( types, notNullValue() );
        assertThat( types.length == 0, is( false ) );
    }

    @Test
    public void shouldGetExistingRegisteredModelTypeRepositoriesIfRegisteringRegisteredUrl() throws Exception {
        final URL[] origRepos = modelTypeManager().modelTypeRepositories();
        final URL[] repos = modelTypeManager().registerModelTypeRepository( INTEGRATION_TEST_METAMODEL_REPOSITORY_URL );
        assertThat( repos, notNullValue() );
        assertThat( repos, is( origRepos ) );
    }

    @Test
    public void shouldGetIntallableModelTypeCategories() throws Exception {
        assertThat( modelTypeManager().installableModelTypeCategories().length == 0, is( false ) );
    }

    @Test
    public void shouldGetModelType() throws Exception {
        modelType();
    }

    @Test
    public void shouldGetModelTypeCategories() throws Exception {
        assertThat( modelTypeManager().modelTypeCategories().length, is( 1 ) );
        assertThat( modelTypeManager().modelTypeCategories()[ 0 ], is( "java" ) );
    }

    @Test
    public void shouldIniitializeModelTypeRepositories() throws Exception {
        modeler().close();
        try ( final Modeler modeler = new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_MODESHAPE_CONFIGURATION_PATH ) )
        {
            assertThat( modeler.modelTypeManager().modelTypeRepositories()[ 0 ], is( new URL(
                                                                                              ModelTypeManager.JBOSS_MODEL_TYPE_REPOSITORY ) ) );
            assertThat( modeler.modelTypeManager().modelTypeRepositories()[ 1 ], is( new URL(
                                                                                              ModelTypeManager.MAVEN_MODEL_TYPE_REPOSITORY ) ) );
        }
    }

    @Test
    public void shouldInstallModelTypes() throws Exception {
        final ModelType[] modelTypes = modelTypeManager().modelTypes();
        assertThat( modelTypes, notNullValue() );
        assertThat( modelTypes.length, not( 0 ) );
    }

    @Test
    public void shouldMoveModelTypeRepositoryDown() throws Exception {
        modeler().close();
        try ( Modeler modeler = new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_MODESHAPE_CONFIGURATION_PATH ) ) {
            final URL url = new URL( ModelTypeManager.JBOSS_MODEL_TYPE_REPOSITORY );
            final int size = modeler.modelTypeManager().modelTypeRepositories().length;
            assertThat( modeler.modelTypeManager().modelTypeRepositories()[ 0 ], is( url ) );
            modeler.modelTypeManager().moveModelTypeRepositoryDown( url );
            assertThat( modeler.modelTypeManager().modelTypeRepositories()[ 1 ], is( url ) );
            assertThat( modeler.modelTypeManager().modelTypeRepositories().length, is( size ) );
        }
    }

    @Test
    public void shouldMoveModelTypeRepositoryUp() throws Exception {
        modeler().close();
        try ( Modeler modeler = new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_MODESHAPE_CONFIGURATION_PATH ) ) {
            final URL url = new URL( ModelTypeManager.MAVEN_MODEL_TYPE_REPOSITORY );
            final int size = modeler.modelTypeManager().modelTypeRepositories().length;
            assertThat( modeler.modelTypeManager().modelTypeRepositories()[ 1 ], is( url ) );
            modeler.modelTypeManager().moveModelTypeRepositoryUp( url );
            assertThat( modeler.modelTypeManager().modelTypeRepositories()[ 0 ], is( url ) );
            assertThat( modeler.modelTypeManager().modelTypeRepositories().length, is( size ) );
        }
    }

    @Test
    public void shouldNotInstallModelTypeCategoryIfAlreadyInstalled() throws Exception {
        assertThat( modelTypeManager().modelTypeCategories().length, is( 1 ) );
        assertThat( modelTypeManager().modelTypes().length, is( 2 ) );
        modelTypeManager().install( CATEGORY );
        assertThat( modelTypeManager().modelTypeCategories().length, is( 1 ) );
        assertThat( modelTypeManager().modelTypes().length, is( 2 ) );
    }

    @Test
    public void shouldNotMoveModelTypeRepositoryDownIfUrlLast() throws Exception {
        modeler().close();
        try ( Modeler modeler = new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_MODESHAPE_CONFIGURATION_PATH ) ) {
            final URL[] urls = modeler.modelTypeManager().modelTypeRepositories();
            final URL url = new URL( ModelTypeManager.MAVEN_MODEL_TYPE_REPOSITORY );
            modeler.modelTypeManager().moveModelTypeRepositoryDown( url );
            assertThat( modeler.modelTypeManager().modelTypeRepositories(), is( urls ) );
        }
    }

    @Test
    public void shouldNotMoveModelTypeRepositoryUpIfUrlFirst() throws Exception {
        final URL[] urls = modelTypeManager().modelTypeRepositories();
        modelTypeManager().moveModelTypeRepositoryUp( INTEGRATION_TEST_METAMODEL_REPOSITORY_URL );
        assertThat( modelTypeManager().modelTypeRepositories(), is( urls ) );
    }

    @Test
    public void shouldOnlyInstallModelTypeCategoryOnce() throws Exception {
        modelTypeManager().install( "java" );
        assertThat( modelTypeManager().modelTypes().length == 0, is( false ) );
        final int size = modelTypeManager().modelTypes().length;
        modelTypeManager().install( "java" );
        assertThat( modelTypeManager().modelTypes().length, is( size ) );
    }

    @Test
    public void shouldRestorePreviouslySavedState() throws Exception {
        modeler().close();
        try ( Modeler modeler = new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH ) ) {
            final ModelTypeManager modelTypeManager = modeler.modelTypeManager();
            for ( final URL url : modelTypeManager.modelTypeRepositories() )
                modelTypeManager.unregisterModelTypeRepository( url );
            modelTypeManager.registerModelTypeRepository( INTEGRATION_TEST_METAMODEL_REPOSITORY_URL );
            modelTypeManager.install( "java" );
        }
        try ( ModeShapeModeler modeler = new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH ) ) {
            final ModelTypeManagerImpl modelTypeManager = ( ModelTypeManagerImpl ) modeler.modelTypeManager();
            assertThat( modelTypeManager.modelTypeRepositories().length, is( 1 ) );
            assertThat( modelTypeManager.modelTypeCategories().length, is( 1 ) );
            assertThat( modelTypeManager.modelTypes().length, is( 2 ) );
        }
    }

    @Test
    public void shouldUninstall() throws Exception {

        assertThat( modelTypeManager().modelTypes().length, is( 2 ) ); // JavaFile, ClassFile
        modelTypeManager().uninstall( "java" );
        assertThat( modelTypeManager().modelTypes().length, is( 0 ) );
    }
}
