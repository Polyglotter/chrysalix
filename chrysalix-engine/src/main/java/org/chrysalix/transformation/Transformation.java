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
import org.chrysalix.common.CheckArg;
import org.modelspace.Model;

/**
 * A class representing a transformation. A transformation uses an ordered set of {@link Operation operations} to calculate a result
 * consisting of one or more {@link Value values}.
 */
public interface Transformation extends Iterable< Operation< ? > > {

    /**
     * Adds models to a transformation or adds a {@link ModelType model type} to an existing model.
     * 
     * @param modelType
     *        the model type (cannot be <code>null</code>)
     * @param models
     *        the models being added to the transformation (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the model type is <code>null</code> or if the models being added is <code>null</code> or empty
     * @throws ChrysalixException
     *         if a model is <code>null</code>, if a model being added has already been added and already has the specified model
     *         type, or if a model cannot be added
     */
    void add( final ModelType modelType,
              final Model... models ) throws ChrysalixException;

    /**
     * @param operations
     *        the operations being added (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the operations being added are <code>null</code> or empty
     * @throws ChrysalixException
     *         if any of the operations have already been added, if an operation is <code>null</code>, or if an operation cannot be
     *         added
     */
    void add( final Operation< ? >... operations ) throws ChrysalixException;

    /**
     * @return a unique identifier (never <code>null</code> or empty)
     */
    String id();

    /**
     * @return a collection of the {@link Operation operations} performed by this transformation (never <code>null</code> but can be
     *         empty)
     * @see Operation#NO_OPERATIONS
     */
    Operation< ? >[] operations();

    /**
     * @param models
     *        the models being removed from the transformation (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the models being removed is <code>null</code> or empty
     * @throws ChrysalixException
     *         if a model is <code>null</code> or if it cannot be removed
     */
    void remove( final Model... models ) throws ChrysalixException;

    /**
     * Removes a model type from an added model or removes the model completely if there are no more model types associcated with
     * the model.
     * 
     * @param modelType
     *        the model type (cannot be <code>null</code>)
     * @param models
     *        the models whose model type is being removed (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the model type is <code>null</code> or if the models being removed is <code>null</code> or empty
     * @throws ChrysalixException
     *         if a model is <code>null</code>, if a model does not have that model type, or if a model cannot be removed
     */
    void remove( final ModelType modelType,
                 final Model... models ) throws ChrysalixException;

    /**
     * @param operations
     *        the operations being removed (cannot be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if the operation being removed is <code>null</code>, empty, or any value in the array is <code>null</code>
     * @throws ChrysalixException
     *         if any of the operations cannot be found or cannot be removed
     */
    void remove( final Operation< ? >... operations ) throws ChrysalixException;

    /**
     * @return all source models (never <code>null</code> but can be empty)
     */
    Model[] sources();

    /**
     * @return all target models (never <code>null</code> but can be empty)
     */
    Model[] targets();

    /**
     * Indicates if a {@link Model model} is being used as a source, target, or both
     */
    enum ModelType {

        /**
         * The model is being used as only a source.
         */
        SOURCE,

        /**
         * The model is being used as both a source and a target.
         */
        SOURCE_TARGET,

        /**
         * The model is being used as only a target.
         */
        TARGET;

        public static boolean isSource( final ModelType modelType ) {
            CheckArg.notNull( modelType, "modelType" );
            return ( ( modelType == SOURCE ) || ( modelType == SOURCE_TARGET ) );
        }

        public static boolean isTarget( final ModelType modelType ) {
            CheckArg.notNull( modelType, "modelType" );
            return ( ( modelType == TARGET ) || ( modelType == SOURCE_TARGET ) );
        }

    }

}
