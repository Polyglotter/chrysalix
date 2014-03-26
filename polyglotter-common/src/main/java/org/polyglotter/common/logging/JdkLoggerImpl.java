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
package org.polyglotter.common.logging;

import org.polyglotter.common.I18n;
import org.polyglotter.common.Logger;

/**
 * Logger that delivers messages to a JDK logger
 */
final class JdkLoggerImpl extends Logger {

    private final java.util.logging.Logger logger;

    public JdkLoggerImpl( final String category ) {
        logger = java.util.logging.Logger.getLogger( category );
    }

    @Override
    public void debug( final String message,
                       final Object... params ) {
        log( java.util.logging.Level.FINE, String.format( message, params ), null );
    }

    @Override
    public void debug( final Throwable t,
                       final String message,
                       final Object... params ) {
        log( java.util.logging.Level.FINE, String.format( message, params ), t );
    }

    @Override
    public void error( final I18n message,
                       final Object... params ) {
        log( java.util.logging.Level.SEVERE, message.text( getLoggingLocale(), params ), null );
    }

    @Override
    public void error( final Throwable t,
                       final I18n message,
                       final Object... params ) {
        log( java.util.logging.Level.SEVERE, message.text( getLoggingLocale(), params ), t );
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public void info( final I18n message,
                      final Object... params ) {
        log( java.util.logging.Level.INFO, message.text( getLoggingLocale(), params ), null );
    }

    @Override
    public void info( final Throwable t,
                      final I18n message,
                      final Object... params ) {
        log( java.util.logging.Level.INFO, message.text( getLoggingLocale(), params ), t );
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isLoggable( java.util.logging.Level.FINE );
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isLoggable( java.util.logging.Level.SEVERE );
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isLoggable( java.util.logging.Level.INFO );
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isLoggable( java.util.logging.Level.FINER );
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isLoggable( java.util.logging.Level.WARNING );
    }

    private void log( final java.util.logging.Level level,
                      final String message,
                      final Throwable ex ) {
        if ( logger.isLoggable( level ) ) {
            final Throwable dummyException = new Throwable();
            final StackTraceElement locations[] = dummyException.getStackTrace();
            String className = "unknown";
            String methodName = "unknown";
            final int depth = 2;
            if ( locations != null && locations.length > depth ) {
                final StackTraceElement caller = locations[ depth ];
                className = caller.getClassName();
                methodName = caller.getMethodName();
            }
            if ( ex == null ) {
                logger.logp( level, className, methodName, message );
            } else {
                logger.logp( level, className, methodName, message, ex );
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.common.Logger#setLevel(org.polyglotter.common.Logger.Level)
     */
    @Override
    public void setLevel( final Level level ) {
        switch ( level ) {
            case OFF: {
                logger.setLevel( java.util.logging.Level.OFF );
                break;
            }
            case ERROR: {
                logger.setLevel( java.util.logging.Level.SEVERE );
                break;
            }
            case WARNING: {
                logger.setLevel( java.util.logging.Level.WARNING );
                break;
            }
            case INFO: {
                logger.setLevel( java.util.logging.Level.INFO );
                break;
            }
            case DEBUG: {
                logger.setLevel( java.util.logging.Level.FINE );
                break;
            }
            case TRACE: {
                logger.setLevel( java.util.logging.Level.FINER );
                break;
            }
        }
    }

    @Override
    public void trace( final String message,
                       final Object... params ) {
        log( java.util.logging.Level.FINER, String.format( message, params ), null );
    }

    @Override
    public void trace( final Throwable t,
                       final String message,
                       final Object... params ) {
        log( java.util.logging.Level.FINER, String.format( message, params ), t );

    }

    @Override
    public void warn( final I18n message,
                      final Object... params ) {
        log( java.util.logging.Level.WARNING, message.text( getLoggingLocale(), params ), null );
    }

    @Override
    public void warn( final Throwable t,
                      final I18n message,
                      final Object... params ) {
        log( java.util.logging.Level.WARNING, message.text( getLoggingLocale(), params ), t );

    }
}
