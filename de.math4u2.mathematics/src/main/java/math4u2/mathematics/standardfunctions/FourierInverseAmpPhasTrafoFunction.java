package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscBinaryStandardFunction;
import math4u2.mathematics.numerics.FourierAlgorithms;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.types.VectorType;

/**
 * Fourier-Transformation
 */

public class FourierInverseAmpPhasTrafoFunction extends MiscBinaryStandardFunction {

	public FourierInverseAmpPhasTrafoFunction() {
		name = "ftiap";
		summaryText = 	"Inverse Fourier-Transfomation aus Amplituden und Phasen."+
        				"<br>Das Ergebnis ist ein Vektor."+
        				"<br>Er enthält die ruecktransformierten Daten.";
	}//Konstruktor 1

	public String[] getArgumentStrings() {
		return new String[] {"Amplituden", "Phasen"};
	}
	
	public String[] getArgumentTexts() {
		return new String[] {"Vektor der Gesamt-Amplituden in aufsteigender Frequenz."+ 
				             "<br>Die Anzahl der Elemente muss (Potenz von 2) + 1 sein,"+
				             "<br>also z.B. 4+1=5 oder 8+1=9 oder 17 oder ...",
				             "Vektor der Phasen in aufsteigender Frequenz."+ 
				             "<br>Die Anzahl der Elemente muss (Potenz von 2) + 1 sein,"+
				             "<br>also z.B. 4+1=5 oder 8+1=9 oder 17 oder ..."
				             };
	}
	
	
	
	// Ergebnistyp: Matrix
	public final Class getResultType(Class[] argTypes) {
		return VectorType.class;
	}

	// Genau eine Variable vom Typ Matrix
	public final Class getVariableType(int pos) throws MathException {
		if (pos == 0 || pos == 1)
			return VectorType.class;

		throw new MathException("Funktion " + name
				+ " hat keine Variable an der Stelle " + pos);
	}

	/**
	 * Gibt den errechneten Funktionswert zurueck
	 */
	public Object eval(Object[] args) throws MathException {
        //int nn = ((VectorDoubleResult) args[0]).rowDim;
		
		double[] amparr = ((VectorDoubleResult) args[0]).getVector();
		double[] phasarr = ((VectorDoubleResult) args[1]).getVector();
		
		return new VectorDoubleResult(VectorDoubleResult.newVectorDoubleArray(FourierAlgorithms.fiap(amparr,phasarr)));
	} //eval

	/**
	 * Ableitung nicht moeglich, erzeugt Ausnahme
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Ableitung bei Fourier-Trafo nicht moeglich");
	} // getDeriveFunction

}