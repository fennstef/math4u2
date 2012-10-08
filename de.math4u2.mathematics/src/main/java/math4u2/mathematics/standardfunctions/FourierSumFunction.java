package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscBinaryStandardFunction;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.ScalarType;

public class FourierSumFunction extends MiscBinaryStandardFunction {

	public FourierSumFunction() {
		name = "fouriersumme";
	}

	public Class getResultType(Class[] argTypes) {
		return ScalarType.class;
	} //getResultType

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
			return MatrixType.class;
		case 1:
			return ScalarType.class;

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		} //switch
	} //getVariableType

	public Object eval(Object[] args) throws MathException {
		MatrixDoubleResult koeff = (MatrixDoubleResult) (args[0]);
		ScalarDoubleResult arg = (ScalarDoubleResult) (args[1]);

		double result = 0.0; 
		for ( int i = 0; i < koeff.rowDim ; i ++ ) {
			result += koeff.valueArray[i][0]*Math.cos(i*arg.value)+ koeff.valueArray[i][1]*Math.sin(i*arg.value);
		}
		
		return new ScalarDoubleResult(result);	
	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		throw new MathException(
				"derive fuer Fourier-Summe nicht definiert");
	}

	

} //FourierSumFunction
