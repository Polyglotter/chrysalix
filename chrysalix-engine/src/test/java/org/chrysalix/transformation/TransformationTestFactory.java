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
package org.chrysalix.transformation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.chrysalix.ChrysalixException;
import org.chrysalix.operation.OperationTestConstants;
import org.chrysalix.operation.ValueImpl;
import org.chrysalix.transformation.OperationDescriptorProvider;
import org.chrysalix.transformation.TransformationFactory;
import org.chrysalix.transformation.Value;
import org.chrysalix.transformation.ValueDescriptor;
import org.modelspace.Model;
import org.modelspace.ModelObject;
import org.modelspace.Modelspace;

@SuppressWarnings( "javadoc" )
public class TransformationTestFactory extends TransformationFactory {

    public TransformationTestFactory() {
        super( mock( Modelspace.class ) );
        addOperationProvider( new TestOperationDescriptorProvider() );
    }

    private ModelObject createModelProperty( final String path ) throws Exception {
        final ModelObject prop = mock( ModelObject.class );
        when( prop.name() ).thenReturn( path );

        final Model model = mock( Model.class );
        when( model.modelspace() ).thenReturn( modelspace() );
        when( prop.model() ).thenReturn( model );

        return prop;
    }

    public Value< Number > createNumberValue( final String path,
                                              final ValueDescriptor< Number > descriptor,
                                              final double propValue ) throws Exception {
        final ModelObject prop = createModelProperty( path );
        final NumberValue value = new NumberValue( descriptor, prop, propValue );
        value.set( propValue );
        return value;
    }

    public Value< Number > createNumberValue( final String path,
                                              final ValueDescriptor< Number > descriptor,
                                              final int propValue ) throws Exception {
        final ModelObject prop = createModelProperty( path );
        final NumberValue value = new NumberValue( descriptor, prop, propValue );
        value.set( propValue );
        return value;
    }

    public Value< Number > createNumberValue( final String path,
                                              final ValueDescriptor< Number > descriptor,
                                              final long propValue ) throws Exception {
        final ModelObject prop = createModelProperty( path );
        final NumberValue value = new NumberValue( descriptor, prop, propValue );
        value.set( propValue );
        return value;
    }

    public Value< Object > createObjectValue( final String path,
                                              final ValueDescriptor< Object > descriptor,
                                              final double propValue ) throws Exception {
        final ModelObject prop = createModelProperty( path );
        final ObjectValue value = new ObjectValue( descriptor, prop, propValue );
        value.set( propValue );
        return value;
    }

    public Value< Object > createObjectValue( final String path,
                                              final ValueDescriptor< Object > descriptor,
                                              final int propValue ) throws Exception {
        final ModelObject prop = createModelProperty( path );
        final ObjectValue value = new ObjectValue( descriptor, prop, propValue );
        value.set( propValue );
        return value;
    }

    public Value< Object > createObjectValue( final String path,
                                              final ValueDescriptor< Object > descriptor,
                                              final String propValue ) throws Exception {
        final ModelObject prop = createModelProperty( path );
        final ObjectValue value = new ObjectValue( descriptor, prop, propValue );
        value.set( propValue );
        return value;
    }

    public Value< Double > createValue( final String path,
                                        final ValueDescriptor< Double > descriptor,
                                        final double propValue ) throws Exception {
        final ModelObject prop = createModelProperty( path );
        final DoubleValue value = new DoubleValue( descriptor, prop, propValue );
        value.set( propValue );
        return value;
    }

    public Value< Float > createValue( final String path,
                                       final ValueDescriptor< Float > descriptor,
                                       final float propValue ) throws Exception {
        final ModelObject prop = createModelProperty( path );
        final FloatValue value = new FloatValue( descriptor, prop, propValue );
        value.set( propValue );
        return value;
    }

    public Value< Integer > createValue( final String path,
                                         final ValueDescriptor< Integer > descriptor,
                                         final int propValue ) throws Exception {
        final ModelObject prop = createModelProperty( path );
        final IntValue value = new IntValue( descriptor, prop, propValue );
        value.set( propValue );
        return value;
    }

    public Value< Long > createValue( final String path,
                                      final ValueDescriptor< Long > descriptor,
                                      final long propValue ) throws Exception {
        final ModelObject prop = createModelProperty( path );
        final LongValue value = new LongValue( descriptor, prop, propValue );
        value.set( propValue );
        return value;
    }

    public Value< String > createValue( final String path,
                                        final ValueDescriptor< String > descriptor,
                                        final String propValue ) throws Exception {
        final ModelObject prop = createModelProperty( path );
        final StringValue value = new StringValue( descriptor, prop, propValue );
        value.set( propValue );
        return value;
    }

    class DoubleValue extends ValueImpl< Double > {

        public DoubleValue( final ValueDescriptor< Double > descriptor,
                            final ModelObject modelObject,
                            final double initialValue ) throws ChrysalixException {
            super( descriptor.id(), modelObject );
            set( initialValue );
        }

    }

    class FloatValue extends ValueImpl< Float > {

        public FloatValue( final ValueDescriptor< Float > descriptor,
                           final ModelObject modelObject,
                           final float initialValue ) throws ChrysalixException {
            super( descriptor.id(), modelObject );
            set( initialValue );
        }

    }

    class IntValue extends ValueImpl< Integer > {

        public IntValue( final ValueDescriptor< Integer > descriptor,
                         final ModelObject modelObject,
                         final int initialValue ) throws ChrysalixException {
            super( descriptor.id(), modelObject );
            set( initialValue );
        }

    }

    class LongValue extends ValueImpl< Long > {

        public LongValue( final ValueDescriptor< Long > descriptor,
                          final ModelObject modelObject,
                          final long initialValue ) throws ChrysalixException {
            super( descriptor.id(), modelObject );
            set( initialValue );
        }

    }

    class NumberValue extends ValueImpl< Number > {

        public NumberValue( final ValueDescriptor< Number > descriptor,
                            final ModelObject modelObject,
                            final Number initialValue ) throws ChrysalixException {
            super( descriptor.id(), modelObject );
            set( initialValue );
        }

    }

    class ObjectValue extends ValueImpl< Object > {

        public ObjectValue( final ValueDescriptor< Object > descriptor,
                            final ModelObject modelObject,
                            final Object initialValue ) throws ChrysalixException {
            super( descriptor.id(), modelObject );
            set( initialValue );
        }

    }

    class StringValue extends ValueImpl< String > {

        public StringValue( final ValueDescriptor< String > descriptor,
                            final ModelObject modelObject,
                            final String initialValue ) throws ChrysalixException {
            super( descriptor.id(), modelObject );
            set( initialValue );
        }

    }

    class TestOperationDescriptorProvider implements OperationDescriptorProvider {

        @Override
        public List< ValueDescriptor< ? >> descriptors() {
            final List< ValueDescriptor< ? >> descriptors = new ArrayList<>();
            descriptors.add( OperationTestConstants.DESCRIPTOR );
            descriptors.add( OperationTestConstants.DOUBLE_DESCRIPTOR );
            descriptors.add( OperationTestConstants.FLOAT_DESCRIPTOR );
            descriptors.add( OperationTestConstants.INT_DESCRIPTOR );
            descriptors.add( OperationTestConstants.LONG_DESCRIPTOR );
            descriptors.add( OperationTestConstants.STRING_DESCRIPTOR );
            return descriptors;
        }

    }

}
