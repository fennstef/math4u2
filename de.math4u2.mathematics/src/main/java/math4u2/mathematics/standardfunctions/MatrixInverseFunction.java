package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MatrixUnaryStandardFunction;
import math4u2.mathematics.numerics.LinearAlgorithms;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.termnodes.TermNode;

public class MatrixInverseFunction extends MatrixUnaryStandardFunction {

	public MatrixInverseFunction() {
		name = "inverse";
		summaryText = "Inverse einer quadratischen Matrix";
	}

	/**
	 * 
	 * @param args
	 *            args[0] enthaelt als Element vom Typ MatrixDoubleResult die zu
	 *            invertierende Matrix. Diese Matrix muss quadratisch und
	 *            regulär sein.
	 * @return Inverse Matrix
	 * @throws MathException :
	 *             wenn die Matrix nicht quadratisch ist oder wenn sie nicht
	 *             invertierbar ist.
	 */
	public Object eval(Object[] args) throws MathException {
		MatrixDoubleResult matrix = (MatrixDoubleResult) (args[0]);

		if (matrix.colDim != matrix.rowDim)
			throw new MathException("Funktion " + name
					+ " nicht definiert bei Matrix vom Typ " + matrix.rowDim
					+ " x " + matrix.colDim);

		double[][] matrixWork = new double[matrix.rowDim][matrix.colDim];

		for (int rowIndex = 0; rowIndex < matrix.rowDim; rowIndex++) {
			for (int colIndex = 0; colIndex < matrix.colDim; colIndex++) {
				matrixWork[rowIndex][colIndex] = matrix.valueArray[rowIndex][colIndex];
			}
		}

		double[][] result = LinearAlgorithms.inverse(matrix.rowDim, matrixWork);

		if (result == null)
			throw new MathException("Fehler bei Funktion " + name
					+ " : singulaere Matrix kann nicht invertiert werden");

		return new MatrixDoubleResult(result);
	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		throw new MathException("derive fuer inverse Matrix nicht definiert");
	}



} //MatrixInverseFunction
