package math4u2.controller.reference;

import math4u2.mathematics.termnodes.TermNode;

/**
 * Enthält alle Informationen über Methode, Rückgabewert usw.
 * 
 * @author Fenn Stefan
 */
public class MethodContext {

	/** Methodenname */
	private String methodName;

	/** alle Parameter */
	private TermNode[] args;

	public MethodContext(String methodName, TermNode[] args) {
		this.methodName = methodName;
		this.args = args;
	}//Konstruktor

	public MethodContext(String string) {
		this(string, new TermNode[] {});
	}//Konstruktor 2

	public TermNode[] getArgs() {
		return args;
	}//getArgs

	public void setArgs(TermNode[] args) {
		this.args = args;
	}//setArgs

	public String getMethodName() {
		return methodName;
	}//getMethodName

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}//setMethodName

	public String toString() {
		String s = methodName + "(";
		for (int i = 0; i < args.length; i++) {
			s += args[i];
		} //for i
		s += ")";
		return s;
	}

}//class MethodContext
