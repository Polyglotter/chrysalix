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
package org.polyglotter.eclipse.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.jcr.Session;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.modeshape.modeler.ModeShapeModeler;
import org.modeshape.modeler.Model;
import org.modeshape.modeler.ModelType;
import org.modeshape.modeler.ModelTypeManager;
import org.modeshape.modeler.TestUtil;
import org.modeshape.modeler.internal.Manager;
import org.modeshape.modeler.internal.ModelTypeManagerImpl;
import org.modeshape.modeler.internal.Task;
import org.polyglotter.eclipse.GuiTestUtil;
import org.polyglotter.eclipse.focustree.FocusTree;
import org.polyglotter.eclipse.focustree.FocusTree.ViewModel;
import org.polyglotter.eclipse.view.ModelContentProvider.PropertyModel;

@SuppressWarnings( { "javadoc" } )
public final class GuiTestModelContentProvider {

    private static final URL MODEL_TYPE_REPOSITORY;
    private static final String TEST_MODESHAPE_CONFIGURATION_PATH = "testModeShapeConfig.json";
    private static final String TEST_REPOSITORY_STORE_PARENT_PATH;

    private static ModeShapeModeler _modeler;

    static {
        try {
            MODEL_TYPE_REPOSITORY = new URL( "file:src/test/resources/" );
            final Path path = Files.createTempDirectory( null );
            path.toFile().deleteOnExit();
            TEST_REPOSITORY_STORE_PARENT_PATH = path.toString();
        } catch ( final IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public static void main( final String[] args ) {
        try {
            final ModelTypeManager modelTypeManager = modelTypeManager();
            final Model model = manager().run( new Task< Model >() {

                @Override
                public Model run( final Session session ) throws Exception {
                    modelTypeManager.install( "sramp" );
                    modelTypeManager.install( "xsd" );

                    final ModelType xsdModelType = modelTypeManager.modelType( "org.modeshape.modeler.xsd.Xsd" );
                    return modeler().generateModel( new File( GuiTestModelContentProvider.class.getClassLoader().getResource( "Books.xsd"
                                                                                               ).toURI() ),
                                                    "/test",
                                                    xsdModelType );
                }
            } );

            final FocusTree focusTree = new FocusTree( GuiTestUtil.shell(), model, new ModelContentProvider() );
            setViewModel( focusTree );

            GuiTestUtil.show( focusTree );
        } catch ( final Exception e ) {
            e.printStackTrace();
        }
    }

    private static Manager manager() throws Exception {
        return TestUtil.manager( modeler() );
    }

    static ModeShapeModeler modeler() throws Exception {
        if ( _modeler == null ) {
            _modeler =
                new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_MODESHAPE_CONFIGURATION_PATH );

            for ( final URL url : _modeler.modelTypeManager().modelTypeRepositories() ) {
                _modeler.modelTypeManager().unregisterModelTypeRepository( url );
            }

            _modeler.modelTypeManager().registerModelTypeRepository( MODEL_TYPE_REPOSITORY );
        }

        return _modeler;
    }

    private static ModelTypeManagerImpl modelTypeManager() throws Exception {
        return ( ModelTypeManagerImpl ) modeler().modelTypeManager();
    }

    private static void setViewModel( final FocusTree focusTree ) {
        focusTree.setViewModel( new ViewModel() {

            @Override
            public Color cellBackgroundColor( final Object obj ) {
                if ( obj instanceof PropertyModel ) return Display.getCurrent().getSystemColor( SWT.COLOR_INFO_BACKGROUND );
                return Display.getCurrent().getSystemColor( SWT.COLOR_WHITE );
            }

            @Override
            public boolean initialIndexIsOne() {
                return true;
            }
        } );
    }

}
