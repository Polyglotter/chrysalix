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
import org.polyglotter.transformation.ValueDescriptor;

/**
 * Counts the number of terms.
 */
public final class Count extends AbstractOperation< Integer > {

    /**
     * The output descriptor.
     */
    public static final ValueDescriptor< Integer > DESCRIPTOR =
        TransformationFactory.createReadOnlyBoundedOneValueDescriptor( TransformationFactory.createId( Count.class.getSimpleName() ),
                                                                       PolyglotterI18n.countOperationDescription.text(),
                                                                       PolyglotterI18n.countOperationName.text(),
                                                                       Integer.class );

    /**
     * The input descriptors.
     */
    private static final List< ValueDescriptor< Object >> INPUT_DESCRIPTORS =
        // TODO id, description, name
        Collections.singletonList(
                   TransformationFactory.createValueDescriptor( TransformationFactory.createId( Count.class.getSimpleName() ),
                                                                PolyglotterI18n.countOperationDescription.text(),
                                                                PolyglotterI18n.countOperationName.text(),
                                                                Object.class,
                                                                true,
                                                                0,
                                                                true ) );

    /**
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Count( final Transformation transformation ) {
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
    protected Integer calculate() {
        assert !problems().isError();
        return inputs().size();
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
        // nothing to do
    }

}
