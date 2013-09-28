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

/**
 * The internationalized string constants for the <code>org.polyglotter.common</code> packages.
 */
@SuppressWarnings( "javadoc" )
public final class CommonI18n {
    
    // public static I18n argumentDidNotContainKey = new I18n( "" );
    // public static I18n argumentDidNotContainObject = new I18n( "" );
    public static I18n argumentMayNotBeEmpty = new I18n( "The \"%s\" argument may not be empty" );
    // public static I18n argumentMayNotBeGreaterThan = new I18n( "" );
    // public static I18n argumentMayNotBeLessThan = new I18n( "" );
    // public static I18n argumentMayNotBeNegative = new I18n( "" );
    public static I18n argumentMayNotBeNull = new I18n( "The \"%s\" argument may not be null" );
    public static I18n argumentMayNotBeZeroLength = new I18n( "The \"%s\" argument may not be zero-length" );
    // public static I18n argumentMayNotBePositive = new I18n( "" );
    // public static I18n argumentMayNotContainNullValue = new I18n( "" );
    // public static I18n argumentMustBeEmpty = new I18n( "" );
    // public static I18n argumentMustBeEquals = new I18n( "" );
    // public static I18n argumentMustBeGreaterThan = new I18n( "" );
    // public static I18n argumentMustBeGreaterThanOrEqualTo = new I18n( "" );
    // public static I18n argumentMustBeInstanceOf = new I18n( "" );
    // public static I18n argumentMustBeLessThan = new I18n( "" );
    // public static I18n argumentMustBeLessThanOrEqualTo = new I18n( "" );
    // public static I18n argumentMustBeNegative = new I18n( "" );
    // public static I18n argumentMustBeNull = new I18n( "" );
    // public static I18n argumentMustBeNumber = new I18n( "" );
    // public static I18n argumentMustBeOfMaximumSize = new I18n( "" );
    // public static I18n argumentMustBeOfMinimumSize = new I18n( "" );
    // public static I18n argumentMustBePositive = new I18n( "" );
    // public static I18n argumentMustBeSameAs = new I18n( "" );
    // public static I18n argumentMustNotBeEquals = new I18n( "" );
    // public static I18n argumentMustNotBeSameAs = new I18n( "" );
    public static I18n errorInitializingCustomLoggerFactory =
        new I18n( "Error loading and/or instantiating the \"%s\" implementation, which is used to tie Polyglotter into a custom "
                  + "logging framework (other than SLF4J, Log4J or the JDK Logging). Falling back to JDK logging." );
    public static I18n i18nFieldEmptyDefaultMessagePattern =
        new I18n( "Internationalization field \"%s\" in %s has an empty default message pattern" );
    public static I18n i18nFieldNull = new I18n( "Internationalization field \"%s\" in %s is null" );
    public static I18n i18nLocalizationFileNotFound = new I18n( "No variant of the localization file for \"%s\" could be found" );
    public static I18n i18nLocalizationProblems =
        new I18n( "Problems were encountered while localizing internationalization %s to locale \"%s\"" );
    public static I18n i18nNotAssignedToStaticField =
        new I18n( "Internationalization object is not assigned to a static member variable\n\tat %s" );
    public static I18n i18nRequiredToSuppliedParameterMismatch = new I18n( "Internationalization field \"%s\" in %s: %s" );
    public static I18n i18nBundleNotFoundInClasspath =
        new I18n( "None of the bundle variants for %s in locale \"%s\" can be located in the classpath: %s" );
    public static I18n i18nUsingUsLocale = new I18n( "Using default U.S. localization for %s" );
    public static I18n text = new I18n( "%s" );
    /*
    argumentDidNotContainKey = "The {0} argument did not contain the expected key {1}
    argumentDidNotContainObject = "The {0} argument did not contain the expected object {1}
    argumentMayNotBeGreaterThan = The {0} argument value, {1}, may not be greater than {2}
    argumentMayNotBeLessThan = The {0} argument value, {1}, may not be less than {2}
    argumentMayNotBeNegative = The {0} argument value, {1}, may not be negative
    argumentMayNotBePositive = The {0} argument value, {1}, may not be positive
    argumentMayNotContainNullValue = The {0} argument may not contain a null value (first null found at position {1})
    argumentMustBeEmpty = The {0} argument must be empty.
    argumentMustBeEquals = The {0} argument is not equal to {1}
    argumentMustBeGreaterThan = The {0} argument value, {1}, must be greater than {2}
    argumentMustBeGreaterThanOrEqualTo = The {0} argument value, {1}, must be greater than or equal to {2}
    argumentMustBeInstanceOf = The {0} argument was an instance of {1} but was expected to be an instance of {2}
    argumentMustBeLessThan = The {0} argument value, {1}, must be less than {2}
    argumentMustBeLessThanOrEqualTo = The {0} argument value, {1}, must be less than or equal to {2}
    argumentMustBeNegative = The {0} argument value, {1}, must be negative
    argumentMustBeNull = The {0} argument must be null
    argumentMustBeNumber = The {0} argument value must be a number
    argumentMustBeOfMaximumSize = The {0} argument is a {1} with {2} elements but must have no more than {3}
    argumentMustBeOfMinimumSize = The {0} argument is a {1} with {2} elements but must have at least {3}
    argumentMustBePositive = The {0} argument value, {1}, must be positive
    argumentMustBeSameAs = The {0} argument is not the same as "{1}"
    argumentMustNotBeEquals = The {0} argument is equal to {1}
    argumentMustNotBeSameAs = The {0} argument is the same as "{1}"
     */
}
