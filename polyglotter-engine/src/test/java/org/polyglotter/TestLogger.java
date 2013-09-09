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
package org.polyglotter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.modeshape.common.i18n.I18nResource;
import org.modeshape.common.logging.Logger;

/**
 * The <code>TestLogger</code> class provides an <code>org.modeshape.common.logging.Logger</code> implementation that uses the
 * Eclipse logger.
 */
public final class TestLogger extends Logger {
    
    private static final List< String > INFO_MESSAGES = new ArrayList<>();
    private static final List< String > WARNING_MESSAGES = new ArrayList<>();
    private static final List< String > ERROR_MESSAGES = new ArrayList<>();
    
    /**
     * @return The set of error messages logged since the last time this method was called. All messages are cleared after this
     *         method is called.
     */
    public static List< String > errorMessages() {
        return messages( ERROR_MESSAGES );
    }
    
    /**
     * @return The set of information messages logged since the last time this method was called. All messages are cleared after
     *         this method is called.
     */
    public static List< String > infoMessages() {
        return messages( INFO_MESSAGES );
    }
    
    private static void log( final Throwable error,
                             final String message,
                             final List< String > messages ) {
        if ( messages != null ) messages.add( message );
        System.out.println( message );
        if ( error != null ) error.printStackTrace();
    }
    
    private static List< String > messages( final List< String > messages ) {
        final List< String > msgs = new ArrayList<>( messages );
        messages.clear();
        return msgs;
    }
    
    /**
     * @return The set of warning messages logged since the last time this method was called. All messages are cleared after this
     *         method is called.
     */
    public static List< String > warningMessages() {
        return messages( WARNING_MESSAGES );
    }
    
    private final String name;
    
    /**
     * @param name
     */
    public TestLogger( final String name ) {
        this.name = name;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#debug(java.lang.String, java.lang.Object[])
     */
    @Override
    public void debug( final String message,
                       final Object... params ) {
        debug( null, message, params );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#debug(java.lang.Throwable, java.lang.String, java.lang.Object[])
     */
    @Override
    public void debug( final Throwable error,
                       String message,
                       final Object... params ) {
        message = message.replace( '\'', '"' ); // jpav Temporary until MS fixed
        if ( isDebugEnabled() ) log( error, MessageFormat.format( message, params ), null );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#error(org.modeshape.common.i18n.I18nResource, java.lang.Object[])
     */
    @Override
    public void error( final I18nResource message,
                       final Object... params ) {
        error( null, message, params );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#error(java.lang.Throwable, org.modeshape.common.i18n.I18nResource,
     *      java.lang.Object[])
     */
    @Override
    public void error( final Throwable error,
                       final I18nResource message,
                       final Object... params ) {
        if ( isErrorEnabled() ) log( error, message.text( params ), ERROR_MESSAGES );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#info(org.modeshape.common.i18n.I18nResource, java.lang.Object[])
     */
    @Override
    public void info( final I18nResource message,
                      final Object... params ) {
        info( null, message, params );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#info(java.lang.Throwable, org.modeshape.common.i18n.I18nResource,
     *      java.lang.Object[])
     */
    @Override
    public void info( final Throwable error,
                      final I18nResource message,
                      final Object... params ) {
        if ( isInfoEnabled() ) log( error, message.text( params ), INFO_MESSAGES );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#isDebugEnabled()
     */
    @Override
    public boolean isDebugEnabled() {
        return false;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#isErrorEnabled()
     */
    @Override
    public boolean isErrorEnabled() {
        return true;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#isInfoEnabled()
     */
    @Override
    public boolean isInfoEnabled() {
        return true;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#isTraceEnabled()
     */
    @Override
    public boolean isTraceEnabled() {
        return false;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#isWarnEnabled()
     */
    @Override
    public boolean isWarnEnabled() {
        return true;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#trace(java.lang.String, java.lang.Object[])
     */
    @Override
    public void trace( final String message,
                       final Object... params ) {
        trace( null, message, params );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#trace(java.lang.Throwable, java.lang.String, java.lang.Object[])
     */
    @Override
    public void trace( final Throwable error,
                       final String message,
                       final Object... params ) {
        if ( isTraceEnabled() ) log( error, message, null );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#warn(org.modeshape.common.i18n.I18nResource, java.lang.Object[])
     */
    @Override
    public void warn( final I18nResource message,
                      final Object... params ) {
        warn( null, message, params );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.Logger#warn(java.lang.Throwable, org.modeshape.common.i18n.I18nResource,
     *      java.lang.Object[])
     */
    @Override
    public void warn( final Throwable error,
                      final I18nResource message,
                      final Object... params ) {
        if ( isWarnEnabled() ) log( error, message.text( params ), WARNING_MESSAGES );
    }
}