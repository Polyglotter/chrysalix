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
 * Column The relational column class
 */
public class Column extends RelationalObject {

    Column( final IObjectDelegate modelObject ) {
        super( modelObject );
    }

    /**
     * @return the characterOctetLength
     * @throws ModelspaceException
     *         if an error occurs
     */
    public int getCharacterOctetLength() throws ModelspaceException {
        return Integer.parseInt( getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CHAR_OCTET_LENGTH ).toString() );
    }

    /**
     * @return the datatype
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getDatatype() throws ModelspaceException {
        final Object propValue = getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.DATATYPE );
        return propValue == null ? null : propValue.toString();
    }

    /**
     * @return datatype
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getDatatypeName() throws ModelspaceException {
        final Object propValue = getPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DATATYPE_NAME );
        return propValue == null ? null : propValue.toString();
    }

    /**
     * @return defaultValue
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getDefaultValue() throws ModelspaceException {
        final Object propValue = getPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DEFAULT_VALUE );
        return propValue == null ? null : propValue.toString();
    }

    /**
     * @return length
     * @throws ModelspaceException
     *         if an error occurs
     */
    public long getLength() throws ModelspaceException {
        return Long.parseLong( getPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.LENGTH ).toString() );
    }

    /**
     * @return maximumValue
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getMaximumValue() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.MAX_VALUE ).toString();
    }

    /**
     * @return minimumValue
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getMinimumValue() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.MIN_VALUE ).toString();
    }

    /**
     * @return nativeType
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getNativeType() throws ModelspaceException {
        final Object propValue = getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.NATIVE_TYPE );
        return propValue == null ? null : propValue.toString();
    }

    /**
     * @return nullable
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getNullable() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.NULLABLE ).toString();
    }

    /**
     * @return nullValueCount
     * @throws ModelspaceException
     *         if an error occurs
     */
    public int getNullValueCount() throws ModelspaceException {
        return Integer.parseInt( getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.NULL_VALUE_COUNT ).toString() );
    }

    /**
     * @return precision
     * @throws NumberFormatException
     *         if precision value cannot be converted to an integer
     * @throws ModelspaceException
     *         if an error occurs
     */
    public int getPrecision() throws NumberFormatException, ModelspaceException {
        return Integer.parseInt( getPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.PRECISION ).toString() );
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public Map< String, Object > getProperties() throws ModelspaceException {
        final Map< String, Object > props = super.getProperties();

        // Add values for column properties
        String[] propKeys = RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.toArray();
        for ( int i = 0; i < propKeys.length; i++ ) {
            props.put( propKeys[ i ], getPropertyValue( propKeys[ i ] ) );
        }

        // Add values for column properties
        propKeys = RelationalConstants.COLUMN_DDL_OPTION_KEYS.toArray();
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

        if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SELECTABLE ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.SELECTABLE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.UPDATABLE ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.UPDATABLE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CURRENCY ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.CURRENCY : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CASE_SENSITIVE ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.CASE_SENSITIVE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SIGNED ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.SIGNED : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.FIXED_LENGTH ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.FIXED_LENGTH : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SEARCHABLE ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.SEARCHABILITY : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.MIN_VALUE ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.MINIMUM_VALUE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.MAX_VALUE ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.MAXIMUM_VALUE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.NATIVE_TYPE ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.NATIVE_TYPE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.NULL_VALUE_COUNT ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.NULL_VALUE_COUNT : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.RADIX ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.RADIX : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CHAR_OCTET_LENGTH ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.CHAR_OCTET_LENGTH : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.NULLABLE ) ) {
            propertyValue = this.delegate.getPropertyValue( StandardDdlLexicon.NULLABLE );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.NULLABLE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.AUTO_INCREMENTED ) ) {
            propertyValue = this.delegate.getPropertyValue( TeiidDdlLexicon.CreateTable.AUTO_INCREMENT );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.AUTO_INCREMENTED : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DATATYPE_NAME ) ) {
            propertyValue = this.delegate.getPropertyValue( StandardDdlLexicon.DATATYPE_NAME );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.DATATYPE_NAME : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.LENGTH ) ) {
            propertyValue = this.delegate.getPropertyValue( StandardDdlLexicon.DATATYPE_LENGTH );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.LENGTH : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.PRECISION ) ) {
            propertyValue = this.delegate.getPropertyValue( StandardDdlLexicon.DATATYPE_PRECISION );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.PRECISION : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.SCALE ) ) {
            propertyValue = this.delegate.getPropertyValue( StandardDdlLexicon.DATATYPE_SCALE );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.SCALE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DEFAULT_VALUE ) ) {
            propertyValue = this.delegate.getPropertyValue( StandardDdlLexicon.DEFAULT_VALUE );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.DEFAULT_VALUE : propertyValue;
        }
        return propertyValue;
    }

    /**
     * @return radix
     * @throws NumberFormatException
     *         if radix value cannot be converted to an integer
     * @throws ModelspaceException
     *         if an error occurs
     */
    public int getRadix() throws NumberFormatException, ModelspaceException {
        return Integer.parseInt( getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.RADIX ).toString() );
    }

    /**
     * @return scale
     * @throws NumberFormatException
     *         if radix value cannot be converted to an integer
     * @throws ModelspaceException
     *         if an error occurs
     */
    public int getScale() throws NumberFormatException, ModelspaceException {
        return Integer.parseInt( getPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.SCALE ).toString() );
    }

    /**
     * @return searchability
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getSearchability() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SEARCHABLE ).toString();
    }

    /**
     * @return autoIncremented
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isAutoIncremented() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.AUTO_INCREMENTED ).toString() );
    }

    /**
     * @return caseSensitive
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isCaseSensitive() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CASE_SENSITIVE ).toString() );
    }

    /**
     * @return currency
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isCurrency() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CURRENCY ).toString() );
    }

    /**
     * @return lengthFixed
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isLengthFixed() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.FIXED_LENGTH ).toString() );
    }

    /**
     * @return selectable
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isSelectable() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SELECTABLE ).toString() );
    }

    /**
     * @return signed
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isSigned() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SIGNED ).toString() );
    }

    /**
     * @return updateable
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isUpdateable() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.UPDATABLE ).toString() );
    }

    /**
     * @param autoIncremented
     *        Sets autoIncremented to the specified value.
     */
    public void setAutoIncremented( final boolean autoIncremented ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.AUTO_INCREMENTED, autoIncremented );
    }

    /**
     * @param caseSensitive
     *        Sets caseSensitive to the specified value.
     */
    public void setCaseSensitive( final boolean caseSensitive ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CASE_SENSITIVE, caseSensitive );
    }

    /**
     * @param characterOctetLength
     *        the characterOctetLength to set
     */
    public void setCharacterOctetLength( final long characterOctetLength ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CHAR_OCTET_LENGTH, characterOctetLength );
    }

    /**
     * @param currency
     *        Sets currency to the specified value.
     */
    public void setCurrency( final boolean currency ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CURRENCY, currency );
    }

    /**
     * @param datatype
     *        the datatype
     */
    public void setDatatype( final String datatype ) {
        // ArgCheck.isNotNull(datatype);
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.DATATYPE, datatype );
    }

    /**
     * @param typeName
     *        Sets datatype to the specified value.
     */
    public void setDatatypeName( final String typeName ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DATATYPE_NAME, typeName );
    }

    /**
     * @param defaultValue
     *        Sets defaultValue to the specified value.
     */
    public void setDefaultValue( final String defaultValue ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DEFAULT_VALUE, defaultValue );
    }

    /**
     * @param length
     *        Sets length to the specified value.
     */
    public void setLength( final long length ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.LENGTH, length );
    }

    /**
     * @param lengthFixed
     *        Sets lengthFixed to the specified value.
     */
    public void setLengthFixed( final boolean lengthFixed ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.FIXED_LENGTH, lengthFixed );
    }

    /**
     * @param maximumValue
     *        Sets maximumValue to the specified value.
     */
    public void setMaximumValue( final String maximumValue ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.MAX_VALUE, maximumValue );
    }

    /**
     * @param minimumValue
     *        Sets minimumValue to the specified value.
     */
    public void setMinimumValue( final String minimumValue ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.MIN_VALUE, minimumValue );
    }

    /**
     * @param nativeType
     *        Sets nativeType to the specified value.
     */
    public void setNativeType( final String nativeType ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.NATIVE_TYPE, nativeType );
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
                setPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.NULLABLE, allowedValues[ i ] );
                matchFound = true;
            }
        }
        // if(!matchFound) throw new
        // IllegalArgumentException(Messages.getString(RELATIONAL.columnError_Nullable_NotAllowable,nullable));
    }

    /**
     * @param nullValueCount
     *        Sets nullValueCount to the specified value.
     */
    public void setNullValueCount( final long nullValueCount ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.NULL_VALUE_COUNT, nullValueCount );
    }

    /**
     * @param precision
     *        Sets precision to the specified value.
     */
    public void setPrecision( final long precision ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.PRECISION, precision );
    }

    @Override
    @SuppressWarnings( "javadoc" )
    public boolean setPropertyValue( final String propertyKey,
                                     final Object propertyValue ) {
        boolean wasSet = super.setPropertyValue( propertyKey, propertyValue );
        if ( wasSet ) return true;

        // ---------------------------
        // Statement Options
        // ---------------------------
        if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SELECTABLE ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.COLUMN_DEFAULT.SELECTABLE ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.UPDATABLE ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.COLUMN_DEFAULT.UPDATABLE ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CURRENCY ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.COLUMN_DEFAULT.CURRENCY ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CASE_SENSITIVE ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.COLUMN_DEFAULT.CASE_SENSITIVE ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SIGNED ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.COLUMN_DEFAULT.SIGNED ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.FIXED_LENGTH ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.COLUMN_DEFAULT.FIXED_LENGTH ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SEARCHABLE ) ) {
            if ( propertyValue == null || ( ( String ) propertyValue ).equals( RelationalConstants.COLUMN_DEFAULT.SEARCHABILITY ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.MIN_VALUE ) ) {
            if ( propertyValue == null || ( ( String ) propertyValue ).equals( RelationalConstants.COLUMN_DEFAULT.MINIMUM_VALUE ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.MAX_VALUE ) ) {
            if ( propertyValue == null || ( ( String ) propertyValue ).equals( RelationalConstants.COLUMN_DEFAULT.MAXIMUM_VALUE ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.NATIVE_TYPE ) ) {
            if ( propertyValue == null || ( ( String ) propertyValue ).equals( RelationalConstants.COLUMN_DEFAULT.NATIVE_TYPE ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.NULL_VALUE_COUNT ) ) {
            if ( propertyValue == null || ( ( Long ) propertyValue ) == RelationalConstants.COLUMN_DEFAULT.NULL_VALUE_COUNT ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.RADIX ) ) {
            if ( propertyValue == null || ( ( Long ) propertyValue ) == RelationalConstants.COLUMN_DEFAULT.RADIX ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CHAR_OCTET_LENGTH ) ) {
            if ( propertyValue == null || ( ( Long ) propertyValue ) == RelationalConstants.COLUMN_DEFAULT.CHAR_OCTET_LENGTH ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
            // ---------------------------
            // Properties
            // ---------------------------
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.NULLABLE ) ) {
            final String pVal = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.NULLABLE : ( String ) propertyValue;
            wasSet = this.delegate.setPropertyValue( StandardDdlLexicon.NULLABLE, pVal );
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.AUTO_INCREMENTED ) ) {
            final Boolean pVal =
                ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.AUTO_INCREMENTED : ( Boolean ) propertyValue;
            wasSet = this.delegate.setPropertyValue( TeiidDdlLexicon.CreateTable.AUTO_INCREMENT, pVal );
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DATATYPE_NAME ) ) {
            final String pVal =
                ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.DATATYPE_NAME : ( String ) propertyValue;
            wasSet = this.delegate.setPropertyValue( StandardDdlLexicon.DATATYPE_NAME, pVal );
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.LENGTH ) ) {
            final Long pVal = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.LENGTH : ( Long ) propertyValue;
            wasSet = this.delegate.setPropertyValue( StandardDdlLexicon.DATATYPE_LENGTH, pVal );
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.PRECISION ) ) {
            final Long pVal = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.PRECISION : ( Long ) propertyValue;
            wasSet = this.delegate.setPropertyValue( StandardDdlLexicon.DATATYPE_PRECISION, pVal );
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.SCALE ) ) {
            final Long pVal = ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.SCALE : ( Long ) propertyValue;
            wasSet = this.delegate.setPropertyValue( StandardDdlLexicon.DATATYPE_SCALE, pVal );
        } else if ( propertyKey.equals( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DEFAULT_VALUE ) ) {
            final String pVal =
                ( propertyValue == null ) ? RelationalConstants.COLUMN_DEFAULT.DEFAULT_VALUE : ( String ) propertyValue;
            wasSet = this.delegate.setPropertyValue( StandardDdlLexicon.DEFAULT_VALUE, pVal );
        }
        return wasSet;
    }

    /**
     * @param radix
     *        Sets radix to the specified value.
     */
    public void setRadix( final long radix ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.RADIX, radix );
    }

    /**
     * @param scale
     *        Sets scale to the specified value.
     */
    public void setScale( final long scale ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.SCALE, scale );
    }

    /**
     * @param searchability
     *        Sets searchability to the specified value.
     */
    public void setSearchability( final String searchability ) {
        // ArgCheck.isNotEmpty(searchability);
        final String[] allowedValues = RelationalConstants.SEARCHABILITY_OPTIONS.AS_ARRAY;
        boolean matchFound = false;
        for ( int i = 0; i < allowedValues.length; i++ ) {
            if ( allowedValues[ i ].equalsIgnoreCase( searchability ) ) {
                setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SEARCHABLE, allowedValues[ i ] );
                matchFound = true;
            }
        }
        // if(!matchFound) throw new
        // IllegalArgumentException(Messages.getString(RELATIONAL.columnError_Searchability_NotAllowable,searchability));
    }

    /**
     * @param selectable
     *        Sets selectable to the specified value.
     */
    public void setSelectable( final boolean selectable ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SELECTABLE, selectable );
    }

    /**
     * @param signed
     *        Sets signed to the specified value.
     */
    public void setSigned( final boolean signed ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SIGNED, signed );
    }

    /**
     * @param updateable
     *        Sets updateable to the specified value.
     */
    public void setUpdateable( final boolean updateable ) {
        setPropertyValue( RelationalConstants.COLUMN_DDL_OPTION_KEYS.UPDATABLE, updateable );
    }

}
