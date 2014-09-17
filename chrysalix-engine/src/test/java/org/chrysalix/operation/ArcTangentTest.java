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

import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.chrysalix.operation.ArcTangent;
import org.chrysalix.transformation.TransformationFactory;
import org.chrysalix.transformation.Value;
import org.chrysalix.transformation.OperationCategory.BuiltInCategory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings( { "javadoc", "unchecked" } )
public final class ArcTangentTest {

    private static Value< Number > DOUBLE_TERM;
    private static Value< Number > FLOAT_TERM;
    private static Value< Number > INT_TERM;
    private static Value< Number > INT2_TERM;
    private static Value< Number > LONG_TERM;

    @BeforeClass
    public static void initializeConstants() throws Exception {
        DOUBLE_TERM = TransformationFactory.createValue( ArcTangent.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_1_VALUE );
        FLOAT_TERM = TransformationFactory.createValue( ArcTangent.TERM_DESCRIPTOR, OperationTestConstants.FLOAT_1_VALUE );
        INT_TERM = TransformationFactory.createValue( ArcTangent.TERM_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        INT2_TERM = TransformationFactory.createValue( ArcTangent.TERM_DESCRIPTOR, OperationTestConstants.INT_2_VALUE );
        LONG_TERM = TransformationFactory.createValue( ArcTangent.TERM_DESCRIPTOR, OperationTestConstants.LONG_1_VALUE );
    }

    private ArcTangent operation;

    @Before
    public void beforeEach() {
        this.operation = new ArcTangent( OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddOneTerm() throws ChrysalixException {
        this.operation.addInput( ArcTangent.TERM_DESCRIPTOR.id(), INT_TERM );
        assertThat( this.operation.inputs().size(), is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs().get( 0 ), is( INT_TERM ) );
    }

    @Test
    public void shouldCalculateDoubleTerm() throws ChrysalixException {
        this.operation.addInput( ArcTangent.TERM_DESCRIPTOR.id(), DOUBLE_TERM );
        assertThat( this.operation.get(), is( ( Number ) Math.atan( DOUBLE_TERM.get().doubleValue() ) ) );
    }

    @Test
    public void shouldCalculateFloatTerm() throws ChrysalixException {
        this.operation.addInput( ArcTangent.TERM_DESCRIPTOR.id(), FLOAT_TERM );
        assertThat( this.operation.get(), is( ( Number ) Math.atan( FLOAT_TERM.get().floatValue() ) ) );
    }

    @Test
    public void shouldCalculateIntegerTerm() throws ChrysalixException {
        this.operation.addInput( ArcTangent.TERM_DESCRIPTOR.id(), INT_TERM );
        assertThat( this.operation.get(), is( ( Number ) Math.atan( INT_TERM.get().intValue() ) ) );
    }

    @Test
    public void shouldCalculateLongTerm() throws ChrysalixException {
        this.operation.addInput( ArcTangent.TERM_DESCRIPTOR.id(), LONG_TERM );
        assertThat( this.operation.get(), is( ( Number ) Math.atan( LONG_TERM.get().longValue() ) ) );
    }

    @Test
    public void shouldCreateOperation() {
        assertThat( ArcTangent.DESCRIPTOR.newInstance( OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( ArcTangent.class ) ) );
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
    public void shouldHaveErrorWhenMoreThanOneTerm() throws ChrysalixException {
        this.operation.addInput( ArcTangent.TERM_DESCRIPTOR.id(), INT_TERM );
        this.operation.addInput( ArcTangent.TERM_DESCRIPTOR.id(), INT2_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenTermIsNotANumber() throws ChrysalixException {
        this.operation.addInput( ArcTangent.TERM_DESCRIPTOR.id(), OperationTestConstants.STRING_1_TERM );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveProblemsAfterConstruction() {
        assertThat( this.operation.problems().isEmpty(), is( false ) );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldNotBeAbleToGetResultAfterConstruction() throws ChrysalixException {
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
        assertThat( this.operation.description(), is( ChrysalixI18n.arcTangentOperationDescription.text() ) );
    }

    @Test
    public void shouldProvideName() {
        assertThat( this.operation.name(), is( ChrysalixI18n.arcTangentOperationName.text() ) );
    }

}
