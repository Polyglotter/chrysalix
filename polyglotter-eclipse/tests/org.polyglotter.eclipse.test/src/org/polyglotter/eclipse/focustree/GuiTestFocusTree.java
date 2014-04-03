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
package org.polyglotter.eclipse.focustree;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.eclipse.GuiTestUtil;
import org.polyglotter.eclipse.TestEclipseI18n;
import org.polyglotter.eclipse.Util;
import org.polyglotter.eclipse.focustree.FocusTree.Indicator;
import org.polyglotter.eclipse.focustree.FocusTree.Model;
import org.polyglotter.eclipse.focustree.FocusTree.ViewModel;

/**
 * 
 */
public class GuiTestFocusTree {

    static final Color FOLDER_COLOR = new Color( Display.getCurrent(), 0, 64, 128 );

    static final Image TRANSFORMATION_INDICATOR_IMAGE = Util.image( "transformation.png" );

    /**
     * @param args
     *        command-line arguments
     */
    public static void main( final String[] args ) {
        // Util.setLogLevel( Level.TRACE );
        final FocusTree focusTree = new FocusTree( GuiTestUtil.shell(), new File( System.getProperty( "user.home" ) ), new Model() {

            @Override
            public int childCount( final Object file ) {
                return children( file ).length;
            }

            @Override
            public Object[] children( final Object file ) {
                final Object[] children = ( ( File ) file ).listFiles();
                return children == null ? Util.EMPTY_ARRAY : children;
            }

            @Override
            public boolean childrenAddable( final Object item ) {
                final File file = ( File ) item;
                return file.isDirectory() && file.canWrite();
            }

            @Override
            public Object add( final Object folder,
                                  final int index ) throws PolyglotterException {
                try {
                    final File file = new File( ( ( File ) folder ), "Unnamed" );
                    if ( !file.createNewFile() ) throw new PolyglotterException( TestEclipseI18n.unableToCreateFile, folder, index );
                    return file;
                } catch ( final IOException e ) {
                    throw new PolyglotterException( e );
                }
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
                                   final Object name ) throws PolyglotterException {
                final File file = ( File ) item;
                final File renamedFile = new File( file.getParentFile(), name.toString() );
                if ( file.renameTo( renamedFile ) ) return renamedFile;
                throw new PolyglotterException( TestEclipseI18n.unableToRenameFile, file, renamedFile );
            }

            @Override
            public String type( final Object file ) {
                return ( ( File ) file ).isDirectory() ? "Folder" : "File";
            }
        } );
        focusTree.setViewModel( new ViewModel() {

            @Override
            public Color cellBackgroundColor( final Object file ) {
                if ( ( ( File ) file ).isDirectory() ) return FOLDER_COLOR;
                return Display.getCurrent().getSystemColor( SWT.COLOR_WHITE );
            }

            @Override
            public Image icon( final Object file ) {
                return Util.image( ( ( File ) file ).isDirectory() ? "folder.gif" : "file.gif" );
            }

            @Override
            public Image iconViewIcon( final Object file ) {
                final Image image = icon( file );
                return new Image( image.getDevice(),
                                  image.getImageData().scaledTo( image.getBounds().width * 4, image.getBounds().width * 4 ) );
            }

            @Override
            public int initialCellWidth() {
                return 100;
            }

            @Override
            public boolean initialIndexIsOne() {
                return true;
            }
        } );
        GuiTestUtil.show( focusTree );
    }
}
