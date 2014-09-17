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
package org.modelspace;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import javax.jcr.Binary;
import javax.jcr.Value;

@SuppressWarnings( "javadoc" )
public final class MockObjectFactory {

    public static Value createBinaryValue() throws Exception {
        final Binary value = mock( Binary.class );
        final Value holder = mock( Value.class );
        when( holder.getBinary() ).thenReturn( value );

        return holder;
    }

    public static Value createBooleanValue() throws Exception {
        final Value holder = mock( Value.class );
        when( holder.getBoolean() ).thenReturn( Boolean.TRUE );

        return holder;
    }

    public static Value createDateValue() throws Exception {
        final Value holder = mock( Value.class );
        when( holder.getDate() ).thenReturn( Calendar.getInstance() );

        return holder;
    }

    public static Value createDoubleValue() throws Exception {
        final Value holder = mock( Value.class );
        when( holder.getDouble() ).thenReturn( Double.valueOf( 0 ) );

        return holder;
    }

    public static Value createLongValue() throws Exception {
        final Value holder = mock( Value.class );
        when( holder.getLong() ).thenReturn( Long.valueOf( 0 ) );

        return holder;
    }

    public static Value createNameValue() throws Exception {
        final Value holder = mock( Value.class );
        when( holder.getString() ).thenReturn( "nameValue" );

        return holder;
    }

    public static Value createPathValue() throws Exception {
        final Value holder = mock( Value.class );
        when( holder.getString() ).thenReturn( "pathValue" );

        return holder;
    }

    public static Value createReferenceValue() throws Exception {
        final Value holder = mock( Value.class );
        when( holder.getString() ).thenReturn( "referenceValue" );

        return holder;
    }

    public static Value createStringValue() throws Exception {
        final Value holder = mock( Value.class );
        when( holder.getString() ).thenReturn( "stringValue" );

        return holder;
    }

    public static Value createUriValue() throws Exception {
        final Value holder = mock( Value.class );
        when( holder.getString() ).thenReturn( "uriValue" );

        return holder;
    }

    public static Value createWeakReferenceValue() throws Exception {
        final Value holder = mock( Value.class );
        when( holder.getString() ).thenReturn( "weakReferenceValue" );

        return holder;
    }

    private MockObjectFactory() {
        // don't allow construction outside of this class
    }

}
