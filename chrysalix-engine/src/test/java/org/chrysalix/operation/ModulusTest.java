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
public final class ModulusTest {

    private static final String DIVIDEND_ID = Modulus.DIVIDEND_DESCRIPTOR.name();
    private static TransformationTestFactory FACTORY;

    private static final String DIVISOR_ID = Modulus.DIVISOR_DESCRIPTOR.name();
    private static Value< Number > DIVIDEND_DOUBLE_TERM;
    private static Value< Number > DIVIDEND_FLOAT_TERM;
    private static Value< Number > DIVIDEND_INT_TERM;
    private static Value< Number > DIVISOR_DOUBLE_TERM;
    private static Value< Number > DIVISOR_FLOAT_TERM;
    private static Value< Number > DIVISOR_INT_TERM;

    @BeforeClass
    public static void initializeConstants() throws Exception {
        FACTORY = new TransformationTestFactory();
        DIVIDEND_DOUBLE_TERM =
            FACTORY.createNumberValue( "/my/path/dividend/double", Modulus.DIVIDEND_DESCRIPTOR, OperationTestConstants.DOUBLE_1_VALUE );
        DIVIDEND_FLOAT_TERM =
            FACTORY.createNumberValue( "/my/path/dividend/float", Modulus.DIVIDEND_DESCRIPTOR, OperationTestConstants.FLOAT_1_VALUE );
        DIVIDEND_INT_TERM =
            FACTORY.createNumberValue( "/my/path/dividend/int", Modulus.DIVIDEND_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        DIVISOR_DOUBLE_TERM =
            FACTORY.createNumberValue( "/my/path/divisor/double", Modulus.DIVISOR_DESCRIPTOR, OperationTestConstants.DOUBLE_2_VALUE );
        DIVISOR_FLOAT_TERM =
            FACTORY.createNumberValue( "/my/path/divisor/float", Modulus.DIVISOR_DESCRIPTOR, OperationTestConstants.FLOAT_2_VALUE );
        DIVISOR_INT_TERM =
            FACTORY.createNumberValue( "/my/path/divisor/int", Modulus.DIVISOR_DESCRIPTOR, OperationTestConstants.INT_2_VALUE );
    }

    private ModelObject modelObject;
    private Modulus operation;

    @Before
    public void beforeEach() throws Exception {
        this.modelObject = mock( ModelObject.class );
        this.operation = new Modulus( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddTwoinputs() throws ChrysalixException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_INT_TERM );
        assertThat( this.operation.inputs().length, is( 2 ) );
        assertThat( this.operation.inputs( DIVIDEND_ID ).size(), is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs( DIVIDEND_ID ).get( 0 ), is( DIVIDEND_INT_TERM ) );
        assertThat( this.operation.inputs( DIVISOR_ID ).size(), is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs( DIVISOR_ID ).get( 0 ), is( DIVISOR_INT_TERM ) );
    }

    @Test
    public void shouldCalculateDoubleinputs() throws ChrysalixException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_DOUBLE_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_DOUBLE_TERM );
        assertThat( this.operation.get(),
                    is( DIVIDEND_DOUBLE_TERM.get().doubleValue() % DIVISOR_DOUBLE_TERM.get().doubleValue() ) );
    }

    @Test
    public void shouldCalculateFloatinputs() throws ChrysalixException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_FLOAT_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_FLOAT_TERM );
        assertThat( this.operation.get(),
                    is( DIVIDEND_FLOAT_TERM.get().doubleValue() % DIVISOR_FLOAT_TERM.get().doubleValue() ) );
    }

    @Test
    public void shouldCalculateIntegerinputs() throws ChrysalixException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_INT_TERM );
        assertThat( this.operation.get(),
                    is( DIVIDEND_INT_TERM.get().doubleValue() % DIVISOR_INT_TERM.get().doubleValue() ) );
    }

    @Test
    public void shouldCalculateMixedinputs() throws ChrysalixException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_DOUBLE_TERM );
        assertThat( this.operation.get(), is( DIVIDEND_INT_TERM.get().intValue() % DIVISOR_DOUBLE_TERM.get().doubleValue() ) );
    }

    @Test
    public void shouldCreateOperation() throws Exception {
        assertThat( Modulus.DESCRIPTOR.newInstance( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( Modulus.class ) ) );
    }

    @Test
    public void shouldHaveErrorsAfterConstruction() throws Exception {
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenDividendTermIsNotANumber() throws ChrysalixException {
        this.operation.addInput( DIVIDEND_ID, OperationTestConstants.STRING_1_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_INT_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenDivisorTermIsNotANumber() throws ChrysalixException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVISOR_ID, OperationTestConstants.STRING_1_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenMoreThanOneDividend() throws ChrysalixException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_DOUBLE_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenMoreThanOneDivisor() throws ChrysalixException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_DOUBLE_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_DOUBLE_TERM );
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
        assertThat( this.operation.descriptor().description(), is( ChrysalixI18n.localize( Modulus.DESCRIPTION ) ) );
    }

    @Test
    public void shouldProvideName() throws Exception {
        assertThat( this.operation.name(), is( ChrysalixI18n.localize( Modulus.NAME ) ) );
    }

}
