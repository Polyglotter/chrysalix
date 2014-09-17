/*
 * Modelspace (http://Modelspace.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * Modelspace is free software. Unless otherwise indicated, all code in Modelspace
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * Modelspace is distributed in the hope that it will be useful,
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

import org.polyglotter.common.BaseException;
import org.polyglotter.common.I18n;

/**
 * 
 */
public class ModelspaceException extends BaseException {

    /**
     * @param message
     *        a localizable exception message
     * @param arguments
     *        optional arguments to the supplied message
     */
    public ModelspaceException( final I18n message,
                             final Object... arguments ) {
        super( message, arguments );
    }

    /**
     * @param message
     *        the localizable exception message
     * @param arguments
     *        optional arguments to the supplied message
     */
    public ModelspaceException( final String message,
                             final Object... arguments ) {
        super( ModelspaceI18n.class, message, arguments );
    }

    /**
     * @param cause
     *        the underlying Throwable that caused of this exception
     * @param message
     *        the localizable exception message
     * @param arguments
     *        optional arguments to the supplied message
     */
    public ModelspaceException( final Throwable cause,
                             final I18n message,
                             final Object... arguments ) {
        super( cause, message, arguments );
    }

    /**
     * @param cause
     *        the underlying Throwable that caused of this exception
     * @param message
     *        the localizable exception message
     * @param arguments
     *        optional arguments to the supplied message
     */
    public ModelspaceException( final Throwable cause,
                             final String message,
                             final Object... arguments ) {
        super( ModelspaceI18n.class, cause, message, arguments );
    }
}
