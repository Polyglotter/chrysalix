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

    public static I18n errorAddingBuiltInCategory = new I18n( "An error occurred adding a built-in category to operation '%s'" );

    public static I18n absoluteValueOperationDescription = new I18n( "Finds the absolute value of a number" );
    public static I18n absoluteValueOperationInputDescription = new I18n( "The number whose absolute value is being calculated" );
    public static I18n absoluteValueOperationInputName = new I18n( "Input" );
    public static I18n absoluteValueOperationInvalidTermType =
        new I18n( "Absolute value operation in transformation '%s' has a term that is not a number" );
    public static I18n absoluteValueOperationMustHaveOneTerm =
        new I18n( "Absolute value operation in transformation '%s' requires one and only one term" );
    public static I18n absoluteValueOperationName = new I18n( "Absolute Value" );

    public static I18n addOperationDescription = new I18n( "Adds two or more numbers together" );
    public static I18n addOperationHasNoTerms = new I18n( "Add operation in transformation '%s' has no terms" );
    public static I18n addOperationInputDescription = new I18n( "An input term being added to other terms." );
    public static I18n addOperationInputName = new I18n( "Input" );
    public static I18n addOperationName = new I18n( "Add" );

    public static I18n arcCosineOperationDescription = new I18n( "Calculates the arc cosine of a numeric term" );
    public static I18n arcCosineOperationInputDescription = new I18n( "The input term whose arc cosine is being calculated." );
    public static I18n arcCosineOperationInputName = new I18n( "Input" );
    public static I18n arcCosineOperationInvalidTermType =
        new I18n( "Arc Cosine operation in transformation '%s' has a term that is not a number" );
    public static I18n arcCosineOperationMustHaveOneTerm =
        new I18n( "Arc Cosine operation in transformation '%s' requires one and only one term" );
    public static I18n arcCosineOperationName = new I18n( "Arc Cosine" );

    public static I18n arcSineOperationDescription = new I18n( "Calculates the arc sine of a numeric term" );
    public static I18n arcSineOperationInputDescription = new I18n( "The input term whose arc sine is being calculated." );
    public static I18n arcSineOperationInputName = new I18n( "Input" );
    public static I18n arcSineOperationInvalidTermType =
        new I18n( "Arc Sine operation in transformation '%s' has a term that is not a number" );
    public static I18n arcSineOperationMustHaveOneTerm =
        new I18n( "Arc Sine operation in transformation '%s' requires one and only one term" );
    public static I18n arcSineOperationName = new I18n( "Arc Sine" );

    public static I18n arcTangentOperationDescription = new I18n( "Calculates the arc tangent of a numeric term" );
    public static I18n arcTangentOperationInputDescription = new I18n( "The input term whose arc sine is being calculated." );
    public static I18n arcTangentOperationInputName = new I18n( "Input" );
    public static I18n arcTangentOperationInvalidTermType =
        new I18n( "Arc Tangent operation in transformation '%s' has a term that is not a number" );
    public static I18n arcTangentOperationMustHaveOneTerm =
        new I18n( "Arc Tangent operation in transformation '%s' requires one and only one term" );
    public static I18n arcTangentOperationName = new I18n( "Arc Tangent" );

    public static I18n averageOperationDescription = new I18n( "Computes the average value of a collection of numeric terms" );
    public static I18n averageOperationError = new I18n( "Average operation in transformation '%s' failed to calculate" );
    public static I18n averageOperationHasNoTerms = new I18n( "Average operation in transformation '%s' has no terms" );
    public static I18n averageOperationInputDescription = new I18n( "An input term being averaged" );
    public static I18n averageOperationInputName = new I18n( "Input" );
    public static I18n averageOperationName = new I18n( "Average" );

    public static I18n ceilingOperationDescription = new I18n( "Find the closest integer greater than or equal to the term" );
    public static I18n ceilingOperationInputDescription = new I18n( "The input term whose ceiling is being calculated" );
    public static I18n ceilingOperationInputName = new I18n( "Input" );
    public static I18n ceilingOperationInvalidTermType =
        new I18n( "Ceiling operation in transformation '%s' has a term that is not a number" );
    public static I18n ceilingOperationMustHaveOneTerm =
        new I18n( "Ceiling operation in transformation '%s' requires one and only one term" );
    public static I18n ceilingOperationName = new I18n( "Floor" );

    public static I18n concatOperationDescription = new I18n( "Concatenates the string representation of two or more terms" );
    public static I18n concatOperationInputDescription = new I18n( "An input term being concatenated with other terms" );
    public static I18n concatOperationInputName = new I18n( "Input" );
    public static I18n concatOperationName = new I18n( "Concat" );

    public static I18n cosineOperationDescription = new I18n( "Calculates the cosine of a numeric term" );
    public static I18n cosineOperationInputDescription = new I18n( "The input term whose cosine is being calculated" );
    public static I18n cosineOperationInputName = new I18n( "Input" );
    public static I18n cosineOperationInvalidTermType =
        new I18n( "Cosine operation in transformation '%s' has a term that is not a number" );
    public static I18n cosineOperationMustHaveOneTerm =
        new I18n( "Cosine operation in transformation '%s' requires one and only one term" );
    public static I18n cosineOperationName = new I18n( "Cosine" );

    public static I18n countOperationDescription = new I18n( "Counts the number of terms" );
    public static I18n countOperationInputDescription = new I18n( "An input being counted toward the total" );
    public static I18n countOperationInputName = new I18n( "Input" );
    public static I18n countOperationName = new I18n( "Count" );

    public static I18n cubeRootOperationDescription = new I18n( "Calculates the cube root of a numeric term" );
    public static I18n cubeRootOperationInputDescription = new I18n( "The input term whose cube root is being calculated" );
    public static I18n cubeRootOperationInputName = new I18n( "Input" );
    public static I18n cubeRootOperationInvalidTermType =
        new I18n( "Cube root operation in transformation '%s' has a term that is not a number" );
    public static I18n cubeRootOperationMustHaveOneTerm =
        new I18n( "Cube root operation in transformation '%s' requires one and only one term" );
    public static I18n cubeRootOperationName = new I18n( "Cube Root" );

    public static I18n decrementOperationDescription = new I18n( "Decrements its integer term" );
    public static I18n decrementOperationInputDescription = new I18n( "The input term being decremented" );
    public static I18n decrementOperationInputName = new I18n( "Input" );
    public static I18n decrementOperationInvalidTermType =
        new I18n( "Decrement operation in transformation '%s' has term that is not an integer" );
    public static I18n decrementOperationMustHaveOneTerm =
        new I18n( "Decrement operation in transformation '%s' requires one and only one term" );
    public static I18n decrementOperationName = new I18n( "Decrement" );

    public static I18n divideOperationDescription = new I18n( "Divides two or more numbers together" );
    public static I18n divideOperationHasNoTerms = new I18n( "Divide operation in transformation '%s' has no terms" );
    public static I18n divideOperationInputDescription = new I18n( "An input term being divided by or into other terms." );
    public static I18n divideOperationInputName = new I18n( "Input" );
    public static I18n divideOperationName = new I18n( "Divide" );

    public static I18n errorComparingTerms =
        new I18n( "Comparator could not finish as an error obtaining the value for either term '%s' or term '%s' occurred" );
    public static I18n errorOnTermChanged =
        new I18n( "Error processing an input value change in operation '%s' of tranformation '%s'" );

    public static I18n errorAddingOrRemovingNullTransformationModel =
        new I18n( "Tried to add a null model to or remove a null model from transformation '%s'" );
    public static I18n errorAddingOrRemovingNullTransformationOperation =
        new I18n( "Tried to add a null operation to or remove a null operation from transformation '%s'" );
    public static I18n errorAddingTransformationModel =
        new I18n( "A model (whose name could not be determined) could not be added to transformation '%s.' The model type '%s' is invalid." );
    public static I18n errorAddingOrRemovingTransformationOperation =
        new I18n( "The operation '%s' could not be added to transformation '%s.'" );
    public static I18n errorAddingTransformationModelWithName =
        new I18n( "The model '%s' could not be added to transformation '%s.' The model type '%s' is invalid." );
    public static I18n errorRemovingTransformationModel =
        new I18n( "A model (whose name could not be determined) could not be removed from transformation '%s.' The model type '%s' is invalid." );
    public static I18n errorRemovingTransformationModelWithName =
        new I18n( "The model '%s' could not be removed from transformation '%s.' The model type '%s' is invalid." );
    public static I18n errorRemovingUnaddedTransformationModel =
        new I18n( "The model (whose name could not be determined) could not be removed from transformation '%s' as it has never been added." );
    public static I18n errorRemovingUnaddedTransformationModelWithName =
        new I18n( "The model '%s' could not be removed from transformation '%s' as it has never been added." );

    public static I18n floorOperationDescription = new I18n( "Find the closest integer less than or equal to the term" );
    public static I18n floorOperationInputDescription = new I18n( "The input term whose floor is being calculated" );
    public static I18n floorOperationInputName = new I18n( "Input" );
    public static I18n floorOperationInvalidTermType =
        new I18n( "Floor operation in transformation '%s' has a term that is not a number" );
    public static I18n floorOperationMustHaveOneTerm =
        new I18n( "Floor operation in transformation '%s' requires one and only one term" );
    public static I18n floorOperationName = new I18n( "Floor" );

    public static I18n hyperbolicCosineOperationDescription = new I18n( "Calculates the hyperbolic cosine of a numeric term" );
    public static I18n hyperbolicCosineOperationInputDescription =
        new I18n( "The input term whose hyperbolic cosine is being calculated." );
    public static I18n hyperbolicCosineOperationInputName = new I18n( "Input" );
    public static I18n hyperbolicCosineOperationInvalidTermType =
        new I18n( "Hyperbolic Cosine operation in transformation '%s' has a term that is not a number" );
    public static I18n hyperbolicCosineOperationMustHaveOneTerm =
        new I18n( "Hyperbolic Cosine operation in transformation '%s' requires one and only one term" );
    public static I18n hyperbolicCosineOperationName = new I18n( "Hyperbolic Cosine" );

    public static I18n hyperbolicSineOperationDescription = new I18n( "Calculates the hyperbolic sine of a numeric term" );
    public static I18n hyperbolicSineOperationInputDescription =
        new I18n( "The input term whose hyperbolic sine is being calculated." );
    public static I18n hyperbolicSineOperationInputName = new I18n( "Input" );
    public static I18n hyperbolicSineOperationInvalidTermType =
        new I18n( "Hyperbolic Sine operation in transformation '%s' has a term that is not a number" );
    public static I18n hyperbolicSineOperationMustHaveOneTerm =
        new I18n( "Hyperbolic Sine operation in transformation '%s' requires one and only one term" );
    public static I18n hyperbolicSineOperationName = new I18n( "Hyperbolic Sine" );

    public static I18n hyperbolicTangentOperationDescription = new I18n( "Calculates the hyperbolic tangent of a numeric term" );
    public static I18n hyperbolicTangentOperationInputDescription =
        new I18n( "The input term whose hyperbolic tangent is being calculated." );
    public static I18n hyperbolicTangentOperationInputName = new I18n( "Input" );
    public static I18n hyperbolicTangentOperationInvalidTermType =
        new I18n( "Hyperbolic Tangent operation in transformation '%s' has a term that is not a number" );
    public static I18n hyperbolicTangentOperationMustHaveOneTerm =
        new I18n( "Hyperbolic Tangent operation in transformation '%s' requires one and only one term" );
    public static I18n hyperbolicTangentOperationName = new I18n( "Hyperbolic Tangent" );

    public static I18n incrementOperationDescription = new I18n( "Increments its integer term" );
    public static I18n incrementOperationInputDescription = new I18n( "The input term being incremented" );
    public static I18n incrementOperationInputName = new I18n( "Input" );
    public static I18n incrementOperationInvalidTermType =
        new I18n( "Increment operation in transformation '%s' has a term that is not an integer" );
    public static I18n incrementOperationMustHaveOneTerm =
        new I18n( "Increment operation in transformation '%s' requires one and only one term" );
    public static I18n incrementOperationName = new I18n( "Increment" );

    public static I18n invalidTermCount = new I18n( "The operation '%s' in transformation '%s' has an invalid term count of '%s.'" );
    public static I18n invalidTermType =
        new I18n( "The operation '%s' in transformation '%s' has a term with an invalid type or has a term that is null" );

    public static I18n logOperationDescription = new I18n( "Calculates natural logarithm of a term" );
    public static I18n logOperationInputDescription = new I18n( "The input term whose log is being calculated" );
    public static I18n logOperationInputName = new I18n( "Input" );
    public static I18n logOperationInvalidTermType =
        new I18n( "Log operation in transformation '%s' has a term that is not a number" );
    public static I18n logOperationMustHaveOneTerm =
        new I18n( "Log operation in transformation '%s' requires one and only one term" );
    public static I18n logOperationName = new I18n( "Natural Logarithm" );

    public static I18n log10OperationDescription = new I18n( "Calculates base 10 logarithm of a term" );
    public static I18n log10OperationInputDescription = new I18n( "The input term whose log base 10 is being calculated" );
    public static I18n log10OperationInputName = new I18n( "Input" );
    public static I18n log10OperationInvalidTermType =
        new I18n( "Log 10 operation in transformation '%s' has a term that is not a number" );
    public static I18n log10OperationMustHaveOneTerm =
        new I18n( "Log 10 operation in transformation '%s' requires one and only one term" );
    public static I18n log10OperationName = new I18n( "Logarithm Base 10" );

    public static I18n mapOperationDescription = new I18n( "Maps one model object's property to another model object's property" );
    public static I18n mapOperationInvalidSourceModelCount =
        new I18n( "A map operation in transformation '%s' does not have exactly one source model object" );
    public static I18n mapOperationInvalidSourceModelObjectType =
        new I18n( "The source of a map operation in transformation '%s' is not a model object" );
    public static I18n mapOperationInvalidSourcePropCount =
        new I18n( "A map operation in transformation '%s' does not have exactly one source model property name" );
    public static I18n mapOperationInvalidSourcePropType =
        new I18n( "A source model object's property name of a map operation in transformation '%s' is not a string" );
    public static I18n mapOperationInvalidTargetModelCount =
        new I18n( "A map operation in transformation '%s' does not have exactly one target model object" );
    public static I18n mapOperationInvalidTargetPropCount =
        new I18n( "A map operation in transformation '%s' does not have exactly one target model property name" );
    public static I18n mapOperationInvalidTargetPropType =
        new I18n( "A target model object's property name of a map operation in transformation '%s' is not a string" );
    public static I18n mapOperationInvalidTargetModelObjectType =
        new I18n( "A target of a map operation in transformation '%s' is not a model object" );
    public static I18n mapOperationInvalidTermCount =
        new I18n( "A map operation in transformation '%s' does not have exactly 4 terms (source model object and property name, and target model object and property name" );
    public static I18n mapOperationName = new I18n( "Map" );
    public static I18n mapOperationSourceDescription = new I18n( "The source model whose property is being mapped" );
    public static I18n mapOperationSourceName = new I18n( "Source" );
    public static I18n mapOperationSourcePropDescription = new I18n( "The name of the source model's property being mapped" );
    public static I18n mapOperationSourcePropName = new I18n( "Source Property" );
    public static I18n mapOperationTargetDescription = new I18n( "The target model whose property is being mapped" );
    public static I18n mapOperationTargetName = new I18n( "Target" );
    public static I18n mapOperationTargetPropDescription = new I18n( "The name of the target model's property being mapped" );
    public static I18n mapOperationTargetPropName = new I18n( "Target Property" );

    public static I18n maxOperationDescription = new I18n( "Finds the maximum value of two or more numbers" );
    public static I18n maxOperationHasNoTerms = new I18n( "Max operation in transformation '%s' has no terms" );
    public static I18n maxOperationInputDescription =
        new I18n( "An input term used to determine the maximum value of a set of terms." );
    public static I18n maxOperationInputName = new I18n( "Input" );
    public static I18n maxOperationName = new I18n( "Max" );

    public static I18n medianOperationDescription = new I18n( "Computes the median value of a collection of numeric terms" );
    public static I18n medianOperationError = new I18n( "Median operation in transformation '%s' failed to calculate" );
    public static I18n medianOperationHasNoTerms = new I18n( "Median operation in transformation '%s' has no terms" );
    public static I18n medianOperationInputDescription =
        new I18n( "An input term used to determine the median value of a set of terms." );
    public static I18n medianOperationInputName = new I18n( "Input" );
    public static I18n medianOperationName = new I18n( "Median" );

    public static I18n message = new I18n( "%s" );

    public static I18n minOperationDescription = new I18n( "Finds the minimum value of two or more numbers" );
    public static I18n minOperationHasNoTerms = new I18n( "Min operation in transformation '%s' has no terms" );
    public static I18n minOperationInputDescription =
        new I18n( "An input term used to determine the minimum value of a set of terms." );
    public static I18n minOperationInputName = new I18n( "Input" );
    public static I18n minOperationName = new I18n( "Min" );

    public static I18n modeOperationDescription = new I18n( "Finds the mode of two or more numbers" );
    public static I18n modeOperationHasNoTerms = new I18n( "Mode operation in transformation '%s' has no terms" );
    public static I18n modeOperationInputDescription =
        new I18n( "An input term used to determine the mode value of a set of terms." );
    public static I18n modeOperationInputName = new I18n( "Input" );
    public static I18n modeOperationName = new I18n( "Mode" );

    public static I18n modulusOperationDescription =
        new I18n( "Calculates the remainder of the first term divided by the second term" );
    public static I18n modulusOperationDividendDescription = new I18n( "The input term being divided by the divisor." );
    public static I18n modulusOperationDividendName = new I18n( "Dividend" );
    public static I18n modulusOperationInvalidDividendTermType =
        new I18n( "The dividend term of modulus operation in transformation '%s' must be a number" );
    public static I18n modulusOperationDivisorDescription = new I18n( "The input term divided into the dividend." );
    public static I18n modulusOperationDivisorName = new I18n( "Divisor" );
    public static I18n modulusOperationInvalidDivisorTermType =
        new I18n( "The divisor term of modulus operation in transformation '%s' must be a number" );
    public static I18n modulusOperationInvalidDividendCount =
        new I18n( "Modulus operation in transformation '%s' must have exactly one dividend term" );
    public static I18n modulusOperationInvalidDivisorCount =
        new I18n( "Modulus operation in transformation '%s' must have exactly one divisor term" );
    public static I18n modulusOperationInvalidTermCount = new I18n( "Modulus operation in transformation '%s' must have 2 terms" );
    public static I18n modulusOperationName = new I18n( "Modulus" );

    public static I18n multiplyOperationDescription = new I18n( "Multiplies two or more numbers together" );
    public static I18n multiplyOperationHasNoTerms = new I18n( "Multiply operation in transformation '%s' has no terms" );
    public static I18n multiplyOperationInputDescription = new I18n( "An input term being multiplied by other terms." );
    public static I18n multiplyOperationInputName = new I18n( "Input" );
    public static I18n multiplyOperationName = new I18n( "Multiply" );

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

    public static I18n errorAddingOrRemovingOperationCategory =
        new I18n( "There was an error adding or removing categories for operation '%s' in transformation '%s'" );
    public static I18n errorAddingOrRemovingOperationInput =
        new I18n( "There was an error adding or removing terms for operation '%s' in transformation '%s' using descriptor '%s'" );
    public static I18n errorRemovingOperationInput =
        new I18n( "There was an error removing terms for operation '%s' in transformation '%s'" );
    public static I18n operationHasErrors =
        new I18n( "The operation '%s' in transformation '%s' has errors and a result cannot being calculated" );
    public static I18n operationResultNotModifiable =
        new I18n( "The '%s' operation's result in transformation '%s' is not directly modifiable" );
    public static I18n operationValidationError =
        new I18n( "An exception occurred when validating the input of operation '%s' in transformation '%s'" );

    public static I18n parseDoubleOperationDescription = new I18n( "Parses the string term into a double value" );
    public static I18n parseDoubleOperationInputDescription =
        new I18n( "The input term whose values is being converted to a double" );
    public static I18n parseDoubleOperationInputName = new I18n( "Input" );
    public static I18n parseDoubleOperationInvalidTermType =
        new I18n( "Parse Double operation in transformation '%s' has a term that is not a number" );
    public static I18n parseDoubleOperationMustHaveOneTerm =
        new I18n( "Parse Double operation in transformation '%s' requires one and only one term" );
    public static I18n parseDoubleOperationName = new I18n( "Parse Double" );

    public static I18n powerOfEOperationDescription = new I18n( "Calculates Euler's number raised to a power" );
    public static I18n powerOfEOperationInputDescription =
        new I18n( "The input term whose value is the power to raise Euler's number to" );
    public static I18n powerOfEOperationInputName = new I18n( "Input" );
    public static I18n powerOfEOperationInvalidTermType =
        new I18n( "Power of E operation in transformation '%s' has a term that is not a number" );
    public static I18n powerOfEOperationMustHaveOneTerm =
        new I18n( "Power of E operation in transformation '%s' requires one and only one term" );
    public static I18n powerOfEOperationName = new I18n( "Power Of E" );

    public static I18n powerOfEMinus1OperationDescription =
        new I18n( "Calculates Euler's number raised to a power and then minus one" );
    public static I18n powerOfEMinus1OperationInputDescription =
        new I18n( "The input term whose value is the power to raise Euler's number" );
    public static I18n powerOfEMinus1OperationInputName = new I18n( "Input" );
    public static I18n powerOfEMinus1OperationInvalidTermType =
        new I18n( "Power of E minus 1 operation in transformation '%s' has a term that is not a number" );
    public static I18n powerOfEMinus1OperationMustHaveOneTerm =
        new I18n( "Power of E minus 1 operation in transformation '%s' requires one and only one term" );
    public static I18n powerOfEMinus1OperationName = new I18n( "Power Of E Minus 1" );

    public static I18n powerOperationBaseDescription = new I18n( "The input term whose value is being raised to a power" );
    public static I18n powerOperationBaseName = new I18n( "Base" );
    public static I18n powerOperationDescription =
        new I18n( "Calculates the value of the first term raised to the power of the second term" );
    public static I18n powerOperationExponentDescription =
        new I18n( "The input term whose value is the power a number is raised to" );
    public static I18n powerOperationExponentName = new I18n( "Exponent" );
    public static I18n powerOperationInvalidBaseTermType =
        new I18n( "The base term of power operation in transformation '%s' must be a number" );
    public static I18n powerOperationInvalidExponentTermType =
        new I18n( "The exponent term of power operation in transformation '%s' must be a number" );
    public static I18n powerOperationInvalidBaseCount =
        new I18n( "Power operation in transformation '%s' must have exactly one base term" );
    public static I18n powerOperationInvalidExponentCount =
        new I18n( "Power operation in transformation '%s' must have exactly one exponent term" );
    public static I18n powerOperationInvalidTermCount = new I18n( "Power operation in transformation '%s' must have 2 terms" );
    public static I18n powerOperationName = new I18n( "Power" );

    public static I18n randomOperationDescription =
        new I18n( "Provides a random number with a positive sign, greater than or equal to 0.0 and less than 1.0." );
    public static I18n randomOperationInvalidNumberOfTerms =
        new I18n( "Random operation in transformation '%s' cannot have any terms" );
    public static I18n randomOperationName = new I18n( "Random" );

    public static I18n roundOperationDescription = new I18n( "Calculates the nearest number" );
    public static I18n roundOperationInputDescription = new I18n( "The input term whose value is being rounded" );
    public static I18n roundOperationInputName = new I18n( "Input" );
    public static I18n roundOperationInvalidTermType =
        new I18n( "Round operation in transformation '%s' has a term that is not a number" );
    public static I18n roundOperationMustHaveOneTerm =
        new I18n( "Round operation in transformation '%s' requires one and only one term" );
    public static I18n roundOperationName = new I18n( "Round" );

    public static I18n signOperationDescription = new I18n( "Determines the sign (0, 1, -1) of a number" );
    public static I18n signOperationInputDescription = new I18n( "The input term whose sign is being determined" );
    public static I18n signOperationInputName = new I18n( "Input" );
    public static I18n signOperationInvalidTermType =
        new I18n( "Sign operation in transformation '%s' has a term that is not a number" );
    public static I18n signOperationMustHaveOneTerm =
        new I18n( "Sign operation in transformation '%s' requires one and only one term" );
    public static I18n signOperationName = new I18n( "Sign" );

    public static I18n sineOperationDescription = new I18n( "Calculates the sine of a numeric term" );
    public static I18n sineOperationInputDescription = new I18n( "The input term whose sine is being calculated" );
    public static I18n sineOperationInputName = new I18n( "Input" );
    public static I18n sineOperationInvalidTermType =
        new I18n( "Sine operation in transformation '%s' has a term that is not a number" );
    public static I18n sineOperationMustHaveOneTerm =
        new I18n( "Sine operation in transformation '%s' requires one and only one term" );
    public static I18n sineOperationName = new I18n( "Sine" );

    public static I18n squareRootOperationDescription = new I18n( "Calculates the square root of a numeric term" );
    public static I18n squareRootOperationInputDescription = new I18n( "The input term whose square root is being calculated" );
    public static I18n squareRootOperationInputName = new I18n( "Input" );
    public static I18n squareRootOperationInvalidTermType =
        new I18n( "Square root operation in transformation '%s' has a term that is not a number" );
    public static I18n squareRootOperationMustHaveOneTerm =
        new I18n( "Square root operation in transformation '%s' requires one and only one term" );
    public static I18n squareRootOperationName = new I18n( "Square Root" );

    public static I18n subtractOperationDescription = new I18n( "Subtracts two or more numbers together" );
    public static I18n subtractOperationHasNoTerms = new I18n( "Subtract operation in transformation '%s' has no terms" );
    public static I18n subtractOperationInputDescription = new I18n( "An input term being subtracted from other terms." );
    public static I18n subtractOperationInputName = new I18n( "Input" );
    public static I18n subtractOperationName = new I18n( "Subtract" );

    public static I18n tangentOperationDescription = new I18n( "Calculates the tangent of a numeric term" );
    public static I18n tangentOperationInputDescription = new I18n( "The input term whose tangent is being calculated" );
    public static I18n tangentOperationInputName = new I18n( "Input" );
    public static I18n tangentOperationInvalidTermType =
        new I18n( "Tangent operation in transformation '%s' has a term that is not a number" );
    public static I18n tangentOperationMustHaveOneTerm =
        new I18n( "Tangent operation in transformation '%s' requires one and only one term" );
    public static I18n tangentOperationName = new I18n( "Tangent" );

    public static I18n toDegreesOperationDescription =
        new I18n( "Converts an angle measured in radians to an approximately equivalent angle measured in degrees" );
    public static I18n toDegreesOperationInputDescription = new I18n( "The input term whose values is being converted to degrees" );
    public static I18n toDegreesOperationInputName = new I18n( "Input" );
    public static I18n toDegreesOperationInvalidTermType =
        new I18n( "To Degrees operation in transformation '%s' has a term that is not a number" );
    public static I18n toDegreesOperationMustHaveOneTerm =
        new I18n( "To Degrees operation in transformation '%s' requires one and only one term" );
    public static I18n toDegreesOperationName = new I18n( "To Degrees" );

    public static I18n toRadiansOperationDescription =
        new I18n( "Converts an angle measured in degrees to an approximately equivalent angle measured in radians" );
    public static I18n toRadiansOperationInputDescription = new I18n( "The input term whose values is being converted to radians" );
    public static I18n toRadiansOperationInputName = new I18n( "Input" );
    public static I18n toRadiansOperationInvalidTermType =
        new I18n( "To Radians operation in transformation '%s' has a term that is not a number" );
    public static I18n toRadiansOperationMustHaveOneTerm =
        new I18n( "To Radians operation in transformation '%s' requires one and only one term" );
    public static I18n toRadiansOperationName = new I18n( "To Radians" );

    public static I18n valueNotModifiable = new I18n( "The value '%s' is not modifiable" );

    public static I18n nullOperationBeingAddedToTransformation =
        new I18n( "Null operation cannot be added to transformation '%s'" );
    public static I18n nullOperationBeingRemovedFromTransformation =
        new I18n( "Null operation cannot be removed from transformation '%s'" );
    public static I18n operationCouldNotBeRemovedFromTransformation =
        new I18n( "Operation '%s' was not found in transformation '%s' and could not be removed" );
    public static I18n operationWasNotAddedToTransformation =
        new I18n( "Operation '%s' was not added to transformation '%s'" );

    public static I18n unhandledOperationDescriptor = new I18n( "Descriptor '%s' was not handled by the operation factory" );

    public static I18n voidTermDescription = new I18n( "A term with a void or null value" );
    public static I18n voidTermName = new I18n( "Void" );

}
