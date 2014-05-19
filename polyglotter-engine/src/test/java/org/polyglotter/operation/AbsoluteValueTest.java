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
import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.Operation.Category;
import org.polyglotter.grammar.TestConstants;
import org.polyglotter.grammar.TestIntegerTerm;

@SuppressWarnings( "javadoc" )
public final class AbsoluteValueTest implements TestConstants {

    private AbsoluteValue operation;

    @Before
    public void beforeEach() {
        this.operation = new AbsoluteValue( ID, TRANSFORM_ID );
    }

    @Test
    public void shouldAddOneTerm() throws PolyglotterException {
        this.operation.add( INT_1 );
        assertThat( this.operation.terms().size(), is( 1 ) );
        assertThat( ( TestIntegerTerm ) this.operation.get( INT_1.id() ), is( INT_1 ) );
    }

    @Test
    public void shouldCalculateDoubleTerm() throws PolyglotterException {
        this.operation.add( DOUBLE_1 );
        assertThat( this.operation.result(), is( ( Number ) DOUBLE_1_VALUE ) );
    }

    @Test
    public void shouldCalculateFloatTerm() throws PolyglotterException {
        this.operation.add( FLOAT_1 );
        assertThat( ( FLOAT_1_VALUE - ( Float ) this.operation.result() ) < Math.ulp( FLOAT_1_VALUE ), is( true ) );
        assertThat( this.operation.result(), is( ( Number ) Math.abs( FLOAT_1_VALUE ) ) );
    }

    @Test
    public void shouldCalculateIntegerTerm() throws PolyglotterException {
        this.operation.add( INT_1 );
        assertThat( this.operation.result(), is( ( Number ) INT_1_VALUE ) );
    }

    @Test
    public void shouldCalculateLongTerm() throws PolyglotterException {
        this.operation.add( LONG_1 );
        assertThat( this.operation.result(), is( ( Number ) Math.abs( LONG_1_VALUE ) ) );

        this.operation.remove( LONG_1.id() );
        this.operation.add( LONG_2 );
        assertThat( this.operation.result(), is( ( Number ) LONG_2_VALUE ) );
    }

    @Test
    public void shouldCalculateNegativeDoubleTerm() throws PolyglotterException {
        this.operation.add( DOUBLE_4 );
        assertThat( this.operation.result(), is( ( Number ) Math.abs( DOUBLE_4_VALUE ) ) );
    }

    @Test
    public void shouldCalculateNegativeIntegerTerm() throws PolyglotterException {
        this.operation.add( INT_4 );
        assertThat( this.operation.result(), is( ( Number ) Math.abs( INT_4_VALUE ) ) );
    }

    @Test
    public void shouldHaveAbbreviation() {
        assertThat( this.operation.abbreviation(), is( "abs" ) );
    }

    @Test
    public void shouldHaveCorrectCategory() {
        assertThat( this.operation.category(), is( Category.ARITHMETIC ) );
    }

    @Test
    public void shouldHaveErrorsAfterConstruction() {
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenMoreThanOneTerm() throws PolyglotterException {
        this.operation.add( INT_1 );
        this.operation.add( INT_2 );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenTermIsNotANumber() throws PolyglotterException {
        this.operation.add( STRING_1 );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveProblemsAfterConstruction() {
        assertThat( this.operation.problems().isEmpty(), is( false ) );
    }

    @Test( expected = PolyglotterException.class )
    public void shouldNotBeAbleToGetResultAfterConstruction() throws PolyglotterException {
        this.operation.result();
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotBeAbleToModifyTermsList() {
        this.operation.terms().add( INT_1 );
    }

    @Test
    public void shouldNotHaveTermsAfterConstruction() {
        assertThat( this.operation.terms().isEmpty(), is( true ) );
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
