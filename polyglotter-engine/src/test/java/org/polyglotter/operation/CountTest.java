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
public final class CountTest {

    private static final String ID = Count.TERM_DESCRIPTOR.id();
    private static Value< Object > DOUBLE_TERM;
    private static Value< Object > INT_TERM;
    private static Value< Object > INT2_TERM;

    @BeforeClass
    public static void initializeConstants() throws Exception {
        DOUBLE_TERM = TransformationFactory.createValue( Count.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_1_VALUE );
        INT_TERM = TransformationFactory.createValue( Count.TERM_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        INT2_TERM = TransformationFactory.createValue( Count.TERM_DESCRIPTOR, OperationTestConstants.INT_2_VALUE );
    }

    private Count operation;

    @Before
    public void beforeEach() {
        this.operation = new Count( OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddOneTerm() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM );
        assertThat( this.operation.inputs().size(), is( 1 ) );
        assertThat( ( Value< Object > ) this.operation.inputs().get( 0 ), is( INT_TERM ) );
    }

    @Test
    public void shouldCalculateResultWithNoinputs() throws PolyglotterException {
        assertThat( this.operation.get(), is( 0 ) );
    }

    @Test
    public void shouldCountInputs() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM, INT2_TERM );
        assertThat( this.operation.inputs().size(), is( 2 ) );
        assertThat( this.operation.get(), is( 2 ) );
    }

    @Test
    public void shouldCountTermsOfDifferentTypes() throws PolyglotterException {
        this.operation.addInput( ID, INT_TERM );
        this.operation.addInput( ID, DOUBLE_TERM );
        this.operation.addInput( ID, OperationTestConstants.STRING_1_TERM );
        assertThat( this.operation.get(), is( 3 ) );
    }

    @Test
    public void shouldCreateOperation() {
        assertThat( Count.DESCRIPTOR.newInstance( OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( Count.class ) ) );
    }

    @Test
    public void shouldHaveCorrectCategory() {
        assertThat( this.operation.categories().size(), is( 1 ) );
        assertThat( this.operation.categories().contains( BuiltInCategory.ARITHMETIC ), is( true ) );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotBeAbleToModifyTermsList() {
        this.operation.inputs().add( INT_TERM );
    }

    @Test
    public void shouldNotHaveProblemsAfterConstruction() {
        assertThat( this.operation.problems().isEmpty(), is( true ) );
    }

    @Test
    public void shouldNotHaveTermsAfterConstruction() {
        assertThat( this.operation.inputs().isEmpty(), is( true ) );
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
