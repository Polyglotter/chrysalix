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
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.chrysalix.transformation.TransformationTestFactory;
import org.chrysalix.transformation.Value;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.modelspace.ModelObject;

@Ignore
@SuppressWarnings( { "javadoc", "unchecked" } )
public final class ModeTest {

    private static final String ID = Mode.TERM_DESCRIPTOR.name();
    private static TransformationTestFactory FACTORY;

    private static Value< Number > DOUBLE_TERM;
    private static Value< Number > DOUBLE2_TERM;
    private static Value< Number > DOUBLE3_TERM;
    private static Value< Number > FLOAT_TERM;
    private static Value< Number > FLOAT2_TERM;
    private static Value< Number > INT_TERM;
    private static Value< Number > INT2_TERM;
    private static Value< Number > INT3_TERM;

    @BeforeClass
    public static void initializeConstants() throws Exception {
        FACTORY = new TransformationTestFactory();
        DOUBLE_TERM = FACTORY.createNumberValue( "/my/path/double", Mode.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_1_VALUE );
        DOUBLE2_TERM = FACTORY.createNumberValue( "/my/path/double2", Mode.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_2_VALUE );
        DOUBLE3_TERM = FACTORY.createNumberValue( "/my/path/double3", Mode.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_3_VALUE );
        FLOAT_TERM = FACTORY.createNumberValue( "/my/path/float", Mode.TERM_DESCRIPTOR, OperationTestConstants.FLOAT_1_VALUE );
        FLOAT2_TERM = FACTORY.createNumberValue( "/my/path/float2", Mode.TERM_DESCRIPTOR, OperationTestConstants.FLOAT_2_VALUE );
        INT_TERM = FACTORY.createNumberValue( "/my/path/int", Mode.TERM_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        INT2_TERM = FACTORY.createNumberValue( "/my/path/int2", Mode.TERM_DESCRIPTOR, OperationTestConstants.INT_2_VALUE );
        INT3_TERM = FACTORY.createNumberValue( "/my/path/int3", Mode.TERM_DESCRIPTOR, OperationTestConstants.INT_3_VALUE );
    }

    private ModelObject modelObject;
    private Mode operation;

    @Before
    public void beforeEach() throws Exception {
        this.modelObject = mock( ModelObject.class );
        this.operation = new Mode( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddOneTerm() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM );
        assertThat( this.operation.inputs().length, is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs()[ 0 ], is( INT_TERM ) );
    }

    @Test
    public void shouldAllowResultWithOneTerm() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM );
        this.operation.get();
    }

    @Test
    public void shouldCreateOperation() throws Exception {
        assertThat( Mode.DESCRIPTOR.newInstance( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( Mode.class ) ) );
    }

    @Test
    public void shouldFindOneNumber() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM,
                                 INT2_TERM,
                                 DOUBLE_TERM,
                                 DOUBLE2_TERM,
                                 FLOAT_TERM,
                                 FLOAT2_TERM,
                                 INT3_TERM );
        final Number[] result = this.operation.get();
        assertThat( result.length, is( 1 ) );
        assertThat( result[ 0 ], is( ( Number ) INT_TERM.get().intValue() ) );
    }

    @Test
    public void shouldFindTwoNumbers() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM,
                                 INT2_TERM,
                                 DOUBLE_TERM,
                                 DOUBLE2_TERM,
                                 FLOAT_TERM,
                                 FLOAT2_TERM,
                                 INT3_TERM,
                                 DOUBLE3_TERM );
        final Number[] result = this.operation.get();
        assertThat( result.length, is( 2 ) );
        assertThat( Arrays.asList( result ), hasItems( INT_TERM.get(), DOUBLE_TERM.get() ) );
    }

    @Test
    public void shouldHaveErrorsAfterConstruction() throws Exception {
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenStringTerm() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM, INT2_TERM ); // will get rid of current problems
        this.operation.addInput( ID, OperationTestConstants.STRING_1_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveProblemsAfterConstruction() throws Exception {
        assertThat( this.operation.problems().isEmpty(), is( false ) );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldNotBeAbleToGetResultAfterConstruction() throws ChrysalixException {
        this.operation.get();
    }

    @Test
    public void shouldNotFindAMode() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM,
                                 INT2_TERM,
                                 DOUBLE_TERM,
                                 DOUBLE2_TERM,
                                 FLOAT_TERM,
                                 FLOAT2_TERM );
        final Number[] result = this.operation.get();
        assertThat( result.length, is( 0 ) );
    }

    @Test
    public void shouldNotHaveTermsAfterConstruction() throws Exception {
        assertThat( this.operation.inputs().length, is( 0 ) );
    }

    @Test
    public void shouldProvideDescription() throws Exception {
        assertThat( this.operation.descriptor().description(), is( ChrysalixI18n.localize( Mode.DESCRIPTION ) ) );
    }

    @Test
    public void shouldProvideName() throws Exception {
        assertThat( this.operation.name(), is( ChrysalixI18n.localize( Mode.NAME ) ) );
    }

}
