package math4u2.mathematics.types;

import math4u2.mathematics.results.ScalarDoubleResult;

public class ScalarType implements ResultType{

	public static String getTypeString() {
		return "";
	}//getTypeString
	
	public static Object getDummyObject() {
		return new ScalarDoubleResult(0);
	}//getDummyObject
}