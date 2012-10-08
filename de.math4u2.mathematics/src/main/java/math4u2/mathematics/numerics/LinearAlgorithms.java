package math4u2.mathematics.numerics;

public class LinearAlgorithms {

	/**
	 * Bestimmt in der Matrix matrix in der Spalte mit Index startIndex den
	 * Index des Pivot-Elements durch Spalten-Pivotisierung. Die Suche beginnt
	 * in der Zeile mit Index startIndex und erstreckt sich auf alle folgenden
	 * Zeilen. Wenn alle entsprechenden Elemente den Wert 0 haben, wird ein
	 * ungueltiger Matrix-Index zurueckgegeben, ansonsten der Indes des
	 * (letzten) betrags-goessten Elements.
	 * 
	 * @param dim
	 *            Ordnung der Matrix
	 * @param startIndex
	 *            Die Suche beginnt beim Diagomalelement mit dem Index
	 *            startIndex.
	 * @param matrix
	 *            Matrix
	 * @return Matrix-Index des Pivot-Elements. Falls die Suche nicht
	 *         erfolgreich war, wird ein ungültiger Matrix-Index zurückgegeben.
	 */
	static matrixIndex searchPivotInRow(int dim, int startIndex,
			double[][] matrix) {
		double absMax = 0;
		int pivIndex = -1;
		for (int rowIndex = startIndex; rowIndex < dim; rowIndex++) {
			if (Math.abs(matrix[rowIndex][startIndex]) > absMax) {
				absMax = Math.abs(matrix[rowIndex][startIndex]);
				pivIndex = rowIndex;
			}
		}
		return new matrixIndex(pivIndex, startIndex);
	}

	/**
	 * Addiert in der Matrix matrix das factor-fache der Zeile mit dem Index
	 * fromRowIndex zur Zeile mit dem Index toRowIndex.
	 * 
	 * @param rowDim
	 *            Anzahl Zeilen - 1
	 * @param colDim
	 *            Anzahl Spalten -1
	 * @param fromRowIndex
	 *            Index der Zeile, die addiert werden soll
	 * @param toRowIndex
	 *            Index der Zeile, zu der addiert werden soll
	 * @param factor
	 *            Faktor
	 * @param matrix
	 */
	private static void addRowMult(int rowDim, int colDim, int fromRowIndex,
			int toRowIndex, double factor, double[][] matrix) {
		for (int i = 0; i < colDim; i++) {
			matrix[toRowIndex][i] += factor * matrix[fromRowIndex][i];
		}
	}

	private static void rowMult(int rowDim, int colDim, int rowIndex,
			double factor, double[][] matrix) {
		for (int i = 0; i < colDim; i++) {
			matrix[rowIndex][i] = factor * matrix[rowIndex][i];
		}
	}

	/**
	 * Vertauscht in der Matrix matrix mit colDim vielen Spalten die beiden
	 * Zeilen mit den Indices index1 und index2. Wenn keine Vertauschung
	 * vorgenommen wird (d.h. die Werte von index1 und index2 sind identisch,
	 * dann wird der Wert 1 zurückgegeben, ausonsten der Wert -1 .
	 * 
	 * @param index1
	 *            Index er einen Zeile
	 * @param index2
	 *            Index der anderen Zeile
	 * @param matrix
	 *            Elemente der Matrix
	 * @return
	 */
	private static int swapRows(int colDim, int index1, int index2,
			double[][] matrix) {
		if (index1 == index2)
			return 1; // nichts zu vertauschen, alles erledigt.

		for (int i = 0; i < colDim; i++) {
			double temp = matrix[index1][i];
			matrix[index1][i] = matrix[index2][i];
			matrix[index2][i] = temp;
		}
		return -1;
	}

	/**
	 * Vertauscht in der Matrix matrix die beiden Spalten an den Positionen
	 * index1 und index2. Wenn keine Vertauschung vorgenommen wird (d.h. die
	 * Werte von index1 und index2 sind identisch, dann wird der Wert 1
	 * zurückgegeben, ausonsten der Wert -1 .
	 * 
	 * @param index1
	 *            Position1
	 * @param index2
	 *            Position2
	 * @param matrix
	 * @return
	 */
	private static int swapCols(int rowDim, int index1, int index2,
			double[][] matrix) {
		if (index1 == index2)
			return 1; // nichts zu vertauschen, alles erledigt.

		for (int i = 0; i < rowDim; i++) {
			double temp = matrix[i][index1];
			matrix[i][index1] = matrix[i][index2];
			matrix[i][index2] = temp;
		}
		return -1;
	}

	/**
	 * Berechnet zu der quadratischen Matrix der Ordnung dim im Array matrix den
	 * Wert der Determinante. Das Array matrix wird dabei nicht verändert.
	 * 
	 * @param dim
	 *            Ordnung der quadratischen Matrix
	 * @param matrix
	 *            Elemente der Matrix
	 * @return Wert der Determinante
	 */
	public static double det(int dim, double[][] matrix) {

		double result = 1;
		double[][] workMatrix = (double[][]) (matrix.clone());

		// Alle Spalten ausser der letzten ausraeumen
		for (int index = 0; index < dim - 1; index++) {
			// Pivot-Position bestimmen
			matrixIndex pivotIndex = searchPivotInRow(dim, index, workMatrix);
			// wenn es keines gibt: det = 0
			if (!pivotIndex.isValid)
				return 0;
			// Zeilen tauschen, Faktor -1 beruecksichtigen
			result *= swapRows(dim, index, pivotIndex.rowInd, workMatrix);
			// Diagonalelement auf Ergebnis multiplizieren
			double pivotValue = workMatrix[index][index];
			result *= pivotValue;
			// Spalte ausraeumen
			for (int delIndex = index + 1; delIndex < dim; delIndex++) {
				addRowMult(dim, dim, index, delIndex,
						-workMatrix[delIndex][index] / pivotValue, workMatrix);
			}
		}
		// letztes Diagonalelement auf Ergebnis multiplizieren
		// result*workMatrix[dim-1][dim-1] + " ----");
		return result * workMatrix[dim - 1][dim - 1];
	}

