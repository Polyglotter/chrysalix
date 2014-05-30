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
 * Provides a random number with a positive sign, greater than or equal to {@code 0.0} and less than {@code 1.0}. There is an
 * optional term for a seed value.
 */
public class Random extends BaseOperation< Double > {

    /**
     * @param id
     *        the random number operation's unique identifier (cannot be <code>null</code>)
     * @param transformId
     *        the owning transform identifier (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if any inputs are <code>null</code>
     */
    public Random( final QName id,
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
        return "rand";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#calculate()
     */
    @Override
    protected Double calculate() {
        assert !problems().isError();

        if ( terms().isEmpty() ) {
            return Math.random();
        }

        final Number value = ( Number ) terms().get( 0 ).value();
        return new java.util.Random( value.longValue() ).nextDouble();
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
        return PolyglotterI18n.randomOperationDescription.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#maxTerms()
     */
    @Override
    public int maxTerms() {
        return 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#minTerms()
     */
    @Override
    public int minTerms() {
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#name()
     */
    @Override
    public String name() {
        return PolyglotterI18n.randomOperationName.text();
    }

    /**
     * Validates the operation's state.
     */
    @Override
    protected void validate() {
        // make sure there is at most one term
        if ( terms().size() > 1 ) {
            final ValidationProblem problem =
                GrammarFactory.createError( id(), PolyglotterI18n.randomOperationInvalidNumberOfTerms.text( id() ) );
            problems().add( problem );
        } else if ( terms().size() == 1 ) {
            // make sure term is a number
            final Object value = terms().get( 0 ).value();

            if ( !( value instanceof Number ) ) {
                final ValidationProblem problem =
                    GrammarFactory.createError( id(),
                                                PolyglotterI18n.randomOperationInvalidTermType.text( id(),
                                                                                                     terms().get( 0 ).id() ) );
                problems().add( problem );
            }
        }
    }

}
