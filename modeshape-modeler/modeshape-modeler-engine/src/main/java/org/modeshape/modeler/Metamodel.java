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

import org.modeshape.jcr.api.sequencer.Sequencer;
import org.modeshape.modeler.extensions.DependencyProcessor;
import org.modeshape.modeler.extensions.Desequencer;

/**
 * 
 */
public interface Metamodel {

    /**
     * @return the category of this metamodel
     */
    String category();

    /**
     * @return the dependency processor or <code>null</code> if one does not exist
     * @throws ModelerException
     *         if a problem occurs
     */
    DependencyProcessor dependencyProcessor() throws ModelerException;

    /**
     * @return this metamodel's desequencer or <code>null</code> if one does not exist
     * @throws ModelerException
     *         if a problem occurs
     */
    Desequencer desequencer() throws ModelerException;

    /**
     * @return the ID of this metamodel
     */
    String id();

    /**
     * @return the name of this metamodel
     */
    String name();

    /**
     * @return this metamodel's sequencer (never <code>null</code>)
     * @throws ModelerException
     *         if any problem occurs
     */
    Sequencer sequencer() throws ModelerException;

    /**
     * @param name
     *        the name of this metamodel
     */
    void setName( String name );

    /**
     * @return the source file extensions associated with this metamodel
     */
    String[] sourceFileExtensions();
}
