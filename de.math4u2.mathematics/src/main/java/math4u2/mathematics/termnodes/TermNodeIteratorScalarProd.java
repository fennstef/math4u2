/*
 * Created on 21.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package math4u2.mathematics.termnodes;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.DoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;

/**
 * @author Dr. Weiss
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TermNodeIteratorScalarProd extends TermNodeIterator {
	
	public TermNodeIteratorScalarProd(ScalarVariable localVar, TermNode fromTerm,
			TermNode toTerm, TermNode function) throws MathException {
		super(localVar, fromTerm, toTerm, function);
	}
	
	protected DoubleResult evalSumOrProd(int from, int to) throws MathException {
		double returnValue = 1;
		
		//Eval muß auf jeden Fall einmal aufgerufen werden, ansonsten
		//kann shalowEvalSumOrProduct falsche Ergebnisse zurückliefern
		if(from>=to){
		    localVar.setValue(new ScalarDoubleResult(from));
		    functionTerm.evalScalar();
		}
		
		for (int i = from; i <= to; i++) {
			localVar.setValue(new ScalarDoubleResult(i));
			returnValue = returnValue * functionTerm.evalScalar();
		}
		return new ScalarDoubleResult(returnValue);
	}
	

	protected DoubleResult shallowEvalSumOrProd(int from, int to) throws MathException {
		double returnValue = 1;
		for (int i = from; i <= to; i++) {
			localVar.setValue(new ScalarDoubleResult(i));
			returnValue = returnValue * functionTerm.shallowEvalScalar();
		}
		return new ScalarDoubleResult(returnValue);
	}
	
	/***************************************************************************
	 * Erzeugt eine Ausnahme.
	 */
	public TermNode derive(Variable var) throws MathException {
			throw new MathException(
					"Ableitung für Produkt-Iterator nicht verfügbar");
	}
	
	public String getTypeString() {
		return "prod";
	}

}
