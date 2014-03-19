package org.polyglotter.eclipse;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.polyglotter.common.PolyglotterException;

/**
 * 
 */
public class View extends ViewPart {

    static final Color FOLDER_COLOR = new Color( Display.getCurrent(), 0, 64, 128 );

    static final Object[] NO_ITEMS = new Object[ 0 ];

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl( final Composite parent ) {
        final TreeSpinner treeSpinner = new TreeSpinner( parent );
        try {
            treeSpinner.setRootAndContentProvider( new File( System.getProperty( "user.home" ) ), new TreeSpinnerContentProvider() {

                @Override
                public Color backgroundColor( final Object item ) {
                    if ( ( ( File ) item ).isDirectory() ) return FOLDER_COLOR;
                    return Display.getCurrent().getSystemColor( SWT.COLOR_WHITE );
                }

                @Override
                public int childCount( final Object item ) {
                    return children( item ).length;
                }

                @Override
                public Object[] children( final Object item ) {
                    final Object[] children = ( ( File ) item ).listFiles();
                    return children == null ? NO_ITEMS : children;
                }

                @Override
                public boolean hasChildren( final Object item ) {
                    return childCount( item ) > 0;
                }

                @Override
                public String name( final Object item ) {
                    final File file = ( File ) item;
                    final String name = file.getName();
                    return name.isEmpty() ? "/" : name;
                }

                //
                // @Override
                // public int preferredWidth( final Object item ) {
                // return 80;
                // }

                @Override
                public String type( final Object item ) {
                    final File file = ( File ) item;
                    return file.isDirectory() ? "Folder" : "File";
                }
            } );
        } catch ( final PolyglotterException e ) {
            Util.handleModelError( parent.getShell(), e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {}
}
