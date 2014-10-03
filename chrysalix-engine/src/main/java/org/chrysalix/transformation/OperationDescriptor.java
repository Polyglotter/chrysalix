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
package org.chrysalix.transformation;

import org.chrysalix.ChrysalixException;
import org.modelspace.ModelObject;
import org.modelspace.ModelspaceException;

/**
 * An {@link Operation operation} descriptor.
 * 
 * @param <T>
 *        the type of the value
 */
public interface OperationDescriptor< T > extends ValueDescriptor< T > {

    /**
     * The name of the class field for the output descriptor. Value is {@value} .
     */
    String DESCRIPTOR_NAME = "DESCRIPTOR";

    /**
     * An empty array of operation descriptors.
     */
    OperationDescriptor< ? >[] NO_DESCRIPTORS = {};

    /**
     * @return a collection of descriptors for the inputs (never <code>null</code> but can be empty)
     */
    ValueDescriptor< ? >[] inputDescriptors();

    /**
     * Wraps an operation already contained in the specified transformation.
     * 
     * @param operation
     *        the operation model object whose domain object is being created (cannot be <code>null</code>)
     * @param transformation
     *        the owning {@link Transformation transformation} (never <code>null</code>)
     * @return the new operation instance (never <code>null</code>)
     * @throws ChrysalixException
     *         if a non-model object error occurs
     * @throws ModelspaceException
     *         if an error with the model object occurs
     */
    Operation< T > newInstance( final ModelObject operation,
                                final Transformation transformation ) throws ChrysalixException, ModelspaceException;

}
