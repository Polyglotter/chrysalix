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
package org.polyglotter.transformation;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.polyglotter.operation.OperationTestConstants;

@SuppressWarnings( "javadoc" )
public final class TransformationFactoryTest {

    private static final String MSG = "MSG";
    private static final String TX_ID = "TX_ID";

    @Test
    public void shouldCreateDoubleValue() throws Exception {
        final double value = 5;
        final Value< Double > term = TransformationFactory.createValue( OperationTestConstants.DOUBLE_DESCRIPTOR, value );
        assertThat( term, is( notNullValue() ) );
        assertThat( term.get(), is( value ) );
        assertThat( term.descriptor(), is( OperationTestConstants.DOUBLE_DESCRIPTOR ) );
    }

    @Test
    public void shouldCreateErrorValidationProblem() {
        final ValidationProblem problem = TransformationFactory.createError( TX_ID, MSG );
        assertThat( problem, is( notNullValue() ) );
        assertThat( problem.severity(), is( ValidationProblem.Severity.ERROR ) );
        assertThat( problem.isError(), is( true ) );
        assertThat( problem.isWarning(), is( false ) );
        assertThat( problem.isInfo(), is( false ) );
        assertThat( problem.isOk(), is( false ) );
        assertThat( problem.sourceId(), is( TX_ID ) );
        assertThat( problem.message(), is( MSG ) );
    }

    @Test
    public void shouldCreateFloatValue() throws Exception {
        final float value = 5;
        final Value< Float > term = TransformationFactory.createValue( OperationTestConstants.FLOAT_DESCRIPTOR, value );
        assertThat( term, is( notNullValue() ) );
        assertThat( term.get(), is( value ) );
        assertThat( term.descriptor(), is( OperationTestConstants.FLOAT_DESCRIPTOR ) );
    }

    @Test
    public void shouldCreateInfoValidationInfo() {
        final ValidationProblem problem = TransformationFactory.createInfo( TX_ID, MSG );
        assertThat( problem, is( notNullValue() ) );
        assertThat( problem.severity(), is( ValidationProblem.Severity.INFO ) );
        assertThat( problem.isError(), is( false ) );
        assertThat( problem.isWarning(), is( false ) );
        assertThat( problem.isInfo(), is( true ) );
        assertThat( problem.isOk(), is( false ) );
        assertThat( problem.sourceId(), is( TX_ID ) );
        assertThat( problem.message(), is( MSG ) );
    }

    @Test
    public void shouldCreateIntegerValue() throws Exception {
        final int value = 5;
        final Value< Integer > term = TransformationFactory.createValue( OperationTestConstants.INT_DESCRIPTOR, value );
        assertThat( term, is( notNullValue() ) );
        assertThat( term.get(), is( value ) );
        assertThat( term.descriptor(), is( OperationTestConstants.INT_DESCRIPTOR ) );
    }

    @Test
    public void shouldCreateLongValue() throws Exception {
        final long value = 5;
        final Value< Long > term = TransformationFactory.createValue( OperationTestConstants.LONG_DESCRIPTOR, value );
        assertThat( term, is( notNullValue() ) );
        assertThat( term.get(), is( value ) );
        assertThat( term.descriptor(), is( OperationTestConstants.LONG_DESCRIPTOR ) );
    }

    @Test
    public void shouldCreateOkValidationProblem() {
        final ValidationProblem problem = TransformationFactory.createOk( TX_ID, MSG );
        assertThat( problem, is( notNullValue() ) );
        assertThat( problem.severity(), is( ValidationProblem.Severity.OK ) );
        assertThat( problem.isError(), is( false ) );
        assertThat( problem.isWarning(), is( false ) );
        assertThat( problem.isInfo(), is( false ) );
        assertThat( problem.isOk(), is( true ) );
        assertThat( problem.sourceId(), is( TX_ID ) );
        assertThat( problem.message(), is( MSG ) );
    }

    @Test
    public void shouldCreateValidationProblems() {
        final ValidationProblems problems = TransformationFactory.createValidationProblems();
        assertThat( problems, is( notNullValue() ) );
        assertThat( problems.isEmpty(), is( true ) );
    }

    @Test
    public void shouldCreateWarningValidationProblem() {
        final ValidationProblem problem = TransformationFactory.createWarning( TX_ID, MSG );
        assertThat( problem, is( notNullValue() ) );
        assertThat( problem.severity(), is( ValidationProblem.Severity.WARNING ) );
        assertThat( problem.isError(), is( false ) );
        assertThat( problem.isWarning(), is( true ) );
        assertThat( problem.isInfo(), is( false ) );
        assertThat( problem.isOk(), is( false ) );
        assertThat( problem.sourceId(), is( TX_ID ) );
        assertThat( problem.message(), is( MSG ) );
    }

    @Test
    public void shouldGetAllBuiltInOperationDescriptors() {
        assertThat( TransformationFactory.descriptors(), is( notNullValue() ) );
    }

}
