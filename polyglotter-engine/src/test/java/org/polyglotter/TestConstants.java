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
package org.polyglotter;

import javax.xml.namespace.QName;

import org.polyglotter.common.PolyglotterException;
import org.polyglotter.grammar.GrammarFactory;
import org.polyglotter.grammar.GrammarPart;
import org.polyglotter.grammar.Operation;
import org.polyglotter.grammar.Operation.Category;
import org.polyglotter.grammar.Term;

@SuppressWarnings( "javadoc" )
public class TestConstants {

    public static final QName ID = new QName( GrammarPart.NAMESPACE_URI, "Test_ID_1", GrammarPart.NAMESPACE_PREFIX );
    public static final QName ID_2 = new QName( GrammarPart.NAMESPACE_URI, "Test_ID_2", GrammarPart.NAMESPACE_PREFIX );

    public static final QName OP_ID = new QName( GrammarPart.NAMESPACE_URI, "TestOp_1_ID", GrammarPart.NAMESPACE_PREFIX );
    public static final QName OP_2_ID = new QName( GrammarPart.NAMESPACE_URI, "TestOp_2_ID", GrammarPart.NAMESPACE_PREFIX );

    public static final QName EMPTY_STRING_ID = new QName( ID.getNamespaceURI(), "EmptyString", ID.getPrefix() );
    public static final String EMPTY_STRING_VALUE = "";
    public static Term< ? > EMPTY_STRING_TERM;

    public static final QName INT_ZERO_ID = new QName( ID.getNamespaceURI(), "IntZero", ID.getPrefix() );
    public static final int INT_ZERO_VALUE = 0;
    public static Term< ? > INT_ZERO_TERM;

    public static final QName INT_1_ID = new QName( ID.getNamespaceURI(), "Int1", ID.getPrefix() );
    public static final int INT_1_VALUE = 10;
    public static Term< Number > INT_1_TERM;

    public static final QName INT_2_ID = new QName( ID.getNamespaceURI(), "Int2", ID.getPrefix() );
    public static final int INT_2_VALUE = 25;
    public static Term< Number > INT_2_TERM;

    /**
     * Same value as INT_1
     */
    public static final QName INT_3_ID = new QName( ID.getNamespaceURI(), "Int3", ID.getPrefix() );
    public static final int INT_3_VALUE = INT_1_VALUE;
    public static Term< Number > INT_3_TERM;

    public static final QName INT_4_ID = new QName( ID.getNamespaceURI(), "Int4", ID.getPrefix() );
    public static final int INT_4_VALUE = -18;
    public static Term< Number > INT_4_TERM;

    public static final QName DOUBLE_ZERO_ID = new QName( ID.getNamespaceURI(), "DoubleZero", ID.getPrefix() );
    public static final double DOUBLE_ZERO_VALUE = 0;
    public static Term< Number > DOUBLE_ZERO_TERM;

    public static final QName DOUBLE_1_ID = new QName( ID.getNamespaceURI(), "Double1", ID.getPrefix() );
    public static final double DOUBLE_1_VALUE = 12.34D;
    public static Term< Number > DOUBLE_1_TERM;

    public static final QName DOUBLE_2_ID = new QName( ID.getNamespaceURI(), "Double2", ID.getPrefix() );
    public static final double DOUBLE_2_VALUE = 56.78D;
    public static Term< Number > DOUBLE_2_TERM;

    /**
     * Same value as DOUBLE_1
     */
    public static final QName DOUBLE_3_ID = new QName( ID.getNamespaceURI(), "Double3", ID.getPrefix() );
    public static final double DOUBLE_3_VALUE = DOUBLE_1_VALUE;
    public static Term< Number > DOUBLE_3_TERM;

    public static final QName DOUBLE_4_ID = new QName( ID.getNamespaceURI(), "Double4", ID.getPrefix() );
    public static final double DOUBLE_4_VALUE = -18.9D;
    public static Term< Number > DOUBLE_4_TERM;

    public static final QName FLOAT_1_ID = new QName( ID.getNamespaceURI(), "Float1", ID.getPrefix() );
    public static final float FLOAT_1_VALUE = -0.12F;
    public static Term< Number > FLOAT_1_TERM;

    public static final QName FLOAT_2_ID = new QName( ID.getNamespaceURI(), "Float2", ID.getPrefix() );
    public static final float FLOAT_2_VALUE = 0.89F;
    public static Term< Number > FLOAT_2_TERM;

