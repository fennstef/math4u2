package math4u2.mathematics.termnodes;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;

public class TermNodeDiagMatrix extends TermNodeMatrix {

	public TermNodeDiagMatrix(TermNode[] diagElements) throws MathException {
		rowDim = diagElements.length;
		if (rowDim < 1)
			throw new MathException("Matrix mit " + rowDim
					+ " Reihen kann nicht erzeugt werden");

		colDim = rowDim;

		elementArray = new TermNode[rowDim][rowDim];

		for (int r = 0; r < rowDim; r++) {
			for (int c = 0; c < colDim; c++) {
				if (r == c) {
					elementArray[r][c] = diagElements[r];
				} else {
					elementArray[r][c] = new TermNodeNum(0.0);
				}
			}
		}
		hasVar = testVars();
	}

	/**
	 * Erzeugt einzeiligen String zur Darstellung
	 * 
	 * @return Darstellung
	 */
	public String getTermString(MathObject parent) {
		String result = "diagmatrix({" + elementArray[0][0].getTermString();
		for (int i = 1; i < rowDim; i++) {
			result += "," + elementArray[i][i].getTermString();
		}
		return result + "})";
	}
}