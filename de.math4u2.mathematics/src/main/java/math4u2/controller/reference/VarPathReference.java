package math4u2.controller.reference;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import math4u2.controller.Broker;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.termnodes.*;

/**
 * Ist ein PathReference mit einer Variablen als Root-Object
 * 
 * @author Fenn Stefan
 */
public class VarPathReference extends PathReference {

	public VarPathReference(RootPathObject root, List methods, Broker broker) {
		super(root, methods, broker);
		hasVar = true;
	} //Konstruktor

	private VarPathReference(RootPathObject root, PathStep step, Broker broker) {
		super(root, step, broker);
		hasVar = true;
	}//Konstruktor

	/***************************************************************************
	 * Ableitung der aktuellen Variable nach der Variable var.
	 * 
	 * @return Ergibt 1 ( new TermNodeNum(1.0) ), wenn die aktuelle Variable und
	 *         var dasselbe Objekt sind, ansonsten 0 ( new TermNodeNum(0.0) ).
	 */
	public TermNode derive(Variable var) throws MathException {
		if (root.equals(var)) {
			return new TermNodeNum(1.0);
		} else {
			return new TermNodeNum(0.0);
		}
	} //derive

	/**
	 * Evaluiert immer den ganzen Term
	 * 
	 * @see math4u2.controller.reference.PathReference#getShallowObject()
	 */
	public Object getShallowObject() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, MathException {
		return getObject();
	}//getShallowObject

	public TermNode getClone(Variable[] oldVars, Variable[] newVars)
			throws Exception {
		RootPathObject newRoot;
		PathStep newStep;
		newRoot = (Variable) (((Variable) root).getClone(oldVars, newVars));
		newStep = step.getClone(oldVars, newVars);
		return new VarPathReference(newRoot, newStep, broker);
	}//getClone

	public TermNode substitute(Variable[] vars, TermNode[] terms)
			throws Exception {
		for (int i = 0; i < vars.length; i++) {
			if (root.equals(vars[i])) {
				root = (RootPathObject)terms[i];
			}//if
		}//for i
		step = step.substitute(vars, terms);
		return this;
	}//substitute
}