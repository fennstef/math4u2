package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscUnaryStandardFunction;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.VectorType;
import math4u2.mathematics.numerics.*;

/**
 * Fourier-Transformation
 */

public class FourierTrafoCosSinFunction extends MiscUnaryStandardFunction {

	public FourierTrafoCosSinFunction() {
		name = "ftcs";
		summaryText = "Fourier-Transformation in cos- und sin-Amplituden"+
		              "<br>Das Ergebnis ist eine Matrix,"+
		              "<br>die erste Spalte enthält die cos-Amplituden,"+
		              "<br>die zweite Spalte die sin-Amplituden,"+
		              "<br>jeweils in aufsteigender Frequenz.";
	}//Konstruktor 1

	// Ergebnistyp: Matrix
	public final Class getResultType(Class[] argTypes) {
		return MatrixType.class;
	}

	public String[] getArgumentStrings() {
		return new String[] {"Datenvektor"};
	}
	
	public String[] getArgumentTexts() {
		return new String[] {"Vektor mit den zu transformierenden Daten"+ 
				"<br>Die Anzahl der Elemente muss eine Potenz von 2 sein,"+
		"<br>z.B. 16, 64, 512,..."};
	}
	
	// Genau eine Variable vom Typ Vektor
	public final Class getVariableType(int pos) throws MathException {
		if (pos == 0)
			return VectorType.class;

		throw new MathException("Funktion " + name
				+ " hat keine Variable an der Stelle " + pos);
	}

	/**
	 * Gibt den errechneten Funktionswert zurueck
	 */
	public Object eval(Object[] args) throws MathException {


		int nn = ((VectorDoubleResult) args[0]).rowDim;
		
		double[][] f = ((MatrixDoubleResult) args[0]).valueArray;
		
        double[] values = new double[2*nn];
        
        for ( int i = 0; i < nn; i ++){
			values[2*i] = f[i][0];  
			values[2*i+1] = 0;
		}
		      
        double[][] transformedData = FourierAlgorithms.ffcs(values);
        
	
		
		return new MatrixDoubleResult(transformedData);
	} //eval

	/**
	 * Ableitung nicht moeglich, erzeugt Ausnahme
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Ableitung bei Fourier-Trafo nicht moeglich");
	} // getDeriveFunction

}