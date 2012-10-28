// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import javax.swing.JComponent;

/**
 * Schnittstelle für alle Objekte die darstellbar sind.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public interface Viewable {
	/**
	 * Gibt eine Swing-Komponente zur späteren Darstellung zurück.
	 * 
	 * @return Eine neue Swing-Komponente zur späteren Darstellung
	 */
	public JComponent getView();
}