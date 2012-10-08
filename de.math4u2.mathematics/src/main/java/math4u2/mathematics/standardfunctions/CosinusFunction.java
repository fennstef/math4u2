package math4u2.mathematics.standardfunctions;

import math4u2.controller.*;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.ScalarUnaryStandardFunction;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.ScalarVariable;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;

/**
 * <p>
 * Cosinus-Funktion
 * </p>
 * 
 * @version 1.0
 */
public class CosinusFunction extends ScalarUnaryStandardFunction {

	public CosinusFunction() {
		name = "cos";
		summaryText = "Cosinus-Funktion";
	}//Konstruktor 1

	/**
	 * Gibt den errechneten Funktionswert zurueck
	 */
	public Object eval(Object[] args) throws MathException {
		return new ScalarDoubleResult(Math
				.cos(((ScalarDoubleResult) args[0]).value));
	} //eval

	/**
	 * Ableitungsfunktion zu cos(x)
	 * 
	 * @param broker
	 * @return -sin(x)
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		ScalarVariable x = new ScalarVariable("x");
		return new UserFunction("", new TermNodeFunct((Function) (broker
				.getObject("-")), new TermNode[] { new TermNodeFunct(
				(Function) (broker.getObject("sin")), new TermNode[] { x },
				broker) }, broker), new ScalarVariable[] { x }, broker, getViewFactory());
	} // getDeriveFunction

	public String[] getArgumentTexts() {
		return new String[] {"Winkel im Bogenmaﬂ"};
	}
	
	

}//CosinusFunction
