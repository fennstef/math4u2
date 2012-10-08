package math4u2.mathematics.termnodes;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;

public class TermNodeUnitMatrix extends TermNodeMatrix {

	public TermNodeUnitMatrix(int dim) throws MathException {
		if (dim < 1)
			throw new MathException("Matrix mit " + dim
					+ " Reihen kann nicht erzeugt werden");

		rowDim = dim;
		colDim = dim;
		elementArray = new TermNode[dim][dim];
		for (int r = 0; r < dim; r++) {
			for (int c = 0; c < dim; c++) {
				if (r == c) {
					elementArray[r][r] = new TermNodeNum(1.0);
				} else {
					elementArray[r][c] = new TermNodeNum(0.0);
				}
			}
		}
		hasVar = testVars();
	}

	public String getTermString(MathObject parent) {
		return "ematrix(" + rowDim + ")";
	}
}