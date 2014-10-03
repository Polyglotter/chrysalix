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
 * A string concatenation operation.
 */
public final class Concat extends AbstractOperation< String > {

    static final String DESCRIPTION = "Concatenates the string representation of two or more terms";
    private static final String INPUT_DESCRIPTION = "An input term being concatenated with other terms";
    private static final String INPUT_NAME = "Input";
    static final String NAME = "Concat";

    /**
     * The input term descriptor.
     */
    public static final ValueDescriptor< Object > TERM_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Concat.class, "input" ),
                                                     ChrysalixI18n.localize( INPUT_DESCRIPTION ),
                                                     ChrysalixI18n.localize( INPUT_NAME ),
                                                     Object.class,
                                                     true,
                                                     2,
                                                     true );
    /**
     * The input descriptors.
     */
    private static final ValueDescriptor< ? >[] INPUT_DESCRIPTORS = { TERM_DESCRIPTOR };

    /**
     * The output descriptor.
     */
    public static final OperationDescriptor< String > DESCRIPTOR =
        new AbstractOperationDescriptor< String >( TransformationFactory.createId( Concat.class ),
                                                   ChrysalixI18n.localize( DESCRIPTION ),
                                                   ChrysalixI18n.localize( NAME ),
                                                   String.class,
                                                   INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.modelspace.ModelObject,
             *      org.chrysalix.transformation.Transformation)
             */
            @Override
            public Operation< String > newInstance( final ModelObject operation,
                                                    final Transformation transformation ) throws ModelspaceException, ChrysalixException {
                return new Concat( operation, transformation );
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
    Concat( final ModelObject operation,
            final Transformation transformation ) throws ModelspaceException, ChrysalixException {
        super( operation, transformation );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#calculate()
     */
    @Override
    protected String calculate() throws ChrysalixException {
        assert !problems().isError();
        final StringBuilder result = new StringBuilder();

        for ( final Value< ? > term : inputs() ) {
            final Object value = term.get();
            result.append( ( value == null ) ? "null" : value.toString() );
        }

        return result.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#problems()
     */
    @Override
    public ValidationProblems problems() throws ChrysalixException {
        this.problems.clear();

        // validate number of terms
        if ( inputs().length < TERM_DESCRIPTOR.requiredValueCount() ) {
            final ValidationProblem problem =
                TransformationFactory.createError( transformationId(),
                                                   ChrysalixI18n.localize( AbstractOperation.INVALID_TERM_COUNT,
                                                                           NAME,
                                                                           transformationId(),
                                                                           inputs().length ) );
            problems().add( problem );
        }

        return super.problems();
    }

}
