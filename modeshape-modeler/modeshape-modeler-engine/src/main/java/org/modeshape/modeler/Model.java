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
package org.modeshape.modeler;

import java.net.URL;
import java.util.Set;

import org.modeshape.modeler.extensions.Dependency;

/**
 * 
 */
public interface Model extends ModelObject {

    /**
     * @return <code>true</code> if all dependencies, recursively, exist in the repository.
     * @throws ModelerException
     *         if any error occurs
     */
    boolean allDependenciesExist() throws ModelerException;

    /**
     * @return the model's dependencies (never <code>null</code> but can be empty)
     * @throws ModelerException
     *         if any error occurs
     */
    Set< Dependency > dependencies() throws ModelerException;

    /**
     * @return the (last) external location, e.g., on the file system, known to contain a materialized representation of this model.
     * @throws ModelerException
     *         if any error occurs
     */
    URL externalLocation() throws ModelerException;

    /**
     * @return this model's metamodel (can be <code>null</code> if a metamodel is not found)
     * @throws ModelerException
     *         if any error occurs
     */
    Metamodel metamodel() throws ModelerException;

    /**
     * @return a collection of missing dependencies (never <code>null</code> but can be empty)
     * @throws ModelerException
     *         if any error occurs
     */
    Set< Dependency > missingDependencies() throws ModelerException;

}
