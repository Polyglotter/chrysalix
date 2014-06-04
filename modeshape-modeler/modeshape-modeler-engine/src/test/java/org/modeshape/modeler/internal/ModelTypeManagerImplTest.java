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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.InputStream;
import java.net.URL;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.junit.Test;
import org.modeshape.jcr.api.JcrTools;
import org.modeshape.modeler.ModeShapeModeler;
import org.modeshape.modeler.ModelType;
import org.modeshape.modeler.ModelTypeManager;
import org.modeshape.modeler.Modeler;
import org.modeshape.modeler.ModelerLexicon;
import org.modeshape.modeler.TestUtil;
import org.modeshape.modeler.test.BaseTest;

@SuppressWarnings( "javadoc" )
public class ModelTypeManagerImplTest extends BaseTest {

    private ModelTypeManager failingModelTypeManager() throws Exception {
        return new ModelTypeManagerImpl( mock( Manager.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetApplicableModelTypesIfPathIsEmpty() throws Exception {
        failingModelTypeManager().modelTypesForArtifact( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetApplicableModelTypesIfPathIsNull() throws Exception {
        failingModelTypeManager().modelTypesForArtifact( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetDefaultModelTypeIfPathIsEmpty() throws Exception {
        failingModelTypeManager().defaultModelType( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetDefaultModelTypeIfPathIsNull() throws Exception {
        failingModelTypeManager().defaultModelType( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelTypeIfNameIsEmpty() throws Exception {
        failingModelTypeManager().modelType( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelTypeIfNameIsNull() throws Exception {
        failingModelTypeManager().modelType( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelTypesForCategoryIfCategoryEmpty() throws Exception {
        failingModelTypeManager().modelTypesForCategory( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelTypesForCategoryIfCategoryNull() throws Exception {
        failingModelTypeManager().modelTypesForCategory( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToInstallModelTypesIfCategoryIsEmpty() throws Exception {
        failingModelTypeManager().install( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToInstallModelTypesIfCategoryIsNull() throws Exception {
        failingModelTypeManager().install( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToInstallModelTypesIfCategoryNotFound() throws Exception {
        modelTypeManager().install( "bogus" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToMoveModelTypeRepositoryDownIfUrlNotFound() throws Exception {
        failingModelTypeManager().moveModelTypeRepositoryDown( modelTypeRepository() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToMoveModelTypeRepositoryDownIfUrlNull() throws Exception {
        failingModelTypeManager().moveModelTypeRepositoryDown( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToMoveModelTypeRepositoryUpIfUrlNotFound() throws Exception {
        failingModelTypeManager().moveModelTypeRepositoryUp( modelTypeRepository() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToMoveModelTypeRepositoryUpIfUrlNull() throws Exception {
        failingModelTypeManager().moveModelTypeRepositoryUp( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToRegisterModelTypeRepositoryIfUrlIsNull() throws Exception {
        failingModelTypeManager().registerModelTypeRepository( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToUninstallIfCategoryEmpty() throws Exception {
        failingModelTypeManager().uninstall( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToUninstallIfCategoryNull() throws Exception {
        failingModelTypeManager().uninstall( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToUnregisterModelTypeRepositoryIfUrlIsNull() throws Exception {
        failingModelTypeManager().unregisterModelTypeRepository( null );
    }

    @Test
    public void shouldGetDefaultRegisteredModelTypeRepositories() throws Exception {
        final URL[] repos = modelTypeManager().modelTypeRepositories();
        assertThat( repos, notNullValue() );
        assertThat( repos.length == 0, is( false ) );
    }

    @Test
    public void shouldGetEmptyApplicableModelTypesIfFileHasUknownMimeType() throws Exception {
        final String path = modeler().importArtifact( stream( "stuff" ), ARTIFACT_NAME );
        final ModelType[] types = modelTypeManager().modelTypesForArtifact( path );
        assertThat( types, notNullValue() );
        assertThat( types.length == 0, is( true ) );
    }

    @Test
    public void shouldGetExistingRegisteredModelTypeRepositoriesIfRegisteringRegisteredUrl() throws Exception {
        final URL[] origRepos = modelTypeManager().modelTypeRepositories();
        final URL[] repos =
            modelTypeManager().registerModelTypeRepository( modelTypeRepository() );
        assertThat( repos, notNullValue() );
        assertThat( repos, is( origRepos ) );
    }

    @Test
    public void shouldGetExistingRegisteredModelTypeRepositoriesIfUnregisteringUnregisteredUrl() throws Exception {
        final int size = modelTypeManager().modelTypeRepositories().length;
        final URL[] repos = modelTypeManager().unregisterModelTypeRepository( new URL( "file:" ) );
        assertThat( repos, notNullValue() );
        assertThat( repos.length, is( size ) );
    }

    @Test
    public void shouldGetModelType() throws Exception {
        modelTypeManager().install( "xml" );
        assertThat( modelTypeManager().modelType( XML_MODEL_TYPE_ID ), notNullValue() );
    }

    @Test
    public void shouldGetModelTypeCategories() throws Exception {
        modelTypeManager().install( "java" );
        assertThat( modelTypeManager().modelTypeCategories().length, is( 1 ) );
        assertThat( modelTypeManager().modelTypeCategories()[ 0 ], is( "java" ) );
    }

    @Test
    public void shouldGetNullDefaultModelTypeIfFileHasUknownMimeType() throws Exception {
        final String path = modeler().importArtifact( stream( "stuff" ), ARTIFACT_NAME );
        assertThat( modelTypeManager().defaultModelType( path ), nullValue() );
    }

    @Test
    public void shouldInstallModelTypes() throws Exception {
        modelTypeManager().install( "java" );
        assertThat( modelTypeManager().modelTypes().length == 0, is( false ) );
        final ModelTypeImpl type = ( ModelTypeImpl ) modelTypeManager().modelTypes()[ 0 ];
        assertThat( type.category(), is( "java" ) );
        assertThat( type.sequencerClass, notNullValue() );
    }

    @Test
    public void shouldLoadState() throws Exception {
        modeler().close();
        int repos;
        try ( Modeler modeler = new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH ) ) {
            final ModelTypeManager modelTypeManager = modeler.modelTypeManager();
            repos = modelTypeManager.modelTypeRepositories().length;
            for ( final URL url : modelTypeManager.modelTypeRepositories() )
                modelTypeManager.unregisterModelTypeRepository( url );
            modelTypeManager.registerModelTypeRepository( modelTypeRepository() );
            modelTypeManager.install( "java" );
            modelTypeManager.install( "sramp" );
            modelTypeManager.install( "xsd" );
        }
        try ( ModeShapeModeler modeler = new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH ) ) {
            final ModelTypeManagerImpl modelTypeManager = ( ModelTypeManagerImpl ) modeler.modelTypeManager();
            assertThat( modelTypeManager.modelTypeRepositories().length, not( repos ) );
            assertThat( modelTypeManager.modelTypes().length == 0, is( false ) );
            assertThat( modelTypeManager.libraryClassLoader.getURLs().length > 0, is( true ) );
            TestUtil.manager( modeler ).run( modelTypeManager, new SystemTask< Void >() {

                @Override
                public Void run( final Session session,
                                 final Node systemNode ) throws Exception {
                    assertThat( systemNode.getNode( ModelerLexicon.MODEL_TYPE_CATEGORIES ).getNodes().getSize(), is( 3L ) );
                    return null;
                }
            } );
        }
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
        final String category = "test";

        manager().run( modelTypeManager(), new SystemTask< Void >() {

            @Override
            public Void run( final Session session,
                             final Node systemNode ) throws Exception {
                final String version = manager().repository().getDescriptor( Repository.REP_VERSION_DESC );
                final String archiveName = "modeshape-sequencer-test-" + version + "-module-with-dependencies.zip";
                final Node categoriesNode = systemNode.getNode( ModelerLexicon.MODEL_TYPE_CATEGORIES );
                final Node categoryNode = categoriesNode.addNode( category, ModelerLexicon.Category.NODE_TYPE );
                final Node archivesNode = categoryNode.addNode( ModelerLexicon.Category.ARCHIVES, ModelerLexicon.Category.ARCHIVES );

                // add jar to category node in repository
                try ( final InputStream stream = getClass().getClassLoader().getResourceAsStream( archiveName ) ) {
                    new JcrTools().uploadFile( session,
                                               archivesNode.getPath() + '/' + archiveName,
                                               stream );
                }

                session.save();
                return null;
            }
        } );

        modelTypeManager().install( category );
        assertThat( modelTypeManager().modelTypes().length == 0, is( true ) );
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
        modelTypeManager().moveModelTypeRepositoryUp( modelTypeRepository() );
        assertThat( modelTypeManager().modelTypeRepositories(), is( urls ) );
    }

    @Test
    public void shouldNotReturnNullModelTypeCategories() throws Exception {
        assertThat( modelTypeManager().modelTypeCategories(), notNullValue() );
        assertThat( modelTypeManager().modelTypeCategories().length == 0, is( true ) );
    }

    @Test
    public void shouldNotReturnNullModelTypesForCategory() throws Exception {
        assertThat( modelTypeManager().modelTypesForCategory( "bogus" ), notNullValue() );
        assertThat( modelTypeManager().modelTypesForCategory( "bogus" ).length == 0, is( true ) );
    }

    @Test
    public void shouldRegisterModelTypeRepository() throws Exception {
        final int size = modelTypeManager().modelTypeRepositories().length;
        final URL url = new URL( "file:bogus" );
        final URL[] repos = modelTypeManager().registerModelTypeRepository( url );
        assertThat( repos, notNullValue() );
        assertThat( repos.length, is( size + 1 ) );
        assertThat( repos[ 0 ], is( url ) );
    }

    @Test
    public void shouldUninstall() throws Exception {
        modelTypeManager().install( "java" );
        assertThat( modelTypeManager().modelTypes().length, is( 2 ) ); // JavaFile, ClassFile
        modelTypeManager().uninstall( "java" );
        assertThat( modelTypeManager().modelTypes().length, is( 0 ) );
        manager().run( modelTypeManager(), new SystemTask< Void >() {

            @Override
            public Void run( final Session session,
                             final Node systemNode ) throws Exception {
                assertThat( systemNode.getNode( ModelerLexicon.MODEL_TYPE_CATEGORIES ).getNodes().getSize(), is( 0L ) );
                return null;
            }
        } );
    }

    @Test
    public void shouldUnregisterModelTypeRepository() throws Exception {
        final int size = modelTypeManager().modelTypeRepositories().length;
        final URL repo = new URL( "file:" );
        modelTypeManager().registerModelTypeRepository( repo );
        final URL[] repos = modelTypeManager().unregisterModelTypeRepository( repo );
        assertThat( repos, notNullValue() );
        assertThat( repos.length, is( size ) );
    }
}
