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
package org.modelspace.java;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.modelspace.test.JavaIntegrationTest;

@SuppressWarnings( "javadoc" )
public class ITJava extends JavaIntegrationTest {

    @Test
    public void shouldGetDependencyProcessor() throws Exception {
        assertThat( metamodel().dependencyProcessor(), notNullValue() );
    }

    // private Node createDependenciesNode( final Session session,
    // final Model model ) throws Exception {
    // final Node dependenciesNode = session.getNode( model.absolutePath() ).addNode( ModelspaceLexicon.Model.DEPENDENCIES );
    // return dependenciesNode;
    // }
    //
    // private Node createDependencyNode( final Node dependenciesNode,
    // final String dependencyNodeName,
    // final String dependencyWorkspacePath ) throws Exception {
    // final Node dependencyNode = dependenciesNode.addNode( dependencyNodeName, ModelspaceLexicon.Dependency.DEPENDENCY );
    // dependencyNode.setProperty( ModelspaceLexicon.Dependency.PATH, dependencyWorkspacePath );
    // dependencyNode.setProperty( ModelspaceLexicon.Dependency.SOURCE_REFERENCE_PROPERTY, new String[] { "import" } ); // mandatory
    // // property
    // return dependencyNode;
    // }
    //
    // @Test
    // public void shouldGetNullExternalLocation() throws Exception {
    // assertThat( importModel().externalLocation(), nullValue() );
    // }
    //
    // @Test
    // public void shouldRecordExternalLocationIfImportArtifact() throws Exception {
    // final URL url = new URL( "File:src/test/resources/Books.xsd" );
    // final String path = modelspace().importArtifact( url, null );
    // modelspace().run( new TaskWithResult< Void >() {
    //
    // @Override
    // public Void run( final Session session ) throws Exception {
    // final Node node = session.getNode( path );
    // assertThat( node, notNullValue() );
    // assertThat( node.getProperty( ModelspaceLexicon.Model.EXTERNAL_LOCATION ).getString(), is( url.toString() ) );
    // return null;
    // }
    // } );
    // }
    //
    // @Test
    // public void shouldRecordUrlIfImportFile() throws Exception {
    // final URI uri = getClass().getClassLoader().getResource( "Books.xsd" ).toURI();
    // final String path = modelspace().importFile( new File( uri ), null );
    // modelspace().run( new TaskWithResult< Void >() {
    //
    // @Override
    // public Void run( final Session session ) throws Exception {
    // final Node node = session.getNode( path );
    // assertThat( node, notNullValue() );
    // assertThat( node.getProperty( ModelspaceLexicon.Model.EXTERNAL_LOCATION ).getString(), is( uri.toString() ) );
    // return null;
    // }
    // } );
    // }
    //
    // private void verifyPathExistsWithContent( final String path ) throws Exception {
    // modelspace().run( new TaskWithResult< Void >() {
    //
    // @Override
    // public Void run( final Session session ) throws Exception {
    // final Node node = session.getNode( path );
    // assertThat( node, notNullValue() );
    // assertThat( node.getNode( JcrLexicon.CONTENT.getString() ), notNullValue() );
    // assertThat( node.getNode( JcrLexicon.CONTENT.getString() ).getProperty( JcrLexicon.DATA.getString() ), notNullValue() );
    // return null;
    // }
    // } );
    // }
    // @Test
    // public void shouldObtainDependencies() throws Exception {
    // modelspace().run( new TaskWithResult< Void >() {
    //
    // @Override
    // public Void run( final Session session ) throws Exception {
    // final ModelImpl model = ( ModelImpl ) modelObject();
    //
    // // setup
    // final Node dependenciesNode = createDependenciesNode( session, model );
    // final String depOneName = "/depOne"; // must start path at root
    // createDependencyNode( dependenciesNode, depOneName, depOneName );
    // final String depTwoName = "/depTwo"; // must start path at root
    // createDependencyNode( dependenciesNode, depTwoName, depTwoName );
    // session.save();
    //
    // // test
    // final Collection< Dependency > dependencies = model.dependencies();
    // assertThat( dependencies.size(), is( 2 ) );
    //
    // boolean foundOne = false;
    // boolean foundTwo = false;
    //
    // for ( final Dependency dependency : dependencies ) {
    // if ( dependency.path().equals( depOneName ) ) {
    // foundOne = true;
    // assertThat( dependency.exists(), is( false ) );
    // } else if ( dependency.path().equals( depTwoName ) ) {
    // foundTwo = true;
    // assertThat( dependency.exists(), is( false ) );
    // }
    // }
    //
    // assertThat( ( foundOne && foundTwo ), is( true ) );
    //
    // return null;
    // }
    // } );
    // }
    //
    // @Test
    // public void shouldObtainMissingDependencies() throws Exception {
    // modelspace().run( new TaskWithResult< Void >() {
    //
    // @Override
    // public Void run( final Session session ) throws Exception {
    // final ModelImpl model = ( ModelImpl ) modelObject();
    //
    // // setup
    // final Node dependenciesNode = createDependenciesNode( session, model );
    // final String nodePath = "/myNode"; // path where a node does not exist
    // createDependencyNode( dependenciesNode, "mydependency", nodePath );
    // session.save();
    //
    // // test
    // assertThat( model.missingDependencies().size(), is( 1 ) );
    // assertThat( model.missingDependencies().iterator().next().path(), is( nodePath ) );
    //
    // return null;
    // }
    // } );
    // }
    //
    // @Test
    // public void shouldVerifyDependenciesAllExist() throws Exception {
    // modelspace().run( new TaskWithResult< Void >() {
    //
    // @Override
    // public Void run( final Session session ) throws Exception {
    // final ModelImpl model = ( ModelImpl ) modelObject();
    //
    // // setup
    // final Node dependenciesNode = createDependenciesNode( session, model );
    // final Node node = session.getRootNode().addNode( "myNode" ); // create a node to use its path
    // createDependencyNode( dependenciesNode, "mydependency", node.getPath() );
    // session.save();
    //
    // // test
    // assertThat( model.allDependenciesExist(), is( true ) );
    //
    // return null;
    // }
    // } );
    // }
    //
    // @Test
    // public void shouldVerifyDependenciesDoNotAllExist() throws Exception {
    // modelspace().run( new TaskWithResult< Void >() {
    //
    // @Override
    // public Void run( final Session session ) throws Exception {
    // final ModelImpl model = ( ModelImpl ) modelObject();
    //
    // // setup
    // final Node dependenciesNode = createDependenciesNode( session, model );
    // createDependencyNode( dependenciesNode, "mydependency", "/myNode" );
    // session.save();
    //
    // // test
    // assertThat( model.allDependenciesExist(), is( false ) );
    //
    // return null;
    // }
    // } );
    // }
    //
    // @Test
    // public void shouldVerifyDependencyExists() throws Exception {
    // modelspace().run( new TaskWithResult< Void >() {
    //
    // @Override
    // public Void run( final Session session ) throws Exception {
    // final ModelImpl model = ( ModelImpl ) modelObject();
    //
    // // setup
    // final Node dependenciesNode = createDependenciesNode( session, model );
    // final Node node = session.getRootNode().addNode( "myNode" ); // create a node to use its path
    // createDependencyNode( dependenciesNode, "mydependency", node.getPath() );
    // session.save();
    //
    // // test
    // assertThat( model.dependencies().size(), is( 1 ) );
    // assertThat( model.dependencies().iterator().next().exists(), is( true ) );
    //
    // return null;
    // }
    // } );
    // }
}
