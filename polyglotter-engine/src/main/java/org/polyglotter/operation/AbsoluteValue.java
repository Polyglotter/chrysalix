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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.transformation.OperationCategory.BuiltInCategory;
import org.polyglotter.transformation.Transformation;
import org.polyglotter.transformation.TransformationFactory;
import org.polyglotter.transformation.ValidationProblem;
import org.polyglotter.transformation.Value;
import org.polyglotter.transformation.ValueDescriptor;

/**
 * Calculates the absolute value of a number.
 * 
 * @see Math#abs(double)
 * @see Math#abs(float)
 * @see Math#abs(int)
 * @see Math#abs(long)
 */
public final class AbsoluteValue extends AbstractOperation< Number > {

    /**
     * The output descriptor.
     */
    public static final ValueDescriptor< Number > DESCRIPTOR =
        TransformationFactory.createReadOnlyBoundedOneValueDescriptor( TransformationFactory.createId( AbsoluteValue.class.getSimpleName() ),
                                                                       PolyglotterI18n.absoluteValueOperationDescription.text(),
                                                                       PolyglotterI18n.absoluteValueOperationName.text(),
                                                                       Number.class );

    /**
     * The input descriptors.
     */
    private static final List< ValueDescriptor< Number >> INPUT_DESCRIPTORS =
        // TODO id, description, name
        Collections.singletonList(
                   TransformationFactory.createWritableBoundedOneValueDescriptor( TransformationFactory.createId( AbsoluteValue.class.getSimpleName() ),
                                                                                  PolyglotterI18n.absoluteValueOperationDescription.text(),
                                                                                  PolyglotterI18n.absoluteValueOperationName.text(),
                                                                                  Number.class ) );

    /**
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    AbsoluteValue( final Transformation transformation ) {
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
        final Number value = ( Number ) inputs().get( 0 ).get();

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
        // make sure there is one term
        if ( inputs().size() != 1 ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   PolyglotterI18n.absoluteValueOperationMustHaveOneTerm.text( transformationId() ) );
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
                                                           PolyglotterI18n.absoluteValueOperationInvalidTermType.text( transformationId() ) );
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
