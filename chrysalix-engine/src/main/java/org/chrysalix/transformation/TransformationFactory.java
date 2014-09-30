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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.chrysalix.Chrysalix;
import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.chrysalix.ChrysalixLexicon;
import org.chrysalix.common.CheckArg;
import org.chrysalix.common.Logger;
import org.chrysalix.operation.BuiltInOperationDescriptorProvider;
import org.chrysalix.operation.ValueDescriptorImpl;
import org.chrysalix.transformation.ValidationProblem.Severity;
import org.modelspace.Model;
import org.modelspace.ModelObject;
import org.modelspace.ModelProperty;
import org.modelspace.Modelspace;
import org.modelspace.ModelspaceException;

/**
 * A factory for creating {@link Transformation transformation}-related objects.
 */
public class TransformationFactory {

    private static final String ERROR_ADDING_TRANSFORMATION_OPERATION =
        "The operation '%s' could not be added to transformation '%s.'";
    private static final String ERROR_ADDING_TRANSFORMATION_SOURCE_MODEL =
        "A source model at path '%s' could not be added to transformation '%s.'";
    private static final String ERROR_ADDING_TRANSFORMATION_SOURCE_MODELS =
        "An error occurred adding source models to transformation '%s.'";
    private static final String ERROR_ADDING_TRANSFORMATION_TARGET_MODEL =
        "A target model at path '%s' could not be added to transformation '%s.'";
    private static final String ERROR_ADDING_TRANSFORMATION_TARGET_MODELS =
        "An error occurred adding target models to transformation '%s.'";
    private static final String ERROR_DESCRIPTOR_TYPE = "Error descriptor '%s' was not an operation descriptor";
    private static final String ERROR_FINDING_OPERATION_DESCRIPTOR = "Error trying to find operation descriptor with ID '%s'";
    private static final String ERROR_FINDING_OPERATIONS = "Unable to obtain operations for transformation model '%s'";
    private static final String ERROR_FINDING_SOURCE_MODELS = "Unable to obtain source models for transformation model '%s'";
    private static final String ERROR_FINDING_TARGET_MODELS = "Unable to obtain targets models for transformation model '%s'";
    private static final String ERROR_FINDING_TRANSFORMATION_MODEL_NAME =
        "Unable to determine transformation's name when adding operation '%s'";
    private static final String ERROR_REMOVING_TRANSFORMATION_OPERATION =
        "The operation '%s' could not be removed from transformation '%s.'";
    private static final String ERROR_REMOVING_TRANSFORMATION_SOURCE_MODELS =
        "An error occurred removing source models from transformation '%s.'";
    private static final String ERROR_REMOVING_TRANSFORMATION_TARGET_MODELS =
        "An error occurred removing target models from transformation '%s.'";
    private static final String ERROR_REMOVING_UNADDED_TRANSFORMATION_SOURCE_MODEL =
        "The source model at path '%s' could not be removed from transformation '%s' as it has never been added.";
    private static final String ERROR_REMOVING_UNADDED_TRANSFORMATION_TARGET_MODEL =
        "The target model at path '%s' could not be removed from transformation '%s' as it has never been added.";
    private static final String NEW_TRANSFORMATION_NAME = "New Transformation";
    private static final String UNABLE_TO_FIND_TRANSFORMATION_ID = "Unble to find transformation path";
    private static final String UNABLE_TO_FIND_TRANSFORMATION_ID_OR_NAME = "Unable to find transformation path or name";
    private static final String UNKNOWN_NAME = "**Unknown**";

    private static final OperationDescriptorProvider BUILT_IN_OP_PROVIDER = new BuiltInOperationDescriptorProvider();
    static final Logger LOGGER = Logger.logger( TransformationFactory.class );

