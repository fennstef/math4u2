package math4u2.mathematics.functions;

import math4u2.mathematics.types.ScalarType;

/**
 * <p>
 * Einstellige Standardfuntionen, die als Argument einen Skalar erwarten und als
 * Ergebnis wieder einen Skalar liefern.
 * </p>
 */

public abstract class ScalarBinaryStandardFunction extends
		BinaryStandardFunction {

	// Ergebnis: Skalar
	public final Class getResultType(Class[] argTypes) {
		return ScalarType.class;
	}

	// Genau zwei Variable vom Typ Skalar
	public final Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
		case 1:
			return ScalarType.class;

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		}
	}

} // ScalarBinaryStandardFunction
