package math4u2.mathematics.functions;


/**
 * 
 * <p>
 * Infix-Standardfunktionen
 * </p>
 * <p>
 * Legt die allgemeinen Eigenschaften der zweistelligen Infix-Funktionen fest.
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
public abstract class InfixStandardFunction extends StandardFunction {

	/***************************************************************************
	 * Infix-Operator der entsprechenden Funktion.
	 */
	protected String operatorName;

	/***************************************************************************
	 * Infix-Operator der entsprechenden Funktion in Schönschrift.
	 */
	protected String operatorNiceName;

	/***************************************************************************
	 * Die Stelligkeit einer Infix-Funktion ist 2.
	 * 
	 * @return 2
	 */
	public int getArity() {
		return 2;
	} //getArity

	/***************************************************************************
	 * Für die Infix-Funktionen werden die Variablennamen x und y verwendet,
	 * also x + y oder sum(x,y).
	 * 
	 * @return Array mit den Namen "x" und "y"
	 */
	public String[] getVariableNames() {
		return new String[] { "x", "y" };
	}

	/**
	 * Konstruiert aus den beiden Argumenten in argStrings[0] und argStrings[1]
	 * die Infix-Darstellung des Gesamtterms.
	 * 
	 * @param argStrings
	 *            Argumente
	 * @return Infix-Darstellung des Terms
	 */
	public String buildTermString(String[] argStrings, String n) {
		return argStrings[0] + operatorName + argStrings[1];
	}
}