// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

/**
 * Schnittstelle f�r alle Objekte die layouten k�nnen sind.
 * 
 * @author Fenn Stefan
 */
public interface Layoutable {
	/**
	 * Baut die Zeichenfl�chen auf
	 * 
	 * @return Eine neue Swing-Komponente zur sp�teren Darstellung
	 */
	public void buildLayout();
}