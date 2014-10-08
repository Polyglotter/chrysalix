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

import java.util.List;
import java.util.Map;

import org.modelspace.ModelspaceException;

/**
 * Procedure The relational procedure class
 */
public class Procedure extends RelationalObject {

    Procedure( final IObjectDelegate modelObject ) {
        super( modelObject );
    }

    /**
     * @return deterministic
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getDeterminism() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.DETERMINISM ).toString();
    }

    /**
     * @return function category
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getFunctionCategory() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.CATEGORY ).toString();
    }

    /**
     * @return java class name for function may be null
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getJavaClassName() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.JAVA_CLASS ).toString();
    }

    /**
     * @return java class name for function may be null
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getJavaMethodName() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.JAVA_METHOD ).toString();
    }

    /**
     * @return nativeQuery may be null
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getNativeQuery() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NATIVE_QUERY ).toString();
    }

    /**
     * Get the Parameter with the specified name (if it exists)
     * 
     * @param paramName
     *        the parameter name
     * @return Parameter
     * @throws ModelspaceException
     *         if an error occurs
     */
    public Parameter getParameter( final String paramName ) throws ModelspaceException {
        Parameter result = null;
        final List< Parameter > params = getChildren( Parameter.class );
        for ( final Parameter param : params ) {
            if ( param.getName().equalsIgnoreCase( paramName ) ) {
                result = param;
                break;
            }
        }
        return result;
    }

    /**
     * Get the child Parameters
     * 
     * @return Parameter list
     * @throws ModelspaceException
     *         if an error occurs
     */
    public List< Parameter > getParameters() throws ModelspaceException {
        return getChildren( Parameter.class );
    }

    /**
     * @return function
     */
    // public boolean isFunction() {
    // String propValue = getPropertyValue(RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.);
    // return propValue == null ? RelationalConstants.PROCEDURE_DEFAULT.NON_PREPARED : Boolean.parseBoolean(propValue);
    // }

