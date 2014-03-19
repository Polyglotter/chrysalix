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

import org.eclipse.core.runtime.ILog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    /**
     * 
     */
    public static String ID = "org.polyglotter.eclipse";

    // The singleton instance of this plug-in
    private static Activator plugin;

    /**
     * @return the logger of the singleton instance (never <code>null</code> when Eclipse platform is running)
     */
    public static ILog logger() {
        return plugin.getLog();
    }

    /**
     * @return the singleton instance of this plug-in.
     */
    public static Activator plugin() {
        return plugin;
    }

    // private Polyglotter polyglotter;

    /**
     * @param path
     *        path to image
     * @return the cached image with the supplied path
     */
    public Image image( final String path ) {
        final Image img = getImageRegistry().get( path );
        if ( img != null ) return img;
        getImageRegistry().put( path, imageDescriptorFromPlugin( ID, "icons/" + path ) );
        return getImageRegistry().get( path );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start( final BundleContext context ) throws Exception {
        super.start( context );
        plugin = this;
        // polyglotter = new Polyglotter( getStateLocation().toOSString() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop( final BundleContext context ) throws Exception {
        // Close Polyglotter
        // if ( polyglotter != null ) polyglotter.close();
        // Stop plug-in
        plugin = null;
        super.stop( context );
    }
}
