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

import org.chrysalix.Chrysalix;
import org.chrysalix.ChrysalixException;
import org.chrysalix.ChrysalixI18n;
import org.chrysalix.transformation.Operation;
import org.chrysalix.transformation.OperationDescriptor;
import org.chrysalix.transformation.Transformation;
import org.chrysalix.transformation.TransformationFactory;
import org.chrysalix.transformation.ValueDescriptor;
import org.chrysalix.transformation.OperationCategory.BuiltInCategory;

/**
 * Counts the number of terms.
 */
public final class Count extends AbstractOperation< Integer > {

    /**
     * The input term descriptor.
     */
    public static final ValueDescriptor< Object > TERM_DESCRIPTOR =
        TransformationFactory.createValueDescriptor( TransformationFactory.createId( Count.class, "input" ),
                                                     ChrysalixI18n.countOperationInputDescription.text(),
                                                     ChrysalixI18n.countOperationInputName.text(),
                                                     Object.class,
                                                     true,
                                                     0,
                                                     true );

    /**
     * The input descriptors.
     */
    private static final ValueDescriptor< ? >[] INPUT_DESCRIPTORS = { TERM_DESCRIPTOR };

    /**
     * The output descriptor.
     */
    public static final OperationDescriptor< Integer > DESCRIPTOR =
        new AbstractOperationDescriptor< Integer >( TransformationFactory.createId( Count.class ),
                                                    ChrysalixI18n.countOperationDescription.text(),
                                                    ChrysalixI18n.countOperationName.text(),
                                                    Integer.class,
                                                    INPUT_DESCRIPTORS ) {

            /**
             * {@inheritDoc}
             * 
             * @see org.chrysalix.transformation.OperationDescriptor#newInstance(org.chrysalix.transformation.Transformation)
             */
            @Override
            public Operation< Integer > newInstance( final Transformation transformation ) {
                return new Count( transformation );
            }

        };

    /**
     * @param transformation
     *        the transformation containing this operation (cannot be <code>null</code>)
     * @throws IllegalArgumentException
     *         if the input is <code>null</code>
     */
    Count( final Transformation transformation ) {
        super( DESCRIPTOR, transformation );

        try {
            addCategory( BuiltInCategory.ARITHMETIC );
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
    protected Integer calculate() {
        assert !problems().isError();
        return inputs().size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#get()
     */
    @Override
    public Integer get() throws ChrysalixException {
        final Integer result = super.get();
        return ( ( result == null ) ? 0 : result );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.operation.AbstractOperation#validate()
     */
    @Override
    protected void validate() {
        // nothing to do
    }

}
