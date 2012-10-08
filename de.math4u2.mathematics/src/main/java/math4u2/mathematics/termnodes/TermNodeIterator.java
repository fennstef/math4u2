package math4u2.mathematics.termnodes;

import java.util.Set;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.DoubleResult;
import math4u2.mathematics.types.DualVectorType;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.ScalarType;
import math4u2.mathematics.types.VectorType;

public abstract class TermNodeIterator extends TermNode {

	// Term fuer die untere Grenze
	TermNode fromTerm;
	
	// Term fuer die obere Grenze
	TermNode toTerm;

	// Term, ueber den iteriert wird
	TermNode functionTerm;

	// Variable, die den Summationsindex darstellt
	ScalarVariable localVar;

	protected TermNodeIterator(ScalarVariable localVar, TermNode fromTerm, TermNode toTerm, TermNode function)
			throws MathException {

		this.localVar = localVar;
		this.fromTerm = fromTerm;
		this.toTerm = toTerm;
		this.functionTerm = function;
		this.hasVar = true;
	}
	
	public TermNode simplify() throws Exception {
		return makeIterator( this.getTypeString(), localVar, 
				             fromTerm.simplify(), toTerm.simplify(), functionTerm.simplify()) ;
	}

	/**
	 * Erstellt je nach Wert von typeString ( "sum" oder "prod" ) und dem Ergebnistyp von 
	 * function den passenden Iterator.
	 * Summen sind definiert, wenn das Einzelergebnis ein Skalar, Matrix, Vektor oder Dualvektor ist.
	 * Das Produkt ist nur fuer einen Skalar definiert. 
	 * In allen anderen Faellen wird eine Ausnahme erzeugt.
	 * @param typeString Typ des Iterators: "sum" oder "prod", ansonsten wird Ausnahme erzeugt.
	 * @param indexVar Variable, die den Index darstellt (Summationsindex, Produktindex)
	 * @param fromTerm Term, der die untere Grenze fuer den Index liefert.
	 * @param toTerm Term, der die obere Grenze fuer den Index liefert.
	 * @param function Term, der das Einzelergebnis fuer die Summe bzw. das Produkt liefert.
	 * @return
	 * @throws MathException
	 */
	public static TermNodeIterator makeIterator(String typeString, ScalarVariable indexVar,
			TermNode fromTerm, TermNode toTerm, TermNode function) throws MathException {
		if (typeString.compareTo("sum") == 0){
			// Reihenfolge entscheidend: zuerst Vektor und Dualvektor, dann Matrix
			if ( ScalarType.class.isAssignableFrom(function.getResultType())){
				return new TermNodeIteratorScalarSum(indexVar, fromTerm, toTerm, function);
			}
			if ( VectorType.class.isAssignableFrom(function.getResultType())){
				return new TermNodeIteratorVectorSum(indexVar, fromTerm, toTerm, function);
			}
			if ( DualVectorType.class.isAssignableFrom(function.getResultType())){
				return new TermNodeIteratorDualVectorSum(indexVar, fromTerm, toTerm, function);
			}
			if ( MatrixType.class.isAssignableFrom(function.getResultType())){
				return new TermNodeIteratorMatrixSum(indexVar, fromTerm, toTerm, function);
			}
		}	
		else {
			if (typeString.compareTo("prod") == 0){
				// Reihenfolge entscheidend: zuerst Vektor und Dualvektor, dann Matrix
				if ( ScalarType.class.isAssignableFrom(function.getResultType())) {
					return new TermNodeIteratorScalarProd(indexVar, fromTerm, toTerm, function);
				}
				if ( VectorType.class.isAssignableFrom(function.getResultType())){
					throw new MathException("Produkt-Iterator fuer Vektoren nicht definiert.");
				}
				if ( DualVectorType.class.isAssignableFrom(function.getResultType())){
					throw new MathException("Produkt-Iterator fuer Zeilen-Vektoren nicht definiert.");
				}
				if ( MatrixType.class.isAssignableFrom(function.getResultType())){
					throw new MathException("Produkt-Iterator fuer Matrizen nicht definiert.");
				}
			}
			else {
				throw new MathException("Iterator mit Typ " + typeString + "nicht bekannt");
			}
		}
		return null;
	}
	
