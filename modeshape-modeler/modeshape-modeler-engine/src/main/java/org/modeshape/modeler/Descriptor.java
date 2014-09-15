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
package org.modeshape.modeler;

/**
 * Represents a {@link ModelObject model object} type definition.
 */
public interface Descriptor {

    /**
     * @return the child {@link ModelObject model object} {@link Descriptor descriptors} (never <code>null</code> but can be empty)
     * @throws ModelerException
     *         if an error occurs
     */
    Descriptor[] childDescriptors() throws ModelerException;

    /**
     * @return the type name (never <code>null</code> or empty)
     * @throws ModelerException
     *         if an error occurs
     */
    String name() throws ModelerException;

    /**
     * @return the {@link ModelObject model object} {@link ModelProperty property} {@link PropertyDescriptor descriptors} (never
     *         <code>null</code> but can be empty)
     * @throws ModelerException
     *         if an error occurs
     */
    PropertyDescriptor[] propertyDescriptors() throws ModelerException;

}
