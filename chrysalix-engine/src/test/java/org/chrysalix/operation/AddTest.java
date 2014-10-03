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
import static org.mockito.Mockito.when;

import org.chrysalix.ChrysalixException;
import org.chrysalix.transformation.TransformationTestFactory;
import org.chrysalix.transformation.Value;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.modelspace.Model;
import org.modelspace.ModelObject;
import org.modelspace.Modelspace;

@Ignore
@SuppressWarnings( { "javadoc", "unchecked" } )
public final class AddTest {

    private static final String ID = Add.TERM_DESCRIPTOR.name();
    private static TransformationTestFactory FACTORY;

    @BeforeClass
    public static void initializeConstants() {
        FACTORY = new TransformationTestFactory();
    }

    private Value< Number > doubleTerm;
    private Value< Number > intTerm;
    private Value< Number > int2Term;

    private ModelObject modelObject;
    private Add operation;

    @Before
    public void beforeEach() throws Exception {
        this.modelObject = createModelObject();
        this.operation = new Add( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION );
        this.doubleTerm = FACTORY.createNumberValue( "/my/path/double", Add.TERM_DESCRIPTOR, OperationTestConstants.DOUBLE_1_VALUE );
        this.intTerm = FACTORY.createNumberValue( "/my/path/int", Add.TERM_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        this.int2Term = FACTORY.createNumberValue( "/my/path/int2", Add.TERM_DESCRIPTOR, OperationTestConstants.INT_2_VALUE );
    }

    private ModelObject createModelObject() throws Exception {
        final ModelObject modelObject = mock( ModelObject.class );
        final Model model = mock( Model.class );
        final Modelspace modeler = mock( Modelspace.class );

        when( modelObject.name() ).thenReturn( Add.DESCRIPTOR.id() );
        when( modelObject.model() ).thenReturn( model );
        when( model.modelspace() ).thenReturn( modeler );
        when( model.absolutePath() ).thenReturn( "/my/path/" + Add.DESCRIPTOR.id() );

        return modelObject;
    }

    @Test
    public void shouldAddIntegerAndDouble() throws ChrysalixException {
        this.operation.addInput( ID, this.intTerm );
        this.operation.addInput( ID, this.doubleTerm );
        assertThat( this.operation.get(),
                    is( ( Number ) ( this.intTerm.get().doubleValue() + this.doubleTerm.get().doubleValue() ) ) );
    }

    @Test
    public void shouldAddMultipleinputs() throws ChrysalixException {
        this.operation.addInput( ID, this.intTerm, this.int2Term, this.doubleTerm );
        assertThat( this.operation.inputs().length, is( 3 ) );
        assertThat( this.operation.get(),
                    is( ( Number ) ( this.intTerm.get().doubleValue()
                                     + this.int2Term.get().doubleValue()
                                     + this.doubleTerm.get().doubleValue() ) ) );
    }

    @Test
    public void shouldAddOneTerm() throws ChrysalixException {
        this.operation.addInput( ID, this.intTerm );
        assertThat( this.operation.inputs().length, is( 1 ) );
        assertThat( ( Value< Number > ) this.operation.inputs()[ 0 ], is( this.intTerm ) );
    }

    @Test
    public void shouldCalculateIntegerResult() throws ChrysalixException {
        this.operation.addInput( ID, this.intTerm );
        this.operation.addInput( ID, this.int2Term );
        // JCR does not have int so must use long
        assertThat( this.operation.get(),
                    is( ( Number ) ( this.intTerm.get().longValue() + this.int2Term.get().longValue() ) ) );
    }

    @Test
    public void shouldCreateOperation() throws Exception {
        assertThat( Add.DESCRIPTOR.newInstance( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( Add.class ) ) );
    }

    @Test
    public void shouldHaveErrorsAfterConstruction() throws Exception {
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveErrorWhenAddingTermWithWrongType() throws ChrysalixException {
        this.operation.addInput( ID, this.intTerm, this.int2Term ); // will get rid of initial problems
        this.operation.addInput( ID, OperationTestConstants.STRING_1_TERM ); // will cause a new problem
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

    @Test( expected = ChrysalixException.class )
    public void shouldNotBeAbleToGetResultWithOnlyOneTerm() throws ChrysalixException {
        this.operation.addInput( ID, this.intTerm );
        this.operation.get();
    }

    @Test
    public void shouldNotHaveProblemsWithTwoTermsOfCorrectType() throws ChrysalixException {
        this.operation.addInput( ID, this.intTerm, this.intTerm );
        assertThat( this.operation.problems().isEmpty(), is( true ) );
        assertThat( this.operation.problems().isOk(), is( true ) );
    }

    @Test
    public void shouldNotHaveTermsAfterConstruction() throws Exception {
        assertThat( this.operation.inputs().length, is( 0 ) );
    }

    @Test
    public void shouldProvideDescription() throws Exception {
        assertThat( this.operation.descriptor().description(), is( Add.DESCRIPTION ) );
    }

    @Test
    public void shouldProvideName() throws Exception {
        assertThat( this.operation.name(), is( Add.NAME ) );
    }

}
