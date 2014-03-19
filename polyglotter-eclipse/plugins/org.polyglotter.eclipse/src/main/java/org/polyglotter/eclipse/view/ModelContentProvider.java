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
package org.polyglotter.eclipse.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.modeshape.modeler.Model;
import org.modeshape.modeler.ModelObject;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.eclipse.TreeSpinnerContentProvider;

/**
 * A content provider for Polyglotter {@link Model models}.
 */
public final class ModelContentProvider extends TreeSpinnerContentProvider {

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.eclipse.TreeSpinnerContentProvider#children(java.lang.Object)
     */
    @Override
    public Object[] children( final Object item ) throws PolyglotterException {
        if ( item instanceof Model ) {
            final Model model = ( ( Model ) item );
            final List< Object > kids = new ArrayList<>();

            for ( final Field field : Model.class.getDeclaredFields() ) {
                kids.add( new FieldWrapper( model, field ) );
            }

            try {
                final Object[] models = model.children();

                if ( ( models != null ) && ( models.length != 0 ) ) {
                    kids.addAll( Arrays.asList( models ) );
                }

                return kids.toArray();
            } catch ( final Exception e ) {
                throw new PolyglotterException( e );
            }
        }

        if ( item instanceof ModelObject ) {
            try {
                return ( ( ModelObject ) item ).children();
            } catch ( final Exception e ) {
                throw new PolyglotterException( e );
            }
        }

        if ( item instanceof FieldWrapper ) {
            final FieldWrapper wrapper = ( FieldWrapper ) item;

            if ( !wrapper.isPrimitive() ) {
                final List< Object > kids = new ArrayList<>();

                try {
                    if ( wrapper.isArray() ) {
                        try {
                            final Object[] values = ( Object[] ) wrapper.value();

                            for ( final Object value : values ) {
                                kids.add( value );
                            }
                        } catch ( final Exception e ) {
                            throw new PolyglotterException( e );
                        }
                    } else if ( wrapper.isCollection() ) {
                        final Collection< ? > values = ( Collection< ? > ) wrapper.value();

                        for ( final Object value : values ) {
                            kids.add( value );
                        }
                    } else {
                        kids.add( wrapper.value() );
                    }

                    return kids.toArray();
                } catch ( final Exception e ) {
                    throw new PolyglotterException( e );
                }
            }
        }

        return super.children( item );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.eclipse.TreeSpinnerContentProvider#hasChildren(java.lang.Object)
     */
    @Override
    public boolean hasChildren( final Object item ) throws PolyglotterException {
        if ( item instanceof ModelObject ) {
            try {
                return ( ( ModelObject ) item ).hasChildren();
            } catch ( final Exception e ) {
                throw new PolyglotterException( e );
            }
        }

        if ( item instanceof FieldWrapper ) {
            return !( ( FieldWrapper ) item ).hasChildren();
        }

        return super.hasChildren( item );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.eclipse.TreeSpinnerContentProvider#name(java.lang.Object)
     */
    @Override
    public String name( final Object item ) throws PolyglotterException {
        if ( item instanceof ModelObject ) {
            try {
                return ( ( ModelObject ) item ).name();
            } catch ( final Exception e ) {
                throw new PolyglotterException( e );
            }
        }

        if ( item instanceof FieldWrapper ) {
            return ( ( FieldWrapper ) item ).field().getName();
        }

        return super.name( item );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.eclipse.TreeSpinnerContentProvider#type(java.lang.Object)
     */
    @Override
    public String type( final Object item ) throws PolyglotterException {
        if ( item instanceof ModelObject ) {
            try {
                return ( ( ModelObject ) item ).primaryType();
            } catch ( final Exception e ) {
                throw new PolyglotterException( e );
            }
        }

        if ( item instanceof FieldWrapper ) {
            return ( ( FieldWrapper ) item ).type();
        }

        return super.type( item );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.eclipse.TreeSpinnerContentProvider#value(java.lang.Object)
     */
    @Override
    public String value( final Object item ) throws PolyglotterException {
        if ( item instanceof FieldWrapper ) {
            final FieldWrapper wrapper = ( FieldWrapper ) item;

            if ( wrapper.isPrimitive() ) {
                try {
                    final Object value = wrapper.value();

                    if ( value != null ) {
                        return value.toString();
                    }
                } catch ( final Exception e ) {
                    throw new PolyglotterException( e );
                }
            }
        }

        return super.value( item );
    }

    class FieldWrapper {

        private Class< ? > valueType;
        private final Model model;
        private final Field field;

        FieldWrapper( final Model wrappedModel,
                      final Field wrappedField ) {
            this.model = wrappedModel;
            this.field = wrappedField;
        }

        Field field() {
            return this.field;
        }

        boolean hasChildren() {
            return !isPrimitive();
        }

        boolean isArray() {
            return valueType().isArray();
        }

        boolean isCollection() {
            return valueType().isAssignableFrom( Collection.class );
        }

        boolean isPrimitive() {
            return valueType().isPrimitive();
        }

        Model model() {
            return this.model;
        }

        String type() {
            return valueType().getName();
        }

        Object value() throws Exception {
            if ( isPrimitive() ) {
                return this.field.get( this.model );
            }

            return null;
        }

        private Class< ? > valueType() {
            if ( this.valueType == null ) {
                this.valueType = this.field.getType();
            }

            return this.valueType;
        }

    }

}
