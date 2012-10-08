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
 * Betrag (Norm) einer Matrix, fuer Vektoren (Spaltenvektoren, Zeilenvektoren)
 * ist das die euklidische Norm, fuer Matrizen ist das die Frobenius-Norm
 */

public class MatrixNormFunction extends MiscUnaryStandardFunction {

	public MatrixNormFunction() {
		name = "norm";
		summaryText = "Euklidische Norm eines Vektors"+
				"<br>Frobenius-Norm einer Matrix";
	}//Konstruktor 1

	public String buildTermString(String[] argStrings, String name) {
		return "|" + argStrings[0] + "|";
	}

	//
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
	 *            Matrix, zu der die Norm berechnet werden soll. Berechnet wird
	 *            die euklidische Norm von Vektoren bzw. die Frobenius-Norm von
	 *            Matrizen.
	 * @return Norm als ScalarDoubleResult
	 */
	public Object eval(Object[] args) {

		double[][] elements = ((MatrixDoubleResult) args[0]).valueArray;
		int rowDim = ((MatrixDoubleResult) args[0]).rowDim;
		int colDim = ((MatrixDoubleResult) args[0]).colDim;

		double result = 0;
		for (int row = 0; row < rowDim; row++) {
			for (int col = 0; col < colDim; col++) {
				result += elements[row][col] * elements[row][col];
			}
		}
		return new ScalarDoubleResult(Math.sqrt(result));
	} //eval

	/**
	 * Ableitung nicht moeglich, erzeugt Ausnahme
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Ableitung bei MatrixNormFunction nicht moeglich");
	} // getDeriveFunction
}