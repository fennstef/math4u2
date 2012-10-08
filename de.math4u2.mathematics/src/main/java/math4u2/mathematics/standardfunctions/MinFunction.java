package math4u2.mathematics.standardfunctions;

import math4u2.mathematics.functions.ScalarBinaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;

public class MinFunction extends ScalarBinaryStandardFunction {

	public MinFunction() {
		name = "min";
		summaryText = "Liefert das Minimum von Wert 1 und Wert 2";
	}

	public Object eval(Object[] args) {
		if (((ScalarDoubleResult) args[0]).value <= ((ScalarDoubleResult) args[1]).value)
			return ((ScalarDoubleResult) args[0]);
		return ((ScalarDoubleResult) args[1]);
	}
}