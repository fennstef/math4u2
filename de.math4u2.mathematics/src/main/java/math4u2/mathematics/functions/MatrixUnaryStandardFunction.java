package math4u2.mathematics.functions;

import math4u2.controller.Broker;
import math4u2.mathematics.types.MatrixType;

/**
 * <p>
 * Einstellige Standardfuntionen, die als Argument eine Matrix erwarten und als
 * Ergebnis wieder eine Matrix liefern.
 * </p>
 */

public abstract class MatrixUnaryStandardFunction extends UnaryStandardFunction {

	// Ergebnistyp: Matrix
	public Class getResultType(Class[] argTypes) {
		return MatrixType.class;
	}

	// Genau eine Variable vom Typ Matrix
	public final Class getVariableType(int pos) throws MathException {
		if (pos == 0)
			return MatrixType.class;

		throw new MathException("Funktion " + name
				+ " hat keine Variable an der Stelle " + pos);
	}

	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Fehler bei Verwendung von " + name
				+ "Ableitung bei Argument vom Typ Matrix nicht definiert");
	}

} //MatrixUnaryStandardFunction
