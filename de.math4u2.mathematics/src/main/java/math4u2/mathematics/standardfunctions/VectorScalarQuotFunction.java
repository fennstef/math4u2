package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.types.ScalarType;
import math4u2.mathematics.types.VectorType;

/**
 * <p>
 * Quotient eines Vektors (erster Operand) durch einen Skalar (zweiter Operand).
 * </p>
 */

public class VectorScalarQuotFunction extends MatrixScalarQuotFunction {

	public VectorScalarQuotFunction() {
		name = "VectorScalarQuot";
		operatorName = "/";
		operatorNiceName = " / ";
	}

	public Class getResultType(Class[] argTypes) {
		return argTypes[0];
	}

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
			return VectorType.class;
		case 1:
			return ScalarType.class;
		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		}
	}

	public Object eval(Object[] args) throws MathException {
		double scalar;
		VectorDoubleResult vector;

		// Prüfen, ob genau zwei Argumente ??
		// Prüfen, ob richtiger Typ?

		if (args[1] instanceof ScalarDoubleResult
				&& args[0] instanceof VectorDoubleResult) {

			scalar = ((ScalarDoubleResult) args[1]).value;
			vector = (VectorDoubleResult) args[0];
		} else
			throw new MathException(
					"Falsche Argumentreihenfolge bei VectorScalarProductFunction");

		int rowDim = vector.rowDim;
		double[][] valueArray = vector.valueArray;
		double[][] resultArray = new double[rowDim][1];

		for (int r = 0; r < rowDim; r++) {
			resultArray[r][0] = valueArray[r][0] / scalar;
		}
		return new VectorDoubleResult(resultArray);
	} //eval

	/**
	 * Konstruiert den Ableitungsterm zur Quotientenregel zu Vektor V(x) und
	 * Skalar g(x): (V(x)/g(x))' = (V'(x)*g(x) - V(x)*g(x))/(g(x)^2) aus den
	 * Termen und den Ableitungstermen.
	 * 
	 * @param arguementTerms
	 *            Array aus V(x) und g(x)
	 * @param derivedArgumentTerms
	 *            Array aus V'(x) und g'(x)
	 * @param broker
	 * @return (V'(x)*g(x) - V(x)*g(x))/(g(x)^2)
	 */
	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		try {
			TermNode s1 = new TermNodeFunct((Function) (broker
					.getObject("VectorScalarProd")), new TermNode[] {
					derivedArgumentTerms[0], argumentTerms[1] }, broker);
			TermNode s2 = new TermNodeFunct((Function) (broker
					.getObject("VectorScalarProd")), new TermNode[] {
					derivedArgumentTerms[1], argumentTerms[0] }, broker);
			TermNode num = new TermNodeFunct((Function) (broker
					.getObject("VectorDiff")), new TermNode[] { s1, s2 },
					broker);
			TermNode denom = new TermNodeFunct((Function) (broker
					.getObject("prod")), new TermNode[] { argumentTerms[1],
					argumentTerms[1] }, broker);
			return new TermNodeFunct((Function) (broker
					.getObject("VectorScalarQuot")), new TermNode[] { num,
					denom }, broker);
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
	}



}//VectorScalarQuotFunction

