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

import java.util.List;
import java.util.Map;

import org.modelspace.ModelspaceException;

/**
 * Table The relational table class
 */
public class Table extends RelationalObject {

    Table( final IObjectDelegate modelObject ) {
        super( modelObject );
    }

    /**
     * Get the child AccessPatterns
     * 
     * @return AccessPatterns
     * @throws ModelspaceException
     *         if an error occurs
     */
    public List< AccessPattern > getAccessPatterns() throws ModelspaceException {
        return getChildren( AccessPattern.class );
    }

    /**
     * @return cardinality
     * @throws NumberFormatException
     *         if the cardinality value cannot be converted to an integer
     * @throws ModelspaceException
     *         if an error occurs
     */
    public int getCardinality() throws NumberFormatException, ModelspaceException {
        return Integer.parseInt( getPropertyValue( RelationalConstants.TABLE_DDL_OPTION_KEYS.CARDINALITY ).toString() );
    }

    /**
     * Get the Column with the specified name (if it exists)
     * 
     * @param colName
     *        the column name
     * @return Column
     * @throws ModelspaceException
     *         if an error occurs
     */
    public Column getColumn( final String colName ) throws ModelspaceException {
        Column result = null;
        final List< Column > cols = getColumns();
        for ( final Column col : cols ) {
            if ( col.getName().equalsIgnoreCase( colName ) ) {
                result = col;
                break;
            }
        }
        return result;
    }

    /**
     * Get the child Columns
     * 
     * @return Columns
     * @throws ModelspaceException
     *         if an error occurs
     */
    public List< Column > getColumns() throws ModelspaceException {
        return getChildren( Column.class );
    }

    /**
     * Get the child ForeignKeys
     * 
     * @return ForeignKeys
     * @throws ModelspaceException
     *         if an error occurs
     */
    public List< ForeignKey > getForeignKeys() throws ModelspaceException {
        return getChildren( ForeignKey.class );
    }

    /**
     * @return Materialized tablename
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getMaterializedTable() throws ModelspaceException {
        final Object propValue = getPropertyValue( RelationalConstants.TABLE_DDL_OPTION_KEYS.MATERIALIZED_TABLE );
        return propValue == null ? null : propValue.toString();
    }

    /**
     * Get the PrimaryKey
     * 
     * @return PrimaryKey
     * @throws ModelspaceException
     *         if an error occurs
     */
    public PrimaryKey getPrimaryKey() throws ModelspaceException {
        final List< PrimaryKey > pkList = getChildren( PrimaryKey.class );
        return ( pkList.size() != 1 ) ? null : pkList.get( 0 );
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public Map< String, Object > getProperties() throws ModelspaceException {
        final Map< String, Object > props = super.getProperties();

        // Add property values for column
        final String[] propKeys = RelationalConstants.TABLE_DDL_OPTION_KEYS.toArray();
        for ( int i = 0; i < propKeys.length; i++ ) {
            props.put( propKeys[ i ], getPropertyValue( propKeys[ i ] ) );
        }

        return props;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public Object getPropertyValue( final String propertyKey ) throws ModelspaceException {
        Object propertyValue = super.getPropertyValue( propertyKey );

        if ( propertyKey.equals( RelationalConstants.TABLE_DDL_OPTION_KEYS.CARDINALITY ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            return propertyValue == null ? RelationalConstants.TABLE_DEFAULT.CARDINALITY : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.TABLE_DDL_OPTION_KEYS.MATERIALIZED ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            return propertyValue == null ? RelationalConstants.TABLE_DEFAULT.MATERIALIZED : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.TABLE_DDL_OPTION_KEYS.MATERIALIZED_TABLE ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            return propertyValue == null ? RelationalConstants.TABLE_DEFAULT.MATERIALIZED_TABLE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.TABLE_DDL_OPTION_KEYS.UPDATABLE ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            return propertyValue == null ? RelationalConstants.TABLE_DEFAULT.UPDATABLE : propertyValue;
        }
        return propertyValue;
    }

    /**
     * Get the child UniqueConstraints
     * 
     * @return UniqueConstraints
     * @throws ModelspaceException
     *         if an error occurs
     */
    public List< UniqueConstraint > getUniqueConstraints() throws ModelspaceException {
        return getChildren( UniqueConstraint.class );
    }

    /**
     * @return isMaterialized
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isMaterialized() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.TABLE_DDL_OPTION_KEYS.MATERIALIZED ).toString() );
    }

    /**
     * Set the cardinality
     * 
     * @param cardinality
     *        the cardinality
     */
    public void setCardinality( final int cardinality ) {
        setPropertyValue( RelationalConstants.TABLE_DDL_OPTION_KEYS.CARDINALITY, cardinality );
    }

    /**
     * Set isMaterialized
     * 
     * @param isMaterialized
     *        materialized state
     */
    public void setMaterialized( final boolean isMaterialized ) {
        setPropertyValue( RelationalConstants.TABLE_DDL_OPTION_KEYS.MATERIALIZED, isMaterialized );
    }

    /**
     * Set the materialized tablename
     * 
     * @param tablename
     *        the Materialized tablename
     */
    public void setMaterializedTable( final String tablename ) {
        setPropertyValue( RelationalConstants.TABLE_DDL_OPTION_KEYS.MATERIALIZED_TABLE, tablename );
    }

    @Override
    @SuppressWarnings( "javadoc" )
    public boolean setPropertyValue( final String propertyKey,
                                     final Object propValue ) {
        boolean wasSet = super.setPropertyValue( propertyKey, propValue );
        if ( wasSet ) return true;

        // ---------------------------
        // Statement Options
        // ---------------------------
        if ( propertyKey.equals( RelationalConstants.TABLE_DDL_OPTION_KEYS.CARDINALITY ) ) {
            if ( propValue == null || ( ( Integer ) propValue ) == RelationalConstants.TABLE_DEFAULT.CARDINALITY ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.TABLE_DDL_OPTION_KEYS.MATERIALIZED ) ) {
            if ( propValue == null || ( ( Boolean ) propValue ) == RelationalConstants.TABLE_DEFAULT.MATERIALIZED ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.TABLE_DDL_OPTION_KEYS.MATERIALIZED_TABLE ) ) {
            if ( propValue == null || ( ( String ) propValue ).equals( RelationalConstants.TABLE_DEFAULT.MATERIALIZED_TABLE ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.TABLE_DDL_OPTION_KEYS.UPDATABLE ) ) {
            if ( propValue == null || ( ( Boolean ) propValue ) == RelationalConstants.TABLE_DEFAULT.UPDATABLE ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propValue );
            }
        }
        return wasSet;
    }

    /**
     * Set updatable property
     * 
     * @param supportsUpdate
     *        'true' if supportsUpdate, 'false' if not.
     */
    public void setSupportsUpdate( final boolean supportsUpdate ) {
        setPropertyValue( RelationalConstants.TABLE_DDL_OPTION_KEYS.UPDATABLE, Boolean.valueOf( supportsUpdate ) );
    }

    /**
     * @return supportsUpdate
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean supportsUpdate() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.TABLE_DDL_OPTION_KEYS.UPDATABLE ).toString() );
    }

}