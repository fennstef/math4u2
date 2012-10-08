package math4u2.mathematics.termnodes;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.types.VectorType;

/**
 * <p>
 * Überschrift: Vektor (Spaltenvektor)
 * </p>
 * <p>
 * Beschreibung: Stellt einen Vektor, d.h. einen Spaltenvektor dar.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Organisation:
 * </p>
 * 
 * @author M. Weiss
 * @version 1.0
 */

public class TermNodeVector extends TermNodeMatrix {

	/**
	 * Erzeugt aus einem eindimensionalen Array von Termen einen Vektor
	 * 
	 * @param vectorArray
	 *            Array mit den Elementen des Vektors
	 * @throws MathException
	 *             Falls Zeilenzahl < 1
	 */
	public TermNodeVector(TermNode[] vectorArray) throws MathException {
		rowDim = vectorArray.length;
		colDim = 1;
		// Zeilenzahl ueberpruefen
		if (rowDim < 1)
			throw new MathException("Vektor mit " + rowDim
					+ " Komponenten kann nicht erzeugt werden");
		// Zeilenzahl ok
		elementArray = new TermNode[rowDim][1];

		for (int r = 0; r < rowDim; r++) {
			elementArray[r][0] = vectorArray[r];
		}
		hasVar = testVars();
	} //Konstruktor1

	/**
	 * Erzeugt aus einem zweidimensionalen Array von Termen einen Vektor. Das
	 * Array muss dabei vom Typ TermNode[rowDim][1] sein.
	 * 
	 * @param elementArray
	 *            Elemente der Matrix
	 * @throws MathException
	 *             Falls Zeilenzahl < 1 oder Spaltenzahl != 1
	 */

	public TermNodeVector(TermNode[][] elementArray) throws MathException {
		rowDim = elementArray.length;
		// Dimensionen ueberprüfen
		if (rowDim < 1)
			throw new MathException("Vektor mit " + rowDim
					+ " Zeilen kann nicht erzeugt werden");
		colDim = elementArray[0].length;
		if (colDim != 1)
			throw new MathException("Vektor mit " + colDim
					+ " Spalten kann nicht erzeugt werden");
		// Dimensionen ok
		this.elementArray = elementArray;
		hasVar = testVars();
	} // Konstruktor2

	/**
	 * Erzeugt aus einem zweidimensionalen Array von Termen einen Vektor. Das
	 * Array muss dabei vom Typ TermNode[rowDim][1] sein.
	 * 
	 * @param elementArray
	 *            Elemente der Matrix
	 * @throws MathException
	 *             Falls Zeilenzahl < 1 oder Spaltenzahl != 1
	 */
	public TermNodeMatrix makeMatrix(TermNode[][] elementArray)
			throws MathException {
		return new TermNodeVector(elementArray);
	}

	/**
	 * Ergebnistyp : MathType.VectorTypeInstance
	 * 
	 * @return Ergebnis wie beschrieben
	 */
	public Class getResultType() {
		return VectorType.class;
	} //getResultType

	public TermNode getNullTermOLD() throws MathException {
		TermNode[] nullNodes = new TermNode[rowDim];
		for (int r = 0; r < rowDim; r++)
			nullNodes[r] = new TermNodeNum(0.0);
		return new TermNodeVector(nullNodes);
	}

	public TermNode getCloneOLD(Variable[] oldVars, Variable[] newVars)
			throws Exception {
		TermNode[] clonedArray = new TermNode[rowDim];

		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			clonedArray[rowIndex] = elementArray[rowIndex][0].getClone(oldVars,
					newVars);
		}

		return new TermNodeVector(clonedArray);
	}//getClone

	public TermNode deriveOLD(Variable var) throws MathException {
		TermNode[] deriveArray = new TermNode[rowDim];
		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			deriveArray[rowIndex] = elementArray[rowIndex][0].derive(var);
		}
		return new TermNodeVector(deriveArray);
	}//derive

	/**
	 * Berechnet den aktuellen Wert des Vektors aus ihren Termen.
	 * 
	 * @return Ergebnis als VectorDoubleResult
	 * @throws Exception
	 */
	public Object eval() throws MathException {
		if(elementArray[0][0].eval() instanceof ScalarDoubleResult){
			double[][] result = new double[rowDim][colDim];
			for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
				for (int colIndex = 0; colIndex < colDim; colIndex++) {
					result[rowIndex][colIndex] = ((ScalarDoubleResult) (elementArray[rowIndex][colIndex]
							.eval())).value;
				}
			}
			currentResult = new VectorDoubleResult(result);
			return currentResult;
		}else{
			Object[] result = new Object[rowDim];
			for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
				result[rowIndex] = elementArray[rowIndex][0].eval();
			}
			return result;
		}
	} //eval

	public TermNode expandOLD() throws Exception {
		TermNode[] expArray = new TermNode[rowDim];

		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			expArray[rowIndex] = elementArray[rowIndex][0].expand();
		}
		return new TermNodeVector(expArray);
	}//expand

	/**
	 * Erzeugt einzeiligen String zur Darstellung
	 * 
	 * @return Darstellung
	 */
	public String getTermString(MathObject parent) {
		String result = "vektor({" + elementArray[0][0].getTermString();
		for (int i = 1; i < rowDim; i++) {
			result += "," + elementArray[i][0].getTermString();
		}
		return result + "})";
	} //getTermString

	public static TermNode getNullVectorOLD(int rowD) throws MathException {
		TermNode[] result = new TermNode[rowD];
		for (int rowIndex = 0; rowIndex < rowD; rowIndex++) {
			result[rowIndex] = new TermNodeNum(0);
		}
		return new TermNodeVector(result);
	} //getNullVector

	public TermNode getElement(int pos) {
		return elementArray[pos - 1][0];
	}//getElement

	public void setElement(int pos, TermNode tn) {
		elementArray[pos - 1][0] = tn;
	}//setElement

}