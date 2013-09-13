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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link Polyglotter}.
 */
@RunWith( TestRunner.class )
public final class PolyglotterTest {
    
    private static final String TEST_MODESHAPE_CONFIGURATION_PATH = "testModeShapeConfig.json";
    
    private Polyglotter engine;
    
    @After
    public void after() throws PolyglotterException {
        final List< String > errMsgs = TestLogger.errorMessages();
        final List< String > warnMsgs = TestLogger.warningMessages();
        engine.stop();
        // Clear remaining captured messages
        TestLogger.errorMessages();
        TestLogger.warningMessages();
        TestLogger.infoMessages();
        assertThat( "Errors: " + errMsgs.toString(), errMsgs.isEmpty(), is( true ) );
        assertThat( "Warnings: " + warnMsgs.toString(), warnMsgs.isEmpty(), is( true ) );
    }
    
    @Before
    public void before() {
        this.engine = new Polyglotter();
    }
    
    @Test
    public void shouldFailToUploadBadXml() throws Exception {
        upload( "bad.xml" );
        assertThat( TestLogger.errorMessages().isEmpty(), is( false ) );
    }
    
    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToUploadNonExistingFile() throws Exception {
        engine.upload( new File( "dummy.file" ), "" );
    }
    
    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToUploadNullFile() throws Exception {
        engine.upload( null, "" );
    }
    
    @Test
    public void shouldObtainNewSessionAfterStop() throws Exception {
        final Session session = engine.session();
        assertThat( session, notNullValue() );
        engine.stop();
        final Session newSession = engine.session();
        assertThat( newSession, notNullValue() );
        assertThat( newSession, not( session ) );
    }
    
    @Test
    public void shouldObtainSession() throws Exception {
        assertThat( this.engine.session(), is( notNullValue() ) );
    }
    
    @Test
    public void shouldReturnChangedModeShapeConfigurationPath() {
        engine.setModeShapeConfigurationPath( TEST_MODESHAPE_CONFIGURATION_PATH );
        assertThat( engine.modeShapeConfigurationPath(), is( TEST_MODESHAPE_CONFIGURATION_PATH ) );
    }
    
    @Test
    public void shouldReturnDefaultModeShapeConfigurationPath() {
        assertThat( new Polyglotter().modeShapeConfigurationPath(), is( Polyglotter.DEFAULT_MODESHAPE_CONFIGURATION_PATH ) );
    }
    
    @Test
    public void shouldReturnDefaultModeShapeConfigurationPathIfSetToNull() {
        engine.setModeShapeConfigurationPath( TEST_MODESHAPE_CONFIGURATION_PATH );
        assertThat( engine.modeShapeConfigurationPath(), is( TEST_MODESHAPE_CONFIGURATION_PATH ) );
        engine.setModeShapeConfigurationPath( null );
        assertThat( new Polyglotter().modeShapeConfigurationPath(), is( Polyglotter.DEFAULT_MODESHAPE_CONFIGURATION_PATH ) );
    }
    
    @Test
    public void shouldUploadAndSequenceXml() throws Exception {
        final Node node = upload( "pom.xml" );
        assertThat( node.getNode( "modexml:document" ), notNullValue() );
    }
    
    @Test
    public void shouldUploadAndSequenceXsd() throws Exception {
        final Node node = upload( "Books.xsd" );
        assertThat( node.getNode( "xs:schemaDocument" ), notNullValue() );
    }
    
    private Node upload( final String file ) throws Exception {
        final Node node = engine.upload( new File( getClass().getClassLoader().getResource( file ).toURI() ), null );
        assertThat( node, notNullValue() );
        assertThat( node.getNode( "jcr:content" ), notNullValue() );
        assertThat( node.getNode( "jcr:content" ).getProperty( "jcr:data" ), notNullValue() );
        return node;
    }
}
