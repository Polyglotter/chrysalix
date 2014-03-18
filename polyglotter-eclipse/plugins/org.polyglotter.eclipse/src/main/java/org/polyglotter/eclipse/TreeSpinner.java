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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.LayoutConstants;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.polyglotter.common.CheckArg;
import org.polyglotter.common.PolyglotterException;

/**
 * 
 */
public class TreeSpinner extends Composite {

    final Composite pathButtonBar;
    final Composite headerBar;
    final TreeCanvas innerCanvas;
    final Composite outerCanvas;
    final ScrolledComposite scroller;
    final List< Column > columns = new ArrayList<>();
    final PaintListener pathButtonPaintListener = new PaintListener() {

        @Override
        public void paintControl( final PaintEvent event ) {
            event.gc.setBackground( TreeSpinner.this.pathButtonBar.getBackground() );
            event.gc.fillRectangle( event.x, event.y, event.width, event.height );
            final Label label = ( Label ) event.widget;
            event.gc.setBackground( label.getBackground() );
            event.gc.fillRoundRectangle( event.x, event.y, event.width, event.height, event.height, event.height );
            event.gc.drawString( label.getText(), event.height / 2, event.y );
        }
    };
    final PaintListener headerPaintListener = new PaintListener() {

        @Override
        public void paintControl( final PaintEvent event ) {
            final Rectangle bounds = ( ( Composite ) event.widget ).getBounds();
            event.gc.drawRectangle( 0, 0, bounds.width - 1, bounds.height - 1 );
        }
    };

    Object root;
    TreeSpinnerContentProvider provider;

    /**
     * @param parent
     *        parent composite
     */
    public TreeSpinner( final Composite parent ) {
        super( parent, SWT.NONE );
        GridLayoutFactory.fillDefaults().numColumns( 4 ).applyTo( this );
        ( ( GridLayout ) getLayout() ).verticalSpacing = 0;

        // Construct left tool bar
        // TODO i18n
        ToolBar toolBar = new ToolBar( this, SWT.NONE );
        newToolBarButton( toolBar, SWT.PUSH, "home.gif", "Scroll to root column" );
        newToolBarButton( toolBar, SWT.PUSH, "add.gif", "Add item below focus line of selected column" );
        newToolBarButton( toolBar, SWT.PUSH, "delete.gif", "Delete item from focus line of selected column" );
        newToolBarButton( toolBar, SWT.PUSH, "collapseall.gif", "Collapse all columns below root column" );
        newToolBarButton( toolBar, SWT.PUSH, "icons.gif", "View only the selected column as thumbnails" );
        newToolBarButton( toolBar, SWT.PUSH, "duplicate.gif", "Create a clone below this tree" );
        newToolBarButton( toolBar, SWT.CHECK, "sync.gif", "Link with other views" );
        newToolBarButton( toolBar, SWT.PUSH, "rotate.gif", "Rotate tree 90°" );

        // Construct zoom slider
        final Composite sliderPanel = new Composite( this, SWT.BORDER );
        GridDataFactory.swtDefaults().applyTo( sliderPanel );
        GridLayoutFactory.fillDefaults().applyTo( sliderPanel );
        final Slider slider = new Slider( sliderPanel, SWT.NONE );
        slider.setSelection( 50 );
        slider.setThumb( 1 );
        slider.setToolTipText( "Zoom view in or out" );

        // Construct search field
        final Text text = new Text( this, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL );
        GridDataFactory.swtDefaults().align( SWT.FILL, SWT.CENTER ).grab( true, false ).applyTo( text );
        text.setToolTipText( "Search for items in or below this tree's root" );

        // Construct right tool bar
        toolBar = new ToolBar( this, SWT.NONE );
        GridDataFactory.swtDefaults().applyTo( toolBar );
        newToolBarButton( toolBar, SWT.PUSH, "close.gif", "Close this tool bar (Reopen using context menu)" );

        // Construct path bar
        final Composite pathBar = new Composite( this, SWT.BORDER );
        GridDataFactory.swtDefaults().align( SWT.FILL, SWT.CENTER ).grab( true, false ).span( 4, 1 ).applyTo( pathBar );
        GridLayoutFactory.fillDefaults().margins( LayoutConstants.getSpacing().x, 0 ).numColumns( 3 ).applyTo( pathBar );
        this.pathButtonBar = new Composite( pathBar, SWT.NONE );
        RowLayoutFactory.fillDefaults().fill( true ).wrap( false ).applyTo( this.pathButtonBar );
        toolBar = new ToolBar( pathBar, SWT.NONE );
        newToolBarButton( toolBar, SWT.PUSH, "copy.gif", "Copy the path of the selected column to the clipboard" );
        toolBar = new ToolBar( pathBar, SWT.NONE );
        GridDataFactory.swtDefaults().align( SWT.END, SWT.CENTER ).grab( true, false ).applyTo( toolBar );
        newToolBarButton( toolBar, SWT.PUSH, "close.gif", "Close this path bar (Reopen using context menu)" );

        // Construct scrolling diagram area
        this.scroller = new ScrolledComposite( this, SWT.H_SCROLL | SWT.V_SCROLL );
        GridDataFactory.fillDefaults().grab( true, true ).span( 4, 1 ).applyTo( this.scroller );
        this.scroller.setExpandVertical( true );

        // Construct outer canvas
        this.outerCanvas = new Composite( this.scroller, SWT.NONE );
        // GridDataFactory.fillDefaults().grab( true, true ).applyTo( outerCanvas );
        GridLayoutFactory.fillDefaults().spacing( 0, 0 ).applyTo( this.outerCanvas );
        this.scroller.setContent( this.outerCanvas );

        // Construct header bar
        this.headerBar = new Composite( this.outerCanvas, SWT.NONE );
        GridDataFactory.swtDefaults().align( SWT.FILL, SWT.CENTER ).grab( true, false ).applyTo( this.headerBar );
        GridLayoutFactory.fillDefaults().spacing( 0, 0 ).numColumns( 0 ).applyTo( this.headerBar );

        // Construct inner canvas
        this.innerCanvas = new TreeCanvas( this, this.outerCanvas, SWT.NONE );
        GridDataFactory.fillDefaults().grab( true, true ).applyTo( this.innerCanvas );
    }

