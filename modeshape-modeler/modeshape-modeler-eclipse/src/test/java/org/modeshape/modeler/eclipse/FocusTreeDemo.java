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
package org.modeshape.modeler.eclipse;

import java.io.File;

import org.modeshape.modeler.ui.FocusTreeController;

@SuppressWarnings( { "javadoc" } )
public class FocusTreeDemo {

    public static void main( final String[] args ) /*throws IOException*/{
        final FocusTreeController controller = new FocusTreeController( new FocusTreeFileSystemModel() );
        final FocusTree focusTree = new FocusTree( DemoUtil.shell(), controller );
        controller.setRoot( new File( System.getProperty( "user.home" ) ) );
        DemoUtil.show( focusTree );
        // // jpav: remove
        // System.out.println( "Creating temp folder..." );
        // final Path path = Files.createTempDirectory( null );
        // path.toFile().deleteOnExit();
        // // jpav: remove
        // System.out.println( "Creating Modeler..." );
        // try ( final ModeShapeModeler modeler = new ModeShapeModeler( path.toString(), "testConfig.json" ) ) {
        // // jpav: remove
        // System.out.println( "Registering metamodel repository..." );
        // modeler.modelTypeManager().registerModelTypeRepository( new URL( "file:resources" ) );
        // final ModelTypeManager modelTypeManager = modeler.modelTypeManager();
        // // jpav: remove
        // System.out.println( "Installing S-RAMP..." );
        // modelTypeManager.install( "sramp" );
        // // jpav: remove
        // System.out.println( "Installing XSD..." );
        // modelTypeManager.install( "xsd" );
        // // jpav: remove
        // System.out.println( "Generating Books.xsd model..." );
        // final File file = new File( "src/test/resources/Books.xsd" );
        // final ModelType xsdModelType = modelTypeManager.modelType( "org.modeshape.modeler.xsd.Xsd" );
        // final Model root = modeler.generateModel( file, "/test", xsdModelType );
        // // jpav: remove
        // System.out.println( "Creating FocusTree..." );
        // final FocusTree focusTree =
        // new FocusTree( DemoUtil.shell(), new FocusTreeController( new FocusTreeXsdModel() ) );
        // // jpav: remove
        // System.out.println( "Showing FocusTree..." );
        // DemoUtil.show( focusTree );
        // } catch ( final Exception e ) {
        // e.printStackTrace();
        // }
    }
}
