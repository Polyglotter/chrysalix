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
package org.modelspace.ddl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;
import org.modelspace.Descriptor;
import org.modelspace.Model;
import org.modelspace.ModelObject;
import org.modelspace.ddl.TeiidDdlLexicon;

@SuppressWarnings( "javadoc" )
public class ITTeiidDdl extends TeiidDdlIntegrationTest {

    @Test
    public void shouldGetDdlModel() throws Exception {

        metamodelManager().install( TeiidDdlLexicon.DDL_METAMODEL_CATEGORY );

        final File dataFile = new File( "src/test/resources/Teiid-MySQLAccounts.ddl" );
        final Model generatedModel =
            modelspace().importModel( dataFile, null, metamodelManager().metamodel( TeiidDdlLexicon.DDL_METAMODEL_ID ) );
        final Model model = modelspace().model( dataFile.getName() );
        assertThat( model, is( generatedModel ) );

        for ( final ModelObject mo : model.children() ) {
            System.out.println( "  CHILD >>>> MO = " + mo.name() );
            for ( final ModelObject statement : mo.children() ) {
                System.out.println( "  CHILD >>>> STATEMENT = " + statement.name() );
                // System.out.println("  CHILD >>>> STATEMENT = " + statement.name());
                if ( statement.hasProperties() ) {
                    for ( final String prop : statement.propertyNames() ) {
                        System.out.println( "  CHILD >>>> Property = " + prop.toString() );
                    }
                    final String exp = statement.property( "ddl:expression" ).stringValue();
                    System.out.println( "  CHILD >>>> Expression = " + exp );
                }
                if ( statement.hasChildren() ) {
                    for ( final ModelObject state_child : statement.children() ) {
                        System.out.println( "  CHILD >>>> StatementChild = " + state_child.name() );
                        final Descriptor[] mixinTypes = state_child.mixinTypes();
                        for ( final Descriptor type : mixinTypes ) {
                            System.out.println( "                 >>> MixinType = " + type );
                        }
                        if ( state_child.hasProperties() ) {
                            for ( final String prop : state_child.propertyNames() ) {
                                System.out.println( "  CHILD >>>> StatementChildProp = " + prop.toString() );
                            }
                        }
                    }
                }
                // System.out.println("  CHILD >>>> STATEMENT = " + statement.propertyNames());
                // System.out.println("  CHILD >>>> STATEMENT = " + statement.name());
            }
        }
    }
}
