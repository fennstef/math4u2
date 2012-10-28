package math4u2.view.gui.listview;

import math4u2.controller.reference.PathReference;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeNum;
import math4u2.mathematics.termnodes.TermNodeVector;
import math4u2.view.graph.util.IVectorDoubleValueHolder;

public abstract class FunctionVectorDoubleHolder implements
		IVectorDoubleValueHolder {
	public abstract Function getFunction() throws Exception;

	public boolean isFixed() {
		return false;
	}

	public void setFixed(boolean fixed) {
		throw new RuntimeException();
	}

	public double[] getVector() throws Exception {

		MatrixDoubleResult mdr = (MatrixDoubleResult) getFunction().eval();
		return convertToVector(mdr);
	}

	public static double[] convertToVector(MatrixDoubleResult mdr) {
		double[][] raw = mdr.valueArray;
		double[] result = new double[raw.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = raw[i][0];
		}
		return result;
	}

	public void setVector(double[] value, boolean propagateChange)
			throws Exception {
		TermNodeVector tnv;
		UserFunction vector = null;
		UserFunction parent = vector;
		vector = (UserFunction) getFunction();
		if (vector.getFunction() instanceof PathReference) {
			UserFunction uf = (UserFunction) ((PathReference) vector
					.getFunction()).getObjectWithoutLastStep();
			parent = uf;
			tnv = (TermNodeVector) uf.getFunction();
		} else if (vector.getFunction() instanceof TermNodeVector) {
			tnv = (TermNodeVector) vector.getFunction();
		} else {
			// Es kann kein neuer Wert gesetzt werden
			return;
		}
		TermNode[] tn = new TermNode[value.length];
		for (int i = 1; i < tn.length + 1; i++) {
			tn[i - 1] = tnv.getElement(i);
		}

		for (int i = 1; i < tn.length + 1; i++) {
			if (tn[i - 1] instanceof TermNodeNum) {
				tnv.setElement(i, new TermNodeNum(value[i - 1]));
			}
		}

		parent.invalidate();
		vector.invalidate();
		if (propagateChange) {
			vector.getBroker().propagateChange(parent);
		}
	}

	public double[] getVectorOrNull() {
		try {
			return getVector();
		} catch (Exception e) {
			return null;
		}
	}

}
