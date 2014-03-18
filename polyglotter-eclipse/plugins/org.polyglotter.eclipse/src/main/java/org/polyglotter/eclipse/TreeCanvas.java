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
package org.polyglotter.eclipse;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.eclipse.TreeSpinner.Column;

class TreeCanvas extends FigureCanvas {

    static final Color DEFAULT_BACKGROUND_COLOR = Display.getCurrent().getSystemColor( SWT.COLOR_WHITE );
    static final Color DEFAULT_FOCUS_COLOR = new Color( Display.getCurrent(), 0, 128, 255 );
    static final Color DEFAULT_SELECTED_COLUMN_BACKGROUND_COLOR = new Color( Display.getCurrent(), 194, 223, 255 );

    static final int DEFAULT_FOCUS_LINE_OFFSET = 75;

    static final int FOCUS_HEIGHT = 3;

    TreeSpinner treeSpinner;
    TreeSpinnerContentProvider provider;

    final FreeformLayer canvas = new FreeformLayer();
    final Panel focusLine = new Panel();
    final Border focusBorder = new LineBorder( DEFAULT_FOCUS_COLOR, FOCUS_HEIGHT ) {

        @Override
        public void paint( final IFigure figure,
                           final Graphics graphics,
                           final Insets insets ) {
            tempRect.setBounds( getPaintRectangle( figure, insets ) );
            if ( getWidth() % 2 == 1 ) {
                tempRect.width--;
                tempRect.height--;
            }
            tempRect.shrink( getWidth() / 2, getWidth() / 2 );
            graphics.setLineWidth( getWidth() );
            graphics.setLineStyle( getStyle() );
            graphics.setForegroundColor( getColor() );
            graphics.drawRoundRectangle( tempRect, 8, 8 );
        }
    };
    final Border noFocusBorder = new LineBorder( this.focusBorder.getInsets( null ).top ) {

        @Override
        public void paint( final IFigure figure,
                           final Graphics graphics,
                           final Insets insets ) {}
    };
    Column selectedColumn;

    TreeCanvas( final TreeSpinner treeSpinner,
                final Composite parent,
                final int style ) {
        super( parent, style );
        this.treeSpinner = treeSpinner;
        // TODO get background color from preferences
        setBackground( Display.getCurrent().getSystemColor( SWT.COLOR_WHITE ) );
        setContents( this.canvas );
        getViewport().setContentsTracksHeight( true );
        this.canvas.addMouseListener( new MouseListener() {

            @Override
            public void mouseDoubleClicked( final MouseEvent event ) {}

            @Override
            public void mousePressed( final MouseEvent event ) {}

            @Override
            public void mouseReleased( final MouseEvent event ) {
                if ( event.button == 1 && ( event.getState() & SWT.MODIFIER_MASK ) == 0 ) {
                    IFigure figure = TreeCanvas.this.canvas.findFigureAt( event.x, event.y );
                    if ( figure instanceof Label ) figure = figure.getParent();
                    if ( figure instanceof Shape ) {
                        // Find column of new focus cell
                        boolean columnFound = false;
                        for ( final Column column : treeSpinner.columns ) {
                            for ( final Object cell : column.childColumn.getChildren() )
                                if ( cell == figure ) {
                                    columnFound = true;
                                    if ( column.focusCell != figure ) {
                                        if ( column.focusCellExpanded ) removeColumnsAfter( column );
                                        focusCell( column, ( Shape ) figure );
                                    }
                                    // Expand/collapse focus cell if its item has children
                                    try {
                                        if ( TreeCanvas.this.provider.hasChildren( column.focusChild ) )
                                            if ( column.focusCellExpanded )
                                                removeColumnsAfter( column );
                                            else {
                                                treeSpinner.addColumn( column.focusChild );
                                                column.focusCellExpanded = true;
                                            }
                                    } catch ( final PolyglotterException e ) {
                                        Util.handleModelError( getShell(), e );
                                    }
                                    // Change selected column
                                    if ( column != TreeCanvas.this.selectedColumn ) {
                                        if ( TreeCanvas.this.selectedColumn != null ) TreeCanvas.this.selectedColumn.backgroundColumn.setOpaque( false );
                                        column.backgroundColumn.setOpaque( true );
                                        TreeCanvas.this.selectedColumn = column;
                                    }
                                    // Scroll so that focus line is at focus line offset from top of view and last column is visible
                                    scrollToY( TreeCanvas.this.focusLine.getLocation().y - DEFAULT_FOCUS_LINE_OFFSET );
                                    treeSpinner.scroll( TreeCanvas.this.canvas.getSize().width );
                                    break;
                                }
                            if ( columnFound ) break;
                        }
                    }
                }
            }

            private void removeColumnsAfter( final Column column ) {
                treeSpinner.removeColumnsAfter( column );
                column.focusCellExpanded = false;
                final Rectangle childColumnBounds = column.childColumn.getBounds();
                TreeCanvas.this.canvas.setSize( childColumnBounds.x + childColumnBounds.width, TreeCanvas.this.canvas.getSize().height );
            }
        } );
        this.canvas.add( this.focusLine );
        // TODO get focus line color from preferences
        this.focusLine.setBackgroundColor( DEFAULT_FOCUS_COLOR );
        this.focusLine.setBounds( new Rectangle( 0, 0, 0, FOCUS_HEIGHT ) );
        this.focusLine.addLayoutListener( new LayoutListener.Stub() {

            @Override
            public void postLayout( final IFigure container ) {
                // Make focus line extend across entire canvas
                final Rectangle bounds = TreeCanvas.this.focusLine.getBounds();
                bounds.width = TreeCanvas.this.canvas.getBounds().width;
                TreeCanvas.this.focusLine.setBounds( bounds );
            }
        } );
    }