	public abstract String getTypeString();
	
	public Class getResultType() {
		return functionTerm.getResultType();
	}

	public TermNode getNullTerm() throws MathException {
		return functionTerm.getNullTerm();
	}

	public boolean containsAnyVar() {
		return functionTerm.containsAnyVar();
	}

	boolean containsVar(ScalarVariable var) {
		return functionTerm.containsVar(var);
	}

	public Object eval() throws MathException {
    	int from, to;
		double doubleFrom, doubleTo;

		/** Grenzen ermitteln **/
		doubleFrom = fromTerm.evalScalar();	
		if (doubleFrom % 1.0 != 0)
			throw new MathException("Untere Grenze für Iterator ist keine Ganzzahl");	
		from = (int) doubleFrom;

		doubleTo = toTerm.evalScalar();	
		if (doubleTo % 1.0 != 0)
			throw new MathException("Obere Grenze für Iterator ist keine Ganzzahl");	
		to = (int) doubleTo;

	    
		return this.evalSumOrProd(from, to);
    }
	
	public TermNode evalNum() throws Exception {

		functionTerm = functionTerm.evalNum();

		return this;
	}

	
	
	
	/**
	 * Fuehrt den Iterator zwischen den Grenzen from und to aus und gibt das Ergebnis
	 * als DoubleResult zurueck.
	 */
	protected abstract DoubleResult evalSumOrProd(int from, int to) throws MathException;


	public Object shallowEval() throws MathException {
    	int from, to;
		double doubleFrom, doubleTo;

		/** Grenzen ermitteln **/
		doubleFrom = fromTerm.shallowEvalScalar();	
		if (doubleFrom % 1.0 != 0)
			throw new MathException("Untere Grenze für Iterator ist keine Ganzzahl");	
		from = (int) doubleFrom;

		doubleTo = toTerm.shallowEvalScalar();	
		if (doubleTo % 1.0 != 0)
			throw new MathException("Obere Grenze für Iterator ist keine Ganzzahl");	
		to = (int) doubleTo;

	    return this.shallowEvalSumOrProd(from, to);
    }
	
	
	/**
	 * Fuehrt den Iterator zwischen den Grenzen from und to aus und gibt das Ergebnis
	 * als DoubleResult zurueck. Terme werden nur insoweit neu ausgewertet, als sie Variablen enthalten.
	 */
	protected abstract DoubleResult shallowEvalSumOrProd(int from, int to) throws MathException;	



	/***************************************************************************
	 * Liefert eine Kopie von sich selbst, die Variablen oldArgs werden dabei
	 * durch die entsprechenden Variablen aus newArgs ersetzt.
	 * 
	 * @return der eigene Klon
	 */
	public TermNode getClone(Variable[] oldArgs, Variable[] newArgs)
			throws Exception {

		// fromTerm klonen
		TermNode newFromTerm = fromTerm.getClone(oldArgs, newArgs);
		// toTerm Klonen
		TermNode newToTerm = toTerm.getClone(oldArgs, newArgs);
		// neue lokale Variable erzeugen
		ScalarVariable newLocalVar = new ScalarVariable(localVar
				.getTermString());
		// bei oldArgs lokale Variable localVar ergänzen
		ScalarVariable[] extendedOldArgs = new ScalarVariable[oldArgs.length + 1];
		System.arraycopy(oldArgs, 0, extendedOldArgs, 0, oldArgs.length);
		extendedOldArgs[oldArgs.length] = localVar;

		// bei newArgs lokale Variable newLocalVar ergänzen
		ScalarVariable[] extendedNewArgs = new ScalarVariable[oldArgs.length + 1];
		System.arraycopy(newArgs, 0, extendedNewArgs, 0, oldArgs.length);
		extendedNewArgs[oldArgs.length] = newLocalVar;
		// Funktionsterm Klonen
		TermNode newFunction = functionTerm.getClone(extendedOldArgs,
				extendedNewArgs);

		// mit Ergebnissen neuen Konten erzeugen und zurückgeben

		return TermNodeIterator.makeIterator(this.getTypeString(), newLocalVar, newFromTerm,
				newToTerm, newFunction);
	}

