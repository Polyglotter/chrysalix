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

import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.GrammarEvent.EventType;

/**
 * A value used as an input to, or output of, an {@link Operation operation}.
 * 
 * @param <T>
 *        the type of term
 */
public interface Term< T > extends GrammarPart, GrammarEventSource {

    /**
     * An empty list of terms.
     */
    List< Term< ? > > NO_TERMS = Collections.emptyList();

    /**
     * @return <code>true</code> if the term's value is modifiable
     */
    boolean modifiable();

    /**
     * Modifies the term value with the specified new value.
     * 
     * @param newValue
     *        the new term value (can be <code>null</code>)
     * @throws PolyglotterException
     *         if there is a problem setting the new value
     * @throws UnsupportedOperationException
     *         if called when the term is not modifiable
     * @see #modifiable()
     */
    void setValue( final T newValue ) throws PolyglotterException, UnsupportedOperationException;

    /**
     * Obtains the current term value.
     * 
     * @return the current value (can be <code>null</code>)
     * @throws PolyglotterException
     *         if there is a problem obtaining the value
     */
    T value() throws PolyglotterException;

    /**
     * Keys used when constructing the data part of an event.
     */
    interface EventTag {

        String OLD_VALUE = "term.oldValue";
        String NEW_VALUE = "term.newValue";

    }

    /**
     * The event types pertaining to terms.
     */
    enum TermEventType implements EventType {

        /**
         * The term value has changed.
         */
        VALUE_CHANGED;

    }

}
