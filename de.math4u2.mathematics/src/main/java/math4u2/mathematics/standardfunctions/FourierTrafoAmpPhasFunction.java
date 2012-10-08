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
/* Fourier-Trafo Amplituden und Phasen
 * Das Argument ist ein Vektor, dessen Lanege eine Zweierpotenz ist, 
 * z.B. 32. Dieser Daten-Vektor enthaelt die zu transformierenden Daten.
 * Erstellt fuer einen Daten-Vektor der Laenge 32 ein Array mit zwei Spalten
 * und 17 ( = 32/2 +1 ) Zeilen.
 * In der ersten Spalte stehen die Gesamt-Amplituden fuer aufsteigende Frequenzen, 
 * in der zweiten Spalte die zugehörigen Phasen.
 * 
 * Analog statt fuer 32 fuer andere Laengen, z.B. 64, 128, ...
 */

public class FourierTrafoAmpPhasFunction extends MiscUnaryStandardFunction {

	public FourierTrafoAmpPhasFunction() {
		name = "ftap";
		summaryText = "Fourier-Transformation in Amplitude und Phase"+
        "<br>Das Ergebnis ist eine Matrix,"+
        "<br>die erste Spalte enthält die Amplituden,"+
        "<br>die zweite Spalte die zugehoerigen Phasen,"+
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
		
        double[] values = new double[nn];
        
        for ( int i = 0; i < nn; i ++){
			values[i] = f[i][0];  
		}
		      
        double[][] transformedData = FourierAlgorithms.ffap(values);
        
	
		
		return new MatrixDoubleResult(transformedData);
	} //eval

	/**
	 * Ableitung nicht moeglich, erzeugt Ausnahme
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Ableitung bei Fourier-Trafo nicht moeglich");
	} // getDeriveFunction

}