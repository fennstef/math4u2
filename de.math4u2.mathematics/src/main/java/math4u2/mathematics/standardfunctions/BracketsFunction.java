package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UnaryStandardFunction;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.termnodes.ScalarVariable;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeNum;

/**
 * <p>
 * Klammer
 * </p>
 * <p>
 * Dient dazu, Klammern, die vom Benutzer eingegeben wurden, im Termbaum
 * abzuspeichern. In jeder anderen Beziehung ist diese Funktion die Identität.
 * </p>
 * <p>
 * Copyright (c) 2002
 * </p>
 * <p>
 * Organisation: FHA
 * </p>
 * 
 * @version 1.0
 */
public class BracketsFunction extends UnaryStandardFunction {

	public BracketsFunction() {
		name = "brackets";
	}//Konstruktor 1

	public Class getResultType(Class[] argTypes) {
		return argTypes[0];
	}

	public Class getVariableType(int pos) throws MathException {
		if (pos == 0)
			return Object.class;
		throw new MathException("Funktion " + name
				+ " hat keine Variable an der Stelle " + pos);
	}

	/**
	 * Setzt argStrings[0] in runde Klammern und gibt das Ergebnis zurueck.
	 * 
	 * @param argStrings
	 * @return Kalmmerausdruck
	 */
	public String buildTermString(String[] argStrings, String n) {
		return "(" + argStrings[0] + ")";
	}

	/**
	 * Gibt args[0] zurück.
	 */
	public Object eval(Object[] args) throws MathException {
		return args[0];
	} //eval

	/**
	 * Die Ableitung ist 1.
	 * 
	 * @param broker
	 * @return Die 1-Funktion.
	 */
	public Function getDeriveFunction(Broker broker) {
		ScalarVariable x = new ScalarVariable("x");
		return new UserFunction("", new TermNodeNum(1.0),
				new ScalarVariable[] { x }, broker, getViewFactory());
	} // getDeriveFunction

	/**
	 * Der Ableitungsterm ist hier derivedArguments[0].
	 * 
	 * @param arguementTerms
	 * @param derivedArgumentTerms
	 * @param broker
	 * @return derivedArguments[0]
	 */
	public TermNode buildDeriveTerm(TermNode[] arguementTerms,
			TermNode[] derivedArgumentTerms, Broker broker) {

		return derivedArgumentTerms[0];
	}

	public TermNode simplify(TermNode oldTerm, TermNode[] arguments, Broker broker)
			throws MathException {
		return arguments[0];
	}

}//BracketsFunction
