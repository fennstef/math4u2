package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;

public class MatrixVectorProdFunction extends MatrixProdFunction {

	public MatrixVectorProdFunction() {
		name = "MatrixVectorProd";
		operatorName = "*";
		operatorNiceName = "\u200A\u00B7\u200A";
	}

	public Object eval(Object[] args) throws MathException {

		// Prüfen, ob genau zwei Argumente ??
		// Prüfen, ob beide Argumente Matrizen ??

		int rowDim0 = ((MatrixDoubleResult) args[0]).rowDim;
		int colDim0 = ((MatrixDoubleResult) args[0]).colDim;
		int rowDim1 = ((VectorDoubleResult) args[1]).rowDim;
		int colDim1 = 1;

		// Pruefen, ob Matrix-Produkt moeglich
		if (colDim0 != rowDim1)
			throw new MathException(
					"Falsche Zeilen/Spaltenzahl bei Matrixprodukt");

		double[][] prodValueArray = new double[rowDim0][colDim1];
		double[][] fak0 = ((MatrixDoubleResult) args[0]).valueArray;
		double[][] fak1 = ((MatrixDoubleResult) args[1]).valueArray;

		for (int r = 0; r < rowDim0; r++) {
			double val = 0;
			for (int i = 0; i < colDim0; i++)
				val += fak0[r][i] * fak1[i][0];

			prodValueArray[r][0] = val;
		}
		return new VectorDoubleResult(prodValueArray);
	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		try {
			TermNode s1 = new TermNodeFunct((Function) (broker
					.getObject("MatrixVectorProd")), new TermNode[] {
					derivedArgumentTerms[0], argumentTerms[1] }, broker);
			TermNode s2 = new TermNodeFunct((Function) (broker
					.getObject("MatrixVectorProd")), new TermNode[] {
					argumentTerms[0], derivedArgumentTerms[1] }, broker);

			return new TermNodeFunct(
					(Function) (broker.getObject("VectorSum")), new TermNode[] {
							s1, s2 }, broker);
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
	}

} //MatrixVectorProdFunction
