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
package org.modelspace.internal;

import static org.mockito.Mockito.mock;

import java.util.Map;

import org.junit.Test;
import org.modelspace.ModelObject;
import org.modelspace.internal.ModelObjectImpl;
import org.modelspace.internal.ModelspaceImpl;
import org.modelspace.test.BaseTest;

@SuppressWarnings( "javadoc" )
public class ModelObjectImplTest extends BaseTest {

    Map< String, ? > NULL_VALUES_BY_PROPERTY = null;

    protected ModelObject modelObject() {
        return new ModelObjectImpl( mock( ModelspaceImpl.class ), "object", 0 );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailPropertyNameIsEmpty() throws Exception {
        modelObject().property( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildIfEmptyName() throws Exception {
        modelObject().addChild( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildIfNullName() throws Exception {
        modelObject().addChild( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildOfTypeIfEmptyName() throws Exception {
        modelObject().addChildOfType( null, " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildOfTypeIfNullName() throws Exception {
        modelObject().addChildOfType( null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildOfTypeWithPropertiesIfEmptyName() throws Exception {
        modelObject().addChildOfType( null, " ", NULL_VALUES_BY_PROPERTY );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddChildOfTypeWithPropertiesIfNullName() throws Exception {
        modelObject().addChildOfType( null, null, NULL_VALUES_BY_PROPERTY );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddMixingTypeIfNullName() throws Exception {
        modelObject().addMixinType( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddMixingTypeWithPropertiesIfNullName() throws Exception {
        modelObject().addMixinType( null, NULL_VALUES_BY_PROPERTY );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddMixinTypeIfEmptyName() throws Exception {
        modelObject().addMixinType( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToAddMixinTypeWithPropertiesIfEmptyName() throws Exception {
        modelObject().addMixinType( " ", NULL_VALUES_BY_PROPERTY );
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
    public void shouldFailToRemoveChildIfEmptyName() throws Exception {
        modelObject().removeChild( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToRemoveChildIfNullName() throws Exception {
        modelObject().removeChild( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailWhenFindingChildrenIfEmptyPrimaryType() throws Exception {
        modelObject().childrenOfType( " " );
        modelObject().childrenOfType( "" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailWhenFindingChildrenIfNullPrimaryType() throws Exception {
        modelObject().childrenOfType( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailWhenPropertyNameIsNull() throws Exception {
        modelObject().property( null );
    }
}
