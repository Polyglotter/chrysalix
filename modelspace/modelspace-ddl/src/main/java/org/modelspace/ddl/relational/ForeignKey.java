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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelspace.ModelspaceException;

/**
 * ForeignKey The relational foreign key class
 */
public class ForeignKey extends RelationalObject {

    ForeignKey( final IObjectDelegate modelObject ) {
        super( modelObject );
    }

    /**
     * @return columns
     * @throws ModelspaceException
     *         if an error occurs
     */
    public List< String > getColumnRefs() throws ModelspaceException {
        final Object[] refs = this.delegate.getPropertyValues( TeiidDdlLexicon.Constraint.REFERENCES );
        final List< String > colRefs = new ArrayList< String >( refs.length );
        for ( final Object ref : refs ) {
            colRefs.add( ref.toString() );
        }
        return colRefs;
    }

    /**
     * Get the properties for this object
     * 
     * @return the properties
     * @throws ModelspaceException
     *         if an error occurs
     */
    @Override
    public Map< String, Object > getProperties() throws ModelspaceException {
        final Map< String, Object > props = super.getProperties();

        // Add property values for column
        final String[] propKeys = RelationalConstants.FOREIGN_KEY_PROP_KEYS.toArray();
        for ( int i = 0; i < propKeys.length; i++ ) {
            props.put( propKeys[ i ], getPropertyValue( propKeys[ i ] ) );
        }

        // props.put(RelationalConstants.FOREIGN_KEY_PROP_KEYS.FOREIGN_KEY_MULTIPLICITY,
        // getPropertyValue(RelationalConstants.FOREIGN_KEY_PROP_KEYS.FOREIGN_KEY_MULTIPLICITY));
        // props.put(RelationalConstants.FOREIGN_KEY_PROP_KEYS.PRIMARY_KEY_MULTIPLICITY,
        // getPropertyValue(RelationalConstants.FOREIGN_KEY_PROP_KEYS.PRIMARY_KEY_MULTIPLICITY));
        // props.put(RelationalConstants.FOREIGN_KEY_PROP_KEYS.UNIQUE_KEY_NAME,
        // getPropertyValue(RelationalConstants.FOREIGN_KEY_PROP_KEYS.UNIQUE_KEY_NAME));
        // props.put(RelationalConstants.FOREIGN_KEY_PROP_KEYS.UNIQUE_KEY_TABLE_NAME,
        // getPropertyValue(RelationalConstants.FOREIGN_KEY_PROP_KEYS.UNIQUE_KEY_TABLE_NAME));

        return props;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ddl.relational.RelationalObject#getPropertyValue(java.lang.String)
     */
    @Override
    public Object getPropertyValue( final String propertyKey ) throws ModelspaceException {
        final Object propertyValue = super.getPropertyValue( propertyKey );

        // if(propertyKey.equals(RelationalConstants.COLUMN_PROP_KEYS.SELECTABLE)) {
        // propertyValue = this.delegate.getOptionValue(TeiidDDLConstants.ColumnOptions.SELECTABLE);
        // } else if(propertyKey.equals(RelationalConstants.COLUMN_PROP_KEYS.DEFAULT_VALUE)) {
        // return this.delegate.getPropertyValue(StandardDdlLexicon.DEFAULT_VALUE);
        // }
        return propertyValue;
    }

    /**
     * @return columns
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getTableRef() throws ModelspaceException {
        final Object propValue = this.delegate.getPropertyValue( TeiidDdlLexicon.Constraint.TABLE_REFERENCE );
        return ( propValue == null ) ? null : propValue.toString();
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
    // if(keyStr.equalsIgnoreCase(KEY_FOREIGN_KEY_MULTIPLICITY) ) {
    // setForeignKeyMultiplicity(value);
    // } else if(keyStr.equalsIgnoreCase(KEY_PRIMARY_KEY_MULTIPLICITY) ) {
    // setPrimaryKeyMultiplicity(value);
    // } else if(keyStr.equalsIgnoreCase(KEY_UNIQUE_KEY_NAME) ) {
    // setUniqueKeyName(value);
    // } else if(keyStr.equalsIgnoreCase(KEY_UNIQUE_KEY_TABLE_NAME) ) {
    // setUniqueKeyTableName(value);
    // }
    // }
    //
    // handleInfoChanged();
    // }
    //
    // @Override
    // public void handleInfoChanged() {
    // super.handleInfoChanged();
    //
    // // Set extension properties here
    //
    // if( !this.allowJoin ) {
    // getExtensionProperties().put(ALLOW_JOIN, Boolean.toString(this.isAllowJoin()) );
    // } else getExtensionProperties().remove(ALLOW_JOIN);
    //
    // }

    // /* (non-Javadoc)
    // * @see java.lang.Object#toString()
    // */
    // @Override
    // public String toString() {
    // StringBuilder sb = new StringBuilder();
    // sb.append(this.getClass().getName());
    //      sb.append(" : name = ").append(getName()); //$NON-NLS-1$
    // if( !getColumns().isEmpty() ) {
    //          sb.append("\n\t").append(getColumns().size()).append(" columns"); //$NON-NLS-1$  //$NON-NLS-2$
    // for( Column col : getColumns() ) {
    //              sb.append("\n\tcol = ").append(col); //$NON-NLS-1$
    // }
    // }
    // return sb.toString();
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
    // final ForeignKey other = (ForeignKey)object;
    //
    // // string properties
    // if (!StringUtil.valuesAreEqual(getForeignKeyMultiplicity(), other.getForeignKeyMultiplicity()) ||
    // !StringUtil.valuesAreEqual(getPrimaryKeyMultiplicity(), other.getPrimaryKeyMultiplicity()) ||
    // !StringUtil.valuesAreEqual(getUniqueKeyName(), other.getUniqueKeyName()) ||
    // !StringUtil.valuesAreEqual(getUniqueKeyTableName(), other.getUniqueKeyTableName()) ) {
    // return false;
    // }
    //
    // // Columns
    // Collection<Column> thisColumns = getColumns();
    // Collection<Column> thatColumns = other.getColumns();
    //
    // if (thisColumns.size() != thatColumns.size()) {
    // return false;
    // }
    //
    // if (!thisColumns.isEmpty() && !thisColumns.containsAll(thatColumns)) {
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
    // if (!StringUtil.isEmpty(getForeignKeyMultiplicity())) {
    // result = HashCodeUtil.hashCode(result, getForeignKeyMultiplicity());
    // }
    // if (!StringUtil.isEmpty(getPrimaryKeyMultiplicity())) {
    // result = HashCodeUtil.hashCode(result, getPrimaryKeyMultiplicity());
    // }
    // if (!StringUtil.isEmpty(getUniqueKeyName())) {
    // result = HashCodeUtil.hashCode(result, getUniqueKeyName());
    // }
    // if (!StringUtil.isEmpty(getUniqueKeyTableName())) {
    // result = HashCodeUtil.hashCode(result, getUniqueKeyTableName());
    // }
    //
    // Collection<Column> cols = getColumns();
    // for(Column col: cols) {
    // result = HashCodeUtil.hashCode(result, col);
    // }
    //
    // return result;
    // }

}
