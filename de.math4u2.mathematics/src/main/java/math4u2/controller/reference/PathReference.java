package math4u2.controller.reference;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.mathematics.affine.AbstractAffineObject;
import math4u2.mathematics.collection.MathList;
import math4u2.mathematics.collection.Sequence;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeNum;
import math4u2.mathematics.termnodes.Variable;
import math4u2.mathematics.types.ScalarType;
import math4u2.util.exception.ExceptionManager;

/**
 * Hier wird eine Pfadstruktur gespeichert Z.B. "k.mitte.x" nicht: "a" oder "p"
 * usw.
 * 
 * @author Fenn Stefan
 */
public class PathReference extends TermNode implements MathObjectReference {
	/** Wurzelknoten */
	protected RootPathObject root;

	/** nächster Schritt */
	protected PathStep step;

	protected Broker broker;

	/** Gecachter Rückgabetyp */
	protected Class cachedResultType;

	/**
	 * @param root
	 *            Anfangsobjekt
	 * @param methods
	 *            Liste von Objekten der Klasse MethodContext
	 */
	public PathReference(RootPathObject root, List methods, Broker broker) {
		this.root = root;
		this.broker = broker;
		try {
			step = ((CreatesPath) root).createPathStep(methods);
		} catch (CreatePathException e) {
			throw new RuntimeException(
					"Der Methodenpfad konnte nicht erstellt werden ("
							+ root.getKey() + ", " + methods + ").\n"
							+ e.getMessage());
		}// catch

		if (step != null) {
			hasVar = step.containsAnyVar();
		} // if
	} // Konstruktor

	/**
	 * @param root
	 *            Anfangsobjekt
	 * @param step
	 *            Erster Schritt, falls vorhanden, sonst null.
	 */
	protected PathReference(RootPathObject root, PathStep step, Broker broker) {
		this.root = root;
		this.step = step;
		this.broker = broker;
		if (step != null) {
			hasVar = step.containsAnyVar();
		} // if
	}

	/** Wird im vollständigem Pfad irgendwo eine Variable benutzt */
	public boolean containsAnyVar() {
		return hasVar;
	} // containsAnyVar

	/**
	 * Evaluierung des Methodenstacks
	 */
	public Object getObject() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, MathException {

		if (step == null) {
			currentResult = root;
		} else {
			currentResult = step.getObject(root);
		} // else
		return currentResult;
	} // getObject

	public MathObject getRootObject() {
		return (MathObject) root;
	}// getRootObject

	/**
	 * Wenn keine Variable gebunden ist, wird ein Zwischenergebnis
	 * zurückgegeben. Anderfalls wie bei <code>getObject()</code>
	 */
	public Object getShallowObject() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, MathException {

		if (!hasVar)
			return currentResult;
		return step.getShallowObject(root);
	} // getObject

	/**
	 * Gibt die letze Methode des Methoden-Stacks zurück.
	 */
	public PathStep getLastStep() {
		if (step == null)
			return null;
		return step.getLastStep();
	} // getLastStep

	/**
	 * Evaluierung des Methodenstacks aber ohne die letzte Methode
	 */
	public Object getObjectWithoutLastStep() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, MathException {

		if (step == null || step.getNextStep() == null) {
			currentResult = root;
		} else {
			currentResult = step.getObjectWithoutLastStep(root);
		} // else
		return currentResult;
	} // getObject

	public String getPathString() {
		String stepStr = "";
		if (step != null)
			stepStr = step.getPathString();
		String rootKey = (String) root.getKey();
		if (root.getKey() == null) {
			if (root instanceof UserFunction) {
				rootKey = ((UserFunction) root).getTermString();
			} else if (root instanceof AbstractAffineObject) {
				rootKey = ((AbstractAffineObject) root).bodyToString();
			} else if (root instanceof MathList) {
				rootKey = ((MathList) root).toBodyString();
			} else if (root instanceof Sequence) {
				rootKey = ((Sequence) root).toBodyString();
			}
		}
		return rootKey + stepStr;
	} // getPathString

	public void swapLinks(MathObject oldObj, MathObject newObj) {
		if ((root == oldObj))
			root = newObj;
		if (step != null)
			step.swapLinks(oldObj, newObj);
		cachedResultType = null;
	} // swapLinks

	/**
	 * Holt sich einen Repräsentanten, der den Rückgabetyp weiß und gibt den
	 * Rückgabetyp zurück.
	 */
	public Class getReturnType() {
		if (cachedResultType == null) {
			if (step == null)
				cachedResultType = ((CreatesPath) root).getReturnType(null);
			else
				cachedResultType = step.getReturnType((CreatesPath) root);
		}// if
		return cachedResultType;
	} // getReturnType

	public String toString() {
		return root.getKey() + "." + step;
	} // toString

	// --------------------------------------------------------------
	// Ab hier implementierung von TermNode

	public Object eval() throws MathException {
		try {
			return getObject();
		} catch (IllegalArgumentException e) {
			ExceptionManager.doError(e);
			throw new MathException(e);
		} catch (IllegalAccessException e) {
			ExceptionManager.doError(e);
			throw new MathException(e);
		} catch (InvocationTargetException e) {
			throw new MathException(e);
		} // catch
	} // eval

	public Object shallowEval() throws MathException {
		try {
			return getShallowObject();
		} catch (IllegalArgumentException e) {
			ExceptionManager.doError(e);
			throw new MathException(e);
		} catch (IllegalAccessException e) {
			ExceptionManager.doError(e);
			throw new MathException(e);
		} catch (InvocationTargetException e) {
			throw new MathException(e);
		} catch (MathException e) {
			ExceptionManager.doError(e);
			throw new MathException(e);
		}
	} // shallowEval

