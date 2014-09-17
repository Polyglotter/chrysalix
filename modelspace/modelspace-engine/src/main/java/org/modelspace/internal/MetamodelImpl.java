/*
 * Chrysalix
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * Chrysalix is free software. Unless otherwise indicated, all code in Chrysalix
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * Chrysalix is distributed in the hope that it will be useful,
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

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;

import org.modelspace.Descriptor;
import org.modelspace.Metamodel;
import org.modelspace.ModelspaceException;
import org.modelspace.internal.task.TaskWithResult;
import org.modelspace.spi.DependencyProcessor;
import org.modelspace.spi.Exporter;
import org.modelspace.spi.Importer;
import org.modeshape.common.util.StringUtil;

final class MetamodelImpl implements Metamodel {

    final ModelspaceImpl modelspace;
    private final String category;
    private final String id;

    private Importer importer;
    private Exporter exporter;
    private DependencyProcessor dependencyProcessor;

    private String name;

    MetamodelImpl( final ModelspaceImpl modelspace,
                   final String category,
                   final String id ) {
        this.modelspace = modelspace;
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
     * @see org.modelspace.Metamodel#dependencyProcessor()
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
        // throw new ModelspaceException( e );
        // }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Metamodel#descriptors()
     */
    @Override
    public Descriptor[] descriptors() throws ModelspaceException {
        return modelspace.run( new TaskWithResult< Descriptor[] >() {

            @Override
            public Descriptor[] run( final Session session ) throws Exception {
                final List< Descriptor > descriptors = new ArrayList<>();
                for ( final NodeTypeIterator iter = session.getWorkspace().getNodeTypeManager().getAllNodeTypes(); iter.hasNext(); ) {
                    final NodeType nodeType = iter.nextNodeType();
                    descriptors.add( new DescriptorImpl( modelspace, nodeType.getName() ) );
                }
                return descriptors.toArray( new Descriptor[ descriptors.size() ] );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Metamodel#exporter()
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
        // throw new ModelspaceException( e );
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
     * @see org.modelspace.Metamodel#importer()
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
