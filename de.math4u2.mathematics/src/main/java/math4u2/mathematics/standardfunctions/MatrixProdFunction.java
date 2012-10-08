package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MatrixInfixStandardFunction;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.types.DualVectorType;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.ScalarType;
import math4u2.mathematics.types.VectorType;

public class MatrixProdFunction extends MatrixInfixStandardFunction {

	public MatrixProdFunction() {
		name = "MatrixProd";
		operatorName = "*";
		operatorNiceName = "\u200A\u00B7\u200A";
	}

	public Class getResultType(Class[] argTypes) {
		if (MatrixType.class.isAssignableFrom(argTypes[0])
				&& VectorType.class.isAssignableFrom(argTypes[1]))
			return argTypes[1];

		if (DualVectorType.class.isAssignableFrom(argTypes[0])
				&& MatrixType.class.isAssignableFrom(argTypes[1]))
			return argTypes[0];

		if (DualVectorType.class.isAssignableFrom(argTypes[0])
				&& VectorType.class.isAssignableFrom(argTypes[1]))
			return ScalarType.class;
		//else
		return MatrixType.class;
	}

	public Object eval(Object[] args) throws MathException {

		// Prüfen, ob genau zwei Argumente ??
		// Prüfen, ob beide Argumente Matrizen ??

		int rowDim0 = ((MatrixDoubleResult) args[0]).rowDim;
		int colDim0 = ((MatrixDoubleResult) args[0]).colDim;
		int rowDim1 = ((MatrixDoubleResult) args[1]).rowDim;
		int colDim1 = ((MatrixDoubleResult) args[1]).colDim;

		// Pruefen, ob Matrix-Produkt moeglich
		if (colDim0 != rowDim1)
			throw new MathException(
					"Falsche Zeilen/Spaltenzahl bei Matrixprodukt");

		double[][] prodValueArray = new double[rowDim0][colDim1];
		double[][] fak0 = ((MatrixDoubleResult) args[0]).valueArray;
		double[][] fak1 = ((MatrixDoubleResult) args[1]).valueArray;

		for (int r = 0; r < rowDim0; r++) {
			for (int c = 0; c < colDim1; c++) {
				double val = 0;
				for (int i = 0; i < colDim0; i++)
					val += fak0[r][i] * fak1[i][c];

				prodValueArray[r][c] = val;
			}
		}
		return new MatrixDoubleResult(prodValueArray);
	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		try {
			TermNode s1 = new TermNodeFunct((Function) (broker
					.getObject("MatrixProd")), new TermNode[] {
					derivedArgumentTerms[0], argumentTerms[1] }, broker);
			TermNode s2 = new TermNodeFunct((Function) (broker
					.getObject("MatrixProd")), new TermNode[] {
					argumentTerms[0], derivedArgumentTerms[1] }, broker);

			return new TermNodeFunct(
					(Function) (broker.getObject("MatrixSum")), new TermNode[] {
							s1, s2 }, broker);
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
	}

	public TermNode simplify(TermNode oldTerm, TermNode[] arguments) {
		return oldTerm;
	}

} //MatrixProdFunction
