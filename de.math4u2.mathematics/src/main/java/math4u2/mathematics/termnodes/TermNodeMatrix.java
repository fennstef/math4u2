package math4u2.mathematics.termnodes;

import java.util.Set;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.types.MatrixType;

/**
 * <p>
 * Überschrift:
 * </p>
 * <p>
 * Beschreibung:
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

public class TermNodeMatrix extends TermNode {

	int rowDim, colDim;

	protected TermNode[][] elementArray;

	TermNodeMatrix() {
	}

	/**
	 * Erzeugt aus einem zweidimensionalen Array von Termen eine Matrix
	 * 
	 * @param elementArray
	 *            Elemente der Matrix
	 * @throws MathException
	 *             Falls Zeilenzahl < 1 oder Spaltenzahl < 1
	 */
	public TermNodeMatrix(TermNode[][] elementArray) throws MathException {
		// Dimensionen ueberprüfen
		rowDim = elementArray.length;
		colDim = elementArray[0].length;
		if (rowDim < 1)
			throw new MathException("Matrix mit " + rowDim
					+ " Zeilen kann nicht erzeugt werden");
		if (colDim < 1)
			throw new MathException("Matrix mit " + colDim
					+ " Spalten kann nicht erzeugt werden");
		// Dimensionen ok
		// auf Variable ueberpruefen
		this.elementArray = elementArray;
		hasVar = testVars();
	} // Konstruktor1

	/**
	 * Erzeugt aus einem zweidimensionalen Array von Termen eine Matrix
	 * 
	 * @param elementArray
	 *            Elemente der Matrix
	 * @throws MathException
	 *             Falls Zeilenzahl < 1 oder Spaltenzahl < 1
	 */
	public TermNodeMatrix makeMatrix(TermNode[][] elementArray)
			throws MathException {
		return new TermNodeMatrix(elementArray);
	}

	
	protected boolean testVars() {
		boolean hasVars = false;
		for ( int r = 0; r < rowDim; r++ ){
			for ( int c = 0; c < colDim; c++ ){
				hasVars = hasVars || elementArray[r][c].containsAnyVar();
			}
		}
		return hasVars;
	}

	
	
	
	/* (non-Javadoc)
	 * @see math4u2.mathematics.termnodes.TermNode#containsAnyVar()
	 */
	public boolean containsAnyVar() {
		return hasVar;
	}
	/**
	 * Berechnet den aktuellen Wert der Matrix aus ihren Termen.
	 * 
	 * @return Ergebnis als MatrixDoubleResult
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
		currentResult = new MatrixDoubleResult(result);;
		return currentResult;
	}//eval

	public int getRowDim() {
		return rowDim;
	}//getRowDim

	public int getColDim() {
		return colDim;
	}//getColDim

	/**
	 * Ergebnistyp : MathType.MatrixTypeInstance
	 * 
	 * @return Ergebnis wie beschrieben
	 */
	public Class getResultType() {
		return MatrixType.class;
	}//getResultType

	/**
	 * Erstellt eine Matrix mit der gleichen Dimension wie die aktuelle Matrix,
	 * in der saemtliche Elemente mit dem WErt 0 belegt sind.
	 * 
	 * @return Matrix mit saemtlichen Elementen = 0
	 * @throws MathException
	 */
	public TermNode getNullTerm() throws MathException {
		TermNode[][] nullNodes = new TermNode[rowDim][colDim];
		for (int r = 0; r < rowDim; r++)
			for (int c = 0; c < colDim; c++)
				nullNodes[r][c] = new TermNodeNum(0.0);
		return this.makeMatrix(nullNodes);
	}

	private String rowToString(int rowIndex) {
		String result = "{" + elementArray[rowIndex][0].getTermString();
		for (int i = 1; i < colDim; i++) {
			result += "," + elementArray[rowIndex][i].getTermString();
		}
		return result + "}";
	}//rowToString

	/**
	 * Erzeugt einzeiligen String zur Darstellung
	 * 
	 * @return Darstellung
	 */
	public String getTermString(MathObject parent) {
		String result = "matrix({" + rowToString(0);
		for (int i = 1; i < rowDim; i++) {
			result += "," + rowToString(i);
		}
		return result + "})";
	}//getTermString

	/**
	 * Erzeugt einen Klon der aktuellen Matrix. Dabei werden in saemtlichen
	 * Termen die Variablen aus oldVars durch die entsprechenden (Position)
	 * Variablen aus newVars ersetzt.
	 * 
	 * @param oldVars
	 *            Variablen, die ersetzt werden.
	 * @param newVars
	 *            Neue Variablen im Klon.
	 * @return Klon der aktuellen Matrix.
	 * @throws Exception
	 */
	public TermNode getClone(Variable[] oldVars, Variable[] newVars)
			throws Exception {
		TermNode[][] clonedArray = new TermNode[rowDim][colDim];

		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			for (int colIndex = 0; colIndex < colDim; colIndex++) {
				clonedArray[rowIndex][colIndex] = elementArray[rowIndex][colIndex]
						.getClone(oldVars, newVars);
			}
		}
		return this.makeMatrix(clonedArray);
	}//getClone

	public TermNode substituteOLD(Variable[] vars, TermNode[] terms)
			throws Exception {
		TermNode[][] substArray = new TermNode[rowDim][colDim];

		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			for (int colIndex = 0; colIndex < colDim; colIndex++) {
				substArray[rowIndex][colIndex] = elementArray[rowIndex][colIndex]
						.substitute(vars, terms);
			}
		}
		return new TermNodeMatrix(substArray);
	}//substitueOLD

	/**
	 * Errzeugt eine Matrix, die aus der aktuellen Matrix dadurch hervorgeht,
	 * dass in jedem Element alle Variablen aus vars durch den entprechenden
	 * Term aus terms ersetzt werden. "entsprechend" heisst dabei: gleiche
	 * Position im Array.
	 * 
	 * @param vars
	 *            Array der Variablen, die ersetzt werden sollen
	 * @param terms
	 *            Array der Terme, durch die die Variablen ersetzt werden
	 * @return Matrix, in der die Ersetzungen vorgenommen sind
	 * @throws Exception
	 */
	public TermNode substitute(Variable[] vars, TermNode[] terms)
			throws Exception {
		TermNode[][] substArray = new TermNode[rowDim][colDim];

		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			for (int colIndex = 0; colIndex < colDim; colIndex++) {
				substArray[rowIndex][colIndex] = elementArray[rowIndex][colIndex]
						.substitute(vars, terms);
			}
		}
		return this.makeMatrix(substArray);
	}//substitue

	public TermNode simplify() throws Exception {
		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			for (int colIndex = 0; colIndex < colDim; colIndex++) {
				elementArray[rowIndex][colIndex] = elementArray[rowIndex][colIndex].simplify();
			}
		}
		return this;
	}//simplify

	public void insertIndexFunctions(Set indexFunctionSet) {
		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			for (int colIndex = 0; colIndex < colDim; colIndex++) {
				elementArray[rowIndex][colIndex]
						.insertIndexFunctions(indexFunctionSet);
			}
		}
	}

	public void insertAllFunctions(Set functionSet) {
		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			for (int colIndex = 0; colIndex < colDim; colIndex++) {
				elementArray[rowIndex][colIndex]
						.insertAllFunctions(functionSet);
			}
		}
	}

	public TermNode derive(Variable var) throws MathException {
		TermNode[][] deriveArray = new TermNode[rowDim][colDim];

		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			for (int colIndex = 0; colIndex < colDim; colIndex++) {
				deriveArray[rowIndex][colIndex] = elementArray[rowIndex][colIndex]
						.derive(var);
			}
		}
		return this.makeMatrix(deriveArray);
	}//derive

	/**
	 * Erzeugt zur aktuellen Matrix eine Matrix, in der saemtliche Elementterme
	 * der aktuellen Matrix expandiert sind.
	 * 
	 * @return Matrix mit expandierten Element-Termen
	 * @throws Exception
	 */

	public TermNode expand() throws Exception {
		TermNode[][] expArray = new TermNode[rowDim][colDim];

		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			for (int colIndex = 0; colIndex < colDim; colIndex++) {
				expArray[rowIndex][colIndex] = elementArray[rowIndex][colIndex]
						.expand();
			}
		}
		return this.makeMatrix(expArray);
	}//expand

	/**
	 * Entspricht getNullTerm
	 * 
	 * @param rowD
	 * @param colD
	 * @return
	 * @throws MathException
	 */
	/*
	 * public static TermNode getNullMatrixOLD(int rowD, int colD ) throws
	 * MathException{ TermNode [][] result = new TermNode[rowD][colD]; for ( int
	 * rowIndex = 0; rowIndex < rowD; rowIndex ++ ) { for ( int colIndex = 0;
	 * colIndex < colD; colIndex ++ ) { result[rowIndex][colIndex] = new
	 * TermNodeNum(0); } } return this.makeMatrix(result); }//getNullMatrix
	 */

	public TermNode getElement(int row, int col) {
		return elementArray[row][col];
	}//getElement

	public void swapLinks(MathObject oldLink, MathObject newLink) {
		for (int rowIndex = 0; rowIndex < rowDim; rowIndex++) {
			for (int colIndex = 0; colIndex < colDim; colIndex++) {
				if (elementArray[rowIndex][colIndex] == oldLink)
					elementArray[rowIndex][colIndex] = (TermNode) newLink;
				else
					elementArray[rowIndex][colIndex]
							.swapLinks(oldLink, newLink);
			}
		}
	}

}