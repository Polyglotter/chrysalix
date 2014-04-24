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
package org.modeshape.modeler;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;

import javax.jcr.Session;

import org.junit.Test;
import org.modeshape.modeler.integration.BaseIntegrationTest;
import org.modeshape.modeler.internal.ModelImpl;
import org.modeshape.modeler.internal.Task;

@SuppressWarnings( "javadoc" )
public class ITModeler extends BaseIntegrationTest {

    private static final String XSD_MODEL_TYPE_ID = "org.modeshape.modeler.xsd.Xsd";

    @Test
    public void shouldDeleteTemporaryArtifactAfterGeneratingModel() throws Exception {
        modelTypeManager().install( "xml" );
        modelTypeManager().install( "sramp" );
        modelTypeManager().install( "xsd" );

        final String path = importArtifact( XSD_ARTIFACT );
        ModelType modelType = null;

        for ( final ModelType type : modelTypeManager().modelTypesForArtifact( path ) ) {
            if ( type.id().equals( XSD_MODEL_TYPE_ID ) ) {
                modelType = type;
                break;
            }
        }

        final Model model = modeler().generateModel( path, ARTIFACT_NAME, modelType, false );
        assertThat( model, notNullValue() );

        manager().run( new Task< Void >() {

            @Override
            public Void run( final Session session ) throws Exception {
                assertThat( session.getRootNode().hasNode( ARTIFACT_NAME ), is( false ) );
                return null;
            }
        } );
    }

    @Test
    public void shouldExportToFile() throws Exception {
        modelTypeManager().install( "java" );
        final String name = ModelImpl.class.getName();
        final File file = new File( "src/main/java/" + name.replace( '.', '/' ) + ".java" );
        assertThat( file.exists(), is( true ) );
        final ModelType modelType = modelTypeManager().modelType( "org.modeshape.modeler.java.JavaFile" );
        assertThat( modelType, notNullValue() );
        final Model model = modeler().generateModel( file, name + ".java", modelType );
        assertThat( model, notNullValue() );
        // TODO complete when issue #7 is closed
    }

    @Test( expected = ModelerException.class )
    public void shouldFailToCreateModelIfTypeIsInapplicable() throws Exception {
        modelTypeManager().install( "xml" );
        modeler().generateModel( importArtifact( "stuff" ), ARTIFACT_NAME, modelTypeManager().modelType( XML_MODEL_TYPE_ID ), true );
    }

    @Test( expected = ModelerException.class )
    public void shouldFailToGenerateModelIfFileIsInvalid() throws Exception {
        modelTypeManager().install( "xml" );
        modeler().generateModel( importArtifact( XML_DECLARATION + "<stuff>" ),
                                 ARTIFACT_NAME,
                                 modelTypeManager().modelType( XML_MODEL_TYPE_ID ), true );
    }

    @Test
    public void shouldGenerateModelOfSuppliedType() throws Exception {
        modelTypeManager().install( "xml" );
        modelTypeManager().install( "sramp" );
        modelTypeManager().install( "xsd" );
        final String path = importArtifact( XSD_ARTIFACT );
        ModelType modelType = null;
        for ( final ModelType type : modelTypeManager().modelTypesForArtifact( path ) ) {
            if ( type.id().equals( XSD_MODEL_TYPE_ID ) ) {
                modelType = type;
                break;
            }
        }
        final Model model = modeler().generateModel( path, ARTIFACT_NAME, modelType, true );
        assertThat( model, notNullValue() );
        manager().run( new Task< Void >() {

            @Override
            public Void run( final Session session ) throws Exception {
                assertThat( session.getRootNode().hasNode( ARTIFACT_NAME ), is( true ) );
                return null;
            }
        } );
    }

    @Test
    public void shouldNotFindDependencyProcessorForXsdModelNode() throws Exception {
        modelTypeManager().install( "sramp" );
        modelTypeManager().install( "xsd" );

        // find XSD model type
        ModelType xsdModelType = null;

        for ( final ModelType type : modelTypeManager().modelTypes() ) {
            if ( type.id().equals( XSD_MODEL_TYPE_ID ) ) {
                xsdModelType = type;
                break;
            }
        }

        assertThat( xsdModelType, notNullValue() );

        final String path = importArtifact( XSD_ARTIFACT );
        final ModelImpl model = ( ModelImpl ) modeler().generateModel( path, ARTIFACT_NAME, xsdModelType, true );
        modeler().manager.run( new Task< Void >() {

            @Override
            public Void run( final Session session ) throws Exception {
                assertThat( model.modelType().dependencyProcessor(), nullValue() );
                return null;
            }
        } );
    }
}
