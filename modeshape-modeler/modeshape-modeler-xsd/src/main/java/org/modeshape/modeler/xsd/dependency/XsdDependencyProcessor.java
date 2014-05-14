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
package org.modeshape.modeler.xsd.dependency;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;

import org.modeshape.common.util.StringUtil;
import org.modeshape.modeler.ModelType;
import org.modeshape.modeler.Modeler;
import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.extensions.DependencyProcessor;
import org.modeshape.modeler.internal.ModelerLexicon;
import org.modeshape.modeler.xsd.XsdModelerI18n;
import org.modeshape.sequencer.xsd.XsdLexicon;
import org.polyglotter.common.Logger;

/**
 * The XSD dependency processor for the ModeShape modeler.
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
     * @see org.modeshape.modeler.extensions.DependencyProcessor#modelType()
     */
    @Override
    public String modelType() {
        return "org.modeshape.modeler.xsd.Xsd";
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
        Node dependenciesNode = null;
        List< MissingDependency > pathsToMissingDependencies = null;

        try {
            final String modelName = modelNode.getName();
            LOGGER.debug( "Processing model node '%s'", modelName );
            Node schemaNode = null;

            { // find schema node
                final NodeIterator itr = modelNode.getParent().getNodes();

                while ( itr.hasNext() ) {
                    final Node kid = itr.nextNode();

                    if ( XsdLexicon.SCHEMA_DOCUMENT.equals( kid.getPrimaryNodeType().getName() ) ) {
                        schemaNode = kid;
                        break;
                    }
                }
            }

            // should always have a schema node
            if ( schemaNode == null ) {
                throw new ModelerException( XsdModelerI18n.schemaNodeNotFound, modelName );
            }

            // iterate over schema node's children to find dependencies
            final NodeIterator itr = schemaNode.getNodes();

            if ( !itr.hasNext() ) {
                return null; // no children of schema node so dependencies node not created
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
                    dependenciesNode = modelNode.addNode( ModelerLexicon.Model.DEPENDENCIES,
                                                          ModelerLexicon.Model.DEPENDENCIES );
                    LOGGER.debug( "Created dependencies folder node '%s'", dependenciesNode.getPath() );
                }

                // create dependency node
                final Node dependencyNode =
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

                        final Node artifactNode = node.getSession().getNode( artifactPath );
                        final String[] artifactSegments = artifactNode.getPath().split( "/" ); // TODO use this

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
                uploadMissingDependencies( artifactPath, modelNode, pathsToMissingDependencies, modeler, persistArtifacts );
            }

            modelNode.getSession().save();
            return dependenciesNode.getPath();
        } catch ( final Exception e ) {
            throw new ModelerException( e );
        }
    }

    void uploadMissingDependencies( final String artifactPath,
                                    final Node modelNode,
                                    final List< MissingDependency > missingDependencies,
                                    final Modeler modeler,
                                    final boolean persistArtifacts ) throws Exception {
        assert ( modelNode != null );
        assert ( missingDependencies != null );
        assert ( modeler != null );

        if ( !modelNode.hasProperty( ModelerLexicon.Model.EXTERNAL_LOCATION )
             || !modelNode.hasProperty( ModelerLexicon.Model.MODEL_TYPE )
             || missingDependencies.isEmpty() ) {
            return;
        }

        final String modelName = modelNode.getName();
        final String type = modelNode.getProperty( ModelerLexicon.Model.MODEL_TYPE ).getString();
        final ModelType modelType = modeler.modelTypeManager().modelType( type );

        String externalLocation = modelNode.getProperty( ModelerLexicon.Model.EXTERNAL_LOCATION ).getString();
        externalLocation = externalLocation.substring( 0, ( externalLocation.lastIndexOf( "/" ) ) );

        final String artifactDir = artifactPath.substring( 0, ( artifactPath.lastIndexOf( "/" ) ) );

        for ( final MissingDependency missingDependency : missingDependencies ) {
            String extPath;
            String artifactLocation;
            String modelPath;

            if ( missingDependency.isRelative() ) {
                artifactLocation = artifactDir;
                String location = externalLocation;
                int numParentDirs = missingDependency.numParentDirs;

                // navigate up parent dirs if necessary
                while ( numParentDirs > 0 ) {
                    location = location.substring( 0, ( externalLocation.lastIndexOf( "/" ) ) );
                    artifactLocation = artifactLocation.substring( 0, ( artifactLocation.lastIndexOf( "/" ) ) );
                    --numParentDirs;
                }

                // setup external path
                extPath = location;

                if ( !extPath.endsWith( "/" ) ) {
                    extPath += '/';
                }

                extPath += missingDependency.path;

                // setup dependency artifact path
                if ( !artifactLocation.endsWith( "/" ) ) {
                    artifactLocation += "/";
                }

                artifactLocation += missingDependency.path;
                modelPath = ( missingDependency.modelParentPath + missingDependency.path );
            } else {
                extPath = missingDependency.path;
                artifactLocation = missingDependency.artifactPath;
                modelPath = missingDependency.modelPath;
            }

            try {
                LOGGER.debug( "Importing XSD dependency from external path '%s' for source '%s' and path '%s'", extPath, modelName, artifactLocation );
                final String dependencyArtifactPath = modeler.importArtifact( new URL( extPath ).openStream(), artifactLocation );

                // create model
                LOGGER.debug( "Generating model for XSD dependency of model '%s' from path '%s'", modelName, modelPath );
                modeler.generateModel( dependencyArtifactPath, modelPath, modelType, persistArtifacts );
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
        final String artifactPath;

        MissingDependency( final String relativePath,
                           final int numParentDirs,
                           final String modelParentPath ) {
            this.path = relativePath;
            this.numParentDirs = numParentDirs;
            this.modelParentPath = modelParentPath;

            this.modelPath = null;
            this.artifactPath = null;
        }

        MissingDependency( final String externalPath,
                           final String artifactPath,
                           final String modelPath ) {
            this.path = externalPath;
            this.artifactPath = artifactPath;
            this.modelPath = modelPath;

            this.numParentDirs = -1;
            this.modelParentPath = null;
        }

        boolean isRelative() {
            return ( this.numParentDirs != -1 );
        }

    }

}
