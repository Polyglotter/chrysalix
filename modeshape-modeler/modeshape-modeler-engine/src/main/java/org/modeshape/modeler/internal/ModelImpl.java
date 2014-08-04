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

import org.modeshape.common.util.StringUtil;
import org.modeshape.modeler.Metamodel;
import org.modeshape.modeler.Model;
import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.ModelerI18n;
import org.modeshape.modeler.ModelerLexicon;
import org.modeshape.modeler.extensions.Dependency;
import org.modeshape.modeler.internal.task.TaskWithResult;

class ModelImpl extends ModelObjectImpl implements Model {

    private Set< Dependency > dependencies;

    /**
     * @param modeler
     *        the modeler's modeler
     * @param modelPath
     *        a path to a model
     */
    ModelImpl( final ModelerImpl modeler,
               final String modelPath ) {
        super( modeler, modelPath, -1 );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.Model#allDependenciesExist()
     */
    @Override
    public boolean allDependenciesExist() throws ModelerException {
        return missingDependencies().isEmpty();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.Model#dependencies()
     */
    @Override
    public Set< Dependency > dependencies() throws ModelerException {
        if ( this.dependencies == null ) {
            this.dependencies = modeler.run( new TaskWithResult< Set< Dependency > >() {

                @Override
                public Set< Dependency > run( final Session session ) throws Exception {
                    final Node modelNode = session.getNode( absolutePath() );

                    if ( modelNode.hasNode( ModelerLexicon.Model.DEPENDENCIES ) ) {
                        final NodeIterator itr = modelNode.getNode( ModelerLexicon.Model.DEPENDENCIES ).getNodes();
                        final Set< Dependency > result = new HashSet<>( ( int ) itr.getSize() );

                        while ( itr.hasNext() ) {
                            final Node dependencyNode = itr.nextNode();

                            // must have source references
                            if ( dependencyNode.hasProperty( ModelerLexicon.Dependency.SOURCE_REFERENCE_PROPERTY ) ) {
                                final Value[] values =
                                    dependencyNode.getProperty( ModelerLexicon.Dependency.SOURCE_REFERENCE_PROPERTY ).getValues();
                                final List< String > refs = new ArrayList<>( values.length );

                                for ( final Value value : values ) {
                                    refs.add( value.getString() );
                                }

                                String dependencyPath = null;
                                boolean exists = false;

                                if ( dependencyNode.hasProperty( ModelerLexicon.Dependency.PATH ) ) {
                                    dependencyPath = dependencyNode.getProperty( ModelerLexicon.Dependency.PATH ).getString();
                                }

                                if ( !StringUtil.isBlank( dependencyPath ) ) {
                                    exists = session.nodeExists( dependencyPath );
                                }

                                final Dependency dependency = new Dependency( dependencyPath, refs, exists );
                                result.add( dependency );
                            } else {
                                throw new ModelerException( ModelerI18n.dependencyDoesNotHaveSourceReferences, absolutePath() );
                            }
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
     * @see org.modeshape.modeler.Model#externalLocation()
     */
    @Override
    public URL externalLocation() throws ModelerException {
        return modeler.run( new TaskWithResult< URL >() {

            @Override
            public URL run( final Session session ) throws Exception {
                final Node model = session.getNode( path );
                return model.hasProperty( ModelerLexicon.Model.EXTERNAL_LOCATION )
                                                                                  ? new URL( model.getProperty( ModelerLexicon.Model.EXTERNAL_LOCATION ).getString() )
                                                                                  : null;
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.Model#metamodel()
     */
    @Override
    public Metamodel metamodel() throws ModelerException {
        return modeler.run( new TaskWithResult< Metamodel >() {

            @Override
            public Metamodel run( final Session session ) throws Exception {
                return modeler.metamodelManager().metamodel( session.getNode( path )
                                                                    .getProperty( ModelerLexicon.Model.METAMODEL )
                                                                    .getString() );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.Model#missingDependencies()
     */
    @Override
    public Set< Dependency > missingDependencies() throws ModelerException {
        return modeler.run( new TaskWithResult< Set< Dependency > >() {

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
                    final Model dependencyModel = new ModelImpl( this.modeler, dependency.path() );
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
     * @see org.modeshape.modeler.internal.ModelObjectImpl#model()
     */
    @Override
    public Model model() {
        return this;
    }

    private boolean modelNode( final Node node ) throws Exception {
        assert ( node != null );

        for ( final NodeType nodeType : node.getMixinNodeTypes() ) {
            if ( ModelerLexicon.Model.MODEL_MIXIN.equals( nodeType.getName() ) ) {
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.internal.ModelObjectImpl#modelRelativePath()
     */
    @Override
    public String modelRelativePath() {
        return "";
    }

}
