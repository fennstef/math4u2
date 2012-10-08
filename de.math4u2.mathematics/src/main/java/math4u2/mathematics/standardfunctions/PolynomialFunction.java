package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.MiscBinaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.termnodes.TermNodeNum;
import math4u2.mathematics.types.ScalarType;
import math4u2.mathematics.types.VectorType;

/**
 * Polynom-Funktion zur schnellen Berechnung von Polynomwerten mit dem
 * Horner-Schema.
 * Die beiden Variablen sind:
 * - position 0: Vektorwertiger Term, der den Koeffizientenvektor v des Polynoms liefert.
 *               v[0] zu x ^0, v[1] zu x^1, ...
 * - position 1: Skalarwertiger Term, der den Argumentwert x für die Auswertung liefert.
 * @author Dr. Weiss
 */

public class PolynomialFunction extends MiscBinaryStandardFunction {

	public PolynomialFunction() {
		name = "polynom";
		summaryText = "Wert des Polynoms mit Koeffizienten cv an der Stelle x";
	}
	
	public String[] getArgumentStrings() {
		return new String[] {"Koeffizienten", "Argument"};
	}

	public Class getResultType(Class[] argTypes) {
		return ScalarType.class;
	} //getResultType

	public Class getVariableType(int pos) throws MathException {
		switch (pos) {
		case 0:
			return VectorType.class; // enthaelt die Koeffizienten des Polynoms
		case 1:
			return ScalarType.class; // Stelle, an der der Wert berechnet wird

		default:
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
		} //switch
	} //getVariableType

	
	/**
	 * Ueberprueft, ob das erste Argument (der Koeffizientenvektor) eine 
	 * Variable enthaelt. 
	 * Wenn ja, dann wird eine entsprechende Ausnahme erzeugt.
	 */	
	public void reportArgumentConflict(TermNode[] arguments) throws MathException{
		if( arguments[0].hasVar()) 
			throw new MathException("Das erste Argument bei " + name + " darf keine Variable enthalten");
	}
	
	
	/**
	 * Berechnet den Polynom-Wert mit dem Horner-Schema.
	 * Dabei wird davon ausgegangen, dass der Koeffizientenvektor in args[0]
	 * keine Variable enthält.
	 */
	public Object eval(Object[] args) throws MathException {
		VectorDoubleResult koeff = (VectorDoubleResult) (args[0]);
		ScalarDoubleResult arg = (ScalarDoubleResult) (args[1]);

		int lastRow = koeff.rowDim - 1;
		double result = koeff.valueArray[lastRow][0]; 
		for ( int row = lastRow -1; row>=0; row -- ) {
			result = result*arg.value + koeff.valueArray[row][0];
		}
		
		return new ScalarDoubleResult(result);	
	} //eval

	
	
	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		Function prod, der;

		try {
			prod = (Function) (broker.getObject("prod"));
			der = (Function) (broker.getObject("polynom_derive"));
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}

		TermNode temp = new TermNodeFunct(der, 
				new TermNode[] { argumentTerms[0], argumentTerms[1], new TermNodeNum(1.0) }, broker );
		return new TermNodeFunct(prod, new TermNode[] {temp, derivedArgumentTerms[1] }, broker);
	}

	

} //PolynomialFunction
