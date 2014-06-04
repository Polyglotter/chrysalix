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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.polyglotter.common.CheckArg;
import org.polyglotter.grammar.GrammarEvent;
import org.polyglotter.grammar.GrammarFactory;
import org.polyglotter.grammar.GrammarListener;
import org.polyglotter.grammar.GrammarPart;
import org.polyglotter.grammar.Term;

/**
 * A number term.
 * 
 */
public class NumberTerm implements Term< Number > {

    private String description;
    private final QName id;
    private final QName operationId;
    private final Set< GrammarListener > listeners;
    private Number value;

    /**
     * @param id
     *        the term identifier (cannot be <code>null</code>)
     * @param operationId
     *        the operation identifier that owns this term (cannot be <code>null</code>)
     */
    public NumberTerm( final QName id,
                       final QName operationId ) {
        CheckArg.notNull( id, "id" );
        CheckArg.notNull( operationId, "operationId" );

        this.id = id;
        this.operationId = operationId;
        this.listeners = new HashSet<>( 5 );
    }

    /**
     * @param id
     *        the term identifier (cannot be <code>null</code>)
     * @param operationId
     *        the operation identifier that owns this term (cannot be <code>null</code>)
     * @param initialValue
     *        the initial value (can be <code>null</code>)
     */
    public NumberTerm( final QName id,
                       final QName operationId,
                       final Number initialValue ) {
        this( id, operationId );
        this.value = initialValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarEventSource#add(org.polyglotter.grammar.GrammarListener)
     */
    @Override
    public void add( final GrammarListener listener ) {
        this.listeners.add( listener );
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public final int compareTo( final Term< Number > that ) {
        if ( this.value == null ) {
            if ( that.value() == null ) return 0;
            return -1;
        }

        if ( that.value() == null ) return 1;

        final Double thisAsDouble = this.value.doubleValue();
        final Double thatAsDouble = that.value().doubleValue();

        return Double.compare( thisAsDouble, thatAsDouble );
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

    private void fire( final GrammarEvent event ) {
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
     * @see org.polyglotter.grammar.GrammarPart#name()
     */
    @Override
    public String name() {
        return id().toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Term#operationId()
     */
    @Override
    public QName operationId() {
        return this.operationId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarEventSource#remove(org.polyglotter.grammar.GrammarListener)
     */
    @Override
    public void remove( final GrammarListener listener ) {
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
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Term#setValue(java.lang.Object)
     */
    @Override
    public void setValue( final Number newValue ) {
        boolean changed = false;
        Number oldValue = null;

        if ( this.value == null ) {
            if ( newValue != null ) {
                this.value = newValue;
                changed = true;
            }
        } else if ( ( newValue == null ) || !this.value.equals( newValue ) ) {
            oldValue = this.value;
            this.value = newValue;
            changed = true;
        }

        if ( changed ) {
            final Map< String, Object > data = new HashMap< String, Object >( 1 );
            data.put( Term.EventTag.OLD_VALUE, oldValue );
            data.put( Term.EventTag.NEW_VALUE, newValue );

            final GrammarEvent event = GrammarFactory.createEvent( TermEventType.VALUE_CHANGED, id(), data );
            fire( event );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Term#value()
     */
    @Override
    public Number value() {
        return this.value;
    }

}
