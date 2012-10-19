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
 * Zweistellige Standardfunktionen
 * </p>
 * <p>
 * Legt die allgemeinen Eigenschaften der zweistelligen Standard-Funktionen
 * fest.
 * </p>
 * <p>
 * Copyright (c) 2002
 * </p>
 * <p>
 * Organisation: FHA
 * </p>
 * 
 * @author M.Weiß
 * @version 1.0
 */
public abstract class BinaryStandardFunction extends StandardFunction {

	protected String[] variableList = { "x", "y" };

	/***************************************************************************
	 * Liefert die entsprechende Ableitungsfunktion, z.B. sin'(x) = cos(x).
	 * 
	 * @param broker
	 *            Verwaltet die Funktionsdefinitionen
	 * @return Ableitungsfunktion
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		throw new Exception("Ableitungsfunktion fuer " + getIdentifier()
				+ " nicht verfuegbar");
	}

	/***************************************************************************
	 * Die Stelligkeit 2.
	 * 
	 * @return 2
	 */
	public int getArity() {
		return 2;
	} //getArity

	/***************************************************************************
	 * Für die zweistelligen Standard-Funktionen werden bei der Darstellung der
	 * Variablenname x und y verwendet, also z.B. max(x,y).
	 * 
	 * @return Array mit dem Namen "x" und "y".
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
				"Kettenregel fuer binaere Standardfunktionen nicht implementiert");
	}

} //BinaryStandardFunction
