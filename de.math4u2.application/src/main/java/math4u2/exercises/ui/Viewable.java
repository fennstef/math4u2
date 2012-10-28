// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import javax.swing.JComponent;

/**
 * Schnittstelle f�r alle Objekte die darstellbar sind.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public interface Viewable {
	/**
	 * Gibt eine Swing-Komponente zur sp�teren Darstellung zur�ck.
	 * 
	 * @return Eine neue Swing-Komponente zur sp�teren Darstellung
	 */
	public JComponent getView();
}