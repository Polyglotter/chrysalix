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

import java.util.HashSet;
import java.util.Set;

import javax.jcr.Session;

import org.infinispan.commons.util.ReflectionUtil;
import org.modeshape.common.util.CheckArg;
import org.modeshape.common.util.StringUtil;
import org.modeshape.jcr.ExtensionLogger;
import org.modeshape.jcr.api.Repository;
import org.modeshape.jcr.api.nodetype.NodeTypeManager;
import org.modeshape.jcr.api.sequencer.Sequencer;
import org.modeshape.modeler.ModelType;
import org.modeshape.modeler.ModelerException;
import org.modeshape.modeler.extensions.DependencyProcessor;
import org.modeshape.modeler.extensions.Desequencer;

/**
 * 
 */
public final class ModelTypeImpl implements ModelType {

    private final Manager manager;

    Sequencer sequencer;
    final String sequencerClassName;
    final Class< Sequencer > sequencerClass;

    private DependencyProcessor dependencyProcessor;
    private String dependencyProcessorClassName;

    private Desequencer desequencer;
    private String desequencerClassName;

    private final String category;
    private final String id;
    private String name;
    private final Set< String > sourceFileExtensions = new HashSet<>();

    /**
     * @param manager
     *        the manager used to access the MS repository (cannot be <code>null</code>)
     * @param category
     *        the model type category (cannot be <code>null</code> or empty)
     * @param id
     *        the model type identifier (cannot be <code>null</code> or empty)
     * @param sequencerClass
     *        the sequencer class (cannot be <code>null</code>)
     */
    ModelTypeImpl( final Manager manager,
                   final String category,
                   final String id,
                   final Class< Sequencer > sequencerClass ) {
        this( manager, category, id, sequencerClass, null );
    }

    /**
     * @param manager
     *        the manager used to access the MS repository (cannot be <code>null</code>)
     * @param category
     *        the model type category (cannot be <code>null</code> or empty)
     * @param id
     *        the model type identifier (cannot be <code>null</code> or empty)
     * @param sequencerClass
     *        the sequencer class (cannot be <code>null</code> if sequencer class name is <code>null</code> or empty)
     * @param sequencerClassName
     *        the name of the sequencer class (cannot be <code>null</code> or empty if sequencer class is <code>null</code>)
     */
    private ModelTypeImpl( final Manager manager,
                           final String category,
                           final String id,
                           final Class< Sequencer > sequencerClass,
                           final String sequencerClassName ) {
        CheckArg.isNotNull( manager, "manager" );
        CheckArg.isNotEmpty( category, "category" );
        CheckArg.isNotEmpty( id, "id" );

        if ( sequencerClass == null ) {
            CheckArg.isNotEmpty( sequencerClassName, "sequencerClassName" );
        } else if ( StringUtil.isBlank( sequencerClassName ) ) {
            CheckArg.isNotNull( sequencerClass, "sequencerClass" );
        }

        this.manager = manager;
        this.category = category;
        this.id = id;
        this.sequencerClass = sequencerClass;
        this.sequencerClassName = sequencerClassName;
    }

    /**
     * @param manager
     *        the manager used to access the MS repository (cannot be <code>null</code>)
     * @param category
     *        the model type category (cannot be <code>null</code> or empty)
     * @param id
     *        the model type identifier (cannot be <code>null</code> or empty)
     * @param sequencerClassName
     *        the name of the sequencer class (cannot be <code>null</code> or empty)
     * @param desequencerClassName
     *        the name of the desequencer class (can be <code>null</code> or empty)
     * @param dependencyProcessorClassName
     *        the name of the dependency processor class (can be <code>null</code> or empty)
     */
    ModelTypeImpl( final Manager manager,
                   final String category,
                   final String id,
                   final String sequencerClassName,
                   final String desequencerClassName,
                   final String dependencyProcessorClassName ) {
        this( manager, category, id, null, sequencerClassName );
        this.desequencerClassName = desequencerClassName;
        this.dependencyProcessorClassName = dependencyProcessorClassName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelType#category()
     */
    @Override
    public String category() {
        return category;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelType#dependencyProcessor()
     */
    @Override
    public DependencyProcessor dependencyProcessor() throws ModelerException {
        if ( dependencyProcessor != null ) return dependencyProcessor;
        if ( StringUtil.isBlank( dependencyProcessorClassName ) ) return null;

        try {
            final Class< ? > clazz = libraryClassLoader().loadClass( dependencyProcessorClassName );
            dependencyProcessor = ( DependencyProcessor ) clazz.newInstance();
            return dependencyProcessor;
        } catch ( final Exception e ) {
            throw new ModelerException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelType#desequencer()
     */
    @Override
    public Desequencer desequencer() throws ModelerException {
        if ( desequencer != null ) return desequencer;
        if ( StringUtil.isBlank( desequencerClassName ) ) return null;

        try {
            final Class< ? > clazz = libraryClassLoader().loadClass( desequencerClassName );
            desequencer = ( Desequencer ) clazz.newInstance();
            return desequencer;
        } catch ( final Exception e ) {
            throw new ModelerException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelType#id()
     */
    @Override
    public String id() {
        return id;
    }

    ClassLoader libraryClassLoader() throws Exception {
        return manager.modelTypeManager().libraryClassLoader;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelType#name()
     */
    @Override
    public String name() {
        return ( StringUtil.isBlank( name ) ? id : name );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.modeler.ModelType#sequencer()
     */
    @Override
    public Sequencer sequencer() throws ModelerException {
        if ( sequencer != null ) return sequencer;

        return manager.run( new Task< Sequencer >() {

            @Override
            public Sequencer run( final Session session ) throws Exception {
                if ( sequencerClass == null ) {
                    final Class< ? > clazz = libraryClassLoader().loadClass( sequencerClassName );
                    sequencer = ( Sequencer ) clazz.newInstance();
                } else {
                    sequencer = sequencerClass.newInstance();
                }

                // Initialize
                ReflectionUtil.setValue( sequencer, "logger", ExtensionLogger.getLogger( sequencer.getClass() ) );
                ReflectionUtil.setValue( sequencer, "repositoryName",
                                         session.getRepository().getDescriptor( Repository.REPOSITORY_NAME ) );
                ReflectionUtil.setValue( sequencer, "name", sequencer.getClass().getSimpleName() );
                sequencer.initialize( session.getWorkspace().getNamespaceRegistry(),
                                      ( NodeTypeManager ) session.getWorkspace().getNodeTypeManager() );

                return sequencer;
            }
        } );
    }

    void setDependencyProcessor( final DependencyProcessor dependencyProcessor ) {
        this.dependencyProcessor = dependencyProcessor;
    }

    void setDesequencer( final Desequencer desequencer ) {
        this.desequencer = desequencer;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelType#setName(String)
     */
    @Override
    public void setName( final String name ) {
        this.name = name;
    }

    /**
     * @param newFileExtensions
     *        the new file extensions (can be <code>null</code> or empty)
     */
    void setSourceFileExtensions( final String[] newFileExtensions ) {
        sourceFileExtensions.clear();

        if ( newFileExtensions != null ) {
            for ( final String ext : newFileExtensions ) {
                sourceFileExtensions.add( ext );
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelType#sourceFileExtensions()
     */
    @Override
    public String[] sourceFileExtensions() {
        return sourceFileExtensions.toArray( new String[ sourceFileExtensions.size() ] );
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
