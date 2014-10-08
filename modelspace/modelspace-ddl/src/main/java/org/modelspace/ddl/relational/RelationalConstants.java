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

import java.util.HashMap;
import java.util.Map;

/**
 * 
 *
 *
 */
@SuppressWarnings( "javadoc" )
public interface RelationalConstants {

    final static String XMI_EXT = ".xmi"; //$NON-NLS-1$

    public static final String PLUGIN_ID = "org.komodo.relational"; //$NON-NLS-1$

    class BASE_TABLE_EXT_PROPERTIES {

        public static final String NATIVE_QUERY = "relational:native-query"; //$NON-NLS-1$
        public static final String VIEW_TABLE_GLOBAL_TEMP_TABLE = "relational:global-temp-table"; //$NON-NLS-1$
    }

    class COLUMN_DDL_OPTION_KEYS {

        public static final String NULL_VALUE_COUNT = "NULL_VALUE_COUNT"; //$NON-NLS-1$
        public static final String DATATYPE = "DATATYPE"; //$NON-NLS-1$
        public static final String NATIVE_TYPE = "NATIVE_TYPE"; //$NON-NLS-1$
        public static final String CASE_SENSITIVE = "CASE_SENSITIVE"; //$NON-NLS-1$
        public static final String CHARACTER_SET_NAME = "CHARACTERSETNAME"; //$NON-NLS-1$
        public static final String CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH"; //$NON-NLS-1$
        public static final String COLLATION_NAME = "COLLATIONNAME"; //$NON-NLS-1$
        public static final String CURRENCY = "CURRENCY"; //$NON-NLS-1$
        public static final String FORMAT = "FORMAT"; //$NON-NLS-1$
        public static final String FIXED_LENGTH = "FIXED_LENGTH"; //$NON-NLS-1$
        public static final String MAX_VALUE = "MAX_VALUE"; //$NON-NLS-1$
        public static final String MIN_VALUE = "MIN_VALUE"; //$NON-NLS-1$
        public static final String RADIX = "RADIX"; //$NON-NLS-1$
        public static final String SIGNED = "SIGNED"; //$NON-NLS-1$
        public static final String SEARCHABLE = "SEARCHABLE"; //$NON-NLS-1$
        public static final String SELECTABLE = "SELECTABLE"; //$NON-NLS-1$
        public static final String UPDATABLE = "UPDATABLE"; //$NON-NLS-1$

        public static String[] toArray() {
            return new String[] { NULL_VALUE_COUNT, DATATYPE, NATIVE_TYPE,
                            CASE_SENSITIVE, CHARACTER_SET_NAME, CHAR_OCTET_LENGTH,
                            COLLATION_NAME, CURRENCY, FORMAT,
                            FIXED_LENGTH, MAX_VALUE, MIN_VALUE,
                            RADIX, SIGNED, SEARCHABLE, SELECTABLE, UPDATABLE };
        }
    }

    class COLUMN_DDL_PROPERTY_KEYS {

        public static final String NULLABLE = "NULLABLE"; //$NON-NLS-1$
        public static final String AUTO_INCREMENTED = "AUTOINCREMENTED"; //$NON-NLS-1$
        public static final String DATATYPE_NAME = "DATATYPE_NAME"; //$NON-NLS-1$
        public static final String LENGTH = "LENGTH"; //$NON-NLS-1$
        public static final String PRECISION = "PRECISION"; //$NON-NLS-1$
        public static final String SCALE = "SCALE"; //$NON-NLS-1$
        public static final String DEFAULT_VALUE = "DEFAULTVALUE"; //$NON-NLS-1$

        public static String[] toArray() {
            return new String[] { DATATYPE_NAME, NULLABLE, AUTO_INCREMENTED, DEFAULT_VALUE,
                            LENGTH, PRECISION, SCALE };
        }
    }

    class COLUMN_DEFAULT {

