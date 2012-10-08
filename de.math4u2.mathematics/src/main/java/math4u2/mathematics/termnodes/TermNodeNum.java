package math4u2.mathematics.termnodes;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.types.ScalarType;
import math4u2.view.Formatierer;

/**
 * Die Klasse TermNodeNum repraesentiert eine (feste) Zahl im TermBaum.
 */

public class TermNodeNum extends TermNode {
	private double value;

	/***************************************************************************
	 * Konstruktor
	 */
	public TermNodeNum(double val) {
		hasVar = false;
		currentResult = new ScalarDoubleResult(val);
		value = val;
	}

	public Class getResultType() {
		return ScalarType.class;
	}

	public TermNode getNullTerm() throws MathException {
		return new TermNodeNum(0.0);
	}

	/***************************************************************************
	 * Gibt die Zahl zurueck
	 */
	public double evalScalar() {
		return value;
	}

	/***************************************************************************
	 * Gibt die Zahl zurueck
	 */
	public Object eval() {
		return new ScalarDoubleResult(value);
	}
	
	
	/***************************************************************************
	 * Gibt die Zahl zurueck
	 */
	public Object shallowEval() {
		return new ScalarDoubleResult(value);
	}

	/***************************************************************************
	 * Die Ableitung eine Zahl ist 0.
	 * 
	 * @return 0 ( new TermNodeNum(0.0) ).
	 */
	public TermNode derive(Variable var) {
		return new TermNodeNum(0.0);
	}

	/***************************************************************************
	 * Weist eine neue Zahl zu
	 */
	public void setValue(double val) {
		this.value = val;
		currentResult = new ScalarDoubleResult(val);
	}

	/***************************************************************************
	 * Gibt die Zahl als String zurueck
	 */
	public String getTermString(MathObject parent) {
		// keine unnoetigen Stellen nach dem Komma
		if (value % 1 == 0)
			return Formatierer.value2String(value);
		else
			return Formatierer.value2String(value);
	}

	/**
	 * ************************************************************************d
	 * Bei einer Zahl gibt es keine Variablen zu ersetzen.
	 * 
	 * @param vars
	 * @param terms
	 * @return Der Knoten selbst.
	 * @throws Exception
	 */
	public TermNode substitute(Variable[] vars, TermNode[] terms)
			throws Exception {
		return this;
	}

	/***************************************************************************
	 * Hier ist nichts zu expandieren.
	 * 
	 * @return Der Knoten selbst.
	 */
	public TermNode expand() {
		return this;
	}

	/***************************************************************************
	 * Liefert einen neuen Konten mit dem aktuellen Wert von value.
	 * 
	 * @return Referenz auf sich
	 */
	public TermNode getClone(Variable[] oldVars, Variable[] newVars)
			throws Exception {
		return new TermNodeNum(value);
	}
} //class
