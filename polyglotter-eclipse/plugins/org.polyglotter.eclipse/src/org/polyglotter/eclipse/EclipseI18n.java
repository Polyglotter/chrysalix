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

import org.polyglotter.common.I18n;

/**
 * The internationalized string constants for the <code>org.polyglotter.eclipse</code> packages.
 */
@SuppressWarnings( "javadoc" )
public final class EclipseI18n {

    // Tool tip suffixes
    public static final I18n focusTreeHeaderToolTip = new I18n( "Double-click to toggle icon view for this column" );
    private static final I18n focusTreeReopenToolTipSuffix = new I18n( " (Re-open via context (i.e., right-click) menu)" );

    // Tool tips
    public static final I18n focusTreeAddToolTip = new I18n( "Add a new cell here" );
    public static final I18n focusTreeCellIndexToolTip = new I18n( "This item's index.\n\n" +
                                                                   "Click to move this cell to the focus line.\n" +
                                                                   "Double-click to change the type if editable or,\n " +
                                                                   "if not, open a new tree with this item as the root." );
    public static final I18n focusTreeCellNameToolTip = new I18n( "%s\n\n" +
                                                                  "This item's name.\n\n" +
                                                                  "Click to move this cell to the focus line.\n" +
                                                                  "Double-click to change the name if editable or,\n " +
                                                                  "if not, open a new tree with this item as the root." );
    public static final I18n focusTreeCellToolTip = new I18n( "Click to move this cell to the focus line.\n" +
                                                              "Double-click to open a new tree with this item as the root." );
    public static final I18n focusTreeCellTypeToolTip = new I18n( "This item's type.\n\n" +
                                                                  "Click to move this cell to the focus line.\n" +
                                                                  "Double-click to change the value if editable or,\n " +
                                                                  "if not, open a new tree with this item as the root." );
    public static final I18n focusTreeCellValueToolTip = new I18n( "This item's value.\n\n" +
                                                                   "Click to move this cell to the focus line.\n" +
                                                                   "Double-click to change the type." );
    public static final I18n focusTreeChildCountToolTip =
        new I18n( "The number of cells in this column.\n" + focusTreeHeaderToolTip );
    public static final I18n focusTreeCloseTreeToolTip = new I18n( "Close this tree" );
    public static final I18n focusTreeCollapseAllToolTip = new I18n( "Collapse all columns below the root column" );
    public static final I18n focusTreeCopyPathToolTip =
        new I18n( "Copy the path shown in the path bar as slash-delimited text to the clipboard" );
    public static final I18n focusTreeDeleteToolTip = new I18n( "Delete this cell" );
    public static final I18n focusTreeDuplicateToolTip = new I18n( "Duplicate this tree" );
    public static final I18n focusTreeFocusLineToolTip = new I18n( "The focus line.  Can be dragged up and down" );
    public static final I18n focusTreeHideColumnToolTip = new I18n( "Hide this column.\n" + focusTreeHeaderToolTip );
    public static final I18n focusTreeHideToolBarToolTip = new I18n( "Hide this tool bar" + focusTreeReopenToolTipSuffix );
    public static final I18n focusTreeLinkToolTip = new I18n( "Link with selections in other widgets" );
    public static final I18n focusTreeNextPathButtonToolTip = new I18n( "Show next path button" );
    public static final I18n focusTreeParentNameToolTip =
        new I18n( "%s\n\nThe parent of the children in this column.\n" + focusTreeHeaderToolTip );
    public static final I18n focusTreePathButtonToolTip = new I18n( "%s\n\nClick to scroll this column visible" );
    public static final I18n focusTreePreviousPathButtonToolTip = new I18n( "Show previous path button" );
    public static final I18n focusTreeResizeColumnToolTip =
        new I18n( "Change this column's size by dragging.\n" +
                  "Double-click to expand column to show complete text in all cells." );
    public static final I18n focusTreeRotateToolTip = new I18n( "Rotate this tree 90Â°" );
    public static final I18n focusTreeSearchToolTip = new I18n( "Search this tree" );
    public static final I18n focusTreeShowColumnToolTip = new I18n( "Show the \"%s\" column" );
    public static final I18n focusTreeZoomToolTip = new I18n( "Zoom this tree in or out" );

    // Context menu text
    public static final I18n focusTreeCloseTreeMenuItem = new I18n( "Close this tree" );
    public static final I18n focusTreeCollapseAllMenuItem = new I18n( "Collapse all columns" );
    public static final I18n focusTreeCopyPathMenuItem = new I18n( "Copy path" );
    public static final I18n focusTreeDuplicateMenuItem = new I18n( "Duplicate tree" );
    public static final I18n focusTreeHideIconViewMenuItem = new I18n( "Hide icon view" );
    public static final I18n focusTreeShowIconViewMenuItem = new I18n( "Show icon view" );

    // Exceptions
    public static final I18n focusTreeChildCountError = new I18n( "<Error>" );
    public static final I18n focusTreeColumnNotFound = new I18n( "Column not found" );
    public static final I18n focusTreeErrorText = new I18n( "<Error: %s>" );
    public static final I18n focusTreeItemNotFound = new I18n( "Item \"%s\" not found in parent item \"%s\"" );
    public static final I18n focusTreeNullReturnedFromCreate =
        new I18n( "FocusTreeModel.create(<%s>, %d) illegally returned null" );
    public static final I18n focusTreeNullReturnedFromSetName = new I18n( "FocusTreeModel.setName(<%s>) illegally returned null" );
    public static final I18n focusTreeNullReturnedFromSetType = new I18n( "FocusTreeModel.setType(<%s>) illegally returned null" );
    public static final I18n focusTreeNullReturnedFromSetValue = new I18n( "FocusTreeModel.setValue(<%s>) illegally returned null" );
    public static final I18n focusTreeUnableToCommitChanges = new I18n( "Unable to commit changes to item \"%s\"" );
    public static final I18n focusTreeUnableToCreateItem = new I18n( "Unable to create item in column \"%s\"" );
    public static final I18n focusTreeUnableToDetermineIfChildrenAddableOrExist =
        new I18n( "Unable to determine if children or addable or already exist for item \"%s\"" );
    public static final I18n focusTreeUnableToDetermineIfChildrenExist =
        new I18n( "Unable to determine if children exist for item \"%s\"" );
    public static final I18n focusTreeUnableToDetermineIfItemHasName = new I18n( "Unable to determine if item \"%s\" has a name" );
    public static final I18n focusTreeUnableToGetChildren = new I18n( "Unable to get children from item \"%s\"" );
    public static final I18n focusTreeUnableToGetQualifiedName = new I18n( "Unable to get qualified name from item \"%s\"" );
    public static final I18n focusTreeUnableToGetName = new I18n( "Unable to get name from item \"%s\"" );
    public static final I18n focusTreeUnableToGetType = new I18n( "Unable to get type from item \"%s\"" );
    public static final I18n focusTreeUnableToGetValue = new I18n( "Unable to get value from item \"%s\"" );

    public static final I18n confirmDialogTitle = new I18n( "Confirm" );
    public static final I18n deleteConfirmationMessage = new I18n( "Delete \"%s\"?" );
    public static final I18n errorDialogTitle = new I18n( "Error" );

    public static final I18n modelCollectionTypeSuffix = new I18n( " (%s)" );
}