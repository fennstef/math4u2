package math4u2.mathematics.functions;

import math4u2.mathematics.types.*;

public abstract class MatrixInfixStandardFunction extends InfixStandardFunction {

	public Class getResultType(Class[] argTypes) {
		return MatrixType.class;
	}

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
		case 1:
			return MatrixType.class;

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		}
	}
}