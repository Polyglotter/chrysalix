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
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import javax.jcr.Node;
import javax.jcr.Session;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.modeshape.jcr.JcrLexicon;
import org.modeshape.modeler.internal.ModelerLexicon;
import org.modeshape.modeler.internal.Task;
import org.modeshape.modeler.test.BaseTest;

@SuppressWarnings( "javadoc" )
public final class ModelerTest extends BaseTest {

    private static Modeler failingModeler;

    @BeforeClass
    public static void beforeClass() throws Exception {
        failingModeler = new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_MODESHAPE_CONFIGURATION_PATH );
        failingModeler.close();
    }

    @Mock
    private ModelType modelType;

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportIfFileNull() throws Exception {
        failingModeler.export( mock( Model.class ), ( File ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportIfStreamNull() throws Exception {
        failingModeler.export( mock( Model.class ), ( OutputStream ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportIfUrlNull() throws Exception {
        failingModeler.export( mock( Model.class ), ( URL ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportToFileIfModelNull() throws Exception {
        failingModeler.export( null, mock( File.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportToStreamIfModelNull() throws Exception {
        failingModeler.export( null, mock( OutputStream.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportToUrlIfModelNull() throws Exception {
        failingModeler.export( null, new URL( "file:" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateDefaultModelIfArtifactPathEmpty() throws Exception {
        failingModeler.generateDefaultModel( " ", null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateDefaultModelIfArtifactPathNull() throws Exception {
        failingModeler.generateDefaultModel( null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfModelTypeNull() throws Exception {
        modeler().generateModel( stream( XML_ARTIFACT ), ARTIFACT_NAME, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfPathEmpty() throws Exception {
        modeler().generateModel( stream( XML_ARTIFACT ), " ", modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfPathNull() throws Exception {
        modeler().generateModel( stream( XML_ARTIFACT ), null, modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfStreamNull() throws Exception {
        failingModeler.generateModel( ( InputStream ) null, ARTIFACT_NAME, modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromWorkspaceArtifactIfArtifactPathNull() throws Exception {
        failingModeler.generateModel( ( String ) null, ARTIFACT_NAME, modelType, true );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactPathEmpty() throws Exception {
        failingModeler.generateModel( " ", ARTIFACT_NAME, modelType, true );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactPathNotFound() throws Exception {
        modeler().generateModel( "doesNotExist", ARTIFACT_NAME, modelType, true );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactUrlNotFound() throws Exception {
        failingModeler.generateModel( new URL( "file:doesNotExist" ), null, modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactUrlNull() throws Exception {
        failingModeler.generateModel( ( URL ) null, null, modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfFileNotFound() throws Exception {
        failingModeler.generateModel( new File( "doesNotExist" ), null, modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfFileNull() throws Exception {
        failingModeler.generateModel( ( File ) null, null, modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelIfPathEmpty() throws Exception {
        failingModeler.model( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelIfPathNotModel() throws Exception {
        modeler().importArtifact( stream( XML_ARTIFACT ), ARTIFACT_NAME );
        modeler().model( ARTIFACT_NAME );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelIfPathNull() throws Exception {
        failingModeler.model( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfNotFound() throws Exception {
        failingModeler.importArtifact( new URL( "file:doesNotExist" ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfPathEmpty() throws Exception {
        failingModeler.importArtifact( stream( "stuff" ), " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfPathNull() throws Exception {
        failingModeler.importArtifact( stream( "stuff" ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfStreamNull() throws Exception {
        failingModeler.importArtifact( ( InputStream ) null, ARTIFACT_NAME );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfUrlNull() throws Exception {
        failingModeler.importArtifact( ( URL ) null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportFileIfFileNull() throws Exception {
        failingModeler.importFile( null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportFileIfNotFound() throws Exception {
        failingModeler.importFile( new File( "doesNotExist" ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportFileUrlIfNotFound() throws Exception {
        failingModeler.importArtifact( new URL( "file:doesNotExist" ), null );
    }

    @Test
    public void shouldGenerateModelFromFile() throws Exception {
        modelTypeManager().install( "xml" );
        final Model model = modeler().generateModel( new File( "src/test/resources/Books.xsd" ),
                                                     null,
                                                     modelTypeManager().modelType( XML_MODEL_TYPE_ID ) );
        assertThat( model, notNullValue() );
    }

    @Test
    public void shouldGenerateModelFromFileWithSuppliedName() throws Exception {
        modelTypeManager().install( "xml" );
        final Model model = modeler().generateModel( new File( "src/test/resources/Books.xsd" ),
                                                     null,
                                                     ARTIFACT_NAME,
                                                     modelTypeManager().modelType( XML_MODEL_TYPE_ID ) );
        assertThat( model, notNullValue() );
        assertThat( model.name(), is( ARTIFACT_NAME ) );
    }

    @Test
    public void shouldGenerateModelFromStream() throws Exception {
        modelTypeManager().install( "xml" );
        modeler().generateModel( stream( XML_ARTIFACT ), ARTIFACT_NAME, modelTypeManager().modelType( XML_MODEL_TYPE_ID ) );
    }

    @Test
    public void shouldGenerateModelFromUrl() throws Exception {
        modelTypeManager().install( "xml" );
        final Model model = modeler().generateModel( new URL( "file:src/test/resources/Books.xsd" ),
                                                     null,
                                                     modelTypeManager().modelType( XML_MODEL_TYPE_ID ) );
        assertThat( model, notNullValue() );
    }

    @Test
    public void shouldGenerateModelFromUrlWithSuppliedName() throws Exception {
        modelTypeManager().install( "xml" );
        final Model model = modeler().generateModel( new URL( "file:src/test/resources/Books.xsd" ),
                                                     null,
                                                     ARTIFACT_NAME,
                                                     modelTypeManager().modelType( XML_MODEL_TYPE_ID ) );
        assertThat( model, notNullValue() );
        assertThat( model.name(), is( ARTIFACT_NAME ) );
    }

    @Test
    public void shouldGenerateModelFromWorkspaceArtifact() throws Exception {
        modelTypeManager().install( "xml" );
        final String path = modeler().importArtifact( stream( XML_ARTIFACT ), ARTIFACT_NAME );
        final Model model = modeler().generateModel( path, ARTIFACT_NAME, modelTypeManager().modelType( XML_MODEL_TYPE_ID ), true );
        assertThat( model, notNullValue() );
    }

    @Test
    public void shouldGetChangedModeShapeConfigurationPath() throws Exception {
        assertThat( modeler().modeShapeConfigurationPath(), is( TEST_MODESHAPE_CONFIGURATION_PATH ) );
    }

    @Test
    public void shouldGetDefaultModeShapeConfigurationPathIfNotSet() throws Exception {
        final ModeShapeModeler modeShapeModeler = new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH );
        assertThat( modeShapeModeler.modeShapeConfigurationPath(), is( Modeler.DEFAULT_MODESHAPE_CONFIGURATION_PATH ) );
        modeShapeModeler.close();
    }

    @Test
    public void shouldGetModel() throws Exception {
        modelTypeManager().install( XML_MODEL_TYPE_CATEGORY );
        final Model generatedModel =
            modeler().generateModel( stream( XML_ARTIFACT ), ARTIFACT_NAME, modelTypeManager().modelType( XML_MODEL_TYPE_ID ) );
        final Model model = modeler().model( ARTIFACT_NAME );
        assertThat( model, is( generatedModel ) );
    }

    @Test
    public void shouldGetNullModelIfPathNotFound() throws Exception {
        assertThat( modeler().model( "pathNotFound" ), nullValue() );
    }

    @Test
    public void shouldGetRepositoryStoreParentPath() throws Exception {
        assertThat( modeler().repositoryStoreParentPath(), is( TEST_REPOSITORY_STORE_PARENT_PATH ) );
    }

    @Test
    public void shouldImportArtifact() throws Exception {
        final String path = modeler().importArtifact( stream( "stuff" ), "stuff" );
        assertThat( path, is( "/stuff" ) );
        verifyPathExistsWithContent( path );
    }

    @Test
    public void shouldImportArtifactToSuppliedPath() throws Exception {
        final String path = modeler().importArtifact( stream( "stuff" ), "test/stuff" );
        assertThat( path, is( "/test/stuff" ) );
        verifyPathExistsWithContent( path );
    }

    @Test
    public void shouldImportFile() throws Exception {
        final String path =
            modeler().importFile( new File( getClass().getClassLoader().getResource( "Books.xsd" ).toURI() ),
                                  null );
        assertThat( path, is( "/Books.xsd" ) );
        verifyPathExistsWithContent( path );
    }

    @Test
    public void shouldImportFileToSuppliedPath() throws Exception {
        final String path =
            modeler().importFile( new File( getClass().getClassLoader().getResource( "Books.xsd" ).toURI() ),
                                  "/test" );
        assertThat( path, is( "/test/Books.xsd" ) );
        verifyPathExistsWithContent( path );
    }

    @Test
    public void shouldRecordExternalLocationIfImportArtifact() throws Exception {
        final URL url = new URL( "File:src/test/resources/Books.xsd" );
        final String path = modeler().importArtifact( url, null );
        manager().run( new Task< Void >() {

            @Override
            public Void run( final Session session ) throws Exception {
                final Node node = session.getNode( path );
                assertThat( node, notNullValue() );
                assertThat( node.getProperty( ModelerLexicon.EXTERNAL_LOCATION ).getString(), is( url.toString() ) );
                return null;
            }
        } );
    }

    @Test
    public void shouldRecordUrlIfImportFile() throws Exception {
        final URI uri = getClass().getClassLoader().getResource( "Books.xsd" ).toURI();
        final String path = modeler().importFile( new File( uri ), null );
        manager().run( new Task< Void >() {

            @Override
            public Void run( final Session session ) throws Exception {
                final Node node = session.getNode( path );
                assertThat( node, notNullValue() );
                assertThat( node.getProperty( ModelerLexicon.EXTERNAL_LOCATION ).getString(), is( uri.toString() ) );
                return null;
            }
        } );
    }

    private void verifyPathExistsWithContent( final String path ) throws Exception {
        manager().run( new Task< Void >() {

            @Override
            public Void run( final Session session ) throws Exception {
                final Node node = session.getNode( path );
                assertThat( node, notNullValue() );
                assertThat( node.getNode( JcrLexicon.CONTENT.getString() ), notNullValue() );
                assertThat( node.getNode( JcrLexicon.CONTENT.getString() ).getProperty( JcrLexicon.DATA.getString() ), notNullValue() );
                return null;
            }
        } );
    }
}
