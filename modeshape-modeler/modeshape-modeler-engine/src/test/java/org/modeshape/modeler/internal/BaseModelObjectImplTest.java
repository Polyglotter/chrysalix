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

import org.modeshape.modeler.test.BaseTest;

@SuppressWarnings( "javadoc" )
public abstract class BaseModelObjectImplTest extends BaseTest {

    // protected ModelObject failingModelObject() {
    // return new ModelObjectImpl( mock( Manager.class ), XML_ROOT, 0 );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetBooleanValueIfPropertyEmpty() throws Exception {
    // failingModelObject().booleanValue( " " );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetBooleanValueIfPropertyNull() throws Exception {
    // failingModelObject().booleanValue( null );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetBooleanValuesIfNonBooleanProperty() throws Exception {
    // modelObject().booleanValues( JcrLexicon.PRIMARY_TYPE.toString() );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetBooleanValuesIfPropertyEmpty() throws Exception {
    // failingModelObject().booleanValues( " " );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetBooleanValuesIfPropertyNull() throws Exception {
    // failingModelObject().booleanValues( null );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetChildIfNameEmpty() throws Exception {
    // failingModelObject().child( " " );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetChildIfNameNull() throws Exception {
    // failingModelObject().child( null );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetChildrenIfNameEmpty() throws Exception {
    // failingModelObject().children( " " );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetChildrenIfNameNull() throws Exception {
    // failingModelObject().children( null );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetLongValueIfNonLongProperty() throws Exception {
    // modelObject().longValue( JcrLexicon.PRIMARY_TYPE.toString() );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetLongValueIfPropertyEmpty() throws Exception {
    // failingModelObject().longValue( " " );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetLongValueIfPropertyNull() throws Exception {
    // failingModelObject().longValue( null );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetLongValuesIfNonLongProperty() throws Exception {
    // modelObject().longValues( JcrLexicon.PRIMARY_TYPE.toString() );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetLongValuesIfPropertyEmpty() throws Exception {
    // failingModelObject().longValues( " " );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetLongValuesIfPropertyNull() throws Exception {
    // failingModelObject().longValues( null );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetStringValueIfPropertyEmpty() throws Exception {
    // failingModelObject().stringValue( " " );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetStringValueIfPropertyNull() throws Exception {
    // failingModelObject().stringValue( null );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetStringValuesIfPropertyEmpty() throws Exception {
    // failingModelObject().stringValues( " " );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToGetStringValuesIfPropertyNull() throws Exception {
    // failingModelObject().stringValues( null );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToIndicateIfChildHasSameNameSiblingsIfNameEmpty() throws Exception {
    // failingModelObject().childHasSameNameSiblings( " " );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToIndicateIfChildHasSameNameSiblingsIfNameNull() throws Exception {
    // failingModelObject().childHasSameNameSiblings( null );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToIndicateIfItHasChildIfEmptyName() throws Exception {
    // failingModelObject().hasChild( " " );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToIndicateIfItHasChildIfNullName() throws Exception {
    // failingModelObject().hasChild( null );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToIndicateIfItHasPropertyIfEmptyName() throws Exception {
    // failingModelObject().hasProperty( " " );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToIndicateIfItHasPropertyIfNullName() throws Exception {
    // failingModelObject().hasProperty( null );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToIndicateIfPropertyHasMultipleValuesIfNameEmpty() throws Exception {
    // failingModelObject().propertyHasMultipleValues( " " );
    // }
    //
    // @Test( expected = IllegalArgumentException.class )
    // public void shouldFailToIndicateIfPropertyHasMultipleValuesIfNameNull() throws Exception {
    // failingModelObject().propertyHasMultipleValues( null );
    // }
    //
    // @Test
    // public void shouldGetNullValueIfBooleanPropertyNotFound() throws Exception {
    // assertThat( modelObject().booleanValue( "bogus" ), nullValue() );
    // }
    //
    // @Test
    // public void shouldGetNullValueIfChildNotFound() throws Exception {
    // assertThat( modelObject().child( "bogus" ), nullValue() );
    // }
    //
    // @Test
    // public void shouldGetNullValueIfLongPropertyNotFound() throws Exception {
    // assertThat( modelObject().longValue( "bogus" ), nullValue() );
    // }
    //
    // @Test
    // public void shouldGetNullValueIfStringPropertyNotFound() throws Exception {
    // assertThat( modelObject().stringValue( "bogus" ), nullValue() );
    // }
    //
    // @Test
    // public void shouldGetStringValues() throws Exception {
    // final String[] vals = modelObject().stringValues( JcrLexicon.PRIMARY_TYPE.toString() );
    // assertThat( vals, notNullValue() );
    // assertThat( vals.length, is( 1 ) );
    // }
}
