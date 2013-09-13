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
package org.polyglotter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.modeshape.common.collection.Problem;
import org.modeshape.common.collection.Problems;
import org.modeshape.common.logging.Logger;
import org.modeshape.common.util.CheckArg;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.NoSuchRepositoryException;
import org.modeshape.jcr.RepositoryConfiguration;
import org.modeshape.jcr.api.JcrTools;
import org.modeshape.jcr.api.observation.Event.Sequencing;

public final class Polyglotter {
    
    public static final String DEFAULT_MODESHAPE_CONFIGURATION_PATH = "jcr/modeShapeConfig.json";
    
    private String modeShapeConfigurationPath = DEFAULT_MODESHAPE_CONFIGURATION_PATH;
    private ModeShapeEngine modeShape;
    private Session session;
    
    /**
     * @return the path to the ModeShape configuration file. Default is {@value #DEFAULT_MODESHAPE_CONFIGURATION_PATH}.
     */
    public String modeShapeConfigurationPath() {
        return modeShapeConfigurationPath;
    }
    
    /**
     * @return the Polyglotter JCR repository session (never <code>null</code>)
     * @throws PolyglotterException
     */
    public Session session() throws PolyglotterException {
        if ( this.session == null ) {
            modeShape = new ModeShapeEngine();
            modeShape.start();
            try {
                final RepositoryConfiguration config = RepositoryConfiguration.read( modeShapeConfigurationPath );
                final Problems problems = config.validate();
                if ( problems.hasProblems() ) {
                    for ( final Problem problem : problems )
                        Logger.getLogger( getClass() ).error( problem.getMessage(), problem.getThrowable() );
                    throw problems.iterator().next().getThrowable();
                }
                Repository repository;
                try {
                    repository = modeShape.getRepository( config.getName() );
                } catch ( final NoSuchRepositoryException err ) {
                    repository = modeShape.deploy( config );
                }
                session = repository.login( "default" );
                Logger.getLogger( getClass() ).info( PolyglotterI18n.polyglotterStarted );
            } catch ( final Throwable e ) {
                throw new PolyglotterException( e );
            }
        }
        return session;
    }
    
    public void setModeShapeConfigurationPath( final String modeShapeConfigurationPath ) {
        this.modeShapeConfigurationPath = modeShapeConfigurationPath == null ? DEFAULT_MODESHAPE_CONFIGURATION_PATH
                                                                            : modeShapeConfigurationPath;
    }
    
    /**
     * @throws PolyglotterException
     */
    public void stop() throws PolyglotterException {
        if ( session == null ) Logger.getLogger( getClass() ).debug( "Attempt to stop Polyglotter when it is already stopped" );
        else {
            session.logout();
            try {
                modeShape.shutdown().get();
            } catch ( InterruptedException | ExecutionException e ) {
                throw new PolyglotterException( e );
            }
            session = null;
            Logger.getLogger( getClass() ).info( PolyglotterI18n.polyglotterStopped );
        }
    }
    
    /**
     * @param file
     * @param workspaceParentPath
     * @return The node representing the imported file
     * @throws PolyglotterException
     */
    public Node upload( final File file,
                        final String workspaceParentPath ) throws PolyglotterException {
        CheckArg.isNotNull( file, "file" );
        final String path = workspaceParentPath == null ? file.getName() : workspaceParentPath.endsWith( "/" )
                                                                                                              ? workspaceParentPath
                                                                                                                + '/'
                                                                                                                + file.getName()
                                                                                                              : workspaceParentPath
                                                                                                                + file.getName();
        try {
            final ObservationManager observationMgr = session().getWorkspace().getObservationManager();
            final CountDownLatch latch = new CountDownLatch( 1 );
            final EventListener listener = new EventListener() {
                
                @Override
                public void onEvent( final EventIterator events ) {
                    final Event event = events.nextEvent();
                    try {
                        try {
                            if ( event.getType() == Sequencing.NODE_SEQUENCING_FAILURE ) {
                                Logger.getLogger( getClass() ).error( PolyglotterI18n.unableToSequenceUploadedFile, path,
                                                                      event.getInfo().get( Sequencing.SEQUENCING_FAILURE_CAUSE ) );
                            }
                        } catch ( final RepositoryException e ) {
                            throw new RuntimeException( e );
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            };
            observationMgr.addEventListener( listener, Sequencing.ALL, "/", true, null, null, false );
            final Node node = new JcrTools().uploadFile( session, path, file );
            node.addMixin( "poly:unstructured" );
            session.save();
            if ( !latch.await( 15, TimeUnit.SECONDS ) ) Logger.getLogger( getClass() ).debug( "Timed out" );
            observationMgr.removeEventListener( listener );
            return node;
        } catch ( RepositoryException | IOException | InterruptedException e ) {
            throw new PolyglotterException( e );
        }
    }
}
