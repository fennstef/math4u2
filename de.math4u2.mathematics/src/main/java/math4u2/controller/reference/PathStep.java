package math4u2.controller.reference;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.termnodes.*;
import math4u2.controller.Broker;

/**
 * Methodenschritt
 * 
 * @author Fenn Stefan
 */
public interface PathStep {

	/**
	 * Gibt das Objekt zur�ck, das mit dieser Methode und dem Objekt obj
	 * zur�ckgegeben wird.
	 */
	Object getObject(Object obj) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, MathException;

	/**
	 * Schnellere Evaluierung falls keine Variablen gebunden sind.
	 */
	Object getShallowObject(Object root) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, MathException;

	/**
	 * Instanziierung kann erst sp�ter erfolgen. Sie wird in diese Methode
	 * ausgelagert
	 */
	void instantiate(String name, Method stepMethod, TermNode[] args,
			PathStep nextStep);

	/**
	 * Gibt den Typ des Schritts als String zur�ck
	 * 
	 * @return Typ
	 */

	public TermNode[] getArgs();

	public String getName();

	/**
	 * Gibt den letzten Methoden-Schritt zur�ck
	 */
	PathStep getLastStep();

	/**
	 * Gibt den n�chsten Methoden-Schritt zur�ck
	 */
	PathStep getNextStep();

	/**
	 * �berpr�ft, ob eine Variable im Ausdruck vorkommt und speichert sich das
	 * Ergebnis in hasVars
	 */
	boolean containsAnyVar();

	/**
	 * Gibt den R�ckgabetye f�r diese Methode zur�ck
	 */
	Class getReturnType(CreatesPath dummyObject);

	/**
	 * Zeichenkettenausgabe
	 */
	String getPathString();

	/**
	 * Update der neuen Objekte
	 */
	void swapLinks(MathObject oldObj, MathObject newObj);

	/**
	 * Setzen des neuen Methodenschritts
	 */
	void setNextStep(PathStep step);

	public void simplify() throws Exception;

	public void insertIndexFunctions(Set indexFunctionSet);

	public void insertAllFunctions(Set functionSet);

	/**
	 * Berechnet Ableitung nach Variable var
	 * 
	 * @param var
	 * @param obj
	 * @param broker
	 * @return
	 */
	TermNode derive(Variable var, CreatesPath obj, Broker broker)
			throws Exception;

	/**
	 * R�ckgabe der Methodeninformation
	 */
	MethodContext getMethodContext();

	/**
	 * Gibt Kindobjekt durch Methodenaufruf zur�ck
	 */
	Object getObjectFromMethod(Object obj) throws Exception;

	/**
	 * Evaluieren des Methodenstack, aber ohne letzte Methode
	 */
	Object getObjectWithoutLastStep(Object root)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, MathException;

	PathStep getEmptyClone();

	/***************************************************************************
	 * Liefert eine Kopie des Schritts mit allen Folgeschritten. Dabei werden
	 * alle Vorkommen von Variablen aus oldVars durch die entsprechende Variable
	 * aus newVars ersetzt. Genauer: jede Variable oldVars[i] wird durch die
	 * Variable newVars[i] ersetzt.
	 * 
	 * @param oldVars
	 *            Zu ersetzende Variablen
	 * @param newVars
	 *            Neue Variablen
	 * @return Kopie
	 * @throws Exception
	 */
	public PathStep getClone(Variable[] oldVars, Variable[] newVars)
			throws Exception;

	public PathStep getClone(TermNode[] args);
	
	public List getMethodContextList();

	public PathStep substitute(Variable[] vars, TermNode[] terms)
			throws Exception;

	/**
	 * Entfernt die letzte Eval-Methode
	 */
	void removeLastEval();
}