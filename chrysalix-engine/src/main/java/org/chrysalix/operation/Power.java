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

import java.math.BigDecimal;
import java.math.BigInteger;
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
                                                     ChrysalixI18n.powerOperationBaseDescription.text(),
                                                     ChrysalixI18n.powerOperationBaseName.text(),
                                                     Number.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The exponent descriptor.
     */
    public static final ValueDescriptor< Number > EXPONENT_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Power.class, "exponent" ),
                                                     ChrysalixI18n.powerOperationExponentDescription.text(),
                                                     ChrysalixI18n.powerOperationExponentName.text(),
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
                                                   ChrysalixI18n.powerOperationDescription.text(),
                                                   ChrysalixI18n.powerOperationName.text(),
                                                   Number.class,
                                                   INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.chrysalix.transformation.Transformation)
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
    protected Number calculate() throws ChrysalixException {
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
     * @see org.chrysalix.operation.AbstractOperation#validate()
     */
    @Override
    protected void validate() {
        // make sure there are terms
        if ( inputs().size() != 2 ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(), ChrysalixI18n.powerOperationInvalidTermCount.text( transformationId() ) );
            problems().add( problem );
        } else {
            { // base
                final List< Value< ? >> baseValues = inputs( BASE_DESCRIPTOR.id() );

                if ( baseValues.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.powerOperationInvalidBaseCount.text( transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = baseValues.get( 0 );
                    Object x;

                    try {
                        x = term.get();

                        if ( !( x instanceof Number ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.powerOperationInvalidBaseTermType.text( transformationId() ) );
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

            { // exponent
                final List< Value< ? >> exponentValues = inputs( EXPONENT_DESCRIPTOR.id() );

                if ( exponentValues.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.powerOperationInvalidExponentCount.text( transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = exponentValues.get( 0 );
                    Object y;

                    try {
                        y = term.get();

                        if ( !( y instanceof Number ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.powerOperationInvalidExponentTermType.text( transformationId() ) );
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

}
