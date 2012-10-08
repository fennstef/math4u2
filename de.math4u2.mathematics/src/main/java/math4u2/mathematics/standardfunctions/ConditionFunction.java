package math4u2.mathematics.standardfunctions;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.TernaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.types.ScalarType;




public class ConditionFunction extends TernaryStandardFunction {

	public ConditionFunction() {
		name = "cond";
		summaryText = "Berechnet den Wert B des Terms bedingung."+
						"<br>Im Fall B < 0 ist das Ergebnis der Wert von term1,"+
						"<br>sonst ist das Ergebnis der Wert von term2.";
	}
	
	public String[] getArgumentStrings() {
		return new String[] {"Bedingung", "term1", "term2"};
	}

	public Class getResultType(Class[] argTypes) {
		return ScalarType.class;
	} //getResultType

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
			return ScalarType.class;
		case 1:
			return ScalarType.class;
		case 2:
			return ScalarType.class;

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		} //switch
	} //getVariableType

	
	
	
	
	
	public Object eval(Object[] args) throws MathException {
		double condVal = ((ScalarDoubleResult) (args[0])).value;
		ScalarDoubleResult val1 = (ScalarDoubleResult) (args[1]);
		ScalarDoubleResult val2 = (ScalarDoubleResult) (args[2]);  
		
		if ( condVal < 0) return val1;
		else return val2;
		
	} //eval

	
	

} //ConditionFunction
