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
package org.modelspace.xsd;

import org.junit.Test;
import org.modelspace.xsd.test.XsdIntegrationTest;

@SuppressWarnings( "javadoc" )
public class ITXsdDesquencerTest extends XsdIntegrationTest {

    @Test
    public void should() {}

    // @Test
    // public void shouldExport() throws Exception {
    // metamodelManager().install( SRAMP_METAMODEL_CATEGORY );
    // metamodelManager().install( XSD_METAMODEL_CATEGORY );
    // final Model model = modelspace().generateModel( new File( "src/test/resources/Books/Books.xsd" ),
    // null,
    // metamodelManager().metamodel( XSD_METAMODEL_ID ) );
    // final Exporter exporter = model.metamodel().exporter();
    // assertThat( exporter, is( notNullValue() ) );
    //
    // try ( final ByteArrayOutputStream stream = new ByteArrayOutputStream() ) {
    // exporter.execute( model, stream );
    // assertThat( stream.toString().startsWith( XML_DECLARATION + "\n<xsd:schema " ), is( true ) );
    // }
    // }
}
