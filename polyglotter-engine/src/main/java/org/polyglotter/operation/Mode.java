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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.transformation.Operation;
import org.polyglotter.transformation.OperationCategory.BuiltInCategory;
import org.polyglotter.transformation.OperationDescriptor;
import org.polyglotter.transformation.Transformation;
import org.polyglotter.transformation.TransformationFactory;
import org.polyglotter.transformation.ValidationProblem;
import org.polyglotter.transformation.Value;
import org.polyglotter.transformation.ValueDescriptor;

/**
 * Calculates the mode, or most common term, of a collection of terms.
 */
public final class Mode extends AbstractOperation< Number[] > {

    /**
     * The input term descriptor.
     */
    public static final ValueDescriptor< Number > TERM_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Mode.class, "input" ),
                                                     PolyglotterI18n.modeOperationInputDescription.text(),
                                                     PolyglotterI18n.modeOperationInputName.text(),
                                                     Number.class,
                                                     true,
                                                     1,
                                                     true );

    /**
     * The input descriptors.
     */
    private static final ValueDescriptor< ? >[] INPUT_DESCRIPTORS = { TERM_DESCRIPTOR };

    /**
     * The output descriptor.
     */
    public static final OperationDescriptor< Number[] > DESCRIPTOR =
        new AbstractOperationDescriptor< Number[] >( TransformationFactory.createId( Mode.class ),
                                                     PolyglotterI18n.modeOperationDescription.text(),
                                                     PolyglotterI18n.modeOperationName.text(),
                                                     Number[].class,
                                                     INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.polyglotter.transformation.OperationDescriptor#newInstance(org.polyglotter.transformation.Transformation)
             */
            @Override
            public Operation< Number[] > newInstance( final Transformation transformation ) {
                return new Mode( transformation );
            }

        };

    /**
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Mode( final Transformation transformation ) {
        super( DESCRIPTOR, transformation );

        try {
            addCategory( BuiltInCategory.ARITHMETIC );
        } catch ( final PolyglotterException e ) {
            this.logger.error( e, PolyglotterI18n.errorAddingBuiltInCategory, transformationId() );
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * When there is no mode an empty array will be returned.
     * 
     * @see org.polyglotter.operation.AbstractOperation#calculate()
     */
    @Override
    protected Number[] calculate() throws PolyglotterException {
        assert !problems().isError();

        final Map< Number, Integer > result = new HashMap<>();
        int max = 0;
        final List< Number > maxElems = new ArrayList<>();

        for ( final Value< ? > term : inputs() ) {
            final Number value = ( Number ) term.get();

            if ( result.containsKey( value ) ) {
                result.put( value, ( result.get( value ) + 1 ) );
            } else {
                result.put( value, 1 );
            }

            if ( result.get( value ) > max ) {
                max = result.get( value );
                maxElems.clear();
                maxElems.add( value );
            } else if ( result.get( value ) == max ) {
                maxElems.add( value );
            }
        }

        if ( maxElems.size() == inputs().size() ) return new Number[ 0 ];
        return maxElems.toArray( new Number[ maxElems.size() ] );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.AbstractOperation#validate()
     */
    @Override
    protected void validate() {
        // make sure there are terms
        if ( inputs().isEmpty() ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   PolyglotterI18n.modeOperationHasNoTerms.text( transformationId() ) );
            problems().add( problem );
        } else {
            if ( inputs().size() < INPUT_DESCRIPTORS[ 0 ].requiredValueCount() ) {
                final ValidationProblem problem =
                    TransformationFactory.createError( transformationId(),
                                                       PolyglotterI18n.invalidTermCount.text( name(),
                                                                                              transformationId(),
                                                                                              inputs().size() ) );
                problems().add( problem );
            }

            // make sure all the terms have types of Number
            for ( final Value< ? > term : inputs() ) {
                Object value;

                try {
                    value = term.get();

                    if ( !( value instanceof Number ) ) {
                        final ValidationProblem problem =
                            TransformationFactory.createError( transformationId(),
                                                               PolyglotterI18n.invalidTermType.text( name(),
                                                                                                     transformationId() ) );
                        problems().add( problem );
                    }
                } catch ( final PolyglotterException e ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           PolyglotterI18n.operationValidationError.text( name(),
                                                                                                          transformationId() ) );
                    problems().add( problem );
                    this.logger.error( e, PolyglotterI18n.message, problem.message() );
                }
            }
        }
    }

}
