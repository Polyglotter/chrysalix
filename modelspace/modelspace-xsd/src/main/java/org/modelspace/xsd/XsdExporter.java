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

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import org.modelspace.Model;
import org.modelspace.spi.Exporter;
import org.modeshape.sequencer.sramp.SrampLexicon;
import org.modeshape.sequencer.xsd.XsdLexicon;

/**
 * 
 */
public class XsdExporter implements Exporter {

    PrintWriter writer;
    String xsdPrefix;
    final Map< String, String > namespacePrefixByUri = new HashMap<>();
    final Map< String, Node > complexTypeByName = new HashMap<>();
    String targetNamespace;

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.spi.Exporter#execute(org.modelspace.Model, java.io.OutputStream)
     */
    @Override
    public void execute( final Model model,
                         final OutputStream stream ) {
        // ( ( ModelImpl ) model ).modelspace.run( new TaskWithResult< Void >() {
        //
        // @Override
        // public Void run( final Session session ) throws Exception {
        // final Node node = session.getNode( model.absolutePath() );
        // // new JcrTools().printSubgraph( node );
        // writer = new PrintWriter( stream, true );
        // // Print XML declaration
        // writer.print( "<?xml version=\"1.0\" encoding=\"" );
        // writer.print( node.getProperty( SrampLexicon.CONTENT_ENCODING ).getString() );
        // writer.println( "\"?>" );
        // // Find XSD namespace declaration & build namespace-prefix-by-URI map
        // for ( final PropertyIterator iter = node.getProperties( "xmlns*" ); iter.hasNext(); ) {
        // final Property prop = iter.nextProperty();
        // final String uri = prop.getString();
        // final int ndx = prop.getName().indexOf( ':' ) + 1;
        // String prefix = prop.getName().substring( ndx );
        // if ( ndx > 0 ) prefix += ':';
        // namespacePrefixByUri.put( uri, prefix );
        // if ( uri.equals( XsdLexicon.Namespace.URI ) ) xsdPrefix = prefix;
        // }
        // // Print schema
        // print( 0, node );
        // return null;
        // }
        // } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.spi.Exporter#metamodelId()
     */
    @Override
    public String metamodelId() {
        return "org.modelspace.xsd.Xsd";
    }

    void print( final int indentLevel,
                final Node node ) throws RepositoryException {
        String element = null;
        final String type = node.getPrimaryNodeType().getName();
        if ( type.equals( XsdLexicon.ATTRIBUTE_DECLARATION ) ) element = "attribute";
        else if ( type.equals( XsdLexicon.CHOICE ) ) element = "choice";
        else if ( type.equals( XsdLexicon.COMPLEX_TYPE_DEFINITION ) ) element = "complexType";
        else if ( type.equals( XsdLexicon.ELEMENT_DECLARATION ) ) element = "element";
        else if ( type.equals( XsdLexicon.IMPORT ) ) element = "import";
        else if ( type.equals( XsdLexicon.SCHEMA_DOCUMENT ) ) element = "schema";
        else if ( type.equals( XsdLexicon.SEQUENCE ) ) element = "sequence";
        else if ( type.equals( "mm:dependencies" ) ) return;
        else throw new UnsupportedOperationException( node.toString() );
        printIndent( indentLevel );
        writer.print( '<' + xsdPrefix + element );
        printAttributes( indentLevel, node );
        if ( node.hasNodes() || node.hasProperty( SrampLexicon.DESCRIPTION ) ) {
            writer.println( '>' );
            int childIndentLevel = indentLevel + 1;
            printAnnotation( childIndentLevel, node );
            if ( element.equals( "complexType" ) ) {
                complexTypeByName.put( node.getProperty( XsdLexicon.NC_NAME ).getString(), node );
                final String baseType = node.getProperty( XsdLexicon.BASE_TYPE_NAME ).getString();
                final String method = node.getProperty( XsdLexicon.METHOD ).getString();
                final String baseTypeNamespace = node.getProperty( XsdLexicon.BASE_TYPE_NAMESPACE ).getString();
                if ( !baseType.equals( "anyType" )
                     || !method.equals( "restriction" )
                     || !baseTypeNamespace.equals( XsdLexicon.Namespace.URI ) ) {
                    final String contentType = complexTypeByName.get( baseType ) == null ? "simpleContent" : "complexContent";
                    printIndent( indentLevel + 1 );
                    writer.println( '<' + xsdPrefix + contentType + '>' );
                    printIndent( indentLevel + 2 );
                    writer.println( '<' + xsdPrefix + method + " base=\"" + namespacePrefixByUri.get( baseTypeNamespace )
                                    + baseType + "\">" );
                    childIndentLevel += 2;
                    for ( final NodeIterator iter = node.getNodes(); iter.hasNext(); )
                        print( childIndentLevel, iter.nextNode() );
                    printIndent( indentLevel + 2 );
                    writer.println( "</" + xsdPrefix + method + '>' );
                    printIndent( indentLevel + 1 );
                    writer.println( "</" + xsdPrefix + contentType + '>' );
                } else for ( final NodeIterator iter = node.getNodes(); iter.hasNext(); )
                    print( childIndentLevel, iter.nextNode() );
            } else for ( final NodeIterator iter = node.getNodes(); iter.hasNext(); )
                print( childIndentLevel, iter.nextNode() );
            printIndent( indentLevel );
            writer.println( "</" + xsdPrefix + element + '>' );
        } else writer.println( "/>" );
    }

    private void printAnnotation( final int indentLevel,
                                  final Node node ) throws RepositoryException {
        if ( !node.hasProperty( SrampLexicon.DESCRIPTION ) ) return;
        final Property prop = node.getProperty( SrampLexicon.DESCRIPTION );
        printIndent( indentLevel );
        writer.println( '<' + xsdPrefix + XsdLexicon.ANNOTATION + '>' );
        printIndent( indentLevel + 1 );
        writer.println( '<' + xsdPrefix + "documentation>" );
        printIndent( indentLevel + 2 );
        writer.println( prop.getString() );
        printIndent( indentLevel + 1 );
        writer.println( "</" + xsdPrefix + "documentation>" );
        printIndent( indentLevel );
        writer.println( "</" + xsdPrefix + XsdLexicon.ANNOTATION + '>' );
    }

    private void printAttribute( final String name,
                                 final String value ) {
        writer.print( ' ' + name + "=\"" + value + "\"" );
    }

    private void printAttributes( final int indentLevel,
                                  final Node node ) throws RepositoryException {
        String maxOccurs = null;
        for ( final PropertyIterator iter = node.getProperties(); iter.hasNext(); ) {
            final Property prop = iter.nextProperty();
            final String name = prop.getName();
            if ( name.equals( XsdLexicon.NC_NAME ) ) printAttribute( "name", prop.getString() );
            else if ( name.equals( XsdLexicon.TYPE_NAME ) ) {
                printAttribute( "type",
                                namespacePrefixByUri.get( node.getProperty( XsdLexicon.TYPE_NAMESPACE ).getString() )
                                                + prop.getString() );
            } else if ( name.equals( XsdLexicon.MIN_OCCURS ) ) {
                if ( prop.getLong() != 1 ) printAttribute( "minOccurs", prop.getString() );
                maxOccurs = "unbounded";
            } else if ( name.equals( XsdLexicon.MAX_OCCURS ) ) {
                if ( prop.getLong() != 1 ) printAttribute( "maxOccurs", prop.getString() );
                maxOccurs = null;
            } else if ( name.equals( XsdLexicon.USE ) ) {
                if ( !prop.getString().equals( "optional" ) ) printAttribute( "use", prop.getString() );
            } else if ( name.startsWith( "xmlns" ) ) printAttribute( name, prop.getString() );
            else if ( name.equals( XsdLexicon.NAMESPACE ) ) {
                if ( !prop.getString().equals( targetNamespace ) ) printAttribute( "namespace", prop.getString() );
            }
            else if ( name.equals( XsdLexicon.SCHEMA_LOCATION ) ) printAttribute( "schemaLocation", prop.getString() );
            else if ( name.equals( "targetNamespace" ) ) {
                targetNamespace = prop.getString();
                printAttribute( name, targetNamespace );
            }
        }
        if ( maxOccurs != null ) printAttribute( "maxOccurs", maxOccurs );
    }

    private void printIndent( final int indentLevel ) {
        for ( int ndx = indentLevel; --ndx >= 0; )
            writer.print( "  " );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.spi.Exporter#supports(java.lang.String)
     */
    @Override
    public boolean supports( final String mimeType ) {
        return true;
    }
}
