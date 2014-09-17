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
package org.modelspace.spi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.modeshape.common.util.CheckArg;
import org.modeshape.common.util.StringUtil;

/**
 * A model dependency.
 */
public class Dependency {

    private static final List< String > NO_SRC_REFS = Collections.emptyList();

    private final boolean exists;

    private final String path;

    private final List< String > sourceReferences;

    /**
     * @param path
     *        the workspace full path of the dependency (can be <code>null</code> or empty)
     * @param sourceReferences
     *        the source document dependency references (cannot be <code>null</code> or empty)
     * @param exists
     *        <code>true</code> if the dependency exists in the workspace
     */
    public Dependency( final String path,
                       final List< String > sourceReferences,
                       final boolean exists ) {
        CheckArg.isNotEmpty( sourceReferences, "sourceReferences" );

        this.path = path;
        this.exists = exists;
        this.sourceReferences = new ArrayList<>( sourceReferences );
    }

    /**
     * @param path
     *        the workspace full path of the dependency (can be <code>null</code> or empty)
     * @param sourceReference
     *        the source document dependency reference (cannot be <code>null</code> or empty)
     * @param exists
     *        <code>true</code> if the dependency exists in the workspace
     */
    public Dependency( final String path,
                       final String sourceReference,
                       final boolean exists ) {
        CheckArg.isNotEmpty( sourceReference, "sourceReference" );

        this.path = path;
        this.exists = exists;
        this.sourceReferences = Collections.singletonList( sourceReference );
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if ( ( obj == null ) || !getClass().equals( obj.getClass() ) ) {
            return false;
        }

        final Dependency that = ( Dependency ) obj;

        if ( this.sourceReferences.equals( that.sourceReferences ) ) {
            if ( this.path == null ) {
                return ( that.path == null );
            }

            if ( that.path == null ) {
                return false;
            }

            return this.path.equals( that.path );
        }

        return false;
    }

    /**
     * @return <code>true</code> if exists in the workspace
     */
    public boolean exists() {
        return this.exists;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hashCode = this.sourceReferences.hashCode();

        if ( !StringUtil.isBlank( this.path ) ) {
            hashCode = ( ( 31 * hashCode ) + this.path.hashCode() );
        }

        return hashCode;
    }

    /**
     * @return the workspace path (can be <code>null</code> or empty if not known)
     */
    public String path() {
        return this.path;
    }

    /**
     * @return a collection of inputs used when creating this dependency (never <code>null</code> but can be empty)
     */
    public List< String > sourceReferences() {
        if ( ( this.sourceReferences == null ) || this.sourceReferences.isEmpty() ) {
            return NO_SRC_REFS;
        }

        return this.sourceReferences;
    }

}
