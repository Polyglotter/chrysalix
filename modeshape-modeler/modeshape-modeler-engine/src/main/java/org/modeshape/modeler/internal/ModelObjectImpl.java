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
import org.modeshape.jcr.JcrLexicon;
import org.modeshape.modeler.Model;
import org.modeshape.modeler.ModelObject;
import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.ModelerI18n;
import org.modeshape.modeler.ModelerLexicon;
import org.modeshape.modeler.internal.task.TaskWithResult;
import org.modeshape.modeler.internal.task.WriteTask;

class ModelObjectImpl implements ModelObject {

    /**
     * 
     */
    public final ModelerImpl modeler;

    /**
     * 
     */
    public final String path;

    /**
     * 
     */
    public final int index;

    ModelObjectImpl( final ModelerImpl modeler,
                     final String path,
                     final int index ) {
        this.modeler = modeler;
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
    public void addChild( final String name,
                          final String... additionalNames ) throws ModelerException {
        addChildOfType( null, name, additionalNames );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#addChildOfType(java.lang.String, java.lang.String, java.lang.String[])
     */
    @Override
    public void addChildOfType( final String primaryType,
                                final String name,
                                final String... additionalNames ) throws ModelerException {
        CheckArg.isNotEmpty( name, "name" );
        modeler.run( new WriteTask() {

            @Override
            public void run( final Session session ) throws Exception {
                final Node node = session.getNode( path );
                try {
                    node.addNode( name, primaryType );
                    for ( final String additionalName : additionalNames ) {
                        CheckArg.isNotEmpty( additionalName, "additionalName" );
                        node.addNode( additionalName, primaryType );
                    }
                } catch ( final NoSuchNodeTypeException e ) {
                    throw new IllegalArgumentException( e );
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#booleanValue(java.lang.String)
     */
    @Override
    public Boolean booleanValue( final String propertyName ) throws ModelerException {
        CheckArg.isNotEmpty( propertyName, "propertyName" );
        return modeler.run( new TaskWithResult< Boolean >() {

            @Override
            public Boolean run( final Session session ) throws Exception {
                try {
                    return session.getNode( path ).getProperty( propertyName ).getBoolean();
                } catch ( final ValueFormatException e ) {
                    throw new IllegalArgumentException( e );
                } catch ( final PathNotFoundException e ) {
                    return null;
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#booleanValues(java.lang.String)
     */
    @Override
    public Boolean[] booleanValues( final String propertyName ) throws ModelerException {
        CheckArg.isNotEmpty( propertyName, "propertyName" );
        return modeler.run( new TaskWithResult< Boolean[] >() {

            @Override
            public Boolean[] run( final Session session ) throws Exception {
                try {
                    final Property prop = session.getNode( path ).getProperty( propertyName );
                    if ( !prop.isMultiple() ) return new Boolean[] { prop.getBoolean() };
                    final Value[] vals = prop.getValues();
                    final Boolean[] booleanVals = new Boolean[ vals.length ];
                    for ( int ndx = 0; ndx < booleanVals.length; ndx++ )
                        booleanVals[ ndx ] = vals[ ndx ].getBoolean();
                    return booleanVals;
                } catch ( final ValueFormatException e ) {
                    throw new IllegalArgumentException( e );
                } catch ( final PathNotFoundException e ) {
                    return null;
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
     * @see org.modeshape.modeler.ModelObject#longValue(java.lang.String)
     */
    @Override
    public Long longValue( final String propertyName ) throws ModelerException {
        CheckArg.isNotEmpty( propertyName, "propertyName" );
        return modeler.run( new TaskWithResult< Long >() {

            @Override
            public Long run( final Session session ) throws Exception {
                try {
                    return session.getNode( path ).getProperty( propertyName ).getLong();
                } catch ( final ValueFormatException e ) {
                    throw new IllegalArgumentException( e );
                } catch ( final PathNotFoundException e ) {
                    return null;
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#longValues(java.lang.String)
     */
    @Override
    public Long[] longValues( final String propertyName ) throws ModelerException {
        CheckArg.isNotEmpty( propertyName, "propertyName" );
        return modeler.run( new TaskWithResult< Long[] >() {

            @Override
            public Long[] run( final Session session ) throws Exception {
                try {
                    final Property prop = session.getNode( path ).getProperty( propertyName );
                    if ( !prop.isMultiple() ) return new Long[] { prop.getLong() };
                    final Value[] vals = prop.getValues();
                    final Long[] longVals = new Long[ vals.length ];
                    for ( int ndx = 0; ndx < longVals.length; ndx++ )
                        longVals[ ndx ] = vals[ ndx ].getLong();
                    return longVals;
                } catch ( final ValueFormatException e ) {
                    throw new IllegalArgumentException( e );
                } catch ( final PathNotFoundException e ) {
                    return null;
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#mixinTypes()
     */
    @Override
    public String[] mixinTypes() throws ModelerException {
        return modeler.run( new TaskWithResult< String[] >() {

            @Override
            public String[] run( final Session session ) throws Exception {
                final Node node = session.getNode( path );
                final NodeType[] nodeTypes = node.getMixinNodeTypes();
                final String[] mixins = new String[ nodeTypes.length ];
                for ( int ndx = 0; ndx < mixins.length; ndx++ )
                    mixins[ ndx ] = nodeTypes[ ndx ].getName();
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
    public String primaryType() throws ModelerException {
        return modeler.run( new TaskWithResult< String >() {

            @Override
            public String run( final Session session ) throws Exception {
                return session.getNode( path ).getProperty( JcrLexicon.PRIMARY_TYPE.toString() ).getString();
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#propertyHasMultipleValues(java.lang.String)
     */
    @Override
    public boolean propertyHasMultipleValues( final String propertyName ) throws ModelerException {
        CheckArg.isNotEmpty( propertyName, "propertyName" );
        return modeler.run( new TaskWithResult< Boolean >() {

            @Override
            public Boolean run( final Session session ) throws Exception {
                try {
                    return session.getNode( path ).getProperty( propertyName ).isMultiple();
                } catch ( final PathNotFoundException e ) {
                    return false;
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
                for ( final String additionalName : additionalNames ) {
                    CheckArg.isNotEmpty( additionalName, "additionalName" );
                    if ( node.hasNode( additionalName ) ) node.getNode( additionalName ).remove();
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
    public void setMixinTypes( final String... types ) throws ModelerException {
        modeler.run( new WriteTask() {

            private void clearMixins( final Node node ) throws RepositoryException {
                for ( final NodeType type : node.getMixinNodeTypes() )
                    node.removeMixin( type.getName() );
            }

            @Override
            public void run( final Session session ) throws Exception {
                try {
                    final Node node = session.getNode( path );
                    clearMixins( node );
                    if ( types != null ) for ( final String type : types ) {
                        if ( type == null ) clearMixins( node );
                        else node.addMixin( type );
                    }
                } catch ( final ConstraintViolationException | NoSuchNodeTypeException e ) {
                    throw new IllegalArgumentException( e );
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#setPrimaryType(java.lang.String)
     */
    @Override
    public void setPrimaryType( final String type ) throws ModelerException {
        CheckArg.isNotEmpty( type, "type" );
        modeler.run( new WriteTask() {

            @Override
            public void run( final Session session ) throws Exception {
                try {
                    session.getNode( path ).setPrimaryType( type );
                } catch ( final ConstraintViolationException | NoSuchNodeTypeException e ) {
                    throw new IllegalArgumentException( e );
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#setProperty(java.lang.String, java.lang.Boolean, java.lang.Boolean[])
     */
    @Override
    public void setProperty( final String name,
                             final Boolean value,
                             final Boolean... additionalValues ) throws ModelerException {
        setProperty( name, value, new SetPropertyTask< Boolean >() {

            @Override
            public Value createValue( final ValueFactory factory,
                                      final Boolean value ) {
                return factory.createValue( value );
            }

            @Override
            public void setProperty( final Node node,
                                     final String propertyName,
                                     final Boolean value ) throws RepositoryException {
                node.setProperty( propertyName, value );
            }

        }, additionalValues );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#setProperty(java.lang.String, java.lang.Long, java.lang.Long[])
     */
    @Override
    public void setProperty( final String name,
                             final Long value,
                             final Long... additionalValues ) throws ModelerException {
        setProperty( name, value, new SetPropertyTask< Long >() {

            @Override
            public Value createValue( final ValueFactory factory,
                                      final Long value ) {
                return factory.createValue( value );
            }

            @Override
            public void setProperty( final Node node,
                                     final String propertyName,
                                     final Long value ) throws RepositoryException {
                node.setProperty( propertyName, value );
            }

        }, additionalValues );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#setProperty(java.lang.String, java.lang.String, java.lang.String[])
     */
    @Override
    public void setProperty( final String name,
                             final String value,
                             final String... additionalValues ) throws ModelerException {
        setProperty( name, value, new SetPropertyTask< String >() {

            @Override
            public Value createValue( final ValueFactory factory,
                                      final String value ) {
                return factory.createValue( value );
            }

            @Override
            public void setProperty( final Node node,
                                     final String propertyName,
                                     final String value ) throws RepositoryException {
                node.setProperty( propertyName, value );
            }

        }, additionalValues );
    }

    private < T > void setProperty( final String name,
                                    final T value,
                                    final SetPropertyTask< T > task,
                                    final T[] additionalValues ) throws ModelerException {
        CheckArg.isNotEmpty( name, "name" );
        CheckArg.isNotNull( additionalValues, "additionalValues" );
        modeler.run( new WriteTask() {

            @Override
            public void run( final Session session ) throws Exception {
                final Node node = session.getNode( path );
                try {
                    if ( additionalValues.length == 0 ) {
                        if ( value == null ) {
                            if ( node.hasProperty( name ) ) node.getProperty( name ).remove();
                        } else if ( node.hasProperty( name ) && node.getProperty( name ).isMultiple() ) {
                            setProperty( session, node );
                        } else task.setProperty( node, name, value );
                    } else setProperty( session, node );
                } catch ( final ConstraintViolationException | PathNotFoundException | ValueFormatException e ) {
                    throw new IllegalArgumentException( e );
                }
            }

            void setProperty( final Session session,
                              final Node node ) throws RepositoryException {
                final Value[] values = new Value[ additionalValues.length + 1 ];
                final ValueFactory factory = session.getValueFactory();
                values[ 0 ] = task.createValue( factory, value );
                int ndx = 1;
                for ( final T val : additionalValues )
                    values[ ndx++ ] = task.createValue( factory, val );
                if ( node.hasProperty( name ) ) {
                    node.setProperty( name, values, node.getProperty( name ).getType() );
                } else node.setProperty( name, values );
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#stringValue(java.lang.String)
     */
    @Override
    public String stringValue( final String propertyName ) throws ModelerException {
        CheckArg.isNotEmpty( propertyName, "propertyName" );
        return modeler.run( new TaskWithResult< String >() {

            @Override
            public String run( final Session session ) throws Exception {
                final Node node = session.getNode( path );
                try {
                    return node.getProperty( propertyName ).getString();
                } catch ( final ValueFormatException e ) {
                    throw new IllegalArgumentException( e );
                } catch ( final PathNotFoundException e ) {
                    return null;
                }
            }
        } );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#stringValues(java.lang.String)
     */
    @Override
    public String[] stringValues( final String propertyName ) throws ModelerException {
        CheckArg.isNotEmpty( propertyName, "propertyName" );
        return modeler.run( new TaskWithResult< String[] >() {

            @Override
            public String[] run( final Session session ) throws Exception {
                try {
                    final Property prop = session.getNode( path ).getProperty( propertyName );
                    if ( !prop.isMultiple() ) return new String[] { prop.getString() };
                    final Value[] vals = prop.getValues();
                    final String[] stringVals = new String[ vals.length ];
                    for ( int ndx = 0; ndx < stringVals.length; ndx++ )
                        stringVals[ ndx ] = vals[ ndx ].getString();
                    return stringVals;
                } catch ( final ValueFormatException e ) {
                    throw new IllegalArgumentException( e );
                } catch ( final PathNotFoundException e ) {
                    return null;
                }
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

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#value(java.lang.String)
     */
    @Override
    public Object value( final String propertyName ) throws ModelerException {
        CheckArg.isNotEmpty( propertyName, "propertyName" );
        return modeler.run( new TaskWithResult< Object >() {

            @Override
            public Object run( final Session session ) throws ModelerException {
                Value value;
                int propType;

                try {
                    final Property property = session.getNode( path ).getProperty( propertyName );
                    value = property.getValue();
                    propType = property.getType();
                } catch ( final RepositoryException e ) {
                    throw new ModelerException( e );
                }

                return value( value, propType );
            }
        } );
    }

    /**
     * @param value
     *        the JCR value holder whose value is being requested (cannot be <code>null</code>)
     * @param propertyType
     *        the {@link PropertyType type} of the value being requested
     * @return the value
     * @throws ModelerException
     *         if there is a problem obtaining the value
     */
    Object value( final Value value,
                  final int propertyType ) throws ModelerException {
        try {
            if ( propertyType == PropertyType.BINARY ) {
                return value.getBinary();
            }

            if ( propertyType == PropertyType.BOOLEAN ) {
                return Boolean.valueOf( value.getBoolean() );
            }

            if ( propertyType == PropertyType.DATE ) {
                return value.getDate();
            }

            if ( propertyType == PropertyType.DOUBLE ) {
                return Double.valueOf( value.getDouble() );
            }

            if ( ( propertyType == PropertyType.LONG ) ) {
                return value.getLong();
            }

            if ( ( propertyType == PropertyType.STRING )
                 || ( propertyType == PropertyType.NAME )
                 || ( propertyType == PropertyType.PATH )
                 || ( propertyType == PropertyType.REFERENCE )
                 || ( propertyType == PropertyType.WEAKREFERENCE )
                 || ( propertyType == PropertyType.URI ) ) {
                return value.toString();
            }

            return null;
        } catch ( final Exception e ) {
            throw new ModelerException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelObject#stringValues(java.lang.String)
     */
    @Override
    public Object[] values( final String propertyName ) throws ModelerException {
        CheckArg.isNotEmpty( propertyName, "propertyName" );
        return modeler.run( new TaskWithResult< Object[] >() {

            @Override
            public Object[] run( final Session session ) throws ModelerException {
                try {
                    final Property prop = session.getNode( path ).getProperty( propertyName );

                    if ( !prop.isMultiple() ) {
                        throw new ModelerException( ModelerI18n.notMultiValuedProperty, propertyName );
                    }

                    final int propType = prop.getType();
                    final Value[] values = prop.getValues();
                    final Object[] result = new Object[ values.length ];
                    int i = 0;

                    for ( final Value value : values ) {
                        result[ i++ ] = value( value, propType );
                    }

                    return result;
                } catch ( final Exception e ) {
                    throw new ModelerException( e );
                }
            }
        } );
    }

    private interface SetPropertyTask< T > {

        Value createValue( ValueFactory factory,
                           T value );

        void setProperty( Node node,
                          String name,
                          T value ) throws RepositoryException;
    }
}