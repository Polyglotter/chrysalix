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
import org.polyglotter.grammar.Term;
import org.polyglotter.grammar.ValidationProblem;

/**
 * A string concatenation operation.
 */
public class Concat extends BaseOperation< String > {
    
    /**
     * @param id
     *        the add operation unique identifier (cannot be <code>null</code>)
     * @param transformId
     *        the transform identifier containing this operation (cannot be <code>null</code>)
     */
    public Concat( final QName id,
                   final QName transformId ) {
        super( id, transformId );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#calculate()
     */
    @Override
    protected String calculate() {
        assert !problems().isError();
        
        final StringBuilder result = new StringBuilder();
        
        for ( final Term< ? > term : terms() ) {
            final Object value = term.value();
            result.append( ( value == null ) ? "null" : value.toString() );
        }
        
        return result.toString();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#description()
     */
    @Override
    public String description() {
        return PolyglotterI18n.concatOperationDescription.text();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#name()
     */
    @Override
    public String name() {
        return PolyglotterI18n.concatOperationName.text();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.BaseOperation#validate()
     */
    @Override
    protected void validate() {
        if ( terms().isEmpty() ) {
            final ValidationProblem problem =
                GrammarFactory.createError( id(), PolyglotterI18n.addOperationHasNoTerms.text( id() ) );
            problems().add( problem );
        } else {
            if ( terms().size() < 2 ) {
                // make sure more than 1 term
                final ValidationProblem problem =
                    GrammarFactory.createError( id(), PolyglotterI18n.invalidTermCount.text( id(), terms().size() ) );
                problems().add( problem );
            }
        }
    }
    
}
