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
package org.modelspace.ddl.relational;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelspace.ModelspaceException;

/**
 * RelationalObject - the basis for all relational objects
 */
public abstract class RelationalObject implements RelationalConstants {

    @SuppressWarnings( "javadoc" )
    protected IObjectDelegate delegate;

    RelationalObject( final IObjectDelegate delegate ) {
        this.delegate = delegate;
    }

    /**
     * Get the object children
     * 
     * @return the children
     * @throws ModelspaceException
     *         if an error occurs
     */
    public List< RelationalObject > getChildren() throws ModelspaceException {
        return this.delegate.getChildren();
    }

    /**
     * Get children of provided type
     * 
     * @param type
     *        the type
     * @return the children of given type
     * @throws ModelspaceException
     *         if an error occurs
     */
    @SuppressWarnings( "unchecked" )
    protected < T > List< T > getChildren( final Class< T > type ) throws ModelspaceException {
        final List< T > result = new ArrayList< T >();
        for ( final Object e : getChildren() ) {
            if ( type.isAssignableFrom( e.getClass() ) ) {
                result.add( ( T ) e );
            }
        }
        return result;
    }

    /**
     * Get the object description
     * 
     * @return description
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getDescription() throws ModelspaceException {
        final Object propValue = getPropertyValue( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION );
        return ( propValue == null ) ? null : propValue.toString();
    }

    /**
     * Get the object's extension properties. These are the non-standard properties that are prefixed.
     * 
     * @return the Map of extension properties
     */
    public Map< String, String > getExtensionProperties() {
        return this.delegate.getExtensionProperties();
    }

    /**
     * Get the object name
     * 
     * @return name
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getName() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME ).toString();
    }

    /**
     * Get the object name in source
     * 
     * @return name in source
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getNameInSource() throws ModelspaceException {
        final Object propValue = getPropertyValue( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE );
        return ( propValue == null ) ? null : propValue.toString();
    }

    /**
     * Get the object properties
     * 
     * @return the properties
     * @throws ModelspaceException
     *         if an error occurs
     */
    public Map< String, Object > getProperties() throws ModelspaceException {
        final Map< String, Object > props = new HashMap< String, Object >();

        // Add values for common properties
        String[] propKeys = RelationalConstants.COMMON_DDL_PROPERTY_KEYS.toArray();
        for ( int i = 0; i < propKeys.length; i++ ) {
            props.put( propKeys[ i ], getPropertyValue( propKeys[ i ] ) );
        }

        // Add values for common options
        propKeys = RelationalConstants.COMMON_DDL_OPTION_KEYS.toArray();
        for ( int i = 0; i < propKeys.length; i++ ) {
            props.put( propKeys[ i ], getPropertyValue( propKeys[ i ] ) );
        }

        // Add extension properties
        props.putAll( getExtensionProperties() );

        return props;
    }

    /**
     * Get the value of a property
     * 
     * @param propertyKey
     *        the property key
     * @return the property value
     * @throws ModelspaceException
     *         if an error occurs
     */
    public Object getPropertyValue( final String propertyKey ) throws ModelspaceException {
        Object propertyValue = null;
        if ( !this.delegate.isExtensionProperty( propertyKey ) ) {
            if ( propertyKey.equals( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME ) ) {
                propertyValue = this.delegate.getName();
            } else if ( propertyKey.equals( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE ) ) {
                propertyValue = this.delegate.getOptionValue( propertyKey );
            } else if ( propertyKey.equals( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION ) ) {
                return this.delegate.getOptionValue( propertyKey );
            }
        } else {
            final Map< String, String > extensionProps = this.delegate.getExtensionProperties();
            for ( final String key : extensionProps.keySet() ) {
                if ( key.equals( propertyKey ) ) {
                    propertyValue = extensionProps.get( key );
                    break;
                }
            }
        }
        return propertyValue;
    }

    /**
     * Get the object type
     * 
     * @return the object type
     * @throws ModelspaceException
     *         if an error occurs
     */
    public RelationalConstants.Type getType() throws ModelspaceException {
        return this.delegate.getType();
    }

    /**
     * Determine if object has children
     * 
     * @return 'true' if has children, 'false' if not
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean hasChildren() throws ModelspaceException {
        return this.delegate.hasChildren();
    }

    /**
     * Set the object description
     * 
     * @param description
     *        the description
     */
    public void setDescription( final String description ) {
        setPropertyValue( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, description );
    }

    /**
     * Set the object name in source
     * 
     * @param nameInSource
     *        the name in source
     */
    public void setNameInSource( final String nameInSource ) {
        setPropertyValue( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, nameInSource );
    }

    @SuppressWarnings( "javadoc" )
    public boolean setPropertyValue( final String propertyKey,
                                     final Object propValue ) {
        boolean wasSet = false;

        // ---------------------------
        // Properties
        // ---------------------------
        if ( propertyKey.equals( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME ) ) {
            wasSet = this.delegate.setPropertyValue( propertyKey, propValue );
            // ---------------------------
            // Statement Options
            // ---------------------------
        } else if ( propertyKey.equals( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE ) ) {
            if ( propValue == null || ( ( String ) propValue ).equals( RelationalConstants.COMMON_DEFAULT.NAME_IN_SOURCE ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION ) ) {
            if ( propValue == null || ( ( String ) propValue ).equals( RelationalConstants.COMMON_DEFAULT.DESCRIPTION ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propValue );
            }
        }

        return wasSet;
    }

    /**
     * Set the value of a property
     * 
     * @param propertyKey
     *        the property key
     * @param propertyValue
     *        the property value
     */
    // public void setPropertyValue(String propertyKey,Object propertyValue) {
    // if(!this.delegate.isExtensionProperty(propertyKey)) {
    // if(propertyKey.equals(RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME)) {
    // propertyValue = this.delegate.getName();
    // } else if(propertyKey.equals(RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE)) {
    // propertyValue = this.delegate.getOptionValue(propertyKey);
    // } else if(propertyKey.equals(RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION)) {
    // return this.delegate.getOptionValue(propertyKey);
    // }
    // } else {
    // Map<String,String> extensionProps = this.delegate.getExtensionProperties();
    // for(String key : extensionProps.keySet()) {
    // if(key.equals(propertyKey)) {
    // propertyValue = extensionProps.get(key);
    // break;
    // }
    // }
    // }
    // }

}
