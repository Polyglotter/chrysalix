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
package org.modelspace.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.nodetype.NodeType;

import org.modelspace.Metamodel;
import org.modelspace.Model;
import org.modelspace.Modelspace;
import org.modelspace.ModelspaceException;
import org.modelspace.ModelspaceLexicon;
import org.modelspace.internal.task.TaskWithResult;
import org.modelspace.spi.Dependency;
import org.modeshape.common.util.StringUtil;

class ModelImpl extends ModelObjectImpl implements Model {

    private Set< Dependency > dependencies;

    /**
     * @param modelspace
     *        the modelspace's modelspace
     * @param modelPath
     *        a path to a model
     */
    ModelImpl( final Modelspace modelspace,
               final String modelPath ) {
        super( modelspace, modelPath, -1 );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Model#allDependenciesExist()
     */
    @Override
    public boolean allDependenciesExist() throws ModelspaceException {
        return missingDependencies().isEmpty();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Model#dependencies()
     */
    @Override
    public Set< Dependency > dependencies() throws ModelspaceException {
        if ( this.dependencies == null ) {
            this.dependencies = modelspace.run( new TaskWithResult< Set< Dependency > >() {

                @Override
                public Set< Dependency > run( final Session session ) throws Exception {
                    final Node modelNode = session.getNode( absolutePath() );

                    if ( modelNode.hasNode( ModelspaceLexicon.Model.DEPENDENCIES ) ) {
                        final NodeIterator itr = modelNode.getNode( ModelspaceLexicon.Model.DEPENDENCIES ).getNodes();
                        final Set< Dependency > result = new HashSet<>( ( int ) itr.getSize() );

                        while ( itr.hasNext() ) {
                            final Node dependencyNode = itr.nextNode();

                            // must have source references
                            if ( dependencyNode.hasProperty( ModelspaceLexicon.Dependency.SOURCE_REFERENCE_PROPERTY ) ) {
                                final Value[] values =
                                    dependencyNode.getProperty( ModelspaceLexicon.Dependency.SOURCE_REFERENCE_PROPERTY ).getValues();
                                final List< String > refs = new ArrayList<>( values.length );

                                for ( final Value value : values ) {
                                    refs.add( value.getString() );
                                }

                                String dependencyPath = null;
                                boolean exists = false;

                                if ( dependencyNode.hasProperty( ModelspaceLexicon.Dependency.PATH ) ) {
                                    dependencyPath = dependencyNode.getProperty( ModelspaceLexicon.Dependency.PATH ).getString();
                                }

                                if ( !StringUtil.isBlank( dependencyPath ) ) {
                                    exists = session.nodeExists( dependencyPath );
                                }

                                final Dependency dependency = new Dependency( dependencyPath, refs, exists );
                                result.add( dependency );
                            } else throw new ModelspaceException( "A dependency node exists for '%s' but has no source references",
                                                               absolutePath() );
                        }

                        return result;
                    }

                    return Collections.emptySet();
                }
            } );
        }

        return this.dependencies;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Model#externalLocation()
     */
    @Override
    public URL externalLocation() throws ModelspaceException {
        return modelspace.run( new TaskWithResult< URL >() {

            @Override
            public URL run( final Session session ) throws Exception {
                final Node model = session.getNode( path );
                return model.hasProperty( ModelspaceLexicon.Model.EXTERNAL_LOCATION )
                                                                                  ? new URL( model.getProperty( ModelspaceLexicon.Model.EXTERNAL_LOCATION ).getString() )
                                                                                  : null;
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Model#metamodel()
     */
    @Override
    public Metamodel metamodel() throws ModelspaceException {
        return modelspace.run( new TaskWithResult< Metamodel >() {

            @Override
            public Metamodel run( final Session session ) throws Exception {
                return modelspace.metamodelManager().metamodel( session.getNode( path )
                                                                    .getProperty( ModelspaceLexicon.Model.METAMODEL )
                                                                    .getString() );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Model#missingDependencies()
     */
    @Override
    public Set< Dependency > missingDependencies() throws ModelspaceException {
        return modelspace.run( new TaskWithResult< Set< Dependency > >() {

            @Override
            public Set< Dependency > run( final Session session ) throws Exception {
                final Set< Dependency > dependencies = dependencies();

                if ( dependencies.isEmpty() ) {
                    return Collections.emptySet();
                }

                final Set< Dependency > missing = new HashSet<>();

                for ( final Dependency dependency : dependencies ) {
                    final String path = dependency.path();

                    if ( !session.nodeExists( path ) ) {
                        missing.add( dependency );
                    } else {
                        missingDependencies( dependency, missing, session );
                    }
                }

                return missing;
            }
        } );
    }

    void missingDependencies( final Dependency dependency,
                              final Collection< Dependency > missing,
                              final Session session ) throws Exception {
        if ( dependency.exists() ) {
            // find all models and all their dependencies
            final Node node = session.getNode( dependency.path() );
            final NodeIterator itr = node.getNodes();

            while ( itr.hasNext() ) {
                final Node kid = itr.nextNode();

                if ( modelNode( kid ) ) {
                    final Model dependencyModel = new ModelImpl( this.modelspace, dependency.path() );
                    missing.addAll( dependencyModel.missingDependencies() );
                }
            }
        } else {
            missing.add( dependency );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.internal.ModelObjectImpl#model()
     */
    @Override
    public Model model() {
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.internal.ModelObjectImpl#modelspace()
     */
    @Override
    public Modelspace modelspace() {
        return super.modelspace();
    }

    private boolean modelNode( final Node node ) throws Exception {
        assert ( node != null );

        for ( final NodeType nodeType : node.getMixinNodeTypes() ) {
            if ( ModelspaceLexicon.Model.MODEL_MIXIN.equals( nodeType.getName() ) ) {
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.internal.ModelObjectImpl#modelRelativePath()
     */
    @Override
    public String modelRelativePath() {
        return "";
    }

}
