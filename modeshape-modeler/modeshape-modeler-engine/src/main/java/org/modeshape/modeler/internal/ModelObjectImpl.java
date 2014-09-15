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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.ValueFormatException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeType;

import org.modeshape.common.util.CheckArg;
import org.modeshape.common.util.StringUtil;
import org.modeshape.jcr.JcrLexicon;
import org.modeshape.jcr.JcrNtLexicon;
import org.modeshape.jcr.api.JcrTools;
import org.modeshape.modeler.Descriptor;
import org.modeshape.modeler.Model;
import org.modeshape.modeler.ModelObject;
import org.modeshape.modeler.ModelProperty;
import org.modeshape.modeler.Modeler;
import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.ModelerI18n;
import org.modeshape.modeler.ModelerLexicon;
import org.modeshape.modeler.internal.task.Task;
import org.modeshape.modeler.internal.task.TaskWithResult;
import org.modeshape.modeler.internal.task.WriteTask;
import org.modeshape.modeler.internal.task.WriteTaskWithResult;

class ModelObjectImpl implements ModelObject {

    private static final String UNABLE_TO_REMOVE_CHILD = "Unable to remove child named '%s' in parent '%s'";
    private static final String UNABLE_TO_FIND_PROPERTY = "Unable to find property '%s' at node '%s'";
    private static final String UNABLE_TO_REMOVE_PROPERTY_THAT_DOES_NOT_EXIST =
        "Unable to remove property '%s' because it does not exist for node '%s'";
    static final String UNABLE_TO_REMOVE_SINGLE_VALUE_PROPERTY_WITH_EMPTY_ARRAY =
        "Unable to remove property '%s' of node '%s' because it is a single-valued property and an empty array cannot be used.";
    static final String UNABLE_TO_SET_SINGLE_VALUE_PROPERTY_WITH_MULTIPLE_VALUES =
        "Unable to set property '%s' of node '%s' because it is a single-valued property and an values were passed in.";

    final ModelerImpl modeler;
    final String path;
    final int index;

