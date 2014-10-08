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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.modelspace.Descriptor;
import org.modelspace.Model;
import org.modelspace.ModelObject;
import org.modelspace.ModelProperty;
import org.modelspace.ddl.TeiidDdlIntegrationTest;
import org.modelspace.ddl.relational.RelationalConstants.DETERMINISM_OPTIONS;
import org.modelspace.ddl.relational.RelationalConstants.NULLABLE_OPTIONS;
import org.modelspace.ddl.relational.RelationalConstants.SEARCHABILITY_OPTIONS;
import org.modelspace.ddl.relational.RelationalConstants.UPDATE_COUNT_OPTIONS;

@SuppressWarnings( "javadoc" )
public class ITTeiidDdlRelational extends TeiidDdlIntegrationTest {

    private static final String TEIID_MYSQL_ACCTS = "src/test/resources/Teiid-MySQLAccounts.ddl";
    private static final String TEIID_MYSQL_BQT = "src/test/resources/Teiid-MySQLBQT.ddl";
    private static final String TEIID_FLATFILE = "src/test/resources/Teiid-FlatFile.ddl";
    private static final String TEIID_SAP_GATEWAY_SMALL = "src/test/resources/Teiid-SAPGatewaySmall.ddl";
    private static final String TEIID_SAP_GATEWAY_MEDIUM = "src/test/resources/Teiid-SAPGatewayMedium.ddl";
    private static final String TEIID_SAP_GATEWAY_FULL = "src/test/resources/Teiid-SAPGateway.ddl";

    private Model getModeshapeModel( final File ddlFile ) throws Exception {
        metamodelManager().install( org.modelspace.ddl.TeiidDdlLexicon.DDL_METAMODEL_CATEGORY );

        final Model generatedModel =
            modelspace().importModel( ddlFile, null, metamodelManager().metamodel( org.modelspace.ddl.TeiidDdlLexicon.DDL_METAMODEL_ID ) );
        final Model model = modelspace().model( ddlFile.getName() );
        assertThat( model, is( generatedModel ) );

        return model;
    }

    private RelationalModel getRelationalModel( final File ddlFile ) throws Exception {
        final Model mmModel = getModeshapeModel( ddlFile );

        // get the relational model
        final IModelDelegate mmModelDelegate = new MMModelDelegate( mmModel );
        final RelationalModel relModel = new RelationalModel( mmModelDelegate );

        return relModel;
    }

