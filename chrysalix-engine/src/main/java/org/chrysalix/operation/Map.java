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

import org.chrysalix.Chrysalix;
import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.chrysalix.transformation.Operation;
import org.chrysalix.transformation.OperationDescriptor;
import org.chrysalix.transformation.Transformation;
import org.chrysalix.transformation.TransformationFactory;
import org.chrysalix.transformation.ValidationProblem;
import org.chrysalix.transformation.Value;
import org.chrysalix.transformation.ValueDescriptor;
import org.chrysalix.transformation.OperationCategory.BuiltInCategory;
import org.modelspace.Model;
import org.modelspace.ModelObject;

/**
 * Maps one {@link ModelObject model object's} property to another model object's property.
 */
public final class Map extends AbstractOperation< Void > {

    /**
     * The source {@link ModelObject model object} descriptor.
     */
    public static final ValueDescriptor< ModelObject > SOURCE_MODEL_OBJECT_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Map.class, "source" ),
                                                     ChrysalixI18n.mapOperationSourceDescription.text(),
                                                     ChrysalixI18n.mapOperationSourceName.text(),
                                                     ModelObject.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The descriptor for the name of the source {@link ModelObject model object} property used in the mapping.
     */
    public static final ValueDescriptor< String > SOURCE_PROP_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Map.class, "sourceProperty" ),
                                                     ChrysalixI18n.mapOperationSourcePropDescription.text(),
                                                     ChrysalixI18n.mapOperationSourcePropName.text(),
                                                     String.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The target {@link ModelObject model object} descriptor.
     */
    public static final ValueDescriptor< ModelObject > TARGET_MODEL_OBJECT_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Map.class, "target" ),
                                                     ChrysalixI18n.mapOperationTargetDescription.text(),
                                                     ChrysalixI18n.mapOperationTargetName.text(),
                                                     ModelObject.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The descriptor for the name of the target {@link ModelObject model object} property used in the mapping.
     */
    public static final ValueDescriptor< String > TARGET_PROP_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Map.class, "targetProperty" ),
                                                     ChrysalixI18n.mapOperationTargetPropDescription.text(),
                                                     ChrysalixI18n.mapOperationTargetPropName.text(),
                                                     String.class,
                                                     true,
                                                     1,
                                                     false );

    /**
     * The input descriptors.
     */
    private static final ValueDescriptor< ? >[] INPUT_DESCRIPTORS = {
                    SOURCE_MODEL_OBJECT_DESCRIPTOR,
                    SOURCE_PROP_DESCRIPTOR,
                    TARGET_MODEL_OBJECT_DESCRIPTOR,
                    TARGET_PROP_DESCRIPTOR };

    /**
     * The output descriptor.
     */
    public static final OperationDescriptor< Void > DESCRIPTOR =
        new AbstractOperationDescriptor< Void >( TransformationFactory.createId( Map.class ),
                                                 ChrysalixI18n.mapOperationDescription.text(),
                                                 ChrysalixI18n.mapOperationName.text(),
                                                 Void.class,
                                                 INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.chrysalix.transformation.Transformation)
             */
            @Override
            public Operation< Void > newInstance( final Transformation transformation ) {
                return new Map( transformation );
            }

        };

    /**
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Map( final Transformation transformation ) {
        super( DESCRIPTOR, transformation );

        try {
            addCategory( BuiltInCategory.ASSIGNMENT );
        } catch ( final ChrysalixException e ) {
            Chrysalix.LOGGER.error( e, ChrysalixI18n.errorAddingBuiltInCategory, transformationId() );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#calculate()
     */
    @Override
    protected Void calculate() throws ChrysalixException {
        assert !problems().isError();
        /*
                final ModelObject sourceModel = ( ModelObject ) inputs( SOURCE_MODEL_OBJECT_DESCRIPTOR.id() ).get( 0 ).get();
                final String sourceProp = ( String ) inputs( SOURCE_PROP_DESCRIPTOR.id() ).get( 0 ).get();

                final ModelObject targetModel = ( ModelObject ) inputs( TARGET_MODEL_OBJECT_DESCRIPTOR.id() ).get( 0 ).get();
                final String targetProp = ( String ) inputs( TARGET_PROP_DESCRIPTOR.id() ).get( 0 ).get();

                try {
                    final Object value = sourceModel.value( sourceProp );
                    targetModel.setProperty( targetProp, value );
                } catch ( final ModelspaceException e ) {
                    throw new ChrysalixException( e, "Unable to calculate operation result" );
                }
        */
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#validate()
     */
    @Override
    protected void validate() {
        // make sure all terms have been added
        if ( inputs().size() != 4 ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   ChrysalixI18n.mapOperationInvalidTermCount.text( transformationId() ) );
            problems().add( problem );
        } else {
            { // source model
                final List< Value< ? >> sourceModels = inputs( SOURCE_MODEL_OBJECT_DESCRIPTOR.id() );

                if ( sourceModels.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.mapOperationInvalidSourceModelCount.text( transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = sourceModels.get( 0 );
                    Object model;

                    try {
                        model = term.get();

                        if ( !( model instanceof ModelObject ) && !( model instanceof Model ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.mapOperationInvalidSourceModelObjectType.text( transformationId() ) );
                            problems().add( problem );
                        }
                    } catch ( final ChrysalixException e ) {
                        final ValidationProblem problem =
                            TransformationFactory.createError( transformationId(),
                                                               ChrysalixI18n.operationValidationError.text( name(),
                                                                                                              transformationId() ) );
                        problems().add( problem );
                        Chrysalix.LOGGER.error( e, ChrysalixI18n.message, problem.message() );
                    }
                }
            }

            { // source model's property name
                final List< Value< ? >> propNames = inputs( SOURCE_PROP_DESCRIPTOR.id() );

                if ( propNames.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.mapOperationInvalidSourcePropCount.text( transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = propNames.get( 0 );
                    Object name;

                    try {
                        name = term.get();

                        if ( !( name instanceof String ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.mapOperationInvalidSourcePropType.text( transformationId() ) );
                            problems().add( problem );
                        }
                    } catch ( final ChrysalixException e ) {
                        final ValidationProblem problem =
                            TransformationFactory.createError( transformationId(),
                                                               ChrysalixI18n.operationValidationError.text( name(),
                                                                                                              transformationId() ) );
                        problems().add( problem );
                        Chrysalix.LOGGER.error( e, ChrysalixI18n.message, problem.message() );
                    }
                }
            }

            { // target model
                final List< Value< ? >> targetModels = inputs( TARGET_MODEL_OBJECT_DESCRIPTOR.id() );

                if ( targetModels.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.mapOperationInvalidTargetModelCount.text( transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = targetModels.get( 0 );
                    Object model;

                    try {
                        model = term.get();

                        if ( !( model instanceof ModelObject ) && !( model instanceof Model ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.mapOperationInvalidTargetModelObjectType.text( transformationId() ) );
                            problems().add( problem );
                        }
                    } catch ( final ChrysalixException e ) {
                        final ValidationProblem problem =
                            TransformationFactory.createError( transformationId(),
                                                               ChrysalixI18n.operationValidationError.text( name(),
                                                                                                              transformationId() ) );
                        problems().add( problem );
                        Chrysalix.LOGGER.error( e, ChrysalixI18n.message, problem.message() );
                    }
                }
            }

            { // target model's property name
                final List< Value< ? >> propNames = inputs( TARGET_PROP_DESCRIPTOR.id() );

                if ( propNames.size() != 1 ) {
                    final ValidationProblem problem =
                        TransformationFactory.createError( transformationId(),
                                                           ChrysalixI18n.mapOperationInvalidTargetPropCount.text( transformationId() ) );
                    problems().add( problem );
                } else {
                    final Value< ? > term = propNames.get( 0 );
                    Object name;

                    try {
                        name = term.get();

                        if ( !( name instanceof String ) ) {
                            final ValidationProblem problem =
                                TransformationFactory.createError( transformationId(),
                                                                   ChrysalixI18n.mapOperationInvalidTargetPropType.text( transformationId() ) );
                            problems().add( problem );
                        }
                    } catch ( final ChrysalixException e ) {
                        final ValidationProblem problem =
                            TransformationFactory.createError( transformationId(),
                                                               ChrysalixI18n.operationValidationError.text( name(),
                                                                                                              transformationId() ) );
                        problems().add( problem );
                        Chrysalix.LOGGER.error( e, ChrysalixI18n.message, problem.message() );
                    }
                }
            }
        }
    }

}
