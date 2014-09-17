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

import org.chrysalix.common.I18n;
import org.chrysalix.common.Logger;
import org.chrysalix.common.Logger.Level;

/**
 * Utilities
 */
public final class UiUtil {

    private static String context() {
        final StackTraceElement element = Thread.currentThread().getStackTrace()[ 3 ];
        return element.getClassName() + '.' + element.getMethodName();
    }

    /**
     * @param path
     *        the relative path of one of the images provided by this UI library under the <code>images</code> folder
     * @return the URL of the image with the supplied path
     */
    public static URL imageUrl( final String path ) {
        final URL url = UiUtil.class.getClassLoader().getResource( "images/" + path );
        if ( url == null ) logError( UiI18n.missingImage, path );
        return url;
    }

    /**
     * 
     */
    public static void logContextTrace() {
        Logger.getLogger( context() ).trace( "==== " + context() + " ====" );
    }

    /**
     * @param message
     *        a contextual message to be shown
     * @param messageParameters
     *        any parameters required by the supplied message
     */
    public static void logError( final I18n message,
                                 final Object... messageParameters ) {
        logError( null, message, messageParameters );
    }

    /**
     * @param e
     *        a throwable
     * @param message
     *        a contextual message to be shown before the supplied throwable's message
     * @param messageParameters
     *        any parameters required by the supplied message
     */
    public static void logError( final Throwable e,
                                 final I18n message,
                                 final Object... messageParameters ) {
        Logger.getLogger( context() ).error( e, message, messageParameters );
    }

    /**
     * @param message
     *        a contextual message to be shown
     * @param messageParameters
     *        any parameters required by the supplied message
     */
    public static void logTrace( final String message,
                                 final Object... messageParameters ) {
        Logger.getLogger( context() ).trace( message, messageParameters );
    }

    /**
     * @param level
     *        a log level
     */
    public static void setLogLevel( final Level level ) {
        Logger.getLogger( context() ).setLevel( level );
    }

    /**
     * Don't allow construction outside this class.
     */
    private UiUtil() {}
}
