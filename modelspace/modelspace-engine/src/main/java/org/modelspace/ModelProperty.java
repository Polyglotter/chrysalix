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
package org.modelspace;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jcr.PropertyType;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.modeshape.common.util.CheckArg;

/**
 * Represents a {@link ModelObject model object} property.
 */
public interface ModelProperty extends ModelElement {

    /**
     * An empty array of values.
     */
    final Value[] NO_VALUES = new Value[ 0 ];

    /**
     * Message indicating the JCR value could not be converted.
     */
    String UNABLE_TO_CONVERT_VALUE = "Unable to convert JCR value to type '%d'";

    /**
     * An empty array of model properties.
     */
    ModelProperty[] NO_PROPS = {};

    /**
     * @return the value represented as a <code>boolean</code> or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if an error occurs
     */
    boolean booleanValue() throws ModelspaceException;

    /**
     * @return the values represented as <code>boolean</code>s or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if not a multi-value property or if an error occurs
     */
    boolean[] booleanValues() throws ModelspaceException;

    /**
     * @return the value represented as a date or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if an error occurs
     */
    Calendar dateValue() throws ModelspaceException;

    /**
     * @return the values represented as dates or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if not a multi-value property or if an error occurs
     */
    Calendar[] dateValues() throws ModelspaceException;

    /**
     * @return the value represented as a <code>decimal</code> or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if an error occurs
     */
    BigDecimal decimalValue() throws ModelspaceException;

    /**
     * @return the values represented as <code>decimal</code>s or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if not a multi-value property or if an error occurs
     */
    BigDecimal[] decimalValues() throws ModelspaceException;

    /**
     * @return the property descriptor (never <code>null</code>)
     * @throws ModelspaceException
     *         if an error occurs
     */
    PropertyDescriptor descriptor() throws ModelspaceException;

    /**
     * @return the value represented as a <code>double</code> or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if an error occurs
     */
    double doubleValue() throws ModelspaceException;

    /**
     * @return the values represented as <code>double</code>s or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if not a multi-value property or if an error occurs
     */
    double[] doubleValues() throws ModelspaceException;

    /**
     * @return the Long value of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if an error occurs
     */
    long longValue() throws ModelspaceException;

    /**
     * @return the Long values of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if not a multi-value property or if an error occurs
     */
    long[] longValues() throws ModelspaceException;

    /**
     * @return the model object this property belongs to (never <code>null</code>)
     * @throws ModelspaceException
     *         if an error occurs
     */
    ModelObject parent() throws ModelspaceException;

    /**
     * Passing in <code>null</code> will remove the existing property from its node.
     * 
     * @param values
     *        the new value for single-valued properties or the new values for multi-valued properties(can be <code>null</code>)
     * @throws ModelspaceException
     *         if an error occurs
     */
    void set( final Object... values ) throws ModelspaceException;

    /**
     * @return the String value of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if an error occurs
     */
    String stringValue() throws ModelspaceException;

    /**
     * @return the String values of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if not a multi-value property or if an error occurs
     */
    String[] stringValues() throws ModelspaceException;

    /**
     * @return the value of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if an error occurs
     */
    Object value() throws ModelspaceException;

    /**
     * @return the values of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelspaceException
     *         if not a multi-value property or if an error occurs
     */
    Object[] values() throws ModelspaceException;

    /**
     * Utilities for converting model properties from one form to another.
     */
    public class Util {

        /**
         * @param value
         *        the JCR value holder (cannot be <code>null</code>)
         * @param propertyType
         *        the required type of the property
         * @return the <code>Object</code> representation of the JCR value (never <code>null</code>)
         * @throws ModelspaceException
         *         if an error occurs
         */
        public static Object convert( final Value value,
                                      final int propertyType ) throws ModelspaceException {
            try {
                switch ( propertyType ) {
                    case PropertyType.BOOLEAN:
                        return value.getBoolean();
                    case PropertyType.LONG:
                        return value.getLong();
                    case PropertyType.DOUBLE:
                        return value.getDouble();
                    case PropertyType.DATE:
                        return value.getDate();
                    case PropertyType.DECIMAL:
                        return value.getDecimal();
                    default:
                        return value.toString();
                }
            } catch ( final Exception e ) {
                throw new ModelspaceException( e, ModelspaceI18n.localize( UNABLE_TO_CONVERT_VALUE, propertyType ) );
            }
        }

