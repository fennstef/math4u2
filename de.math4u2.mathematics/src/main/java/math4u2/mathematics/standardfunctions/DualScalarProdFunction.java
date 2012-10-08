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
import math4u2.mathematics.types.DualVectorType;
import math4u2.mathematics.types.ScalarType;

public class DualScalarProdFunction extends MiscInfixStandardFunction {

	public DualScalarProdFunction() {

		name = "DualScalarProd";

		operatorName = "<*>";

		operatorNiceName = "\u200A<\u00B7>\u200A";

	}

	public Class getResultType(Class[] argTypes) {

		return ScalarType.class;

	}

	public Class getVariableType(int pos) throws MathException {

		switch (pos) {

		case 0:
			return DualVectorType.class;

		case 1:
			return DualVectorType.class;

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);

		}

	}

	public Object eval(Object[] args) throws MathException {

		// Prüfen, ob genau zwei Argumente ??

		// Prüfen, ob beide Argumente Matrizen ??

		int colDim0 = ((MatrixDoubleResult) args[0]).colDim;

		int colDim1 = ((MatrixDoubleResult) args[1]).colDim;

		if (colDim0 != colDim1)

			throw new MathException(
					"Unterschiedliche Zeilenzahl bei Skalarprodukt");

		double[][] fak0 = ((MatrixDoubleResult) args[0]).valueArray;

		double[][] fak1 = ((MatrixDoubleResult) args[1]).valueArray;

		double result = 0;

		for (int r = 0; r < colDim0; r++) {

			result += fak0[0][r] * fak1[0][r];

		}

		return new ScalarDoubleResult(result);

	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,

	TermNode[] derivedArgumentTerms,

	Broker broker)

	throws MathException {

		try {

			TermNode s1 = new TermNodeFunct((Function) (broker
					.getObject("DualScalarProd")),

			new TermNode[] {

			derivedArgumentTerms[0],

			argumentTerms[1]

			},

			broker);

			TermNode s2 = new TermNodeFunct((Function) (broker
					.getObject("DualScalarProd")),

			new TermNode[] {

			argumentTerms[0],

			derivedArgumentTerms[1]

			},

			broker);

			return new TermNodeFunct((Function) (broker.getObject("sum")),

			new TermNode[] {

			s1, s2

			},

			broker);

		} catch (BrokerException e) {

			throw new MathException(e.getMessage());

		}

	}

	

}//DualVectorScalarProdFunction

