package math4u2.mathematics.numerics;

class matrixIndex {
	final int rowInd, colInd;

	final boolean isValid;

	public matrixIndex(int rowInd, int colInd) {
		this.rowInd = rowInd;
		this.colInd = colInd;
		isValid = (rowInd >= 0) && (colInd >= 0);
	}

	public matrixIndex() {
		rowInd = -1;
		colInd = -1;
		isValid = false;
	}
}