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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.polyglotter.PolyglotterException;
import org.polyglotter.PolyglotterI18n;
import org.polyglotter.transformation.Operation;
import org.polyglotter.transformation.OperationCategory.BuiltInCategory;
import org.polyglotter.transformation.OperationDescriptor;
import org.polyglotter.transformation.Transformation;
import org.polyglotter.transformation.TransformationFactory;
import org.polyglotter.transformation.ValidationProblem;
import org.polyglotter.transformation.Value;
import org.polyglotter.transformation.ValueDescriptor;

/**
 * Calculates the value of the first term raised to the power of the second term.
 * 
 * @see Math#pow(double, double)
 */
public final class Power extends AbstractOperation< Number > {

    /**
     * The base descriptor.
     */
    public static final ValueDescriptor< Number > BASE_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Power.class, "base" ),
                                                     PolyglotterI18n.powerOperationBaseDescription.text(),
                                                     PolyglotterI18n.powerOperationBaseName.text(),
                                                     Number.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The exponent descriptor.
     */
    public static final ValueDescriptor< Number > EXPONENT_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Power.class, "exponent" ),
                                                     PolyglotterI18n.powerOperationExponentDescription.text(),
                                                     PolyglotterI18n.powerOperationExponentName.text(),
                                                     Number.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The input descriptors.
     */
    private static final ValueDescriptor< ? >[] INPUT_DESCRIPTORS = { BASE_DESCRIPTOR, EXPONENT_DESCRIPTOR };

    /**
     * The output descriptor.
     */
    public static final OperationDescriptor< Number > DESCRIPTOR =
        new AbstractOperationDescriptor< Number >( TransformationFactory.createId( Power.class ),
                                                   PolyglotterI18n.powerOperationDescription.text(),
                                                   PolyglotterI18n.powerOperationName.text(),
                                                   Number.class,
                                                   INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.polyglotter.transformation.OperationDescriptor#newInstance(org.polyglotter.transformation.Transformation)
             */
            @Override
            public Operation< Number > newInstance( final Transformation transformation ) {
                return new Power( transformation );
            }

        };

    /**
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Power( final Transformation transformation ) {
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
    protected Number calculate() throws PolyglotterException {
        assert !problems().isError();

        final Number base = ( Number ) inputs( BASE_DESCRIPTOR.id() ).get( 0 ).get();
        final Number exponent = ( Number ) inputs( EXPONENT_DESCRIPTOR.id() ).get( 0 ).get();

        if ( base instanceof BigInteger ) return ( ( BigInteger ) base ).pow( exponent.intValue() );
        if ( base instanceof BigDecimal ) return ( ( BigDecimal ) base ).pow( exponent.intValue() );

        return Math.pow( base.doubleValue(), exponent.doubleValue() );
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
                TransformationFactory.createError( transformationId(), PolyglotterI18n.powerOperationInvalidTermCount.text( transformationId() ) );
            problems().add( problem );
        } else {
            { // base
                final List< Value< ? >> baseValues = inputs( BASE_DESCRIPTOR.id() );

                if ( baseValues.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           PolyglotterI18n.powerOperationInvalidBaseCount.text( transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = baseValues.get( 0 );
                    Object x;

                    try {
                        x = term.get();

                        if ( !( x instanceof Number ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   PolyglotterI18n.powerOperationInvalidBaseTermType.text( transformationId() ) );
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

            { // exponent
                final List< Value< ? >> exponentValues = inputs( EXPONENT_DESCRIPTOR.id() );

                if ( exponentValues.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           PolyglotterI18n.powerOperationInvalidExponentCount.text( transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = exponentValues.get( 0 );
                    Object y;

                    try {
                        y = term.get();

                        if ( !( y instanceof Number ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   PolyglotterI18n.powerOperationInvalidExponentTermType.text( transformationId() ) );
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

}