	/***************************************************************************
	 * Ersetzt im Term, der den aktuellen Knoten als Wurzel hat, alle Variablen
	 * aus vars durch den entsprechenden Term aus Terms. Genauer: Die
	 * NEWVaiable2 vars[i] wird durch den Term terms[i] ersetzt. Der so
	 * entstehende Term wird zurückgegeben. Im konkreten Fall treten Variablen
	 * nur im Funktionsterm auf, dort werden sie entsprechend ersetzt.
	 * 
	 * @param vars
	 *            Zu ersetzende Variablen.
	 * @param terms
	 *            Terme, die die Variablen ersetzen.
	 * @return Term nach den entsprechenden Ersetzungen.
	 * @throws Exception
	 */
	public TermNode substitute(Variable[] vars, TermNode[] terms)
			throws Exception {
		// in unterer und oberer Grenze ersetzen
		fromTerm = fromTerm.substitute(vars, terms);
		toTerm = toTerm.substitute(vars, terms);	
		// bei vars lokale Variable localVar ergänzen
		ScalarVariable[] extendedVars = new ScalarVariable[vars.length + 1];
		System.arraycopy(vars, 0, extendedVars, 0, vars.length);
		extendedVars[vars.length] = localVar;
		// bei terms lokale Variable localVar ergänzen
		TermNode[] extendedTerms = new TermNode[vars.length + 1];
		System.arraycopy(terms, 0, extendedTerms, 0, vars.length);
		extendedTerms[vars.length] = localVar;
		functionTerm = functionTerm.substitute(extendedVars, extendedTerms);
		return this;
	}

	/***************************************************************************
	 * Expandiert den Term, der den aktuellen Term als Wurzel hat, d.h. ersetzt
	 * alle Aufrufe von benutzerdefinierten Funktionen durch den entsprechenden
	 * Definitionsterm. Expandiert im konkreten Fall den Term für die untere
	 * Grenze, den Term für die obere Grenze und den Funktionsterm.
	 * 
	 * @return Expandierter Term
	 * @throws Exception
	 */
	public TermNode expand() throws Exception {
		fromTerm = fromTerm.expand();
		toTerm = toTerm.expand();
		functionTerm = functionTerm.expand();
		return this;
	}
	
	public void swapLinks(MathObject oldObject, MathObject newObject) {
			fromTerm.swapLinks(oldObject, newObject);
			toTerm.swapLinks(oldObject, newObject);
			functionTerm.swapLinks(oldObject, newObject);
	}

	public void insertIndexFunctions(Set indexFunctionSet) {
		fromTerm.insertIndexFunctions(indexFunctionSet);
		toTerm.insertIndexFunctions(indexFunctionSet);
		functionTerm.insertIndexFunctions(indexFunctionSet);
	}

	public void insertAllFunctions(Set functionSet) {
		fromTerm.insertAllFunctions(functionSet);
		toTerm.insertAllFunctions(functionSet);
		functionTerm.insertAllFunctions(functionSet);
	}
	

	/**
	 * Gibt den Iterator als String zurueck
	 * 
	 * @return Stringrepraesentation
	 */
	public String getTermString(MathObject parent) {
		return this.getTypeString() + "(" + localVar.getTermString(parent) + ", "
				+ fromTerm.getTermString(parent) + ", "
				+ toTerm.getTermString(parent) + ", "
				+ functionTerm.getTermString(parent) + ") ";
	}

}