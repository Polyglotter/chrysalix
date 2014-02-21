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
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.GrammarListener;
import org.polyglotter.grammar.GrammarPart;
import org.polyglotter.grammar.Term;
import org.polyglotter.grammar.TestDoubleTerm;
import org.polyglotter.grammar.TestGrammarListener;
import org.polyglotter.grammar.TestIntegerTerm;
import org.polyglotter.grammar.TestStringTerm;

@SuppressWarnings( "javadoc" )
public final class AddTest {
    
    private static final QName ID = new QName( GrammarPart.NAMESPACE_URI, "AddTest", GrammarPart.NAMESPACE_PREFIX );
    
    private static final QName INT_1_ID = new QName( ID.getNamespaceURI(), "Int1", ID.getPrefix() );
    private static final int INT_1_VALUE = 10;
    private static final TestIntegerTerm INT_1 = new TestIntegerTerm( INT_1_ID, ID, INT_1_VALUE );
    
    private static final QName INT_2_ID = new QName( ID.getNamespaceURI(), "Int2", ID.getPrefix() );
    private static final int INT_2_VALUE = 25;
    private static final TestIntegerTerm INT_2 = new TestIntegerTerm( INT_2_ID, ID, INT_2_VALUE );
    
    private static final QName DOUBLE_1_ID = new QName( ID.getNamespaceURI(), "Double1", ID.getPrefix() );
    private static final double DOUBLE_1_VALUE = 12.34D;
    private static final TestDoubleTerm DOUBLE_1 = new TestDoubleTerm( DOUBLE_1_ID, ID, DOUBLE_1_VALUE );
    
    private static final QName STRING_1_ID = new QName( ID.getNamespaceURI(), "String1", ID.getPrefix() );
    private static final TestStringTerm STRING_1 = new TestStringTerm( STRING_1_ID, ID );
    
    private static final QName TRANSFORM_ID =
        new QName( GrammarPart.NAMESPACE_URI, "TransformTest", GrammarPart.NAMESPACE_PREFIX );
    
    private TestGrammarListener listener;
    private Add operation;
    
    @Before
    public void beforeEach() {
        this.operation = new Add( ID, TRANSFORM_ID );
        this.listener = new TestGrammarListener();
        this.operation.add( this.listener );
    }
    
    @Test
    public void shouldAddIntegerAndDouble() throws PolyglotterException {
        this.operation.add( INT_1 );
        this.operation.add( DOUBLE_1 );
        // assertThat( this.operation.terms(), hasItems( INT_1, DOUBLE_1 ) ); // TODO fix
        assertThat( this.operation.result().value(), is( ( Number ) ( INT_1_VALUE + DOUBLE_1_VALUE ) ) );
    }
    
    @Test
    public void shouldAddMultipleTerms() throws PolyglotterException {
        this.operation.add( INT_1, INT_2, DOUBLE_1 );
        assertThat( this.operation.terms().size(), is( 3 ) );
        // assertThat(this.operation.terms(), hasItems(INT_1, INT_2, DOUBLE_1)); // TODO fix
        assertThat( this.operation.result().value(), is( ( Number ) ( INT_1_VALUE + INT_2_VALUE + DOUBLE_1_VALUE ) ) );
    }
    
    @Test
    public void shouldAddOneTerm() throws PolyglotterException {
        this.operation.add( INT_1 );
        assertThat( this.operation.terms().size(), is( 1 ) );
        // assertThat( this.operation.get( INT_1.id() ), is( INT_1 ) ); / TODO fix
    }
    
    @Test
    public void shouldCalculateIntegerResult() throws PolyglotterException {
        this.operation.add( INT_1 );
        this.operation.add( INT_2 );
        assertThat( this.operation.result().value(), is( ( Number ) Integer.valueOf( INT_1_VALUE + INT_2_VALUE ).longValue() ) ); // TODO
    }
    
    @Test( expected = IllegalArgumentException.class )
    public void shouldErrorWhenRemovingUnregisteredListener() {
        this.operation.remove( new TestGrammarListener() );
    }
    
    @Test
    public void shouldGetNotifiedWhenTermIsAdded() throws PolyglotterException {
        this.operation.add( INT_1 );
        assertThat( this.listener.count(), is( 1 ) );
    }
    
    @Test
    public void shouldGetNotifiedWhenTermIsRemoved() throws PolyglotterException {
        this.operation.add( INT_1 );
        this.listener.clear();
        this.operation.remove( INT_1_ID );
        assertThat( this.listener.count(), is( 1 ) );
        // assertThat( this.listener.lastEvent().type(), is( OperationEventType.TERM_REMOVED ) ); // TODO fix
    }
    
