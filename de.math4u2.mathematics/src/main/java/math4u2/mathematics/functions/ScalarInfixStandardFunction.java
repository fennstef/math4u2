package math4u2.mathematics.functions;

import math4u2.mathematics.types.*;

/**
 * <p>
 * Zweistellige Standardfuntionen in Infix-Schreibweise, die als Argument zwei
 * Skalare erwarten und als Ergebnis wieder einen Skalar liefern.
 * </p>
 */

public abstract class ScalarInfixStandardFunction extends InfixStandardFunction {

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

} // ScalarInfixStandardFunction
