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
package org.modeshape.modeler.java;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.modeshape.jcr.JcrLexicon;
import org.modeshape.modeler.ModelObject;
import org.modeshape.modeler.test.BaseTest;

@SuppressWarnings( "javadoc" )
public class ITJava extends BaseTest {

    private static final String CATEGORY = "java";
    private static final String MODEL_NAME = "src/integration/resources/org/modeshape/modeler/java/Mock.java";
    private static final String JAVA_ID = "org.modeshape.modeler.java.JavaFile";

    @Override
    @Before
    public void before() throws Exception {
        modelTypeManager().registerModelTypeRepository( modelTypeRepository() );
        super.before();
    }

    private ModelObject modelObject() throws Exception {
        modelTypeManager().install( CATEGORY );
        final ModelObject modelObject =
            modeler().generateModel( new File( MODEL_NAME ), MODEL_NAME, modelTypeManager().modelType( JAVA_ID ) );
        assertThat( modelObject, notNullValue() );
        return modelObject;
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetBooleanValueIfNonBooleanProperty() throws Exception {
        modelObject().booleanValue( JcrLexicon.PRIMARY_TYPE.toString() );
    }
}
