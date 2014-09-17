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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.chrysalix.Chrysalix;
import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.chrysalix.transformation.Operation;
import org.chrysalix.transformation.OperationDescriptor;
import org.chrysalix.transformation.Transformation;
import org.chrysalix.transformation.TransformationFactory;
import org.chrysalix.transformation.ValidationProblem;
import org.chrysalix.transformation.Value;
import org.chrysalix.transformation.ValueDescriptor;
import org.chrysalix.transformation.OperationCategory.BuiltInCategory;

/**
 * Computes the median value of a collection of number terms.
 */
public final class Median extends AbstractOperation< Number > {

    /**
     * The input term descriptor.
     */
    public static final ValueDescriptor< Number > TERM_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Median.class, "input" ),
                                                     ChrysalixI18n.medianOperationInputDescription.text(),
                                                     ChrysalixI18n.medianOperationInputName.text(),
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
    public static final OperationDescriptor< Number > DESCRIPTOR =
        new AbstractOperationDescriptor< Number >( TransformationFactory.createId( Median.class ),
                                                   ChrysalixI18n.medianOperationDescription.text(),
                                                   ChrysalixI18n.medianOperationName.text(),
                                                   Number.class,
                                                   INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.chrysalix.transformation.Transformation)
             */
            @Override
            public Operation< Number > newInstance( final Transformation transformation ) {
                return new Median( transformation );
            }

        };

    /**
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Median( final Transformation transformation ) {
        super( DESCRIPTOR, transformation );

        try {
            addCategory( BuiltInCategory.ARITHMETIC );
        } catch ( final ChrysalixException e ) {
            Chrysalix.LOGGER.error( e, ChrysalixI18n.errorAddingBuiltInCategory, transformationId() );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#calculate()
     */
    @SuppressWarnings( "unchecked" )
    @Override
    protected Number calculate() throws ChrysalixException {
        assert !problems().isError();

        int size = 0;
        List< Value< Number >> numberTerms = null;

        { // convert terms to number terms
            final List< Value< ? >> terms = inputs();
            size = terms.size();
            numberTerms = new ArrayList<>( size );

            // OK to cast since this method should not be run if there is a non-number term
            for ( final Value< ? > term : terms ) {
                numberTerms.add( ( Value< Number > ) term );
            }

            // sort values
            Collections.sort( numberTerms, Value.ASCENDING_NUMBER_SORTER );
        }

        final boolean even = ( ( size & 1 ) == 0 );
        final int halfwayIndex = ( size / 2 );

        if ( even ) {
            final Value< ? > first = numberTerms.get( halfwayIndex - 1 );
            final Value< ? > second = numberTerms.get( halfwayIndex );
            final Add addOp = new Add( transformation() );

            try {
                addOp.addInput( Add.TERM_DESCRIPTOR.id(), first, second );
                final Number sum = addOp.get();

                final Divide divideOp = new Divide( transformation() );
                divideOp.addInput( Divide.TERM_DESCRIPTOR.id(), sum, 2 );

                return divideOp.get();
            } catch ( final ChrysalixException e ) {
                final ValidationProblem problem =
                    TransformationFactory.createError( transformationId(),
                                                       ChrysalixI18n.medianOperationError.text( transformationId() ) );
                problems().add( problem );
                Chrysalix.LOGGER.error( e, ChrysalixI18n.medianOperationError, transformationId() );
            }
        }

        return numberTerms.get( halfwayIndex ).get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#validate()
     */
    @Override
    protected void validate() {
        // make sure there are terms
        if ( inputs().isEmpty() ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   ChrysalixI18n.medianOperationHasNoTerms.text( transformationId() ) );
            problems().add( problem );
        } else {
            if ( inputs().size() < INPUT_DESCRIPTORS[ 0 ].requiredValueCount() ) {
                final ValidationProblem problem =
                    TransformationFactory.createError( transformationId(),
                                                       ChrysalixI18n.invalidTermCount.text( name(),
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
                                                               ChrysalixI18n.invalidTermType.text( name(),
                                                                                                     transformationId() ) );
                        problems().add( problem );
                    }
                } catch ( final ChrysalixException e ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.operationValidationError.text( name(),
                                                                                                          transformationId() ) );
                    problems().add( problem );
                    Chrysalix.LOGGER.error( e, ChrysalixI18n.message, problem.message() );
                }
            }
        }
    }

}
