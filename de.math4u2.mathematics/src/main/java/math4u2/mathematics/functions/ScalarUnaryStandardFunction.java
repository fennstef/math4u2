package math4u2.mathematics.functions;

import math4u2.mathematics.types.ScalarType;

/**
 * <p>
 * Einstellige Standardfuntionen, die als Argument einen Skalar erwarten und als
 * Ergebnis wieder einen Skalar liefern.
 * </p>
 */

public abstract class ScalarUnaryStandardFunction extends UnaryStandardFunction {

	// Ergebnistyp: Skalar
	public final Class getResultType(Class[] argTypes) {
		return ScalarType.class;
	}

	// Genau eine Variable vom Typ Skalar
	public final Class getVariableType(int pos) throws MathException {
		if (pos == 0)
			return ScalarType.class;

		throw new MathException("Funktion " + name
				+ " hat keine Variable an der Stelle " + pos);
	}

} //ScalarUnaryStandardFunction
