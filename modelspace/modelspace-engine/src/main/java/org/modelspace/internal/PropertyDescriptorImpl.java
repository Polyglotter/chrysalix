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
package org.modelspace.internal;

import javax.jcr.PropertyType;
import javax.jcr.Value;
import javax.jcr.nodetype.PropertyDefinition;

import org.modelspace.ModelObject;
import org.modelspace.ModelProperty;
import org.modelspace.ModelspaceException;
import org.modelspace.PropertyDescriptor;
import org.modeshape.common.util.CheckArg;

/**
 * A {@link PropertyDescriptor property descriptor} implementation.
 */
public class PropertyDescriptorImpl implements PropertyDescriptor {

    /**
     * @param type
     *        the {@link PropertyType JCR property type} being converted
     * @return the model object property type (never <code>null</code>)
     */
    static Type convert( final int type ) {
        if ( type == PropertyType.BINARY ) return Type.BINARY;
        if ( type == PropertyType.BOOLEAN ) return Type.BOOLEAN;
        if ( type == PropertyType.DATE ) return Type.DATE;
        if ( type == PropertyType.DECIMAL ) return Type.DECIMAL;
        if ( type == PropertyType.DOUBLE ) return Type.DOUBLE;
        if ( type == PropertyType.LONG ) return Type.LONG;
        if ( type == PropertyType.NAME ) return Type.NAME;
        if ( type == PropertyType.PATH ) return Type.PATH;
        if ( type == PropertyType.REFERENCE ) return Type.REFERENCE;
        if ( type == PropertyType.STRING ) return Type.STRING;
        if ( type == PropertyType.URI ) return Type.URI;
        if ( type == PropertyType.WEAKREFERENCE ) return Type.WEAKREFERENCE;
        return Type.UNDEFINED;
    }

    /**
     * @param type
     *        the {@link ModelObject model object} {@link ModelProperty property}
     *        {@link org.modelspace.PropertyDescriptor.Type type} being converted (never <code>null</code>)
     * 
     * @return the {@link PropertyType JCR property type}
     */
    static int convert( final Type type ) {
        if ( type == Type.BINARY ) return PropertyType.BINARY;
        if ( type == Type.BOOLEAN ) return PropertyType.BOOLEAN;
        if ( type == Type.DATE ) return PropertyType.DATE;
        if ( type == Type.DECIMAL ) return PropertyType.DECIMAL;
        if ( type == Type.DOUBLE ) return PropertyType.DOUBLE;
        if ( type == Type.LONG ) return PropertyType.LONG;
        if ( type == Type.NAME ) return PropertyType.NAME;
        if ( type == Type.PATH ) return PropertyType.PATH;
        if ( type == Type.REFERENCE ) return PropertyType.REFERENCE;
        if ( type == Type.STRING ) return PropertyType.STRING;
        if ( type == Type.URI ) return PropertyType.URI;
        if ( type == Type.WEAKREFERENCE ) return PropertyType.WEAKREFERENCE;
        return PropertyType.UNDEFINED;
    }

    private final Object[] defaultValues;
    private final boolean mandatory;
    private final boolean modifiable;
    private final boolean multiple;
    private final String name;
    private final Type type;

    PropertyDescriptorImpl( final PropertyDefinition propDefn ) throws ModelspaceException {
        CheckArg.isNotNull( propDefn, "propDefn" );

        this.mandatory = propDefn.isMandatory();
        this.modifiable = propDefn.isProtected();
        this.multiple = propDefn.isMultiple();
        this.name = propDefn.getName();
        this.type = convert( propDefn.getRequiredType() );

        final Value[] values = propDefn.getDefaultValues();

        if ( ( values == null ) || ( values.length == 0 ) ) {
            this.defaultValues = NO_VALUES;
        } else {
            this.defaultValues = new Object[ values.length ];
            final int type = propDefn.getRequiredType();
            int i = 0;

            for ( final Value value : values ) {
                this.defaultValues[ i++ ] = ModelPropertyImpl.convert( value, type );
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.PropertyDescriptor#defaultValues()
     */
    @Override
    public Object[] defaultValues() {
        return this.defaultValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.PropertyDescriptor#mandatory()
     */
    @Override
    public boolean mandatory() {
        return this.mandatory;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.PropertyDescriptor#modifiable()
     */
    @Override
    public boolean modifiable() {
        return this.modifiable;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.PropertyDescriptor#multiple()
     */
    @Override
    public boolean multiple() {
        return this.multiple;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.PropertyDescriptor#name()
     */
    @Override
    public String name() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.PropertyDescriptor#type()
     */
    @Override
    public Type type() {
        return this.type;
    }

}
