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
package org.modelspace;

import java.util.Map;

/**
 * Represents a model object.
 */
public interface ModelObject extends ModelElement {

    /**
     * @param name
     *        the first name of the new children to be added to this object
     * @param additionalNames
     *        additional names of the new children to be added to this object
     * @return the child model object(s) created (never <code>null</code> or empty)
     * @throws ModelspaceException
     *         if any error occurs
     */
    ModelObject[] addChild( final String name,
                            final String... additionalNames ) throws ModelspaceException;

    /**
     * Adds a child with the supplied name and primary type. If the primary type has mandatory properties, use
     * {@link #addChildOfType} instead.
     * 
     * @param primaryTypeId
     *        the primary type ID of the child, or <code>null</code>
     * @param name
     *        the name of the new child to be added to this object
     * @param valuesByProperty
     *        values identified by their (possibly mandatory) properties to be set on the new child
     * @return the model object created (never <code>null</code>)
     * @throws ModelspaceException
     *         if any error occurs
     */
    ModelObject addChildOfType( final String primaryTypeId,
                                final String name,
                                final Map< String, ? > valuesByProperty ) throws ModelspaceException;

    /**
     * Adds a child with the supplied name and primary type. If the primary type has mandatory properties, use
     * {@link #addChildOfType(String, String, Map)} instead.
     * 
     * @param primaryTypeId
     *        the primary type ID of the children, or <code>null</code>
     * @param name
     *        the first name of the new children to be added to this object
     * @param additionalNames
     *        additional names of the new children to be added to this object
     * @return the model object(s) created (never <code>null</code> or empty)
     * @throws ModelspaceException
     *         if any error occurs
     */
    ModelObject[] addChildOfType( final String primaryTypeId,
                                  final String name,
                                  final String... additionalNames ) throws ModelspaceException;

    /**
     * @param typeId
     *        the type ID of a mixin type to set on this model object
     * @param valuesByProperty
     *        values identified by their (possibly mandatory) properties to be set on the new child
     * @throws ModelspaceException
     *         if any error occurs
     */
    void addMixinType( final String typeId,
                       final Map< String, ? > valuesByProperty ) throws ModelspaceException;

    /**
     * @param typeId
     *        the ID of the first mixin type to be added to this object
     * @param additionalTypeIds
     *        the IDs of additional mixin types to be added to this object
     * @throws ModelspaceException
     *         if any error occurs
     */
    void addMixinType( final String typeId,
                       final String... additionalTypeIds ) throws ModelspaceException;

    /**
     * @param childName
     *        the name of one of this model object's children
     * @return the child model object with the supplied name
     * @throws ModelspaceException
     *         if any error occurs
     */
    ModelObject child( final String childName ) throws ModelspaceException;

    /**
     * @param childName
     *        the name of one of this model object's children
     * @return <code>true</code> if this model object has multiple children with the supplied name
     * @throws ModelspaceException
     *         if any error occurs
     */
    boolean childHasSameNameSiblings( final String childName ) throws ModelspaceException;

    /**
     * @return the child model objects of this model object
     * @throws ModelspaceException
     *         if any error occurs
     */
    ModelObject[] children() throws ModelspaceException;

    /**
     * @param childName
     *        the name of one of this model object's children
     * @return the child model objects of this model object with the supplied name
     * @throws ModelspaceException
     *         if any error occurs
     */
    ModelObject[] children( final String childName ) throws ModelspaceException;

    /**
     * @param primaryTypeId
     *        the primary type ID of the children being requested (cannot be <code>null</code> or empty)
     * @return the matching children (never <code>null</code> but can be empty)
     * @throws ModelspaceException
     *         if an error occurs
     */
    ModelObject[] childrenOfType( final String primaryTypeId ) throws ModelspaceException;

    /**
     * @param childName
     *        the name of one of this model object's children
     * @return <code>true</code> if this model object has a child with the supplied name
     * @throws ModelspaceException
     *         if any error occurs
     */
    boolean hasChild( final String childName ) throws ModelspaceException;

    /**
     * @return <code>true</code> if this model object has children
     * @throws ModelspaceException
     *         if any error occurs
     */
    boolean hasChildren() throws ModelspaceException;

    /**
     * @return <code>true</code> if this model object has properties
     * @throws ModelspaceException
     *         if any error occurs
     */
    boolean hasProperties() throws ModelspaceException;

    /**
     * @param propertyName
     *        the name of one of this model object's properties
     * @return <code>true</code> if this model object has the a property with the supplied name
     * @throws ModelspaceException
     *         if any error occurs
     */
    boolean hasProperty( final String propertyName ) throws ModelspaceException;

