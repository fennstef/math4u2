package math4u2.mathematics.types;

import math4u2.mathematics.results.VectorDoubleResult;

public class VectorType extends MatrixType {

	public static String getTypeString() {
		return "<vektor>";
	}//getTypeString
	
	public static Object getDummyObject() {
		return new VectorDoubleResult(new double[][] { { 0 } });
	}//getDummyObject	
}