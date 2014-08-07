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

import javax.xml.namespace.QName;

import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.CheckArg;
import org.polyglotter.common.ObjectUtil;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.transformation.Operation;
import org.polyglotter.transformation.OperationCategory;
import org.polyglotter.transformation.Transformation;
import org.polyglotter.transformation.TransformationEvent;
import org.polyglotter.transformation.TransformationEvent.EventType;
import org.polyglotter.transformation.TransformationFactory;
import org.polyglotter.transformation.TransformationListener;
import org.polyglotter.transformation.ValidationProblem;
import org.polyglotter.transformation.ValidationProblems;
import org.polyglotter.transformation.Value;
import org.polyglotter.transformation.ValueDescriptor;

/**
 * The base class for {@link Operation operations}.
 * 
 * @param <T>
 *        the operation's result type
 */
public abstract class AbstractOperation< T > extends ValueImpl< T > implements TransformationListener, Operation< T > {

    /**
     * A transformation that can be used for intermediate results. It does not do anything.
     */
    protected static final Transformation TEMP_TRANSFORMATION = new Transformation() {

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#add(org.polyglotter.transformation.Operation[])
         */
        @Override
        public void add( final Operation< ? >... operations ) {
            // does nothing
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#execute()
         */
        @Override
        public void execute() {
            // does nothing
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#id()
         */
        @Override
        public QName id() {
            return null;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Iterable#iterator()
         */
        @Override
        public Iterator< Operation< ? >> iterator() {
            return null;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#operations()
         */
        @Override
        public List< Operation< ? >> operations() {
            return null;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#remove(org.polyglotter.transformation.Operation[])
         */
        @Override
        public void remove( final Operation< ? >... operations ) {
            // does nothing
        }

    };

    private final Set< OperationCategory > categories;
    private final ValidationProblems problems;
    private final Map< QName, List< Value< ? > >> inputs;
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

        if ( categoriesToAdd.length == 1 ) {
            notifyObservers( OperationEventType.CATEGORY_ADDED, EventTag.NEW, categoriesToAdd[ 0 ] );
        } else {
            notifyObservers( OperationEventType.CATEGORIES_ADDED, EventTag.NEW, categoriesToAdd );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#addInput(javax.xml.namespace.QName, java.lang.Object[])
     */
    @Override
    public void addInput( final QName descriptorId,
                          final Object... valuesBeingAdded ) throws PolyglotterException {
        CheckArg.notNull( descriptorId, "descriptorId" );
        CheckArg.isNotEmpty( valuesBeingAdded, "valuesBeingAdded" );

        if ( !isValidInputDescriptorId( descriptorId ) ) {
            throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput,
                                            name(),
                                            transformationId() );
        }

        List< Value< ? >> values = this.inputs.get( descriptorId );

        if ( values == null ) {
            values = new ArrayList<>();
        }

        final ValueDescriptor< ? > descriptor = descriptor( descriptorId );

        for ( final Object value : valuesBeingAdded ) {
            if ( value == null ) {
                throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput, descriptor().id() );
            }

            if ( value instanceof Value< ? > ) {
                // TODO
            } else {
                values.add( TransformationFactory.createValue( descriptor, value ) );
            }
        }

        if ( !this.inputs.containsKey( descriptorId ) ) {
            this.inputs.put( descriptorId, values );
        }

        if ( valuesBeingAdded.length == 1 ) {
            notifyObservers( OperationEventType.VALUE_ADDED, EventTag.NEW, valuesBeingAdded[ 0 ] );
        } else {
            notifyObservers( OperationEventType.VALUES_ADDED, EventTag.NEW, valuesBeingAdded );
        }
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

    private ValueDescriptor< ? > descriptor( final QName id ) {
        for ( final ValueDescriptor< ? > descriptor : inputDescriptors() ) {
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
     * @see #isValidInputDescriptorId(QName)
     * @throws IllegalArgumentException
     *         if the descriptor is not found
     */
    protected List< Value< ? >> inputs( final QName descriptorId ) {
        CheckArg.notNull( descriptor( descriptorId ), "descriptorId" );
        final List< Value< ? > > values = this.inputs.get( descriptorId );

        if ( values == null ) {
            return Value.NO_VALUES;
        }

        return values;
    }

    private boolean isValidInputDescriptorId( final QName id ) {
        return ( descriptor( id ) != null );
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator< Value< ? >> iterator() {
        return inputs().iterator();
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
     * @see org.polyglotter.transformation.TransformationListener#notify(org.polyglotter.transformation.TransformationEvent)
     */
    @Override
    public void notify( final TransformationEvent event ) {
        CheckArg.notNull( event, "event" );

        // recalculate as a value changed
        if ( event.type() == ValueEventType.VALUE_CHANGED ) {
            valueChanged();
        }
    }

    /**
     * @param type
     *        the event type being handled (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if type is <code>null</code>)
     */
    protected void notifyObservers( final EventType type ) {
        notifyObservers( type, null, null );
    }

    /**
     * @param type
     *        the event type being handled (cannot be <code>null</code>)
     * @param eventData
     *        the event data properties (can be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if type is <code>null</code>)
     */
    protected void notifyObservers( final EventType type,
                                    final Map< String, Object > eventData ) {
        CheckArg.notNull( type, "type" );

        if ( !this.listeners.isEmpty() ) {
            final TransformationEvent event = TransformationFactory.createEvent( type, this, eventData );
            List< TransformationListener > remove = null;

            for ( final TransformationListener listener : this.listeners ) {
                try {
                    listener.notify( event );
                } catch ( final Exception e ) {
                    // remove listener since it threw an exception
                    if ( remove == null ) {
                        remove = new ArrayList<>();
                    }

                    this.logger.error( e, PolyglotterI18n.listenerError, listener.getClass(), event );
                    remove.add( listener );
                }
            }

            if ( remove != null ) {
                for ( final TransformationListener listenerToRemove : remove ) {
                    remove( listenerToRemove );
                }
            }
        }
    }

    /**
     * @param type
     *        the event type being handled (cannot be <code>null</code>)
     * @param key
     *        the identifier of a property of event data (can be <code>null</code> or empty)
     * @param value
     *        the value of the property of event data (can be <code>null</code> or empty)
     * @throws IllegalArgumentException
     *         if type is <code>null</code>)
     */
    protected void notifyObservers( final EventType type,
                                    final String key,
                                    final Object value ) {
        Map< String, Object > map = null;

        if ( ( key != null ) && !key.isEmpty() ) {
            map = Collections.singletonMap( key, value );
        }

        notifyObservers( type, map );
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

        if ( categoriesToRemove.length == 1 ) {
            notifyObservers( OperationEventType.CATEGORY_REMOVED, EventTag.OLD, categoriesToRemove[ 0 ] );
        } else {
            notifyObservers( OperationEventType.CATEGORIES_REMOVED, EventTag.OLD, categoriesToRemove );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.Operation#removeInput(org.polyglotter.transformation.Value[])
     */
    @Override
    public void removeInput( final Value< ? >... inputsToRemove ) throws PolyglotterException {
        CheckArg.isNotEmpty( inputsToRemove, "inputsToRemove" );

        for ( final Value< ? > term : inputsToRemove ) {
            if ( term == null ) {
                throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput,
                                                name(),
                                                transformationId() );
            }

            final QName descriptorId = term.descriptor().id();
            final List< Value< ? >> values = inputs( descriptorId );

            if ( ( values == null ) || !values.remove( term ) ) {
                throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput,
                                                name(),
                                                transformationId() );
            }

            if ( values.isEmpty() ) {
                this.inputs.remove( descriptorId );
            }
        }

        if ( inputsToRemove.length == 1 ) {
            notifyObservers( OperationEventType.VALUE_REMOVED, EventTag.OLD, inputsToRemove[ 0 ] );
        } else {
            notifyObservers( OperationEventType.VALUES_REMOVED, EventTag.OLD, inputsToRemove );
        }
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
     * @see org.polyglotter.transformation.Operation#setInput(javax.xml.namespace.QName, java.lang.Object[])
     */
    @Override
    public void setInput( final QName descriptorId,
                          final Object... value ) throws PolyglotterException {
        if ( ( ( valuesBeingAdded == null ) || ( valuesBeingAdded.length == 0 ) ) && isValidInputDescriptorId( descriptorId ) ) {
            this.inputs.remove( descriptorId );
            this.logger.debug( "Input values for descriptor '%s' were removed", descriptorId );
        } else if ( !isValidInputDescriptorId( descriptorId ) ) {
            throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingOperationInput,
                                            name(),
                                            transformationId() );
        }
        CheckArg.notNull( descriptorId, "descriptorId" );
        int index = 0;

        for ( final ValueDescriptor< ? > descriptor : inputDescriptors() ) {
            final int numRequired = descriptor.requiredValueCount();

            if ( descriptor.id().equals( descriptorId ) ) {
                // TODO process

                break;
            } else {
                if ( descriptor().unbounded() ) {
                    // TODO error finding unbounded means we could not find matching descriptor
                }

                index += numRequired;
            }
        }
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
    protected QName transformationId() {
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

            try {
                oldValue = get();
            } catch ( final PolyglotterException e ) {
                // TODO add a problem
            }

            T newValue = null;

            try {
                newValue = calculate();

                if ( !ObjectUtil.equals( oldValue, newValue ) ) {
                    super.set( newValue );
                }
            } catch ( final PolyglotterException e ) {
                final ValidationProblem problem =
                    TransformationFactory.createError( descriptor().id(),
                                                       PolyglotterI18n.errorOnTermChanged.text( newValue,
                                                                                                descriptor().id() ) );
                problems().add( problem );
            }
        }
    }

}