    void addColumn( final Column column ) {
        column.backgroundColumn = new Figure();
        // Add columns to beginning of canvas's children to ensure their backgrounds are painted first
        this.canvas.add( column.backgroundColumn, 0 );
        column.backgroundColumn.setBackgroundColor( DEFAULT_SELECTED_COLUMN_BACKGROUND_COLOR );
        column.childColumn = new Figure();
        this.canvas.add( column.childColumn );
        final GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 20;
        column.childColumn.setLayoutManager( layout );

        // Create cells
        int ndx = 0;
        try {
            for ( final Object child : this.provider.children( column.item ) ) {
                final Shape cell = this.provider.createCell( child );
                // Focus on first cell
                if ( column.focusCell == null ) column.focusCell = cell;
                column.childColumn.add( cell );
                column.childColumn.setConstraint( cell, new GridData( SWT.FILL, SWT.DEFAULT, false, false ) );
                cell.setLayoutManager( new GridLayout() );
                cell.setBorder( this.noFocusBorder );
                cell.setBackgroundColor( this.provider.backgroundColor( child ) );
                final Label label = new Label( String.valueOf( ndx++ ) );
                cell.add( label );
                label.setForegroundColor( this.provider.childIndexColor( column.item ) );
                newLabel( this.provider.name( child ), cell, child, this.provider );
                newLabel( this.provider.type( child ), cell, child, this.provider );
            }
        } catch ( final PolyglotterException e ) {
            Util.handleModelError( getShell(), e );
        }

        // Set size of first cell to focus on
        final Dimension firstCellSize = column.focusCell.getPreferredSize();
        column.focusCell.setSize( firstCellSize );

        // Set bounds
        final Dimension childColumnSize = column.childColumn.getPreferredSize();
        final int childColumnX;
        if ( this.treeSpinner.columns.size() == 1 )
            childColumnX = 0;
        else {
            final Rectangle previousChildColumnBounds =
                this.treeSpinner.columns.get( this.treeSpinner.columns.size() - 2 ).childColumn.getBounds();
            childColumnX = previousChildColumnBounds.x + previousChildColumnBounds.width;
        }
        column.childColumn.setBounds( new Rectangle( childColumnX, 0, childColumnSize.width, childColumnSize.height ) );
        final int canvasWidth = childColumnX + childColumnSize.width;
        this.focusLine.setSize( canvasWidth, this.focusLine.getSize().height );
        final int canvasHeight = this.canvas.getSize().height;
        this.canvas.setSize( canvasWidth, canvasHeight );
        column.backgroundColumn.setBounds( new Rectangle( childColumnX, 0, childColumnSize.width, canvasHeight ) );

        // Focus on first cell
        focusCell( column, column.focusCell );
    }

