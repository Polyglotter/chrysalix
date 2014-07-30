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

import org.eclipse.draw2d.Border;
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
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.eclipse.EclipseI18n;
import org.polyglotter.eclipse.Util;
import org.polyglotter.eclipse.focustree.FocusTree.Column;
import org.polyglotter.eclipse.focustree.FocusTree.Indicator;

class FocusTreeCanvas extends FigureCanvas {

    final FocusTree focusTree;
    final FreeformLayer canvas = new FreeformLayer();
    final Figure toolBar = new Figure();
    final Panel focusLine = new Panel();
    int focusLineOffset;
    boolean initialIndexIsOne;
    int initialCellWidth;
    int iconViewCellWidth;
    final LineBorder focusBorder, noFocusBorder;
    final AddButton addButton;
    final Dimension columnMargins;
    final MouseListener canvasMouseListener = new MouseListener.Stub() {

        final AtomicLong lastClicked = new AtomicLong( System.currentTimeMillis() );

        @Override
        public void mouseDoubleClicked( final MouseEvent event ) {
            if ( !leftMouseButtonClicked( event ) ) return;
            mouseDoubleClickedOverCanvas( event );
            lastClicked.set( System.currentTimeMillis() );
        }

        @Override
        public void mouseReleased( final MouseEvent event ) {
            if ( !leftMouseButtonClicked( event ) ) return;
            getDisplay().timerExec( 400, new Runnable() {

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
            // jpav: remove
            System.out.println( "canvas dragged" );
            if ( !leftMouseButtonClicked( event ) ) return;
            // jpav: remove
            System.out.println( "left" );
            final IFigure figure = canvas.findFigureAt( event.x, event.y );
            if ( figure instanceof Cell ) cellDragged( ( Cell ) figure, event );
            // Dragging focus line make cause mouse to move outside of focus line bounds, so the canvas needs to propagate mouse
            // events to the focus line listener
            else if ( focusLineMouseListener.dragging ) focusLineMouseListener.mouseDragged( event );
        }

        @Override
        public void mouseMoved( final MouseEvent event ) {
            mouseMovedOverCanvas( event );
        }
    };
    final FocusLineMouseListener focusLineMouseListener = new FocusLineMouseListener();
    final Map< Object, Object > lastFocusItemByParent = new HashMap<>();
    ImageFigure mouseOverButton;
    CellEditor editor;
    EditorHandler editorHandler;
    Label fieldEdited;
    Column iconViewColumn;

    FocusTreeCanvas( final FocusTree focusTree,
                     final Composite parent,
                     final int style ) {
        super( parent, style );
        this.focusTree = focusTree;
        setContents( canvas );
        getViewport().setContentsTracksHeight( true );
        getViewport().setContentsTracksWidth( true );
        getHorizontalBar().setEnabled( false );
        setHorizontalScrollBarVisibility( NEVER );
        canvas.setOpaque( true );
        canvas.addMouseListener( canvasMouseListener );
        canvas.addMouseMotionListener( canvasMouseMotionListener );
        canvas.addLayoutListener( new LayoutListener.Stub() {

            @Override
            public void postLayout( final IFigure container ) {
                // Make focus line extend horizontally across entire canvas
                final Rectangle bounds = new Rectangle( focusLine.getBounds() );
                bounds.width = canvas.getBounds().width;
                focusLine.setBounds( bounds );
            }
        } );
        toolBar.setLayoutManager( new GridLayout() );
        final ImageFigure collapseAllButton = new ImageFigure( Util.image( "collapseall.gif" ) );
        toolBar.add( collapseAllButton );
        collapseAllButton.setToolTip( new Label( EclipseI18n.focusTreeCollapseAllToolTip.text() ) );
        collapseAllButton.addMouseListener( new MouseListener.Stub() {

            @Override
            public void mouseReleased( final MouseEvent event ) {
                if ( leftMouseButtonClicked( event ) ) collapseAllSelected();
            }
        } );
        final ImageFigure duplicateButton = new ImageFigure( Util.image( "duplicate.gif" ) );
        toolBar.add( duplicateButton );
        duplicateButton.setToolTip( new Label( EclipseI18n.focusTreeDuplicateToolTip.text() ) );
        duplicateButton.addMouseListener( new MouseListener.Stub() {

            @Override
            public void mouseReleased( final MouseEvent event ) {
                if ( leftMouseButtonClicked( event ) ) focusTree.duplicate( focusTree.root );
            }
        } );
        toolBar.setSize( toolBar.getPreferredSize() );
        toolBar.setVisible( false );
        focusLine.setToolTip( new Label( EclipseI18n.focusTreeFocusLineToolTip.text() ) );
        focusLine.setBackgroundColor( focusTree.viewModel.focusLineColor() );
        focusLine.addMouseListener( focusLineMouseListener );
        focusLine.addMouseMotionListener( focusLineMouseListener );
        focusLine.setSize( 0, focusTree.viewModel.focusLineHeight() );
        focusLineOffset = focusTree.viewModel.focusLineOffset();
        addButton = new AddButton( Util.image( "add.gif" ) );
        addButton.setToolTip( new Label( EclipseI18n.focusTreeAddToolTip.text() ) );
        addButton.setSize( addButton.getPreferredSize() );
        columnMargins = new Dimension( addButton.getPreferredSize() ).expand( 2, 2 );
        focusBorder = new LineBorder( focusTree.viewModel.focusCellBorderColor(), focusTree.viewModel.focusLineHeight() ) {

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
        noFocusBorder = new LineBorder( focusTree.viewModel.focusLineHeight() ) {

            @Override
            public void paint( final IFigure figure,
                               final Graphics graphics,
                               final Insets insets ) {}
        };
        initialCellWidth = focusTree.viewModel.initialCellWidth();
        iconViewCellWidth = focusTree.viewModel.iconViewCellWidth();
    }

    Cell addCell( final Object item,
                  final Column column ) throws PolyglotterException {
        // Add new cell for item
        final Cell cell = addCell( item, column, modelIndex( item, column ) );
        // Update indexes
        final List< ? > children = column.cellColumn.getChildren();
        for ( int ndx = cell.index + 1; ndx < children.size(); ndx++ ) {
            final Cell afterCell = ( Cell ) children.get( ndx );
            afterCell.indexField.setText( String.valueOf( Integer.parseInt( afterCell.indexField.getText() ) + 1 ) );
            afterCell.index++;
        }
        // Force layout to get cell location set
        column.cellColumn.getLayoutManager().layout( column.cellColumn );
        getDisplay().asyncExec( new Runnable() {

            @Override
            public void run() {
                changeFocusCell( column, cell );
            }
        } );
        return cell;
    }

    private Cell addCell( final Object item,
                          final Column column,
                          final int index ) {
        // Create cell
        final Cell cell = new Cell( focusTree.viewModel.createCell( item ) );
        column.cellColumn.add( cell, new GridData( SWT.FILL, SWT.DEFAULT, true, false ), index );
        cell.item = item;
        cell.index = index;
        final GridLayout gridLayout = new GridLayout( 3, false );
        gridLayout.marginHeight = gridLayout.marginWidth = 0;
        cell.setLayoutManager( gridLayout );
        cell.setBorder( noFocusBorder );
        cell.setBackgroundColor( focusTree.viewModel.cellBackgroundColor( item ) );
        cell.setToolTip( new Label( EclipseI18n.focusTreeCellToolTip.text() ) );
        // Construct cell
        cell.indexField = new IndexField( String.valueOf( initialIndexIsOne ? index + 1 : index ) );
        cell.add( cell.indexField, new GridData( SWT.LEFT, SWT.TOP, false, false ) );
        cell.indexField.setLabelAlignment( PositionConstants.LEFT );
        cell.indexField.setForegroundColor( focusTree.viewModel.childIndexColor( column.item ) );
        cell.indexField.setToolTip( new Label( EclipseI18n.focusTreeCellIndexToolTip.text() ) );
        final Image image = iconViewShown() ? focusTree.viewModel.iconViewIcon( item ) : focusTree.viewModel.icon( item );
        cell.icon = new ImageFigure();
        cell.add( cell.icon, new GridData( SWT.FILL, SWT.CENTER, true, false ) );
        if ( image != null ) cell.icon.setImage( image );
        final Figure indicators = new Figure();
        cell.add( indicators, new GridData( SWT.LEFT, SWT.TOP, false, true ) );
        indicators.setLayoutManager( new FlowLayout() );
        for ( final Indicator indicator : focusTree.model.indicators( item ) ) {
            final IndicatorButton button = new IndicatorButton( indicator.image, indicator );
            indicators.add( button );
            button.setToolTip( new Label( indicator.toolTip ) );
        }
        final DeleteButton deleteButton = new DeleteButton( Util.image( "delete.png" ) );
        indicators.add( deleteButton );
        cell.deleteButton = deleteButton;
        deleteButton.setToolTip( new Label( EclipseI18n.focusTreeDeleteToolTip.text() ) );
        deleteButton.setVisible( false );
        // Save preferred width as minimum width before adding name, type, and value labels
        cell.setMinimumSize( cell.getPreferredSize() );
        // Add name field
        try {
            if ( focusTree.model.hasName( item ) ) {
                try {
                    cell.nameField = new NameField( focusTree.model.name( item ).toString() );
                } catch ( final PolyglotterException e ) {
                    Util.logError( e, EclipseI18n.focusTreeUnableToGetName, item );
                    cell.nameField = new NameField( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) );
                }
                cell.add( cell.nameField );
                cell.nameField.setTextAlignment( PositionConstants.CENTER );
                cell.nameField.setForegroundColor( focusTree.viewModel.cellForegroundColor( item ) );
                try {
                    cell.nameField.setToolTip( new Label( EclipseI18n.focusTreeCellNameToolTip.text( focusTree.model.qualifiedName( item ) ) ) );
                } catch ( final PolyglotterException e ) {
                    Util.logError( e, EclipseI18n.focusTreeUnableToGetQualifiedName, item );
                    cell.nameField.setToolTip( new Label( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) ) );
                }
                cell.setConstraint( cell.nameField, new GridData( SWT.FILL, SWT.DEFAULT, true, false, 3, 1 ) );
            }
        } catch ( final PolyglotterException e ) {
            Util.logError( e, EclipseI18n.focusTreeUnableToDetermineIfItemHasName, item );
        }
        if ( focusTree.model.hasType( item ) ) {
            // Add type field
            TypeField typeField;
            try {
                final Object type = focusTree.model.type( item );
                typeField = new TypeField( type == null ? null : type.toString() );
            } catch ( final PolyglotterException e ) {
                Util.logError( e, EclipseI18n.focusTreeUnableToGetType, item );
                typeField = new TypeField( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) );
            }
            cell.add( typeField );
            typeField.setTextAlignment( PositionConstants.CENTER );
            typeField.setForegroundColor( focusTree.viewModel.cellForegroundColor( item ) );
            typeField.setToolTip( new Label( EclipseI18n.focusTreeCellTypeToolTip.text() ) );
            cell.setConstraint( typeField, new GridData( SWT.FILL, SWT.DEFAULT, true, false, 3, 1 ) );
        }
        try {
            if ( focusTree.model.hasValue( item ) ) {
                // Add value field
                try {
                    final Object value = focusTree.model.value( item );
                    cell.valueField = new ValueField( value == null ? null : value.toString() );
                } catch ( final PolyglotterException e ) {
                    Util.logError( e, EclipseI18n.focusTreeUnableToGetValue, item );
                    cell.valueField = new ValueField( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) );
                }
                cell.add( cell.valueField );
                cell.valueField.setTextAlignment( PositionConstants.CENTER );
                cell.valueField.setForegroundColor( focusTree.viewModel.cellForegroundColor( item ) );
                cell.valueField.setToolTip( new Label( EclipseI18n.focusTreeCellValueToolTip.text() ) );
                cell.setConstraint( cell.valueField, new GridData( SWT.FILL, SWT.DEFAULT, true, false, 3, 1 ) );
            }
        } catch ( final PolyglotterException e ) {}
        // Make index and spacer labels the same size so icon is centered
        final int width = Math.max( cell.indexField.getPreferredSize().width, indicators.getPreferredSize().width );
        ( ( GridData ) cell.getLayoutManager().getConstraint( cell.indexField ) ).widthHint = width;
        ( ( GridData ) cell.getLayoutManager().getConstraint( indicators ) ).widthHint = width;
        return cell;
    }

    void addColumn( final Column column ) {
        column.cellColumn = new CellColumn();
        column.cellColumn.column = column;
        canvas.add( column.cellColumn );
        setCellColumnLayoutManager( column );

        // Get last focus cell for this column if available
        final Object lastFocusItem = lastFocusItemByParent.get( column.item );
        // Create cells and subsequent add-after buttons
        try {
            int index = 0;
            for ( final Object child : focusTree.model.children( column.item ) ) {
                final Cell cell = addCell( child, column, index++ );
                if ( column.focusCell == null && ( lastFocusItem == null || child.equals( lastFocusItem ) ) ) column.focusCell =
                    cell;
            }
        } catch ( final PolyglotterException e ) {
            Util.logAndShowError( e, EclipseI18n.focusTreeUnableToGetChildren, column.item );
        }
        updateCellPreferredWidth( column );
        // Set preferred width of cells to model value
        for ( final Object child : column.cellColumn.getChildren() ) {
            final Cell cell = ( Cell ) child;
            final GridData gridData = ( GridData ) column.cellColumn.getLayoutManager().getConstraint( cell );
            gridData.widthHint = initialCellWidth;
            column.cellColumn.setConstraint( cell, gridData );
        }
        // Force layout to get cell locations set
        column.cellColumn.getLayoutManager().layout( column.cellColumn );
        // Set bounds
        if ( column.focusCell != null ) column.focusCell.setSize( column.focusCell.getPreferredSize() );
        if ( focusTree.columns.size() == 1 ) column.bounds.x = 0;
        else {
            final Column previousColumn = focusTree.columns.get( focusTree.columns.size() - 2 );
            column.bounds.x = previousColumn.bounds.x + previousColumn.bounds.width;
        }
        final Dimension cellColumnSize = column.cellColumn.getPreferredSize();
        column.cellColumn.setBounds( new Rectangle( column.bounds.x + columnMargins.width, 0, cellColumnSize.width, cellColumnSize.height ) );
        column.bounds.width = cellColumnSize.width + columnMargins.width * 2;
        final int canvasWidth = column.bounds.x + column.bounds.width;
        focusLine.setSize( canvasWidth, focusLine.getSize().height );
        canvas.setSize( canvasWidth, canvas.getSize().height );

        // Wire cell column to show focus line tool tip on mouse-over
        // Note, the cell column figures are transparent, but overlap the focus line and canvas, so mouse events need to be
        // propagated to these other figures whenever they are also listening for the same event types
        propagateEvents( column );

        // Focus on first cell
        focusCell( column, column.focusCell );
    }

    void addItem() {
        final Column column = addButton.column;
        try {
            final Object item = focusTree.model.add( column.item, addButton.index );
            if ( item == null )
                throw new PolyglotterException( EclipseI18n.focusTreeNullReturnedFromCreate, column.item, addButton.index );
            final Cell cell = addCell( item, column );
            updateCellPreferredWidth( column );
            if ( focusTree.model.nameEditable( cell.item ) ) {
                getDisplay().asyncExec( new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if ( focusTree.model.hasName( cell.item ) ) editNameField( cell );
                            else editValueField( cell );
                        } catch ( final PolyglotterException e ) {}
                    }
                } );
            }
        } catch ( final PolyglotterException e ) {
            Util.logAndShowError( e, EclipseI18n.focusTreeUnableToCreateItem, column.item );
        }
    }

    Cell cell( IFigure figure ) {
        while ( figure != canvas && !( figure instanceof Cell ) )
            figure = figure.getParent();
        return figure == canvas ? null : ( Cell ) figure;
    }

    void cellDragged( final Cell cell,
                      final MouseEvent event ) {
        // jpav: remove
        System.out.println( "drag" );
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
        scrollToFocusLine();
    }

    void collapseAllSelected() {
        final Column firstColumn = focusTree.columns.get( 0 );
        if ( firstColumn.focusCellExpanded ) changeFocusCell( firstColumn, firstColumn.focusCell );
        lastFocusItemByParent.clear();
    }

    Column column( IFigure figure ) {
        while ( !( figure instanceof CellColumn ) )
            figure = figure.getParent();
        return ( ( CellColumn ) figure ).column;
    }

    private void deleteCell( final Cell cell ) {
        final CellColumn cellColumn = ( CellColumn ) cell.getParent();
        cellColumn.remove( cell );
        // Update indexes
        final List< ? > children = cellColumn.getChildren();
        for ( int ndx = cell.index; ndx < children.size(); ndx++ ) {
            final Cell afterCell = ( Cell ) children.get( ndx );
            afterCell.index--;
            afterCell.indexField.setText( String.valueOf( Integer.parseInt( afterCell.indexField.getText() ) - 1 ) );
        }
        // Focus on next cell, or previous cell if last cell was deleted
        cellColumn.getLayoutManager().layout( cellColumn );
        if ( children.size() > 1 ) {
            final Cell newCell = ( Cell ) children.get( Math.min( cell.index + 1, children.size() - 1 ) );
            addPaintListener( new PaintListener() {

                @Override
                public void paintControl( final PaintEvent e ) {
                    changeFocusCell( cellColumn.column, newCell );
                    removePaintListener( this );
                }
            } );
            changeFocusCell( cellColumn.column, newCell );
        }
        updateCellPreferredWidth( cellColumn.column );
    }

    private void edit( final Label fieldEdited,
                       final CellEditor editor,
                       final EditorHandler editorHandler ) {
        this.editor = editor;
        this.editorHandler = editorHandler;
        this.fieldEdited = fieldEdited;
        if ( editor.getControl() == null ) editor.create( this );
        final Cell cell = cell( fieldEdited );
        focusCell( column( cell ), cell );
        getDisplay().asyncExec( new Runnable() {

            @Override
            public void run() {
                editor.setValue( fieldEdited.getText() );
                final Rectangle fieldBounds = fieldEdited.getBounds();
                final Point viewLocation = getViewport().getViewLocation();
                editor.getControl().setBounds( fieldBounds.x - viewLocation.x,
                                               fieldBounds.y - viewLocation.y,
                                               fieldBounds.width,
                                               editor.getControl().computeSize( SWT.DEFAULT, SWT.DEFAULT ).y );
                editor.setValidator( new ICellEditorValidator() {

                    private final Color originalForegroundColor = editor.getControl().getForeground();

                    @Override
                    public String isValid( final Object value ) {
                        final String problem = editorHandler.problem();
                        if ( problem == null ) {
                            editor.getControl().setToolTipText( null );
                            editor.getControl().setForeground( originalForegroundColor );
                        } else {
                            editor.getControl().setToolTipText( problem );
                            editor.getControl().setForeground( getDisplay().getSystemColor( SWT.COLOR_RED ) );
                        }
                        return problem;
                    }
                } );
                editor.addListener( new ICellEditorListener() {

                    @Override
                    public void applyEditorValue() {
                        endEdit();
                    }

                    @Override
                    public void cancelEditor() {
                        endEdit();
                    }

                    @Override
                    public void editorValueChanged( final boolean oldValidState,
                                                    final boolean newValidState ) {}
                } );
                editor.getControl().setVisible( true );
                editor.setFocus();
                editor.performSelectAll();
            }
        } );
    }

    void editNameField( final Cell cell ) {
        edit( cell.nameField, focusTree.viewModel.nameEditor( cell.item ), new EditorHandler() {

            @Override
            public Object commit() throws PolyglotterException {
                final Object item = focusTree.model.setName( cell.item, editor.getValue().toString() );
                if ( item == null )
                    throw new PolyglotterException( EclipseI18n.focusTreeNullReturnedFromSetName, cell.item );
                return item;
            }

            @Override
            public String problem() {
                return focusTree.model.nameProblem( cell.item,
                                                    editor.getValue() == null ? null : editor.getValue().toString() );
            }
        } );
    }

    void editValueField( final Cell cell ) {
        edit( cell.valueField, focusTree.viewModel.valueEditor( cell.item ), new EditorHandler() {

            @Override
            public Object commit() throws PolyglotterException {
                final Object value = editor.getValue();
                final Object item = focusTree.model.setValue( cell.item, value == null ? null : value.toString() );
                if ( item == null )
                    throw new PolyglotterException( EclipseI18n.focusTreeNullReturnedFromSetValue, cell.item );
                return item;
            }

            @Override
            public String problem() {
                return focusTree.model.valueProblem( cell.item,
                                                     editor.getValue() == null ? null : editor.getValue().toString() );
            }
        } );
    }

    void endEdit() {
        if ( editor == null ) return;
        if ( editor.isValueValid() )
            try {
                final Object item = editorHandler.commit();
                final Cell cell = cell( fieldEdited );
                final Column column = column( cell );
                if ( cell.item != item ) {
                    deleteCell( cell );
                    addCell( item, column );
                } else {
                    fieldEdited.setText( editor.getValue() == null ? null : editor.getValue().toString() );
                    if ( fieldEdited instanceof NameField ) {
                        final Label toolTip = ( Label ) fieldEdited.getToolTip();
                        try {
                            toolTip.setText( EclipseI18n.focusTreeCellNameToolTip.text( focusTree.model.qualifiedName( item ) ) );
                        } catch ( final PolyglotterException e ) {
                            Util.logError( e, EclipseI18n.focusTreeUnableToGetQualifiedName, item );
                            toolTip.setText( EclipseI18n.focusTreeErrorText.text( e.getMessage() ) );
                        }
                    }
                    column.cellColumn.getLayoutManager().layout( column.cellColumn );
                    addPaintListener( new PaintListener() {

                        @Override
                        public void paintControl( final PaintEvent e ) {
                            changeFocusCell( column, cell );
                            removePaintListener( this );
                        }
                    } );
                }
            } catch ( final PolyglotterException e ) {
                Util.logAndShowError( e, EclipseI18n.focusTreeUnableToCommitChanges, cell( fieldEdited ).item );
            }
        editor.deactivate();
        editor = null;
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
        Util.logContextTrace();
        // Collapse previous focus cell and give it a no-focus border
        if ( column.focusCell != null ) column.focusCell.setBorder( noFocusBorder );

        // Set new focus cell and child
        column.focusCell = focusCell;
        if ( focusCell != null ) {
            lastFocusItemByParent.put( column.item, focusCell.item );
            focusCell.setBorder( focusBorder );
        }
        final Point cellColumnLocation = column.cellColumn.getLocation();
        Util.logTrace( "cellColumnLocation: %s", cellColumnLocation );
        final Rectangle focusCellBounds = ( focusCell == null ? new Rectangle() : focusCell.getBounds() );
        Util.logTrace( "focusCellBounds: %s", focusCellBounds );
        final Rectangle focusLineBounds = focusLine.getBounds();
        Util.logTrace( "focusLineBounds: %s", focusLineBounds );
        cellColumnLocation.y -= focusCellBounds.y + focusCellBounds.height / 2 - focusLineBounds.y - focusLineBounds.height / 2;
        column.cellColumn.setLocation( cellColumnLocation );
        Util.logTrace( "cellColumnLocation: %s", cellColumnLocation );
        updateBounds();
    }

    void hideColumn( final Column column,
                     final int columnWidth ) {
        updateColumnWidth( column, columnWidth, true );
    }

    void hideIconView( final Column column ) {
        iconViewColumn = null;
        column.cellColumn.setBorder( null );
        focusLine.setVisible( true );
        for ( final Column col : focusTree.columns ) {
            col.cellColumn.setVisible( true );
        }
        setCellColumnLayoutManager( column );
        for ( final Object child : column.cellColumn.getChildren() ) {
            final Cell cell = ( Cell ) child;
            cell.setPreferredSize( null );
            cell.icon.setImage( focusTree.viewModel.icon( cell.item ) );
            final GridData gridData = new GridData( SWT.FILL, SWT.DEFAULT, false, false );
            gridData.widthHint = column.cellWidthBeforeIconView;
            column.cellColumn.setConstraint( cell, gridData );
        }
        column.cellColumn.setLocation( new Point( column.bounds.x + columnMargins.width, 0 ) );
        column.cellColumn.setSize( column.cellColumn.getPreferredSize() );
        focusCell( column, column.focusCell );
    }

    private void hideMouseOverButton() {
        if ( mouseOverButton != null ) {
            mouseOverButton.setVisible( false );
            mouseOverButton = null;
        }
    }

    boolean iconViewShown() {
        return iconViewColumn != null;
    }

    boolean leftMouseButtonClicked( final MouseEvent event ) {
        return event.button == 1 && ( event.getState() & SWT.MODIFIER_MASK ) == 0;
    }

    void modelChanged() {
        canvas.removeAll();
        canvas.add( toolBar );
        canvas.add( focusLine );
    }

    int modelIndex( final Object item,
                    final Column column ) throws PolyglotterException {
        // Find actual index of item in model
        int index = 0;
        for ( final Object child : focusTree.model.children( column.item ) ) {
            if ( child.equals( item ) ) return index;
            index++;
        }
        throw new PolyglotterException( EclipseI18n.focusTreeItemNotFound, item, column.item );
    }

    void mouseClickedOverCanvas( final MouseEvent event ) {
        if ( editor != null ) endEdit();
        final IFigure figure = canvas.findFigureAt( event.x, event.y );
        if ( figure instanceof AddButton ) addItem();
        else if ( figure instanceof DeleteButton ) {
            final Cell cell = cell( figure );
            String name;
            try {
                name = EclipseI18n.deleteConfirmationMessage.text( focusTree.model.name( cell.item ) );
            } catch ( final Exception e ) {
                name = EclipseI18n.focusTreeErrorText.text( e.getMessage() );
            }
            if ( MessageDialog.openConfirm( getShell(), EclipseI18n.confirmDialogTitle.text(), name )
                 && focusTree.model.delete( cell.item ) ) deleteCell( cell );
        } else if ( figure instanceof IndicatorButton ) {
            ( ( IndicatorButton ) figure ).indicator.selected( cell( figure ).item );
        } else {
            final Cell cell = cell( figure );
            if ( cell != null ) changeFocusCell( column( cell ), cell );
        }
    }

    void mouseDoubleClickedOverCanvas( final MouseEvent event ) {
        if ( editor != null ) endEdit();
        final IFigure figure = canvas.findFigureAt( event.x, event.y );
        final Cell cell = cell( figure );
        if ( figure instanceof IndexField ) {
            if ( focusTree.model.movable( cell.item ) ) {
                edit( ( IndexField ) figure, focusTree.viewModel.indexEditor( cell.item ), new EditorHandler() {

                    @Override
                    public Object commit() throws PolyglotterException {
                        final Object value = editor.getValue();
                        if ( value == null ) return cell.item;
                        final int index = Integer.parseInt( value.toString() );
                        final Column column = column( cell );
                        final Object item = focusTree.model.setIndex( cell.item, column.item, index );
                        if ( item == null )
                            throw new PolyglotterException( EclipseI18n.focusTreeNullReturnedFromSetType, cell.item );
                        int actualIndex = focusTree.model.indexOf( item, column.item );
                        if ( focusTree.viewModel.initialIndexIsOne() ) actualIndex++;
                        if ( index != actualIndex ) editor.setValue( Integer.valueOf( actualIndex ).toString() );
                        return item;
                    }

                    @Override
                    public String problem() {
                        return focusTree.model.typeProblem( cell.item,
                                                            editor.getValue() == null ? null : editor.getValue().toString() );
                    }
                } );
                return;
            }
        } else if ( figure instanceof NameField ) {
            if ( focusTree.model.nameEditable( cell.item ) ) {
                editNameField( cell );
                return;
            }
        } else if ( figure instanceof TypeField ) {
            if ( focusTree.model.typeEditable( cell.item ) ) {
                edit( ( TypeField ) figure, focusTree.viewModel.typeEditor( cell.item ), new EditorHandler() {

                    @Override
                    public Object commit() throws PolyglotterException {
                        final Object type = editor.getValue();
                        final Object item = focusTree.model.setType( cell.item, type == null ? null : type.toString() );
                        if ( item == null )
                            throw new PolyglotterException( EclipseI18n.focusTreeNullReturnedFromSetType, cell.item );
                        return item;
                    }

                    @Override
                    public String problem() {
                        return focusTree.model.typeProblem( cell.item,
                                                            editor.getValue() == null ? null : editor.getValue().toString() );
                    }
                } );
                return;
            }
        } else if ( figure instanceof ValueField ) {
            if ( focusTree.model.valueEditable( cell.item ) ) {
                editValueField( cell );
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
        final IFigure figure = canvas.findFigureAt( event.x, event.y );
        if ( figure instanceof CellColumn ) {
            addButton.column = column( figure );
            if ( focusTree.model.childrenAddable( addButton.column.item ) ) {
                Cell lastCell = null;
                Cell cell = null;
                final Dimension addButtonSize = addButton.getSize();
                final Point addButtonLocation = new Point();
                Rectangle cellBounds = new Rectangle();
                if ( iconViewShown() ) {
                    for ( final Object child : figure.getChildren() ) {
                        lastCell = ( Cell ) child;
                        cellBounds = lastCell.getBounds();
                        if ( cellBounds.x > event.x && cellBounds.x - columnMargins.width <= event.x && cellBounds.y <= event.y
                             && cellBounds.y + cellBounds.height > event.y ) {
                            cell = lastCell;
                            break;
                        }
                    }
                    if ( cell == null ) {
                        if ( lastCell == null ) {
                            addButtonLocation.setLocation( 1, 1 );
                            addButton.index = 0;
                        } else if ( cellBounds.x + cellBounds.width <= event.x
                                    && cellBounds.x + cellBounds.width + columnMargins.width > event.x
                                    && cellBounds.y <= event.y && cellBounds.y + cellBounds.height > event.y ) {
                            addButtonLocation.y = cellBounds.y + ( cellBounds.height - addButtonSize.height ) / 2;
                            addButtonLocation.x = cellBounds.x + cellBounds.width + 1;
                            addButton.index = lastCell.index + 1;
                        } else {
                            hideMouseOverButton();
                            return;
                        }
                    } else {
                        addButtonLocation.y = cellBounds.y + ( cellBounds.height - addButtonSize.height ) / 2;
                        addButtonLocation.x = cellBounds.x - addButtonSize.width - 1;
                        addButton.index = cell.index;
                    }
                } else {
                    for ( final Object child : figure.getChildren() ) {
                        lastCell = ( Cell ) child;
                        cellBounds = lastCell.getBounds();
                        if ( cellBounds.y > event.y && cellBounds.y - columnMargins.height <= event.y ) {
                            cell = lastCell;
                            break;
                        }
                    }
                    final Rectangle cellColumnBounds = figure.getBounds();
                    addButtonLocation.x = cellColumnBounds.x + ( cellColumnBounds.width - addButtonSize.width ) / 2;
                    if ( cell == null ) {
                        if ( lastCell == null ) {
                            addButtonLocation.y = 1;
                            addButton.index = 0;
                        } else {
                            addButtonLocation.y = cellBounds.y + cellBounds.height + 1;
                            addButton.index = lastCell.index + 1;
                        }
                    } else {
                        addButtonLocation.y = cellBounds.y - addButtonSize.height - 1;
                        addButton.index = cell.index;
                    }
                }
                if ( mouseOverButton == addButton ) return;
                if ( mouseOverButton != null ) mouseOverButton.setVisible( false );
                addButton.setLocation( addButtonLocation );
                canvas.add( addButton );
                addButton.setVisible( true );
                mouseOverButton = addButton;
                return;
            }
        } else if ( figure instanceof AddButton ) return;
        else {
            final Cell cell = cell( figure );
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
                // jpav: remove
                System.out.println( "cell column dragged" );
                if ( focusLineMouseListener.dragging || focusLine.containsPoint( event.getLocation() ) )
                    focusLineMouseListener.mouseDragged( event );
            }

            @Override
            public void mouseMoved( final MouseEvent event ) {
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
        canvas.remove( column.cellColumn );
    }

    private void removeColumnsAfter( final Column column ) {
        focusTree.removeColumnsAfter( column );
        canvas.setSize( column.bounds.x + column.bounds.width, canvas.getSize().height );
    }

    void scrollToFocusLine() {
        getDisplay().asyncExec( new Runnable() {

            @Override
            public void run() {
                scrollToY( focusLine.getLocation().y - focusLineOffset );
                // scrollSmoothTo( getViewport().getClientArea().x, focusLine.getLocation().y - focusLineOffset );
            }
        } );
    }

    private void setCellColumnLayoutManager( final Column column ) {
        final GridLayout layout = new GridLayout();
        layout.marginHeight = layout.verticalSpacing = columnMargins.height;
        layout.marginWidth = 0;
        column.cellColumn.setLayoutManager( layout );
    }

    void setViewModel() {
        setBackground( focusTree.viewModel.treeBackgroundColor() );
        focusBorder.setColor( focusTree.viewModel.focusCellBorderColor() );
        focusLine.setBackgroundColor( focusTree.viewModel.focusLineColor() );
        focusLineOffset = focusTree.viewModel.focusLineOffset();
        iconViewCellWidth = focusTree.viewModel.iconViewCellWidth();
        initialIndexIsOne = focusTree.viewModel.initialIndexIsOne();
        initialCellWidth = focusTree.viewModel.initialCellWidth();
        final int focusLineHeight = focusTree.viewModel.focusLineHeight();
        focusBorder.setWidth( focusLineHeight );
        noFocusBorder.setWidth( focusLineHeight );
        final Rectangle focusLineBounds = new Rectangle( focusLine.getBounds() );
        focusLineBounds.height = focusLineHeight;
        focusLine.setBounds( focusLineBounds );
    }

    void showColumn( final Column column,
                     final int width ) {
        updateColumnWidth( column, width, true );
        scrollToFocusLine();
    }

    void showIconView( final Column column ) {
        iconViewColumn = column;
        focusLine.setVisible( false );
        for ( final Column col : focusTree.columns ) {
            if ( col != column ) col.cellColumn.setVisible( false );
        }
        column.cellWidthBeforeIconView = 0;
        for ( final Object figure : column.cellColumn.getChildren() ) {
            final Cell cell = ( Cell ) figure;
            if ( column.cellWidthBeforeIconView == 0 ) column.cellWidthBeforeIconView =
                ( ( GridData ) column.cellColumn.getLayoutManager().getConstraint( cell ) ).widthHint;
            cell.icon.setImage( focusTree.viewModel.iconViewIcon( cell.item ) );
            final Dimension size = cell.getPreferredSize( iconViewCellWidth, SWT.DEFAULT );
            cell.setPreferredSize( size );
        }
        final FlowLayout layout = new FlowLayout();
        layout.setMinorAlignment( OrderedLayout.ALIGN_BOTTOMRIGHT );
        layout.setMajorSpacing( columnMargins.width );
        layout.setMinorSpacing( columnMargins.height );
        column.cellColumn.setLayoutManager( layout );
        column.cellColumn.setBorder( new MarginBorder( columnMargins.height, columnMargins.width,
                                                       columnMargins.height, columnMargins.width ) );
        updateIconViewBounds();
    }

    void updateBounds() {
        Util.logContextTrace();
        int minY = Short.MAX_VALUE;
        int maxY = 0;
        int canvasWidth = 0;
        for ( final Column column : focusTree.columns ) {
            final Rectangle cellColumnBounds = column.cellColumn.getBounds();
            Util.logTrace( "cellColumnBounds: %s", cellColumnBounds );
            canvasWidth += column.bounds.width;
            minY = Math.min( minY, cellColumnBounds.y );
            maxY = Math.max( maxY, cellColumnBounds.y + cellColumnBounds.height );
        }
        Util.logTrace( "canvasWidth: %d", canvasWidth );
        Util.logTrace( "minY: %d", minY );
        Util.logTrace( "maxY: %d", maxY );
        int canvasHeight = maxY - minY;
        Util.logTrace( "canvasHeight: %d", canvasHeight );
        final Point focusLineLocation = focusLine.getLocation();
        Util.logTrace( "focusLineLocation: %s", focusLineLocation );
        final int topMargin = focusLineOffset - ( focusLineLocation.y - minY );
        Util.logTrace( "topMargin: %d", topMargin );
        if ( topMargin > 0 ) {
            canvasHeight += topMargin;
            Util.logTrace( "canvasHeight: %d", canvasHeight );
            minY -= topMargin;
            Util.logTrace( "minY: %d", minY );
        }
        final int bottomMargin = focusLineLocation.y + getViewport().getClientArea().height - focusLineOffset - maxY;
        Util.logTrace( "bottomMargin: %d", bottomMargin );
        if ( bottomMargin > 0 ) {
            canvasHeight += bottomMargin;
            Util.logTrace( "canvasHeight: %d", canvasHeight );
        }
        final Dimension canvasBounds = canvas.getSize();
        Util.logTrace( "canvasBounds: %s", canvasBounds );
        if ( canvasHeight != canvasBounds.height ) {
            canvasBounds.height = canvasHeight;
            canvas.setSize( canvasBounds );
            Util.logTrace( "canvasBounds: %s", canvasBounds );
            for ( final Column column : focusTree.columns ) {
                column.bounds.height = canvasBounds.height;
                Util.logTrace( "column.bounds: %s", column.bounds );
            }
        }
        if ( minY != 0 ) {
            focusLineLocation.y -= minY;
            focusLine.setLocation( focusLineLocation );
            Util.logTrace( "focusLineLocation: %s", focusLineLocation );
            for ( final Column column : focusTree.columns ) {
                final Point cellColumnLocation = column.cellColumn.getLocation();
                cellColumnLocation.y -= minY;
                column.cellColumn.setLocation( cellColumnLocation );
                Util.logTrace( "cellColumnLocation: %s", cellColumnLocation );
            }
        }
        if ( canvasBounds.width > canvasWidth ) {
            canvasBounds.width = canvasWidth;
            canvas.setSize( canvasBounds );
            Util.logTrace( "canvasBounds: %s", canvasBounds );
        }
        focusTree.scroller.setMinWidth( canvasBounds.width );
        focusTree.scroller.setOrigin( canvasBounds.width - focusTree.scroller.getClientArea().width, 0 );
    }

    private void updateCellPreferredWidth( final Column column ) {
        column.cellPreferredWidth = column.cellColumn.getPreferredSize().width;
    }

    void updateColumnWidth( final Column column,
                            final int width,
                            final boolean visible ) {
        final int delta = width - column.bounds.width;
        column.bounds.width = width;
        final Dimension cellColumnSize = column.cellColumn.getSize();
        column.cellColumn.setSize( cellColumnSize.width + delta, cellColumnSize.height );
        for ( final Object cell : column.cellColumn.getChildren() )
            ( ( GridData ) column.cellColumn.getLayoutManager().getConstraint( ( IFigure ) cell ) ).widthHint += delta;
        column.cellColumn.setVisible( visible );
        boolean afterColumn = false;
        for ( final Column col : focusTree.columns )
            if ( col == column ) afterColumn = true;
            else if ( afterColumn ) {
                final Point cellColumnLocation = col.cellColumn.getLocation();
                cellColumnLocation.x += delta;
                col.cellColumn.setLocation( cellColumnLocation );
                col.cellColumn.revalidate();
                col.bounds.x += delta;
            }
        final Dimension canvasSize = canvas.getSize();
        canvas.setSize( canvasSize.width + delta, canvasSize.height );
        canvas.revalidate();
    }

    void updateIconViewBounds() {
        final Dimension size = iconViewColumn.cellColumn.getPreferredSize( getViewport().getClientArea().width, SWT.DEFAULT );
        iconViewColumn.cellColumn.setBounds( Rectangle.SINGLETON.setBounds( 0, 0, size.width, size.height ) );
        canvas.setSize( size );
    }

    private static class AddButton extends ImageFigure {

        int index;
        Column column;

        AddButton( final Image image ) {
            super( image );
        }
    }

    class Cell extends Figure {

        IFigure delegate;
        Object item;
        int index;
        IndexField indexField;
        ImageFigure icon;
        NameField nameField;
        ValueField valueField;
        DeleteButton deleteButton;

        Cell( final IFigure delegate ) {
            this.delegate = delegate;
            final org.eclipse.draw2d.GridLayout layout = new org.eclipse.draw2d.GridLayout();
            layout.marginHeight = layout.marginWidth = 0;
            super.setLayoutManager( layout );
            super.add( delegate, null, 0 );
            super.setConstraint( delegate, new org.eclipse.draw2d.GridData( SWT.FILL, SWT.FILL, true, true ) );
        }

        @Override
        public void add( final IFigure figure,
                         final Object constraint,
                         final int index ) {
            delegate.add( figure, constraint, index );
        }

        @Override
        public LayoutManager getLayoutManager() {
            return delegate.getLayoutManager();
        }

        @Override
        public void setBackgroundColor( final Color bg ) {
            delegate.setBackgroundColor( bg );
        }

        @Override
        public void setBorder( final Border border ) {
            delegate.setBorder( border );
        }

        @Override
        public void setConstraint( final IFigure child,
                                   final Object constraint ) {
            delegate.setConstraint( child, constraint );
        }

        @Override
        public void setLayoutManager( final LayoutManager manager ) {
            delegate.setLayoutManager( manager );
        }

        @Override
        public void setToolTip( final IFigure f ) {
            delegate.setToolTip( f );
        }
    }

    class CellColumn extends Figure {

        Column column;
    }

    class DeleteButton extends ImageFigure {

        DeleteButton( final Image image ) {
            super( image );
        }
    }

    private interface EditorHandler {

        Object commit() throws PolyglotterException;

        String problem();
    }

    class FocusLineMouseListener extends MouseMotionListener.Stub implements MouseListener {

        private int offset;
        boolean dragging;

        @Override
        public void mouseDoubleClicked( final MouseEvent event ) {}

        @Override
        public void mouseDragged( final MouseEvent event ) {
            // jpav: remove
            System.out.println( "focus drag" );
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

    static class IndexField extends Label {

        IndexField( final String text ) {
            super( text );
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

    static class NameField extends Label {

        NameField( final String text ) {
            super( text );
        }
    }

    private class TypeField extends Label {

        TypeField( final String text ) {
            super( text );
        }
    }

    static class ValueField extends Label {

        ValueField( final String text ) {
            super( text );
        }
    }
}