	/**
	 * Berechnet zu einer quadratsischen Matrix der Ordnung dim die Inverse,
	 * falls dies moeglich ist.
	 * 
	 * @param dim
	 *            Ordnung der quadratischen Matrix
	 * @param matrix
	 *            Elemente der Matrix
	 * @return null, falls nicht invertierbar, ansonsten die invese Matrix in
	 *         einem Array vom Typ double[dim][dim].
	 */
	public static double[][] inverse(int dim, double[][] matrix) {
		// Eingabe klonen
		double[][] workMatrix = (double[][]) (matrix.clone());
		//Einheitsmatrix erzeugen
		double[][] inverse = new double[dim][dim];
		for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
			for (int colIndex = 0; colIndex < dim; colIndex++) {
				if (rowIndex == colIndex)
					inverse[rowIndex][rowIndex] = 1;
				else
					inverse[rowIndex][colIndex] = 0;
			}
		}
		// Alle Spalten
		for (int index = 0; index < dim; index++) {
			// Pivot-Position bestimmen
			matrixIndex pivotIndex = searchPivotInRow(dim, index, workMatrix);
			// wenn es keines gibt: nicht invertierbar
			if (!pivotIndex.isValid)
				return null;
			// Zeilen tauschen
			swapRows(dim, index, pivotIndex.rowInd, workMatrix);
			swapRows(dim, index, pivotIndex.rowInd, inverse);
			// Diagonalelement in Workmatrix zu 1 machen
			double pivotValue = workMatrix[index][index];
			rowMult(dim, dim, index, 1 / pivotValue, workMatrix);
			rowMult(dim, dim, index, 1 / pivotValue, inverse);
			// Spalte nach oben und unten ausraeumen
			for (int delIndex = 0; delIndex < dim; delIndex++) {
				double factor = -workMatrix[delIndex][index];
				if (delIndex != index) {
					addRowMult(dim, dim, index, delIndex, factor, workMatrix);
					addRowMult(dim, dim, index, delIndex, factor, inverse);
				}
			}
		}
		return inverse;
	}

	/**
	 * Bestimmt zur quadratischen regulaeren Matrix koeffizientenMatrix der
	 * Ordnung dim die Loesung des linearen Gleichungssystems mit der
	 * Inhomogenitaet inhomogenitaet.
	 * 
	 * @param dim
	 *            Ordnung der Koeffizientenmatrix
	 * @param koeffizientenMatrix
	 *            Elemente der Koeffizientenmatrix in einem Array vom Typ
	 *            double[dim][dim]; wird nicht veraendert.
	 * @param inhomogenitaet
	 *            Elemente der Inhomogenitaet in einem Array vom Typ
	 *            double[dim][1]; wird nicht veraendert.
	 * @return null, falls die Koeffizientenmatrix singulaer ist, ansonsten die
	 *         (eindeutige) Loesung in einem Array von Typ double[dim][1].
	 */

	public static double[][] solveLinearEquation(int dim,
			double[][] koeffizientenMatrix, double[][] inhomogenitaet) {
		// Eingabe klonen
		double[][] matrix = (double[][]) (koeffizientenMatrix.clone());
		double[][] inhom = (double[][]) (inhomogenitaet.clone());

		// Alle Spalten
		for (int index = 0; index < dim; index++) {
			// Pivot-Position bestimmen
			matrixIndex pivotIndex = searchPivotInRow(dim, index, matrix);
			// wenn es keines gibt: nicht invertierbar
			if (!pivotIndex.isValid)
				return null;

			int pivotRow = pivotIndex.rowInd;
			// Zeilen in Matrix tauschen
			swapRows(dim, index, pivotRow, matrix);
			// Zeilen in Inhomogenitaet tauschen
			double temp = inhom[index][0];
			inhom[index][0] = inhom[pivotRow][0];
			inhom[pivotRow][0] = temp;
			// Diagonalelement in matrix zu 1 machen
			double pivotValue = matrix[index][index];
			rowMult(dim, dim, index, 1 / pivotValue, matrix);
			// Element in inhom entsprechend multiplizieren
			inhom[index][0] /= pivotValue;
			// Spalte nach oben und unten ausraeumen
			for (int delIndex = 0; delIndex < dim; delIndex++) {
				double factor = -matrix[delIndex][index];
				if (delIndex != index) {
					// auraeumen
					addRowMult(dim, dim, index, delIndex, factor, matrix);
					// entsprechende Operation auf inhom anwenden
					inhom[delIndex][0] += factor * inhom[index][0];
				}
			}
		}
		return inhom;
	}

	// Faktorisierung
	// Norm
}