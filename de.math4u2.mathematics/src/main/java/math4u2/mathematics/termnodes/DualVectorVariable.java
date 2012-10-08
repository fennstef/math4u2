package math4u2.mathematics.termnodes;

import math4u2.mathematics.types.DualVectorType;

public class DualVectorVariable extends MatrixVariable {

	public DualVectorVariable(String name) {
		super(name);
	}

	public Class getResultType() {
		return DualVectorType.class;
	}

	public Variable getClone() {
		return new DualVectorVariable(this.getTermString());
	}
}