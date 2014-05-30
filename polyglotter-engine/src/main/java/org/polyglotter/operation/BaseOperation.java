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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.CheckArg;
import org.polyglotter.common.Logger;
import org.polyglotter.common.ObjectUtil;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.GrammarEvent;
import org.polyglotter.grammar.GrammarEvent.EventType;
import org.polyglotter.grammar.GrammarFactory;
import org.polyglotter.grammar.GrammarListener;
import org.polyglotter.grammar.Operation;
import org.polyglotter.grammar.Term;
import org.polyglotter.grammar.Term.TermEventType;
import org.polyglotter.grammar.Transform;
import org.polyglotter.grammar.ValidationProblem;
import org.polyglotter.grammar.ValidationProblems;

/**
 * @param <T>
 *        the operation result type
 */
public abstract class BaseOperation< T > implements GrammarListener, Operation< T > {

    private final QName id;
    private List< GrammarListener > listeners;
    private final Logger logger;
    private final ValidationProblems problems;
    private T result;
    private final List< Term< ? > > terms;
    private final QName transformId;

    /**
     * @param id
     *        the operation identifier (cannot be <code>null</code> or empty)
     * @param transformId
     *        the identifier of the {@link Transform transform} this operation belongs to (cannot be <code>null</code> or empty)
     */
    protected BaseOperation( final QName id,
                             final QName transformId ) {
        CheckArg.notNull( id, "id" );
        CheckArg.notNull( transformId, "transformId" );

        this.id = id;
        this.logger = Logger.getLogger( getClass() );
        this.problems = GrammarFactory.createValidationProblems();
        this.transformId = transformId;
        this.terms = new ArrayList<>();

        termChanged();
    }

