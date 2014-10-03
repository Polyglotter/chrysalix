/*
 * Chrysalix
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * Chrysalix is free software. Unless otherwise indicated, all code in Chrysalix
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * Chrysalix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.chrysalix.operation;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.modelspace.ModelObject;

@Ignore
@SuppressWarnings( { "javadoc" } )
public final class RandomTest {

    private ModelObject modelObject;
    private Random operation;

    @Before
    public void beforeEach() throws Exception {
        this.modelObject = mock( ModelObject.class );
        this.operation = new Random( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldBeAbleToGetResultAfterConstruction() throws ChrysalixException {
        this.operation.get();
    }

    @Test
    public void shouldCreateOperation() throws Exception {
        assertThat( Random.DESCRIPTOR.newInstance( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( Random.class ) ) );
    }

    @Test
    public void shouldNotHaveErrorsAfterConstruction() throws Exception {
        assertThat( this.operation.problems().isError(), is( false ) );
    }

    @Test
    public void shouldNotHaveProblemsAfterConstruction() throws Exception {
        assertThat( this.operation.problems().isEmpty(), is( true ) );
    }

    @Test
    public void shouldNotHaveTermsAfterConstruction() throws Exception {
        assertThat( this.operation.inputs().length, is( 0 ) );
    }

    @Test
    public void shouldProvideDescription() throws Exception {
        assertThat( this.operation.descriptor().description(), is( ChrysalixI18n.localize( Random.DESCRIPTION ) ) );
    }

    @Test
    public void shouldProvideName() throws Exception {
        assertThat( this.operation.name(), is( ChrysalixI18n.localize( Random.NAME ) ) );
    }

}
