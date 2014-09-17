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
package org.chrysalix.transformation;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import org.chrysalix.ChrysalixI18n;
import org.chrysalix.common.I18n;

/**
 * The category of an {@link Operation operation}.
 */
public interface OperationCategory {

    /**
     * An empty set of categories.
     */
    final Set< OperationCategory > NO_CATEGORIES = Collections.emptySet();

    /**
     * Sorts categories by their name.
     */
    Comparator< OperationCategory > CATEGORY_SORTER = new Comparator< OperationCategory >() {

        /**
         * {@inheritDoc}
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare( final OperationCategory thisCategory,
                            final OperationCategory thatCategory ) {
            return thisCategory.name().compareTo( thatCategory.name() );
        }

    };

    /**
     * @return the unique identifier of an operation category (never <code>null</code> or empty)
     */
    String id();

    /**
     * @return the localized name of the category (never <code>null</code> or empty)
     */
    String name();

    /**
     * Built-in categories of an operation.
     */
    enum BuiltInCategory implements OperationCategory {

        /**
         * A category for operations that act on and/or have a numeric result.
         */
        ARITHMETIC( "arithmetic", ChrysalixI18n.opCatArithmeticLabel, ChrysalixI18n.opCatArithmeticDescription ),

        /**
         * A category for operations that assign a term a value.
         */
        ASSIGNMENT( "assignment", ChrysalixI18n.opCatAssignmentLabel, ChrysalixI18n.opCatAssignmentDescription ),

        /**
         * A category for operations who perform on the bit level.
         */
        BITWISE( "bitwise", ChrysalixI18n.opCatBitwiseLabel, ChrysalixI18n.opCatBitwiseDescription ),

        /**
         * A category for operations that act on and/or have a date or time result.
         */
        DATE_TIME( "date_time", ChrysalixI18n.opCatDateTimeLabel, ChrysalixI18n.opCatDateTimeDescription ),

        /**
         * A category for operations that compare two expressions.
         */
        LOGICAL( "logical", ChrysalixI18n.opCatLogicalLabel, ChrysalixI18n.opCatLogicalDescription ),

        /**
         * A category for miscellaneous operations.
         */
        OTHER( "other", ChrysalixI18n.opCatOtherLabel, ChrysalixI18n.opCatOtherDescription ),

        /**
         * A category for operations that compare one operand to another.
         */
        RELATIONAL( "relational", ChrysalixI18n.opCatRelationalLabel, ChrysalixI18n.opCatRelationalDescription ),

        /**
         * A category for operations that act on and/or have a string result.
         */
        STRING( "string", ChrysalixI18n.opCatStringLabel, ChrysalixI18n.opCatStringDescription );

        private final I18n description;
        private final String id;
        private final I18n label;

        private BuiltInCategory( final String categoryId,
                                 final I18n categoryLabel,
                                 final I18n categoryDescription ) {
            this.id = ( OperationCategory.class.getName() + '.' + categoryId );
            this.label = categoryLabel;
            this.description = categoryDescription;
        }

        /**
         * @return a localized category description (never <code>null</code> or empty)
         */
        public String description() {
            return this.description.text();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.OperationCategory#id()
         */
        @Override
        public String id() {
            return this.id;
        }

        /**
         * @return a localized short description (never <code>null</code> or empty)
         */
        public String label() {
            return this.label.text();
        }

    }

}