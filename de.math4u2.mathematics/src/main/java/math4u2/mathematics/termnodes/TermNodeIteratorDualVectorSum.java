/*
 * Created on 20.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package math4u2.mathematics.termnodes;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.DoubleResult;
import math4u2.mathematics.results.DualVectorDoubleResult;

/**
 * @author Dr. Weiss
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TermNodeIteratorDualVectorSum extends TermNodeIteratorMatrixSum {
	
	public TermNodeIteratorDualVectorSum(ScalarVariable localVar, TermNode fromTerm,
			TermNode toTerm, TermNode function) throws MathException {
		super(localVar, fromTerm, toTerm, function);
	}

	/**
	 * Fuehrt die Summe aus und liefert den Ergebnisvektor als DualVectorDoubleResult zurueck.
	 * Im Fall from > to wird der vom Typ her passende Nullvektor zurueckgegeben.
	 */
	
protected DoubleResult evalSumOrProd(int from, int to) throws MathException {
	return new DualVectorDoubleResult(evalSumArray(from, to));
}

	/**
	 * Wie evalSumOrProduct, Terme werden nur insoweit neu ausgewertet, als sie 
	 * Variablen enthalten.
	 */
protected DoubleResult shallowEvalSumOrProd(int from, int to) throws MathException {
	return new DualVectorDoubleResult(shallowEvalSumArray(from, to));
}

}