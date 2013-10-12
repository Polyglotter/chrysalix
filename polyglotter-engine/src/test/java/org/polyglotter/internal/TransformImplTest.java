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
import org.polyglotter.Operation;
import org.polyglotter.test.BaseTest;

@SuppressWarnings( "javadoc" )
public class TransformImplTest extends BaseTest {

    private TransformImpl xform;

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.test.BaseTest#before()
     */
    @Override
    public void before() throws Exception {
        super.before();
        xform = new TransformImpl();
    }

    @Test
    public void shouldAddOperation() {
        final Operation op = mock( Operation.class );
        xform.add( op );
        assertThat( xform.operations()[ 0 ], is( op ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailIfAddOperationWithNullOperation() {
        xform.add( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailIfRemoveOperationWithNullOperation() {
        xform.remove( null );
    }

    @Test
    public void shouldGetEmptyOperations() {
        assertThat( xform.operations(), notNullValue() );
        assertThat( xform.operations().length == 0, is( true ) );
    }

    @Test
    public void shouldRemoveOperation() {
        final Operation op = mock( Operation.class );
        xform.add( op );
        xform.remove( op );
        assertThat( xform.operations().length == 0, is( true ) );
    }
}
