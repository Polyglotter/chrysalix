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
import java.util.Set;

import javax.xml.namespace.QName;

import org.polyglotter.common.PolyglotterException;
import org.polyglotter.transformation.TransformationEvent.EventType;

/**
 * A class that produces a result by using zero or more {@link Value values}.
 * 
 * @param <T>
 *        the operation result type
 */
public interface Operation< T > extends Value< T >, Iterable< Value< ? > > {

    /**
     * Sorts operations by their name.
     */
    Comparator< Operation< ? > > NAME_SORTER = new Comparator< Operation< ? >>() {

        /**
         * {@inheritDoc}
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare( final Operation< ? > thisOp,
                            final Operation< ? > thatOp ) {
            return ValueDescriptor.NAME_SORTER.compare( thisOp.descriptor(), thatOp.descriptor() );
        }

    };

    /**
     * An empty list of operations.
     */
    final List< Operation< ? > > NO_OPERATIONS = Collections.emptyList();

    /**
     * @param categoriesBeingAdded
     *        the categories being added (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the categories array is <code>null</code>, empty, or any value in the array is <code>null</code>
     * @throws PolyglotterException
     *         if any of the categories cannot be added
     */
    void addCategory( final OperationCategory... categoriesBeingAdded ) throws PolyglotterException;

    /**
     * @param descriptorId
     *        the identifier of the {@link ValueDescriptor descriptor} to use when adding the values (cannot be <code>null</code>)
     * @param valuesBeingAdded
     *        the values being added (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the values array is <code>null</code>, empty, or any value in the array is <code>null</code>
     * @throws PolyglotterException
     *         if there is a problem adding any of the values
     */
    void addInput( final QName descriptorId,
                   final Object... valuesBeingAdded ) throws PolyglotterException;

    /**
     * @return a collection of {@link OperationCategory categories} this operation belongs to (never <code>null</code> but can be
     *         empty)
     */
    Set< OperationCategory > categories();

    /**
     * @return a collection of descriptors for the inputs (never <code>null</code> but can be empty)
     */
    List< ValueDescriptor< ? >> inputDescriptors();

    /**
     * @return all inputs whose value is non-<code>null</code> (never <code>null</code> but can be empty)
     */
    List< Value< ? >> inputs();

    /**
     * @return the validation problems (never <code>null</code> but can be empty)
     */
    ValidationProblems problems();

    /**
     * @param categoriesBeingRemoved
     *        the {@link OperationCategory categories} being removed (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the categories array is <code>null</code>, empty, or any value in the array is <code>null</code>
     * @throws PolyglotterException
     *         if any of the categories cannot be found or cannot be removed
     */
    void removeCategory( final OperationCategory... categoriesBeingRemoved ) throws PolyglotterException;

    /**
     * @param inputsBeingRemoved
     *        the input {@link Value values} being removed (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the values array is <code>null</code>, empty, or any value in the array is <code>null</code>
     * @throws PolyglotterException
     *         if any of the values cannot be found or cannot be removed
     */
    void removeInput( final Value< ? >... inputsBeingRemoved ) throws PolyglotterException;

    /**
     * @param descriptorId
     *        the identifier of the {@link ValueDescriptor descriptor} used when setting input values (cannot be <code>null</code>)
     * @param valuesBeingSet
     *        the new input values (can be <code>null</code> or empty)
     * @throws PolyglotterException
     *         if there is a problem setting the input values
     */
    void setInput( final QName descriptorId,
                   final Object... valuesBeingSet ) throws PolyglotterException;

    /**
     * @return the owning {@link Transformation transformation} (never <code>null</code>)
     */
    Transformation transformation();

    /**
     * The event types pertaining to operations.
     */
    enum OperationEventType implements EventType {

        /**
         * Multiple categories have been added.
         */
        CATEGORIES_ADDED,

        /**
         * A category has been added.
         */
        CATEGORY_ADDED,

        /**
         * Multiple categories have been removed.
         */
        CATEGORIES_REMOVED,

        /**
         * A category has been removed.
         */
        CATEGORY_REMOVED,

        /**
         * The key for when the result has changed.
         */
        RESULT_CHANGED,

        /**
         * An input value has been added.
         */
        VALUE_ADDED,

        /**
         * Multiple input values have been added.
         */
        VALUES_ADDED,

        /**
         * An input value has been removed.
         */
        VALUE_REMOVED,

        /**
         * Multiple input values have been removed.
         */
        VALUES_REMOVED;

    }

}
