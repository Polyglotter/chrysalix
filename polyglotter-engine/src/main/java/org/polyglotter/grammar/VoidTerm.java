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

import javax.xml.namespace.QName;

import org.polyglotter.PolyglotterI18n;

/**
 * A term for the {@link Void} type.
 */
public final class VoidTerm implements Term< Void > {

    private final QName operationId;

    /**
     * @param operationId
     *        the identifier of the owning {@link Operation operation}.
     */
    public VoidTerm( final QName operationId ) {
        this.operationId = operationId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarEventSource#add(org.polyglotter.grammar.GrammarListener)
     */
    @Override
    public void add( final GrammarListener listener ) {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( final Term< Void > that ) {
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#description()
     */
    @Override
    public String description() {
        return PolyglotterI18n.voidTermDescription.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#id()
     */
    @Override
    public QName id() {
        return new QName( GrammarPart.NAMESPACE_URI, "VoidTerm", GrammarPart.NAMESPACE_PREFIX );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarPart#name()
     */
    @Override
    public String name() {
        return PolyglotterI18n.voidTermName.text();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Term#operationId()
     */
    @Override
    public QName operationId() {
        return this.operationId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.GrammarEventSource#remove(org.polyglotter.grammar.GrammarListener)
     */
    @Override
    public void remove( final GrammarListener listener ) {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Term#setValue(java.lang.Object)
     * @throws UnsupportedOperationException
     *         if method is called
     */
    @Override
    public final void setValue( final Void newValue ) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.grammar.Term#value()
     */
    @Override
    public final Void value() {
        return null;
    }

}
