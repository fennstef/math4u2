package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscUnaryStandardFunction;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.types.MatrixType;

/**
 * Fourier-Transformation
 */

public class FourierTrafoFunction extends MiscUnaryStandardFunction {

	public FourierTrafoFunction() {
		name = "ft";
	}//Konstruktor 1

	// Ergebnistyp: Matrix
	public final Class getResultType(Class[] argTypes) {
		return MatrixType.class;
	}

	// Genau eine Variable vom Typ Vektor
	public final Class getVariableType(int pos) throws MathException {
		if (pos == 0)
			return VectorDoubleResult.class;

		throw new MathException("Funktion " + name
				+ " hat keine Variable an der Stelle " + pos);
	}

	/**
	 * Gibt den errechneten Funktionswert zurueck
	 */
	public Object eval(Object[] args) throws MathException {

		int dim = ((VectorDoubleResult) args[0]).rowDim;
		if (dim % 2 != 0)
			throw new MathException(
					"Fourier-Trafo nur bei einer geraden Anzahl von Elementen");

		double[][] f = ((MatrixDoubleResult) args[0]).valueArray;
		int halfDim = dim / 2;
		double[][] resultArray = new double[halfDim + 1][4];
		double xStep = 2.0 * Math.PI / dim;

		
		// cos-Amplituden
		double element = 0;
		for (int k = 0; k < dim; k++) {
			element += f[k][0];
		}
		resultArray[0][0] = element/dim;
		
		for (int i = 1; i <= halfDim; i++) {
			element = 0;
			for (int k = 0; k < dim; k++) {
				element += f[k][0] * Math.cos(i * k * xStep);
			}
			resultArray[i][0] = element / halfDim;
		}
		resultArray[halfDim][0] /= 2;
		
         
		// sin-Amplituden
		resultArray[0][1] = 0;
		resultArray[halfDim][1] = 0;

		for (int i = 1; i < halfDim; i++) {
			element = 0;
			for (int k = 0; k < dim; k++) {
				element += f[k][0] * Math.sin(i * k * xStep);
			}
			resultArray[i][1] = element / halfDim;
		}

		// Gesamt-Amplituden
		double maxAmp = 0;
		//resultArray[0][2] = resultArray[0][0];
		//maxAmp = resultArray[0][2];
		//resultArray[halfDim][2] = resultArray[halfDim][0];
		for (int i = 0; i <= halfDim; i++) {
			
			double amp = Math.sqrt(    resultArray[i][0] * resultArray[i][0]																		
									 + resultArray[i][1] * resultArray[i][1]	 );
			resultArray[i][2] = amp;
			if( amp > maxAmp) maxAmp = amp;	
		}
		
		if ( maxAmp == 0 ) maxAmp = 1;
		
		// Phasen
		//resultArray[0][3] = 0;
		//resultArray[halfDim][3] = 0;
		for (int i = 0; i <= halfDim; i++) {
		    if( resultArray[i][2]/ maxAmp < 1.0E-7 ) 
		    	 resultArray[i][3] = 0;
			else 
				resultArray[i][3] = Math.atan2(    resultArray[i][1] , resultArray[i][0]);
		}	
		return new MatrixDoubleResult(resultArray);
	} //eval

	/**
	 * Ableitung nicht moeglich, erzeugt Ausnahme
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Ableitung bei Fourier-Trafo nicht moeglich");
	} // getDeriveFunction

}