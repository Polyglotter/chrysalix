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
package org.modeshape.modeler.extensions;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.modeshape.modeler.spi.metamodel.Dependency;

/**
 * A test for the {@link Dependency} class.
 */
@SuppressWarnings( "javadoc" )
public class DependencyTest {

    private static final List< String > ONE_SRC_REF = Collections.singletonList( "import org.junit.Test;" );

    @SuppressWarnings( "unused" )
    @Test
    public void shouldAllowEmptyPath() {
        new Dependency( "", ONE_SRC_REF, false );
    }

    @SuppressWarnings( "unused" )
    @Test
    public void shouldAllowNullPath() {
        new Dependency( null, ONE_SRC_REF, false );
    }

    @Test
    public void shouldBeEqualWhenSameSourceReferencesAndNullPaths() {
        final Dependency depOne = new Dependency( null, ONE_SRC_REF, false );
        final Dependency depTwo = new Dependency( depOne.path(), depOne.sourceReferences(), depOne.exists() );
        assertThat( depOne.equals( depTwo ), is( true ) );
    }

    @Test
    public void shouldBeEqualWhenSameSourceReferencesAndPath() {
        final Dependency depOne = new Dependency( "/my/path", ONE_SRC_REF, false );
        final Dependency depTwo = new Dependency( depOne.path(), depOne.sourceReferences(), depOne.exists() );
        assertThat( depOne.equals( depTwo ), is( true ) );
    }

    @Test
    public void shouldHaveCorrectDependencyExists() {
        { // exists = true
            final boolean exists = true;
            final Dependency dep = new Dependency( "/my/path", ONE_SRC_REF, exists );
            assertThat( dep.exists(), is( exists ) );
        }

        { // exists = false
            final boolean exists = false;
            final Dependency dep = new Dependency( "/my/path", ONE_SRC_REF, exists );
            assertThat( dep.exists(), is( exists ) );
        }
    }

    @Test
    public void shouldHaveCorrectDependencyPath() {
        final String path = "/my/path";
        final Dependency dep = new Dependency( path, ONE_SRC_REF, false );
        assertThat( dep.path(), is( path ) );
    }

    @Test
    public void shouldHaveCorrectSourceReferenceAfterConstruction() {
        final String input = "import java.util.Collections;";
        final Dependency dep = new Dependency( "/my/path", input, false );
        assertThat( dep.sourceReferences().size(), is( 1 ) );
        assertThat( dep.sourceReferences().get( 0 ), is( input ) );
    }

    @Test
    public void shouldHaveCorrectSourceReferencesAfterConstruction() {
        final Dependency dep = new Dependency( "/my/path", ONE_SRC_REF, false );
        assertThat( dep.sourceReferences(), is( ONE_SRC_REF ) );
    }

    @SuppressWarnings( "unused" )
    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowEmptySourceReference() {
        new Dependency( "/my/path", Collections.< String >emptyList(), false );
    }

    @SuppressWarnings( "unused" )
    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullSourceReference() {
        new Dependency( "/my/path", ( List< String > ) null, false );
    }

    @Test
    public void shouldNotBeEqualWhenSamePathsDifferentSourceReferences() {
        final Dependency depOne = new Dependency( "/my/path", ONE_SRC_REF, false );

        final List< String > sourceRefs = new ArrayList<>();
        sourceRefs.add( ONE_SRC_REF.get( 0 ) );
        sourceRefs.add( "import java.util.List;" );

        final Dependency depTwo = new Dependency( depOne.path(), sourceRefs, depOne.exists() );
        assertThat( depOne.equals( depTwo ), is( false ) );
    }

    @Test
    public void shouldNotBeEqualWhenSameSourceReferencesDifferentPaths() {
        final Dependency depOne = new Dependency( "/my/path", ONE_SRC_REF, false );
        final Dependency depTwo = new Dependency( depOne.path() + "different", depOne.sourceReferences(), depOne.exists() );
        assertThat( depOne.equals( depTwo ), is( false ) );
    }

}
