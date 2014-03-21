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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.draw2d.ToolTipHelper;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.eclipse.EclipseI18n;
import org.polyglotter.eclipse.Util;
import org.polyglotter.eclipse.focustree.FocusTree.Cell;
import org.polyglotter.eclipse.focustree.FocusTree.Column;
import org.polyglotter.eclipse.focustree.FocusTree.Indicator;

class FocusTreeCanvas extends FigureCanvas {

    final FocusTree focusTree;
    final FreeformLayer canvas = new FreeformLayer();
    final Figure toolBar = new Figure();
    final Panel focusLine = new Panel();
    final Label focusColumnToolTip = new Label( EclipseI18n.focusTreeFocusColumnToolTip.text() );
    int focusLineOffset = FocusTree.DEFAULT_FOCUS_LINE_OFFSET;
    boolean initialIndexIsOne;
    int initialCellWidth = SWT.DEFAULT;
    Color focusColumnColor = FocusTree.DEFAULT_FOCUS_COLUMN_COLOR;
    final LineBorder focusBorder =
        new LineBorder( FocusTree.DEFAULT_FOCUS_CELL_BORDER_COLOR, FocusTree.DEFAULT_FOCUS_LINE_HEIGHT ) {

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
    final LineBorder noFocusBorder = new LineBorder( FocusTree.DEFAULT_FOCUS_LINE_HEIGHT ) {

        @Override
        public void paint( final IFigure figure,
                           final Graphics graphics,
                           final Insets insets ) {}
    };
    final MouseListener canvasMouseListener = new MouseListener.Stub() {

        final AtomicLong lastClicked = new AtomicLong( System.currentTimeMillis() );

        @Override
        public void mouseDoubleClicked( final MouseEvent event ) {
            mouseDoubleClickedOverCanvas( event );
            lastClicked.set( System.currentTimeMillis() );
        }

        @Override
        public void mouseReleased( final MouseEvent event ) {
            getShell().getDisplay().timerExec( 400, new Runnable() {

                @Override
                public void run() {
                    if ( System.currentTimeMillis() - lastClicked.get() > 600L )
                        if ( event.button == 1 && ( event.getState() & SWT.MODIFIER_MASK ) == 0 ) mouseClickedOverCanvas( event );
                    lastClicked.set( System.currentTimeMillis() );
                }
            } );
        }
    };
    final MouseMotionListener canvasMouseMotionListener = new MouseMotionListener.Stub() {

        @Override
        public void mouseDragged( final MouseEvent event ) {
            // Dragging focus line make cause mouse to move outside of focus line bounds, so the canvas needs to propagate mouse
            // events to the focus line listener
            if ( focusLineMouseListener.dragging ) focusLineMouseListener.mouseDragged( event );
        }

        @Override
        public void mouseMoved( final MouseEvent event ) {
            mouseMovedOverCanvas( event );
        }
    };
    final FocusLineMouseListener focusLineMouseListener = new FocusLineMouseListener();
    final Map< Object, Object > lastFocusItemByParent = new HashMap<>();
    ToolTipHelper toolTipHelper;

    ImageFigure mouseOverButton;

    Text textEditor;
    TextEditorHandler textEditorHandler;
    Label fieldEdited;

    FocusTreeCanvas( final FocusTree focusTree,
                     final Composite parent,
                     final int style ) {
        super( parent, style );
        getLightweightSystem().setEventDispatcher( new SWTEventDispatcher() {

            @Override
            protected ToolTipHelper getToolTipHelper() {
                final ToolTipHelper toolTipHelper = super.getToolTipHelper();
                if ( FocusTreeCanvas.this.toolTipHelper != toolTipHelper )
                    FocusTreeCanvas.this.toolTipHelper = toolTipHelper;
                return toolTipHelper;
            }
        } );
        toolTipHelper = new ToolTipHelper( this );
        this.focusTree = focusTree;
        setContents( canvas );
        getViewport().setContentsTracksHeight( true );
        getHorizontalBar().setEnabled( false );
        setHorizontalScrollBarVisibility( NEVER );
        canvas.addMouseListener( canvasMouseListener );
        toolBar.setLayoutManager( new GridLayout() );
        final Figure homeButton = new Figure() {

            @Override
            protected void paintFigure( final Graphics g ) {
                final Rectangle bounds = getBounds();
                g.setAntialias( SWT.ON );
                g.setBackgroundColor( focusColumnColor );
                g.fillRectangle( bounds.x + 4, bounds.y, 8, 16 );
                g.setBackgroundColor( focusLine.getBackgroundColor() );
                g.fillRectangle( bounds.x, bounds.y + 7, 16, 2 );
            }
        };
        toolBar.add( homeButton );
        homeButton.setPreferredSize( new Dimension( 16, 16 ) );
        homeButton.setToolTip( new Label( EclipseI18n.focusTreeHomeToolTip.text() ) );
        homeButton.addMouseListener( new MouseListener.Stub() {

            @Override
            public void mouseReleased( final MouseEvent event ) {
                homeSelected();
            }
        } );
        final ImageFigure collapseAllButton = new ImageFigure( Util.image( "collapseall.gif" ) );
        toolBar.add( collapseAllButton );
        collapseAllButton.setToolTip( new Label( EclipseI18n.focusTreeCollapseAllToolTip.text() ) );
        collapseAllButton.addMouseListener( new MouseListener.Stub() {

            @Override
            public void mouseReleased( final MouseEvent event ) {
                collapseAllSelected();
            }
        } );
        final ImageFigure duplicateButton = new ImageFigure( Util.image( "duplicate.gif" ) );
        toolBar.add( duplicateButton );
        duplicateButton.setToolTip( new Label( EclipseI18n.focusTreeDuplicateToolTip.text() ) );
        duplicateButton.addMouseListener( new MouseListener.Stub() {

            @Override
            public void mouseReleased( final MouseEvent event ) {
                focusTree.duplicate( focusTree.root );
            }
        } );
        toolBar.setSize( toolBar.getPreferredSize() );
        toolBar.setVisible( false );
        final Rectangle focusLineBounds = new Rectangle( focusLine.getBounds() );
        focusLineBounds.height = FocusTree.DEFAULT_FOCUS_LINE_HEIGHT;
        focusLine.setBounds( focusLineBounds );
        focusLine.setToolTip( new Label( EclipseI18n.focusTreeFocusLineToolTip.text() ) );
        focusLine.setBackgroundColor( FocusTree.DEFAULT_FOCUS_LINE_COLOR );
        focusLine.addMouseListener( focusLineMouseListener );
        focusLine.addMouseMotionListener( focusLineMouseListener );
        canvas.addLayoutListener( new LayoutListener.Stub() {

            @Override
            public void postLayout( final IFigure container ) {
                // Make focus line extend horizontally across entire canvas
                final Rectangle bounds = new Rectangle( focusLine.getBounds() );
                bounds.width = canvas.getBounds().width;
                focusLine.setBounds( bounds );
            }
        } );
        canvas.addMouseMotionListener( canvasMouseMotionListener );
        canvas.setOpaque( true );
    }

    private AddPanel addAddPanel( final Column column,
                                  final int modelIndex,
                                  final int viewIndex ) {
        final AddPanel addPanel = new AddPanel();
        column.cellColumn.add( addPanel, viewIndex );
        addPanel.modelIndex = modelIndex;
        addPanel.viewIndex = viewIndex;
        column.cellColumn.setConstraint( addPanel, new GridData( SWT.FILL, SWT.DEFAULT, false, false ) );
        final GridLayout layout = new GridLayout();
        layout.marginHeight = layout.marginWidth = 0;
        addPanel.setLayoutManager( layout );
        final AddButton addButton = new AddButton( Util.image( "add.gif" ) );
        addPanel.add( addButton );
        addPanel.setConstraint( addButton, new GridData( SWT.CENTER, SWT.CENTER, true, true ) );
        addButton.setToolTip( new Label( EclipseI18n.focusTreeAddToolTip.text() ) );
        addButton.setVisible( false );
        return addPanel;
    }

    private Cell addCell( final Object item,
                          final Column column,
                          final AddPanel addBeforePanel ) {
        // Create cell
        final Cell cell = focusTree.model.createCell( item );
        column.cellColumn.add( cell, addBeforePanel.viewIndex + 1 );
        cell.item = item;
        cell.addPanel = addBeforePanel;
        column.cellColumn.setConstraint( cell, new GridData( SWT.FILL, SWT.DEFAULT, false, false ) );
        GridLayout gridLayout = new GridLayout( 3, false );
        gridLayout.marginHeight = gridLayout.marginWidth = 0;
        cell.setLayoutManager( gridLayout );
        cell.setBorder( noFocusBorder );
        cell.setBackgroundColor( focusTree.model.cellBackgroundColor( item ) );
        cell.setToolTip( new Label( EclipseI18n.focusTreeCellToolTip.text() ) );
        // Construct cell
        final int modelIndex = addBeforePanel.modelIndex;
        final Label indexLabel = new Label( String.valueOf( initialIndexIsOne ? modelIndex + 1 : modelIndex ) );
        cell.add( indexLabel );
        indexLabel.setLabelAlignment( PositionConstants.LEFT );
        cell.indexLabel = indexLabel;
        indexLabel.setForegroundColor( focusTree.model.childIndexColor( column.item ) );
        indexLabel.setToolTip( new Label( EclipseI18n.focusTreeCellIndexToolTip.text() ) );
        final Image image = focusTree.model.icon( item );
        final Label iconLabel = new Label();
        cell.add( iconLabel );
        cell.setConstraint( iconLabel, new GridData( SWT.FILL, SWT.CENTER, true, false ) );
        if ( image != null ) iconLabel.setIcon( image );
        final Figure statusPanel = new Figure();
        cell.add( statusPanel );
        gridLayout = new GridLayout();
        gridLayout.marginHeight = gridLayout.marginWidth = 0;
        statusPanel.setLayoutManager( gridLayout );
        for ( final Indicator indicator : focusTree.model.indicators( item ) ) {
            final IndicatorButton button = new IndicatorButton( indicator.image, indicator );
            gridLayout.numColumns++;
            statusPanel.add( button );
            button.setToolTip( new Label( indicator.toolTip ) );
        }
        final DeleteButton deleteButton = new DeleteButton( Util.image( "delete.png" ) );
        statusPanel.add( deleteButton );
        cell.deleteButton = deleteButton;
        deleteButton.setToolTip( new Label( EclipseI18n.focusTreeDeleteToolTip.text() ) );
        deleteButton.setVisible( false );
        // Make index and spacer labels the same size so icon is centered
        final int width = Math.max( indexLabel.getPreferredSize().width, statusPanel.getPreferredSize().width );
        cell.setConstraint( indexLabel, new GridData( width, SWT.DEFAULT ) );
        cell.setConstraint( statusPanel, new GridData( width, SWT.DEFAULT ) );
        // Save preferred width as minimum width before adding name, type, and value labels
        cell.setMinimumSize( cell.getPreferredSize() );
        // Add name field
        NameField nameField;
        try {
            nameField = new NameField( focusTree.model.name( item ) );
        } catch ( final PolyglotterException e ) {
            Util.logError( e, EclipseI18n.focusTreeUnableToGetName, item );
            nameField = new NameField( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) );
        }
        cell.add( nameField );
        nameField.setTextAlignment( PositionConstants.CENTER );
        nameField.setForegroundColor( focusTree.model.cellForegroundColor( item ) );
        try {
            nameField.setToolTip( new Label( EclipseI18n.focusTreeCellNameToolTip.text( focusTree.model.qualifiedName( item ) ) ) );
        } catch ( final PolyglotterException e ) {
            Util.logError( e, EclipseI18n.focusTreeUnableToGetQualifiedName, item );
            nameField.setToolTip( new Label( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) ) );
        }
        GridData gridData = new GridData( SWT.FILL, SWT.DEFAULT, true, false );
        gridData.horizontalSpan = 3;
        cell.setConstraint( nameField, gridData );
        // Add type field
        TypeField typeField;
        try {
            typeField = new TypeField( focusTree.model.type( item ) );
        } catch ( final PolyglotterException e ) {
            Util.logError( e, EclipseI18n.focusTreeUnableToGetType, item );
            typeField = new TypeField( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) );
        }
        cell.add( typeField );
        typeField.setTextAlignment( PositionConstants.CENTER );
        typeField.setForegroundColor( focusTree.model.cellForegroundColor( item ) );
        typeField.setToolTip( new Label( EclipseI18n.focusTreeCellTypeToolTip.text() ) );
        gridData = new GridData( SWT.FILL, SWT.DEFAULT, true, false );
        gridData.horizontalSpan = 3;
        cell.setConstraint( typeField, gridData );
        if ( focusTree.model.hasValue( item ) ) {
            // Add value field
            ValueField valueField;
            try {
                valueField = new ValueField( focusTree.model.value( item ) );
            } catch ( final PolyglotterException e ) {
                Util.logError( e, EclipseI18n.focusTreeUnableToGetValue, item );
                valueField = new ValueField( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) );
            }
            cell.add( valueField );
            valueField.setTextAlignment( PositionConstants.CENTER );
            valueField.setForegroundColor( focusTree.model.cellForegroundColor( item ) );
            valueField.setToolTip( new Label( EclipseI18n.focusTreeCellValueToolTip.text() ) );
            gridData = new GridData( SWT.FILL, SWT.DEFAULT, true, false );
            gridData.horizontalSpan = 3;
            cell.setConstraint( valueField, gridData );
        }

        // Create add-after panel
        addAddPanel( column, modelIndex + 1, addBeforePanel.viewIndex + 2 );

        column.cellColumn.revalidate();

        return cell;
    }

    void addColumn( final Column column ) {
        column.backgroundColumn = new Figure();
        // Add columns to beginning of canvas's children to ensure their backgrounds are painted first
        canvas.add( column.backgroundColumn, 0 );
        column.backgroundColumn.setBackgroundColor( focusColumnColor );
        column.cellColumn = new CellColumn();
        column.cellColumn.column = column;
        canvas.add( column.cellColumn );
        setCellColumnLayoutManager( column );

        // Get last focus cell for this column if available
        final Object lastFocusItem = lastFocusItemByParent.get( column.item );
        // Create first add button
        AddPanel addPanel = addAddPanel( column, 0, 0 );
        // Create cells and subsequent add-after buttons
        try {
            for ( final Object child : focusTree.model.children( column.item ) ) {
                final Cell cell = addCell( child, column, addPanel );
                // Adjust initial cell width if below minimum width
                final int minWidth = cell.getMinimumSize().width;
                if ( initialCellWidth < minWidth ) initialCellWidth = minWidth;
                if ( column.focusCell == null && ( lastFocusItem == null || child.equals( lastFocusItem ) ) ) column.focusCell =
                    cell;
                final List< ? > children = column.cellColumn.getChildren();
                addPanel = ( AddPanel ) children.get( children.size() - 1 );
            }
        } catch ( final PolyglotterException e ) {
            Util.logAndShowError( e, EclipseI18n.focusTreeUnableToGetChildren, column.item );
        }
        // Set preferred width of cells to model value
        for ( final Object child : column.cellColumn.getChildren() ) {
            if ( child instanceof Cell ) {
                final Cell cell = ( Cell ) child;
                final GridData gridData = ( ( GridData ) column.cellColumn.getLayoutManager().getConstraint( cell ) );
                gridData.widthHint = initialCellWidth;
                column.cellColumn.setConstraint( cell, gridData );
            }
        }
        // Save column preferred width for use by double-clicking header border
        column.preferredWidth = column.cellColumn.getPreferredSize().width;
        // Force layout to get cell locations set
        column.cellColumn.getLayoutManager().layout( column.cellColumn );
        // Set bounds
        if ( column.focusCell != null ) column.focusCell.setSize( column.focusCell.getPreferredSize() );
        final Dimension cellColumnSize = column.cellColumn.getPreferredSize();
        final int cellColumnX;
        if ( focusTree.columns.size() == 1 ) cellColumnX = 0;
        else {
            final Rectangle previousCellColumnBounds = focusTree.columns.get( focusTree.columns.size() - 2 ).cellColumn.getBounds();
            cellColumnX = previousCellColumnBounds.x + previousCellColumnBounds.width;
        }
        // column.cellColumn.revalidate();
        column.cellColumn.setBounds( new Rectangle( cellColumnX, 0, cellColumnSize.width, cellColumnSize.height ) );
        final int canvasWidth = cellColumnX + cellColumnSize.width;
        focusLine.setSize( canvasWidth, focusLine.getSize().height );
        final int canvasHeight = canvas.getSize().height;
        canvas.setSize( canvasWidth, canvasHeight );
        column.backgroundColumn.setBounds( new Rectangle( cellColumnX, 0, cellColumnSize.width, canvasHeight ) );

        // Wire cell column to show focus line tool tip on mouse-over
        // Note, the cell column figures are transparent, but overlap the focus line and canvas, so mouse events need to be
        // propagated to these other figures whenever they are also listening for the same event types
        propagateEvents( column );

        // Focus on first cell
        focusCell( column, column.focusCell );
    }

    Cell cellFor( IFigure figure ) {
        while ( figure != canvas && !( figure instanceof Cell ) )
            figure = figure.getParent();
        return figure == canvas ? null : ( Cell ) figure;
    }

    void changeFocusCell( final Column column,
                          final Cell cell ) {
        if ( column.focusCell == cell ) {
            // Collapse current focus cell if expanded
            if ( column.focusCellExpanded ) removeColumnsAfter( column );
            // Else expand focus cell if it has children
            else expandFocusCell( column );
        } else {
            // Collapse current focus cell if expanded
            if ( column.focusCellExpanded ) removeColumnsAfter( column );
            // Focus on cell
            focusCell( column, cell );
            expandFocusCell( column );
        }
        // Change focus column
        // Scroll so that focus line is at focus line offset from top of view and last column is visible
        focusTree.focusColumn( column );
    }

    void collapseAllSelected() {
        final Column firstColumn = focusTree.columns.get( 0 );
        if ( firstColumn.focusCellExpanded ) changeFocusCell( firstColumn, firstColumn.focusCell );
        lastFocusItemByParent.clear();
    }

    private Column columnFor( IFigure figure ) {
        while ( !( figure instanceof CellColumn ) )
            figure = figure.getParent();
        return ( ( CellColumn ) figure ).column;
    }

    private void edit( final IFigure figure,
                       final TextEditorHandler handler ) {
        textEditor = new Text( this, SWT.NONE );
        textEditorHandler = handler;
        fieldEdited = ( Label ) figure;
        textEditor.setText( fieldEdited.getText() );
        final Rectangle fieldBounds = fieldEdited.getBounds();
        final Point viewLocation = getViewport().getViewLocation();
        textEditor.setBounds( fieldBounds.x - viewLocation.x,
                              fieldBounds.y - viewLocation.y,
                              fieldBounds.width,
                              fieldBounds.height );
        textEditor.addKeyListener( new KeyAdapter() {

            private final Color originalForegroundColor = textEditor.getForeground();

            @Override
            public void keyReleased( final KeyEvent event ) {
                if ( textEditor == null ) return;
                final String problem = handler.problem();
                if ( problem == null ) {
                    textEditor.setToolTipText( null );
                    textEditor.setForeground( originalForegroundColor );
                } else {
                    textEditor.setToolTipText( problem );
                    textEditor.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_RED ) );
                }
            }
        } );
        textEditor.addTraverseListener( new TraverseListener() {

            @Override
            public void keyTraversed( final TraverseEvent event ) {
                if ( ( event.detail & ( SWT.TRAVERSE_ESCAPE ) ) != 0 ) {
                    textEditor.dispose();
                    textEditor = null;
                } else if ( ( event.detail & ( SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS ) ) != 0 ) endEdit();
                else if ( ( event.detail & SWT.TRAVERSE_RETURN ) != 0 && textEditor.getToolTipText() == null ) endEdit();
            }
        } );
        textEditor.addFocusListener( new FocusAdapter() {

            @Override
            public void focusLost( final FocusEvent event ) {
                endEdit();
            }
        } );
        textEditor.setFocus();
    }

    // TODO what if name change causes index change?
    void endEdit() {
        if ( textEditor.getToolTipText() == null )
            try {
                final Object item = textEditorHandler.commit();
                final Cell cell = cellFor( fieldEdited );
                if ( cell.item != item ) {
                    cell.item = item;
                    final Column column = columnFor( cell );
                    column.focusCell = null;
                    changeFocusCell( column, cell );
                }
                fieldEdited.setText( textEditor.getText() );
                if ( fieldEdited instanceof NameField )
                    try {
                        ( ( Label ) fieldEdited.getToolTip() ).setText( EclipseI18n.focusTreeCellNameToolTip.text( focusTree.model.qualifiedName( item ) ) );
                    } catch ( final PolyglotterException e ) {
                        Util.logError( e, EclipseI18n.focusTreeUnableToGetQualifiedName, item );
                        ( ( Label ) fieldEdited.getToolTip() ).setText( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) );
                    }
            } catch ( final PolyglotterException e ) {
                Util.logAndShowError( e, EclipseI18n.focusTreeUnableToCommitChanges, cellFor( fieldEdited ).item );
            }
        textEditor.dispose();
        textEditor = null;
    }

    private void expandFocusCell( final Column column ) {
        try {
            if ( focusTree.model.childrenAddable( column.focusCell.item ) || focusTree.model.hasChildren( column.focusCell.item ) ) {
                focusTree.addColumn( column.focusCell.item );
                column.focusCellExpanded = true;
            }
        } catch ( final PolyglotterException e ) {
            Util.logAndShowError( e, EclipseI18n.focusTreeUnableToDetermineIfChildrenExist, column.focusCell.item );
        }
    }

    void focusCell( final Column column,
                    final Cell focusCell ) {
        // Collapse previous focus cell and give it a no-focus border
        if ( column.focusCell != null ) column.focusCell.setBorder( noFocusBorder );

        // Set new focus cell and child
        column.focusCell = focusCell;
        if ( focusCell != null ) {
            lastFocusItemByParent.put( column.item, focusCell.item );
            focusCell.setBorder( focusBorder );
        }
        final Rectangle focusCellBounds =
            ( focusCell == null ? ( AddPanel ) column.cellColumn.getChildren().get( 0 ) : focusCell ).getBounds();
        final Rectangle cellColumnBounds = new Rectangle( column.cellColumn.getBounds() );
        final Rectangle focusLineBounds = focusLine.getBounds();
        cellColumnBounds.y -= focusCellBounds.y + focusCellBounds.height / 2 - focusLineBounds.y - focusLineBounds.height / 2;
        column.cellColumn.setBounds( cellColumnBounds );
        updateBounds();
    }

    void hideColumn( final Column column,
                     final int columnWidth ) {
        updateColumnWidth( column, columnWidth, true );
        focusTree.unfocusColumn( column );
    }

    void hideIconView( final Column column ) {
        focusLine.setVisible( true );
        for ( final Column col : focusTree.columns ) {
            col.backgroundColumn.setVisible( true );
            col.cellColumn.setVisible( true );
        }
        setCellColumnLayoutManager( column );
        for ( final Object figure : column.cellColumn.getChildren() ) {
            ( ( IFigure ) figure ).setPreferredSize( null );
            // jpav: remove
            System.out.println( column.cellColumn.getLayoutManager().getConstraint( ( IFigure ) figure ) );
        }
        final Rectangle backgroundColumnBounds = column.backgroundColumn.getBounds();
        column.cellColumn.setBounds( new Rectangle( backgroundColumnBounds.x, 0,
                                                    backgroundColumnBounds.width, column.cellColumn.getPreferredSize().height ) );
        focusCell( column, column.focusCell );
    }

    private void hideMouseOverButton() {
        if ( mouseOverButton != null ) {
            mouseOverButton.setVisible( false );
            mouseOverButton = null;
        }
    }

    void homeSelected() {
        focusTree.scrollToFocusColumn();
        scrollToFocusLine();
    }

    boolean iconViewShown() {
        return !focusLine.isVisible();
    }

    void modelChanged() {
        canvas.removeAll();
        canvas.add( toolBar );
        canvas.add( focusLine );
    }

    void mouseClickedOverCanvas( final MouseEvent event ) {
        if ( textEditor != null ) endEdit();
        final IFigure figure = canvas.findFigureAt( event.x, event.y );
        if ( figure instanceof AddButton ) {
            final Column column = columnFor( figure );
            try {
                AddPanel addPanel = ( AddPanel ) figure.getParent();
                final Object item = focusTree.model.createChildAt( column.item, addPanel.modelIndex );
                if ( item == null )
                    throw new PolyglotterException( EclipseI18n.focusTreeNullReturnedFromCreateChildAt,
                                                    column.item,
                                                    addPanel.modelIndex );
                // Determine at what index model really created item
                int modelIndex = 0;
                for ( final Object child : focusTree.model.children( column.item ) ) {
                    if ( child.equals( item ) ) break;
                    modelIndex++;
                }
                // Find correct add panel to create cell after
                for ( final Object child : column.cellColumn.getChildren() )
                    if ( child instanceof AddPanel && ( ( AddPanel ) child ).modelIndex == modelIndex ) {
                        addPanel = ( AddPanel ) child;
                        break;
                    }
                // Add new cell for item
                final Cell cell = addCell( item, column, addPanel );
                // Update indexes
                final List< ? > children = column.cellColumn.getChildren();
                for ( int ndx = cell.addPanel.viewIndex + 3; ndx < children.size(); ndx++ ) {
                    final Cell afterCell = ( Cell ) children.get( ndx++ );
                    afterCell.indexLabel.setText( String.valueOf( Integer.parseInt( afterCell.indexLabel.getText() ) + 1 ) );
                    final AddPanel afterAddPanel = ( AddPanel ) children.get( ndx );
                    afterAddPanel.viewIndex += 2;
                    afterAddPanel.modelIndex++;
                }
                column.cellColumn.setSize( column.cellColumn.getPreferredSize() );
                column.cellColumn.getLayoutManager().layout( column.cellColumn );
                addPaintListener( new PaintListener() {

                    @Override
                    public void paintControl( final PaintEvent e ) {
                        changeFocusCell( column, cell );
                        removePaintListener( this );
                    }
                } );
                changeFocusCell( column, cell );
            } catch ( final PolyglotterException e ) {
                Util.logAndShowError( e, EclipseI18n.focusTreeUnableToCreateItem, column.item );
            }
        } else if ( figure instanceof DeleteButton ) {
            final Cell cell = cellFor( figure );
            String name;
            try {
                name = EclipseI18n.deleteConfirmationMessage.text( focusTree.model.name( cell.item ) );
            } catch ( final Exception e ) {
                name = EclipseI18n.focusTreeErrorText.text( e.getMessage() );
            }
            if ( MessageDialog.openConfirm( getShell(), EclipseI18n.confirmDialogTitle.text(), name ) ) {
                if ( focusTree.model.delete( cell.item ) ) {
                    final CellColumn cellColumn = ( CellColumn ) cell.getParent();
                    cellColumn.remove( cell.addPanel );
                    cellColumn.remove( cell );
                    // Update indexes
                    final List< ? > children = cellColumn.getChildren();
                    for ( int ndx = cell.addPanel.viewIndex; ndx < children.size(); ndx++ ) {
                        final AddPanel addPanel = ( AddPanel ) children.get( ndx++ );
                        addPanel.viewIndex -= 2;
                        addPanel.modelIndex--;
                        if ( ndx < children.size() ) {
                            final Cell afterCell = ( Cell ) children.get( ndx );
                            afterCell.indexLabel.setText( String.valueOf( Integer.parseInt( afterCell.indexLabel.getText() ) - 1 ) );
                        }
                    }
                    // Focus on next cell, or previous cell if last cell was deleted
                    cellColumn.getLayoutManager().layout( cellColumn );
                    if ( children.size() > 1 ) {
                        final Cell newCell =
                            ( Cell ) children.get( Math.min( cell.addPanel.viewIndex + 1, children.size() - 2 ) );
                        addPaintListener( new PaintListener() {

                            @Override
                            public void paintControl( final PaintEvent e ) {
                                changeFocusCell( cellColumn.column, newCell );
                                removePaintListener( this );
                            }
                        } );
                        changeFocusCell( cellColumn.column, newCell );
                    }
                }
            }
        } else if ( figure instanceof IndicatorButton ) {
            ( ( IndicatorButton ) figure ).indicator.selected( cellFor( figure ).item );
        } else {
            final Cell cell = cellFor( figure );
            if ( cell != null ) changeFocusCell( columnFor( cell ), cell );
        }
    }

    void mouseDoubleClickedOverCanvas( final MouseEvent event ) {
        if ( textEditor != null ) endEdit();
        final IFigure figure = canvas.findFigureAt( event.x, event.y );
        final Cell cell = cellFor( figure );
        if ( figure instanceof NameField ) {
            if ( focusTree.model.nameEditable( cell.item ) ) {
                edit( figure, new TextEditorHandler() {

                    @Override
                    public Object commit() throws PolyglotterException {
                        return focusTree.model.setName( cell.item, textEditor.getText() );
                    }

                    @Override
                    public String problem() {
                        return focusTree.model.nameProblem( cell.item, textEditor.getText() );
                    }
                } );
                return;
            }
        } else if ( figure instanceof TypeField ) {
            if ( focusTree.model.typeEditable( cell.item ) ) {
                edit( figure, new TextEditorHandler() {

                    @Override
                    public Object commit() throws PolyglotterException {
                        return focusTree.model.setType( cell.item, textEditor.getText() );
                    }

                    @Override
                    public String problem() {
                        return focusTree.model.typeProblem( cell.item, textEditor.getText() );
                    }
                } );
                return;
            }
        } else if ( figure instanceof ValueField ) {
            if ( focusTree.model.valueEditable( cell.item ) ) {
                edit( figure, new TextEditorHandler() {

                    @Override
                    public Object commit() throws PolyglotterException {
                        return focusTree.model.setValue( cell.item, textEditor.getText() );
                    }

                    @Override
                    public String problem() {
                        return focusTree.model.valueProblem( cell.item, textEditor.getText() );
                    }
                } );
                return;
            }
        }
        try {
            if ( cell != null && ( focusTree.model.childrenAddable( cell.item ) || focusTree.model.hasChildren( cell.item ) ) )
                focusTree.duplicate( cell.item );
        } catch ( final PolyglotterException e ) {
            Util.logAndShowError( e, EclipseI18n.focusTreeUnableToDetermineIfChildrenAddableOrExist, cell.item );
        }
    }

    void mouseMovedOverCanvas( final MouseEvent event ) {
        IFigure figure = canvas.findFigureAt( event.x, event.y );
        if ( figure instanceof AddButton ) figure = figure.getParent();
        if ( figure instanceof AddPanel ) {
            final AddButton addButton = ( AddButton ) figure.getChildren().get( 0 );
            if ( mouseOverButton == addButton ) return;
            if ( focusTree.model.childrenAddable( columnFor( figure ).item ) ) {
                if ( mouseOverButton != null ) mouseOverButton.setVisible( false );
                addButton.setVisible( true );
                mouseOverButton = addButton;
                return;
            }
        } else {
            final Cell cell = cellFor( figure );
            if ( cell != null ) {
                if ( mouseOverButton == cell.deleteButton ) return;
                if ( focusTree.model.deletable( cell.item ) ) {
                    if ( mouseOverButton != null ) mouseOverButton.setVisible( false );
                    cell.deleteButton.setVisible( true );
                    mouseOverButton = cell.deleteButton;
                    return;
                }
            }
        }
        hideMouseOverButton();
    }

    void moveToolBar( final int x ) {
        canvas.remove( toolBar );
        toolBar.setLocation( new Point( x, getViewport().getClientArea().y ) );
        canvas.add( toolBar );
    }

    private void propagateEvents( final Column column ) {
        column.cellColumn.addMouseMotionListener( new MouseMotionListener.Stub() {

            @Override
            public void mouseDragged( final MouseEvent event ) {
                if ( focusLineMouseListener.dragging || focusLine.containsPoint( event.getLocation() ) )
                    focusLineMouseListener.mouseDragged( event );
            }

            @Override
            public void mouseMoved( final MouseEvent event ) {
                // TODO fix bug where initial tool tip display after change is showing up outside of app briefly
                // and frequent invalid thread access errors from tool tip helper timer on app exit
                if ( focusLine.containsPoint( event.getLocation() ) ) {
                    if ( column.cellColumn.getToolTip() != focusLine.getToolTip() ) {
                        toolTipHelper.updateToolTip( focusLine, focusLine.getToolTip(), event.x, event.y );
                        column.cellColumn.setToolTip( focusLine.getToolTip() );
                    }
                } else if ( column == focusTree.focusColumn ) {
                    if ( column.cellColumn.getToolTip() != focusColumnToolTip ) {
                        toolTipHelper.updateToolTip( focusLine, focusColumnToolTip, event.x, event.y );
                        column.cellColumn.setToolTip( focusColumnToolTip );
                    }
                } else column.cellColumn.setToolTip( null );
                canvasMouseMotionListener.mouseMoved( event );
            }
        } );
        column.cellColumn.addMouseListener( new MouseListener.Stub() {

            @Override
            public void mouseDoubleClicked( final MouseEvent event ) {
                canvasMouseListener.mouseDoubleClicked( event );
            }

            @Override
            public void mousePressed( final MouseEvent event ) {
                if ( focusLine.containsPoint( event.getLocation() ) ) focusLineMouseListener.mousePressed( event );
            }

            @Override
            public void mouseReleased( final MouseEvent event ) {
                canvasMouseListener.mouseReleased( event );
                focusLineMouseListener.mouseReleased( event );
            }
        } );
    }

    void removeColumn( final Column column ) {
        canvas.remove( column.backgroundColumn );
        canvas.remove( column.cellColumn );
    }

    private void removeColumnsAfter( final Column column ) {
        focusTree.removeColumnsAfter( column );
        final Rectangle childColumnBounds = column.cellColumn.getBounds();
        canvas.setSize( childColumnBounds.x + childColumnBounds.width, canvas.getSize().height );
    }

    void scrollToFocusLine() {
        scrollToY( focusLine.getLocation().y - focusLineOffset );
        // scrollSmoothTo( getViewport().getClientArea().x, focusLine.getLocation().y - focusLineOffset );
    }

    private void setCellColumnLayoutManager( final Column column ) {
        final GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 20;
        layout.verticalSpacing = 2;
        column.cellColumn.setLayoutManager( layout );
    }

    void setFocusLineHeight( final int height ) {
        focusBorder.setWidth( height );
        noFocusBorder.setWidth( height );
        final Rectangle focusLineBounds = new Rectangle( focusLine.getBounds() );
        focusLineBounds.height = height;
        focusLine.setBounds( focusLineBounds );
    }

    void showColumn( final Column column,
                     final int width ) {
        updateColumnWidth( column, width, true );
        focusTree.focusColumn( column );
    }

    void showIconView( final Column column ) {
        int maxHeight = 0;
        focusLine.setVisible( false );
        for ( final Column col : focusTree.columns ) {
            col.backgroundColumn.setVisible( false );
            if ( col != column ) col.cellColumn.setVisible( false );
        }
        for ( final Object figure : column.cellColumn.getChildren() )
            if ( figure instanceof Cell ) {
                final Cell cell = ( Cell ) figure;
                final Dimension size = cell.getPreferredSize( initialCellWidth, SWT.DEFAULT );
                cell.setPreferredSize( size );
                maxHeight = Math.max( maxHeight, size.height );
            }
        for ( final Object figure : column.cellColumn.getChildren() )
            if ( figure instanceof AddPanel ) {
                final AddPanel addPanel = ( AddPanel ) figure;
                addPanel.setPreferredSize( Dimension.SINGLETON.setSize( addPanel.getPreferredSize().height, maxHeight ) );
            }
        column.cellColumn.setLayoutManager( new FlowLayout() );
        final Dimension size = column.cellColumn.getPreferredSize( getViewport().getClientArea().width, SWT.DEFAULT );
        column.cellColumn.setBounds( Rectangle.SINGLETON.setBounds( 0, 0, size.width, size.height ) );
        canvas.setSize( size );
    }

    private void updateBounds() {
        int minY = Short.MAX_VALUE;
        int maxY = 0;
        for ( final Column column : focusTree.columns ) {
            // // jpav: remove
            // System.out.println( "column: " + column.item );
            final Rectangle cellColumnBounds = column.cellColumn.getBounds();
            // // jpav: remove
            // System.out.println( "cell column bounds: " + cellColumnBounds );
            minY = Math.min( minY, cellColumnBounds.y );
            // // jpav: remove
            // System.out.println( "minY: " + minY );
            maxY = Math.max( maxY, cellColumnBounds.y + cellColumnBounds.height );
            // // jpav: remove
            // System.out.println( "maxY: " + maxY );
        }
        int newCanvasHeight = maxY - minY;
        // // jpav: remove
        // System.out.println( "new canvas height: " + newCanvasHeight );
        final Rectangle focusLineBounds = new Rectangle( focusLine.getBounds() );
        // // jpav: remove
        // System.out.println( "focus line bounds: " + focusLineBounds );
        final int topMargin = focusLineOffset - ( focusLineBounds.y - minY );
        // // jpav: remove
        // System.out.println( "top margin: " + topMargin );
        if ( topMargin > 0 ) {
            newCanvasHeight += topMargin;
            // // jpav: remove
            // System.out.println( "new canvas height: " + newCanvasHeight );
            minY -= topMargin;
            // // jpav: remove
            // System.out.println( "minY: " + minY );
        }
        final Rectangle viewBounds = getViewport().getClientArea();
        // // jpav: remove
        // System.out.println( "view bounds: " + viewBounds );
        final int bottomMargin = focusLineBounds.y + viewBounds.height - focusLineOffset - maxY;
        // // jpav: remove
        // System.out.println( "bottom margin: " + bottomMargin );
        if ( bottomMargin > 0 ) {
            newCanvasHeight += bottomMargin;
            // // jpav: remove
            // System.out.println( "new canvas height: " + newCanvasHeight );
        }
        final Rectangle canvasBounds = new Rectangle( canvas.getBounds() );
        // // jpav: remove
        // System.out.println( "canvas bounds: " + canvasBounds );
        if ( newCanvasHeight != canvasBounds.height ) {
            canvasBounds.height = newCanvasHeight;
            canvas.setBounds( canvasBounds );
            // // jpav: remove
            // System.out.println( "canvas bounds: " + canvasBounds );
            for ( final Column column : focusTree.columns ) {
                // // jpav: remove
                // System.out.println( "column: " + column.item );
                final Rectangle cellColumnBounds = column.cellColumn.getBounds();
                // // jpav: remove
                // System.out.println( "cell column bounds: " + cellColumnBounds );
                column.backgroundColumn.setBounds( new Rectangle( cellColumnBounds.x, 0, cellColumnBounds.width, canvasBounds.height ) );
                // // jpav: remove
                // System.out.println( "background column bounds: " + column.backgroundColumn.getBounds() );
            }
        }
        if ( minY != 0 ) {
            focusLineBounds.y -= minY;
            focusLine.setBounds( focusLineBounds );
            // // jpav: remove
            // System.out.println( "focus line bounds: " + focusLineBounds );
            for ( final Column column : focusTree.columns ) {
                // // jpav: remove
                // System.out.println( "column: " + column.item );
                final Rectangle cellColumnBounds = new Rectangle( column.cellColumn.getBounds() );
                cellColumnBounds.y -= minY;
                column.cellColumn.setBounds( cellColumnBounds );
                // // jpav: remove
                // System.out.println( "cell column bounds: " + column.cellColumn.getBounds() );
            }
        }
        focusTree.scroller.setMinWidth( canvasBounds.width );
        focusTree.scroller.setOrigin( canvasBounds.width -
                                      focusTree.scroller.getClientArea().width,
                                      0 );
    }

    void updateColumnWidth( final Column column,
                            final int width,
                            final boolean visible ) {
        column.backgroundColumn.setSize( width, column.backgroundColumn.getSize().height );
        column.backgroundColumn.setVisible( visible );
        final Dimension childColumnSize = column.cellColumn.getSize();
        final int delta = width - childColumnSize.width;
        column.cellColumn.setSize( width, childColumnSize.height );
        for ( final Object cell : column.cellColumn.getChildren() )
            ( ( GridData ) column.cellColumn.getLayoutManager().getConstraint( ( IFigure ) cell ) ).widthHint += delta;
        column.cellColumn.setVisible( visible );
        Column previousColumn = null;
        for ( final Column col : focusTree.columns )
            if ( col == column ) previousColumn = col;
            else if ( previousColumn != null ) {
                final Rectangle previousColumnBounds = previousColumn.cellColumn.getBounds();
                final Rectangle childColBounds = new Rectangle( col.cellColumn.getBounds() );
                childColBounds.x = previousColumnBounds.x + previousColumnBounds.width;
                col.cellColumn.setBounds( childColBounds );
                col.cellColumn.revalidate();
                final Rectangle backgroundColBounds = new Rectangle( col.backgroundColumn.getBounds() );
                backgroundColBounds.x = childColBounds.x;
                col.backgroundColumn.setBounds( backgroundColBounds );
                col.backgroundColumn.revalidate();
                previousColumn = col;
            }
        @SuppressWarnings( "null" ) final Rectangle previousColumnBounds = previousColumn.cellColumn.getBounds();
        final Rectangle canvasBounds = new Rectangle( canvas.getBounds() );
        canvasBounds.width = previousColumnBounds.x + previousColumnBounds.width;
        canvas.setBounds( canvasBounds );
        canvas.revalidate();
    }

    private class AddButton extends ImageFigure {

        AddButton( final Image image ) {
            super( image );
        }
    }

    class AddPanel extends Figure {

        int modelIndex;
        int viewIndex;
    }

    class CellColumn extends Figure {

        Column column;
    }

    class DeleteButton extends ImageFigure {

        DeleteButton( final Image image ) {
            super( image );
        }
    }

    class FocusLineMouseListener extends MouseMotionListener.Stub implements MouseListener {

        private int offset;
        boolean dragging;

        @Override
        public void mouseDoubleClicked( final MouseEvent event ) {}

        @Override
        public void mouseDragged( final MouseEvent event ) {
            dragging = true;
            final int delta = event.y - offset - focusLine.getBounds().y;
            focusLineOffset += delta;
            focusLine.translate( 0, delta );
            canvas.repaint();
            for ( final Column column : focusTree.columns )
                focusCell( column, column.focusCell );
            scrollToFocusLine();
        }

        @Override
        public void mousePressed( final MouseEvent event ) {
            offset = event.y - focusLine.getLocation().y;
        }

        @Override
        public void mouseReleased( final MouseEvent event ) {
            dragging = false;
        }
    }

    private class IndicatorButton extends ImageFigure {

        final Indicator indicator;

        IndicatorButton( final Image image,
                         final Indicator indicator ) {
            super( image );
            this.indicator = indicator;
        }
    }

    private class NameField extends Label {

        NameField( final String text ) {
            super( text );
        }
    }

    private interface TextEditorHandler {

        Object commit() throws PolyglotterException;

        String problem();
    }

    private class TypeField extends Label {

        TypeField( final String text ) {
            super( text );
        }
    }

    private class ValueField extends Label {

        ValueField( final String text ) {
            super( text );
        }
    }
}