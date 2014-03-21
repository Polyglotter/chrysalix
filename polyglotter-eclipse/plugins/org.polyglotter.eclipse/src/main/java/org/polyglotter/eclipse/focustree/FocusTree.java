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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.polyglotter.common.CheckArg;
import org.polyglotter.common.I18n;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.eclipse.EclipseI18n;
import org.polyglotter.eclipse.Util;
import org.polyglotter.eclipse.focustree.FocusTreeCanvas.AddPanel;
import org.polyglotter.eclipse.focustree.FocusTreeCanvas.CellColumn;
import org.polyglotter.eclipse.focustree.FocusTreeCanvas.DeleteButton;

//TODO icon view
// TODO move to modeshape modeler eclipse
//TODO cell editors
//TODO drag focus line under cell column
//TODO edit index via double-click
//TODO move position of cells
//TODO resize bug
//TODO zoom, search
//TODO context menu contributions; indicator context menu contributions
// TODO keyboard arrows
// TODO tab traversal
// TODO rotate
// TODO user override double-click cell
// TODO listen for model updates
//TODO swt vertical scroll bar
// TODO tutorial
//TODO filter mapped properties
//TODO filter referencing operations in sync'd tree
// TODO color registry?
// TODO separate view from controller
//TODO renderers for cells, add and delete buttons (background column, cell columns, headers?) to save memory
/**
 * 
 */
public class FocusTree extends Composite {

    /**
     * 
     */
    public static final Color DEFAULT_FOCUS_CELL_BORDER_COLOR = new Color( Display.getCurrent(), 0, 128, 255 );

    /**
     * 
     */
    public static final Color DEFAULT_FOCUS_COLUMN_COLOR = new Color( Display.getCurrent(), 194, 223, 255 );

    /**
     * 
     */
    public static final Color DEFAULT_FOCUS_LINE_COLOR = DEFAULT_FOCUS_CELL_BORDER_COLOR;

    /**
     * 
     */
    public static final int DEFAULT_FOCUS_LINE_HEIGHT = 5;

    /**
     *
     */
    public static final Color DEFAULT_TREE_BACKGROUND_COLOR = Display.getCurrent().getSystemColor( SWT.COLOR_WHITE );

    /**
     * 
     */
    public static final int DEFAULT_FOCUS_LINE_OFFSET = 75;

    static final Clipboard CLIPBOARD = new Clipboard( Display.getCurrent() );

    static final int HEADER_MARGIN = 2;

    private static final String HIDE_BUTTON_PROPERTY = "org.polyglotter.hideButton";

    /**
     * @param arguments
     *        ignored command-line arguments
     */
    public static void main( final String[] arguments ) {
        final Image image = new Image( Display.getDefault(), 16, 16 );
        final GC gc = new GC( image );
        gc.setAntialias( SWT.ON );
        gc.setBackground( image.getDevice().getSystemColor( SWT.COLOR_BLACK ) );
        gc.fillRectangle( 0, 0, 16, 16 );
        gc.setBackground( image.getDevice().getSystemColor( SWT.COLOR_GREEN ) );
        gc.fillOval( 0, 0, 16, 16 );
        gc.setForeground( image.getDevice().getSystemColor( SWT.COLOR_BLACK ) );
        gc.drawString( "T", ( 17 - gc.getCharWidth( 'T' ) ) / 2, 0, true );
        gc.dispose();
        final ImageLoader loader = new ImageLoader();
        final ImageData data = image.getImageData();
        data.transparentPixel = 0;
        loader.data = new ImageData[] { data };
        loader.save( "icons/transformation.png", SWT.IMAGE_PNG );
    }

