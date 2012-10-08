package math4u2.mathematics.types;

import math4u2.mathematics.results.MatrixDoubleResult;

public class MatrixType implements ResultType{

	public static String getTypeString() {
		return "<matrix>";
	}//getTypeString

	public static Object getDummyObject() {
		return new MatrixDoubleResult(new double[][] { { 0 } });
	}//getDummyObject
}