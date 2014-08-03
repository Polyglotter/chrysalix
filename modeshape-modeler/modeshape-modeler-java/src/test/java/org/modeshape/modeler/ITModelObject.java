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

    @Test
    public void shouldAddChild() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.hasChild( "blah" ), is( false ) );
        obj.addChild( "blah" );
        assertThat( obj.hasChild( "blah" ), is( true ) );
        obj.addChild( "blah", "blah" );
        ModelObject[] children = obj.children( "blah" );
        assertThat( children, notNullValue() );
        assertThat( children.length, is( 3 ) );

        obj.addChildOfType( ClassFileSequencerLexicon.ANNOTATIONS, "blahblah" );
        assertThat( obj.hasChild( "blahblah" ), is( true ) );
        assertThat( obj.child( "blahblah" ).primaryType(), is( ClassFileSequencerLexicon.ANNOTATIONS ) );
        obj.addChildOfType( ClassFileSequencerLexicon.ANNOTATIONS, "blahblah", "blahblah" );
        children = obj.children( "blahblah" );
        assertThat( children, notNullValue() );
        assertThat( children.length, is( 3 ) );
        for ( final ModelObject child : children )
            assertThat( child.primaryType(), is( ClassFileSequencerLexicon.ANNOTATIONS ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildIfEmptyAdditionalName() throws Exception {
        modelObject().addChild( "blah", " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildIfInvalidType() throws Exception {
        modelObject().addChildOfType( "blah", "name" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildIfNullAdditionalName() throws Exception {
        modelObject().addChild( "blah", ( String ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetBooleanValueIfMultiValuedProperty() throws Exception {
        final ModelObject obj = modelObject();
        obj.setProperty( "blah", true, true );
        obj.booleanValue( "blah" );
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
    public void shouldFailToGetLongValueIfMultiValuedProperty() throws Exception {
        final ModelObject obj = modelObject();
        obj.setProperty( "blah", 0L, 0L );
        obj.longValue( "blah" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetLongValueIfNonLongCompatibleProperty() throws Exception {
        modelObject().longValue( JcrLexicon.PRIMARY_TYPE.toString() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetLongValuesIfNonLongCompatibleProperty() throws Exception {
        modelObject().longValues( JcrLexicon.PRIMARY_TYPE.toString() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetStringValueIfMultiValuedProperty() throws Exception {
        modelObject().stringValue( JcrLexicon.MIXIN_TYPES.getString() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToRemoveChildIfEmptyAdditionalName() throws Exception {
        modelObject().removeChild( "blah", " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToRemoveChildIfNullAdditionalName() throws Exception {
        modelObject().removeChild( "blah", ( String ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetBooleanValuesIfAdditionalValuesNullArray() throws Exception {
        modelObject().setProperty( "blah", null, ( Boolean[] ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetLongValuesIfAdditionalValuesNullArray() throws Exception {
        modelObject().setProperty( "blah", null, ( Long[] ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetStringValuesIfAdditionalValuesNullArray() throws Exception {
        modelObject().setProperty( "blah", null, ( String[] ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetStringValuesIfNotMultiValuedProperty() throws Exception {
        importModel().setProperty( ModelerLexicon.Model.EXTERNAL_LOCATION, " ", " " );
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
        assertThat( modelObject().booleanValue( "blah" ), nullValue() );
    }

    @Test
    public void shouldGetNullValueIfChildNotFound() throws Exception {
        assertThat( modelObject().child( "blah" ), nullValue() );
    }

    @Test
    public void shouldGetNullValueIfLongPropertyNotFound() throws Exception {
        assertThat( modelObject().longValue( "blah" ), nullValue() );
    }

    @Test
    public void shouldGetNullValueIfStringPropertyNotFound() throws Exception {
        assertThat( modelObject().stringValue( "blah" ), nullValue() );
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
        final ModelObject obj = modelObject();
        String[] vals = obj.stringValues( JcrLexicon.PRIMARY_TYPE.toString() );
        assertThat( vals, notNullValue() );
        assertThat( vals.length, is( 1 ) );
        vals = obj.stringValues( JcrLexicon.MIXIN_TYPES.toString() );
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
        assertThat( obj.hasChild( "blah" ), is( false ) );
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
        assertThat( obj.hasProperty( "blah" ), is( false ) );
    }

    @Test
    public void shouldIndicateIfPropertyHasMultipleValues() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.propertyHasMultipleValues( JcrLexicon.PRIMARY_TYPE.getString() ), is( false ) );
        assertThat( obj.propertyHasMultipleValues( JcrLexicon.MIXIN_TYPES.getString() ), is( true ) );
        assertThat( obj.propertyHasMultipleValues( "blah" ), is( false ) );
    }

    @Test
    public void shouldRemoveChild() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.hasChild( "blah" ), is( false ) );
        obj.addChild( "blah" );
        assertThat( obj.hasChild( "blah" ), is( true ) );
        obj.removeChild( "blah" );
        assertThat( obj.hasChild( "blah" ), is( false ) );
        obj.addChild( "blah", "blahblah", "blahblah", "blahblahblah" );
        assertThat( obj.children( "blah*" ).length, is( 4 ) );
        obj.removeChild( "blah", "blahblahblah" );
        assertThat( obj.children( "blah*" ).length, is( 2 ) );
        obj.removeChild( "blahblah" );
        assertThat( obj.children( "blah*" ).length, is( 1 ) );
    }

    @Test
    public void shouldSetValues() throws Exception {
        final ModelObject obj = modelObject();

        obj.setProperty( JcrLexicon.MIXIN_TYPES.getString(), ClassFileSequencerLexicon.PACKAGE, ClassFileSequencerLexicon.PACKAGE );
        obj.setProperty( JcrLexicon.MIXIN_TYPES.getString(), ClassFileSequencerLexicon.PACKAGE );

        obj.setProperty( "blah", ( String ) null );
        assertThat( obj.hasProperty( "blah" ), is( false ) );
        obj.setProperty( "blah", "" );
        assertThat( obj.hasProperty( "blah" ), is( true ) );
        assertThat( obj.stringValue( "blah" ), is( "" ) );
        assertThat( obj.stringValues( "blah" ), is( new String[] { "" } ) );
        obj.setProperty( "blah", false );
        assertThat( obj.stringValue( "blah" ), is( "false" ) );
        obj.setProperty( "blah", 1L );
        assertThat( obj.stringValue( "blah" ), is( "1" ) );
        obj.setProperty( "blah", ( String ) null );
        assertThat( obj.hasProperty( "blah" ), is( false ) );
        obj.setProperty( "blah", "", "" );
        assertThat( obj.stringValues( "blah" ), is( new String[] { "", "" } ) );
        obj.setProperty( "blah", ( String ) null );
        assertThat( obj.hasProperty( "blah" ), is( false ) );

        obj.setProperty( "blah", ( Boolean ) null );
        assertThat( obj.hasProperty( "blah" ), is( false ) );
        obj.setProperty( "blah", false );
        assertThat( obj.hasProperty( "blah" ), is( true ) );
        assertThat( obj.booleanValue( "blah" ), is( false ) );
        assertThat( obj.booleanValues( "blah" ), is( new Boolean[] { false } ) );
        obj.setProperty( "blah", ( Boolean ) null );
        assertThat( obj.hasProperty( "blah" ), is( false ) );
        obj.setProperty( "blah", false, false );
        assertThat( obj.booleanValues( "blah" ), is( new Boolean[] { false, false } ) );
        obj.setProperty( "blah", ( Boolean ) null );
        assertThat( obj.hasProperty( "blah" ), is( false ) );

        obj.setProperty( "blah", ( Long ) null );
        assertThat( obj.hasProperty( "blah" ), is( false ) );
        obj.setProperty( "blah", 0L );
        assertThat( obj.hasProperty( "blah" ), is( true ) );
        assertThat( obj.longValue( "blah" ), is( 0L ) );
        assertThat( obj.longValues( "blah" ), is( new Long[] { 0L } ) );
        // obj.setValue( "blah", 0 );
        // assertThat( obj.longValue( "blah" ), is( 0L ) );
        // obj.setValue( "blah", '\0' );
        // assertThat( obj.longValue( "blah" ), is( 0L ) );
        // obj.setValue( "blah", false );
        // assertThat( obj.longValue( "blah" ), is( 0L ) );
        obj.setProperty( "blah", ( Long ) null );
        assertThat( obj.hasProperty( "blah" ), is( false ) );
        obj.setProperty( "blah", 0L, 0L );
        assertThat( obj.longValues( "blah" ), is( new Long[] { 0L, 0L } ) );
        obj.setProperty( "blah", ( Long ) null );
        assertThat( obj.hasProperty( "blah" ), is( false ) );
    }
}
