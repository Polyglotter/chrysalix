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
package org.polyglotter;

import org.polyglotter.common.I18n;

/**
 * Internationalized string constants for the <strong>Polyglotter</strong> project.
 */
@SuppressWarnings( "javadoc" )
public class PolyglotterI18n {

    public static I18n addOperationDescription = new I18n( "Adds two or more numbers together" );
    public static I18n addOperationHasNoTerms = new I18n( "Add operation \"%s\" has no terms" );
    public static I18n addOperationName = new I18n( "Add" );

    public static I18n averageOperationDescription = new I18n( "Computes the average value of a collection of numeric terms" );
    public static I18n averageOperationError = new I18n( "Average operation \"%s\" failed to calculate" );
    public static I18n averageOperationHasNoTerms = new I18n( "Average operation \"%s\" has no terms" );
    public static I18n averageOperationName = new I18n( "Average" );

    public static I18n concatOperationDescription =
        new I18n( "Concatenates the string representaion of two or more terms together" );
    public static I18n concatOperationName = new I18n( "Concat" );

    public static I18n countOperationDescription = new I18n( "Counts the number of terms" );
    public static I18n countOperationName = new I18n( "Count" );

    public static I18n decrementOperationDescription = new I18n( "Decrements its integer term" );
    public static I18n decrementOperationInvalidTermType = new I18n( "Decrement operation \"%s\" term is not an integer" );
    public static I18n decrementOperationMustHaveOneTerm = new I18n( "Decrement operation \"%s\" requires one term" );
    public static I18n decrementOperationName = new I18n( "Decrement" );

    public static I18n divideOperationDescription = new I18n( "Divides two or more numbers together" );
    public static I18n divideOperationHasNoTerms = new I18n( "Divide operation \"%s\" has no terms" );
    public static I18n divideOperationName = new I18n( "Divide" );

    public static I18n errorOnTermChanged = new I18n( "Error setting term to \"%s\" in operation \"%s\"" );

    public static I18n incrementOperationDescription = new I18n( "Increments its integer term" );
    public static I18n incrementOperationInvalidTermType = new I18n( "Increment operation \"%s\" term is not an integer" );
    public static I18n incrementOperationMustHaveOneTerm = new I18n( "Increment operation \"%s\" requires one term" );
    public static I18n incrementOperationName = new I18n( "Increment" );

    public static I18n invalidTermCount = new I18n( "The operation \"%s\" has an invalid term count of \"%s.\"" );
    public static I18n invalidTermType =
        new I18n( "The term \"%s\" in operation \"%s\" does not have a result type of \"Number\" or is null" );

    public static I18n listenerAlreadyRegistered = new I18n( "The listener is already registered" );
    public static I18n listenerError =
        new I18n( "The listener \"%s\" is being unregistered. Event: \"%s\"" );
    public static I18n listenerNotFoundToUnregister = new I18n( "The listener being unregistered was not a registered listener" );

    public static I18n maxOperationDescription = new I18n( "Finds the maximum value of two or more numbers" );
    public static I18n maxOperationHasNoTerms = new I18n( "Max operation \"%s\" has no terms" );
    public static I18n maxOperationName = new I18n( "Max" );

    public static I18n medianOperationDescription = new I18n( "Computes the median value of a collection of numeric terms" );
    public static I18n medianOperationError = new I18n( "Median operation \"%s\" failed to calculate" );
    public static I18n medianOperationHasNoTerms = new I18n( "Median operation \"%s\" has no terms" );
    public static I18n medianOperationName = new I18n( "Median" );

    public static I18n minOperationDescription = new I18n( "Finds the minimum value of two or more numbers" );
    public static I18n minOperationHasNoTerms = new I18n( "Min operation \"%s\" has no terms" );
    public static I18n minOperationName = new I18n( "Min" );

    public static I18n multiplyOperationDescription = new I18n( "Multiplies two or more numbers together" );
    public static I18n multiplyOperationHasNoTerms = new I18n( "Multiply operation \"%s\" has no terms" );
    public static I18n multiplyOperationName = new I18n( "Multiply" );

    public static I18n nullTerm = new I18n( "The null term was passed to operation \"%s.\"" );
    public static I18n nullTermId = new I18n( "The null term identifier was passed to operation \"%s.\"" );

    public static I18n opCatArithmeticLabel = new I18n( "Arithmetic" );
    public static I18n opCatArithmeticDescription = new I18n( "Perform their operation on numbers" );
    public static I18n opCatAssignmentLabel = new I18n( "Assignment" );
    public static I18n opCatAssignmentDescription = new I18n( "Perform an assignment operation" );
    public static I18n opCatBitwiseLabel = new I18n( "Bitwise" );
    public static I18n opCatBitwiseDescription = new I18n( "Perform their operation on bit level" );
    public static I18n opCatDateTimeLabel = new I18n( "DateTime" );
    public static I18n opCatDateTimeDescription = new I18n( "Permorm their operation on dates and times" );
    public static I18n opCatLogicalLabel = new I18n( "Logical" );
    public static I18n opCatLogicalDescription = new I18n( "Perform their operation by comparing expressions" );
    public static I18n opCatOtherLabel = new I18n( "Other" );
    public static I18n opCatOtherDescription = new I18n( "An uncategorized operation" );
    public static I18n opCatRelationalLabel = new I18n( "Relational" );
    public static I18n opCatRelationalDescription = new I18n( "Perform their operation by comparing operand" );
    public static I18n opCatStringLabel = new I18n( "String" );
    public static I18n opCatStringDescription = new I18n( "Perform their operation on strings" );

    public static I18n operationHasErrors = new I18n( "The operation \"%s\" has errors and a result cannot be calculated" );

    public static I18n subtractOperationDescription = new I18n( "Subtracts two or more numbers together" );
    public static I18n subtractOperationHasNoTerms = new I18n( "Subtract operation \"%s\" has no terms" );
    public static I18n subtractOperationName = new I18n( "Subtract" );

    public static I18n termNotFound = new I18n( "The term \"%s\" in operation \"%s\" could not be found" );
    public static I18n termExists = new I18n( "The term \"%s\" in operation \"%s\" already exists" );

    public static I18n voidTermDescription = new I18n( "A term with a void or null value" );
    public static I18n voidTermName = new I18n( "Void" );

}