	public TermNode simplify() throws Exception {
		if (step != null) {
			step.simplify();
		}
		return this;
	}

	public Class getResultType() {
		return getReturnType();
	} // getResultType

	public String getTermString(MathObject parent) {
		return getPathString();
	} // getTermString

	public TermNode getNullTerm() throws MathException {
		throw new MathException(
				"getNullTerm nicht implementiert bei PathReference");
	}

	public void insertIndexFunctions(Set indexFunctionSet) {
		if (root instanceof UserFunction)
			((UserFunction) root).insertIndexFunctions(indexFunctionSet);
		if (step != null)
			step.insertIndexFunctions(indexFunctionSet);
	}

	public void insertAllFunctions(Set functionSet) {
		if (root instanceof Function)
			functionSet.add(root);
		if (root instanceof UserFunction)
			((UserFunction) root).insertAllFunctions(functionSet);
		if (step != null)
			step.insertAllFunctions(functionSet);
	}

	/***************************************************************************
	 * Gibt den Term zurück, der entsteht, wenn man den aktuellen Term nach der
	 * Variable var ableitet.
	 */

	public TermNode derive1(Variable var) throws MathException {
		if (!hasVar)
			if (ScalarType.class.isAssignableFrom(getResultType()))
				return new TermNodeNum(0.0);

		if (!((root instanceof Function) && (step instanceof EvalPathStep))) {
			try {
				return step.derive(var, (CreatesPath) root, broker);
			} catch (Exception e) {
				throw new MathException(e.toString());
			}
		}
		TermNode[] arguments = step.getArgs(); // von eval-Step die //
												// Argument-terme holen

		TermNode[] derivedArgumentTerms;

		if (!hasVar) {
			derivedArgumentTerms = new TermNode[1];
			derivedArgumentTerms[0] = new TermNodeNum(0.0);
		} else {
			derivedArgumentTerms = new TermNode[arguments.length];
			for (int i = 0; i < arguments.length; i++) {
				derivedArgumentTerms[i] = arguments[i].derive(var);
			}
		}

		TermNode newTerm = ((Function) root).buildDeriveTerm(arguments,
				derivedArgumentTerms, broker);

		PathStep newStep = step.getClone(new TermNode[] { newTerm });

		try {
			return new PathReference(broker.getObject("brackets"), newStep,
					broker);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/***************************************************************************
	 * Gibt den Term zurück, der entsteht, wenn man den aktuellen Term nach der
	 * Variable var ableitet.
	 */

	public TermNode derive(Variable var) throws MathException {
		if (root instanceof Function && (step instanceof CalcEvalPathStep))
		// d.h. Funktionsaufruf wie sin(x+x) oder matrixSum(m1(x), m2(x))
		{
			if (step.getNextStep() == null) {
				if (!hasVar
						&& ScalarType.class.isAssignableFrom(getResultType()))
				// Aufruf einer skalaren Funktion ohne Variable
				{
					return new TermNodeNum(0.0);
				} else // Aufruf einer Funktion, aber mit Variable oder
				// keine skalare Funktion
				{
					TermNode[] arguments = step.getArgs(); // von eval-Step die
															// Argument-Terme
															// holen
					TermNode[] derivedArgumentTerms = new TermNode[arguments.length];
					for (int i = 0; i < arguments.length; i++) {
						derivedArgumentTerms[i] = arguments[i].derive(var);
					}
					return ((Function) root).buildDeriveTerm(arguments,
							derivedArgumentTerms, broker);
				}
			} else { // es gibt nach dem EvalPathStep noch einen Schritt
						// Ableitung bauen, IndexPathStep klonen und anhängen
				TermNode[] arguments = step.getArgs(); // von eval-Step die
														// Argument-Terme holen
				TermNode[] derivedArgumentTerms = new TermNode[arguments.length];
				for (int i = 0; i < arguments.length; i++) {
					derivedArgumentTerms[i] = arguments[i].derive(var);
				}

				TermNode deriveTerm = ((Function) root).buildDeriveTerm(
						arguments, derivedArgumentTerms, broker);

				UserFunction deriveFunc = new UserFunction("", deriveTerm,
						new Variable[] { var }, broker, ((Function)root).getViewFactory());

				List methodContList = step.getNextStep().getMethodContextList();

				methodContList.add(0, new MethodContext("calceval",
						new TermNode[] { var }));
				return new PathReference(deriveFunc, methodContList, broker);
			}
		} else // root ist keine Funktion oder step ist kein eval-Schritt oder
				// nicht der letzte
		{
			try {
				return step.derive(var, (CreatesPath) root, broker);
			} catch (Exception e) {
				throw new MathException(e.toString());
			}
		}
	}

	public TermNode substitute(Variable[] vars, TermNode[] terms)
			throws Exception {
		step = step.substitute(vars, terms);
		return this;
	}

	public TermNode getClone(Variable[] oldVars, Variable[] newVars)
			throws Exception {

		RootPathObject newRoot;
		PathStep newStep;

		newRoot = root;

		newStep = step.getClone(oldVars, newVars);

		return new PathReference(newRoot, newStep, broker);
	}

	public TermNode expand() throws Exception {
		return this;
	}

	/**
	 * Entfernt die letzte Eval-Methode
	 */
	public void removeLastEval() {
		if (step == null)
			return;
		if ((step.getNextStep() == null) && (step instanceof EvalPathStep))
			step = null;
		else
			step.removeLastEval();
	}// removeLastEval

} // class PathReference
