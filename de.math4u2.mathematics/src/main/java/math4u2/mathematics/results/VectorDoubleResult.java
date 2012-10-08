package math4u2.mathematics.results;

import math4u2.controller.reference.MethodContext;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeDoubleResult;
import math4u2.mathematics.types.ScalarType;
import math4u2.mathematics.types.VectorType;

/**
 * 
 * @author Fenn Stefan
 */
public class VectorDoubleResult extends MatrixDoubleResult {
	
	/**
	 * @param rowDim
	 *            Dimension der Reihe
	 * @param colDim
	 *            Dimension der Spalte, muﬂ hier eins sein
	 * @param valueArray
	 */

	
	public VectorDoubleResult(double[][] valueArray) {
		super(valueArray);
		if (colDim != 1)
			throw new IllegalArgumentException(
					"Anzahl der Spalten hat bei Vektor nicht den Wert 1");
	}//Konstruktor
	
	public VectorDoubleResult(double[] vectorArray){
		this(newVectorDoubleArray(vectorArray));
	}
	
	public static double[][] newVectorDoubleArray(double[] vectorArray) {		
		int n = vectorArray.length;
		double [][] valueArray = new double[n][1];
		for ( int i = 0; i < n; i++) {
			valueArray[i][0] = vectorArray[i];
		}
		return valueArray;
	}
	
	public double[][] getMatrix() throws MathException {
		throw new MathException("Vektor kann nicht in Matrix konvertiert werden.");
	}

	public double getScalar() throws MathException {
		throw new MathException("Vektor kann nicht in Skalar konvertiert werden.");
	}

	public double[] getVector() throws MathException {
		double[] result = new double[valueArray.length];
		for(int i=0; i<result.length; i++) result[i] = valueArray[i][0];
		return result;
	}	

	public Object operator_index(Object[] args) throws MathException {
		ScalarDoubleResult col = new ScalarDoubleResult(1);
		return super.operator_index(new Object[] { args[0], col });
	}//operator_index
	
	//--------------------------------------

	public Object operator_x(Object[] args) throws MathException {
		ScalarDoubleResult row = new ScalarDoubleResult(1);
		return operator_index(new Object[] { row });
	}//operator_x

	public Class returnType_x(MethodContext mc) {
		return ScalarType.class;
	}//class returnType_x

	//-------------------------------------------
	

	public Object operator_y(Object[] args) throws MathException {
		ScalarDoubleResult row = new ScalarDoubleResult(2);
		return operator_index(new Object[] { row });
	}//operator_y

	public Class returnType_y(MethodContext mc) {
		return ScalarType.class;
	}//class returnType_y

	//-------------------------------------------	

	public Object operator_z(Object[] args) throws MathException {
		ScalarDoubleResult row = new ScalarDoubleResult(3);
		return operator_index(new Object[] { row });
	}//operator_z

	public Class returnType_z(MethodContext mc) {
		return ScalarType.class;
	}//class returnType_z

	//-------------------------------------------	
	
	public Object operator_dimension(Object[] value) throws MathException {
		return new ScalarDoubleResult(valueArray.length);
	} //operator_dimension

	public Class returnType_dimension(MethodContext mc) {
		return ScalarType.class;
	} //class returnType_dimension
	
	public VectorDoubleResult getNullResultVector() {
		double[][] nullValueArray = new double[rowDim][colDim];
		for (int r = 0; r < rowDim; r++)
			for (int c = 0; c < colDim; c++)
				nullValueArray[r][c] = 0.0;
		return new VectorDoubleResult(nullValueArray);
	}
	
	public Class getDataType() {
		return VectorType.class;
	}
	
	public String getTypeString() {
		return "vektor";
	}//getTypeString
	
	public TermNode getNullTerm() {		
		return new TermNodeDoubleResult(getNullResultVector());
	}

	//-------------------------------------------	
	
}//class VectorDoubleResult
