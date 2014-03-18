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
package org.polyglotter.eclipse.view;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.eclipse.TreeSpinner;
import org.polyglotter.eclipse.TreeSpinnerContentProvider;
import org.polyglotter.eclipse.Util;

/**
 * A view whose contents are the transforms contained in the workspace.
 */
public class TransformView extends ViewPart {

    static final Color FOLDER_COLOR = new Color( Display.getCurrent(), 0, 64, 128 );

    /**
     * The workspace part identifier for this view.
     */
    public static final String ID = WorkspaceView.class.getPackage().getName() + ".transformationsView";

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl( final Composite parent ) {
        final TreeSpinner treeSpinner = new TreeSpinner( parent );
        try {
            treeSpinner.setRootAndContentProvider( new File( System.getProperty( "user.home" ) ), new TreeSpinnerContentProvider() {

                @Override
                public Color backgroundColor( final Object item ) {
                    if ( ( ( File ) item ).isDirectory() ) return FOLDER_COLOR;
                    return Display.getCurrent().getSystemColor( SWT.COLOR_WHITE );
                }

                @Override
                public int childCount( final Object item ) {
                    return children( item ).length;
                }

                @Override
                public Object[] children( final Object item ) {
                    final Object[] children = ( ( File ) item ).listFiles();
                    return children == null ? Util.EMPTY_ARRAY : children;
                }

                @Override
                public boolean hasChildren( final Object item ) {
                    return childCount( item ) > 0;
                }

                @Override
                public String name( final Object item ) {
                    final File file = ( File ) item;
                    final String name = file.getName();
                    return name.isEmpty() ? "/" : name;
                }

                @Override
                public int preferredWidth( final Object item ) {
                    return 80;
                }

                @Override
                public String type( final Object item ) {
                    final File file = ( File ) item;
                    return file.isDirectory() ? "Folder" : "File";
                }
            } );
        } catch ( final PolyglotterException e ) {
            Util.handleModelError( parent.getShell(), e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {}
}
