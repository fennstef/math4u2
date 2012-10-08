package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscInfixStandardFunction;
import math4u2.mathematics.results.*;
import math4u2.mathematics.termnodes.*;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.ScalarType;

/**
 * <p>
 * Produkt eines Skalars (erster Faktor) mit einer Matrix (zweiter Faktor).
 * </p>
 */

public class ScalarMatrixProdFunction extends MiscInfixStandardFunction {

	public ScalarMatrixProdFunction() {
		name = "ScalarMatrixProd";
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
			return MatrixType.class;

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

		if (args[0] instanceof ScalarDoubleResult
				&& args[1] instanceof MatrixDoubleResult) {

			scalar = ((ScalarDoubleResult) args[0]).value;
			matrix = (MatrixDoubleResult) args[1];
		} else
			throw new MathException(
					"Falsche Argumentreihenfolge bei ScalarMatrixProductFunction");

		int rowDim = matrix.rowDim;
		int colDim = matrix.colDim;
		double[][] valueArray = matrix.valueArray;
		double[][] resultArray = new double[rowDim][colDim];

		for (int r = 0; r < rowDim; r++) {
			for (int c = 0; c < colDim; c++) {
				resultArray[r][c] = valueArray[r][c] * scalar;
			}
		}
		return new MatrixDoubleResult(resultArray);
	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		try {
			TermNode s1 = new TermNodeFunct((Function) (broker
					.getObject("ScalarMatrixProd")), new TermNode[] {
					derivedArgumentTerms[0], argumentTerms[1] }, broker);
			TermNode s2 = new TermNodeFunct((Function) (broker
					.getObject("ScalarMatrixProd")), new TermNode[] {
					argumentTerms[0], derivedArgumentTerms[1] }, broker);

			return new TermNodeFunct(
					(Function) (broker.getObject("MatrixSum")), new TermNode[] {
							s1, s2 }, broker);
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
	}

	public TermNode simplify(TermNode oldTerm, TermNode[] arguments)
			throws MathException {
		if (arguments[0] instanceof TermNodeNum) {
			if (((TermNodeNum) arguments[0]).evalScalar() == 1.0)
				return arguments[1];

			if (((TermNodeNum) arguments[0]).evalScalar() == 0.0
					&& arguments[1] instanceof TermNodeMatrix) {
				/*
				 * return TermNodeMatrix.getNullMatrix(
				 * ((TermNodeMatrix)arguments[1]).getRowDim(),
				 * ((TermNodeMatrix)arguments[1]).getColDim());
				 */
				return ((TermNodeMatrix) arguments[1]).getNullTerm();
			}
		}
		return oldTerm;
	}

	/*
	 * @see math4u2.controller.MathObject#swapLinks(math4u2.controller.MathObject,
	 *      math4u2.controller.MathObject)
	 */
	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
	}

} //ScalarMatrixProdFunction
