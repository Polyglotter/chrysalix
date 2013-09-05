/*
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.
 *
 * This software is made available by Red Hat, Inc. under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * See the AUTHORS.txt file in the distribution for a full listing of
 * individual contributors.
 */
package org.jboss.xform;

import static org.modeshape.jcr.api.RepositoryFactory.URL;

import java.util.Collections;
import java.util.Map;
import java.util.ServiceLoader;

import javax.jcr.Repository;
import javax.jcr.RepositoryFactory;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public final class XFormEngine {
    
    private static final Logger LOGGER = LoggerFactory.getLogger( XFormEngine.class );
    
    private final String configurationFilePath;
    
    private Repository repository;
    
    private Session session;
    
    /**
     * @param configurationFilePath
     */
    public XFormEngine( final String configurationFilePath ) {
        this.configurationFilePath = configurationFilePath;
    }
    
    /**
     * Starts the engine if not already started.
     * 
     * @return the XForm JCR repository session (never <code>null</code>)
     * @throws Exception
     *             if there is a problem obtaining the session
     */
    public Session session() throws Exception {
        start();
        
        if ( this.session == null ) {
            this.session = this.repository.login( "default" );
            LOGGER.debug( "XForm engined stopped" );
        }
        
        return this.session;
    }
    
    /**
     * Obtains the XForm repository from ModeShape.
     * 
     * @throws Exception
     *             if there is a problem starting the XForm engine
     */
    public void start() throws Exception {
        if ( this.repository == null ) {
            final String configUrl = ( "file:" + this.configurationFilePath );
            final Map< String, String > params = Collections.singletonMap( URL, configUrl );
            
            for ( final RepositoryFactory factory : ServiceLoader.load( RepositoryFactory.class ) ) {
                this.repository = factory.getRepository( params );
                
                if ( this.repository != null ) {
                    LOGGER.debug( "XForm engine started using configuration file '" + this.configurationFilePath + '\'' );
                    break;
                }
            }
            
            if ( this.repository == null ) {
                throw new Exception( "XForm engine cannot be started using configuration file '" + this.configurationFilePath
                                     + '\'' ); // TODO i18n this
            }
        } else {
            LOGGER.info( "Attempt to start XForm engine when it is already started." ); // TODO i18n this
        }
    }
    
    /**
     * 
     */
    public void stop() {
        if ( this.repository == null ) LOGGER.debug( "Attempt to stop XForm engine when it is already stopped" );
        else {
            if ( this.session != null ) {
                this.session.logout();
                this.session = null;
            }
            
            this.repository = null;
            LOGGER.debug( "XForm engine stopped" );
        }
    }
    
}
