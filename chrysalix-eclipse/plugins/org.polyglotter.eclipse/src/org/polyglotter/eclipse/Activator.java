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
package org.chrysalix.eclipse;

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
    public static String ID = "org.chrysalix.eclipse";

    static Activator plugin;

    static Image image( final String path ) {
        final Image img = plugin.getImageRegistry().get( path );
        if ( img != null ) return img;
        plugin.getImageRegistry().put( path, imageDescriptorFromPlugin( ID, path ) );
        return plugin.getImageRegistry().get( path );
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
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop( final BundleContext context ) throws Exception {
        // Stop plug-in
        plugin = null;
        super.stop( context );
    }
}
