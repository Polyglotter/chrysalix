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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.net.URL;

import org.junit.Test;
import org.modeshape.modeler.ModelTypeManager;
import org.modeshape.modeler.test.BaseTest;

@SuppressWarnings( "javadoc" )
public class ModelTypeManagerImplTest extends BaseTest {

    private ModelTypeManager modelTypeManager() throws Exception {
        return new ModelTypeManagerImpl( mock( Manager.class ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetApplicableModelTypesIfPathIsEmpty() throws Exception {
        modelTypeManager().modelTypesForArtifact( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetApplicableModelTypesIfPathIsNull() throws Exception {
        modelTypeManager().modelTypesForArtifact( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetDefaultModelTypeIfPathIsEmpty() throws Exception {
        modelTypeManager().defaultModelType( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetDefaultModelTypeIfPathIsNull() throws Exception {
        modelTypeManager().defaultModelType( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelTypeIfNameIsEmpty() throws Exception {
        modelTypeManager().modelType( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelTypeIfNameIsNull() throws Exception {
        modelTypeManager().modelType( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelTypesForCategoryIfCategoryEmpty() throws Exception {
        modelTypeManager().modelTypesForCategory( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToGetModelTypesForCategoryIfCategoryNull() throws Exception {
        modelTypeManager().modelTypesForCategory( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToInstallModelTypesIfCategoryIsEmpty() throws Exception {
        modelTypeManager().install( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToInstallModelTypesIfCategoryIsNull() throws Exception {
        modelTypeManager().install( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToMoveModelTypeRepositoryDownIfUrlNotFound() throws Exception {
        modelTypeManager().moveModelTypeRepositoryDown( new URL( "file:bogus" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToMoveModelTypeRepositoryDownIfUrlNull() throws Exception {
        modelTypeManager().moveModelTypeRepositoryDown( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToMoveModelTypeRepositoryUpIfUrlNotFound() throws Exception {
        modelTypeManager().moveModelTypeRepositoryUp( new URL( "file:bogus" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToMoveModelTypeRepositoryUpIfUrlNull() throws Exception {
        modelTypeManager().moveModelTypeRepositoryUp( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToRegisterModelTypeRepositoryIfUrlIsNull() throws Exception {
        modelTypeManager().registerModelTypeRepository( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToUninstallIfCategoryEmpty() throws Exception {
        modelTypeManager().uninstall( " " );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToUninstallIfCategoryNull() throws Exception {
        modelTypeManager().uninstall( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldFailToUnregisterModelTypeRepositoryIfUrlIsNull() throws Exception {
        modelTypeManager().unregisterModelTypeRepository( null );
    }

    @Test
    public void shouldGetExistingRegisteredModelTypeRepositoriesIfUnregisteringUnregisteredUrl() throws Exception {
        final int size = modelTypeManager().modelTypeRepositories().length;
        final URL[] repos = modelTypeManager().unregisterModelTypeRepository( new URL( "file:bogus" ) );
        assertThat( repos, notNullValue() );
        assertThat( repos.length, is( size ) );
    }

    @Test
    public void shouldNotReturnNullModelTypeCategories() throws Exception {
        assertThat( modelTypeManager().modelTypeCategories(), notNullValue() );
        assertThat( modelTypeManager().modelTypeCategories().length == 0, is( true ) );
    }

    @Test
    public void shouldNotReturnNullModelTypesForCategory() throws Exception {
        assertThat( modelTypeManager().modelTypesForCategory( "bogus" ), notNullValue() );
        assertThat( modelTypeManager().modelTypesForCategory( "bogus" ).length == 0, is( true ) );
    }

    @Test
    public void shouldRegisterModelTypeRepository() throws Exception {
        final int size = modelTypeManager().modelTypeRepositories().length;
        final URL url = new URL( "file:bogus" );
        final URL[] repos = modelTypeManager().registerModelTypeRepository( url );
        assertThat( repos, notNullValue() );
        assertThat( repos.length, is( size + 1 ) );
        assertThat( repos[ 0 ], is( url ) );
    }

    @Test
    public void shouldUnregisterModelTypeRepository() throws Exception {
        final int size = modelTypeManager().modelTypeRepositories().length;
        final URL repo = new URL( "file:" );
        modelTypeManager().registerModelTypeRepository( repo );
        final URL[] repos = modelTypeManager().unregisterModelTypeRepository( repo );
        assertThat( repos, notNullValue() );
        assertThat( repos.length, is( size ) );
    }
}
