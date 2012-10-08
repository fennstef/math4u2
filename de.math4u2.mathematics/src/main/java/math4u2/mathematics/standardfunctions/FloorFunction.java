package math4u2.mathematics.standardfunctions;

import math4u2.controller.*;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.ScalarUnaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;

/**
 * <p>
 * Floor-Funktion
 * </p>
 * Liefert die gr��te ganze Zahl, die nicht gr��er ist als das Argument.
 * 
 * @version 1.0
 */
public class FloorFunction extends ScalarUnaryStandardFunction {

	public FloorFunction() {
		name = "floor";
		summaryText = "Rundet ab auf die n�chst-kleinere ganze Zahl, z.B.<br>floor(3.2)=3<br>floor(-7.6)=-8";
	}//Konstruktor 1

	/**
	 * Liefert die gr��te ganze Zahl, die nicht gr��er ist als arg[0].
	 */
	public Object eval(Object[] args) throws MathException {
		return new ScalarDoubleResult(Math
				.floor(((ScalarDoubleResult) args[0]).value));
	} //eval

	/**
	 * Kann derzeit nicht ad�quat dargestellt werden
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Ableitung der Funktion floor nicht definiert");
	}


	
	

}//FloorFunction
