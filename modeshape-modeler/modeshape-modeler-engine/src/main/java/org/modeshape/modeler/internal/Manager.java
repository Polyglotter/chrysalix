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

import java.util.concurrent.ExecutionException;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.modeshape.common.collection.Problem;
import org.modeshape.common.collection.Problems;
import org.modeshape.common.util.CheckArg;
import org.modeshape.jcr.JcrRepository;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.NoSuchRepositoryException;
import org.modeshape.jcr.RepositoryConfiguration;
import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.ModelerI18n;
import org.polyglotter.common.CommonI18n;
import org.polyglotter.common.Logger;

/**
 * 
 */
public class Manager {

    /**
     * 
     */
    public static final String REPOSITORY_STORE_PARENT_PATH_PROPERTY = "org.modeshape.modeler.repositoryStoreParentPath";

    private ModeShapeEngine modeShape;
    private JcrRepository repository;
    private ModelTypeManagerImpl modelTypeManager;

    /**
     * 
     */
    public final String modeShapeConfigurationPath;

    /**
     * @param repositoryStoreParentPath
     *        the path to the folder that should contain the ModeShape repository store
     * @param modeShapeConfigurationPath
     *        the path to a ModeShape configuration file
     */
    public Manager( final String repositoryStoreParentPath,
                    final String modeShapeConfigurationPath ) {
        CheckArg.isNotEmpty( repositoryStoreParentPath, "repositoryStoreParentPath" );
        CheckArg.isNotEmpty( modeShapeConfigurationPath, "modeShapeConfigurationPath" );
        System.setProperty( REPOSITORY_STORE_PARENT_PATH_PROPERTY, repositoryStoreParentPath );
        this.modeShapeConfigurationPath = modeShapeConfigurationPath;
    }

    /**
     * @param session
     *        a session
     * @param filePath
     *        a file's repository path
     * @return the node with the supplied filePath
     * @throws Exception
     *         if any problem occurs
     */
    public Node artifactNode( final Session session,
                              final String filePath ) throws Exception {
        try {
            return session.getNode( filePath.charAt( 0 ) == '/' ? filePath : '/' + filePath );
        } catch ( final PathNotFoundException e ) {
            throw new IllegalArgumentException( e );
        }
    }

    /**
     * @throws ModelerException
     *         if any problem occurs
     */
    public void close() throws ModelerException {
        try {
            if ( modeShape != null ) modeShape.shutdown().get();
        } catch ( InterruptedException | ExecutionException e ) {
            throw new ModelerException( e );
        }
        Logger.getLogger( getClass() ).info( ModelerI18n.modelerStopped );
    }

    /**
     * @return the model type manager
     * @throws ModelerException
     *         if any error occurs
     */
    public ModelTypeManagerImpl modelTypeManager() throws ModelerException {
        if ( modelTypeManager == null ) modelTypeManager = new ModelTypeManagerImpl( this );
        return modelTypeManager;
    }

    JcrRepository repository() throws ModelerException {
        if ( repository == null ) {
            try {
                modeShape = new ModeShapeEngine();
                modeShape.start();
                final RepositoryConfiguration config = RepositoryConfiguration.read( modeShapeConfigurationPath );
                final Problems problems = config.validate();
                if ( problems.hasProblems() ) {
                    for ( final Problem problem : problems )
                        Logger.getLogger( getClass() ).error( problem.getThrowable(), CommonI18n.text, problem.getMessage().text() );
                    throw problems.iterator().next().getThrowable();
                }
                JcrRepository repository;
                try {
                    repository = modeShape.getRepository( config.getName() );
                } catch ( final NoSuchRepositoryException err ) {
                    repository = modeShape.deploy( config );
                }
                this.repository = repository;
                Logger.getLogger( getClass() ).info( ModelerI18n.modelerStarted );
            } catch ( final Throwable e ) {
                throw new ModelerException( e );
            }
        }
        return repository;
    }

    /**
     * @param systemObject
     *        the system class for which the supplied system task will be run.
     * @param task
     *        a system task
     * @return the return value of the supplied system task
     * @throws ModelerException
     *         if any problem occurs
     */
    public < T > T run( final Object systemObject,
                        final SystemTask< T > task
                    ) throws ModelerException {
        try {
            final Session session = repository().login( "modeler" );
            final String path = '/' + systemObject.getClass().getSimpleName();
            final Node node;
            if ( session.nodeExists( path ) )
                node = session.getNode( path );
            else {
                node = session.getRootNode().addNode( path );
                session.save();
            }
            try {
                return task.run( session, node );
            } catch ( final RuntimeException e ) {
                throw e;
            } catch ( final Exception e ) {
                throw new ModelerException( e );
            } finally {
                session.logout();
            }
        } catch ( final RepositoryException e ) {
            throw new ModelerException( e );
        }
    }

    /**
     * @param task
     *        a task
     * @return the return value of the supplied task
     * @throws ModelerException
     *         if any problem occurs
     */
    public < T > T run( final Task< T > task ) throws ModelerException {
        try {
            final Session session = repository().login( "default" );
            try {
                return task.run( session );
            } catch ( final RuntimeException e ) {
                throw e;
            } catch ( final Exception e ) {
                throw new ModelerException( e );
            } finally {
                session.logout();
            }
        } catch ( final RepositoryException e ) {
            throw new ModelerException( e );
        }
    }
}
