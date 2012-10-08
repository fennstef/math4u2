package math4u2.mathematics.standardfunctions;

import math4u2.mathematics.functions.ScalarBinaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;

public class ModFunction extends ScalarBinaryStandardFunction {

	public ModFunction() {
		name = "mod";	
		summaryText = "Rest nach Division von x durch y";
	}
	
	public String[] getArgumentStrings() {
		return new String[] {"Dividend x", "Divisor y"};
	}

	public Object eval(Object[] args) {
		double a = ((ScalarDoubleResult) args[0]).value;
		double b = ((ScalarDoubleResult) args[1]).value;
		
		
		if ( a == 0.0 ) return new ScalarDoubleResult(0.0);
		
		else {
			double aabs = Math.abs(a);
			double babs = Math.abs(b);
			double resabs = aabs - ( Math.floor(aabs/babs))*babs;
			if (a > 0.0) {
				return new ScalarDoubleResult(resabs);
			}
			else {
				return new ScalarDoubleResult( - resabs);
			}
		}
	}
}