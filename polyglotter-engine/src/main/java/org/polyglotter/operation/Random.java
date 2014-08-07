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

import java.util.List;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.transformation.OperationCategory.BuiltInCategory;
import org.polyglotter.transformation.Transformation;
import org.polyglotter.transformation.TransformationFactory;
import org.polyglotter.transformation.ValidationProblem;
import org.polyglotter.transformation.ValueDescriptor;

/**
 * Provides a random number with a positive sign, greater than or equal to {@code 0.0} and less than {@code 1.0}. There is an
 * optional term for a seed value.
 */
public final class Random extends AbstractOperation< Double > {

    /**
     * The output descriptor.
     */
    public static final ValueDescriptor< Double > DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Random.class.getSimpleName() ),
                                                     PolyglotterI18n.randomOperationDescription.text(),
                                                     PolyglotterI18n.randomOperationName.text(),
                                                     Double.class,
                                                     false,
                                                     0,
                                                     false );

    /**
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Random( final Transformation transformation ) {
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

        if ( terms().isEmpty() ) {
            return Math.random();
        }

        final Number value = ( Number ) inputs().get( 0 ).get();
        return new java.util.Random( value.longValue() ).nextDouble();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#inputDescriptors()
     */
    @Override
    public List< ValueDescriptor< ? >> inputDescriptors() {
        return ValueDescriptor.NO_DESCRIPTORS;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.AbstractOperation#validate()
     */
    @Override
    protected void validate() {
        // make sure there is at most one term
        if ( !inputs().isEmpty() ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   PolyglotterI18n.randomOperationInvalidNumberOfTerms.text( transformationId() ) );
            problems().add( problem );
        }
    }

}
