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
 * Calculates the value of the first term raised to the power of the second term.
 * 
 * @see Math#pow(double, double)
 */
public final class Power extends AbstractOperation< Number > {

    private static final String BASE_DESCRIPTION = "The input term whose value is being raised to a power";
    private static final String BASE_NAME = "Base";
    static final String DESCRIPTION = "Calculates the value of the first term raised to the power of the second term";
    private static final String EXPONENT_DESCRIPTION = "The input term whose value is the power a number is raised to";
    private static final String EXPONENT_NAME = "Exponent";
    private static final String INVALID_BASE_TERM_TYPE = "The base term of power operation in transformation '%s' must be a number";
    private static final String INVALID_EXPONENT_TERM_TYPE =
        "The exponent term of power operation in transformation '%s' must be a number";
    private static final String INVALID_BASE_COUNT = "Power operation in transformation '%s' must have exactly one base term";
    private static final String INVALID_EXPONENT_COUNT =
        "Power operation in transformation '%s' must have exactly one exponent term";
    static final String NAME = "Power";

    /**
     * The base descriptor.
     */
    public static final ValueDescriptor< Number > BASE_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Power.class, "base" ),
                                                     ChrysalixI18n.localize( BASE_DESCRIPTION ),
                                                     ChrysalixI18n.localize( BASE_NAME ),
                                                     Number.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The exponent descriptor.
     */
    public static final ValueDescriptor< Number > EXPONENT_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Power.class, "exponent" ),
                                                     ChrysalixI18n.localize( EXPONENT_DESCRIPTION ),
                                                     ChrysalixI18n.localize( EXPONENT_NAME ),
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
                                                   ChrysalixI18n.localize( DESCRIPTION ),
                                                   ChrysalixI18n.localize( NAME ),
                                                   Number.class,
                                                   INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.modelspace.ModelObject,
             *      org.chrysalix.transformation.Transformation)
             */
            @Override
            public Operation< Number > newInstance( final ModelObject operation,
                                                    final Transformation transformation ) throws ModelspaceException, ChrysalixException {
                return new Power( operation, transformation );
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
    Power( final ModelObject operation,
           final Transformation transformation ) throws ModelspaceException, ChrysalixException {
        super( operation, transformation );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#calculate()
     */
    @Override
    protected Number calculate() throws ChrysalixException {
        assert !problems().isError();
        final Number base = ( Number ) inputs( BASE_DESCRIPTOR.name() ).get( 0 ).get();
        final Number exponent = ( Number ) inputs( EXPONENT_DESCRIPTOR.name() ).get( 0 ).get();

        if ( base instanceof BigInteger ) return ( ( BigInteger ) base ).pow( exponent.intValue() );
        if ( base instanceof BigDecimal ) return ( ( BigDecimal ) base ).pow( exponent.intValue() );

        return Math.pow( base.doubleValue(), exponent.doubleValue() );
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
                TransformationFactory.createError( transformationId(), ChrysalixI18n.localize( AbstractOperation.INVALID_TERM_COUNT,
                                                                                               NAME,
                                                                                               transformationId(),
                                                                                               inputs().length ) );
            problems().add( problem );
        } else {
            { // base
                final List< Value< ? >> baseValues = inputs( BASE_DESCRIPTOR.name() );

                if ( baseValues.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.localize( INVALID_BASE_COUNT,
                                                                                   transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = baseValues.get( 0 );
                    Object x;

                    try {
                        x = term.get();

                        if ( !( x instanceof Number ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.localize( INVALID_BASE_TERM_TYPE,
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

            { // exponent
                final List< Value< ? >> exponentValues = inputs( EXPONENT_DESCRIPTOR.name() );

                if ( exponentValues.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.localize( INVALID_EXPONENT_COUNT,
                                                                                   transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = exponentValues.get( 0 );
                    Object y;

                    try {
                        y = term.get();

                        if ( !( y instanceof Number ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.localize( INVALID_EXPONENT_TERM_TYPE,
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
        }

        return super.problems();
    }

}
