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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.modeshape.common.i18n.I18nResource;

/**
 * Tests for {@link PolyglotterI18n}.
 */
public class PolyglotterI18nTest {
    
    @Test
    public void shouldHaveAllMessagesInitialized() throws Exception {
        for ( final Field field : PolyglotterI18n.class.getFields() ) {
            if ( !Modifier.isStatic( field.getModifiers() ) || !( I18nResource.class.isAssignableFrom( field.getType() ) ) ) return;
            final String message = field.get( null ).toString();
            assertThat( message, message.startsWith( "<" ), is( false ) );
        }
    }
}