    void focusCell( final Column column,
                    final Shape focusCell ) {

        // Collapse previous focus cell and give it a no-focus border
        column.focusCell.setBorder( this.noFocusBorder );

        // Set new focus cell and child
        column.focusCell = focusCell;
        focusCell.setBorder( this.focusBorder );
        int ndx = 0;
        for ( final Object child : column.childColumn.getChildren() ) {
            if ( child == focusCell ) {
                try {
                    column.focusChild = this.provider.children( column.item )[ ndx ];
                } catch ( final PolyglotterException e ) {
                    Util.handleModelError( getShell(), e );
                }
                break;
            }
            ndx++;
        }
        final Rectangle focusLineBounds = this.focusLine.getBounds();
        int focusLineCenterY = focusLineBounds.y + focusLineBounds.height / 2;
        final Rectangle focusCellBounds = focusCell.getBounds();
        final int focusCellCenterY = focusCellBounds.y + focusCellBounds.height / 2;
        final Rectangle childColumnBounds = column.childColumn.getBounds();
        childColumnBounds.y -= focusCellCenterY - focusLineCenterY;
        int minY = Short.MAX_VALUE;
        int maxY = 0;
        final Rectangle reusableBounds = new Rectangle();
        for ( final Column col : this.treeSpinner.columns ) {
            reusableBounds.setBounds( col.childColumn.getBounds() );
            minY = Math.min( minY, reusableBounds.y );
            maxY = Math.max( maxY, reusableBounds.y + reusableBounds.height );
        }
        int newCanvasHeight = maxY - minY;
        final int topMargin = DEFAULT_FOCUS_LINE_OFFSET - ( focusLineBounds.y - minY );
        if ( topMargin > 0 ) {
            newCanvasHeight += topMargin;
            minY -= topMargin;
        }
        final Rectangle viewBounds = getViewport().getClientArea();
        final int bottomMargin = focusLineBounds.y + viewBounds.height - DEFAULT_FOCUS_LINE_OFFSET - maxY;
        if ( bottomMargin > 0 ) newCanvasHeight += bottomMargin;
        final Rectangle canvasBounds = this.canvas.getBounds();
        if ( newCanvasHeight != canvasBounds.height ) {
            canvasBounds.height = newCanvasHeight;
            this.canvas.setBounds( canvasBounds );
            for ( final Column col : this.treeSpinner.columns ) {
                reusableBounds.setBounds( col.childColumn.getBounds() );
                col.backgroundColumn.setBounds( new Rectangle( reusableBounds.x,
                                                               0,
                                                               reusableBounds.width,
                                                               canvasBounds.height ) );
            }
        }
        if ( minY != 0 ) {
            focusLineCenterY -= minY;
            focusLineBounds.y -= minY;
            this.focusLine.setBounds( focusLineBounds );
            for ( final Column col : this.treeSpinner.columns ) {
                // Update item backgroundColumn Y
                reusableBounds.setBounds( col.childColumn.getBounds() );
                reusableBounds.y -= minY;
                col.childColumn.setBounds( reusableBounds );
            }
        }
    }

    void newLabel( final String text,
                   final Shape cell,
                   final Object item,
                   final TreeSpinnerContentProvider provider ) {
        final Label label = new Label( text );
        cell.add( label );
        label.setTextAlignment( PositionConstants.CENTER );
        label.setForegroundColor( provider.foregroundColor( item ) );
        final GridData gridData = new GridData( SWT.FILL, SWT.DEFAULT, true, false );
        gridData.widthHint = provider.preferredWidth( item );
        cell.setConstraint( label, gridData );
    }

    void removeColumn( final Column column ) {
        this.canvas.remove( column.backgroundColumn );
        this.canvas.remove( column.childColumn );
    }
}
