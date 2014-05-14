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
import java.util.Collection;
import java.util.Map;

import javax.xml.namespace.QName;

import org.polyglotter.common.CheckArg;
import org.polyglotter.grammar.GrammarEvent.EventType;
import org.polyglotter.grammar.ValidationProblem.Severity;
import org.polyglotter.internal.NumberTerm;

/**
 * A factory for grammar-related objects.
 */
public final class GrammarFactory {

    /**
     * @param grammarPartId
     *        the source grammar part's identifier (cannot be <code>null</code>)
     * @param message
     *        the problem message (can be <code>null</code> or empty)
     * @return the problem (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the grammar part identifier is <code>null</code>
     */
    public static ValidationProblem createError( final QName grammarPartId,
                                                 final String message ) {
        CheckArg.notNull( grammarPartId, "grammarPartId" );
        return new Problem( Severity.ERROR, grammarPartId, message );
    }

    /**
     * @param type
     *        the event type (cannot be <code>null</code>)
     * @param sourceId
     *        the grammar part identifier that sourced this event (cannot be <code>null</code>)
     * @param data
     *        an optional collection of event data
     * @return the event (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the type or source identifier is <code>null</code>
     */
    public static GrammarEvent createEvent( final EventType type,
                                            final QName sourceId,
                                            final Map< String, Object > data ) {
        CheckArg.notNull( type, "type" );
        CheckArg.notNull( sourceId, "sourceId" );
        return new Event( type, sourceId, data );
    }

    /**
     * @param grammarPartId
     *        the source grammar part's identifier (cannot be <code>null</code>)
     * @param message
     *        the problem message (can be <code>null</code> or empty)
     * @return the problem (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the grammar part identifier is <code>null</code>
     */
    public static ValidationProblem createInfo( final QName grammarPartId,
                                                final String message ) {
        CheckArg.notNull( grammarPartId, "grammarPartId" );
        return new Problem( Severity.INFO, grammarPartId, message );
    }

    /**
     * @param id
     *        the term identifier (cannot be <code>null</code>)
     * @param operationId
     *        the operation identifier that owns this term (cannot be <code>null</code>)
     * @param number
     *        the initial value (can be <code>null</code>)
     * @return the new term (never <code>null</code>)
     */
    public static Term< Number > createNumberTerm( final QName id,
                                                   final QName operationId,
                                                   final Number number ) {
        return new NumberTerm( id, operationId, number );
    }

    /**
     * @param grammarPartId
     *        the source grammar part's identifier (cannot be <code>null</code>)
     * @param message
     *        the problem message (can be <code>null</code> or empty)
     * @return the problem (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the grammar part identifier is <code>null</code>
     */
    public static ValidationProblem createOk( final QName grammarPartId,
                                              final String message ) {
        CheckArg.notNull( grammarPartId, "grammarPartId" );
        return new Problem( Severity.OK, grammarPartId, message );
    }

    /**
     * @return an empty validation problems collection (never <code>null</code>)
     */
    public static ValidationProblems createValidationProblems() {
        return new Problems();
    }

    /**
     * @param grammarPartId
     *        the source grammar part's identifier (cannot be <code>null</code>)
     * @param message
     *        the problem message (can be <code>null</code> or empty)
     * @return the problem (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the grammar part identifier is <code>null</code>
     */
    public static ValidationProblem createWarning( final QName grammarPartId,
                                                   final String message ) {
        CheckArg.notNull( grammarPartId, "grammarPartId" );
        return new Problem( Severity.WARNING, grammarPartId, message );
    }

    /**
     * Don't allow construction outside this class.
     */
    private GrammarFactory() {
        // nothing to do
    }

    private static class Event implements GrammarEvent {

        private final Map< String, Object > data;
        private final QName sourceId;
        private final EventType type;

