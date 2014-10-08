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
package org.modelspace.ddl.relational;

import java.util.List;
import java.util.Map;

import org.modelspace.ModelspaceException;

/**
 * Interface for delegate Relational Objects
 */
public interface IObjectDelegate {

    /**
     * Get the object children
     * 
     * @return the children
     * @throws ModelspaceException
     *         if an error occurs
     */
    public List< RelationalObject > getChildren() throws ModelspaceException;

    /**
     * Get the extension properties (prefixed options)
     * 
     * @return the extension property map
     */
    public Map< String, String > getExtensionProperties();

    /**
     * Get the object name
     * 
     * @return name
     */
    public String getName();

    /**
     * Get the value of objects option with specified name
     * 
     * @param optionName
     *        the option key
     * @return the option value
     */
    public Object getOptionValue( String optionName );

    /**
     * Get the value of objects property with specified name
     * 
     * @param propName
     *        the property key
     * @return the property value
     * @throws ModelspaceException
     *         if an error occurs
     */
    public Object getPropertyValue( String propName ) throws ModelspaceException;

    /**
     * Get the values of objects multi-valued property with specified name
     * 
     * @param propName
     *        the property key
     * @return the property values
     * @throws ModelspaceException
     *         if an error occurs
     */
    public Object[] getPropertyValues( String propName ) throws ModelspaceException;

    /**
     * Get the object type
     * 
     * @return the object type
     * @throws ModelspaceException
     *         if an error occurs
     */
    public RelationalConstants.Type getType() throws ModelspaceException;

    /**
     * Determine if object has children
     * 
     * @return 'true' if has children, 'false' if not
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean hasChildren() throws ModelspaceException;

    /**
     * Determine if the supplied key is an extension property
     * 
     * @param propName
     *        the property name
     * @return 'true' if the property is an extension, 'false' if not
     */
    public boolean isExtensionProperty( String propName );

    /**
     * Get the value of objects option with specified name
     * 
     * @param optionName
     *        the option key
     * @param value
     *        the value
     * @return 'true' if property was set, 'false' if not
     */
    public boolean setOptionValue( String optionName,
                                   Object value );

    /**
     * Set the value of object property with specified name
     * 
     * @param propName
     *        the property key
     * @param value
     *        the value
     * @return 'true' if property was set, 'false' if not
     */
    public boolean setPropertyValue( String propName,
                                     Object value );

    /**
     * Unset the objects option with specified name (remove it)
     * 
     * @param optionName
     *        the option key
     * @return 'true' if property was unset, 'false' if not
     */
    public boolean unsetOptionValue( String optionName );

}
