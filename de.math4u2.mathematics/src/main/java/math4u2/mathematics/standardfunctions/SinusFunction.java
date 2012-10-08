package math4u2.mathematics.standardfunctions;

import math4u2.controller.*;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.ScalarUnaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;

/**
 * <p>
 * Sinus-Funktion
 * </p>
 * 
 * @version 1.0
 */
public class SinusFunction extends ScalarUnaryStandardFunction {

	public SinusFunction() {
		name = "sin";
		summaryText = "Sinus-Funktion";
	}//Konstruktor 1

	/**
	 * Gibt den errechneten Funktionswert zurueck
	 */
	public Object eval(Object[] args) throws MathException {
		return new ScalarDoubleResult(Math
				.sin(((ScalarDoubleResult) args[0]).value));
	}//eval

	/**
	 * Ableitungsfunktion zu sin(x)
	 * 
	 * @param broker
	 * @return cos(x)
	 */
	public Function getDeriveFunction(Broker broker) throws MathException {
		try {
			return (Function) broker.getObject("cos");
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
	}
	
	public String[] getArgumentTexts() {
		return new String[] {"Winkel im Bogenmaﬂ"};
	}

}//SinusFunction
