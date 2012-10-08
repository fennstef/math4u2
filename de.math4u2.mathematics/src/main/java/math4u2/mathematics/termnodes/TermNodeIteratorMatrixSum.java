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
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TermNodeIteratorMatrixSum extends TermNodeIteratorSum {
 
	
	public TermNodeIteratorMatrixSum(ScalarVariable localVar, TermNode fromTerm,
			TermNode toTerm, TermNode function) throws MathException {
		super(localVar, fromTerm, toTerm, function);
	}
	
	/**
	 * Stellt den Summationsindex in Einserschritten aufsteigend von from bis to,
	 * berechnet jeweils den Wert des Summanden (functionTerm),summiert die Ergebnisse auf 
	 * und liefert die Ergebnismatrix als Array zurueck.
	 * Im Fall from > to wird ein vom Typ her passendes Array mit 0-Werten zurueckgegeben.
	 */
	
	protected double[][] evalSumArray(int from, int to) throws MathException {
		
		localVar.setValue(new ScalarDoubleResult(from));
		MatrixDoubleResult res = (MatrixDoubleResult)(functionTerm.eval());
		
		// leere Summe, liefert die vom Typ her passende Nullmatrix
		if ( from > to ) {
			return res.getNullResultMatrix().valueArray;
		}
		
		int rowDim = res.rowDim;
		int colDim = res.colDim; 
		
		
		//Eval muß auf jeden Fall einmal aufgerufen werden, ansonsten
		//kann evalSumArray falsche Ergebnisse zurückliefern
		if(from>=to){
		    localVar.setValue(new ScalarDoubleResult(from));
		    functionTerm.eval();
		}
		
		for (int i = from + 1; i <= to; i++) {
			localVar.setValue(new ScalarDoubleResult(i));
			MatrixDoubleResult newRes = (MatrixDoubleResult)(functionTerm.eval());
			for ( int row = 0; row <rowDim; row++ ){
				for ( int col = 0; col < colDim; col++){
					res.valueArray[row][col] = res.valueArray[row][col] + newRes.valueArray[row][col];
				}
			}
		}
		return res.valueArray;
	}
	
	/**
	 * Wie evalSumArray, Terme werden nur insoweit neu ausgewertet, als sie 
	 * Variablen enthalten.
	 */
	
	protected double[][] shallowEvalSumArray(int from, int to) throws MathException {
		
		localVar.setValue(new ScalarDoubleResult(from));
		MatrixDoubleResult res = (MatrixDoubleResult)(functionTerm.shallowEval());
		
		// leere Summe, liefert die vom Typ her passende Nullmatrix
		if ( from > to ) {
			return res.getNullResultMatrix().valueArray;
		}
		
		int rowDim = res.rowDim;
		int colDim = res.colDim; 
		
		for (int i = from + 1; i <= to; i++) {
			localVar.setValue(new ScalarDoubleResult(i));
			MatrixDoubleResult newRes = (MatrixDoubleResult)(functionTerm.shallowEval());
			for ( int row = 0; row <rowDim; row++ ){
				for ( int col = 0; col < colDim; col++){
					res.valueArray[row][col] = res.valueArray[row][col] + newRes.valueArray[row][col];
				}
			}
		}
		return res.valueArray;
	}

	
	
	/**
	 * Fuehrt die Summe aus und liefert die Ergebnismatrix als MatrixDoubleResult zurueck.
	 * Im Fall from > to wird die vom Typ her passende Nullmatrix zurueckgegeben.
	 */
	protected DoubleResult evalSumOrProd(int from, int to) throws MathException {
		return new MatrixDoubleResult(evalSumArray(from, to));
	}
	
	/**
	 * Wie evalSumOrProduct, Terme werden nur insoweit neu ausgewertet, als sie 
	 * Variablen enthalten.
	 */
	protected DoubleResult shallowEvalSumOrProd(int from, int to) throws MathException {
		return new MatrixDoubleResult(shallowEvalSumArray(from, to));
	}

}
