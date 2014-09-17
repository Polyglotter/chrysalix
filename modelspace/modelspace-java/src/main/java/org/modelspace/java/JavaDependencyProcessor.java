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
package org.modelspace.java;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.modelspace.Modelspace;
import org.modelspace.ModelspaceException;
import org.modelspace.spi.DependencyProcessor;
import org.modeshape.jcr.api.JcrTools;

/**
 * 
 */
public class JavaDependencyProcessor implements DependencyProcessor {

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.spi.DependencyProcessor#metamodelId()
     */
    @Override
    public String metamodelId() {
        return "org.modelspace.java.JavaFile";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.spi.DependencyProcessor#process(java.lang.String, javax.jcr.Node,
     *      org.modelspace.Modelspace, boolean)
     */
    @Override
    public String process( final String dataPath,
                           final Node modelNode,
                           final Modelspace modelspace,
                           final boolean persistArtifacts ) throws ModelspaceException {
        try {
            new JcrTools().printSubgraph( modelNode );
            return null;
        } catch ( final RepositoryException e ) {
            throw new ModelspaceException( e, "Unable to resolve dependencies for \"%s\"", modelNode );
        }
    }

}
