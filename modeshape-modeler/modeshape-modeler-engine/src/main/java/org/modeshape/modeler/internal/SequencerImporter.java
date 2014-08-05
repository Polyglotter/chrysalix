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
package org.modeshape.modeler.internal;

import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.Property;

import org.modeshape.jcr.api.sequencer.Sequencer;
import org.modeshape.jcr.api.sequencer.Sequencer.Context;
import org.modeshape.modeler.Data;
import org.modeshape.modeler.spi.metamodel.Importer;

class SequencerImporter implements Importer {

    final Sequencer sequencer;

    SequencerImporter( final Sequencer sequencer ) {
        this.sequencer = sequencer;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.spi.metamodel.Importer#execute(java.io.InputStream, org.modeshape.modeler.Data)
     */
    @Override
    public void execute( final InputStream stream,
                         final Data data ) {
        // TODO Complete once Data is supported
    }

    boolean execute( final Property property,
                     final Node node,
                     final Context context ) throws Exception {
        return sequencer.execute( property, node, context );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.spi.metamodel.Importer#supports(java.lang.String)
     */
    @Override
    public boolean supports( final String mimeType ) {
        return sequencer.isAccepted( mimeType );
    }

}
