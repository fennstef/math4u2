package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MatrixUnaryStandardFunction;
import math4u2.mathematics.results.DoubleResult;
import math4u2.mathematics.results.DualVectorDoubleResult;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.types.DualVectorType;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.VectorType;

public class MatrixTransposeFunction extends MatrixUnaryStandardFunction {

	public MatrixTransposeFunction() {
		name = "transpose";
		summaryText = "Transponiert eine Matrix";
	}


	public Class getResultType(Class[] argTypes) {
		if ( VectorType.class == argTypes[0])
			return DualVectorType.class;
		else if ( DualVectorType.class == argTypes[0])
			return VectorType.class;	
		else return MatrixType.class;
	}
	
	
	/**
	 * Transponieren
	 * 
	 * @param args
	 *            args[0] enthaelt als Element vom Typ MatrixDoubleResult die zu
	 *            transponierende Matrix.
	 * @return Transponierte Matrix
	 */
	public Object eval(Object[] args) {
		MatrixDoubleResult matrix = (MatrixDoubleResult) (args[0]);

		double[][] elements = matrix.valueArray;
		double[][] result = new double[matrix.colDim][matrix.rowDim];

		for (int row = 0; row < matrix.rowDim; row++) {
			for (int col = 0; col < matrix.colDim; col++) {
				result[col][row] = elements[row][col];
			}
		}
		if ( ((DoubleResult)args[0]).getDataType() == VectorType.class)
			return new DualVectorDoubleResult(result);
		else if ( ((DoubleResult)args[0]).getDataType() == DualVectorType.class)
			return new VectorDoubleResult(result);
		else return new MatrixDoubleResult(result);
	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		throw new MathException(
				"derive fuer transponierte Matrix nicht definiert");
	}

	public TermNode simplify(TermNode oldTerm, TermNode[] arguments) {
		return oldTerm;
	}

}//MatrixTransposeFunction
