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

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.GrammarEvent.EventType;

/**
 * A class that performs an action that produces a result value using zero or more input {@link Term terms}.
 * 
 * @param <T>
 *        the operation result term type
 */
public interface Operation< T > extends GrammarPart, GrammarEventSource, Iterable< Term< ? > > {
    
    /**
     * An empty list of operations.
     */
    List< Operation< ? > > NO_OPERATIONS = Collections.emptyList();
    
    /**
     * @param terms
     *        the terms being added (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the term array is <code>null</code>, empty, or any value in the array is <code>null</code>
     * @throws PolyglotterException
     *         if any of the terms have already been added
     */
    void add( Term< ? >... terms ) throws PolyglotterException;
    
    /**
     * @param termId
     *        the identifier of the term being requested (cannot be <code>null</code>)
     * @return the requested term
     * @throws IllegalArgumentException
     *         if the term identifier is <code>null</code>
     * @throws PolyglotterException
     *         if the term cannot be found
     */
    Term< ? > get( final QName termId ) throws PolyglotterException;
    
    /**
     * @return the validation problems (never <code>null</code> but can be empty)
     */
    ValidationProblems problems();
    
    /**
     * @param termIds
     *        the identifiers of the terms being removed (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the term identifiers array is <code>null</code>, empty, or any value in the array is <code>null</code>
     * @throws PolyglotterException
     *         if any of the terms cannot be found or cannot be removed
     */
    void remove( QName... termIds ) throws PolyglotterException;
    
    /**
     * @return the result term (never <code>null</code>)
     * @throws PolyglotterException
     *         if there are validation errors or problems obtaining the result
     * @see #problems()
     */
    Term< T > result() throws PolyglotterException;
    
    /**
     * @return an unmodifiable ordered collection of the input terms (never <code>null</code> but can be empty)
     * @see Term#NO_TERMS
     */
    List< Term< ? >> terms();
    
    /**
     * @return the identifier of the owning {@link Transform transform} (never <code>null</code>)
     */
    QName transformId();
    
    /**
     * The event types pertaining to operations.
     */
    enum OperationEventType implements EventType {
        
        /**
         * The result value has changed.
         */
        RESULT_CHANGED,
        
        /**
         * A term has been added.
         */
        TERM_ADDED,
        
        /**
         * A term has been removed.
         */
        TERM_REMOVED;
        
    }
    
}
