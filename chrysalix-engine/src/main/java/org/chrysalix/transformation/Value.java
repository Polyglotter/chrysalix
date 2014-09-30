/*
 * Chrysalix
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * Chrysalix is free software. Unless otherwise indicated, all code in Chrysalix
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * Chrysalix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.chrysalix.transformation;

import java.util.Comparator;

import org.chrysalix.ChrysalixException;
import org.modelspace.ModelElement;
import org.modelspace.ModelObject;

/**
 * An input or output {@link Value value}.
 * 
 * @param <T>
 *        the type of value
 */
public interface Value< T > extends ModelElement {

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
            } catch ( final ChrysalixException e ) {
                return 0;
            }

            Number thatValue;

            try {
                thatValue = thatNumber.get();
            } catch ( final ChrysalixException e ) {
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
     * An empty array of values.
     */
    Value< ? >[] NO_VALUES = new Value< ? >[ 0 ];

    /**
     * @return the descriptor identifier (never <code>null</code>)
     * @throws ChrysalixException
     *         if an error occurs
     */
    String descriptorId() throws ChrysalixException;

    /**
     * @return the value of the model object property or the scalar value set (can be <code>null</code>)
     * @throws ChrysalixException
     *         if there is an error
     */
    T get() throws ChrysalixException;

    /**
     * @return the model object being wrapped by this domain object (never <code>null</code>)
     */
    ModelObject modelObect();

    /**
     * If the value had been the value of a model property it now becomes the value of this scalar value.
     * 
     * @param newValue
     *        the new value (can be <code>null</code>)
     * @throws ChrysalixException
     *         if an error occurs
     * @throws UnsupportedOperationException
     *         if the value is not modifiable
     * @see ValueDescriptor#modifiable()
     */
    void set( final Object newValue ) throws ChrysalixException;

}
