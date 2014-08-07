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

import java.util.Collections;
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
 * A string concatenation operation.
 */
public final class Concat extends AbstractOperation< String > {

    /**
     * The output descriptor.
     */
    public static final ValueDescriptor< String > DESCRIPTOR =
        TransformationFactory.createReadOnlyBoundedOneValueDescriptor( TransformationFactory.createId( Add.class.getSimpleName() ),
                                                                       PolyglotterI18n.concatOperationDescription.text(),
                                                                       PolyglotterI18n.concatOperationName.text(),
                                                                       String.class );
    /**
     * The input term descriptor.
     */
    public static final ValueDescriptor< String > TERM_DESCRIPTOR =
        // TODO id, description, name
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Add.class.getSimpleName() ),
                                                     PolyglotterI18n.concatOperationDescription.text(),
                                                     PolyglotterI18n.concatOperationName.text(),
                                                     String.class,
                                                     true,
                                                     2,
                                                     true );
    /**
     * The input descriptors.
     */
    private static final List< ValueDescriptor< String >> INPUT_DESCRIPTORS = Collections.singletonList( TERM_DESCRIPTOR );

    /**
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Concat( final Transformation transformation ) {
        super( DESCRIPTOR, transformation );

        try {
            addCategory( BuiltInCategory.STRING );
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
    protected String calculate() throws PolyglotterException {
        assert !problems().isError();

        final StringBuilder result = new StringBuilder();

        for ( final Value< ? > term : inputs() ) {
            final Object value = term.get();
            result.append( ( value == null ) ? "null" : value.toString() );
        }

        return result.toString();
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
        // validate number of terms
        if ( inputs().size() < TERM_DESCRIPTOR.requiredValueCount() ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   PolyglotterI18n.invalidTermCount.text( name(),
                                                                                          transformationId(),
                                                                                          inputs().size() ) );
            problems().add( problem );
        }
    }

}