    /**
     * A descriptor registry
     */
    public static final Map< Modelspace, TransformationFactory > REGISTRY = new HashMap<>();

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
     * Creates an ID using the {@link Chrysalix#NAMESPACE_PREFIX} and {@link Chrysalix#NAMESPACE_URI}.
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
     * @return an empty validation problems collection (never <code>null</code>)
     */
    public static ValidationProblems createValidationProblems() {
        return new Problems();
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

    private final Modelspace modeler;
    private final Set< OperationDescriptorProvider > operationProviders;
    private final Set< ValueDescriptor< ? >> descriptors;

    /**
     * @param factoryModeler
     *        the modelspace that this factory is working with (cannot be <code>null</code>)
     */
    public TransformationFactory( final Modelspace factoryModeler ) {
        CheckArg.notNull( factoryModeler, "factoryModeler" );
        this.modeler = factoryModeler;
        this.descriptors = new HashSet<>();

        this.operationProviders = new HashSet<>( 5 );
        addOperationProvider( BUILT_IN_OP_PROVIDER );

        if ( !REGISTRY.containsKey( this.modeler ) ) {
            REGISTRY.put( this.modeler, this );
        }
    }

    /**
     * @param provider
     *        the provider being added (cannot be <code>null</code>)
     */
    public void addOperationProvider( final OperationDescriptorProvider provider ) {
        CheckArg.notNull( provider, "provider" );

        if ( this.operationProviders.add( provider ) ) {
            for ( final ValueDescriptor< ? > descriptor : provider.descriptors() ) {
                this.descriptors.add( descriptor );
            }
        }
    }

    /**
     * Creates a new transformation or retrieves the existing one.
     * 
     * @param transformationModel
     *        the transformation model whose domain object is being created (cannot be <code>null</code> or empty)
     * @return the transformation model domain object (never <code>null</code>)
     * @throws ModelspaceException
     *         if an error occurs
     * @throws IllegalArgumentException
     *         if model is <code>null</code> or if it is not a transformation model
     */
    public Transformation createTransformation( final Model transformationModel ) throws ModelspaceException {
        CheckArg.notNull( transformationModel, "transformationModel" );
        CheckArg.notNull( transformationModel.metamodel().id(), Transformation.METAMODEL_ID );
        return new TransformationImpl( transformationModel );
    }

    /**
     * Creates a new transformation or retrieves the existing one.
     * 
     * @param path
     *        the path of the model, not including the name (cannot be <code>null</code> or empty)
     * @return the transformation model (never <code>null</code>)
     * @throws ModelspaceException
     *         if an error occurs
     * @throws IllegalArgumentException
     *         if path is <code>null</code> or empty
     */
    public Transformation createTransformation( final String path ) throws ModelspaceException {
        return new TransformationImpl( this.modeler, path );
    }

    /**
     * @param id
     *        the identifier of the descriptor being requested (cannot be <code>null</code> or empty)
     * @return the descriptor (never <code>null</code>)
     * @throws ChrysalixException
     *         if the descriptor cannot be found
     * 
     */
    public ValueDescriptor< ? > descriptor( final String id ) throws ChrysalixException {
        CheckArg.notEmpty( id, "id" );

        for ( final ValueDescriptor< ? > descriptor : descriptors() ) {
            if ( id.equals( descriptor.id() ) ) {
                return descriptor;
            }
        }

        throw new ChrysalixException( ChrysalixI18n.localize( ERROR_FINDING_OPERATION_DESCRIPTOR, id ) );
    }

    /**
     * @return an unmodifiable list of descriptors (never <code>null</code> or empty)
     */
    public Set< ValueDescriptor< ? > > descriptors() {
        return Collections.unmodifiableSet( this.descriptors );
    }

    /**
     * @return the modeler associated with this factory (never <code>null</code>)
     */
    protected Modelspace modelspace() {
        return this.modeler;
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
         * @see org.chrysalix.transformation.ValidationProblem#isError()
         */
        @Override
        public boolean isError() {
            return ( this.severity == Severity.ERROR );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.ValidationProblem#isInfo()
         */
        @Override
        public boolean isInfo() {
            return ( this.severity == Severity.INFO );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.ValidationProblem#isOk()
         */
        @Override
        public boolean isOk() {
            return ( this.severity == Severity.OK );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.ValidationProblem#isWarning()
         */
        @Override
        public boolean isWarning() {
            return ( this.severity == Severity.WARNING );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.ValidationProblem#message()
         */
        @Override
        public String message() {
            return this.message;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.ValidationProblem#severity()
         */
        @Override
        public Severity severity() {
            return this.severity;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.ValidationProblem#sourceId()
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
         * @see org.chrysalix.transformation.ValidationProblems#isError()
         */
        @Override
        public boolean isError() {
            return ( this.severity == Severity.ERROR );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.ValidationProblems#isInfo()
         */
        @Override
        public boolean isInfo() {
            return ( this.severity == Severity.INFO );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.ValidationProblems#isOk()
         */
        @Override
        public boolean isOk() {
            return ( this.severity == Severity.OK );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.ValidationProblems#isWarning()
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

        private final Model model;

        /**
         * Constructs an existing transformation model.
         * 
         * @param model
         *        the transformation model (cannot be <code>null</code>)
         */
        TransformationImpl( final Model model ) {
            CheckArg.notNull( model, "model" );
            this.model = model;
        }

        /**
         * Creates a new transformation model.
         * 
         * @param modeler
         *        the modeler used to create the transformation model (cannot be <code>null</code>)
         * @param parentPath
         *        the path of the container where the new transformation model will be created (cannot be <code>null</code> or
         *        empty)
         * @throws ModelspaceException
         *         if an error occurs
         */
        TransformationImpl( final Modelspace modeler,
                            final String parentPath ) throws ModelspaceException {
            CheckArg.notNull( modeler, "modeler" );
            CheckArg.notEmpty( parentPath, "parentPath" );
            this.model = modeler.newModel( parentPath + "/" + ChrysalixI18n.localize( NEW_TRANSFORMATION_NAME ),
                                           Transformation.METAMODEL_ID,
                                           false );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modelspace.ModelElement#absolutePath()
         */
        @Override
        public String absolutePath() throws ModelspaceException {
            return this.model.modelRelativePath();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.Transformation#add(org.chrysalix.transformation.Operation[])
         */
        @Override
        public void add( final Operation< ? >... operationsToAdd ) throws ChrysalixException {
            CheckArg.isNotEmpty( operationsToAdd, "operationsToAdd" );

            for ( final Operation< ? > operation : operationsToAdd ) {
                CheckArg.notNull( operation, "operation" );
                String opName = ChrysalixI18n.localize( UNKNOWN_NAME );

                try {
                    final ModelObject[] modelObjects = this.model.addChildOfType( ChrysalixLexicon.Operation.NODE_TYPE,
                                                                                  operation.descriptorId() );
                    final ModelObject operationModelObject = modelObjects[ 0 ];
                    opName = operationModelObject.name();
                    LOGGER.debug( "Added operation '%s' with descriptor '%s' to transformation '%s'",
                                  opName,
                                  operation.descriptorId(),
                                  id() );
                } catch ( final Exception e ) {
                    try {
                        throw new ChrysalixException( e,
                                                      ChrysalixI18n.localize( ERROR_ADDING_TRANSFORMATION_OPERATION,
                                                                              opName,
                                                                              this.model.name() ) );
                    } catch ( final ModelspaceException error ) {
                        throw new ChrysalixException( error,
                                                      ChrysalixI18n.localize( ERROR_FINDING_TRANSFORMATION_MODEL_NAME, opName ) );
                    }
                }
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.Transformation#addSource(org.modelspace.Model[])
         */
        @Override
        public void addSource( final Model... sources ) throws ChrysalixException {
            CheckArg.isNotEmpty( sources, "sources" );

            try {
                final String[] pathBeingAdded = new String[ sources.length ];

                for ( int i = 0; i < sources.length; ++i ) {
                    CheckArg.notNull( sources[ i ], "sources[" + i + ']' );
                    pathBeingAdded[ i ] = sources[ i ].absolutePath();
                }

                String[] sourcePaths = new String[ 0 ]; // current sources go here
                final ModelProperty property = this.model.property( ChrysalixLexicon.Transformation.SOURCES );

                // make sure no duplicates
                if ( property != null ) {
                    sourcePaths = property.stringValues();

                    for ( final Object path : pathBeingAdded ) {
                        for ( final Object source : sourcePaths ) {
                            if ( path.equals( source ) ) {
                                throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_TRANSFORMATION_SOURCE_MODEL,
                                                                                      path,
                                                                                      getName() ) );
                            }
                        }
                    }
                }

                final String[] newSources = new String[ pathBeingAdded.length + sourcePaths.length ];
                System.arraycopy( pathBeingAdded, 0, newSources, 0, pathBeingAdded.length );

                if ( sourcePaths.length != 0 ) {
                    System.arraycopy( sourcePaths, 0, newSources, pathBeingAdded.length, sourcePaths.length );
                }

                this.model.setProperty( ChrysalixLexicon.Transformation.SOURCES, ( Object[] ) newSources );
            } catch ( final Exception e ) {
                throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_TRANSFORMATION_SOURCE_MODELS,
                                                                      getName() ) );
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.Transformation#addTarget(org.modelspace.Model[])
         */
        @Override
        public void addTarget( final Model... targets ) throws ChrysalixException {
            CheckArg.isNotEmpty( targets, "targets" );

            try {
                final String[] pathsBeingAdded = new String[ targets.length ];

                for ( int i = 0; i < targets.length; ++i ) {
                    CheckArg.notNull( targets[ i ], "targets[" + i + ']' );
                    pathsBeingAdded[ i ] = targets[ i ].absolutePath();
                }

                String[] targetPaths = new String[ 0 ]; // current targets go here
                final ModelProperty property = this.model.property( ChrysalixLexicon.Transformation.TARGETS );

                // make sure no duplicates
                if ( property != null ) {
                    targetPaths = property.stringValues();

                    for ( final Object path : pathsBeingAdded ) {
                        for ( final Object target : targetPaths ) {
                            if ( path.equals( target ) ) {
                                throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_TRANSFORMATION_TARGET_MODEL,
                                                                                      path,
                                                                                      getName() ) );
                            }
                        }
                    }
                }

                final String[] newTargets = new String[ pathsBeingAdded.length + targetPaths.length ];
                System.arraycopy( pathsBeingAdded, 0, newTargets, 0, pathsBeingAdded.length );

                if ( targetPaths.length != 0 ) {
                    System.arraycopy( targetPaths, 0, newTargets, pathsBeingAdded.length, targetPaths.length );
                }

                this.model.setProperty( ChrysalixLexicon.Transformation.TARGETS, ( Object ) newTargets );
            } catch ( final Exception e ) {
                throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_TRANSFORMATION_TARGET_MODELS,
                                                                      getName() ) );
            }
        }

        private TransformationFactory factory() {
            return REGISTRY.get( this.model.modelspace() );
        }

        private String getName() {
            try {
                final String name = this.model.name();
                return name;
            } catch ( final ModelspaceException e ) {
                return ChrysalixI18n.localize( UNKNOWN_NAME );
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.Transformation#id()
         */
        @Override
        public String id() throws ChrysalixException {
            try {
                return model().absolutePath();
            } catch ( final ModelspaceException e ) {
                try {
                    throw new ChrysalixException( e, ChrysalixI18n.localize( UNABLE_TO_FIND_TRANSFORMATION_ID, name() ) );
                } catch ( final ModelspaceException error ) {
                    final ChrysalixException pe =
                        new ChrysalixException( ChrysalixI18n.localize( UNABLE_TO_FIND_TRANSFORMATION_ID_OR_NAME ) );
                    pe.addSuppressed( error );
                    throw pe;
                }
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Iterable#iterator()
         */
        @Override
        public Iterator< Operation< ? >> iterator() {
            try {
                final List< Operation< ? >> copy = Arrays.asList( operations() );
                return copy.iterator();
            } catch ( final Exception e ) {
                throw new RuntimeException( e );
            }
        }

        @Override
        public Model model() {
            return this.model;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modelspace.ModelElement#modelRelativePath()
         */
        @Override
        public String modelRelativePath() throws ModelspaceException {
            return this.model.modelRelativePath();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modelspace.ModelElement#name()
         */
        @Override
        public String name() throws ModelspaceException {
            return this.model.name();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.Transformation#operations()
         */
        @Override
        public Operation< ? >[] operations() throws ChrysalixException {
            final List< Operation< ? > > operations = new ArrayList<>();

            try {
                for ( final ModelObject kid : this.model.childrenOfType( ChrysalixLexicon.Operation.NODE_TYPE ) ) {
                    final ValueDescriptor< ? > descriptor = factory().descriptor( kid.name() );

                    if ( descriptor instanceof OperationDescriptor ) {
                        final Operation< ? > operation = ( ( OperationDescriptor< ? > ) descriptor ).newInstance( kid, this );
                        operations.add( operation );
                    } else {
                        throw new ChrysalixException( ChrysalixI18n.localize( ERROR_DESCRIPTOR_TYPE, descriptor.id() ) );
                    }
                }
            } catch ( final ModelspaceException e ) {
                throw new ChrysalixException( e, ChrysalixI18n.localize( ERROR_FINDING_OPERATIONS, getName() ) );
            }

            return operations.toArray( new Operation< ? >[ operations.size() ] );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.Transformation#remove(org.chrysalix.transformation.Operation[])
         */
        @Override
        public void remove( final Operation< ? >... operationsToRemove ) throws ChrysalixException {
            CheckArg.isNotEmpty( operationsToRemove, "operationsToRemove" );

            for ( final Operation< ? > operation : operationsToRemove ) {
                CheckArg.notNull( operation, "operation" );

                try {
                    this.model.removeChild( operation.descriptorId() );
                } catch ( final Exception e ) {
                    throw new ChrysalixException( e,
                                                  ChrysalixI18n.localize( ERROR_REMOVING_TRANSFORMATION_OPERATION,
                                                                          operation.descriptorId(),
                                                                          getName() ) );
                }
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.Transformation#removeSource(org.modelspace.Model[])
         */
        @Override
        public void removeSource( final Model... sources ) throws ChrysalixException {
            CheckArg.isNotEmpty( sources, "sources" );

            try {
                final Object[] pathsBeingRemoved = new Object[ sources.length ];

                for ( int i = 0; i < sources.length; ++i ) {
                    CheckArg.notNull( sources[ i ], "targets[" + i + ']' );
                    pathsBeingRemoved[ i ] = sources[ i ].absolutePath();
                }

                if ( !this.model.hasProperty( ChrysalixLexicon.Transformation.SOURCES ) ) {
                    throw new ChrysalixException( ChrysalixI18n.localize( ERROR_REMOVING_TRANSFORMATION_SOURCE_MODELS,
                                                                          getName() ) );
                }

                final ModelProperty property = this.model.property( ChrysalixLexicon.Transformation.SOURCES );

                // remove paths from current paths
                final List< Object > newSourcePaths = new ArrayList<>( Arrays.asList( property.values() ) );

                for ( final Object path : pathsBeingRemoved ) {
                    if ( !newSourcePaths.remove( path ) ) {
                        // not found to remove
                        throw new ChrysalixException( ChrysalixI18n.localize( ERROR_REMOVING_UNADDED_TRANSFORMATION_SOURCE_MODEL,
                                                                              path,
                                                                              getName() ) );
                    }
                }

                final Object[] newValue = ( newSourcePaths.isEmpty() ? null : newSourcePaths.toArray() );
                this.model.setProperty( ChrysalixLexicon.Transformation.SOURCES, newValue );
            } catch ( final Exception e ) {
                throw new ChrysalixException( ChrysalixI18n.localize( ERROR_REMOVING_TRANSFORMATION_SOURCE_MODELS,
                                                                      getName() ) );
            }
        }

        @Override
        public void removeTarget( final Model... targets ) throws ChrysalixException {
            CheckArg.isNotEmpty( targets, "targets" );

            try {
                final Object[] pathsBeingRemoved = new Object[ targets.length ];

                for ( int i = 0; i < targets.length; ++i ) {
                    CheckArg.notNull( targets[ i ], "targets[" + i + ']' );
                    pathsBeingRemoved[ i ] = targets[ i ].absolutePath();
                }

                if ( !this.model.hasProperty( ChrysalixLexicon.Transformation.TARGETS ) ) {
                    throw new ChrysalixException( ChrysalixI18n.localize( ERROR_REMOVING_TRANSFORMATION_TARGET_MODELS,
                                                                          getName() ) );
                }

                final ModelProperty property = this.model.property( ChrysalixLexicon.Transformation.TARGETS );

                // remove paths from current paths
                final List< Object > newTargetPaths = new ArrayList<>( Arrays.asList( property.values() ) );

                for ( final Object path : pathsBeingRemoved ) {
                    if ( !newTargetPaths.remove( path ) ) {
                        // not found to remove
                        throw new ChrysalixException( ChrysalixI18n.localize( ERROR_REMOVING_UNADDED_TRANSFORMATION_TARGET_MODEL,
                                                                              path,
                                                                              getName() ) );
                    }
                }

                final Object[] newValue = ( newTargetPaths.isEmpty() ? null : newTargetPaths.toArray() );
                this.model.setProperty( ChrysalixLexicon.Transformation.TARGETS, newValue );
            } catch ( final Exception e ) {
                throw new ChrysalixException( ChrysalixI18n.localize( ERROR_REMOVING_TRANSFORMATION_TARGET_MODELS,
                                                                      getName() ) );
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.Transformation#sources()
         */
        @Override
        public Model[] sources() throws ChrysalixException {
            try {
                if ( this.model.hasProperty( ChrysalixLexicon.Transformation.SOURCES ) ) {
                    final String[] sourceModelPaths =
                        this.model.property( ChrysalixLexicon.Transformation.SOURCES ).stringValues();
                    final Set< Model > sources = new HashSet<>( sourceModelPaths.length );
                    final Modelspace modeler = this.model.modelspace();

                    for ( final String path : sourceModelPaths ) {
                        final Model source = modeler.model( path );

                        if ( source != null ) {
                            sources.add( source );
                        } else {
                            // TODO maybe create a proxy model or missing model???
                            LOGGER.debug( "Source model at path '%s' was not found", path );
                        }
                    }

                    return sources.toArray( new Model[ sources.size() ] );
                }

                return Model.NO_MODELS;
            } catch ( final ModelspaceException e ) {
                throw new ChrysalixException( e, ChrysalixI18n.localize( ERROR_FINDING_SOURCE_MODELS, getName() ) );
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.chrysalix.transformation.Transformation#targets()
         */
        @Override
        public Model[] targets() throws ChrysalixException {
            try {
                if ( this.model.hasProperty( ChrysalixLexicon.Transformation.TARGETS ) ) {
                    final String[] targetModelPaths =
                        this.model.property( ChrysalixLexicon.Transformation.TARGETS ).stringValues();
                    final Set< Model > targets = new HashSet<>( targetModelPaths.length );
                    final Modelspace modeler = this.model.modelspace();

                    for ( final String path : targetModelPaths ) {
                        final Model target = modeler.model( path );

                        if ( target != null ) {
                            targets.add( target );
                        } else {
                            // TODO maybe create a proxy model or missing model???
                            LOGGER.debug( "Target model at path '%s' was not found", path );
                        }
                    }

                    return targets.toArray( new Model[ targets.size() ] );
                }

                return Model.NO_MODELS;
            } catch ( final ModelspaceException e ) {
                throw new ChrysalixException( e, ChrysalixI18n.localize( ERROR_FINDING_TARGET_MODELS, getName() ) );
            }
        }

    }

}