    /**
     * @return the abbreviated name more commonly used in math (never <code>null</code>)
     */
    public abstract String abbreviation();

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarEventSource#add(org.polyglotter.grammar.GrammarListener)
     */
    @Override
    public void add( final GrammarListener listener ) {
        CheckArg.notNull( listener, "listener" );

        if ( this.listeners == null ) {
            this.listeners = new ArrayList<>();
        }

        if ( !this.listeners.contains( listener ) ) {
            this.listeners.add( listener );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#add(org.polyglotter.grammar.Term[])
     */
    @Override
    public void add( final Term< ? >... terms ) throws PolyglotterException {
        CheckArg.isNotEmpty( terms, "terms" );

        for ( final Term< ? > term : terms ) {
            if ( term == null ) {
                throw new IllegalArgumentException( PolyglotterI18n.nullTerm.text( this.id ) );
            }

            // check to see if already added
            if ( this.terms.contains( term ) ) {
                throw new PolyglotterException( PolyglotterI18n.termExists, term.id(), this.id );
            }

            this.terms.add( term );
            term.add( this ); // register to receive grammar events

            // fire event
            notifyObservers( OperationEventType.TERM_ADDED, OperationEventType.TERM_ADDED.toString(), term.id() );
        }

        termChanged();
    }

    /**
     * Calculates the result. Calculation should only be done when there are no {@link ValidationProblem error validation problems}.
     * 
     * @return the result (can be <code>null</code>)
     * @throws PolyglotterException
     *         if there are validation problem errors
     */
    protected abstract T calculate() throws PolyglotterException;

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#get(javax.xml.namespace.QName)
     */
    @Override
    public Term< ? > get( final QName termId ) throws PolyglotterException {
        CheckArg.notNull( termId, "termId" );

        if ( !this.terms.isEmpty() ) {
            for ( final Term< ? > term : this.terms ) {
                if ( termId.equals( term.id() ) ) {
                    return term;
                }
            }
        }

        throw new PolyglotterException( PolyglotterI18n.termNotFound, termId, this.id );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#id()
     */
    @Override
    public final QName id() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator< Term< ? >> iterator() {
        return Collections.unmodifiableList( this.terms ).iterator();
    }

    /**
     * @return the maximum number of terms allowed or {@link #UNLIMITED unlimited}
     */
    public abstract int maxTerms();

    /**
     * @return the minimum number of terms allowed (never smaller than zero)
     */
    public abstract int minTerms();

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarListener#notify(org.polyglotter.grammar.GrammarEvent)
     */
    @Override
    public void notify( final GrammarEvent event ) {
        CheckArg.notNull( event, "event" );

        // recalculate as a term value changed
        if ( event.type() == TermEventType.VALUE_CHANGED ) {
            termChanged();
        }
    }

    /**
     * @param type
     *        the event type being handled (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if type is <code>null</code>)
     */
    protected void notifyObservers( final EventType type ) {
        notifyObservers( type, null, null );
    }

    /**
     * @param type
     *        the event type being handled (cannot be <code>null</code>)
     * @param eventData
     *        the event data properties (can be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if type is <code>null</code>)
     */
    protected void notifyObservers( final EventType type,
                                    final Map< String, Object > eventData ) {
        CheckArg.notNull( type, "type" );

        if ( this.listeners != null ) {
            final GrammarEvent event = GrammarFactory.createEvent( type, this.id, eventData );
            List< GrammarListener > remove = null;

            for ( final GrammarListener listener : this.listeners ) {
                try {
                    listener.notify( event );
                } catch ( final Exception e ) {
                    // remove listener since it through an exception
                    if ( remove == null ) {
                        remove = new ArrayList<>();
                    }

                    this.logger.error( e, PolyglotterI18n.listenerError, listener.getClass(), event );
                    remove.add( listener );
                }
            }

            if ( remove != null ) {
                for ( final GrammarListener listener : remove ) {
                    remove( listener );
                }
            }
        }
    }

    /**
     * @param type
     *        the event type being handled (cannot be <code>null</code>)
     * @param key
     *        the identifier of a property of event data (can be <code>null</code> or empty)
     * @param value
     *        the value of the property of event data (can be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if type is <code>null</code>)
     */
    protected void notifyObservers( final EventType type,
                                    final String key,
                                    final Object value ) {
        Map< String, Object > map = null;

        if ( ( key != null ) && !key.isEmpty() ) {
            map = Collections.singletonMap( key, value );
        }

        notifyObservers( type, map );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#problems()
     */
    @Override
    public ValidationProblems problems() {
        return this.problems;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarEventSource#remove(org.polyglotter.grammar.GrammarListener)
     */
    @Override
    public void remove( final GrammarListener listener ) {
        CheckArg.notNull( listener, "listener" );

        if ( ( this.listeners == null ) || !this.listeners.remove( listener ) ) {
            throw new IllegalArgumentException( PolyglotterI18n.listenerNotFoundToUnregister.text() );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#remove(javax.xml.namespace.QName[])
     */
    @Override
    public void remove( final QName... termIds ) throws PolyglotterException {
        CheckArg.isNotEmpty( termIds, "termIds" );

        for ( final QName termId : termIds ) {
            if ( termId == null ) {
                throw new IllegalArgumentException( PolyglotterI18n.nullTermId.text() );
            }

            final Term< ? > term = get( termId );
            term.remove( this ); // unregister from receiving grammar events
            this.terms.remove( term );

            // fire event
            notifyObservers( OperationEventType.TERM_REMOVED, OperationEventType.TERM_REMOVED.toString(), term.id() );
        }

        termChanged();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#result()
     */
    @Override
    public T result() throws PolyglotterException {
        if ( !this.problems.isError() ) {
            return this.result;
        }

        throw new PolyglotterException( PolyglotterI18n.operationHasErrors, this.id );
    }

    /**
     * Called after a change to the terms.
     */
    protected void termChanged() {
        this.problems.clear();
        validate();

        if ( !this.problems.isError() ) {
            final T oldValue = this.result;
            T newValue = null;

            try {
                newValue = calculate();

                if ( !ObjectUtil.equals( oldValue, newValue ) ) {
                    this.result = newValue;
                    notifyObservers( OperationEventType.RESULT_CHANGED );
                }
            } catch ( final PolyglotterException e ) {
                final ValidationProblem problem =
                    GrammarFactory.createError( id(), PolyglotterI18n.errorOnTermChanged.text( newValue, id() ) );
                problems().add( problem );
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#terms()
     */
    @Override
    public List< Term< ? >> terms() {
        return Collections.unmodifiableList( this.terms );
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder( abbreviation() );
        builder.append( '(' );

        int i = 0;

        for ( final Term< ? > term : terms() ) {
            if ( i != 0 ) builder.append( ", " );
            builder.append( term );
            ++i;
        }

        builder.append( ')' );
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#transformId()
     */
    @Override
    public final QName transformId() {
        return this.transformId;
    }

    /**
     * Validates the state of the operation.
     */
    protected abstract void validate();

}
