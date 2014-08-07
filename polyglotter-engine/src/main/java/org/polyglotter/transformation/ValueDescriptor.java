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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * An input or output {@link Value value} descriptor.
 * 
 * @param <T>
 *        the type of the value
 */
public interface ValueDescriptor< T > {

    /**
     * Sorts {@link ValueDescriptor descriptors} by their name.
     */
    Comparator< ValueDescriptor< ? > > NAME_SORTER = new Comparator< ValueDescriptor< ? >>() {

        /**
         * {@inheritDoc}
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare( final ValueDescriptor< ? > thisDescriptor,
                            final ValueDescriptor< ? > thatDescriptor ) {
            return thisDescriptor.name().compareTo( thatDescriptor.name() );
        }

    };

    /**
     * An empty collection of descriptors.
     */
    List< ValueDescriptor< ? > > NO_DESCRIPTORS = Collections.emptyList();

    /**
     * @return the localized description of the input or output (never <code>null</code> or empty)
     */
    String description();

    /**
     * @return a unique identifier (never <code>null</code>)
     */
    QName id();

    /**
     * @return <code>true</code> if the value is modifiable
     */
    boolean modifiable();

    /**
     * @return the localized name of the input or output (never <code>null</code> or empty)
     */
    String name();

    /**
     * @return the namespace URI (can be <code>null</code>)
     */
    String namespace();

    /**
     * @return the number of required values (never smaller than zero)
     */
    int requiredValueCount();

    /**
     * @return the type of the input or output (never <code>null</code>)
     */
    Class< T > type();

    /**
     * @return <code>true</code> if an unlimited number of values can be created
     */
    boolean unbounded();

}
