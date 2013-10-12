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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.polyglotter.Transform;
import org.polyglotter.test.BaseTest;

@SuppressWarnings( "javadoc" )
public class ModelTransformImplTest extends BaseTest {

    private ModelTransformImpl modelXform;

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.test.BaseTest#before()
     */
    @Override
    public void before() throws Exception {
        super.before();
        modelXform = new ModelTransformImpl();
    }

    @Test
    public void shouldAddTransform() {
        final Transform xform = mock( Transform.class );
        modelXform.add( xform );
        assertThat( modelXform.transforms()[ 0 ], is( xform ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailIfAddTransformWithNullTransform() {
        modelXform.add( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailIfRemoveTransformWithNullTransform() {
        modelXform.remove( null );
    }

    @Test
    public void shouldGetEmptyTransforms() {
        assertThat( modelXform.transforms(), notNullValue() );
        assertThat( modelXform.transforms().length == 0, is( true ) );
    }

    @Test
    public void shouldRemoveTransform() {
        final Transform xform = mock( Transform.class );
        modelXform.add( xform );
        modelXform.remove( xform );
        assertThat( modelXform.transforms().length == 0, is( true ) );
    }
}
