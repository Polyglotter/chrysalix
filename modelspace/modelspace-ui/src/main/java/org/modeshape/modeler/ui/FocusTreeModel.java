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
package org.modeshape.modeler.ui;

import java.net.URL;
import java.util.Collection;

import org.modeshape.modeler.ModelerException;
import org.chrysalix.common.CheckArg;
import org.chrysalix.common.ObjectUtil;

/**
 * The default model used by a {@link FocusTreeController}
 */
public class FocusTreeModel {

    /**
     *
     */
    public static final Indicator[] NO_INDICATORS = new Indicator[ 0 ];

    /**
     * @param parent
     *        a parent item in the tree
     * @param index
     *        the index within the supplied parent where a new item is to be added
     * @return The newly added item. Must not be <code>null</code> unless {@link #childrenAddable(Object) children can not be added
     *         to the supplied parent}. Default is <code>null</code>
     * @throws ModelerException
     *         if an error occurs
     */
    public Object add( final Object parent,
                       final int index ) throws ModelerException {
        return true;
    }

    /**
     * @param item
     *        an item in the tree
     * @return the number of children of the supplied item. Default is 0.
     * @throws ModelerException
     *         if an error occurs
     */
    public int childCount( final Object item ) throws ModelerException {
        return 0;
    }

    /**
     * @param item
     *        an item in the tree
     * @return the children of the supplied item. Must not be <code>null</code>. Default is an empty array.
     * @throws ModelerException
     *         if an error occurs
     */
    public Object[] children( final Object item ) throws ModelerException {
        return ObjectUtil.EMPTY_ARRAY;
    }

    /**
     * @param item
     *        an item in the tree
     * @return <code>true</code> if the supplied item can be added. Default is <code>true</code> for collections and arrays.
     */
    public boolean childrenAddable( final Object item ) {
        return item.getClass().isArray() || item instanceof Collection< ? >;
    }

    /**
     * @param item
     *        an item in the tree
     * @return <code>true</code> if the supplied item can be deleted. Default is <code>false</code>
     */
    public boolean deletable( final Object item ) {
        return false;
    }

    /**
     * @param item
     *        an item in the tree
     * @return <code>true</code> if the supplied item was successfully deleted. Default is <code>true</code>
     */
    public boolean delete( final Object item ) {
        return true;
    }

    /**
     * @param item
     *        an item in the tree
     * @return <code>true</code> if the supplied item has children. Default is <code>false</code>,
     * @throws ModelerException
     *         if an error occurs
     */
    public boolean hasChildren( final Object item ) throws ModelerException {
        return false;
    }

    /**
     * @param item
     *        an item in the tree
     * @return <code>true</code> if the supplied item has a name. Default is <code>true</code>
     * @throws ModelerException
     *         if an error occurs
     */
    public boolean hasName( final Object item ) throws ModelerException {
        return true;
    }

    /**
     * @param item
     *        an item in the tree
     * @return <code>true</code> if the supplied item has a type. Default is <code>false</code>
     */
    public boolean hasType( final Object item ) {
        return false;
    }

    /**
     * @param item
     *        an item in the tree
     * @return <code>true</code> if the supplied item has a value. Default is <code>false</code>
     * @throws ModelerException
     *         if an error occurs
     */
    public boolean hasValue( final Object item ) throws ModelerException {
        return false;
    }

    /**
     * @param item
     *        an item in the tree
     * @param parent
     *        the parent of the supplied item
     * @return the supplied item's index, or <code>-1</code> if not found. Default is the item's index within
     *         {@link #children(Object)}.
     * @throws ModelerException
     *         if an error occurs
     */
    public int indexOf( final Object item,
                        final Object parent ) throws ModelerException {
        int ndx = 0;
        for ( final Object child : children( parent ) ) {
            if ( child.equals( item ) ) return ndx;
            ndx++;
        }
        return -1;
    }

    /**
     * @param item
     *        an item in the tree
     * @return the status indicators applicable to the supplied item. Must not be <code>null</code>. Default is an empty array.
     */
    public Indicator[] indicators( final Object item ) {
        return NO_INDICATORS;
    }

    /**
     * @param item
     *        an item in the tree
     * @return <code>true</code> if the supplied item can be moved to a different index within its parent. Default is
     *         <code>false</code>
     */
    public boolean movable( final Object item ) {
        return false;
    }

    /**
     * @param item
     *        an item in the tree
     * @return the name of the supplied item's cell. Must not be <code>null</code>. Default is the item's {@link Object#toString()}.
     * @throws ModelerException
     *         if an error occurs
     */
    public Object name( final Object item ) throws ModelerException {
        return item.toString();
    }

    /**
     * @param item
     *        an item in the tree
     * @return <code>true</code> if the supplied item's name can be edited. Default is <code>false</code>
     */
    public boolean nameEditable( final Object item ) {
        return false;
    }

