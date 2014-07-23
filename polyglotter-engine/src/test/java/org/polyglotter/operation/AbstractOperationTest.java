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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.polyglotter.TestConstants;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.GrammarEvent.EventType;
import org.polyglotter.grammar.GrammarListener;
import org.polyglotter.grammar.Operation;
import org.polyglotter.grammar.Operation.OperationEventType;
import org.polyglotter.grammar.Term;
import org.polyglotter.grammar.TestGrammarListener;
import org.polyglotter.internal.NumberTerm;

@SuppressWarnings( "javadoc" )
public final class AbstractOperationTest {

    private TestGrammarListener listener;
    private Operation< Long > operation;

    @Before
    public void beforeEach() {
        this.operation = new TestOperation();
        this.listener = new TestGrammarListener();
        this.operation.add( this.listener );
    }

    @Test
    public void shouldAddMultipleTerms() throws PolyglotterException {
        this.operation.add( TestConstants.STRING_1_TERM );
        this.operation.add( TestConstants.STRING_2_TERM );
        this.operation.add( TestConstants.STRING_3_TERM );
        assertThat( this.operation.terms(),
                    hasItems( new Term< ? >[] { TestConstants.STRING_1_TERM, TestConstants.STRING_2_TERM, TestConstants.STRING_3_TERM } ) );
        assertThat( this.operation.terms().size(), is( 3 ) );
    }

    @Test
    public void shouldAddMultipleTerms2() throws PolyglotterException {
        this.operation.add( TestConstants.STRING_1_TERM, TestConstants.STRING_2_TERM, TestConstants.STRING_3_TERM );
        assertThat( this.operation.terms(),
                    hasItems( new Term< ? >[] { TestConstants.STRING_1_TERM, TestConstants.STRING_2_TERM, TestConstants.STRING_3_TERM } ) );
        assertThat( this.operation.terms().size(), is( 3 ) );
    }

    @Test
    public void shouldBeAbleToUseOperationAsTerm() throws PolyglotterException {
        final Add addOp = new Add( TestConstants.ID, TestConstants.TRANSFORM_ID );
        addOp.add( this.operation );
        addOp.add( new NumberTerm( TestConstants.ID_2, 2 ) );
        assertThat( addOp.result(), is( ( Number ) 3L ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldErrorWhenRemovingUnregisteredListener() {
        this.operation.remove( new TestGrammarListener() );
    }

    @Test
    public void shouldGetNotifiedWhenTermIsAdded() throws PolyglotterException {
        this.operation.add( TestConstants.STRING_1_TERM );
        assertThat( this.listener.count(), is( 1 ) );
    }

    @Test
    public void shouldGetNotifiedWhenTermIsRemoved() throws PolyglotterException {
        this.operation.add( TestConstants.STRING_1_TERM );
        this.listener.clear();
        this.operation.remove( TestConstants.STRING_1_ID );
        assertThat( this.listener.count(), is( 1 ) );
        assertThat( this.listener.lastEvent().type(), is( ( EventType ) OperationEventType.TERM_REMOVED ) );
    }

    @Test
    public void shouldHaveEmpyIteratorAfterConstruction() {
        assertThat( this.operation.iterator().hasNext(), is( false ) );
    }

    @Test
    public void shouldHaveIteratorSizeEqualToTermSize() throws PolyglotterException {
        this.operation.add( TestConstants.STRING_1_TERM, TestConstants.STRING_2_TERM, TestConstants.STRING_3_TERM );

        final Iterator< Term< ? >> itr = this.operation.iterator();
        assertThat( itr.next(), is( notNullValue() ) );
        assertThat( itr.next(), is( notNullValue() ) );
        assertThat( itr.next(), is( notNullValue() ) );
        assertThat( itr.hasNext(), is( false ) );
    }

    @Test( expected = PolyglotterException.class )
    public void shouldNotAllowDuplicateTermToBeAdded() throws PolyglotterException {
        this.operation.add( TestConstants.INT_1_TERM, TestConstants.INT_1_TERM );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowEmptyTermArrayToBeAdded() throws PolyglotterException {
        this.operation.add( ( new Term< ? >[ 0 ] ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowEmptyTermArrayToBeRemoved() throws PolyglotterException {
        this.operation.remove( ( new QName[ 0 ] ) );
    }

    @Test( expected = PolyglotterException.class )
    public void shouldNotAllowGetWhenTermIdIsNotFound() throws PolyglotterException {
        this.operation.get( TestConstants.STRING_1_ID );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowGetWithNullTermId() throws PolyglotterException {
        this.operation.get( null );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotAllowIteratorToRemoveTerms() throws PolyglotterException {
        this.operation.add( TestConstants.STRING_1_TERM );

        final Iterator< Term< ? > > itr = this.operation.iterator();
        itr.next();
        itr.remove();
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullListenerToBeAdded() {
        this.operation.add( ( GrammarListener ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullListenerToBeRemoved() {
        this.operation.remove( ( GrammarListener ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullTermArrayToBeAdded() throws PolyglotterException {
        this.operation.add( ( Term< ? >[] ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullTermArrayToBeRemoved() throws PolyglotterException {
        this.operation.remove( ( QName[] ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullTermToBeAdded() throws PolyglotterException {
        this.operation.add( ( Term< ? > ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullTermToBeRemoved() throws PolyglotterException {
        this.operation.remove( ( QName ) null );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotAllowSettingValue() throws PolyglotterException {
        this.operation.setValue( 2L );
    }

    @Test
    public void shouldNotGetNotifiedWhenTermAddedAfterListenerIsRemoved() throws PolyglotterException {
        this.operation.remove( this.listener );
        this.operation.add( TestConstants.STRING_1_TERM );
        assertThat( this.listener.count(), is( 0 ) );
    }

    @Test
    public void shouldRemoveMultipleTerms() throws PolyglotterException {
        this.operation.add( TestConstants.INT_1_TERM, TestConstants.INT_2_TERM, TestConstants.DOUBLE_1_TERM );
        this.operation.remove( TestConstants.INT_1_TERM.id(), TestConstants.INT_2_TERM.id(), TestConstants.DOUBLE_1_TERM.id() );
        assertThat( this.operation.terms().isEmpty(), is( true ) );
    }

    @Test
    public void shouldRemoveOneTerm() throws PolyglotterException {
        this.operation.add( TestConstants.INT_1_TERM );
        this.operation.remove( TestConstants.INT_1_TERM.id() );
        assertThat( this.operation.terms().size(), is( 0 ) );
    }

    @Test
    public void shouldSetIdAtConstruction() {
        assertThat( this.operation.id(), is( TestConstants.ID ) );
    }

    @Test
    public void shouldSetTransformIdAtConstruction() {
        assertThat( this.operation.transformId(), is( TestConstants.TRANSFORM_ID ) );
    }

    class TestOperation extends AbstractOperation< Long > {

        protected TestOperation() {
            super( TestConstants.ID, TestConstants.TRANSFORM_ID, new Descriptor() {

                @Override
                public String abbreviation() {
                    return "abc";
                }

                @Override
                public Category category() {
                    return Category.ARITHMETIC;
                }

                @Override
                public String description() {
                    return "description";
                }

                @Override
                public String name() {
                    return "name";
                }
            } );
        }

        @Override
        protected Long calculate() {
            return 1L;
        }

        @Override
        public int maxTerms() {
            return 0;
        }

        @Override
        public int minTerms() {
            return 0;
        }

        @Override
        public void validate() {}

    }

}