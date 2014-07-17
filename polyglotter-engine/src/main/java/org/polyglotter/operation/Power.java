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

import javax.xml.namespace.QName;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.GrammarFactory;
import org.polyglotter.grammar.Term;
import org.polyglotter.grammar.ValidationProblem;

/**
 * Calculates the value of the first term raised to the power of the second term.
 * 
 * @see Math#pow(double, double)
 */
public final class Power extends AbstractOperation< Number > {

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
            return "pow";
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
            return PolyglotterI18n.powerOperationDescription.text();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Operation.Descriptor#name()
         */
        @Override
        public String name() {
            return PolyglotterI18n.powerOperationName.text();
        }

    };

    /**
     * @param id
     *        the power operation's unique identifier (cannot be <code>null</code>)
     * @param transformId
     *        the owning transform identifier (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if any inputs are <code>null</code>
     */
    Power( final QName id,
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

        final Number base = ( Number ) terms().get( 0 ).value();
        final Number exponent = ( Number ) terms().get( 1 ).value();

        if ( base instanceof BigInteger ) return ( ( BigInteger ) base ).pow( exponent.intValue() );
        if ( base instanceof BigDecimal ) return ( ( BigDecimal ) base ).pow( exponent.intValue() );

        return Math.pow( base.doubleValue(), exponent.doubleValue() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.AbstractOperation#maxTerms()
     */
    @Override
    public int maxTerms() {
        return 2;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.AbstractOperation#minTerms()
     */
    @Override
    public int minTerms() {
        return 2;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#validate()
     */
    @Override
    public void validate() {
        // make sure there are terms
        if ( terms().size() != 2 ) {
            final ValidationProblem problem =
                GrammarFactory.createError( id(), PolyglotterI18n.powerOperationInvalidTermCount.text( id() ) );
            problems().add( problem );
        } else {
            { // make sure first term is a number
                final Term< ? > term = terms().get( 0 );
                Object x;

                try {
                    x = term.value();

                    if ( !( x instanceof Number ) ) {
                        final ValidationProblem problem =
                            GrammarFactory.createError( id(),
                                                        PolyglotterI18n.powerOperationInvalidBaseTermType.text( term.id(),
                                                                                                                id() ) );
                        problems().add( problem );
                    }
                } catch ( final PolyglotterException e ) {
                    final ValidationProblem problem =
                        GrammarFactory.createError( id(), PolyglotterI18n.operationValidationError.text( term.id(), id() ) );
                    problems().add( problem );
                    this.logger.error( e, PolyglotterI18n.message, problem.message() );
                }
            }

            { // make sure second term is an integer
                final Term< ? > term = terms().get( 1 );
                Object y;

                try {
                    y = term.value();

                    if ( !( y instanceof Number ) ) {
                        final ValidationProblem problem =
                            GrammarFactory.createError( id(),
                                                        PolyglotterI18n.powerOperationInvalidExponentTermType.text( term.id(),
                                                                                                                    id() ) );
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

}
