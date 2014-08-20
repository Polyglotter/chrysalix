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
package org.polyglotter.operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.CheckArg;
import org.polyglotter.common.ObjectUtil;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.transformation.Operation;
import org.polyglotter.transformation.OperationCategory;
import org.polyglotter.transformation.OperationDescriptor;
import org.polyglotter.transformation.Transformation;
import org.polyglotter.transformation.TransformationFactory;
import org.polyglotter.transformation.ValidationProblem;
import org.polyglotter.transformation.ValidationProblems;
import org.polyglotter.transformation.Value;
import org.polyglotter.transformation.ValueDescriptor;

/**
 * A base class implementation for an {@link Operation operation}.
 * 
 * @param <T>
 *        the operation's result type
 */
abstract class AbstractOperation< T > extends ValueImpl< T > implements Operation< T > {

    private final Set< OperationCategory > categories;
    private final ValidationProblems problems;
    private final Map< String, List< Value< ? > >> inputs;
    private final Transformation transformation;

    /**
     * @param operationDescriptor
     *        the operation descriptor (cannot be <code>null</code>) for the output value
     * @param operationTransformation
     *        the transformation that owns this operation (cannot be <code>null</code>)
     */
    protected AbstractOperation( final ValueDescriptor< T > operationDescriptor,
                                 final Transformation operationTransformation ) {
        super( operationDescriptor );
        CheckArg.notNull( operationTransformation, "operationTransformation" );

        this.categories = new HashSet<>( 5 );
        this.problems = TransformationFactory.createValidationProblems();
        this.transformation = operationTransformation;
        this.inputs = new HashMap<>( 5 );

        valueChanged(); // to get initial validation and value set
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#addCategory(org.polyglotter.transformation.OperationCategory[])
     */
    @Override
    public void addCategory( final OperationCategory... categoriesToAdd ) throws PolyglotterException {
        CheckArg.isNotEmpty( categoriesToAdd, "categoriesToAdd" );

        for ( final OperationCategory category : categoriesToAdd ) {
            if ( ( category == null ) || !this.categories.add( category ) ) {
                throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationCategory,
                                                name(),
                                                transformationId() );
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#addInput(java.lang.String, java.lang.Object[])
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public void addInput( final String descriptorId,
                          final Object... valuesBeingAdded ) throws PolyglotterException {
        CheckArg.notEmpty( descriptorId, "descriptorId" );
        CheckArg.isNotEmpty( valuesBeingAdded, "valuesBeingAdded" );

        if ( !isValidInputDescriptorId( descriptorId ) ) {
            throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput,
                                            name(),
                                            transformationId(),
                                            descriptorId );
        }

        List< Value< ? >> values = this.inputs.get( descriptorId );

        if ( values == null ) {
            values = new ArrayList<>();
        }

        final ValueDescriptor< ? > descriptor = descriptor( descriptorId );

        for ( final Object value : valuesBeingAdded ) {
            if ( value == null ) {
                throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput,
                                                name(),
                                                transformationId(),
                                                descriptorId );
            }

            boolean added = false;

            if ( value instanceof Value< ? > ) {
                added = values.add( ( Value< ? > ) value );
            } else {
                added = values.add( TransformationFactory.createValue( ( ValueDescriptor< Object > ) descriptor, value ) );
            }

            if ( !added ) {
                throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput,
                                                name(),
                                                transformationId(),
                                                descriptorId );
            }
        }

        if ( !this.inputs.containsKey( descriptorId ) ) {
            this.inputs.put( descriptorId, values );
        }

        valueChanged();
    }

    /**
     * Calculates the result. Calculation should only be done when there are no {@link ValidationProblem validation problems}.
     * 
     * @return the result (can be <code>null</code>)
     * @throws PolyglotterException
     *         if there are validation problems
     * @see #problems()
     */
    protected abstract T calculate() throws PolyglotterException;

