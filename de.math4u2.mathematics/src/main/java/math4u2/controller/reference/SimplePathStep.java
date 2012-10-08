package math4u2.controller.reference;

import math4u2.mathematics.functions.MathException;

/**
 * Wird für "normale" Methodenaufrufe benutzt. Z.B. bei k.mitte.x : "mitte" und
 * "x"
 * 
 * @author Fenn Stefan
 */
public class SimplePathStep extends AbstractPathStep {
	
	public SimplePathStep(){
	}

	/**
	 * Einfacher Methodenname
	 */
	protected String getBodyString() throws MathException {
		return "." + name;
	} //getBodyString

	public PathStep getEmptyClone() {
		return new SimplePathStep();
	}//getEmptyClone

} //class SimplePathStep
