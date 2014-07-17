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

import javax.xml.namespace.QName;

import org.polyglotter.grammar.GrammarEvent.EventType;

/**
 * The base part of the Polyglotter transformation grammar.
 */
public interface GrammarPart {

    /**
     * The Polyglotter namespace prefix. Value is {@value} .
     */
    String NAMESPACE_PREFIX = "poly";

    /**
     * The Polyglotter namespace. Value is {@value} .
     */
    String NAMESPACE_URI = "www.redhat.com/polyglotter/1.0";

    /**
     * @return a localized part description (can be <code>null</code> or empty)
     */
    String description();

    /**
     * @return the unique part identifier (never <code>null</code>)
     */
    QName id();

    /**
     * @return a part name (never <code>null</code> or empty)
     */
    String name();

    /**
     * Keys used when constructing the data part of an event.
     */
    interface EventTag {

        String OLD_DESCRIPTION = "grammarPart.oldDescription";
        String NEW_DESCRIPTION = "grammarPart.newDescription";

        String OLD_NAME = "grammarPart.oldName";
        String NEW_NAME = "grammarPart.newName";

    }

    /**
     * The event types pertaining to grammar parts.
     */
    enum GrammarPartEventType implements EventType {

        /**
         * The term description has changed.
         */
        DESCRIPTION_CHANGED,

        /**
         * The term name has changed.
         */
        NAME_CHANGED;

    }

}
