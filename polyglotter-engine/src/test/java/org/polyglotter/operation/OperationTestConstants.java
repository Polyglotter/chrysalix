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

import java.util.Iterator;

import org.polyglotter.common.PolyglotterException;
import org.polyglotter.transformation.Operation;
import org.polyglotter.transformation.OperationDescriptor;
import org.polyglotter.transformation.Transformation;
import org.polyglotter.transformation.TransformationFactory;
import org.polyglotter.transformation.Value;
import org.polyglotter.transformation.ValueDescriptor;

@SuppressWarnings( "javadoc" )
public class OperationTestConstants {

    public static ValueDescriptor< Double > DOUBLE_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( "doubleValueId",
                                                     "doubleValueDescription",
                                                     "doubleValueName",
                                                     Double.class,
                                                     true,
                                                     0,
                                                     true );

    public static ValueDescriptor< Float > FLOAT_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( "floatValueId",
                                                     "floatValueDescription",
                                                     "floatValueName",
                                                     Float.class,
                                                     true,
                                                     0,
                                                     true );

    public static ValueDescriptor< Integer > INT_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( "intValueId",
                                                     "intValueDescription",
                                                     "intValueName",
                                                     Integer.class,
                                                     true,
                                                     0,
                                                     true );

    public static ValueDescriptor< Long > LONG_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( "longValueId",
                                                     "longValueDescription",
                                                     "longValueName",
                                                     Long.class,
                                                     true,
                                                     0,
                                                     true );

    public static ValueDescriptor< String > STRING_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( "stringValueId",
                                                     "stringValueDescription",
                                                     "stringValueName",
                                                     String.class,
                                                     true,
                                                     0,
                                                     true );

    static final ValueDescriptor< ? >[] TEST_INPUT_DESCRIPTORS = new ValueDescriptor< ? >[] {
                    DOUBLE_DESCRIPTOR,
                    FLOAT_DESCRIPTOR,
                    INT_DESCRIPTOR,
                    LONG_DESCRIPTOR,
                    STRING_DESCRIPTOR,
    };

    public static OperationDescriptor< Integer > TEST_OPERATION_DESCRIPTOR =
        new AbstractOperationDescriptor< Integer >( "outputDescriptorId",
                                                    "operationDescription",
                                                    "org.polyglotter.TestConstants$TestOperation",
                                                    Integer.class,
                                                    TEST_INPUT_DESCRIPTORS ) {

            @Override
            public Operation< Integer > newInstance( final Transformation transformation ) {
                return new TestOperation( transformation );
            }

        };

