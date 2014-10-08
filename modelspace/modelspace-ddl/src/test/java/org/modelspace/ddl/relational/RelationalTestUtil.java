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
package org.modelspace.ddl.relational;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.modelspace.ModelspaceException;

/**
 *
 */
public class RelationalTestUtil {

    /**
     * Determine if the parent has child objects of supplied type that match the list of supplied names
     * 
     * @param model
     *        the parent
     * @param objectNames
     *        the array of names
     * @param objType
     *        the type of object
     * @return 'true' if objects match the supplied names, 'false' if not
     * @throws ModelspaceException
     *         if an error occurs
     */
    public static boolean childrenMatch( final RelationalModel model,
                                         final String[] objectNames,
                                         final RelationalConstants.Type objType ) throws ModelspaceException {
        final List< RelationalObject > rObjs = new ArrayList< RelationalObject >();
        final Collection< RelationalObject > children = model.getChildren();
        for ( final RelationalObject relObj : children ) {
            if ( relObj.getType() == objType ) {
                rObjs.add( relObj );
            }
        }

        // Must have equal numbers
        if ( objectNames.length != rObjs.size() ) {
            return false;
        }

        for ( final RelationalObject rObj : rObjs ) {
            final String objName = rObj.getName();
            boolean found = false;
            for ( final String roName : objectNames ) {
                if ( roName.equalsIgnoreCase( objName ) ) {
                    found = true;
                    break;
                }
            }
            if ( !found ) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determine if the parent has child objects of supplied type that match the list of supplied names
     * 
     * @param parent
     *        the parent
     * @param objectNames
     *        the array of names
     * @param objType
     *        the type of object
     * @return 'true' if objects match the supplied names, 'false' if not
     * @throws ModelspaceException
     *         if an error occurs
     */
    public static boolean childrenMatch( final RelationalObject parent,
                                         final String[] objectNames,
                                         final RelationalConstants.Type objType ) throws ModelspaceException {
        final List< RelationalObject > rObjs = new ArrayList< RelationalObject >();
        final Collection< RelationalObject > children = parent.getChildren();
        for ( final RelationalObject relObj : children ) {
            final RelationalConstants.Type roType = relObj.getType();
            if ( roType == objType ) {
                rObjs.add( relObj );
            }
        }

        // Must have equal numbers
        if ( objectNames.length != rObjs.size() ) {
            return false;
        }

        for ( final RelationalObject rObj : rObjs ) {
            final String objName = rObj.getName();
            boolean found = false;
            for ( final String roName : objectNames ) {
                if ( roName.equalsIgnoreCase( objName ) ) {
                    found = true;
                    break;
                }
            }
            if ( !found ) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determine if the object properties match the expected properties
     * 
     * @param relObj
     *        the relational object
     * @param expectedProps
     *        the expected properties
     * @return 'true' if properties match, 'false' if not
     * @throws ModelspaceException
     *         if an error occurs
     */
    public static String compareProperties( final RelationalObject relObj,
                                            final Map< String, Object > expectedProps ) throws ModelspaceException {
        String result = "OK"; //$NON-NLS-1$
        final Map< String, Object > actualProps = relObj.getProperties();
        if ( actualProps.size() > expectedProps.size() ) {
            for ( final String actualKey : actualProps.keySet() ) {
                if ( !expectedProps.keySet().contains( actualKey ) ) {
                    result = "Actual Props has property [" + actualKey + "], but expected props does not";
                    break;
                }
            }
            return result;
        } else if ( expectedProps.size() > actualProps.size() ) {
            for ( final String expectedKey : expectedProps.keySet() ) {
                if ( !actualProps.keySet().contains( expectedKey ) ) {
                    result = "Actual Props has property [" + expectedKey + "], but expected props does not";
                    break;
                }
            }
            return result;
        }
        for ( final String expectedName : expectedProps.keySet() ) {
            if ( !actualProps.keySet().contains( expectedName ) ) {
                result = "Object properties do not contain property '" + expectedName + "'"; //$NON-NLS-1$ //$NON-NLS-2$
                break;
            }
            final Object pVal = expectedProps.get( expectedName );
            final String expectedValue = ( pVal != null ) ? pVal.toString() : "null";
            final Object aVal = actualProps.get( expectedName );
            final String actualValue = ( aVal != null ) ? aVal.toString() : "null";
            if ( ( expectedValue == null && actualValue != null )
                 || ( actualValue == null && expectedValue != null )
                 || ( actualValue != null && !actualValue.equalsIgnoreCase( expectedValue ) ) ) {
                result =
                    "'" + expectedName + "' Actual value [" + actualValue + "] does not match Expected value [" + expectedValue + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                break;
            }
        }
        return result;
    }

    /**
     * Get the child column from the provided object
     * 
     * @param relObj
     *        the relational object
     * @param colName
     *        the name of the column
     * @return the column
     * @throws ModelspaceException
     *         if an error occurs
     */
    public static Column getChildColumn( final RelationalObject relObj,
                                         final String colName ) throws ModelspaceException {
        Column column = null;
        final List< RelationalObject > children = relObj.getChildren();
        for ( final RelationalObject childObj : children ) {
            if ( childObj.getType() == RelationalConstants.Type.COLUMN && childObj.getName().equals( colName ) ) {
                column = ( Column ) childObj;
                break;
            }
        }
        return column;
    }

    /**
     * Get the model procedure with the provided name
     * 
     * @param model
     *        the model
     * @param procName
     *        the procedure name
     * @return the procedure
     * @throws ModelspaceException
     *         if an error occurs
     */
    public static Procedure getChildProcedure( final RelationalModel model,
                                               final String procName ) throws ModelspaceException {
        Procedure proc = null;
        final List< RelationalObject > children = model.getChildren();
        for ( final RelationalObject childObj : children ) {
            if ( childObj.getType() == RelationalConstants.Type.PROCEDURE && childObj.getName().equals( procName ) ) {
                proc = ( Procedure ) childObj;
                break;
            }
        }
        return proc;
    }

    /**
     * Get the model table with the provided name
     * 
     * @param model
     *        the model
     * @param tableName
     *        the table name
     * @return the table
     * @throws ModelspaceException
     *         if an error occurs
     */
    public static Table getChildTable( final RelationalModel model,
                                       final String tableName ) throws ModelspaceException {
        Table table = null;
        final List< RelationalObject > children = model.getChildren();
        for ( final RelationalObject childObj : children ) {
            if ( childObj.getType() == RelationalConstants.Type.TABLE && childObj.getName().equals( tableName ) ) {
                table = ( Table ) childObj;
                break;
            }
        }
        return table;
    }

}
