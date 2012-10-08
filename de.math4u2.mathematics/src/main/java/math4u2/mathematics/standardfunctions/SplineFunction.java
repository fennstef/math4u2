package math4u2.mathematics.standardfunctions;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscBinaryStandardFunction;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.ScalarType;

public class SplineFunction extends MiscBinaryStandardFunction {

    public SplineFunction() {
		name = "spline";
		summaryText = "Kubische Spline-Interpolation:"+ 
		              "<br>Wert der kubischen Spline-Interpolatoin an der Stelle x." +
		"<br>Die Koeffizienten sc der Interpolation " +
		"<br>werden mit der Funktion splinecoeff(xv, yv) berechnet.";
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
	
		double[][]  polyCoeff = koeff.valueArray;
		int dim = polyCoeff.length-1;	
		
		double x = ((ScalarDoubleResult) args[1]).value;
		
		if ( x < polyCoeff[0][4] ) return new ScalarDoubleResult(Double.NaN);
		if ( x > polyCoeff[dim][4] ) return new ScalarDoubleResult(Double.NaN);

		int ii = 0;
		while( x >= polyCoeff[ii][4]) ii++;
	    ii--;
	    
		double xdiff = x-polyCoeff[ii][4];
		
		double result = polyCoeff[ii][0]+polyCoeff[ii][1]*xdiff+polyCoeff[ii][2]*xdiff*xdiff+ 
                        polyCoeff[ii][3]*xdiff*xdiff*xdiff;
		
		return new ScalarDoubleResult(result);
	} //eval
	
	
	public String[] getArgumentStrings() {
		return new String[] {"Spline-Koeffizienten", "x"};
	}
	
	public String[] getArgumentTexts() {
		return new String[] {"Die Spline-Koeffizienten werden" +
				"<br>mit der Funktion splinecoeff(xv, yv) berechnet", 
				"Für die Stelle x wird der Wert " +
				"<br>der Spline-Interpolation berechnet."};
	}


} //class SplineFunction
