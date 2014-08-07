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
package org.polyglotter.internal;

import java.util.Comparator;

import org.polyglotter.common.PolyglotterException;
import org.polyglotter.operation.ValueImpl;
import org.polyglotter.transformation.Value;
import org.polyglotter.transformation.ValueDescriptor;

/**
 * A number value.
 */
public class NumberValue extends ValueImpl< Number > {

    /**
     * A {@link Number number} term sorter that sorts the term values in ascending order. If there is an error accessing either
     * term's value a zero is returned.
     */
    public static final Comparator< Value< Number > > ASCENDING_SORTER = new Comparator< Value< Number > >() {

        @Override
        public int compare( final Value< Number > thisNumber,
                            final Value< Number > thatNumber ) {
            final Number thisValue = thisNumber.get();
            final Number thatValue = thatNumber.get();

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
    public static final Comparator< Value< Number > > DESCENDING_SORTER = new Comparator< Value< Number > >() {

        @Override
        public int compare( final Value< Number > thisNumber,
                            final Value< Number > thatNumber ) {
            return ASCENDING_SORTER.compare( thatNumber, thisNumber );
        }

    };

    /**
     * @param descriptor
     *        the value descriptor (cannot be <code>null</code>)
     */
    public NumberValue( final ValueDescriptor< Number > descriptor ) {
        super( descriptor );
    }

    /**
     * @param descriptor
     *        the value descriptor (cannot be <code>null</code>)
     * @param initialValue
     *        the initial value (can be <code>null</code>)
     * @throws PolyglotterException
     *         if there is a problem setting the initial value
     */
    public NumberValue( final ValueDescriptor< Number > descriptor,
                       final Number initialValue ) throws PolyglotterException {
        this( descriptor );
        set( initialValue );
    }

}
