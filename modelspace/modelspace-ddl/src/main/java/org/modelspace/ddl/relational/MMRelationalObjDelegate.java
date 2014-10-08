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
import org.modelspace.ModelObject;
import org.modelspace.ModelProperty;
import org.modelspace.ModelspaceException;

/**
 * Implementation of the RelationalObject delegate based on ModeShape Modeler
 */
public class MMRelationalObjDelegate implements IObjectDelegate {

    ModelObject modelObj;

    /**
     * JSR-283 character encodings
     */
    char asterisk = '\uF02A';
    char fwdSlash = '\uF02F';
    char colon = '\uF03A';
    char leftBracket = '\uF05B';
    char rightBracket = '\uF05D';
    char pipe = '\uF07C';

    MMRelationalObjDelegate( final ModelObject modelObj ) {
        this.modelObj = modelObj;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public List< RelationalObject > getChildren() throws ModelspaceException {
        final List< RelationalObject > roChildren = new ArrayList< RelationalObject >();

        ModelObject[] moChildren = null;
        try {
            moChildren = this.modelObj.children();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        if ( moChildren == null ) return roChildren;

        for ( final ModelObject mo : moChildren ) {
            final IObjectDelegate roDelegate = new MMRelationalObjDelegate( mo );
            final RelationalConstants.Type relationalType = roDelegate.getType();
            if ( relationalType == null ) continue;
            switch ( relationalType ) {
                case TABLE:
                    roChildren.add( new Table( roDelegate ) );
                    break;
                case ACCESS_PATTERN:
                    roChildren.add( new AccessPattern( roDelegate ) );
                    break;
                case CATALOG:
                    break;
                case COLUMN:
                    roChildren.add( new Column( roDelegate ) );
                    break;
                case FOREIGN_KEY:
                    roChildren.add( new ForeignKey( roDelegate ) );
                    break;
                case INDEX:
                    roChildren.add( new Index( roDelegate ) );
                    break;
                case MODEL:
                    break;
                case PARAMETER:
                    roChildren.add( new Parameter( roDelegate ) );
                    break;
                case PRIMARY_KEY:
                    roChildren.add( new PrimaryKey( roDelegate ) );
                    break;
                case PROCEDURE:
                    roChildren.add( new Procedure( roDelegate ) );
                    break;
                case RESULT_SET:
                    roChildren.add( new ProcedureResultSet( roDelegate ) );
                    break;
                case SCHEMA:
                    roChildren.add( new Schema( roDelegate ) );
                    break;
                case UNIQUE_CONSTRAINT:
                    roChildren.add( new UniqueConstraint( roDelegate ) );
                    break;
                case VIEW:
                    roChildren.add( new View( roDelegate ) );
                    break;
                default:
                    break;
            }
        }
        return roChildren;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public Map< String, String > getExtensionProperties() {
        final Map< String, String > extensionProps = new HashMap< String, String >();
        try {
            final List< ModelObject > statementOptions = getStatementOptions();
            for ( final ModelObject modelObj : statementOptions ) {
                // Get name, replacing any jsr-283 encodings
                final String optionName = replaceJsr283Chars( modelObj.name() );
                if ( isPrefixNamespaced( optionName ) || isUriNamespaced( optionName ) ) {
                    final ModelProperty prop = modelObj.property( StandardDdlLexicon.VALUE );
                    extensionProps.put( optionName, prop.stringValue() );
                }
            }
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        return extensionProps;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public String getName() {
        String name = null;
        try {
            name = this.modelObj.name();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        return name;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public Object getOptionValue( final String name ) {
        Object optionValue = null;
        try {
            final List< ModelObject > statementOptions = getStatementOptions();
            for ( final ModelObject modelObj : statementOptions ) {
                if ( modelObj.name().equals( name ) ) {
                    final ModelProperty prop = modelObj.property( StandardDdlLexicon.VALUE );
                    optionValue = prop.value();
                    break;
                }
            }
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        return optionValue;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public Object getPropertyValue( final String name ) throws ModelspaceException {
        final ModelProperty prop = modelObj.property( name );
        if ( prop == null ) return null;

        Object propValue = null;

        try {
            propValue = prop.value();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        return propValue;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public Object[] getPropertyValues( final String name ) throws ModelspaceException {
        final ModelProperty prop = modelObj.property( name );
        if ( ( prop == null ) || !prop.descriptor().multiple() ) return null;

        Object[] propValues = null;

        try {
            propValues = prop.values();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        return propValues;
    }

    /**
     * Get the statementOption with the provided name
     * 
     * @param name
     *        the name
     * @return the StatementOption, null if not found
     * @throws ModelspaceException
     *         if an error occurs
     */
    private ModelObject getStatementOption( final String name ) throws ModelspaceException {
        ModelObject result = null;
        final List< ModelObject > statementOptions = getStatementOptions();

        for ( final ModelObject mObj : statementOptions ) {
            String mObjName = null;
            try {
                mObjName = mObj.name();
            } catch ( final ModelspaceException e ) {
                // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
            }
            if ( mObjName != null && mObjName.equals( name ) ) {
                result = mObj;
                break;
            }
        }
        return result;
    }

    /**
     * Get the ModelObjects that are statement options
     * 
     * @return the list of statement option objects
     * @throws ModelspaceException
     *         if an error occurs
     */
    private List< ModelObject > getStatementOptions() throws ModelspaceException {
        final List< ModelObject > statementOptions = new ArrayList< ModelObject >();

        ModelObject[] moChildren = null;
        try {
            moChildren = this.modelObj.children();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        if ( moChildren == null ) return statementOptions;

        for ( final ModelObject mo : moChildren ) {
            if ( hasMixinType( mo, StandardDdlLexicon.TYPE_STATEMENT_OPTION ) ) {
                statementOptions.add( mo );
            }
        }
        return statementOptions;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public RelationalConstants.Type getType() throws ModelspaceException {
        RelationalConstants.Type relationalType = null;
        Descriptor[] mixinTypes = null;
        Object constraintType = null;
        try {
            mixinTypes = modelObj.mixinTypes();
            final ModelProperty prop = modelObj.property( TeiidDdlLexicon.Constraint.TYPE );
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
    public boolean hasChildren() throws ModelspaceException {
        return getChildren().isEmpty() ? false : true;
    }

    private boolean hasMixinType( final ModelObject modelObject,
                                  final String type ) throws ModelspaceException {
        boolean hasMixin = false;
        Descriptor[] mixinTypes = null;
        try {
            mixinTypes = modelObject.mixinTypes();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        if ( mixinTypes == null ) return hasMixin;

        for ( final Descriptor mType : mixinTypes ) {
            if ( mType.name().equals( type ) ) {
                hasMixin = true;
                break;
            }
        }
        return hasMixin;
    }

    /**
     * Determine if the supplied property name has open and closed braces
     * 
     * @param propName
     *        the extension property name
     * @return 'true' if both open and closed braces are found
     */
    private boolean hasOpenCloseBraces( final String propName ) {
        boolean hasBoth = false;
        if ( propName != null && propName.indexOf( '{' ) != -1 && propName.indexOf( '}' ) != -1 ) {
            hasBoth = true;
        }
        return hasBoth;
    }

    /* (non-Javadoc)
     * @see org.komodo.relational.model2.IObjectDelegate#isExtensionProperty(java.lang.String)
     */
    @SuppressWarnings( "javadoc" )
    @Override
    public boolean isExtensionProperty( final String propName ) {
        boolean isExtension = false;
        if ( isPrefixNamespaced( propName ) || isUriNamespaced( propName ) ) {
            isExtension = true;
        }
        return isExtension;
    }

    /**
     * Determine if the property name has a leading namespace prefix
     * 
     * @param propName
     *        the extension property name, including namespace
     * @return 'true' if a namespace is present, 'false' if not.
     */
    private boolean isPrefixNamespaced( final String propName ) {
        boolean isPrefixNamespaced = false;
        if ( propName != null && !hasOpenCloseBraces( propName ) && propName.indexOf( ':' ) != -1 ) {
            isPrefixNamespaced = true;
        }
        return isPrefixNamespaced;
    }

    /**
     * Determine if the property name has a leading namespace uri
     * 
     * @param propName
     *        the extension property name, including namespace uri
     * @return 'true' if a namespace uri is present, 'false' if not.
     */
    private boolean isUriNamespaced( final String propName ) {
        boolean isUriNamespaced = false;
        if ( propName != null && hasOpenCloseBraces( propName ) ) {
            isUriNamespaced = true;
        }
        return isUriNamespaced;
    }

    /*
     * Replace any jsr283 chars in the supplied string with the corresponding standard character
     */
    private String replaceJsr283Chars( final String inStr ) {
        final StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < inStr.length(); i++ ) {
            final char c = inStr.charAt( i );
            if ( c == colon ) {
                sb.append( ':' );
            } else if ( c == asterisk ) {
                sb.append( '*' );
            } else if ( c == fwdSlash ) {
                sb.append( '/' );
            } else if ( c == leftBracket ) {
                sb.append( '[' );
            } else if ( c == rightBracket ) {
                sb.append( ']' );
            } else if ( c == pipe ) {
                sb.append( '|' );
            } else {
                sb.append( c );
            }
        }
        return sb.toString();
    }

    /**
     * Set the mixinType of the specified child
     * 
     * @param name
     *        the child name
     * @param mixinType
     *        the mixinType
     * @param propMap
     *        the map of (possibly mandatory) props
     * @throws ModelspaceException
     *         the exception
     */
    private void setChildMixinType( final String name,
                                    final String mixinType,
                                    final Map< String, ? > propMap ) throws ModelspaceException {
        ModelObject[] moChildren = null;
        try {
            moChildren = this.modelObj.children();
        } catch ( final Exception ex ) {
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        if ( moChildren != null ) {
            for ( final ModelObject mo : moChildren ) {
                if ( mo.name().equals( name ) ) {
                    mo.addMixinType( mixinType, propMap );
                }
            }
        }
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public boolean setOptionValue( final String name,
                                   final Object value ) {
        boolean wasSet = false;
        try {
            // Get the statement options with supplied name
            final ModelObject statementOption = getStatementOption( name );
            // If no statement option, create one
            if ( statementOption == null ) {
                // Create new option node
                this.modelObj.addChild( name );
                // Property map - value is required
                final Map< String, Object > propMap = new HashMap< String, Object >();
                propMap.put( StandardDdlLexicon.VALUE, value );
                // Set the Type and value property
                setChildMixinType( name, StandardDdlLexicon.TYPE_STATEMENT_OPTION, propMap );
                wasSet = true;
            } else {
                statementOption.setProperty( StandardDdlLexicon.VALUE, value );
                wasSet = true;
            }
        } catch ( final Exception ex ) {
            System.out.println();
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        return wasSet;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public boolean setPropertyValue( final String name,
                                     final Object value ) {
        boolean wasSet = false;
        try {
            modelObj.setProperty( name, value );
            wasSet = true;
        } catch ( final Exception ex ) {
            System.out.println();
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        return wasSet;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public boolean unsetOptionValue( final String name ) {
        boolean wasSet = false;
        try {
            this.modelObj.removeChild( name );
            wasSet = true;
        } catch ( final Exception ex ) {
            System.out.println();
            // KLog.getLogger().error(ex.getLocalizedMessage(), ex);
        }
        return wasSet;
    }

}
