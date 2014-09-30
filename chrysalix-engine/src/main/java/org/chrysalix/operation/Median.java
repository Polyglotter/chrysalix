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
import org.modelspace.ModelObject;
import org.modelspace.ModelspaceException;

/**
 * Computes the median value of a collection of number terms.
 */
public final class Median extends AbstractOperation< Number > {

    private static String ADD_OPERATION_NOT_FOUND = "Add operation child of the median operation was not found";
    static final String DESCRIPTION = "Computes the median value of a collection of numeric terms";
    private static String DIVIDE_OPERATION_NOT_FOUND = "Divide operation child of the median operation was not found";
    private static final String ERROR = "Median operation in transformation '%s' failed to calculate";
    private static final String INPUT_DESCRIPTION = "An input term used to determine the median value of a set of terms.";
    private static final String INPUT_NAME = "Input";
    static final String NAME = "Median";

    /**
     * The input term descriptor.
     */
    public static final ValueDescriptor< Number > TERM_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Median.class, "input" ),
                                                     ChrysalixI18n.localize( INPUT_DESCRIPTION ),
                                                     ChrysalixI18n.localize( INPUT_NAME ),
                                                     Number.class,
                                                     true,
                                                     1,
                                                     true );

    /**
     * The input descriptors.
     */
    private static final ValueDescriptor< ? >[] INPUT_DESCRIPTORS = { TERM_DESCRIPTOR };

    /**
     * The output descriptor.
     */
    public static final OperationDescriptor< Number > DESCRIPTOR =
        new AbstractOperationDescriptor< Number >( TransformationFactory.createId( Median.class ),
                                                   ChrysalixI18n.localize( DESCRIPTION ),
                                                   ChrysalixI18n.localize( NAME ),
                                                   Number.class,
                                                   INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.modelspace.ModelObject,
             *      org.chrysalix.transformation.Transformation)
             */
            @Override
            public Operation< Number > newInstance( final ModelObject operation,
                                                    final Transformation transformation ) throws ModelspaceException, ChrysalixException {
                return new Median( operation, transformation );
            }

        };

    /**
     * @param operation
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
    Median( final ModelObject operation,
            final Transformation transformation ) throws ModelspaceException, ChrysalixException {
        super( operation, transformation );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#addInput(java.lang.String, java.lang.Object[])
     */
    @Override
    public void addInput( final String descriptorId,
                          final Object... valuesBeingAdded ) throws ChrysalixException {
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

        final Add add = addOperation();
        add.addInput( Add.TERM_DESCRIPTOR.id(), valuesBeingAdded );
        divideOperation().setInput( Divide.TERM_DESCRIPTOR.id(), add, valuesBeingAdded.length );
    }

    private Add addOperation() throws ChrysalixException {
        ModelObject modelObject = null;

        try {
            modelObject = modelObect().child( Add.TERM_DESCRIPTOR.id() );

            if ( modelObject == null ) {
                throw new ChrysalixException( ChrysalixI18n.localize( ADD_OPERATION_NOT_FOUND ) );
            }

            return ( Add ) Add.DESCRIPTOR.newInstance( modelObject, transformation() );
        } catch ( final ModelspaceException e ) {
            throw new ChrysalixException( e, ChrysalixI18n.localize( ADD_OPERATION_NOT_FOUND ) );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#calculate()
     */
    @SuppressWarnings( "unchecked" )
    @Override
    protected Number calculate() throws ChrysalixException {
        // assert !problems().isError();
        //
        // // convert terms to number terms
        // final Value< ? >[] inputs = addOperation().inputs();
        // final int size = inputs.length;
        // final List< Value< Number >> numberTerms = new ArrayList<>( size );
        //
        // // OK to cast since this method should not be run if there is a non-number term
        // for ( final Value< ? > term : inputs ) {
        // numberTerms.add( ( Value< Number > ) term );
        // }
        //
        // // sort values
        // Collections.sort( numberTerms, Value.ASCENDING_NUMBER_SORTER );
        //
        // final boolean even = ( ( size & 1 ) == 0 );
        // final int halfwayIndex = ( size / 2 );
        //
        // if ( even ) {
        // final Value< ? > first = numberTerms.get( halfwayIndex - 1 );
        // final Value< ? > second = numberTerms.get( halfwayIndex );
        // final Add addOp = new Add( transformation() );
        //
        // try {
        // addOp.addInput( Add.TERM_DESCRIPTOR.name(), first, second );
        // final Number sum = addOp.get();
        //
        // final Divide divideOp = new Divide( transformation() );
        // divideOp.addInput( Divide.TERM_DESCRIPTOR.name(), sum, 2 );
        //
        // return divideOp.get();
        // } catch ( final ChrysalixException e ) {
        // final ValidationProblem problem =
        // TransformationFactory.createError( transformationId(),
        // ChrysalixI18n.localize( ERROR, transformationId() ) );
        // problems().add( problem );
        // }
        // }
        //
        // return numberTerms.get( halfwayIndex ).get();
        // TODO impl
        return 0;
    }

    private Divide divideOperation() throws ChrysalixException {
        ModelObject modelObject = null;

        try {
            modelObject = modelObect().child( Divide.TERM_DESCRIPTOR.id() );

            if ( modelObject == null ) {
                throw new ChrysalixException( ChrysalixI18n.localize( DIVIDE_OPERATION_NOT_FOUND ) );
            }

            return ( Divide ) Divide.DESCRIPTOR.newInstance( modelObject, transformation() );
        } catch ( final ModelspaceException e ) {
            throw new ChrysalixException( e, ChrysalixI18n.localize( DIVIDE_OPERATION_NOT_FOUND ) );
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

        // make sure there are terms
        if ( inputs().length == 0 ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   ChrysalixI18n.localize( AbstractOperation.HAS_NO_TERMS,
                                                                           NAME,
                                                                           transformationId() ) );
            problems().add( problem );
        } else {
            if ( inputs().length < INPUT_DESCRIPTORS[ 0 ].requiredValueCount() ) {
                final ValidationProblem problem =
                    TransformationFactory.createError( transformationId(),
                                                       ChrysalixI18n.localize( AbstractOperation.INVALID_TERM_COUNT,
                                                                               NAME,
                                                                               transformationId(),
                                                                               inputs().length ) );
                problems().add( problem );
            }

            // make sure all the terms have types of Number
            for ( final Value< ? > term : inputs() ) {
                Object value;

                try {
                    value = term.get();

                    if ( !( value instanceof Number ) ) {
                        final ValidationProblem problem =
                            TransformationFactory.createError( transformationId(),
                                                               ChrysalixI18n.localize( AbstractOperation.INVALID_TERM_TYPE,
                                                                                       NAME,
                                                                                       transformationId() ) );
                        problems().add( problem );
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

        return super.problems();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#removeInput(java.lang.String, java.lang.Object[])
     */
    @Override
    public void removeInput( final String descriptorId,
                             final Object... valuesBeingRemoved ) throws ChrysalixException {
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

        final Add add = addOperation();
        add.removeInput( Add.TERM_DESCRIPTOR.id(), valuesBeingRemoved );
        divideOperation().setInput( Divide.TERM_DESCRIPTOR.id(), add, add.inputs().length );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#setInput(java.lang.String, java.lang.Object[])
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

        final Add add = addOperation();
        add.setInput( Add.TERM_DESCRIPTOR.id(), valuesBeingSet );
        divideOperation().setInput( Divide.TERM_DESCRIPTOR.id(), add, valuesBeingSet.length );
    }

}
