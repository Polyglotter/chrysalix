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
package org.polyglotter.internal;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.polyglotter.Operation;
import org.polyglotter.Transform;
import org.polyglotter.common.CheckArg;
import org.polyglotter.common.PolyglotterException;

/**
 * 
 */
public class TransformImpl implements Transform {
    
    private final LinkedList< Operation > operations = new LinkedList<>();
    
    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.Transform#add(org.polyglotter.Operation)
     */
    @Override
    public void add( final Operation operation ) {
        CheckArg.notNull( operation, "operation" );
        operations.add( operation );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.Transform#execute()
     */
    @Override
    public void execute() throws PolyglotterException {
        for ( final Operation op : operations )
            op.execute();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.Transform#operations()
     */
    @Override
    public List< Operation > operations() {
        return Collections.unmodifiableList( operations );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.Transform#remove(org.polyglotter.Operation)
     */
    @Override
    public void remove( final Operation operation ) {
        CheckArg.notNull( operation, "operation" );
        operations.remove( operation );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder( "transform( " );
        for ( final Iterator< Operation > iter = operations.iterator(); iter.hasNext(); ) {
            final Operation op = iter.next();
            builder.append( op );
            if ( iter.hasNext() ) builder.append( ", " );
        }
        builder.append( " )" );
        return builder.toString();
    }
}
