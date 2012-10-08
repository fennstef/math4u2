package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscUnaryStandardFunction;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.ScalarType;

/**
 * Maximum der Elemente einer Matrix
 */

public class MatrixMaxElemFunction extends MiscUnaryStandardFunction {

	public MatrixMaxElemFunction() {
		name = "maxelem";
		summaryText = "Maximum der Elemente einer Matrix oder eines Vektors";
	}//Konstruktor 1

	

	public final Class getResultType(Class[] argTypes) {
		return ScalarType.class;
	}

	// Genau eine Variable vom Typ Matrix
	public final Class getVariableType(int pos) throws MathException {
		if (pos == 0)
			return MatrixType.class;

		throw new MathException("Funktion " + name
				+ " hat keine Variable an der Stelle " + pos);
	}

	/**
	 * 
	 * @param args
	 *            args[0] enthaelt als Element vom Typ MatrixDoubleResult die
	 *            Matrix, von der das größte Element berechnet wird. 
	 * @return Größtes Element als ScalarDoubleResult
	 */
	public Object eval(Object[] args) {

		double[][] elements = ((MatrixDoubleResult) args[0]).valueArray;
		int rowDim = ((MatrixDoubleResult) args[0]).rowDim;
		int colDim = ((MatrixDoubleResult) args[0]).colDim;

		double result = elements[0][0];
		for (int row = 0; row < rowDim; row++) {
			for (int col = 0; col < colDim; col++) {
				if ( elements[row][col] > result )
					result = elements[row][col];
			}
		}
		return new ScalarDoubleResult(result);
	} //eval

	/**
	 * Ableitung nicht moeglich, erzeugt Ausnahme
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Ableitung bei Funktion " + name + " nicht moeglich");
	} // getDeriveFunction

}