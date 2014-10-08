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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelspace.ModelspaceException;

/**
 * RelationalModel - the relational model object
 */
public class RelationalModel {

    private final IModelDelegate delegate;

    /**
     * Constructor
     * 
     * @param delegate
     *        the delegate
     */
    public RelationalModel( final IModelDelegate delegate ) {
        this.delegate = delegate;
    }

    /**
     * Get the model children
     * 
     * @return the children
     * @throws ModelspaceException
     *         if an error occurs
     */
    public List< RelationalObject > getChildren() throws ModelspaceException {
        return this.delegate.getChildren();
    }

    /**
     * Get children of provided type
     * 
     * @param type
     *        the type
     * @return the children of given type
     * @throws ModelspaceException
     *         if an error occurs
     */
    @SuppressWarnings( "unchecked" )
    private < T > List< T > getChildren( final Class< T > type ) throws ModelspaceException {
        final List< T > result = new ArrayList< T >();
        for ( final Object e : getChildren() ) {
            if ( type.isAssignableFrom( e.getClass() ) ) {
                result.add( ( T ) e );
            }
        }
        return result;
    }

    /**
     * Get the model name
     * 
     * @return name
     */
    public String getName() {
        return this.delegate.getName();
    }

    /**
     * Get mapping of namespaces - if any
     * 
     * @return the namespace key to uri map
     * @throws ModelspaceException
     *         if an error occurs
     */
    public Map< String, String > getNamespaceMap() throws ModelspaceException {
        return this.delegate.getNamespaceMap();
    }

    /**
     * Get the getProcedure with the specified name (if it exists)
     * 
     * @param procName
     *        the Procedure name
     * @return Procedure
     * @throws ModelspaceException
     *         if an error occurs
     */
    public Procedure getProcedure( final String procName ) throws ModelspaceException {
        Procedure result = null;
        final List< Procedure > kids = getProcedures();
        for ( final Procedure kid : kids ) {
            if ( kid.getName().equalsIgnoreCase( procName ) ) {
                result = kid;
                break;
            }
        }
        return result;
    }

    /**
     * Get the child getProcedures
     * 
     * @return Procedure list
     * @throws ModelspaceException
     *         if an error occurs
     */
    public List< Procedure > getProcedures() throws ModelspaceException {
        return getChildren( Procedure.class );
    }

    /**
     * Get the Table with the specified name (if it exists)
     * 
     * @param tableName
     *        the Table name
     * @return Table
     * @throws ModelspaceException
     *         if an error occurs
     */
    public Table getTable( final String tableName ) throws ModelspaceException {
        Table result = null;
        final List< Table > kids = getTables();
        for ( final Table kid : kids ) {
            if ( kid.getName().equalsIgnoreCase( tableName ) ) {
                result = kid;
                break;
            }
        }
        return result;
    }

    /**
     * Get the child Tables
     * 
     * @return Table
     * @throws ModelspaceException
     *         if an error occurs
     */
    public List< Table > getTables() throws ModelspaceException {
        return getChildren( Table.class );
    }

    /**
     * Determine if model has children
     * 
     * @return 'true' if has children, 'false' if not
     */
    public boolean hasChildren() {
        return this.delegate.hasChildren();
    }

}
