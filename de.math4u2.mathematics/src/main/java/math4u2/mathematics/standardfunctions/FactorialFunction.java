package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.ScalarUnaryStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;

/**
 * <p>
 * Fakultaet
 * </p>
 * 
 * @version 1.0
 */
public class FactorialFunction extends ScalarUnaryStandardFunction {
	int lastArgument = 0; // speichert das Argument aus dem letzten Aufruf.

	double lastValue = 1; // speichert den Funktionswert zu lastArgument.

	public FactorialFunction() {
		name = "fac";
	} //Konstruktor 1

	public String buildTermString(String[] argStrings, String n) {
		return argStrings[0] + "!";
	}

	/**
	 * Gibt den errechneten Funktionswert zurueck. Falls das Argument args[0]
	 * keinen Wert aus 0, 1, 2, .. hat, wird eine Ausnahme erzeugt.
	 */
	public Object eval(Object[] args) throws MathException {
		double value = ((ScalarDoubleResult) args[0]).value;
		int facArg = (int) value;
		/*
		 * Eine Berechnung ist nur sinnvoll, wenn das Argument eine Ganzzahl >=
		 * 0 ist.
		 */
		if ((value - facArg) == 0.0f && facArg >= 0) {
			/*
			 * Wenn das aktuelle Argument facArg kleiner ist als das beim
			 * letzten Aufruf vorliegende, dann muss ausgehend von lastArgument =
			 * 0 und lastValue = 1 neu berechent werden.
			 */
			if (facArg < lastArgument) {
				lastValue = 1;
				lastArgument = 0;
			}
			/* ansonsten wird beim letzten Ergebnis weitergerechnet. */

			for (int i = lastArgument + 1; i <= facArg; i++)
				lastValue *= i;

			lastArgument = facArg;
			return new ScalarDoubleResult(lastValue);
		} //if
		/* ansonsten wird eine Ausnahme erzeugt. */
		else {
			lastArgument = 0;
			throw new MathException("Fakultaet für das Argument " + value
					+ " nicht definiert.");
		}
	} //eval

	/**
	 * Die Fakultaet ist nicht differenzierbar. Ein Aufruf dieser Methode
	 * erzeugt immer eine Ausnahme.
	 * 
	 * @param broker
	 * @return
	 * @throws Exception
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Fakultaet kann nicht differenziert werden.");
	}

	public String getSummaryText() {
			return "Berechnet die Fakultät (!), z.B. <br>4!=1*2*3*4<br>0!=1";
	}
	
	

} //FactorialFunction
