package math4u2.view.graph.util;

public abstract class FixedVectorDoubleValueHolder implements IVectorDoubleValueHolder{

	public boolean isFixed() {
		return true;
	}

	public void setFixed(boolean fixed) {
		throw new RuntimeException();
	}

	public abstract double[] getVector() throws Exception;

	public void setVector(double[] value, boolean propagateChange)
			throws Exception {
		throw new RuntimeException();
	}

	public double[] getVectorOrNull() {
		try {
			return getVector();
		} catch (Exception e) {
			return null;
		}
	}
}
