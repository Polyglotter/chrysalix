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
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.junit.Test;
import org.mockito.Mock;
import org.modeshape.modeler.test.BaseModelerTest;

@SuppressWarnings( "javadoc" )
public final class ModelerTest extends BaseModelerTest {

    private static final String MODEL_NAME = "model";

    @Mock
    private ModelType modelType;

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportIfFileNull() throws Exception {
        modeler().export( mock( Model.class ), ( File ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportIfStreamNull() throws Exception {
        modeler().export( mock( Model.class ), ( OutputStream ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportIfUrlNull() throws Exception {
        modeler().export( mock( Model.class ), ( URL ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportToFileIfModelNull() throws Exception {
        modeler().export( null, mock( File.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportToStreamIfModelNull() throws Exception {
        modeler().export( null, mock( OutputStream.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportToUrlIfModelNull() throws Exception {
        modeler().export( null, new URL( "file:" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateDefaultModelIfArtifactPathEmpty() throws Exception {
        modeler().generateDefaultModel( " ", null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateDefaultModelIfArtifactPathNull() throws Exception {
        modeler().generateDefaultModel( null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfModelTypeNull() throws Exception {
        modeler().generateModel( stream( " " ), MODEL_NAME, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfPathEmpty() throws Exception {
        modeler().generateModel( stream( " " ), " ", modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfPathNull() throws Exception {
        modeler().generateModel( stream( " " ), null, modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfStreamNull() throws Exception {
        modeler().generateModel( ( InputStream ) null, MODEL_NAME, modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromWorkspaceArtifactIfArtifactPathNull() throws Exception {
        modeler().generateModel( ( String ) null, MODEL_NAME, modelType, true );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactPathEmpty() throws Exception {
        modeler().generateModel( " ", MODEL_NAME, modelType, true );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactPathNotFound() throws Exception {
        modeler().generateModel( "doesNotExist", MODEL_NAME, modelType, true );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactUrlNotFound() throws Exception {
        modeler().generateModel( new URL( "file:doesNotExist" ), null, modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactUrlNull() throws Exception {
        modeler().generateModel( ( URL ) null, null, modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfFileNotFound() throws Exception {
        modeler().generateModel( new File( "doesNotExist" ), null, modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfFileNull() throws Exception {
        modeler().generateModel( ( File ) null, null, modelType );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelIfPathEmpty() throws Exception {
        modeler().model( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelIfPathNotModel() throws Exception {
        modeler().importArtifact( stream( " " ), MODEL_NAME );
        modeler().model( MODEL_NAME );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelIfPathNull() throws Exception {
        modeler().model( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfNotFound() throws Exception {
        modeler().importArtifact( new URL( "file:doesNotExist" ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfPathEmpty() throws Exception {
        modeler().importArtifact( stream( "stuff" ), " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfPathNull() throws Exception {
        modeler().importArtifact( stream( "stuff" ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfStreamNull() throws Exception {
        modeler().importArtifact( ( InputStream ) null, MODEL_NAME );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfUrlNull() throws Exception {
        modeler().importArtifact( ( URL ) null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportFileIfFileNull() throws Exception {
        modeler().importFile( null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportFileIfNotFound() throws Exception {
        modeler().importFile( new File( "doesNotExist" ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportFileUrlIfNotFound() throws Exception {
        modeler().importArtifact( new URL( "file:doesNotExist" ), null );
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
    }

    @Test
    public void shouldImportArtifactToSuppliedPath() throws Exception {
        final String path = modeler().importArtifact( stream( "stuff" ), "test/stuff" );
        assertThat( path, is( "/test/stuff" ) );
    }

    @Test
    public void shouldImportFile() throws Exception {
        final String path =
            modeler().importFile( new File( getClass().getClassLoader().getResource( "log4j.properties" ).toURI() ), null );
        assertThat( path, is( "/log4j.properties" ) );
    }

    @Test
    public void shouldImportFileToSuppliedPath() throws Exception {
        final String path =
            modeler().importFile( new File( getClass().getClassLoader().getResource( "log4j.properties" ).toURI() ), "/test" );
        assertThat( path, is( "/test/log4j.properties" ) );
    }
}
