package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MatrixSumDiffStandardFunction;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;

public class MatrixSumFunction extends MatrixSumDiffStandardFunction {

	public MatrixSumFunction() {
		name = "MatrixSum";
		operatorName = "+";
		operatorNiceName = " + ";
	}

	double[][] evalArray(MatrixDoubleResult arg1, MatrixDoubleResult arg2) throws MathException {
		double[][] sumValueArray;
		int rowDim, colDim;
		// Prüfen, ob genau zwei Argumente ??
		// Prüfen, ob beide Argumente Matrizen ??

		rowDim = arg1.rowDim;
		colDim = arg1.colDim;

		// Prüfen, ob beide Matrizen die gleiche Dimension haben
		if (arg2.rowDim != rowDim)
			throw new MathException(
					"Unterschiedliche Zeilenzahl bei Summe von Matrizen oder Vektoren");
		if (arg2.colDim != colDim)
			throw new MathException(
					"Unterschiedliche Spaltenzahl bei Summe von Matrizen oder Zeilenvektoren");

		sumValueArray = new double[rowDim][colDim];
		double[][] sum1 = arg1.valueArray;
		double[][] sum2 = arg2.valueArray;

		for (int r = 0; r < rowDim; r++) {
			for (int c = 0; c < colDim; c++) {
				sumValueArray[r][c] = sum1[r][c] + sum2[r][c];
			}
		}
		return sumValueArray;
	}
	
	public Object eval(Object[] args) throws MathException {		
		return new MatrixDoubleResult(evalArray((MatrixDoubleResult)args[0], (MatrixDoubleResult)args[1]));
	} //eval

	public TermNode buildDeriveTerm(TermNode[] arguementTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {
		try {
			return new TermNodeFunct(
					(Function) (broker.getObject("MatrixSum")), new TermNode[] {
							derivedArgumentTerms[0], derivedArgumentTerms[1] },
					broker);
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
	} // buildDeriveTerm

	public TermNode simplify(TermNode oldTerm, TermNode[] arguments) {
		return oldTerm;
	}

} //MatrixSumFunction
