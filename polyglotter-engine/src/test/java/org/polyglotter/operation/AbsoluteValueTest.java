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
public final class AbsoluteValueTest {

    private static final String ID = AbsoluteValue.TERM_DESCRIPTOR.id();
    private static Value< Number > DOUBLE_TERM;
    private static Value< Number > DOUBLE4_TERM;
    private static Value< Number > FLOAT_TERM;
    private static Value< Number > INT_TERM;
    private static Value< Number > INT4_TERM;
    private static Value< Number > LONG_TERM;
    private static Value< Number > LONG2_TERM;

    @BeforeClass
    public static void initializeConstants() throws Exception {
        DOUBLE_TERM = TransformationFactory.createValue( AbsoluteValue.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_1_VALUE );
        DOUBLE4_TERM = TransformationFactory.createValue( AbsoluteValue.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_4_VALUE );
        FLOAT_TERM = TransformationFactory.createValue( AbsoluteValue.TERM_DESCRIPTOR, OperationTestConstants.FLOAT_1_VALUE );
        INT_TERM = TransformationFactory.createValue( AbsoluteValue.TERM_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        INT4_TERM = TransformationFactory.createValue( AbsoluteValue.TERM_DESCRIPTOR, OperationTestConstants.INT_4_VALUE );
        LONG_TERM = TransformationFactory.createValue( AbsoluteValue.TERM_DESCRIPTOR, OperationTestConstants.LONG_1_VALUE );
        LONG2_TERM = TransformationFactory.createValue( AbsoluteValue.TERM_DESCRIPTOR, OperationTestConstants.LONG_2_VALUE );
    }

    private AbsoluteValue operation;

    @Before
    public void beforeEach() {
        this.operation = new AbsoluteValue( OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddOneTerm() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM );
        assertThat( this.operation.inputs().size(), is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs().get( 0 ), is( INT_TERM ) );
    }

    @Test
    public void shouldCalculateDoubleTerm() throws PolyglotterException {
        this.operation.addInput( ID, DOUBLE_TERM );
        assertThat( this.operation.get(), is( DOUBLE_TERM.get() ) );
    }

    @Test
    public void shouldCalculateFloatTerm() throws PolyglotterException {
        this.operation.addInput( ID, FLOAT_TERM );
        assertThat( ( FLOAT_TERM.get().floatValue() - this.operation.get().floatValue() ) < Math.ulp( FLOAT_TERM.get().floatValue() ),
                    is( true ) );
        assertThat( this.operation.get(), is( ( Number ) Math.abs( FLOAT_TERM.get().floatValue() ) ) );
    }

    @Test
    public void shouldCalculateIntegerTerm() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM );
        assertThat( this.operation.get(), is( INT_TERM.get() ) );
    }

    @Test
    public void shouldCalculateLongTerm() throws PolyglotterException {
        this.operation.addInput( ID, LONG_TERM );
        assertThat( this.operation.get(), is( ( Number ) Math.abs( LONG_TERM.get().longValue() ) ) );

        this.operation.removeInput( ID, LONG_TERM );

        this.operation.addInput( ID, LONG2_TERM );
        assertThat( this.operation.get(), is( LONG2_TERM.get() ) );
    }

    @Test
    public void shouldCalculateNegativeDoubleTerm() throws PolyglotterException {
        this.operation.addInput( ID, DOUBLE4_TERM );
        assertThat( this.operation.get(), is( ( Number ) Math.abs( DOUBLE4_TERM.get().doubleValue() ) ) );
    }

    @Test
    public void shouldCalculateNegativeIntegerTerm() throws PolyglotterException {
        this.operation.addInput( ID, INT4_TERM );
        assertThat( this.operation.get(), is( ( Number ) Math.abs( INT4_TERM.get().intValue() ) ) );
    }

    @Test
    public void shouldCreateOperation() throws Exception {
        assertThat( AbsoluteValue.DESCRIPTOR.newInstance( OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( AbsoluteValue.class ) ) );
    }

    @Test
    public void shouldHaveCorrectCategory() {
        assertThat( this.operation.categories().contains( BuiltInCategory.ARITHMETIC ), is( true ) );
    }

    @Test
    public void shouldHaveErrorsAfterConstruction() {
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenMoreThanOneTerm() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM );
        this.operation.addInput( ID, INT4_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenTermIsNotANumber() throws PolyglotterException {
        this.operation.addInput( ID, OperationTestConstants.STRING_1_TERM );
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
        this.operation.inputs().add( INT_TERM );
    }

    @Test
    public void shouldNotHaveTermsAfterConstruction() {
        assertThat( this.operation.inputs().isEmpty(), is( true ) );
    }

    @Test
    public void shouldProvideDescription() {
        assertThat( this.operation.description(), is( PolyglotterI18n.absoluteValueOperationDescription.text() ) );
    }

    @Test
    public void shouldProvideName() {
        assertThat( this.operation.name(), is( PolyglotterI18n.absoluteValueOperationName.text() ) );
    }

}
