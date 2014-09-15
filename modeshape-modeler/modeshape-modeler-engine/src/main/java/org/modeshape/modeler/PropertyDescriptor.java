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

/**
 * A {@link ModelObject model object} {@link ModelProperty property} descriptor.
 */
public interface PropertyDescriptor {

    /**
     * An empty array of objects.
     */
    Object[] NO_VALUES = {};

    /**
     * An empty array of property descriptors.
     */
    PropertyDescriptor[] NO_DESCRIPTORS = {};

    /**
     * If the property is multi-valued, there can be more than one default value. For a single-valued property there can be at most
     * one default value.
     * 
     * @return the default values or an empty array if no default values exist.
     * @throws ModelerException
     *         if an error occurs
     */
    Object[] defaultValues() throws ModelerException;

    /**
     * @return <code>true</code> if property is required
     * @throws ModelerException
     *         if an error occurs
     */
    boolean mandatory() throws ModelerException;

    /**
     * @return <code>true</code> if the property value is modifiable
     * @throws ModelerException
     *         if an error occurs
     */
    boolean modifiable() throws ModelerException;

    /**
     * @return <code>true</code> if this property is multi-valued
     * @throws ModelerException
     *         if an error occurs
     */
    boolean multiple() throws ModelerException;

    /**
     * @return the property name (never <code>null</code> or empty)
     * @throws ModelerException
     *         if an error occurs
     */
    String name() throws ModelerException;

    /**
     * @return the type of the property (never <code>null</code>)
     * @throws ModelerException
     *         if an error occurs
     */
    Type type() throws ModelerException;

    /**
     * The property type.
     */
    public enum Type {

        /**
         * A binary property.
         */
        BINARY,

        /**
         * A boolean property.
         */
        BOOLEAN,

        /**
         * A date property.
         */
        DATE,

        /**
         * A decimal property.
         */
        DECIMAL,

        /**
         * A double property.
         */
        DOUBLE,

        /**
         * A long property.
         */
        LONG,

        /**
         * A name property has a namespace and a local name.
         */
        NAME,

        /**
         * A path property.
         */
        PATH,

        /**
         * A reference property that is used to enforce referential integrity
         */
        REFERENCE,

        /**
         * A string property.
         */
        STRING,

        /**
         * Property can be of any type.
         */
        UNDEFINED,

        /**
         * A string property whose value must conform to the URI syntax.
         */
        URI,

        /**
         * A reference property that is not used to enforce referential integrity.
         */
        WEAKREFERENCE

    }

}
