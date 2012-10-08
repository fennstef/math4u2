package math4u2.view.graph.util;

public class SimpleVectorDoubleValueHolder extends AbstractValueHolder implements IVectorDoubleValueHolder {
	private double[] value;

	public SimpleVectorDoubleValueHolder(double[] value){
		this.value = value;
	}
	
	public double[] getVector() throws Exception {
		return value;
	}

	public void setVector(double[] value, boolean propagateChange) throws Exception {
		if(fixed) throw new Exception("Der Wert kann nicht gesetzt werden");
		this.value = value;
	}

	public double[] getVectorOrNull() {
		return value;
	}
}
