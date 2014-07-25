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
package org.modeshape.modeler.ui;

import java.net.URL;

/**
 * 
 */
public interface FocusTreeView {

    /**
     * 
     */
    void close();

    /**
     * 
     */
    void collapseAllColumns();

    /**
     * @param color
     *        the tree's background color
     * @return the canvas
     */
    Object constructCanvas( Object color );

    /**
     * @param name
     *        the UI's localized name
     * @param description
     *        the UI's localized description
     * @param imageUrl
     *        the UI's icon image
     * @return the UI
     */
    Object constructCloseTreeUi( String name,
                                 String description,
                                 URL imageUrl );

    /**
     * @param name
     *        the UI's localized name
     * @param description
     *        the UI's localized description
     * @return the UI
     */
    Object constructCollapseAllColumnsUi( String name,
                                          String description );

    /**
     * @param name
     *        the UI's localized name
     * @param description
     *        the UI's localized description
     * @param imageUrl
     *        the UI's icon image
     * @return the UI
     */
    Object constructCopyPathUi( String name,
                                String description,
                                URL imageUrl );

    /**
     * @param name
     *        the UI's localized name
     * @param description
     *        the UI's localized description
     * @return the UI
     */
    Object constructDuplicateTreeUi( String name,
                                     String description );

    /**
     * @param description
     *        the focus line's localized description
     * @param color
     *        the focus line's color
     * @param y
     *        the focus line's y value
     * @param height
     *        the focus line's height
     * @return the focus line
     */
    Object constructFocusLine( String description,
                               Object color,
                               int y,
                               int height );

    /**
     * @return the path button bar
     */
    Object constructPathButtonBar();

    /**
     * @param description
     *        the UI's localized description
     * @param imageUrl
     *        the UI's icon image
     * @return the UI
     */
    Object constructScrollPathBarLeftUi( String description,
                                         URL imageUrl );

    /**
     * @param description
     *        the UI's localized description
     * @param imageUrl
     *        the UI's icon image
     * @return the UI
     */
    Object constructScrollPathBarRightUi( String description,
                                          URL imageUrl );

    /**
     * @param name
     *        the UI's localized name
     * @return the UI
     */
    Object constructToggleIconViewUi( String name );

    /**
     * 
     */
    void copyPath();

    /**
     * 
     */
    void disableCollapseAllColumnsUi();

    /**
     * 
     */
    void disableCopyPathUi();

    /**
     * 
     */
    void disableDuplicateTreeUi();

    /**
     * 
     */
    void disableToggleIconViewUi();

    /**
     * 
     */
    void duplicateTree();

    /**
     * 
     */
    void hideScrollPathBarLeft();

    /**
     * 
     */
    void hideScrollPathBarRight();

    /**
     * @return a new column
     */
    Column newColumn();

    /**
     * @param header
     *        a new column header
     * @param description
     *        the action's localized description
     * @param imageUrl
     *        the action's icon image
     * @return a new hide column widget
     */
    Object newHideColumnAction( Object header,
                                String description,
                                URL imageUrl );

    /**
     * @param text
     *        the button's text
     * @param description
     *        the button's localized description
     * @param backgroundColor
     *        the button's background color
     * @param foregroundColor
     *        the button's foreground color
     * @return a new path button
     */
    Object newPathButton( String text,
                          String description,
                          final Object backgroundColor,
                          final Object foregroundColor );

    /**
     * 
     */
    void pathBarResized();

    /**
     * @return the preferences for this view
     */
    Preferences preferences();

    /**
     * 
     */
    void scrollPathBarLeft();

    /**
     * 
     */
    void scrollPathBarRight();

    /**
     * @param text
     *        the text for toggle icon view action
     */
    void toggleIconView( String text );

    /**
     * 
     */
    void treeResized();

    /**
     * 
     */
    class Cell {

        Object cellView;
        Object item;
        int index;

        // IndexField indexField;
        // ImageFigure icon;
        // NameField nameField;
        // ValueField valueField;
        // DeleteButton deleteButton;

        public Cell( final Object cellView ) {
            this.cellView = cellView;
        }
    }

    /**
     * 
     */
    interface Column {

        int cellPreferredWidth();

        /**
         * @param description
         *        the header's localized description
         * @return the header
         */
        Header constructHeader( String description );

        Cell focusCell();

        boolean focusCellExpanded();

        Header header();

        int height();

        Object item();

        Object pathButton();

        void setHeader( Header header );

        void setItem( Object item );

        void setPathButton( Object pathButton );

        int width();

        int widthBeforeHiding();

        int widthBeforeIconView();

        int x();

        int y();

        interface Header {

            Object childCountUi();

            /**
             * @param count
             *        the number of children in the column
             * @param description
             *        the child count UI's localized description
             * @return the UI
             */
            Object constructChildCountUi( String count,
                                          String description );

            /**
             * @param name
             *        the header's name
             * @param description
             *        the header's localized description
             * @return the UI
             */
            Object constructNameUi( String name,
                                    String description );

