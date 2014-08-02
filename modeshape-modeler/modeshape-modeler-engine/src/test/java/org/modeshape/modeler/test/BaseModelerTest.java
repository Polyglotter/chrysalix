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
package org.modeshape.modeler.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.After;
import org.modeshape.modeler.ModeShapeModeler;
import org.modeshape.modeler.MetamodelManager;

@SuppressWarnings( "javadoc" )
public abstract class BaseModelerTest extends BaseTest {

    protected static final String TEST_MODESHAPE_CONFIGURATION_PATH = "testModeShapeConfig.json";
    protected static final String TEST_REPOSITORY_STORE_PARENT_PATH;

    protected static final URL MODULE_TEST_METAMODEL_REPOSITORY_URL;
    protected static final URL INTEGRATION_TEST_METAMODEL_REPOSITORY_URL;

    static {
        try {
            final Path path = Files.createTempDirectory( null );
            path.toFile().deleteOnExit();
            TEST_REPOSITORY_STORE_PARENT_PATH = path.toString();

            MODULE_TEST_METAMODEL_REPOSITORY_URL = new URL( "file:target" );
            INTEGRATION_TEST_METAMODEL_REPOSITORY_URL = new URL( "file:../../integration-test-metamodel-repository" );
        } catch ( final IOException e ) {
            throw new RuntimeException( e );
        }
    }

    private ModeShapeModeler modeler;

    @After
    public void after() throws Exception {
        if ( modeler != null ) {
            modeler.close();
            modeler = null;
        }
        deleteFolder( TEST_REPOSITORY_STORE_PARENT_PATH + "/modelerRepository" );
        deleteFolder( System.getProperty( "java.io.tmpdir" ) + "/modeshape-binary-store" );
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

    public ModeShapeModeler modeler() throws Exception {
        if ( modeler == null ) {
            modeler = new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_MODESHAPE_CONFIGURATION_PATH );
            for ( final URL url : modeler.metamodelManager().metamodelRepositories() )
                modeler.metamodelManager().unregisterMetamodelRepository( url );
            metamodelManager().registerMetamodelRepository( MODULE_TEST_METAMODEL_REPOSITORY_URL );
            metamodelManager().registerMetamodelRepository( INTEGRATION_TEST_METAMODEL_REPOSITORY_URL );
        }
        return modeler;
    }

    public MetamodelManager metamodelManager() throws Exception {
        return modeler().metamodelManager();
    }
}
