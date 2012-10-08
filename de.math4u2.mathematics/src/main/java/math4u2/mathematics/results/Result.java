package math4u2.mathematics.results;

import math4u2.mathematics.functions.MathException;

/**
 * @author Fenn Stefan
 *
 * Allgemeiner Ergebnistyp.
 */
public interface Result {
	
	/**
	 * @return String, der den zugehoerigen Datentyp beim Parsen identifiziert,
	 * z.B. bei einem Vektor der String "vektor".
	 */
	public abstract String getTypeString();
	public abstract double getScalar() throws MathException;
	public abstract double[] getVector() throws MathException;
	public abstract double[][] getMatrix() throws MathException;
}