    Object root;
    final Composite pathButtonBar;
    final Composite headerBar;
    final FocusTreeCanvas focusTreeCanvas;
    final Composite headeredCanvas;
    final ScrolledComposite scroller;
    final List< Column > columns = new ArrayList<>();
    final Label leftPathBarButton, rightPathBarButton;
    final PaintListener pathButtonPaintListener = new PaintListener() {

        @Override
        public void paintControl( final PaintEvent event ) {
            event.gc.setBackground( pathButtonBar.getBackground() );
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

    Model model;
    Column focusColumn;

    /**
     * Creates a non-closable focus tree
     * 
     * @param parent
     *        parent composite
     */
    public FocusTree( final Composite parent ) {
        this( parent, false );
    }

    /**
     * @param parent
     *        parent composite
     * @param closable
     *        <code>true</code> if this tree can be closed by the user
     */
    @SuppressWarnings( "unused" )
    public FocusTree( final Composite parent,
                      final boolean closable ) {
        super( parent, SWT.NONE );
        ( ( FillLayout ) parent.getLayout() ).type = SWT.VERTICAL;
        GridLayoutFactory.fillDefaults().spacing( 0, 0 ).applyTo( this );

        // Construct pop-up menu
        final Menu popup = new Menu( getShell(), SWT.POP_UP );
        setMenu( popup );
        final MenuItem homeMenuItem = new MenuItem( popup, SWT.PUSH );
        homeMenuItem.setText( EclipseI18n.focusTreeFocusMenuItem.text() );
        final MenuItem collapseAllMenuItem = new MenuItem( popup, SWT.PUSH );
        collapseAllMenuItem.setText( EclipseI18n.focusTreeCollapseAllMenuItem.text() );
        final MenuItem duplicateMenuItem = new MenuItem( popup, SWT.PUSH );
        duplicateMenuItem.setText( EclipseI18n.focusTreeDuplicateMenuItem.text() );
        new MenuItem( popup, SWT.SEPARATOR );
        final MenuItem copyPathMenuItem = new MenuItem( popup, SWT.PUSH );
        copyPathMenuItem.setText( EclipseI18n.focusTreeCopyPathMenuItem.text() );

        // // Construct zoom slider
        // final Composite sliderPanel = new Composite( toolBar, SWT.BORDER );
        // GridDataFactory.swtDefaults().applyTo( sliderPanel );
        // GridLayoutFactory.fillDefaults().applyTo( sliderPanel );
        // final Slider zoomSlider = new Slider( sliderPanel, SWT.NONE );
        // zoomSlider.setSelection( 50 );
        // zoomSlider.setThumb( 1 );
        // zoomSlider.setToolTipText( EclipseI18n.focusTreeZoomToolTip.text() );
        // // Construct search field
        // final Text searchText = new Text( toolBar, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL );
        // GridDataFactory.swtDefaults().align( SWT.FILL, SWT.CENTER ).grab( true, false ).applyTo( searchText );
        // searchText.setToolTipText( EclipseI18n.focusTreeSearchToolTip.text() );

        // Construct path bar
        final Composite pathBar = new Composite( this, SWT.NONE );
        GridDataFactory.swtDefaults().align( SWT.FILL, SWT.CENTER ).grab( true, false ).applyTo( pathBar );
        GridLayoutFactory.fillDefaults().numColumns( 5 ).applyTo( pathBar );
        ToolBar subToolBar = new ToolBar( pathBar, SWT.NONE );
        final SelectionAdapter copyPathSelectionListener = new SelectionAdapter() {

            @Override
            public void widgetSelected( final SelectionEvent event ) {
                final TextTransfer textTransfer = TextTransfer.getInstance();
                final StringBuilder path = new StringBuilder();
                for ( final Control control : pathButtonBar.getChildren() )
                    path.append( '/' ).append( ( ( Label ) control ).getText() );
                CLIPBOARD.setContents( new Object[] { path.toString() }, new Transfer[] { textTransfer } );
            }
        };
        newToolBarButton( subToolBar, SWT.PUSH, "copy.gif", EclipseI18n.focusTreeCopyPathToolTip, copyPathSelectionListener );
        copyPathMenuItem.addSelectionListener( copyPathSelectionListener );
        leftPathBarButton = new Label( pathBar, SWT.NONE );
        final int arrowSize = leftPathBarButton.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y * 2 / 3;
        leftPathBarButton.setImage( newArrowImage( arrowSize, true ) );
        leftPathBarButton.setToolTipText( EclipseI18n.focusTreePreviousPathButtonToolTip.text() );
        leftPathBarButton.setVisible( false );
        pathButtonBar = new Composite( pathBar, SWT.NONE );
        GridDataFactory.swtDefaults().align( SWT.FILL, SWT.CENTER ).grab( true, false ).applyTo( pathButtonBar );
        RowLayoutFactory.fillDefaults().fill( true ).wrap( false ).applyTo( pathButtonBar );
        rightPathBarButton = new Label( pathBar, SWT.NONE );
        rightPathBarButton.setImage( newArrowImage( arrowSize, false ) );
        rightPathBarButton.setVisible( false );
        rightPathBarButton.setToolTipText( EclipseI18n.focusTreeNextPathButtonToolTip.text() );
        leftPathBarButton.addMouseListener( new MouseAdapter() {

            @Override
            public void mouseUp( final MouseEvent event ) {
                final Control[] pathButtons = pathButtonBar.getChildren();
                // Show last hidden path button on left
                for ( int ndx = 0; ndx < pathButtons.length; ++ndx ) {
                    Control pathButton = pathButtons[ ndx ];
                    if ( pathButton.isVisible() ) {
                        // Show previous path button
                        pathButton = pathButtons[ --ndx ];
                        pathButton.setVisible( true );
                        ( ( RowData ) pathButton.getLayoutData() ).exclude = false;
                        break;
                    }
                }
                // Hide last shown path button on right until all buttons fit in button bar
                final int width = pathButtonBar.getBounds().width;
                for ( int ndx = pathButtons.length; --ndx >= 0 && pathButtonBar.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x > width; ) {
                    final Control pathButton = pathButtons[ ndx ];
                    if ( pathButton.isVisible() ) {
                        pathButton.setVisible( false );
                        ( ( RowData ) pathButton.getLayoutData() ).exclude = true;
                    }
                }
                pathButtonBar.layout();
                leftPathBarButton.setVisible( !pathButtons[ 0 ].isVisible() );
                rightPathBarButton.setVisible( true );
                return;
            }
        } );
        rightPathBarButton.addMouseListener( new MouseAdapter() {

            @Override
            public void mouseUp( final MouseEvent event ) {
                final Control[] pathButtons = pathButtonBar.getChildren();
                // Show first hidden path button on right
                for ( int ndx = pathButtons.length; --ndx >= 0; ) {
                    Control pathButton = pathButtons[ ndx ];
                    if ( pathButton.isVisible() ) {
                        // Show next path button
                        pathButton = pathButtons[ ++ndx ];
                        pathButton.setVisible( true );
                        ( ( RowData ) pathButton.getLayoutData() ).exclude = false;
                        break;
                    }
                }
                // Hide first shown path button on left until all buttons fit in button bar
                hideExcessiveLeftMostPathButtons();
            }
        } );
        subToolBar = new ToolBar( pathBar, SWT.NONE );
        if ( closable ) {
            final SelectionAdapter closeSelectionListener = new SelectionAdapter() {

                @Override
                public void widgetSelected( final SelectionEvent event ) {
                    final Composite parent = getParent();
                    dispose();
                    parent.layout();
                }
            };
            newToolBarButton( subToolBar, SWT.PUSH, "close.gif", EclipseI18n.focusTreeCloseTreeToolTip, closeSelectionListener );
            new MenuItem( popup, SWT.SEPARATOR );
            final MenuItem closeMenuItem = new MenuItem( popup, SWT.PUSH );
            closeMenuItem.setText( EclipseI18n.focusTreeCloseTreeMenuItem.text() );
            closeMenuItem.addSelectionListener( closeSelectionListener );
        }
        pathButtonBar.addListener( SWT.Resize, new Listener() {

            @Override
            public void handleEvent( final Event event ) {
                // Show all path buttons
                final Control[] pathButtons = pathButtonBar.getChildren();
                for ( final Control pathButton : pathButtons ) {
                    pathButton.setVisible( true );
                    ( ( RowData ) pathButton.getLayoutData() ).exclude = false;
                }
                // Hide first shown path button on left until all buttons fit in button bar
                hideExcessiveLeftMostPathButtons();
            }
        } );

        // Construct horizontally-scrolling diagram area
        scroller = new ScrolledComposite( this, SWT.H_SCROLL | SWT.V_SCROLL );
        GridDataFactory.fillDefaults().grab( true, true ).applyTo( scroller );
        scroller.setExpandVertical( true );
        scroller.setExpandHorizontal( true );
        scroller.setBackgroundMode( SWT.INHERIT_FORCE );

        // Construct canvas with header
        headeredCanvas = new Composite( scroller, SWT.NONE );
        GridLayoutFactory.fillDefaults().spacing( 0, 0 ).applyTo( headeredCanvas );
        scroller.setContent( headeredCanvas );
        scroller.setBackground( Display.getCurrent().getSystemColor( SWT.COLOR_WHITE ) );

        // Construct header bar
        headerBar = new Composite( headeredCanvas, SWT.NONE );
        GridDataFactory.swtDefaults().align( SWT.FILL, SWT.CENTER ).grab( true, false ).applyTo( headerBar );
        GridLayoutFactory.fillDefaults().spacing( 0, 0 ).numColumns( 0 ).applyTo( headerBar );

        // Construct inner canvas
        focusTreeCanvas = new FocusTreeCanvas( this, headeredCanvas, SWT.BORDER | SWT.DOUBLE_BUFFERED );
        GridDataFactory.fillDefaults().grab( true, true ).applyTo( focusTreeCanvas );
        focusTreeCanvas.addMouseTrackListener( new MouseTrackAdapter() {

            @Override
            public void mouseExit( final MouseEvent event ) {
                focusTreeCanvas.toolBar.setVisible( false );
            }

            @Override
            public void mouseHover( final MouseEvent event ) {
                final Point origin = scroller.getOrigin();
                if ( !focusTreeCanvas.iconViewShown() && event.x - origin.x < focusTreeCanvas.toolBar.getSize().width ) {
                    focusTreeCanvas.moveToolBar( origin.x );
                    focusTreeCanvas.toolBar.setVisible( true );
                }
                else focusTreeCanvas.toolBar.setVisible( false );
            }
        } );

        // Wire related pop-up actions
        homeMenuItem.addSelectionListener( new SelectionAdapter() {

            @Override
            public void widgetSelected( final SelectionEvent event ) {
                focusTreeCanvas.homeSelected();
            }
        } );
        collapseAllMenuItem.addSelectionListener( new SelectionAdapter() {

            @Override
            public void widgetSelected( final SelectionEvent event ) {
                focusTreeCanvas.collapseAllSelected();
            }
        } );
        duplicateMenuItem.addSelectionListener( new SelectionAdapter() {

            @Override
            public void widgetSelected( final SelectionEvent event ) {
                duplicate( root );
            }
        } );
    }

    void addColumn( final Object item ) {

        // Save previous column for use when resizing column
        final Column previousColumn = columns.isEmpty() ? null : columns.get( columns.size() - 1 );

        // Add a new column
        final Column column = new Column();
        columns.add( column );
        column.item = item;

        // Create listener for path bar button and header
        final MouseAdapter headerControlMouseListener = new MouseAdapter() {

            @Override
            public void mouseUp( final MouseEvent event ) {
                focusColumn( column );
            }
        };

        final Color pathButtonBackgroundColor = model.pathButtonBackgroundColor( column.item );
        final Color pathButtonForegroundColor = model.pathButtonForegroundColor( column.item );

        // Add button for column to path button bar
        createPathBarButton( column, pathButtonForegroundColor, pathButtonBackgroundColor, headerControlMouseListener );

        // Construct header
        createHeader( column, previousColumn, pathButtonForegroundColor, pathButtonBackgroundColor, headerControlMouseListener );

        // Add column to inner canvas
        focusTreeCanvas.addColumn( column );

        // Update width of header to match child column width
        GridDataFactory.swtDefaults().hint( column.cellColumn.getSize().width, SWT.DEFAULT ).applyTo( column.header );
        headerBar.layout();
    }

    boolean columnShown( final Column column ) {
        return ( ( GridLayout ) column.header.getLayout() ).numColumns > 1;
    }

    private void createHeader( final Column column,
                               final Column previousColumn,
                               final Color pathButtonForegroundColor,
                               final Color pathButtonBackgroundColor,
                               final MouseListener headerControlMouseListener ) {
        ( ( GridLayout ) headerBar.getLayout() ).numColumns++;
        column.header = new Composite( headerBar, SWT.NONE );
        GridLayoutFactory.fillDefaults().numColumns( 3 ).margins( HEADER_MARGIN, 1 ).applyTo( column.header );
        column.header.setBackground( Display.getCurrent().getSystemColor( SWT.COLOR_GRAY ) );
        column.header.addPaintListener( headerPaintListener );
        final ColumnHeaderMouseListener columnHeaderMouseListener = new ColumnHeaderMouseListener( column, previousColumn );
        column.header.addMouseListener( columnHeaderMouseListener );
        column.header.addMouseMoveListener( columnHeaderMouseListener );
        column.header.addMouseTrackListener( columnHeaderMouseListener );
        column.header.addDragDetectListener( columnHeaderMouseListener );
        column.header.setToolTipText( EclipseI18n.focusTreeHeaderToolTip.text() );
        final Label childCount = new Label( column.header, SWT.NONE );
        GridDataFactory.swtDefaults().applyTo( childCount );
        childCount.setText( String.valueOf( model.childCount( column.item ) ) );
        childCount.setToolTipText( EclipseI18n.focusTreeChildCountToolTip.text() );
        childCount.addMouseListener( headerControlMouseListener );
        childCount.setVisible( false );
        final Label ItemName = new Label( column.header, SWT.CENTER );
        GridDataFactory.swtDefaults().align( SWT.FILL, SWT.FILL ).grab( true, true ).applyTo( ItemName );
        String name;
        try {
            name = model.name( column.item );
        } catch ( final PolyglotterException e ) {
            Util.logError( e, EclipseI18n.focusTreeUnableToGetName, column.item );
            name = EclipseI18n.focusTreeErrorText.text( e.getMessage() );
        }
        ItemName.setText( name );
        try {
            ItemName.setToolTipText( EclipseI18n.focusTreeParentNameToolTip.text( model.qualifiedName( column.item ) ) );
        } catch ( final PolyglotterException e ) {
            Util.logError( e, EclipseI18n.focusTreeUnableToGetQualifiedName, column.item );
            ItemName.setToolTipText( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) );
        }
        final FontData fontData = ItemName.getFont().getFontData()[ 0 ];
        fontData.setStyle( SWT.BOLD );
        ItemName.setFont( new Font( Display.getCurrent(), fontData ) );
        ItemName.addMouseListener( headerControlMouseListener );
        final Composite hideButtonPanel = new Composite( column.header, SWT.NONE );
        GridDataFactory.swtDefaults().applyTo( hideButtonPanel );
        GridLayoutFactory.fillDefaults().applyTo( hideButtonPanel );
        hideButtonPanel.setToolTipText( EclipseI18n.focusTreeHeaderToolTip.text() );
        final Label hideButton = new Label( hideButtonPanel, SWT.NONE );
        GridDataFactory.swtDefaults().align( SWT.RIGHT, SWT.CENTER ).grab( true, false ).applyTo( hideButton );
        hideButton.setImage( Util.image( "minimize.gif" ) );
        hideButton.setAlignment( SWT.RIGHT );
        hideButton.setToolTipText( EclipseI18n.focusTreeHideColumnToolTip.text() );
        hideButton.setVisible( false );
        column.header.setData( HIDE_BUTTON_PROPERTY, hideButton );
        final Label showButton = new Label( column.header, SWT.NONE );
        GridDataFactory.swtDefaults().exclude( true ).applyTo( showButton );
        showButton.setImage( Util.image( "maximize.gif" ) );
        showButton.setToolTipText( EclipseI18n.focusTreeShowColumnToolTip.text( name ) );
        showButton.setVisible( false );

        // Wire extra header info to show on mouse over
        final MouseTrackListener headerMouseTrackListener = new MouseTrackAdapter() {

            @Override
            public void mouseEnter( final MouseEvent event ) {
                if ( columnShown( column ) ) {
                    childCount.setVisible( true );
                    if ( !focusTreeCanvas.iconViewShown() ) hideButton.setVisible( true );
                }
            }

            @Override
            public void mouseExit( final MouseEvent event ) {
                childCount.setVisible( false );
                hideButton.setVisible( false );
            }
        };
        column.header.addMouseTrackListener( headerMouseTrackListener );
        ItemName.addMouseTrackListener( headerMouseTrackListener );
        childCount.addMouseTrackListener( headerMouseTrackListener );
        hideButtonPanel.addMouseTrackListener( headerMouseTrackListener );
        hideButton.addMouseTrackListener( headerMouseTrackListener );

        // Wire drill-into ability
        final MouseListener headerMouseListener = new MouseAdapter() {

            @Override
            public void mouseDoubleClick( final MouseEvent event ) {
                if ( focusTreeCanvas.iconViewShown() ) {
                    focusTreeCanvas.hideIconView( column );
                    hideIconView( column );
                } else {
                    focusTreeCanvas.showIconView( column );
                    showIconView( column );
                }
            }
        };
        column.header.addMouseListener( headerMouseListener );
        ItemName.addMouseListener( headerMouseListener );
        childCount.addMouseListener( headerMouseListener );
        hideButtonPanel.addMouseListener( headerMouseListener );

        // Wire show and hide buttons
        final Color pathButtonHiddenBackgroundColor = model.pathButtonHiddenBackgroundColor( column.item );
        final Color pathButtonHiddenForegroundColor = model.pathButtonHiddenForegroundColor( column.item );
        hideButton.addMouseListener( new MouseAdapter() {

            @Override
            public void mouseUp( final MouseEvent event ) {
                // Hide column header
                ( ( GridData ) childCount.getLayoutData() ).exclude = true;
                childCount.setVisible( false );
                ( ( GridData ) ItemName.getLayoutData() ).exclude = true;
                ItemName.setVisible( false );
                ( ( GridData ) hideButtonPanel.getLayoutData() ).exclude = true;
                hideButtonPanel.setVisible( false );
                ( ( GridData ) showButton.getLayoutData() ).exclude = false;
                showButton.setVisible( true );
                // Save current width for later re-show
                final GridData gridData = ( ( GridData ) column.header.getLayoutData() );
                column.widthBeforeHiding = gridData.widthHint;
                // Change layout to use new preferred width
                ( ( GridLayout ) column.header.getLayout() ).numColumns = 1;
                final int width = column.header.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
                gridData.widthHint = width;
                headerBar.layout();
                // Hide column's canvas figures
                focusTreeCanvas.hideColumn( column, width );
                column.pathButton.setBackground( pathButtonHiddenBackgroundColor );
                column.pathButton.setForeground( pathButtonHiddenForegroundColor );
            }
        } );
        showButton.addMouseListener( new MouseAdapter() {

            @Override
            public void mouseUp( final MouseEvent event ) {
                // Show column header
                ( ( GridData ) childCount.getLayoutData() ).exclude = false;
                childCount.setVisible( true );
                ( ( GridData ) ItemName.getLayoutData() ).exclude = false;
                ItemName.setVisible( true );
                ( ( GridData ) hideButtonPanel.getLayoutData() ).exclude = false;
                hideButtonPanel.setVisible( true );
                ( ( GridData ) showButton.getLayoutData() ).exclude = true;
                showButton.setVisible( false );
                // Change layout to use new preferred width
                ( ( GridLayout ) column.header.getLayout() ).numColumns = 3;
                ( ( GridData ) column.header.getLayoutData() ).widthHint = column.widthBeforeHiding;
                headerBar.layout();
                // Show column's canvas figures
                focusTreeCanvas.showColumn( column, column.widthBeforeHiding );
                column.pathButton.setBackground( pathButtonBackgroundColor );
                column.pathButton.setForeground( pathButtonForegroundColor );
            }
        } );

        // Make child count and hide buttons the same size so arrow is centered
        final int width = Math.max( childCount.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x,
                                    hideButtonPanel.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x );
        GridDataFactory.swtDefaults().hint( width, SWT.DEFAULT ).applyTo( childCount );
        GridDataFactory.swtDefaults().hint( width, SWT.DEFAULT ).applyTo( hideButtonPanel );
    }

    private void createPathBarButton( final Column column,
                                      final Color pathButtonForegroundColor,
                                      final Color pathButtonBackgroundColor,
                                      final MouseListener listener ) {
        column.pathButton = new Label( pathButtonBar, SWT.NONE );
        column.pathButton.setBackground( pathButtonBackgroundColor );
        column.pathButton.setForeground( pathButtonForegroundColor );
        try {
            column.pathButton.setText( model.name( column.item ) );
        } catch ( final PolyglotterException e ) {
            Util.logError( e, EclipseI18n.focusTreeUnableToGetName, column.item );
            column.pathButton.setText( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) );
        }
        final Point size = column.pathButton.computeSize( SWT.DEFAULT, SWT.DEFAULT );
        column.pathButton.setAlignment( SWT.CENTER );
        column.pathButton.setLayoutData( new RowData( size.x + 10, size.y ) );
        column.pathButton.addPaintListener( pathButtonPaintListener );
        column.pathButton.addMouseListener( listener );
        try {
            column.pathButton.setToolTipText( EclipseI18n.focusTreePathButtonToolTip.text( model.qualifiedName( column.item ) ) );
        } catch ( final PolyglotterException e ) {
            Util.logError( e, EclipseI18n.focusTreeUnableToGetName, column.item );
            column.pathButton.setToolTipText( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) );
        }
        hideExcessiveLeftMostPathButtons();
    }

    void duplicate( final Object root ) {
        final FocusTree tree = new FocusTree( getParent(), true );
        tree.setModel( model );
        tree.setRoot( root );
        tree.moveBelow( this );
        getParent().layout();
    }

    /**
     * @return the border color of focus cells. Default is {link {@link #DEFAULT_FOCUS_CELL_BORDER_COLOR} .
     */
    public Color focusCellBorderColor() {
        return focusTreeCanvas.focusBorder.getColor();
    }

    void focusColumn( final Column column ) {
        if ( columnShown( column ) && column != focusColumn ) {
            if ( focusColumn != null ) {
                focusColumn.backgroundColumn.setOpaque( false );
                focusColumn.backgroundColumn.setToolTip( null );
                focusColumn.cellColumn.setToolTip( null );
            }
            column.backgroundColumn.setOpaque( true );
            focusColumn = column;
            focusColumn.backgroundColumn.setToolTip( focusTreeCanvas.focusColumnToolTip );
            focusColumn.cellColumn.setToolTip( focusTreeCanvas.focusColumnToolTip );
        }
        scrollToFocusColumn();
        focusTreeCanvas.scrollToFocusLine();
    }

    /**
     * @return the color of the focus column. Default is {@link #DEFAULT_FOCUS_COLUMN_COLOR}.
     */
    public Color focusColumnColor() {
        return focusTreeCanvas.focusColumnColor;
    }

    /**
     * @return the color of the focus line. Default is {@link #DEFAULT_FOCUS_LINE_COLOR}.
     */
    public Color focusLineColor() {
        return focusTreeCanvas.focusLine.getBackgroundColor();
    }

    /**
     * @return the height of the focus line. Default is {@value #DEFAULT_FOCUS_LINE_HEIGHT}.
     */
    public int focusLineHeight() {
        return focusTreeCanvas.focusLine.getSize().height;
    }

    /**
     * @return the initial offset of the focus line. Default is {@value #DEFAULT_FOCUS_LINE_OFFSET}.
     */
    public int focusLineOffset() {
        return focusTreeCanvas.focusLineOffset;
    }

    void hideExcessiveLeftMostPathButtons() {
        final Control[] pathButtons = pathButtonBar.getChildren();
        final int width = pathButtonBar.getBounds().width;
        for ( int ndx = 0; ndx < pathButtons.length &&
                           pathButtonBar.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x > width; ++ndx ) {
            leftPathBarButton.setVisible( true );
            final Control pathButton = pathButtons[ ndx ];
            if ( pathButton.isVisible() ) {
                pathButton.setVisible( false );
                ( ( RowData ) pathButton.getLayoutData() ).exclude = true;
            }
        }
        leftPathBarButton.setVisible( ( ( RowData ) ( ( Label ) pathButtons[ 0 ] ).getLayoutData() ).exclude );
        rightPathBarButton.setVisible( ( ( RowData ) ( ( Label ) pathButtons[ pathButtons.length - 1 ] ).getLayoutData() ).exclude );
        pathButtonBar.layout();
        pathButtonBar.getParent().layout();
    }

    void hideIconView( final Column column ) {
        for ( final Control control : headerBar.getChildren() ) {
            ( ( GridData ) control.getLayoutData() ).exclude = false;
            control.setVisible( true );
        }
        final GridData gridData = ( GridData ) column.header.getLayoutData();
        gridData.grabExcessHorizontalSpace = false;
        gridData.horizontalAlignment = SWT.BEGINNING;
        headerBar.layout();
    }

    /**
     * @return the initial width of a cell. Default is {@value SWT#DEFAULT}, indicating to use the largest preferred width of all
     *         cells in a column.
     */
    public int initialCellWidth() {
        return focusTreeCanvas.initialCellWidth;
    }

    /**
     * @return <code>true</code> if the initial index shown in columns with multiple cells is one. Default is <code>false</code>.
     */
    public boolean initialIndexIsOne() {
        return focusTreeCanvas.initialIndexIsOne;
    }

    private void initialize() {
        // Dispose of all controls dependent upon old model
        for ( final Control control : pathButtonBar.getChildren() )
            control.dispose();
        for ( final Control control : headerBar.getChildren() )
            control.dispose();

        focusColumn = null;

        focusTreeCanvas.modelChanged();

        // Add new first column for root
        if ( root != null && model != null ) addColumn( root );
    }

    private Image newArrowImage( final int size,
                                 final boolean leftArrow ) {
        final Image image = new Image( Display.getDefault(), size / 2 + 1, size );
        final GC gc = new GC( image );
        gc.setAntialias( SWT.ON );
        gc.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_BLACK ) );
        if ( leftArrow ) for ( int x = 0, y = size / 2; y >= 0; x++, y-- )
            gc.drawLine( x, y, x, y + x * 2 );
        else for ( int y = 0, x = 0; y <= size / 2; x++, y++ )
            gc.drawLine( x, y, x, y + size - 1 - x * 2 );
        gc.dispose();
        final ImageData data = image.getImageData();
        image.dispose();
        data.transparentPixel = data.palette.getPixel( new RGB( 255, 255, 255 ) );
        return new Image( Display.getCurrent(), data );
    }

    private ToolItem newToolBarButton( final ToolBar toolBar,
                                       final int style,
                                       final String iconName,
                                       final I18n toolTip,
                                       final SelectionListener selectionListener ) {
        final ToolItem item = new ToolItem( toolBar, style );
        item.setImage( Util.image( iconName ) );
        item.setToolTipText( toolTip.text() );
        if ( selectionListener == null ) item.addSelectionListener( new SelectionAdapter() {

            @Override
            public void widgetSelected( final SelectionEvent event ) {
                final Shell dialog = new Shell( getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
                dialog.setLayout( new RowLayout() );
                final Label label = new Label( dialog, SWT.NONE );
                label.setText( "Not yet implemented" );
                dialog.pack();
                final Rectangle itemBounds = getShell().getDisplay().map( toolBar, null, item.getBounds() );
                dialog.setLocation( itemBounds.x, itemBounds.y );
                dialog.open();
            }
        } );
        else item.addSelectionListener( selectionListener );
        return item;
    }

    void removeColumnsAfter( final Column column ) {
        int ndx = columns.size() - 1;
        for ( Column col = columns.get( ndx ); col != column; col = columns.get( --ndx ) ) {
            col.header.dispose();
            ( ( GridLayout ) headerBar.getLayout() ).numColumns--;
            col.pathButton.dispose();
            focusTreeCanvas.removeColumn( col );
            columns.remove( ndx );
        }
        column.focusCellExpanded = false;
        headerBar.layout();
        pathButtonBar.layout();
    }

    void scrollToFocusColumn() {
        if ( focusColumn != null ) scroller.setOrigin( focusColumn.cellColumn.getBounds().x, scroller.getOrigin().y );
    }

    /**
     * @param color
     *        the border color of focus cells, or the {link {@link #DEFAULT_FOCUS_CELL_BORDER_COLOR default color} if
     *        <code>null</code>
     */
    public void setFocusCellBorderColor( final Color color ) {
        focusTreeCanvas.focusBorder.setColor( color == null ? DEFAULT_FOCUS_CELL_BORDER_COLOR : color );
    }

    /**
     * @param color
     *        the color of the focus column, or the {@link #DEFAULT_FOCUS_COLUMN_COLOR default color} if <code>null</code>.
     */
    public void setFocusColumnColor( final Color color ) {
        focusTreeCanvas.focusColumnColor = color == null ? DEFAULT_FOCUS_COLUMN_COLOR : color;
    }

    /**
     * @param color
     *        the color of the focus line, or the {@link #DEFAULT_FOCUS_LINE_COLOR default color} if <code>null</code>.
     */
    public void setFocusLineColor( final Color color ) {
        focusTreeCanvas.focusLine.setBackgroundColor( color == null ? DEFAULT_FOCUS_LINE_COLOR : color );
    }

    /**
     * @param height
     *        the height of the focus line, or the {@value #DEFAULT_FOCUS_LINE_HEIGHT default height} if less than or equal to zero.
     */
    public void setFocusLineHeight( final int height ) {
        focusTreeCanvas.setFocusLineHeight( height <= 0 ? DEFAULT_FOCUS_LINE_HEIGHT : height );
    }

    /**
     * @param offset
     *        the initial offset of the focus line, or the {@value #DEFAULT_FOCUS_LINE_OFFSET default offset} if less than zero.
     */
    public void setFocusLineOffset( final int offset ) {
        focusTreeCanvas.focusLineOffset = offset < 0 ? DEFAULT_FOCUS_LINE_OFFSET : offset;
    }

    /**
     * @param width
     *        the initial width of a cell, or the {@value SWT#DEFAULT default width} if less than or equal to zero.
     */
    public void setInitialCellWidth( final int width ) {
        focusTreeCanvas.initialCellWidth = width <= 0 ? SWT.DEFAULT : width;
    }

    /**
     * @param initialIndexIsOne
     *        <code>true</code> if the initial index shown in columns with multiple cells is one.
     */
    public void setInitialIndexIsOne( final boolean initialIndexIsOne ) {
        focusTreeCanvas.initialIndexIsOne = initialIndexIsOne;
    }

    /**
     * @param model
     *        a focus tree model
     */
    public void setModel( final Model model ) {
        this.model = model;
        initialize();
    }

    /**
     * @param root
     *        the root item for this tree
     */
    public void setRoot( final Object root ) {
        this.root = root;
        initialize();
    }

    /**
     * @param color
     *        the background color of the {@link FocusTree focus tree}, or the {link {@link #DEFAULT_TREE_BACKGROUND_COLOR default
     *        color} if <code>null</code>.
     */
    public void setTreeBackgroundColor( final Color color ) {
        focusTreeCanvas.setBackground( color == null ? DEFAULT_TREE_BACKGROUND_COLOR : color );
    }

    void showIconView( final Column column ) {
        for ( final Control control : headerBar.getChildren() )
            if ( control != column.header ) {
                ( ( GridData ) control.getLayoutData() ).exclude = true;
                control.setVisible( false );
            }
        final GridData gridData = ( GridData ) column.header.getLayoutData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = SWT.FILL;
        ( ( Control ) column.header.getData( HIDE_BUTTON_PROPERTY ) ).setVisible( false );
        headerBar.layout();
        // TODO path buttons hide icon view, click hides icon view, context menu
    }

    /**
     * @return the background color of the {@link FocusTree focus tree}. Default is {link {@link #DEFAULT_TREE_BACKGROUND_COLOR} .
     */
    public Color treeBackgroundColor() {
        return focusTreeCanvas.getBackground();
    }

    /**
     * @param item
     *        an item in the tree
     * @return the type of the supplied item's cell. Default is the item's simple class name.
     */
    public String type( final Object item ) {
        return item.getClass().getSimpleName();
    }

    void unfocusColumn( final Column column ) {
        if ( column == focusColumn ) {
            column.backgroundColumn.setOpaque( false );
            focusColumn = null;
        }
    }

    /**
     * @param item
     *        an item in the tree
     * @return the value of the supplied item's cell. Default is <code>null</code>.
     */
    public String value( final Object item ) {
        return null;
    }

    /**
     * 
     */
    public static class Cell extends Figure {

        IFigure delegate;
        Object item;
        AddPanel addPanel;
        org.eclipse.draw2d.Label indexLabel;
        DeleteButton deleteButton;

        /**
         * @param delegate
         *        the delegate that determines this cell's shape
         */
        public Cell( final IFigure delegate ) {
            CheckArg.notNull( delegate, "delegate" );
            this.delegate = delegate;
            final org.eclipse.draw2d.GridLayout layout = new org.eclipse.draw2d.GridLayout();
            layout.marginHeight = layout.marginWidth = 0;
            super.setLayoutManager( layout );
            super.add( delegate, null, 0 );
            super.setConstraint( delegate, new org.eclipse.draw2d.GridData( SWT.FILL, SWT.FILL, true, true ) );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.draw2d.Figure#add(org.eclipse.draw2d.IFigure, java.lang.Object, int)
         */
        @Override
        public void add( final IFigure figure,
                         final Object constraint,
                         final int index ) {
            delegate.add( figure, constraint, index );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.draw2d.Figure#setBackgroundColor(org.eclipse.swt.graphics.Color)
         */
        @Override
        public void setBackgroundColor( final Color bg ) {
            delegate.setBackgroundColor( bg );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.draw2d.Figure#setBorder(org.eclipse.draw2d.Border)
         */
        @Override
        public void setBorder( final Border border ) {
            delegate.setBorder( border );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.draw2d.Figure#setConstraint(org.eclipse.draw2d.IFigure, java.lang.Object)
         */
        @Override
        public void setConstraint( final IFigure child,
                                   final Object constraint ) {
            delegate.setConstraint( child, constraint );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.draw2d.Figure#setLayoutManager(org.eclipse.draw2d.LayoutManager)
         */
        @Override
        public void setLayoutManager( final LayoutManager manager ) {
            delegate.setLayoutManager( manager );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.draw2d.Figure#setToolTip(org.eclipse.draw2d.IFigure)
         */
        @Override
        public void setToolTip( final IFigure f ) {
            delegate.setToolTip( f );
        }
    }

    class Column {

        Object item;
        Composite header;
        Label pathButton;
        IFigure backgroundColumn;
        CellColumn cellColumn;
        Cell focusCell;
        boolean focusCellExpanded;
        int preferredWidth;
        int widthBeforeHiding;
    }

    private class ColumnHeaderMouseListener extends MouseAdapter
        implements
        MouseMoveListener,
        MouseTrackListener,
        DragDetectListener {

        private final Column column, previousColumn;
        private Column targetColumn;
        private boolean dragging;
        private int offset;

        ColumnHeaderMouseListener( final Column column,
                                   final Column previousColumn ) {
            this.column = column;
            this.previousColumn = previousColumn;
        }

        @Override
        public void dragDetected( final DragDetectEvent event ) {
            if ( targetColumn == null ) return;
            if ( targetColumn == column ) offset = column.header.getSize().x - event.x;
            else offset = -event.x;
            dragging = true;
        }

        @Override
        public void mouseDoubleClick( final MouseEvent event ) {
            if ( targetColumn != null ) {
                focusTreeCanvas.updateColumnWidth( targetColumn, targetColumn.preferredWidth, true );
                ( ( GridData ) targetColumn.header.getLayoutData() ).widthHint = targetColumn.cellColumn.getSize().width;
                headerBar.layout();
            }
        }

        @Override
        public void mouseEnter( final MouseEvent event ) {
            if ( focusTreeCanvas.iconViewShown() ) return;
            if ( event.x >= column.header.getSize().x - 1 - HEADER_MARGIN ) setTargetColumn( column );
            else if ( event.x <= HEADER_MARGIN && previousColumn != null )
                setTargetColumn( previousColumn );
        }

        @Override
        public void mouseExit( final MouseEvent event ) {
            if ( !dragging ) {
                column.header.setCursor( Display.getCurrent().getSystemCursor( SWT.CURSOR_ARROW ) );
                column.header.setToolTipText( null );
                targetColumn = null;
            }
        }

        @Override
        public void mouseHover( final MouseEvent event ) {}

        @Override
        public void mouseMove( final MouseEvent event ) {
            if ( dragging ) {
                if ( targetColumn == previousColumn ) {
                    final Point point = column.header.toDisplay( event.x, event.y );
                    event.x = previousColumn.header.toControl( point ).x;
                }
                final int width = event.x + offset;
                ( ( GridData ) targetColumn.header.getLayoutData() ).widthHint = width;
                headerBar.layout();
                focusTreeCanvas.updateColumnWidth( targetColumn, width, true );
            }
        }

        @Override
        public void mouseUp( final MouseEvent event ) {
            if ( dragging ) {
                dragging = false;
                if ( Display.getCurrent().getCursorControl() != column.header ) targetColumn = null;
            } else focusColumn( column );
        }

        private void setTargetColumn( final Column targetColumn ) {
            this.targetColumn = targetColumn;
            column.header.setCursor( Display.getCurrent().getSystemCursor( SWT.CURSOR_SIZEWE ) );
            column.header.setToolTipText( EclipseI18n.focusTreeResizeColumnToolTip.text() );
        }
    }

    /**
     * 
     */
    public static class Indicator {

        final Image image;
        final String toolTip;

        /**
         * @param image
         *        the image for this indicator's button
         * @param toolTip
         *        the tool tip for this indicator's button
         */
        public Indicator( final Image image,
                          final String toolTip ) {
            CheckArg.notNull( image, "image" );
            CheckArg.notEmpty( toolTip, "toolTip" );
            this.image = image;
            this.toolTip = toolTip;
        }

        /**
         * Does nothing by default.
         * 
         * @param item
         *        the item containing this indicator
         */
        protected void selected( final Object item ) {}
    }

    /**
     * 
     */
    public static class Model {

        /**
         *
         */
        public static final Color DEFAULT_CELL_BACKGROUND_COLOR = Display.getCurrent().getSystemColor( SWT.COLOR_WHITE );

        /**
         * 
         */
        public static final Color DEFAULT_CHILD_INDEX_COLOR = FocusTree.DEFAULT_FOCUS_CELL_BORDER_COLOR;

        /**
         *
         */
        public static final Color DEFAULT_PATH_BUTTON_MINIMIZED_BACKGROUND_COLOR =
            Display.getCurrent().getSystemColor( SWT.COLOR_GRAY );

        /**
         * 
         */
        public static final Object[] NO_CHILDREN = new Object[ 0 ];

        /**
         * 
         */
        public static final Indicator[] NO_INDICATORS = new Indicator[ 0 ];

        /**
         * @param item
         *        an item in the tree
         * @return the background color of the supplied item's cell. Default is {link #DEFAULT_CELL_BACKGROUND_COLOR}.
         */
        public Color cellBackgroundColor( final Object item ) {
            return DEFAULT_CELL_BACKGROUND_COLOR;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the foreground color of the supplied item's cell. Default is white or black, whichever contrasts more with the
         *         {@link #cellBackgroundColor(Object) cell's background color}.
         */
        public Color cellForegroundColor( final Object item ) {
            final Color color = cellBackgroundColor( item );
            final double yiq = ( ( color.getRed() * 299 ) + ( color.getGreen() * 587 ) + ( color.getBlue() * 114 ) ) / 1000.0;
            return yiq >= 128.0 ? Display.getCurrent().getSystemColor( SWT.COLOR_BLACK )
                               : Display.getCurrent().getSystemColor( SWT.COLOR_WHITE );
        }

        /**
         * @param item
         *        an item in the tree
         * @return the number of children of the supplied item. Default is 0.
         */
        public int childCount( final Object item ) {
            return 0;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the color of the child index shown in the supplied item's cell. Default is {@link #DEFAULT_CHILD_INDEX_COLOR}.
         */
        public Color childIndexColor( final Object item ) {
            return DEFAULT_CHILD_INDEX_COLOR;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the children of the supplied item. Default is an empty array.
         * @throws PolyglotterException
         *         if an error occurs
         */
        public Object[] children( final Object item ) throws PolyglotterException {
            return NO_CHILDREN;
        }

        /**
         * @param item
         *        an item in the tree
         * @return <code>true</code> if the supplied item can be added. Default is <code>true</code> for collections and arrays.
         */
        public boolean childrenAddable( final Object item ) {
            return item.getClass().isArray() || item instanceof Collection< ? >;
        }

        /**
         * @param item
         *        an item in the tree
         * @return Creates a cell for the supplied item. Default is a cell that delegates to create a {@link RoundedRectangle}.
         */
        public Cell createCell( final Object item ) {
            return new Cell( new RoundedRectangle() );
        }

        /**
         * @param parent
         *        a parent item in the tree
         * @param index
         *        the index within the supplied parent where a new item is to be added
         * @return The newly created item. Never <code>null</code>
         * @throws PolyglotterException
         *         if an error occurs
         */
        public Object createChildAt( final Object parent,
                                     final int index ) throws PolyglotterException {
            return true;
        }

        /**
         * @param item
         *        an item in the tree
         * @return <code>true</code> if the supplied item can be deleted. Default is <code>false</code>
         */
        public boolean deletable( final Object item ) {
            return false;
        }

        /**
         * @param item
         *        an item in the tree
         * @return <code>true</code> if the supplied item was successfully deleted. Default is <code>true</code>
         */
        public boolean delete( final Object item ) {
            return true;
        }

        /**
         * @param item
         *        an item in the tree
         * @return <code>true</code> if the supplied item has children. Default is <code>false</code>,
         * @throws PolyglotterException
         *         if an error occurs
         */
        public boolean hasChildren( final Object item ) throws PolyglotterException {
            return false;
        }

        /**
         * @param item
         *        an item in the tree
         * @return <code>true</code> if the supplied item has a value. Default is <code>false</code>
         */
        public boolean hasValue( final Object item ) {
            return false;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the icon of the cell for the supplied item. Default is <code>null</code>.
         */
        public Image icon( final Object item ) {
            return null;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the status indicators applicable to the supplied item. Default is an empty array. Must not be <code>null</code>.
         */
        public Indicator[] indicators( final Object item ) {
            return NO_INDICATORS;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the name of the supplied item's cell. Default is the item's {@link Object#toString()}.
         * @throws PolyglotterException
         *         if an error occurs
         */
        public String name( final Object item ) throws PolyglotterException {
            return item.toString();
        }

        /**
         * @param item
         *        an item in the tree
         * @return <code>true</code> if the supplied item's name can be edited. Default is <code>false</code>
         */
        public boolean nameEditable( final Object item ) {
            return false;
        }

        /**
         * @param item
         *        an item in the tree
         * @param name
         *        a name for the supplied item
         * @return the problem message for the supplied name for the supplied item, or <code>null</code> if the name is valid.
         *         Default is <code>null</code>.
         */
        public String nameProblem( final Object item,
                                   final String name ) {
            return null;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the background color of the supplied item's path button. Default is {@link #cellBackgroundColor(Object) cell's
         *         background color}.
         */
        public Color pathButtonBackgroundColor( final Object item ) {
            return cellBackgroundColor( item );
        }

        /**
         * @param item
         *        an item in the tree
         * @return the foreground color of the supplied item's cell. Default is {@link #cellForegroundColor(Object) cell's
         *         foreground color}.
         */
        public Color pathButtonForegroundColor( final Object item ) {
            return cellForegroundColor( item );
        }

        /**
         * @param item
         *        an item in the tree
         * @return the background color of the supplied item's path button if its column is minimized. Default is
         *         {@link #DEFAULT_PATH_BUTTON_MINIMIZED_BACKGROUND_COLOR}.
         */
        public Color pathButtonHiddenBackgroundColor( final Object item ) {
            return DEFAULT_PATH_BUTTON_MINIMIZED_BACKGROUND_COLOR;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the foreground color of the supplied item's path button if its column is minimized. Default is
         *         {@link #cellForegroundColor(Object) cell's foreground color}.
         */
        public Color pathButtonHiddenForegroundColor( final Object item ) {
            return cellForegroundColor( item );
        }

        /**
         * @param item
         *        an item in the tree
         * @return the fully-qualified name of the supplied item's cell. Default is the item's {@link #name(Object) name}
         * @throws PolyglotterException
         *         if an error occurs
         */
        public String qualifiedName( final Object item ) throws PolyglotterException {
            return name( item );
        }

        /**
         * Called after a user changes the supplied item's name to the supplied name. Does nothing by default.
         * 
         * @param item
         *        an item in the tree
         * @param name
         *        a name for the supplied item
         * @return an item with the supplied name. Default is the supplied item.
         * @throws PolyglotterException
         *         if an error occurs
         */
        public Object setName( final Object item,
                               final String name ) throws PolyglotterException {
            return item;
        }

        /**
         * Called after a user changes the supplied item's type to the supplied type. Does nothing by default.
         * 
         * @param item
         *        an item in the tree
         * @param type
         *        a type for the supplied item
         * @return an item with the supplied type. Default is the supplied item.
         * @throws PolyglotterException
         *         if an error occurs
         */
        public Object setType( final Object item,
                               final String type ) throws PolyglotterException {
            return item;
        }

        /**
         * Called after a user changes the supplied item's value to the supplied value. Does nothing by default.
         * 
         * @param item
         *        an item in the tree
         * @param value
         *        a value for the supplied item
         * @return an item with the supplied value. Default is the supplied item.
         * @throws PolyglotterException
         *         if an error occurs
         */
        public Object setValue( final Object item,
                                final String value ) throws PolyglotterException {
            return item;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the type of the supplied item's cell. Default is the item's simple class name.
         * @throws PolyglotterException
         *         if an error occurs
         */
        public String type( final Object item ) throws PolyglotterException {
            return item.getClass().getSimpleName();
        }

        /**
         * @param item
         *        an item in the tree
         * @return <code>true</code> if the supplied item's type can be edited. Default is <code>false</code>
         */
        public boolean typeEditable( final Object item ) {
            return false;
        }

        /**
         * @param item
         *        an item in the tree
         * @param type
         *        a type for the supplied item
         * @return the problem message for the supplied type for the supplied item, or <code>null</code> if the type is valid.
         *         Default is <code>null</code>.
         */
        public String typeProblem( final Object item,
                                   final String type ) {
            return null;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the value of the supplied item's cell. Default is <code>null</code>.
         * @throws PolyglotterException
         *         if an error occurs
         */
        public String value( final Object item ) throws PolyglotterException {
            return null;
        }

        /**
         * @param item
         *        an item in the tree
         * @return <code>true</code> if the supplied item's value can be edited. Default is <code>false</code>
         */
        public boolean valueEditable( final Object item ) {
            return false;
        }

        /**
         * @param item
         *        an item in the tree
         * @param value
         *        a value for the supplied item
         * @return the problem message for the supplied value for the supplied item, or <code>null</code> if the value is valid.
         *         Default is <code>null</code>.
         */
        public String valueProblem( final Object item,
                                    final String value ) {
            return null;
        }
    }
}