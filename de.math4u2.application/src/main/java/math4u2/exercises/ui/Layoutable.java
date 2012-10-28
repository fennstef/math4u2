// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

/**
 * Schnittstelle für alle Objekte die layouten können sind.
 * 
 * @author Fenn Stefan
 */
public interface Layoutable {
	/**
	 * Baut die Zeichenflächen auf
	 * 
	 * @return Eine neue Swing-Komponente zur späteren Darstellung
	 */
	public void buildLayout();
}