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
import static org.mockito.Mockito.mock;

import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.chrysalix.transformation.TransformationTestFactory;
import org.chrysalix.transformation.Value;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.modelspace.ModelObject;

@Ignore
@SuppressWarnings( { "javadoc", "unchecked" } )
public final class AverageTest {

    private static final String ID = Average.TERM_DESCRIPTOR.name();
    private static TransformationTestFactory FACTORY;

    private static Value< Number > DOUBLE_TERM;
    private static Value< Number > INT_TERM;
    private static Value< Number > INT2_TERM;

    @BeforeClass
    public static void initializeConstants() throws Exception {
        FACTORY = new TransformationTestFactory();
        DOUBLE_TERM = FACTORY.createNumberValue( "/my/path/double", Average.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_1_VALUE );
        INT_TERM = FACTORY.createNumberValue( "/my/path/int", Average.TERM_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        INT2_TERM = FACTORY.createNumberValue( "/my/path/int2", Average.TERM_DESCRIPTOR, OperationTestConstants.INT_2_VALUE );
    }

    private ModelObject modelObject;
    private Average operation;

    @Before
    public void beforeEach() throws Exception {
        this.modelObject = mock( ModelObject.class );
        this.operation = new Average( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddOneTerm() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM );
        assertThat( this.operation.inputs().length, is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs()[ 0 ], is( INT_TERM ) );
    }

    @Test
    public void shouldAverageIntegerAndDouble() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM );
        this.operation.addInput( ID, DOUBLE_TERM );
        assertThat( this.operation.get(), is( ( Number ) ( ( INT_TERM.get().intValue() + DOUBLE_TERM.get().doubleValue() ) / 2 ) ) );
    }

    @Test
    public void shouldAverageMultipleinputs() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM, INT2_TERM, DOUBLE_TERM );
        assertThat( this.operation.inputs().length, is( 3 ) );
        assertThat( this.operation.get(),
                    is( ( Number ) ( ( INT_TERM.get().intValue() + INT2_TERM.get().intValue() + DOUBLE_TERM.get().doubleValue() ) / 3 ) ) );
    }

    @Test
    public void shouldCalculateIntegerResult() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM );
        this.operation.addInput( ID, INT2_TERM );
        assertThat( this.operation.get().intValue(), is( ( INT_TERM.get().intValue() + INT2_TERM.get().intValue() ) / 2 ) );
    }

    @Test
    public void shouldCreateOperation() throws Exception {
        assertThat( Average.DESCRIPTOR.newInstance( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( Average.class ) ) );
    }

    @Test
    public void shouldGetResultWithOnlyOneTerm() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM );
        assertThat( this.operation.get(), is( ( Number ) INT_TERM.get().intValue() ) );
    }

    @Test
    public void shouldHaveErrorsAfterConstruction() throws Exception {
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenAveragingWithStringTerm() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM, INT2_TERM ); // will get rid of initial problems
        this.operation.addInput( ID, OperationTestConstants.STRING_1_TERM ); // adds a problem
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveProblemsAfterConstruction() throws Exception {
        assertThat( this.operation.problems().isEmpty(), is( false ) );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldNotBeAbleToGetResultAfterConstruction() throws ChrysalixException {
        this.operation.get();
    }

    @Test
    public void shouldNotHaveProblemsWithTwoTermsOfCorrectType() throws ChrysalixException {
        this.operation.addInput( ID, INT_TERM, INT2_TERM );
        assertThat( this.operation.problems().isEmpty(), is( true ) );
        assertThat( this.operation.problems().isOk(), is( true ) );
    }

    @Test
    public void shouldNotHaveTermsAfterConstruction() throws Exception {
        assertThat( this.operation.inputs().length, is( 0 ) );
    }

    @Test
    public void shouldProvideDescription() throws Exception {
        assertThat( this.operation.descriptor().description(), is( ChrysalixI18n.localize( Average.DESCRIPTION ) ) );
    }

    @Test
    public void shouldProvideName() throws Exception {
        assertThat( this.operation.name(), is( ChrysalixI18n.localize( Average.NAME ) ) );
    }

}
