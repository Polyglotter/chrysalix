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

import java.util.List;

import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.chrysalix.transformation.Operation;
import org.chrysalix.transformation.OperationDescriptor;
import org.chrysalix.transformation.Transformation;
import org.chrysalix.transformation.TransformationFactory;
import org.chrysalix.transformation.ValidationProblem;
import org.chrysalix.transformation.ValidationProblems;
import org.chrysalix.transformation.Value;
import org.chrysalix.transformation.ValueDescriptor;
import org.modelspace.ModelElement;
import org.modelspace.ModelObject;
import org.modelspace.ModelProperty;
import org.modelspace.ModelspaceException;

/**
 * Maps one {@link ModelObject model object's} property to another model object's property.
 */
public final class Map extends AbstractOperation< Void > {

    static final String DESCRIPTION = "Maps one model object's property to another model object's property";
    private static final String ERROR_READING_MODEL_PROP =
        "Map operation could not read a model property descriptor in transformation '%s'";
    private static final String ERROR_SETTING_TARGET =
        "Map operation could not set target property in transformation '%s'";
    private static final String INVALID_VALUES_COUNT =
        "A map operation in transformation '%s' source model and target model property are not both single-valued or both multi-valued.";
    private static final String INVALID_SOURCE_PROP_COUNT =
        "A map operation in transformation '%s' does not have exactly one source model property";
    private static final String INVALID_SOURCE_PROP_TYPE =
        "The source property of a map operation in transformation '%s' is not a model object property or operation";
    private static final String INVALID_TARGET_PROP_COUNT =
        "A map operation in transformation '%s' does not have exactly one target model property name";
    private static final String INVALID_TARGET_PROP_TYPE =
        "The target property of a map operation in transformation '%s' is not a model object property";
    static final String NAME = "Map";
    private static final String SOURCE_PROP_DESCRIPTION = "The name of the source model's property being mapped";
    private static final String SOURCE_PROP_NAME = "Source Property";
    private static final String TARGET_PROP_DESCRIPTION = "The name of the target model's property being mapped";
    private static final String TARGET_PROP_NAME = "Target Property";

