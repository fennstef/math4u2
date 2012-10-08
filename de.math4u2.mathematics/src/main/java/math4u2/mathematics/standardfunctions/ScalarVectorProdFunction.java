package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.termnodes.TermNodeNum;
import math4u2.mathematics.termnodes.TermNodeVector;
import math4u2.mathematics.types.ScalarType;
import math4u2.mathematics.types.VectorType;

/**
 * <p>
 * Produkt eines Skalars (erster Faktor) mit einem Vektor (zweiter Faktor).
 * </p>
 */

public class ScalarVectorProdFunction extends ScalarMatrixProdFunction {

	public ScalarVectorProdFunction() {
		name = "ScalarVectorProd";
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
			return VectorType.class;

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		}
	}

	public Object eval(Object[] args) throws MathException {
		double scalar;
		VectorDoubleResult vector;

		// Pr�fen, ob genau zwei Argumente ??
		// Pr�fen, ob richtiger Typ?

		if (args[0] instanceof ScalarDoubleResult
				&& args[1] instanceof VectorDoubleResult) {

			scalar = ((ScalarDoubleResult) args[0]).value;
			vector = (VectorDoubleResult) args[1];
		} else
			throw new MathException(
					"Falsche Argumentreihenfolge bei ScalarVectorProductFunction");

		int rowDim = vector.rowDim;
		double[][] valueArray = vector.valueArray;
		double[][] resultArray = new double[rowDim][1];

		for (int r = 0; r < rowDim; r++) {
			resultArray[r][0] = valueArray[r][0] * scalar;
		}
		return new VectorDoubleResult(resultArray);
	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		try {
			TermNode s1 = new TermNodeFunct((Function) (broker
					.getObject("ScalarVectorProd")), new TermNode[] {
					derivedArgumentTerms[0], argumentTerms[1] }, broker);
			TermNode s2 = new TermNodeFunct((Function) (broker
					.getObject("ScalarVectorProd")), new TermNode[] {
					argumentTerms[0], derivedArgumentTerms[1] }, broker);

			return new TermNodeFunct(
					(Function) (broker.getObject("VectorSum")), new TermNode[] {
							s1, s2 }, broker);
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
					&& arguments[1] instanceof TermNodeVector) {
				/*
				 * return TermNodeVector.getNullVector(
				 * ((TermNodeVector)arguments[1]).getRowDim());
				 */
				return ((TermNodeVector) arguments[1]).getNullTerm();
			}
		}
		return oldTerm;
	}

} //ScalarVectorProdFunction
