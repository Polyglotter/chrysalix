/*
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.
 *
 * This software is made available by Red Hat, Inc. under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * See the AUTHORS.txt file in the distribution for a full listing of
 * individual contributors.
 */
package org.polyglotter;

import org.modeshape.common.i18n.I18n;

/**
 * Internationalized string constants for the <code>org.polyglotter</code> project.
 */
@SuppressWarnings( "javadoc" )
public class PolyglotterI18n {
    
    public static I18n polyglotterStarted;
    public static I18n polyglotterStopped;
    
    static {
        try {
            I18n.initialize( PolyglotterI18n.class );
        } catch ( final Exception err ) {
            System.err.println( err );
        }
    }
}
