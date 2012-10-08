package math4u2.mathematics.standardfunctions;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.DualVectorDoubleResult;

/**
 * <p>
 * Unaere Dualvektor-Minus-Funktion -Dualvektor
 * </p>
 * 
 * @version 1.0
 */
public class DualVectorMinusFunction extends MatrixMinusFunction {

	public DualVectorMinusFunction() {
		name = "DualVectorMinus";
	} //Konstruktor 1
	
	
	public Object eval(Object[] args) throws MathException {		
		return new DualVectorDoubleResult(evalArray((DualVectorDoubleResult)args[0]));
	} //eval

	

} //DualVectorMinusFunction
