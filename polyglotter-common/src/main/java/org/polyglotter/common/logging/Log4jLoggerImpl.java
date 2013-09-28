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

final class Log4jLoggerImpl extends Logger {
    
    private final org.apache.log4j.Logger logger;
    
    public Log4jLoggerImpl( final String name ) {
        logger = org.apache.log4j.Logger.getLogger( name );
    }
    
    @Override
    public void debug( final String message,
                       final Object... params ) {
        if ( !isDebugEnabled() ) return;
        logger.debug( String.format( message, params ) );
    }
    
    @Override
    public void debug( final Throwable t,
                       final String message,
                       final Object... params ) {
        if ( empty( message ) ) {
            return;
        }
        if ( !isDebugEnabled() ) return;
        logger.debug( String.format( message, params ), t );
    }
    
    private boolean empty( final String str ) {
        return str == null || str.trim().isEmpty();
    }
    
    @Override
    public void error( final I18n message,
                       final Object... params ) {
        if ( message == null ) {
            return;
        }
        if ( !isErrorEnabled() ) return;
        logger.error( message.text( getLoggingLocale(), params ) );
    }
    
    @Override
    public void error( final Throwable t,
                       final I18n message,
                       final Object... params ) {
        if ( message == null ) {
            return;
        }
        if ( !isErrorEnabled() ) return;
        logger.error( message.text( getLoggingLocale(), params ), t );
        
    }
    
    @Override
    public String getName() {
        return logger.getName();
    }
    
    @Override
    public void info( final I18n message,
                      final Object... params ) {
        if ( message == null ) {
            return;
        }
        if ( !isInfoEnabled() ) return;
        logger.info( message.text( getLoggingLocale(), params ) );
    }
    
    @Override
    public void info( final Throwable t,
                      final I18n message,
                      final Object... params ) {
        if ( message == null ) {
            return;
        }
        if ( !isInfoEnabled() ) return;
        logger.info( message.text( getLoggingLocale(), params ), t );
    }
    
    @Override
    public boolean isDebugEnabled() {
        return logger.isEnabledFor( org.apache.log4j.Level.DEBUG );
    }
    
    @Override
    public boolean isErrorEnabled() {
        return logger.isEnabledFor( org.apache.log4j.Level.ERROR );
    }
    
    @Override
    public boolean isInfoEnabled() {
        return logger.isEnabledFor( org.apache.log4j.Level.INFO );
    }
    
    @Override
    public boolean isTraceEnabled() {
        return logger.isEnabledFor( org.apache.log4j.Level.TRACE );
    }
    
    @Override
    public boolean isWarnEnabled() {
        return logger.isEnabledFor( org.apache.log4j.Level.WARN );
    }
    
    @Override
    public void trace( final String message,
                       final Object... params ) {
        if ( empty( message ) ) {
            return;
        }
        if ( !isTraceEnabled() ) return;
        logger.trace( String.format( message, params ) );
    }
    
    @Override
    public void trace( final Throwable t,
                       final String message,
                       final Object... params ) {
        if ( empty( message ) ) {
            return;
        }
        if ( !isTraceEnabled() ) return;
        logger.trace( String.format( message, params ), t );
    }
    
    @Override
    public void warn( final I18n message,
                      final Object... params ) {
        if ( message == null ) {
            return;
        }
        if ( !isWarnEnabled() ) return;
        logger.warn( message.text( getLoggingLocale(), params ) );
    }
    
    @Override
    public void warn( final Throwable t,
                      final I18n message,
                      final Object... params ) {
        if ( message == null ) {
            return;
        }
        if ( !isWarnEnabled() ) return;
        logger.warn( message.text( getLoggingLocale(), params ), t );
    }
}
