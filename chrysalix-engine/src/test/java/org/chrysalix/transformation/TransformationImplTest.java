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
import org.chrysalix.transformation.Operation;
import org.chrysalix.transformation.Transformation;
import org.chrysalix.transformation.TransformationFactory;
import org.chrysalix.transformation.Transformation.ModelType;
import org.junit.Before;
import org.junit.Test;
import org.modelspace.Model;

/**
 * Test class for the transformation class created by the factory.
 */
@SuppressWarnings( "javadoc" )
public final class TransformationImplTest {

    private Transformation transformation;

    @Before
    public void beforeEach() {
        this.transformation = TransformationFactory.createTransformation( OperationTestConstants.TRANSFORM_ID );
    }

    @Test
    public void shouldAddModelAsBothSourceAndTarget() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( Transformation.ModelType.SOURCE_TARGET, model );
        assertThat( this.transformation.sources().length, is( 1 ) );
        assertThat( this.transformation.sources()[ 0 ], is( model ) );
        assertThat( this.transformation.targets().length, is( 1 ) );
        assertThat( this.transformation.targets()[ 0 ], is( model ) );
    }

    @Test
    public void shouldAddMultipeSourceModels() throws Exception {
        final Model model_1 = mock( Model.class );
        final Model model_2 = mock( Model.class );
        this.transformation.add( Transformation.ModelType.SOURCE, model_1, model_2 );
        assertThat( this.transformation.sources().length, is( 2 ) );
        assertThat( Arrays.asList( this.transformation.sources() ), hasItems( model_1, model_2 ) );
        assertThat( this.transformation.targets().length, is( 0 ) );
    }

    @Test
    public void shouldAddMultipeSourceTargetModels() throws Exception {
        final Model model_1 = mock( Model.class );
        final Model model_2 = mock( Model.class );
        this.transformation.add( Transformation.ModelType.SOURCE_TARGET, model_1, model_2 );
        assertThat( this.transformation.sources().length, is( 2 ) );
        assertThat( Arrays.asList( this.transformation.sources() ), hasItems( model_1, model_2 ) );
        assertThat( this.transformation.targets().length, is( 2 ) );
        assertThat( Arrays.asList( this.transformation.targets() ), hasItems( model_1, model_2 ) );
    }

    @Test
    public void shouldAddMultipeTargetModels() throws Exception {
        final Model model_1 = mock( Model.class );
        final Model model_2 = mock( Model.class );
        this.transformation.add( Transformation.ModelType.TARGET, model_1, model_2 );
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
        this.transformation.add( Transformation.ModelType.SOURCE, model );
        assertThat( this.transformation.sources().length, is( 1 ) );
        assertThat( this.transformation.sources()[ 0 ], is( model ) );
        assertThat( this.transformation.targets().length, is( 0 ) );
    }

    @Test
    public void shouldAddSourceToTargetModel() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( Transformation.ModelType.TARGET, model );
        this.transformation.add( Transformation.ModelType.SOURCE, model );
        assertThat( this.transformation.sources().length, is( 1 ) );
        assertThat( this.transformation.sources()[ 0 ], is( model ) );
        assertThat( this.transformation.targets().length, is( 1 ) );
        assertThat( this.transformation.targets()[ 0 ], is( model ) );
    }

    @Test
    public void shouldAddTargetModel() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( Transformation.ModelType.TARGET, model );
        assertThat( this.transformation.targets().length, is( 1 ) );
        assertThat( this.transformation.targets()[ 0 ], is( model ) );
        assertThat( this.transformation.sources().length, is( 0 ) );
    }

    @Test
    public void shouldAddTargetToSourceModel() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( Transformation.ModelType.SOURCE, model );
        this.transformation.add( Transformation.ModelType.TARGET, model );
        assertThat( this.transformation.sources().length, is( 1 ) );
        assertThat( this.transformation.sources()[ 0 ], is( model ) );
        assertThat( this.transformation.targets().length, is( 1 ) );
        assertThat( this.transformation.targets()[ 0 ], is( model ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingEmptyModels() throws Exception {
        this.transformation.add( ModelType.SOURCE, new Model[ 0 ] );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingEmptyOperations() throws Exception {
        this.transformation.add( new Operation< ? >[ 0 ] );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailAddingModelAgainWithSameModelType() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( Transformation.ModelType.SOURCE, model );
        this.transformation.add( Transformation.ModelType.SOURCE, model );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailAddingModelAgainWithSameModelType2() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( Transformation.ModelType.TARGET, model );
        this.transformation.add( Transformation.ModelType.TARGET, model );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailAddingModelAgainWithSameModelType3() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( Transformation.ModelType.SOURCE_TARGET, model );
        this.transformation.add( Transformation.ModelType.SOURCE_TARGET, model );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingModelAndNullModelType() throws Exception {
        this.transformation.add( null, mock( Model.class ) );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailAddingNullModel() throws Exception {
        this.transformation.add( Transformation.ModelType.SOURCE, mock( Model.class ), null, mock( Model.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailAddingNullModels() throws Exception {
        this.transformation.add( Transformation.ModelType.TARGET, ( Model[] ) null );
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
    public void shouldFailAddingOperationMultipleTimes() throws Exception {
        final Operation< ? > op = OperationTestConstants.TEST_OPERATION_DESCRIPTOR.newInstance( this.transformation );
        this.transformation.add( op, mock( Operation.class ), op );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailRemovingEmptyModels() throws Exception {
        this.transformation.remove( new Model[ 0 ] );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailRemovingModelThatWasNeverAdded() throws Exception {
        this.transformation.remove( mock( Model.class ) );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailRemovingModelTypeThatWasNeverAdded() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( Transformation.ModelType.SOURCE, model );
        this.transformation.remove( Transformation.ModelType.TARGET, model );
    }

    @Test( expected = ChrysalixException.class )
    public void shouldFailRemovingNullModel() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( Transformation.ModelType.SOURCE, model );
        this.transformation.remove( Transformation.ModelType.TARGET, model, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailRemovingNullModels() throws Exception {
        this.transformation.remove( Transformation.ModelType.TARGET, ( Model[] ) null );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void shouldIterateOverOperations() throws Exception {
        final int count = 3;
        final List< Operation< Integer >> operations = new ArrayList<>( count );

        for ( int i = 0; i < count; ++i ) {
            final Operation< Integer > op = OperationTestConstants.TEST_OPERATION_DESCRIPTOR.newInstance( this.transformation );
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
    public void shouldNotInitiallyHaveOperations() {
        assertThat( this.transformation.operations().length, is( 0 ) );
        assertThat( this.transformation.iterator().hasNext(), is( false ) );
    }

    @Test
    public void shouldNotInitiallyHaveSources() {
        assertThat( this.transformation.sources().length, is( 0 ) );
    }

    @Test
    public void shouldNotInitiallyHaveTargets() {
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
    public void shouldObtainId() {
        assertThat( this.transformation.id(), is( OperationTestConstants.TRANSFORM_ID ) );
    }

    @Test
    public void shouldRemoveModel() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( ModelType.SOURCE, model );
        this.transformation.remove( model );
        assertThat( this.transformation.sources().length, is( 0 ) );
    }

    @Test
    public void shouldRemoveModel2() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( ModelType.TARGET, model );
        this.transformation.remove( model );
        assertThat( this.transformation.sources().length, is( 0 ) );
    }

    @Test
    public void shouldRemoveModel3() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( ModelType.SOURCE_TARGET, model );
        this.transformation.remove( model );
        assertThat( this.transformation.sources().length, is( 0 ) );
    }

    @Test
    public void shouldRemoveModelWhenLastModelTypeIsRemoved() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( ModelType.SOURCE_TARGET, model );
        this.transformation.remove( ModelType.SOURCE, model );
        this.transformation.remove( ModelType.TARGET, model );
        assertThat( this.transformation.sources().length, is( 0 ) );
    }

    @Test
    public void shouldRemoveMultipleModels() throws Exception {
        final Model source = mock( Model.class );
        final Model target = mock( Model.class );
        this.transformation.add( ModelType.SOURCE, source );
        this.transformation.add( ModelType.TARGET, target );
        this.transformation.remove( source, target );
        assertThat( this.transformation.sources().length, is( 0 ) );
        assertThat( this.transformation.targets().length, is( 0 ) );
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
        this.transformation.add( ModelType.SOURCE, model_1, model_2 );
        this.transformation.add( ModelType.TARGET, mock( Model.class ) );
        this.transformation.remove( ModelType.SOURCE, model_1, model_2 );
        assertThat( this.transformation.sources().length, is( 0 ) );
        assertThat( this.transformation.targets().length, is( 1 ) );
    }

    @Test
    public void shouldRemoveMultipleTargetModels() throws Exception {
        final Model model_1 = mock( Model.class );
        final Model model_2 = mock( Model.class );
        this.transformation.add( ModelType.TARGET, model_1, model_2 );
        this.transformation.add( ModelType.SOURCE, mock( Model.class ) );
        this.transformation.remove( ModelType.TARGET, model_1, model_2 );
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
        this.transformation.add( ModelType.SOURCE, model );
        this.transformation.remove( ModelType.SOURCE, model );
        assertThat( this.transformation.sources().length, is( 0 ) );
    }

    @Test
    public void shouldRemoveTargetModel() throws Exception {
        final Model model = mock( Model.class );
        this.transformation.add( ModelType.TARGET, model );
        this.transformation.remove( ModelType.TARGET, model );
        assertThat( this.transformation.sources().length, is( 0 ) );
    }

}
