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

import java.io.InputStream;
import java.net.URL;

import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.modeshape.modeler.ui.FocusTreeController;
import org.modeshape.modeler.ui.FocusTreeView;
import org.modeshape.modeler.ui.FocusTreeView.Column.View;

/**
 * 
 */
public class FocusTree extends Composite implements FocusTreeView {

    private static final Clipboard CLIPBOARD = new Clipboard( Display.getCurrent() );

    private static final int HEADER_MARGIN = 2;

    final FocusTreeController controller;
    Preferences preferences;
    final Menu popup;
    MenuItem collapseAllColumnsMenuItem;
    MenuItem duplicateTreeMenuItem;
    MenuItem iconViewMenuItem;
    MenuItem copyPathMenuItem;
    MenuItem closeTreeMenuItem;
    final Composite pathBar;
    ToolItem copyPathButton;
    Label scrollPathBarLeftButton;
    Composite pathButtonBar;
    Label scrollPathBarRightButton;
    ToolItem closeTreeButton;
    final ScrolledComposite scroller;
    final Composite scrollable;
    final Composite headerBar;
    final FocusTreeLightweight focusTreeLightweight;
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

    /**
     * Creates a non-closable focus tree
     * 
     * @param parent
     *        parent composite
     * @param controller
     *        the controller for this tree
     */
    public FocusTree( final Composite parent,
                      final FocusTreeController controller ) {
        this( parent, controller, false );
    }

