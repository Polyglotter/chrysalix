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
package org.modelspace.ddl.relational;

import java.util.Map;

import org.modelspace.ModelspaceException;

/**
 * Parameter The relational parameter class
 */
public class Parameter extends RelationalObject {

    Parameter( final IObjectDelegate modelObject ) {
        super( modelObject );
    }

    /**
     * @return datatype
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getDatatypeName() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME ).toString();
    }

    /**
     * @return defaultValue
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getDefaultValue() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DEFAULT_VALUE ).toString();
    }

    /**
     * @return direction
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getDirection() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DIRECTION ).toString();
    }

    /**
     * @return length
     * @throws NumberFormatException
     *         if length cannot be converted to a long
     * @throws ModelspaceException
     *         if an error occurs
     */
    public long getLength() throws NumberFormatException, ModelspaceException {
        return Long.parseLong( getPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.LENGTH ).toString() );
    }

    /**
     * @return nativeType
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getNativeType() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.PARAMETER_DDL_OPTION_KEYS.NATIVE_TYPE ).toString();
    }

    /**
     * @return nullable
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getNullable() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.NULLABLE ).toString();
    }

    /**
     * @return precision
     * @throws NumberFormatException
     *         if precision cannot be converted to an integer
     * @throws ModelspaceException
     *         if an error occurs
     */
    public int getPrecision() throws NumberFormatException, ModelspaceException {
        return Integer.parseInt( getPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.PRECISION ).toString() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ddl.relational.RelationalObject#getProperties()
     */
    @Override
    public Map< String, Object > getProperties() throws ModelspaceException {
        final Map< String, Object > props = super.getProperties();

        // Add values for parameter properties
        String[] propKeys = RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.toArray();
        for ( int i = 0; i < propKeys.length; i++ ) {
            props.put( propKeys[ i ], getPropertyValue( propKeys[ i ] ) );
        }

        // Add values for column options
        propKeys = RelationalConstants.PARAMETER_DDL_OPTION_KEYS.toArray();
        for ( int i = 0; i < propKeys.length; i++ ) {
            props.put( propKeys[ i ], getPropertyValue( propKeys[ i ] ) );
        }

        return props;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ddl.relational.RelationalObject#getPropertyValue(java.lang.String)
     */
    @Override
    public Object getPropertyValue( final String propertyKey ) throws ModelspaceException {
        Object propertyValue = super.getPropertyValue( propertyKey );

        if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.LENGTH ) ) {
            propertyValue = this.delegate.getPropertyValue( StandardDdlLexicon.DATATYPE_LENGTH );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.LENGTH : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME ) ) {
            propertyValue = this.delegate.getPropertyValue( StandardDdlLexicon.DATATYPE_NAME );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.DATATYPE_NAME : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DEFAULT_VALUE ) ) {
            propertyValue = this.delegate.getPropertyValue( StandardDdlLexicon.DEFAULT_VALUE );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.DEFAULT_VALUE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.NULLABLE ) ) {
            propertyValue = this.delegate.getPropertyValue( StandardDdlLexicon.NULLABLE );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.NULLABLE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.PRECISION ) ) {
            propertyValue = this.delegate.getPropertyValue( StandardDdlLexicon.DATATYPE_PRECISION );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.PRECISION : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.SCALE ) ) {
            propertyValue = this.delegate.getPropertyValue( StandardDdlLexicon.DATATYPE_SCALE );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.SCALE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DIRECTION ) ) {
            propertyValue = this.delegate.getPropertyValue( TeiidDdlLexicon.CreateProcedure.PARAMETER_TYPE );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.DIRECTION : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_OPTION_KEYS.NATIVE_TYPE ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.NATIVE_TYPE : propertyValue;
        }
        return propertyValue;
    }

    /**
     * @return scale
     * @throws NumberFormatException
     *         if scale value cannot be converted to an integer
     * @throws ModelspaceException
     *         if an error occurs
     */
    public int getScale() throws ModelspaceException, NumberFormatException {
        return Integer.parseInt( getPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.SCALE ).toString() );
    }

    /**
     * Set the datatype name
     * 
     * @param datatype
     *        the datatype name
     */
    public void setDatatypeName( final String datatype ) {
        setPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME, datatype );
    }

    /**
     * @param defaultValue
     *        the default value
     */
    public void setDefaultValue( final String defaultValue ) {
        setPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DEFAULT_VALUE, defaultValue );
    }

    /**
     * @param direction
     *        Sets direction to the specified value.
     */
    public void setDirection( final String direction ) {
        // ArgCheck.isNotEmpty(direction);
        final String[] allowedValues = RelationalConstants.DIRECTION_OPTIONS.AS_ARRAY;
        boolean matchFound = false;
        for ( int i = 0; i < allowedValues.length; i++ ) {
            if ( allowedValues[ i ].equalsIgnoreCase( direction ) ) {
                setPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DIRECTION, allowedValues[ i ] );
                matchFound = true;
            }
        }
        // if(!matchFound) throw new
        // IllegalArgumentException(Messages.getString(RELATIONAL.parameterError_Direction_NotAllowable,direction));
    }

    /**
     * @param length
     *        the length
     */
    public void setLength( final long length ) {
        setPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.LENGTH, length );
    }

    /**
     * Set the nativeType
     * 
     * @param nativeType
     *        the native type
     */
    public void setNativeType( final String nativeType ) {
        setPropertyValue( RelationalConstants.PARAMETER_DDL_OPTION_KEYS.NATIVE_TYPE, nativeType );
    }

    /**
     * @param nullable
     *        Sets nullable to the specified value.
     */
    public void setNullable( final String nullable ) {
        // ArgCheck.isNotEmpty(nullable);
        final String[] allowedValues = RelationalConstants.NULLABLE_OPTIONS.AS_ARRAY;
        boolean matchFound = false;
        for ( int i = 0; i < allowedValues.length; i++ ) {
            if ( allowedValues[ i ].equalsIgnoreCase( nullable ) ) {
                setPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.NULLABLE, allowedValues[ i ] );
                matchFound = true;
            }
        }
        // if(!matchFound) throw new
        // IllegalArgumentException(Messages.getString(RELATIONAL.columnError_Nullable_NotAllowable,nullable));
    }

    /**
     * @param precision
     *        the precision
     */
    public void setPrecision( final long precision ) {
        setPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.PRECISION, precision );
    }

    @Override
    @SuppressWarnings( "javadoc" )
    public boolean setPropertyValue( final String propertyKey,
                                     final Object propertyValue ) {
        boolean wasSet = super.setPropertyValue( propertyKey, propertyValue );
        if ( wasSet ) return true;

        // ---------------------------
        // Properties
        // ---------------------------
        if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.LENGTH ) ) {
            final Long pVal = ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.LENGTH : ( Long ) propertyValue;
            wasSet = this.delegate.setPropertyValue( StandardDdlLexicon.DATATYPE_LENGTH, pVal );
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME ) ) {
            final String pVal =
                ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.DATATYPE_NAME : ( String ) propertyValue;
            wasSet = this.delegate.setPropertyValue( StandardDdlLexicon.DATATYPE_NAME, pVal );
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DEFAULT_VALUE ) ) {
            final String pVal =
                ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.DEFAULT_VALUE : ( String ) propertyValue;
            wasSet = this.delegate.setPropertyValue( StandardDdlLexicon.DEFAULT_VALUE, pVal );
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.NULLABLE ) ) {
            final String pVal =
                ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.NULLABLE : ( String ) propertyValue;
            wasSet = this.delegate.setPropertyValue( StandardDdlLexicon.NULLABLE, pVal );
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.PRECISION ) ) {
            final Long pVal = ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.PRECISION : ( Long ) propertyValue;
            wasSet = this.delegate.setPropertyValue( StandardDdlLexicon.DATATYPE_PRECISION, pVal );
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.SCALE ) ) {
            final Long pVal = ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.SCALE : ( Long ) propertyValue;
            wasSet = this.delegate.setPropertyValue( StandardDdlLexicon.DATATYPE_SCALE, pVal );
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DIRECTION ) ) {
            final String pVal =
                ( propertyValue == null ) ? RelationalConstants.PARAMETER_DEFAULT.DIRECTION : ( String ) propertyValue;
            wasSet = this.delegate.setPropertyValue( TeiidDdlLexicon.CreateProcedure.PARAMETER_TYPE, pVal );
            // ---------------------------
            // Statement Options
            // ---------------------------
        } else if ( propertyKey.equals( RelationalConstants.PARAMETER_DDL_OPTION_KEYS.NATIVE_TYPE ) ) {
            if ( propertyValue == null || ( ( String ) propertyValue ).equals( RelationalConstants.PARAMETER_DEFAULT.NATIVE_TYPE ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        }
        return wasSet;
    }

    /**
     * @param scale
     *        the scale
     */
    public void setScale( final long scale ) {
        setPropertyValue( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.SCALE, scale );
    }

    // /**
    // * Set properties
    // * @param props the properties
    // */
    // @Override
    // public void setProperties(Properties props) {
    // // Set common properties
    // super.setProperties(props);
    //
    // for( Object key : props.keySet() ) {
    // String keyStr = (String)key;
    // String value = props.getProperty(keyStr);
    //
    // if( value != null && value.length() == 0 ) {
    // continue;
    // }
    //
    // if(keyStr.equalsIgnoreCase(KEY_LENGTH) ) {
    // setLength(Integer.parseInt(value));
    // } else if(keyStr.equalsIgnoreCase(KEY_DATATYPE) ) {
    // setDatatypeName(value);
    // } else if(keyStr.equalsIgnoreCase(KEY_DEFAULT_VALUE) ) {
    // setDefaultValue(value);
    // } else if(keyStr.equalsIgnoreCase(KEY_DIRECTION) ) {
    // setDirection(value);
    // } else if(keyStr.equalsIgnoreCase(KEY_NATIVE_TYPE) ) {
    // setNativeType(value);
    // } else if(keyStr.equalsIgnoreCase(KEY_NULLABLE) ) {
    // setNullable(value);
    // } else if(keyStr.equalsIgnoreCase(KEY_PRECISION) ) {
    // setPrecision(Integer.parseInt(value));
    // } else if(keyStr.equalsIgnoreCase(KEY_SCALE) ) {
    // setScale(Integer.parseInt(value));
    // } else if(keyStr.equalsIgnoreCase(KEY_RADIX) ) {
    // setRadix(Integer.parseInt(value));
    // }
    // }
    //
    // handleInfoChanged();
    // }
    //
    // /**
    // * {@inheritDoc}
    // *
    // * @see java.lang.Object#equals(java.lang.Object)
    // */
    // @Override
    // public boolean equals( final Object object ) {
    // if (!super.equals(object)) {
    // return false;
    // }
    // if (this == object)
    // return true;
    // if (object == null)
    // return false;
    // if (getClass() != object.getClass())
    // return false;
    // final Parameter other = (Parameter)object;
    //
    // // string properties
    // if (!StringUtil.valuesAreEqual(getDatatypeName(), other.getDatatypeName()) ||
    // !StringUtil.valuesAreEqual(getDefaultValue(), other.getDefaultValue()) ||
    // !StringUtil.valuesAreEqual(getDirection(), other.getDirection()) ||
    // !StringUtil.valuesAreEqual(getNativeType(), other.getNativeType()) ||
    // !StringUtil.valuesAreEqual(getNullable(), other.getNullable()) ) {
    // return false;
    // }
    //
    // if( !(getLength()==other.getLength()) ||
    // !(getPrecision()==other.getPrecision()) ||
    // !(getRadix()==other.getRadix()) ||
    // !(getScale()==other.getScale()) ) {
    // return false;
    // }
    //
    // return true;
    // }
    //
    // /**
    // * {@inheritDoc}
    // *
    // * @see java.lang.Object#hashCode()
    // */
    // @Override
    // public int hashCode() {
    // int result = super.hashCode();
    //
    // // string properties
    // if (!StringUtil.isEmpty(getDatatypeName())) {
    // result = HashCodeUtil.hashCode(result, getDatatype());
    // }
    // if (!StringUtil.isEmpty(getDefaultValue())) {
    // result = HashCodeUtil.hashCode(result, getDefaultValue());
    // }
    // if (!StringUtil.isEmpty(getDirection())) {
    // result = HashCodeUtil.hashCode(result, getDirection());
    // }
    // if (!StringUtil.isEmpty(getNativeType())) {
    // result = HashCodeUtil.hashCode(result, getNativeType());
    // }
    // if (!StringUtil.isEmpty(getNullable())) {
    // result = HashCodeUtil.hashCode(result, getNullable());
    // }
    //
    // result = HashCodeUtil.hashCode(result, getLength());
    // result = HashCodeUtil.hashCode(result, getPrecision());
    // result = HashCodeUtil.hashCode(result, getRadix());
    // result = HashCodeUtil.hashCode(result, getScale());
    //
    // return result;
    // }

}
