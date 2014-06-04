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

import javax.xml.namespace.QName;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.CheckArg;
import org.polyglotter.grammar.Operation;
import org.polyglotter.grammar.Operation.Category;
import org.polyglotter.grammar.Operation.Descriptor;

/**
 * A factory for {@link Operation operations}.
 */
public final class OperationFactory {

    private static List< Descriptor > DESCRIPTORS;

    static {
        final List< Descriptor > temp = new ArrayList<>();
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

        Collections.sort( temp, Operation.DESCRIPTOR_NAME_SORTER );
        DESCRIPTORS = Collections.unmodifiableList( temp );
    }

    /**
     * @param descriptor
     *        the descriptor of the operation being created (cannot be <code>null</code>)
     * @param id
     *        the ID to use for the operation being created (cannot be <code>null</code>)
     * @param transformId
     *        the ID of the transform the operation belongs to (cannot be <code>null</code>)
     * @return the new operation (never <code>null</code>)
     */
    public Operation< ? > create( final Descriptor descriptor,
                                  final QName id,
                                  final QName transformId ) {
        CheckArg.notNull( descriptor, "descriptor" );
        CheckArg.notNull( id, "id" );
        CheckArg.notNull( transformId, "transformId" );

        if ( descriptor == AbsoluteValue.DESCRIPTOR ) return new AbsoluteValue( id, transformId );
        if ( descriptor == Add.DESCRIPTOR ) return new Add( id, transformId );
        if ( descriptor == ArcCosine.DESCRIPTOR ) return new ArcCosine( id, transformId );
        if ( descriptor == ArcSine.DESCRIPTOR ) return new ArcSine( id, transformId );
        if ( descriptor == ArcTangent.DESCRIPTOR ) return new ArcTangent( id, transformId );
        if ( descriptor == Average.DESCRIPTOR ) return new Average( id, transformId );
        if ( descriptor == Ceiling.DESCRIPTOR ) return new Ceiling( id, transformId );
        if ( descriptor == Concat.DESCRIPTOR ) return new Concat( id, transformId );
        if ( descriptor == Cosine.DESCRIPTOR ) return new Cosine( id, transformId );
        if ( descriptor == Count.DESCRIPTOR ) return new Count( id, transformId );
        if ( descriptor == CubeRoot.DESCRIPTOR ) return new CubeRoot( id, transformId );
        if ( descriptor == Decrement.DESCRIPTOR ) return new Decrement( id, transformId );
        if ( descriptor == Divide.DESCRIPTOR ) return new Divide( id, transformId );
        if ( descriptor == Floor.DESCRIPTOR ) return new Floor( id, transformId );
        if ( descriptor == HyperbolicCosine.DESCRIPTOR ) return new HyperbolicCosine( id, transformId );
        if ( descriptor == HyperbolicSine.DESCRIPTOR ) return new HyperbolicSine( id, transformId );
        if ( descriptor == HyperbolicTangent.DESCRIPTOR ) return new HyperbolicTangent( id, transformId );
        if ( descriptor == Increment.DESCRIPTOR ) return new Increment( id, transformId );
        if ( descriptor == Log.DESCRIPTOR ) return new Log( id, transformId );
        if ( descriptor == Log10.DESCRIPTOR ) return new Log10( id, transformId );
        if ( descriptor == Max.DESCRIPTOR ) return new Max( id, transformId );
        if ( descriptor == Median.DESCRIPTOR ) return new Median( id, transformId );
        if ( descriptor == Min.DESCRIPTOR ) return new Min( id, transformId );
        if ( descriptor == Mode.DESCRIPTOR ) return new Mode( id, transformId );
        if ( descriptor == Modulus.DESCRIPTOR ) return new Modulus( id, transformId );
        if ( descriptor == Multiply.DESCRIPTOR ) return new Multiply( id, transformId );
        if ( descriptor == ParseDouble.DESCRIPTOR ) return new ParseDouble( id, transformId );
        if ( descriptor == Power.DESCRIPTOR ) return new Power( id, transformId );
        if ( descriptor == PowerOfE.DESCRIPTOR ) return new PowerOfE( id, transformId );
        if ( descriptor == PowerOfEMinus1.DESCRIPTOR ) return new PowerOfEMinus1( id, transformId );
        if ( descriptor == Random.DESCRIPTOR ) return new Random( id, transformId );
        if ( descriptor == Round.DESCRIPTOR ) return new Round( id, transformId );
        if ( descriptor == Sign.DESCRIPTOR ) return new Sign( id, transformId );
        if ( descriptor == Sine.DESCRIPTOR ) return new Sine( id, transformId );
        if ( descriptor == SquareRoot.DESCRIPTOR ) return new SquareRoot( id, transformId );
        if ( descriptor == Subtract.DESCRIPTOR ) return new Subtract( id, transformId );
        if ( descriptor == Tangent.DESCRIPTOR ) return new Tangent( id, transformId );
        if ( descriptor == ToDegrees.DESCRIPTOR ) return new ToDegrees( id, transformId );
        if ( descriptor == ToRadians.DESCRIPTOR ) return new ToRadians( id, transformId );

        throw new IllegalStateException( PolyglotterI18n.unhandledOperationDescriptor.text( descriptor.name() ) );
    }

    /**
     * @return an unmodifiable list of descriptors sorted by operation name (never <code>null</code> or empty)
     */
    public List< Descriptor > descriptors() {
        return DESCRIPTORS;
    }

    /**
     * @param category
     *        the category whose descriptors are being requested (cannot be <code>null</code>)
     * @return the descriptors (never <code>null</code> but can be empty)
     */
    public List< Descriptor > descriptors( final Category category ) {
        CheckArg.notNull( category, "category" );
        final List< Descriptor > result = new ArrayList<>();

        for ( final Descriptor descriptor : DESCRIPTORS ) {
            if ( descriptor.category() == category ) result.add( descriptor );
        }

        return result;
    }

}
