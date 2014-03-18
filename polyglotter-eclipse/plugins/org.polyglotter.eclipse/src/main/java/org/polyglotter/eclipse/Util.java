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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.polyglotter.common.CheckArg;
import org.polyglotter.common.I18n;
import org.polyglotter.common.PolyglotterException;

/**
 * Utilities used in the Polyglotter Eclipse project.
 */
public final class Util {

    /**
     * An empty array of objects.
     */
    public static final Object[] EMPTY_ARRAY = new Object[ 0 ];

    /**
     * An empty string.
     */
    public static final String EMPTY_STRING = "";

    /**
     * @param error
     *        the error (can be <code>null</code> if there is no error)
     * @return a {@link IStatus status} with an {@link IStatus#ERROR error} severity (never <code>null</code>)
     */
    public static IStatus createErrorStatus( final Throwable error ) {
        return createErrorStatus( error, null );
    }

    /**
     * @param error
     *        the error (can be <code>null</code> if there is no error)
     * @param i18nMessage
     *        the localized message (can be <code>null</code> if there is not a message)
     * @param args
     *        the error message arguments (can be <code>null</code> or empty)
     * @return a {@link IStatus status} with an {@link IStatus#ERROR error} severity (never <code>null</code>)
     */
    public static IStatus createErrorStatus( final Throwable error,
                                             final I18n i18nMessage,
                                             final Object... args ) {
        final String msg = ( ( i18nMessage == null ) ? null : i18nMessage.text() );
        return new Status( IStatus.ERROR, Activator.ID, msg, error );
    }

    /**
     * @param shell
     *        the parent of the dialog that will be displayed (can be <code>null</code>)
     * @param error
     *        the error that is being logged and shown to the user (cannot be <code>null</code>)
     */
    public static void handleModelError( final Shell shell,
                                         final PolyglotterException error ) {
        CheckArg.notNull( error, "error" );
        final IStatus status = createErrorStatus( error );

        if ( Platform.isRunning() ) {
            Activator.logger().log( status );

            shell.getDisplay().asyncExec( new Runnable() {

                /**
                 * {@inheritDoc}
                 * 
                 * @see java.lang.Runnable#run()
                 */
                @Override
                public void run() {
                    MessageDialog.openError( shell, PolyglotterEclipseI18n.errorDialogTitle.text(), status.getMessage() );
                }
            } );
        }
    }

    /**
     * Don't allow construction outside this class.
     */
    private Util() {
        // nothing to do
    }

}
