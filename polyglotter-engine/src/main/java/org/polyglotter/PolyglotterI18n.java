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

import org.polyglotter.common.I18n;

/**
 * Internationalized string constants for the <strong>Polyglotter</strong> project.
 */
@SuppressWarnings( "javadoc" )
public class PolyglotterI18n {
    
    public static I18n addOperationDescription = new I18n( "Adds two or more numbers together" );
    public static I18n addOperationHasNoTerms = new I18n( "Add operation \"%s\" has no terms" );
    public static I18n addOperationName = new I18n( "Add" );
    
    public static I18n concatOperationDescription =
        new I18n( "Concatenates the string representaion of two or more terms together" );
    public static I18n concatOperationName = new I18n( "Concat" );
    
    public static I18n errorOnTermChanged = new I18n( "Error setting term to \"%s\" in operation \"%s\"" );
    public static I18n invalidTermCount = new I18n( "The operation \"%s\" has an invalid term count of \"%s.\"" );
    public static I18n invalidTermType =
        new I18n( "The term \"%s\" in operation \"%s\" does not have a result type of \"Number\" or is null" );
    
    public static I18n nullTerm = new I18n( "The null term was passed to operation \"%s.\"" );
    public static I18n nullTermId = new I18n( "The null term identifier was passed to operation \"%s.\"" );
    
    public static I18n listenerAlreadyRegistered = new I18n( "The listener is already registered" );
    public static I18n listenerError =
        new I18n( "The listener \"%s\" is being unregistered. Event: \"%s\"" );
    public static I18n listenerNotFoundToUnregister = new I18n( "The listener being unregistered was not a registered listener" );
    
    public static I18n operationHasErrors = new I18n( "The operation \"%s\" has errors and a result cannot be calculated" );
    
    public static I18n termNotFound = new I18n( "The term \"%s\" in operation \"%s\" could not be found" );
    public static I18n termExists = new I18n( "The term \"%s\" in operation \"%s\" already exists" );
    
    public static I18n voidTermDescription = new I18n( "A term with a void or null value" );
    public static I18n voidTermName = new I18n( "Void" );
    
}
