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
package org.modelspace.ddl.relational;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelspace.Descriptor;
import org.modelspace.Model;
import org.modelspace.ModelObject;
import org.modelspace.ModelProperty;
import org.modelspace.ModelspaceException;

/**
 * Implementation of the model delegate based on ModeShape Modeler
 */
public class MMModelDelegate implements IModelDelegate {

    Model model;

    /**
     * Constructor
     * 
     * @param model
     *        the Modeshape Modeler model
     */
    public MMModelDelegate( final Model model ) {
        this.model = model;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public List< RelationalObject > getChildren() throws ModelspaceException {
        final List< RelationalObject > roChildren = new ArrayList< RelationalObject >();
        ModelObject[] moChildren = null;
        try {
            // Model has one child (ddl:statements) that has children statements
            moChildren = this.model.children();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        if ( moChildren == null ) return roChildren;

        try {
            if ( moChildren.length == 1 && moChildren[ 0 ].hasChildren() ) {
                // the statement children for the model
                moChildren = moChildren[ 0 ].children();
            }
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        if ( moChildren == null ) return roChildren;

        for ( final ModelObject mo : moChildren ) {
            final RelationalConstants.Type moType = getRelationalType( mo );
            if ( moType != null ) {
                if ( moType == RelationalConstants.Type.TABLE ) {
                    roChildren.add( new Table( new MMRelationalObjDelegate( mo ) ) );
                } else if ( moType == RelationalConstants.Type.PROCEDURE ) {
                    roChildren.add( new Procedure( new MMRelationalObjDelegate( mo ) ) );
                }
            }
        }
        return roChildren;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public String getName() {
        String mName = null;
        try {
            mName = this.model.name();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        return mName;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public Map< String, String > getNamespaceMap() throws ModelspaceException {
        final Map< String, String > namespaceMap = new HashMap< String, String >();
        ModelObject[] moChildren = null;
        try {
            moChildren = this.model.children();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        if ( moChildren == null ) return namespaceMap;

        try {
            if ( moChildren.length == 1 && moChildren[ 0 ].hasChildren() ) {
                moChildren = moChildren[ 0 ].children();
            }
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        if ( moChildren == null ) return namespaceMap;

        for ( final ModelObject mo : moChildren ) {
            if ( isNamespaceOption( mo ) ) {
                String nsPrefix = null;
                String nsUri = null;
                try {
                    nsPrefix = mo.name();
                    final ModelProperty prop = mo.property( TeiidDdlLexicon.OptionNamespace.URI );
                    nsUri = prop.stringValue();
                    namespaceMap.put( nsPrefix, nsUri );
                } catch ( final Exception ex ) {
                    // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
                }
            }
        }
        return namespaceMap;
    }

    private RelationalConstants.Type getRelationalType( final ModelObject modelObject ) throws ModelspaceException {
        RelationalConstants.Type relationalType = null;
        Descriptor[] mixinTypes = null;
        Object constraintType = null;
        try {
            mixinTypes = modelObject.mixinTypes();
            final ModelProperty prop = modelObject.property( TeiidDdlLexicon.Constraint.TYPE );
            constraintType = prop.value();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }

        if ( mixinTypes == null ) return null;
        final String cType = ( constraintType == null ) ? null : constraintType.toString();
        for ( final Descriptor type : mixinTypes ) {
            relationalType = TeiidLexiconMapper.getRelationalType( type.name(), cType );
            if ( relationalType != null ) {
                break;
            }
        }
        return relationalType;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public boolean hasChildren() {
        boolean hasChildren = false;
        try {
            hasChildren = !getChildren().isEmpty();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        return hasChildren;
    }

    private boolean isNamespaceOption( final ModelObject modelObject ) throws ModelspaceException {
        boolean isNamespace = false;
        Descriptor[] mixinTypes = null;
        try {
            mixinTypes = modelObject.mixinTypes();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        if ( mixinTypes == null ) return false;
        for ( final Descriptor type : mixinTypes ) {
            if ( type.name().equals( TeiidDdlLexicon.OptionNamespace.STATEMENT ) ) {
                isNamespace = true;
            }
        }
        return isNamespace;
    }

}