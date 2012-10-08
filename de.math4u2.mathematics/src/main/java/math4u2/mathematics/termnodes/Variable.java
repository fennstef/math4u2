package math4u2.mathematics.termnodes;

import java.lang.reflect.Method;
import java.util.List;

import math4u2.controller.MathObject;
import math4u2.controller.reference.AbstractPathStep;
import math4u2.controller.reference.CreatesPath;
import math4u2.controller.reference.MethodContext;
import math4u2.controller.reference.PathStep;
import math4u2.controller.reference.RootPathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.DoubleResult;
import math4u2.util.exception.ExceptionManager;

/*******************************************************************************
 * Stellt eine Variabele (Formalparameter) einer Funktion dar, z.B. in f(x,y) =
 * 2*x+sin(y) die Variable x (oder y).
 */
public abstract class Variable extends TermNode implements RootPathObject,
		CreatesPath {

	protected String name;

	protected DoubleResult value;

	/***************************************************************************
	 * Erzeugt eine Variable mit dem Namen name. Der Name wird zur Darstellung
	 * der Variable in Termen verwendet.
	 */
	public Variable(String name) {
		this.name = name;
	}

	public abstract Class getResultType();

	public TermNode getNullTerm() throws MathException {
		throw new MathException(
				"getNullTerm() fuer Term mit Variable nicht zulaessig.");
	}

	/***************************************************************************
	 * Setzt den Wert der Variable auf newValue.
	 */
	public void setValue(Object newValue) {
		value = (DoubleResult) newValue;
	} // setValue

	/***************************************************************************
	 * Wert der Variable.
	 * 
	 * @return Wert
	 */
	public Object eval() {
		return value;
	} // eval

	/***************************************************************************
	 * Ableitung der aktuellen Variable nach der Variable var.
	 * 
	 * @return Ergibt 1 ( new TermNodeNum(1.0) ), wenn die aktuelle Variable und
	 *         var dasselbe Objekt sind, ansonsten 0 ( new TermNodeNum(0.0) ).
	 */
	public TermNode derive(Variable var) {
		if (this.equals(var))
			return new TermNodeNum(1.0);
		else
			return new TermNodeNum(0.0);
	} //derive

	public boolean containsAnyVar() {
		return true;
	}

	boolean containsVar(Variable var) {
		return this.equals(var);
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
	 * Sucht das Element in vars, das mit der aktuellen Variable übereinstimmt.
	 * Der entsprechende Term aus Terms wird dann als Substitutionsterm
	 * zurückgeliefert.
	 * 
	 * @param vars
	 *            Variable, die ersetzt werden sollen.
	 * @param terms
	 *            Terme, durch welche die Variablen ersetzt werden.
	 * @return Substitutionsterm
	 * @throws Exception
	 */
	public TermNode substitute(Variable[] vars, TermNode[] terms)
			throws MathException {
		for (int i = 0; i < vars.length; i++) {
			if (this.equals(vars[i])) {
				return terms[i];
			}
		}
		return this;
		//throw new MathException("Fehler bei Term-Substitution");
	}

	/**
	 * Erzeugt eine neue Variable mut dem gleichen Namen wie die aktuelle
	 * Variable
	 * 
	 * @return
	 */
	public abstract Variable getClone();

	/***************************************************************************
	 * Sucht das Element in oldVars, das mit der aktuellen Variable
	 * übereinstimmt. Die entsprechende Variable aus newVars wird zurückgegeben.
	 * 
	 * @param oldVars
	 * @param newVars
	 * @return Variable, die die aktuelle Variable im Klon ersetzt.
	 * @throws Exception
	 */
	public TermNode getClone(Variable[] oldVars, Variable[] newVars)
			throws MathException {

		for (int i = 0; i < oldVars.length; i++) {
			if (this.equals(oldVars[i])) {
				return newVars[i];
			}
		}
		throw new MathException("Fehler bei Variablen-Substitution");
	}

	/***************************************************************************
	 * Stringdarstellung
	 * 
	 * @return Name der Variable
	 */
	public String getTermString(MathObject parent) {
		return name;
	} // getTermString

	public Object getKey() {
		return name;
	}//getKey

	public Object operator_eval(Object[] value) throws MathException {
		return eval();
	}//operator_eval

	public Class returnType_eval(MethodContext mc) {
		return getResultType();
	}//class returnType_eval

	public PathStep createPathStep(List methods) {
		//Methoden Überprüfung und Umbau
		MethodContext mc = null;
		if (!methods.isEmpty()) {
			//falls Methode da ist
			mc = (MethodContext) methods.get(0);
		} else {
			//andernfalls eine Eval-Methode erzeugen und speichern
			mc = new MethodContext("eval", new TermNode[] {});
			methods.add(mc);
		} //else

		if (!mc.getMethodName().equals("funktion")
				&& !mc.getMethodName().equals("eval")) {
			//wenn die nächste Anweisung nicht "funktion" oder "eval" ist,
			//dann wird die Methode "eval" vorangestellt
			mc = new MethodContext("eval", new TermNode[0]);
			methods.add(0, mc);
		} //else
		//Eigentlicher Methoden aufbau
		mc = (MethodContext) methods.get(0);
		methods.remove(0);
		try {
			//Methode erzeugen
			Method m = this.getClass().getMethod(
					"operator_" + mc.getMethodName(),
					new Class[] { Object[].class });

			// Step erzeugen
			// es ist noch nicht der nächste Schritt eingtragen worden
			PathStep ps = AbstractPathStep.createStep(this, mc.getMethodName(),m,mc.getArgs());

			//falls keine weiteren Methoden folgen, aufhören
			if (methods.isEmpty())
				return ps;

			//nächstes Dummy-Objekt ermitteln
			Object nextObject;
			Class resultType = getReturnType(mc);

			//die Rückgabeklasse nach einen DummyObject fragen
			Method dummyGetMethode = resultType.getMethod("getDummyObject",
					new Class[] {});
			nextObject = dummyGetMethode.invoke(null, new Object[] {});

			//nächsten Schritt erzeugen lassen und eintragen
			PathStep nextStep = ((CreatesPath) nextObject)
					.createPathStep(methods);
			ps.setNextStep(nextStep);
			return ps;
		} catch (NoSuchMethodException e) {
			String info = mc.getMethodName() + " " + this;
			ExceptionManager.doError("Fehler beim Erstellen des Methodenpfads einer Variablen\n("+info+")", e);
		} catch (Throwable e) {
			String info = mc.getMethodName() + " " + this;
			ExceptionManager.doError("Fehler beim Erstellen des Methodenpfads einer Variablen\n("+info+")", e);
		}//catch
		return null;
	} //createPathStep

	public Class getReturnType(PathStep nextStep) {
		MethodContext mc;
		if (nextStep == null)
			mc = new MethodContext("eval");
		else
			mc = nextStep.getMethodContext();
		return getReturnType(mc);
	} //getReturnType

	public Class getReturnType(MethodContext mc) {
		try {
			Method m = this.getClass().getMethod(
					"returnType_" + mc.getMethodName(),
					new Class[] { MethodContext.class });
			return (Class) m.invoke(this, new Object[] { mc });
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Ermitteln des Rückgabetyps einer Variablen ("+name+").",e);
			throw new RuntimeException(e);
		}//catch
	}//getReturnType

}