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

/**
 * UniqueConstraint The relational unique constraint class
 */
public class UniqueConstraint extends RelationalObject {

    UniqueConstraint( final IObjectDelegate modelObject ) {
        super( modelObject );
    }

    // /**
    // * @return columns
    // */
    // public List<Column> getColumns() {
    // return columns;
    // }
    // /**
    // * @param columns Sets columns to the specified value.
    // */
    // public void setColumns( Collection<Column> columns ) {
    // this.columns = new ArrayList(columns);
    // handleInfoChanged();
    // }
    //
    // /**
    // * Add a column to the unique constraint
    // * @param column the column
    // */
    // public void addColumn(Column column) {
    // if( this.columns.add(column) ) {
    // column.setParent(this);
    // handleInfoChanged();
    // }
    // }
    //
    // /**
    // * Remove a column
    // * @param column the column to remove
    // * @return 'true' if the move was successful
    // */
    // public boolean removeColumn(Column column) {
    // if( this.columns.remove(column) ) {
    // handleInfoChanged();
    // return true;
    // }
    // return false;
    // }
    //
    // /**
    // * Set the object properties
    // * @param props the properties
    // */
    // @Override
    // public void setProperties(Properties props) {
    // // Set common properties
    // super.setProperties(props);
    //
    // handleInfoChanged();
    // }
    //
    // /**
    // * Get the parent table
    // * @return the table
    // */
    // public Table getTable() {
    // if( getParent() != null ) {
    // return (Table)getParent();
    // }
    //
    // return null;
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
    // final UniqueConstraint other = (UniqueConstraint)object;
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
    // List<Column> cols = getColumns();
    // for(Column col: cols) {
    // result = HashCodeUtil.hashCode(result, col);
    // }
    //
    // return result;
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

}
