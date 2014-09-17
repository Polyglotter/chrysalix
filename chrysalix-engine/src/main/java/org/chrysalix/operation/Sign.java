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
 * Calculates the sign (0, 1, or -1) of a number.
 * 
 * @see Math#signum(double)
 * @see Math#signum(float)
 * @see BigInteger#signum()
 * @see BigDecimal#signum()
 */
public final class Sign extends AbstractOperation< Integer > {

    /**
     * The input term descriptor.
     */
    public static final ValueDescriptor< Number > TERM_DESCRIPTOR =
        TransformationFactory.createWritableBoundedOneValueDescriptor( TransformationFactory.createId( Sign.class, "input" ),
                                                                       ChrysalixI18n.signOperationInputDescription.text(),
                                                                       ChrysalixI18n.signOperationInputName.text(),
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
                                                    ChrysalixI18n.signOperationDescription.text(),
                                                    ChrysalixI18n.signOperationName.text(),
                                                    Integer.class,
                                                    INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.chrysalix.transformation.Transformation)
             */
            @Override
            public Operation< Integer > newInstance( final Transformation transformation ) {
                return new Sign( transformation );
            }

        };

    /**
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Sign( final Transformation transformation ) {
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
    protected Integer calculate() throws ChrysalixException {
        assert !problems().isError();
        final Number value = ( Number ) inputs().get( 0 ).get();

        if ( value instanceof BigDecimal ) return ( ( BigDecimal ) value ).signum();
        if ( value instanceof BigInteger ) return ( ( BigInteger ) value ).signum();
        if ( value instanceof Float ) return Double.valueOf( Math.signum( ( Float ) value ) ).intValue();

        return Double.valueOf( Math.signum( value.doubleValue() ) ).intValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#validate()
     */
    @Override
    protected void validate() {
        // make sure there is one term
        if ( inputs().size() != 1 ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   ChrysalixI18n.signOperationMustHaveOneTerm.text( transformationId() ) );
            problems().add( problem );
        } else {
            // must be a number
            final Value< ? > term = inputs().get( 0 );
            Object value;

            try {
                value = term.get();

                if ( !( value instanceof Number ) ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.signOperationInvalidTermType.text( transformationId() ) );
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
