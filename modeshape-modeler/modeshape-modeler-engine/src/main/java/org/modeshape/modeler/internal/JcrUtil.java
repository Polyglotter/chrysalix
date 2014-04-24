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
package org.modeshape.modeler.internal;

import javax.jcr.Node;
import javax.jcr.Value;

import org.modeshape.common.util.CheckArg;
import org.modeshape.modeler.ModelerException;

/**
 * A collection of utilities related to JCR.
 */
public final class JcrUtil {

    /**
     * @param node
     *        the node whose property value is being requested (cannot be <code>null</code>)
     * @param propertyName
     *        the name of the property whose value is being requested (cannot be <code>null</code> or empty)
     * @return the property value (never <code>null</code> or empty)
     * @throws ModelerException
     *         if property does not exist or if an error occurs
     */
    public static String value( final Node node,
                                final String propertyName ) throws ModelerException {
        CheckArg.isNotNull( node, "node" );
        CheckArg.isNotEmpty( propertyName, "propertyName" );

        try {
            return node.getProperty( propertyName ).getString();
        } catch ( final Exception e ) {
            throw new ModelerException( e );
        }
    }

    /**
     * @param node
     *        the node whose property value is being requested (cannot be <code>null</code>)
     * @param propertyName
     *        the name of the multi-valued property whose values are being requested (cannot be <code>null</code> or empty)
     * @return the property value (never <code>null</code> or empty)
     * @throws ModelerException
     *         if property does not exist, if property is not multi-valued, or if an error occurs
     */
    public static String[] values( final Node node,
                                   final String propertyName ) throws ModelerException {
        CheckArg.isNotNull( node, "node" );
        CheckArg.isNotEmpty( propertyName, "propertyName" );

        try {
            final Value[] values = node.getProperty( propertyName ).getValues();
            final String[] result = new String[ values.length ];
            int i = 0;

            for ( final Value value : values ) {
                result[ i++ ] = value.getString();
            }

            return result;
        } catch ( final Exception e ) {
            throw new ModelerException( e );
        }
    }

    /**
     * Don't allow construction outside of this class.
     */
    private JcrUtil() {
        // nothing to do
    }

}
