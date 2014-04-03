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
package org.modeshape.modeler;

import org.polyglotter.common.I18n;

/**
 * Internationalized string constants, in alphabetical order, for the <strong>ModeShape ModeShapeModeler</strong> project.
 */
@SuppressWarnings( "javadoc" )
public final class ModelerI18n {

    public static final I18n dependencyDoesNotHaveSourceReferences =
        new I18n( "A dependency node exists for '%s' but has no source references" );
    public static final I18n modelerStarted = new I18n( "ModeShape Modeler started" );
    public static final I18n modelerStopped = new I18n( "ModeShape Modeler stopped" );
    public static final I18n mustBeModelNode = new I18n( "Node '%s' is not a model node" );
    public static final I18n sessionNotSavedWhenCreatingModel =
        new I18n( "The session was not saved when creating model for '%s'" );
    public static final I18n unableToDetermineDefaultModelType = new I18n( "Unable to determine default model type for file %s" );
    public static final I18n unableToFindModelTypeCategory =
        new I18n( "Unable to find model type category \"%s\" in registered model type repositories" );
    public static final I18n urlNotFound = new I18n( "URL not found: %s" );
    public static final I18n notModelPath = new I18n( "Not a path to a model: %s" );
    public static final I18n notMultiValuedProperty = new I18n( "Property \"%s\" is not a multi-value property" );
}
