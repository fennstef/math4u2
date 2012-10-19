package math4u2.view.graph.util;

public abstract class FixedScalarStringValueHolder implements IScalarStringHolder {

	public boolean isFixed() {
		return true;
	}

	public void setFixed(boolean fixed) {		
		throw new RuntimeException();
	}

	public String getScalar() throws Exception {
		return getScalarOrNull();
	}

	public void setScalar(String value, boolean propagateChange)
			throws Exception {
		throw new RuntimeException();
	}

	public abstract String getScalarOrNull();
}
