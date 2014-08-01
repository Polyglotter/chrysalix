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
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.modeshape.modeler.test.JavaIntegrationTest;

@SuppressWarnings( "javadoc" )
public class ITModel extends JavaIntegrationTest {

    @Test
    public void shouldGetDependencies() throws Exception {
        assertThat( importModel().dependencies(), notNullValue() );
    }

    @Test
    public void shouldGetExternalLocation() throws Exception {
        final Model model = importModel();
        assertThat( model, notNullValue() );
        assertThat( model.stringValue( ModelerLexicon.Model.EXTERNAL_LOCATION ).endsWith( MODEL_PATH ), is( true ) );
    }

    @Test
    public void shouldGetIndex() throws Exception {
        assertThat( importModel().index(), is( -1 ) );
    }

    @Test
    public void shouldGetMissingDependencies() throws Exception {
        assertThat( importModel().missingDependencies(), notNullValue() );
    }

    @Test
    public void shouldGetModel() throws Exception {
        final Model model = importModel();
        assertThat( model.model(), is( model ) );
    }

    @Test
    public void shouldGetModelRelativePath() throws Exception {
        assertThat( importModel().modelRelativePath(), is( "" ) );
    }

    @Test
    public void shouldGetModelType() throws Exception {
        final ModelType type = importModel().modelType();
        assertThat( type, notNullValue() );
        assertThat( type.id(), is( JAVA_ID ) );
    }
}
