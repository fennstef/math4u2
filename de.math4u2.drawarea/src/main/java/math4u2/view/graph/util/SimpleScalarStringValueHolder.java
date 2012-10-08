package math4u2.view.graph.util;

public class SimpleScalarStringValueHolder extends AbstractValueHolder implements IScalarStringHolder {
	private String value;

	public SimpleScalarStringValueHolder(String value){
		this.value = value;
	}
	
	public String getScalar() throws Exception {
		return value;
	}

	public void setScalar(String value, boolean propagateChange) throws Exception {
		if(fixed) throw new Exception("Der Wert kann nicht gesetzt werden");
		this.value = value;
	}

	public String getScalarOrNull() {
		return value;
	}
}
