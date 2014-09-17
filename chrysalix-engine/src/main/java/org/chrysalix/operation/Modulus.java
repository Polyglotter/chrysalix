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
 * Calculates the modulus (remainder) of the first term divided by the second term.
 */
public final class Modulus extends AbstractOperation< Double > {

    /**
     * The dividend descriptor.
     */
    public static final ValueDescriptor< Number > DIVIDEND_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Modulus.class, "dividend" ),
                                                     ChrysalixI18n.modulusOperationDividendDescription.text(),
                                                     ChrysalixI18n.modulusOperationDividendName.text(),
                                                     Number.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The dividend descriptor.
     */
    public static final ValueDescriptor< Number > DIVISOR_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Modulus.class, "divisor" ),
                                                     ChrysalixI18n.modulusOperationDivisorDescription.text(),
                                                     ChrysalixI18n.modulusOperationDivisorName.text(),
                                                     Number.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The input descriptors.
     */
    private static final ValueDescriptor< ? >[] INPUT_DESCRIPTORS = { DIVIDEND_DESCRIPTOR, DIVISOR_DESCRIPTOR };

    /**
     * The output descriptor.
     */
    public static final OperationDescriptor< Double > DESCRIPTOR =
        new AbstractOperationDescriptor< Double >( TransformationFactory.createId( Modulus.class ),
                                                   ChrysalixI18n.modulusOperationDescription.text(),
                                                   ChrysalixI18n.modulusOperationName.text(),
                                                   Double.class,
                                                   INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.chrysalix.transformation.Transformation)
             */
            @Override
            public Operation< Double > newInstance( final Transformation transformation ) {
                return new Modulus( transformation );
            }

        };

    /**
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Modulus( final Transformation transformation ) {
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
    @Override
    protected Double calculate() throws ChrysalixException {
        assert !problems().isError();

        final Number dividend = ( Number ) inputs( DIVIDEND_DESCRIPTOR.id() ).get( 0 ).get();
        final Number divisor = ( Number ) inputs( DIVISOR_DESCRIPTOR.id() ).get( 0 ).get();

        return ( dividend.doubleValue() % divisor.doubleValue() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#validate()
     */
    @Override
    protected void validate() {
        // make sure there are terms
        if ( inputs().size() != 2 ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   ChrysalixI18n.modulusOperationInvalidTermCount.text( transformationId() ) );
            problems().add( problem );
        } else {
            { // dividend
                final List< Value< ? >> dividendValues = inputs( DIVIDEND_DESCRIPTOR.id() );

                if ( dividendValues.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.modulusOperationInvalidDividendCount.text( transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = dividendValues.get( 0 );
                    Object x;

                    try {
                        x = term.get();

                        if ( !( x instanceof Number ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.modulusOperationInvalidDividendTermType.text( transformationId() ) );
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

            { // divisor
                final List< Value< ? >> divisorValues = inputs( DIVISOR_DESCRIPTOR.id() );

                if ( divisorValues.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.modulusOperationInvalidDivisorCount.text( transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = divisorValues.get( 0 );
                    Object y;

                    try {
                        y = term.get();

                        if ( !( y instanceof Number ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.modulusOperationInvalidDivisorTermType.text( transformationId() ) );
                            problems().add( problem );
                        }
                    } catch ( final ChrysalixException e ) {
                        final ValidationProblem problem =
                            TransformationFactory.createError( transformationId(), ChrysalixI18n.operationValidationError.text( name(),
                                                                                                                                  transformationId() ) );
                        problems().add( problem );
                        Chrysalix.LOGGER.error( e, ChrysalixI18n.message, problem.message() );
                    }
                }
            }
        }
    }

}