        public static final int LENGTH = 0;
        public static final int PRECISION = 0;
        public static final int NUMERIC_PRECISION = 1;
        public static final int RADIX = 0;
        public static final int SCALE = 0;
        public static final int DISTINCT_VALUE_COUNT = -1;
        public static final int NULL_VALUE_COUNT = -1;
        public static final String DATATYPE = null;
        public static final String DATATYPE_NAME = null;
        public static final String DEFAULT_VALUE = null;
        public static final String NATIVE_TYPE = null;
        public static final String NULLABLE = NULLABLE_OPTIONS.NULL;
        public static final boolean AUTO_INCREMENTED = false;
        public static final boolean CASE_SENSITIVE = true;
        public static final String CHARACTER_SET_NAME = null;
        public static final int CHAR_OCTET_LENGTH = 0;
        public static final String COLLATION_NAME = null;
        public static final boolean CURRENCY = false;
        public static final String VALUE = null;
        public static final String FORMAT = null;
        public static final boolean FIXED_LENGTH = false;
        public static final String MAXIMUM_VALUE = null;
        public static final String MINIMUM_VALUE = null;
        public static final boolean SIGNED = true;
        public static final String SEARCHABILITY = SEARCHABILITY_OPTIONS.SEARCHABLE;
        public static final boolean SELECTABLE = true;
        public static final boolean UPDATABLE = true;

