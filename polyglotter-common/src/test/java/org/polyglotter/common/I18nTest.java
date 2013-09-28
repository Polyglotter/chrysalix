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
package org.polyglotter.common;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Locale;

import org.junit.Test;

@SuppressWarnings( "javadoc" )
public class I18nTest {
    
    @SuppressWarnings( "unused" )
    @Test( expected = IllegalArgumentException.class )
    public void shouldFailIfDefaultMessagePatternIsEmpty() {
        new I18n( " " );
    }
    
    @SuppressWarnings( "unused" )
    @Test( expected = IllegalArgumentException.class )
    public void shouldFailIfDefaultMessagePatternIsNull() {
        new I18n( null );
    }
    
    @Test( expected = IllegalStateException.class )
    public void shouldFailToInitializeFieldIfNotStatic() {
        final class I18nClass {
            
            final I18n test = new I18n( "test" );
        }
        assertThat( new I18nClass().test.text(), is( "test" ) );
    }
    
    @Test
    public void shouldInitializeAbstractClassWithPrivateConstant() throws Exception {
        final Field fld = AbstractI18nClassWithPrivateConstant.class.getDeclaredField( "test" );
        ClassUtil.makeAccessible( fld );
        assertThat( ( ( I18n ) fld.get( null ) ).text(), is( "test" ) );
    }
    
    @Test
    public void shouldInitializeFinalClassWithPrivateConstant() throws Exception {
        final Field fld = FinalI18nClassWithPrivateConstant.class.getDeclaredField( "test" );
        ClassUtil.makeAccessible( fld );
        assertThat( ( ( I18n ) fld.get( null ) ).text(), is( "test" ) );
    }
    
    @Test
    public void shouldInitializeI18nInterface() {
        assertThat( I18nInterface.test.text(), is( "test" ) );
    }
    
    @Test
    public void shouldProvideTextIfDifferentLocale() {
        assertThat( I18nInterface.test.text( Locale.CANADA ), is( "canada" ) );
    }
    
    @Test
    public void shouldUseUsLocaleTextIfDifferentLocaleWithMissingProperty() {
        assertThat( I18nInterface.test2.text( Locale.CANADA ), is( "test2" ) );
    }
    
    @Test
    public void shouldUseUsLocaleTextIfNoBundleInDifferentLocale() {
        assertThat( I18nWithNoBundle.test.text( Locale.CANADA ), is( "test" ) );
    }
    
    private interface I18nInterface {
        
        I18n test = new I18n( "test" );
        I18n test2 = new I18n( "test2" );
        @SuppressWarnings( "unused" )
        I18n test3 = null;
    }
    
    private interface I18nWithNoBundle {
        
        I18n test = new I18n( "test" );
    }
}
