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
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.polyglotter.PolyglotterException;
import org.polyglotter.transformation.TransformationFactory;
import org.polyglotter.transformation.Value;

@SuppressWarnings( "javadoc" )
public final class AbstractOperationTest {

    private AbstractOperation< Integer > operation;

    @Before
    public void beforeEach() {
        this.operation =
            ( AbstractOperation< Integer > ) OperationTestConstants.TEST_OPERATION_DESCRIPTOR.newInstance( OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldAddMultipleinputs() throws PolyglotterException {
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(), OperationTestConstants.STRING_1_TERM );
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(), OperationTestConstants.STRING_2_TERM );
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(), OperationTestConstants.STRING_3_TERM );
        assertThat( this.operation.inputs( OperationTestConstants.STRING_DESCRIPTOR.id() ).size(), is( 3 ) );
        assertThat( this.operation.inputs().size(), is( 3 ) );
    }

    @Test
    public void shouldAddMultipleTerms2() throws PolyglotterException {
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(),
                                 OperationTestConstants.STRING_1_TERM,
                                 OperationTestConstants.STRING_2_TERM,
                                 OperationTestConstants.STRING_3_TERM );
        assertThat( this.operation.inputs( OperationTestConstants.STRING_DESCRIPTOR.id() ).size(), is( 3 ) );
        assertThat( this.operation.inputs().size(), is( 3 ) );
    }

    @Test
    public void shouldBeAbleToUseOperationAsTerm() throws PolyglotterException {
        this.operation.addInput( OperationTestConstants.INT_DESCRIPTOR.id(), OperationTestConstants.INT_1_TERM );

        final long term = 2;
        final Add addOp = new Add( OperationTestConstants.TEST_TRANSFORMATION );
        addOp.addInput( Add.TERM_DESCRIPTOR.id(), this.operation );
        addOp.addInput( Add.TERM_DESCRIPTOR.id(), TransformationFactory.createValue( Add.TERM_DESCRIPTOR, term ) );

        assertThat( addOp.get(), is( ( Number ) ( term + OperationTestConstants.INT_1_VALUE ) ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingEmptyTerms() throws PolyglotterException {
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(), new Object[ 0 ] );
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(), ( Object[] ) new Value< ? >[ 0 ] );
    }

    @Test( expected = PolyglotterException.class )
    public void shouldFailAddingNullTerm() throws PolyglotterException {
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(),
                                 new Object[] { OperationTestConstants.STRING_1_TERM,
                                                 null,
                                                 OperationTestConstants.STRING_2_TERM } );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingNullTerms() throws PolyglotterException {
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(), ( Object[] ) null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailIfNullDescriptor() {
        this.operation.inputs( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailRemovingEmptyTerms() throws PolyglotterException {
        this.operation.removeInput( OperationTestConstants.STRING_DESCRIPTOR.id(), ( Object[] ) new Value< ? >[ 0 ] );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldFailRemovingInputUsingIterator() throws PolyglotterException {
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(), OperationTestConstants.STRING_1_TERM );

        final Iterator< Value< ? > > itr = this.operation.iterator();
        itr.next();
        itr.remove();
    }

    @Test( expected = PolyglotterException.class )
    public void shouldFailRemovingInutThatWasNotAdded() throws PolyglotterException {
        this.operation.removeInput( OperationTestConstants.STRING_DESCRIPTOR.id(),
                                    OperationTestConstants.STRING_1_TERM );
    }

    @Test( expected = PolyglotterException.class )
    public void shouldFailRemovingNullTerm() throws PolyglotterException {
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(),
                                 new Object[] { OperationTestConstants.STRING_1_TERM,
                                                 OperationTestConstants.STRING_2_TERM } );
        this.operation.removeInput( OperationTestConstants.STRING_DESCRIPTOR.id(),
                                    ( Object[] ) new Value< ? >[] { OperationTestConstants.STRING_1_TERM,
                                                    null,
                                                    OperationTestConstants.STRING_2_TERM } );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailRemovingNullTerms() throws PolyglotterException {
        this.operation.removeInput( OperationTestConstants.STRING_DESCRIPTOR.id(), ( Object[] ) null );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldFailSettingValue() {
        this.operation.set( 2 );
    }

    @Test
    public void shouldHaveEmpyIteratorAfterConstruction() {
        assertThat( this.operation.iterator().hasNext(), is( false ) );
    }

    @Test
    public void shouldHaveIteratorSizeEqualToTermSize() throws PolyglotterException {
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(),
                                 OperationTestConstants.STRING_1_TERM,
                                 OperationTestConstants.STRING_2_TERM,
                                 OperationTestConstants.STRING_3_TERM );

        final Iterator< Value< ? >> itr = this.operation.iterator();

        for ( int i = 0, size = this.operation.inputs().size(); i < size; ++i ) {
            assertThat( itr.next(), is( notNullValue() ) );
        }

        assertThat( itr.hasNext(), is( false ) );
    }

    @Test
    public void shouldObtainDescriptor() {
        assertThat( this.operation.descriptor(), is( notNullValue() ) );
        assertThat( this.operation.descriptor(), is( sameInstance( OperationTestConstants.TEST_OPERATION_DESCRIPTOR ) ) );
    }

    @Test
    public void shouldRemoveMultipleInputs() throws PolyglotterException {
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(),
                                 OperationTestConstants.STRING_1_TERM,
                                 OperationTestConstants.STRING_2_TERM );
        this.operation.removeInput( OperationTestConstants.STRING_DESCRIPTOR.id(),
                                    OperationTestConstants.STRING_1_TERM,
                                    OperationTestConstants.STRING_2_TERM );
        assertThat( this.operation.inputs().isEmpty(), is( true ) );
    }

    @Test
    public void shouldRemoveOneTerm() throws PolyglotterException {
        this.operation.addInput( OperationTestConstants.STRING_DESCRIPTOR.id(), OperationTestConstants.STRING_1_TERM );
        this.operation.removeInput( OperationTestConstants.STRING_DESCRIPTOR.id(), OperationTestConstants.STRING_1_TERM );
        assertThat( this.operation.inputs().size(), is( 0 ) );
    }

    @Test
    public void shouldSetIdAtConstruction() {
        assertThat( this.operation.transformationId(), is( OperationTestConstants.TRANSFORM_ID ) );
    }

    @Test
    public void shouldSetTransformIdAtConstruction() {
        assertThat( this.operation.transformation(), is( OperationTestConstants.TEST_TRANSFORMATION ) );
    }

}
