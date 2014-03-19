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
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.modeshape.jcr.JcrLexicon;
import org.modeshape.modeler.ModelObject;

@SuppressWarnings( "javadoc" )
public class ModelObjectImplTest extends BaseModelObjectImplTest {
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.internal.BaseModelObjectImplTest#modelObject()
     */
    @Override
    protected ModelObject modelObject() throws Exception {
        return super.modelObject().child( XML_ROOT );
    }
    
    @Test
    public void shouldGetAbsolutePath() throws Exception {
        assertThat( modelObject().absolutePath(), is( '/' + MODEL_NAME + '/' + XML_ROOT ) );
    }
    
    @Test
    public void shouldGetChild() throws Exception {
        final ModelObject child = modelObject().child( XML_LEAF );
        assertThat( child, notNullValue() );
        assertThat( child.name(), is( XML_LEAF ) );
    }
    
    @Test
    public void shouldGetChildren() throws Exception {
        ModelObject[] children = modelObject().children();
        assertThat( children, notNullValue() );
        assertThat( children.length, is( 3 ) );
        children = modelObject().child( XML_LEAF ).children();
        assertThat( children, notNullValue() );
        assertThat( children.length, is( 0 ) );
    }
    
    @Test
    public void shouldGetChildrenMatchingPattern() throws Exception {
        final ModelObject[] children = modelObject().children( XML_SAME_NAME_SIBLING );
        assertThat( children, notNullValue() );
        assertThat( children.length, is( 2 ) );
        assertThat( children[ 0 ].name(), is( XML_SAME_NAME_SIBLING ) );
        assertThat( children[ 1 ].name(), is( XML_SAME_NAME_SIBLING ) );
    }
    
    @Test
    public void shouldGetIndex() throws Exception {
        assertThat( modelObject().index(), is( 0 ) );
        final ModelObject[] objs = modelObject().children( XML_SAME_NAME_SIBLING );
        assertThat( objs[ 0 ].index(), is( 0 ) );
        assertThat( objs[ 1 ].index(), is( 1 ) );
    }
    
    @Test
    public void shouldGetMixinTypes() throws Exception {
        final String[] types = modelObject().mixinTypes();
        assertThat( types, notNullValue() );
        assertThat( types.length, is( 0 ) );
    }
    
    @Test
    public void shouldGetModel() throws Exception {
        assertThat( modelObject().model(), notNullValue() );
    }
    
    @Test
    public void shouldGetModelPath() throws Exception {
        assertThat( modelObject().modelRelativePath(), is( XML_ROOT ) );
    }
    
    @Test
    public void shouldGetName() throws Exception {
        assertThat( modelObject().name(), is( XML_ROOT ) );
    }
    
    @Test
    public void shouldGetPrimaryType() throws Exception {
        assertThat( modelObject().primaryType(), is( "modexml:element" ) );
    }
    
    @Test
    public void shouldGetPropertyNames() throws Exception {
        assertThat( modelObject().propertyNames().length, is( 1 ) );
        assertThat( modelObject().child( XML_LEAF ).propertyNames().length, is( 0 ) );
    }
    
    @Test
    public void shouldGetStringValue() throws Exception {
        assertThat( modelObject().stringValue( XML_ROOT_PROPERTY ), is( XML_STRING_VALUE ) );
    }
    
    @Test
    public void shouldIndicateIfChildHasSameNameSiblings() throws Exception {
        assertThat( modelObject().childHasSameNameSiblings( XML_LEAF ), is( false ) );
        assertThat( modelObject().childHasSameNameSiblings( XML_SAME_NAME_SIBLING ), is( true ) );
        assertThat( modelObject().childHasSameNameSiblings( "bogus" ), is( false ) );
    }
    
    @Test
    public void shouldIndicateIfItHasChild() throws Exception {
        assertThat( modelObject().hasChild( XML_LEAF ), is( true ) );
        assertThat( modelObject().hasChild( "bogus" ), is( false ) );
    }
    
    @Test
    public void shouldIndicateIfItHasChildren() throws Exception {
        assertThat( modelObject().hasChildren(), is( true ) );
        assertThat( modelObject().child( XML_LEAF ).hasChildren(), is( false ) );
    }
    
    @Test
    public void shouldIndicateIfItHasProperties() throws Exception {
        assertThat( modelObject().hasProperties(), is( true ) );
        assertThat( modelObject().child( XML_LEAF ).hasProperties(), is( false ) );
    }
    
    @Test
    public void shouldIndicateIfItHasProperty() throws Exception {
        assertThat( modelObject().hasProperty( XML_ROOT_PROPERTY ), is( true ) );
        assertThat( modelObject().hasProperty( "bogus" ), is( false ) );
    }
    
    @Test
    public void shouldIndicateIfPropertyHasMultipleValues() throws Exception {
        assertThat( modelObject().propertyHasMultipleValues( JcrLexicon.PRIMARY_TYPE.toString() ), is( false ) );
        assertThat( modelObject().propertyHasMultipleValues( JcrLexicon.MIXIN_TYPES.toString() ), is( false ) );
        assertThat( modelObject().propertyHasMultipleValues( "bogus" ), is( false ) );
    }
}
