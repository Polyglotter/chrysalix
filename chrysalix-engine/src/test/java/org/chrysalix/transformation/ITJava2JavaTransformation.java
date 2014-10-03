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
package org.chrysalix.transformation;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.chrysalix.operation.Map;
import org.chrysalix.operation.OperationTestConstants;
import org.chrysalix.test.BaseTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.modelspace.Descriptor;
import org.modelspace.Metamodel;
import org.modelspace.Model;
import org.modelspace.ModelObject;
import org.modelspace.ModelProperty;
import org.modelspace.Modelspace;

@Ignore
@SuppressWarnings( { "javadoc" } )
public final class ITJava2JavaTransformation extends BaseTest {

    protected static final String CATEGORY = "java";
    private static final String METAMODEL_ID = "org.modeshape.modeler.java.JavaFile";

    private static final String SOURCE_MODEL_NAME = "RocketSkates.java";
    private static final String SOURCE_MODEL_PATH = "src/test/resources/org/acme/" + SOURCE_MODEL_NAME;

    private static final String TARGET_MODEL_NAME = "RocketSled.java";
    private static final String TARGET_MODEL_PATH = "src/test/resources/org/acme/" + TARGET_MODEL_NAME;

    private static final URL INTEGRATION_TEST_METAMODEL_REPOSITORY_URL;

    static {
        try {
            INTEGRATION_TEST_METAMODEL_REPOSITORY_URL = new URL( "file:../../integration-test-metamodel-repository" );
        } catch ( final IOException e ) {
            throw new RuntimeException( e );
        }
    }

    private TransformationFactory factory;

    @Before
    public void beforeEach() throws Exception {
        this.factory = new TransformationFactory( mock( Modelspace.class ) );
        chrysalix().metamodelManager().registerMetamodelRepository( INTEGRATION_TEST_METAMODEL_REPOSITORY_URL );
    }

    private ModelObject findModelObject( final ModelObject modelObject,
                                         final String primaryTypeNameToMatch ) throws Exception {
        // find first model object with specified primary type
        for ( final ModelObject child : modelObject.children() ) {
            final Descriptor primaryTypeDescriptor = child.primaryType();

            if ( primaryTypeDescriptor.name().equals( primaryTypeNameToMatch ) ) {
                return child;
            }

            // recurse
            final ModelObject result = findModelObject( child, primaryTypeNameToMatch );

            if ( result != null ) {
                return result;
            }
        }

        return null;
    }

    @Before
    public void installCategory() throws Exception {
        chrysalix().metamodelManager().install( CATEGORY );
    }

    private Metamodel metamodel() throws Exception {
        return chrysalix().metamodelManager().metamodel( METAMODEL_ID );
    }

    @Test
    public void shouldMapFieldToField() throws Exception {
        final String nodeType = "class:field";
        final String sourceFieldName = "skates";
        final String propName = "class:final";
        final String targetFieldName = "sled";

        final Transformation transform = this.factory.createTransformation( OperationTestConstants.TRANSFORM_ID );
        final Operation< ? > mapOp = Map.DESCRIPTOR.newInstance( mock( ModelObject.class ), transform );
        transform.add( mapOp );

        // setup source
        final File sourceFile = new File( SOURCE_MODEL_PATH );
        final Model sourceModel = chrysalix().importModel( sourceFile, "test", metamodel() );
        assertThat( sourceModel, is( notNullValue() ) );
        transform.addSource( sourceModel );

        // find source model object
        final ModelObject sourceModelObject = findModelObject( sourceModel, nodeType );
        assertThat( sourceModelObject, is( notNullValue() ) );
        assertThat( sourceModelObject.name(), is( sourceFieldName ) );

        // find source model object property
        final ModelProperty sourceProperty = sourceModelObject.property( propName );
        assertThat( sourceProperty, is( notNullValue() ) );
        final boolean sourcePropValue = sourceProperty.booleanValue();
        assertThat( sourceProperty.booleanValue(), is( true ) );
        mapOp.addInput( Map.SOURCE_PROP_DESCRIPTOR.name(), sourceProperty );

        // setup target
        final File targetFile = new File( TARGET_MODEL_PATH );
        final Model targetModel = chrysalix().importModel( targetFile, "test", metamodel() );
        assertThat( targetModel, is( notNullValue() ) );
        transform.addTarget( targetModel );

        // find target model object
        final ModelObject targetModelObject = findModelObject( targetModel, nodeType );
        assertThat( targetModelObject, is( notNullValue() ) );
        assertThat( targetModelObject.name(), is( targetFieldName ) );

        // find target model object property
        final ModelProperty targetProperty = targetModelObject.property( propName );
        assertThat( targetProperty, is( notNullValue() ) );
        final boolean targetOldValue = targetProperty.booleanValue();
        assertThat( targetProperty.booleanValue(), is( false ) );
        assertThat( targetProperty.booleanValue(), is( not( sourcePropValue ) ) );
        mapOp.addInput( Map.TARGET_PROP_DESCRIPTOR.name(), targetProperty );

        // run map operation
        mapOp.get();
        assertThat( targetProperty.booleanValue(), is( sourcePropValue ) ); // make sure this changed
        assertThat( targetProperty.booleanValue(), is( not( targetOldValue ) ) );
        assertThat( sourceProperty.booleanValue(), is( sourcePropValue ) ); // make sure this did not change
    }

}
