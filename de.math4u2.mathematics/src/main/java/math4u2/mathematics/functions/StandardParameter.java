package math4u2.mathematics.functions;

import math4u2.controller.Broker;
import math4u2.mathematics.results.*;
import math4u2.mathematics.termnodes.*;
import math4u2.mathematics.types.ScalarType;

/**
 * <p>
 * Konstanten
 * </p>
 * t
 * 
 * @version 1.0
 */
public abstract class StandardParameter extends StandardFunction {

	public Class getResultType(Class[] argTypes) {
		return ScalarType.class;
	}

	// End: Statische Daten und Methoden

	/**
	 * Haelt den Wert der Konstante. Muss vom Konstruktor der abgeleiteten
	 * Klassen initialisiert werden.
	 */
	protected double value;

	public StandardParameter() {
	}

	public Object eval(Object[] v) {
		return new ScalarDoubleResult(value);
	}

	/**
	 * Die Ableitung einer Konstanten ist immer 0.
	 * 
	 * @param argumentTerms
	 * @param derivedArgumentTerms
	 * @param broker
	 * @return 0-Konten.
	 */
	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker) {

		return new TermNodeNum(0.0);
	}

	/**
	 * Stelligkeit ist 0.
	 * 
	 * @return 0
	 */
	public int getArity() {
		return 0;
	}

	public Class getVariableType(int pos) throws MathException {
		throw new MathException("Funktion " + name
				+ " hat keine Variable an der Stelle " + pos);
	}

	/**
	 * Parameter sind nullstellige Funktionen. Für die Variablennamen wir
	 * deshalb ein Arra der Länge 0 zurückgegeben.
	 * 
	 * @return Array der Laenge 0.
	 */
	public String[] getVariableNames() {
		return new String[] {};
	}

	/**
	 * Der Term-String besteht aus dem Namen des Parameters.
	 * 
	 * @param terms
	 *            Wird in diesem Fall (nullstellige Funktion) nicht verwendet.
	 * @return Name des Parameters.
	 */
	public String buildTermString(String[] terms) {
		return (String) getKey();
	}
}