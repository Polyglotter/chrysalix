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

import java.io.InputStream;

import org.modelspace.Data;
import org.modelspace.ModelspaceException;

/**
 * 
 */
public interface Importer {

    /**
     * @param stream
     *        the input stream from which the data will be imported
     * @param data
     *        the workspace data to which the data will be imported
     * @throws ModelspaceException
     *         if any error occurs
     */
    void execute( InputStream stream,
                  Data data ) throws ModelspaceException;

    /**
     * @param mimeType
     *        a MIME type
     * @return <code>true</code> if this importer supports data of the supplied MIME type
     * @throws ModelspaceException
     *         if any error occurs
     */
    boolean supports( String mimeType ) throws ModelspaceException;
}
