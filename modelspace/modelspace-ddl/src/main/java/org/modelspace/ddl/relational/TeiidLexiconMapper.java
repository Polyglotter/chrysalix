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


/**
 * TeiidLexiconMapper gets corresponding relational type for supplied lexicon
 */
public class TeiidLexiconMapper {

    /**
     * Maps the DDL lexicon to the matching Relational Object Type
     * 
     * @param ddlType
     *        the ddl type
     * @param constraintType
     *        for constraints, need this to narrow type
     * @return the Relational Type
     */
    public static RelationalConstants.Type getRelationalType( final String ddlType,
                                                              final String constraintType ) {
        RelationalConstants.Type relType = null;
        if ( ddlType.equals( TeiidDdlLexicon.CreateTable.TABLE_STATEMENT ) || ddlType.equals( TeiidDdlLexicon.CreateTable.VIEW_STATEMENT ) ) {
            relType = RelationalConstants.Type.TABLE;
        } else if ( ddlType.equals( TeiidDdlLexicon.CreateTable.TABLE_ELEMENT ) || ddlType.equals( StandardDdlLexicon.TYPE_COLUMN_DEFINITION ) ) {
            relType = RelationalConstants.Type.COLUMN;
        } else if ( ddlType.equals( TeiidDdlLexicon.CreateProcedure.PROCEDURE_STATEMENT ) || ddlType.equals( TeiidDdlLexicon.CreateProcedure.FUNCTION_STATEMENT ) ) {
            relType = RelationalConstants.Type.PROCEDURE;
        } else if ( ddlType.equals( TeiidDdlLexicon.Constraint.TABLE_ELEMENT ) ) {
            if ( constraintType.equals( "PRIMARY KEY" ) ) {
                relType = RelationalConstants.Type.PRIMARY_KEY;
            } else if ( constraintType.equals( "FOREIGN KEY" ) ) {
                relType = RelationalConstants.Type.FOREIGN_KEY;
            } else if ( constraintType.equals( "UNIQUE" ) ) {
                relType = RelationalConstants.Type.UNIQUE_CONSTRAINT;
            } else if ( constraintType.equals( "ACCESSPATTERN" ) ) {
                relType = RelationalConstants.Type.ACCESS_PATTERN;
            } else if ( constraintType.equals( "INDEX" ) ) {
                relType = RelationalConstants.Type.INDEX;
            } else {
                relType = RelationalConstants.Type.PRIMARY_KEY;
            }
        } else if ( ddlType.equals( TeiidDdlLexicon.Constraint.FOREIGN_KEY_CONSTRAINT ) ) {
            relType = RelationalConstants.Type.FOREIGN_KEY;
        } else if ( ddlType.equals( TeiidDdlLexicon.Constraint.INDEX_CONSTRAINT ) ) {
            relType = RelationalConstants.Type.INDEX;
        } else if ( ddlType.equals( TeiidDdlLexicon.CreateTable.TABLE_ELEMENT ) ) {
            relType = RelationalConstants.Type.COLUMN;
        } else if ( ddlType.equals( TeiidDdlLexicon.CreateProcedure.PARAMETER ) ) {
            relType = RelationalConstants.Type.PARAMETER;
        } else if ( ddlType.equals( TeiidDdlLexicon.CreateProcedure.RESULT_COLUMNS ) ) {
            relType = RelationalConstants.Type.RESULT_SET;
        } else if ( ddlType.equals( TeiidDdlLexicon.CreateProcedure.RESULT_COLUMN ) ) {
            relType = RelationalConstants.Type.COLUMN;
        } else if ( ddlType.equals( TeiidDdlLexicon.CreateProcedure.RESULT_DATA_TYPE ) ) {
            relType = RelationalConstants.Type.RESULT_SET;
        }
        return relType;
    }

}
