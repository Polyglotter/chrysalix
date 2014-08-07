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
import org.junit.Test;
import org.polyglotter.TestConstants;
import org.polyglotter.transformation.Operation;
import org.polyglotter.transformation.Operation.Category;

@SuppressWarnings( "javadoc" )
public final class OperationFactoryTest {

    private OperationFactory factory;

    private void assertOperation( final Operation< ? > operation,
                                  final Class< ? extends Operation< ? >> operationClass ) {
        assertThat( operation, is( instanceOf( operationClass ) ) );
        assertThat( operation.transformationId(), is( TestConstants.ID ) );
        assertThat( operation.transformId(), is( TestConstants.TRANSFORM_ID ) );
    }

    @Before
    public void beforeEach() {
        this.factory = new OperationFactory();
    }

    @Test
    public void shouldCreateAbsoluteValueOperation() {
        assertOperation( this.factory.create( AbsoluteValue.OUTPUT, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         AbsoluteValue.class );
    }

    @Test
    public void shouldCreateAddOperation() {
        assertOperation( this.factory.create( Add.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Add.class );
    }

    @Test
    public void shouldCreateArcCosineOperation() {
        assertOperation( this.factory.create( ArcCosine.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         ArcCosine.class );
    }

    @Test
    public void shouldCreateArcSineOperation() {
        assertOperation( this.factory.create( ArcSine.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         ArcSine.class );
    }

    @Test
    public void shouldCreateArcTangentOperation() {
        assertOperation( this.factory.create( ArcTangent.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         ArcTangent.class );
    }

    @Test
    public void shouldCreateAverageOperation() {
        assertOperation( this.factory.create( Average.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Average.class );
    }

    @Test
    public void shouldCreateCeilingOperation() {
        assertOperation( this.factory.create( Ceiling.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Ceiling.class );
    }

    @Test
    public void shouldCreateConcatOperation() {
        assertOperation( this.factory.create( Concat.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Concat.class );
    }

    @Test
    public void shouldCreateCosineOperation() {
        assertOperation( this.factory.create( Cosine.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Cosine.class );
    }

    @Test
    public void shouldCreateCountOperation() {
        assertOperation( this.factory.create( Count.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Count.class );
    }

    @Test
    public void shouldCreateCubeRootOperation() {
        assertOperation( this.factory.create( CubeRoot.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         CubeRoot.class );
    }

    @Test
    public void shouldCreateDecrementOperation() {
        assertOperation( this.factory.create( Decrement.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Decrement.class );
    }

    @Test
    public void shouldCreateDivideOperation() {
        assertOperation( this.factory.create( Divide.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Divide.class );
    }

    @Test
    public void shouldCreateFloorOperation() {
        assertOperation( this.factory.create( Floor.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Floor.class );
    }

    @Test
    public void shouldCreateHyperbolicCosineOperation() {
        assertOperation( this.factory.create( HyperbolicCosine.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         HyperbolicCosine.class );
    }

    @Test
    public void shouldCreateHyperbolicSineOperation() {
        assertOperation( this.factory.create( HyperbolicSine.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         HyperbolicSine.class );
    }

    @Test
    public void shouldCreateHyperbolicTangentOperation() {
        assertOperation( this.factory.create( HyperbolicTangent.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         HyperbolicTangent.class );
    }

    @Test
    public void shouldCreateIncrementOperation() {
        assertOperation( this.factory.create( Increment.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Increment.class );
    }

    @Test
    public void shouldCreateLog10Operation() {
        assertOperation( this.factory.create( Log10.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Log10.class );
    }

    @Test
    public void shouldCreateLogOperation() {
        assertOperation( this.factory.create( Log.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Log.class );
    }

    @Test
    public void shouldCreateMaxOperation() {
        assertOperation( this.factory.create( Max.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Max.class );
    }

    @Test
    public void shouldCreateMedianOperation() {
        assertOperation( this.factory.create( Median.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Median.class );
    }

    @Test
    public void shouldCreateMinOperation() {
        assertOperation( this.factory.create( Min.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Min.class );
    }

    @Test
    public void shouldCreateModeOperation() {
        assertOperation( this.factory.create( Mode.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Mode.class );
    }

    @Test
    public void shouldCreateModulusOperation() {
        assertOperation( this.factory.create( Modulus.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Modulus.class );
    }

    @Test
    public void shouldCreateMultiplyOperation() {
        assertOperation( this.factory.create( Multiply.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Multiply.class );
    }

    @Test
    public void shouldCreateParseDoubleOperation() {
        assertOperation( this.factory.create( ParseDouble.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         ParseDouble.class );
    }

    @Test
    public void shouldCreatePowerOfEMinus1Operation() {
        assertOperation( this.factory.create( PowerOfEMinus1.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         PowerOfEMinus1.class );
    }

    @Test
    public void shouldCreatePowerOfEOperation() {
        assertOperation( this.factory.create( PowerOfE.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         PowerOfE.class );
    }

    @Test
    public void shouldCreatePowerOperation() {
        assertOperation( this.factory.create( Power.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Power.class );
    }

    @Test
    public void shouldCreateRandomOperation() {
        assertOperation( this.factory.create( Random.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Random.class );
    }

    @Test
    public void shouldCreateRoundOperation() {
        assertOperation( this.factory.create( Round.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Round.class );
    }

    @Test
    public void shouldCreateSignOperation() {
        assertOperation( this.factory.create( Sign.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Sign.class );
    }

    @Test
    public void shouldCreateSineOperation() {
        assertOperation( this.factory.create( Sine.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Sine.class );
    }

    @Test
    public void shouldCreateSquareRootOperation() {
        assertOperation( this.factory.create( SquareRoot.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         SquareRoot.class );
    }

    @Test
    public void shouldCreateSubtractOperation() {
        assertOperation( this.factory.create( Subtract.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Subtract.class );
    }

    @Test
    public void shouldCreateTangentOperation() {
        assertOperation( this.factory.create( Tangent.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         Tangent.class );
    }

    @Test
    public void shouldCreateToDegreesOperation() {
        assertOperation( this.factory.create( ToDegrees.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         ToDegrees.class );
    }

    @Test
    public void shouldCreateToRadiansOperation() {
        assertOperation( this.factory.create( ToRadians.DESCRIPTOR, TestConstants.ID, TestConstants.TRANSFORM_ID ),
                         ToRadians.class );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullCategory() {
        this.factory.descriptors( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullDescriptor() {
        this.factory.create( null, TestConstants.ID, TestConstants.TRANSFORM_ID );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullId() {
        this.factory.create( AbsoluteValue.OUTPUT, null, TestConstants.TRANSFORM_ID );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullTransformId() {
        this.factory.create( AbsoluteValue.OUTPUT, TestConstants.ID, null );
    }

    @Test( expected = IllegalStateException.class )
    public void shouldNotHandleUnknownDescriptor() {
        final Operation.Descriptor bogus = new Operation.Descriptor() {

            @Override
            public String abbreviation() {
                return null;
            }

            @Override
            public Category category() {
                return null;
            }

            @Override
            public String description() {
                return null;
            }

            @Override
            public String name() {
                return null;
            }

        };

        this.factory.create( bogus, TestConstants.ID, TestConstants.TRANSFORM_ID );
    }

}
