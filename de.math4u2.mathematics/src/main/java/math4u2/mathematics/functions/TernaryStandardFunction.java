package math4u2.mathematics.functions;

/**
 * <p>Überschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */

import math4u2.controller.Broker;
import math4u2.mathematics.termnodes.*;

/**
 * <p>
 * Dreistellige Standardfunktionen
 * </p>
 * <p>
 * Legt die allgemeinen Eigenschaften der dreistelligen Standard-Funktionen
 * fest.
 * </p>
 * <p>
 * Copyright (c) 2005
 * </p>
 * <p>
 * Organisation: FHA
 * </p>
 * 
 * @author M.Weiß
 * @version 1.0
 */
public abstract class TernaryStandardFunction extends StandardFunction {

	protected String[] variableList = { "x", "y", "z" };

	/***************************************************************************
	 * Liefert die entsprechende Ableitungsfunktion, z.B. sin'(x) = cos(x).
	 * 
	 * @param broker
	 *            Verwaltet die Funktionsdefinitionen
	 * @return Ableitungsfunktion
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Ableitungsfunktion fuer " + getKey()
				+ " nicht verfuegbar");
	}

	/***************************************************************************
	 * Die Stelligkeit 3.
	 * 
	 * @return 3
	 */
	public int getArity() {
		return 3;
	} //getArity

	/***************************************************************************
	 * Für die dreistelligen Standard-Funktionen werden bei der Darstellung der
	 * Variablenname x,y und z verwendet.
	 */
	public String[] getVariableNames() {
		return variableList;
	} //getVariableNames

	/***************************************************************************
	 * Implementiert die Kettenregel (Nachdifferenzieren). derzeit nicht
	 * implementiert
	 */
	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {
		throw new MathException(
				"Kettenregel fuer ternaere Standardfunktionen nicht implementiert");
	}

} //TernaryStandardFunction
