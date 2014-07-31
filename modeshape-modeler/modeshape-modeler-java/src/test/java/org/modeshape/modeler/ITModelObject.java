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
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.modeshape.jcr.JcrLexicon;
import org.modeshape.jcr.JcrNtLexicon;
import org.modeshape.modeler.test.JavaIntegrationTest;
import org.modeshape.sequencer.classfile.ClassFileSequencerLexicon;

@SuppressWarnings( "javadoc" )
public class ITModelObject extends JavaIntegrationTest {

    private ModelObject modelObject() throws Exception {
        return importModel().child( "org" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetBooleanValueIfNonBooleanProperty() throws Exception {
        modelObject().booleanValue( JcrLexicon.PRIMARY_TYPE.toString() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetBooleanValuesIfNonBooleanProperty() throws Exception {
        modelObject().booleanValues( JcrLexicon.PRIMARY_TYPE.toString() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetLongValueIfNonLongProperty() throws Exception {
        modelObject().longValue( JcrLexicon.PRIMARY_TYPE.toString() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetLongValuesIfNonLongProperty() throws Exception {
        modelObject().longValues( JcrLexicon.PRIMARY_TYPE.toString() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetStringValueIfMultiValuedProperty() throws Exception {
        modelObject().stringValue( JcrLexicon.MIXIN_TYPES.getString() );
    }

    @Test
    public void shouldGetAbsolutePath() throws Exception {
        assertThat( modelObject().absolutePath(), is( '/' + MODEL_NAME + "/org" ) );
    }

    @Test
    public void shouldGetChild() throws Exception {
        final ModelObject child = modelObject().child( "modeshape" );
        assertThat( child, notNullValue() );
        assertThat( child.name(), is( "modeshape" ) );
        assertThat( child.child( "blah" ), nullValue() );
    }

    @Test
    public void shouldGetChildren() throws Exception {
        ModelObject[] children = modelObject().children();
        assertThat( children, notNullValue() );
        assertThat( children.length, not( 0 ) );
        children = children[ 0 ].children()[ 0 ].children()[ 0 ].children()[ 0 ].children()[ 0 ].children();
        assertThat( children, notNullValue() );
        assertThat( children.length, is( 0 ) );
    }

    @Test
    public void shouldGetChildrenMatchingPattern() throws Exception {
        ModelObject[] children = modelObject().children( "m*" );
        assertThat( children, notNullValue() );
        assertThat( children.length, not( 0 ) );
        final ModelObject child = children[ 0 ];
        assertThat( child.name(), is( "modeshape" ) );
        children = child.children( "blah" );
        assertThat( children, notNullValue() );
        assertThat( children.length, is( 0 ) );
    }

    @Test
    public void shouldGetIndex() throws Exception {
        assertThat( modelObject().index(), is( 0 ) );
    }

    @Test
    public void shouldGetMixinTypes() throws Exception {
        final String[] types = modelObject().mixinTypes();
        assertThat( types, notNullValue() );
        assertThat( types.length, not( 0 ) );
        assertThat( types[ 0 ], is( ClassFileSequencerLexicon.PACKAGE ) );
    }

    @Test
    public void shouldGetModel() throws Exception {
        assertThat( modelObject().model(), is( importModel() ) );
    }

    @Test
    public void shouldGetModelPath() throws Exception {
        assertThat( modelObject().modelRelativePath(), is( "org" ) );
    }

    @Test
    public void shouldGetName() throws Exception {
        assertThat( modelObject().name(), is( "org" ) );
    }

    @Test
    public void shouldGetNullValueIfBooleanPropertyNotFound() throws Exception {
        assertThat( modelObject().booleanValue( "bogus" ), nullValue() );
    }

    @Test
    public void shouldGetNullValueIfChildNotFound() throws Exception {
        assertThat( modelObject().child( "bogus" ), nullValue() );
    }

    @Test
    public void shouldGetNullValueIfLongPropertyNotFound() throws Exception {
        assertThat( modelObject().longValue( "bogus" ), nullValue() );
    }

    @Test
    public void shouldGetNullValueIfStringPropertyNotFound() throws Exception {
        assertThat( modelObject().stringValue( "bogus" ), nullValue() );
    }

    @Test
    public void shouldGetPrimaryType() throws Exception {
        assertThat( modelObject().primaryType(), is( JcrNtLexicon.UNSTRUCTURED.getString() ) );
    }

    @Test
    public void shouldGetPropertyNames() throws Exception {
        final String[] names = modelObject().propertyNames();
        assertThat( names, notNullValue() );
        assertThat( names.length, is( 0 ) );
    }

    @Test
    public void shouldGetStringValue() throws Exception {
        assertThat( modelObject().stringValue( JcrLexicon.PRIMARY_TYPE.getString() ), is( JcrNtLexicon.UNSTRUCTURED.getString() ) );
    }

    @Test
    public void shouldGetStringValues() throws Exception {
        final String[] vals = modelObject().stringValues( JcrLexicon.PRIMARY_TYPE.toString() );
        assertThat( vals, notNullValue() );
        assertThat( vals.length, is( 1 ) );
    }

    @Test
    public void shouldIndicateIfChildHasSameNameSiblings() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.childHasSameNameSiblings( "modeshape" ), is( false ) );
        assertThat( obj.childHasSameNameSiblings( "blah" ), is( false ) );
    }

    @Test
    public void shouldIndicateIfHasChild() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.hasChild( "modeshape" ), is( true ) );
        assertThat( obj.hasChild( "bogus" ), is( false ) );
    }

    @Test
    public void shouldIndicateIfHasChildren() throws Exception {
        assertThat( modelObject().hasChildren(), is( true ) );
    }

    @Test
    public void shouldIndicateIfHasProperties() throws Exception {
        assertThat( modelObject().hasProperties(), is( false ) );
    }

    @Test
    public void shouldIndicateIfHasProperty() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.hasProperty( JcrLexicon.PRIMARY_TYPE.getString() ), is( true ) );
        assertThat( obj.hasProperty( "bogus" ), is( false ) );
    }

    @Test
    public void shouldIndicateIfPropertyHasMultipleValues() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.propertyHasMultipleValues( JcrLexicon.PRIMARY_TYPE.getString() ), is( false ) );
        assertThat( obj.propertyHasMultipleValues( JcrLexicon.MIXIN_TYPES.getString() ), is( true ) );
        assertThat( obj.propertyHasMultipleValues( "bogus" ), is( false ) );
    }
}
