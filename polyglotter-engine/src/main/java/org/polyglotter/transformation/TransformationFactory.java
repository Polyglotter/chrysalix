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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.modeshape.modeler.Model;
import org.modeshape.modeler.ModelerException;
import org.polyglotter.Polyglotter;
import org.polyglotter.PolyglotterException;
import org.polyglotter.PolyglotterI18n;
import org.polyglotter.common.CheckArg;
import org.polyglotter.operation.BuiltInOperationDescriptorProvider;
import org.polyglotter.operation.ValueDescriptorImpl;
import org.polyglotter.operation.ValueImpl;
import org.polyglotter.transformation.ValidationProblem.Severity;

/**
 * A factory for creating {@link Transformation transformation}-related objects.
 */
public final class TransformationFactory {

    private static final OperationDescriptorProvider OP_PROVIDER = new BuiltInOperationDescriptorProvider();

    /**
     * @param transformationId
     *        the source transformation's identifier (cannot be <code>null</code> or empty)
     * @param message
     *        the problem message (can be <code>null</code> or empty)
     * @return the problem (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the transformation identifier is <code>null</code>
     */
    public static ValidationProblem createError( final String transformationId,
                                                 final String message ) {
        CheckArg.notEmpty( transformationId, "transformationId" );
        return new Problem( Severity.ERROR, transformationId, message );
    }

    /**
     * Creates an ID using the {@link Polyglotter#NAMESPACE_PREFIX} and {@link Polyglotter#NAMESPACE_URI}.
     * 
     * @param clazz
     *        the class whose name will be used to construct the identifier (cannot be <code>null</code>)
     * @return the ID (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the class is <code>null</code>
     */
    public static String createId( final Class< ? > clazz ) {
        return createId( clazz, null );
    }

    /**
     * @param clazz
     *        the class whose name will be used to construct the identifier (cannot be <code>null</code>)
     * @param suffix
     *        the identifier's suffix (can be <code>null</code> or empty)
     * @return the ID (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the class is <code>null</code>
     */
    public static String createId( final Class< ? > clazz,
                                   final String suffix ) {
        CheckArg.notNull( clazz, "clazz" );

        final StringBuilder result = new StringBuilder( clazz.getName() );

        if ( ( suffix != null ) && !suffix.isEmpty() ) {
            return result.append( '.' ).append( suffix ).toString();
        }

        return result.toString();
    }

    /**
     * @param transformationId
     *        the source transformation's identifier (cannot be <code>null</code>)
     * @param message
     *        the problem message (can be <code>null</code> or empty)
     * @return the problem (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the transformation identifier is <code>null</code>
     */
    public static ValidationProblem createInfo( final String transformationId,
                                                final String message ) {
        CheckArg.notEmpty( transformationId, "transformationId" );
        return new Problem( Severity.INFO, transformationId, message );
    }

    /**
     * @param transformationId
     *        the source transformation's identifier (cannot be <code>null</code>)
     * @param message
     *        the problem message (can be <code>null</code> or empty)
     * @return the problem (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the transformation identifier is <code>null</code>
     */
    public static ValidationProblem createOk( final String transformationId,
                                              final String message ) {
        CheckArg.notEmpty( transformationId, "transformationId" );
        return new Problem( Severity.OK, transformationId, message );
    }

    /**
     * @param id
     *        the transformation identifier (cannot be <code>null</code> or empty)
     * @return the transformation (never <code>null</code>)
     */
    public static Transformation createTransformation( final String id ) {
        return new TransformationImpl( id );
    }

    /**
     * @return an empty validation problems collection (never <code>null</code>)
     */
    public static ValidationProblems createValidationProblems() {
        return new Problems();
    }

    /**
     * @param descriptor
     *        the value descriptor (cannot be <code>null</code>)
     * @param initialValue
     *        the initial value
     * @return the new double value (never <code>null</code>)
     * @throws PolyglotterException
     *         if there is a problem setting the initial value
     */
    public static Value< Double > createValue( final ValueDescriptor< Double > descriptor,
                                               final double initialValue ) throws PolyglotterException {
        return new DoubleValue( descriptor, initialValue );
    }

