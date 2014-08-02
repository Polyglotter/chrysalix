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
package org.modeshape.modeler.xsd;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;

import org.modeshape.common.util.StringUtil;
import org.modeshape.modeler.Metamodel;
import org.modeshape.modeler.Modeler;
import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.ModelerLexicon;
import org.modeshape.modeler.extensions.DependencyProcessor;
import org.modeshape.modeler.internal.ModelerImpl;
import org.modeshape.sequencer.xsd.XsdLexicon;
import org.polyglotter.common.Logger;

/**
 * The XSD dependency processor for the modeler.
 */
public final class XsdDependencyProcessor implements DependencyProcessor {

    private static final Logger LOGGER = Logger.getLogger( XsdDependencyProcessor.class );

    /**
     * @param path
     *        the path being normalized (cannot be <code>null</code> or empty)
     * @return the normalized path (never <code>null</code> or empty)
     * @throws Exception
     *         if an error occurs
     */
    private static String normalizePath( final String path ) throws Exception {
        final URI uri = new URI( path ).normalize();
        return uri.toString();
    }

    private static boolean pathIsRelative( final String path ) throws Exception {
        assert ( ( path != null ) && !path.isEmpty() );

        if ( path.startsWith( "/" ) ) {
            return false;
        }

        if ( path.startsWith( PARENT_PATH ) || path.startsWith( SELF_PATH ) ) {
            return true;
        }

        final URI uri = new URI( path ).normalize();
        final String scheme = uri.getScheme();

        if ( StringUtil.isBlank( scheme ) ) {
            return true;
        }

        return false;
    }

