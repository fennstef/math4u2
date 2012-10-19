package math4u2.mathematics.termnodes;

import java.util.AbstractSet;
import java.util.Set;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.types.OperatorExpert;

/**
 * Die Klasse TermNodeDerive repräsentiert eine Ableitung
 */

public class TermNodeDerive extends TermNode {

	protected TermNode[] arguments;
	
	protected TermNode deriveOrderTerm;

	/** Referenz auf den Broker */
	private Broker broker;

	/** Referenz auf die Funktion */
	protected UserFunction function;

	/**
	 * Term-Knoten für Ableitung
	 * @param function  Funktion, die abgeleitet wird
	 * @param deriveOrderTerm  Term, der die Ordnung der Ableitung liefert
	 * @param arguments  Argumentterme
	 * @param broker
	 * @throws MathException
	 */
	
	public TermNodeDerive( UserFunction function, 
			               TermNode deriveOrderTerm, 
			               TermNode[] arguments, 
						   Broker broker)
			throws MathException {
		this.deriveOrderTerm = deriveOrderTerm;
		this.arguments = arguments;
		this.hasVar = this.containsAnyVar();

		if (function == null)
			throw new MathException("Funtionsaufruf ohne Funktionsdefinition");
		
		if (deriveOrderTerm == null)
			throw new MathException("Ordnung der Ableitung nicht definiert");
		
		if (function.getArity() != 1)
			throw new MathException("Stelligkeit einer Funktion, die abgeleitet werden soll, ist nicht 1");
		
		if (function.getArity() != arguments.length)
			throw new MathException("Stelligkeit der Funktion "
					+ function.getName()
					+ " stimmt nicht mit der Anzahl der Argumente ueberein");

		function.reportArgumentConflict(arguments);
		
		for (int i = 0; i < arguments.length; i++) {
			if (  !function.getVariableType(i).isAssignableFrom(
					                          arguments[i].getResultType())	
				)
				throw new MathException("Fehler bei " + function.getName()
						+ ": Typ des Arguments an Position " + i + " : "
						+ arguments[i].getResultType().toString()
						+ " nicht kompatibel mit Typ der Variable "
						+ function.getVariableType(i).toString() + " "
						+ arguments[i].getTermString());
		}
		this.function = function;

		this.broker = broker;
	}

	/***************************************************************************
	 * Gibt den Namen der Funktion zurueck
	 * 
	 * @return Funktionsname
	 */
	public String getName() {
		return (String) function.getIdentifier();
	}

	// ?? muss man das immer neu ermitteln oder kann man es beim Erzeugen schon
	// feststellen ??
	public Class getResultType() {
		Class[] argTypes = new Class[arguments.length];
		for (int i = 0; i < arguments.length; i++)
			argTypes[i] = arguments[i].getResultType();
		return function.getResultType(argTypes);
	}

	public TermNode getNullTerm() throws MathException {
		TermNode[] nullArguments = new TermNode[arguments.length];
		for (int i = 0; i < arguments.length; i++)
			nullArguments[i] = arguments[i].getNullTerm();
		return new TermNodeFunct(function, nullArguments, broker);
	}

	public void insertParts(AbstractSet s) {
		for (int i = 0; i < arguments.length; i++) {
			arguments[i].insertParts(s);
		}
		if ((function instanceof UserFunction)
				&& (function.getName().indexOf("%") != 0)) {
			s.add(function);
		}
	}

	/***************************************************************************
	 * Gibt die aktuellen Argumente der Funktion zurueck, z.B. 5 und a+3 bei
	 * f(5, a+3)
	 * 
	 * @return die TermNodes, die in die Funktion eingesetzt werden
	 */
	public TermNode[] getArguments() {
		return arguments;
	}

	/**
	 * Gibt den Subknoten an der uebergebenen Stelle zurueck
	 * 
	 * @param arrayIndex
	 *            spezifiziert den Subterm
	 * @return Subknoten
	 */
	public TermNode getArgumentTerm(int arrayIndex) {
		return arguments[arrayIndex];
	}

	public boolean containsAnyVar() {
		for (int i = 0; i < arguments.length; i++) {
			if (arguments[i].containsAnyVar())
				return true;
		}

		return deriveOrderTerm.containsAnyVar();
	}

	boolean containsVar(Variable var) {
		for (int i = 0; i < arguments.length; i++) {
			if (arguments[i].containsVar(var))
				return true;
		}
		return false;
	}

