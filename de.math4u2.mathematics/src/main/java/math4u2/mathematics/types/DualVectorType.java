package math4u2.mathematics.types;

import math4u2.mathematics.results.DualVectorDoubleResult;

public class DualVectorType extends MatrixType {

	public static String getTypeString() {
		return "<dualvektor>";
	}//getTypeString
	
	public static Object getDummyObject() {
		return new DualVectorDoubleResult(new double[][] { { 0 } });
	}//getDummyObject
	
}