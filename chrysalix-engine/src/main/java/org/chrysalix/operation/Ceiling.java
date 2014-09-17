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
 * Calculates the ceiling of a number.
 * 
 * @see Math#ceil(double)
 */
public final class Ceiling extends AbstractOperation< Integer > {

    /**
     * The input term descriptor.
     */
    public static final ValueDescriptor< Number > TERM_DESCRIPTOR =
        TransformationFactory.createWritableBoundedOneValueDescriptor( TransformationFactory.createId( Ceiling.class, "input" ),
                                                                       ChrysalixI18n.ceilingOperationInputDescription.text(),
                                                                       ChrysalixI18n.ceilingOperationInputName.text(),
                                                                       Number.class );

    /**
     * The input descriptors.
     */
    private static final ValueDescriptor< ? >[] INPUT_DESCRIPTORS = { TERM_DESCRIPTOR };

    /**
     * The output descriptor.
     */
    public static final OperationDescriptor< Integer > DESCRIPTOR =
        new AbstractOperationDescriptor< Integer >( TransformationFactory.createId( Ceiling.class ),
                                                    ChrysalixI18n.ceilingOperationDescription.text(),
                                                    ChrysalixI18n.ceilingOperationName.text(),
                                                    Integer.class,
                                                    INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.chrysalix.transformation.Transformation)
             */
            @Override
            public Operation< Integer > newInstance( final Transformation transformation ) {
                return new Ceiling( transformation );
            }

        };

    /**
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Ceiling( final Transformation transformation ) {
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

        if ( value instanceof Integer ) return ( Integer ) value;
        return ( int ) Math.ceil( value.doubleValue() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#validate()
     */
    @Override
    protected void validate() {
        // make sure there are terms
        if ( inputs().size() != 1 ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   ChrysalixI18n.ceilingOperationMustHaveOneTerm.text( transformationId() ) );
            problems().add( problem );
        } else {
            // make sure term is a number
            final Value< ? > term = inputs().get( 0 );
            Object value;

            try {
                value = term.get();

                if ( !( value instanceof Number ) ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.ceilingOperationInvalidTermType.text( transformationId() ) );
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