    @Test
    public void shouldHaveEmpyIteratorAfterConstruction() {
        assertThat( this.operation.iterator().hasNext(), is( false ) );
    }
    
    @Test
    public void shouldHaveErrorsAfterConstruction() {
        assertThat( this.operation.problems().isError(), is( true ) );
    }
    
    @Test
    public void shouldHaveErrorWhenAddingTermWithWrongType() throws PolyglotterException {
        this.operation.add( INT_1, INT_2 ); // will get rid of current problems
        this.operation.add( STRING_1 );
        assertThat( this.operation.problems().size(), is( 1 ) );
        assertThat( this.operation.problems().isError(), is( true ) );
    }
    
    @Test
    public void shouldHaveIteratorSizeEqualToTermSize() throws PolyglotterException {
        this.operation.add( INT_1, INT_2, DOUBLE_1 );
        
        final Iterator< Term< ? >> itr = this.operation.iterator();
        assertThat( itr.next(), is( notNullValue() ) );
        assertThat( itr.next(), is( notNullValue() ) );
        assertThat( itr.next(), is( notNullValue() ) );
        assertThat( itr.hasNext(), is( false ) );
    }
    
    @Test
    public void shouldHaveProblemsAfterConstruction() {
        assertThat( this.operation.problems().isEmpty(), is( false ) );
    }
    
    @Test( expected = PolyglotterException.class )
    public void shouldNotAllowDuplicateTermToBeAdded() throws PolyglotterException {
        this.operation.add( INT_1, INT_1 );
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
        this.operation.get( INT_1_ID );
    }
    
    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowGetWithNullTermId() throws PolyglotterException {
        this.operation.get( null );
    }
    
    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotAllowIteratorToRemoveTerms() throws PolyglotterException {
        this.operation.add( INT_1 );
        
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
    
    @Test( expected = PolyglotterException.class )
    public void shouldNotBeAbleToGetResultAfterConstruction() throws PolyglotterException {
        this.operation.result();
    }
    
    @Test( expected = PolyglotterException.class )
    public void shouldNotBeAbleToGetResultWithOnlyOneTerm() throws PolyglotterException {
        this.operation.add( INT_1 );
        this.operation.result();
    }
    
    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotBeAbleToModifyTermsList() {
        this.operation.terms().add( INT_1 );
    }
    
    @Test
    public void shouldNotGetNotifiedWhenTermAddedAfterListenerIsRemoved() throws PolyglotterException {
        this.operation.remove( this.listener );
        this.operation.add( INT_1 );
        assertThat( this.listener.count(), is( 0 ) );
    }
    
    @Test
    public void shouldNotHaveProblemsWithTwoTermsOfCorrectType() throws PolyglotterException {
        this.operation.add( INT_1, INT_2 );
        assertThat( this.operation.problems().isEmpty(), is( true ) );
        assertThat( this.operation.problems().isOk(), is( true ) );
    }
    
    @Test
    public void shouldNotHaveTermsAfterConstruction() {
        assertThat( this.operation.terms().isEmpty(), is( true ) );
    }
    
    @Test
    public void shouldNotRegisterSameListenerMoreThanOnce() throws PolyglotterException {
        this.operation.add( this.listener ); // 2nd time added (other is in test construction)
        this.operation.add( INT_1 );
        assertThat( this.listener.count(), is( 1 ) );
    }
    
    @Test
    public void shouldProvideDescription() {
        assertThat( this.operation.description(), is( PolyglotterI18n.addOperationDescription.text() ) );
    }
    
    @Test
    public void shouldProvideName() {
        assertThat( this.operation.name(), is( PolyglotterI18n.addOperationName.text() ) );
    }
    
    @Test
    public void shouldRemoveMultipleTerms() throws PolyglotterException {
        this.operation.add( INT_1, INT_2, DOUBLE_1 );
        this.operation.remove( INT_1.id(), INT_2.id(), DOUBLE_1.id() );
        assertThat( this.operation.terms().isEmpty(), is( true ) );
    }
    
    @Test
    public void shouldRemoveOneTerm() throws PolyglotterException {
        this.operation.add( INT_1 );
        this.operation.remove( INT_1.id() );
        assertThat( this.operation.terms().size(), is( 0 ) );
    }
    
    @Test
    public void shouldSetIdAtConstruction() {
        assertThat( this.operation.id(), is( ID ) );
    }
    
    @Test
    public void shouldSetTransformIdAtConstruction() {
        assertThat( this.operation.transformId(), is( TRANSFORM_ID ) );
    }
    
    @Test( expected = PolyglotterException.class )
    public void shouldThrowErrorAfterConstructionWhenRetrievingResult() throws PolyglotterException {
        this.operation.result();
    }
}
