// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import javax.swing.JComponent;

/**
 * Schnittstelle f�r alle Objekte deren Aussehen sich �ber Erscheinungsbilder
 * steuern l�sst.
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
	 * Gibt eine Ansicht im aktuellen Erscheinungsbild zur�ck.
	 * 
	 * @return Eine neue Swing-Komponente f�r die sp�tere Darstellung des
	 *         Erscheinungsbilds
	 */
	public JComponent getSkinnedView();
}