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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
 * Calculates the absolute value of a number.
 * 
 * @see Math#abs(double)
 * @see Math#abs(float)
 * @see Math#abs(int)
 * @see Math#abs(long)
 */
public final class AbsoluteValue extends AbstractOperation< Number > {

    static final String DESCRIPTION = "Finds the absolute value of a number";
    private static final String INPUT_DESCRIPTION = "The number whose absolute value is being calculated";
    private static final String INPUT_NAME = "Input";
    private static final String MUST_HAVE_ONE_TERM =
        "Absolute value operation in transformation '%s' requires one and only one term";
    static final String NAME = "Absolute Value";

    /**
     * The input term descriptor.
     */
    public static final ValueDescriptor< Number > TERM_DESCRIPTOR =
        TransformationFactory.createWritableBoundedOneValueDescriptor( TransformationFactory.createId( AbsoluteValue.class, "input" ),
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
    public static final OperationDescriptor< Number > DESCRIPTOR =
        new AbstractOperationDescriptor< Number >( TransformationFactory.createId( AbsoluteValue.class ),
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
                return new AbsoluteValue( operation, transformation );
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
    AbsoluteValue( final ModelObject operation,
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
        final Number value = ( Number ) inputs()[ 0 ].get();

        if ( value instanceof Double ) return Math.abs( ( Double ) value );
        if ( value instanceof Float ) return Math.abs( ( Float ) value );
        if ( value instanceof Integer ) return Math.abs( ( Integer ) value );
        if ( value instanceof Long ) return Math.abs( ( Long ) value );
        if ( value instanceof Short ) return Math.abs( ( Short ) value );
        if ( value instanceof BigDecimal ) return ( ( BigDecimal ) value ).abs();
        if ( value instanceof BigInteger ) return ( ( BigInteger ) value ).abs();
        if ( value instanceof AtomicInteger ) return ( new AtomicInteger( Math.abs( ( ( AtomicInteger ) value ).get() ) ) );
        if ( value instanceof AtomicLong ) return ( new AtomicLong( Math.abs( ( ( AtomicLong ) value ).get() ) ) );

        return Math.abs( value.doubleValue() );
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
                                                   ChrysalixI18n.localize( MUST_HAVE_ONE_TERM, transformationId() ) );
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
