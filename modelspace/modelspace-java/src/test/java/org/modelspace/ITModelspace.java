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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import org.junit.Test;
import org.modelspace.Model;
import org.modelspace.ModelspaceException;
import org.modelspace.test.JavaIntegrationTest;

@SuppressWarnings( "javadoc" )
public class ITModelspace extends JavaIntegrationTest {

    @Test
    public void shouldDeleteTemporaryDataAfterImportingModel() throws Exception {
        final String path = modelspace().importData( MODEL_FILE, null );
        final Model model = modelspace().generateModel( path, "model", metamodel() );
        assertThat( model, notNullValue() );
        // TODO: Complete when task #185 is complete
        // final Model model = modelspace().generateModel( path, "model", metamodel(), false );
        // assertThat( model, notNullValue() );
    }

    @Test( expected = ModelspaceException.class )
    public void shouldFailToCreateModelIfMetamodelIsInapplicable() throws Exception {
        modelspace().importModel( stream( "stuff" ), MODEL_NAME, metamodel() );
    }

    @Test
    public void shouldGetModel() throws Exception {
        final Model importedModel = importModel();
        final Model model = modelspace().model( MODEL_NAME );
        assertThat( model, is( importedModel ) );
    }

    @Test
    public void shouldImportModelFromFile() throws Exception {
        importModel();
    }

    @Test
    public void shouldImportModelFromFileWithSuppliedName() throws Exception {
        final Model model = modelspace().importModel( MODEL_FILE, null, MODEL_NAME, metamodel() );
        assertThat( model, notNullValue() );
        assertThat( model.name(), is( MODEL_NAME ) );
    }

    @Test
    public void shouldImportModelFromStream() throws Exception {
        try ( InputStream stream = MODEL_FILE.toURI().toURL().openStream() ) {
            final Model model = modelspace().importModel( stream, MODEL_NAME, metamodel() );
            assertThat( model, notNullValue() );
        }
    }

    @Test
    public void shouldImportModelFromUrl() throws Exception {
        final Model model = modelspace().importModel( MODEL_FILE.toURI().toURL(), null, metamodel() );
        assertThat( model, notNullValue() );
    }

    @Test
    public void shouldImportModelFromUrlWithSuppliedName() throws Exception {
        final Model model = modelspace().importModel( MODEL_FILE.toURI().toURL(), null, MODEL_NAME, metamodel() );
        assertThat( model, notNullValue() );
        assertThat( model.name(), is( MODEL_NAME ) );
    }

    @Test
    public void shouldImportModelFromWorkspace() throws Exception {
        final String path = modelspace().importData( MODEL_FILE, null );
        final Model model = modelspace().generateModel( path, "model", metamodel() );
        assertThat( model, notNullValue() );
    }
}