    ModelObjectImpl( final Modeler modeler,
                     final String path,
                     final int index ) {
        CheckArg.isNotNull( modeler, "modeler" );
        CheckArg.isNotEmpty( path, "path" );

        this.modeler = ( ModelerImpl ) modeler;
        this.path = path;
        this.index = index;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#absolutePath()
     */
    @Override
    public String absolutePath() {
        return path;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#addChild(java.lang.String, java.lang.String[])
     */
    @Override
    public ModelObject[] addChild( final String name,
                                   final String... additionalNames ) throws ModelerException {
        return addChildOfType( null, name, additionalNames );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#addChildOfType(java.lang.String, java.lang.String, java.util.Map)
     */
    @Override
    public ModelObject addChildOfType( final String primaryTypeId,
                                       final String name,
                                       final Map< String, ? > valuesByProperty ) throws ModelerException {
        CheckArg.isNotEmpty( name, "name" );
        return modeler.run( new WriteTaskWithResult< ModelObject >() {

            @Override
            public ModelObject run( final Session session ) throws Exception {
                final String id = StringUtil.isBlank( primaryTypeId ) ? JcrNtLexicon.UNSTRUCTURED.getString() : primaryTypeId;
                try {
                    final Node node = session.getNode( path ).addNode( name, id );
                    setProperty( session, node, valuesByProperty );
                    session.save(); // To catch possible constraint violation due to invalid type
                    return new ModelObjectImpl( modeler, node.getPath(), node.getIndex() );
                } catch ( final ConstraintViolationException | NoSuchNodeTypeException e ) {
                    throw new IllegalArgumentException( e );
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#addChildOfType(java.lang.String, java.lang.String, java.lang.String[])
     */
    @Override
    public ModelObject[] addChildOfType( final String primaryTypeId,
                                         final String name,
                                         final String... additionalNames ) throws ModelerException {
        CheckArg.isNotEmpty( name, "name" );
        return modeler.run( new WriteTaskWithResult< ModelObject[] >() {

            @Override
            public ModelObject[] run( final Session session ) throws Exception {
                final List< ModelObject > newKids = new ArrayList<>();
                final Node node = session.getNode( path );
                final String id = StringUtil.isBlank( primaryTypeId ) ? JcrNtLexicon.UNSTRUCTURED.getString() : primaryTypeId;

                try {
                    { // create first new child
                        final Node kidNode = node.addNode( name, id );
                        final ModelObject newModelObject = new ModelObjectImpl( modeler,
                                                                                kidNode.getPath(),
                                                                                kidNode.getIndex() );
                        newKids.add( newModelObject );
                    }

                    { // create additional children
                        if ( additionalNames != null ) {
                            for ( final String additionalName : additionalNames ) {
                                CheckArg.isNotEmpty( additionalName, "additionalName" );
                                final Node kidNode = node.addNode( additionalName, id );
                                final ModelObject newModelObject = new ModelObjectImpl( modeler,
                                                                                        kidNode.getPath(),
                                                                                        kidNode.getIndex() );
                                newKids.add( newModelObject );
                            }
                        }
                    }

                    session.save(); // To catch possible constraint violation due to invalid type
                    return newKids.toArray( new ModelObject[ newKids.size() ] );
                } catch ( final ConstraintViolationException | NoSuchNodeTypeException e ) {
                    throw new IllegalArgumentException( e );
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#addMixinType(java.lang.String, java.util.Map)
     */
    @Override
    public void addMixinType( final String typeId,
                              final Map< String, ? > valuesByProperty ) throws ModelerException {
        CheckArg.isNotEmpty( typeId, "typeId" );
        modeler.run( new WriteTask() {

            @Override
            public void run( final Session session ) throws Exception {
                final Node node = session.getNode( path );
                try {
                    node.addMixin( typeId );
                    setProperty( session, node, valuesByProperty );
                    session.save(); // To catch possible constraint violation due to invalid type
                } catch ( final ConstraintViolationException | NoSuchNodeTypeException e ) {
                    throw new IllegalArgumentException( e );
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#addMixinType(java.lang.String, java.lang.String[])
     */
    @Override
    public void addMixinType( final String typeId,
                              final String... additionalTypeIds ) throws ModelerException {
        CheckArg.isNotEmpty( typeId, "typeId" );
        modeler.run( new WriteTask() {

            @Override
            public void run( final Session session ) throws Exception {
                final Node node = session.getNode( path );
                try {
                    node.addMixin( typeId );
                    if ( additionalTypeIds != null )
                        for ( final String additionalTypeId : additionalTypeIds ) {
                            CheckArg.isNotEmpty( additionalTypeId, "additionalName" );
                            node.addMixin( additionalTypeId );
                        }
                    session.save(); // To catch possible constraint violation due to invalid type
                } catch ( final ConstraintViolationException | NoSuchNodeTypeException e ) {
                    throw new IllegalArgumentException( e );
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#child(java.lang.String)
     */
    @Override
    public ModelObject child( final String childName ) throws ModelerException {
        CheckArg.isNotEmpty( childName, "childName" );
        return modeler.run( new TaskWithResult< ModelObject >() {

            @Override
            public ModelObject run( final Session session ) throws Exception {
                try {
                    return new ModelObjectImpl( modeler, session.getNode( path ).getNode( childName ).getPath(), 0 );
                } catch ( final PathNotFoundException e ) {
                    return null;
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#childHasSameNameSiblings(java.lang.String)
     */
    @Override
    public boolean childHasSameNameSiblings( final String childName ) throws ModelerException {
        return children( childName ).length > 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#children()
     */
    @Override
    public ModelObject[] children() throws ModelerException {
        return modeler.run( new TaskWithResult< ModelObject[] >() {

            @Override
            public ModelObject[] run( final Session session ) throws Exception {
                return children( session.getNode( path ).getNodes() );
            }
        } );
    }

    ModelObject[] children( final NodeIterator iterator ) throws Exception {
        final ModelObject[] children = new ModelObject[ ( int ) iterator.getSize() ];
        for ( int ndx = 0; iterator.hasNext(); ndx++ ) {
            final Node child = iterator.nextNode();
            children[ ndx ] = new ModelObjectImpl( modeler, child.getPath(), child.getIndex() - 1 );
        }
        return children;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#children(java.lang.String)
     */
    @Override
    public ModelObject[] children( final String childName ) throws ModelerException {
        CheckArg.isNotEmpty( childName, "childName" );
        return modeler.run( new TaskWithResult< ModelObject[] >() {

            @Override
            public ModelObject[] run( final Session session ) throws Exception {
                return children( session.getNode( path ).getNodes( childName ) );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#childrenOfType(java.lang.String)
     */
    @Override
    public ModelObject[] childrenOfType( final String primaryTypeId ) throws ModelerException {
        CheckArg.isNotEmpty( primaryTypeId, "primaryTypeId" );

        return modeler.run( new TaskWithResult< ModelObject[] >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modeshape.modeler.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public ModelObject[] run( final Session session ) throws Exception {
                ModelObject[] kids = children( session.getNode( path ).getNodes() );

                if ( kids.length != 0 ) {
                    final List< ModelObject > result = new ArrayList<>( kids.length );

                    for ( final ModelObject kid : kids ) {
                        if ( primaryTypeId.equals( kid.primaryType() ) ) {
                            result.add( kid );
                        }
                    }

                    kids = result.toArray( new ModelObject[ result.size() ] );
                }

                return kids;
            }
        } );
    }

    void clearMixinTypes( final Node node ) throws RepositoryException {
        for ( final NodeType type : node.getMixinNodeTypes() )
            node.removeMixin( type.getName() );
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object object ) {
        if ( getClass() != object.getClass() ) return false;
        return Objects.equals( path, ( ( ModelObjectImpl ) object ).path );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#hasChild(java.lang.String)
     */
    @Override
    public boolean hasChild( final String childName ) throws ModelerException {
        CheckArg.isNotEmpty( childName, "childName" );
        return modeler.run( new TaskWithResult< Boolean >() {

            @Override
            public Boolean run( final Session session ) throws Exception {
                return session.getNode( path ).hasNode( childName );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#hasChildren()
     */
    @Override
    public boolean hasChildren() throws ModelerException {
        return modeler.run( new TaskWithResult< Boolean >() {

            @Override
            public Boolean run( final Session session ) throws Exception {
                return session.getNode( path ).hasNodes();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash( path );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#hasProperties()
     */
    @Override
    public boolean hasProperties() throws ModelerException {
        return propertyNames().length > 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#hasProperty(java.lang.String)
     */
    @Override
    public boolean hasProperty( final String propertyName ) throws ModelerException {
        CheckArg.isNotEmpty( propertyName, "propertyName" );
        return modeler.run( new TaskWithResult< Boolean >() {

            @Override
            public Boolean run( final Session session ) throws Exception {
                return session.getNode( path ).hasProperty( propertyName );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#index()
     */
    @Override
    public int index() {
        return index;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#mixinTypes()
     */
    @Override
    public Descriptor[] mixinTypes() throws ModelerException {
        return modeler.run( new TaskWithResult< Descriptor[] >() {

            @Override
            public Descriptor[] run( final Session session ) throws Exception {
                final Node node = session.getNode( path );
                final NodeType[] nodeTypes = node.getMixinNodeTypes();
                final Descriptor[] mixins = new Descriptor[ nodeTypes.length ];
                int i = 0;

                for ( final NodeType nodeType : nodeTypes ) {
                    mixins[ i++ ] = new DescriptorImpl( modeler, nodeType.getName() );
                }

                return mixins;
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#model()
     */
    @Override
    public Model model() throws ModelerException {
        return modeler.run( new TaskWithResult< Model >() {

            @Override
            public Model run( final Session session ) throws Exception {
                return new ModelImpl( modeler, modelNode( session ).getPath() );
            }
        } );
    }

    Modeler modeler() {
        return this.modeler;
    }

    Node modelNode( final Session session ) throws Exception {
        Node node = session.getNode( path );
        while ( !node.isNodeType( ModelerLexicon.Model.MODEL_MIXIN ) )
            node = node.getParent();
        return node;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#modelRelativePath()
     */
    @Override
    public String modelRelativePath() throws ModelerException {
        return modeler.run( new TaskWithResult< String >() {

            @Override
            public String run( final Session session ) throws Exception {
                return path.substring( modelNode( session ).getPath().length() + 1 );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#name()
     */
    @Override
    public String name() throws ModelerException {
        return modeler.run( new TaskWithResult< String >() {

            @Override
            public String run( final Session session ) throws Exception {
                return session.getNode( path ).getName();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#primaryType()
     */
    @Override
    public Descriptor primaryType() throws ModelerException {
        return modeler.run( new TaskWithResult< Descriptor >() {

            @Override
            public Descriptor run( final Session session ) throws Exception {
                final NodeType nodeType = session.getNode( path ).getPrimaryNodeType();
                return new DescriptorImpl( modeler, nodeType.getName() );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#print()
     */
    @Override
    public void print() throws ModelerException {
        modeler.run( new Task() {

            @Override
            public void run( final Session session ) throws Exception {
                final JcrTools tools = new JcrTools();
                tools.setDebug( true );
                tools.printSubgraph( session.getNode( path ) );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#property(java.lang.String)
     */
    @Override
    public ModelProperty property( final String propertyName ) throws ModelerException {
        CheckArg.isNotEmpty( propertyName, "propertyName" );

        return modeler.run( new TaskWithResult< ModelProperty >() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modeshape.modeler.internal.task.TaskWithResult#run(javax.jcr.Session)
             */
            @Override
            public ModelProperty run( final Session session ) throws ModelerException {
                try {
                    final Property jcrProperty = session.getNode( path ).getProperty( propertyName );
                    return new ModelPropertyImpl( ModelObjectImpl.this, jcrProperty );
                } catch ( final PathNotFoundException e ) {
                    return null;
                } catch ( final Exception e ) {
                    throw new ModelerException( e, ModelerI18n.localize( UNABLE_TO_FIND_PROPERTY, propertyName, path ) );
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#propertyNames()
     */
    @Override
    public String[] propertyNames() throws ModelerException {
        return modeler.run( new TaskWithResult< String[] >() {

            @Override
            public String[] run( final Session session ) throws Exception {
                final List< String > names = new ArrayList<>();
                for ( final PropertyIterator iter = session.getNode( path ).getProperties(); iter.hasNext(); ) {
                    final String name = iter.nextProperty().getName();
                    if ( !name.startsWith( JcrLexicon.Namespace.PREFIX ) && !name.startsWith( ModelerLexicon.NAMESPACE_PREFIX ) )
                        names.add( name );
                }
                return names.toArray( new String[ names.size() ] );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#removeChild(java.lang.String, java.lang.String[])
     */
    @Override
    public void removeChild( final String name,
                             final String... additionalNames ) throws ModelerException {
        CheckArg.isNotEmpty( name, "name" );
        modeler.run( new WriteTask() {

            @Override
            public void run( final Session session ) throws Exception {
                final Node node = session.getNode( path );

                if ( node.hasNode( name ) ) node.getNode( name ).remove();
                else throw new ModelerException( ModelerI18n.localize( UNABLE_TO_REMOVE_CHILD, name, path ) );

                for ( final String additionalName : additionalNames ) {
                    if ( node.hasNode( additionalName ) ) node.getNode( additionalName ).remove();
                    else throw new ModelerException( ModelerI18n.localize( UNABLE_TO_REMOVE_CHILD, additionalName, path ) );
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#removeMixinType(java.lang.String, java.lang.String[])
     */
    @Override
    public void removeMixinType( final String typeId,
                                 final String... additionalTypeIds ) throws ModelerException {
        CheckArg.isNotEmpty( typeId, "typeId" );
        modeler.run( new WriteTask() {

            @Override
            public void run( final Session session ) throws Exception {
                final Node node = session.getNode( path );
                final NodeType[] mixinTypes = node.getMixinNodeTypes();
                for ( final NodeType mixinType : mixinTypes )
                    if ( mixinType.getName().equals( typeId ) ) node.removeMixin( typeId );
                for ( final String additionalTypeId : additionalTypeIds ) {
                    CheckArg.isNotEmpty( additionalTypeId, "additionalTypeIds" );
                    for ( final NodeType mixinType : mixinTypes )
                        if ( mixinType.getName().equals( additionalTypeId ) ) node.removeMixin( additionalTypeId );
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#setMixinType(java.lang.String, java.util.Map)
     */
    @Override
    public void setMixinType( final String typeId,
                              final Map< String, ? > valuesByProperty ) throws ModelerException {
        modeler.run( new WriteTask() {

            @Override
            public void run( final Session session ) throws Exception {
                try {
                    final Node node = session.getNode( path );
                    clearMixinTypes( node );
                    if ( typeId != null ) node.addMixin( typeId );
                    setProperty( session, node, valuesByProperty );
                    session.save(); // To catch possible constraint violation due to invalid type
                } catch ( final ConstraintViolationException | NoSuchNodeTypeException e ) {
                    throw new IllegalArgumentException( e );
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#setMixinTypes(java.lang.String[])
     */
    @Override
    public void setMixinTypes( final String... typeIds ) throws ModelerException {
        modeler.run( new WriteTask() {

            @Override
            public void run( final Session session ) throws Exception {
                try {
                    final Node node = session.getNode( path );
                    clearMixinTypes( node );
                    if ( typeIds != null ) for ( final String typeId : typeIds ) {
                        if ( typeId == null ) clearMixinTypes( node );
                        else node.addMixin( typeId );
                    }
                } catch ( final ConstraintViolationException | NoSuchNodeTypeException e ) {
                    throw new IllegalArgumentException( e );
                }
            }
        } );
    }

    void setMultiValuedProperty( final Session session,
                                 final Node node,
                                 final ValueFactory factory,
                                 final String name,
                                 final Object[] propValues,
                                 final int propertyType ) throws Exception {
        final Value[] values = new Value[ propValues.length ];
        int ndx = 0;

        for ( final Object val : propValues ) {
            values[ ndx++ ] = ModelPropertyImpl.createValue( factory, val, propertyType );
        }

        node.setProperty( name, values );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#setPrimaryType(java.lang.String)
     */
    @Override
    public void setPrimaryType( final String typeId ) throws ModelerException {
        modeler.run( new WriteTask() {

            @Override
            public void run( final Session session ) throws Exception {
                try {
                    final String id = StringUtil.isBlank( typeId ) ? JcrNtLexicon.UNSTRUCTURED.getString() : typeId;
                    session.getNode( path ).setPrimaryType( id );
                    session.save(); // To catch possible constraint violation due to invalid type
                } catch ( final ConstraintViolationException | NoSuchNodeTypeException e ) {
                    throw new IllegalArgumentException( e );
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#setPrimaryType(java.lang.String, java.util.Map)
     */
    @Override
    public void setPrimaryType( final String typeId,
                                final Map< String, ? > valuesByProperty ) throws ModelerException {
        modeler.run( new WriteTask() {

            @Override
            public void run( final Session session ) throws Exception {
                try {
                    final Node node = session.getNode( path );
                    final String id = StringUtil.isBlank( typeId ) ? JcrNtLexicon.UNSTRUCTURED.getString() : typeId;
                    node.setPrimaryType( id );
                    setProperty( session, node, valuesByProperty );
                    session.save(); // To catch possible constraint violation due to invalid type
                } catch ( final ConstraintViolationException | NoSuchNodeTypeException e ) {
                    throw new IllegalArgumentException( e );
                }
            }
        } );
    }

    void setProperty( final Session session,
                      final Node node,
                      final Map< String, ? > valuesByProperty ) throws Exception {
        if ( valuesByProperty != null ) {
            for ( final Entry< String, ? > entry : valuesByProperty.entrySet() ) {
                setProperty( session, node, entry.getKey(), entry.getValue() );
            }
        }
    }

    void setProperty( final Session session,
                      final Node node,
                      final String name,
                      final Object... values ) throws Exception {
        final ValueFactory factory = session.getValueFactory();

        try {
            final boolean exists = node.hasProperty( name );

            // remove property
            if ( values == null ) {
                if ( exists ) {
                    node.getProperty( name ).remove();
                } else {
                    throw new ModelerException( ModelerI18n.localize( UNABLE_TO_REMOVE_PROPERTY_THAT_DOES_NOT_EXIST,
                                                                      name,
                                                                      node.getName() ) );
                }
            } else {
                // must be an array at this point
                final int count = values.length;

                if ( exists ) {
                    final Property property = node.getProperty( name );
                    final int type = property.getType();
                    final boolean multiple = property.isMultiple();

                    if ( count == 0 ) {
                        if ( multiple ) {
                            property.remove();
                        } else {
                            throw new ModelerException( ModelerI18n.localize( UNABLE_TO_REMOVE_SINGLE_VALUE_PROPERTY_WITH_EMPTY_ARRAY,
                                                                              name,
                                                                              node.getName() ) );
                        }
                    } else if ( count > 1 ) {
                        if ( multiple ) {
                            setMultiValuedProperty( session, node, factory, name, values, type );
                        } else {
                            throw new ModelerException( ModelerI18n.localize( UNABLE_TO_SET_SINGLE_VALUE_PROPERTY_WITH_MULTIPLE_VALUES,
                                                                              name,
                                                                              node.getName() ) );
                        }
                    } else {
                        // only one value so set property
                        if ( multiple ) {
                            setMultiValuedProperty( session, node, factory, name, values, type );
                        } else {
                            node.setProperty( name, ModelPropertyImpl.createValue( factory, values[ 0 ] ) );
                        }
                    }
                } else {
                    // property does not exist and no values being set
                    if ( count == 0 ) {
                        throw new ModelerException( ModelerI18n.localize( UNABLE_TO_REMOVE_PROPERTY_THAT_DOES_NOT_EXIST,
                                                                          name,
                                                                          node.getName() ) );
                    }

                    if ( count > 1 ) {
                        setMultiValuedProperty( session, node, factory, name, values, PropertyType.UNDEFINED );
                    } else {
                        node.setProperty( name, ModelPropertyImpl.createValue( factory, values[ 0 ] ) );
                    }
                }
            }
        } catch ( final ConstraintViolationException | PathNotFoundException | ValueFormatException e ) {
            throw new IllegalArgumentException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#setProperty(java.lang.String, java.lang.Object[])
     */
    @Override
    public void setProperty( final String name,
                             final Object... values ) throws ModelerException {
        CheckArg.isNotEmpty( name, "name" );

        modeler.run( new WriteTask() {

            /**
             * {@inheritDoc}
             * 
             * @see org.modeshape.modeler.internal.task.WriteTask#run(javax.jcr.Session)
             */
            @Override
            public void run( final Session session ) throws Exception {
                final Node node = session.getNode( path );
                setProperty( session, node, name, values );
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
        return path;
    }

}