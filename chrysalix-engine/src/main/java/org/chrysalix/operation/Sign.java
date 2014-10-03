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
 * Calculates the sign (0, 1, or -1) of a number.
 * 
 * @see Math#signum(double)
 * @see Math#signum(float)
 * @see BigInteger#signum()
 * @see BigDecimal#signum()
 */
public final class Sign extends AbstractOperation< Integer > {

    static final String DESCRIPTION = "Determines the sign (0, 1, -1) of a number";
    private static final String INPUT_DESCRIPTION = "The input term whose sign is being determined";
    private static final String INPUT_NAME = "Input";
    static final String NAME = "Sign";

    /**
     * The input term descriptor.
     */
    public static final ValueDescriptor< Number > TERM_DESCRIPTOR =
        TransformationFactory.createWritableBoundedOneValueDescriptor( TransformationFactory.createId( Sign.class, "input" ),
                                                                       ChrysalixI18n.localize( INPUT_DESCRIPTION ),
                                                                       ChrysalixI18n.localize( INPUT_NAME ),
                                                                       Number.class );

    /**
     * The input descriptors.
     */
    private static final ValueDescriptor< ? >[] INPUT_DESCRIPTORS = { TERM_DESCRIPTOR };

    /**
     * The output descriptor.
     */
    public static final OperationDescriptor< Integer > DESCRIPTOR =
        new AbstractOperationDescriptor< Integer >( TransformationFactory.createId( Sign.class ),
                                                    ChrysalixI18n.localize( DESCRIPTION ),
                                                    ChrysalixI18n.localize( NAME ),
                                                    Integer.class,
                                                    INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.modelspace.ModelObject,
             *      org.chrysalix.transformation.Transformation)
             */
            @Override
            public Operation< Integer > newInstance( final ModelObject operation,
                                                     final Transformation transformation ) throws ModelspaceException, ChrysalixException {
                return new Sign( operation, transformation );
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
    Sign( final ModelObject operation,
          final Transformation transformation ) throws ModelspaceException, ChrysalixException {
        super( operation, transformation );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#calculate()
     */
    @Override
    protected Integer calculate() throws ChrysalixException {
        assert !problems().isError();
        final Number value = ( Number ) inputs()[ 0 ].get();

        if ( value instanceof BigDecimal ) return ( ( BigDecimal ) value ).signum();
        if ( value instanceof BigInteger ) return ( ( BigInteger ) value ).signum();
        if ( value instanceof Float ) return Double.valueOf( Math.signum( ( Float ) value ) ).intValue();

        return Double.valueOf( Math.signum( value.doubleValue() ) ).intValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#problems()
     */
    @Override
    public ValidationProblems problems() throws ChrysalixException {
        this.problems.clear();

        // make sure there is one term
        if ( inputs().length != 1 ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   ChrysalixI18n.localize( AbstractOperation.MUST_HAVE_ONE_TERM,
                                                                           NAME,
                                                                           transformationId() ) );
            problems().add( problem );
        } else {
            // must be a number
            final Value< ? > term = inputs()[ 0 ];
            Object value;

            try {
                value = term.get();

                if ( !( value instanceof Number ) ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.localize( AbstractOperation.INVALID_TERM_TYPE,
                                                                                   NAME,
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

        return super.problems();
    }

}
