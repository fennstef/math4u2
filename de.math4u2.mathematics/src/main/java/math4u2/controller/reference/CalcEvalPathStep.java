package math4u2.controller.reference;

import math4u2.mathematics.functions.MathException;

/**
 * Evaluierung einer Funktion g(x) usw.
 * 
 * @author Fenn Stefan
 */
public class CalcEvalPathStep extends EvalPathStep {
	
	public CalcEvalPathStep(){
	}

	/**
	 * Einfacher Methodenname
	 */
	protected String getBodyString() throws MathException {
		return "";
	} //getBodyString
	
	public PathStep getEmptyClone() {
		return new CalcEvalPathStep();
	}//getEmptyClone

} //class CalcEvalPathStep
