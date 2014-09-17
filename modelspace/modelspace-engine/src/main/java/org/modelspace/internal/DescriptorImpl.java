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

import javax.jcr.Session;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.PropertyDefinition;

import org.modelspace.Descriptor;
import org.modelspace.ModelObject;
import org.modelspace.Modelspace;
import org.modelspace.ModelspaceException;
import org.modelspace.PropertyDescriptor;
import org.modelspace.internal.task.TaskWithResult;
import org.modeshape.common.util.CheckArg;

/**
 * An implementation of a {@link ModelObject model object} {@link PropertyDescriptor property descriptor}.
 */
class DescriptorImpl implements Descriptor {

    final ModelspaceImpl modelspace;
    final String name;

    DescriptorImpl( final Modelspace modelspace,
                    final String nodeTypeName ) {
        CheckArg.isNotNull( modelspace, "modelspace" );
        CheckArg.isNotEmpty( nodeTypeName, "nodeTypeName" );

        this.modelspace = ( ModelspaceImpl ) modelspace;
        this.name = nodeTypeName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Descriptor#childDescriptors()
     */
    @Override
    public Descriptor[] childDescriptors() throws ModelspaceException {
        return this.modelspace.run( new TaskWithResult< Descriptor[] >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public Descriptor[] run( final Session session ) throws Exception {
                final NodeTypeManager nodeTypeMgr = session.getWorkspace().getNodeTypeManager();
                final NodeDefinition[] childDefns = nodeTypeMgr.getNodeType( name ).getChildNodeDefinitions();
                final Descriptor[] childDescriptors = new Descriptor[ childDefns.length ];
                int i = 0;

                for ( final NodeDefinition childDefn : childDefns ) {
                    childDescriptors[ i++ ] = new DescriptorImpl( modelspace, childDefn.getName() );
                }

                return childDescriptors;
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Descriptor#name()
     */
    @Override
    public String name() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modelspace.Descriptor#propertyDescriptors()
     */
    @Override
    public PropertyDescriptor[] propertyDescriptors() throws ModelspaceException {
        return this.modelspace.run( new TaskWithResult< PropertyDescriptor[] >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modelspace.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public PropertyDescriptor[] run( final Session session ) throws Exception {
                final NodeTypeManager nodeTypeMgr = session.getWorkspace().getNodeTypeManager();
                final PropertyDefinition[] propDefns = nodeTypeMgr.getNodeType( name ).getPropertyDefinitions();
                final PropertyDescriptor[] propDescriptors = new PropertyDescriptor[ propDefns.length ];
                int i = 0;

                for ( final PropertyDefinition propDefn : propDefns ) {
                    propDescriptors[ i++ ] = new PropertyDescriptorImpl( propDefn );
                }

                return propDescriptors;
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.name;
    }

}