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
package org.modelspace.internal;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.net.URL;

import org.junit.Test;
import org.modelspace.MetamodelManager;
import org.modelspace.internal.MetamodelManagerImpl;
import org.modelspace.internal.ModelspaceImpl;
import org.modelspace.test.BaseTest;

@SuppressWarnings( "javadoc" )
public class MetamodelManagerImplTest extends BaseTest {

    private MetamodelManager metamodelManager() throws Exception {
        return new MetamodelManagerImpl( mock( ModelspaceImpl.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetApplicableMetamodelsIfPathIsEmpty() throws Exception {
        metamodelManager().metamodelsForArtifact( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetApplicableMetamodelsIfPathIsNull() throws Exception {
        metamodelManager().metamodelsForArtifact( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetDefaultMetamodelIfPathIsEmpty() throws Exception {
        metamodelManager().defaultMetamodel( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetDefaultMetamodelIfPathIsNull() throws Exception {
        metamodelManager().defaultMetamodel( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetMetamodelIfNameIsEmpty() throws Exception {
        metamodelManager().metamodel( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetMetamodelIfNameIsNull() throws Exception {
        metamodelManager().metamodel( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetMetamodelsForCategoryIfCategoryEmpty() throws Exception {
        metamodelManager().metamodelsForCategory( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetMetamodelsForCategoryIfCategoryNull() throws Exception {
        metamodelManager().metamodelsForCategory( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToInstallMetamodelsIfCategoryIsEmpty() throws Exception {
        metamodelManager().install( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToInstallMetamodelsIfCategoryIsNull() throws Exception {
        metamodelManager().install( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToMoveMetamodelRepositoryDownIfUrlNotFound() throws Exception {
        metamodelManager().moveMetamodelRepositoryDown( new URL( "file:bogus" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToMoveMetamodelRepositoryDownIfUrlNull() throws Exception {
        metamodelManager().moveMetamodelRepositoryDown( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToMoveMetamodelRepositoryUpIfUrlNotFound() throws Exception {
        metamodelManager().moveMetamodelRepositoryUp( new URL( "file:bogus" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToMoveMetamodelRepositoryUpIfUrlNull() throws Exception {
        metamodelManager().moveMetamodelRepositoryUp( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToRegisterMetamodelRepositoryIfUrlIsNull() throws Exception {
        metamodelManager().registerMetamodelRepository( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToUninstallIfCategoryEmpty() throws Exception {
        metamodelManager().uninstall( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToUninstallIfCategoryNull() throws Exception {
        metamodelManager().uninstall( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToUnregisterMetamodelRepositoryIfUrlIsNull() throws Exception {
        metamodelManager().unregisterMetamodelRepository( null );
    }

    @Test
    public void shouldGetExistingRegisteredMetamodelRepositoriesIfUnregisteringUnregisteredUrl() throws Exception {
        final int size = metamodelManager().metamodelRepositories().length;
        final URL[] repos = metamodelManager().unregisterMetamodelRepository( new URL( "file:bogus" ) );
        assertThat( repos, notNullValue() );
        assertThat( repos.length, is( size ) );
    }

    @Test
    public void shouldNotReturnNullMetamodelCategories() throws Exception {
        assertThat( metamodelManager().metamodelCategories(), notNullValue() );
        assertThat( metamodelManager().metamodelCategories().length == 0, is( true ) );
    }

    @Test
    public void shouldNotReturnNullMetamodelsForCategory() throws Exception {
        assertThat( metamodelManager().metamodelsForCategory( "bogus" ), notNullValue() );
        assertThat( metamodelManager().metamodelsForCategory( "bogus" ).length == 0, is( true ) );
    }

    @Test
    public void shouldRegisterMetamodelRepository() throws Exception {
        final int size = metamodelManager().metamodelRepositories().length;
        final URL url = new URL( "file:bogus" );
        final URL[] repos = metamodelManager().registerMetamodelRepository( url );
        assertThat( repos, notNullValue() );
        assertThat( repos.length, is( size + 1 ) );
        assertThat( repos[ 0 ], is( url ) );
    }

    @Test
    public void shouldUnregisterMetamodelRepository() throws Exception {
        final int size = metamodelManager().metamodelRepositories().length;
        final URL repo = new URL( "file:" );
        metamodelManager().registerMetamodelRepository( repo );
        final URL[] repos = metamodelManager().unregisterMetamodelRepository( repo );
        assertThat( repos, notNullValue() );
        assertThat( repos.length, is( size ) );
    }
}
