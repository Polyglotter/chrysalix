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
package org.polyglotter.transformation;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.polyglotter.Polyglotter;
import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.I18n;

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
     * @return the unique identifier of an operation category (never <code>null</code>)
     */
    QName id();

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
        ARITHMETIC( "arithmetic", PolyglotterI18n.opCatArithmeticLabel, PolyglotterI18n.opCatArithmeticDescription ),

        /**
         * A category for operations that assign a term a value.
         */
        ASSIGNMENT( "assignment", PolyglotterI18n.opCatAssignmentLabel, PolyglotterI18n.opCatAssignmentDescription ),

        /**
         * A category for operations who perform on the bit level.
         */
        BITWISE( "bitwise", PolyglotterI18n.opCatBitwiseLabel, PolyglotterI18n.opCatBitwiseDescription ),

        /**
         * A category for operations that act on and/or have a date or time result.
         */
        DATE_TIME( "date_time", PolyglotterI18n.opCatDateTimeLabel, PolyglotterI18n.opCatDateTimeDescription ),

        /**
         * A category for operations that compare two expressions.
         */
        LOGICAL( "logical", PolyglotterI18n.opCatLogicalLabel, PolyglotterI18n.opCatLogicalDescription ),

        /**
         * A category for miscellaneous operations.
         */
        OTHER( "other", PolyglotterI18n.opCatOtherLabel, PolyglotterI18n.opCatOtherDescription ),

        /**
         * A category for operations that compare one operand to another.
         */
        RELATIONAL( "relational", PolyglotterI18n.opCatRelationalLabel, PolyglotterI18n.opCatRelationalDescription ),

        /**
         * A category for operations that act on and/or have a string result.
         */
        STRING( "string", PolyglotterI18n.opCatStringLabel, PolyglotterI18n.opCatStringDescription );

        private final I18n description;
        private final QName id;
        private final I18n label;

        private BuiltInCategory( final String categoryId,
                                 final I18n categoryLabel,
                                 final I18n categoryDescription ) {
            this.id = new QName( Polyglotter.NAMESPACE_URI, categoryId, Polyglotter.NAMESPACE_PREFIX );
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
         * @see org.polyglotter.transformation.OperationCategory#id()
         */
        @Override
        public QName id() {
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