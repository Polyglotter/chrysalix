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
public final class AddTest {

    private static final String ID = Add.TERM_DESCRIPTOR.id();
    private static Value< Number > DOUBLE_TERM;
    private static Value< Number > INT_TERM;
    private static Value< Number > INT2_TERM;

    @BeforeClass
    public static void initializeConstants() throws Exception {
        DOUBLE_TERM = TransformationFactory.createValue( Add.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_1_VALUE );
        INT_TERM = TransformationFactory.createValue( Add.TERM_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        INT2_TERM = TransformationFactory.createValue( Add.TERM_DESCRIPTOR, OperationTestConstants.INT_2_VALUE );
    }

    private Add operation;

    @Before
    public void beforeEach() {
        this.operation = new Add( OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddIntegerAndDouble() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM );
        this.operation.addInput( ID, DOUBLE_TERM );
        assertThat( this.operation.get(),
                    is( ( Number ) ( INT_TERM.get().doubleValue() + DOUBLE_TERM.get().doubleValue() ) ) );
    }

    @Test
    public void shouldAddMultipleinputs() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM, INT2_TERM, DOUBLE_TERM );
        assertThat( this.operation.inputs().size(), is( 3 ) );
        assertThat( this.operation.get(),
                    is( ( Number ) ( INT_TERM.get().doubleValue() + INT2_TERM.get().doubleValue() + DOUBLE_TERM.get().doubleValue() ) ) );
    }

    @Test
    public void shouldAddOneTerm() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM );
        assertThat( this.operation.inputs().size(), is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs().get( 0 ), is( INT_TERM ) );
    }

    @Test
    public void shouldCalculateIntegerResult() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM );
        this.operation.addInput( ID, INT2_TERM );
        // JCR does not have int so must use long
        assertThat( this.operation.get(), is( ( Number ) ( INT_TERM.get().longValue() + INT2_TERM.get().longValue() ) ) );
    }

    @Test
    public void shouldCreateOperation() {
        assertThat( Add.DESCRIPTOR.newInstance( OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( Add.class ) ) );
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
    public void shouldHaveErrorWhenAddingTermWithWrongType() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM, INT2_TERM ); // will get rid of initial problems
        this.operation.addInput( ID, OperationTestConstants.STRING_1_TERM ); // will cause a new problem
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

    @Test( expected = PolyglotterException.class )
    public void shouldNotBeAbleToGetResultWithOnlyOneTerm() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM );
        this.operation.get();
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotBeAbleToModifyTermsList() {
        this.operation.inputs().add( INT_TERM );
    }

    @Test
    public void shouldNotHaveProblemsWithTwoTermsOfCorrectType() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM, INT_TERM );
        assertThat( this.operation.problems().isEmpty(), is( true ) );
        assertThat( this.operation.problems().isOk(), is( true ) );
    }

    @Test
    public void shouldNotHaveTermsAfterConstruction() {
        assertThat( this.operation.inputs().isEmpty(), is( true ) );
    }

    @Test
    public void shouldProvideDescription() {
        assertThat( this.operation.description(), is( PolyglotterI18n.addOperationDescription.text() ) );
    }

    @Test
    public void shouldProvideName() {
        assertThat( this.operation.name(), is( PolyglotterI18n.addOperationName.text() ) );
    }

}
