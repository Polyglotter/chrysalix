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
import org.chrysalix.transformation.TransformationTestFactory;
import org.chrysalix.transformation.Value;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.modelspace.ModelObject;

@Ignore
@SuppressWarnings( { "javadoc", "unchecked" } )
public final class PowerTest {

    private static final String BASE_ID = Power.BASE_DESCRIPTOR.name();
    private static final String EXP_ID = Power.EXPONENT_DESCRIPTOR.name();
    private static TransformationTestFactory FACTORY;

    private static Value< Number > BASE_DOUBLE_TERM;
    private static Value< Number > BASE_FLOAT_TERM;
    private static Value< Number > BASE_INT_TERM;
    private static Value< Number > EXP_DOUBLE_TERM;
    private static Value< Number > EXP_FLOAT_TERM;
    private static Value< Number > EXP_INT_TERM;

    @BeforeClass
    public static void initializeConstants() throws Exception {
        FACTORY = new TransformationTestFactory();
        BASE_DOUBLE_TERM =
            FACTORY.createNumberValue( "/my/path/base/double", Power.BASE_DESCRIPTOR, OperationTestConstants.DOUBLE_1_VALUE );
        BASE_FLOAT_TERM =
            FACTORY.createNumberValue( "/my/path/base/float", Power.BASE_DESCRIPTOR, OperationTestConstants.FLOAT_1_VALUE );
        BASE_INT_TERM = FACTORY.createNumberValue( "/my/path/base/int", Power.BASE_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        EXP_DOUBLE_TERM =
            FACTORY.createNumberValue( "/my/path/exp/double", Power.EXPONENT_DESCRIPTOR, OperationTestConstants.DOUBLE_2_VALUE );
        EXP_FLOAT_TERM =
            FACTORY.createNumberValue( "/my/path/exp/float", Power.EXPONENT_DESCRIPTOR, OperationTestConstants.FLOAT_2_VALUE );
        EXP_INT_TERM =
            FACTORY.createNumberValue( "/my/path/exp/int", Power.EXPONENT_DESCRIPTOR, OperationTestConstants.INT_2_VALUE );
    }

    private ModelObject modelObject;
    private Power operation;

    @Before
    public void beforeEach() throws Exception {
        this.modelObject = mock( ModelObject.class );
        this.operation = new Power( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddTwoinputs() throws ChrysalixException {
        this.operation.addInput( BASE_ID, BASE_INT_TERM );
        this.operation.addInput( EXP_ID, EXP_INT_TERM );
        assertThat( this.operation.inputs().length, is( 2 ) );
        assertThat( this.operation.inputs( BASE_ID ).size(), is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs( BASE_ID ).get( 0 ), is( BASE_INT_TERM ) );
        assertThat( this.operation.inputs( EXP_ID ).size(), is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs( EXP_ID ).get( 0 ), is( EXP_INT_TERM ) );
    }

    @Test
    public void shouldCalculateDoubleinputs() throws ChrysalixException {
        this.operation.addInput( BASE_ID, BASE_DOUBLE_TERM );
        this.operation.addInput( EXP_ID, EXP_DOUBLE_TERM );
        assertThat( this.operation.get(),
                    is( ( Number ) Math.pow( BASE_DOUBLE_TERM.get().doubleValue(), EXP_DOUBLE_TERM.get().doubleValue() ) ) );
    }

    @Test
    public void shouldCalculateFloatinputs() throws ChrysalixException {
        this.operation.addInput( BASE_ID, BASE_FLOAT_TERM );
        this.operation.addInput( EXP_ID, EXP_FLOAT_TERM );
        assertThat( this.operation.get(),
                    is( ( Number ) Math.pow( BASE_FLOAT_TERM.get().floatValue(), EXP_FLOAT_TERM.get().floatValue() ) ) );
    }

    @Test
    public void shouldCreateOperation() throws Exception {
        assertThat( Power.DESCRIPTOR.newInstance( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( Power.class ) ) );
    }

    @Test
    public void shouldHaveErrorsAfterConstruction() throws Exception {
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenBaseIsNotANumber() throws ChrysalixException {
        this.operation.addInput( BASE_ID, OperationTestConstants.STRING_1_TERM );
        this.operation.addInput( EXP_ID, EXP_DOUBLE_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenExponentIsNotANumber() throws ChrysalixException {
        this.operation.addInput( BASE_ID, BASE_INT_TERM );
        this.operation.addInput( EXP_ID, OperationTestConstants.STRING_1_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenMoreThanOneBase() throws ChrysalixException {
        this.operation.addInput( BASE_ID, BASE_DOUBLE_TERM );
        this.operation.addInput( BASE_ID, BASE_INT_TERM );
        this.operation.addInput( EXP_ID, EXP_DOUBLE_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenMoreThanOneExponent() throws ChrysalixException {
        this.operation.addInput( BASE_ID, BASE_INT_TERM );
        this.operation.addInput( EXP_ID, EXP_DOUBLE_TERM );
        this.operation.addInput( EXP_ID, EXP_FLOAT_TERM );
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
    public void shouldNotHaveTermsAfterConstruction() throws Exception {
        assertThat( this.operation.inputs().length, is( 0 ) );
    }

    @Test
    public void shouldProvideDescription() throws Exception {
        assertThat( this.operation.descriptor().description(), is( ChrysalixI18n.localize( Power.DESCRIPTION ) ) );
    }

    @Test
    public void shouldProvideName() throws Exception {
        assertThat( this.operation.name(), is( ChrysalixI18n.localize( Power.NAME ) ) );
    }

}
