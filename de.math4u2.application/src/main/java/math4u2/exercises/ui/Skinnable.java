// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import javax.swing.JComponent;

/**
 * Schnittstelle für alle Objekte deren Aussehen sich über Erscheinungsbilder
 * steuern lässt.
 * 
 * @author Erich Seifert
 * @version 0.1
 * @see ESkin
 */
public interface Skinnable {
	/**
	 * Legt ein neues Erscheinungsbild fest.
	 * 
	 * @param skin
	 *            Skin-Objekt, das das Erscheinungsbild steuert
	 */
	public void setSkin(ESkin skin);

	/**
	 * Gibt eine Ansicht im aktuellen Erscheinungsbild zurück.
	 * 
	 * @return Eine neue Swing-Komponente für die spätere Darstellung des
	 *         Erscheinungsbilds
	 */
	public JComponent getSkinnedView();
}