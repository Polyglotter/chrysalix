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

import java.lang.reflect.ParameterizedType;

import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.chrysalix.common.CheckArg;
import org.chrysalix.transformation.Value;
import org.chrysalix.transformation.ValueDescriptor;

/**
 * The base class for {@link Value value} transformation objects.
 * 
 * @param <T>
 *        the value type
 */
public class ValueImpl< T > implements Value< T > {

    private final ValueDescriptor< T > descriptor;

    /**
     * The current value.
     */
    protected T value;

    /**
     * @param valueDescriptor
     *        the value descriptor (cannot be <code>null</code>)
     */
    public ValueImpl( final ValueDescriptor< T > valueDescriptor ) {
        CheckArg.notNull( valueDescriptor, "valueDescriptor" );

        this.descriptor = valueDescriptor;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Value#descriptor()
     */
    @Override
    public ValueDescriptor< T > descriptor() {
        return this.descriptor;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Value#get()
     */
    @SuppressWarnings( "unused" )
    @Override
    public T get() throws ChrysalixException {
        return this.value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Value#set(java.lang.Object)
     */
    @SuppressWarnings( "unused" )
    @Override
    public void set( final T newValue ) throws ChrysalixException {
        if ( !this.descriptor.modifiable() ) {
            throw new UnsupportedOperationException( ChrysalixI18n.valueNotModifiable.text( this.descriptor.id() ) );
        }

        boolean changed = false;
        final T oldValue = this.value;

        if ( this.value == null ) {
            if ( newValue != null ) {
                this.value = newValue;
                changed = true;
            }
        } else if ( ( newValue == null ) || !this.value.equals( newValue ) ) {
            this.value = newValue;
            changed = true;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        @SuppressWarnings( "unchecked" ) final Class< T > clazz =
            ( Class< T > ) ( ( ParameterizedType ) getClass().getGenericSuperclass() ).getActualTypeArguments()[ 0 ];
        final StringBuilder builder = new StringBuilder( clazz.getName() );
        builder.append( " " ).append( descriptor().name() );

        return builder.toString();
    }

}
