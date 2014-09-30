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

import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.chrysalix.transformation.Operation;
import org.chrysalix.transformation.OperationDescriptor;
import org.chrysalix.transformation.Transformation;
import org.chrysalix.transformation.TransformationFactory;
import org.chrysalix.transformation.ValidationProblem;
import org.chrysalix.transformation.ValidationProblems;
import org.chrysalix.transformation.Value;
import org.chrysalix.transformation.ValueDescriptor;
import org.modelspace.ModelObject;
import org.modelspace.ModelspaceException;

/**
 * Calculates the modulus (remainder) of the first term divided by the second term.
 */
public final class Modulus extends AbstractOperation< Double > {

    static final String DESCRIPTION = "Calculates the remainder of the first term divided by the second term";
    private static final String DIVIDEND_DESCRIPTION = "The input term being divided by the divisor.";
    private static final String DIVIDEND_NAME = "Dividend";
    private static final String INVALID_DIVIDEND_TERM_TYPE =
        "The dividend term of modulus operation in transformation '%s' must be a number";
    private static final String DIVISOR_DESCRIPTION = "The input term divided into the dividend.";
    private static final String DIVISOR_NAME = "Divisor";
    private static final String INVALID_DIVISOR_TERM_TYPE =
        "The divisor term of modulus operation in transformation '%s' must be a number";
    private static final String INVALID_DIVIDEND_COUNT =
        "Modulus operation in transformation '%s' must have exactly one dividend term";
    private static final String INVALID_DIVISOR_COUNT =
        "Modulus operation in transformation '%s' must have exactly one divisor term";
    static final String NAME = "Modulus";

    /**
     * The dividend descriptor.
     */
    public static final ValueDescriptor< Number > DIVIDEND_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Modulus.class, "dividend" ),
                                                     ChrysalixI18n.localize( DIVIDEND_DESCRIPTION ),
                                                     ChrysalixI18n.localize( DIVIDEND_NAME ),
                                                     Number.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The dividend descriptor.
     */
    public static final ValueDescriptor< Number > DIVISOR_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Modulus.class, "divisor" ),
                                                     ChrysalixI18n.localize( DIVISOR_DESCRIPTION ),
                                                     ChrysalixI18n.localize( DIVISOR_NAME ),
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
                                                   ChrysalixI18n.localize( DESCRIPTION ),
                                                   ChrysalixI18n.localize( NAME ),
                                                   Double.class,
                                                   INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.modelspace.ModelObject,
             *      org.chrysalix.transformation.Transformation)
             */
            @Override
            public Operation< Double > newInstance( final ModelObject operation,
                                                    final Transformation transformation ) throws ModelspaceException, ChrysalixException {
                return new Modulus( operation, transformation );
            }

        };

    /**
     * @param operation
     *        the operation model object (cannot be <code>null</code>)
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws ModelspaceException
     *         if an error with the model object occurs
     * @throws ChrysalixException
     *         if a non-model object error occurs
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Modulus( final ModelObject operation,
             final Transformation transformation ) throws ModelspaceException, ChrysalixException {
        super( operation, transformation );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#calculate()
     */
    @Override
    protected Double calculate() throws ChrysalixException {
        assert !problems().isError();
        final Number dividend = ( Number ) inputs( DIVIDEND_DESCRIPTOR.name() ).get( 0 ).get();
        final Number divisor = ( Number ) inputs( DIVISOR_DESCRIPTOR.name() ).get( 0 ).get();

        return ( dividend.doubleValue() % divisor.doubleValue() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#problems()
     */
    @Override
    public ValidationProblems problems() throws ChrysalixException {
        this.problems.clear();

        // make sure there are terms
        if ( inputs().length != 2 ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   ChrysalixI18n.localize( AbstractOperation.INVALID_TERM_COUNT,
                                                                           NAME,
                                                                           transformationId(),
                                                                           inputs().length ) );
            problems().add( problem );
        } else {
            { // dividend
                final List< Value< ? >> dividendValues = inputs( DIVIDEND_DESCRIPTOR.name() );

                if ( dividendValues.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.localize( INVALID_DIVIDEND_COUNT, transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = dividendValues.get( 0 );
                    Object x;

                    try {
                        x = term.get();

                        if ( !( x instanceof Number ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.localize( INVALID_DIVIDEND_TERM_TYPE,
                                                                                           transformationId() ) );
                            problems().add( problem );
                        }
                    } catch ( final ChrysalixException e ) {
                        final ValidationProblem problem =
                            TransformationFactory.createError( transformationId(),
                                                               ChrysalixI18n.localize( AbstractOperation.OPERATION_VALIDATION_ERROR,
                                                                                       NAME,
                                                                                       transformationId() ) );
                        problems().add( problem );
                    }
                }
            }

            { // divisor
                final List< Value< ? >> divisorValues = inputs( DIVISOR_DESCRIPTOR.name() );

                if ( divisorValues.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.localize( INVALID_DIVISOR_COUNT,
                                                                                   transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = divisorValues.get( 0 );
                    Object y;

                    try {
                        y = term.get();

                        if ( !( y instanceof Number ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.localize( INVALID_DIVISOR_TERM_TYPE,
                                                                                           transformationId() ) );
                            problems().add( problem );
                        }
                    } catch ( final ChrysalixException e ) {
                        final ValidationProblem problem =
                            TransformationFactory.createError( transformationId(), ChrysalixI18n.localize( AbstractOperation.OPERATION_VALIDATION_ERROR,
                                                                                                           NAME,
                                                                                                           transformationId() ) );
                        problems().add( problem );
                    }
                }
            }
        }

        return super.problems();
    }

}
