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
package org.polyglotter.operation;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.CheckArg;
import org.polyglotter.common.Logger;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.transformation.TransformationEvent;
import org.polyglotter.transformation.TransformationFactory;
import org.polyglotter.transformation.TransformationListener;
import org.polyglotter.transformation.Value;
import org.polyglotter.transformation.ValueDescriptor;

/**
 * The base class for {@link Value value} transformation objects.
 * 
 * @param <T>
 *        the value type
 */
public class ValueImpl< T > implements Value< T > {

    private final ValueDescriptor< T > descriptor;

    /**
     * The registered listeners of this value.
     */
    protected final Set< TransformationListener > listeners;

    /**
     * The current value.
     */
    protected T value;

    /**
     * The logger.
     */
    protected final Logger logger;

    /**
     * @param valueDescriptor
     *        the value descriptor (cannot be <code>null</code>)
     */
    public ValueImpl( final ValueDescriptor< T > valueDescriptor ) {
        CheckArg.notNull( valueDescriptor, "valueDescriptor" );

        this.descriptor = valueDescriptor;
        this.listeners = new HashSet<>( 5 );
        this.logger = Logger.getLogger( getClass() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.TransformationSource#add(org.polyglotter.transformation.TransformationListener)
     */
    @Override
    public void add( final TransformationListener listener ) {
        CheckArg.notNull( listener, "listener" );

        if ( !this.listeners.contains( listener ) ) {
            this.listeners.add( listener );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Value#descriptor()
     */
    @Override
    public final ValueDescriptor< T > descriptor() {
        return this.descriptor;
    }

    /**
     * @param event
     *        the event being broadcast to the registered listeners (cannot be <code>null</code>)
     */
    protected void fire( final TransformationEvent event ) {
        CheckArg.notNull( event, "event" );

        for ( final TransformationListener listener : this.listeners ) {
            listener.notify( event );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Value#get()
     */
    @SuppressWarnings( "unused" )
    @Override
    public T get() throws PolyglotterException {
        return this.value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.TransformationSource#remove(org.polyglotter.transformation.TransformationListener)
     */
    @Override
    public void remove( final TransformationListener listener ) {
        CheckArg.notNull( listener, "listener" );

        if ( !this.listeners.remove( listener ) ) {
            throw new IllegalArgumentException( PolyglotterI18n.listenerNotFoundToUnregister.text() );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Value#set(java.lang.Object)
     */
    @SuppressWarnings( "unused" )
    @Override
    public void set( final T newValue ) throws PolyglotterException {
        if ( !this.descriptor.modifiable() ) {
            throw new UnsupportedOperationException( PolyglotterI18n.valueNotModifiable.text( this.descriptor.id() ) );
        }

        boolean changed = false;
        final T oldValue = this.value;

        if ( this.value == null ) {
            if ( newValue != null ) {
                this.value = newValue;
                changed = true;
            }
        } else if ( ( newValue == null ) || !this.value.equals( newValue ) ) {
            this.value = newValue;
            changed = true;
        }

        if ( changed ) {
            final Map< String, Object > data = new HashMap< String, Object >( 1 );
            data.put( Value.EventTag.OLD, oldValue );
            data.put( Value.EventTag.NEW, newValue );

            final TransformationEvent event = TransformationFactory.createEvent( Value.ValueEventType.VALUE_CHANGED, this, data );
            this.logger.debug( "Value '%s' value changed from '%s' to '%s'", descriptor().id(), oldValue, this.value );
            fire( event );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        @SuppressWarnings( "unchecked" ) final Class< T > clazz =
            ( Class< T > ) ( ( ParameterizedType ) getClass().getGenericSuperclass() ).getActualTypeArguments()[ 0 ];
        final StringBuilder builder = new StringBuilder( clazz.getName() );
        builder.append( " " ).append( descriptor().name() );

        return builder.toString();
    }

}
