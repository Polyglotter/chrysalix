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
import org.slf4j.LoggerFactory;

/**
 * Logger that delivers messages to a Log4J logger
 * 
 * @since 2.5
 */
final class SLF4JLoggerImpl extends Logger {

    private static Boolean debugEnabled;
    private static Boolean errorEnabled;
    private static Boolean infoEnabled;
    private static Boolean traceEnabled;
    private static Boolean warnEnabled;

    private final org.slf4j.Logger logger;

    public SLF4JLoggerImpl( final String category ) {
        logger = LoggerFactory.getLogger( category );
    }

    /**
     * Log a message at the DEBUG level according to the specified format and (optional) parameters. The message should contain a
     * pair of empty curly braces for each of the parameter, which should be passed in the correct order. This method is efficient
     * and avoids superfluous object creation when the logger is disabled for the DEBUG level.
     * 
     * @param message
     *        the message string
     * @param params
     *        the parameter values that are to replace the variables in the format string
     */
    @Override
    public void debug( final String message,
                       final Object... params ) {
        if ( !isDebugEnabled() ) return;
        if ( message == null ) return;
        logger.debug( String.format( message, params ) );
    }

    /**
     * Log an exception (throwable) at the DEBUG level with an accompanying message. If the exception is null, then this method
     * calls {@link #debug(String, Object...)}.
     * 
     * @param t
     *        the exception (throwable) to log
     * @param message
     *        the message accompanying the exception
     * @param params
     *        the parameter values that are to replace the variables in the format string
     */
    @Override
    public void debug( final Throwable t,
                       final String message,
                       final Object... params ) {
        if ( !isDebugEnabled() ) return;
        if ( t == null ) {
            debug( message, params );
            return;
        }
        if ( message == null ) {
            logger.debug( null, t );
            return;
        }
        logger.debug( String.format( message, params ), t );
    }

    /**
     * Log a message at the ERROR level according to the specified format and (optional) parameters. The message should contain a
     * pair of empty curly braces for each of the parameter, which should be passed in the correct order. This method is efficient
     * and avoids superfluous object creation when the logger is disabled for the ERROR level.
     * 
     * @param message
     *        the message string
     * @param params
     *        the parameter values that are to replace the variables in the format string
     */
    @Override
    public void error( final I18n message,
                       final Object... params ) {
        if ( !isErrorEnabled() ) return;
        if ( message == null ) return;
        logger.error( message.text( getLoggingLocale(), params ) );
    }

    /**
     * Log an exception (throwable) at the ERROR level with an accompanying message. If the exception is null, then this method
     * calls {@link Logger#error(I18n, Object...)}.
     * 
     * @param t
     *        the exception (throwable) to log
     * @param message
     *        the message accompanying the exception
     * @param params
     *        the parameter values that are to replace the variables in the format string
     */
    @Override
    public void error( final Throwable t,
                       final I18n message,
                       final Object... params ) {
        if ( !isErrorEnabled() ) return;
        if ( t == null ) {
            error( message, params );
            return;
        }
        if ( message == null ) {
            logger.error( null, t );
            return;
        }
        logger.error( message.text( getLoggingLocale(), params ), t );
    }

