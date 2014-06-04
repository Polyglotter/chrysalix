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
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.Operation.Category;
import org.polyglotter.grammar.Term;
import org.polyglotter.grammar.TestConstants;
import org.polyglotter.grammar.TestIntegerTerm;

@SuppressWarnings( "javadoc" )
public final class CountTest implements TestConstants {

    private Count operation;

    @Before
    public void beforeEach() {
        this.operation = new Count( ID, TRANSFORM_ID );
    }

    @Test
    public void shouldAddOneTerm() throws PolyglotterException {
        this.operation.add( INT_1 );
        assertThat( this.operation.terms().size(), is( 1 ) );
        assertThat( ( TestIntegerTerm ) this.operation.get( INT_1.id() ), is( INT_1 ) );
    }

    @Test
    public void shouldCalculateResultWithNoTerms() throws PolyglotterException {
        assertThat( this.operation.result(), is( 0 ) );
    }

    @Test
    public void shouldCountTerms() throws PolyglotterException {
        this.operation.add( INT_1, INT_2 );
        assertThat( this.operation.terms().size(), is( 2 ) );
        assertThat( this.operation.terms(), hasItems( new Term< ? >[] { INT_1, INT_2 } ) );
        assertThat( this.operation.result(), is( 2 ) );
    }

    @Test
    public void shouldCountTermsOfDifferentTypes() throws PolyglotterException {
        this.operation.add( INT_1 );
        this.operation.add( DOUBLE_1 );
        this.operation.add( STRING_1 );
        assertThat( this.operation.terms(), hasItems( new Term< ? >[] { INT_1, DOUBLE_1, STRING_1 } ) );
        assertThat( this.operation.result(), is( 3 ) );
    }

    @Test
    public void shouldHaveAbbreviation() {
        assertThat( this.operation.descriptor().abbreviation(), is( "count" ) );
    }

    @Test
    public void shouldHaveCorrectCategory() {
        assertThat( this.operation.descriptor().category(), is( Category.ARITHMETIC ) );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotBeAbleToModifyTermsList() {
        this.operation.terms().add( INT_1 );
    }

    @Test
    public void shouldNotHaveProblemsAfterConstruction() {
        assertThat( this.operation.problems().isEmpty(), is( true ) );
    }

    @Test
    public void shouldNotHaveTermsAfterConstruction() {
        assertThat( this.operation.terms().isEmpty(), is( true ) );
    }

    @Test
    public void shouldProvideDescription() {
        assertThat( this.operation.description(), is( PolyglotterI18n.countOperationDescription.text() ) );
    }

    @Test
    public void shouldProvideName() {
        assertThat( this.operation.name(), is( PolyglotterI18n.countOperationName.text() ) );
    }

}
