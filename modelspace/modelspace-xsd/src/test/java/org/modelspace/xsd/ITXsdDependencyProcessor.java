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
package org.modelspace.xsd;

import org.junit.Test;
import org.modelspace.spi.DependencyProcessor;
import org.modelspace.test.BaseTest;
import org.modelspace.xsd.XsdDependencyProcessor;

/**
 * An integration test for the {@link XsdDependencyProcessor}.
 */
@SuppressWarnings( "javadoc" )
public class ITXsdDependencyProcessor extends BaseTest {

    // private static final String XSD_METAMODEL_ID = "org.modelspace.xsd.Xsd";

    DependencyProcessor processor;

    @Test
    public void should() {}

    // /**
    // * {@inheritDoc}
    // *
    // * @see org.modelspace.test.BaseTest#before()
    // */
    // @Override
    // public void before() throws Exception {
    // super.before();
    // this.processor = new XsdDependencyProcessor();
    // metamodelManager().install( "sramp" );
    // metamodelManager().install( "xsd" );
    // }
    //
    // @Test
    // public void shouldFindDependencyProcessorForXsdModelNode() throws Exception {
    // // find XSD metamodel
    // Metamodel xsdMetamodel = null;
    //
    // for ( final Metamodel metamodel : metamodelManager().metamodels() ) {
    // if ( metamodel.id().equals( XSD_METAMODEL_ID ) ) {
    // xsdMetamodel = metamodel;
    // break;
    // }
    // }
    //
    // assertThat( xsdMetamodel, notNullValue() );
    //
    // final String path = importArtifact( XSD_ARTIFACT );
    // final ModelImpl model = ( ModelImpl ) modelspace().generateModel( path, ARTIFACT_NAME, xsdMetamodel, true );
    // modelspace().run( new TaskWithResult< Void >() {
    //
    // @Override
    // public Void run( final Session session ) throws Exception {
    // assertThat( model.metamodel().dependencyProcessor(), is( notNullValue() ) );
    // return null;
    // }
    // } );
    // }
    //
    // @Test
    // public void shouldFindDependencyWithAbstractPath() throws Exception {
    // // create dependency XSD
    // final Path a = Files.createTempDirectory( null );
    // final Path b = Files.createTempDirectory( a, null );
    // final Path c = Files.createTempDirectory( b, null );
    // final Path dependencyXsdPath = Files.createTempFile( c, null, ".xsd" );
    //
    // // create dependent XSD content
    // final StringBuilder content = new StringBuilder( XML_DECLARATION );
    // content.append( "<xsd:schema targetNamespace=\"http://www.blahblah.com/XMLSchema/Blah/Blah\" " );
    // content.append( "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" >" );
    // content.append( "<xsd:import namespace=\"http://www.blahblah.com/XMLSchema/Blah/BlahBlah\" " );
    // content.append( "schemaLocation=\"" ).append( dependencyXsdPath.toString() );
    // content.append( "\" />" );
    // content.append( "</xsd:schema>" );
    //
    // // create dependent XSD file with content
    // final Path dependentXsdPath = Files.createTempFile( b, null, ".xsd" );
    // final File xsd = dependentXsdPath.toFile();
    // Files.write( FileSystems.getDefault().getPath( xsd.getAbsolutePath() ),
    // content.toString().getBytes(),
    // StandardOpenOption.CREATE );
    //
    // // create XSD workspace data
    // final String xsdName = xsd.getName();
    // final String workspacePath = modelspace().importFile( xsd, "data" );
    // assertThat( workspacePath, is( "/data/" + xsdName ) );
    //
    // // create model
    // final Metamodel xsdMetamodel = xsdMetamodel();
    // final String modelPath = "model/dependentXsd";
    // final ModelImpl model = ( ModelImpl ) modelspace().generateModel( workspacePath, modelPath, xsdMetamodel, true );
    //
    // // check dependencies
    // modelspace().run( new TaskWithResult< Node >() {
    //
    // @Override
    // public Node run( final Session session ) throws Exception {
    // final String dependenciesPath = ( '/' + modelPath + '/' + ModelspaceLexicon.Model.DEPENDENCIES );
    // final Node dependenciesNode = session.getNode( dependenciesPath );
    // assertThat( dependenciesNode.getNodes().getSize(), is( 1L ) );
    //
    // final Node dependencyNode = dependenciesNode.getNodes().nextNode();
    // final String dependencyModelName = "/model/" + c.getFileName() + '/' + dependencyXsdPath.getFileName();
    // assertThat( dependencyNode.getProperty( ModelspaceLexicon.Dependency.PATH ).getString(), is( dependencyModelName ) );
    //
    // // TODO uncomment this test out when the data path is correct
    // // session.getNode( "/data/" + c.getFileName().toString() + '/' + dependencyXsdPath.getFileName() );
    //
    // return null;
    // }
    // } );
    // }
    //
    // @Test
    // public void shouldNotFindDependenciesInMusicXsd() throws Exception {
    // final URL xsdUrl = getClass().getClassLoader().getResource( "music.xsd" );
    // final String path = modelspace().importFile( new File( xsdUrl.toURI() ), null );
    // assertThat( path, is( "/music.xsd" ) );
    //
    // final Metamodel xsdMetamodel = xsdMetamodel();
    // modelspace().generateModel( path, MODEL_NAME, xsdMetamodel, true );
    //
    // modelspace().run( new TaskWithResult< Node >() {
    //
    // @Override
    // public Node run( final Session session ) throws Exception {
    // final String dependenciesPath = ( '/' + MODEL_NAME + '/' + ModelspaceLexicon.Model.DEPENDENCIES );
    // // final Node modelNode = session.getNode( model.absolutePath() );
    // assertThat( session.itemExists( dependenciesPath ), is( false ) );
    //
    // return null;
    // }
    // } );
    // }
    //
    // @Test
    // public void shouldProcessBooksSoapEncodingXsd() throws Exception {
    // final URL xsdUrl = getClass().getClassLoader().getResource( "Books/SOAP/BooksWithSOAPEncoding.xsd" );
    // final String dataPath = modelspace().importFile( new File( xsdUrl.toURI() ), "Artifact/Books/SOAP" );
    // assertThat( dataPath, is( "/Artifact/Books/SOAP/BooksWithSOAPEncoding.xsd" ) );
    //
    // final Metamodel xsdMetamodel = xsdMetamodel();
    // final String modelPath = "Model/Books/SOAP/BooksWithSOAPEncoding.xsd";
    // modelspace().generateModel( dataPath, modelPath, xsdMetamodel, true );
    //
    // modelspace().run( new TaskWithResult< Node >() {
    //
    // @Override
    // public Node run( final Session session ) throws Exception {
    // final String dependenciesPath = ( '/' + modelPath + '/' + ModelspaceLexicon.Model.DEPENDENCIES );
    // final Node dependenciesNode = session.getNode( dependenciesPath );
    // assertThat( dependenciesNode.getNodes().getSize(), is( 2L ) );
    //
    // final NodeIterator itr = dependenciesNode.getNodes();
    //
    // final String dataTypesArtifactPath = "/Artifact/Books/data/types/BookDatatypes.xsd";
    // final String dataTypesModelPath = "/Model/Books/data/types/BookDatatypes.xsd";
    // final String dataTypesSourceRef = "../data/types/BookDatatypes.xsd";
    //
    // final String soapEncodingArtifactPath = "/Artifact/Books/SOAP/encoding/soap_encoding.xsd";
    // final String soapEncodingModelPath = "/Model/Books/SOAP/encoding/soap_encoding.xsd";
    // final String soapEncodingSourceRef = "./encoding/soap_encoding.xsd";
    //
    // final Node depOneNode = itr.nextNode();
    // assertThat( depOneNode.getPrimaryNodeType().getName(), is( ModelspaceLexicon.Dependency.DEPENDENCY ) );
    //
    // final Node depTwoNode = itr.nextNode();
    // assertThat( depOneNode.getPrimaryNodeType().getName(), is( ModelspaceLexicon.Dependency.DEPENDENCY ) );
    //
    // if ( depOneNode.getProperty( ModelspaceLexicon.Dependency.PATH ).getString().equals( dataTypesModelPath ) ) {
    // { // first dependency node is datatypes
    // final String input =
    // depOneNode.getProperty( ModelspaceLexicon.Dependency.SOURCE_REFERENCE_PROPERTY ).getValues()[ 0 ].getString();
    // assertThat( input, is( dataTypesSourceRef ) );
    //
    // // make sure dependency resource was imported and model created (getNode throws exception if path not found)
    // session.getNode( dataTypesArtifactPath );
    // session.getNode( dataTypesModelPath );
    // }
    //
    // { // dependency two must be soap encoding
    // final String input =
    // depTwoNode.getProperty( ModelspaceLexicon.Dependency.SOURCE_REFERENCE_PROPERTY ).getValues()[ 0 ].getString();
    // assertThat( input, is( soapEncodingSourceRef ) );
    //
    // // make sure dependency resource was imported and model created (getNode throws exception if path not found)
    // session.getNode( soapEncodingArtifactPath );
    // session.getNode( soapEncodingModelPath );
    // }
    // } else if ( depOneNode.getProperty( ModelspaceLexicon.Dependency.PATH ).getString().equals( soapEncodingModelPath ) ) {
    // { // first dependency is soap encoding
    // final String input =
    // depOneNode.getProperty( ModelspaceLexicon.Dependency.SOURCE_REFERENCE_PROPERTY ).getValues()[ 0 ].getString();
    // assertThat( input, is( soapEncodingSourceRef ) );
    //
    // // make sure dependency resource was imported and model created (getNode throws exception if path not found)
    // session.getNode( soapEncodingArtifactPath );
    // session.getNode( soapEncodingModelPath );
    // }
    //
    // { // dependency two must be datatypes
    // final String input =
    // depTwoNode.getProperty( ModelspaceLexicon.Dependency.SOURCE_REFERENCE_PROPERTY ).getValues()[ 0 ].getString();
    // assertThat( input, is( dataTypesSourceRef ) );
    //
    // // make sure dependency resource was imported and model created (getNode throws exception if path not found)
    // session.getNode( dataTypesArtifactPath );
    // session.getNode( dataTypesModelPath );
    // }
    // } else {
    // fail( "path=" + depOneNode.getProperty( ModelspaceLexicon.Dependency.PATH ).getString() );
    // }
    //
    // return null;
    // }
    // } );
    // }
    //
    // @Test
    // public void shouldProcessBooksXsd() throws Exception {
    // final URL xsdUrl = getClass().getClassLoader().getResource( "Books/Books.xsd" );
    // final String dataPath = modelspace().importFile( new File( xsdUrl.toURI() ), "Artifact/Books" );
    // assertThat( dataPath, is( "/Artifact/Books/Books.xsd" ) );
    //
    // final Metamodel xsdMetamodel = xsdMetamodel();
    // final String modelPath = "Model/Books/Books.xsd";
    // modelspace().generateModel( dataPath, modelPath, xsdMetamodel, true );
    //
    // modelspace().run( new TaskWithResult< Node >() {
    //
    // @Override
    // public Node run( final Session session ) throws Exception {
    // final String dependenciesPath = ( '/' + modelPath + '/' + ModelspaceLexicon.Model.DEPENDENCIES );
    // final Node dependenciesNode = session.getNode( dependenciesPath );
    // assertThat( dependenciesNode.getNodes().getSize(), is( 1L ) );
    //
    // final Node dependencyNode = dependenciesNode.getNodes().nextNode();
    // assertThat( dependencyNode.getPrimaryNodeType().getName(), is( ModelspaceLexicon.Dependency.DEPENDENCY ) );
    //
    // final String dependencyPath = "/Model/Books/data/types/BookDatatypes.xsd";
    // assertThat( dependencyNode.getProperty( ModelspaceLexicon.Dependency.PATH ).getString(), is( dependencyPath ) );
    //
    // final String input =
    // dependencyNode.getProperty( ModelspaceLexicon.Dependency.SOURCE_REFERENCE_PROPERTY ).getValues()[ 0 ].getString();
    // assertThat( input, is( "./data/types/BookDatatypes.xsd" ) );
    //
    // // make sure dependency resource was imported and model created (getNode throws exception if path not found)
    // session.getNode( "/Artifact/Books/data/types/BookDatatypes.xsd" );
    // session.getNode( dependencyPath );
    //
    // return null;
    // }
    // } );
    // }
    //
    // @Test
    // public void shouldProcessDependencyWithInvalidRelativePath() throws Exception {
    // final URL xsdUrl = getClass().getClassLoader().getResource( "Books/SOAP/BooksWithSOAPEncoding.xsd" );
    // final String dataPath = modelspace().importFile( new File( xsdUrl.toURI() ), null );
    //
    // final Metamodel xsdMetamodel = xsdMetamodel();
    //
    // // relative path of ../data/types/BookDatatypes.xsd dependency is not valid since there is no parent folder
    // final String modelPath = "Books.xsd";
    // modelspace().generateModel( dataPath, modelPath, xsdMetamodel, true );
    //
    // modelspace().run( new TaskWithResult< Node >() {
    //
    // @Override
    // public Node run( final Session session ) throws Exception {
    // final String dependenciesPath = ( '/' + modelPath + '/' + ModelspaceLexicon.Model.DEPENDENCIES );
    // final Node dependenciesNode = session.getNode( dependenciesPath );
    // assertThat( dependenciesNode.getNodes().getSize(), is( 2L ) );
    //
    // // the node with the invalid relative path should not have its path set
    // final NodeIterator itr = dependenciesNode.getNodes();
    //
    // if ( itr.nextNode().hasProperty( ModelspaceLexicon.Dependency.PATH ) && itr.nextNode().hasProperty(
    // ModelspaceLexicon.Dependency.PATH ) ) {
    // fail( "Invalid dependency relative path should not have a path property on its dependency node" );
    // }
    //
    // return null;
    // }
    // } );
    // }
    //
    // @Test
    // public void shouldProcessMoviesXsd() throws Exception {
    // final URL xsdUrl = getClass().getClassLoader().getResource( "Movies/Movies.xsd" );
    // final String dataPath = modelspace().importFile( new File( xsdUrl.toURI() ), null );
    // assertThat( dataPath, is( "/Movies.xsd" ) );
    //
    // final Metamodel xsdMetamodel = xsdMetamodel();
    // final String modelPath = "Model/Movies.xsd";
    // modelspace().generateModel( dataPath, modelPath, xsdMetamodel, true );
    //
    // modelspace().run( new TaskWithResult< Node >() {
    //
    // @Override
    // public Node run( final Session session ) throws Exception {
    // final String dependenciesPath = ( '/' + modelPath + '/' + ModelspaceLexicon.Model.DEPENDENCIES );
    // final Node dependenciesNode = session.getNode( dependenciesPath );
    // assertThat( dependenciesNode.getNodes().getSize(), is( 1L ) );
    //
    // final Node dependencyNode = dependenciesNode.getNodes().nextNode();
    // assertThat( dependencyNode.getPrimaryNodeType().getName(), is( ModelspaceLexicon.Dependency.DEPENDENCY ) );
    //
    // final String dependencyPath = "/Model/MovieDatatypes.xsd";
    // assertThat( dependencyNode.getProperty( ModelspaceLexicon.Dependency.PATH ).getString(), is( dependencyPath ) );
    //
    // final String input =
    // dependencyNode.getProperty( ModelspaceLexicon.Dependency.SOURCE_REFERENCE_PROPERTY ).getValues()[ 0 ].getString();
    // assertThat( input, is( "MovieDatatypes.xsd" ) );
    //
    // // make sure dependency resource was imported and model created (getNode throws exception if path not found)
    // session.getNode( dependencyPath );
    // session.getNode( "/MovieDatatypes.xsd" );
    //
    // return null;
    // }
    // } );
    //
    // }
    // //
    // // private Metamodel xsdMetamodel() throws Exception {
    // // Metamodel xsdMetamodel = null;
    // //
    // // for ( final Metamodel metamodel : metamodelManager().metamodels() ) {
    // // if ( metamodel.id().equals( XsdLexicon.METAMODEL_ID ) ) {
    // // xsdMetamodel = metamodel;
    // // break;
    // // }
    // // }
    //
    // assertThat( xsdMetamodel, notNullValue() );
    // return xsdMetamodel;
    // }

}
