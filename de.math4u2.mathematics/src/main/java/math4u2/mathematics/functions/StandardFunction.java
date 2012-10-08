package math4u2.mathematics.functions;

import java.util.Set;

import math4u2.controller.MathObject;

public abstract class StandardFunction extends Function {


	protected  String summaryText = "Beschreibung nicht vorhanden";
	
	
	/**
	 * Eine Standardfunktion kann nicht redefiniert werden, d.h. durch eine
	 * andere Definition ersetzt werden. Der Aufruf von test
	 * 
	 * @param oldObject
	 * @param oldAggregateSet
	 * @return false
	 */
	public boolean testSubstitution(MathObject oldObject, Set oldAggregateSet) {
		return false;
	}

	/**
	 * Eine Standardfunktion kann nicht redefiniert werden.
	 * 
	 * @param oldObject
	 *            Die Funktion, die ersetzt werden soll.
	 * @return
	 */
	public MathObject constructSubstitution(MathObject oldObject)
			throws Exception {
		throw new Exception("Redefinition der Standard-Funktion "
				+ (String) (oldObject.getKey()) + " nicht möglich");
	}

	/**
	 * @see math4u2.controller.MathObject#swapLinks(math4u2.controller.MathObject,
	 *      math4u2.controller.MathObject)
	 */
	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
	}//swapLinks

	
	public String getSummaryText() {
		return summaryText;
	}
	
	public String[] getArgumentStrings(){
		String[] arguments = new String[getArity()];
		if ( arguments.length == 1) 
			arguments[0]= "Argument";
		else {
			for (int i = 1; i <= arguments.length; i++) {
				arguments[i-1] = "Argument " + i; 
			}
		}
		return arguments;
	}
	
	public String[] getArgumentTexts(){
		String[] arguments = new String[getArity()];
		for (int i = 1; i <= arguments.length; i++) {
			arguments[i-1] = null; 
		}
		return arguments;
	}
	
	
} //StandardFunction
