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
package org.polyglotter.internal;

import java.util.StringTokenizer;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;

import org.modeshape.jcr.api.JcrTools;
import org.modeshape.modeler.Model;
import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.ModelerLexicon;
import org.modeshape.modeler.internal.ModelImpl;
import org.modeshape.modeler.internal.Task;
import org.polyglotter.Operation;
import org.polyglotter.common.PolyglotterException;

/**
 *
 */
public class CloneOperation implements Operation {

    final Model sourceModel;
    final String sourcePropertyPath;
    final String targetModelPath;

    /**
     * @param sourceModel
     *        the model that is the source of the transforms
     * @param sourcePropertyPath
     *        the workspace path to a source model object property, relative to the source model path
     * @param targetModelPath
     *        the workspace path to a new model that will be the target of the transforms
     */
    public CloneOperation( final Model sourceModel,
                           final String sourcePropertyPath,
                           final String targetModelPath ) {
        this.sourceModel = sourceModel;
        this.sourcePropertyPath = sourcePropertyPath;
        this.targetModelPath = targetModelPath;
    }

    void cloneProperty( final Property prop,
                        final Node targetNode ) throws Exception {
        switch ( prop.getDefinition().getRequiredType() ) {
            case PropertyType.BOOLEAN:
                targetNode.setProperty( prop.getName(), prop.getBoolean() );
                break;
            case PropertyType.LONG:
                targetNode.setProperty( prop.getName(), prop.getLong() );
                break;
            default:
                targetNode.setProperty( prop.getName(), prop.getString() );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.Operation#execute()
     */
    @Override
    public void execute() throws PolyglotterException {
        try {
            ( ( ModelImpl ) sourceModel ).manager.run( new Task< Void >() {

                @Override
                public Void run( final Session session ) throws Exception {
                    Node sourceNode = session.getNode( sourceModel.absolutePath() );
                    final JcrTools tools = new JcrTools();
                    Node targetNode = tools.findOrCreateNode( session, targetModelPath, sourceNode.getPrimaryNodeType().getName() );
                    for ( final NodeType mixin : sourceNode.getMixinNodeTypes() )
                        targetNode.addMixin( mixin.getName() );
                    for ( final PropertyIterator iter = sourceNode.getProperties( ModelerLexicon.NAMESPACE_PREFIX + '*' );
                    iter.hasNext(); )
                        cloneProperty( iter.nextProperty(), targetNode );
                    for ( final StringTokenizer iter = new StringTokenizer( sourcePropertyPath, "/" ); iter.hasMoreTokens(); ) {
                        final String token = iter.nextToken();
                        if ( token.charAt( 0 ) == '@' ) {
                            final Property prop = sourceNode.getProperty( token.substring( 1 ) );
                            cloneProperty( prop, targetNode );
                            break;
                        }
                        sourceNode = sourceNode.getNode( token );
                        targetNode = tools.findOrCreateChild( targetNode, token, sourceNode.getPrimaryNodeType().getName() );
                        for ( final NodeType mixin : sourceNode.getMixinNodeTypes() )
                            targetNode.addMixin( mixin.getName() );
                    }
                    session.save();
                    return null;
                }
            } );
        } catch ( final ModelerException e ) {
            throw new PolyglotterException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.polyglotter.Operation#name()
     */
    @Override
    public String name() {
        return "clone";
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name() + "( " + sourceModel.absolutePath() + '/' + sourcePropertyPath + ", " + targetModelPath + " )";
    }
}
