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
package org.modeshape.modeler.ddl;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.modeshape.jcr.api.JcrTools;
import org.modeshape.modeler.Modeler;
import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.extensions.DependencyProcessor;

/**
 * 
 */
public class TeiidDdlDependencyProcessor implements DependencyProcessor {

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.extensions.DependencyProcessor#modelType()
     */
    @Override
    public String modelType() {
        return TeiidDdlLexicon.DDL_MODEL_TYPE_ID;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.extensions.DependencyProcessor#process(java.lang.String, javax.jcr.Node,
     *      org.modeshape.modeler.Modeler, boolean)
     */
    @Override
    public String process( final String artifactPath,
                           final Node modelNode,
                           final Modeler modeler,
                           final boolean persistArtifacts ) throws ModelerException {
        try {
            new JcrTools().printSubgraph( modelNode );
            return null;
        } catch ( final RepositoryException e ) {
            throw new ModelerException( e );
        }
    }

}
