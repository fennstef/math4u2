package math4u2.controller.reference;

import java.util.List;

/**
 * 
 * @author Fenn Stefan
 */
public interface CreatesPath {

	/**
	 * Erzeugt einen Methodenschritt aus der Methodenliste und gibt diesen
	 * wieder zurück.
	 */
	PathStep createPathStep(List methods) throws CreatePathException;

	/**
	 * Gibt den Rückgabetypen für die Methode zurück
	 */
	Class getReturnType(PathStep nextStep);

}