        Event( final EventType type,
               final QName sourceId,
               final Map< String, Object > data ) {
            this.type = type;
            this.sourceId = sourceId;
            this.data = data;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.GrammarEvent#data()
         */
        @Override
        public Map< String, ? > data() {
            return this.data;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.GrammarEvent#sourceId()
         */
        @Override
        public QName sourceId() {
            return this.sourceId;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return ( "event type = " + this.type + ", source = " + this.sourceId + ", data = " + this.data );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.GrammarEvent#type()
         */
        @Override
        public EventType type() {
            return this.type;
        }

    }

    private static final class Problem implements ValidationProblem {

        private final Severity severity;
        private final String message;
        private final QName sourceId;

        Problem( final Severity problemSeverity,
                 final QName problemPartId,
                 final String problemMessage ) {
            this.severity = problemSeverity;
            this.message = problemMessage;
            this.sourceId = problemPartId;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.ValidationProblem#isError()
         */
        @Override
        public boolean isError() {
            return ( this.severity == Severity.ERROR );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.ValidationProblem#isInfo()
         */
        @Override
        public boolean isInfo() {
            return ( this.severity == Severity.INFO );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.ValidationProblem#isOk()
         */
        @Override
        public boolean isOk() {
            return ( this.severity == Severity.OK );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.ValidationProblem#isWarning()
         */
        @Override
        public boolean isWarning() {
            return ( this.severity == Severity.WARNING );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.ValidationProblem#message()
         */
        @Override
        public String message() {
            return this.message;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.ValidationProblem#severity()
         */
        @Override
        public Severity severity() {
            return this.severity;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.ValidationProblem#sourceId()
         */
        @Override
        public QName sourceId() {
            return this.sourceId;
        }

    }

    private static final class Problems extends ArrayList< ValidationProblem > implements ValidationProblems {

        private Severity severity;

        Problems() {
            this.severity = Severity.OK;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#add(int, java.lang.Object)
         */
        @Override
        public void add( final int index,
                         final ValidationProblem problem ) {
            super.add( index, problem );
            updateSeverity( problem );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#add(java.lang.Object)
         */
        @Override
        public boolean add( final ValidationProblem problem ) {
            final boolean added = super.add( problem );

            if ( added ) {
                updateSeverity( problem );
            }

            return added;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#addAll(java.util.Collection)
         */
        @Override
        public boolean addAll( final Collection< ? extends ValidationProblem > c ) {
            if ( this.severity != Severity.ERROR ) {
                for ( final ValidationProblem problem : c ) {
                    updateSeverity( problem );

                    if ( this.severity == Severity.ERROR ) {
                        break;
                    }
                }
            }

            return super.addAll( c );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#addAll(int, java.util.Collection)
         */
        @Override
        public boolean addAll( final int index,
                               final Collection< ? extends ValidationProblem > c ) {
            if ( this.severity != Severity.ERROR ) {
                for ( final ValidationProblem problem : c ) {
                    updateSeverity( problem );

                    if ( this.severity == Severity.ERROR ) {
                        break;
                    }
                }
            }

            return super.addAll( index, c );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#clear()
         */
        @Override
        public void clear() {
            super.clear();
            this.severity = Severity.OK;
        }

        private void determineSeverity() {
            this.severity = Severity.OK;

            for ( final ValidationProblem problem : this ) {
                updateSeverity( problem );

                if ( this.severity == Severity.ERROR ) {
                    break;
                }
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.ValidationProblems#isError()
         */
        @Override
        public boolean isError() {
            return ( this.severity == Severity.ERROR );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.ValidationProblems#isInfo()
         */
        @Override
        public boolean isInfo() {
            return ( this.severity == Severity.INFO );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.ValidationProblems#isOk()
         */
        @Override
        public boolean isOk() {
            return ( this.severity == Severity.OK );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.ValidationProblems#isWarning()
         */
        @Override
        public boolean isWarning() {
            return ( this.severity == Severity.WARNING );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#remove(int)
         */
        @Override
        public ValidationProblem remove( final int index ) {
            final ValidationProblem problem = super.remove( index );

            if ( this.severity == problem.severity() ) {
                determineSeverity();
            }

            return problem;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#remove(java.lang.Object)
         */
        @Override
        public boolean remove( final Object o ) {
            final boolean removed = super.remove( o );

            if ( removed ) {
                final ValidationProblem problem = ( ValidationProblem ) o;

                if ( problem.severity() == this.severity ) {
                    determineSeverity();
                }
            }

            return removed;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#removeAll(java.util.Collection)
         */
        @Override
        public boolean removeAll( final Collection< ? > c ) {
            final boolean changed = super.removeAll( c );

            if ( changed ) {
                determineSeverity();
            }

            return changed;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#retainAll(java.util.Collection)
         */
        @Override
        public boolean retainAll( final Collection< ? > c ) {
            final boolean changed = super.retainAll( c );

            if ( changed ) {
                determineSeverity();
            }

            return changed;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#set(int, java.lang.Object)
         */
        @Override
        public ValidationProblem set( final int index,
                                      final ValidationProblem newProblem ) {
            final ValidationProblem oldProblem = super.set( index, newProblem );

            if ( newProblem.severity().isMoreSevereThan( this.severity ) ) {
                this.severity = newProblem.severity();
            } else if ( oldProblem.severity() == this.severity ) {
                determineSeverity();
            }

            return oldProblem;
        }

        private void updateSeverity( final ValidationProblem problem ) {
            if ( problem.severity().isMoreSevereThan( this.severity ) ) {
                this.severity = problem.severity();
            }
        }

    }

}