        public static Map< String, Object > toMap() {
            final Map< String, Object > defaultMap = COMMON_DEFAULT.toMap();
            defaultMap.put( COLUMN_DDL_PROPERTY_KEYS.NULLABLE, NULLABLE );
            defaultMap.put( COLUMN_DDL_PROPERTY_KEYS.AUTO_INCREMENTED, AUTO_INCREMENTED );
            defaultMap.put( COLUMN_DDL_PROPERTY_KEYS.DATATYPE_NAME, DATATYPE_NAME );
            defaultMap.put( COLUMN_DDL_PROPERTY_KEYS.LENGTH, LENGTH );
            defaultMap.put( COLUMN_DDL_PROPERTY_KEYS.PRECISION, PRECISION );
            defaultMap.put( COLUMN_DDL_PROPERTY_KEYS.SCALE, SCALE );
            defaultMap.put( COLUMN_DDL_PROPERTY_KEYS.DEFAULT_VALUE, DEFAULT_VALUE );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.NULL_VALUE_COUNT, NULL_VALUE_COUNT );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.DATATYPE, DATATYPE );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.NATIVE_TYPE, NATIVE_TYPE );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.CASE_SENSITIVE, CASE_SENSITIVE );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.CHARACTER_SET_NAME, CHARACTER_SET_NAME );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.CHAR_OCTET_LENGTH, CHAR_OCTET_LENGTH );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.COLLATION_NAME, COLLATION_NAME );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.CURRENCY, CURRENCY );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.FORMAT, FORMAT );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.FIXED_LENGTH, FIXED_LENGTH );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.MAX_VALUE, MAXIMUM_VALUE );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.MIN_VALUE, MINIMUM_VALUE );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.SIGNED, SIGNED );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.RADIX, RADIX );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.SEARCHABLE, SEARCHABILITY );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.SELECTABLE, SELECTABLE );
            defaultMap.put( COLUMN_DDL_OPTION_KEYS.UPDATABLE, UPDATABLE );
            return defaultMap;
        }
    }

    class COMMON_DDL_OPTION_KEYS {

        public static final String NAME_IN_SOURCE = "NAMEINSOURCE"; //$NON-NLS-1$
        public static final String DESCRIPTION = "ANNOTATION"; //$NON-NLS-1$

        public static String[] toArray() {
            return new String[] { NAME_IN_SOURCE, DESCRIPTION };
        }
    }

    class COMMON_DDL_PROPERTY_KEYS {

        public static final String NAME = "NAME"; //$NON-NLS-1$

        public static String[] toArray() {
            return new String[] { NAME };
        }
    }

    class COMMON_DEFAULT {

        public static final String NAME = null;
        public static final String NAME_IN_SOURCE = null;
        public static final String DESCRIPTION = null;

        public static Map< String, Object > toMap() {
            final Map< String, Object > defaultMap = new HashMap< String, Object >();
            defaultMap.put( COMMON_DDL_PROPERTY_KEYS.NAME, NAME );
            defaultMap.put( COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, NAME_IN_SOURCE );
            defaultMap.put( COMMON_DDL_OPTION_KEYS.DESCRIPTION, DESCRIPTION );
            return defaultMap;
        }
    }

    class DETERMINISM_OPTIONS {

        public static final String NONDETERMINISTIC = "NONDETERMINISTIC";
        public static final String COMMAND_DETERMINISTIC = "COMMAND_DETERMINISTIC";
        public static final String SESSION_DETERMINISTIC = "SESSION_DETERMINISTIC";
        public static final String USER_DETERMINISTIC = "USER_DETERMINISTIC";
        public static final String VDB_DETERMINISTIC = "VDB_DETERMINISTIC";
        public static final String DETERMINISTIC = "DETERMINISTIC";
    }

    // IN, INOUT, OUT, VARIADIC
    class DIRECTION_OPTIONS {

        public static final String IN = "IN"; //$NON-NLS-1$
        public static final String INOUT = "INOUT"; //$NON-NLS-1$
        public static final String OUT = "OUT"; //$NON-NLS-1$
        public static final String VARIADIC = "VARIADIC"; //$NON-NLS-1$
        public static final String[] AS_ARRAY = { IN, INOUT, OUT, VARIADIC };
        public static final String DEFAULT_VALUE = IN;
    }

    class FOREIGN_KEY_DEFAULT {

        public static final String FOREIGN_KEY_MULTIPLICITY = MULTIPLICITY_OPTIONS.ZERO_TO_MANY;
        public static final String PRIMARY_KEY_MULTIPLICITY = MULTIPLICITY_OPTIONS.ONE;
        public static final String UNIQUE_KEY_NAME = null;
        public static final String UNIQUE_KEY_TABLE_NAME = null;
        public static final boolean ALLOW_JOIN = true;

        public static Map< String, Object > toMap() {
            final Map< String, Object > defaultMap = COMMON_DEFAULT.toMap();
            defaultMap.put( FOREIGN_KEY_PROP_KEYS.FOREIGN_KEY_MULTIPLICITY, FOREIGN_KEY_MULTIPLICITY );
            defaultMap.put( FOREIGN_KEY_PROP_KEYS.PRIMARY_KEY_MULTIPLICITY, PRIMARY_KEY_MULTIPLICITY );
            defaultMap.put( FOREIGN_KEY_PROP_KEYS.UNIQUE_KEY_NAME, UNIQUE_KEY_NAME );
            defaultMap.put( FOREIGN_KEY_PROP_KEYS.UNIQUE_KEY_TABLE_NAME, UNIQUE_KEY_TABLE_NAME );
            return defaultMap;
        }
    }

    class FOREIGN_KEY_EXT_PROPERTIES {

        public static final String ALLOW_JOIN = "relational:allow-join"; //$NON-NLS-1$
    }

    class FOREIGN_KEY_PROP_KEYS {

        public static final String FOREIGN_KEY_MULTIPLICITY = "FKMULTIPLICITY"; //$NON-NLS-1$
        public static final String PRIMARY_KEY_MULTIPLICITY = "PKMULTIPLICITY"; //$NON-NLS-1$
        public static final String UNIQUE_KEY_NAME = "UNIQUEKEYNAME"; //$NON-NLS-1$
        public static final String UNIQUE_KEY_TABLE_NAME = "UNIQUEKEYTABLENAME"; //$NON-NLS-1$

        public static String[] toArray() {
            return new String[] { FOREIGN_KEY_MULTIPLICITY, PRIMARY_KEY_MULTIPLICITY, UNIQUE_KEY_NAME, UNIQUE_KEY_TABLE_NAME };
        }
    }

    class INDEX_DEFAULT {

        public static final boolean AUTO_UPDATE = false;
        public static final String FILTER_CONDITION = null;
        public static final boolean NULLABLE = false;
        public static final boolean UNIQUE = false;

        public static Map< String, Object > toMap() {
            final Map< String, Object > defaultMap = COMMON_DEFAULT.toMap();
            defaultMap.put( INDEX_PROP_KEYS.AUTO_UPDATE, AUTO_UPDATE );
            defaultMap.put( INDEX_PROP_KEYS.FILTER_CONDITION, FILTER_CONDITION );
            defaultMap.put( INDEX_PROP_KEYS.NULLABLE, NULLABLE );
            defaultMap.put( INDEX_PROP_KEYS.UNIQUE, UNIQUE );
            return defaultMap;
        }
    }

    class INDEX_PROP_KEYS {

        public static final String AUTO_UPDATE = "AUTOUPDATE"; //$NON-NLS-1$
        public static final String FILTER_CONDITION = "FILTERCONDITION"; //$NON-NLS-1$
        public static final String NULLABLE = "NULLABLE"; //$NON-NLS-1$
        public static final String UNIQUE = "UNIQUE"; //$NON-NLS-1$

        public static String[] toArray() {
            return new String[] { AUTO_UPDATE, FILTER_CONDITION, NULLABLE, UNIQUE };
        }
    }

    // MANY, ONE, UNSPECIFICIED, ZERO_TO_MANY, ZERO_TO_ONE
    class MULTIPLICITY_OPTIONS {

        public static final String MANY = "MANY"; //$NON-NLS-1$
        public static final String ONE = "ONE"; //$NON-NLS-1$
        public static final String UNSPECIFICIED = "UNSPECIFICIED"; //$NON-NLS-1$
        public static final String ZERO_TO_MANY = "ZERO_TO_MANY"; //$NON-NLS-1$
        public static final String ZERO_TO_ONE = "ZERO_TO_ONE"; //$NON-NLS-1$
        public static final String[] AS_ARRAY = { MANY, ONE, UNSPECIFICIED, ZERO_TO_MANY, ZERO_TO_ONE };
    }

    // NO_NULLS, NULLABLE, NULLABLE_UNKNOWN
    class NULLABLE_OPTIONS {

        public static final String NOT_NULL = "NOT NULL"; //$NON-NLS-1$
        public static final String NULL = "NULL"; //$NON-NLS-1$
        public static final String[] AS_ARRAY = { NOT_NULL, NULL };
        public static final String DEFAULT_VALUE = NULL;
    }

    class PARAMETER_DDL_OPTION_KEYS {

        public static final String NATIVE_TYPE = "NATIVE_TYPE"; //$NON-NLS-1$

        public static String[] toArray() {
            return new String[] { NATIVE_TYPE };
        }
    }

    class PARAMETER_DDL_PROPERTY_KEYS {

        public static final String DIRECTION = "DIRECTION"; //$NON-NLS-1$
        public static final String DATATYPE_NAME = "DATATYPE_NAME"; //$NON-NLS-1$
        public static final String NULLABLE = "NULLABLE"; //$NON-NLS-1$
        public static final String DEFAULT_VALUE = "DEFAULTVALUE"; //$NON-NLS-1$
        public static final String LENGTH = "LENGTH"; //$NON-NLS-1$
        public static final String PRECISION = "PRECISION"; //$NON-NLS-1$
        public static final String SCALE = "SCALE"; //$NON-NLS-1$

        public static String[] toArray() {
            return new String[] { DIRECTION, DATATYPE_NAME, NULLABLE, DEFAULT_VALUE, LENGTH, PRECISION, SCALE };
        }
    }

    class PARAMETER_DEFAULT {

        public static final int SCALE = 0;
        public static final int LENGTH = 0;
        public static final int PRECISION = 0;
        public static final String NATIVE_TYPE = null;
        public static final String DATATYPE_NAME = null;
        public static final String NULLABLE = NULLABLE_OPTIONS.NULL;
        public static final String DIRECTION = DIRECTION_OPTIONS.IN;
        public static final String DEFAULT_VALUE = null;
        public static final int STRING_LENGTH = 4000;

        public static Map< String, Object > toMap() {
            final Map< String, Object > defaultMap = COMMON_DEFAULT.toMap();
            defaultMap.put( PARAMETER_DDL_PROPERTY_KEYS.SCALE, SCALE );
            defaultMap.put( PARAMETER_DDL_PROPERTY_KEYS.LENGTH, LENGTH );
            defaultMap.put( PARAMETER_DDL_PROPERTY_KEYS.PRECISION, PRECISION );
            defaultMap.put( PARAMETER_DDL_PROPERTY_KEYS.NULLABLE, NULLABLE );
            defaultMap.put( PARAMETER_DDL_PROPERTY_KEYS.DIRECTION, DIRECTION );
            defaultMap.put( PARAMETER_DDL_PROPERTY_KEYS.DEFAULT_VALUE, DEFAULT_VALUE );
            defaultMap.put( PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME, DATATYPE_NAME );
            defaultMap.put( PARAMETER_DDL_OPTION_KEYS.NATIVE_TYPE, NATIVE_TYPE );
            return defaultMap;
        }
    }

    class PROCEDURE_DDL_OPTION_KEYS {

        public static final String UPDATECOUNT = "UPDATECOUNT";
        public static final String CATEGORY = "CATEGORY";
        public static final String AGGREGATE = "AGGREGATE";
        public static final String ALLOWS_DISTINCT = "ALLOWS-DISTINCT";
        public static final String ALLOWS_ORDERBY = "ALLOWS-ORDERBY";
        public static final String ANALYTIC = "ANALYTIC";
        public static final String DECOMPOSABLE = "DECOMPOSABLE";
        public static final String NON_PREPARED = "NON_PREPARED";
        public static final String NULL_ON_NULL = "NULL-ON-NULL";
        public static final String USES_DISTINCT_ROWS = "USES-DISTINCT-ROWS";
        public static final String VARARGS = "VARARGS";
        public static final String DETERMINISM = "DETERMINISM";
        public static final String NATIVE_QUERY = "NATIVE_QUERY";
        public static final String JAVA_CLASS = "JAVA_CLASS";
        public static final String JAVA_METHOD = "JAVA_METHOD";

        public static String[] toArray() {
            return new String[] { UPDATECOUNT, CATEGORY, AGGREGATE, ALLOWS_DISTINCT, ALLOWS_ORDERBY,
                            ANALYTIC, DECOMPOSABLE, NON_PREPARED, NULL_ON_NULL, USES_DISTINCT_ROWS,
                            VARARGS, DETERMINISM, NATIVE_QUERY, JAVA_CLASS, JAVA_METHOD };
        }
    }

    class PROCEDURE_DDL_PROPERTY_KEYS {

        // public static final String FUNCTION = "FUNCTION";

        public static String[] toArray() {
            return new String[] {};
        }
    }

    class PROCEDURE_DEFAULT {

        public static final boolean FUNCTION = false;
        public static final boolean NON_PREPARED = false;
        public static final String NATIVE_QUERY = null;
        public static final String UPDATECOUNT = "AUTO"; //$NON-NLS-1$
        public static final String DATATYPE = "string"; //$NON-NLS-1$
        public static final String CATEGORY = null;
        public static final String DETERMINISM = DETERMINISM_OPTIONS.NONDETERMINISTIC;
        public static final boolean NULL_ON_NULL = false;
        public static final String JAVA_CLASS = null;
        public static final String JAVA_METHOD = null;
        public static final boolean VARARGS = false;
        public static final boolean AGGREGATE = false;
        public static final boolean ANALYTIC = false;
        public static final boolean ALLOWS_ORDERBY = false;
        public static final boolean ALLOWS_DISTINCT = false;
        public static final boolean DECOMPOSABLE = false;
        public static final boolean USES_DISTINCT_ROWS = false;

        public static Map< String, Object > toMap() {
            final Map< String, Object > defaultMap = COMMON_DEFAULT.toMap();
            // defaultMap.put(PROCEDURE_DDL_PROPERTY_KEYS.FUNCTION, FUNCTION);
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.NON_PREPARED, NON_PREPARED );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.NATIVE_QUERY, NATIVE_QUERY );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.UPDATECOUNT, UPDATECOUNT );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.CATEGORY, CATEGORY );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.DETERMINISM, DETERMINISM );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.NULL_ON_NULL, NULL_ON_NULL );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.JAVA_CLASS, JAVA_CLASS );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.JAVA_METHOD, JAVA_METHOD );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.VARARGS, VARARGS );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.AGGREGATE, AGGREGATE );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.ANALYTIC, ANALYTIC );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.ALLOWS_ORDERBY, ALLOWS_ORDERBY );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.ALLOWS_DISTINCT, ALLOWS_DISTINCT );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.DECOMPOSABLE, DECOMPOSABLE );
            defaultMap.put( PROCEDURE_DDL_OPTION_KEYS.USES_DISTINCT_ROWS, USES_DISTINCT_ROWS );
            return defaultMap;
        }
    }

    class PROCEDURE_EXT_PROPERTIES {

        public static final String AGGREGATE = "relational:aggregate"; //$NON-NLS-1$
        public static final String ALLOWS_ORDER_BY = "relational:allows-orderby"; //$NON-NLS-1$
        public static final String ALLOWS_DISTINCT = "relational:allows-distinct"; //$NON-NLS-1$
        public static final String ANALYTIC = "relational:analytic"; //$NON-NLS-1$
        public static final String DECOMPOSABLE = "relational:decomposable"; //$NON-NLS-1$
        public static final String DETERMINISTIC = "relational:deterministic"; //$NON-NLS-1$
        public static final String NATIVE_QUERY = "relational:native-query"; //$NON-NLS-1$
        public static final String NON_PREPARED = "relational:non-prepared"; //$NON-NLS-1$
        public static final String USES_DISTINCT_ROWS = "relational:uses-distinct-rows"; //$NON-NLS-1$
        public static final String VARARGS = "relational:varargs"; //$NON-NLS-1$
        public static final String NULL_ON_NULL = "relational:null-on-null"; //$NON-NLS-1$
        public static final String JAVA_CLASS = "relational:java-class"; //$NON-NLS-1$
        public static final String JAVA_METHOD = "relational:java-method"; //$NON-NLS-1$
        public static final String UDF_JAR_PATH = "relational:udfJarPath"; //$NON-NLS-1$
        public static final String FUNCTION_CATEGORY = "relational:function-category"; //$NON-NLS-1$
    }

    // ALL_EXCEPT_LIKE, LIKE_ONLY, SEARCHABLE, UNSEARCHABLE

    class SEARCHABILITY_OPTIONS {

        public static final String ALL_EXCEPT_LIKE = "ALL_EXCEPT_LIKE"; //$NON-NLS-1$
        public static final String LIKE_ONLY = "LIKE_ONLY"; //$NON-NLS-1$
        public static final String SEARCHABLE = "SEARCHABLE"; //$NON-NLS-1$
        public static final String UNSEARCHABLE = "UNSEARCHABLE"; //$NON-NLS-1$
        public static final String[] AS_ARRAY = { ALL_EXCEPT_LIKE, LIKE_ONLY, SEARCHABLE, UNSEARCHABLE };
    }

    class TABLE_DDL_OPTION_KEYS {

        public static final String CARDINALITY = "CARDINALITY"; //$NON-NLS-1$
        public static final String MATERIALIZED = "MATERIALIZED"; //$NON-NLS-1$
        public static final String MATERIALIZED_TABLE = "MATERIALIZEDTABLE"; //$NON-NLS-1$
        public static final String UPDATABLE = "UPDATABLE"; //$NON-NLS-1$

        //public static final String SYSTEM = "SYSTEM"; //$NON-NLS-1$

        public static String[] toArray() {
            return new String[] { CARDINALITY, MATERIALIZED, MATERIALIZED_TABLE, UPDATABLE };
        }
    }

    class TABLE_DEFAULT {

        public static final int CARDINALITY = -1;
        public static final boolean MATERIALIZED = false;
        public static final String MATERIALIZED_TABLE = null;
        public static final boolean UPDATABLE = true;

        public static Map< String, Object > toMap() {
            final Map< String, Object > defaultMap = COMMON_DEFAULT.toMap();
            defaultMap.put( TABLE_DDL_OPTION_KEYS.CARDINALITY, CARDINALITY );
            defaultMap.put( TABLE_DDL_OPTION_KEYS.MATERIALIZED, MATERIALIZED );
            defaultMap.put( TABLE_DDL_OPTION_KEYS.MATERIALIZED_TABLE, MATERIALIZED_TABLE );
            defaultMap.put( TABLE_DDL_OPTION_KEYS.UPDATABLE, UPDATABLE );
            return defaultMap;
        }
    }

    public enum Type {
        MODEL( "MODEL" ), //$NON-NLS-1$
        SCHEMA( "SCHEMA" ), //$NON-NLS-1$
        CATALOG( "CATALOG" ), //$NON-NLS-1$
        TABLE( "TABLE" ), //$NON-NLS-1$
        VIEW( "VIEW" ), //$NON-NLS-1$
        PROCEDURE( "PROCEDURE" ), //$NON-NLS-1$
        PARAMETER( "PARAMETER" ), //$NON-NLS-1$
        COLUMN( "COLUMN" ), //$NON-NLS-1$
        PRIMARY_KEY( "PRIMARY_KEY" ), //$NON-NLS-1$
        FOREIGN_KEY( "FOREIGN_KEY" ), //$NON-NLS-1$
        UNIQUE_CONSTRAINT( "UNIQUE_CONSTRAINT" ), //$NON-NLS-1$
        ACCESS_PATTERN( "ACCESS_PATTERN" ), //$NON-NLS-1$
        RESULT_SET( "RESULT_SET" ), //$NON-NLS-1$
        INDEX( "INDEX" ); //$NON-NLS-1$

        private final String text;

        Type( final String text ) {
            this.text = text;
        }

        /**
         * @param name
         *        the name
         * @return 'true' if equal, 'false' if not.
         */
        public boolean equalsIgnoreCase( final String name ) {
            return text.equalsIgnoreCase( name );
        }

        public String text() {
            return text;
        }

        /**
         * @return the lowercase string
         */
        public String toLowerCase() {
            return text.toLowerCase();
        }
    }

    /**
     * relational object type literal strings
     */
    class TYPES_LITERAL {

        public static final String UNDEFINED = "UNDEFINED"; //$NON-NLS-1$
        public static final String MODEL = "MODEL"; //$NON-NLS-1$
        public static final String SCHEMA = "SCHEMA"; //$NON-NLS-1$
        public static final String CATALOG = "CATALOG"; //$NON-NLS-1$
        public static final String TABLE = "TABLE"; //$NON-NLS-1$
        public static final String VIEW = "VIEW"; //$NON-NLS-1$
        public static final String PROCEDURE = "PROCEDURE"; //$NON-NLS-1$
        public static final String PARAMETER = "PARAMETER"; //$NON-NLS-1$
        public static final String COLUMN = "COLUMN"; //$NON-NLS-1$
        public static final String PRIMARYKEY = "PRIMARY-KEY"; //$NON-NLS-1$
        public static final String FOREIGNKEY = "FOREIGN-KEY"; //$NON-NLS-1$
        public static final String UNIQUECONSTRAINT = "UNIQUE-CONSTRAINT"; //$NON-NLS-1$
        public static final String ACCESSPATTERN = "ACCESS-PATTERN"; //$NON-NLS-1$
        public static final String RESULTSET = "RESULT-SET"; //$NON-NLS-1$
        public static final String INDEX = "INDEX"; //$NON-NLS-1$
    }

    // AUTO, MULTIPLE, ONE, ZERO
    class UPDATE_COUNT_OPTIONS {

        public static final String AUTO = "AUTO"; //$NON-NLS-1$
        public static final String MULTIPLE = "MULTIPLE"; //$NON-NLS-1$
        public static final String ONE = "ONE"; //$NON-NLS-1$
        public static final String ZERO = "ZERO"; //$NON-NLS-1$
        public static final String[] AS_ARRAY = { AUTO, MULTIPLE, ONE, ZERO };
        public static final String DEFAULT_VALUE = AUTO;
    }

}