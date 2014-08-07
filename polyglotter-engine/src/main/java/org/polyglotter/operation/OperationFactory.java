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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.CheckArg;
import org.polyglotter.transformation.Operation;
import org.polyglotter.transformation.Transformation;
import org.polyglotter.transformation.ValueDescriptor;

/**
 * A factory for {@link Operation operations}.
 */
public final class OperationFactory {

    private static List< ValueDescriptor< ? > > DESCRIPTORS;

    static {
        final List< ValueDescriptor< ? > > temp = new ArrayList<>();
        temp.add( AbsoluteValue.DESCRIPTOR );
        temp.add( Add.DESCRIPTOR );
        temp.add( ArcCosine.DESCRIPTOR );
        temp.add( ArcSine.DESCRIPTOR );
        temp.add( ArcTangent.DESCRIPTOR );
        temp.add( Average.DESCRIPTOR );
        temp.add( Ceiling.DESCRIPTOR );
        temp.add( Concat.DESCRIPTOR );
        temp.add( Cosine.DESCRIPTOR );
        temp.add( Count.DESCRIPTOR );
        temp.add( CubeRoot.DESCRIPTOR );
        temp.add( Decrement.DESCRIPTOR );
        temp.add( Divide.DESCRIPTOR );
        temp.add( Floor.DESCRIPTOR );
        temp.add( HyperbolicCosine.DESCRIPTOR );
        temp.add( HyperbolicSine.DESCRIPTOR );
        temp.add( HyperbolicTangent.DESCRIPTOR );
        temp.add( Increment.DESCRIPTOR );
        temp.add( Log.DESCRIPTOR );
        temp.add( Log10.DESCRIPTOR );
        temp.add( Max.DESCRIPTOR );
        temp.add( Median.DESCRIPTOR );
        temp.add( Min.DESCRIPTOR );
        temp.add( Mode.DESCRIPTOR );
        temp.add( Modulus.DESCRIPTOR );
        temp.add( Multiply.DESCRIPTOR );
        temp.add( ParseDouble.DESCRIPTOR );
        temp.add( Power.DESCRIPTOR );
        temp.add( PowerOfE.DESCRIPTOR );
        temp.add( PowerOfEMinus1.DESCRIPTOR );
        temp.add( Random.DESCRIPTOR );
        temp.add( Round.DESCRIPTOR );
        temp.add( Sign.DESCRIPTOR );
        temp.add( Sine.DESCRIPTOR );
        temp.add( SquareRoot.DESCRIPTOR );
        temp.add( Subtract.DESCRIPTOR );
        temp.add( Tangent.DESCRIPTOR );
        temp.add( ToDegrees.DESCRIPTOR );
        temp.add( ToRadians.DESCRIPTOR );

        Collections.sort( temp, ValueDescriptor.NAME_SORTER );
        DESCRIPTORS = Collections.unmodifiableList( temp );
    }

    /**
     * @param descriptor
     *        the descriptor of the operation being created (cannot be <code>null</code>)
     * @param transformation
     *        the transformation the operation belongs to (cannot be <code>null</code>)
     * @return the new operation (never <code>null</code>)
     */
    public Operation< ? > create( final ValueDescriptor< ? > descriptor,
                                  final Transformation transformation ) {
        CheckArg.notNull( descriptor, "descriptor" );
        CheckArg.notNull( transformation, "transformation" );

        if ( descriptor == AbsoluteValue.DESCRIPTOR ) return new AbsoluteValue( transformation );
        if ( descriptor == Add.DESCRIPTOR ) return new Add( transformation );
        if ( descriptor == ArcCosine.DESCRIPTOR ) return new ArcCosine( transformation );
        if ( descriptor == ArcSine.DESCRIPTOR ) return new ArcSine( transformation );
        if ( descriptor == ArcTangent.DESCRIPTOR ) return new ArcTangent( transformation );
        if ( descriptor == Average.DESCRIPTOR ) return new Average( transformation );
        if ( descriptor == Ceiling.DESCRIPTOR ) return new Ceiling( transformation );
        if ( descriptor == Concat.DESCRIPTOR ) return new Concat( transformation );
        if ( descriptor == Cosine.DESCRIPTOR ) return new Cosine( transformation );
        if ( descriptor == Count.DESCRIPTOR ) return new Count( transformation );
        if ( descriptor == CubeRoot.DESCRIPTOR ) return new CubeRoot( transformation );
        if ( descriptor == Decrement.DESCRIPTOR ) return new Decrement( transformation );
        if ( descriptor == Divide.DESCRIPTOR ) return new Divide( transformation );
        if ( descriptor == Floor.DESCRIPTOR ) return new Floor( transformation );
        if ( descriptor == HyperbolicCosine.DESCRIPTOR ) return new HyperbolicCosine( transformation );
        if ( descriptor == HyperbolicSine.DESCRIPTOR ) return new HyperbolicSine( transformation );
        if ( descriptor == HyperbolicTangent.DESCRIPTOR ) return new HyperbolicTangent( transformation );
        if ( descriptor == Increment.DESCRIPTOR ) return new Increment( transformation );
        if ( descriptor == Log.DESCRIPTOR ) return new Log( transformation );
        if ( descriptor == Log10.DESCRIPTOR ) return new Log10( transformation );
        if ( descriptor == Max.DESCRIPTOR ) return new Max( transformation );
        if ( descriptor == Median.DESCRIPTOR ) return new Median( transformation );
        if ( descriptor == Min.DESCRIPTOR ) return new Min( transformation );
        if ( descriptor == Mode.DESCRIPTOR ) return new Mode( transformation );
        if ( descriptor == Modulus.DESCRIPTOR ) return new Modulus( transformation );
        if ( descriptor == Multiply.DESCRIPTOR ) return new Multiply( transformation );
        if ( descriptor == ParseDouble.DESCRIPTOR ) return new ParseDouble( transformation );
        if ( descriptor == Power.DESCRIPTOR ) return new Power( transformation );
        if ( descriptor == PowerOfE.DESCRIPTOR ) return new PowerOfE( transformation );
        if ( descriptor == PowerOfEMinus1.DESCRIPTOR ) return new PowerOfEMinus1( transformation );
        if ( descriptor == Random.DESCRIPTOR ) return new Random( transformation );
        if ( descriptor == Round.DESCRIPTOR ) return new Round( transformation );
        if ( descriptor == Sign.DESCRIPTOR ) return new Sign( transformation );
        if ( descriptor == Sine.DESCRIPTOR ) return new Sine( transformation );
        if ( descriptor == SquareRoot.DESCRIPTOR ) return new SquareRoot( transformation );
        if ( descriptor == Subtract.DESCRIPTOR ) return new Subtract( transformation );
        if ( descriptor == Tangent.DESCRIPTOR ) return new Tangent( transformation );
        if ( descriptor == ToDegrees.DESCRIPTOR ) return new ToDegrees( transformation );
        if ( descriptor == ToRadians.DESCRIPTOR ) return new ToRadians( transformation );

        throw new IllegalStateException( PolyglotterI18n.unhandledOperationDescriptor.text( descriptor.name() ) );
    }

    /**
     * @return an unmodifiable list of descriptors sorted by operation name (never <code>null</code> or empty)
     */
    public List< ValueDescriptor< ? > > descriptors() {
        return DESCRIPTORS;
    }

}