    /**
     * @param parent
     *        parent composite
     * @param controller
     *        the controller for this tree, on which this tree is {@link FocusTreeController#setView(FocusTreeView) set as its view}
     *        at the end of construction
     * @param closable
     *        <code>true</code> if this tree can be closed by the user
     */
    public FocusTree( final Composite parent,
                      final FocusTreeController controller,
                      final boolean closable ) {
        super( parent, SWT.NONE );
        this.controller = controller;

        GridLayoutFactory.fillDefaults().spacing( 0, 0 ).applyTo( this );

        // Construct pop-up menu
        popup = new Menu( getShell(), SWT.POP_UP );
        setMenu( popup );

        // Construct path bar
        pathBar = new Composite( this, SWT.NONE );
        GridDataFactory.swtDefaults().align( SWT.FILL, SWT.CENTER ).grab( true, false ).applyTo( pathBar );
        GridLayoutFactory.fillDefaults().numColumns( 5 ).applyTo( pathBar );

        // Construct horizontally-scrolling diagram area
        scroller = new ScrolledComposite( this, SWT.H_SCROLL | SWT.V_SCROLL );
        GridDataFactory.fillDefaults().grab( true, true ).applyTo( scroller );
        scroller.setExpandVertical( true );
        scroller.setExpandHorizontal( true );
        scroller.setBackgroundMode( SWT.INHERIT_FORCE );

        // Construct scrollable for header bar and lightweight tree as scroller contents
        scrollable = new Composite( scroller, SWT.NONE );
        GridLayoutFactory.fillDefaults().spacing( 0, 0 ).applyTo( scrollable );
        scroller.setContent( scrollable );

        // Construct header bar
        headerBar = new Composite( scrollable, SWT.NONE );
        GridDataFactory.swtDefaults().align( SWT.FILL, SWT.CENTER ).grab( true, false ).applyTo( headerBar );
        GridLayoutFactory.fillDefaults().spacing( 0, 0 ).numColumns( 0 ).applyTo( headerBar );

        // Construct lightweight focus tree
        focusTreeLightweight = new FocusTreeLightweight( this, scrollable, SWT.BORDER | SWT.DOUBLE_BUFFERED );
        GridDataFactory.fillDefaults().grab( true, true ).applyTo( focusTreeLightweight );

        controller.setView( this );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#close()
     */
    @Override
    public void close() {
        final Composite parent = getParent();
        dispose();
        parent.layout();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#collapseAllColumns()
     */
    @Override
    public void collapseAllColumns() {
        // focusTreeCanvas.collapseAllSelected();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#constructCanvas(Object)
     */
    @Override
    public void constructCanvas( final Object treeBackgroundColor ) {
        focusTreeLightweight.constructCanvas( ( Color ) treeBackgroundColor );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#constructCloseTreeAction(java.lang.String, java.lang.String, java.net.URL)
     */
    @SuppressWarnings( "unused" )
    @Override
    public void constructCloseTreeAction( final String name,
                                          final String description,
                                          final URL imageUrl ) {
        final ToolBar toolBar = new ToolBar( pathBar, SWT.NONE );
        closeTreeButton = newToolBarButton( toolBar, SWT.PUSH, imageUrl, description );
        new MenuItem( popup, SWT.SEPARATOR );
        closeTreeMenuItem = new MenuItem( popup, SWT.PUSH );
        closeTreeMenuItem.setText( name );
        final SelectionAdapter closeSelectionListener = new SelectionAdapter() {

            @Override
            public void widgetSelected( final SelectionEvent event ) {
                controller.closeTree();
            }
        };
        closeTreeButton.addSelectionListener( closeSelectionListener );
        closeTreeMenuItem.addSelectionListener( closeSelectionListener );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#constructCollapseAllColumnsAction(String, String)
     */
    @Override
    public void constructCollapseAllColumnsAction( final String name,
                                                   final String description ) {
        collapseAllColumnsMenuItem = new MenuItem( popup, SWT.PUSH );
        collapseAllColumnsMenuItem.setText( name );
        collapseAllColumnsMenuItem.addSelectionListener( new SelectionAdapter() {

            @Override
            public void widgetSelected( final SelectionEvent event ) {
                controller.collapseAllColumns();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#constructCopyPathAction(String, String, URL)
     */
    @SuppressWarnings( "unused" )
    @Override
    public void constructCopyPathAction( final String name,
                                         final String description,
                                         final URL imageUrl ) {
        final ToolBar toolBar = new ToolBar( pathBar, SWT.NONE );
        copyPathButton = newToolBarButton( toolBar, SWT.PUSH, imageUrl, description );
        new MenuItem( popup, SWT.SEPARATOR );
        copyPathMenuItem = new MenuItem( popup, SWT.PUSH );
        copyPathMenuItem.setText( name );
        final SelectionAdapter copyPathSelectionListener = new SelectionAdapter() {

            @Override
            public void widgetSelected( final SelectionEvent event ) {
                controller.copyPath();
            }
        };
        copyPathButton.addSelectionListener( copyPathSelectionListener );
        copyPathMenuItem.addSelectionListener( copyPathSelectionListener );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#constructDuplicateTreeAction(java.lang.String, java.lang.String)
     */
    @Override
    public void constructDuplicateTreeAction( final String name,
                                              final String description ) {
        duplicateTreeMenuItem = new MenuItem( popup, SWT.PUSH );
        duplicateTreeMenuItem.setText( name );
        duplicateTreeMenuItem.addSelectionListener( new SelectionAdapter() {

            @Override
            public void widgetSelected( final SelectionEvent event ) {
                controller.duplicateTree();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#constructFocusLine(java.lang.String, java.lang.Object, int, int)
     */
    @Override
    public void constructFocusLine( final String description,
                                    final Object color,
                                    final int y,
                                    final int height ) {
        focusTreeLightweight.constructFocusLine( description, ( Color ) color, y, height );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#constructPathButtonBar()
     */
    @Override
    public void constructPathButtonBar() {
        pathButtonBar = new Composite( pathBar, SWT.NONE );
        GridDataFactory.swtDefaults().align( SWT.FILL, SWT.CENTER ).grab( true, false ).applyTo( pathButtonBar );
        RowLayoutFactory.fillDefaults().fill( true ).wrap( false ).applyTo( pathButtonBar );
        pathButtonBar.addListener( SWT.Resize, new Listener() {

            @Override
            public void handleEvent( final Event event ) {
                controller.pathBarResized();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#constructScrollPathBarLeftAction(java.lang.String, java.net.URL)
     */
    @Override
    public void constructScrollPathBarLeftAction( final String description,
                                                  final URL imageUrl ) {
        scrollPathBarLeftButton = new Label( pathBar, SWT.NONE );
        scrollPathBarLeftButton.setImage( image( imageUrl ) );
        scrollPathBarLeftButton.setToolTipText( description );
        scrollPathBarLeftButton.addMouseListener( new MouseAdapter() {

            @Override
            public void mouseUp( final MouseEvent event ) {
                if ( leftMouseButtonClicked( event ) ) controller.scrollPathBarLeft();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#constructScrollPathBarRightAction(java.lang.String, java.net.URL)
     */
    @Override
    public void constructScrollPathBarRightAction( final String description,
                                                   final URL imageUrl ) {
        scrollPathBarRightButton = new Label( pathBar, SWT.NONE );
        scrollPathBarRightButton.setImage( image( imageUrl ) );
        scrollPathBarRightButton.setToolTipText( description );
        scrollPathBarRightButton.addMouseListener( new MouseAdapter() {

            @Override
            public void mouseUp( final MouseEvent event ) {
                if ( leftMouseButtonClicked( event ) ) controller.scrollPathBarRight();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#constructToggleIconViewAction(java.lang.String)
     */
    @Override
    public void constructToggleIconViewAction( final String name ) {
        iconViewMenuItem = new MenuItem( popup, SWT.PUSH );
        iconViewMenuItem.setText( name );
        iconViewMenuItem.addSelectionListener( new SelectionAdapter() {

            @Override
            public void widgetSelected( final SelectionEvent event ) {
                controller.toggleIconView();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#copyPath()
     */
    @Override
    public void copyPath() {
        final TextTransfer textTransfer = TextTransfer.getInstance();
        final StringBuilder path = new StringBuilder();
        for ( final Control control : pathButtonBar.getChildren() )
            path.append( '/' ).append( ( ( Label ) control ).getText() );
        CLIPBOARD.setContents( new Object[] { path.toString() }, new Transfer[] { textTransfer } );
        controller.copyPath();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#disableCollapseAllColumnsAction()
     */
    @Override
    public void disableCollapseAllColumnsAction() {
        collapseAllColumnsMenuItem.setEnabled( false );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#disableCopyPathAction()
     */
    @Override
    public void disableCopyPathAction() {
        copyPathButton.setEnabled( false );
        copyPathMenuItem.setEnabled( false );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#disableDuplicateTreeAction()
     */
    @Override
    public void disableDuplicateTreeAction() {
        duplicateTreeMenuItem.setEnabled( false );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#disableToggleIconViewAction()
     */
    @Override
    public void disableToggleIconViewAction() {
        iconViewMenuItem.setEnabled( false );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#duplicateTree()
     */
    @Override
    public void duplicateTree() {
        // duplicate( root );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#hideChildCount(Object)
     */
    @Override
    public void hideChildCount( final Object childCountWidget ) {
        ( ( Label ) childCountWidget ).setVisible( false );
    }

    void hideExcessiveLeftMostPathButtons() {
        final Control[] pathButtons = pathButtonBar.getChildren();
        final int width = pathButtonBar.getBounds().width;
        for ( int ndx = 0; ndx < pathButtons.length &&
                           pathButtonBar.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x > width; ++ndx ) {
            scrollPathBarLeftButton.setVisible( true );
            final Control pathButton = pathButtons[ ndx ];
            if ( pathButton.isVisible() ) {
                pathButton.setVisible( false );
                ( ( RowData ) pathButton.getLayoutData() ).exclude = true;
            }
        }
        scrollPathBarLeftButton.setVisible( pathButtons.length == 0 ? false
                                                                   : ( ( RowData ) ( ( Label ) pathButtons[ 0 ] ).getLayoutData() ).exclude );
        scrollPathBarRightButton.setVisible( pathButtons.length == 0 ? false
                                                                    : ( ( RowData ) ( ( Label ) pathButtons[ pathButtons.length - 1 ] ).getLayoutData() ).exclude );
        pathButtonBar.layout();
        pathButtonBar.getParent().layout();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#hideHideColumnAction(java.lang.Object)
     */
    @Override
    public void hideHideColumnAction( final Object hideButton ) {
        ( ( Label ) hideButton ).setVisible( false );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#hideScrollPathBarLeft()
     */
    @Override
    public void hideScrollPathBarLeft() {
        scrollPathBarLeftButton.setVisible( false );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#hideScrollPathBarRight()
     */
    @Override
    public void hideScrollPathBarRight() {
        scrollPathBarRightButton.setVisible( false );
    }

    private Image image( final URL imageUrl ) {
        try ( final InputStream imageStream = imageUrl.openStream() ) {
            return /*Platform.isRunning() ? Activator.image( path ) : */new Image( getDisplay(), imageStream );
        } catch ( final Exception e ) {
            return null;
        }
    }

    boolean leftMouseButtonClicked( final MouseEvent event ) {
        return event.button == 1 && ( event.stateMask & SWT.MODIFIER_MASK ) == 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#newColumn()
     */
    @Override
    public Column newColumn() {
        return new Column();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#newColumnChildCount(java.lang.Object, java.lang.String, java.lang.String)
     */
    @Override
    public Object newColumnChildCount( final Object header,
                                       final String count,
                                       final String description ) {
        final Label childCount = new Label( ( Composite ) header, SWT.NONE );
        GridDataFactory.swtDefaults().applyTo( childCount );
        childCount.setText( count );
        childCount.setToolTipText( description );
        return childCount;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#newColumnHeader(String)
     */
    @Override
    public Object newColumnHeader( final String description ) {
        ( ( GridLayout ) headerBar.getLayout() ).numColumns++;
        final Composite header = new Composite( headerBar, SWT.NONE );
        GridLayoutFactory.fillDefaults().numColumns( 3 ).margins( HEADER_MARGIN, 1 ).applyTo( header );
        header.addPaintListener( headerPaintListener );
        header.setToolTipText( description );
        return header;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#newColumnName(java.lang.Object, java.lang.String, java.lang.String)
     */
    @Override
    public Object newColumnName( final Object header,
                                 final String name,
                                 final String description ) {
        final Label columnName = new Label( ( Composite ) header, SWT.CENTER );
        GridDataFactory.swtDefaults().align( SWT.FILL, SWT.FILL ).grab( true, true ).applyTo( columnName );
        columnName.setText( name );
        columnName.setToolTipText( description );
        final FontData fontData = columnName.getFont().getFontData()[ 0 ];
        fontData.setStyle( SWT.BOLD );
        columnName.setFont( new Font( getDisplay(), fontData ) );
        return columnName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#newColumnView()
     */
    @Override
    public View newColumnView() {
        return focusTreeLightweight.newColumnView();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#newHideColumnAction(java.lang.Object, java.lang.String, java.net.URL)
     */
    @Override
    public Object newHideColumnAction( final Object parent,
                                       final String description,
                                       final URL imageUrl ) {
        final Composite header = ( Composite ) parent;
        final Composite hideButtonPanel = new Composite( header, SWT.NONE );
        GridDataFactory.swtDefaults().applyTo( hideButtonPanel );
        GridLayoutFactory.fillDefaults().applyTo( hideButtonPanel );
        hideButtonPanel.setToolTipText( header.getToolTipText() );
        final Label hideButton = new Label( hideButtonPanel, SWT.NONE );
        GridDataFactory.swtDefaults().align( SWT.RIGHT, SWT.CENTER ).grab( true, false ).applyTo( hideButton );
        hideButton.setImage( image( imageUrl ) );
        hideButton.setToolTipText( description );
        return hideButtonPanel;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#newPathButton(java.lang.String, java.lang.String, java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    public Object newPathButton( final String text,
                                 final String description,
                                 final Object backgroundColor,
                                 final Object foregroundColor ) {
        final Label button = new Label( pathButtonBar, SWT.NONE );
        button.setText( text );
        button.setToolTipText( description );
        button.setBackground( ( Color ) backgroundColor );
        button.setForeground( ( Color ) foregroundColor );
        button.setAlignment( SWT.CENTER );
        final Point size = button.computeSize( SWT.DEFAULT, SWT.DEFAULT );
        button.setLayoutData( new RowData( size.x + 10, size.y ) );
        button.addPaintListener( pathButtonPaintListener );
        hideExcessiveLeftMostPathButtons();
        return button;
    }

    private ToolItem newToolBarButton( final ToolBar toolBar,
                                       final int style,
                                       final URL imageUrl,
                                       final String toolTip ) {
        final ToolItem item = new ToolItem( toolBar, style );
        item.setImage( image( imageUrl ) );
        item.setToolTipText( toolTip );
        return item;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#pathBarResized()
     */
    @Override
    public void pathBarResized() {
        // Show all path buttons
        final Control[] pathButtons = pathButtonBar.getChildren();
        for ( final Control pathButton : pathButtons ) {
            pathButton.setVisible( true );
            ( ( RowData ) pathButton.getLayoutData() ).exclude = false;
        }
        // Hide first shown path button on left until all buttons fit in button bar
        hideExcessiveLeftMostPathButtons();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#preferences()
     */
    @Override
    public Preferences preferences() {
        if ( preferences == null ) preferences = new Preferences();
        return preferences;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#scrollPathBarLeft()
     */
    @Override
    public void scrollPathBarLeft() {
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
        scrollPathBarLeftButton.setVisible( !pathButtons[ 0 ].isVisible() );
        scrollPathBarRightButton.setVisible( true );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#scrollPathBarRight()
     */
    @Override
    public void scrollPathBarRight() {
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

    /**
     * @param preferences
     *        the preferences
     */
    public void setPreferences( final Preferences preferences ) {
        this.preferences = preferences;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#toggleIconView(String)
     */
    @Override
    public void toggleIconView( final String text ) {
        iconViewMenuItem.setText( text );
        // final Control control = getDisplay().getCursorControl();
        // Column column = null;
        // if ( control instanceof FocusTreeCanvas ) for ( final Column col : columns ) {
        // if ( col.bounds.contains( event.x, event.y ) ) {
        // column = col;
        // break;
        // }
        // }
        // else column = ( Column ) control.getData( COLUMN_PROPERTY );
        // iconViewMenuItem.setEnabled( column != null );
        // iconViewMenuItem.setData( COLUMN_PROPERTY, column );
        // toggleIconView( ( Column ) iconViewMenuItem.getData( COLUMN_PROPERTY ) );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ui.FocusTreeView#treeResized()
     */
    @Override
    public void treeResized() {
        focusTreeLightweight.treeResized();
    }

    class Column implements FocusTreeView.Column {

        int cellPreferredWidth;
        Cell focusCell;
        boolean focusCellExpanded;
        Composite header;
        int height;
        Object item;
        Label pathButton;
        View view;
        int width;
        int widthBeforeHiding;
        int widthBeforeIconView;
        int x;
        int y;

        @Override
        public int cellPreferredWidth() {
            return cellPreferredWidth;
        }

        @Override
        public Cell focusCell() {
            return focusCell;
        }

        @Override
        public boolean focusCellExpanded() {
            return focusCellExpanded;
        }

        @Override
        public Composite header() {
            return header;
        }

        @Override
        public int height() {
            return height;
        }

        @Override
        public Object item() {
            return item;
        }

        @Override
        public Label pathButton() {
            return pathButton;
        }

        @Override
        public void setHeader( final Object header ) {
            this.header = ( Composite ) header;
        }

        @Override
        public void setItem( final Object item ) {
            this.item = item;
        }

        @Override
        public void setPathButton( final Object pathButton ) {
            this.pathButton = ( Label ) pathButton;
        }

        @Override
        public View view() {
            return view;
        }

        @Override
        public int width() {
            return width;
        }

        @Override
        public int widthBeforeHiding() {
            return widthBeforeHiding;
        }

        @Override
        public int widthBeforeIconView() {
            return widthBeforeIconView;
        }

        @Override
        public int x() {
            return x;
        }

        @Override
        public int y() {
            return y;
        }
    }

    /**
     * 
     */
    public static class Preferences extends FocusTreeView.Preferences {

        /**
         *
         */
        public static final Color DEFAULT_CELL_BACKGROUND_COLOR = Display.getDefault().getSystemColor( SWT.COLOR_WHITE );

        /**
         * 
         */
        public static final Color DEFAULT_CHILD_INDEX_COLOR = new Color( null, 0, 128, 255 );

        /**
         * 
         */
        public static final TextCellEditor DEFAULT_EDITOR = new TextCellEditor();

        /**
         * 
         */
        public static final Color DEFAULT_FOCUS_CELL_BORDER_COLOR = DEFAULT_CHILD_INDEX_COLOR;

        /**
         * 
         */
        public static final Color DEFAULT_FOCUS_LINE_COLOR = DEFAULT_CHILD_INDEX_COLOR;

        /**
         *
         */
        public static final Color DEFAULT_PATH_BUTTON_HIDDEN_BACKGROUND_COLOR =
            Display.getDefault().getSystemColor( SWT.COLOR_GRAY );

        /**
         *
         */
        public static final Color DEFAULT_TREE_BACKGROUND_COLOR = Display.getDefault().getSystemColor( SWT.COLOR_WHITE );

        static {
            DEFAULT_EDITOR.setStyle( SWT.BORDER );
        }

        /**
         * {@inheritDoc} Default is {@link #DEFAULT_CELL_BACKGROUND_COLOR}.
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Preferences#cellBackgroundColor(java.lang.Object)
         */
        @Override
        public Color cellBackgroundColor( final Object item ) {
            return DEFAULT_CELL_BACKGROUND_COLOR;
        }

        /**
         * {@inheritDoc} Default is white or black, whichever contrasts more with the {@link #cellBackgroundColor(Object) cell's
         * background color}.
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Preferences#cellForegroundColor(java.lang.Object)
         */
        @Override
        public Color cellForegroundColor( final Object item ) {
            final Color color = cellBackgroundColor( item );
            final double yiq = ( ( color.getRed() * 299 ) + ( color.getGreen() * 587 ) + ( color.getBlue() * 114 ) ) / 1000.0;
            return yiq >= 128.0 ? Display.getCurrent().getSystemColor( SWT.COLOR_BLACK )
                               : Display.getCurrent().getSystemColor( SWT.COLOR_WHITE );
        }

        /**
         * {@inheritDoc} Default is {@link #DEFAULT_CHILD_INDEX_COLOR}.
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Preferences#childIndexColor(java.lang.Object)
         */
        @Override
        public Color childIndexColor( final Object item ) {
            return DEFAULT_CHILD_INDEX_COLOR;
        }

        /**
         * {@inheritDoc} Default is a cell with a {@link RoundedRectangle} view.
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Preferences#createCell(java.lang.Object)
         */
        @Override
        public Cell createCell( final Object item ) {
            return new Cell( new RoundedRectangle() );
        }

        /**
         * {@inheritDoc} Default is {@link #DEFAULT_FOCUS_CELL_BORDER_COLOR}.
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Preferences#focusCellBorderColor()
         */
        @Override
        public Color focusCellBorderColor() {
            return DEFAULT_FOCUS_CELL_BORDER_COLOR;
        }

        /**
         * {@inheritDoc} Default is {@link #DEFAULT_FOCUS_LINE_COLOR}.
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Preferences#focusLineColor()
         */
        @Override
        public Color focusLineColor() {
            return DEFAULT_FOCUS_LINE_COLOR;
        }

        /**
         * {@inheritDoc} Default is {link {@link #DEFAULT_EDITOR}.
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Preferences#indexEditor(java.lang.Object)
         */
        @Override
        public CellEditor indexEditor( final Object item ) {
            return DEFAULT_EDITOR;
        }

        /**
         * {@inheritDoc} Default is {link {@link #DEFAULT_EDITOR}.
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Preferences#nameEditor(java.lang.Object)
         */
        @Override
        public CellEditor nameEditor( final Object item ) {
            return DEFAULT_EDITOR;
        }

        /**
         * {@inheritDoc} Default is {@link #DEFAULT_PATH_BUTTON_HIDDEN_BACKGROUND_COLOR}.
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Preferences#pathButtonHiddenBackgroundColor(java.lang.Object)
         */
        @Override
        public Color pathButtonHiddenBackgroundColor( final Object item ) {
            return DEFAULT_PATH_BUTTON_HIDDEN_BACKGROUND_COLOR;
        }

        /**
         * {@inheritDoc} Default is {@link #DEFAULT_TREE_BACKGROUND_COLOR}.
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Preferences#treeBackgroundColor()
         */
        @Override
        public Color treeBackgroundColor() {
            return DEFAULT_TREE_BACKGROUND_COLOR;
        }

        /**
         * {@inheritDoc} Default is {link {@link #DEFAULT_EDITOR}.
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Preferences#typeEditor(java.lang.Object)
         */
        @Override
        public CellEditor typeEditor( final Object item ) {
            return DEFAULT_EDITOR;
        }

        /**
         * {@inheritDoc} Default is {link {@link #DEFAULT_EDITOR}.
         * 
         * @see org.modeshape.modeler.ui.FocusTreeView.Preferences#valueEditor(java.lang.Object)
         */
        @Override
        public CellEditor valueEditor( final Object item ) {
            return DEFAULT_EDITOR;
        }
    }
}
