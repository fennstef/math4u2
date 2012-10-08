package math4u2.mathematics.standardfunctions;

import math4u2.controller.*;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.ScalarUnaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;

/**
 * <p>
 * Ceil-Funktion
 * </p>
 * Liefert die kleinste ganze Zahl, die nicht kleiner ist als das Argument.
 * 
 * @version 1.0
 */
public class CeilFunction extends ScalarUnaryStandardFunction {

	public CeilFunction() {
		name = "ceil";
		summaryText = "Rundet zur nächst-größere ganze Zahl auf, z.B.<br>ceil(3.2)=4<br>ceil(-7.6)=-7";
	}//Konstruktor 1

	/**
	 * Liefert die kleinste ganze Zahl, die nicht kleiner ist als arg[0].
	 */
	public Object eval(Object[] args) throws MathException {
		return new ScalarDoubleResult(Math
				.ceil(((ScalarDoubleResult) args[0]).value));
	} //eval

	/**
	 * Kann derzeit nicht adäquat dargestellt werden
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Ableitung der Funktion ceil nicht definiert");
	}

}//CeilFunction
