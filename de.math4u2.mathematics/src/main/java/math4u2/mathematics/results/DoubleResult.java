package math4u2.mathematics.results;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.termnodes.TermNode;

public abstract class DoubleResult implements Result {

	public abstract double evalScalar() throws MathException;
	
	/** 
	 * @return 
	 */
	public abstract TermNode getNullTerm();
	
	
	/**
	 * @return Die Klasse, die den zugehoerigen Datentyp beschreibt,
	 * z.B. bei einem Vektor die Klasse types.VectorType
	 */
	public abstract Class getDataType();

}//class DoubleResult
