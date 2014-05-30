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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.grammar.GrammarFactory;
import org.polyglotter.grammar.Operation;
import org.polyglotter.grammar.Term;
import org.polyglotter.grammar.ValidationProblem;

/**
 * Calculates the mode, or most common term, of a collection of terms.
 */
public class Mode extends BaseOperation< Number[] > {

    /**
     * @param id
     *        the add operation's unique identifier (cannot be <code>null</code>)
     * @param transformId
     *        the owning transform identifier (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if any inputs are <code>null</code>
     */
    public Mode( final QName id,
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
        return "mode";
    }

    /**
     * {@inheritDoc}
     * <p>
     * When there is no mode an empty array will be returned.
     * 
     * @see org.polyglotter.operation.BaseOperation#calculate()
     */
    @Override
    protected Number[] calculate() {
        assert !problems().isError();

        final Map< Number, Integer > result = new HashMap<>();
        int max = 0;
        final List< Number > maxElems = new ArrayList<>();

        for ( final Term< ? > term : terms() ) {
            assert ( term.value() instanceof Number ); // validate check
            final Number value = ( Number ) term.value();

            if ( result.containsKey( value ) ) {
                result.put( value, ( result.get( value ) + 1 ) );
            } else {
                result.put( value, 1 );
            }

            if ( result.get( value ) > max ) {
                max = result.get( value );
                maxElems.clear();
                maxElems.add( value );
            } else if ( result.get( value ) == max ) {
                maxElems.add( value );
            }
        }

        if ( maxElems.size() == terms().size() ) return new Number[ 0 ];
        return maxElems.toArray( new Number[ maxElems.size() ] );
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
        return PolyglotterI18n.modeOperationDescription.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#maxTerms()
     */
    @Override
    public int maxTerms() {
        return Operation.UNLIMITED;
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
        return PolyglotterI18n.modeOperationName.text();
    }

    /**
     * Validates the operation's state.
     */
    @Override
    protected void validate() {
        // make sure there are terms
        if ( terms().isEmpty() ) {
            final ValidationProblem problem =
                GrammarFactory.createError( id(), PolyglotterI18n.modeOperationHasNoTerms.text( id() ) );
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
