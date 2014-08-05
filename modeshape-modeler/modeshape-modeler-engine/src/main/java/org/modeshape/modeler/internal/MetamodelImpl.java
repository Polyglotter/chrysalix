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

import org.modeshape.common.util.CheckArg;
import org.modeshape.common.util.StringUtil;
import org.modeshape.modeler.Metamodel;
import org.modeshape.modeler.spi.metamodel.DependencyProcessor;
import org.modeshape.modeler.spi.metamodel.Exporter;
import org.modeshape.modeler.spi.metamodel.Importer;

/**
 * 
 */
public final class MetamodelImpl implements Metamodel {

    private Importer importer;
    private DependencyProcessor dependencyProcessor;
    private Exporter exporter;

    private final String category;
    private final String id;
    private String name;

    /**
     * @param category
     *        the metamodel category (cannot be <code>null</code> or empty)
     * @param id
     *        the metamodel identifier (cannot be <code>null</code> or empty)
     */
    MetamodelImpl( final String category,
                   final String id ) {
        CheckArg.isNotEmpty( category, "category" );
        CheckArg.isNotEmpty( id, "id" );

        this.category = category;
        this.id = id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see Metamodel#category()
     */
    @Override
    public String category() {
        return category;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.Metamodel#dependencyProcessor()
     */
    @Override
    public DependencyProcessor dependencyProcessor() {
        return dependencyProcessor;
        // if ( StringUtil.isBlank( dependencyProcessorClassName ) ) return null;
        //
        // try {
        // final Class< ? > clazz = libraryClassLoader().loadClass( dependencyProcessorClassName );
        // dependencyProcessor = ( DependencyProcessor ) clazz.newInstance();
        // return dependencyProcessor;
        // } catch ( final Exception e ) {
        // throw new ModelerException( e );
        // }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.Metamodel#exporter()
     */
    @Override
    public Exporter exporter() {
        return exporter;
        // if ( StringUtil.isBlank( exporterClassName ) ) return null;
        //
        // try {
        // final Class< ? > clazz = libraryClassLoader().loadClass( exporterClassName );
        // exporter = ( Exporter ) clazz.newInstance();
        // return exporter;
        // } catch ( final Exception e ) {
        // throw new ModelerException( e );
        // }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Metamodel#id()
     */
    @Override
    public String id() {
        return id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.Metamodel#importer()
     */
    @Override
    public Importer importer() {
        return importer;
    }

    /**
     * {@inheritDoc}
     * 
     * @see Metamodel#name()
     */
    @Override
    public String name() {
        return ( StringUtil.isBlank( name ) ? id : name );
    }

    void setDependencyProcessor( final DependencyProcessor dependencyProcessor ) {
        this.dependencyProcessor = dependencyProcessor;
    }

    void setExporter( final Exporter exporter ) {
        this.exporter = exporter;
    }

    void setImporter( final Importer importer ) {
        this.importer = importer;
    }

    /**
     * {@inheritDoc}
     * 
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return ( name() + " [ category = " + category + ']' );
    }
}
