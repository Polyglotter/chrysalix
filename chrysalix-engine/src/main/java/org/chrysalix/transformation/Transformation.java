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

import org.chrysalix.ChrysalixException;
import org.modelspace.Model;
import org.modelspace.ModelElement;

/**
 * A class representing a transformation. A transformation uses an ordered set of {@link Operation operations} to calculate a result
 * consisting of one or more {@link Value values}.
 */
public interface Transformation extends Iterable< Operation< ? > >, ModelElement {

    /**
     * The unique identifier for the transformation model type. Value is {@value} .
     */
    String METAMODEL_ID = "org.polyglotter.transformation.Transformation";

    /**
     * An empty array of transformations
     */
    Transformation[] NO_TRANSFORMATIONS = new Transformation[ 0 ];

    /**
     * @param operations
     *        the operations being added (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the operations collection being added is <code>null</code>, empty, or contains a <code>null</code> item
     * @throws ChrysalixException
     *         if any of the operations have already been added or if an error occurs
     */
    void add( final Operation< ? >... operations ) throws ChrysalixException;

    /**
     * Adds source models to the transformation.
     * 
     * @param models
     *        the models being added as sources (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the models collection being added is <code>null</code>, empty, or contains a <code>null</code> item
     * @throws ChrysalixException
     *         if a model being added has already been added as a source or if an error occurs
     */
    void addSource( final Model... models ) throws ChrysalixException;

    /**
     * Adds target models to the transformation.
     * 
     * @param models
     *        the models being added as targets (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the models colleciton being added is <code>null</code>, empty, or contains a <code>null</code> item
     * @throws ChrysalixException
     *         if a model being added has already been added as a target or if an error occurs
     */
    void addTarget( final Model... models ) throws ChrysalixException;

    /**
     * @return the transformation identifier (workspace path)
     * @throws ChrysalixException
     *         if an error occurs
     */
    String id() throws ChrysalixException;

    /**
     * @return a collection of the {@link Operation operations} performed by this transformation (never <code>null</code> but can be
     *         empty)
     * @throws ChrysalixException
     *         if there is a problem obtaining the transformation's operations
     * @see Operation#NO_OPERATIONS
     */
    Operation< ? >[] operations() throws ChrysalixException;

    /**
     * Operations are added to transformations using the operation's descriptor ID. When using this method, the first occurrence of
     * an operation named with that descriptor ID is removed.
     * 
     * @param operations
     *        the operations being removed (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the operations collection being removed is <code>null</code>, empty, or contains a <code>null</code> item
     * @throws ChrysalixException
     *         if any of the operations cannot be found or cannot be removed
     */
    void remove( final Operation< ? >... operations ) throws ChrysalixException;

    /**
     * Removes source models from the transformation.
     * 
     * @param models
     *        the source models being removed (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the models collection being removed is <code>null</code>, empty, or contains a <code>null</code> item
     * @throws ChrysalixException
     *         if a model was not previously added as a source or if an error occurs
     */
    void removeSource( final Model... models ) throws ChrysalixException;

    /**
     * Removes a target model from the transformation.
     * 
     * @param models
     *        the target models being removed (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the models collection being removed is <code>null</code>, empty, or contains a <code>null</code> item
     * @throws ChrysalixException
     *         if a model was not previously added as a target or if an error occurs
     */
    void removeTarget( final Model... models ) throws ChrysalixException;

    /**
     * @return all source models (never <code>null</code> but can be empty)
     * @throws ChrysalixException
     *         if there is a problem obtaining the transformation's source models
     */
    Model[] sources() throws ChrysalixException;

    /**
     * @return all target models (never <code>null</code> but can be empty)
     * @throws ChrysalixException
     *         if there is a problem obtaining the transformation's target models
     */
    Model[] targets() throws ChrysalixException;

}
