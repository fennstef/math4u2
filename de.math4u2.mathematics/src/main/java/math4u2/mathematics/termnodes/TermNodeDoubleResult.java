package math4u2.mathematics.termnodes;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.DoubleResult;

public class TermNodeDoubleResult extends TermNode {

private DoubleResult result;	
	
	public TermNodeDoubleResult( DoubleResult res){
		result = res;
	}


public Object eval() throws MathException {
		return result;
	}

	public Class getResultType() {
		return result.getDataType();
	}

	public TermNode getNullTerm() throws MathException {
		return result.getNullTerm();
	}

	public String getTermString(MathObject parent) {
		return "tNDRS";
	}

	public TermNode derive(Variable var) throws MathException {
		return this.getNullTerm();
	}

	public TermNode expand() throws Exception {
		return this;
	}

	public TermNode substitute(Variable[] vars, TermNode[] terms)
			throws Exception {
		return this;
	}

	public TermNode getClone(Variable[] oldVars, Variable[] newVars)
			throws Exception {
		return new TermNodeDoubleResult(result);
	}
	
	public String getTypeStrimng(){
		return result.getTypeString();
	}

}
