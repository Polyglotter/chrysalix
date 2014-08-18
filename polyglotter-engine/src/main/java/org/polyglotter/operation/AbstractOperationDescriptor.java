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

import org.polyglotter.transformation.OperationDescriptor;
import org.polyglotter.transformation.ValueDescriptor;

/**
 * A base class implementation for an {@link OperationDescriptor operation descriptor}.
 * 
 * @param <T>
 *        the operation's result type
 */
abstract class AbstractOperationDescriptor< T > extends ValueDescriptorImpl< T > implements OperationDescriptor< T > {

    private final ValueDescriptor< ? >[] inputDescriptors;

    /**
     * @param outputDescriptorId
     *        the output descriptor identifier (cannot be <code>null</code> or empty)
     * @param operationDescription
     *        the operation description (cannot be <code>null</code> or empty)
     * @param operationName
     *        the operation name (cannot be <code>null</code> or empty)
     * @param outputValueType
     *        the output value type (cannot be <code>null</code>)
     * @param operationInputDescriptors
     *        the input descriptors (can be <code>null</code> or empty)
     */
    protected AbstractOperationDescriptor( final String outputDescriptorId,
                                           final String operationDescription,
                                           final String operationName,
                                           final Class< T > outputValueType,
                                           final ValueDescriptor< ? >[] operationInputDescriptors ) {
        super( outputDescriptorId, operationDescription, operationName, outputValueType, false, 1, false );
        this.inputDescriptors = ( operationInputDescriptors == null ) ? OperationDescriptor.NO_DESCRIPTORS
                                                                     : operationInputDescriptors;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.OperationDescriptor#inputDescriptors()
     */
    @Override
    public ValueDescriptor< ? >[] inputDescriptors() {
        return this.inputDescriptors;
    }

}