    /**
     * @param descriptor
     *        the value descriptor (cannot be <code>null</code>)
     * @param initialValue
     *        the initial value
     * @return the new double value (never <code>null</code>)
     * @throws PolyglotterException
     *         if there is a problem setting the initial value
     */
    public static Value< Float > createValue( final ValueDescriptor< Float > descriptor,
                                              final float initialValue ) throws PolyglotterException {
        return new FloatValue( descriptor, initialValue );
    }

    /**
     * @param descriptor
     *        the value descriptor (cannot be <code>null</code>)
     * @param initialValue
     *        the initial value
     * @return the new double value (never <code>null</code>)
     * @throws PolyglotterException
     *         if there is a problem setting the initial value
     */
    public static Value< Integer > createValue( final ValueDescriptor< Integer > descriptor,
                                                final int initialValue ) throws PolyglotterException {
        return new IntegerValue( descriptor, initialValue );
    }

    /**
     * @param descriptor
     *        the value descriptor (cannot be <code>null</code>)
     * @param initialValue
     *        the initial value
     * @return the new double value (never <code>null</code>)
     * @throws PolyglotterException
     *         if there is a problem setting the initial value
     */
    public static Value< Long > createValue( final ValueDescriptor< Long > descriptor,
                                             final long initialValue ) throws PolyglotterException {
        return new LongValue( descriptor, initialValue );
    }

    /**
     * @param descriptor
     *        the value descriptor (cannot be <code>null</code>)
     * @return the new value (never <code>null</code>)
     */
    public static < T > Value< T > createValue( final ValueDescriptor< T > descriptor ) {
        CheckArg.notNull( descriptor, "descriptor" );
        return new ValueImpl<>( descriptor );
    }

    /**
     * @param descriptor
     *        the value descriptor (cannot be <code>null</code>)
     * @param value
     *        the initial value (can be <code>null</code>)
     * @return the new value (never <code>null</code>)
     * @throws PolyglotterException
     *         if an error occurs
     * @throws UnsupportedOperationException
     *         if the descriptor is read-only
     */
    public static < T > Value< T > createValue( final ValueDescriptor< T > descriptor,
                                                final T value ) throws PolyglotterException {
        final Value< T > result = createValue( descriptor );
        result.set( value );

        return result;
    }

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
     * @return the value descriptor (never <code>null</code>)
     */
    public static < T > ValueDescriptor< T > createValueDescriptor( final String valueId,
                                                                    final String valueDescription,
                                                                    final String valueName,
                                                                    final Class< T > valueType,
                                                                    final boolean isModifiable,
                                                                    final int requiredValueCount,
                                                                    final boolean isUnbounded ) {
        return new ValueDescriptorImpl<>( valueId,
                                          valueDescription,
                                          valueName,
                                          valueType,
                                          isModifiable,
                                          requiredValueCount,
                                          isUnbounded );
    }

    /**
     * @param transformationId
     *        the source transformation's identifier (cannot be <code>null</code> or empty)
     * @param message
     *        the problem message (can be <code>null</code> or empty)
     * @return the problem (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the transformation identifier is <code>null</code>
     */
    public static ValidationProblem createWarning( final String transformationId,
                                                   final String message ) {
        CheckArg.notEmpty( transformationId, "transformationId" );
        return new Problem( Severity.WARNING, transformationId, message );
    }

    /**
     * Creates a descriptor that is modifiable, requires one value, and is limited to one value.
     * 
     * @param valueId
     *        the value identifier (cannot be <code>null</code> or empty)
     * @param valueDescription
     *        the value description (cannot be <code>null</code> or empty)
     * @param valueName
     *        the value name (cannot be <code>null</code> or empty)
     * @param valueType
     *        the value type (cannot be <code>null</code>)
     * @return the value descriptor (never <code>null</code>)
     */
    public static < T > ValueDescriptor< T > createWritableBoundedOneValueDescriptor( final String valueId,
                                                                                      final String valueDescription,
                                                                                      final String valueName,
                                                                                      final Class< T > valueType ) {
        return createValueDescriptor( valueId, valueDescription, valueName, valueType, true, 1, false );
    }

