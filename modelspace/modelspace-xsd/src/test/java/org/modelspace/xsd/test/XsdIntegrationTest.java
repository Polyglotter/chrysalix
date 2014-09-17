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
package org.modelspace.xsd.test;

import org.modelspace.test.BaseTest;

@SuppressWarnings( "javadoc" )
public abstract class XsdIntegrationTest extends BaseTest {

    protected static final String SRAMP_METAMODEL_CATEGORY = "sramp";
    protected static final String XSD_METAMODEL_CATEGORY = "xsd";
    protected static final String XSD_METAMODEL_ID = "org.modelspace.xsd.Xsd";
    // protected static final String XML_METAMODEL_CATEGORY = "xml";
    // protected static final String XML_METAMODEL_ID = "org.modelspace.xml.Xml";
    // protected static final String XML_ROOT = "root";
    // protected static final String XML_ROOT_PROPERTY = "property";
    // protected static final String XML_LEAF = "child";
    // protected static final String XML_STRING_VALUE = "string";
    // protected static final String XML_SAME_NAME_SIBLING = "sameNameSibling";
    // protected static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    // protected static final String XML_ARTIFACT = XML_DECLARATION
    // + '<' + XML_ROOT + " " + XML_ROOT_PROPERTY + "='" + XML_STRING_VALUE + "'>"
    // + "<" + XML_LEAF + "></" + XML_LEAF + ">"
    // + "<" + XML_SAME_NAME_SIBLING + "></" + XML_SAME_NAME_SIBLING + ">"
    // + "<" + XML_SAME_NAME_SIBLING + "></" + XML_SAME_NAME_SIBLING + ">"
    // + "</" + XML_ROOT + ">";
    // protected static final String XSD_ARTIFACT =
    // XML_DECLARATION + "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"></xs:schema>";
}
