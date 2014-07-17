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

import javax.xml.namespace.QName;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.CheckArg;
import org.polyglotter.common.Logger;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.GrammarEvent;
import org.polyglotter.grammar.GrammarFactory;
import org.polyglotter.grammar.GrammarListener;
import org.polyglotter.grammar.GrammarPart;
import org.polyglotter.grammar.Term;

/**
 * The base class for terms.
 * 
 * @param <T>
 *        the term type
 */
public abstract class AbstractTerm< T > implements Term< T > {

    private String description;
    private final QName id;
    private boolean modifiable = true;
    private String name;
    // private final QName operationId;
    private final Set< GrammarListener > listeners;
    private T value;

    /**
     * The logger.
     */
    protected final Logger logger;

    /**
     * @param id
     *        the term identifier (cannot be <code>null</code>)
     */
    protected AbstractTerm( final QName id ) {
        CheckArg.notNull( id, "id" );

        this.id = id;
        this.listeners = new HashSet<>( 5 );
        this.logger = Logger.getLogger( getClass() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarEventSource#add(org.polyglotter.grammar.GrammarListener)
     */
    @Override
    public void add( final GrammarListener listener ) {
        CheckArg.notNull( listener, "listener" );
        this.listeners.add( listener );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#description()
     */
    @Override
    public String description() {
        return this.description;
    }

    /**
     * @param event
     *        the event being broadcast to the registered listeners (cannot be <code>null</code>)
     */
    protected void fire( final GrammarEvent event ) {
        for ( final GrammarListener listener : this.listeners ) {
            listener.notify( event );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#id()
     */
    @Override
    public QName id() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Term#modifiable()
     */
    @Override
    public boolean modifiable() {
        return this.modifiable;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#name()
     */
    @Override
    public String name() {
        if ( ( this.name == null ) || this.name.isEmpty() ) {
            return id().toString();
        }

        return this.name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarEventSource#remove(org.polyglotter.grammar.GrammarListener)
     */
    @Override
    public void remove( final GrammarListener listener ) {
        CheckArg.notNull( listener, "listener" );
        this.listeners.remove( listener );
    }

    /**
     * @param newDescription
     *        the new description (can be <code>null</code> or empty)
     */
    public void setDescription( final String newDescription ) {
        boolean changed = false;
        String oldDescription = null;

        if ( this.description == null ) {
            if ( newDescription != null ) {
                this.description = newDescription;
                changed = true;
            }
        } else if ( ( newDescription == null ) || !this.description.equals( newDescription ) ) {
            oldDescription = this.description;
            this.description = newDescription;
            changed = true;
        }

        if ( changed ) {
            final Map< String, Object > data = new HashMap< String, Object >( 1 );
            data.put( GrammarPart.EventTag.OLD_DESCRIPTION, oldDescription );
            data.put( GrammarPart.EventTag.NEW_DESCRIPTION, newDescription );

            final GrammarEvent event = GrammarFactory.createEvent( GrammarPartEventType.DESCRIPTION_CHANGED, id(), data );
            fire( event );
        }
    }

    /**
     * @param newModifiable
     *        <code>true</code> if term is modifiable
     * @see #setValue(Object)
     */
    public void setModifiable( final boolean newModifiable ) {
        this.modifiable = newModifiable;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Term#setValue(java.lang.Object)
     */
    @SuppressWarnings( "unused" )
    @Override
    public void setValue( final T newValue ) throws PolyglotterException, UnsupportedOperationException {
        if ( !modifiable() ) {
            throw new UnsupportedOperationException( PolyglotterI18n.termNotModifiable.text( id() ) );
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
            data.put( Term.EventTag.OLD_VALUE, oldValue );
            data.put( Term.EventTag.NEW_VALUE, newValue );

            final GrammarEvent event = GrammarFactory.createEvent( TermEventType.VALUE_CHANGED, id(), data );
            this.logger.debug( "Term '%s' value changed from '%s' to '%s'", id(), oldValue, this.value );
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
        builder.append( " " ).append( name() );

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Term#value()
     */
    @SuppressWarnings( "unused" )
    @Override
    public T value() throws PolyglotterException {
        return this.value;
    }

}
