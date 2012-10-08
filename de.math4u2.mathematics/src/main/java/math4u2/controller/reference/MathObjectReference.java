package math4u2.controller.reference;

import java.lang.reflect.InvocationTargetException;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;

/**
 * Allgemeine Referenz auf Knoten z.B. ist k.mitte.x und liste[3].x, p ein
 * MathObjectReference
 * 
 * @author Fenn Stefan
 */
public interface MathObjectReference {

	/**
	 * Holt rekursiv das Objekt, dass mit dem Pfad angegeben wurde.
	 */
	Object getObject() throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, MathException;

	/**
	 * Gibt den letzten PathStep zurück. Nötig für Set-Methoden
	 */
	PathStep getLastStep();

	/**
	 * String-Darstellung z.B. k.mitte.x
	 */
	String getPathString();

	/**
	 * Ermittelt den Rückgabetyp für den Operator-Experten.
	 */
	Class getReturnType();

	/**
	 * Update der Links, wenn ein neues Objekt registriert wurde
	 */
	void swapLinks(MathObject oldObj, MathObject newObj);

} //MathObjectReference
