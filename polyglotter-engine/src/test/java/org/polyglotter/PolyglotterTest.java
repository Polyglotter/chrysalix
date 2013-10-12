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
package org.polyglotter;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.modeshape.modeler.Model;
import org.modeshape.modeler.ModelObject;
import org.polyglotter.test.BaseTest;

@SuppressWarnings( "javadoc" )
public class PolyglotterTest extends BaseTest {

    private void mockPropertyValuesByName( final ModelObject object ) throws Exception {
        when( object.propertyNames() ).thenReturn( new String[] { "prop1", "prop2" } );
    }

    @Test
    public void shouldCreateCloneModelTransform() throws Exception {
        final Model sourceModel = mock( Model.class );
        mockPropertyValuesByName( sourceModel );
        final ModelObject child1 = mock( ModelObject.class );
        mockPropertyValuesByName( child1 );
        when( child1.children() ).thenReturn( new ModelObject[ 0 ] );
        final ModelObject child2 = mock( ModelObject.class );
        mockPropertyValuesByName( child2 );
        when( child2.children() ).thenReturn( new ModelObject[ 0 ] );
        when( sourceModel.children() ).thenReturn( new ModelObject[] { child1, child2 } );
        final ModelTransform xform = polyglotter().newCloneModelTransform( sourceModel, "target" );
        assertThat( xform, notNullValue() );
        assertThat( xform.transforms().length, is( 9 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToCreateCloneModelTransformIfSourceNull() throws Exception {
        polyglotter().newCloneModelTransform( null, "target" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToCreateCloneModelTransformIfTargetEmpty() throws Exception {
        polyglotter().newCloneModelTransform( mock( Model.class ), " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToCreateCloneModelTransformIfTargetNull() throws Exception {
        polyglotter().newCloneModelTransform( mock( Model.class ), null );
    }
}
