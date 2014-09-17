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
package org.modelspace.internal;

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
import org.modelspace.Metamodel;
import org.modelspace.Model;
import org.modelspace.Modelspace;
import org.modelspace.internal.ModelspaceImpl;
import org.modelspace.test.BaseModelspaceTest;

@SuppressWarnings( "javadoc" )
public final class ModelspaceImplTest extends BaseModelspaceTest {

    private static final String MODEL_NAME = "model";

    @Mock
    private Metamodel metamodel;

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportIfFileNull() throws Exception {
        modelspace().export( mock( Model.class ), ( File ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportIfStreamNull() throws Exception {
        modelspace().export( mock( Model.class ), ( OutputStream ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportIfUrlNull() throws Exception {
        modelspace().export( mock( Model.class ), ( URL ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportToFileIfModelNull() throws Exception {
        modelspace().export( null, mock( File.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportToStreamIfModelNull() throws Exception {
        modelspace().export( null, mock( OutputStream.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToExportToUrlIfModelNull() throws Exception {
        modelspace().export( null, new URL( "file:" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateDefaultModelIfArtifactPathEmpty() throws Exception {
        modelspace().generateModel( " ", null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateDefaultModelIfArtifactPathNull() throws Exception {
        modelspace().generateModel( null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfMetamodelNull() throws Exception {
        modelspace().importModel( stream( " " ), MODEL_NAME, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfPathEmpty() throws Exception {
        modelspace().importModel( stream( " " ), " ", metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfPathNull() throws Exception {
        modelspace().importModel( stream( " " ), null, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfStreamNull() throws Exception {
        modelspace().importModel( ( InputStream ) null, MODEL_NAME, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromWorkspaceArtifactIfArtifactPathNull() throws Exception {
        modelspace().generateModel( ( String ) null, MODEL_NAME, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactPathEmpty() throws Exception {
        modelspace().generateModel( " ", MODEL_NAME, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactPathNotFound() throws Exception {
        modelspace().generateModel( "doesNotExist", MODEL_NAME, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactUrlNotFound() throws Exception {
        modelspace().importModel( new URL( "file:doesNotExist" ), null, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactUrlNull() throws Exception {
        modelspace().importModel( ( URL ) null, null, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfFileNotFound() throws Exception {
        modelspace().importModel( new File( "doesNotExist" ), null, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfFileNull() throws Exception {
        modelspace().importModel( ( File ) null, null, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelIfPathEmpty() throws Exception {
        modelspace().model( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelIfPathNotModel() throws Exception {
        modelspace().importData( stream( " " ), MODEL_NAME );
        modelspace().model( MODEL_NAME );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelIfPathNull() throws Exception {
        modelspace().model( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfNotFound() throws Exception {
        modelspace().importData( new URL( "file:doesNotExist" ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfPathEmpty() throws Exception {
        modelspace().importData( stream( "stuff" ), " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfPathNull() throws Exception {
        modelspace().importData( stream( "stuff" ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfStreamNull() throws Exception {
        modelspace().importData( ( InputStream ) null, MODEL_NAME );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfUrlNull() throws Exception {
        modelspace().importData( ( URL ) null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportFileIfFileNull() throws Exception {
        modelspace().importData( ( File ) null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportFileIfNotFound() throws Exception {
        modelspace().importData( new File( "doesNotExist" ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportFileUrlIfNotFound() throws Exception {
        modelspace().importData( new URL( "file:doesNotExist" ), null );
    }

    @Test
    public void shouldGetChangedConfigurationPath() throws Exception {
        assertThat( modelspace().configurationPath(), is( TEST_CONFIGURATION_PATH ) );
    }

    @Test
    public void shouldGetDefaultConfigurationPathIfNotSet() throws Exception {
        try ( final Modelspace modelspace = new ModelspaceImpl( TEST_REPOSITORY_STORE_PARENT_PATH ) ) {
            assertThat( modelspace.configurationPath(), is( ModelspaceImpl.DEFAULT_CONFIGURATION_PATH ) );
        }
    }

    @Test
    public void shouldGetNullModelIfPathNotFound() throws Exception {
        assertThat( modelspace().model( "pathNotFound" ), nullValue() );
    }

    @Test
    public void shouldGetRepositoryStoreParentPath() throws Exception {
        assertThat( modelspace().repositoryStoreParentPath(), is( TEST_REPOSITORY_STORE_PARENT_PATH ) );
    }

    @Test
    public void shouldImportArtifact() throws Exception {
        final String path = modelspace().importData( stream( "stuff" ), "stuff" );
        assertThat( path, is( "/stuff" ) );
    }

    @Test
    public void shouldImportArtifactToSuppliedPath() throws Exception {
        final String path = modelspace().importData( stream( "stuff" ), "test/stuff" );
        assertThat( path, is( "/test/stuff" ) );
    }

    @Test
    public void shouldImportFile() throws Exception {
        final String path =
            modelspace().importData( new File( getClass().getClassLoader().getResource( "log4j.properties" ).toURI() ), null );
        assertThat( path, is( "/log4j.properties" ) );
    }

    @Test
    public void shouldImportFileToSuppliedPath() throws Exception {
        final String path =
            modelspace().importData( new File( getClass().getClassLoader().getResource( "log4j.properties" ).toURI() ), "/test" );
        assertThat( path, is( "/test/log4j.properties" ) );
    }
}
