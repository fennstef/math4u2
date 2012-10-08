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
import math4u2.mathematics.types.ScalarType;
import math4u2.mathematics.types.VectorType;

public class ScalarProdFunction extends MiscInfixStandardFunction {

	public ScalarProdFunction() {
		name = "ScalarProd";
		operatorName = "<*>";
		operatorNiceName = "\u200A<\u00B7>\u200A";
	}

	public Class getResultType(Class[] argTypes) {
		return ScalarType.class;
	}

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
			return VectorType.class;
		case 1:
			return VectorType.class;

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		}
	}

	public Object eval(Object[] args) throws MathException {

		// Prüfen, ob genau zwei Argumente ??
		// Prüfen, ob beide Argumente Matrizen ??

		int rowDim0 = ((MatrixDoubleResult) args[0]).rowDim;
		//int colDim0 = ((MatrixDoubleResult)args[0]).colDim;
		int rowDim1 = ((MatrixDoubleResult) args[1]).rowDim;
		//int colDim1 = ((MatrixDoubleResult)args[1]).colDim;

		// Pruefen, ob Matrix-Produkt moeglich
		if (rowDim0 != rowDim1)
			throw new MathException(
					"Unterschiedliche Zeilenzahl bei Skalarprodukt");

		double[][] fak0 = ((MatrixDoubleResult) args[0]).valueArray;
		double[][] fak1 = ((MatrixDoubleResult) args[1]).valueArray;
		double result = 0;

		for (int r = 0; r < rowDim0; r++) {
			result += fak0[r][0] * fak1[r][0];
		}
		return new ScalarDoubleResult(result);
	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		try {
			TermNode s1 = new TermNodeFunct((Function) (broker
					.getObject("ScalarProd")), new TermNode[] {
					derivedArgumentTerms[0], argumentTerms[1] }, broker);
			TermNode s2 = new TermNodeFunct((Function) (broker
					.getObject("ScalarProd")), new TermNode[] {
					argumentTerms[0], derivedArgumentTerms[1] }, broker);

			return new TermNodeFunct((Function) (broker.getObject("sum")),
					new TermNode[] { s1, s2 }, broker);
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
	}

	public TermNode simplify(TermNode oldTerm, TermNode[] arguments) {

		return oldTerm;
	}

}//ScalarProdFunction

