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
package org.modeshape.modeler.ui;

import java.util.ArrayList;
import java.util.List;

import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.ui.FocusTreeView.Column;
import org.modeshape.modeler.ui.FocusTreeView.Column.Header;
import org.modeshape.modeler.ui.FocusTreeView.Preferences;
import org.chrysalix.common.CheckArg;

/**
 * 
 */
public final class FocusTreeController {

    private FocusTreeModel model;
    private FocusTreeView view;
    private Object root;
    final List< Column > columns = new ArrayList<>();
    private boolean iconViewShown;

    /**
     * @param model
     *        this controller's model; must not be <code>null</code>.
     */
    public FocusTreeController( final FocusTreeModel model ) {
        this( model, null );
    }

    /**
     * @param model
     *        this controller's model; must not be <code>null</code>.
     * @param root
     *        the root node of this controller's model; may be <code>null</code>.
     */
    public FocusTreeController( final FocusTreeModel model,
                                final Object root ) {
        CheckArg.notNull( model, "model" );
        this.model = model;
        this.root = root;
    }

    private void addColumn( final Object item ) {
        // Add a column
        final Column column = view.newColumn();
        columns.add( column );
        column.setItem( item );
        // Add a path button
        final Preferences prefs = view.preferences();
        String name;
        try {
            name = model.name( item ).toString();
        } catch ( final ModelerException e ) {
            UiUtil.logError( e, UiI18n.focusTreeUnableToGetName, item );
            name = "<?>";
        }
        String qualifiedName;
        try {
            qualifiedName = model.qualifiedName( item ).toString();
        } catch ( final ModelerException e ) {
            UiUtil.logError( e, UiI18n.focusTreeUnableToGetQualifiedName, item );
            qualifiedName = name;
        }
        column.setPathButton( column.constructPathButton( name, UiI18n.focusTreePathButtonDescription.text( qualifiedName ),
                                                          prefs.pathButtonBackgroundColor( item ),
                                                          prefs.pathButtonForegroundColor( item ) ) );
        // Add a column header
        final Header header = column.constructHeader( UiI18n.focusTreeColumnHeaderDescription.text() );
        column.setHeader( header );
        // Add a child count to header
        String count;
        try {
            count = String.valueOf( model.childCount( item ) );
        } catch ( final ModelerException e ) {
            UiUtil.logError( e, UiI18n.focusTreeUnableToGetChildCount, item );
            count = "<?>";
        }
        header.constructChildCountUi( count, UiI18n.focusTreeColumnChildCountDescription.text() );
        header.hideChildCountUi();
        // Add a column name to header
        header.constructNameUi( name, UiI18n.focusTreeColumnNameDescription.text( qualifiedName ) );
        // Add a column hide button to header
        header.constructHideUi( UiI18n.focusTreeColumnHideDescription.text(), UiUtil.imageUrl( "close.gif" ) );
        header.hideHideUi();
    }

    /**
     * Copy to current path to the clipboard
     */
    public void closeTree() {
        view.close();
    }

    /**
     * Called by the {@link FocusTreeView} to collapse all columns
     */
    public void collapseAllColumns() {
        view.collapseAllColumns();
    }

    /**
     * Copy to current path to the clipboard
     */
    public void copyPath() {
        view.copyPath();
    }

    /**
     * Called by the {@link FocusTreeView} to duplicate the tree
     */
    public void duplicateTree() {
        view.duplicateTree();
    }

    /**
     * 
     */
    public void pathBarResized() {
        view.pathBarResized();
    }

    /**
     * @return the root node of this controller's model
     */
    public Object root() {
        return root;
    }

    /**
     * 
     */
    public void scrollPathBarLeft() {
        view.scrollPathBarLeft();
    }

    /**
     * 
     */
    public void scrollPathBarRight() {
        view.scrollPathBarRight();
    }

    /**
     * @param model
     *        this controller's model; must not be <code>null</code>
     */
    public void setModel( final FocusTreeModel model ) {
        CheckArg.notNull( model, "model" );
        this.model = model;
    }

    /**
     * @param root
     *        the root Item to show in this controller's view; must not be <code>null</code>
     */
    public void setRoot( final Object root ) {
        CheckArg.notNull( root, "root" );
        this.root = root;
        if ( view != null ) addColumn( root );
    }

    /**
     * @param view
     *        this controller's view; must not be <code>null</code>
     */
    public void setView( final FocusTreeView view ) {
        CheckArg.notNull( view, "view" );
        if ( this.view != view ) {
            final Preferences prefs = view.preferences();
            view.constructCollapseAllColumnsAction( UiI18n.focusTreeCollapseAllColumns.text(),
                                                    UiI18n.focusTreeCollapseAllColumnsDescription.text() );
            view.disableCollapseAllColumnsAction();
            view.constructDuplicateTreeAction( UiI18n.focusTreeDuplicateTree.text(),
                                               UiI18n.focusTreeDuplicateTreeDescription.text() );
            view.disableDuplicateTreeAction();
            view.constructToggleIconViewAction( UiI18n.focusTreeShowIconView.text() );
            view.disableToggleIconViewAction();
            view.constructCopyPathAction( UiI18n.focusTreeCopyPath.text(), UiI18n.focusTreeCopyPathDescription.text(),
                                          UiUtil.imageUrl( "copy.gif" ) );
            view.disableCopyPathAction();
            view.constructScrollPathBarLeftAction( UiI18n.focusTreeScrollPathBarLeftDescription.text(),
                                                   UiUtil.imageUrl( "scrollLeft.png" ) );
            view.hideScrollPathBarLeft();
            view.constructPathButtonBar();
            view.constructScrollPathBarRightAction( UiI18n.focusTreeScrollPathBarRightDescription.text(),
                                                    UiUtil.imageUrl( "scrollRight.png" ) );
            view.hideScrollPathBarRight();
            view.constructCanvas( prefs.treeBackgroundColor() );
            view.constructFocusLine( UiI18n.focusTreeFocusLineDescription.text(),
                                     prefs.focusLineColor(), prefs.focusLineOffset(), prefs.focusLineHeight() );
        }
        this.view = view;
        if ( root != null ) addColumn( root );
    }

    /**
     * 
     */
    public void toggleIconView() {
        iconViewShown = !iconViewShown;
        view.toggleIconView( iconViewShown ? UiI18n.focusTreeHideIconView.text() : UiI18n.focusTreeShowIconView.text() );
    }

    /**
     * 
     */
    public void treeResized() {
        view.treeResized();
    }
}
