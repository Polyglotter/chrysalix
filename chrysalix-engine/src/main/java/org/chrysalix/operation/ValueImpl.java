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
import org.chrysalix.ChrysalixLexicon;
import org.chrysalix.common.CheckArg;
import org.chrysalix.transformation.Operation;
import org.chrysalix.transformation.TransformationFactory;
import org.chrysalix.transformation.Value;
import org.chrysalix.transformation.ValueDescriptor;
import org.modelspace.Model;
import org.modelspace.ModelObject;
import org.modelspace.ModelProperty;
import org.modelspace.ModelspaceException;

/**
 * The base class for {@link Value value} transformation objects.
 * 
 * @param <T>
 *        the value type
 */
public class ValueImpl< T > implements Value< T > {

    private static final String DESCRIPTOR_ERROR = "Error obtaining input descriptor '%s.'";
    private static final String PATH_ERROR = "Error obtaining absolute path for input descriptor '%s.'";
    private static final String ERROR_GETTING_PATH_VALUE = "Error obtaining path value for input descriptor '%s.'";
    private static final String ERROR_GETTING_VALUE = "Error obtaining value for input descriptor '%s.'";
    private static final String ERROR_SETTING_VALUE = "Error setting value for input descriptor '%s.'";
    private static final String TRANSFORMATION_FACTORY_ERROR = "Unable to obtain transformation factory";
    static final String VALUE_NOT_MODIFIABLE = "The value '%s' is not modifiable";

    private final String descriptorId;
    private final ModelObject input;

    /**
     * @param descriptorId
     *        the ID of the descriptor for this input (cannot be <code>null</code> or empty)
     * @param modelObject
     *        the model object for this value (cannot be <code>null</code>)
     */
    public ValueImpl( final String descriptorId,
                      final ModelObject modelObject ) {
        CheckArg.notEmpty( descriptorId, "descriptorId" );
        CheckArg.notNull( modelObject, "modelObject" );

        this.descriptorId = descriptorId;
        this.input = modelObject;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelElement#absolutePath()
     */
    @Override
    public String absolutePath() throws ModelspaceException {
        return this.input.absolutePath();
    }

    @SuppressWarnings( "unchecked" )
    private ValueDescriptor< T > descriptor() throws ChrysalixException {
        try {
            final TransformationFactory factory = TransformationFactory.REGISTRY.get( this.input.model().modelspace() );
            return ( ValueDescriptor< T > ) factory.descriptor( this.descriptorId );
        } catch ( final ModelspaceException e ) {
            throw new ChrysalixException( e, ChrysalixI18n.localize( TRANSFORMATION_FACTORY_ERROR ) );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Value#descriptorId()
     */
    @SuppressWarnings( "unused" )
    @Override
    public String descriptorId() throws ChrysalixException {
        return this.descriptorId;
    }

    /**
     * @param proposedValue
     *        the proposed new value (can be <code>null</code>)
     * @return <code>true</code> if proposed value is a {@link ModelProperty model property} or an {@link Operation operation}
     * @throws ChrysalixException
     *         if an error occurs
     */
    protected boolean determinePathValueBasedOn( final Object proposedValue ) throws ChrysalixException {
        return ( proposedValue instanceof ModelObject );
    }

    /**
     * @param proposedValue
     *        the proposed new value (can be <code>null</code>)
     * @return the new value based on using the specified proposed value (can be <code>null</code>)
     * @throws ChrysalixException
     *         if an error occurs
     */
    protected Object determineValueBasedOn( final Object proposedValue ) throws ChrysalixException {
        if ( ( proposedValue instanceof ModelProperty ) || ( proposedValue instanceof Operation< ? > ) ) {
            try {
                return ( ( ModelObject ) proposedValue ).modelRelativePath();
            } catch ( final ModelspaceException e ) {
                throw new ChrysalixException( e, ChrysalixI18n.localize( PATH_ERROR, this.descriptorId ) );
            }
        }

        return proposedValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Value#get()
     */
    @SuppressWarnings( { "unchecked" } )
    @Override
    public T get() throws ChrysalixException {
        if ( this.input instanceof Operation< ? > ) {
            return ( ( Operation< T > ) this.input ).get();
        }

        boolean valueIsPath = false;

        try {
            if ( this.input.hasProperty( ChrysalixLexicon.Input.PATH ) ) {
                valueIsPath = this.input.property( ChrysalixLexicon.Input.PATH ).booleanValue();
            }

            final boolean hasValue = this.input.hasProperty( ChrysalixLexicon.Input.VALUE );

            if ( valueIsPath ) {
                // value is a model property path
                if ( hasValue ) {
                    final String propPath = this.input.property( ChrysalixLexicon.Input.VALUE ).stringValue();
                    final ModelProperty modelProperty = this.model().property( propPath );

                    if ( modelProperty.descriptor().multiple() ) {
                        return ( T ) modelProperty.values();
                    }

                    return ( T ) modelProperty.value();
                }

                throw new ChrysalixException( ChrysalixI18n.localize( ERROR_GETTING_PATH_VALUE, this.descriptorId ) );
            }

            // value is a literal
            if ( hasValue ) {
                return ( T ) this.input.property( ChrysalixLexicon.Input.VALUE ).value();
            }

            return null; // no value set
        } catch ( final Exception pe ) {
            throw new ChrysalixException( ChrysalixI18n.localize( ERROR_GETTING_VALUE, this.descriptorId ) );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelElement#model()
     */
    @Override
    public Model model() throws ModelspaceException {
        return this.input.model();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Value#modelObect()
     */
    @Override
    public ModelObject modelObect() {
        return this.input;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelElement#modelRelativePath()
     */
    @Override
    public String modelRelativePath() throws ModelspaceException {
        return this.input.modelRelativePath();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.ModelElement#name()
     */
    @Override
    public String name() throws ModelspaceException {
        return this.input.name();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.Value#set(java.lang.Object)
     */
    @Override
    public void set( final Object proposedValue ) throws ChrysalixException {
        if ( !descriptor().modifiable() ) {
            throw new UnsupportedOperationException( ChrysalixI18n.localize( VALUE_NOT_MODIFIABLE, this.descriptorId ) );
        }

        boolean isPath = false;
        Object newValue = null;

        if ( proposedValue != null ) {
            newValue = determineValueBasedOn( proposedValue );
            isPath = determinePathValueBasedOn( proposedValue );
        }

        try {
            this.input.setProperty( ChrysalixLexicon.Input.PATH, isPath );
            this.input.setProperty( ChrysalixLexicon.Input.VALUE, newValue );
        } catch ( final ModelspaceException e ) {
            throw new UnsupportedOperationException( ChrysalixI18n.localize( ERROR_SETTING_VALUE, this.descriptorId ) );
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
            return descriptor().toString();
        } catch ( final Exception e ) {
            return ChrysalixI18n.localize( DESCRIPTOR_ERROR, this.descriptorId );
        }
    }

}