    /**
     * @param categoryBeingChecked
     *        the category being checked (cannot be <code>null</code>)
     * @return <code>true</code> if the category can be removed
     */
    public boolean canRemoveCategory( final OperationCategory categoryBeingChecked ) {
        CheckArg.notNull( categoryBeingChecked, "categoryToRemove" );
        return this.categories.contains( categoryBeingChecked )
               && !( categoryBeingChecked instanceof OperationCategory.BuiltInCategory );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#categories()
     */
    @Override
    public Set< OperationCategory > categories() {
        return Collections.unmodifiableSet( this.categories );
    }

    /**
     * @return the descriptor description for this value (never <code>null</code>)
     */
    protected String description() {
        return descriptor().description();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.ValueImpl#descriptor()
     */
    @Override
    public final OperationDescriptor< T > descriptor() {
        return ( OperationDescriptor< T > ) super.descriptor();
    }

    private ValueDescriptor< ? > descriptor( final String id ) {
        for ( final ValueDescriptor< ? > descriptor : descriptor().inputDescriptors() ) {
            if ( descriptor.id().equals( id ) ) {
                return descriptor;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Do not call if there are validation errors as this will throw an exception.</strong>
     * 
     * @see org.polyglotter.operation.ValueImpl#get()
     */
    @Override
    public T get() throws PolyglotterException {
        if ( !this.problems.isError() ) {
            return super.get();
        }

        throw new PolyglotterException( PolyglotterI18n.operationHasErrors, name(), transformationId() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#inputs()
     */
    @Override
    public List< Value< ? >> inputs() {
        if ( this.inputs.isEmpty() ) {
            return Value.NO_VALUES;
        }

        final List< Value< ? >> result = new ArrayList<>();

        for ( final List< Value< ? >> values : this.inputs.values() ) {
            result.addAll( values );
        }

        return result;
    }

    /**
     * @param descriptorId
     *        the identifier of the {@link ValueDescriptor descriptor} whose inputs are being requested (cannot be <code>null</code>
     *        or invalid)
     * @return the values (never <code>null</code> but can be empty)
     * @see #isValidInputDescriptorId(String)
     * @throws IllegalArgumentException
     *         if the descriptor is not found
     */
    protected List< Value< ? >> inputs( final String descriptorId ) {
        CheckArg.notNull( descriptor( descriptorId ), "descriptorId" );
        final List< Value< ? > > values = this.inputs.get( descriptorId );

        if ( values == null ) {
            return Value.NO_VALUES;
        }

        return values;
    }

    private boolean isValidInputDescriptorId( final String id ) {
        return ( descriptor( id ) != null );
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator< Value< ? >> iterator() {
        return Collections.unmodifiableCollection( inputs() ).iterator();
    }

    /**
     * @return the descriptor name for this value (never <code>null</code>)
     */
    protected String name() {
        return descriptor().name();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#problems()
     */
    @Override
    public ValidationProblems problems() {
        return this.problems;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#removeCategory(org.polyglotter.transformation.OperationCategory[])
     */
    @Override
    public void removeCategory( final OperationCategory... categoriesToRemove ) throws PolyglotterException {
        CheckArg.isNotEmpty( categoriesToRemove, "categoriesToRemove" );

        for ( final OperationCategory category : categoriesToRemove ) {
            if ( ( category == null )
                 || ( category instanceof OperationCategory.BuiltInCategory )
                 || !this.categories.remove( category ) ) {
                throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationCategory,
                                                name(),
                                                transformationId() );
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#removeInput(java.lang.String, java.lang.Object[])
     */
    @Override
    public void removeInput( final String descriptorId,
                             final Object... valuesBeingRemoved ) throws PolyglotterException {
        CheckArg.notEmpty( descriptorId, "descriptorId" );
        CheckArg.isNotEmpty( valuesBeingRemoved, "valuesBeingRemoved" );

        if ( !isValidInputDescriptorId( descriptorId ) ) {
            throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput,
                                            name(),
                                            transformationId(),
                                            descriptorId );
        }

        final List< Value< ? >> values = inputs( descriptorId );

        // error if there are no values to delete
        if ( values.isEmpty() ) {
            throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput,
                                            name(),
                                            transformationId(),
                                            descriptorId );
        }

        for ( final Object valueToDelete : valuesBeingRemoved ) {
            if ( valueToDelete == null ) {
                throw new PolyglotterException( PolyglotterI18n.errorRemovingOperationInput,
                                                name(),
                                                transformationId() );
            }

            boolean removed = false;

            if ( valueToDelete instanceof Value< ? > ) {
                removed = values.remove( valueToDelete );
            } else {
                // delete first occurrence of value
                for ( final Value< ? > input : values ) {
                    if ( valueToDelete.equals( input.get() ) ) {
                        removed = values.remove( valueToDelete );
                        break;
                    }
                }
            }

            if ( !removed ) {
                throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput,
                                                name(),
                                                transformationId(),
                                                descriptorId );
            }
        }

        if ( values.isEmpty() ) {
            this.inputs.remove( descriptorId );
        }

        valueChanged();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.operation.ValueImpl#set(java.lang.Object)
     */
    @Override
    public void set( final T newValue ) throws UnsupportedOperationException {
        throw new UnsupportedOperationException( PolyglotterI18n.operationResultNotModifiable.text( name(),
                                                                                                    transformation().id() ) );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#setInput(java.lang.String, java.lang.Object[])
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public void setInput( final String descriptorId,
                          final Object... valuesBeingSet ) throws PolyglotterException {
        if ( !isValidInputDescriptorId( descriptorId ) ) {
            throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput,
                                            name(),
                                            transformationId(),
                                            descriptorId );
        }

        // remove any previous values
        this.inputs.remove( descriptorId );

        if ( ( valuesBeingSet == null ) || ( valuesBeingSet.length == 0 ) ) {
            this.logger.debug( "Input values for descriptor '%s' were removed", descriptorId );
            return;
        }

        final List< Value< ? >> values = new ArrayList<>();
        final ValueDescriptor< ? > descriptor = descriptor( descriptorId ); // descriptor is valid

        for ( final Object value : valuesBeingSet ) {
            if ( value == null ) {
                throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput,
                                                name(),
                                                transformationId(),
                                                descriptorId );
            }

            if ( value instanceof Value< ? > ) {
                values.add( ( Value< ? > ) value );
            } else {
                values.add( TransformationFactory.createValue( ( ValueDescriptor< Object > ) descriptor, value ) );
            }
        }

        this.inputs.put( descriptorId, values );

        valueChanged();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder( descriptor().name() );
        builder.append( '(' );

        boolean firstTime = true;

        for ( final Value< ? > term : inputs() ) {
            if ( firstTime ) {
                firstTime = false;
            } else {
                builder.append( ", " );
            }

            builder.append( term );
        }

        builder.append( ')' );
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#transformation()
     */
    @Override
    public Transformation transformation() {
        return this.transformation;
    }

    /**
     * @return the identifier for {@link Transformation transformation} this operation is contained in (never <code>null</code>)
     */
    protected String transformationId() {
        return transformation().id();
    }

    /**
     * Validate the state of the input values. Validation errors should be stored as {@link ValidationProblem problems}.
     */
    protected abstract void validate();

    /**
     * Called after a change to an input value.
     */
    protected void valueChanged() {
        this.problems.clear();
        validate();

        if ( !this.problems.isError() ) {
            T oldValue = null;
            T newValue = null;

            try {
                oldValue = get();
                newValue = calculate();

                if ( !ObjectUtil.equals( oldValue, newValue ) ) {
                    this.value = newValue; // do not call set method as it throws exception
                }
            } catch ( final PolyglotterException e ) {
                final ValidationProblem problem =
                    TransformationFactory.createError( descriptor().id(),
                                                       PolyglotterI18n.errorOnTermChanged.text( descriptor().id(),
                                                                                                transformationId() ) );
                problems().add( problem );
            }
        }
    }

}
