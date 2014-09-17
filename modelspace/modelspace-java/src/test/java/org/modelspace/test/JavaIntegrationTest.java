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
package org.modelspace.test;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.modelspace.Metamodel;
import org.modelspace.Model;
import org.modelspace.test.BaseIntegrationTest;

@SuppressWarnings( "javadoc" )
public abstract class JavaIntegrationTest extends BaseIntegrationTest {

    protected static final String CATEGORY = "java";
    protected static final String METAMODEL_ID = "org.modelspace.java.JavaFile";

    protected static final String MODEL_NAME = "Mock.java";
    protected static final String MODEL_PATH = "src/test/resources/" + MODEL_NAME;
    protected static final File MODEL_FILE = new File( MODEL_PATH );

    @Override
    @Before
    public void before() throws Exception {
        super.before();
        metamodelManager().install( CATEGORY );
    }

    protected Model importModel() throws Exception {
        final Model model = modelspace().importModel( MODEL_FILE, null, metamodel() );
        assertThat( model, notNullValue() );
        return model;
    }

    protected Metamodel metamodel() throws Exception {
        final Metamodel metamodel = modelspace().metamodelManager().metamodel( METAMODEL_ID );
        assertThat( metamodel, notNullValue() );
        return metamodel;
    }
}
