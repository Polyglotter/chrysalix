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
package org.modeshape.modeler.eclipse;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.modeshape.modeler.ui.UiI18n;
import org.modeshape.modeler.ui.UiUtil;
import org.polyglotter.common.I18n;

/**
 * 
 */
public class EclipseUtil {

    /**
     * @param e
     *        a throwable
     * @param message
     *        a contextual message to be shown before the supplied throwable's message
     * @param messageParameters
     *        any parameters required by the supplied message
     */
    public static void logAndShowError( final Throwable e,
                                        final I18n message,
                                        final Object... messageParameters ) {
        UiUtil.logError( e, message, messageParameters );
        showError( e, message, messageParameters );
    }

    /**
     * @param message
     *        a contextual message to be shown
     * @param messageParameters
     *        any parameters required by the supplied message
     */
    public static void showError( final I18n message,
                                  final Object... messageParameters ) {
        showError( message.text( messageParameters ) );
    }

    private static void showError( final String message ) {
        final Shell shell = Display.getCurrent().getActiveShell();
        if ( Display.getCurrent().getThread() == Thread.currentThread() ) {
            MessageDialog.openError( shell, UiI18n.errorDialogTitle.text(), message );
        } else shell.getDisplay().asyncExec( new Runnable() {

            @Override
            public void run() {
                MessageDialog.openError( shell, UiI18n.errorDialogTitle.text(), message );
            }
        } );
    }

    /**
     * @param e
     *        a throwable
     * @param message
     *        a contextual message to be shown before the supplied throwable's message
     * @param messageParameters
     *        any parameters required by the supplied message
     */
    public static void showError( final Throwable e,
                                  final I18n message,
                                  final Object... messageParameters ) {
        String eMessage = e.getMessage();
        if ( eMessage == null || eMessage.trim().length() == 0 ) eMessage = e.getClass().getSimpleName();
        showError( message.text( messageParameters ) + "\n\n" + eMessage );
    }

    /**
     * Don't allow construction outside this class.
     */
    private EclipseUtil() {}
}
