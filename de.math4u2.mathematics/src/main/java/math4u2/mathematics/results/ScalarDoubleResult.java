package math4u2.mathematics.results;

import java.util.List;

import math4u2.controller.reference.CreatesPath;
import math4u2.controller.reference.PathStep;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeDoubleResult;
import math4u2.mathematics.types.ScalarType;
import math4u2.util.exception.NotImplementedException;

public class ScalarDoubleResult extends DoubleResult implements CreatesPath {

	public double value;

	public static Object getDummyObject() {
		return new ScalarDoubleResult(0);
	}//getDummyObject

	public ScalarDoubleResult(double value) {
		this.value = value;
	}//Konstruktor

	public String getTypeString() {
		return "skalar";
	}//getTypeString

	public double evalScalar() {
		return value;
	}//evalScalar

	public PathStep createPathStep(List methods) {
		throw new NotImplementedException();
	}//createPathStep

	public Class getReturnType(PathStep nextStep) {
		return ScalarType.class;
	}
	
	public Class getDataType() {
		return ScalarType.class;
	}
	
	public TermNode getNullTerm() {		
		return new TermNodeDoubleResult(new ScalarDoubleResult(0.0));
	}

	public double[][] getMatrix() throws MathException {
		throw new MathException("Skalar kann nicht in Matrix konvertiert werden.");
	}

	public double getScalar() throws MathException {
		return value;
	}

	public double[] getVector() throws MathException {
		throw new MathException("Skalar kann nicht in Vektor konvertiert werden.");
	}

}//class ScalarDoubleResult
