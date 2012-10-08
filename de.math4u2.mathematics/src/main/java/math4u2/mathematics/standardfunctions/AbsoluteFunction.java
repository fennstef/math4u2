package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.ScalarUnaryStandardFunction;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.ScalarVariable;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.termnodes.TermNodeNum;

/**
 * <p>
 * Betrags-Funktion
 * </p>
 * 
 * @version 1.0
 */
public class AbsoluteFunction extends ScalarUnaryStandardFunction {

	public AbsoluteFunction() {
		name = "abs";
		summaryText = "Berechnet den Betrag des Arguments";
	}//Konstruktor 1

	public String buildTermString(String[] argStrings, String n) {
		return "|" + argStrings[0] + "|";
	}

	/**
	 * Gibt den errechneten Funktionswert zurueck
	 */
	public Object eval(Object[] args) throws MathException {
		return new ScalarDoubleResult(Math
				.abs(((ScalarDoubleResult) args[0]).value));
	} //eval

	/**
	 * Die Ableitung der Betragsfunktion ist 1 fuer x > 0 und -1 fuer x < 0.
	 * Diese Werte erhaelt man aus dem Term |x|/x .
	 * 
	 * @param broker
	 * @return Die Funktion |x|/x .
	 */


	public Function getDeriveFunction(Broker broker) throws Exception {
		ScalarVariable x = new ScalarVariable("x");
		
		TermNode temp = new TermNodeFunct((Function) (
        		broker.getObject("signum")), 
				new TermNode[] { x } ,
				broker);
		
		return new UserFunction("", 
				                new TermNodeFunct((Function) (
				                		broker.getObject("not_defined_at")), 
										new TermNode[] { temp, new TermNodeNum(0.0), new TermNodeNum(0.0) } ,
										broker),
				                new ScalarVariable[] { x }, 
								broker, getViewFactory());
	}

}//AbsoluteFunction

