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
import org.polyglotter.grammar.ValidationProblem;
import org.polyglotter.grammar.ValidationProblems;

/**
 * Adds sums a collection of terms.
 */
public class Add implements GrammarListener, Operation< Number > {
    
    private final QName id;
    private List< GrammarListener > listeners;
    private final Logger logger;
    private final ValidationProblems problems;
    private final Result result;
    private Number resultValue;
    private final List< Term< ? > > terms;
    private final QName transformId;
    
    /**
     * @param id
     *        the add operation unique identifier (cannot be <code>null</code>)
     * @param transformId
     *        the owning transform identifier (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if any inputs are <code>null</code>
     */
    public Add( final QName id,
                final QName transformId ) {
        CheckArg.notNull( id, "id" );
        CheckArg.notNull( transformId, "transformId" );
        
        this.id = id;
        this.logger = Logger.getLogger( getClass() );
        this.problems = GrammarFactory.createValidationProblems();
        this.result = new Result( this );
        this.transformId = transformId;
        this.terms = new ArrayList<>();
        
        calculate();
    }
    
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
        
        calculate();
    }
    
    /**
     * Validates and then, if no errors, calculates the result.
     */
    protected void calculate() {
        validate();
        
        if ( !this.problems.isError() ) {
            final Number oldValue = this.resultValue;
            this.resultValue = new Long( 0 );
            
            for ( final Term< ? > term : this.terms ) {
                assert ( term.value() instanceof Number ); // validate check
                Number value = ( Number ) term.value();
                
                if ( ( value instanceof Integer ) || ( value instanceof Short ) || ( value instanceof Byte ) ) {
                    value = value.longValue();
                } else if ( value instanceof Float ) {
                    value = ( ( Float ) value ).doubleValue();
                }
                
                if ( ( this.resultValue instanceof Double ) || ( value instanceof Double ) ) {
                    this.resultValue = ( this.resultValue.doubleValue() + value.doubleValue() );
                } else {
                    this.resultValue = ( this.resultValue.longValue() + value.longValue() );
                }
            }
            
            if ( !ObjectUtil.equals( oldValue, this.resultValue ) ) {
                notifyObservers( OperationEventType.RESULT_CHANGED );
            }
        }
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#description()
     */
    @Override
    public String description() {
        return PolyglotterI18n.addOperationDescription.text();
    }
    
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
     * @return the result of the operation (can be <code>null</code>);
     */
    private Number getOperationResultValue() {
        return this.resultValue;
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
     * Returns an iterator that does not allow modifications to the term collection.
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator< Term< ? >> iterator() {
        return Collections.unmodifiableList( this.terms ).iterator();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#name()
     */
    @Override
    public String name() {
        return PolyglotterI18n.addOperationName.text();
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
            calculate();
        }
    }
    
    private void notifyObservers( final EventType type ) {
        notifyObservers( type, null, null );
    }
    
    private void notifyObservers( final EventType type,
                                  final String key,
                                  final Object value ) {
        if ( this.listeners != null ) {
            Map< String, Object > map = null;
            
            if ( ( key != null ) && !key.isEmpty() ) {
                map = Collections.singletonMap( key, value );
            }
            
            final GrammarEvent event = GrammarFactory.createEvent( type, this.id, map );
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
        
        calculate();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#result()
     */
    @Override
    public Result result() throws PolyglotterException {
        if ( !this.problems.isError() ) {
            return this.result;
        }
        
        throw new PolyglotterException( PolyglotterI18n.operationHasErrors, this.id );
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
     * @see org.polyglotter.grammar.Operation#transformId()
     */
    @Override
    public QName transformId() {
        return this.transformId;
    }
    
    /**
     * Validates the operation's state.
     */
    protected void validate() {
        this.problems.clear();
        
        // make sure there are terms
        if ( this.terms.isEmpty() ) {
            final ValidationProblem problem =
                GrammarFactory.createError( this.id, PolyglotterI18n.addOperationHasNoTerms.text( this.id ) );
            this.problems.add( problem );
        } else {
            if ( this.terms.size() < 2 ) {
                // make sure more than 1 term
                final ValidationProblem problem =
                    GrammarFactory.createError( this.id, PolyglotterI18n.invalidTermCount.text( this.id, this.terms.size() ) );
                this.problems.add( problem );
            }
            
            // make sure all the terms have types of Number
            for ( final Term< ? > term : this.terms ) {
                final Object value = term.value();
                
                if ( !( value instanceof Number ) ) {
                    final ValidationProblem problem =
                        GrammarFactory.createError( this.id, PolyglotterI18n.invalidTermType.text( term.id(), this.id ) );
                    this.problems.add( problem );
                }
            }
        }
    }
    
    /**
     * The result of the add operation.
     */
    public class Result implements Term< Number > {
        
        private final Add operation;
        private final QName id;
        
        Result( final Add addOperation ) {
            this.operation = addOperation;
            
            final QName parentId = this.operation.id();
            this.id = new QName( parentId.getNamespaceURI(), "result", parentId.getPrefix() );
        }
        
        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.GrammarEventSource#add(org.polyglotter.grammar.GrammarListener)
         * @throws UnsupportedOperationException
         *         if method is called
         */
        @Override
        public void add( final GrammarListener listener ) {
            throw new UnsupportedOperationException();
        }
        
        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.GrammarPart#description()
         */
        @Override
        public String description() {
            return PolyglotterI18n.addOperationResultDescription.text();
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
            return PolyglotterI18n.addOperationResultName.text();
        }
        
        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Term#operationId()
         */
        @Override
        public QName operationId() {
            return this.operation.id();
        }
        
        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.GrammarEventSource#remove(org.polyglotter.grammar.GrammarListener)
         * @throws UnsupportedOperationException
         *         if method is called
         */
        @Override
        public void remove( final GrammarListener listener ) {
            throw new UnsupportedOperationException();
        }
        
        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Term#setValue(java.lang.Object)
         * @throws UnsupportedOperationException
         *         if method is called
         */
        @Override
        public void setValue( final Number newValue ) {
            throw new UnsupportedOperationException();
        }
        
        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Term#value()
         */
        @SuppressWarnings( "synthetic-access" )
        @Override
        public Number value() {
            return getOperationResultValue();
        }
        
    }
    
}
