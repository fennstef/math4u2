package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscInfixStandardFunction;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.ScalarType;

/**
 * <p>
 * Quotient einer Matrix (erster Operand) durch einen Skalar (zweiter Operand).
 * </p>
 */

public class MatrixScalarQuotFunction extends MiscInfixStandardFunction {

	public MatrixScalarQuotFunction() {
		name = "MatrixScalarQuot";
		operatorName = "/";
		operatorNiceName = " / ";
	}

	public Class getResultType(Class[] argTypes) {
		return argTypes[0];
	}

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
			return MatrixType.class;
		case 1:
			return ScalarType.class;
		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		}
	}

	public Object eval(Object[] args) throws MathException {
		double scalar;
		MatrixDoubleResult matrix;

		// Prüfen, ob genau zwei Argumente ??
		// Prüfen, ob richtiger Typ?

		if (args[1] instanceof ScalarDoubleResult
				&& args[0] instanceof MatrixDoubleResult) {

			scalar = ((ScalarDoubleResult) args[1]).value;
			matrix = (MatrixDoubleResult) args[0];
		} else
			throw new MathException(
					"Falsche Argumentreihenfolge bei MatrixScalarQuotientFunction");

		int rowDim = matrix.rowDim;
		int colDim = matrix.colDim;
		double[][] valueArray = matrix.valueArray;
		double[][] resultArray = new double[rowDim][colDim];

		for (int r = 0; r < rowDim; r++) {
			for (int c = 0; c < colDim; c++) {
				resultArray[r][c] = valueArray[r][c] / scalar;
			}
		}
		return new MatrixDoubleResult(resultArray);
	} //eval

	/**
	 * Konstruiert den Ableitungsterm zur Quotientenregel zu Matrix M(x) und
	 * Skalar g(x): (M(x)/g(x))' = (M'(x)*g(x) - M(x)*g(x))/(g(x)^2) aus den
	 * Termen und den Ableitungstermen.
	 * 
	 * @param arguementTerms
	 *            Array aus M(x) und g(x)
	 * @param derivedArgumentTerms
	 *            Array aus V'(x) und g'(x)
	 * @param broker
	 * @return (M'(x)*g(x) - M(x)*g(x))/(g(x)^2)
	 */
	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		try {
			TermNode s1 = new TermNodeFunct((Function) (broker
					.getObject("MatrixScalarProd")), new TermNode[] {
					derivedArgumentTerms[0], argumentTerms[1] }, broker);
			TermNode s2 = new TermNodeFunct((Function) (broker
					.getObject("MatrixScalarProd")), new TermNode[] {
					derivedArgumentTerms[1], argumentTerms[0] }, broker);
			TermNode num = new TermNodeFunct((Function) (broker
					.getObject("MatrixDiff")), new TermNode[] { s1, s2 },
					broker);
			TermNode denom = new TermNodeFunct((Function) (broker
					.getObject("prod")), new TermNode[] { argumentTerms[1],
					argumentTerms[1] }, broker);
			return new TermNodeFunct((Function) (broker
					.getObject("MatrixScalarQuot")), new TermNode[] { num,
					denom }, broker);
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
	}

	public TermNode simplify(TermNode oldTerm, TermNode[] arguments) {
		return oldTerm;
	}

}//ScalarMatrixQuotFunction

