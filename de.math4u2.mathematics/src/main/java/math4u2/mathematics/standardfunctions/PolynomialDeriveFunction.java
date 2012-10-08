package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.TernaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.termnodes.TermNodeNum;
import math4u2.mathematics.types.ScalarType;
import math4u2.mathematics.types.VectorType;


/**
 * @author Dr. Weiss
 * Die drei Variablen sind:
 * - position 0: Vektorwertiger Term, der den Koeffizientenvektor v des Polynoms liefert.
 *               v[0] zu x ^0, v[1] zu x^1, ...
 * - position 1: Skalarwertiger Term, der den Argumentwert x für die Auswertung liefert.
 * - position 2: Skalarwertiger Term mit Wert 0,1,2,... , der die Ordnung der 
 *               Ableitung festlegt.
 */

public class PolynomialDeriveFunction extends TernaryStandardFunction {

	public PolynomialDeriveFunction() {
		name = "polynom_derive";
	}

	public Class getResultType(Class[] argTypes) {
		return ScalarType.class;
	} //getResultType

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
			return VectorType.class;
		case 1:
			return ScalarType.class;
		case 2:
			return ScalarType.class;

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		} //switch
	} //getVariableType

	
	/**
	 * Ueberprueft, ob das erste Argument (der Koeffizientenvektor) 
	 * oder das dritte Argument (Ordnung der Ableitung)
	 * eine Variable enthaelt. 
	 * Wenn ja, dann wird eine entsprechende Ausnahme erzeugt.
	 */	
	public void reportArgumentConflict(TermNode[] arguments) throws MathException{
		if( arguments[0].hasVar()) 
			throw new MathException("Das erste Argument bei " + name + " darf keine Variable enthalten");
		if( arguments[2].hasVar()) 
			throw new MathException("Das dritte Argument bei " + name + " darf keine Variable enthalten");
	}
	
	
	/**
	 * Berechnet den Polynom-Wert mit dem Horner-Schema.
	 * Dabei wird davon ausgegangen, dass der Koeffizientenvektor in args[0]
	 * keine Variable enthält.
	 */
	public Object eval(Object[] args) throws MathException {
		VectorDoubleResult koeff = (VectorDoubleResult) (args[0]);
		ScalarDoubleResult arg = (ScalarDoubleResult) (args[1]);
		ScalarDoubleResult ordDouble = (ScalarDoubleResult) (args[2]);  
		
		// test auf Ordnung >=0 
		if ( ordDouble.value < 0 )
			throw new MathException("Ordnung der Ableitung in " + name + " ist negativ");
			
		int ord = (int)ordDouble.value;  // Ordnung der Ableitung
		
		// test auf ganzzahlige Ordnung
		if ( ordDouble.value % 1 != 0 )
			throw new MathException("Ordnung der Ableitung in " + name + " ist keine ganze Zahl");
			
		if ( ord >= koeff.rowDim ) 
			return new ScalarDoubleResult(0.0);
		
		int len = koeff.rowDim - ord;    // Anzahl der Koeffizienten
		
		// Koeffizienten bereitstellen
		double[] derivekoeff = new double[len];
		
		for( int row = 0; row < len; row ++) {
			derivekoeff[row] = koeff.valueArray[row+ord][0];
			for ( int c = 1; c <= ord; c++){
				derivekoeff[row] *= c+row;
			}
		}		
		// Wert berechnen
		double result = derivekoeff[len-1]; 
		for ( int row = len-2; row>=0; row -- ) {
			result = result*arg.value + derivekoeff[row];
		}		
		return new ScalarDoubleResult(result);	
	} //eval

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		Function prod, sum;
		try {
			prod = (Function) (broker.getObject("prod"));
			sum = (Function) (broker.getObject("sum"));
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
		
		// Neue Ordnung: um 1 erhoehen
		TermNode newOrderTerm = new TermNodeFunct(sum, 
				                                  new TermNode[] { argumentTerms[2],
				                                                   new TermNodeNum(1.0)}, 
												  broker);
		// f'(g(x))
		TermNode temp = new TermNodeFunct(this, 
				new TermNode[] { argumentTerms[0], argumentTerms[1], newOrderTerm }, broker );
		// f'(g(x))*g'(x)
		return new TermNodeFunct(prod, new TermNode[] {temp, derivedArgumentTerms[1] }, broker);
	}

	

} //PolynomialDeriveFunction
