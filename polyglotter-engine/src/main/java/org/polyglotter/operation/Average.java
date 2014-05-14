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

import java.util.List;

import javax.xml.namespace.QName;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.Logger;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.GrammarFactory;
import org.polyglotter.grammar.Term;
import org.polyglotter.grammar.ValidationProblem;

/**
 * Computes the average value of a collection of number terms.
 */
public class Average extends BaseOperation< Number > {

    /**
     * @param id
     *        the average operation's unique identifier (cannot be <code>null</code>)
     * @param transformId
     *        the owning transform identifier (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if any inputs are <code>null</code>
     */
    public Average( final QName id,
                    final QName transformId ) {
        super( id, transformId );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#calculate()
     */
    @Override
    protected Number calculate() {
        assert !problems().isError();

        final Add addOp = new Add( Term.TEMP_ID, id() );
        final List< Term< ? >> terms = terms();

        if ( terms.size() == 1 ) {
            return ( Number ) terms.get( 0 ).value();
        }

        try {
            addOp.add( terms().toArray( new Term< ? >[ terms().size() ] ) );
            final Number total = addOp.calculate();

            final Divide divideOp = new Divide( id(), transformId() );
            divideOp.add( GrammarFactory.createNumberTerm( Term.TEMP_ID, id(), total ) );
            divideOp.add( GrammarFactory.createNumberTerm( Term.TEMP2_ID, id(), terms.size() ) );

            return divideOp.calculate();
        } catch ( final PolyglotterException e ) {
            final ValidationProblem problem = GrammarFactory.createError( id(),
                                                                          PolyglotterI18n.averageOperationError.text( id() ) );
            problems().add( problem );
            Logger.getLogger( getClass() ).error( e, PolyglotterI18n.averageOperationError, id() );

            return null;
        }
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
        return PolyglotterI18n.averageOperationDescription.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#maxTerms()
     */
    @Override
    public int maxTerms() {
        return BaseOperation.UNLIMITED;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#minTerms()
     */
    @Override
    public int minTerms() {
        return 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#name()
     */
    @Override
    public String name() {
        return PolyglotterI18n.averageOperationName.text();
    }

    /**
     * Validates the operation's state.
     */
    @Override
    protected void validate() {
        // make sure there are terms
        if ( terms().isEmpty() ) {
            final ValidationProblem problem =
                GrammarFactory.createError( id(), PolyglotterI18n.averageOperationHasNoTerms.text( id() ) );
            problems().add( problem );
        } else {
            if ( terms().size() < minTerms() ) {
                final ValidationProblem problem =
                    GrammarFactory.createError( id(), PolyglotterI18n.invalidTermCount.text( id(), terms().size() ) );
                problems().add( problem );
            }

            // make sure all the terms have types of Number
            for ( final Term< ? > term : terms() ) {
                final Object value = term.value();

                if ( !( value instanceof Number ) ) {
                    final ValidationProblem problem =
                        GrammarFactory.createError( id(), PolyglotterI18n.invalidTermType.text( term.id(), id() ) );
                    problems().add( problem );
                }
            }
        }
    }

}
