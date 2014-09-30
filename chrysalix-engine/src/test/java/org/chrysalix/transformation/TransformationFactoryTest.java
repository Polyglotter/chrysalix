/*
 * Chrysalix
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * Chrysalix is free software. Unless otherwise indicated, all code in Chrysalix
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * Chrysalix is distributed in the hope that it will be useful,
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
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.chrysalix.operation.OperationTestConstants;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.modelspace.Model;

@Ignore
@SuppressWarnings( "javadoc" )
public final class TransformationFactoryTest {

    private static final String MSG = "MSG";

    private TransformationTestFactory factory;

    @Before
    public void constructFactory() {
        this.factory = new TransformationTestFactory();
    }

    @Test
    public void shouldCreateErrorValidationProblem() {
        final ValidationProblem problem = TransformationFactory.createError( OperationTestConstants.TRANSFORM_ID, MSG );
        assertThat( problem, is( notNullValue() ) );
        assertThat( problem.severity(), is( ValidationProblem.Severity.ERROR ) );
        assertThat( problem.isError(), is( true ) );
        assertThat( problem.isWarning(), is( false ) );
        assertThat( problem.isInfo(), is( false ) );
        assertThat( problem.isOk(), is( false ) );
        assertThat( problem.sourceId(), is( OperationTestConstants.TRANSFORM_ID ) );
        assertThat( problem.message(), is( MSG ) );
    }

    @Test
    public void shouldCreateInfoValidationInfo() {
        final ValidationProblem problem = TransformationFactory.createInfo( OperationTestConstants.TRANSFORM_ID, MSG );
        assertThat( problem, is( notNullValue() ) );
        assertThat( problem.severity(), is( ValidationProblem.Severity.INFO ) );
        assertThat( problem.isError(), is( false ) );
        assertThat( problem.isWarning(), is( false ) );
        assertThat( problem.isInfo(), is( true ) );
        assertThat( problem.isOk(), is( false ) );
        assertThat( problem.sourceId(), is( OperationTestConstants.TRANSFORM_ID ) );
        assertThat( problem.message(), is( MSG ) );
    }

    @Test
    public void shouldCreateOkValidationProblem() {
        final ValidationProblem problem = TransformationFactory.createOk( OperationTestConstants.TRANSFORM_ID, MSG );
        assertThat( problem, is( notNullValue() ) );
        assertThat( problem.severity(), is( ValidationProblem.Severity.OK ) );
        assertThat( problem.isError(), is( false ) );
        assertThat( problem.isWarning(), is( false ) );
        assertThat( problem.isInfo(), is( false ) );
        assertThat( problem.isOk(), is( true ) );
        assertThat( problem.sourceId(), is( OperationTestConstants.TRANSFORM_ID ) );
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
        final ValidationProblem problem = TransformationFactory.createWarning( OperationTestConstants.TRANSFORM_ID, MSG );
        assertThat( problem, is( notNullValue() ) );
        assertThat( problem.severity(), is( ValidationProblem.Severity.WARNING ) );
        assertThat( problem.isError(), is( false ) );
        assertThat( problem.isWarning(), is( true ) );
        assertThat( problem.isInfo(), is( false ) );
        assertThat( problem.isOk(), is( false ) );
        assertThat( problem.sourceId(), is( OperationTestConstants.TRANSFORM_ID ) );
        assertThat( problem.message(), is( MSG ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToCreatTransformationIfParentPathIsEmpty() throws Exception {
        this.factory.createTransformation( "" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToCreatTransformationIfParentPathIsNull() throws Exception {
        this.factory.createTransformation( ( Model ) null );
    }

    @Test
    public void shouldGetAllBuiltInOperationDescriptors() {
        assertThat( this.factory.descriptors(), is( notNullValue() ) );
    }

}
