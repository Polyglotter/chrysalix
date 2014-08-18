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
package org.polyglotter.transformation;

import org.polyglotter.common.PolyglotterException;

/**
 * A class representing a transformation. A transformation uses an ordered set of {@link Operation operations} to calculate a result
 * consisting of one or more {@link Value values}.
 */
public interface Transformation extends Iterable< Operation< ? > > {

    /**
     * @param operations
     *        the operations being added (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the operation being added is <code>null</code>, empty, or any value in the array is <code>null</code>
     * @throws PolyglotterException
     *         if any of the operations have already been added or if an operation cannot be added
     */
    void add( final Operation< ? >... operations ) throws PolyglotterException;

    /**
     * @return a unique identifier (never <code>null</code> or empty)
     */
    String id();

    /**
     * @return a collection of the {@link Operation operations} performed by this transformation (never <code>null</code> but can be
     *         empty)
     * @see Operation#NO_OPERATIONS
     */
    Operation< ? >[] operations();

    /**
     * @param operations
     *        the operations being removed (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the operation being removed is <code>null</code>, empty, or any value in the array is <code>null</code>
     * @throws PolyglotterException
     *         if any of the operations cannot be found or cannot be removed
     */
    void remove( final Operation< ? >... operations ) throws PolyglotterException;

}
