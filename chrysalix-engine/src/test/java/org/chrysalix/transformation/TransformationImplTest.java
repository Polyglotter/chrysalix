/*
 * Chrysalix
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * Chrysalix is free software. Unless otherwise indicated, all code in Chrysalix
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * Chrysalix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.chrysalix.transformation;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.chrysalix.ChrysalixException;
import org.chrysalix.operation.OperationTestConstants;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.modelspace.Model;
import org.modelspace.ModelObject;
import org.modelspace.Modelspace;

/**
 * Test class for the transformation class created by the factory.
 */
@Ignore
@SuppressWarnings( "javadoc" )
public final class TransformationImplTest {

    private static final TransformationFactory FACTORY = new TransformationFactory( mock( Modelspace.class ) );
    private Transformation transformation;

    @Before
    public void beforeEach() throws Exception {
        this.transformation = FACTORY.createTransformation( OperationTestConstants.TRANSFORM_ID );
    }

    @Test
    public void shouldAddModelAsBothSourceAndTarget() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.addSource( model );
        this.transformation.addTarget( model );
        assertThat( this.transformation.sources().length, is( 1 ) );
        assertThat( this.transformation.sources()[ 0 ], is( model ) );
        assertThat( this.transformation.targets().length, is( 1 ) );
        assertThat( this.transformation.targets()[ 0 ], is( model ) );
    }

    @Test
    public void shouldAddMultipeSourceModels() throws Exception {
        final Model model_1 = mock( Model.class );
        final Model model_2 = mock( Model.class );
        this.transformation.addSource( model_1, model_2 );
        assertThat( this.transformation.sources().length, is( 2 ) );
        assertThat( Arrays.asList( this.transformation.sources() ), hasItems( model_1, model_2 ) );
        assertThat( this.transformation.targets().length, is( 0 ) );
    }

    @Test
    public void shouldAddMultipeSourceTargetModels() throws Exception {
        final Model model_1 = mock( Model.class );
        final Model model_2 = mock( Model.class );
        this.transformation.addSource( model_1, model_2 );
        this.transformation.addTarget( model_1, model_2 );
        assertThat( this.transformation.sources().length, is( 2 ) );
        assertThat( Arrays.asList( this.transformation.sources() ), hasItems( model_1, model_2 ) );
        assertThat( this.transformation.targets().length, is( 2 ) );
        assertThat( Arrays.asList( this.transformation.targets() ), hasItems( model_1, model_2 ) );
    }

    @Test
    public void shouldAddMultipeTargetModels() throws Exception {
        final Model model_1 = mock( Model.class );
        final Model model_2 = mock( Model.class );
        this.transformation.addTarget( model_1, model_2 );
        assertThat( this.transformation.targets().length, is( 2 ) );
        assertThat( Arrays.asList( this.transformation.targets() ), hasItems( model_1, model_2 ) );
        assertThat( this.transformation.sources().length, is( 0 ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void shouldAddMultipleOperations() throws Exception {
        final Operation< ? > op_1 = mock( Operation.class );
        final Operation< ? > op_2 = mock( Operation.class );
        this.transformation.add( op_1, op_2 );
        assertThat( this.transformation.operations().length, is( 2 ) );
        assertThat( Arrays.asList( this.transformation.operations() ), hasItems( op_1, op_2 ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void shouldAddOperations() throws Exception {
        final Operation< ? > op_1 = mock( Operation.class );
        final Operation< ? > op_2 = mock( Operation.class );
        this.transformation.add( new Operation< ? >[] { op_1, op_2 } );
        assertThat( this.transformation.operations().length, is( 2 ) );
        assertThat( Arrays.asList( this.transformation.operations() ), hasItems( op_1, op_2 ) );
    }

    @Test
    public void shouldAddSourceModel() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.addSource( model );
        assertThat( this.transformation.sources().length, is( 1 ) );
        assertThat( this.transformation.sources()[ 0 ], is( model ) );
        assertThat( this.transformation.targets().length, is( 0 ) );
    }

    @Test
    public void shouldAddTargetModel() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.addTarget( model );
        assertThat( this.transformation.targets().length, is( 1 ) );
        assertThat( this.transformation.targets()[ 0 ], is( model ) );
        assertThat( this.transformation.sources().length, is( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingEmptyModels() throws Exception {
        this.transformation.addSource( new Model[ 0 ] );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingEmptyOperations() throws Exception {
        this.transformation.add( new Operation< ? >[ 0 ] );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailAddingNullOperation() throws Exception {
        this.transformation.add( mock( Operation.class ), null, mock( Operation.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingNullOperations() throws Exception {
        this.transformation.add( ( Operation< ? >[] ) null );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailAddingNullSourceModel() throws Exception {
        this.transformation.addSource( mock( Model.class ), null, mock( Model.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingNullSourceModels() throws Exception {
        this.transformation.addSource( ( Model[] ) null );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailAddingNullTargetModel() throws Exception {
        this.transformation.addTarget( mock( Model.class ), null, mock( Model.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingNullTargetModels() throws Exception {
        this.transformation.addTarget( ( Model[] ) null );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailAddingOperationMultipleTimes() throws Exception {
        final Operation< ? > op = OperationTestConstants.DESCRIPTOR.newInstance( mock( ModelObject.class ),
                                                                                 this.transformation );
        this.transformation.add( op, mock( Operation.class ), op );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailAddingSameModelAsSourceTwice() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.addSource( model );
        this.transformation.addSource( model );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailAddingSameModelAsTargetTwice() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.addTarget( model );
        this.transformation.addTarget( model );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailRemovingEmptySourceModels() throws Exception {
        this.transformation.removeSource( new Model[ 0 ] );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailRemovingEmptyTargetModels() throws Exception {
        this.transformation.removeTarget( new Model[ 0 ] );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailRemovingNullSourceModel() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.addSource( model );
        this.transformation.removeSource( model, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailRemovingNullSourceModels() throws Exception {
        this.transformation.removeSource( ( Model[] ) null );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailRemovingNullTargetModel() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.addTarget( model );
        this.transformation.removeTarget( model, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailRemovingNullTargetModels() throws Exception {
        this.transformation.removeTarget( ( Model[] ) null );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailRemovingSourceModelThatWasNeverAdded() throws Exception {
        this.transformation.removeSource( mock( Model.class ) );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailRemovingTargetModelThatWasNeverAdded() throws Exception {
        this.transformation.removeTarget( mock( Model.class ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void shouldIterateOverOperations() throws Exception {
        final int count = 3;
        final List< Operation< Integer >> operations = new ArrayList<>( count );

        for ( int i = 0; i < count; ++i ) {
            final Operation< Integer > op =
                OperationTestConstants.DESCRIPTOR.newInstance( mock( ModelObject.class ),
                                                               this.transformation );
            this.transformation.add( op );
            operations.add( op );
        }

        final Iterator< Operation< ? > > itr = this.transformation.iterator();
        assertThat( itr.hasNext(), is( true ) );

        for ( int i = 0; i < count; ++i ) {
            assertThat( ( Operation< Integer > ) itr.next(), is( operations.get( i ) ) );
        }
    }

    @Test
    public void shouldNotInitiallyHaveOperations() throws Exception {
        assertThat( this.transformation.operations().length, is( 0 ) );
        assertThat( this.transformation.iterator().hasNext(), is( false ) );
    }

    @Test
    public void shouldNotInitiallyHaveSources() throws Exception {
        assertThat( this.transformation.sources().length, is( 0 ) );
    }

    @Test
    public void shouldNotInitiallyHaveTargets() throws Exception {
        assertThat( this.transformation.targets().length, is( 0 ) );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotObtainModifiableOperationIterator() throws Exception {
        this.transformation.add( mock( Operation.class ) );
        final Iterator< Operation< ? > > itr = this.transformation.iterator();
        itr.next();
        itr.remove();
    }

    @Test
    public void shouldObtainId() throws Exception {
        assertThat( this.transformation.name(), is( OperationTestConstants.TRANSFORM_ID ) );
    }

    @Test
    public void shouldRemoveMultipleOperations() throws Exception {
        final Operation< ? > op_1 = mock( Operation.class );
        final Operation< ? > op_2 = mock( Operation.class );
        this.transformation.add( op_1, op_2 );
        this.transformation.remove( op_1, op_2 );
        assertThat( this.transformation.operations().length, is( 0 ) );
    }

    @Test
    public void shouldRemoveMultipleSourceModels() throws Exception {
        final Model model_1 = mock( Model.class );
        final Model model_2 = mock( Model.class );
        this.transformation.addSource( model_1, model_2 );
        this.transformation.addTarget( model_1 );
        this.transformation.removeSource( model_1, model_2 );
        assertThat( this.transformation.sources().length, is( 0 ) );
        assertThat( this.transformation.targets().length, is( 1 ) );
    }

    @Test
    public void shouldRemoveMultipleTargetModels() throws Exception {
        final Model model_1 = mock( Model.class );
        final Model model_2 = mock( Model.class );
        this.transformation.addTarget( model_1, model_2 );
        this.transformation.addSource( model_1 );
        this.transformation.removeTarget( model_1, model_2 );
        assertThat( this.transformation.targets().length, is( 0 ) );
        assertThat( this.transformation.sources().length, is( 1 ) );
    }

    @Test
    public void shouldRemoveOperation() throws Exception {
        final Operation< ? > op = mock( Operation.class );
        this.transformation.add( op );
        this.transformation.remove( op );
        assertThat( this.transformation.operations().length, is( 0 ) );
    }

    @Test
    public void shouldRemoveSourceModel() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.addSource( model );
        this.transformation.removeSource( model );
        assertThat( this.transformation.sources().length, is( 0 ) );
    }

    @Test
    public void shouldRemoveTargetModel() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.addTarget( model );
        this.transformation.removeTarget( model );
        assertThat( this.transformation.targets().length, is( 0 ) );
    }

}
