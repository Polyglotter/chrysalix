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
package org.polyglotter.grammar;

import java.util.Collection;

import org.polyglotter.common.PolyglotterException;

/**
 * A class that contains {@link Operation operations} used in transformations.
 */
public interface Transform extends GrammarPart, Iterable< Operation< ? > > {
    
    /**
     * @param operations
     *        the operations being added (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the operation array is <code>null</code> or any value in the array is <code>null</code>
     * @throws PolyglotterException
     *         if any of the operations have already been added
     */
    void add( Operation< ? >... operations ) throws PolyglotterException;
    
    /**
     * @return a collection of all the {@link Operation operations} performed by this transform (never <code>null</code>)
     */
    Collection< Operation< ? > > operations();
    
    /**
     * @param type
     *        the result term type of the operations being requested (cannot be <code>null</code>)
     * @return a collection of operations that match (never <code>null</code> but can be empty)
     * @throws PolyglotterException
     *         if the input term type is <code>null</code>
     * @see Operation#NO_OPERATIONS
     */
    < T > Collection< Operation< T >> operationsFor( T type ) throws PolyglotterException;
    
    /**
     * @param operationIds
     *        the identifiers of the operations being removed (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the operation identifiers array is <code>null</code> or any value in the array is <code>null</code>
     * @throws PolyglotterException
     *         if any of the operations cannot be found or cannot be removed
     */
    void remove( Operation< ? >... operationIds ) throws PolyglotterException;
    
}
