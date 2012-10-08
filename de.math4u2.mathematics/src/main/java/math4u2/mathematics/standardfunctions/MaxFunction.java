package math4u2.mathematics.standardfunctions;

import math4u2.mathematics.functions.ScalarBinaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;

public class MaxFunction extends ScalarBinaryStandardFunction {

	public MaxFunction() {
		name = "max";
		summaryText = "Liefert das Maximum von Wert 1 und Wert 2";
	}

	public Object eval(Object[] args) {
		if (((ScalarDoubleResult) args[0]).value >= ((ScalarDoubleResult) args[1]).value)
			return new ScalarDoubleResult(((ScalarDoubleResult) args[0]).value);
		return new ScalarDoubleResult(((ScalarDoubleResult) args[1]).value);
	}

	public String[] getArgumentStrings() {
		return new String[] {"Wert 1", "Wert 2"};
	}

	
	
	
}