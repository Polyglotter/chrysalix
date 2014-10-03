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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.chrysalix.ChrysalixLexicon;
import org.chrysalix.common.CheckArg;
import org.chrysalix.common.Logger;
import org.chrysalix.transformation.Operation;
import org.chrysalix.transformation.OperationDescriptor;
import org.chrysalix.transformation.Transformation;
import org.chrysalix.transformation.TransformationFactory;
import org.chrysalix.transformation.ValidationProblems;
import org.chrysalix.transformation.Value;
import org.chrysalix.transformation.ValueDescriptor;
import org.modelspace.ModelElement;
import org.modelspace.ModelObject;
import org.modelspace.ModelProperty;
import org.modelspace.ModelspaceException;

/**
 * A base class implementation for an {@link Operation operation}.
 * 
 * @param <T>
 *        the operation's result type
 */
abstract class AbstractOperation< T > extends ValueImpl< T > implements Operation< T > {

    protected static final String ERROR_ADDING_OR_REMOVING_OPERATION_INPUT =
        "There was an error adding or removing terms for operation '%s' in transformation '%s' using descriptor '%s'";
    protected static final String ERROR_ADDING_OR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME =
        "There was an error adding or removing terms for an operation (name cannot be determined) in transformation '%s' using descriptor '%s'";
    private static final String ERROR_FINDING_ALL_INPUTS =
        "There was an error obtaining the inputs for operation '%s' in transformation '%s.'";
    private static final String ERROR_FINDING_ALL_INPUTS_UNKNOWN_NAME =
        "There was an error obtaining the inputs for operation (name cannot be determined) in transformation '%s.'";
    private static final String ERROR_FINDING_DESCRIPTOR_INPUTS =
        "There was an error obtaining the inputs for descriptor '%s' of operation '%s' in transformation '%s.'";
    private static final String ERROR_FINDING_DESCRIPTOR_INPUTS_UNKNOWN_NAME =
        "There was an error obtaining the inputs for descriptor '%s' of operation (name cannot be determined) in transformation '%s.'";
    private static final String ERROR_FINDING_NAME =
        "There was an error obtaining an operation name in transformation '%s.'";
    protected static final String ERROR_FINDING_PATH =
        "There was an error obtaining an operation path in transformation '%s.'";
    private static final String ERROR_FINDING_TRANSFORMATION_ID = "There was an error obtaining a transformation path.'";
    private static final String ERROR_REMOVING_OPERATION_INPUT =
        "There was an error removing terms for operation '%s' in transformation '%s'";
    private static final String ERROR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME =
        "There was an error removing terms for operation (name cannot be determined) in transformation '%s'";
    protected static final String HAS_NO_TERMS = "'%s' operation in transformation '%s' has no terms";
    protected static final String INVALID_TERM_COUNT =
        "The operation '%s' in transformation '%s' has an invalid term count of '%s.'";
    protected static final String INVALID_TERM_TYPE =
        "The operation '%s' in transformation '%s' has a term with an invalid type or has a term that is null";
    protected static final String MESSAGE = "%s";
    protected static final String MUST_HAVE_ONE_TERM = "'%s' operation in transformation '%s' requires one and only one term";
    protected static final String OPERATION_HAS_ERRORS =
        "The operation '%s' in transformation '%s' has errors and a result cannot being calculated";
    private static final String OPERATION_RESULT_NOT_MODIFIABLE =
        "The '%s' operation's result in transformation '%s' is not directly modifiable";
    private static final String OPERATION_RESULT_NOT_MODIFIABLE_UNKNOWN_NAME =
        "The operation (name cannot be determined) result in transformation '%s' is not directly modifiable";
    protected static final String OPERATION_VALIDATION_ERROR =
        "An exception occurred when validating the input of operation '%s' in transformation '%s'";
    private static final String UNABLE_TO_FIND_FACTORY = "Could not find transformation factory for operation";

    private final Logger logger;
    protected final ValidationProblems problems;
    private final ModelObject operation;
    private final Transformation transformation;

