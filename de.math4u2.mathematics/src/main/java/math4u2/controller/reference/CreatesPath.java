package math4u2.controller.reference;

import java.util.List;

/**
 * 
 * @author Fenn Stefan
 */
public interface CreatesPath {

	/**
	 * Erzeugt einen Methodenschritt aus der Methodenliste und gibt diesen
	 * wieder zur�ck.
	 */
	PathStep createPathStep(List methods) throws CreatePathException;

	/**
	 * Gibt den R�ckgabetypen f�r die Methode zur�ck
	 */
	Class getReturnType(PathStep nextStep);

}