    /**
     * @return an unmodifiable list of descriptors sorted by operation name (never <code>null</code> or empty)
     */
    public static List< OperationDescriptor< ? > > descriptors() {
        return OP_PROVIDER.descriptors();
    }

    /**
     * Don't allow construction outside this class.
     */
    private TransformationFactory() {
        // nothing to do
    }

    private static class DoubleValue extends ValueImpl< Double > {

        /**
         * @param descriptor
         *        the value descriptor (cannot be <code>null</code>)
         */
        public DoubleValue( final ValueDescriptor< Double > descriptor ) {
            super( descriptor );
        }

        /**
         * @param descriptor
         *        the value descriptor (cannot be <code>null</code>)
         * @param initialValue
         *        the initial value (can be <code>null</code>)
         * @throws PolyglotterException
         *         if there is a problem setting the initial value
         */
        public DoubleValue( final ValueDescriptor< Double > descriptor,
                            final double initialValue ) throws PolyglotterException {
            this( descriptor );
            set( initialValue );
        }

    }

    private static class FloatValue extends ValueImpl< Float > {

        /**
         * @param descriptor
         *        the value descriptor (cannot be <code>null</code>)
         */
        public FloatValue( final ValueDescriptor< Float > descriptor ) {
            super( descriptor );
        }

        /**
         * @param descriptor
         *        the value descriptor (cannot be <code>null</code>)
         * @param initialValue
         *        the initial value (can be <code>null</code>)
         * @throws PolyglotterException
         *         if there is a problem setting the initial value
         */
        public FloatValue( final ValueDescriptor< Float > descriptor,
                           final float initialValue ) throws PolyglotterException {
            this( descriptor );
            set( initialValue );
        }

    }

    private static class IntegerValue extends ValueImpl< Integer > {

        /**
         * @param descriptor
         *        the value descriptor (cannot be <code>null</code>)
         */
        public IntegerValue( final ValueDescriptor< Integer > descriptor ) {
            super( descriptor );
        }

        /**
         * @param descriptor
         *        the value descriptor (cannot be <code>null</code>)
         * @param initialValue
         *        the initial value (can be <code>null</code>)
         * @throws PolyglotterException
         *         if there is a problem setting the initial value
         */
        public IntegerValue( final ValueDescriptor< Integer > descriptor,
                             final int initialValue ) throws PolyglotterException {
            this( descriptor );
            set( initialValue );
        }

    }

    private static class LongValue extends ValueImpl< Long > {

        /**
         * @param descriptor
         *        the value descriptor (cannot be <code>null</code>)
         */
        public LongValue( final ValueDescriptor< Long > descriptor ) {
            super( descriptor );
        }

        /**
         * @param descriptor
         *        the value descriptor (cannot be <code>null</code>)
         * @param initialValue
         *        the initial value (can be <code>null</code>)
         * @throws PolyglotterException
         *         if there is a problem setting the initial value
         */
        public LongValue( final ValueDescriptor< Long > descriptor,
                          final long initialValue ) throws PolyglotterException {
            this( descriptor );
            set( initialValue );
        }

    }

    private static final class Problem implements ValidationProblem {

        private final Severity severity;
        private final String message;
        private final String sourceId;

