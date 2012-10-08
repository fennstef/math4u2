package math4u2.mathematics.functions;

import math4u2.controller.Broker;
import math4u2.mathematics.termnodes.*;

/**
 * <p>
 * Einstellige Standardfunktionen
 * </p>
 * <p>
 * Legt die allgemeinen Eigenschaften der einstelligen Standard-Funktionen fest.
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
public abstract class UnaryStandardFunction extends StandardFunction {

	protected String[] variableList = { "x" };

	/***************************************************************************
	 * Liefert die entsprechende Ableitungsfunktion, z.B. sin'(x) = cos(x).
	 * 
	 * @param broker
	 *            Verwaltet die Funktionsdefinitionen
	 * @return Ableitungsfunktion
	 */
	public abstract Function getDeriveFunction(Broker broker) throws Exception;

	public Function getDeriveFunction(int derivePos, int deriveOrder,
			Broker broker) throws MathException {
		if (derivePos != 1)
			throw new MathException("Ableitung nach Position " + derivePos
					+ " bei einstelliger Funktion nicht moeglich");
		try {
			if (deriveOrder < 0)
				throw new MathException("Wert " + deriveOrder
						+ " fuer Ordnung einer Ableitung nicht erlaubt");
			if (deriveOrder == 0)
				return this;
			else {
				Function f = getDeriveFunction(broker);
				if (deriveOrder > 1)
					f = f.getDeriveFunction(derivePos, deriveOrder - 1, broker);
				return f;
			}
		} catch (Exception e) {
			throw new MathException(e.toString());
		}
	}

	/***************************************************************************
	 * Die Stelligkeit 1.
	 * 
	 * @return 1
	 */
	public int getArity() {
		return 1;
	} //getArity

	/***************************************************************************
	 * Für die einstelligen Standard-Funktionen wird bie der Darstellung der
	 * Variablenname x verwendet, also z.B. sin(x).
	 * 
	 * @return Array mit dem Namen "x".
	 */
	public String[] getVariableNames() {
		return variableList;
	} //getVariableNames

	/***************************************************************************
	 * Implementiert die Kettenregel (Nachdifferenzieren). Beispiel: Für den
	 * Aufruf sin(x^2) gilt: argumentTerms enthält den Term x^2,
	 * derivedArgumentTerms enthält den Term 2*x, daraus wird als Rückgabe der
	 * Term cos(x^2) * (2*x) konstuiert.
	 * 
	 * @param argumentTerms
	 * @param derivedArgumentTerms
	 * @param broker
	 * @return Funktionsterm der Ableitung nach der Kettenregel.
	 */
	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {
		try {
			if (  derivedArgumentTerms[0] instanceof TermNodeNum
		        	  && ((TermNodeNum)derivedArgumentTerms[0]).evalScalar() == 0.0 ) {
		              return derivedArgumentTerms[0];
		        }
			else {
				return new TermNodeFunct((Function) (broker.getObject("prod")),
						new TermNode[] {
								new TermNodeFunct(getDeriveFunction(broker),
										argumentTerms, broker),
								derivedArgumentTerms[0] }, broker);			
			}
			
		} catch (Exception e) {
			throw new MathException(e.getMessage());
		}

	}

} //UnaryStandardFunction
