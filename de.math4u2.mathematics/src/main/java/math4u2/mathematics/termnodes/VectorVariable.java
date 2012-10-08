package math4u2.mathematics.termnodes;

import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.types.VectorType;

public class VectorVariable extends MatrixVariable {

	public VectorVariable(String name) {
		super(name);
		value = new VectorDoubleResult(new double[][] { { 1 } });
	}//Konstruktor

	public Class getResultType() {
		return VectorType.class;
	}//getRsultType

	public Variable getClone() {
		return new VectorVariable(this.getTermString());
	}

}