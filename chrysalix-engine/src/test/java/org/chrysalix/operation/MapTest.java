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
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.chrysalix.ChrysalixException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.modelspace.ModelObject;
import org.modelspace.ModelProperty;

@Ignore
@SuppressWarnings( { "javadoc" } )
public final class MapTest {

    private static final String SOURCE_PROP_ID = Map.SOURCE_PROP_DESCRIPTOR.name();
    private static final String TARGET_PROP_ID = Map.TARGET_PROP_DESCRIPTOR.name();

    private ModelObject modelObject;
    private Map operation;

    private void addValidInputs() throws Exception {
        this.operation.addInput( SOURCE_PROP_ID, mock( ModelProperty.class ) );
        this.operation.addInput( TARGET_PROP_ID, mock( ModelProperty.class ) );
        assertThat( this.operation.problems().isEmpty(), is( true ) );
    }

    @Before
    public void beforeEach() throws Exception {
        this.modelObject = mock( ModelObject.class );
        this.operation = new Map( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailGettingResultAfterConstruction() throws Exception {
        this.operation.get();
    }

    @Test
    public void shouldHaveNullResult() throws Exception {
        addValidInputs();
        assertThat( this.operation.get(), is( nullValue() ) );
    }

    @Test
    public void shouldHaveProblemsAfterConstruction() throws Exception {
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveProblemWhenMoreThanOneSourceProperty() throws Exception {
        addValidInputs();
        this.operation.addInput( SOURCE_PROP_ID, "sourceProp2" );
        assertThat( this.operation.problems().size(), is( 1 ) );
    }

    @Test
    public void shouldHaveProblemWhenMoreThanOneTargetProperty() throws Exception {
        addValidInputs();
        this.operation.addInput( TARGET_PROP_ID, "targetProp2" );
        assertThat( this.operation.problems().size(), is( 1 ) );
    }

    @Test
    public void shouldHaveProblemWhenSourcePropertyHasWrongType() throws Exception {
        addValidInputs();
        this.operation.addInput( SOURCE_PROP_ID, true );
        assertThat( this.operation.problems().size(), is( 1 ) );
    }

    @Test
    public void shouldHaveProblemWhenTargetPropertyHasWrongType() throws Exception {
        addValidInputs();
        this.operation.addInput( TARGET_PROP_ID, 5 );
        assertThat( this.operation.problems().size(), is( 1 ) );
    }

}
