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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;

import javax.jcr.Node;
import javax.jcr.Session;

import org.junit.Test;
import org.modeshape.modeler.ModeShapeModeler;
import org.modeshape.modeler.ModelType;
import org.modeshape.modeler.ModelTypeManager;
import org.modeshape.modeler.Modeler;
import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.integration.BaseIntegrationTest;

@SuppressWarnings( "javadoc" )
public class ITModelTypeManager extends BaseIntegrationTest {

    @Test
    public void shouldAllowRequestWhenModelTypeDoesNotHaveDependencyProcessor() throws Exception {
        manager().run( new Task< Void >() {

            @Override
            public Void run( final Session session ) throws Exception {
                final Node rootNode = session.getRootNode();
                final Node modelNode = rootNode.addNode( "elvis" );
                modelNode.addMixin( ModelerLexicon.MODEL_MIXIN );
                assertThat( modelTypeManager().dependencyProcessor( modelNode ), nullValue() );
                return null;
            }
        } );
    }

    @Test( expected = ModelerException.class )
    public void shouldFailToProcessDependenciesWhenNotAModelNode() throws Exception {
        final ModelTypeManagerImpl modelTypeMgr = modelTypeManager();
        modelTypeManager().manager.run( new Task< Void >() {

            @Override
            public Void run( final Session session ) throws Exception {
                final Node rootNode = session.getRootNode();
                modelTypeMgr.dependencyProcessor( rootNode );
                return null;
            }
        } );
    }

    @Test
    public void shouldGetApplicableModelTypeManager() throws Exception {
        modelTypeManager().install( "sramp" );
        modelTypeManager().install( "xsd" );
        final ModelType[] types = modelTypeManager().modelTypesForArtifact( importArtifact( XSD_ARTIFACT ) );
        assertThat( types, notNullValue() );
        assertThat( types.length == 0, is( false ) );
    }

    @Test
    public void shouldGetModelTypeCategories() throws Exception {
        assertThat( modelTypeManager().installableModelTypeCategories().length == 0, is( false ) );
    }

    @Test
    public void shouldIniitializeModelTypeRepositories() throws Exception {
        modeler().close();
        try ( final Modeler modeler = new ModeShapeModeler( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_MODESHAPE_CONFIGURATION_PATH ) ) {
            assertThat( modeler.modelTypeManager().modelTypeRepositories()[ 0 ], is( new URL( ModelTypeManager.JBOSS_MODEL_TYPE_REPOSITORY ) ) );
            assertThat( modeler.modelTypeManager().modelTypeRepositories()[ 1 ], is( new URL( ModelTypeManager.MAVEN_MODEL_TYPE_REPOSITORY ) ) );
        }
    }

    @Test
    public void shouldInstallModelTypes() throws Exception {
        final String[] potentialSequencerClassNames = modelTypeManager().install( "java" );
        assertThat( potentialSequencerClassNames.length == 0, is( true ) );
        assertThat( modelTypeManager().modelTypes().length == 0, is( false ) );
    }

    @Test
    public void shouldOnlyInstallModelTypeCategoryOnce() throws Exception {
        modelTypeManager().install( "java" );
        assertThat( modelTypeManager().modelTypes().length == 0, is( false ) );
        final int size = modelTypeManager().modelTypes().length;
        modelTypeManager().install( "java" );
        assertThat( modelTypeManager().modelTypes().length, is( size ) );
    }

    @Test
    public void shouldReturnUninstantiablePotentialSequencerClassNames() throws Exception {
        String[] potentialSequencerClassNames = modelTypeManager().install( "xsd" );
        assertThat( potentialSequencerClassNames.length == 0, is( false ) );
        potentialSequencerClassNames = modelTypeManager().install( "sramp" );
        assertThat( potentialSequencerClassNames.length == 0, is( true ) );
    }
}