    /**
     * The descriptor for the name of the source {@link ModelObject model object} property used in the mapping.
     */
    public static final ValueDescriptor< ModelProperty > SOURCE_PROP_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Map.class, "sourceProperty" ),
                                                     ChrysalixI18n.localize( SOURCE_PROP_DESCRIPTION ),
                                                     ChrysalixI18n.localize( SOURCE_PROP_NAME ),
                                                     ModelProperty.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The descriptor for the name of the target {@link ModelObject model object} property used in the mapping.
     */
    public static final ValueDescriptor< ModelProperty > TARGET_PROP_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Map.class, "targetProperty" ),
                                                     ChrysalixI18n.localize( TARGET_PROP_DESCRIPTION ),
                                                     ChrysalixI18n.localize( TARGET_PROP_NAME ),
                                                     ModelProperty.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The input descriptors.
     */
    private static final ValueDescriptor< ? >[] INPUT_DESCRIPTORS = {
                    SOURCE_PROP_DESCRIPTOR,
                    TARGET_PROP_DESCRIPTOR };

    /**
     * The output descriptor.
     */
    public static final OperationDescriptor< Void > DESCRIPTOR =
        new AbstractOperationDescriptor< Void >( TransformationFactory.createId( Map.class ),
                                                 ChrysalixI18n.localize( DESCRIPTION ),
                                                 ChrysalixI18n.localize( NAME ),
                                                 Void.class,
                                                 INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.modelspace.ModelObject,
             *      org.chrysalix.transformation.Transformation)
             */
            @Override
            public Operation< Void > newInstance( final ModelObject operation,
                                                  final Transformation transformation ) throws ModelspaceException, ChrysalixException {
                return new Map( operation, transformation );
            }

        };

    /**
     * @param opModelOperation
     *        the operation model object (cannot be <code>null</code>)
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws ModelspaceException
     *         if an error with the model object occurs
     * @throws ChrysalixException
     *         if a non-model object error occurs
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Map( final ModelObject opModelOperation,
         final Transformation transformation ) throws ModelspaceException, ChrysalixException {
        super( opModelOperation, transformation );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#calculate()
     */
    @Override
    protected Void calculate() throws ChrysalixException {
        assert !problems().isError();
        final ModelElement source = inputs( SOURCE_PROP_DESCRIPTOR.name() ).get( 0 );
        final ModelProperty targetProp = ( ModelProperty ) inputs( TARGET_PROP_DESCRIPTOR.name() ).get( 0 );

        try {
            if ( source instanceof ModelProperty ) {
                final ModelProperty sourceProp = ( ModelProperty ) source;

                if ( sourceProp.descriptor().multiple() ) {
                    final Object[] values = sourceProp.values();
                    targetProp.set( values );
                } else {
                    final Object value = sourceProp.value();
                    targetProp.set( value );
                }
            } else if ( source instanceof Operation ) {
                final Object value = ( ( Operation< ? > ) source ).get();
                targetProp.set( value );
            }

            return null;
        } catch ( final ModelspaceException e ) {
            throw new ChrysalixException( e, ChrysalixI18n.localize( ERROR_SETTING_TARGET, this.transformationId() ) );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#problems()
     */
    @Override
    public ValidationProblems problems() throws ChrysalixException {
        this.problems.clear();

        // make sure all terms have been added
        if ( inputs().length != 2 ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   ChrysalixI18n.localize( AbstractOperation.INVALID_TERM_COUNT,
                                                                           NAME,
                                                                           transformationId(),
                                                                           inputs().length ) );
            problems().add( problem );
        } else {
            boolean sourcePropIsMultiValued = false;
            boolean targetPropIsMultiValued = false;

            { // source model property
                final List< Value< ? >> sourceProps = inputs( SOURCE_PROP_DESCRIPTOR.id() );

                if ( sourceProps.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.localize( INVALID_SOURCE_PROP_COUNT,
                                                                                   transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = sourceProps.get( 0 );
                    Object prop;

                    try {
                        prop = term.get();

                        if ( !( prop instanceof ModelProperty ) || !( prop instanceof Operation ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.localize( INVALID_SOURCE_PROP_TYPE,
                                                                                           transformationId() ) );
                            problems().add( problem );
                        } else {
                            if ( prop instanceof ModelProperty ) {
                                try {
                                    sourcePropIsMultiValued = ( ( ModelProperty ) prop ).descriptor().multiple();
                                } catch ( final ModelspaceException e ) {
                                    final ValidationProblem problem =
                                        TransformationFactory.createError( transformationId(),
                                                                           ChrysalixI18n.localize( ERROR_READING_MODEL_PROP,
                                                                                                   NAME ) );
                                    problems().add( problem );
                                }
                            }
                        }
                    } catch ( final ChrysalixException e ) {
                        final ValidationProblem problem =
                            TransformationFactory.createError( transformationId(),
                                                               ChrysalixI18n.localize( AbstractOperation.OPERATION_VALIDATION_ERROR,
                                                                                       NAME,
                                                                                       transformationId() ) );
                        problems().add( problem );
                    }
                }
            }

            { // target model property
                final List< Value< ? >> props = inputs( TARGET_PROP_DESCRIPTOR.id() );

                if ( props.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.localize( INVALID_TARGET_PROP_COUNT,
                                                                                   transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = props.get( 0 );
                    Object prop;

                    try {
                        prop = term.get();

                        if ( !( prop instanceof ModelProperty ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.localize( INVALID_TARGET_PROP_TYPE,
                                                                                           transformationId() ) );
                            problems().add( problem );
                        } else {
                            try {
                                targetPropIsMultiValued = ( ( ModelProperty ) prop ).descriptor().multiple();
                            } catch ( final ModelspaceException e ) {
                                final ValidationProblem problem =
                                    TransformationFactory.createError( transformationId(),
                                                                       ChrysalixI18n.localize( ERROR_READING_MODEL_PROP,
                                                                                               NAME ) );
                                problems().add( problem );
                            }
                        }
                    } catch ( final ChrysalixException e ) {
                        final ValidationProblem problem =
                            TransformationFactory.createError( transformationId(),
                                                               ChrysalixI18n.localize( AbstractOperation.OPERATION_VALIDATION_ERROR,
                                                                                       NAME,
                                                                                       transformationId() ) );
                        problems().add( problem );
                    }
                }
            }

            if ( sourcePropIsMultiValued != targetPropIsMultiValued ) {
                final ValidationProblem problem =
                    TransformationFactory.createError( transformationId(),
                                                       ChrysalixI18n.localize( INVALID_VALUES_COUNT,
                                                                               transformationId() ) );
                problems().add( problem );
            }
        }

        return super.problems();
    }

}