	/***************************************************************************
	 * Gibt den errechneten Funktionswert zurueck
	 * 
	 * @return Ergebnis als DoubleResult
	 */
	public Object eval() throws MathException {
		
		Object argValue = arguments[0].eval();
		int order = (int)Math.floor(((ScalarDoubleResult)deriveOrderTerm.eval()).value);
		
		currentResult = function.eval(order,argValue);
		
		return currentResult;
	}

	
	public Object shallowEval() throws MathException {
		if (!hasVar)
			return currentResult;
	
		Object argValue = arguments[0].eval();
		int order = (int)Math.floor(((ScalarDoubleResult)deriveOrderTerm.shallowEval()).value);

		currentResult = function.shallowEval(order, argValue);
		return currentResult;
	}

	public double evalScalar() throws MathException {
		return ((ScalarDoubleResult) eval()).value;
	}

	public double shallowEvalScalar() throws MathException {
		return ((ScalarDoubleResult) shallowEval()).value;
	}

	/***************************************************************************
	 * Vereinfacht den Knoten
	 * 
	 * @return Der vereinfachte Term
	 */
	/***************************************************************************
	 * public ScalarDoubleResult peval( HashSet usedFunctionNames, HashSet
	 * usedVaraibles ) throws Exception { boolean funcEval = true; HashSet
	 * localNames = new HashSet(); //ScalarDoubleResult[] arguments =
	 * getSubTerms();
	 * 
	 * 
	 * for(int i=0; i <arguments.length; i++) { // arguments[i] =
	 * arguments[i].peval(localNames ); funcEval = funcEval && ( arguments[i]
	 * instanceof TermNodeNum ); }
	 *  // wenn sich alle Argumente zu Zahlen vereinfacht haben, // dann kann
	 * jetzt die Funktion ausgewertet werden if ( funcEval ) { double[] values =
	 * new double[arguments.length];
	 * 
	 * for(int i=0; i <values.length; i++) values[i] = arguments[i].eval();
	 * 
	 * return new TermNodeNum( function.eval(values)); } // ansonsten wird der
	 * Funktionsknoten mit den vereinfachten // Argumenten zurückgegeben else {
	 * usedFunctionNames.addAll(localNames);
	 * usedFunctionNames.add(function.name); return new TermNodeFunct(
	 * this.function, arguments, this.broker ); } } // peval
	 **************************************************************************/

	public void insertIndexFunctions(Set indexFunctionSet) {
		if (function instanceof UserFunction)
			((UserFunction) function).insertIndexFunctions(indexFunctionSet);
		for (int i = 0; i < arguments.length; i++) {
			arguments[i].insertIndexFunctions(indexFunctionSet);
		}
	}

	public void insertAllFunctions(Set functionSet) {
		functionSet.add(function);
		if (function instanceof UserFunction)
			((UserFunction) function).insertAllFunctions(functionSet);
		for (int i = 0; i < arguments.length; i++) {
			arguments[i].insertAllFunctions(functionSet);
		}
	}

	/***************************************************************************
	 * Bestimmt den Term, der entsteht, wenn man den Term, der den aktuellen
	 * Knoten als Wurzel hat, nach der ScalarVariable var differenziert.
	 * Beispiel: Aus dem Term sin(x+y^2) entsteht mit var == x der Term
	 * cos(x+y^2) und mit var == y der Term cos(x+y^2)*2*y.
	 * 
	 * @param var
	 *            ScalarVariable, nach der differienziert wird.
	 * @return (Partielle) Ableitung
	 * @throws Exception
	 */
	public TermNode derive(Variable var) throws MathException {

		//ScalarDoubleResult[] argumentTerms = getSubTerms();

		TermNode[] derivedArgumentTerms = new TermNode[arguments.length];

		for (int i = 0; i < arguments.length; i++) {
			// if ( arguments[i].containsVar(var) ) {
			derivedArgumentTerms[i] = arguments[i].derive(var);
			// }
			// else {
			//     derivedArgumentTerms[i] = this.getNullTerm();
			// }
		}
		// Ordnung um eins erhöhen
		try {
			TermNode newDeriveOrderTerm = (new TermNodeFunct((Function) (broker.getObject("sum")),
					new TermNode[] { deriveOrderTerm, new TermNodeNum(1.0)}, broker));
		
		
			TermNode newDeriveTerm = new TermNodeDerive( function, 
				                                     	 newDeriveOrderTerm,
													     arguments, broker);
			
		   
			Function f = OperatorExpert.getFunctionForArguments(  newDeriveTerm,
	                                                     derivedArgumentTerms[0], "*", broker);
	        return new TermNodeFunct(f,  new TermNode[] {  newDeriveTerm,  
											               derivedArgumentTerms[0] }, broker);
		
		} catch (MathException e) {
			e.printStackTrace();
			throw e;
		} catch (BrokerException e) {
			e.printStackTrace();
			throw new MathException(e.getMessage());
		}
	}