    public static final Transformation TEST_TRANSFORMATION = new Transformation() {

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#add(org.polyglotter.transformation.Operation[])
         */
        @Override
        public void add( final Operation< ? >... operations ) {
            // nothing to do
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#id()
         */
        @Override
        public String id() {
            return TRANSFORM_ID;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Iterable#iterator()
         */
        @Override
        public Iterator< Operation< ? >> iterator() {
            return null;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#operations()
         */
        @Override
        public Operation< ? >[] operations() {
            return null;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#remove(org.polyglotter.transformation.Operation[])
         */
        @Override
        public void remove( final Operation< ? >... operations ) {
            // nothing to do
        }

    };

    public static final String EMPTY_STRING_VALUE = "";
    public static Value< String > EMPTY_STRING_TERM;

    public static final int INT_ZERO_VALUE = 0;
    public static Value< Integer > INT_ZERO_TERM;

    public static final int INT_1_VALUE = 10;
    public static Value< Integer > INT_1_TERM;

    public static final int INT_2_VALUE = 25;
    public static Value< Integer > INT_2_TERM;

    public static final int INT_3_VALUE = INT_1_VALUE;
    public static Value< Integer > INT_3_TERM;

    public static final int INT_4_VALUE = -18;
    public static Value< Integer > INT_4_TERM;

    public static final double DOUBLE_ZERO_VALUE = 0;
    public static Value< Double > DOUBLE_ZERO_TERM;

    public static final double DOUBLE_1_VALUE = 12.34D;
    public static Value< Double > DOUBLE_1_TERM;

    public static final double DOUBLE_2_VALUE = 56.78D;
    public static Value< Double > DOUBLE_2_TERM;

    public static final double DOUBLE_3_VALUE = DOUBLE_1_VALUE;
    public static Value< Double > DOUBLE_3_TERM;

    public static final double DOUBLE_4_VALUE = -18.9D;
    public static Value< Double > DOUBLE_4_TERM;

    public static final float FLOAT_1_VALUE = -0.12F;
    public static Value< Float > FLOAT_1_TERM;

    public static final float FLOAT_2_VALUE = 0.89F;
    public static Value< Float > FLOAT_2_TERM;

    public static final long LONG_1_VALUE = -45L;
    public static Value< Long > LONG_1_TERM;

    public static final long LONG_2_VALUE = 21L;
    public static Value< Long > LONG_2_TERM;

    public static final String NULL_STRING_VALUE = null;
    public static Value< String > NULL_STRING_TERM;

    public static final String STRING_1_VALUE = "value-1";
    public static Value< String > STRING_1_TERM;

    public static final String STRING_2_VALUE = "value-2";
    public static Value< String > STRING_2_TERM;

    public static final String STRING_3_VALUE = "value-3";
    public static Value< String > STRING_3_TERM;

    public static final String TRANSFORM_ID = "Transform_1";
    public static final String TRANSFORM_2_ID = "Transform_2";

    static {
        try {
            EMPTY_STRING_TERM = TransformationFactory.createValue( STRING_DESCRIPTOR, EMPTY_STRING_VALUE );
            INT_ZERO_TERM = TransformationFactory.createValue( INT_DESCRIPTOR, INT_ZERO_VALUE );
            INT_1_TERM = TransformationFactory.createValue( INT_DESCRIPTOR, INT_1_VALUE );
            INT_2_TERM = TransformationFactory.createValue( INT_DESCRIPTOR, INT_2_VALUE );
            INT_3_TERM = TransformationFactory.createValue( INT_DESCRIPTOR, INT_3_VALUE );
            INT_4_TERM = TransformationFactory.createValue( INT_DESCRIPTOR, INT_4_VALUE );
            DOUBLE_ZERO_TERM = TransformationFactory.createValue( DOUBLE_DESCRIPTOR, DOUBLE_ZERO_VALUE );
            DOUBLE_1_TERM = TransformationFactory.createValue( DOUBLE_DESCRIPTOR, DOUBLE_1_VALUE );
            DOUBLE_2_TERM = TransformationFactory.createValue( DOUBLE_DESCRIPTOR, DOUBLE_2_VALUE );
            DOUBLE_3_TERM = TransformationFactory.createValue( DOUBLE_DESCRIPTOR, DOUBLE_1_VALUE );
            DOUBLE_4_TERM = TransformationFactory.createValue( DOUBLE_DESCRIPTOR, DOUBLE_4_VALUE );
            FLOAT_1_TERM = TransformationFactory.createValue( FLOAT_DESCRIPTOR, FLOAT_1_VALUE );
            FLOAT_2_TERM = TransformationFactory.createValue( FLOAT_DESCRIPTOR, FLOAT_2_VALUE );
            LONG_1_TERM = TransformationFactory.createValue( LONG_DESCRIPTOR, LONG_1_VALUE );
            LONG_2_TERM = TransformationFactory.createValue( LONG_DESCRIPTOR, LONG_2_VALUE );
            NULL_STRING_TERM = TransformationFactory.createValue( STRING_DESCRIPTOR, NULL_STRING_VALUE );
            STRING_1_TERM = TransformationFactory.createValue( STRING_DESCRIPTOR, STRING_1_VALUE );
            STRING_2_TERM = TransformationFactory.createValue( STRING_DESCRIPTOR, STRING_2_VALUE );
            STRING_3_TERM = TransformationFactory.createValue( STRING_DESCRIPTOR, STRING_3_VALUE );
        } catch ( final Exception e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * Returns an integer value by adding up values added with the integer value descriptor.
     * 
     * @see #INT_DESCRIPTOR
     */
    public static class TestOperation extends AbstractOperation< Integer > {

        public TestOperation( final Transformation operationTransformation ) {
            super( TEST_OPERATION_DESCRIPTOR, operationTransformation );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.operation.AbstractOperation#calculate()
         */
        @Override
        protected Integer calculate() {
            int result = 0;

            for ( final Value< ? > integerValue : inputs( INT_DESCRIPTOR.id() ) ) {
                try {
                    result += ( Integer ) integerValue.get();
                } catch ( final PolyglotterException e ) {
                    throw new RuntimeException( e );
                }
            }

            return result;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.operation.AbstractOperation#validate()
         */
        @Override
        protected void validate() {
            // nothing to do
        }

    }

}
