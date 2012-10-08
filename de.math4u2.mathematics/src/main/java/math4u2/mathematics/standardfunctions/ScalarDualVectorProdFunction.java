package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.DualVectorDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.termnodes.TermNodeNum;
import math4u2.mathematics.termnodes.TermNodeDualVector;
import math4u2.mathematics.types.DualVectorType;
import math4u2.mathematics.types.ScalarType;

/**
 * <p>
 * Produkt eines Skalars (erster Faktor) mit einem Dualvektor (zweiter Faktor).
 * </p>
 */

public class ScalarDualVectorProdFunction extends ScalarMatrixProdFunction {

	public ScalarDualVectorProdFunction() {
		name = "ScalarDualVectorProd";
		operatorName = "*";
		operatorNiceName = "\u200A\u00B7\u200A";
	}

	public Class getResultType(Class[] argTypes) {
		return argTypes[1];
	}

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
			return ScalarType.class;
		case 1:
			return DualVectorType.class;

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		}
	}

	public Object eval(Object[] args) throws MathException {
		double scalar;
		DualVectorDoubleResult dualVector;

		// Prüfen, ob genau zwei Argumente ??
		// Prüfen, ob richtiger Typ?

		if (args[0] instanceof ScalarDoubleResult
				&& args[1] instanceof DualVectorDoubleResult) {

			scalar = ((ScalarDoubleResult) args[0]).value;
			dualVector = (DualVectorDoubleResult) args[1];
		} else
			throw new MathException(
					"Falsche Argumentreihenfolge bei ScalarDualVectorProductFunction");

		int colDim = dualVector.colDim;
		double[][] valueArray = dualVector.valueArray;
		double[][] resultArray = new double[1][colDim];

		for (int c = 0; c < colDim; c++) {
			resultArray[0][c] = valueArray[0][c] * scalar;
		}
		return new DualVectorDoubleResult(resultArray);
	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		try {
			TermNode s1 = new TermNodeFunct((Function) (broker
					.getObject("ScalarDualVectorProd")), new TermNode[] {
					derivedArgumentTerms[0], argumentTerms[1] }, broker);
			TermNode s2 = new TermNodeFunct((Function) (broker
					.getObject("ScalarDualVectorProd")), new TermNode[] {
					argumentTerms[0], derivedArgumentTerms[1] }, broker);

			return new TermNodeFunct((Function) (broker
					.getObject("DualVectorSum")), new TermNode[] { s1, s2 },
					broker);
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
	}

	public TermNode simplify(TermNode oldTerm, TermNode[] arguments, Broker broker)
			throws MathException {
		if (arguments[0] instanceof TermNodeNum) {
			if (((TermNodeNum) arguments[0]).evalScalar() == 1.0)
				return arguments[1];

			if (((TermNodeNum) arguments[0]).evalScalar() == 0.0
					&& arguments[1] instanceof TermNodeDualVector) {
				/*
				 * return TermNodeVector.getNullVector(
				 * ((TermNodeVector)arguments[1]).getRowDim());
				 */
				return ((TermNodeDualVector) arguments[1]).getNullTerm();
			}
		}
		return oldTerm;
	}

} //ScalarDualVectorProdFunction
