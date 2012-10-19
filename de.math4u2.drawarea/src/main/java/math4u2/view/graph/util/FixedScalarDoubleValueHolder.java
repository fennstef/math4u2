package math4u2.view.graph.util;

public abstract class FixedScalarDoubleValueHolder implements IScalarDoubleHolder {

	public boolean isFixed() {
		return true;
	}

	public void setFixed(boolean fixed) {
		throw new RuntimeException();
	}

	public abstract double getScalar() throws Exception;

	public void setScalar(double value, boolean propagateChange)
			throws Exception {
		throw new RuntimeException();
	}

	public  double getScalarOrNan(){
		try {
			return getScalar();
		} catch (Exception e) {
			return Double.NaN;
		}
	}
}
