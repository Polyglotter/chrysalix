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
import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.transformation.OperationCategory.BuiltInCategory;
import org.polyglotter.transformation.TransformationFactory;
import org.polyglotter.transformation.Value;

@SuppressWarnings( { "javadoc", "unchecked" } )
public final class PowerTest {

    private static final String BASE_ID = Power.BASE_DESCRIPTOR.id();
    private static final String EXP_ID = Power.EXPONENT_DESCRIPTOR.id();
    private static Value< Number > BASE_DOUBLE_TERM;
    private static Value< Number > BASE_FLOAT_TERM;
    private static Value< Number > BASE_INT_TERM;
    private static Value< Number > EXP_DOUBLE_TERM;
    private static Value< Number > EXP_FLOAT_TERM;
    private static Value< Number > EXP_INT_TERM;

    @BeforeClass
    public static void initializeConstants() throws Exception {
        BASE_DOUBLE_TERM = TransformationFactory.createValue( Power.BASE_DESCRIPTOR, OperationTestConstants.DOUBLE_1_VALUE );
        BASE_FLOAT_TERM = TransformationFactory.createValue( Power.BASE_DESCRIPTOR, OperationTestConstants.FLOAT_1_VALUE );
        BASE_INT_TERM = TransformationFactory.createValue( Power.BASE_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        EXP_DOUBLE_TERM = TransformationFactory.createValue( Power.EXPONENT_DESCRIPTOR, OperationTestConstants.DOUBLE_2_VALUE );
        EXP_FLOAT_TERM = TransformationFactory.createValue( Power.EXPONENT_DESCRIPTOR, OperationTestConstants.FLOAT_2_VALUE );
        EXP_INT_TERM = TransformationFactory.createValue( Power.EXPONENT_DESCRIPTOR, OperationTestConstants.INT_2_VALUE );
    }

    private Power operation;

    @Before
    public void beforeEach() {
        this.operation = new Power( OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddTwoinputs() throws PolyglotterException {
        this.operation.addInput( BASE_ID, BASE_INT_TERM );
        this.operation.addInput( EXP_ID, EXP_INT_TERM );
        assertThat( this.operation.inputs().size(), is( 2 ) );
        assertThat( this.operation.inputs( BASE_ID ).size(), is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs( BASE_ID ).get( 0 ), is( BASE_INT_TERM ) );
        assertThat( this.operation.inputs( EXP_ID ).size(), is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs( EXP_ID ).get( 0 ), is( EXP_INT_TERM ) );
    }

    @Test
    public void shouldCalculateDoubleinputs() throws PolyglotterException {
        this.operation.addInput( BASE_ID, BASE_DOUBLE_TERM );
        this.operation.addInput( EXP_ID, EXP_DOUBLE_TERM );
        assertThat( this.operation.get(),
                    is( ( Number ) Math.pow( BASE_DOUBLE_TERM.get().doubleValue(), EXP_DOUBLE_TERM.get().doubleValue() ) ) );
    }

    @Test
    public void shouldCalculateFloatinputs() throws PolyglotterException {
        this.operation.addInput( BASE_ID, BASE_FLOAT_TERM );
        this.operation.addInput( EXP_ID, EXP_FLOAT_TERM );
        assertThat( this.operation.get(),
                    is( ( Number ) Math.pow( BASE_FLOAT_TERM.get().floatValue(), EXP_FLOAT_TERM.get().floatValue() ) ) );
    }

    @Test
    public void shouldCreateOperation() throws Exception {
        assertThat( Power.DESCRIPTOR.newInstance( OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( Power.class ) ) );
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
    public void shouldHaveErrorWhenBaseIsNotANumber() throws PolyglotterException {
        this.operation.addInput( BASE_ID, OperationTestConstants.STRING_1_TERM );
        this.operation.addInput( EXP_ID, EXP_DOUBLE_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenExponentIsNotANumber() throws PolyglotterException {
        this.operation.addInput( BASE_ID, BASE_INT_TERM );
        this.operation.addInput( EXP_ID, OperationTestConstants.STRING_1_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenMoreThanOneBase() throws PolyglotterException {
        this.operation.addInput( BASE_ID, BASE_DOUBLE_TERM );
        this.operation.addInput( BASE_ID, BASE_INT_TERM );
        this.operation.addInput( EXP_ID, EXP_DOUBLE_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenMoreThanOneExponent() throws PolyglotterException {
        this.operation.addInput( BASE_ID, BASE_INT_TERM );
        this.operation.addInput( EXP_ID, EXP_DOUBLE_TERM );
        this.operation.addInput( EXP_ID, EXP_FLOAT_TERM );
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
        this.operation.inputs().add( BASE_DOUBLE_TERM );
    }

    @Test
    public void shouldNotHaveTermsAfterConstruction() {
        assertThat( this.operation.inputs().isEmpty(), is( true ) );
    }

    @Test
    public void shouldProvideDescription() {
        assertThat( this.operation.description(), is( PolyglotterI18n.powerOperationDescription.text() ) );
    }

    @Test
    public void shouldProvideName() {
        assertThat( this.operation.name(), is( PolyglotterI18n.powerOperationName.text() ) );
    }

}
