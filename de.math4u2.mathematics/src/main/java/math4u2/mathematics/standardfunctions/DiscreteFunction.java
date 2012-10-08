package math4u2.mathematics.standardfunctions;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscTernaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.types.ScalarType;
import math4u2.mathematics.types.VectorType;

public class DiscreteFunction extends MiscTernaryStandardFunction {

    public DiscreteFunction() {
		name = "diskret";
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
				
		double[][] xv = ((VectorDoubleResult) (args[0])).valueArray;
		double[][] yv = ((VectorDoubleResult) (args[1])).valueArray;
		double x = ((ScalarDoubleResult)(args[2])).value;
			
		int length = xv.length;		
		
		for( int i=0; i < length; i++) {
			if ( x == xv[i][0]) return new ScalarDoubleResult(yv[i][0]);
		}

		return new ScalarDoubleResult( Double.NaN);
	} //eval
	
	/**
	 * Gibt das Array der diskreten x-Stuetzwerte zurueck.
	 * @param args
	 * @return
	 * @throws MathException
	 */
	public double [] getXArray(Object[] args) throws MathException{
		double[][] xv = ((VectorDoubleResult) (args[0])).valueArray; 
		int len = xv.length;
		double [] result = new double[len];
		for ( int i = 0; i < len; i++) 
			result[i] = xv[i][0];
		
		return result;
	}

} //class DiscreteFunction
