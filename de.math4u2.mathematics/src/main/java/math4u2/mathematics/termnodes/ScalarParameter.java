package math4u2.mathematics.termnodes;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.*;
import math4u2.mathematics.termnodes.ScalarVariable;

public class ScalarParameter extends ScalarVariable {

	public ScalarParameter(String name) {
		super(name);
		value = new ScalarDoubleResult(0);
	}

	
	/*
	public boolean containsAnyVar() {
		return false;
	}

	boolean containsVar(Variable var) {
		return false;
	}*/
	
	public boolean containsAnyVar() {
		return true;
	}

	boolean containsVar(Variable var) {
		return this.equals(var);
	}
	
	public TermNode getNullTerm() throws MathException {
		return new TermNodeNum(0.0);
	}
		
}