    /**
     * Recursively prints model object and its children
     * 
     * @param level
     *        the current model level for indenting
     * @param modelObj
     *        the model object
     * @throws Exception
     *         the exception
     */
    private void printModelObject( int level,
                                   final ModelObject modelObj ) throws Exception {
        // Calculate indent
        final StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < level; i++ ) {
            sb.append( "    " );
        }
        System.out.println( sb.toString() + "ModelObject= " + modelObj.name() + " =====" );
        final Descriptor[] mixinTypes = modelObj.mixinTypes();
        for ( final Descriptor type : mixinTypes ) {
            System.out.println( sb.toString() + "    Type : " + type.name() );
        }
        if ( modelObj.hasProperties() ) {
            for ( final String prop : modelObj.propertyNames() ) {
                final ModelProperty modelProperty = modelObj.property( prop );
                if ( modelProperty.descriptor().multiple() ) {
                    System.out.println( sb.toString() + "    Property= " + prop + ", **Multi-Valued** " );
                    final Object[] values = modelProperty.values();
                    for ( final Object val : values ) {
                        System.out.println( sb.toString() + "      Value=" + val.toString() );
                    }
                } else {
                    final Object value = modelProperty.value();
                    System.out.println( sb.toString() + "    Property= " + prop + ",  Value= " + value.toString() );
                }
            }
            try {
                final ModelProperty expression = modelObj.property( "ddl:expression" );
                System.out.println( sb.toString() + "    Expression= " + expression.stringValue() );
            } catch ( final Exception e ) {}
        }
        if ( modelObj.hasChildren() ) {
            level++;
            for ( final ModelObject mo : modelObj.children() ) {
                printModelObject( level, mo );
            }

        }
    }

    /**
     * Recursively prints relational object and its children
     * 
     * @param level
     *        the current relational level for indenting
     * @param relObj
     *        the relational object
     * @throws Exception
     *         the exception
     */
    private void printRelationalObject( int level,
                                        final RelationalObject relObj ) throws Exception {
        // Calculate indent
        final StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < level; i++ ) {
            sb.append( "    " );
        }
        System.out.println( sb.toString() + "RelationalObject= " + relObj.getName() );
        System.out.println( sb.toString() + "    Type : " + relObj.getType() );
        System.out.println( sb.toString() + "    Name in Source : " + relObj.getNameInSource() );
        System.out.println( sb.toString() + "    Description : " + relObj.getDescription() );
        if ( relObj.getType() == RelationalConstants.Type.TABLE ) {
            System.out.println( sb.toString() + "    Cardinality : " + ( ( Table ) relObj ).getCardinality() );
            System.out.println( sb.toString() + "    isMaterialized : " + ( ( Table ) relObj ).isMaterialized() );
            System.out.println( sb.toString() + "    MaterializedTable : " + ( ( Table ) relObj ).getMaterializedTable() );
            System.out.println( sb.toString() + "    Supports Update : " + ( ( Table ) relObj ).supportsUpdate() );
        } else if ( relObj.getType() == RelationalConstants.Type.COLUMN ) {
            System.out.println( sb.toString() + "    Default value : " + ( ( Column ) relObj ).getDefaultValue() );
            System.out.println( sb.toString() + "    Length : " + ( ( Column ) relObj ).getLength() );
            System.out.println( sb.toString() + "    Datatype name : " + ( ( Column ) relObj ).getDatatypeName() );
            System.out.println( sb.toString() + "    Native type : " + ( ( Column ) relObj ).getNativeType() );
            System.out.println( sb.toString() + "    is nullable : " + ( ( Column ) relObj ).getNullable() );
            System.out.println( sb.toString() + "    is updatable : " + ( ( Column ) relObj ).isUpdateable() );
            System.out.println( sb.toString() + "    is selectable : " + ( ( Column ) relObj ).isSelectable() );
        }
        final Map< String, String > extProps = relObj.getExtensionProperties();
        System.out.println( sb.toString() + "    --- Extension props --- " );
        for ( final String key : extProps.keySet() ) {
            System.out.println( sb.toString() + "    key: " + key + "  value: " + extProps.get( key ) );
        }

        // if( modelObj.hasProperties() ) {
        // for( String prop : modelObj.propertyNames() ) {
        // System.out.println(sb.toString()+"    Property= " + prop.toString());
        // }
        // try {
        // String expression = (String) modelObj.value("ddl:expression");
        // System.out.println(sb.toString()+"    Expression= " + expression);
        // } catch (Exception e) {
        // }
        // }
        if ( relObj.hasChildren() ) {
            level++;
            for ( final RelationalObject mo : relObj.getChildren() ) {
                printRelationalObject( level, mo );
            }

        }
    }

    /**
     * Test import of Teiid-MySQLBQT.ddl Expected outcome - successful creation
     * 
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldImport_FlatFile() throws Exception {
        final File ddlFile = new File( TEIID_FLATFILE );
        final RelationalModel relationalModel = getRelationalModel( ddlFile );

        // Get Model name
        // String modelName = relationalModel.getName();

        // ---------------------------------------
        // Test expected procedures for the model
        // ---------------------------------------
        final String[] procNames = new String[] { "getFiles", "getTextFiles", "saveFile" };
        final boolean hasProcs = RelationalTestUtil.childrenMatch( relationalModel, procNames, RelationalConstants.Type.PROCEDURE );
        if ( !hasProcs ) {
            Assert.fail( "expected Procs do not match" ); //$NON-NLS-1$
        }

        // =============================================================================================================
        // getFiles procedure
        //
        // CREATE FOREIGN PROCEDURE getFiles(IN pathAndPattern string OPTIONS (ANNOTATION 'The path and pattern of what files to
        // return. Currently the only pattern supported is *.<ext>, which returns only the files matching the given extension at the
        // given path.')) RETURNS TABLE (file blob, filePath string)
        // OPTIONS (ANNOTATION 'Returns files that match the given path and pattern as BLOBs')
        // =============================================================================================================
        // ----------------------------------------
        // Test expected params
        // ----------------------------------------
        Procedure proc = relationalModel.getProcedure( "getFiles" );
        String[] paramNames = new String[] { "pathAndPattern" };
        boolean hasParams = RelationalTestUtil.childrenMatch( proc, paramNames, RelationalConstants.Type.PARAMETER );
        if ( !hasParams ) {
            Assert.fail( "expected parameters do not match" ); //$NON-NLS-1$
        }
        // ----------------------------------------
        // Test expected resultSet
        // ----------------------------------------
        final String[] rsNames = new String[] { "resultSet" };
        final boolean hasRS = RelationalTestUtil.childrenMatch( proc, rsNames, RelationalConstants.Type.RESULT_SET );
        if ( !hasRS ) {
            Assert.fail( "expected parameters do not match" ); //$NON-NLS-1$
        }
        // --------------------------------------------
        // Test expected properties for getFiles proc
        // --------------------------------------------
        Map< String, Object > expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.PROCEDURE_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "getFiles" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, "Returns files that match the given path and pattern as BLOBs" ); //$NON-NLS-1$ 

        // Compare object properties to expected
        String result = RelationalTestUtil.compareProperties( proc, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }
        // -----------------------------------------------------
        // Test expected properties for pathAndPattern parameter
        // -----------------------------------------------------
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.PARAMETER_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "pathAndPattern" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, "The path and pattern of what files to return.  Currently the only pattern supported is *.<ext>, which returns only the files matching the given extension at the given path." ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME, "STRING" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DIRECTION, "IN" ); //$NON-NLS-1$ 

        Parameter pParam = proc.getParameter( "pathAndPattern" );
        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( pParam, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }
        // -----------------------------------------------------
        // Test ResultSet columns
        // -----------------------------------------------------
        ProcedureResultSet rSet = proc.getProcedureResultSet();
        final String[] colNames = new String[] { "file", "filePath" };
        final boolean hasCols = RelationalTestUtil.childrenMatch( rSet, colNames, RelationalConstants.Type.COLUMN );
        if ( !hasCols ) {
            Assert.fail( "expected columns do not match" ); //$NON-NLS-1$
        }

        // -----------------------------------------------------
        // Test expected properties for resultSet column 'file'
        // -----------------------------------------------------
        final Column fileCol = rSet.getColumn( "file" );
        // Expected properties for file column
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.COLUMN_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "file" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME, "BLOB" ); //$NON-NLS-1$ 

        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( fileCol, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // ---------------------------------------------------------
        // Test expected properties for resultSet column 'filePath'
        // ---------------------------------------------------------
        final Column filePathCol = rSet.getColumn( "filePath" );
        // Expected properties for file column
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.COLUMN_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "filePath" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME, "STRING" ); //$NON-NLS-1$ 

        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( filePathCol, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // =============================================================================================================
        // getFiles procedure
        //
        // CREATE FOREIGN PROCEDURE saveFile(IN filePath string, IN file object OPTIONS (ANNOTATION 'The contents to save. Can be
        // one of CLOB, BLOB, or XML'))
        // OPTIONS (ANNOTATION 'Saves the given value to the given path. Any existing file will be overriden.')
        // =============================================================================================================
        // ----------------------------------------
        // Test expected params for saveFile
        // ----------------------------------------
        proc = RelationalTestUtil.getChildProcedure( relationalModel, "saveFile" );
        paramNames = new String[] { "filePath", "file" };
        hasParams = RelationalTestUtil.childrenMatch( proc, paramNames, RelationalConstants.Type.PARAMETER );
        if ( !hasParams ) {
            Assert.fail( "expected parameters do not match" ); //$NON-NLS-1$
        }

        // --------------------------------------------
        // Test expected properties for saveFile proc
        // --------------------------------------------
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.PROCEDURE_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "saveFile" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, "Saves the given value to the given path.  Any existing file will be overriden." ); //$NON-NLS-1$ 

        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( proc, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }
        // -----------------------------------------------------
        // Test expected properties for filePath parameter
        // -----------------------------------------------------
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.PARAMETER_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "filePath" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME, "STRING" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DIRECTION, "IN" ); //$NON-NLS-1$ 

        pParam = proc.getParameter( "filePath" );
        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( pParam, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }
        // -----------------------------------------------------
        // Test expected properties for file parameter
        // -----------------------------------------------------
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.PARAMETER_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "file" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME, "OBJECT" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, "The contents to save.  Can be one of CLOB, BLOB, or XML" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DIRECTION, "IN" ); //$NON-NLS-1$ 

        pParam = proc.getParameter( "file" );
        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( pParam, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }
        // -----------------------------------------------------
        // Test ResultSet - should not be one
        // -----------------------------------------------------
        rSet = proc.getProcedureResultSet();
        if ( rSet != null ) {
            Assert.fail( "expected ResultSet to be null" ); //$NON-NLS-1$
        }

    }

    /**
     * Test import of Teiid-MySQLAccounts.ddl Expected outcome - successful creation
     * 
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldImport_MySQLAccts() throws Exception {
        final File ddlFile = new File( TEIID_MYSQL_ACCTS );
        final RelationalModel relationalModel = getRelationalModel( ddlFile );

        // Get Model name
        // String modelName = relationalModel.getName();

        // ----------------------------------
        // Test expected tables for the model
        // ----------------------------------
        final String[] tableNames =
            new String[] { "accounts.ACCOUNT", "accounts.CUSTOMER", "accounts.HOLDINGS", "accounts.PRODUCT", "accounts.SUBSCRIPTIONS" };
        final boolean hasTables = RelationalTestUtil.childrenMatch( relationalModel, tableNames, RelationalConstants.Type.TABLE );
        if ( !hasTables ) {
            Assert.fail( "expected tables do not match" ); //$NON-NLS-1$
        }

        // ----------------------------------------
        // Test expected columns for ACCOUNT table
        // ----------------------------------------
        Table table = RelationalTestUtil.getChildTable( relationalModel, "accounts.ACCOUNT" );
        String[] colNames = new String[] { "ACCOUNT_ID", "SSN", "STATUS", "TYPE", "DATEOPENED", "DATECLOSED" };
        boolean hasCols = RelationalTestUtil.childrenMatch( table, colNames, RelationalConstants.Type.COLUMN );
        if ( !hasCols ) {
            Assert.fail( "expected columns do not match" ); //$NON-NLS-1$
        }

        // ------------------------------------------
        // Test expected columns for CUSTOMER table
        // ------------------------------------------
        table = RelationalTestUtil.getChildTable( relationalModel, "accounts.CUSTOMER" );
        colNames = new String[] { "SSN", "FIRSTNAME", "LASTNAME", "ST_ADDRESS", "APT_NUMBER", "CITY", "STATE", "ZIPCODE", "PHONE" };
        hasCols = RelationalTestUtil.childrenMatch( table, colNames, RelationalConstants.Type.COLUMN );
        if ( !hasCols ) {
            Assert.fail( "expected columns do not match" ); //$NON-NLS-1$
        }

        // ------------------------------------------
        // Test expected columns for HOLDINGS table
        // ------------------------------------------
        table = RelationalTestUtil.getChildTable( relationalModel, "accounts.HOLDINGS" );
        colNames = new String[] { "TRANSACTION_ID", "ACCOUNT_ID", "PRODUCT_ID", "PURCHASE_DATE", "SHARES_COUNT" };
        hasCols = RelationalTestUtil.childrenMatch( table, colNames, RelationalConstants.Type.COLUMN );
        if ( !hasCols ) {
            Assert.fail( "expected columns do not match" ); //$NON-NLS-1$
        }

        // ------------------------------------------
        // Test expected columns for PRODUCT table
        // ------------------------------------------
        table = RelationalTestUtil.getChildTable( relationalModel, "accounts.PRODUCT" );
        colNames = new String[] { "ID", "SYMBOL", "COMPANY_NAME" };
        hasCols = RelationalTestUtil.childrenMatch( table, colNames, RelationalConstants.Type.COLUMN );
        if ( !hasCols ) {
            Assert.fail( "expected columns do not match" ); //$NON-NLS-1$
        }

        // ------------------------------------------
        // Test expected columns for SUBSCRIPTIONS table
        // ------------------------------------------
        table = RelationalTestUtil.getChildTable( relationalModel, "accounts.SUBSCRIPTIONS" );
        colNames = new String[] { "value", "type", "end_date" };
        hasCols = RelationalTestUtil.childrenMatch( table, colNames, RelationalConstants.Type.COLUMN );
        if ( !hasCols ) {
            Assert.fail( "expected columns do not match" ); //$NON-NLS-1$
        }

        // --------------------------------------------
        // Test expected properties on HOLDINGS table
        // --------------------------------------------
        // Expected properties
        Map< String, Object > expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.TABLE_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "accounts.HOLDINGS" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, null );
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, "`accounts`.`HOLDINGS`" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.TABLE_DDL_OPTION_KEYS.UPDATABLE, "true" ); //$NON-NLS-1$ 

        // Compare object properties to expected
        table = RelationalTestUtil.getChildTable( relationalModel, "accounts.HOLDINGS" );
        String result = RelationalTestUtil.compareProperties( table, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // -------------------------------------------------------------
        // Test expected properties on HOLDINGS.PURCHASE_DATE column
        // -------------------------------------------------------------
        final RelationalObject column = RelationalTestUtil.getChildColumn( table, "PURCHASE_DATE" );
        Assert.assertNotNull( column );

        // Expected properties
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.COLUMN_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "PURCHASE_DATE" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, "`PURCHASE_DATE`" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.NATIVE_TYPE, "TIMESTAMP" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.NULLABLE, "NOT NULL" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DEFAULT_VALUE, "CURRENT_TIMESTAMP" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DATATYPE_NAME, "TIMESTAMP" ); //$NON-NLS-1$ 

        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( column, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$ 
            Assert.fail( result );
        }
    }

    /**
     * Test import of Teiid-MySQLBQT.ddl Expected outcome - successful creation
     * 
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldImport_MySQLBQT() throws Exception {
        final File ddlFile = new File( TEIID_MYSQL_BQT );
        final RelationalModel relationalModel = getRelationalModel( ddlFile );

        // Get Model name
        // String modelName = relationalModel.getName();

        // ----------------------------------
        // Test expected tables for the model
        // ----------------------------------
        final String[] tableNames =
            new String[] { "bqt2.g1", "bqt2.g2", "bqt2.hugea", "bqt2.hugeb", "bqt2.largea", "bqt2.largeb", "bqt2.mediuma", "bqt2.mediumb", "bqt2.plan_table",
                            "bqt2.scalardata", "bqt2.smalla", "bqt2.smallb", "bqt2.splitparttxn1", "bqt2.splitparttxn2", "bqt2.tinya" };
        final boolean hasTables = RelationalTestUtil.childrenMatch( relationalModel, tableNames, RelationalConstants.Type.TABLE );
        if ( !hasTables ) {
            Assert.fail( "expected tables do not match" ); //$NON-NLS-1$
        }

        // ----------------------------------------
        // Test expected columns for bqt2.mediumb
        // ----------------------------------------
        Table table = RelationalTestUtil.getChildTable( relationalModel, "bqt2.mediumb" );
        String[] colNames =
            new String[] { "INTKEY", "STRINGKEY", "INTNUM", "STRINGNUM", "FLOATNUM", "LONGNUM", "DOUBLENUM", "BYTENUM",
                            "DATEVALUE", "TIMEVALUE", "TIMESTAMPVALUE", "BOOLEANVALUE", "CHARVALUE", "SHORTVALUE", "BIGINTEGERVALUE",
                            "BIGDECIMALVALUE", "OBJECTVALUE" };
        boolean hasCols = RelationalTestUtil.childrenMatch( table, colNames, RelationalConstants.Type.COLUMN );
        if ( !hasCols ) {
            Assert.fail( "expected columns do not match" ); //$NON-NLS-1$
        }

        // ------------------------------------------
        // Test expected columns for bqt2.plan_table
        // ------------------------------------------
        table = RelationalTestUtil.getChildTable( relationalModel, "bqt2.plan_table" );
        colNames =
            new String[] {
                            "STATEMENT_ID", "TIMESTAMP", "REMARKS", "OPERATION", "OPTIONS", "OBJECT_NODE", "OBJECT_OWNER",
                            "OBJECT_NAME", "OBJECT_INSTANCE", "OBJECT_TYPE", "OPTIMIZER", "SEARCH_COLUMNS", "ID",
                            "PARENT_ID", "POSITION", "COST", "CARDINALITY", "BYTES", "OTHER_TAG", "PARTITION_START", "PARTITION_STOP",
                            "PARTITION_ID", "OTHER", "DISTRIBUTION", "CPU_COST", "IO_COST", "TEMP_SPACE", "ACCESS_PREDICATES", "FILTER_PREDICATES"
            };
        hasCols = RelationalTestUtil.childrenMatch( table, colNames, RelationalConstants.Type.COLUMN );
        if ( !hasCols ) {
            Assert.fail( "expected columns do not match" ); //$NON-NLS-1$
        }

        // --------------------------------------------
        // Test expected properties on plan_table table
        // --------------------------------------------
        // Expected properties
        Map< String, Object > expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.TABLE_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "bqt2.plan_table" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, null );
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, "\"bqt2\".\"plan_table\"" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.TABLE_DDL_OPTION_KEYS.UPDATABLE, "true" ); //$NON-NLS-1$ 

        // Compare object properties to expected
        table = RelationalTestUtil.getChildTable( relationalModel, "bqt2.plan_table" );
        String result = RelationalTestUtil.compareProperties( table, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // -------------------------------------------------------------
        // Test expected properties on bqt2.plan_table.PURCHASE_DATE column
        // -------------------------------------------------------------
        final RelationalObject column = RelationalTestUtil.getChildColumn( table, "PARTITION_ID" );
        Assert.assertNotNull( column );

        // Expected properties
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.COLUMN_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "PARTITION_ID" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, "\"PARTITION_ID\"" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.NATIVE_TYPE, "DECIMAL" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DATATYPE_NAME, "bigdecimal" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.PRECISION, 38 );
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.SCALE, 2147483647 );

        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( column, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$ 
            Assert.fail( result );
        }
    }

    /**
     * Test import of Teiid-SAPGateway.ddl Expected outcome - successful creation
     * 
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldImport_SAPGateway() throws Exception {
        final File ddlFile = new File( TEIID_SAP_GATEWAY_FULL );
        final RelationalModel relationalModel = getRelationalModel( ddlFile );

        // Get Model name
        // String modelName = relationalModel.getName();

        // ---------------------------------------
        // Test expected tables for the model
        // ---------------------------------------
        final String[] tableNames =
            new String[] { "BookingCollection", "CarrierCollection", "FlightCollection", "NotificationCollection",
                            "SubscriptionCollection", "TravelAgencies", "TravelagencyCollection" };
        final boolean hasTables = RelationalTestUtil.childrenMatch( relationalModel, tableNames, RelationalConstants.Type.TABLE );
        if ( !hasTables ) {
            Assert.fail( "expected Tables do not match" ); //$NON-NLS-1$
        }

        // ---------------------------------------
        // Test expected procedures for the model
        // ---------------------------------------
        final String[] procNames =
            new String[] { "CheckFlightAvailability", "GetAgencyDetails", "GetAvailableFlights", "GetFlightDetails",
                            "UpdateAgencyPhoneNo" };
        final boolean hasProcs = RelationalTestUtil.childrenMatch( relationalModel, procNames, RelationalConstants.Type.PROCEDURE );
        if ( !hasProcs ) {
            Assert.fail( "expected Procedures do not match" ); //$NON-NLS-1$
        }

        // -----------------------------------------------------
        // Test expected params for GetFlightDetails procedure
        // -----------------------------------------------------
        // CREATE FOREIGN PROCEDURE GetFlightDetails(IN airlineid string, IN connectionid string) RETURNS TABLE (countryFrom string,
        // cityFrom string, airportFrom string, countryTo string, cityTo string, airportTo string, flightTime integer, departureTime
        // time, arrivalTime time, distance bigdecimal, distanceUnit string, flightType string, period byte)
        // OPTIONS ("teiid_odata:EntityType" 'RMTSAMPLEFLIGHT.FlightDetails', "teiid_odata:HttpMethod" 'GET')

        final Procedure proc = relationalModel.getProcedure( "GetFlightDetails" );
        final String[] paramNames = new String[] { "airlineid", "connectionid" };
        final boolean hasParams = RelationalTestUtil.childrenMatch( proc, paramNames, RelationalConstants.Type.PARAMETER );
        if ( !hasParams ) {
            Assert.fail( "expected parameters do not match" ); //$NON-NLS-1$
        }

        // ----------------------------------------------------
        // Test expected properties for GetFlightDetails procedure
        // ----------------------------------------------------
        Map< String, Object > expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.PROCEDURE_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "GetFlightDetails" ); //$NON-NLS-1$ 
        expectedProps.put( "teiid_odata:EntityType", "RMTSAMPLEFLIGHT.FlightDetails" ); //$NON-NLS-1$ //$NON-NLS-2$
        expectedProps.put( "teiid_odata:HttpMethod", "GET" ); //$NON-NLS-1$ //$NON-NLS-2$

        // Compare object properties to expected
        String result = RelationalTestUtil.compareProperties( proc, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // --------------------------------------------------------
        // Test expected properties for Proc Parameter 'airlineid'
        // --------------------------------------------------------
        final Parameter param = proc.getParameter( "airlineid" );
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.PARAMETER_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "airlineid" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME, "STRING" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DIRECTION, "IN" ); //$NON-NLS-1$

        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( param, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        final ProcedureResultSet rs = proc.getProcedureResultSet();
        if ( rs == null ) {
            Assert.fail( "result set is null" );
        }

        // Test that all result set columns are as expected
        final String[] colNames =
            new String[] { "countryFrom", "cityFrom", "airportFrom", "countryTo", "cityTo", "airportTo", "flightTime", "departureTime",
                            "arrivalTime", "distance", "distanceUnit", "flightType", "period" };
        final boolean hasCols = RelationalTestUtil.childrenMatch( rs, colNames, RelationalConstants.Type.COLUMN );
        if ( !hasCols ) {
            Assert.fail( "expected result set columns do not match" ); //$NON-NLS-1$
        }

    }

    /**
     * Test import of Teiid-SAPGatewayMedium.ddl Expected outcome - successful creation
     * 
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldImport_SAPGatewayMedium() throws Exception {
        final File ddlFile = new File( TEIID_SAP_GATEWAY_MEDIUM );
        final RelationalModel relationalModel = getRelationalModel( ddlFile );

        // Get Model name
        // String modelName = relationalModel.getName();

        // ---------------------------------------
        // Test expected tables for the model
        // ---------------------------------------
        final String[] tableNames = new String[] { "CarrierCollection", "FlightCollection" };
        final boolean hasTables = RelationalTestUtil.childrenMatch( relationalModel, tableNames, RelationalConstants.Type.TABLE );
        if ( !hasTables ) {
            Assert.fail( "expected Tables do not match" ); //$NON-NLS-1$
        }

        // ----------------------------------------
        // Test expected params
        // ----------------------------------------
        final Table table = relationalModel.getTable( "FlightCollection" );
        final String[] colNames =
            new String[] { "flightDetails_countryFrom", "flightDetails_cityFrom", "flightDetails_airportFrom", "flightDetails_countryTo",
                            "flightDetails_cityTo", "flightDetails_airportTo", "flightDetails_flightTime", "flightDetails_departureTime",
                            "flightDetails_arrivalTime", "flightDetails_distance", "flightDetails_distanceUnit", "flightDetails_flightType",
                            "flightDetails_period", "carrid", "connid", "fldate",
                            "PRICE", "CURRENCY", "PLANETYPE", "SEATSMAX",
                            "SEATSOCC", "PAYMENTSUM", "SEATSMAX_B", "SEATSOCC_B",
                            "SEATSMAX_F", "SEATSOCC_F" };
        final boolean hasColumns = RelationalTestUtil.childrenMatch( table, colNames, RelationalConstants.Type.COLUMN );
        if ( !hasColumns ) {
            Assert.fail( "expected columns do not match" ); //$NON-NLS-1$
        }
        // ----------------------------------------------------
        // Test expected properties for FlightCollection table
        // ----------------------------------------------------
        Map< String, Object > expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.TABLE_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "FlightCollection" ); //$NON-NLS-1$ 
        expectedProps.put( "teiid_odata:EntityType", "RMTSAMPLEFLIGHT.Flight" ); //$NON-NLS-1$ //$NON-NLS-2$

        // Compare object properties to expected
        String result = RelationalTestUtil.compareProperties( table, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }
        // ---------------------------------------------------------------
        // Test expected properties for flightDetails_flightType column
        // ---------------------------------------------------------------
        // flightDetails_flightType string(1) NOT NULL OPTIONS (ANNOTATION 'Charter', NAMEINSOURCE 'flightType',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.COLUMN_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "flightDetails_flightType" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DATATYPE_NAME, "STRING" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.NULLABLE, "NOT NULL" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.LENGTH, 1 );
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, "flightType" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, "Charter" ); //$NON-NLS-1$ 
        expectedProps.put( "teiid_odata:ColumnGroup", "flightDetails" );
        expectedProps.put( "teiid_odata:ComplexType", "FlightDetails" );

        final Column column = table.getColumn( "flightDetails_flightType" );
        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( column, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // ----------------------------------------
        // Test PrimaryKey
        // ----------------------------------------
        final PrimaryKey pk = table.getPrimaryKey();
        if ( pk == null ) {
            Assert.fail( "expected PK , but got null" ); //$NON-NLS-1$
        }

        // ----------------------------------------
        // Test ForeignKey
        // ----------------------------------------
        final List< ForeignKey > fks = table.getForeignKeys();
        if ( fks.size() != 1 ) {
            Assert.fail( "expected 1 FK, but got " + fks.size() ); //$NON-NLS-1$
        }

        final ForeignKey fk = fks.get( 0 );
        final String fkName = fk.getName();
        Assert.assertEquals( "FK name does not match expected name", "CarrierToFlight", fkName );

        final String fkTableRef = fk.getTableRef();
        if ( fkTableRef == null ) {
            Assert.fail( "fk TableRef is null" );
        }
        final List< String > fkColumnRefs = fk.getColumnRefs();
        if ( fkColumnRefs.size() != 1 ) {
            Assert.fail( "Expected 1 column ref, but got : " + fkColumnRefs.size() );
        }

    }

    /**
     * Test import of Teiid-SAPGatewaySmall.ddl Expected outcome - successful creation
     * 
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldImport_SAPGatewaySmall() throws Exception {
        final File ddlFile = new File( TEIID_SAP_GATEWAY_SMALL );
        final RelationalModel relationalModel = getRelationalModel( ddlFile );

        // Get Model name
        // String modelName = relationalModel.getName();

        // ---------------------------------------
        // Test expected tables for the model
        // ---------------------------------------
        final String[] tableNames =
            new String[] { "BookingCollection", "CarrierCollection", "FlightCollection", "NotificationCollection",
                            "SubscriptionCollection", "TravelAgencies", "TravelagencyCollection" };
        final boolean hasTables = RelationalTestUtil.childrenMatch( relationalModel, tableNames, RelationalConstants.Type.TABLE );
        if ( !hasTables ) {
            Assert.fail( "expected Tables do not match" ); //$NON-NLS-1$
        }

        // =============================================================================================================
        // getFiles procedure
        //
        // CREATE FOREIGN TABLE FlightCollection (
        // flightDetails_countryFrom string(3) NOT NULL OPTIONS (ANNOTATION 'Country', NAMEINSOURCE 'countryFrom',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // flightDetails_cityFrom string(20) NOT NULL OPTIONS (ANNOTATION 'Depart.city', NAMEINSOURCE 'cityFrom',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // flightDetails_airportFrom string(3) NOT NULL OPTIONS (ANNOTATION 'Dep. airport', NAMEINSOURCE 'airportFrom',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // flightDetails_countryTo string(3) NOT NULL OPTIONS (ANNOTATION 'Country', NAMEINSOURCE 'countryTo',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // flightDetails_cityTo string(20) NOT NULL OPTIONS (ANNOTATION 'Arrival city', NAMEINSOURCE 'cityTo',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // flightDetails_airportTo string(3) NOT NULL OPTIONS (ANNOTATION 'Dest. airport', NAMEINSOURCE 'airportTo',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // flightDetails_flightTime integer NOT NULL OPTIONS (ANNOTATION 'Flight time', NAMEINSOURCE 'flightTime',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // flightDetails_departureTime time NOT NULL OPTIONS (ANNOTATION 'Departure', NAMEINSOURCE 'departureTime',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // flightDetails_arrivalTime time NOT NULL OPTIONS (ANNOTATION 'Arrival Time', NAMEINSOURCE 'arrivalTime',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // flightDetails_distance bigdecimal NOT NULL OPTIONS (ANNOTATION 'Distance', NAMEINSOURCE 'distance',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // flightDetails_distanceUnit string(3) NOT NULL OPTIONS (ANNOTATION 'Distance in', NAMEINSOURCE 'distanceUnit',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // flightDetails_flightType string(1) NOT NULL OPTIONS (ANNOTATION 'Charter', NAMEINSOURCE 'flightType',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // flightDetails_period byte NOT NULL OPTIONS (ANNOTATION 'n day(s) later', NAMEINSOURCE 'period', "teiid_odata:ColumnGroup"
        // 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        // carrid string(3) NOT NULL OPTIONS (ANNOTATION 'Airline'),
        // connid string(4) NOT NULL OPTIONS (ANNOTATION 'Flight Number'),
        // fldate timestamp NOT NULL OPTIONS (ANNOTATION 'Date'),
        // PRICE bigdecimal NOT NULL OPTIONS (ANNOTATION 'Airfare'),
        // CURRENCY string(5) NOT NULL OPTIONS (ANNOTATION 'Airline Currency', SEARCHABLE 'Unsearchable'),
        // PLANETYPE string(10) NOT NULL OPTIONS (ANNOTATION 'Type of the plane'),
        // SEATSMAX integer NOT NULL OPTIONS (ANNOTATION 'Max. capacity econ.'),
        // SEATSOCC integer NOT NULL OPTIONS (ANNOTATION 'Occupied econ.'),
        // PAYMENTSUM bigdecimal NOT NULL OPTIONS (ANNOTATION 'Total'),
        // SEATSMAX_B integer NOT NULL OPTIONS (ANNOTATION 'Max. capacity bus.'),
        // SEATSOCC_B integer NOT NULL OPTIONS (ANNOTATION 'Occupied bus.'),
        // SEATSMAX_F integer NOT NULL OPTIONS (ANNOTATION 'Max. capacity 1st'),
        // SEATSOCC_F integer NOT NULL OPTIONS (ANNOTATION 'Occupied 1st'),
        // PRIMARY KEY(carrid, connid, fldate)
        // ) OPTIONS (UPDATABLE TRUE, "teiid_odata:EntityType" 'RMTSAMPLEFLIGHT.Flight');
        // =============================================================================================================
        // ----------------------------------------
        // Test expected params
        // ----------------------------------------
        final Table table = relationalModel.getTable( "FlightCollection" );
        String[] colNames =
            new String[] { "flightDetails_countryFrom", "flightDetails_cityFrom", "flightDetails_airportFrom", "flightDetails_countryTo",
                            "flightDetails_cityTo", "flightDetails_airportTo", "flightDetails_flightTime", "flightDetails_departureTime",
                            "flightDetails_arrivalTime", "flightDetails_distance", "flightDetails_distanceUnit", "flightDetails_flightType",
                            "flightDetails_period", "carrid", "connid", "fldate",
                            "PRICE", "CURRENCY", "PLANETYPE", "SEATSMAX",
                            "SEATSOCC", "PAYMENTSUM", "SEATSMAX_B", "SEATSOCC_B",
                            "SEATSMAX_F", "SEATSOCC_F" };
        final boolean hasColumns = RelationalTestUtil.childrenMatch( table, colNames, RelationalConstants.Type.COLUMN );
        if ( !hasColumns ) {
            Assert.fail( "expected columns do not match" ); //$NON-NLS-1$
        }
        // ----------------------------------------------------
        // Test expected properties for FlightCollection table
        // ----------------------------------------------------
        Map< String, Object > expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.TABLE_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "FlightCollection" ); //$NON-NLS-1$ 
        expectedProps.put( "teiid_odata:EntityType", "RMTSAMPLEFLIGHT.Flight" ); //$NON-NLS-1$ //$NON-NLS-2$

        // Compare object properties to expected
        String result = RelationalTestUtil.compareProperties( table, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }
        // ---------------------------------------------------------------
        // Test expected properties for flightDetails_flightType column
        // ---------------------------------------------------------------
        // flightDetails_flightType string(1) NOT NULL OPTIONS (ANNOTATION 'Charter', NAMEINSOURCE 'flightType',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.COLUMN_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "flightDetails_flightType" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DATATYPE_NAME, "STRING" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.NULLABLE, "NOT NULL" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.LENGTH, 1 );
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, "flightType" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, "Charter" ); //$NON-NLS-1$ 
        expectedProps.put( "teiid_odata:ColumnGroup", "flightDetails" );
        expectedProps.put( "teiid_odata:ComplexType", "FlightDetails" );

        final Column column = table.getColumn( "flightDetails_flightType" );
        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( column, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // ----------------------------------------
        // Test PrimaryKey
        // ----------------------------------------
        final PrimaryKey pk = table.getPrimaryKey();
        colNames = new String[] { "carrid", "CARRNAME", "CURRCODE", "URL",
                        "mimeType", "flightDetails_flightType" };
        final List< String > pkCols = pk.getColumns();
        if ( pkCols.size() != 3 ) {
            Assert.fail( "expected 3 PK columns, but got " + pkCols.size() ); //$NON-NLS-1$
        }

    }

    @Test
    public void shouldOutputModel() throws Exception {
        final File ddlFile = new File( TEIID_MYSQL_ACCTS );
        final Model model = getModeshapeModel( ddlFile );

        // Output the ModeshapeModeler Model
        int level = 0;
        for ( final ModelObject mo : model.children() ) {
            printModelObject( level, mo );
        }

        // Output the RelationalModel for same ddl
        final RelationalModel relModel = getRelationalModel( ddlFile );
        level = 0;
        for ( final RelationalObject ro : relModel.getChildren() ) {
            printRelationalObject( level, ro );
        }
    }

    /**
     * Test setting of properties of relational columns after import of Teiid-SAPGateway.ddl Expected outcome - successful
     * modification
     * 
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSetColumnProperties() throws Exception {
        final File ddlFile = new File( TEIID_SAP_GATEWAY_MEDIUM );
        final RelationalModel relationalModel = getRelationalModel( ddlFile );

        // Get Model name
        // String modelName = relationalModel.getName();

        // ---------------------------------------
        // Test expected tables for the model
        // ---------------------------------------
        final String[] tableNames = new String[] { "CarrierCollection", "FlightCollection" };
        final boolean hasTables = RelationalTestUtil.childrenMatch( relationalModel, tableNames, RelationalConstants.Type.TABLE );
        if ( !hasTables ) {
            Assert.fail( "expected Tables do not match" ); //$NON-NLS-1$
        }

        // ----------------------------------------
        // Test expected params
        // ----------------------------------------
        final Table table = relationalModel.getTable( "FlightCollection" );
        final String[] colNames =
            new String[] { "flightDetails_countryFrom", "flightDetails_cityFrom", "flightDetails_airportFrom", "flightDetails_countryTo",
                            "flightDetails_cityTo", "flightDetails_airportTo", "flightDetails_flightTime", "flightDetails_departureTime",
                            "flightDetails_arrivalTime", "flightDetails_distance", "flightDetails_distanceUnit", "flightDetails_flightType",
                            "flightDetails_period", "carrid", "connid", "fldate",
                            "PRICE", "CURRENCY", "PLANETYPE", "SEATSMAX",
                            "SEATSOCC", "PAYMENTSUM", "SEATSMAX_B", "SEATSOCC_B",
                            "SEATSMAX_F", "SEATSOCC_F" };
        final boolean hasColumns = RelationalTestUtil.childrenMatch( table, colNames, RelationalConstants.Type.COLUMN );
        if ( !hasColumns ) {
            Assert.fail( "expected columns do not match" ); //$NON-NLS-1$
        }

        // ---------------------------------------------------------------
        // Test expected properties for flightDetails_flightType column
        // ---------------------------------------------------------------
        // flightDetails_flightType string(1) NOT NULL OPTIONS (ANNOTATION 'Charter', NAMEINSOURCE 'flightType',
        // "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
        Map< String, Object > expectedProps = new HashMap< String, Object >();
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.COLUMN_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "flightDetails_flightType" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DATATYPE_NAME, "STRING" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.NULLABLE, "NOT NULL" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.LENGTH, 1 );
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, "flightType" ); //$NON-NLS-1$ 
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, "Charter" ); //$NON-NLS-1$ 
        expectedProps.put( "teiid_odata:ColumnGroup", "flightDetails" );
        expectedProps.put( "teiid_odata:ComplexType", "FlightDetails" );

        final Column column = table.getColumn( "flightDetails_flightType" );
        // Compare object properties to expected
        String result = RelationalTestUtil.compareProperties( column, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // ----------------------------------
        // Set existing OPTION PROPERTIES
        // ----------------------------------
        // Set the NameInSource
        column.setNameInSource( "newNIS" );
        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, "newNIS" ); //$NON-NLS-1$ 
        result = RelationalTestUtil.compareProperties( column, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // UnSet the NameInSource
        column.setNameInSource( null );
        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, null );
        result = RelationalTestUtil.compareProperties( column, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // UnSet the Description
        column.setNameInSource( "anotherNIS" );
        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, "anotherNIS" ); //$NON-NLS-1$ 
        result = RelationalTestUtil.compareProperties( column, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // Set the Description
        column.setDescription( "newDescription" );
        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, "newDescription" ); //$NON-NLS-1$ 
        result = RelationalTestUtil.compareProperties( column, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // UnSet the Description
        column.setDescription( null );
        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, null );
        result = RelationalTestUtil.compareProperties( column, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // Set new Description
        column.setDescription( "anotherDescription" );
        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, "anotherDescription" ); //$NON-NLS-1$ 
        result = RelationalTestUtil.compareProperties( column, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // ----------------------------------
        // Set other column PROPERTIES
        // ----------------------------------
        // Set the updatable property
        column.setNullable( NULLABLE_OPTIONS.NULL );
        column.setAutoIncremented( true );
        column.setLength( 3 );
        column.setCaseSensitive( false );
        column.setCharacterOctetLength( 3 );
        column.setCurrency( true );
        column.setDatatypeName( "string" );
        column.setDefaultValue( "def" );
        column.setLengthFixed( true );
        column.setMaximumValue( "10" );
        column.setMinimumValue( "9" );
        column.setNativeType( "string" );
        column.setNullValueCount( 10 );
        column.setPrecision( 77 );
        column.setRadix( 88 );
        column.setScale( 99 );
        column.setSearchability( SEARCHABILITY_OPTIONS.LIKE_ONLY );
        column.setSelectable( false );
        column.setSigned( false );
        column.setUpdateable( false );

        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.NULLABLE, NULLABLE_OPTIONS.NULL );
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.AUTO_INCREMENTED, true );
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.LENGTH, 3 );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CASE_SENSITIVE, false );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CHAR_OCTET_LENGTH, 3 );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.CURRENCY, true );
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DATATYPE_NAME, "string" );
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.DEFAULT_VALUE, "def" );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.FIXED_LENGTH, true );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.MAX_VALUE, "10" );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.MIN_VALUE, "9" );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.NATIVE_TYPE, "string" );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.NULL_VALUE_COUNT, 10 );
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.PRECISION, 77 );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.RADIX, 88 );
        expectedProps.put( RelationalConstants.COLUMN_DDL_PROPERTY_KEYS.SCALE, 99 );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SEARCHABLE, SEARCHABILITY_OPTIONS.LIKE_ONLY );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SELECTABLE, false );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.SIGNED, false );
        expectedProps.put( RelationalConstants.COLUMN_DDL_OPTION_KEYS.UPDATABLE, false );
        result = RelationalTestUtil.compareProperties( column, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

    }

    /**
     * Test setting of properties of relational procedure parameters after import of Teiid-SAPGateway.ddl Expected outcome -
     * successful modification
     * 
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSetProcParamProperties() throws Exception {
        final File ddlFile = new File( TEIID_SAP_GATEWAY_FULL );
        final RelationalModel relationalModel = getRelationalModel( ddlFile );

        // Get Model name
        // String modelName = relationalModel.getName();

        // ---------------------------------------
        // Test expected procedures for the model
        // ---------------------------------------
        final String[] procNames =
            new String[] { "CheckFlightAvailability", "GetAgencyDetails", "GetAvailableFlights", "GetFlightDetails",
                            "UpdateAgencyPhoneNo" };
        final boolean hasProcs = RelationalTestUtil.childrenMatch( relationalModel, procNames, RelationalConstants.Type.PROCEDURE );
        if ( !hasProcs ) {
            Assert.fail( "expected Procedures do not match" ); //$NON-NLS-1$
        }

        // -----------------------------------------------------
        // Test expected params for GetFlightDetails procedure
        // -----------------------------------------------------
        // CREATE FOREIGN PROCEDURE GetFlightDetails(IN airlineid string, IN connectionid string) RETURNS TABLE (countryFrom string,
        // cityFrom string, airportFrom string, countryTo string, cityTo string, airportTo string, flightTime integer, departureTime
        // time, arrivalTime time, distance bigdecimal, distanceUnit string, flightType string, period byte)
        // OPTIONS ("teiid_odata:EntityType" 'RMTSAMPLEFLIGHT.FlightDetails', "teiid_odata:HttpMethod" 'GET')

        final Procedure proc = relationalModel.getProcedure( "GetFlightDetails" );
        final String[] paramNames = new String[] { "airlineid", "connectionid" };
        final boolean hasParams = RelationalTestUtil.childrenMatch( proc, paramNames, RelationalConstants.Type.PARAMETER );
        if ( !hasParams ) {
            Assert.fail( "expected parameters do not match" ); //$NON-NLS-1$
        }

        // --------------------------------------------------------
        // Test expected properties for Proc Parameter 'airlineid'
        // --------------------------------------------------------
        Map< String, Object > expectedProps = new HashMap< String, Object >();
        final Parameter param = proc.getParameter( "airlineid" );
        expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.PARAMETER_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "airlineid" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME, "STRING" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DIRECTION, "IN" ); //$NON-NLS-1$

        // Compare object properties to expected
        String result = RelationalTestUtil.compareProperties( param, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        param.setDatatypeName( "long" );
        param.setDefaultValue( "dunno" );
        param.setLength( 99 );
        param.setNativeType( "long" );
        param.setPrecision( 98 );
        param.setScale( 97 );
        param.setNullable( RelationalConstants.NULLABLE_OPTIONS.NOT_NULL );
        param.setDirection( RelationalConstants.DIRECTION_OPTIONS.INOUT );

        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DATATYPE_NAME, "long" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DEFAULT_VALUE, "dunno" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.LENGTH, 99 );
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.PRECISION, 98 );
        expectedProps.put( RelationalConstants.PARAMETER_DDL_OPTION_KEYS.NATIVE_TYPE, "long" ); //$NON-NLS-1$
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.SCALE, 97 );
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.NULLABLE, RelationalConstants.NULLABLE_OPTIONS.NOT_NULL );
        expectedProps.put( RelationalConstants.PARAMETER_DDL_PROPERTY_KEYS.DIRECTION, RelationalConstants.DIRECTION_OPTIONS.INOUT );

        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( param, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }
    }

    /**
     * Test setting of properties of relational procedures after import of Teiid-SAPGateway.ddl Expected outcome - successful
     * modification
     * 
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSetProcProperties() throws Exception {
        final File ddlFile = new File( TEIID_SAP_GATEWAY_FULL );
        final RelationalModel relationalModel = getRelationalModel( ddlFile );

        // Get Model name
        // String modelName = relationalModel.getName();

        // ---------------------------------------
        // Test expected procedures for the model
        // ---------------------------------------
        final String[] procNames =
            new String[] { "CheckFlightAvailability", "GetAgencyDetails", "GetAvailableFlights", "GetFlightDetails",
                            "UpdateAgencyPhoneNo" };
        final boolean hasProcs = RelationalTestUtil.childrenMatch( relationalModel, procNames, RelationalConstants.Type.PROCEDURE );
        if ( !hasProcs ) {
            Assert.fail( "expected Procedures do not match" ); //$NON-NLS-1$
        }

        // -----------------------------------------------------
        // Test expected params for GetFlightDetails procedure
        // -----------------------------------------------------
        // CREATE FOREIGN PROCEDURE GetFlightDetails(IN airlineid string, IN connectionid string) RETURNS TABLE (countryFrom string,
        // cityFrom string, airportFrom string, countryTo string, cityTo string, airportTo string, flightTime integer, departureTime
        // time, arrivalTime time, distance bigdecimal, distanceUnit string, flightType string, period byte)
        // OPTIONS ("teiid_odata:EntityType" 'RMTSAMPLEFLIGHT.FlightDetails', "teiid_odata:HttpMethod" 'GET')

        final Procedure proc = relationalModel.getProcedure( "GetFlightDetails" );
        final String[] paramNames = new String[] { "airlineid", "connectionid" };
        final boolean hasParams = RelationalTestUtil.childrenMatch( proc, paramNames, RelationalConstants.Type.PARAMETER );
        if ( !hasParams ) {
            Assert.fail( "expected parameters do not match" ); //$NON-NLS-1$
        }

        // ----------------------------------------------------
        // Test expected properties for GetFlightDetails procedure
        // ----------------------------------------------------
        final Map< String, Object > expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.PROCEDURE_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "GetFlightDetails" ); //$NON-NLS-1$ 
        expectedProps.put( "teiid_odata:EntityType", "RMTSAMPLEFLIGHT.FlightDetails" ); //$NON-NLS-1$ //$NON-NLS-2$
        expectedProps.put( "teiid_odata:HttpMethod", "GET" ); //$NON-NLS-1$ //$NON-NLS-2$

        // Compare object properties to expected
        String result = RelationalTestUtil.compareProperties( proc, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // ----------------------------------
        // Set other procedure PROPERTIES
        // ----------------------------------
        proc.setAggregate( true );
        proc.setAllowsDistinct( true );
        proc.setAllowsOrderBy( true );
        proc.setAnalytic( true );
        proc.setDecomposable( true );
        proc.setDeterminism( DETERMINISM_OPTIONS.SESSION_DETERMINISTIC );
        proc.setFunctionCategory( "MyCat" );
        proc.setJavaClassName( "JClass" );
        proc.setJavaMethodName( "JMethod" );
        proc.setNonPrepared( true );
        proc.setReturnsNullOnNull( true );
        proc.setUpdateCount( UPDATE_COUNT_OPTIONS.MULTIPLE );
        proc.setUseDistinctRows( true );
        proc.setVariableArguments( true );

        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.AGGREGATE, true );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ALLOWS_DISTINCT, true );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ALLOWS_ORDERBY, true );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.ANALYTIC, true );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.DECOMPOSABLE, true );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.DETERMINISM, DETERMINISM_OPTIONS.SESSION_DETERMINISTIC );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.CATEGORY, "MyCat" );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.JAVA_CLASS, "JClass" );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.JAVA_METHOD, "JMethod" );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NON_PREPARED, true );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.NULL_ON_NULL, true );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.UPDATECOUNT, UPDATE_COUNT_OPTIONS.MULTIPLE );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.USES_DISTINCT_ROWS, true );
        expectedProps.put( RelationalConstants.PROCEDURE_DDL_OPTION_KEYS.VARARGS, true );

        // Compare object properties to expected
        result = RelationalTestUtil.compareProperties( proc, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }
    }

    /**
     * Test setting of properties of relational tables after import of Teiid-SAPGateway.ddl Expected outcome - successful
     * modification
     * 
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSetTableProperties() throws Exception {
        final File ddlFile = new File( TEIID_SAP_GATEWAY_MEDIUM );
        final RelationalModel relationalModel = getRelationalModel( ddlFile );

        // Get Model name
        // String modelName = relationalModel.getName();

        // ---------------------------------------
        // Test expected tables for the model
        // ---------------------------------------
        final String[] tableNames = new String[] { "CarrierCollection", "FlightCollection" };
        final boolean hasTables = RelationalTestUtil.childrenMatch( relationalModel, tableNames, RelationalConstants.Type.TABLE );
        if ( !hasTables ) {
            Assert.fail( "expected Tables do not match" ); //$NON-NLS-1$
        }

        // ----------------------------------------
        // Test expected params
        // ----------------------------------------
        final Table table = relationalModel.getTable( "FlightCollection" );

        // ----------------------------------------------------
        // Test expected properties for FlightCollection table
        // ----------------------------------------------------
        final Map< String, Object > expectedProps = new HashMap< String, Object >();
        expectedProps.putAll( RelationalConstants.TABLE_DEFAULT.toMap() );
        expectedProps.put( RelationalConstants.COMMON_DDL_PROPERTY_KEYS.NAME, "FlightCollection" ); //$NON-NLS-1$ 
        expectedProps.put( "teiid_odata:EntityType", "RMTSAMPLEFLIGHT.Flight" ); //$NON-NLS-1$ //$NON-NLS-2$

        // Compare object properties to expected
        String result = RelationalTestUtil.compareProperties( table, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // ----------------------------------
        // Set existing OPTION PROPERTIES
        // ----------------------------------
        // Set the NameInSource
        table.setNameInSource( "newNIS" );
        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, "newNIS" ); //$NON-NLS-1$ 
        result = RelationalTestUtil.compareProperties( table, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // UnSet the NameInSource
        table.setNameInSource( null );
        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, null );
        result = RelationalTestUtil.compareProperties( table, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // UnSet the Description
        table.setNameInSource( "anotherNIS" );
        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.NAME_IN_SOURCE, "anotherNIS" ); //$NON-NLS-1$ 
        result = RelationalTestUtil.compareProperties( table, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // Set the Description
        table.setDescription( "newDescription" );
        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, "newDescription" ); //$NON-NLS-1$ 
        result = RelationalTestUtil.compareProperties( table, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // UnSet the Description
        table.setDescription( null );
        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, null );
        result = RelationalTestUtil.compareProperties( table, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // Set new Description
        table.setDescription( "anotherDescription" );
        // Compare object properties to expected
        expectedProps.put( RelationalConstants.COMMON_DDL_OPTION_KEYS.DESCRIPTION, "anotherDescription" ); //$NON-NLS-1$ 
        result = RelationalTestUtil.compareProperties( table, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

        // ----------------------------------
        // Set other column PROPERTIES
        // ----------------------------------
        table.setCardinality( 999 );
        table.setMaterialized( true );
        table.setMaterializedTable( "matTable" );
        table.setSupportsUpdate( false );

        // Compare object properties to expected
        expectedProps.put( RelationalConstants.TABLE_DDL_OPTION_KEYS.CARDINALITY, 999 );
        expectedProps.put( RelationalConstants.TABLE_DDL_OPTION_KEYS.MATERIALIZED, true );
        expectedProps.put( RelationalConstants.TABLE_DDL_OPTION_KEYS.MATERIALIZED_TABLE, "matTable" );
        expectedProps.put( RelationalConstants.TABLE_DDL_OPTION_KEYS.UPDATABLE, false );
        result = RelationalTestUtil.compareProperties( table, expectedProps );
        if ( !result.equals( "OK" ) ) { //$NON-NLS-1$
            Assert.fail( result );
        }

    }
}
