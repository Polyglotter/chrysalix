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
package org.chrysalix.operation;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.chrysalix.ChrysalixI18n;
import org.chrysalix.transformation.TransformationTestFactory;
import org.chrysalix.transformation.Value;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.modelspace.ModelObject;

@Ignore
@SuppressWarnings( { "javadoc" } )
public final class ConcatTest {

    private static final String ID = Concat.TERM_DESCRIPTOR.name();
    private static TransformationTestFactory FACTORY;

    private static Value< Object > EMPTY_STRING_TERM;
    private static Value< Object > INT_TERM;
    private static Value< Object > NULL_STRING_TERM;
    private static Value< Object > STRING_TERM;
    private static Value< Object > STRING2_TERM;
    private static Value< Object > STRING3_TERM;

    @BeforeClass
    public static void initializeConstants() throws Exception {
        FACTORY = new TransformationTestFactory();
        EMPTY_STRING_TERM =
            FACTORY.createObjectValue( "/my/path/empty", Concat.TERM_DESCRIPTOR, OperationTestConstants.EMPTY_STRING_VALUE );
        INT_TERM = FACTORY.createObjectValue( "/my/path/int", Concat.TERM_DESCRIPTOR, OperationTestConstants.INT_1_VALUE );
        NULL_STRING_TERM =
            FACTORY.createObjectValue( "/my/path/null", Concat.TERM_DESCRIPTOR, OperationTestConstants.NULL_STRING_VALUE );
        STRING_TERM = FACTORY.createObjectValue( "/my/path/string", Concat.TERM_DESCRIPTOR, OperationTestConstants.STRING_1_VALUE );
        STRING2_TERM =
            FACTORY.createObjectValue( "/my/path/string2", Concat.TERM_DESCRIPTOR, OperationTestConstants.STRING_2_VALUE );
        STRING3_TERM =
            FACTORY.createObjectValue( "/my/path/string3", Concat.TERM_DESCRIPTOR, OperationTestConstants.STRING_3_VALUE );
    }

    private ModelObject modelObject;
    private Concat operation;

    @Before
    public void beforeEach() throws Exception {
        this.modelObject = mock( ModelObject.class );
        this.operation = new Concat( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION );
    }

    @Test
    public void shouldConcatMultipleinputs() throws Exception {
        this.operation.addInput( ID, STRING_TERM, STRING2_TERM, STRING3_TERM );
        assertThat( this.operation.get(),
                    is( STRING_TERM.get().toString() + STRING2_TERM.get().toString() + STRING3_TERM.get().toString() ) );
    }

    @Test
    public void shouldConcatTermsWithEmptyValues() throws Exception {
        this.operation.addInput( ID, STRING_TERM, EMPTY_STRING_TERM, STRING3_TERM );
        assertThat( this.operation.get(),
                    is( STRING_TERM.get().toString() + EMPTY_STRING_TERM.get().toString() + STRING3_TERM.get().toString() ) );
    }

    @Test
    public void shouldConcatTermsWithNullValues() throws Exception {
        this.operation.addInput( ID, STRING_TERM, NULL_STRING_TERM, STRING3_TERM );
        assertThat( this.operation.get(),
                    is( STRING_TERM.get().toString() + NULL_STRING_TERM.get() + STRING3_TERM.get().toString() ) );
    }

    @Test
    public void shouldConcatTermsWithNumberValues() throws Exception {
        this.operation.addInput( ID, STRING_TERM, INT_TERM, STRING3_TERM );
        assertThat( this.operation.get(),
                    is( STRING_TERM.get().toString() + INT_TERM.get().toString() + STRING3_TERM.get().toString() ) );
    }

    @Test
    public void shouldCreateOperation() throws Exception {
        assertThat( Concat.DESCRIPTOR.newInstance( this.modelObject, OperationTestConstants.TEST_TRANSFORMATION ),
                    is( instanceOf( Concat.class ) ) );
    }

    @Test
    public void shouldProvideDescription() throws Exception {
        assertThat( this.operation.descriptor().description(), is( ChrysalixI18n.localize( Concat.DESCRIPTION ) ) );
    }

    @Test
    public void shouldProvideName() throws Exception {
        assertThat( this.operation.name(), is( ChrysalixI18n.localize( Concat.NAME ) ) );
    }

}
