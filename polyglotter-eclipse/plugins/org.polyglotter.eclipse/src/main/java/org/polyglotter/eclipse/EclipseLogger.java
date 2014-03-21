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
package org.polyglotter.eclipse;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.polyglotter.common.I18n;
import org.polyglotter.common.Logger;

/**
 * The <code>EclipseLogger</code> class provides an <code>Logger</code> implementation that uses the Eclipse logger.
 */
public final class EclipseLogger extends Logger {

    /**
     * <code>true</code> if info-level logging is enabled
     */
    public static boolean infoEnabled;

    private final String name;

    /**
     * @param name
     *        the name of this logger
     */
    public EclipseLogger( final String name ) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#debug(String, Object[])
     */
    @Override
    public void debug( final String message,
                       final Object... params ) {
        debug( null, message, params );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#debug(Throwable, String, Object[])
     */
    @Override
    public void debug( final Throwable error,
                       String message,
                       final Object... params ) {
        message = message.replace( '\'', '"' ); // TODO Temporary until ModeShape fixed
        if ( isDebugEnabled() ) log( IStatus.INFO, MessageFormat.format( message, params ), error );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#error(I18n, Object[])
     */
    @Override
    public void error( final I18n message,
                       final Object... params ) {
        error( null, message, params );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#error(Throwable, I18n, Object[])
     */
    @Override
    public void error( final Throwable error,
                       final I18n message,
                       final Object... params ) {
        if ( isErrorEnabled() ) log( IStatus.ERROR, message.text( params ), error );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#info(I18n, Object[])
     */
    @Override
    public void info( final I18n message,
                      final Object... params ) {
        info( null, message, params );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#info(Throwable, I18n, Object[])
     */
    @Override
    public void info( final Throwable error,
                      final I18n message,
                      final Object... params ) {
        if ( isInfoEnabled() ) log( IStatus.INFO, message.text( params ), error );
    }

    /**
     * @return <code>true</code> if information-level logging is enabled
     */
    public boolean infoLoggingEnabled() {
        return infoEnabled;
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#isDebugEnabled()
     */
    @Override
    public boolean isDebugEnabled() {
        return isInfoEnabled();
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#isErrorEnabled()
     */
    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#isInfoEnabled()
     */
    @Override
    public boolean isInfoEnabled() {
        return infoEnabled;
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#isTraceEnabled()
     */
    @Override
    public boolean isTraceEnabled() {
        return isInfoEnabled();
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#isWarnEnabled()
     */
    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    private void log( final int severity,
                      final String message,
                      final Throwable throwable ) {
        if ( severity == IStatus.INFO && !infoLoggingEnabled() ) return;
        if ( Platform.isRunning() ) Activator.plugin.getLog().log( new Status( severity,
                                                                               Activator.plugin.getBundle().getSymbolicName(),
                                                                               message,
                                                                               throwable ) );
        else if ( severity == IStatus.ERROR ) {
            System.err.println( message );
            if ( throwable != null ) System.err.println( throwable );
        } else {
            System.out.println( message );
            if ( throwable != null ) System.out.println( throwable );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#trace(String, Object[])
     */
    @Override
    public void trace( final String message,
                       final Object... params ) {
        trace( null, message, params );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#trace(Throwable, String, Object[])
     */
    @Override
    public void trace( final Throwable t,
                       final String message,
                       final Object... params ) {
        if ( isTraceEnabled() ) debug( message, t );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#warn(I18n, Object[])
     */
    @Override
    public void warn( final I18n message,
                      final Object... params ) {
        warn( null, message, params );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Logger#warn(Throwable, I18n, Object[])
     */
    @Override
    public void warn( final Throwable error,
                      final I18n message,
                      final Object... params ) {
        if ( isWarnEnabled() ) log( IStatus.WARNING, message.text( params ), error );
    }
}