        Problem( final Severity problemSeverity,
                 final String problemPartId,
                 final String problemMessage ) {
            this.severity = problemSeverity;
            this.message = problemMessage;
            this.sourceId = problemPartId;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#isError()
         */
        @Override
        public boolean isError() {
            return ( this.severity == Severity.ERROR );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#isInfo()
         */
        @Override
        public boolean isInfo() {
            return ( this.severity == Severity.INFO );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#isOk()
         */
        @Override
        public boolean isOk() {
            return ( this.severity == Severity.OK );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#isWarning()
         */
        @Override
        public boolean isWarning() {
            return ( this.severity == Severity.WARNING );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#message()
         */
        @Override
        public String message() {
            return this.message;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#severity()
         */
        @Override
        public Severity severity() {
            return this.severity;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#sourceId()
         */
        @Override
        public String sourceId() {
            return this.sourceId;
        }

    }

    private static final class Problems extends ArrayList< ValidationProblem > implements ValidationProblems {

        private Severity severity;

        Problems() {
            this.severity = Severity.OK;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#add(int, java.lang.Object)
         */
        @Override
        public void add( final int index,
                         final ValidationProblem problem ) {
            super.add( index, problem );
            updateSeverity( problem );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#add(java.lang.Object)
         */
        @Override
        public boolean add( final ValidationProblem problem ) {
            final boolean added = super.add( problem );

            if ( added ) {
                updateSeverity( problem );
            }

            return added;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#addAll(java.util.Collection)
         */
        @Override
        public boolean addAll( final Collection< ? extends ValidationProblem > c ) {
            if ( this.severity != Severity.ERROR ) {
                for ( final ValidationProblem problem : c ) {
                    updateSeverity( problem );

                    if ( this.severity == Severity.ERROR ) {
                        break;
                    }
                }
            }

            return super.addAll( c );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#addAll(int, java.util.Collection)
         */
        @Override
        public boolean addAll( final int index,
                               final Collection< ? extends ValidationProblem > c ) {
            if ( this.severity != Severity.ERROR ) {
                for ( final ValidationProblem problem : c ) {
                    updateSeverity( problem );

                    if ( this.severity == Severity.ERROR ) {
                        break;
                    }
                }
            }

            return super.addAll( index, c );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#clear()
         */
        @Override
        public void clear() {
            super.clear();
            this.severity = Severity.OK;
        }

        private void determineSeverity() {
            this.severity = Severity.OK;

            for ( final ValidationProblem problem : this ) {
                updateSeverity( problem );

                if ( this.severity == Severity.ERROR ) {
                    break;
                }
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblems#isError()
         */
        @Override
        public boolean isError() {
            return ( this.severity == Severity.ERROR );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblems#isInfo()
         */
        @Override
        public boolean isInfo() {
            return ( this.severity == Severity.INFO );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblems#isOk()
         */
        @Override
        public boolean isOk() {
            return ( this.severity == Severity.OK );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblems#isWarning()
         */
        @Override
        public boolean isWarning() {
            return ( this.severity == Severity.WARNING );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#remove(int)
         */
        @Override
        public ValidationProblem remove( final int index ) {
            final ValidationProblem problem = super.remove( index );

            if ( this.severity == problem.severity() ) {
                determineSeverity();
            }

            return problem;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#remove(java.lang.Object)
         */
        @Override
        public boolean remove( final Object o ) {
            final boolean removed = super.remove( o );

            if ( removed ) {
                final ValidationProblem problem = ( ValidationProblem ) o;

                if ( problem.severity() == this.severity ) {
                    determineSeverity();
                }
            }

            return removed;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#removeAll(java.util.Collection)
         */
        @Override
        public boolean removeAll( final Collection< ? > c ) {
            final boolean changed = super.removeAll( c );

            if ( changed ) {
                determineSeverity();
            }

            return changed;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#retainAll(java.util.Collection)
         */
        @Override
        public boolean retainAll( final Collection< ? > c ) {
            final boolean changed = super.retainAll( c );

            if ( changed ) {
                determineSeverity();
            }

            return changed;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#set(int, java.lang.Object)
         */
        @Override
        public ValidationProblem set( final int index,
                                      final ValidationProblem newProblem ) {
            final ValidationProblem oldProblem = super.set( index, newProblem );

            if ( newProblem.severity().isMoreSevereThan( this.severity ) ) {
                this.severity = newProblem.severity();
            } else if ( oldProblem.severity() == this.severity ) {
                determineSeverity();
            }

            return oldProblem;
        }

        private void updateSeverity( final ValidationProblem problem ) {
            if ( problem.severity().isMoreSevereThan( this.severity ) ) {
                this.severity = problem.severity();
            }
        }

    }

    private static class TransformationImpl implements Transformation {

        private final String id;
        private final Map< Model, ModelType > models;
        private final List< Operation< ? > > operations;

        /**
         * @param tranformationId
         *        the transformation identifier (cannot be <code>null</code> or empty)
         */
        public TransformationImpl( final String tranformationId ) {
            CheckArg.notEmpty( tranformationId, "tranformationId" );
            this.id = tranformationId;
            this.models = new HashMap<>();
            this.operations = new ArrayList<>();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#add(org.polyglotter.transformation.Transformation.ModelType,
         *      org.modeshape.modeler.Model[])
         */
        @Override
        public void add( final ModelType modelType,
                         final Model... modelsBeingAdded ) throws PolyglotterException {
            CheckArg.notNull( modelType, "modelType" );
            CheckArg.isNotEmpty( modelsBeingAdded, "modelsBeingAdded" );

            for ( final Model model : modelsBeingAdded ) {
                if ( model == null ) {
                    throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingNullTransformationModel,
                                                    id() );
                }

                final ModelType currentType = this.models.get( model );

                // if not found just add it
                if ( currentType == null ) {
                    this.models.put( model, modelType );
                } else {
                    // model has already been added with that type
                    if ( ( currentType == ModelType.SOURCE_TARGET ) || ( currentType == modelType ) ) {
                        String modelName = null;

                        try {
                            modelName = model.name();
                        } catch ( final ModelerException e ) {
                            throw new PolyglotterException( e,
                                                            PolyglotterI18n.errorAddingTransformationModel,
                                                            id(),
                                                            modelType.name() );
                        }

                        throw new PolyglotterException( PolyglotterI18n.errorAddingTransformationModelWithName,
                                                        modelName,
                                                        id(),
                                                        modelType.name() );
                    }

                    // added either source or target so now model can be used as both
                    this.models.put( model, ModelType.SOURCE_TARGET );
                }
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#add(org.polyglotter.transformation.Operation[])
         */
        @Override
        public void add( final Operation< ? >... operationsToAdd ) throws PolyglotterException {
            CheckArg.isNotEmpty( operationsToAdd, "operationsToAdd" );

            for ( final Operation< ? > operation : operationsToAdd ) {
                if ( operation == null ) {
                    throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingNullTransformationOperation,
                                                    id() );
                }

                if ( this.operations.contains( operation ) || !this.operations.add( operation ) ) {
                    throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingTransformationOperation,
                                                    operation.descriptor().id(),
                                                    id() );
                }
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#id()
         */
        @Override
        public String id() {
            return this.id;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Iterable#iterator()
         */
        @Override
        public Iterator< Operation< ? >> iterator() {
            final List< Operation< ? >> copy = Collections.unmodifiableList( this.operations );
            return copy.iterator();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#operations()
         */
        @Override
        public Operation< ? >[] operations() {
            return this.operations.toArray( new Operation< ? >[ this.operations.size() ] );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#remove(org.modeshape.modeler.Model[])
         */
        @Override
        public void remove( final Model... modelsBeingRemoved ) throws PolyglotterException {
            CheckArg.isNotEmpty( modelsBeingRemoved, "modelsBeingRemoved" );

            for ( final Model model : modelsBeingRemoved ) {
                if ( model == null ) {
                    throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingNullTransformationModel,
                                                    id() );
                }

                final ModelType currentType = this.models.get( model );

                // model was not added
                if ( currentType == null ) {
                    String modelName = null;

                    try {
                        modelName = model.name();
                    } catch ( final ModelerException e ) {
                        throw new PolyglotterException( e,
                                                        PolyglotterI18n.errorRemovingUnaddedTransformationModel,
                                                        id() );
                    }

                    throw new PolyglotterException( PolyglotterI18n.errorRemovingUnaddedTransformationModelWithName,
                                                    modelName,
                                                    id() );
                }

                this.models.remove( model );
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#remove(org.polyglotter.transformation.Transformation.ModelType,
         *      org.modeshape.modeler.Model[])
         */
        @Override
        public void remove( final ModelType modelType,
                            final Model... modelsBeingRemoved ) throws PolyglotterException {
            CheckArg.notNull( modelType, "modelType" );
            CheckArg.isNotEmpty( modelsBeingRemoved, "modelsBeingRemoved" );

            for ( final Model model : modelsBeingRemoved ) {
                if ( model == null ) {
                    throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingNullTransformationModel,
                                                    id() );
                }

                final ModelType currentType = this.models.get( model );

                // model was not added
                if ( currentType == null ) {
                    String modelName = null;

                    try {
                        modelName = model.name();
                    } catch ( final ModelerException e ) {
                        throw new PolyglotterException( e,
                                                        PolyglotterI18n.errorRemovingUnaddedTransformationModel,
                                                        id() );
                    }

                    throw new PolyglotterException( PolyglotterI18n.errorRemovingUnaddedTransformationModelWithName,
                                                    modelName,
                                                    id() );
                }

                // model had been previously added so can be removed
                if ( currentType == modelType ) {
                    this.models.remove( model );
                } else if ( currentType == ModelType.SOURCE_TARGET ) {
                    if ( modelType == ModelType.SOURCE ) {
                        this.models.put( model, ModelType.TARGET );
                    } else {
                        this.models.put( model, ModelType.SOURCE );
                    }
                } else {
                    String modelName = null;

                    try {
                        modelName = model.name();
                    } catch ( final ModelerException e ) {
                        throw new PolyglotterException( e,
                                                        PolyglotterI18n.errorRemovingTransformationModel,
                                                        id(),
                                                        modelType.name() );
                    }

                    throw new PolyglotterException( PolyglotterI18n.errorRemovingTransformationModelWithName,
                                                    modelName,
                                                    id(),
                                                    modelType.name() );
                }
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#remove(org.polyglotter.transformation.Operation[])
         */
        @Override
        public void remove( final Operation< ? >... operationsToRemove ) throws PolyglotterException {
            CheckArg.isNotEmpty( operationsToRemove, "operationsToRemove" );

            for ( final Operation< ? > operation : operationsToRemove ) {
                if ( operation == null ) {
                    throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingNullTransformationOperation,
                                                    id() );
                }

                if ( !this.operations.remove( operation ) ) {
                    throw new PolyglotterException( PolyglotterI18n.errorAddingOrRemovingTransformationOperation,
                                                    operation.descriptor().id(),
                                                    id() );
                }
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#sources()
         */
        @Override
        public Model[] sources() {
            if ( this.models.isEmpty() ) {
                return Model.NO_MODELS;
            }

            final List< Model > sources = new ArrayList<>();

            for ( final Map.Entry< Model, ModelType > entry : this.models.entrySet() ) {
                final ModelType modelType = entry.getValue();

                if ( ModelType.isSource( modelType ) ) {
                    sources.add( entry.getKey() );
                }
            }

            return sources.toArray( new Model[ sources.size() ] );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.Transformation#targets()
         */
        @Override
        public Model[] targets() {
            if ( this.models.isEmpty() ) {
                return Model.NO_MODELS;
            }

            final List< Model > targets = new ArrayList<>();

            for ( final Map.Entry< Model, ModelType > entry : this.models.entrySet() ) {
                final ModelType modelType = entry.getValue();

                if ( ModelType.isTarget( modelType ) ) {
                    targets.add( entry.getKey() );
                }
            }

            return targets.toArray( new Model[ targets.size() ] );
        }

    }

}
