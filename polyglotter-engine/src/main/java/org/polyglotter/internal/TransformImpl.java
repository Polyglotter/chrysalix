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

import javax.xml.namespace.QName;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.CheckArg;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.transformation.Operation;
import org.polyglotter.transformation.Transformation;

/**
 * 
 */
public class TransformImpl implements Transformation {

    private final QName id;
    private final LinkedList< Operation< ? > > operations = new LinkedList<>();

    /**
     * @param transformId
     *        the transformation's unique identifier (cannot be <code>null</code>)
     */
    public TransformImpl( final QName transformId ) {
        CheckArg.notNull( transformId, "transformId" );
        this.id = transformId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Transformation#add(org.polyglotter.transformation.Operation[])
     */
    @Override
    public void add( final Operation< ? >... operations ) throws PolyglotterException {
        CheckArg.isNotEmpty( operations, "operations" );

        for ( final Operation< ? > operation : operations ) {
            if ( operation == null ) {
                throw new PolyglotterException( PolyglotterI18n.nullOperationBeingAddedToTransformation, this.id );
            }

            if ( !this.operations.add( operation ) ) {
                throw new PolyglotterException( PolyglotterI18n.operationWasNotAddedToTransformation,
                                                operation.descriptor().name(),
                                                this.id );
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Transformation#execute()
     */
    @Override
    public void execute() throws PolyglotterException {
        for ( final Operation op : operations )
            op.execute();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Transformation#id()
     */
    @Override
    public QName id() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator< Operation< ? >> iterator() {
        return new Iterator< Operation< ? >>() {

            private final List< Operation< ? >> copy = operations();
            private final int count = this.copy.size();
            private int currentIndex = 0;

            /**
             * @see java.util.Iterator#hasNext()
             */
            @Override
            public boolean hasNext() {
                return ( this.currentIndex < this.count );
            }

            /**
             * @see java.util.Iterator#next()
             */
            @Override
            public Operation< ? > next() {
                return this.copy.get( this.currentIndex++ );
            }

            /**
             * @see java.util.Iterator#remove()
             */
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Transformation#operations()
     */
    @Override
    public List< Operation< ? >> operations() {
        return Collections.unmodifiableList( this.operations );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Transformation#remove(org.polyglotter.transformation.Operation[])
     */
    @Override
    public void remove( final Operation< ? >... operations ) throws PolyglotterException {
        CheckArg.isNotEmpty( operations, "operations" );

        for ( final Operation< ? > operation : operations ) {
            if ( operation == null ) {
                throw new PolyglotterException( PolyglotterI18n.nullOperationBeingRemovedFromTransformation, this.id );
            }

            if ( !this.operations.remove( operation ) ) {
                throw new PolyglotterException( PolyglotterI18n.operationCouldNotBeRemovedFromTransformation,
                                                operation.descriptor().name(),
                                                this.id );
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder( "transform( " );

        for ( final Iterator< Operation< ? > > iter = this.operations.iterator(); iter.hasNext(); ) {
            final Operation< ? > op = iter.next();
            builder.append( op );

            if ( iter.hasNext() ) builder.append( ", " );
        }

        builder.append( " )" );
        return builder.toString();
    }

}