    /**
     * @param item
     *        an item in the tree
     * @param name
     *        a name for the supplied item
     * @return the problem message for the supplied name for the supplied item, or <code>null</code> if the name is valid. Default
     *         is <code>null</code>.
     */
    public String nameProblem( final Object item,
                               final Object name ) {
        return null;
    }

    /**
     * @param item
     *        an item in the tree
     * @return the fully-qualified name of the supplied item's cell. Must not be <code>null</code>. Default is the item's
     *         {@link #name(Object) name}
     * @throws ModelerException
     *         if an error occurs
     */
    public Object qualifiedName( final Object item ) throws ModelerException {
        return name( item );
    }

    /**
     * If an item is {@link #movable(Object) movable}, called after a user changes the supplied item's index to the supplied index.
     * Does nothing by default.
     * 
     * @param item
     *        an item in the tree
     * @param parent
     *        the parent of the supplied item
     * @param index
     *        an index for the supplied item
     * @return an item with the supplied index. Default is the supplied item.
     * @throws ModelerException
     *         if an error occurs
     */
    public Object setIndex( final Object item,
                            final Object parent,
                            final int index ) throws ModelerException {
        return item;
    }

    /**
     * Called after a user changes the supplied item's name to the supplied name. Does nothing by default.
     * 
     * @param item
     *        an item in the tree
     * @param name
     *        a name for the supplied item
     * @return an item with the supplied name. Must not be <code>null</code>. Default is the supplied item.
     * @throws ModelerException
     *         if an error occurs
     */
    public Object setName( final Object item,
                           final Object name ) throws ModelerException {
        return item;
    }

    /**
     * Called after a user changes the supplied item's type to the supplied type. Does nothing by default.
     * 
     * @param item
     *        an item in the tree
     * @param type
     *        a type for the supplied item
     * @return an item with the supplied type. Must not be <code>null</code>. Default is the supplied item.
     * @throws ModelerException
     *         if an error occurs
     */
    public Object setType( final Object item,
                           final Object type ) throws ModelerException {
        return item;
    }

    /**
     * Called after a user changes the supplied item's value to the supplied value. Does nothing by default.
     * 
     * @param item
     *        an item in the tree
     * @param value
     *        a value for the supplied item
     * @return an item with the supplied value. Default is the supplied item.
     * @throws ModelerException
     *         if an error occurs
     */
    public Object setValue( final Object item,
                            final Object value ) throws ModelerException {
        return item;
    }

    /**
     * @param item
     *        an item in the tree
     * @return the type of the supplied item's cell. Default is the item's simple class name.
     * @throws ModelerException
     *         if an error occurs
     */
    public Object type( final Object item ) throws ModelerException {
        return item.getClass().getSimpleName();
    }

    /**
     * @param item
     *        an item in the tree
     * @return <code>true</code> if the supplied item's type can be edited. Default is <code>false</code>
     */
    public boolean typeEditable( final Object item ) {
        return false;
    }

    /**
     * @param item
     *        an item in the tree
     * @param type
     *        a type for the supplied item
     * @return the problem message for the supplied type for the supplied item, or <code>null</code> if the type is valid. Default
     *         is <code>null</code>.
     */
    public String typeProblem( final Object item,
                               final Object type ) {
        return null;
    }

    /**
     * @param item
     *        an item in the tree
     * @return the value of the supplied item's cell. Default is <code>null</code>.
     * @throws ModelerException
     *         if an error occurs
     */
    public Object value( final Object item ) throws ModelerException {
        return null;
    }

    /**
     * @param item
     *        an item in the tree
     * @return <code>true</code> if the supplied item's value can be edited. Default is <code>false</code>
     */
    public boolean valueEditable( final Object item ) {
        return false;
    }

    /**
     * @param item
     *        an item in the tree
     * @param value
     *        a value for the supplied item
     * @return the problem message for the supplied value for the supplied item, or <code>null</code> if the value is valid. Default
     *         is <code>null</code>.
     */
    public String valueProblem( final Object item,
                                final Object value ) {
        return null;
    }

    /**
     * 
     */
    public static class Indicator {

        final URL imageUrl;
        final String toolTip;

        /**
         * @param imageUrl
         *        the image URL for this indicator's button
         * @param toolTip
         *        the tool tip for this indicator's button
         */
        public Indicator( final URL imageUrl,
                          final String toolTip ) {
            CheckArg.notNull( imageUrl, "imageUrl" );
            CheckArg.notEmpty( toolTip, "toolTip" );
            this.imageUrl = imageUrl;
            this.toolTip = toolTip;
        }

        /**
         * Does nothing by default.
         * 
         * @param item
         *        the item containing this indicator
         */
        protected void selected( final Object item ) {}
    }
}