    /**
     * @return this model object's 0-based index relative to any other same-name-siblings, or -1 if this is a model
     */
    int index();

    /**
     * @return this model object's mixin type descriptors (never <code>null</code> but can be empty)
     * @throws ModelspaceException
     *         if any error occurs
     */
    Descriptor[] mixinTypes() throws ModelspaceException;

    /**
     * <<<<<<< HEAD:modelspace/modelspace-engine/src/main/java/org/modelspace/ModelObject.java
     * 
     * @return this model object's enclosing model
     * @throws ModelspaceException
     *         if any error occurs
     */
    @Override
    Model model() throws ModelspaceException;

    /**
     * @return this model object's path relative to its {@link #model() model}
     * @throws ModelspaceException
     *         if any error occurs
     */
    @Override
    String modelRelativePath() throws ModelspaceException;

    /**
     * @return this model object's name
     * @throws ModelspaceException
     *         if any error occurs
     */
    @Override
    String name() throws ModelspaceException;

    /**
     * ======= >>>>>>> Issue 170 Change transformation framework to use ModeShape structure via
     * CND:modeshape-modeler/modeshape-modeler-engine/src/main/java/org/modeshape/modeler/ModelObject.java
     * 
     * @return this model object's primary type descriptor (never <code>null</code>)
     * @throws ModelspaceException
     *         if any error occurs
     */
    Descriptor primaryType() throws ModelspaceException;

    /**
     * Prints this object's subtree to standard out
     * 
     * @throws ModelspaceException
     *         if any error occurs
     */
    void print() throws ModelspaceException;

    /**
     * @param propertyName
     *        the name of model object property being selected (cannot be <code>null</code> or empty)
     * @return the property or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if any error occurs
     */
    ModelProperty property( final String propertyName ) throws ModelspaceException;

    /**
     * @return the property names for this model object
     * @throws ModelspaceException
     *         if any error occurs
     */
    String[] propertyNames() throws ModelspaceException;

    /**
     * @param name
     *        the name of the first child to be removed from this object
     * @param additionalNames
     *        the names of additional children to be removed from this object
     * @throws ModelspaceException
     *         if any error occurs
     */
    void removeChild( final String name,
                      final String... additionalNames ) throws ModelspaceException;

    /**
     * @param typeId
     *        the ID of the first mixin type to be removed from this object
     * @param additionalTypeIds
     *        the IDs of additional mixin types to be removed from this object
     * @throws ModelspaceException
     *         if any error occurs
     */
    void removeMixinType( final String typeId,
                          final String... additionalTypeIds ) throws ModelspaceException;

    /**
     * @param typeId
     *        the type ID of a mixin type to set on this model object, or <code>null</code>
     * @param valuesByProperty
     *        values identified by their (possibly mandatory) properties to be set on the new child
     * @throws ModelspaceException
     *         if any error occurs
     */
    void setMixinType( final String typeId,
                       final Map< String, ? > valuesByProperty ) throws ModelspaceException;

    /**
     * @param typeIds
     *        one or more mixin type IDs for this model object, or <code>null</code>
     * @throws ModelspaceException
     *         if any error occurs
     */
    void setMixinTypes( final String... typeIds ) throws ModelspaceException;

    /**
     * @param typeId
     *        the primary type ID of this model object, or <code>null</code>
     * @throws ModelspaceException
     *         if any error occurs
     */
    void setPrimaryType( final String typeId ) throws ModelspaceException;

    /**
     * @param typeId
     *        the primary type ID of this model object, or <code>null</code>
     * @param valuesByProperty
     *        values identified by their (possibly mandatory) properties to be set on the new child
     * @throws ModelspaceException
     *         if any error occurs
     */
    void setPrimaryType( final String typeId,
                         final Map< String, ? > valuesByProperty ) throws ModelspaceException;

    /**
     * Creates the property if it does not exist. Passing a <code>null</code> value will remove the property. Passing multiple
     * values should only be used for creating and setting multi-valued properties.
     * 
     * @param propertyName
     *        the name of one of this model object's properties (cannot be <code>null</code> or empty)
     * @param values
     *        one or more new values
     * @throws ModelspaceException
     *         if an error occurs
     * @throws IllegalArgumentException
     *         if trying to set a single-valued property with multiple values or trying to set values that are not compatible with
     *         the property definition
     */
    void setProperty( final String propertyName,
                      final Object... values ) throws ModelspaceException;

}