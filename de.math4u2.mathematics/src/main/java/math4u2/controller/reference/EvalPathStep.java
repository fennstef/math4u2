package math4u2.controller.reference;

import math4u2.mathematics.functions.MathException;

/**
 * Evaluierung einer Funktion g(x) usw.
 * 
 * @author Fenn Stefan
 */
public class EvalPathStep extends AbstractPathStep {
	
	public EvalPathStep(){
	}

	/**
	 * Einfacher Methodenname
	 */
	protected String getBodyString() throws MathException {
		return "";
	} //getBodyString
	
	public PathStep getEmptyClone() {
		return new EvalPathStep();
	}//getEmptyClone

} //class EvalPathStep
