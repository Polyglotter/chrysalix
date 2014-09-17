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
package org.chrysalix.operation;

import org.chrysalix.common.CheckArg;
import org.chrysalix.transformation.ValueDescriptor;

/**
 * An implementation of a {@link ValueDescriptor value descriptor}.
 * 
 * @param <T>
 *        the type of the descriptor
 */
public class ValueDescriptorImpl< T > implements ValueDescriptor< T > {

    private final String description;
    private final String id;
    private final boolean modifiable;
    private final String name;
    private final int numRequiredValues;
    private final Class< T > type;
    private final boolean unbounded;

    /**
     * @param valueId
     *        the value identifier (cannot be <code>null</code> or empty)
     * @param valueDescription
     *        the value description (cannot be <code>null</code> or empty)
     * @param valueName
     *        the value name (cannot be <code>null</code> or empty)
     * @param valueType
     *        the value type (cannot be <code>null</code>)
     * @param isModifiable
     *        <code>true</code> if value is modifiable
     * @param requiredValueCount
     *        the number of required values (cannot be a negative number)
     * @param isUnbounded
     *        <code>true</code> if there is no limit to the number of values
     */
    public ValueDescriptorImpl( final String valueId,
                                final String valueDescription,
                                final String valueName,
                                final Class< T > valueType,
                                final boolean isModifiable,
                                final int requiredValueCount,
                                final boolean isUnbounded ) {
        CheckArg.notEmpty( valueId, "valueId" );
        CheckArg.notEmpty( valueDescription, "valueDescription" );
        CheckArg.notEmpty( valueName, "valueName" );
        CheckArg.notNull( valueType, "valueType" );
        CheckArg.isNonNegative( requiredValueCount, "requiredValueCount" );

        this.id = valueId;
        this.description = valueDescription;
        this.name = valueName;
        this.type = valueType;
        this.modifiable = isModifiable;
        this.numRequiredValues = requiredValueCount;
        this.unbounded = isUnbounded;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.ValueDescriptor#description()
     */
    @Override
    public String description() {
        return this.description;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.ValueDescriptor#id()
     */
    @Override
    public String id() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.ValueDescriptor#modifiable()
     */
    @Override
    public boolean modifiable() {
        return this.modifiable;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.ValueDescriptor#name()
     */
    @Override
    public String name() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.ValueDescriptor#requiredValueCount()
     */
    @Override
    public int requiredValueCount() {
        return this.numRequiredValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.ValueDescriptor#type()
     */
    @Override
    public Class< T > type() {
        return this.type;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.ValueDescriptor#unbounded()
     */
    @Override
    public boolean unbounded() {
        return this.unbounded;
    }

}