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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.modeshape.jcr.JcrLexicon;
import org.modeshape.jcr.JcrNtLexicon;
import org.modeshape.modeler.test.JavaIntegrationTest;
import org.modeshape.sequencer.classfile.ClassFileSequencerLexicon;

@SuppressWarnings( "javadoc" )
public class ITModelObject extends JavaIntegrationTest {

    Map< String, ? > NULL_VALUES_BY_PROPERTY = null;

    private ModelObject modelObject() throws Exception {
        return importModel().child( "org" );
    }

    Map< String, ? > primaryTypeProperties() {
        final Map< String, Object > props = new HashMap<>();
        props.put( ClassFileSequencerLexicon.FINAL, "blah" );
        props.put( ClassFileSequencerLexicon.INTERFACE, "blah" );
        props.put( ClassFileSequencerLexicon.ABSTRACT, "blah" );
        props.put( ClassFileSequencerLexicon.NAME, "blah" );
        props.put( ClassFileSequencerLexicon.STRICT_FP, "blah" );
        props.put( ClassFileSequencerLexicon.VISIBILITY, "public" );
        return props;
    }

    @Test
    public void shouldAddChild() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.hasChild( "blah" ), is( false ) );
        obj.addChild( "blah" );
        assertThat( obj.hasChild( "blah" ), is( true ) );
        obj.addChild( "blah", "blah" );
        final ModelObject[] children = obj.children( "blah" );
        assertThat( children, notNullValue() );
        assertThat( children.length, is( 3 ) );
    }

    @Test
    public void shouldAddChildOfType() throws Exception {
        final ModelObject obj = modelObject();
        obj.addChildOfType( ClassFileSequencerLexicon.ANNOTATIONS, "blah" );
        assertThat( obj.hasChild( "blah" ), is( true ) );
        assertThat( obj.child( "blah" ).primaryType(), is( ClassFileSequencerLexicon.ANNOTATIONS ) );

        obj.addChildOfType( ClassFileSequencerLexicon.ANNOTATIONS, "blah", "blah" );
        final ModelObject[] children = obj.children( "blah" );
        assertThat( children, notNullValue() );
        assertThat( children.length, is( 3 ) );
        for ( final ModelObject child : children )
            assertThat( child.primaryType(), is( ClassFileSequencerLexicon.ANNOTATIONS ) );

        obj.addChildOfType( null, "blahblah" );
        ModelObject child = obj.child( "blahblah" );
        assertThat( child, notNullValue() );
        assertThat( child.primaryType(), is( JcrNtLexicon.UNSTRUCTURED.getString() ) );
        obj.addChildOfType( " ", "blahblahblah" );
        child = obj.child( "blahblahblah" );
        assertThat( child, notNullValue() );
        assertThat( child.primaryType(), is( JcrNtLexicon.UNSTRUCTURED.getString() ) );
    }

    @Test
    public void shouldAddChildOfTypeWithProperties() throws Exception {
        final ModelObject obj = modelObject();
        obj.addChildOfType( ClassFileSequencerLexicon.ANNOTATIONS, "blah", NULL_VALUES_BY_PROPERTY );
        assertThat( obj.hasChild( "blah" ), is( true ) );
        assertThat( obj.child( "blah" ).primaryType(), is( ClassFileSequencerLexicon.ANNOTATIONS ) );

        final Map< String, ? > props = primaryTypeProperties();
        obj.addChildOfType( ClassFileSequencerLexicon.CLASS, "blahblah", props );
        final ModelObject child = obj.child( "blahblah" );
        assertThat( child, notNullValue() );
        assertThat( child.hasProperty( ClassFileSequencerLexicon.VISIBILITY ), is( true ) );
        assertThat( child.stringValue( ClassFileSequencerLexicon.VISIBILITY ), is( "public" ) );

        obj.addChildOfType( null, "blahblahblah", props );
        obj.addChildOfType( " ", "blahblahblah", props );
        final ModelObject[] children = obj.children( "blahblahblah" );
        assertThat( children, notNullValue() );
        assertThat( children.length, is( 2 ) );
        for ( final ModelObject child2 : children )
            assertThat( child2.primaryType(), is( JcrNtLexicon.UNSTRUCTURED.getString() ) );
    }

    @Test
    public void shouldAddMixinType() throws Exception {
        final ModelObject obj = modelObject();
        final int mixins = obj.mixinTypes().length;
        obj.addMixinType( ModelerLexicon.UNSTRUCTURED_MIXIN, ModelerLexicon.Model.MODEL_MIXIN );
        assertThat( obj.mixinTypes().length, is( mixins + 2 ) );
    }

    @Test
    public void shouldAddMixinTypeWithProperties() throws Exception {
        final ModelObject obj = modelObject();
        final int mixins = obj.mixinTypes().length;
        final Map< String, ? > props = primaryTypeProperties();
        obj.addMixinType( ModelerLexicon.UNSTRUCTURED_MIXIN, props );
        assertThat( obj.mixinTypes().length, is( mixins + 1 ) );
        assertThat( obj.hasProperty( ClassFileSequencerLexicon.VISIBILITY ), is( true ) );
        assertThat( obj.stringValue( ClassFileSequencerLexicon.VISIBILITY ), is( "public" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildIfEmptyAdditionalName() throws Exception {
        modelObject().addChild( "blah", " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildIfNullAdditionalName() throws Exception {
        modelObject().addChild( "blah", ( String ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildOfTypeIfEmptyAdditionalName() throws Exception {
        modelObject().addChildOfType( null, "blah", " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildOfTypeIfInvalidType() throws Exception {
        modelObject().addChildOfType( "blah", "blah" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildOfTypeIfMissingMandatoryProperties() throws Exception {
        modelObject().addChildOfType( ClassFileSequencerLexicon.CLASS, "blah" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildOfTypeIfNullAdditionalName() throws Exception {
        modelObject().addChildOfType( null, "blah", ( String ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildOfTypeWithPropertiesIfInvalidType() throws Exception {
        modelObject().addChildOfType( "blah", "blah", NULL_VALUES_BY_PROPERTY );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildOfTypeWithPropertiesIfMissingMandatoryProperties() throws Exception {
        modelObject().addChildOfType( ClassFileSequencerLexicon.CLASS, "blah", NULL_VALUES_BY_PROPERTY );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddMixinTypeIfEmptyAdditionalName() throws Exception {
        modelObject().addMixinType( "blah", " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddMixinTypeIfInvalidType() throws Exception {
        modelObject().addMixinType( "blah" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddMixinTypeIfMissingMandatoryProperties() throws Exception {
        modelObject().addMixinType( ClassFileSequencerLexicon.CLASS );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddMixinTypeIfNullAdditionalName() throws Exception {
        modelObject().addMixinType( "blah", ( String ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddMixinTypeWithPropertiesIfInvalidType() throws Exception {
        modelObject().addMixinType( "blah", NULL_VALUES_BY_PROPERTY );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddMixinTypeWithPropertiesIfMissingMandatoryProperties() throws Exception {
        modelObject().addMixinType( ClassFileSequencerLexicon.CLASS, NULL_VALUES_BY_PROPERTY );
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
    public void shouldFailToRemoveMixinTypeIfEmptyAdditionalName() throws Exception {
        modelObject().removeMixinType( "blah", " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToRemoveMixinTypeIfNullAdditionalName() throws Exception {
        modelObject().removeMixinType( "blah", ( String ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetMixinTypeIfInvalidType() throws Exception {
        modelObject().setMixinType( ClassFileSequencerLexicon.CLASS, NULL_VALUES_BY_PROPERTY );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetMixinTypesIfInvalidType() throws Exception {
        modelObject().setMixinTypes( ClassFileSequencerLexicon.CLASS );
    }

    @Test
    public void shouldFailToSetMixinTypesIfMissingMandatoryProperties() {
        // TODO The Java CND doesn't define any mixin types with mandatory properties
        // modelObject().setMixinTypes( ClassFileSequencerLexicon.? );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetMixinTypesIfNonExistingType() throws Exception {
        modelObject().setMixinTypes( "blah" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetPrimaryTypeIfInvalidType() throws Exception {
        modelObject().setPrimaryType( ClassFileSequencerLexicon.PACKAGE );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetPrimaryTypeIfMissingMandatoryProperties() throws Exception {
        modelObject().setPrimaryType( ClassFileSequencerLexicon.CLASS );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetPrimaryTypeIfNonExistingType() throws Exception {
        modelObject().setPrimaryType( "blah" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetPrimaryTypeWithPropertiesIfInvalidType() throws Exception {
        modelObject().setPrimaryType( ClassFileSequencerLexicon.PACKAGE, NULL_VALUES_BY_PROPERTY );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetPropertyIfAdditionalValuesNullArray() throws Exception {
        modelObject().setProperty( "blah", null, ( Object[] ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToSetPropertyIfNotMultiValuedProperty() throws Exception {
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
    public void shouldRemoveMixinType() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.mixinTypes().length, is( 1 ) );
        obj.addMixinType( ModelerLexicon.UNSTRUCTURED_MIXIN );
        assertThat( obj.mixinTypes().length, is( 2 ) );
        obj.removeMixinType( ClassFileSequencerLexicon.PACKAGE, ModelerLexicon.UNSTRUCTURED_MIXIN );
        assertThat( obj.mixinTypes().length, is( 0 ) );
    }

    @Test
    public void shouldSetMixinTypes() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.mixinTypes().length, not( 0 ) );
        obj.setMixinTypes();
        assertThat( obj.mixinTypes().length, is( 0 ) );
        obj.setMixinTypes( ClassFileSequencerLexicon.PACKAGE, ModelerLexicon.Model.MODEL_MIXIN );
        assertThat( obj.mixinTypes().length, is( 2 ) );
        obj.setMixinTypes();
        assertThat( obj.mixinTypes().length, is( 0 ) );
    }

    @Test
    public void shouldSetMixinTypeWithProperties() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.mixinTypes().length, not( 0 ) );
        obj.setMixinType( null, NULL_VALUES_BY_PROPERTY );
        assertThat( obj.mixinTypes().length, is( 0 ) );
        final Map< String, ? > props = primaryTypeProperties();
        obj.setMixinType( ClassFileSequencerLexicon.PACKAGE, props );
        String[] mixinTypes = obj.mixinTypes();
        assertThat( mixinTypes.length, not( 0 ) );
        assertThat( mixinTypes[ 0 ], is( ClassFileSequencerLexicon.PACKAGE ) );
        assertThat( obj.hasProperty( ClassFileSequencerLexicon.VISIBILITY ), is( true ) );
        assertThat( obj.stringValue( ClassFileSequencerLexicon.VISIBILITY ), is( "public" ) );
        final ModelObject child = obj.child( "modeshape" );
        child.setMixinType( null, props );
        assertThat( child.mixinTypes().length, is( 0 ) );
        mixinTypes = child.mixinTypes();
        assertThat( mixinTypes.length, is( 0 ) );
        assertThat( obj.hasProperty( ClassFileSequencerLexicon.VISIBILITY ), is( true ) );
        assertThat( obj.stringValue( ClassFileSequencerLexicon.VISIBILITY ), is( "public" ) );
    }

    @Test
    public void shouldSetPrimaryType() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.primaryType(), is( JcrNtLexicon.UNSTRUCTURED.getString() ) );
        obj.setPrimaryType( ClassFileSequencerLexicon.ANNOTATIONS );
        assertThat( obj.primaryType(), is( ClassFileSequencerLexicon.ANNOTATIONS ) );
        obj.setPrimaryType( null );
        assertThat( obj.primaryType(), is( JcrNtLexicon.UNSTRUCTURED.getString() ) );
        obj.setPrimaryType( " " );
        assertThat( obj.primaryType(), is( JcrNtLexicon.UNSTRUCTURED.getString() ) );
    }

    @Test
    public void shouldSetPrimaryTypeWithProperties() throws Exception {
        final ModelObject obj = modelObject();
        assertThat( obj.primaryType(), is( JcrNtLexicon.UNSTRUCTURED.getString() ) );
        final Map< String, ? > props = primaryTypeProperties();
        obj.setPrimaryType( ClassFileSequencerLexicon.CLASS, props );
        assertThat( obj.primaryType(), is( ClassFileSequencerLexicon.CLASS ) );
        assertThat( obj.hasProperty( ClassFileSequencerLexicon.VISIBILITY ), is( true ) );
        assertThat( obj.stringValue( ClassFileSequencerLexicon.VISIBILITY ), is( "public" ) );

        obj.setPrimaryType( null, props );
        assertThat( obj.primaryType(), is( JcrNtLexicon.UNSTRUCTURED.getString() ) );
        obj.setPrimaryType( " ", props );
        assertThat( obj.primaryType(), is( JcrNtLexicon.UNSTRUCTURED.getString() ) );
    }

    @Test
    public void shouldSetProperty() throws Exception {
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
        obj.setProperty( "blah", ( Long ) null );
        assertThat( obj.hasProperty( "blah" ), is( false ) );
        obj.setProperty( "blah", 0L, 0L );
        assertThat( obj.longValues( "blah" ), is( new Long[] { 0L, 0L } ) );
        obj.setProperty( "blah", ( Long ) null );
        assertThat( obj.hasProperty( "blah" ), is( false ) );
    }
}