	/***************************************************************************
	 * Expandiert den Term, der den aktuellen Term als Wurzel hat, d.h. ersetzt
	 * alle Aufrufe von benutzerdefinierten Funktionen durch den entsprechenden
	 * Definitionsterm.
	 * 
	 * @return Expandierter Term
	 * @throws Exception
	 */
	public TermNode expand() throws Exception {
		return this;
		/*
		if (arguments.length == 0)
			return this;

		//alle Argumente expandieren
		for (int i = 0; i < function.getArity(); i++) {
			arguments[i] = arguments[i].expand();
		}
		
		// also userFunction
		// klonen
		UserFunction newFunction = ((UserFunction) function).cloneFunction();
		// Term expandieren
		newFunction.setFunction(newFunction.getFunction().expand());
		// im Funktionsterm Variablen durch Aktualparameter ersetzen
		newFunction.getFunction().substitute(newFunction.getVariables(),
				arguments);
		// Funktionsterm zurückgeben
		return newFunction.getFunction(); */
	}

	/***************************************************************************
	 * Gibt den Knoten als String zurueck
	 * 
	 * @return Stringrepraesentation
	 */
	public String getTermString(MathObject parent) {
		
		return "derive(vars(" + (function.getVariableNames())[0]  + "),"
		       + function.getTermString() + "," + deriveOrderTerm.getTermString()
		       +")(" + arguments[0].getTermString() + ")";
	}//getTermString


	/***************************************************************************
	 * Ersetzt in sämtlichen Argument-Termen (Aktualparameter) sämtliche
	 * Vorkommen der Variablen aus vars jeweils durch den entsprechenden Term
	 * aus terms.
	 * 
	 * @param vars
	 *            Variablen, die ersetzt werden.
	 * @param terms
	 *            Terme, durch die die Variablen ersetzt werden.
	 * @return Term nach Substitutuion.
	 * @throws Exception
	 */
	public TermNode substitute(Variable[] vars, TermNode[] terms)
			throws Exception {
		int arity = arguments.length;
		for (int i = 0; i < arity; i++) {
			arguments[i] = arguments[i].substitute(vars, terms);
		}
		deriveOrderTerm = deriveOrderTerm.substitute(vars, terms);
		return this;
	}

	/***************************************************************************
	 * Liefert eine Kopie von sich selbst, die Variablen oldArgs werden dabei
	 * durch die entsprechenden Variablen aus newArgs ersetzt.
	 * 
	 * @return der eigene Klon
	 */
	public TermNode getClone(Variable[] oldArgs, Variable[] newArgs)
			throws Exception {

		TermNode[] args = new TermNode[arguments.length];
		for (int i = 0; i < args.length; i++) {
			args[i] = arguments[i].getClone(oldArgs, newArgs);
		}
		return new TermNodeDerive(function, deriveOrderTerm.getClone(oldArgs, newArgs), args, broker);
	}

	/**
	 * Falls die Funktion function des Knotens mit oldObject uebereinstimmt,
	 * wird sie durch die Funktion newObject ersetzt, in allen Argumenttermen
	 * wird jede Referenz auf oldObject durch newObject ersetzt.
	 * 
	 * @param oldObject
	 * @param newObject
	 */

	public void swapLinks(MathObject oldObject, MathObject newObject) {
		if (function == oldObject)
			function = (UserFunction) newObject;
		for (int i = 0; i < arguments.length; i++) {
			arguments[i].swapLinks(oldObject, newObject);
		}
		deriveOrderTerm.swapLinks(oldObject, newObject);
	}

	/***************************************************************************
	 * Vereinfacht den aktuellen Knoten, wenn moeglich.
	 * 
	 * @return Vereinfachter Knoten
	 */

	public TermNode simplify() throws Exception {
		for (int i = 0; i < arguments.length; i++)
			arguments[i] = arguments[i].simplify();

		return function.simplify(this, arguments, broker);
	}

	public TermNode evalNum() throws Exception {

		for (int i = 0; i < arguments.length; i++) {
			arguments[i] = arguments[i].evalNum();
		}

		TermNode r = function.evalNum(this, arguments, broker);
		return r;
	}

	/**
	 * @return
	 */
	public Function getFunction() {
		return function;
	}

} //class