    private boolean dependencyNode( final Node node ) throws Exception {
        assert ( node != null );

        final String primaryType = node.getPrimaryNodeType().getName();
        return ( XsdLexicon.IMPORT.equals( primaryType )
                 || XsdLexicon.INCLUDE.equals( primaryType )
                 || XsdLexicon.REDEFINE.equals( primaryType ) );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.extensions.DependencyProcessor#metamodel()
     */
    @Override
    public String metamodel() {
        return "org.modeshape.modeler.xsd.Xsd";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.extensions.DependencyProcessor#process(java.lang.String, javax.jcr.Node,
     *      org.modeshape.modeler.Modeler, boolean)
     */
    @Override
    public String process( final String dataPath,
                           final Node modelNode,
                           final Modeler modeler,
                           final boolean persistArtifacts ) throws ModelerException {
        List< MissingDependency > pathsToMissingDependencies = null;

        try {
            final String modelName = modelNode.getName();
            LOGGER.debug( "Processing model node '%s'", modelName );

            // may or may not have a schema node. if there is one iterate over it to find dependencies.
            NodeIterator itr = modelNode.getNodes();
            boolean hasSchemaNode = false;

            while ( itr.hasNext() ) {
                final Node kid = itr.nextNode();

                if ( XsdLexicon.SCHEMA_DOCUMENT.equals( kid.getPrimaryNodeType().getName() ) ) {
                    itr = kid.getNodes();
                    hasSchemaNode = true;
                    break;
                }
            }

            // no schema node so construct iterator using model node again
            if ( !hasSchemaNode ) {
                itr = modelNode.getNodes();
            }

            Node dependenciesNode = null;

            if ( !itr.hasNext() ) {
                return null; // no children so dependencies
            }

            pathsToMissingDependencies = new ArrayList<>();

            // find the dependency nodes
            DEPENDENCIES:
            while ( itr.hasNext() ) {
                final Node kid = itr.nextNode();

                if ( !dependencyNode( kid ) ) {
                    continue;
                }

                LOGGER.debug( "Processing dependency node '%s'", kid.getName() );

                // create dependencies folder node if not already created
                if ( dependenciesNode == null ) {
                    if ( modelNode.hasNode( ModelerLexicon.Model.DEPENDENCIES ) ) {
                        dependenciesNode = modelNode.getNode( ModelerLexicon.Model.DEPENDENCIES );
                    } else {
                        dependenciesNode = modelNode.addNode( ModelerLexicon.Model.DEPENDENCIES,
                                                              ModelerLexicon.Model.DEPENDENCIES );
                        LOGGER.debug( "Created dependencies folder node '%s'", dependenciesNode.getPath() );
                    }
                }

                // create dependency node
                @SuppressWarnings( "null" ) final Node dependencyNode =
                    dependenciesNode.addNode( ModelerLexicon.Dependency.DEPENDENCY, ModelerLexicon.Dependency.DEPENDENCY );

                // set input property
                final Property locationProp = kid.getProperty( XsdLexicon.SCHEMA_LOCATION );
                final String location = locationProp.getString();
                dependencyNode.setProperty( ModelerLexicon.Dependency.SOURCE_REFERENCE_PROPERTY, new String[] { location } );
                LOGGER.debug( "Setting dependency source reference property to '%s'", location );

                // derive path using model node parent as starting point
                Node node = modelNode.getParent();
                String path = normalizePath( location );
                LOGGER.debug( "Normalized schema location is '%s'", path );

                if ( pathIsRelative( path ) ) {
                    int count = 0;

                    while ( path.startsWith( SELF_PATH ) || path.startsWith( PARENT_PATH ) ) {
                        if ( path.startsWith( PARENT_PATH ) ) {
                            // if root node there is no parent
                            if ( node.getDepth() == 0 ) {
                                LOGGER.debug( "The relative path of '%s' is not valid for a dependency node of model '%s'", path, modelName );
                                continue DEPENDENCIES;
                            }

                            node = node.getParent();
                            path = path.substring( ( PARENT_PATH + '/' ).length() );
                            ++count;
                        } else {
                            path = path.substring( ( SELF_PATH + '/' ).length() );
                        }
                    }

                    String parentModelPath = node.getPath();

                    if ( !parentModelPath.endsWith( "/" ) ) {
                        parentModelPath += "/";
                    }

                    final String fullModelPath = parentModelPath + path;
                    dependencyNode.setProperty( ModelerLexicon.Dependency.PATH, fullModelPath );
                    LOGGER.debug( "Setting dependency path property to '%s'", fullModelPath );

                    final boolean exists = node.hasNode( path );

                    if ( !exists ) {
                        final MissingDependency md = new MissingDependency( path, count, parentModelPath );
                        pathsToMissingDependencies.add( md );
                    }
                } else {
                    LOGGER.debug( "Found absolute dependency path '%s'", path );

                    // find common part of path and external location to determine workspace location
                    if ( modelNode.hasProperty( ModelerLexicon.Model.EXTERNAL_LOCATION ) ) {
                        String extLocation = modelNode.getProperty( ModelerLexicon.Model.EXTERNAL_LOCATION ).getString();
                        extLocation = normalizePath( extLocation );
                        extLocation = extLocation.substring( 0, extLocation.lastIndexOf( "/" ) );

                        // add same scheme if necessary
                        if ( ( extLocation.indexOf( ':' ) != -1 ) && ( path.indexOf( ':' ) == -1 ) ) {
                            path = extLocation.substring( 0, extLocation.indexOf( ':' ) + 1 ) + path;
                        }

                        String dependencyModelPath = "";
                        final String dependencyArtifactPath = "blahblah"; // TODO need to set later
                        final String[] extLocSegments = extLocation.split( "/" );
                        final String[] pathSegments = path.split( "/" );
                        final List< String > commonPath = new ArrayList<>();

                        // final Node dataNode = node.getSession().getNode( dataPath );
                        // final String[] dataSegments = dataNode.getPath().split( "/" ); // TODO use this

                        // find common parent path between schema location and model's external location
                        for ( int i = 0; i < extLocSegments.length; ++i ) {
                            if ( i < pathSegments.length ) {
                                if ( extLocSegments[ i ].equals( pathSegments[ i ] ) ) {
                                    commonPath.add( extLocSegments[ i ] );
                                } else {
                                    break;
                                }
                            }
                        }

                        // add to the common path the remainder of dependency model's schema location
                        for ( int i = ( commonPath.size() - 1 ); i > 0; --i ) {
                            if ( node.getName().equals( commonPath.get( i ) ) ) {
                                if ( node.getDepth() == 0 ) {
                                    // TODO at root node so can't get a corresponding parent node
                                } else {
                                    node = node.getParent();
                                }
                            } else if ( pathSegments.length > i ) {
                                for ( int j = ( i + 1 ); j < pathSegments.length; ++j ) {
                                    dependencyModelPath += pathSegments[ j ];

                                    if ( j != ( pathSegments.length - 1 ) ) {
                                        dependencyModelPath += '/';
                                    }
                                }

                                break;
                            } else {
                                // TODO ???
                            }
                        }

                        final boolean exists = node.hasNode( dependencyModelPath );
                        String parentPath = node.getPath();

                        if ( !parentPath.endsWith( "/" ) ) {
                            parentPath += "/";
                        }

                        dependencyModelPath = parentPath + dependencyModelPath;
                        dependencyNode.setProperty( ModelerLexicon.Dependency.PATH, dependencyModelPath );
                        LOGGER.debug( "Setting dependency path property to '%s'", dependencyModelPath );

                        if ( !exists ) {
                            final MissingDependency md = new MissingDependency( path, dependencyArtifactPath, dependencyModelPath );
                            pathsToMissingDependencies.add( md );
                        }
                    } else {
                        // TODO no external location for dependent model node
                    }
                }
            }

            if ( dependenciesNode == null ) {
                return null;
            }

            // process any missing dependencies
            if ( !pathsToMissingDependencies.isEmpty() ) {
                uploadMissingDependencies( dataPath, modelNode, pathsToMissingDependencies, modeler, persistArtifacts );
            }

            modelNode.getSession().save();
            return dependenciesNode.getPath();
        } catch ( final Exception e ) {
            if ( e instanceof ModelerException ) throw ( ModelerException ) e;
            throw new ModelerException( e );
        }
    }

    void uploadMissingDependencies( final String dataPath,
                                    final Node modelNode,
                                    final List< MissingDependency > missingDependencies,
                                    final Modeler modeler,
                                    final boolean persistArtifacts ) throws Exception {
        assert ( modelNode != null );
        assert ( missingDependencies != null );
        assert ( modeler != null );

        if ( !modelNode.hasProperty( ModelerLexicon.Model.EXTERNAL_LOCATION )
             || !modelNode.hasProperty( ModelerLexicon.Model.METAMODEL )
             || missingDependencies.isEmpty() ) {
            return;
        }

        final String modelName = modelNode.getName();
        final String id = modelNode.getProperty( ModelerLexicon.Model.METAMODEL ).getString();
        final Metamodel metamodel = modeler.metamodelManager().metamodel( id );

        String externalLocation = modelNode.getProperty( ModelerLexicon.Model.EXTERNAL_LOCATION ).getString();
        externalLocation = externalLocation.substring( 0, ( externalLocation.lastIndexOf( "/" ) ) );

        final String dataDir = dataPath.substring( 0, ( dataPath.lastIndexOf( "/" ) ) );

        for ( final MissingDependency missingDependency : missingDependencies ) {
            String extPath;
            String dataLocation;
            String modelPath;

            if ( missingDependency.isRelative() ) {
                dataLocation = dataDir;
                String location = externalLocation;
                int numParentDirs = missingDependency.numParentDirs;

                // navigate up parent dirs if necessary
                while ( numParentDirs > 0 ) {
                    location = location.substring( 0, ( externalLocation.lastIndexOf( "/" ) ) );
                    dataLocation = dataLocation.substring( 0, ( dataLocation.lastIndexOf( "/" ) ) );
                    --numParentDirs;
                }

                // setup external path
                extPath = location;

                if ( !extPath.endsWith( "/" ) ) {
                    extPath += '/';
                }

                extPath += missingDependency.path;

                // setup dependency data path
                if ( !dataLocation.endsWith( "/" ) ) {
                    dataLocation += "/";
                }

                dataLocation += missingDependency.path;
                modelPath = ( missingDependency.modelParentPath + missingDependency.path );
            } else {
                extPath = missingDependency.path;
                dataLocation = missingDependency.dataPath;
                modelPath = missingDependency.modelPath;
            }

            try {
                LOGGER.debug( "Importing XSD dependency from external path '%s' for source '%s' and path '%s'", extPath, modelName, dataLocation );
                final String dependencyArtifactPath = modeler.importData( new URL( extPath ).openStream(), dataLocation );

                // create model
                LOGGER.debug( "Generating model for XSD dependency of model '%s' from path '%s'", modelName, modelPath );
                ( ( ModelerImpl ) modeler ).generateModel( dependencyArtifactPath, modelPath, metamodel, persistArtifacts );
            } catch ( final Exception e ) {
                LOGGER.error( e, XsdModelerI18n.errorImportingXsdDependencyArtifact, extPath, modelName );
            }
        }
    }

    private static class MissingDependency {

        final String modelParentPath;
        final int numParentDirs;
        final String path;

        final String modelPath;
        final String dataPath;

        MissingDependency( final String relativePath,
                           final int numParentDirs,
                           final String modelParentPath ) {
            this.path = relativePath;
            this.numParentDirs = numParentDirs;
            this.modelParentPath = modelParentPath;

            this.modelPath = null;
            this.dataPath = null;
        }

        MissingDependency( final String externalPath,
                           final String dataPath,
                           final String modelPath ) {
            this.path = externalPath;
            this.dataPath = dataPath;
            this.modelPath = modelPath;

            this.numParentDirs = -1;
            this.modelParentPath = null;
        }

        boolean isRelative() {
            return ( this.numParentDirs != -1 );
        }

    }

}