    public static final QName LONG_1_ID = new QName( ID.getNamespaceURI(), "Long1", ID.getPrefix() );
    public static final long LONG_1_VALUE = -45L;
    public static Term< Number > LONG_1_TERM;

    public static final QName LONG_2_ID = new QName( ID.getNamespaceURI(), "Long2", ID.getPrefix() );
    public static final long LONG_2_VALUE = 21L;
    public static Term< Number > LONG_2_TERM;

    public static final QName NULL_STRING_ID = new QName( ID.getNamespaceURI(), "NullString", ID.getPrefix() );
    public static final String NULL_STRING_VALUE = null;
    public static Term< String > NULL_STRING_TERM;

    public static final QName STRING_1_ID = new QName( ID.getNamespaceURI(), "String1", ID.getPrefix() );
    public static final String STRING_1_VALUE = "value-1";
    public static Term< String > STRING_1_TERM;

    public static final QName STRING_2_ID = new QName( ID.getNamespaceURI(), "String2", ID.getPrefix() );
    public static final String STRING_2_VALUE = "value-2";
    public static Term< String > STRING_2_TERM;

    public static final QName STRING_3_ID = new QName( ID.getNamespaceURI(), "String3", ID.getPrefix() );
    public static final String STRING_3_VALUE = "value-3";
    public static Term< String > STRING_3_TERM;

    public static final QName TRANSFORM_ID =
        new QName( GrammarPart.NAMESPACE_URI, "Transform_1", GrammarPart.NAMESPACE_PREFIX );
    public static final QName TRANSFORM_2_ID =
        new QName( GrammarPart.NAMESPACE_URI, "Transform_2", GrammarPart.NAMESPACE_PREFIX );

    public static final Operation.Descriptor DESCRIPTOR = new Operation.Descriptor() {

        @Override
        public String abbreviation() {
            return "abbreviation";
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

    };

    static {
        try {
            EMPTY_STRING_TERM = GrammarFactory.createStringTerm( EMPTY_STRING_ID, EMPTY_STRING_VALUE );

            INT_ZERO_TERM = GrammarFactory.createNumberTerm( INT_ZERO_ID, INT_ZERO_VALUE );
            INT_1_TERM = GrammarFactory.createNumberTerm( INT_1_ID, INT_1_VALUE );
            INT_2_TERM = GrammarFactory.createNumberTerm( INT_2_ID, INT_2_VALUE );
            INT_3_TERM = GrammarFactory.createNumberTerm( INT_3_ID, INT_3_VALUE );
            INT_4_TERM = GrammarFactory.createNumberTerm( INT_4_ID, INT_4_VALUE );

            DOUBLE_ZERO_TERM = GrammarFactory.createNumberTerm( DOUBLE_ZERO_ID, DOUBLE_ZERO_VALUE );
            DOUBLE_1_TERM = GrammarFactory.createNumberTerm( DOUBLE_1_ID, DOUBLE_1_VALUE );
            DOUBLE_2_TERM = GrammarFactory.createNumberTerm( DOUBLE_2_ID, DOUBLE_2_VALUE );
            DOUBLE_3_TERM = GrammarFactory.createNumberTerm( DOUBLE_3_ID, DOUBLE_3_VALUE );
            DOUBLE_4_TERM = GrammarFactory.createNumberTerm( DOUBLE_4_ID, DOUBLE_4_VALUE );

            FLOAT_1_TERM = GrammarFactory.createNumberTerm( FLOAT_1_ID, FLOAT_1_VALUE );
            FLOAT_2_TERM = GrammarFactory.createNumberTerm( FLOAT_2_ID, FLOAT_2_VALUE );

            LONG_1_TERM = GrammarFactory.createNumberTerm( LONG_1_ID, LONG_1_VALUE );
            LONG_2_TERM = GrammarFactory.createNumberTerm( LONG_2_ID, LONG_2_VALUE );

            NULL_STRING_TERM = GrammarFactory.createStringTerm( NULL_STRING_ID, NULL_STRING_VALUE );
            STRING_1_TERM = GrammarFactory.createStringTerm( STRING_1_ID, STRING_1_VALUE );
            STRING_2_TERM = GrammarFactory.createStringTerm( STRING_2_ID, STRING_2_VALUE );
            STRING_3_TERM = GrammarFactory.createStringTerm( STRING_3_ID, STRING_3_VALUE );
        } catch ( final PolyglotterException e ) {
            throw new RuntimeException( e );
        }
    }

}
