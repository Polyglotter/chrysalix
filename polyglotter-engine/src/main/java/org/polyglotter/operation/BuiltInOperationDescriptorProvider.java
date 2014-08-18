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
package org.polyglotter.operation;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.polyglotter.transformation.Operation;
import org.polyglotter.transformation.OperationDescriptor;
import org.polyglotter.transformation.OperationDescriptorProvider;

/**
 * A provider for the built-in {@link Operation operation} descriptors.
 */
public final class BuiltInOperationDescriptorProvider implements OperationDescriptorProvider {

    private static final String CLASS_EXT = ".class";
    private static final String PKG_NAME = BuiltInOperationDescriptorProvider.class.getPackage().getName();
    private static final String PATH = PKG_NAME.replace( '.', '/' );

    private final List< OperationDescriptor< ? > > descriptors = new ArrayList<>();

    private String className( final File file ) {
        if ( file.getName().endsWith( CLASS_EXT ) ) {
            return ( PKG_NAME + '.' + file.getName().substring( 0, file.getName().length() - CLASS_EXT.length() ) );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.transformation.OperationDescriptorProvider#descriptors()
     */
    @Override
    public List< OperationDescriptor< ? >> descriptors() {
        if ( this.descriptors == null ) {
            discoverDescriptors();
        }

        return this.descriptors;
    }

    private void discoverDescriptors() {
        try {
            final Enumeration< URL > urls = getClass().getClassLoader().getResources( PATH );

            while ( urls.hasMoreElements() ) {
                final URL url = urls.nextElement();
                final File dir = new File( url.getFile() );

                for ( final File file : dir.listFiles() ) {
                    final String className = className( file );

                    if ( className != null ) {
                        final Class< ? > clazz = Class.forName( className );

                        if ( isOperation( clazz ) ) {
                            final Field descriptor = clazz.getField( OperationDescriptor.DESCRIPTOR_NAME );

                            if ( Modifier.isStatic( descriptor.getModifiers() ) ) {
                                this.descriptors.add( ( OperationDescriptor< ? > ) descriptor.get( null ) );
                            }
                        }
                    }
                }
            }
        } catch ( final Exception e ) {
            e.printStackTrace();
        }
    }

    private boolean isOperation( final Class< ? > clazz ) {
        return ( Operation.class.isAssignableFrom( clazz ) && !Modifier.isAbstract( clazz.getModifiers() ) );
    }

}
