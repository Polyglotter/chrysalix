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
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.chrysalix.operation.Mode;
import org.chrysalix.transformation.TransformationFactory;
import org.chrysalix.transformation.Value;
import org.chrysalix.transformation.OperationCategory.BuiltInCategory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings( { "javadoc", "unchecked" } )
public final class ModeTest {

    private static final String ID = Mode.TERM_DESCRIPTOR.id();
    private static Value< Number > DOUBLE_TERM;
    private static Value< Number > DOUBLE2_TERM;
    private static Value< Number > DOUBLE3_TERM;
    private static Value< Number > FLOAT_TERM;
    private static Value< Number > FLOAT2_TERM;
    private static Value< Number > INT_TERM;
    private static Value< Number > INT2_TERM;
    private static Value< Number > INT3_TERM;

    @BeforeClass
    public static void initializeConstants() throws Exception {
        DOUBLE_TERM = TransformationFactory.createValue( Mode.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_1_VALUE );
        DOUBLE2_TERM = TransformationFactory.createValue( Mode.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_2_VALUE );
        DOUBLE3_TERM = TransformationFactory.createValue( Mode.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_3_VALUE );
        FLOAT_TERM = TransformationFactory.createValue( Mode.TERM_DESCRIPTOR, OperationTestConstants.FLOAT_1_VALUE );
        FLOAT2_TERM = TransformationFactory.createValue( Mode.TERM_DESCRIPTOR, OperationTestConstants.FLOAT_2_VALUE );
        INT_TERM = TransformationFactory.createValue( Mode.TERM_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        INT2_TERM = TransformationFactory.createValue( Mode.TERM_DESCRIPTOR, OperationTestConstants.INT_2_VALUE );
        INT3_TERM = TransformationFactory.createValue( Mode.TERM_DESCRIPTOR, OperationTestConstants.INT_3_VALUE );
    }

    private Mode operation;

    @Before
    public void beforeEach() {
        this.operation = new Mode( OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddOneTerm() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM );
        assertThat( this.operation.inputs().size(), is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs().get( 0 ), is( INT_TERM ) );
    }

    @Test
    public void shouldAllowResultWithOneTerm() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM );
        this.operation.get();
    }

    @Test
    public void shouldCreateOperation() {
        assertThat( Mode.DESCRIPTOR.newInstance( OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( Mode.class ) ) );
    }

    @Test
    public void shouldFindOneNumber() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM,
                                 INT2_TERM,
                                 DOUBLE_TERM,
                                 DOUBLE2_TERM,
                                 FLOAT_TERM,
                                 FLOAT2_TERM,
                                 INT3_TERM );
        final Number[] result = this.operation.get();
        assertThat( result.length, is( 1 ) );
        assertThat( result[ 0 ], is( ( Number ) INT_TERM.get().intValue() ) );
    }

    @Test
    public void shouldFindTwoNumbers() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM,
                                 INT2_TERM,
                                 DOUBLE_TERM,
                                 DOUBLE2_TERM,
                                 FLOAT_TERM,
                                 FLOAT2_TERM,
                                 INT3_TERM,
                                 DOUBLE3_TERM );
        final Number[] result = this.operation.get();
        assertThat( result.length, is( 2 ) );
        assertThat( Arrays.asList( result ), hasItems( INT_TERM.get(), DOUBLE_TERM.get() ) );
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
    public void shouldHaveErrorWhenStringTerm() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM, INT2_TERM ); // will get rid of current problems
        this.operation.addInput( ID, OperationTestConstants.STRING_1_TERM );
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
    public void shouldNotFindAMode() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM,
                                 INT2_TERM,
                                 DOUBLE_TERM,
                                 DOUBLE2_TERM,
                                 FLOAT_TERM,
                                 FLOAT2_TERM );
        final Number[] result = this.operation.get();
        assertThat( result.length, is( 0 ) );
    }

    @Test
    public void shouldNotHaveTermsAfterConstruction() {
        assertThat( this.operation.inputs().isEmpty(), is( true ) );
    }

    @Test
    public void shouldProvideDescription() {
        assertThat( this.operation.description(), is( ChrysalixI18n.modeOperationDescription.text() ) );
    }

    @Test
    public void shouldProvideName() {
        assertThat( this.operation.name(), is( ChrysalixI18n.modeOperationName.text() ) );
    }

}
