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
package org.polyglotter;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.modeshape.modeler.Model;
import org.modeshape.modeler.ModelObject;
import org.polyglotter.test.BaseTest;

@SuppressWarnings( "javadoc" )
public class ITPolyglotter extends BaseTest {

    @Test
    public void shouldCreateExecutableCloneModelTransform() throws Exception {
        polyglotter().modelTypeManager().install( XML_MODEL_TYPE_CATEGORY );
        final Model source = polyglotter().generateModel( stream( XML_ARTIFACT ),
                                                          "source",
                                                          polyglotter().modelTypeManager().modelType( XML_MODEL_TYPE_NAME ) );
        final ModelTransform xform = polyglotter().createCloneModelTransform( source, "target" );
        assertThat( xform, notNullValue() );
        assertThat( xform.transforms().size(), is( 6 ) );
        xform.execute();
        final Model target = polyglotter().model( "target" );
        assertThat( target, notNullValue() );
        assertThat( target.name(), is( "target" ) );
        verifyTarget( source, target );
    }

    private void verifyTarget( final ModelObject source,
                               final ModelObject target ) throws Exception {
        assertThat( target.propertyNames().length, is( source.propertyNames().length ) );
        assertThat( target.children().length, is( source.children().length ) );
        for ( final String name : source.propertyNames() )
            assertThat( target.stringValue( name ), is( source.stringValue( name ) ) );
        for ( final ModelObject sourceChild : source.children() ) {
            final ModelObject targetChild = target.child( sourceChild.name() );
            assertThat( targetChild, notNullValue() );
            verifyTarget( sourceChild, targetChild );
        }
    }
}
