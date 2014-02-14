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

import java.util.Map;

import javax.xml.namespace.QName;

/**
 * A class defining a {@link GrammarPart grammar part} event.
 */
public interface GrammarEvent {
    
    /**
     * @return a collection of data related to the event (can be <code>null</code> or empty)
     */
    Map< String, ? > data();
    
    /**
     * @return the grammar part identifier that sourced this event (never <code>null</code>)
     */
    QName sourceId();
    
    /**
     * @return the event type
     */
    EventType type();
    
    /**
     * The type of the event.
     */
    interface EventType {
        // nothing to do
    }
    
}
