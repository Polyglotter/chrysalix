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
package org.polyglotter.operation;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.modeshape.modeler.Model;
import org.polyglotter.PolyglotterException;
import org.polyglotter.transformation.OperationCategory.BuiltInCategory;

@SuppressWarnings( { "javadoc" } )
public final class MapTest {

    private static final String SOURCE_ID = Map.SOURCE_MODEL_OBJECT_DESCRIPTOR.id();
    private static final String SOURCE_PROP_ID = Map.SOURCE_PROP_DESCRIPTOR.id();
    private static final String TARGET_ID = Map.TARGET_MODEL_OBJECT_DESCRIPTOR.id();
    private static final String TARGET_PROP_ID = Map.TARGET_PROP_DESCRIPTOR.id();

    private Map operation;

    private void addValidInputs() throws Exception {
        this.operation.addInput( SOURCE_ID, mock( Model.class ) );
        this.operation.addInput( SOURCE_PROP_ID, "sourceProp" );
        this.operation.addInput( TARGET_ID, mock( Model.class ) );
        this.operation.addInput( TARGET_PROP_ID, "targetProp" );
        assertThat( this.operation.problems().isEmpty(), is( true ) );
    }

    @Before
    public void beforeEach() {
        this.operation = new Map( OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test( expected = PolyglotterException.class )
    public void shouldFailGettingResultAfterConstruction() throws Exception {
        this.operation.get();
    }

    @Test
    public void shouldHaveCorrectCategory() {
        assertThat( this.operation.categories().size(), is( 1 ) );
        assertThat( this.operation.categories().contains( BuiltInCategory.ASSIGNMENT ), is( true ) );
    }

    @Test
    public void shouldHaveNullResult() throws Exception {
        addValidInputs();
        assertThat( this.operation.get(), is( nullValue() ) );
    }

    @Test
    public void shouldHaveProblemsAfterConstruction() {
        assertThat( this.operation.problems().isError(), is( true ) );
    }

    @Test
    public void shouldHaveProblemWhenMoreThanOneSourceModel() throws Exception {
        addValidInputs();
        this.operation.addInput( SOURCE_ID, mock( Model.class ) );
        assertThat( this.operation.problems().size(), is( 1 ) );
    }

    @Test
    public void shouldHaveProblemWhenMoreThanOneSourceProperty() throws Exception {
        addValidInputs();
        this.operation.addInput( SOURCE_PROP_ID, "sourceProp2" );
        assertThat( this.operation.problems().size(), is( 1 ) );
    }

    @Test
    public void shouldHaveProblemWhenMoreThanOneTargetModel() throws Exception {
        addValidInputs();
        this.operation.addInput( TARGET_ID, mock( Model.class ) );
        assertThat( this.operation.problems().size(), is( 1 ) );
    }

    @Test
    public void shouldHaveProblemWhenMoreThanOneTargetProperty() throws Exception {
        addValidInputs();
        this.operation.addInput( TARGET_PROP_ID, "targetProp2" );
        assertThat( this.operation.problems().size(), is( 1 ) );
    }

    @Test
    public void shouldHaveProblemWhenSourceModelHasWrongType() throws Exception {
        addValidInputs();
        this.operation.addInput( SOURCE_ID, "wrongType" );
        assertThat( this.operation.problems().size(), is( 1 ) );
    }

    @Test
    public void shouldHaveProblemWhenSourcePropertyHasWrongType() throws Exception {
        addValidInputs();
        this.operation.addInput( SOURCE_PROP_ID, true );
        assertThat( this.operation.problems().size(), is( 1 ) );
    }

    @Test
    public void shouldHaveProblemWhenTargetModelHasWrongType() throws Exception {
        addValidInputs();
        this.operation.addInput( TARGET_ID, "wrongType" );
        assertThat( this.operation.problems().size(), is( 1 ) );
    }

    @Test
    public void shouldHaveProblemWhenTargetPropertyHasWrongType() throws Exception {
        addValidInputs();
        this.operation.addInput( TARGET_PROP_ID, 5 );
        assertThat( this.operation.problems().size(), is( 1 ) );
    }

}
