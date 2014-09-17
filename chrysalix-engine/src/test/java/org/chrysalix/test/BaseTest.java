/*
 * Chrysalix (http://chrysalix.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * Chrysalix is free software. Unless otherwise indicated, all code in Chrysalix
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * Chrysalix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.chrysalix.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.chrysalix.Chrysalix;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

@RunWith( TestRunner.class )
@SuppressWarnings( "javadoc" )
public abstract class BaseTest {

    protected static final String TEST_CONFIGURATION_PATH = "testConfig.json";
    protected static final String TEST_REPOSITORY_STORE_PARENT_PATH;

    protected static final String XML_METAMODEL_CATEGORY = "xml";
    protected static final String XML_METAMODEL_NAME = "org.modelspace.xml.Xml";
    protected static final String XML_DECLARATION = "<?xml version='1.0' encoding='UTF-8'?>";
    protected static final String XML_ROOT = "root";
    protected static final String XML_ROOT_PROPERTY = "property";
    protected static final String XML_LEAF = "leaf";
    protected static final String XML_STRING_VALUE = "string";
    protected static final String XML_SAME_NAME_SIBLING = "sameNameSibling";
    protected static final String XML_ARTIFACT = XML_DECLARATION
                                                 + '<' + XML_ROOT + " " + XML_ROOT_PROPERTY + "='" + XML_STRING_VALUE + "'>"
                                                 + "<" + XML_LEAF + "></" + XML_LEAF + ">"
                                                 + "<" + XML_SAME_NAME_SIBLING + "></" + XML_SAME_NAME_SIBLING + ">"
                                                 + "<" + XML_SAME_NAME_SIBLING + "></" + XML_SAME_NAME_SIBLING + ">"
                                                 + "</" + XML_ROOT + ">";
    protected static final String XML_MODEL_NAME = "XML Model";

    static {
        try {
            final Path path = Files.createTempDirectory( null );
            path.toFile().deleteOnExit();
            TEST_REPOSITORY_STORE_PARENT_PATH = path.toString();
        } catch ( final IOException e ) {
            throw new RuntimeException( e );
        }
    }

    private Chrysalix chrysalix;

    @After
    public void after() throws Exception {
        if ( chrysalix != null ) {
            chrysalix.close();
            chrysalix = null;
        }
        deleteFolder( TEST_REPOSITORY_STORE_PARENT_PATH + "/modelerRepository" );
        deleteFolder( System.getProperty( "java.io.tmpdir" ) + "/modeshape-binary-store" );
    }

    @Before
    @SuppressWarnings( "unused" )
    public void before() throws Exception {
        MockitoAnnotations.initMocks( this );
    }

    private void deleteFolder( final String folder ) throws Exception {
        final File file = new File( folder );
        if ( file.exists() )
            Files.walkFileTree( FileSystems.getDefault().getPath( file.toString() ), new SimpleFileVisitor< Path >() {

                @Override
                public FileVisitResult postVisitDirectory( final Path folder,
                                                           final IOException e ) throws IOException {
                    if ( e != null ) throw e;
                    Files.delete( folder );
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile( final Path file,
                                                  final BasicFileAttributes attrs ) throws IOException {
                    Files.delete( file );
                    return FileVisitResult.CONTINUE;
                }
            } );
    }

    public Chrysalix chrysalix() {
        if ( chrysalix == null )
            chrysalix = new Chrysalix( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_CONFIGURATION_PATH );
        return chrysalix;
    }

    protected InputStream stream( final String content ) {
        return new ByteArrayInputStream( content.getBytes() );
    }
}
