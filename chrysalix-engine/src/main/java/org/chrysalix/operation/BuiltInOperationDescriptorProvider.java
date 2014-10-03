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

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.chrysalix.transformation.Operation;
import org.chrysalix.transformation.OperationDescriptorProvider;
import org.chrysalix.transformation.ValueDescriptor;

/**
 * A provider for the built-in {@link Operation operation} descriptors.
 */
public final class BuiltInOperationDescriptorProvider implements OperationDescriptorProvider {

    private static final String CLASS_EXT = ".class";
    private static final String PKG_NAME = BuiltInOperationDescriptorProvider.class.getPackage().getName();
    private static final String PATH = PKG_NAME.replace( '.', '/' );

    private List< ValueDescriptor< ? > > descriptors;

    private String className( final File file ) {
        if ( file.getName().endsWith( CLASS_EXT ) ) {
            return ( PKG_NAME + '.' + file.getName().substring( 0, file.getName().length() - CLASS_EXT.length() ) );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.chrysalix.transformation.OperationDescriptorProvider#descriptors()
     */
    @Override
    public List< ValueDescriptor< ? >> descriptors() {
        if ( this.descriptors == null ) {
            discoverDescriptors();
        }

        return this.descriptors;
    }

    /**
     * Discover and add descriptors.
     */
    private void discoverDescriptors() {
        descriptors = new ArrayList<>();

        try {
            final Enumeration< URL > urls = getClass().getClassLoader().getResources( PATH );
            final List< File > files = new ArrayList<>();

            while ( urls.hasMoreElements() ) {
                final URL url = urls.nextElement();
                final File file = new File( url.getFile() );
                findFiles( file, files );
            }

            for ( final File file : files ) {
                final String className = className( file );

                if ( className != null ) {
                    final Class< ? > clazz = Class.forName( className );

                    if ( isOperation( clazz ) ) {
                        // find all static fields that are descriptors
                        for ( final Field field : clazz.getFields() ) {
                            if ( Modifier.isStatic( field.getModifiers() )
                                 && !Modifier.isAbstract( field.getModifiers() )
                                 && ValueDescriptor.class.isAssignableFrom( field.getType() ) ) {
                                this.descriptors.add( ( ValueDescriptor< ? > ) field.get( null ) );
                            }
                        }
                    }
                }
            }
        } catch ( final Exception e ) {
            e.printStackTrace();
        }
    }

    private void findFiles( final File file,
                            final List< File > files ) {
        if ( file.isDirectory() ) {
            final File dir = file;

            for ( final File dirFile : dir.listFiles() ) {
                findFiles( dirFile, files );
            }
        } else {
            files.add( file );
        }
    }

    private boolean isOperation( final Class< ? > clazz ) {
        return ( Operation.class.isAssignableFrom( clazz ) && !Modifier.isAbstract( clazz.getModifiers() ) );
    }

}
