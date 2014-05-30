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

import javax.xml.namespace.QName;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.grammar.GrammarFactory;
import org.polyglotter.grammar.ValidationProblem;

/**
 * Calculates the value of the first term raised to the power of the second term.
 * 
 * @see Math#pow(double, double)
 */
public class Power extends BaseOperation< Double > {

    /**
     * @param id
     *        the power operation's unique identifier (cannot be <code>null</code>)
     * @param transformId
     *        the owning transform identifier (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if any inputs are <code>null</code>
     */
    public Power( final QName id,
                  final QName transformId ) {
        super( id, transformId );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#abbreviation()
     */
    @Override
    public String abbreviation() {
        return "pow";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#calculate()
     */
    @Override
    protected Double calculate() {
        assert !problems().isError();

        final Number base = ( Number ) terms().get( 0 ).value();
        final Number exponent = ( Number ) terms().get( 1 ).value();

        return Math.pow( base.doubleValue(), exponent.doubleValue() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Operation#category()
     */
    @Override
    public Category category() {
        return Category.ARITHMETIC;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#description()
     */
    @Override
    public String description() {
        return PolyglotterI18n.powerOperationDescription.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#maxTerms()
     */
    @Override
    public int maxTerms() {
        return 2;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#minTerms()
     */
    @Override
    public int minTerms() {
        return 2;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#name()
     */
    @Override
    public String name() {
        return PolyglotterI18n.powerOperationName.text();
    }

    /**
     * Validates the operation's state.
     */
    @Override
    protected void validate() {
        // make sure there are terms
        if ( terms().size() != 2 ) {
            final ValidationProblem problem =
                GrammarFactory.createError( id(), PolyglotterI18n.powerOperationInvalidTermCount.text( id() ) );
            problems().add( problem );
        } else {
            { // make sure first term is a number
                final Object x = terms().get( 0 ).value();

                if ( !( x instanceof Number ) ) {
                    final ValidationProblem problem =
                        GrammarFactory.createError( id(),
                                                    PolyglotterI18n.powerOperationInvalidBaseTermType.text( terms().get( 0 ).id(),
                                                                                                            id() ) );
                    problems().add( problem );
                }
            }

            { // make sure second term is a number
                final Object y = terms().get( 1 ).value();

                if ( !( y instanceof Number ) ) {
                    final ValidationProblem problem =
                        GrammarFactory.createError( id(),
                                                    PolyglotterI18n.powerOperationInvalidExponentTermType.text( terms().get( 1 ).id(),
                                                                                                                id() ) );
                    problems().add( problem );
                }
            }
        }
    }

}
