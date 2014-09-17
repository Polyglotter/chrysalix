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
package org.modeshape.modeler.eclipse;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.modeshape.modeler.eclipse.FocusTree.Column;
import org.modeshape.modeler.ui.FocusTreeView;

/**
 * 
 */
class FocusTreeLightweight extends FigureCanvas {

    final FocusTree focusTree;
    FreeformLayer canvas;
    Panel focusLine;

    FocusTreeLightweight( final FocusTree focusTree,
                          final Composite parent,
                          final int style ) {
        super( parent, style );
        this.focusTree = focusTree;
        setViewport( new FreeformViewport() );
        getViewport().setContentsTracksHeight( true );
        getViewport().setContentsTracksWidth( true );
        getHorizontalBar().setEnabled( false );
        setHorizontalScrollBarVisibility( NEVER );
    }

    void constructCanvas( final Color color ) {
        canvas = new FreeformLayer();
        setContents( canvas );
        canvas.setLayoutManager( new FreeformLayout() );
        canvas.setOpaque( true );
        canvas.setBackgroundColor( color );
    }

    void constructFocusLine( final String description,
                             final Color color,
                             final int y,
                             final int height ) {
        focusLine = new Panel();
        canvas.add( focusLine );
        focusLine.setToolTip( new Label( description ) );
        focusLine.setBackgroundColor( color );
        // Make focus line extend horizontally across entire canvas
        focusLine.setBounds( new Rectangle( 0, y, 0, height ) );
        canvas.addFigureListener( new FigureListener() {

            @Override
            public void figureMoved( final IFigure source ) {
                focusTree.controller.treeResized();
            }
        } );
    }

    ColumnView newColumnView() {
        return new ColumnView();
    }

    void treeResized() {
        final Rectangle bounds = focusLine.getBounds();
        bounds.width = canvas.getBounds().width;
        focusLine.setBounds( bounds );
    }

    class ColumnView extends Figure implements FocusTreeView.Column.View {

        Column column;

        /**
         * {@inheritDoc}
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Column.View#column()
         */
        @Override
        public FocusTreeView.Column column() {
            return column;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Column.View#setColumn(org.modeshape.modeler.ui.FocusTreeView.Column)
         */
        @Override
        public void setColumn( final org.modeshape.modeler.ui.FocusTreeView.Column column ) {
            this.column = ( Column ) column;
        }
    }
}
