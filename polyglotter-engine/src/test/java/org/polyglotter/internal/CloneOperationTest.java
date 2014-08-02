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
package org.polyglotter.internal;

import org.junit.Test;
import org.polyglotter.test.BaseTest;

@SuppressWarnings( "javadoc" )
public class CloneOperationTest extends BaseTest {

    // private static final String NEW_XML_MODEL = "New XML Model";
    //
    // private Model model;
    //
    // /**
    // * {@inheritDoc}
    // *
    // * @see org.polyglotter.test.BaseTest#before()
    // */
    // @Override
    // public void before() throws Exception {
    // super.before();
    // final MetamodelManager metamodelManager = polyglotter().metamodelManager();
    // metamodelManager.install( XML_METAMODEL_CATEGORY );
    // final Metamodel metamodel = metamodelManager.metamodel( XML_METAMODEL_NAME );
    // model = polyglotter().importModel( stream( XML_ARTIFACT ), XML_MODEL_NAME, metamodel );
    // }

    @Test
    public void shouldCreateNewModelWithClonedChild() {
        // final CloneOperation op = new CloneOperation( model, XML_ROOT, NEW_XML_MODEL );
        // op.execute();
        // final Model model = polyglotter().model( NEW_XML_MODEL );
        // assertThat( model, notNullValue() );
        // assertThat( model.child( XML_ROOT ), notNullValue() );
        // assertThat( model.primaryType(), is( this.model.primaryType() ) );
        // assertThat( model.mixinTypes(), is( this.model.mixinTypes() ) );
        // assertThat( model.stringValue( ModelerLexicon.Model.METAMODEL ), is( this.model.stringValue(
        // ModelerLexicon.Model.METAMODEL ) ) );
    }

    @Test
    public void shouldCreateNewModelWithClonedProperty() {
        // final CloneOperation op = new CloneOperation( model, XML_ROOT + "/@" + XML_ROOT_PROPERTY, NEW_XML_MODEL );
        // op.execute();
        // final Model model = polyglotter().model( NEW_XML_MODEL );
        // assertThat( model, notNullValue() );
        // assertThat( model.child( XML_ROOT ).stringValue( XML_ROOT_PROPERTY ), is( XML_STRING_VALUE ) );
    }

    @Test
    public void shouldGetName() {
        // final CloneOperation op = new CloneOperation( model, XML_ROOT, NEW_XML_MODEL );
        // assertThat( op.name(), is( "clone" ) );
    }
}
