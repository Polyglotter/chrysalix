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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.namespace.QName;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.GrammarFactory;
import org.polyglotter.grammar.Term;
import org.polyglotter.grammar.ValidationProblem;

/**
 * Calculates the absolute value of a number.
 * 
 * @see Math#abs(double)
 * @see Math#abs(float)
 * @see Math#abs(int)
 * @see Math#abs(long)
 */
public final class AbsoluteValue extends AbstractOperation< Number > {

    /**
     * The operation descriptor.
     */
    public static final Descriptor DESCRIPTOR = new Descriptor() {

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Operation.Descriptor#abbreviation()
         */
        @Override
        public String abbreviation() {
            return "abs";
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Operation.Descriptor#category()
         */
        @Override
        public Category category() {
            return Category.ARITHMETIC;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Operation.Descriptor#description()
         */
        @Override
        public String description() {
            return PolyglotterI18n.absoluteValueOperationDescription.text();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Operation.Descriptor#name()
         */
        @Override
        public String name() {
            return PolyglotterI18n.absoluteValueOperationName.text();
        }

    };

    /**
     * @param id
     *        the absolute value operation's unique identifier (cannot be <code>null</code>)
     * @param transformId
     *        the owning transform identifier (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if any inputs are <code>null</code>
     */
    AbsoluteValue( final QName id,
                   final QName transformId ) {
        super( id, transformId, DESCRIPTOR );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.AbstractOperation#calculate()
     */
    @Override
    protected Number calculate() throws PolyglotterException {
        assert !problems().isError();
        final Number value = ( Number ) terms().get( 0 ).value();

        if ( value instanceof Double ) return Math.abs( ( Double ) value );
        if ( value instanceof Float ) return Math.abs( ( Float ) value );
        if ( value instanceof Integer ) return Math.abs( ( Integer ) value );
        if ( value instanceof Long ) return Math.abs( ( Long ) value );
        if ( value instanceof Short ) return Math.abs( ( Short ) value );
        if ( value instanceof BigDecimal ) return ( ( BigDecimal ) value ).abs();
        if ( value instanceof BigInteger ) return ( ( BigInteger ) value ).abs();
        if ( value instanceof AtomicInteger ) return ( new AtomicInteger( Math.abs( ( ( AtomicInteger ) value ).get() ) ) );
        if ( value instanceof AtomicLong ) return ( new AtomicLong( Math.abs( ( ( AtomicLong ) value ).get() ) ) );

        return Math.abs( value.doubleValue() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.AbstractOperation#maxTerms()
     */
    @Override
    public int maxTerms() {
        return 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.AbstractOperation#minTerms()
     */
    @Override
    public int minTerms() {
        return 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#validate()
     */
    @Override
    public void validate() {
        // make sure there is one term
        if ( terms().size() != 1 ) {
            final ValidationProblem problem =
                GrammarFactory.createError( id(), PolyglotterI18n.absoluteValueOperationMustHaveOneTerm.text( id() ) );
            problems().add( problem );
        } else {
            // must be a number
            final Term< ? > term = terms().get( 0 );
            Object value;

            try {
                value = term.value();

                if ( !( value instanceof Number ) ) {
                    final ValidationProblem problem =
                        GrammarFactory.createError( id(),
                                                    PolyglotterI18n.absoluteValueOperationInvalidTermType.text( id(),
                                                                                                                term.id() ) );
                    problems().add( problem );
                }
            } catch ( final PolyglotterException e ) {
                final ValidationProblem problem =
                    GrammarFactory.createError( id(), PolyglotterI18n.operationValidationError.text( term.id(), id() ) );
                problems().add( problem );
                this.logger.error( e, PolyglotterI18n.message, problem.message() );
            }
        }
    }

}
