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
package org.polyglotter.grammar;

import java.util.ArrayList;
import java.util.List;

import org.polyglotter.transformation.TransformationEvent;
import org.polyglotter.transformation.TransformationListener;

@SuppressWarnings( "javadoc" )
public class TestGrammarListener implements TransformationListener {
    
    private int count;
    private final List< TransformationEvent > events = new ArrayList<>();
    
    public void clear() {
        this.count = 0;
        this.events.clear();
    }
    
    public int count() {
        return this.count;
    }
    
    public List< TransformationEvent > events() {
        return this.events;
    }
    
    public TransformationEvent lastEvent() {
        if ( this.events.isEmpty() ) {
            return null;
        }
        
        return this.events.get( this.events.size() - 1 );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.TransformationListener#notify(org.polyglotter.transformation.TransformationEvent)
     */
    @Override
    public void notify( final TransformationEvent event ) {
        ++this.count;
        this.events.add( event );
    }
    
}
