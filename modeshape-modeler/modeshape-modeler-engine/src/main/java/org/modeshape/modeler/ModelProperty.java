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
package org.modeshape.modeler;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Represents a {@link ModelObject model object} property.
 */
public interface ModelProperty {

    /**
     * An empty array of model properties.
     */
    ModelProperty[] NO_PROPS = {};

    /**
     * @return the value represented as a <code>boolean</code> or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if an error occurs
     */
    boolean booleanValue() throws ModelerException;

    /**
     * @return the values represented as <code>boolean</code>s or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if not a multi-value property or if an error occurs
     */
    boolean[] booleanValues() throws ModelerException;

    /**
     * @return the value represented as a date or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if an error occurs
     */
    Calendar dateValue() throws ModelerException;

    /**
     * @return the values represented as dates or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if not a multi-value property or if an error occurs
     */
    Calendar[] dateValues() throws ModelerException;

    /**
     * @return the value represented as a <code>decimal</code> or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if an error occurs
     */
    BigDecimal decimalValue() throws ModelerException;

    /**
     * @return the values represented as <code>decimal</code>s or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if not a multi-value property or if an error occurs
     */
    BigDecimal[] decimalValues() throws ModelerException;

    /**
     * @return the property descriptor (never <code>null</code>)
     * @throws ModelerException
     *         if an error occurs
     */
    PropertyDescriptor descriptor() throws ModelerException;

    /**
     * @return the value represented as a <code>double</code> or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if an error occurs
     */
    double doubleValue() throws ModelerException;

    /**
     * @return the values represented as <code>double</code>s or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if not a multi-value property or if an error occurs
     */
    double[] doubleValues() throws ModelerException;

    /**
     * @return the Long value of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if an error occurs
     */
    long longValue() throws ModelerException;

    /**
     * @return the Long values of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if not a multi-value property or if an error occurs
     */
    long[] longValues() throws ModelerException;

    /**
     * Passing in <code>null</code> will remove the existing property from its node.
     * 
     * @param values
     *        the new value for single-valued properties or the new values for multi-valued properties(can be <code>null</code>)
     * @throws ModelerException
     *         if an error occurs
     */
    void set( final Object... values ) throws ModelerException;

    /**
     * @return the String value of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if an error occurs
     */
    String stringValue() throws ModelerException;

    /**
     * @return the String values of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if not a multi-value property or if an error occurs
     */
    String[] stringValues() throws ModelerException;

    /**
     * @return the value of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if an error occurs
     */
    Object value() throws ModelerException;

    /**
     * @return the values of the supplied property, or <code>null</code> if the property doesn't exist
     * @throws ModelerException
     *         if not a multi-value property or if an error occurs
     */
    Object[] values() throws ModelerException;

}