        /**
         * @param factory
         *        the factory used to perform the conversion (cannot be <code>null</code>)
         * @param value
         *        the value being converted to a JCR value holder (cannot be <code>null</code>)
         * @return the JCR value holder (never <code>null</code>)
         */
        public static Value createValue( final ValueFactory factory,
                                         final Object value ) {
            CheckArg.isNotNull( factory, "factory" );
            CheckArg.isNotNull( value, "value" );

            if ( value instanceof Value ) return ( Value ) value;
            if ( value instanceof Boolean ) return factory.createValue( Boolean.class.cast( value ) );
            if ( value instanceof Long ) return factory.createValue( Long.class.cast( value ) );
            if ( value instanceof Double ) return factory.createValue( Double.class.cast( value ) );
            if ( value instanceof Calendar ) return factory.createValue( Calendar.class.cast( value ) );
            if ( value instanceof BigDecimal ) return factory.createValue( BigDecimal.class.cast( value ) );
            return factory.createValue( value.toString() );
        }

        /**
         * @param factory
         *        the factory used to perform the conversion (cannot be <code>null</code>)
         * @param value
         *        the value being converted to a JCR value holder (cannot be <code>null</code>)
         * @param jcrPropType
         *        the JCR {@link PropertyType property type}
         * @return the JCR value holder (never <code>null</code>)
         * @throws Exception
         *         if an error occurs
         */
        public static Value createValue( final ValueFactory factory,
                                         final Object value,
                                         final int jcrPropType ) throws Exception {
            CheckArg.isNotNull( factory, "factory" );
            CheckArg.isNotNull( value, "value" );

            if ( PropertyType.BOOLEAN == jcrPropType ) {
                if ( value instanceof Boolean ) {
                    return factory.createValue( ( Boolean ) value );
                }

                return factory.createValue( Boolean.parseBoolean( value.toString() ) );
            }

            if ( PropertyType.LONG == jcrPropType ) {
                if ( value instanceof Long ) {
                    return factory.createValue( ( Long ) value );
                }

                return factory.createValue( Long.parseLong( value.toString() ) );
            }

            if ( PropertyType.DOUBLE == jcrPropType ) {
                if ( value instanceof Double ) {
                    return factory.createValue( ( Double ) value );
                }

                return factory.createValue( Double.parseDouble( value.toString() ) );
            }

            if ( PropertyType.DATE == jcrPropType ) {
                if ( value instanceof Calendar ) {
                    return factory.createValue( ( Calendar ) value );
                }

                final Calendar calendar = Calendar.getInstance();
                final Date date = DateFormat.getDateInstance().parse( value.toString() );
                calendar.setTime( date );

                return factory.createValue( calendar );
            }

            if ( PropertyType.DECIMAL == jcrPropType ) {
                if ( value instanceof BigDecimal ) {
                    return factory.createValue( ( BigDecimal ) value );
                }

                return factory.createValue( new BigDecimal( value.toString() ) );
            }

            return factory.createValue( value.toString() );
        }

        /**
         * @param factory
         *        the factory used to perform the conversion (cannot be <code>null</code>)
         * @param values
         *        the values being converted to a JCR value holders (cannot be <code>null</code>)
         * @param jcrPropType
         *        the JCR {@link PropertyType property type}
         * @return the JCR value holders (never <code>null</code>)
         * @throws Exception
         *         if an error occurs
         */
        public static Value[] createValues( final ValueFactory factory,
                                            final Object[] values,
                                            final int jcrPropType ) throws Exception {
            final List< Value > result = new ArrayList<>();

            if ( ( values == null ) || ( values.length == 0 ) ) {
                return NO_VALUES;
            }

            for ( final Object value : values ) {
                result.add( createValue( factory, value, jcrPropType ) );
            }

            return result.toArray( new Value[ result.size() ] );
        }
    }

}
