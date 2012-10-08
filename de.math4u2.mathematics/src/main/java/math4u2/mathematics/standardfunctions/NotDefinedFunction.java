package math4u2.mathematics.standardfunctions;

import math4u2.controller.*;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.TernaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.termnodes.TermNodeNum;
import math4u2.mathematics.types.ScalarType;

/**
 * 
 * @version 1.0
 */

public class NotDefinedFunction extends TernaryStandardFunction {

	public NotDefinedFunction() {
		name = "not_defined_at";
	}

	public Class getResultType(Class[] argTypes) {
		return ScalarType.class;
	} //getResultType

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
			return ScalarType.class;  //  eigentliche Variable
		case 1:
			return ScalarType.class;  //  Definitionsluecke
		case 2:
			return ScalarType.class;  // Ordnung der Ableitung

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		} //switch
	} //getVariableType

	
	/**
	 * Ueberprueft, ob das zweite Argument (Definitionsluecke) 
	 * oder das dritte Argument (Ordnung der Ableitung)
	 * eine Variable enthaelt. 
	 * Wenn ja, dann wird eine entsprechende Ausnahme erzeugt.
	 */	
	public void reportArgumentConflict(TermNode[] arguments) throws MathException{
		if( arguments[1].hasVar()) 
			throw new MathException("Das zweite Argument bei " + name + " darf keine Variable enthalten");
		if( arguments[2].hasVar()) 
			throw new MathException("Das dritte Argument bei " + name + " darf keine Variable enthalten");
	}
	
	
	/**
	 * Berechnet den Wert
	 */
	public Object eval(Object[] args) throws MathException {
		ScalarDoubleResult arg = (ScalarDoubleResult) (args[0]);
		ScalarDoubleResult gap = (ScalarDoubleResult) (args[1]);
		ScalarDoubleResult ordDouble = (ScalarDoubleResult) (args[2]);  
		
		// test auf Ordnung >=0 
		if ( ordDouble.value < 0 )
			throw new MathException("Ordnung der Ableitung in " + name + " ist negativ");
			
		int ord = (int)ordDouble.value;  // Ordnung der Ableitung
		
		// test auf ganzzahlige Ordnung
		if ( ord-ordDouble.value != 0 )
			throw new MathException("Ordnung der Ableitung in " + name + " ist keine ganze Zahl");
			
		if ( arg.value == gap.value)
			return new ScalarDoubleResult(java.lang.Double.NaN);
		
		switch (ord) {
		    case 0:  return arg; 
		    case 1:  return new ScalarDoubleResult(1.0); 
		    default: return new ScalarDoubleResult(0.0); 
		}
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
		return new TermNodeFunct(prod, new TermNode[] {temp, derivedArgumentTerms[0] }, broker);
	}

	public TermNode simplify(TermNode oldTerm, TermNode[] arguments) {
		return oldTerm;
	} //simplify

} //NotDefinedFunction
