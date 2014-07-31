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

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.modeshape.modeler.ModelObject;
import org.modeshape.modeler.test.BaseTest;

@SuppressWarnings( "javadoc" )
public class ModelObjectImplTest extends BaseTest {

    protected ModelObject modelObject() {
        return new ModelObjectImpl( mock( Manager.class ), "object", 0 );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetBooleanValueIfPropertyEmpty() throws Exception {
        modelObject().booleanValue( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetBooleanValueIfPropertyNull() throws Exception {
        modelObject().booleanValue( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetBooleanValuesIfPropertyEmpty() throws Exception {
        modelObject().booleanValues( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetBooleanValuesIfPropertyNull() throws Exception {
        modelObject().booleanValues( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetChildIfNameEmpty() throws Exception {
        modelObject().child( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetChildIfNameNull() throws Exception {
        modelObject().child( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetChildrenIfNameEmpty() throws Exception {
        modelObject().children( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetChildrenIfNameNull() throws Exception {
        modelObject().children( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetLongValueIfPropertyEmpty() throws Exception {
        modelObject().longValue( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetLongValueIfPropertyNull() throws Exception {
        modelObject().longValue( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetLongValuesIfPropertyEmpty() throws Exception {
        modelObject().longValues( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetLongValuesIfPropertyNull() throws Exception {
        modelObject().longValues( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetStringValueIfPropertyEmpty() throws Exception {
        modelObject().stringValue( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetStringValueIfPropertyNull() throws Exception {
        modelObject().stringValue( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetStringValuesIfPropertyEmpty() throws Exception {
        modelObject().stringValues( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetStringValuesIfPropertyNull() throws Exception {
        modelObject().stringValues( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToIndicateIfChildHasSameNameSiblingsIfNameEmpty() throws Exception {
        modelObject().childHasSameNameSiblings( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToIndicateIfChildHasSameNameSiblingsIfNameNull() throws Exception {
        modelObject().childHasSameNameSiblings( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToIndicateIfItHasChildIfEmptyName() throws Exception {
        modelObject().hasChild( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToIndicateIfItHasChildIfNullName() throws Exception {
        modelObject().hasChild( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToIndicateIfItHasPropertyIfEmptyName() throws Exception {
        modelObject().hasProperty( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToIndicateIfItHasPropertyIfNullName() throws Exception {
        modelObject().hasProperty( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToIndicateIfPropertyHasMultipleValuesIfNameEmpty() throws Exception {
        modelObject().propertyHasMultipleValues( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToIndicateIfPropertyHasMultipleValuesIfNameNull() throws Exception {
        modelObject().propertyHasMultipleValues( null );
    }
}
