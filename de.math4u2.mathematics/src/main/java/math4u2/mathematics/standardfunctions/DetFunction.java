package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscUnaryStandardFunction;
import math4u2.mathematics.numerics.LinearAlgorithms;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.ScalarType;

/**
 * Determinante
 */

public class DetFunction extends MiscUnaryStandardFunction {

	public DetFunction() {
		name = "det";
		summaryText = "Determinante einer quadratischen Matrix";
	}//Konstruktor 1

	/*
	 * public String buildTermString(String[] argStrings) { return "|" +
	 * argStrings[0] + "|"; }
	 */

	// Ergebnistyp: Skalar
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
	 * Gibt den errechneten Funktionswert zurueck
	 */
	public Object eval(Object[] args) throws MathException {
		int dim = ((MatrixDoubleResult) args[0]).colDim;
		if (dim != ((MatrixDoubleResult) args[0]).rowDim)
			throw new MathException(
					"Von einer nicht-quadratischen Matrix kann keine Determinante berechnet werden");

		return new ScalarDoubleResult(LinearAlgorithms.det(dim,
				((MatrixDoubleResult) args[0]).valueArray));
	} // eval

	/**
	 * Ableitung nicht moeglich, erzeugt Ausnahme
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Ableitung bei DetFunction nicht moeglich");
	} // getDeriveFunction

	public String[] getArgumentStrings() {
		return new String[] {"quadr. Matrix"};
	}
	
	

}