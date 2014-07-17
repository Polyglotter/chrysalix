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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.polyglotter.grammar.ValidationProblem;
import org.polyglotter.grammar.ValidationProblems;

/**
 * The base class for {@link Operation operations}.
 * 
 * @param <T>
 *        the operation's result type
 */
public abstract class AbstractOperation< T > implements GrammarListener, Operation< T > {

    private final Descriptor descriptor;
    private final QName id;
    private final Set< GrammarListener > listeners;
    private final ValidationProblems problems;
    private T result;
    private final List< Term< ? > > terms;
    private final QName transformId;

    /**
     * The logger.
     */
    protected final Logger logger;

    /**
     * @param id
     *        the term identifier (cannot be <code>null</code>)
     * @param transformId
     *        the operation identifier that owns this term (cannot be <code>null</code>)
     * @param descriptor
     *        the operation {@link org.polyglotter.grammar.Operation.Descriptor descriptor} (cannot be <code>null</code>)
     */
    protected AbstractOperation( final QName id,
                                 final QName transformId,
                                 final Descriptor descriptor ) {
        CheckArg.notNull( id, "id" );
        CheckArg.notNull( transformId, "transformId" );
        CheckArg.notNull( descriptor, "descriptor" );

        this.descriptor = descriptor;
        this.id = id;
        this.listeners = new HashSet<>( 5 );
        this.logger = Logger.getLogger( getClass() );
        this.problems = GrammarFactory.createValidationProblems();
        this.transformId = transformId;
        this.terms = new ArrayList<>( 5 );

        termChanged();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarEventSource#add(org.polyglotter.grammar.GrammarListener)
     */
    @Override
    public void add( final GrammarListener listener ) {
        assert ( this.listeners != null );
        CheckArg.notNull( listener, "listener" );
        this.listeners.add( listener );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#add(org.polyglotter.grammar.Term[])
     */
    @Override
    public void add( final Term< ? >... termsBeingAdded ) throws PolyglotterException {
        CheckArg.isNotEmpty( termsBeingAdded, "termsBeingAdded" );
        assert ( this.terms != null );

        for ( final Term< ? > term : termsBeingAdded ) {
            if ( term == null ) {
                throw new IllegalArgumentException( PolyglotterI18n.nullTerm.text( this.id ) );
            }

            // check to see if already added
            for ( final Term< ? > existingTerm : this.terms ) {
                if ( existingTerm.id().equals( term.id() ) ) {
                    throw new PolyglotterException( PolyglotterI18n.termExists, term.id(), this.id );
                }
            }

            if ( this.terms.add( term ) ) {
                term.add( this ); // register to receive grammar events
            } else {
                throw new PolyglotterException( PolyglotterI18n.operationTermNotAdded, term.id(), id() );
            }

            // fire event
            notifyObservers( OperationEventType.TERM_ADDED, OperationEventType.TERM_ADDED.toString(), term.id() );
        }

        termChanged();
    }

    /**
     * Calculates the result. Calculation should only be done when there are no {@link ValidationProblem validation problems}.
     * 
     * @return the result (can be <code>null</code>)
     * @throws PolyglotterException
     *         if there are validation problems
     * @see #problems()
     */
    protected abstract T calculate() throws PolyglotterException;

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#description()
     */
    @Override
    public final String description() {
        assert ( this.descriptor != null );
        return this.descriptor.description();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#descriptor()
     */
    @Override
    public final Descriptor descriptor() {
        assert ( this.descriptor != null );
        return this.descriptor;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#get(javax.xml.namespace.QName)
     */
    @Override
    public Term< ? > get( final QName termId ) throws PolyglotterException {
        CheckArg.notNull( termId, "termId" );
        assert ( this.terms != null );

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
    public QName id() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator< Term< ? >> iterator() {
        assert ( this.terms != null );
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
     * @see org.polyglotter.grammar.Term#modifiable()
     */
    @Override
    public boolean modifiable() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#name()
     */
    @Override
    public final String name() {
        assert ( this.descriptor != null );
        return this.descriptor.name();
    }

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
        assert ( this.listeners != null );

        if ( !this.listeners.isEmpty() ) {
            final GrammarEvent event = GrammarFactory.createEvent( type, this.id, eventData );
            List< GrammarListener > remove = null;

            for ( final GrammarListener listener : this.listeners ) {
                try {
                    listener.notify( event );
                } catch ( final Exception e ) {
                    // remove listener since it threw an exception
                    if ( remove == null ) {
                        remove = new ArrayList<>();
                    }

                    this.logger.error( e, PolyglotterI18n.listenerError, listener.getClass(), event );
                    remove.add( listener );
                }
            }

            if ( remove != null ) {
                for ( final GrammarListener listenerToRemove : remove ) {
                    remove( listenerToRemove );
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
        assert ( this.problems != null );
        return this.problems;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarEventSource#remove(org.polyglotter.grammar.GrammarListener)
     */
    @Override
    public void remove( final GrammarListener listener ) {
        assert ( this.listeners != null );
        CheckArg.notNull( listener, "listener" );

        if ( !this.listeners.remove( listener ) ) {
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
        assert ( this.terms != null );

        for ( final QName termId : termIds ) {
            if ( termId == null ) {
                throw new IllegalArgumentException( PolyglotterI18n.nullTermId.text() );
            }

            final Term< ? > term = get( termId ); // throws exception if term not found
            term.remove( this ); // unregister from receiving grammar events

            if ( !this.terms.remove( term ) ) {
                throw new PolyglotterException( PolyglotterI18n.operationTermNotRemoved, term.id(), id() );
            }

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
        assert ( this.problems != null );

        if ( !this.problems.isError() ) {
            return this.result;
        }

        throw new PolyglotterException( PolyglotterI18n.operationHasErrors, this.id );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Term#setValue(java.lang.Object)
     * @throws UnsupportedOperationException
     *         if method is called
     */
    @Override
    public void setValue( final T newValue ) throws UnsupportedOperationException {
        throw new UnsupportedOperationException( PolyglotterI18n.operationResultNotModifiable.text( id() ) );
    }

    /**
     * Called after a change to the terms.
     */
    protected void termChanged() {
        assert ( this.problems != null );

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
        assert ( this.terms != null );
        return Collections.unmodifiableList( this.terms );
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        assert ( this.descriptor != null );
        assert ( this.terms != null );

        final StringBuilder builder = new StringBuilder( descriptor().abbreviation() );
        builder.append( '(' );

        boolean firstTime = true;

        for ( final Term< ? > term : terms() ) {
            if ( firstTime ) {
                firstTime = false;
            } else {
                builder.append( ", " );
            }

            builder.append( term );
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
    public QName transformId() {
        return this.transformId;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws PolyglotterException
     *         if there is an issue
     * 
     * @see org.polyglotter.grammar.Term#value()
     */
    @Override
    public T value() throws PolyglotterException {
        return result();
    }

    /**
     * The operation's descriptor implementation.
     */
    protected class DescriptorImpl implements Descriptor {

        private final String abbreviation;
        private final Category category;
        private final String description;
        private final String name;

        /**
         * @param abbreviation
         *        the operation's abbreviation (cannot be <code>null</code> or empty)
         * @param category
         *        the operations's category (cannot be <code>null</code>)
         * @param description
         *        the operation's description (cannot be <code>null</code> or empty)
         * @param name
         *        the operation's name (can be <code>null</code> or empty)
         */
        protected DescriptorImpl( final String abbreviation,
                                  final Category category,
                                  final String description,
                                  final String name ) {
            CheckArg.notEmpty( abbreviation, "abbreviation" );
            CheckArg.notNull( category, "category" );
            CheckArg.notEmpty( description, "description" );

            this.abbreviation = abbreviation;
            this.category = category;
            this.description = description;
            this.name = name;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Operation.Descriptor#abbreviation()
         */
        @Override
        public String abbreviation() {
            return this.abbreviation;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Operation.Descriptor#category()
         */
        @Override
        public Category category() {
            return this.category;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Operation.Descriptor#description()
         */
        @Override
        public String description() {
            return this.description;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Operation.Descriptor#name()
         */
        @Override
        public String name() {
            if ( ( this.name == null ) || this.name.isEmpty() ) {
                return id().toString();
            }

            return this.name;
        }

    }

}
