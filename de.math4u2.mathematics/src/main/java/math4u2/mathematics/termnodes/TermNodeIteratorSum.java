package math4u2.mathematics.termnodes;

import math4u2.mathematics.functions.MathException;

/**
 * Abstrakter Summen-Itertor. Abgeleitete Klassen fuer 
 * verschiedene Typen von Summanden: Skalar, Matrix, Vektor, ... 
 * @author Dr. Weiss
 * 
 */
public abstract class TermNodeIteratorSum extends TermNodeIterator {
	
	
	public TermNodeIteratorSum(ScalarVariable localVar, TermNode fromTerm,
			TermNode toTerm, TermNode function) throws MathException {
		super( localVar, fromTerm, toTerm, function);
	}
	
	/**
	 * Gibt den Summen-Iterator zurück, der entsteht,
	 * wenn man den Funktionsterm ableitet. 
	 */
	public TermNode derive(Variable var) throws MathException {	
		return TermNodeIterator.makeIterator("sum", localVar, fromTerm, toTerm,
					functionTerm.derive(var));
	}

	public String getTypeString() {
		return "sum";
	}
    
}