    /**
     * @param operation
     *        the operation model object being wrapped by this domain object (cannot be <code>null</code>)
     * @param transformation
     *        the transformation that owns this operation (cannot be <code>null</code>)
     * @throws ModelspaceException
     *         if an error with the model object occurs
     * @throws ChrysalixException
     *         if a non-model object error occurs
     */
    protected AbstractOperation( final ModelObject operation,
                                 final Transformation transformation ) throws ModelspaceException, ChrysalixException {
        super( operation.name(), operation );
        this.operation = operation;
        this.transformation = transformation;
        this.problems = TransformationFactory.createValidationProblems();
        this.logger = Logger.logger( getClass() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Operation#addInput(java.lang.String, java.lang.Object[])
     */
    @Override
    public void addInput( final String descriptorId,
                          final Object... valuesBeingAdded ) throws ChrysalixException {
        CheckArg.notEmpty( descriptorId, "descriptorId" );
        CheckArg.isNotEmpty( valuesBeingAdded, "valuesBeingAdded" );

        if ( !isValidInputDescriptorId( descriptorId ) ) {
            try {
                throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT,
                                                                      name(),
                                                                      transformationId(),
                                                                      descriptorId ) );
            } catch ( final ModelspaceException e ) {
                final ChrysalixException pe =
                    new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME,
                                                                    transformationId(),
                                                                    descriptorId ) );
                pe.addSuppressed( e );
                throw pe;
            }
        }

        for ( final Object value : valuesBeingAdded ) {
            if ( value == null ) {
                try {
                    throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT,
                                                                          name(),
                                                                          transformationId(),
                                                                          descriptorId ) );
                } catch ( final ModelspaceException e ) {
                    final ChrysalixException pe =
                        new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME,
                                                                        transformationId(),
                                                                        descriptorId ) );
                    pe.addSuppressed( e );
                    throw pe;
                }
            }

            try {
                // add one at a time
                final ModelObject[] added = this.operation.addChildOfType( ChrysalixLexicon.Input.NODE_TYPE, descriptorId );
                final ModelObject input = added[ 0 ];

                if ( value instanceof ModelProperty ) {
                    input.setProperty( ChrysalixLexicon.Input.PATH, true );
                    input.setProperty( ChrysalixLexicon.Input.VALUE, ( ( ModelProperty ) value ).absolutePath() );
                } else if ( value instanceof Operation ) {
                    input.setProperty( ChrysalixLexicon.Input.PATH, true );
                    input.setProperty( ChrysalixLexicon.Input.VALUE, ( ( Operation< ? > ) value ).absolutePath() );
                } else if ( value instanceof Value< ? > ) {
                    input.setProperty( ChrysalixLexicon.Input.PATH, false );
                    input.setProperty( ChrysalixLexicon.Input.VALUE, ( ( Value< ? > ) value ).get() );
                } else {
                    input.setProperty( ChrysalixLexicon.Input.PATH, false );
                    input.setProperty( ChrysalixLexicon.Input.VALUE, value );
                }
            } catch ( final Exception e ) {
                try {
                    throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT,
                                                                          name(),
                                                                          transformationId(),
                                                                          descriptorId ) );
                } catch ( final ModelspaceException error ) {
                    final ChrysalixException pe =
                        new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME,
                                                                        transformationId(),
                                                                        descriptorId ) );
                    pe.addSuppressed( error );
                    throw pe;
                }
            }
        }
    }

    /**
     * @return the operation's output value (can be <code>null</code>)
     * @throws ChrysalixException
     *         if an error occurs
     */
    protected abstract T calculate() throws ChrysalixException;

    @SuppressWarnings( "unchecked" )
    protected final OperationDescriptor< T > descriptor() throws ChrysalixException {
        return ( OperationDescriptor< T > ) factory().descriptor( descriptorId() );
    }

    protected ValueDescriptor< ? > descriptor( final String id ) throws ChrysalixException {
        for ( final ValueDescriptor< ? > descriptor : descriptor().inputDescriptors() ) {
            if ( descriptor.name().equals( id ) ) {
                return descriptor;
            }
        }

        return null;
    }

    private TransformationFactory factory() throws ChrysalixException {
        String opName = null;

        try {
            opName = operation.name();
            return TransformationFactory.REGISTRY.get( this.operation.model().modelspace() );
        } catch ( final ModelspaceException e ) {
            throw new ChrysalixException( e, ChrysalixI18n.localize( UNABLE_TO_FIND_FACTORY, opName ) );
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Do not call if there are validation errors as this will throw an exception.</strong>
     * 
     * @see org.chrysalix.operation.ValueImpl#get()
     */
    @Override
    public final T get() throws ChrysalixException {
        if ( problems().isError() ) {
            try {
                throw new ChrysalixException( ChrysalixI18n.localize( AbstractOperation.OPERATION_HAS_ERRORS,
                                                                      absolutePath(),
                                                                      transformationId() ) );
            } catch ( final ModelspaceException e ) {
                final ChrysalixException pe =
                    new ChrysalixException( ChrysalixI18n.localize( AbstractOperation.ERROR_FINDING_PATH,
                                                                    transformationId() ) );
                pe.addSuppressed( e );
                throw pe;
            }
        }

        return calculate();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Operation#inputs()
     */
    @Override
    public Value< ? >[] inputs() throws ChrysalixException {
        try {
            final ModelObject[] kids = this.operation.childrenOfType( ChrysalixLexicon.Input.NODE_TYPE );
            final Value< ? >[] inputs = new Value< ? >[ kids.length ];

            for ( int i = 0; i < kids.length; ++i ) {
                final String descriptorId = kids[ i ].name();
                inputs[ i ] = new ValueImpl< Object >( descriptorId, kids[ i ] );
            }

            return inputs;
        } catch ( final Exception e ) {
            try {
                throw new ChrysalixException( e, ChrysalixI18n.localize( ERROR_FINDING_ALL_INPUTS,
                                                                         name(),
                                                                         this.transformation.id() ) );
            } catch ( final ModelspaceException error ) {
                final ChrysalixException pe =
                    new ChrysalixException( ChrysalixI18n.localize( ERROR_FINDING_ALL_INPUTS_UNKNOWN_NAME,
                                                                    this.transformation.id() ) );
                pe.addSuppressed( error );
                throw pe;
            }
        }
    }

    /**
     * @param descriptorId
     *        the identifier of the {@link ValueDescriptor descriptor} whose inputs are being requested (cannot be <code>null</code>
     *        or invalid)
     * @return the values (never <code>null</code> but can be empty)
     * @throws ChrysalixException
     *         if an error occurs
     * @see #isValidInputDescriptorId(String)
     * @throws IllegalArgumentException
     *         if the descriptor is not found
     */
    protected List< Value< ? >> inputs( final String descriptorId ) throws ChrysalixException {
        CheckArg.notNull( descriptor( descriptorId ), "descriptorId" );

        try {
            final ModelObject[] kids = this.operation.children( descriptorId );

            if ( kids == null ) {
                return Collections.emptyList();
            }

            final List< Value< ? >> inputs = new ArrayList<>();

            for ( final ModelObject kid : kids ) {
                final Value< ? > input = new ValueImpl< Object >( descriptorId, kid );
                inputs.add( input );
            }

            return inputs;
        } catch ( final Exception e ) {
            try {
                throw new ChrysalixException( e, ChrysalixI18n.localize( ERROR_FINDING_DESCRIPTOR_INPUTS,
                                                                         descriptorId,
                                                                         name(),
                                                                         this.transformation.id() ) );
            } catch ( final ModelspaceException error ) {
                final ChrysalixException pe =
                    new ChrysalixException( ChrysalixI18n.localize( ERROR_FINDING_DESCRIPTOR_INPUTS_UNKNOWN_NAME,
                                                                    transformationId(),
                                                                    descriptorId ) );
                pe.addSuppressed( error );
                throw pe;
            }
        }
    }

    protected boolean isValidInputDescriptorId( final String id ) {
        try {
            return ( descriptor( id ) != null );
        } catch ( final ChrysalixException e ) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator< Value< ? >> iterator() {
        try {
            return Arrays.asList( inputs() ).iterator();
        } catch ( final ChrysalixException e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Value#name()
     */
    @Override
    public String name() throws ModelspaceException {
        try {
            return descriptorId();
        } catch ( final ChrysalixException e ) {
            try {
                throw new ModelspaceException( e, ChrysalixI18n.localize( ERROR_FINDING_NAME, this.transformation.id() ) );
            } catch ( final ChrysalixException error ) {
                final ModelspaceException me = new ModelspaceException( e, ERROR_FINDING_TRANSFORMATION_ID );
                me.addSuppressed( error );
                throw me;
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Operation#problems()
     */
    @SuppressWarnings( "unused" )
    @Override
    public ValidationProblems problems() throws ChrysalixException {
        return this.problems;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Operation#removeInput(java.lang.String, java.lang.Object[])
     */
    @Override
    public void removeInput( final String descriptorId,
                             final Object... valuesBeingRemoved ) throws ChrysalixException {
        CheckArg.notEmpty( descriptorId, "descriptorId" );
        CheckArg.isNotEmpty( valuesBeingRemoved, "valuesBeingRemoved" );

        if ( !isValidInputDescriptorId( descriptorId ) ) {
            try {
                throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT,
                                                                      name(),
                                                                      transformationId(),
                                                                      descriptorId ) );
            } catch ( final ModelspaceException e ) {
                final ChrysalixException pe =
                    new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME,
                                                                    transformationId(),
                                                                    descriptorId ) );
                pe.addSuppressed( e );
                throw pe;
            }
        }

        final List< Value< ? >> inputs = inputs( descriptorId );

        // error if there are no inputs to delete
        if ( inputs.isEmpty() ) {
            try {
                throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT,
                                                                      name(),
                                                                      transformationId(),
                                                                      descriptorId ) );
            } catch ( final ModelspaceException e ) {
                final ChrysalixException pe =
                    new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME,
                                                                    transformationId(),
                                                                    descriptorId ) );
                pe.addSuppressed( e );
                throw pe;
            }
        }

        for ( final Object valueToDelete : valuesBeingRemoved ) {
            if ( valueToDelete == null ) {
                try {
                    throw new ChrysalixException( ChrysalixI18n.localize( ERROR_REMOVING_OPERATION_INPUT,
                                                                          name(),
                                                                          this.transformation.id() ) );
                } catch ( final ModelspaceException error ) {
                    final ChrysalixException pe =
                        new ChrysalixException( ChrysalixI18n.localize( ERROR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME,
                                                                        this.transformation.id() ) );
                    pe.addSuppressed( error );
                    throw pe;
                }
            }

            boolean removed = false;

            // find input
            for ( final Value< ? > input : inputs ) {
                try {
                    final Object value = input.get();

                    if ( valueToDelete.equals( input ) || valueToDelete.equals( input.get() ) ) {
                        this.operation.removeChild( input.name() );
                        removed = true;
                    } else {
                        if ( ( valueToDelete instanceof ModelProperty ) || ( valueToDelete instanceof Operation ) ) {
                            final String path = ( ( ModelElement ) valueToDelete ).absolutePath();

                            if ( path.equals( value ) ) {
                                this.operation.removeChild( input.name() );
                                removed = true;
                            }
                        }
                    }
                } catch ( final ModelspaceException e ) {
                    try {
                        throw new ChrysalixException( ChrysalixI18n.localize( ERROR_REMOVING_OPERATION_INPUT,
                                                                              name(),
                                                                              this.transformation.id() ) );
                    } catch ( final ModelspaceException error ) {
                        final ChrysalixException pe =
                            new ChrysalixException( ChrysalixI18n.localize( ERROR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME,
                                                                            this.transformation.id() ) );
                        pe.addSuppressed( error );
                        throw pe;
                    }
                }
            }

            if ( !removed ) {
                try {
                    throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT,
                                                                          name(),
                                                                          transformationId(),
                                                                          descriptorId ) );
                } catch ( final ModelspaceException error ) {
                    final ChrysalixException pe =
                        new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME,
                                                                        this.transformation.id() ) );
                    pe.addSuppressed( error );
                    throw pe;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This throws {@link UnsupportedOperationException}.
     * 
     * @see org.chrysalix.operation.ValueImpl#set(java.lang.Object)
     */
    @Override
    public void set( final Object proposedValue ) throws ChrysalixException {
        try {
            throw new UnsupportedOperationException( ChrysalixI18n.localize( OPERATION_RESULT_NOT_MODIFIABLE,
                                                                             name(),
                                                                             this.transformation.id() ) );
        } catch ( final ModelspaceException e ) {
            final ChrysalixException pe =
                new ChrysalixException( ChrysalixI18n.localize( OPERATION_RESULT_NOT_MODIFIABLE_UNKNOWN_NAME,
                                                                transformationId(),
                                                                descriptorId() ) );
            pe.addSuppressed( e );
            throw pe;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Operation#setInput(java.lang.String, java.lang.Object[])
     */
    @Override
    public void setInput( final String descriptorId,
                          final Object... valuesBeingSet ) throws ChrysalixException {
        if ( !isValidInputDescriptorId( descriptorId ) ) {
            try {
                throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT,
                                                                      name(),
                                                                      transformationId(),
                                                                      descriptorId ) );
            } catch ( final ModelspaceException e ) {
                final ChrysalixException pe =
                    new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME,
                                                                    transformationId(),
                                                                    descriptorId ) );
                pe.addSuppressed( e );
                throw pe;
            }
        }

        // remove all previous values
        try {
            for ( final ModelObject input : this.operation.childrenOfType( ChrysalixLexicon.Input.NODE_TYPE ) ) {
                this.operation.removeChild( input.name() );
                this.logger.debug( "Removed input '%s' for descriptor '%s'", input.name(), descriptorId );
            }
        } catch ( final ModelspaceException e ) {
            throw new ChrysalixException( e, ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME,
                                                                     transformationId(),
                                                                     descriptorId ) );
        }

        for ( final Object value : valuesBeingSet ) {
            if ( value == null ) {
                try {
                    throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT,
                                                                          name(),
                                                                          transformationId(),
                                                                          descriptorId ) );
                } catch ( final ModelspaceException error ) {
                    final ChrysalixException pe =
                        new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME,
                                                                        transformationId(),
                                                                        descriptorId ) );
                    pe.addSuppressed( error );
                    throw pe;
                }
            }

            try {
                final ModelObject[] added = this.operation.addChildOfType( ChrysalixLexicon.Input.NODE_TYPE, descriptorId );
                final ModelObject input = added[ 0 ];

                if ( value instanceof ModelProperty ) {
                    input.setProperty( ChrysalixLexicon.Input.PATH, true );
                    input.setProperty( ChrysalixLexicon.Input.VALUE, ( ( ModelProperty ) value ).absolutePath() );
                } else if ( value instanceof Operation ) {
                    input.setProperty( ChrysalixLexicon.Input.PATH, true );
                    input.setProperty( ChrysalixLexicon.Input.VALUE, ( ( Operation< ? > ) value ).absolutePath() );
                } else if ( value instanceof Value< ? > ) {
                    input.setProperty( ChrysalixLexicon.Input.PATH, false );
                    input.setProperty( ChrysalixLexicon.Input.VALUE, ( ( Value< ? > ) value ).get() );
                } else {
                    input.setProperty( ChrysalixLexicon.Input.PATH, false );
                    input.setProperty( ChrysalixLexicon.Input.VALUE, value );
                }
            } catch ( final Exception e ) {
                try {
                    throw new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT,
                                                                          name(),
                                                                          transformationId(),
                                                                          descriptorId ) );
                } catch ( final ModelspaceException error ) {
                    final ChrysalixException pe =
                        new ChrysalixException( ChrysalixI18n.localize( ERROR_ADDING_OR_REMOVING_OPERATION_INPUT_UNKNOWN_NAME,
                                                                        transformationId(),
                                                                        descriptorId ) );
                    pe.addSuppressed( error );
                    throw pe;
                }
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
        try {
            final StringBuilder builder = new StringBuilder( descriptor().name() );
            builder.append( '(' );

            boolean firstTime = true;

            for ( final Value< ? > term : inputs() ) {
                if ( firstTime ) {
                    firstTime = false;
                } else {
                    builder.append( ", " );
                }

                builder.append( factory().descriptor( term.descriptorId() ).signature() );
            }

            builder.append( ')' );
            return builder.toString();
        } catch ( final ChrysalixException e ) {
            this.logger.debug( e, "Error calculating toString of operation" );
            return super.toString();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Operation#transformation()
     */
    @Override
    public Transformation transformation() {
        return this.transformation;
    }

    protected String transformationId() throws ChrysalixException {
        return this.transformation.id();
    }

}
