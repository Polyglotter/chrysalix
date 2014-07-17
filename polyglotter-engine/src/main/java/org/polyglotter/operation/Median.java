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
import java.util.List;

import javax.xml.namespace.QName;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.Logger;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.GrammarFactory;
import org.polyglotter.grammar.Operation;
import org.polyglotter.grammar.Term;
import org.polyglotter.grammar.ValidationProblem;
import org.polyglotter.internal.NumberTerm;

/**
 * Computes the median value of a collection of number terms.
 */
public final class Median extends AbstractOperation< Number > {

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
            return "median";
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
            return PolyglotterI18n.medianOperationDescription.text();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.grammar.Operation.Descriptor#name()
         */
        @Override
        public String name() {
            return PolyglotterI18n.medianOperationName.text();
        }

    };

    /**
     * @param id
     *        the median operation's unique identifier (cannot be <code>null</code>)
     * @param transformId
     *        the owning transform identifier (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if any inputs are <code>null</code>
     */
    Median( final QName id,
            final QName transformId ) {
        super( id, transformId, DESCRIPTOR );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.AbstractOperation#calculate()
     */
    @SuppressWarnings( "unchecked" )
    @Override
    protected Number calculate() throws PolyglotterException {
        assert !problems().isError();

        int size = 0;
        List< Term< Number >> numberTerms = null;

        { // convert terms to number terms
            final List< Term< ? >> terms = terms();
            size = terms.size();
            numberTerms = new ArrayList<>( size );

            // OK to cast since this method should not be run if there is a non-number term
            for ( final Term< ? > term : terms ) {
                numberTerms.add( ( Term< Number > ) term );
            }

            // sort values
            Collections.sort( numberTerms, NumberTerm.ASCENDING_SORTER );
        }

        final boolean even = ( ( size & 1 ) == 0 );
        final int halfwayIndex = ( size / 2 );

        if ( even ) {
            final Term< ? > first = numberTerms.get( halfwayIndex - 1 );
            final Term< ? > second = numberTerms.get( halfwayIndex );
            final QName tempId1 = new QName( "temp1" );
            final QName tempId2 = new QName( "temp2" );
            final Add addOp = new Add( tempId1, id() );

            try {
                addOp.add( first, second );
                final Number sum = addOp.calculate();

                final Divide divideOp = new Divide( tempId1, id() );
                divideOp.add( GrammarFactory.createNumberTerm( tempId1, sum ),
                              GrammarFactory.createNumberTerm( tempId2, 2 ) );

                return divideOp.calculate();
            } catch ( final PolyglotterException e ) {
                final ValidationProblem problem = GrammarFactory.createError( id(),
                                                                              PolyglotterI18n.medianOperationError.text( id() ) );
                problems().add( problem );
                Logger.getLogger( getClass() ).error( e, PolyglotterI18n.medianOperationError, id() );
            }
        }

        return numberTerms.get( halfwayIndex ).value();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.AbstractOperation#maxTerms()
     */
    @Override
    public int maxTerms() {
        return Operation.UNLIMITED;
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
        if ( terms().isEmpty() ) {
            final ValidationProblem problem =
                GrammarFactory.createError( id(), PolyglotterI18n.medianOperationHasNoTerms.text( id() ) );
            problems().add( problem );
        } else {
            if ( terms().size() < minTerms() ) {
                final ValidationProblem problem =
                    GrammarFactory.createError( id(), PolyglotterI18n.invalidTermCount.text( id(), terms().size() ) );
                problems().add( problem );
            }

            // make sure all the terms have types of Number
            for ( final Term< ? > term : terms() ) {
                Object value;

                try {
                    value = term.value();

                    if ( !( value instanceof Number ) ) {
                        final ValidationProblem problem =
                            GrammarFactory.createError( id(), PolyglotterI18n.invalidTermType.text( term.id(), id() ) );
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
