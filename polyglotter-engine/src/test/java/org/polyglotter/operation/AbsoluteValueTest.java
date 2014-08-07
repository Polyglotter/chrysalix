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
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.modeshape.jcr.query.model.FullTextSearch.Term;
import org.polyglotter.PolyglotterI18n;
import org.polyglotter.TestConstants;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.transformation.OperationCategory.BuiltInCategory;

@SuppressWarnings( { "javadoc", "unchecked" } )
public final class AbsoluteValueTest {

    private AbsoluteValue operation;

    @Before
    public void beforeEach() {
        this.operation = new AbsoluteValue( TestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddOneTerm() throws PolyglotterException {
        this.operation.addInput( TestConstants.INT_1_TERM );
        assertThat( this.operation.inputs().size(), is( 1 ) );
        assertThat( ( Term< Number > ) this.operation.get( TestConstants.INT_1_ID ), is( TestConstants.INT_1_TERM ) );
    }

    @Test
    public void shouldCalculateDoubleTerm() throws PolyglotterException {
        this.operation.addInput( TestConstants.DOUBLE_1_TERM );
        assertThat( this.operation.get(), is( ( Number ) TestConstants.DOUBLE_1_VALUE ) );
    }

    @Test
    public void shouldCalculateFloatTerm() throws PolyglotterException {
        this.operation.addInput( TestConstants.FLOAT_1_TERM );
        assertThat( ( TestConstants.FLOAT_1_VALUE - ( Float ) this.operation.get() ) < Math.ulp( TestConstants.FLOAT_1_VALUE ), is( true ) );
        assertThat( this.operation.get(), is( ( Number ) Math.abs( TestConstants.FLOAT_1_VALUE ) ) );
    }

    @Test
    public void shouldCalculateIntegerTerm() throws PolyglotterException {
        this.operation.addInput( TestConstants.INT_1_TERM );
        assertThat( this.operation.get(), is( ( Number ) TestConstants.INT_1_VALUE ) );
    }

    @Test
    public void shouldCalculateLongTerm() throws PolyglotterException {
        this.operation.addInput( TestConstants.LONG_1_TERM );
        assertThat( this.operation.get(), is( ( Number ) Math.abs( TestConstants.LONG_1_VALUE ) ) );

        this.operation.remove( TestConstants.LONG_1_ID );

        this.operation.addInput( TestConstants.LONG_2_TERM );
        assertThat( this.operation.get(), is( ( Number ) TestConstants.LONG_2_VALUE ) );
    }

    @Test
    public void shouldCalculateNegativeDoubleTerm() throws PolyglotterException {
        this.operation.addInput( TestConstants.DOUBLE_4_TERM );
        assertThat( this.operation.get(), is( ( Number ) Math.abs( TestConstants.DOUBLE_4_VALUE ) ) );
    }

    @Test
    public void shouldCalculateNegativeIntegerTerm() throws PolyglotterException {
        this.operation.addInput( TestConstants.INT_4_TERM );
        assertThat( this.operation.get(), is( ( Number ) Math.abs( TestConstants.INT_4_VALUE ) ) );
    }

    @Test
    public void shouldHaveAbbreviation() {
        assertThat( this.operation.descriptor().abbreviation(), is( "abs" ) );
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
        this.operation.addInput( TestConstants.INT_1_TERM );
        this.operation.addInput( TestConstants.INT_2_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenTermIsNotANumber() throws PolyglotterException {
        this.operation.addInput( TestConstants.STRING_1_TERM );
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
        this.operation.inputs().add( TestConstants.INT_1_TERM );
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
