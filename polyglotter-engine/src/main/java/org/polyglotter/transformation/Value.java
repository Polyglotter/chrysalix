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

import org.polyglotter.PolyglotterException;

/**
 * An input or output {@link Value value}.
 * 
 * @param <T>
 *        the type of value
 */
public interface Value< T > {

    /**
     * A {@link Number number} term sorter that sorts the term values in ascending order. If there is an error accessing either
     * term's value a zero is returned.
     */
    public static final Comparator< Value< Number > > ASCENDING_NUMBER_SORTER = new Comparator< Value< Number > >() {

        @Override
        public int compare( final Value< Number > thisNumber,
                            final Value< Number > thatNumber ) {
            Number thisValue;

            try {
                thisValue = thisNumber.get();
            } catch ( final PolyglotterException e ) {
                return 0;
            }

            Number thatValue;

            try {
                thatValue = thatNumber.get();
            } catch ( final PolyglotterException e ) {
                return 0;
            }

            if ( thisValue == null ) {
                return ( ( thatValue == null ) ? 0 : -1 );
            }

            if ( thatValue == null ) {
                return 1;
            }

            return Double.compare( thisValue.doubleValue(), thatValue.doubleValue() );
        }

    };

    /**
     * A {@link Number number} value sorter that sorts in descending order. If there is an error accessing either value a zero is
     * returned.
     */
    public static final Comparator< Value< Number > > DESCENDING_NUMBER_SORTER = new Comparator< Value< Number > >() {

        @Override
        public int compare( final Value< Number > thisNumber,
                            final Value< Number > thatNumber ) {
            return ASCENDING_NUMBER_SORTER.compare( thatNumber, thisNumber );
        }

    };

    /**
     * An empty list of values.
     */
    List< Value< ? > > NO_VALUES = Collections.emptyList();

    /**
     * @return the descriptor (never <code>null</code>)
     */
    ValueDescriptor< T > descriptor();

    /**
     * @return the value (can be <code>null</code>)
     * @throws PolyglotterException
     *         if there is an error
     */
    T get() throws PolyglotterException;

    /**
     * @param newValue
     *        the new value (can be <code>null</code>)
     * @throws PolyglotterException
     *         if an error occurs
     * @throws UnsupportedOperationException
     *         if the value is not modifiable
     * @see ValueDescriptor#modifiable()
     */
    void set( final T newValue ) throws PolyglotterException;

}
