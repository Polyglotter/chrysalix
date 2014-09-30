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
package org.modelspace;

/**
 * An element contained in a {@link Model model}.
 */
public interface ModelElement {

    /**
     * @return the model element's absolute workspace path (never <code>null</code> or empty)
     * @throws ModelspaceException
     *         if an error occurs
     */
    String absolutePath() throws ModelspaceException;

    /**
     * @return this model element's enclosing model
     * @throws ModelspaceException
     *         if an error occurs
     */
    Model model() throws ModelspaceException;

    /**
     * @return the model element's path relative to its {@link Model model}
     * @throws ModelspaceException
     *         if an error occurs
     */
    String modelRelativePath() throws ModelspaceException;

    /**
     * @return the model element's name (never <code>null</code> or empty)
     * @throws ModelspaceException
     *         if an error occurs
     */
    String name() throws ModelspaceException;

}