    /**
     * Get the child ResultSet, 'null' if none defined
     * 
     * @return ResultSet
     * @throws ModelspaceException
     *         if an error occurs
     */
    public ProcedureResultSet getProcedureResultSet() throws ModelspaceException {
        final List< ProcedureResultSet > rsList = getChildren( ProcedureResultSet.class );
        return ( rsList.size() != 1 ) ? null : rsList.get( 0 );
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public Map< String, Object > getProperties() throws ModelspaceException {
        final Map< String, Object > props = super.getProperties();

        // Add values for column properties
        String[] propKeys = RelationalConstants.PROCEDURE_DDL_PROPERTY_KEYS.toArray();
        for ( int i = 0; i < propKeys.length; i++ ) {
            props.put( propKeys[ i ], getPropertyValue( propKeys[ i ] ) );
        }

        // Add values for column options
        propKeys = RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.toArray();
        for ( int i = 0; i < propKeys.length; i++ ) {
            props.put( propKeys[ i ], getPropertyValue( propKeys[ i ] ) );
        }

        return props;
    }

    @SuppressWarnings( "javadoc" )
    @Override
    public Object getPropertyValue( final String propertyKey ) throws ModelspaceException {
        Object propertyValue = super.getPropertyValue( propertyKey );

        if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.UPDATECOUNT ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.UPDATECOUNT : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.CATEGORY ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.CATEGORY : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.AGGREGATE ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.AGGREGATE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ALLOWS_DISTINCT ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.ALLOWS_DISTINCT : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ALLOWS_ORDERBY ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.ALLOWS_ORDERBY : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ANALYTIC ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.ANALYTIC : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.DECOMPOSABLE ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.DECOMPOSABLE : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NON_PREPARED ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.NON_PREPARED : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NULL_ON_NULL ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.NULL_ON_NULL : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.USES_DISTINCT_ROWS ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.USES_DISTINCT_ROWS : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.VARARGS ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.VARARGS : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.DETERMINISM ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.DETERMINISM : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NATIVE_QUERY ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.NATIVE_QUERY : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.JAVA_CLASS ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.JAVA_CLASS : propertyValue;
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.JAVA_METHOD ) ) {
            propertyValue = this.delegate.getOptionValue( propertyKey );
            propertyValue = ( propertyValue == null ) ? RelationalConstants.PROCEDURE_DEFAULT.JAVA_METHOD : propertyValue;
        }
        return propertyValue;
    }

    /**
     * @return updateCount
     * @throws ModelspaceException
     *         if an error occurs
     */
    public String getUpdateCount() throws ModelspaceException {
        return getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.UPDATECOUNT ).toString();
    }

    /**
     * @return aggregate
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isAggregate() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.AGGREGATE ).toString() );
    }

    /**
     * @return allowsDistinct
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isAllowsDistinct() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ALLOWS_DISTINCT ).toString() );
    }

    /**
     * @return allowsOrderBy
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isAllowsOrderBy() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ALLOWS_ORDERBY ).toString() );
    }

    /**
     * @return analytic
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isAnalytic() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ANALYTIC ).toString() );
    }

    /**
     * @return decomposable
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isDecomposable() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.DECOMPOSABLE ).toString() );
    }

    /**
     * @return function
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isNonPrepared() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NON_PREPARED ).toString() );
    }

    /**
     * @return returnsNullOnNull
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isReturnsNullOnNull() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NULL_ON_NULL ).toString() );
    }

    /**
     * @return useDistinctRows
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isUseDistinctRows() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.USES_DISTINCT_ROWS ).toString() );
    }

    /**
     * @return variableArguments
     * @throws ModelspaceException
     *         if an error occurs
     */
    public boolean isVariableArguments() throws ModelspaceException {
        return Boolean.parseBoolean( getPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.VARARGS ).toString() );
    }

    /**
     * @param aggregate
     *        is aggregate value
     */
    public void setAggregate( final boolean aggregate ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.AGGREGATE, aggregate );
    }

    /**
     * @param allowsDistinct
     *        value
     */
    public void setAllowsDistinct( final boolean allowsDistinct ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ALLOWS_DISTINCT, allowsDistinct );
    }

    /**
     * @param allowsOrderBy
     *        value
     */
    public void setAllowsOrderBy( final boolean allowsOrderBy ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ALLOWS_ORDERBY, allowsOrderBy );
    }

    /**
     * @param analytic
     *        value
     */
    public void setAnalytic( final boolean analytic ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ANALYTIC, analytic );
    }

    /**
     * @param decomposable
     *        value
     */
    public void setDecomposable( final boolean decomposable ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.DECOMPOSABLE, decomposable );
    }

    /**
     * @return is source function
     */
    // public boolean isSourceFunction() {
    // String propValue = getPropertyValue(RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NON_PREPARED);
    // return propValue == null ? RelationalConstants.PROCEDURE_DEFAULT.NON_PREPARED : Boolean.parseBoolean(propValue);
    // }

    /**
     * @param determinism
     *        value
     */
    public void setDeterminism( final String determinism ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.DETERMINISM, determinism );
    }

    /**
     * @param category
     *        value
     */
    public void setFunctionCategory( final String category ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.CATEGORY, category );
    }

    /**
     * @param className
     *        value
     */
    public void setJavaClassName( final String className ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.JAVA_CLASS, className );
    }

    /**
     * @param methodName
     *        value
     */
    public void setJavaMethodName( final String methodName ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.JAVA_METHOD, methodName );
    }

    /**
     * @param nativeQuery
     *        may be null
     */
    public void setNativeQuery( final String nativeQuery ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NATIVE_QUERY, nativeQuery );
    }

    /**
     * @param nonPrepared
     *        value
     */
    public void setNonPrepared( final boolean nonPrepared ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NON_PREPARED, nonPrepared );
    }

    @Override
    @SuppressWarnings( "javadoc" )
    public boolean setPropertyValue( final String propertyKey,
                                     final Object propertyValue ) {
        boolean wasSet = super.setPropertyValue( propertyKey, propertyValue );
        if ( wasSet ) return true;

        // ---------------------------
        // Statement Options
        // ---------------------------
        if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.UPDATECOUNT ) ) {
            if ( propertyValue == null || ( ( String ) propertyValue ).equals( RelationalConstants.PROCEDURE_DEFAULT.UPDATECOUNT ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.CATEGORY ) ) {
            if ( propertyValue == null || ( ( String ) propertyValue ).equals( RelationalConstants.PROCEDURE_DEFAULT.CATEGORY ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.AGGREGATE ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.PROCEDURE_DEFAULT.AGGREGATE ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ALLOWS_DISTINCT ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.PROCEDURE_DEFAULT.ALLOWS_DISTINCT ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ALLOWS_ORDERBY ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.PROCEDURE_DEFAULT.ALLOWS_ORDERBY ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ANALYTIC ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.PROCEDURE_DEFAULT.ANALYTIC ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.DECOMPOSABLE ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.PROCEDURE_DEFAULT.DECOMPOSABLE ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NON_PREPARED ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.PROCEDURE_DEFAULT.NON_PREPARED ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NULL_ON_NULL ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.PROCEDURE_DEFAULT.NULL_ON_NULL ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.USES_DISTINCT_ROWS ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.PROCEDURE_DEFAULT.USES_DISTINCT_ROWS ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.VARARGS ) ) {
            if ( propertyValue == null || ( ( Boolean ) propertyValue ) == RelationalConstants.PROCEDURE_DEFAULT.VARARGS ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.DETERMINISM ) ) {
            if ( propertyValue == null || ( ( String ) propertyValue ).equals( RelationalConstants.PROCEDURE_DEFAULT.DETERMINISM ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NATIVE_QUERY ) ) {
            if ( propertyValue == null || ( ( String ) propertyValue ).equals( RelationalConstants.PROCEDURE_DEFAULT.NATIVE_QUERY ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.JAVA_CLASS ) ) {
            if ( propertyValue == null || ( ( String ) propertyValue ).equals( RelationalConstants.PROCEDURE_DEFAULT.JAVA_CLASS ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        } else if ( propertyKey.equals( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.JAVA_METHOD ) ) {
            if ( propertyValue == null || ( ( String ) propertyValue ).equals( RelationalConstants.PROCEDURE_DEFAULT.JAVA_METHOD ) ) {
                wasSet = this.delegate.unsetOptionValue( propertyKey );
            } else {
                wasSet = this.delegate.setOptionValue( propertyKey, propertyValue );
            }
        }
        return wasSet;
    }

    /**
     * @param returnsNullOnNull
     *        value
     */
    public void setReturnsNullOnNull( final boolean returnsNullOnNull ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NULL_ON_NULL, returnsNullOnNull );
    }

    /**
     * @param updateCount
     *        the update count
     */
    public void setUpdateCount( final String updateCount ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.UPDATECOUNT, updateCount );
    }

    /**
     * @param useDistinctRows
     *        value
     */
    public void setUseDistinctRows( final boolean useDistinctRows ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.USES_DISTINCT_ROWS, useDistinctRows );
    }

    /**
     * @param variableArguments
     *        value
     */
    public void setVariableArguments( final boolean variableArguments ) {
        setPropertyValue( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.VARARGS, variableArguments );
    }

}
