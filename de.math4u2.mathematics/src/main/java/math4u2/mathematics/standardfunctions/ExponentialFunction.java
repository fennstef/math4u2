package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.ScalarUnaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;

/**
 * <p>
 * Exponentialfunktion zur Basis e
 * </p>
 */
public class ExponentialFunction extends ScalarUnaryStandardFunction {

	
	public ExponentialFunction() {
		name = "exp";
		summaryText = "Exponantialfunktion zur Basis e";
	} //Konstruktor 1

	/**
	 * Gibt den errechneten Funktionswert zurueck
	 */
	public Object eval(Object[] args) throws MathException {
		return new ScalarDoubleResult(Math
				.exp(((ScalarDoubleResult) args[0]).value));
	} //eval

	/**
	 * Die Ableitung von exp(x) ist die Funktion selber.
	 * 
	 * @param broker
	 * @return exp(x)
	 */
	public Function getDeriveFunction(Broker broker) {
		return this;
	}


	
	
	

} //ExponentialFunction
