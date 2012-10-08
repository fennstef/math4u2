package math4u2.mathematics.standardfunctions;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.VectorDoubleResult;

/**
 * <p>
 * Unaere Vector-Minus-Funktion -Vektor
 * </p>
 * 
 * @version 1.0
 */
public class VectorMinusFunction extends MatrixMinusFunction {

	public VectorMinusFunction() {
		name = "VectorMinus";
	} //Konstruktor 1

	
	public Object eval(Object[] args) throws MathException {		
		return new VectorDoubleResult(evalArray((VectorDoubleResult)args[0]));
	} //eval


} //VectorMinusFunction
