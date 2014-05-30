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

    public static I18n absoluteValueOperationDescription = new I18n( "Finds the absolute value of a number" );
    public static I18n absoluteValueOperationInvalidTermType =
        new I18n( "Absolute value operation '%s' has term '%s' that is not a number" );
    public static I18n absoluteValueOperationMustHaveOneTerm =
        new I18n( "Absolute value operation '%s' requires one and only one term" );
    public static I18n absoluteValueOperationName = new I18n( "Absolute Value" );

    public static I18n addOperationDescription = new I18n( "Adds two or more numbers together" );
    public static I18n addOperationHasNoTerms = new I18n( "Add operation '%s' has no terms" );
    public static I18n addOperationName = new I18n( "Add" );

    public static I18n arcCosineOperationDescription = new I18n( "Calculates the arc cosine of a numeric term" );
    public static I18n arcCosineOperationInvalidTermType =
        new I18n( "Arc Cosine operation '%s' has term '%s' that is not a number" );
    public static I18n arcCosineOperationMustHaveOneTerm = new I18n( "Arc Cosine operation '%s' requires one and only one term" );
    public static I18n arcCosineOperationName = new I18n( "Arc Cosine" );

    public static I18n arcSineOperationDescription = new I18n( "Calculates the arc sine of a numeric term" );
    public static I18n arcSineOperationInvalidTermType = new I18n( "Arc Sine operation '%s' has term '%s' that is not a number" );
    public static I18n arcSineOperationMustHaveOneTerm = new I18n( "Arc Sine operation '%s' requires one and only one term" );
    public static I18n arcSineOperationName = new I18n( "Arc Sine" );

    public static I18n arcTangentOperationDescription = new I18n( "Calculates the arc tangent of a numeric term" );
    public static I18n arcTangentOperationInvalidTermType =
        new I18n( "Arc Tangent operation '%s' has term '%s' that is not a number" );
    public static I18n arcTangentOperationMustHaveOneTerm = new I18n( "Arc Tangent operation '%s' requires one and only one term" );
    public static I18n arcTangentOperationName = new I18n( "Arc Tangent" );

    public static I18n averageOperationDescription = new I18n( "Computes the average value of a collection of numeric terms" );
    public static I18n averageOperationError = new I18n( "Average operation '%s' failed to calculate" );
    public static I18n averageOperationHasNoTerms = new I18n( "Average operation '%s' has no terms" );
    public static I18n averageOperationName = new I18n( "Average" );

    public static I18n ceilingOperationDescription = new I18n( "Find the closest integer greater than or equal to the term" );
    public static I18n ceilingOperationInvalidTermType = new I18n( "Ceiling operation '%s' has term '%s' that is not a number" );
    public static I18n ceilingOperationMustHaveOneTerm = new I18n( "Ceiling operation '%s' requires one and only one term" );
    public static I18n ceilingOperationName = new I18n( "Floor" );

    public static I18n concatOperationDescription =
        new I18n( "Concatenates the string representaion of two or more terms together" );
    public static I18n concatOperationName = new I18n( "Concat" );

    public static I18n cosineOperationDescription = new I18n( "Calculates the cosine of a numeric term" );
    public static I18n cosineOperationInvalidTermType = new I18n( "Cosine operation '%s' has term '%s' that is not a number" );
    public static I18n cosineOperationMustHaveOneTerm = new I18n( "Cosine operation '%s' requires one and only one term" );
    public static I18n cosineOperationName = new I18n( "Cosine" );

    public static I18n countOperationDescription = new I18n( "Counts the number of terms" );
    public static I18n countOperationName = new I18n( "Count" );

    public static I18n cubeRootOperationDescription = new I18n( "Calculates the cube root of a numeric term" );
    public static I18n cubeRootOperationInvalidTermType = new I18n( "Cube root operation '%s' has term '%s' that is not a number" );
    public static I18n cubeRootOperationMustHaveOneTerm = new I18n( "Cube root operation '%s' requires one and only one term" );
    public static I18n cubeRootOperationName = new I18n( "Cube Root" );

    public static I18n decrementOperationDescription = new I18n( "Decrements its integer term" );
    public static I18n decrementOperationInvalidTermType = new I18n( "Decrement operation '%s' has term '%s' that is not a number" );
    public static I18n decrementOperationMustHaveOneTerm = new I18n( "Decrement operation '%s' requires one and only one term" );
    public static I18n decrementOperationName = new I18n( "Decrement" );

    public static I18n divideOperationDescription = new I18n( "Divides two or more numbers together" );
    public static I18n divideOperationHasNoTerms = new I18n( "Divide operation '%s' has no terms" );
    public static I18n divideOperationName = new I18n( "Divide" );

    public static I18n errorOnTermChanged = new I18n( "Error setting term to '%s' in operation '%s'" );

    public static I18n floorOperationDescription = new I18n( "Find the closest integer less than or equal to the term" );
    public static I18n floorOperationInvalidTermType = new I18n( "Floor operation '%s' has term '%s' that is not a number" );
    public static I18n floorOperationMustHaveOneTerm = new I18n( "Floor operation '%s' requires one and only one term" );
    public static I18n floorOperationName = new I18n( "Floor" );

    public static I18n hyperbolicCosineOperationDescription = new I18n( "Calculates the hyperbolic cosine of a numeric term" );
    public static I18n hyperbolicCosineOperationInvalidTermType =
        new I18n( "Hyperbolic Cosine operation '%s' has term '%s' that is not a number" );
    public static I18n hyperbolicCosineOperationMustHaveOneTerm =
        new I18n( "Hyperbolic Cosine operation '%s' requires one and only one term" );
    public static I18n hyperbolicCosineOperationName = new I18n( "Hyperbolic Cosine" );

    public static I18n hyperbolicSineOperationDescription = new I18n( "Calculates the hyperbolic sine of a numeric term" );
    public static I18n hyperbolicSineOperationInvalidTermType =
        new I18n( "Hyperbolic Sine operation '%s' has term '%s' that is not a number" );
    public static I18n hyperbolicSineOperationMustHaveOneTerm =
        new I18n( "Hyperbolic Sine operation '%s' requires one and only one term" );
    public static I18n hyperbolicSineOperationName = new I18n( "Hyperbolic Sine" );

    public static I18n hyperbolicTangentOperationDescription = new I18n( "Calculates the hyperbolic tangent of a numeric term" );
    public static I18n hyperbolicTangentOperationInvalidTermType =
        new I18n( "Hyperbolic Tangent operation '%s' has term '%s' that is not a number" );
    public static I18n hyperbolicTangentOperationMustHaveOneTerm =
        new I18n( "Hyperbolic Tangent operation '%s' requires one and only one term" );
    public static I18n hyperbolicTangentOperationName = new I18n( "Hyperbolic Tangent" );

    public static I18n incrementOperationDescription = new I18n( "Increments its integer term" );
    public static I18n incrementOperationInvalidTermType =
        new I18n( "Increment operation '%s' has term '%s' that is not an integer" );
    public static I18n incrementOperationMustHaveOneTerm = new I18n( "Increment operation '%s' requires one and only one term" );
    public static I18n incrementOperationName = new I18n( "Increment" );

    public static I18n invalidTermCount = new I18n( "The operation '%s' has an invalid term count of '%s.'" );
    public static I18n invalidTermType = new I18n( "The term '%s' in operation '%s' is not a number or is null" );

    public static I18n listenerAlreadyRegistered = new I18n( "The listener is already registered" );
    public static I18n listenerError = new I18n( "The listener '%s' is being unregistered. Event: '%s'" );
    public static I18n listenerNotFoundToUnregister = new I18n( "The listener being unregistered was not a registered listener" );

    public static I18n logOperationDescription = new I18n( "Calculates natural logarithm of a term" );
    public static I18n logOperationInvalidTermType = new I18n( "Log operation '%s' has term '%s' that is not a number" );
    public static I18n logOperationMustHaveOneTerm = new I18n( "Log operation '%s' requires one and only one term" );
    public static I18n logOperationName = new I18n( "Natural Logarithm" );

    public static I18n log10OperationDescription = new I18n( "Calculates base 10 logarithm of a term" );
    public static I18n log10OperationInvalidTermType = new I18n( "Log 10 operation '%s' has term '%s' that is not a number" );
    public static I18n log10OperationMustHaveOneTerm = new I18n( "Log 10 operation '%s' requires one and only one term" );
    public static I18n log10OperationName = new I18n( "Logarithm Base 10" );

    public static I18n maxOperationDescription = new I18n( "Finds the maximum value of two or more numbers" );
    public static I18n maxOperationHasNoTerms = new I18n( "Max operation '%s' has no terms" );
    public static I18n maxOperationName = new I18n( "Max" );

    public static I18n medianOperationDescription = new I18n( "Computes the median value of a collection of numeric terms" );
    public static I18n medianOperationError = new I18n( "Median operation '%s' failed to calculate" );
    public static I18n medianOperationHasNoTerms = new I18n( "Median operation '%s' has no terms" );
    public static I18n medianOperationName = new I18n( "Median" );

    public static I18n minOperationDescription = new I18n( "Finds the minimum value of two or more numbers" );
    public static I18n minOperationHasNoTerms = new I18n( "Min operation '%s' has no terms" );
    public static I18n minOperationName = new I18n( "Min" );

    public static I18n modeOperationDescription = new I18n( "Finds the mode of two or more numbers" );
    public static I18n modeOperationHasNoTerms = new I18n( "Mode operation '%s' has no terms" );
    public static I18n modeOperationName = new I18n( "Mode" );

    public static I18n modulusOperationDescription =
        new I18n( "Calculates the remainder of the first term divided by the second term" );
    public static I18n modulusOperationInvalidDividendTermType =
        new I18n( "The dividend term '%s' of modulus operation '%s' must be a number" );
    public static I18n modulusOperationInvalidDivisorTermType =
        new I18n( "The divisor term '%s' of modulus operation '%s' must be a number" );
    public static I18n modulusOperationInvalidTermCount = new I18n( "Modulus operation '%s' must have 2 terms" );
    public static I18n modulusOperationName = new I18n( "Modulus" );

    public static I18n multiplyOperationDescription = new I18n( "Multiplies two or more numbers together" );
    public static I18n multiplyOperationHasNoTerms = new I18n( "Multiply operation '%s' has no terms" );
    public static I18n multiplyOperationName = new I18n( "Multiply" );

    public static I18n nullTerm = new I18n( "The null term was passed to operation '%s.'" );
    public static I18n nullTermId = new I18n( "The null term identifier was passed to operation '%s.'" );

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

    public static I18n operationHasErrors = new I18n( "The operation '%s' has errors and a result cannot be calculated" );

    public static I18n parseDoubleOperationDescription = new I18n( "Parses the string term into a double value" );
    public static I18n parseDoubleOperationInvalidTermType =
        new I18n( "Parse Double operation '%s' has term '%s' that is not a number" );
    public static I18n parseDoubleOperationMustHaveOneTerm =
        new I18n( "Parse Double operation '%s' requires one and only one term" );
    public static I18n parseDoubleOperationName = new I18n( "Parse Double" );

    public static I18n powerOfEOperationDescription = new I18n( "Calculates Euler's number raised to a power" );
    public static I18n powerOfEOperationInvalidTermType = new I18n( "Power of E operation '%s' has term '%s' that is not a number" );
    public static I18n powerOfEOperationMustHaveOneTerm = new I18n( "Power of E operation '%s' requires one and only one term" );
    public static I18n powerOfEOperationName = new I18n( "Power Of E" );

    public static I18n powerOfEMinus1OperationDescription =
        new I18n( "Calculates Euler's number raised to a power and then minus one" );
    public static I18n powerOfEMinus1OperationInvalidTermType =
        new I18n( "Power of E minus 1 operation '%s' has term '%s' that is not a number" );
    public static I18n powerOfEMinus1OperationMustHaveOneTerm =
        new I18n( "Power of E minus 1 operation '%s' requires one and only one term" );
    public static I18n powerOfEMinus1OperationName = new I18n( "Power Of E Minus 1" );

    public static I18n powerOperationDescription =
        new I18n( "Calculates the value of the first term raised to the power of the second term" );
    public static I18n powerOperationInvalidBaseTermType = new I18n( "The base term '%s' of power operation '%s' must be a number" );
    public static I18n powerOperationInvalidExponentTermType =
        new I18n( "The exponent term '%s' of power operation '%s' must be a number" );
    public static I18n powerOperationInvalidTermCount = new I18n( "Power operation '%s' must have 2 terms" );
    public static I18n powerOperationName = new I18n( "Power" );

    public static I18n randomOperationDescription =
        new I18n( "Provides a random number with a positive sign, greater than or equal to 0.0 and less than 1.0." );
    public static I18n randomOperationInvalidNumberOfTerms = new I18n( "Random operation '%s' cannot have more than 1 term" );
    public static I18n randomOperationInvalidTermType = new I18n( "Random operation '%s' seed term must be a number" );
    public static I18n randomOperationName = new I18n( "Random" );

    public static I18n sineOperationDescription = new I18n( "Calculates the sine of a numeric term" );
    public static I18n sineOperationInvalidTermType = new I18n( "Sine operation '%s' has term '%s' that is not a number" );
    public static I18n sineOperationMustHaveOneTerm = new I18n( "Sine operation '%s' requires one and only one term" );
    public static I18n sineOperationName = new I18n( "Sine" );

    public static I18n squareRootOperationDescription = new I18n( "Calculates the square root of a numeric term" );
    public static I18n squareRootOperationInvalidTermType =
        new I18n( "Square root operation '%s' has term '%s' that is not a number" );
    public static I18n squareRootOperationMustHaveOneTerm = new I18n( "Square root operation '%s' requires one and only one term" );
    public static I18n squareRootOperationName = new I18n( "Square Root" );

    public static I18n subtractOperationDescription = new I18n( "Subtracts two or more numbers together" );
    public static I18n subtractOperationHasNoTerms = new I18n( "Subtract operation '%s' has no terms" );
    public static I18n subtractOperationName = new I18n( "Subtract" );

    public static I18n tangentOperationDescription = new I18n( "Calculates the tangent of a numeric term" );
    public static I18n tangentOperationInvalidTermType = new I18n( "Tangent operation '%s' has term '%s' that is not a number" );
    public static I18n tangentOperationMustHaveOneTerm = new I18n( "Tangent operation '%s' requires one and only one term" );
    public static I18n tangentOperationName = new I18n( "Tangent" );

    public static I18n toDegreesOperationDescription =
        new I18n( "Converts an angle measured in radians to an approximately equivalent angle measured in degrees" );
    public static I18n toDegreesOperationInvalidTermType =
        new I18n( "To Degrees operation '%s' has term '%s' that is not a number" );
    public static I18n toDegreesOperationMustHaveOneTerm = new I18n( "To Degrees operation '%s' requires one and only one term" );
    public static I18n toDegreesOperationName = new I18n( "To Degrees" );

    public static I18n toRadiansOperationDescription =
        new I18n( "Converts an angle measured in degrees to an approximately equivalent angle measured in radians" );
    public static I18n toRadiansOperationInvalidTermType =
        new I18n( "To Radians operation '%s' has term '%s' that is not a number" );
    public static I18n toRadiansOperationMustHaveOneTerm = new I18n( "To Radians operation '%s' requires one and only one term" );
    public static I18n toRadiansOperationName = new I18n( "To Radians" );

    public static I18n termNotFound = new I18n( "The term '%s' in operation '%s' could not be found" );
    public static I18n termExists = new I18n( "The term '%s' in operation '%s' already exists" );

    public static I18n voidTermDescription = new I18n( "A term with a void or null value" );
    public static I18n voidTermName = new I18n( "Void" );

}
