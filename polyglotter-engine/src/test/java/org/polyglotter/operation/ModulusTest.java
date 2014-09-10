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
package org.polyglotter.operation;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.polyglotter.PolyglotterException;
import org.polyglotter.PolyglotterI18n;
import org.polyglotter.transformation.OperationCategory.BuiltInCategory;
import org.polyglotter.transformation.TransformationFactory;
import org.polyglotter.transformation.Value;

@SuppressWarnings( { "javadoc", "unchecked" } )
public final class ModulusTest {

    private static final String DIVIDEND_ID = Modulus.DIVIDEND_DESCRIPTOR.id();
    private static final String DIVISOR_ID = Modulus.DIVISOR_DESCRIPTOR.id();
    private static Value< Number > DIVIDEND_DOUBLE_TERM;
    private static Value< Number > DIVIDEND_FLOAT_TERM;
    private static Value< Number > DIVIDEND_INT_TERM;
    private static Value< Number > DIVISOR_DOUBLE_TERM;
    private static Value< Number > DIVISOR_FLOAT_TERM;
    private static Value< Number > DIVISOR_INT_TERM;

    @BeforeClass
    public static void initializeConstants() throws Exception {
        DIVIDEND_DOUBLE_TERM =
            TransformationFactory.createValue( Modulus.DIVIDEND_DESCRIPTOR, OperationTestConstants.DOUBLE_1_VALUE );
        DIVIDEND_FLOAT_TERM = TransformationFactory.createValue( Modulus.DIVIDEND_DESCRIPTOR, OperationTestConstants.FLOAT_1_VALUE );
        DIVIDEND_INT_TERM = TransformationFactory.createValue( Modulus.DIVIDEND_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        DIVISOR_DOUBLE_TERM = TransformationFactory.createValue( Modulus.DIVISOR_DESCRIPTOR, OperationTestConstants.DOUBLE_2_VALUE );
        DIVISOR_FLOAT_TERM = TransformationFactory.createValue( Modulus.DIVISOR_DESCRIPTOR, OperationTestConstants.FLOAT_2_VALUE );
        DIVISOR_INT_TERM = TransformationFactory.createValue( Modulus.DIVISOR_DESCRIPTOR, OperationTestConstants.INT_2_VALUE );
    }

    private Modulus operation;

    @Before
    public void beforeEach() {
        this.operation = new Modulus( OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddTwoinputs() throws PolyglotterException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_INT_TERM );
        assertThat( this.operation.inputs().size(), is( 2 ) );
        assertThat( this.operation.inputs( DIVIDEND_ID ).size(), is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs( DIVIDEND_ID ).get( 0 ), is( DIVIDEND_INT_TERM ) );
        assertThat( this.operation.inputs( DIVISOR_ID ).size(), is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs( DIVISOR_ID ).get( 0 ), is( DIVISOR_INT_TERM ) );
    }

    @Test
    public void shouldCalculateDoubleinputs() throws PolyglotterException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_DOUBLE_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_DOUBLE_TERM );
        assertThat( this.operation.get(),
                    is( DIVIDEND_DOUBLE_TERM.get().doubleValue() % DIVISOR_DOUBLE_TERM.get().doubleValue() ) );
    }

    @Test
    public void shouldCalculateFloatinputs() throws PolyglotterException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_FLOAT_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_FLOAT_TERM );
        assertThat( this.operation.get(),
                    is( DIVIDEND_FLOAT_TERM.get().doubleValue() % DIVISOR_FLOAT_TERM.get().doubleValue() ) );
    }

    @Test
    public void shouldCalculateIntegerinputs() throws PolyglotterException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_INT_TERM );
        assertThat( this.operation.get(),
                    is( DIVIDEND_INT_TERM.get().doubleValue() % DIVISOR_INT_TERM.get().doubleValue() ) );
    }

    @Test
    public void shouldCalculateMixedinputs() throws PolyglotterException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_DOUBLE_TERM );
        assertThat( this.operation.get(), is( DIVIDEND_INT_TERM.get().intValue() % DIVISOR_DOUBLE_TERM.get().doubleValue() ) );
    }

    @Test
    public void shouldCreateOperation() {
        assertThat( Modulus.DESCRIPTOR.newInstance( OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( Modulus.class ) ) );
    }

    @Test
    public void shouldHaveCorrectCategory() {
        assertThat( this.operation.categories().size(), is( 1 ) );
        assertThat( this.operation.categories().contains( BuiltInCategory.ARITHMETIC ), is( true ) );
    }

    @Test
    public void shouldHaveErrorsAfterConstruction() {
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenDividendTermIsNotANumber() throws PolyglotterException {
        this.operation.addInput( DIVIDEND_ID, OperationTestConstants.STRING_1_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_INT_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenDivisorTermIsNotANumber() throws PolyglotterException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVISOR_ID, OperationTestConstants.STRING_1_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenMoreThanOneDividend() throws PolyglotterException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_DOUBLE_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenMoreThanOneDivisor() throws PolyglotterException {
        this.operation.addInput( DIVIDEND_ID, DIVIDEND_INT_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_DOUBLE_TERM );
        this.operation.addInput( DIVISOR_ID, DIVISOR_DOUBLE_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveProblemsAfterConstruction() {
        assertThat( this.operation.problems().isEmpty(), is( false ) );
    }

    @Test( expected = PolyglotterException.class )
    public void shouldNotBeAbleToGetResultAfterConstruction() throws PolyglotterException {
        this.operation.get();
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotBeAbleToModifyTermsList() {
        this.operation.inputs().add( DIVIDEND_INT_TERM );
    }

    @Test
    public void shouldNotHaveTermsAfterConstruction() {
        assertThat( this.operation.inputs().isEmpty(), is( true ) );
    }

    @Test
    public void shouldProvideDescription() {
        assertThat( this.operation.description(), is( PolyglotterI18n.modulusOperationDescription.text() ) );
    }

    @Test
    public void shouldProvideName() {
        assertThat( this.operation.name(), is( PolyglotterI18n.modulusOperationName.text() ) );
    }

}
