package math4u2.mathematics.termnodes;

import java.util.AbstractSet;
import java.util.Set;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.ScalarDoubleResult;

/*******************************************************************************
 * Repraesentiert einen Knoten in einem Termbaum.
 */
public abstract class TermNode {
	protected boolean hasVar = true;

	protected Object currentResult = null;

	public boolean hasVar() {
		return hasVar;
	}

	/***************************************************************************
	 * Berechnet den Wert des Terms, der den aktuellen Knoten als Wurzel hat.
	 * 
	 * @return Ergebnis 
	 */
	public abstract Object eval() throws MathException;

	public Object shallowEval() throws MathException {
		if (!hasVar)
			return currentResult;
		return eval();
	}

	public abstract Class getResultType();

	/**
	 * Gibt zu einem Term ohne Variable einen Term ohne Variable zurück, - der
	 * vom gleichen Typ ist, wie der aktuelle Term ( Skalar, 3x3-Matrix, Vektor
	 * mit 4 Zeilen, DualVektor mit 5 Spalten) und - bei dem nach eval alle
	 * Elemente den Wert 0 haben. Wenn der aktuelle Term eine Variable ist oder
	 * enthält, wird eine Ausnahme erzeugt.
	 */
	public abstract TermNode getNullTerm() throws MathException;

	public double evalScalar() throws MathException {
		return ((ScalarDoubleResult) eval()).value;
	}
	
	public boolean hasResult0() {
		return this instanceof TermNodeNum
		       && ((TermNodeNum) this).evalScalar() == 0.0 ;
	}
	
	public boolean hasResult1() {
		return this instanceof TermNodeNum
		       && ((TermNodeNum) this).evalScalar() == 1.0 ;
	}
	
	public boolean hasResultMinus1() {
		return this instanceof TermNodeNum
		       && ((TermNodeNum) this).evalScalar() == -1.0 ;
	}

	public double shallowEvalScalar() throws MathException {
		return ((ScalarDoubleResult) (shallowEval())).value;
	}

	public void swapLinks(MathObject oldLink, MathObject newLink) {
	}

	public void insertParts(AbstractSet s) {
		return;
	}

	/***************************************************************************
	 * Erzeugt die String-Darstelung des Terms.
	 * 
	 * @return Stringrepraesentation
	 */
	/***************************************************************************
	 * public abstract String getTermString(MathObject parent);
	 **************************************************************************/

	public abstract String getTermString(MathObject parent);

	public String getTermString() {
		return getTermString(null);
	}

	/***************************************************************************
	 * Vereinfacht den aktuellen Knoten soweit moeglich.
	 * 
	 * @return Der vereinfachte TermNode
	 */
	public TermNode simplify() throws Exception {
		return this;
	}

	public TermNode evalNum() throws Exception {
		return this;
	}

	/**
	 * Liefert true, wenn der Term irgendeine Variable enthaelt. Wird von den
	 * Klassen, die Variablen enthalten koennen, ueberschrieben.
	 * 
	 * @return
	 */

	public boolean containsAnyVar() {
		return false;
	}

	/***************************************************************************
	 * Wenn der Term die Variabel var enthält, ist das Ergebnis true, ansonsten
	 * false. Wird von den Klassen, die Variablen enthalten können,
	 * überschrieben.
	 */
	boolean containsVar(Variable var) {
		return false;
	}

	/**
	 * Fuegt alle Funktionen zu indexFunctions hinzu, die in einem Index (Liste,
	 * Matrix, Vektor) des aktuellen Knotens und seines Teilbaums verwendet
	 * werden. Muss von den abgeleiteten Klassen ueberschrieben werden, in denen
	 * tatsaechlich Indices verwendet werden.
	 * 
	 * @param indexFunctionSet
	 *            Menge, in der die in Indices verwendeten Funktionen gesammelt
	 *            werden.
	 */
	public void insertIndexFunctions(Set indexFunctionSet) {
	}

	/**
	 * Fuegt alle Funktionen zu functionSet hinzu, die im aktuellen Knotens und
	 * seinem Teilbaums verwendet werden. Muss von den abgeleiteten Klassen
	 * ueberschrieben werden, in denen tatsaechlich Funktionen verwendet werden.
	 * 
	 * @param functionSet
	 *            Menge, in der die verwendeten Funktionen gesammelt werden.
	 */
	public void insertAllFunctions(Set functionSet) {
	}

	/***************************************************************************
	 * Gibt den Term zurück, der entsteht, wenn man den aktuellen Term nach der
	 * Variable var ableitet.
	 */
	public abstract TermNode derive(Variable var) throws MathException;

	/***************************************************************************
	 * Expandiert den Term, der den aktuellen Term als Wurzel hat, d.h. ersetzt
	 * alle Aufrufe von benutzerdefinierten Funktionen durch den entsprechenden
	 * Definitionsterm.
	 * 
	 * @return Expandierter Term
	 * @throws Exception
	 */
	public abstract TermNode expand() throws Exception;

	/***************************************************************************
	 * Ersetzt im Term, der den aktuellen Knoten als Wurzel hat, alle Variablen
	 * aus vars durch den entsprechenden Term aus Terms. Genauer: Die Variable
	 * vars[i] wird durch den Term terms[i] ersetzt. Der so entstehende Term
	 * wird zurückgegeben.
	 * 
	 * @param vars
	 *            Zu ersetzende Variablen.
	 * @param terms
	 *            Terme, die die Variablen ersetzen.
	 * @return Term nach den entsprechenden Ersetzungen.
	 * @throws Exception
	 */
	public abstract TermNode substitute(Variable[] vars, TermNode[] terms)
			throws Exception;

	/***************************************************************************
	 * Liefert eine Kopie des Terms, der den aktuellen Term als Wurzel hat.
	 * Dabei werden alle Vorkommen von Variablen aus oldVars durch die
	 * entsprechende Variable aus newVars ersetzt. Genauer: jede Variable
	 * oldVars[i] wird durch die Variable newVars[i] ersetzt.
	 * 
	 * @param oldVars
	 *            Zu ersetzende Variablen
	 * @param newVars
	 *            Neue Variablen
	 * @return Kopie
	 * @throws Exception
	 */
	public abstract TermNode getClone(Variable[] oldVars, Variable[] newVars)
			throws Exception;

}