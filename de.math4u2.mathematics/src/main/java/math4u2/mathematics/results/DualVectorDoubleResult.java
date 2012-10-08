package math4u2.mathematics.results;

import math4u2.controller.reference.MethodContext;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeDoubleResult;
import math4u2.mathematics.types.DualVectorType;
import math4u2.mathematics.types.ScalarType;

/**
 * 
 * @author Fenn Stefan
 */
public class DualVectorDoubleResult extends MatrixDoubleResult {

	/**
	 * @param rowDim
	 *            Dimension der Reihe, muß hier eins sein
	 * @param colDim
	 *            Dimension der Spalte
	 * @param valueArray
	 */
	/*
	public DualVectorDoubleResult(int rowDim, int colDim, double[][] valueArray) {
		super(rowDim, colDim, valueArray);
		if (rowDim != 1)
			throw new IllegalArgumentException(
					"Die Reihendimension, darf hier nur die Größe eins besitzen");
	}//Konstruktor*/
	
	public DualVectorDoubleResult(double[][] valueArray) {
		super(valueArray);
		if (rowDim != 1)
			throw new IllegalArgumentException(
					"Anzahl der Zeilen hat bei Zeilenvektor nicht den Wert 1");
	}//Konstruktor
	
	public DualVectorDoubleResult(double[] vectorArray){
		super(new double[][] {vectorArray});
	}
	
	public double[][] getMatrix() throws MathException {
		throw new MathException("Vektor kann nicht in Matrix konvertiert werden.");
	}

	public double getScalar() throws MathException {
		throw new MathException("Vektor kann nicht in Skalar konvertiert werden.");
	}

	public double[] getVector() throws MathException {
		return valueArray[0];
	}

	public Object operator_index(Object[] args) throws MathException {
		ScalarDoubleResult row = new ScalarDoubleResult(1);
		return super.operator_index(new Object[] { row, args[0] });
	}//operator_index
	
	//--------------------------------------

	public Object operator_x(Object[] args) throws MathException {
		ScalarDoubleResult col = new ScalarDoubleResult(1);
		return operator_index(new Object[] { col });
	}//operator_x

	public Class returnType_x(MethodContext mc) {
		return ScalarType.class;
	}//class returnType_x

	//-------------------------------------------
	

	public Object operator_y(Object[] args) throws MathException {
		ScalarDoubleResult col = new ScalarDoubleResult(2);
		return operator_index(new Object[] { col });
	}//operator_y

	public Class returnType_y(MethodContext mc) {
		return ScalarType.class;
	}//class returnType_y

	//-------------------------------------------	

	public Object operator_z(Object[] args) throws MathException {
		ScalarDoubleResult col = new ScalarDoubleResult(3);
		return operator_index(new Object[] { col });
	}//operator_z

	public Class returnType_z(MethodContext mc) {
		return ScalarType.class;
	}//class returnType_z

	//-------------------------------------------	
	
	public Object operator_dimension(Object[] value) throws MathException {
		return new ScalarDoubleResult(valueArray[0].length);
	} //operator_dimension

	public Class returnType_dimension(MethodContext mc) {
		return ScalarType.class;
	} //class returnType_dimension
	
	public DualVectorDoubleResult getNullResultDualVector() {
		double[][] nullValueArray = new double[rowDim][colDim];
		for (int r = 0; r < rowDim; r++)
			for (int c = 0; c < colDim; c++)
				nullValueArray[r][c] = 0.0;
		return new DualVectorDoubleResult(nullValueArray);
	}
	
	public Class getDataType() {
		return DualVectorType.class;
	}
	
	public String getTypeString() {
		return "dualvektor";
	}//getTypeString
	
	public TermNode getNullTerm() {		
		return new TermNodeDoubleResult(getNullResultDualVector());
	}

	//-------------------------------------------	

}//class DualVectorDoubleResult
