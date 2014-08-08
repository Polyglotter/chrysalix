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
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.junit.Test;
import org.mockito.Mock;
import org.modeshape.modeler.Metamodel;
import org.modeshape.modeler.Model;
import org.modeshape.modeler.Modeler;
import org.modeshape.modeler.internal.ModelerImpl;
import org.modeshape.modeler.test.BaseModelerTest;

@SuppressWarnings( "javadoc" )
public final class ModelerImplTest extends BaseModelerTest {

    private static final String MODEL_NAME = "model";

    @Mock
    private Metamodel metamodel;

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
        modeler().generateModel( " ", null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateDefaultModelIfArtifactPathNull() throws Exception {
        modeler().generateModel( null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfMetamodelNull() throws Exception {
        modeler().importModel( stream( " " ), MODEL_NAME, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfPathEmpty() throws Exception {
        modeler().importModel( stream( " " ), " ", metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfPathNull() throws Exception {
        modeler().importModel( stream( " " ), null, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromStreamIfStreamNull() throws Exception {
        modeler().importModel( ( InputStream ) null, MODEL_NAME, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelFromWorkspaceArtifactIfArtifactPathNull() throws Exception {
        modeler().generateModel( ( String ) null, MODEL_NAME, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactPathEmpty() throws Exception {
        modeler().generateModel( " ", MODEL_NAME, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactPathNotFound() throws Exception {
        modeler().generateModel( "doesNotExist", MODEL_NAME, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactUrlNotFound() throws Exception {
        modeler().importModel( new URL( "file:doesNotExist" ), null, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfArtifactUrlNull() throws Exception {
        modeler().importModel( ( URL ) null, null, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfFileNotFound() throws Exception {
        modeler().importModel( new File( "doesNotExist" ), null, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGenerateModelIfFileNull() throws Exception {
        modeler().importModel( ( File ) null, null, metamodel );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelIfPathEmpty() throws Exception {
        modeler().model( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelIfPathNotModel() throws Exception {
        modeler().importData( stream( " " ), MODEL_NAME );
        modeler().model( MODEL_NAME );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelIfPathNull() throws Exception {
        modeler().model( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfNotFound() throws Exception {
        modeler().importData( new URL( "file:doesNotExist" ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfPathEmpty() throws Exception {
        modeler().importData( stream( "stuff" ), " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfPathNull() throws Exception {
        modeler().importData( stream( "stuff" ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfStreamNull() throws Exception {
        modeler().importData( ( InputStream ) null, MODEL_NAME );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportArtifactIfUrlNull() throws Exception {
        modeler().importData( ( URL ) null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportFileIfFileNull() throws Exception {
        modeler().importData( ( File ) null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportFileIfNotFound() throws Exception {
        modeler().importData( new File( "doesNotExist" ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToImportFileUrlIfNotFound() throws Exception {
        modeler().importData( new URL( "file:doesNotExist" ), null );
    }

    @Test
    public void shouldGetChangedConfigurationPath() throws Exception {
        assertThat( modeler().configurationPath(), is( TEST_CONFIGURATION_PATH ) );
    }

    @Test
    public void shouldGetDefaultConfigurationPathIfNotSet() throws Exception {
        final Modeler modeler = new ModelerImpl( TEST_REPOSITORY_STORE_PARENT_PATH );
        assertThat( modeler.configurationPath(), is( ModelerImpl.DEFAULT_CONFIGURATION_PATH ) );
        modeler.close();
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
        final String path = modeler().importData( stream( "stuff" ), "stuff" );
        assertThat( path, is( "/stuff" ) );
    }

    @Test
    public void shouldImportArtifactToSuppliedPath() throws Exception {
        final String path = modeler().importData( stream( "stuff" ), "test/stuff" );
        assertThat( path, is( "/test/stuff" ) );
    }

    @Test
    public void shouldImportFile() throws Exception {
        final String path =
            modeler().importData( new File( getClass().getClassLoader().getResource( "log4j.properties" ).toURI() ), null );
        assertThat( path, is( "/log4j.properties" ) );
    }

    @Test
    public void shouldImportFileToSuppliedPath() throws Exception {
        final String path =
            modeler().importData( new File( getClass().getClassLoader().getResource( "log4j.properties" ).toURI() ), "/test" );
        assertThat( path, is( "/test/log4j.properties" ) );
    }
}
