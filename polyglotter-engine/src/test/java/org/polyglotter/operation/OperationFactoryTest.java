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
import org.polyglotter.grammar.Operation;
import org.polyglotter.grammar.Operation.Category;
import org.polyglotter.grammar.TestConstants;

@SuppressWarnings( "javadoc" )
public final class OperationFactoryTest implements TestConstants {

    private OperationFactory factory;

    @Before
    public void beforeEach() {
        this.factory = new OperationFactory();
    }

    @Test
    public void shouldCreateAbsoluteValueOperation() {
        assertThat( this.factory.create( AbsoluteValue.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( AbsoluteValue.class ) ) );
    }

    @Test
    public void shouldCreateAddOperation() {
        assertThat( this.factory.create( Add.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Add.class ) ) );
    }

    @Test
    public void shouldCreateArcCosineOperation() {
        assertThat( this.factory.create( ArcCosine.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( ArcCosine.class ) ) );
    }

    @Test
    public void shouldCreateArcSineOperation() {
        assertThat( this.factory.create( ArcSine.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( ArcSine.class ) ) );
    }

    @Test
    public void shouldCreateArcTangentOperation() {
        assertThat( this.factory.create( ArcTangent.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( ArcTangent.class ) ) );
    }

    @Test
    public void shouldCreateAverageOperation() {
        assertThat( this.factory.create( Average.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Average.class ) ) );
    }

    @Test
    public void shouldCreateCeilingOperation() {
        assertThat( this.factory.create( Ceiling.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Ceiling.class ) ) );
    }

    @Test
    public void shouldCreateConcatOperation() {
        assertThat( this.factory.create( Concat.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Concat.class ) ) );
    }

    @Test
    public void shouldCreateCosineOperation() {
        assertThat( this.factory.create( Cosine.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Cosine.class ) ) );
    }

    @Test
    public void shouldCreateCountOperation() {
        assertThat( this.factory.create( Count.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Count.class ) ) );
    }

    @Test
    public void shouldCreateCubeRootOperation() {
        assertThat( this.factory.create( CubeRoot.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( CubeRoot.class ) ) );
    }

    @Test
    public void shouldCreateDecrementOperation() {
        assertThat( this.factory.create( Decrement.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Decrement.class ) ) );
    }

    @Test
    public void shouldCreateDivideOperation() {
        assertThat( this.factory.create( Divide.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Divide.class ) ) );
    }

    @Test
    public void shouldCreateFloorOperation() {
        assertThat( this.factory.create( Floor.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Floor.class ) ) );
    }

    @Test
    public void shouldCreateHyperbolicCosineOperation() {
        assertThat( this.factory.create( HyperbolicCosine.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( HyperbolicCosine.class ) ) );
    }

    @Test
    public void shouldCreateHyperbolicSineOperation() {
        assertThat( this.factory.create( HyperbolicSine.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( HyperbolicSine.class ) ) );
    }

    @Test
    public void shouldCreateHyperbolicTangentOperation() {
        assertThat( this.factory.create( HyperbolicTangent.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( HyperbolicTangent.class ) ) );
    }

    @Test
    public void shouldCreateIncrementOperation() {
        assertThat( this.factory.create( Increment.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Increment.class ) ) );
    }

    @Test
    public void shouldCreateLog10Operation() {
        assertThat( this.factory.create( Log10.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Log10.class ) ) );
    }

    @Test
    public void shouldCreateLogOperation() {
        assertThat( this.factory.create( Log.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Log.class ) ) );
    }

    @Test
    public void shouldCreateMaxOperation() {
        assertThat( this.factory.create( Max.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Max.class ) ) );
    }

    @Test
    public void shouldCreateMedianOperation() {
        assertThat( this.factory.create( Median.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Median.class ) ) );
    }

    @Test
    public void shouldCreateMinOperation() {
        assertThat( this.factory.create( Min.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Min.class ) ) );
    }

    @Test
    public void shouldCreateModeOperation() {
        assertThat( this.factory.create( Mode.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Mode.class ) ) );
    }

    @Test
    public void shouldCreateModulusOperation() {
        assertThat( this.factory.create( Modulus.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Modulus.class ) ) );
    }

    @Test
    public void shouldCreateMultiplyOperation() {
        assertThat( this.factory.create( Multiply.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Multiply.class ) ) );
    }

    @Test
    public void shouldCreateParseDoubleOperation() {
        assertThat( this.factory.create( ParseDouble.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( ParseDouble.class ) ) );
    }

    @Test
    public void shouldCreatePowerOfEMinus1Operation() {
        assertThat( this.factory.create( PowerOfEMinus1.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( PowerOfEMinus1.class ) ) );
    }

    @Test
    public void shouldCreatePowerOfEOperation() {
        assertThat( this.factory.create( PowerOfE.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( PowerOfE.class ) ) );
    }

    @Test
    public void shouldCreatePowerOperation() {
        assertThat( this.factory.create( Power.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Power.class ) ) );
    }

    @Test
    public void shouldCreateRandomOperation() {
        assertThat( this.factory.create( Random.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Random.class ) ) );
    }

    @Test
    public void shouldCreateRoundOperation() {
        assertThat( this.factory.create( Round.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Round.class ) ) );
    }

    @Test
    public void shouldCreateSignOperation() {
        assertThat( this.factory.create( Sign.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Sign.class ) ) );
    }

    @Test
    public void shouldCreateSineOperation() {
        assertThat( this.factory.create( Sine.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Sine.class ) ) );
    }

    @Test
    public void shouldCreateSquareRootOperation() {
        assertThat( this.factory.create( SquareRoot.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( SquareRoot.class ) ) );
    }

    @Test
    public void shouldCreateSubtractOperation() {
        assertThat( this.factory.create( Subtract.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Subtract.class ) ) );
    }

    @Test
    public void shouldCreateTangentOperation() {
        assertThat( this.factory.create( Tangent.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( Tangent.class ) ) );
    }

    @Test
    public void shouldCreateToDegreesOperation() {
        assertThat( this.factory.create( ToDegrees.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( ToDegrees.class ) ) );
    }

    @Test
    public void shouldCreateToRadiansOperation() {
        assertThat( this.factory.create( ToRadians.DESCRIPTOR, ID, TRANSFORM_ID ), is( instanceOf( ToRadians.class ) ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullCategory() {
        this.factory.descriptors( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullDescriptor() {
        this.factory.create( null, ID, TRANSFORM_ID );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullId() {
        this.factory.create( AbsoluteValue.DESCRIPTOR, null, TRANSFORM_ID );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullTransformId() {
        this.factory.create( AbsoluteValue.DESCRIPTOR, ID, null );
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

        this.factory.create( bogus, ID, TRANSFORM_ID );
    }

}