    void addColumn( final Object item ) throws PolyglotterException {

        // Add a new column
        final Column column = new Column();
        this.columns.add( column );
        column.item = item;

        // Add button for column to path button bar
        column.pathButton = new Label( this.pathButtonBar, SWT.NONE );
        column.pathButton.setBackground( this.provider.backgroundColor( item ) );
        column.pathButton.setForeground( this.provider.foregroundColor( item ) );
        column.pathButton.setText( this.provider.name( item ) );
        final Point size = column.pathButton.computeSize( SWT.DEFAULT, SWT.DEFAULT );
        column.pathButton.setAlignment( SWT.CENTER );
        column.pathButton.setLayoutData( new RowData( size.x + 10, size.y ) );
        column.pathButton.addPaintListener( this.pathButtonPaintListener );
        this.pathButtonBar.getParent().layout();

        // Construct header
        ( ( GridLayout ) this.headerBar.getLayout() ).numColumns++;
        column.header = new Composite( this.headerBar, SWT.NONE );
        GridLayoutFactory.fillDefaults().numColumns( 3 ).applyTo( column.header );
        column.header.setBackground( Display.getCurrent().getSystemColor( SWT.COLOR_GRAY ) );
        column.header.addPaintListener( this.headerPaintListener );
        final Label childCount = new Label( column.header, SWT.NONE );
        childCount.setText( String.valueOf( this.provider.childCount( item ) ) );
        final Label label = new Label( column.header, SWT.CENTER );
        label.setText( this.provider.name( item ) );
        final FontData fontData = label.getFont().getFontData()[ 0 ];
        fontData.setStyle( SWT.BOLD );
        label.setFont( new Font( Display.getCurrent(), fontData ) );
        GridDataFactory.swtDefaults().align( SWT.FILL, SWT.FILL ).grab( true, true ).applyTo( label );
        final Label minimizeButton = new Label( column.header, SWT.NONE );
        minimizeButton.setImage( Activator.plugin().image( "minimize.gif" ) );
        minimizeButton.setAlignment( SWT.RIGHT );
        minimizeButton.setToolTipText( "Minimize this column" );

        // Make child count and minimize buttons the same size so arrow is centered
        final Point childCountSize = childCount.computeSize( SWT.DEFAULT, SWT.DEFAULT );
        final Point minimizeButtonSize = minimizeButton.computeSize( SWT.DEFAULT, SWT.DEFAULT );
        int width = Math.max( childCountSize.x, minimizeButtonSize.x );
        GridDataFactory.swtDefaults().hint( width, SWT.DEFAULT ).applyTo( childCount );
        GridDataFactory.swtDefaults().hint( width, SWT.DEFAULT ).applyTo( minimizeButton );

        // Add column to inner canvas
        this.innerCanvas.addColumn( column );

        // Update width of header to match child column width
        width = column.childColumn.getSize().width;
        GridDataFactory.swtDefaults().hint( width, SWT.DEFAULT ).applyTo( column.header );
        this.headerBar.layout();

        this.outerCanvas.setSize( this.outerCanvas.computeSize( SWT.DEFAULT, this.outerCanvas.getSize().y ) );
    }

