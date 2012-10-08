package math4u2.mathematics.termnodes;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.DualVectorDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.types.DualVectorType;

/**
 * <p>
 * Überschrift: Dual-Vektor (
 * </p>
 * <p>
 * Beschreibung: Stelle einen Dual-Vektor, d.h. einen Zeilenvektor dar.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Organisation:
 * </p>
 * 
 * @author unbekannt
 * @version 1.0
 */

public class TermNodeDualVector extends TermNodeMatrix {

	public TermNodeDualVector(TermNode[] vectorArray) throws MathException {

		colDim = vectorArray.length;

		if (colDim < 1)
			throw new MathException("Vektor mit " + colDim
					+ " Komponenten kann nicht erzeugt werden");

		rowDim = 1;

		elementArray = new TermNode[1][colDim];

		for (int c = 0; c < colDim; c++) {
			elementArray[0][c] = vectorArray[c];
		}
		hasVar = testVars();
	}

	/**
	 * Erzeugt aus einem zweidimensionalen Array von Termen einen Dual-Vektor.
	 * Das Array muss dabei vom Typ TermNode[1][colDim] sein.
	 * 
	 * @param elementArray
	 *            Elemente der Matrix
	 * @throws MathException
	 *             Falls Zeilenzahl < != oder Spaltenzahl < 1
	 */
	public TermNodeDualVector(TermNode[][] elementArray) throws MathException {
		rowDim = elementArray.length;
		// Dimensionen ueberprüfen
		if (rowDim != 1)
			throw new MathException("Dual-Vektor mit " + rowDim
					+ " Zeilen kann nicht erzeugt werden");
		colDim = elementArray[0].length;
		if (colDim < 1)
			throw new MathException("Dual-Vektor mit " + colDim
					+ " Spalten kann nicht erzeugt werden");
		// Dimensionen ok
		this.elementArray = elementArray;
		hasVar = testVars();
	} // Konstruktor2

	/**
	 * Erzeugt aus einem zweidimensionalen Array von Termen einen Dual-Vektor.
	 * Das Array muss dabei vom Typ TermNode[rowDim][1] sein.
	 * 
	 * @param elementArray
	 *            Elemente der Matrix
	 * @throws MathException
	 *             Falls Zeilenzahl != 1 oder Spaltenzahl < 1
	 */
	public TermNodeMatrix makeMatrix(TermNode[][] elementArray)
			throws MathException {
		return new TermNodeDualVector(elementArray);
	}

	/**
	 * Ergebnistyp : MathType.VectorTypeInstance
	 * 
	 * @return Ergebnis wie beschrieben
	 */
	public Class getResultType() {
		return DualVectorType.class;
	}

	/**
	 * Berechnet den aktuellen Wert des Dualvektors aus seinen Termen.
	 * 
	 * @return Ergebnis als DualVectorDoubleResult
	 * @throws Exception
	 */
	public Object eval() throws MathException {
		double[][] result = new double[rowDim][colDim];

		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			for (int colIndex = 0; colIndex < colDim; colIndex++) {
				result[rowIndex][colIndex] = ((ScalarDoubleResult) (elementArray[rowIndex][colIndex]
						.eval())).value;
			}
		}
		currentResult =  new DualVectorDoubleResult(result);  
		return currentResult;
	} //eval
	
	
	public TermNode getNullTermOLD() throws MathException {
		TermNode[] nullNodes = new TermNode[colDim];
		for (int c = 0; c < colDim; c++)
			nullNodes[c] = new TermNodeNum(0.0);
		return new TermNodeDualVector(nullNodes);
	}

	public TermNode getCloneOLD(Variable[] oldVars, Variable[] newVars)
			throws Exception {
		TermNode[] clonedArray = new TermNode[colDim];
		for (int colIndex = 0; colIndex < colDim; colIndex++) {
			clonedArray[colIndex] = elementArray[0][colIndex].getClone(oldVars,
					newVars);
		}
		return new TermNodeDualVector(clonedArray);
	}//getClone

	public TermNode deriveOLD(Variable var) throws MathException {
		TermNode[] deriveArray = new TermNode[colDim];
		for (int colIndex = 0; colIndex < colDim; colIndex++) {
			deriveArray[colIndex] = elementArray[0][colIndex].derive(var);
		}
		return new TermNodeDualVector(deriveArray);
	}//derive

	/**
	 * Erzeugt einzeiligen String zur Darstellung
	 * 
	 * @return Darstellung
	 */
	public String getTermString(MathObject parent) {
		String result = "dualvektor({" + elementArray[0][0].getTermString();
		for (int i = 1; i < colDim; i++) {
			result += "," + elementArray[0][i].getTermString();
		}
		return result + "})";
	}

	public static TermNode getNullDualVectorOLD(int colD) throws MathException {
		TermNode[] result = new TermNode[colD];
		for (int colIndex = 0; colIndex < colD; colIndex++) {
			result[colIndex] = new TermNodeNum(0);
		}
		return new TermNodeDualVector(result);
	}
}