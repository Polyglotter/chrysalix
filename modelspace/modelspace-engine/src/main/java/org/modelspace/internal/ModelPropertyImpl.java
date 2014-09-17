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
package org.modelspace.internal;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.modelspace.ModelProperty;
import org.modelspace.ModelspaceException;
import org.modelspace.ModelspaceI18n;
import org.modelspace.PropertyDescriptor;
import org.modelspace.internal.task.TaskWithResult;
import org.modelspace.internal.task.WriteTask;
import org.modeshape.common.util.CheckArg;

/**
 * The base class for a {@link ModelProperty model property}.
 */
public class ModelPropertyImpl implements ModelProperty {

    private static final Value[] NO_VALUES = new Value[ 0 ];
    private static final String UNABLE_TO_CONSTRUCT_PROPERTY = "Unable to construct model property for parent '%s'";
    private static final String UNABLE_TO_CONVERT_VALUE = "Unable to convert JCR value to type '%d'";

    /**
     * @param value
     *        the JCR value holder (cannot be <code>null</code>)
     * @param propertyType
     *        the required type of the property
     * @return the <code>Object</code> representation of the JCR value (never <code>null</code>)
     * @throws ModelspaceException
     *         if an error occurs
     */
    static Object convert( final Value value,
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
    static Value createValue( final ValueFactory factory,
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
    static Value createValue( final ValueFactory factory,
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

    static Value[] createValues( final ValueFactory factory,
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

    final ModelObjectImpl parent;
    final String path;

    /**
     * @param parent
     *        the property's parent (cannot be <code>null</code>)
     * @param jcrProperty
     *        the JCR property (cannot be <code>null</code>)
     * @throws ModelspaceException
     *         if there is an error constructing the property
     */
    ModelPropertyImpl( final ModelObjectImpl parent,
                       final Property jcrProperty ) throws ModelspaceException {
        CheckArg.isNotNull( parent, "parent" );
        CheckArg.isNotNull( jcrProperty, "jcrProperty" );

        this.parent = parent;

        try {
            this.path = jcrProperty.getPath();
        } catch ( final Exception e ) {
            throw new ModelspaceException( e, ModelspaceI18n.localize( UNABLE_TO_CONSTRUCT_PROPERTY, this.parent.name() ) );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#booleanValue()
     */
    @Override
    public boolean booleanValue() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< Boolean >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public Boolean run( final Session session ) throws Exception {
                return session.getProperty( path ).getBoolean();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#booleanValues()
     */
    @Override
    public boolean[] booleanValues() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< boolean[] >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public boolean[] run( final Session session ) throws Exception {
                final Property property = session.getProperty( path );

                if ( property.isMultiple() ) {
                    final Value[] values = property.getValues();
                    final boolean[] booleanValues = new boolean[ values.length ];
                    int i = 0;

                    for ( final Value value : values ) {
                        booleanValues[ i++ ] = value.getBoolean();
                    }

                    return booleanValues;
                }

                return new boolean[] { property.getBoolean() };
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#dateValue()
     */
    @Override
    public Calendar dateValue() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< Calendar >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public Calendar run( final Session session ) throws Exception {
                return session.getProperty( path ).getDate();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#dateValues()
     */
    @Override
    public Calendar[] dateValues() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< Calendar[] >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public Calendar[] run( final Session session ) throws Exception {
                final Property property = session.getProperty( path );

                if ( property.isMultiple() ) {
                    final Value[] values = property.getValues();
                    final Calendar[] dateValues = new Calendar[ values.length ];
                    int i = 0;

                    for ( final Value value : values ) {
                        dateValues[ i++ ] = value.getDate();
                    }

                    return dateValues;
                }

                return new Calendar[] { property.getDate() };
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#decimalValue()
     */
    @Override
    public BigDecimal decimalValue() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< BigDecimal >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public BigDecimal run( final Session session ) throws Exception {
                return session.getProperty( path ).getDecimal();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#decimalValues()
     */
    @Override
    public BigDecimal[] decimalValues() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< BigDecimal[] >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public BigDecimal[] run( final Session session ) throws Exception {
                final Property property = session.getProperty( path );

                if ( property.isMultiple() ) {
                    final Value[] values = property.getValues();
                    final BigDecimal[] decimalValues = new BigDecimal[ values.length ];
                    int i = 0;

                    for ( final Value value : values ) {
                        decimalValues[ i++ ] = value.getDecimal();
                    }

                    return decimalValues;
                }

                return new BigDecimal[] { property.getDecimal() };
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#descriptor()
     */
    @Override
    public PropertyDescriptor descriptor() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< PropertyDescriptor >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public PropertyDescriptor run( final Session session ) throws Exception {
                final Property property = session.getProperty( path );
                return new PropertyDescriptorImpl( property.getDefinition() );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#doubleValue()
     */
    @Override
    public double doubleValue() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< Double >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public Double run( final Session session ) throws Exception {
                return session.getProperty( path ).getDouble();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#doubleValues()
     */
    @Override
    public double[] doubleValues() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< double[] >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public double[] run( final Session session ) throws Exception {
                final Property property = session.getProperty( path );

                if ( property.isMultiple() ) {
                    final Value[] values = property.getValues();
                    final double[] doubleValues = new double[ values.length ];
                    int i = 0;

                    for ( final Value value : values ) {
                        doubleValues[ i++ ] = value.getDouble();
                    }

                    return doubleValues;
                }

                return new double[] { property.getDouble() };
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#longValue()
     */
    @Override
    public long longValue() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< Long >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public Long run( final Session session ) throws Exception {
                return session.getProperty( path ).getLong();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#longValues()
     */
    @Override
    public long[] longValues() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< long[] >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public long[] run( final Session session ) throws Exception {
                final Property property = session.getProperty( path );

                if ( property.isMultiple() ) {
                    final Value[] values = property.getValues();
                    final long[] longValues = new long[ values.length ];
                    int i = 0;

                    for ( final Value value : values ) {
                        longValues[ i++ ] = value.getLong();
                    }

                    return longValues;
                }

                return new long[] { property.getLong() };
            }
        } );
    }

    ModelspaceImpl modelspace() {
        return this.parent.modelspace;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#set(java.lang.Object[])
     */
    @Override
    public void set( final Object... values ) throws ModelspaceException {
        modelspace().run( new WriteTask() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.WriteTask#run(javax.jcr.Session)
             */
            @Override
            public void run( final Session session ) throws Exception {
                final Property property = session.getProperty( path );

                if ( values == null ) {
                    property.remove();
                } else {
                    final int count = values.length;
                    final boolean multiple = property.isMultiple();
                    final int type = property.getType();

                    if ( count == 0 ) {
                        // remove if property is multi-valued
                        if ( multiple ) {
                            property.remove();
                        } else {
                            // single-valued property
                            throw new ModelspaceException( ModelspaceI18n.localize( ModelObjectImpl.UNABLE_TO_REMOVE_SINGLE_VALUE_PROPERTY_WITH_EMPTY_ARRAY,
                                                                              path,
                                                                              parent.name() ) );
                        }
                    } else if ( count > 1 ) {
                        if ( multiple ) {
                            property.setValue( createValues( session.getValueFactory(), values, type ) );
                        } else {
                            throw new ModelspaceException( ModelspaceI18n.localize( ModelObjectImpl.UNABLE_TO_SET_SINGLE_VALUE_PROPERTY_WITH_MULTIPLE_VALUES,
                                                                              property.getName(),
                                                                              parent.name() ) );
                        }
                    } else {
                        // only one value so set property
                        if ( multiple ) {
                            property.setValue( createValues( session.getValueFactory(), values, type ) );
                        } else {
                            property.setValue( createValue( session.getValueFactory(), values[ 0 ] ) );
                        }
                    }
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#stringValue()
     */
    @Override
    public String stringValue() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< String >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public String run( final Session session ) throws Exception {
                return session.getProperty( path ).getString();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#stringValues()
     */
    @Override
    public String[] stringValues() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< String[] >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public String[] run( final Session session ) throws Exception {
                final Property property = session.getProperty( path );

                if ( property.isMultiple() ) {
                    final Value[] values = property.getValues();
                    final String[] stringValues = new String[ values.length ];
                    int i = 0;

                    for ( final Value value : values ) {
                        stringValues[ i++ ] = value.getString();
                    }

                    return stringValues;
                }

                return new String[] { property.getString() };
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.path;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#value()
     */
    @Override
    public Object value() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< Object >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public Object run( final Session session ) throws Exception {
                final Property property = session.getProperty( path );
                final Value value = property.getValue();
                final int propType = property.getType();

                return convert( value, propType );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelProperty#values()
     */
    @Override
    public Object[] values() throws ModelspaceException {
        return modelspace().run( new TaskWithResult< Object[] >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public Object[] run( final Session session ) throws Exception {
                final Property property = session.getProperty( path );
                final int propType = property.getType();

                if ( property.isMultiple() ) {
                    final Value[] values = property.getValues();
                    final Object[] objectValues = new Object[ values.length ];
                    int i = 0;

                    for ( final Value value : values ) {
                        objectValues[ i++ ] = convert( value, propType );
                    }

                    return objectValues;
                }

                return new Object[] { convert( property.getValue(), propType ) };
            }
        } );
    }

}
