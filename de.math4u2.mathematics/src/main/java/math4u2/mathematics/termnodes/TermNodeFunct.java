package math4u2.mathematics.termnodes;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.controller.relation.RelationContainer;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.StandardFunction;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.util.exception.ExceptionManager;

/**
 * Die Klasse TermNodeFunct repräsentiert einen Funtionsaufruf wie z.B. den
 * Aufruf sin(3*pi) der einstelligen Sinus-Funktion mit dem Arguemnt 3*pi oder
 * den Aufruf x + 3 der zweistelligen Summenfunktion mit den beiden Argumenten x
 * und 3.
 */

public class TermNodeFunct extends TermNode {

	protected TermNode[] arguments;

	/** Referenz auf den Broker */
	private Broker broker;

	/** Referenz auf die Funktion */
	protected Function function;

	/***************************************************************************
	 * Die Funktion function wird auf die Argumente arguments angewendet. Die
	 * Stelligkeit der Funktion muss mit der Anzahl der Elemente in arguments
	 * übereinstimmen.
	 * 
	 * @param name
	 *            Function-Object
	 * @param arguments
	 *            Arguemnte
	 * @param broker
	 */
	public TermNodeFunct(Function function, TermNode[] arguments, Broker broker)
			throws MathException {
		this.arguments = arguments;
		this.hasVar = this.containsAnyVar();

		if (function == null)
			throw new MathException("Funtionsaufruf ohne Funktionsdefinition");
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
		return false;
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

		if(values==null || values.length != arguments.length)
			values = new Object[arguments.length];

		for (int i = 0; i < values.length; i++)
			values[i] = arguments[i].eval();

		currentResult = function.eval(values);
		return currentResult;
	}

	
	private Object[] values;
	
	public Object shallowEval() throws MathException {
		if (!hasVar)
			return currentResult;

		if(values==null || values.length != arguments.length)
			values = new Object[arguments.length];

		for (int i = 0; i < values.length; i++)
			values[i] = arguments[i].shallowEval();

		currentResult = function.shallowEval(values);
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

		//if ( ! this.containsAnyVar()) return this.getNullTerm(); 
		
		
		TermNode[] derivedArgumentTerms = new TermNode[arguments.length];

		for (int i = 0; i < arguments.length; i++) {
			// if ( arguments[i].containsVar(var) ) {
			derivedArgumentTerms[i] = arguments[i].derive(var);
			// }
			// else {
			//     derivedArgumentTerms[i] = this.getNullTerm();
			// }
		}
		return function
				.buildDeriveTerm(arguments, derivedArgumentTerms, broker);
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
		if (arguments.length == 0)
			return this;

		//alle Argumente expandieren
		for (int i = 0; i < function.getArity(); i++) {
			arguments[i] = arguments[i].expand();
		}
		if (function instanceof StandardFunction) {
			//nichts weiter zu expandieren
			return this;
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
		return newFunction.getFunction();
	}

	/***************************************************************************
	 * Gibt die Funktion als String zurueck
	 * 
	 * @return Stringrepraesentation
	 */
	public String getTermString(MathObject parent) {
		String name = getNameFromRelation(parent);

		//TermNode[] arguments = getSubTerms();
		String[] argStrings = new String[arguments.length];

		for (int i = 0; i < arguments.length; i++)
			argStrings[i] = arguments[i].getTermString(parent);

		return function.buildTermString(argStrings, name);
	}//getTermString

	private String getNameFromRelation(MathObject parent) {
		String name = null;
		if (parent == null)
			return null;
		//Beziehungen des Objects überprüfen
		name = getCreationPath(parent);
		if (name != null)
			return name;
		//Benannte Beziehungen verfolgen
		RelationContainer rc = parent.getRelationContainer();
		Iterator iterator = rc.getAllNamedRelationNames().iterator();
		while (iterator.hasNext()) {
			String objName = (String) iterator.next();
			try {
				MathObject mo = (MathObject) rc
						.getObjectByName(objName);
				name = getCreationPath(mo);
				if (name != null)
					return name;
			} catch (ObjectNotInRelationException e) {
				ExceptionManager.doError("Fehler beim Holen des Kindobjekts '"+objName+"'",e);
			}//catch
		}//while
		return name;
	}//getNameFromRelation

	/**
	 * Holt von einem Object den CreationPath für diese TermNodeFunct. Falls es
	 * keinen CreationPath gibt, wird <code>null</code> zurück gegeben.
	 */
	private String getCreationPath(MathObject parent) {
		Iterator iter = parent.getRelationContainer().getAllRelationsIterator();
		while (iter.hasNext()) {
			RelationInterface ri = (RelationInterface) iter.next();
			try {
				//falls die Beziehung nichts mit dieser
				//TermNodeFunct zu tun hat, dann ignorieren.
				if (ri.getPartner(parent) != function)
					continue;
			} catch (ObjectNotInRelationException e) {
				e.printStackTrace();
				continue;
			}
			if (ri.getCreationPath() != null) {
				return ri.getCreationPath().toString();
			}//if
		} //while
		return null;
	}//getCreationPath

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
		return new TermNodeFunct(function, args, broker);
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
			function = (Function) newObject;
		for (int i = 0; i < arguments.length; i++) {
			arguments[i].swapLinks(oldObject, newObject);
		}
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
