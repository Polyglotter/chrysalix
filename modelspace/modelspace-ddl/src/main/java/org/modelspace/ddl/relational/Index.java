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
 * 
 *
 *
 */
public class Index extends RelationalObject {

    Index( final IObjectDelegate modelObject ) {
        super( modelObject );
    }

    // @Override
    // public Index clone() {
    // Index clonedIndex = new Index(getName());
    // clonedIndex.setNameInSource(getNameInSource());
    // clonedIndex.setDescription(getDescription());
    // clonedIndex.setModelType(getModelType());
    // clonedIndex.setUnique(isUnique());
    // clonedIndex.setAutoUpdate(isAutoUpdate());
    // clonedIndex.setFilterCondition(getFilterCondition());
    // clonedIndex.setNullable(isNullable());
    // for( Column col : getColumns() ) {
    // clonedIndex.addColumn(col);
    // }
    // return clonedIndex;
    // }
    //
    // @Override
    // public void inject(RelationalObject originalIndex) {
    // super.inject(originalIndex);
    // Index theIndex = (Index)originalIndex;
    // setName(theIndex.getName());
    // setNameInSource(theIndex.getNameInSource());
    // setDescription(theIndex.getDescription());
    // setModelType(theIndex.getModelType());
    // setFilterCondition(theIndex.getFilterCondition());
    // setNullable(theIndex.isNullable());
    // setAutoUpdate(theIndex.isAutoUpdate());
    // setUnique(theIndex.isUnique());
    // getColumns().clear();
    // for( Column col : theIndex.getColumns() ) {
    // addColumn(col);
    // }
    // }
    //
    // /**
    // * @return columns
    // */
    // public List<Column> getColumns() {
    // return columns;
    // }
    //
    // /**
    // * @param column the collumn
    // */
    // public void addColumn( Column column ) {
    // this.columns.add(column);
    // }
    // /**
    // * @return autoUpdate
    // */
    // public boolean isAutoUpdate() {
    // return autoUpdate;
    // }
    // /**
    // * @param autoUpdate Sets autoUpdate to the specified value.
    // */
    // public void setAutoUpdate( boolean autoUpdate ) {
    // this.autoUpdate = autoUpdate;
    // }
    // /**
    // * @return filterCondition
    // */
    // public String getFilterCondition() {
    // return filterCondition;
    // }
    // /**
    // * @param filterCondition Sets filterCondition to the specified value.
    // */
    // public void setFilterCondition( String filterCondition ) {
    // this.filterCondition = filterCondition;
    // }
    // /**
    // * @return nullable
    // */
    // public boolean isNullable() {
    // return nullable;
    // }
    // /**
    // * @param nullable Sets nullable to the specified value.
    // */
    // public void setNullable( boolean nullable ) {
    // this.nullable = nullable;
    // }
    // /**
    // * @return unique
    // */
    // public boolean isUnique() {
    // return unique;
    // }
    // /**
    // * @param unique Sets unique to the specified value.
    // */
    // public void setUnique( boolean unique ) {
    // this.unique = unique;
    // }
    //
    // /**
    // * @return the existingTable
    // */
    // public boolean usesExistingTable() {
    // return this.existingTable;
    // }
    //
    // /**
    // * @param usesExistingTable the existingTable to set
    // */
    // public void setUsesExistingTable(boolean usesExistingTable) {
    // this.existingTable = usesExistingTable;
    // }
    //
    // /**
    // * @return the relationalTable
    // */
    // public Table getRelationalTable() {
    // return this.relationalTable;
    // }
    //
    // /**
    // * @param relationalTable the relationalTable to set
    // */
    // public void setRelationalTable(Table relationalTable) {
    // this.relationalTable = relationalTable;
    // }
    //

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ddl.relational.RelationalObject#getProperties()
     */
    @Override
    public Map< String, Object > getProperties() throws ModelspaceException {
        final Map< String, Object > props = super.getProperties();

        // // Add property values for column
        // String[] propKeys = RelationalConstants.INDEX_PROP_KEYS.toArray();
        // for(int i=0; i<propKeys.length; i++) {
        // props.put(propKeys[i], getPropertyValue(propKeys[i]));
        // }
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
        // props.put(RelationalConstants.INDEX_PROP_KEYS.NULLABLE, getPropertyValue(RelationalConstants.INDEX_PROP_KEYS.NULLABLE));
        // props.put(RelationalConstants.INDEX_PROP_KEYS.UNIQUE, getPropertyValue(RelationalConstants.INDEX_PROP_KEYS.UNIQUE));
        // props.put(RelationalConstants.INDEX_PROP_KEYS.AUTO_UPDATE,
        // getPropertyValue(RelationalConstants.INDEX_PROP_KEYS.AUTO_UPDATE));
        // props.put(RelationalConstants.INDEX_PROP_KEYS.FILTER_CONDITION,
        // getPropertyValue(RelationalConstants.INDEX_PROP_KEYS.FILTER_CONDITION));

        return propertyValue;
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
    // if(keyStr.equalsIgnoreCase(KEY_NULLABLE) ) {
    // setNullable(Boolean.parseBoolean(value));
    // } else if(keyStr.equalsIgnoreCase(KEY_UNIQUE) ) {
    // setUnique(Boolean.parseBoolean(value));
    // } else if(keyStr.equalsIgnoreCase(KEY_AUTO_UPDATE) ) {
    // setAutoUpdate(Boolean.parseBoolean(value));
    // } else if(keyStr.equalsIgnoreCase(KEY_FILTER_CONDITION) ) {
    // setFilterCondition(value);
    // }
    // }
    //
    // handleInfoChanged();
    // }
    //
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
    // final Index other = (Index)object;
    //
    //
    //
    // // string properties
    // if (!StringUtil.valuesAreEqual(getFilterCondition(), other.getFilterCondition()) ) {
    // return false;
    // }
    //
    // if( !(isAutoUpdate()==other.isAutoUpdate()) ||
    // !(isNullable()==other.isNullable()) ||
    // !(isUnique()==other.isUnique()) ) {
    // return false;
    // }
    //
    // // Table
    // if (relationalTable == null) {
    // if (other.relationalTable != null)
    // return false;
    // } else if (!relationalTable.equals(other.relationalTable))
    // return false;
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
    // if (!StringUtil.isEmpty(getFilterCondition())) {
    // result = HashCodeUtil.hashCode(result, getFilterCondition());
    // }
    //
    // result = HashCodeUtil.hashCode(result, isAutoUpdate());
    // result = HashCodeUtil.hashCode(result, isNullable());
    // result = HashCodeUtil.hashCode(result, isUnique());
    //
    // if(relationalTable!=null) {
    // result = HashCodeUtil.hashCode(result, relationalTable);
    // }
    //
    // List<Column> cols = getColumns();
    // for(Column col: cols) {
    // result = HashCodeUtil.hashCode(result, col);
    // }
    //
    // return result;
    // }

}
