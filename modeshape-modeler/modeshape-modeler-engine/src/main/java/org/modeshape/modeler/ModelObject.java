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
 * 
 */
public interface ModelObject {

    /**
     * @return this model object's absolute workspace path; never <code>null</code>
     */
    String absolutePath();

    /**
     * @param name
     *        the first name of the new children to be added to this object
     * @param additionalNames
     *        additional names of the new children to be added to this object
     * @throws ModelerException
     *         if any error occurs
     */
    void addChild( String name,
                   String... additionalNames ) throws ModelerException;

    /**
     * @param primaryType
     *        the primary type of the children, or <code>null</code>
     * @param name
     *        the first name of the new children to be added to this object
     * @param additionalNames
     *        additional names of the new children to be added to this object
     * @throws ModelerException
     *         if any error occurs
     */
    void addChildOfType( String primaryType,
                         String name,
                         String... additionalNames ) throws ModelerException;

    /**
     * @param propertyName
     *        the name of one of this model object's single-valued properties
     * @return the Boolean value of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if any error occurs
     */
    Boolean booleanValue( String propertyName ) throws ModelerException;

    /**
     * @param propertyName
     *        the name of one of this model object's properties
     * @return the Boolean values of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if any error occurs
     */
    Boolean[] booleanValues( String propertyName ) throws ModelerException;

    /**
     * @param childName
     *        the name of one of this model object's children
     * @return the child model object with the supplied name
     * @throws ModelerException
     *         if any error occurs
     */
    ModelObject child( String childName ) throws ModelerException;

    /**
     * @param childName
     *        the name of one of this model object's children
     * @return <code>true</code> if this model object has multiple children with the supplied name
     * @throws ModelerException
     *         if any error occurs
     */
    boolean childHasSameNameSiblings( String childName ) throws ModelerException;

    /**
     * @return the child model objects of this model object
     * @throws ModelerException
     *         if any error occurs
     */
    ModelObject[] children() throws ModelerException;

    /**
     * @param childName
     *        the name of one of this model object's children
     * @return the child model objects of this model object with the supplied name
     * @throws ModelerException
     *         if any error occurs
     */
    ModelObject[] children( String childName ) throws ModelerException;

    /**
     * @param childName
     *        the name of one of this model object's children
     * @return <code>true</code> if this model object has a child with the supplied name
     * @throws ModelerException
     *         if any error occurs
     */
    boolean hasChild( String childName ) throws ModelerException;

    /**
     * @return <code>true</code> if this model object has children
     * @throws ModelerException
     *         if any error occurs
     */
    boolean hasChildren() throws ModelerException;

    /**
     * @return <code>true</code> if this model object has properties
     * @throws ModelerException
     *         if any error occurs
     */
    boolean hasProperties() throws ModelerException;

    /**
     * @param propertyName
     *        the name of one of this model object's properties
     * @return <code>true</code> if this model object has the a property with the supplied name
     * @throws ModelerException
     *         if any error occurs
     */
    boolean hasProperty( String propertyName ) throws ModelerException;

    /**
     * @return this model object's 0-based index relative to any other same-name-siblings, or -1 if this is a model
     */
    int index();

    /**
     * @param propertyName
     *        the name of one of this model object's single-valued properties
     * @return the Long value of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if any error occurs
     */
    Long longValue( String propertyName ) throws ModelerException;

    /**
     * @param propertyName
     *        the name of one of this model object's properties
     * @return the Long values of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if any error occurs
     */
    Long[] longValues( String propertyName ) throws ModelerException;

    /**
     * @return this model object's mixin types; never <code>null</code>
     * @throws ModelerException
     *         if any error occurs
     */
    String[] mixinTypes() throws ModelerException;

    /**
     * @return this model object's enclosing model. Never <code>null</code>.
     * @throws ModelerException
     *         if any error occurs
     */
    Model model() throws ModelerException;

    /**
     * @return this model object's path relative to its {@link #model() model}; never <code>null</code>
     * @throws ModelerException
     *         if any error occurs
     */
    String modelRelativePath() throws ModelerException;

    /**
     * @return this model object's name; never <code>null</code>
     * @throws ModelerException
     *         if any error occurs
     */
    String name() throws ModelerException;

    /**
     * @return this model object's primary type; never <code>null</code>
     * @throws ModelerException
     *         if any error occurs
     */
    String primaryType() throws ModelerException;

    /**
     * Prints this object's subtree to standard out
     * 
     * @throws ModelerException
     *         if any error occurs
     */
    void print() throws ModelerException;

    /**
     * @param propertyName
     *        the name of one of this model object's properties
     * @return <code>true</code> if the supplied property exists and has multiple values
     * @throws ModelerException
     *         if any error occurs
     */
    boolean propertyHasMultipleValues( String propertyName ) throws ModelerException;

    /**
     * @return the property names for this model object; never <code>null</code>.
     * @throws ModelerException
     *         if any error occurs
     */
    String[] propertyNames() throws ModelerException;

    /**
     * @param name
     *        the name of the first child to be removed from this object
     * @param additionalNames
     *        the names of additional children to be removed from this object
     * @throws ModelerException
     *         if any error occurs
     */
    void removeChild( String name,
                      String... additionalNames ) throws ModelerException;

    /**
     * @param types
     *        one or more mixin types for this model object, or <code>null</code>
     * @throws ModelerException
     *         if any error occurs
     */
    void setMixinTypes( String... types ) throws ModelerException;

    /**
     * @param type
     *        the non-empty primary type of this model object
     * @throws ModelerException
     *         if any error occurs
     */
    void setPrimaryType( String type ) throws ModelerException;

    /**
     * @param propertyName
     *        the name of one of this model object's properties
     * @param value
     *        the (first) value of the supplied property
     * @param additionalValues
     *        additional value(s) of the supplied property if it is multi-valued
     * @throws ModelerException
     *         if any error occurs
     */
    void setProperty( String propertyName,
                      Boolean value,
                      Boolean... additionalValues ) throws ModelerException;

    /**
     * @param propertyName
     *        the name of one of this model object's properties
     * @param value
     *        the (first) value of the supplied property
     * @param additionalValues
     *        additional value(s) of the supplied property if it is multi-valued
     * @throws ModelerException
     *         if any error occurs
     */
    void setProperty( String propertyName,
                      Long value,
                      Long... additionalValues ) throws ModelerException;

    /**
     * @param propertyName
     *        the name of one of this model object's properties
     * @param value
     *        the (first) value of the supplied property
     * @param additionalValues
     *        additional value(s) of the supplied property if it is multi-valued
     * @throws ModelerException
     *         if any error occurs
     */
    void setProperty( String propertyName,
                      String value,
                      String... additionalValues ) throws ModelerException;

    /**
     * @param propertyName
     *        the name of one of this model object's single-valued properties
     * @return the String value of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if any error occurs
     */
    String stringValue( String propertyName ) throws ModelerException;

    /**
     * @param propertyName
     *        the name of one of this model object's properties
     * @return the String values of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if any error occurs
     */
    String[] stringValues( String propertyName ) throws ModelerException;

    /**
     * @param propertyName
     *        the name of one of this model object's single-valued properties
     * @return the value of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if any error occurs
     */
    Object value( String propertyName ) throws ModelerException;

    /**
     * @param propertyName
     *        the name of one of this model object's properties
     * @return the values of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if any error occurs
     */
    Object[] values( String propertyName ) throws ModelerException;

}