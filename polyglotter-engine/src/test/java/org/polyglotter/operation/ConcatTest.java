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
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.polyglotter.PolyglotterI18n;
import org.polyglotter.TestConstants;
import org.polyglotter.transformation.OperationCategory.BuiltInCategory;

@SuppressWarnings( { "javadoc" } )
public final class ConcatTest {

    private Concat operation;

    @Before
    public void beforeEach() {
        this.operation = new Concat( TestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldConcatMultipleinputs() throws Exception {
        this.operation.addInput( TestConstants.STRING_1_TERM, TestConstants.STRING_2_TERM, TestConstants.STRING_3_TERM );
        assertThat( this.operation.get(), is( TestConstants.STRING_1_VALUE + TestConstants.STRING_2_VALUE + TestConstants.STRING_3_VALUE ) );
    }

    @Test
    public void shouldConcatTermsWithEmptyValues() throws Exception {
        this.operation.addInput( TestConstants.STRING_1_TERM, TestConstants.EMPTY_STRING_TERM, TestConstants.STRING_3_TERM );
        assertThat( this.operation.get(), is( TestConstants.STRING_1_VALUE + TestConstants.EMPTY_STRING_VALUE + TestConstants.STRING_3_VALUE ) );
    }

    @Test
    public void shouldConcatTermsWithNullValues() throws Exception {
        this.operation.addInput( TestConstants.STRING_1_TERM, TestConstants.NULL_STRING_TERM, TestConstants.STRING_3_TERM );
        assertThat( this.operation.get(), is( TestConstants.STRING_1_VALUE + TestConstants.NULL_STRING_VALUE + TestConstants.STRING_3_VALUE ) );
    }

    @Test
    public void shouldConcatTermsWithNumberValues() throws Exception {
        this.operation.addInput( TestConstants.STRING_1_TERM, TestConstants.INT_1_TERM, TestConstants.STRING_3_TERM );
        assertThat( this.operation.get(), is( TestConstants.STRING_1_VALUE + TestConstants.INT_1_VALUE + TestConstants.STRING_3_VALUE ) );
    }

    @Test
    public void shouldHaveCorrectCategory() {
        assertThat( this.operation.categories().size(), is( 1 ) );
        assertThat( this.operation.categories().contains( BuiltInCategory.STRING ), is( true ) );
    }

    @Test
    public void shouldProvideDescription() {
        assertThat( this.operation.description(), is( PolyglotterI18n.concatOperationDescription.text() ) );
    }

    @Test
    public void shouldProvideName() {
        assertThat( this.operation.name(), is( PolyglotterI18n.concatOperationName.text() ) );
    }

}
