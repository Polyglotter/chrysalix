package org.polyglotter.eclipse.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

/**
 * The workspace view.
 */
public final class WorkspaceView extends ViewPart {

    /**
     * The workspace part identifier for this view.
     */
    public static final String ID = WorkspaceView.class.getPackage().getName() + ".workspaceView";

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl( final Composite parent ) {
        // TODO implement createPartControl
        final Label temp = new Label( parent, SWT.NONE );
        temp.setText( "Blah Blah Blah" );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        // TODO implement setFocus
    }

}
