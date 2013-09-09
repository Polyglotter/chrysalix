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
package org.modeshape.common.logging;

import java.util.HashMap;
import java.util.Map;

import org.polyglotter.TestLogger;

final class CustomLoggerFactory extends LogFactory {
    
    /**
     * Map of loggers keyed by logger name.
     */
    private final Map< String, Logger > loggerMap;
    
    /**
     * Constructs the factory.
     */
    CustomLoggerFactory() {
        this.loggerMap = new HashMap< String, Logger >();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.common.logging.LogFactory#getLogger(java.lang.String)
     */
    @Override
    public Logger getLogger( final String name ) {
        Logger logger = null;
        
        // protect against concurrent access of the loggerMap
        synchronized ( this ) {
            logger = this.loggerMap.get( name );
            
            if ( logger == null ) {
                logger = new TestLogger( name );
                this.loggerMap.put( name, logger );
            }
        }
        
        return logger;
    }
}