    @Override
    public void error( final Throwable t,
                       final String message ) {
        if ( !isErrorEnabled() ) return;
        if ( t == null ) {
            logger.error( message );
            return;
        }
        if ( message == null ) {
            logger.error( null, t );
            return;
        }
        logger.error( message, t );
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    /**
     * Log a message at the INFO level according to the specified format and (optional) parameters. The message should contain a
     * pair of empty curly braces for each of the parameter, which should be passed in the correct order. This method is efficient
     * and avoids superfluous object creation when the logger is disabled for the INFO level.
     * 
     * @param message
     *        the message string
     * @param params
     *        the parameter values that are to replace the variables in the format string
     */
    @Override
    public void info( final I18n message,
                      final Object... params ) {
        if ( !isInfoEnabled() ) return;
        if ( message == null ) return;
        logger.info( message.text( getLoggingLocale(), params ) );
    }

    /**
     * Log an exception (throwable) at the INFO level with an accompanying message. If the exception is null, then this method calls
     * {@link Logger#info(I18n, Object...)}.
     * 
     * @param t
     *        the exception (throwable) to log
     * @param message
     *        the message accompanying the exception
     * @param params
     *        the parameter values that are to replace the variables in the format string
     */
    @Override
    public void info( final Throwable t,
                      final I18n message,
                      final Object... params ) {
        if ( !isInfoEnabled() ) return;
        if ( t == null ) {
            info( message, params );
            return;
        }
        if ( message == null ) {
            logger.info( null, t );
            return;
        }
        logger.info( message.text( getLoggingLocale(), params ), t );
    }

    @Override
    public boolean isDebugEnabled() {
        return debugEnabled == null ? logger.isDebugEnabled() : debugEnabled;
    }

    @Override
    public boolean isErrorEnabled() {
        return errorEnabled == null ? logger.isErrorEnabled() : errorEnabled;
    }

    @Override
    public boolean isInfoEnabled() {
        return infoEnabled == null ? logger.isInfoEnabled() : infoEnabled;
    }

    @Override
    public boolean isTraceEnabled() {
        return traceEnabled == null ? logger.isTraceEnabled() : traceEnabled;
    }

    @Override
    public boolean isWarnEnabled() {
        return warnEnabled == null ? logger.isWarnEnabled() : warnEnabled;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.common.Logger#setLevel(org.polyglotter.common.Logger.Level)
     */
    @Override
    public void setLevel( final Level level ) {
        errorEnabled = level.ordinal() >= Level.ERROR.ordinal();
        warnEnabled = level.ordinal() >= Level.WARNING.ordinal();
        infoEnabled = level.ordinal() >= Level.INFO.ordinal();
        debugEnabled = level.ordinal() >= Level.DEBUG.ordinal();
        traceEnabled = level.ordinal() >= Level.TRACE.ordinal();
    }

    /**
     * Log a message at the TRACE level according to the specified format and (optional) parameters. The message should contain a
     * pair of empty curly braces for each of the parameter, which should be passed in the correct order. This method is efficient
     * and avoids superfluous object creation when the logger is disabled for the TRACE level.
     * 
     * @param message
     *        the message string
     * @param params
     *        the parameter values that are to replace the variables in the format string
     */
    @Override
    public void trace( final String message,
                       final Object... params ) {
        if ( !isTraceEnabled() ) return;
        if ( message == null ) return;
        logger.trace( String.format( message, params ) );
    }

    /**
     * Log an exception (throwable) at the TRACE level with an accompanying message. If the exception is null, then this method
     * calls {@link #trace(String, Object...)}.
     * 
     * @param t
     *        the exception (throwable) to log
     * @param message
     *        the message accompanying the exception
     * @param params
     *        the parameter values that are to replace the variables in the format string
     */
    @Override
    public void trace( final Throwable t,
                       final String message,
                       final Object... params ) {
        if ( !isTraceEnabled() ) return;
        if ( t == null ) {
            this.trace( message, params );
            return;
        }
        if ( message == null ) {
            logger.trace( null, t );
            return;
        }
        logger.trace( String.format( message, params ), t );
    }

    @Override
    public void warn( final I18n message,
                      final Object... params ) {
        if ( !isWarnEnabled() ) return;
        if ( message == null ) return;
        logger.warn( message.text( getLoggingLocale(), params ) );
    }

    @Override
    public void warn( final Throwable t,
                      final I18n message,
                      final Object... params ) {
        if ( !isWarnEnabled() ) return;
        if ( t == null ) {
            warn( message, params );
            return;
        }
        if ( message == null ) {
            logger.warn( null, t );
            return;
        }
        logger.warn( message.text( getLoggingLocale(), params ), t );
    }

}
