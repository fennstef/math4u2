package math4u2.mathematics.termnodes;

import math4u2.mathematics.results.*;
import math4u2.mathematics.types.ScalarType;

public class ScalarVariable extends Variable {

	public ScalarVariable(String name, double doubleValue) {
		super(name);
		value = new ScalarDoubleResult(doubleValue);
	}
	
	public ScalarVariable(String name) {
		super(name);
		value = new ScalarDoubleResult(0);
	}

	public Class getResultType() {
		return ScalarType.class;
	}
	
	/***************************************************************************
	 * Setzt den Wert der Variable auf newValue.
	 */
	public void setValue(Object newValue) {
		value = (ScalarDoubleResult) newValue;
	} // setValue

	public Variable getClone() {
		return new ScalarVariable(this.getTermString());
	}
}