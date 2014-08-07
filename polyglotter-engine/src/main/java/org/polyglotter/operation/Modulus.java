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

import java.util.Arrays;
import java.util.List;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.transformation.OperationCategory.BuiltInCategory;
import org.polyglotter.transformation.Transformation;
import org.polyglotter.transformation.TransformationFactory;
import org.polyglotter.transformation.ValidationProblem;
import org.polyglotter.transformation.Value;
import org.polyglotter.transformation.ValueDescriptor;

/**
 * Calculates the modulus (remainder) of the first term divided by the second term.
 */
public final class Modulus extends AbstractOperation< Double > {

    /**
     * The output descriptor.
     */
    public static final ValueDescriptor< Double > DESCRIPTOR =
        TransformationFactory.createReadOnlyBoundedOneValueDescriptor( TransformationFactory.createId( Modulus.class.getSimpleName() ),
                                                                       PolyglotterI18n.modulusOperationDescription.text(),
                                                                       PolyglotterI18n.modulusOperationName.text(),
                                                                       Double.class );

    /**
     * The dividend descriptor.
     */
    // TODO id, description, name
    public static final ValueDescriptor< Number > DIVIDEND_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Modulus.class.getSimpleName() ),
                                                     PolyglotterI18n.modulusOperationDescription.text(),
                                                     PolyglotterI18n.modulusOperationName.text(),
                                                     Number.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The dividend descriptor.
     */
    // TODO id, description, name
    public static final ValueDescriptor< Number > DIVISOR_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Modulus.class.getSimpleName() ),
                                                     PolyglotterI18n.modulusOperationDescription.text(),
                                                     PolyglotterI18n.modulusOperationName.text(),
                                                     Number.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The input descriptors.
     */
    private static final List< ValueDescriptor< ? >> INPUT_DESCRIPTORS =
        Arrays.asList( new ValueDescriptor< ? >[] { DIVIDEND_DESCRIPTOR, DIVISOR_DESCRIPTOR } );

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
        } catch ( final PolyglotterException e ) {
            this.logger.error( e, PolyglotterI18n.errorAddingBuiltInCategory, transformationId() );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.AbstractOperation#calculate()
     */
    @Override
    protected Double calculate() throws PolyglotterException {
        assert !problems().isError();

        final Number dividend = ( Number ) inputs( DIVIDEND_DESCRIPTOR.id() ).get( 0 ).get();
        final Number divisor = ( Number ) inputs( DIVISOR_DESCRIPTOR.id() ).get( 0 ).get();

        return ( dividend.doubleValue() % divisor.doubleValue() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#inputDescriptors()
     */
    @Override
    public List< ValueDescriptor< ? >> inputDescriptors() {
        return INPUT_DESCRIPTORS;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.AbstractOperation#validate()
     */
    @Override
    protected void validate() {
        // make sure there are terms
        if ( inputs().size() != 2 ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   PolyglotterI18n.modulusOperationInvalidTermCount.text( transformationId() ) );
            problems().add( problem );
        } else {
            { // dividend
                final List< Value< ? >> dividendValues = inputs( DIVIDEND_DESCRIPTOR.id() );

                if ( dividendValues.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           PolyglotterI18n.modulusOperationInvalidDividendCount.text( transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = dividendValues.get( 0 );
                    Object x;

                    try {
                        x = term.get();

                        if ( !( x instanceof Number ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   PolyglotterI18n.modulusOperationInvalidDividendTermType.text( transformationId() ) );
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

            { // divisor
                final List< Value< ? >> divisorValues = inputs( DIVISOR_DESCRIPTOR.id() );

                if ( divisorValues.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           PolyglotterI18n.modulusOperationInvalidDivisorCount.text( transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = divisorValues.get( 0 );
                    Object y;

                    try {
                        y = term.get();

                        if ( !( y instanceof Number ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   PolyglotterI18n.modulusOperationInvalidDivisorTermType.text( transformationId() ) );
                            problems().add( problem );
                        }
                    } catch ( final PolyglotterException e ) {
                        final ValidationProblem problem =
                            TransformationFactory.createError( transformationId(), PolyglotterI18n.operationValidationError.text( name(),
                                                                                                                                  transformationId() ) );
                        problems().add( problem );
                        this.logger.error( e, PolyglotterI18n.message, problem.message() );
                    }
                }
            }
        }
    }

}
