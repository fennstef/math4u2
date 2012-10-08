/*
 * Created on 20.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package math4u2.mathematics.termnodes;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.*;

/**
 * @author Dr. Weiss
 * Summen-Iterator fuer einen skalaren Summanden.
 */
public class TermNodeIteratorScalarSum extends TermNodeIteratorSum {

	public TermNodeIteratorScalarSum(ScalarVariable localVar, TermNode fromTerm,
			TermNode toTerm, TermNode function) throws MathException {
		super(localVar, fromTerm, toTerm, function);
	}

	/**
	 * Stellt den Summationsindex in Einserschritten aufsteigend von from bis to,
	 * berechnet jeweils den Wert des Summanden (functionTerm),summiert die Ergebnisse auf 
	 * und liefert das Ergebnis als ScalarDoubleResult zurueck.
	 * Im Fall from > to wird das Ergebnis 0 zurueckgegeben.
	 */
	protected DoubleResult evalSumOrProd(int from, int to) throws MathException {
		double returnValue = 0;
		
		
		//Eval muß auf jeden Fall einmal aufgerufen werden, ansonsten
		//kann shalowEvalSumOrProduct falsche Ergebnisse zurückliefern
		if(from>=to){
		    localVar.setValue(new ScalarDoubleResult(from));
		    functionTerm.evalScalar();
		}
		
		for (int i = from; i <= to; i++) {
			localVar.setValue(new ScalarDoubleResult(i));
			returnValue = returnValue + functionTerm.evalScalar();
		}
		return new ScalarDoubleResult(returnValue);
	}
	
	/**
	 * Wie evalSumOrProduct, Terme werden nur insoweit neu ausgewertet, als sie 
	 * Variablen enthalten.
	 */
	protected DoubleResult shallowEvalSumOrProd(int from, int to) throws MathException {
		double returnValue = 0;
		for (int i = from; i <= to; i++) {
			localVar.setValue(new ScalarDoubleResult(i));		
			returnValue = returnValue + functionTerm.shallowEvalScalar();
			// returnValue = returnValue + functionTerm.evalScalar();
		}
		return new ScalarDoubleResult(returnValue);
	}
	

}