            /**
             * 
             */
            void hideChildCountUi();

            /**
             * 
             */
            void hideHideUi();

            Object hideUi();

            Object nameUi();
        }
    }

    /**
     * The default preferences for a {@link FocusTreeView}
     */
    abstract class Preferences {

        /**
         * 
         */
        public static final int DEFAULT_FOCUS_LINE_HEIGHT = 5;

        /**
         * 
         */
        public static final int DEFAULT_FOCUS_LINE_OFFSET = 100;

        /**
         * 
         */
        public static final int PREFERRED_WIDTH = -1;

        /**
         * @param item
         *        an item in the tree
         * @return the background color of the supplied item's cell.
         */
        public abstract Object cellBackgroundColor( final Object item );

        /**
         * @param item
         *        an item in the tree
         * @return the foreground color of the supplied item's cell. Must not be <code>null</code>.
         */
        public abstract Object cellForegroundColor( final Object item );

        /**
         * @param item
         *        an item in the tree
         * @return the color of the child index shown in the supplied item's cell. Must not be <code>null</code>.
         */
        public abstract Object childIndexColor( final Object item );

        /**
         * @param item
         *        an item in the tree
         * @return Creates a cell for the supplied item. Must not be <code>null</code>.
         */
        public abstract Cell createCell( final Object item );

        /**
         * @return the border color of focus cells.
         */
        public abstract Object focusCellBorderColor();

        /**
         * @return the color of the focus line.
         */
        public abstract Object focusLineColor();

        /**
         * @return the height of the focus line. Default is {@value #DEFAULT_FOCUS_LINE_HEIGHT}.
         */
        public int focusLineHeight() {
            return DEFAULT_FOCUS_LINE_HEIGHT;
        }

        /**
         * @return the initial offset of the focus line. Default is {@value #DEFAULT_FOCUS_LINE_OFFSET}.
         */
        public int focusLineOffset() {
            return DEFAULT_FOCUS_LINE_OFFSET;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the icon of the cell for the supplied item. Default is <code>null</code>.
         */
        public Object icon( final Object item ) {
            return null;
        }

        /**
         * @return the width of a cells in the icon view. Default is {@value #PREFERRED_WIDTH}, indicating to use the largest
         *         preferred width of all cells.
         */
        public int iconViewCellWidth() {
            return PREFERRED_WIDTH;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the icon of the cell for the supplied item in an icon view. Default is <code>null</code>.
         */
        public Object iconViewIcon( final Object item ) {
            return null;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the cell editor used to edit the supplied item's index. Must not be <code>null</code>.
         */
        public abstract Object indexEditor( final Object item );

        /**
         * @return the initial width of a cell. Default is {@value #PREFERRED_WIDTH}, indicating to use the largest preferred width
         *         of all cells in a column.
         */
        public int initialCellWidth() {
            return PREFERRED_WIDTH;
        }

        /**
         * @return <code>true</code> if the initial index shown in columns with multiple cells is one. Default is <code>false</code>
         *         .
         */
        public boolean initialIndexIsOne() {
            return false;
        }

        /**
         * @param item
         *        an item in the tree
         * @return the cell editor used to edit the supplied item's name. Must not be <code>null</code>.
         */
        public abstract Object nameEditor( final Object item );

        /**
         * @param item
         *        an item in the tree
         * @return the background color of the supplied item's path button. Must not be <code>null</code>. Default is
         *         {@link #cellBackgroundColor(Object) cell's background color}.
         */
        public Object pathButtonBackgroundColor( final Object item ) {
            return cellBackgroundColor( item );
        }

        /**
         * @param item
         *        an item in the tree
         * @return the foreground color of the supplied item's cell. Must not be <code>null</code>. Default is
         *         {@link #cellForegroundColor(Object) cell's foreground color}.
         */
        public Object pathButtonForegroundColor( final Object item ) {
            return cellForegroundColor( item );
        }

        /**
         * @param item
         *        an item in the tree
         * @return the background color of the supplied item's path button if its column is minimized. Must not be <code>null</code>
         *         .
         */
        public abstract Object pathButtonHiddenBackgroundColor( final Object item );

        /**
         * @param item
         *        an item in the tree
         * @return the foreground color of the supplied item's path button if its column is minimized. Must not be <code>null</code>
         *         . Default is {@link #cellForegroundColor(Object) cell's foreground color}.
         */
        public Object pathButtonHiddenForegroundColor( final Object item ) {
            return cellForegroundColor( item );
        }

        /**
         * @return the background color of the {@link FocusTreeView}.
         */
        public abstract Object treeBackgroundColor();

        /**
         * @param item
         *        an item in the tree
         * @return the cell editor used to edit the supplied item's type. Must not be <code>null</code>.
         */
        public abstract Object typeEditor( final Object item );

        /**
         * @param item
         *        an item in the tree
         * @return the cell editor used to edit the supplied item's value. Must not be <code>null</code>.
         */
        public abstract Object valueEditor( final Object item );
    }
}
