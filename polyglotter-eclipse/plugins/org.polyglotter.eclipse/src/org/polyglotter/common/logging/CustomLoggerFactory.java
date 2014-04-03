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

import java.util.HashMap;
import java.util.Map;

import org.polyglotter.common.LogFactory;
import org.polyglotter.common.Logger;
import org.polyglotter.eclipse.EclipseLogger;

/**
 * 
 */
public final class CustomLoggerFactory extends LogFactory {

    private final Map< String, Logger > loggerMap;

    /**
     * 
     */
    public CustomLoggerFactory() {
        loggerMap = new HashMap< String, Logger >();
    }

    /**
     * {@inheritDoc}
     * 
     * @see LogFactory#getLogger(String)
     */
    @Override
    public Logger getLogger( final String name ) {
        Logger logger;
        synchronized ( this ) {
            logger = loggerMap.get( name );
            if ( logger == null ) {
                logger = new EclipseLogger( name );
                loggerMap.put( name, logger );
            }
        }
        return logger;
    }
}