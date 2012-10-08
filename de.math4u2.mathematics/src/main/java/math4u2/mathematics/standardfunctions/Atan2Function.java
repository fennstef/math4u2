package math4u2.mathematics.standardfunctions;

import math4u2.mathematics.functions.ScalarBinaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;

public class Atan2Function extends ScalarBinaryStandardFunction {

	public Atan2Function() {
		name = "atan2";
		summaryText = "atan2(y,x) berechnet fuer einen Punkt P(y,x)<br>"+
		              "den zugehoerigen Polarwinkel im Bereich -pi, ..., pi.";
	}
	
	public String[] getArgumentStrings() {
		return new String[] {"y-Koordinate", "x-Koordinate"};
	}


	public Object eval(Object[] args) {
		return new ScalarDoubleResult(Math.atan2( ((ScalarDoubleResult) args[0]).value, 
				                                  ((ScalarDoubleResult) args[1]).value));
	}
}