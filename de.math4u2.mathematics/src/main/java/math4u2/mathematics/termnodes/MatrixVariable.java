package math4u2.mathematics.termnodes;

import math4u2.mathematics.results.*;
import math4u2.mathematics.types.MatrixType;

public class MatrixVariable extends Variable {

	public MatrixVariable(String name) {
		super(name);
		value = new MatrixDoubleResult(new double[][] { { 1 } });
	}

	public Class getResultType() {
		return MatrixType.class;
	}

	public Variable getClone() {
		return new MatrixVariable(this.getTermString());
	}

}