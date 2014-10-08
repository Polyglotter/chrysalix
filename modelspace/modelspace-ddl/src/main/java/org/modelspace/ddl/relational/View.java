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

/**
 * View The relational view class
 */
public class View extends Table {

    View( final IObjectDelegate modelObject ) {
        super( modelObject );
    }

    // @Override
    // public boolean addForeignKey( ForeignKey fk ) {
    //        throw new UnsupportedOperationException("addForeignKey() not supported for Relational Views"); //$NON-NLS-1$
    // }

    // @Override
    // public boolean setPrimaryKey( PrimaryKey pk ) {
    //        throw new UnsupportedOperationException("addPrimaryKey() not supported for Relational Views"); //$NON-NLS-1$
    // }

    // @Override
    // public boolean setUniqueConstraint( UniqueConstraint uc ) {
    //        throw new UnsupportedOperationException("addUniqueConstraint() not supported for Relational Views"); //$NON-NLS-1$
    // }

}
