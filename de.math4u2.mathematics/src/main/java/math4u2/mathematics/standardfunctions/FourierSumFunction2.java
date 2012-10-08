package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscTernaryStandardFunction;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.types.VectorType;
import math4u2.mathematics.types.ScalarType;

public class FourierSumFunction2 extends MiscTernaryStandardFunction {

	public FourierSumFunction2() {
		name = "fouriersumme2";
	}

	public Class getResultType(Class[] argTypes) {
		return ScalarType.class;
	} //getResultType

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
			return VectorType.class;
		case 1:
			return VectorType.class;
		case 2:
			return ScalarType.class;

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		} //switch
	} //getVariableType

	public Object eval(Object[] args) throws MathException {
		VectorDoubleResult ampl = (VectorDoubleResult) (args[0]);
		VectorDoubleResult phas = (VectorDoubleResult) (args[1]);
		ScalarDoubleResult arg = (ScalarDoubleResult) (args[2]);

		double result = 0.0; 
		for ( int i = 0; i < ampl.rowDim ; i ++ ) {
			result += ampl.valueArray[i][0]*Math.cos(i*arg.value- phas.valueArray[i][0]);
		}
		
		return new ScalarDoubleResult(result);	
	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		throw new MathException(
				"derive fuer Fourier-Summe nicht definiert");
	}

	

} //FourierSumFunction2
