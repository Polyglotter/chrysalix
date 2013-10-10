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
package org.polyglotter.test;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import org.apache.log4j.Logger;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

@SuppressWarnings( "javadoc" )
public class TestRunner extends BlockJUnit4ClassRunner {
    
    public TestRunner( final Class< ? > testClass ) throws InitializationError {
        super( testClass );
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.junit.runners.BlockJUnit4ClassRunner#runChild(org.junit.runners.model.FrameworkMethod,
     *      org.junit.runner.notification.RunNotifier)
     */
    @Override
    protected void runChild( final FrameworkMethod method,
                             final RunNotifier notifier ) {
        if ( Logger.getLogger( getClass() ).isDebugEnabled() ) {
            final StringBuilder builder = new StringBuilder();
            final StringCharacterIterator iter = new StringCharacterIterator( testName( method ) );
            for ( char c = iter.first(); c != CharacterIterator.DONE; c = iter.next() ) {
                if ( Character.isLowerCase( c ) )
                    builder.append( c );
                else builder.append( ' ' ).append( Character.toLowerCase( c ) );
            }
            String testClassName = getTestClass().getJavaClass().getSimpleName();
            if ( testClassName.endsWith( "Test" ) )
                testClassName = testClassName.substring( 0, testClassName.length() - "Test".length() );
            else if ( testClassName.startsWith( "IT" ) )
                testClassName = testClassName.substring( "IT".length() );
            Logger.getLogger( getClass() ).debug( "Testing " + testClassName + ' ' + builder + "..." );
        }
        super.runChild( method, notifier );
    }
}