    //
    // Image newArrowImage( final int width,
    // final int height,
    // final boolean upArrow ) {
    // final Image image = new Image( Display.getDefault(), width, height );
    // final GC gc = new GC( image );
    // gc.setAntialias( SWT.ON );
    // gc.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_BLACK ) );
    // if ( upArrow ) for ( int x = width / 2, y = 2; y < height - 2; x--, y++ )
    // gc.drawLine( x, y, x + y * 2 - 2, y );
    // else for ( int y = 2, w = ( height - 3 ) * 2 - 2, x = ( width - w ) / 2; y < height - 2; x++, y++, w -= 2 )
    // gc.drawLine( x, y, x + w, y );
    // gc.dispose();
    // final ImageData data = image.getImageData();
    // image.dispose();
    // data.transparentPixel = data.palette.getPixel( new RGB( 255, 255, 255 ) );
    // return new Image( Display.getCurrent(), data );
    // }

    void newToolBarButton( final ToolBar toolBar,
                           final int style,
                           final String iconName,
                           final String toolTip ) {
        final ToolItem item = new ToolItem( toolBar, style );
        item.setImage( Activator.plugin().image( iconName ) );
        item.setToolTipText( toolTip );
    }

    void removeColumnsAfter( final Column column ) {
        int ndx = this.columns.size() - 1;
        for ( Column col = this.columns.get( ndx ); col != column; col = this.columns.get( --ndx ) ) {
            col.header.dispose();
            ( ( GridLayout ) this.headerBar.getLayout() ).numColumns--;
            col.pathButton.dispose();
            this.innerCanvas.removeColumn( col );
            this.columns.remove( ndx );
        }
        this.headerBar.layout();
        this.pathButtonBar.getParent().layout();
    }

    void scroll( final int innerCanvasWidth ) {
        this.scroller.setOrigin( innerCanvasWidth - this.scroller.getClientArea().width, 0 );
    }

    /**
     * @param root
     *        the root object of the tree
     * @param provider
     *        a tree content provider
     * @throws PolyglotterException
     *         if there is an error
     */
    public void setRootAndContentProvider( final Object root,
                                           final TreeSpinnerContentProvider provider ) throws PolyglotterException {
        CheckArg.notNull( root, "root" );
        CheckArg.notNull( provider, "provider" );
        this.root = root;
        this.provider = provider;
        this.innerCanvas.provider = provider;
        for ( final Control control : this.pathButtonBar.getChildren() )
            control.dispose();
        for ( final Control control : this.headerBar.getChildren() )
            control.dispose();
        addColumn( root );
    }

    class Column {

        Object item;
        Composite header;
        Label pathButton;
        IFigure backgroundColumn;
        IFigure childColumn;
        Object focusChild;
        Shape focusCell;
        boolean focusCellExpanded;
    }
}
