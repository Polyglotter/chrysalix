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
package org.modeshape.modeler.eclipse;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.ui.FocusTreeModel;
import org.modeshape.modeler.ui.UiUtil;
import org.polyglotter.common.ObjectUtil;

@SuppressWarnings( "javadoc" )
public class FocusTreeFileSystemModel extends FocusTreeModel {

    static final URL TRANSFORMATION_INDICATOR_IMAGE = UiUtil.imageUrl( "transformation.png" );

    @Override
    public Object add( final Object folder,
                       final int index ) throws ModelerException {
        try {
            final File file = new File( ( ( File ) folder ), "Unnamed" );
            if ( !file.createNewFile() ) throw new ModelerException( TestEclipseI18n.unableToCreateFile, folder, index );
            return file;
        } catch ( final IOException e ) {
            throw new ModelerException( e );
        }
    }

    @Override
    public int childCount( final Object file ) {
        return children( file ).length;
    }

    @Override
    public Object[] children( final Object file ) {
        final Object[] children = ( ( File ) file ).listFiles();
        return children == null ? ObjectUtil.EMPTY_ARRAY : children;
    }

    @Override
    public boolean childrenAddable( final Object item ) {
        final File file = ( File ) item;
        return file.isDirectory() && file.canWrite();
    }

    @Override
    public boolean deletable( final Object file ) {
        return ( ( File ) file ).canWrite();
    }

    @Override
    public boolean delete( final Object file ) {
        return ( ( File ) file ).delete();
    }

    @Override
    public boolean hasChildren( final Object item ) {
        return children( item ).length > 0;
    }

    @Override
    public Indicator[] indicators( final Object file ) {
        return ( ( File ) file ).isDirectory() ? NO_INDICATORS : new Indicator[] { new Indicator( TRANSFORMATION_INDICATOR_IMAGE,
                                                                                                  "This item is part of at least one transformation" ) {

            @Override
            protected void selected( final Object item ) {
                System.out.println( "transformation for " + name( item ) );
            }
        } };
    }

    @Override
    public boolean movable( final Object item ) {
        return true;
    }

    @Override
    public String name( final Object file ) {
        final String name = ( ( File ) file ).getName();
        return name.isEmpty() ? "/" : name;
    }

    @Override
    public boolean nameEditable( final Object file ) {
        return ( ( File ) file ).canWrite();
    }

    @Override
    public String nameProblem( final Object file,
                               final Object name ) {
        for ( final File sibling : ( ( File ) file ).getParentFile().listFiles() )
            if ( !sibling.equals( file ) && sibling.getName().equals( name ) )
                return "A file with this name already exists";
        return null;
    }

    @Override
    public String qualifiedName( final Object file ) {
        return ( ( File ) file ).getAbsolutePath();
    }

    @Override
    public Object setName( final Object item,
                           final Object name ) throws ModelerException {
        final File file = ( File ) item;
        final File renamedFile = new File( file.getParentFile(), name.toString() );
        if ( file.renameTo( renamedFile ) ) return renamedFile;
        throw new ModelerException( TestEclipseI18n.unableToRenameFile, file, renamedFile );
    }

    @Override
    public String type( final Object file ) {
        return ( ( File ) file ).isDirectory() ? "Folder" : "File";
    }
}
