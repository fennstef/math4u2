package math4u2.view.graph.util;

public class SimpleScalarDoubleValueHolder extends AbstractValueHolder implements IScalarDoubleHolder {
	private double value;

	public SimpleScalarDoubleValueHolder(double value){
		this.value = value;
	}
	
	public double getScalar() throws Exception {
		return value;
	}

	public void setScalar(double value, boolean propagateChange) throws Exception {
		if(fixed) throw new Exception("Der Wert kann nicht gesetzt werden");
		this.value = value;
	}

	public double getScalarOrNan() {
		return value;
	}
}
