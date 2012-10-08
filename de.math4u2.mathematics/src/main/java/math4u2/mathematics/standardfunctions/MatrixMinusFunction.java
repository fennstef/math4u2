package math4u2.mathematics.standardfunctions;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MatrixUnaryStandardFunction;
import math4u2.mathematics.results.DoubleResult;
import math4u2.mathematics.results.DualVectorDoubleResult;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.types.DualVectorType;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.VectorType;

/**
 * <p>
 * Unaere Matrix-Minus-Funktion -Matrix
 * </p>
 * 
 * @version 1.0
 */
public class MatrixMinusFunction extends MatrixUnaryStandardFunction {

	public MatrixMinusFunction() {
		name = "MatrixMinus";
	} //Konstruktor 1

	
	public Class getResultType(Class[] argTypes) {
		if ( VectorType.class == argTypes[0])
			return VectorType.class;
		else if ( DualVectorType.class == argTypes[0])
			return DualVectorType.class;	
		else return MatrixType.class;
	}
	
	
	double[][] evalArray(MatrixDoubleResult arg1) throws MathException {
		double[][] resultValueArray;
		int rowDim, colDim;

		rowDim = arg1.rowDim;
		colDim = arg1.colDim;

		resultValueArray = new double[rowDim][colDim];
		double[][] val = arg1.valueArray;

		for (int r = 0; r < rowDim; r++) {
			for (int c = 0; c < colDim; c++) {
				resultValueArray[r][c] = - val[r][c];
			}
		}
		return resultValueArray;
	}
	
	public Object eval(Object[] args) throws MathException {	
		if ( ((DoubleResult)args[0]).getDataType() == VectorType.class)
			return new VectorDoubleResult(evalArray((MatrixDoubleResult)args[0]));
		else if ( ((DoubleResult)args[0]).getDataType() == DualVectorType.class)
			return new DualVectorDoubleResult(evalArray((MatrixDoubleResult)args[0]));
		else return new MatrixDoubleResult(evalArray((MatrixDoubleResult)args[0]));
	} //eval

	/**
	 * Setzt vor argStrings[0] den Operator "-" .
	 */
	public String buildTermString(String[] argStrings, String name) {
		return "-" + argStrings[0];
	}

	

} //MatrixMinusFunction
