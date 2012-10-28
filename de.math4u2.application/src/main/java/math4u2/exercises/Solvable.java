// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises;

/**
 * Schnittstelle f�r alle �bungen mit L�sung
 * 
 * @author Erich Seifert
 * @version 0.1
 */
public interface Solvable {
	/**
	 * Zeigt dem Benutzer die L�sung an.
	 */
	public void showSolution();

	/**
	 * Zeigt dem Benutzer falsche Eingaben an.
	 */
	public void showFailures();
}