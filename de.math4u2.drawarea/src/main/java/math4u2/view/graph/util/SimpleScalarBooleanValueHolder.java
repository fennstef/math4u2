package math4u2.view.graph.util;

public class SimpleScalarBooleanValueHolder extends AbstractValueHolder implements IScalarBooleanHolder {
	private boolean value;

	public SimpleScalarBooleanValueHolder(boolean value){
		this.value = value;
	}
	
	public boolean getScalar() throws Exception {
		return value;
	}

	public void setScalar(boolean value, boolean propagateChange) throws Exception {
		if(fixed) throw new Exception("Der Wert kann nicht gesetzt werden");
		this.value = value;
	}

	public boolean getScalarOrNan() {
		return value;
	}
}
