// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises;

import javax.swing.*;

import math4u2.exercises.ui.*;

/**
 * Abstrakte Basisklasse f�r alle �bungen und �bungsbeh�lter.
 * 
 * @author Erich Seifert
 * @version 0.1
 * @see EContainer
 * @see EItem
 */
public abstract class EElement implements Solvable, Viewable, Skinnable {
	private StyledText description;

	private boolean valid;

	/**
	 * Das Erscheinungsbild das f�r dieses Element festegelgt wurde.
	 */
	protected ESkin skin;

	/**
	 * Cache f�r die Swing-Darstellung.
	 */
	transient protected JComponent view;

	/**
	 * Konstruktor, der ein neues EElement-Objekt erzeugt.
	 * 
	 * @param description
	 *            Beschreibung des Elements
	 */
	public EElement(StyledText description) {
		valid = false;
		this.description = description;
	}

	/**
	 * Gibt den aktuellen Korrektheitszustand zur�ck: <code>true</code> falls
	 * die L�sung korrekt ist, <code>false</code> falls die L�sung falsch ist.
	 * 
	 * @return Aktueller Zustand
	 */
	public boolean getValid() {
		return valid;
	}

	/**
	 * Setzt den Korrektheitszustand.
	 * 
	 * @param valid
	 *            Neuer Zustand der gesetzt werden soll
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * Gibt die Beschreibung des Elements zur�ck.
	 * 
	 * @return Den aktuellen Beschreibungstext
	 */
	public StyledText getDescription() {
		return description;
	}

	/**
	 * Setzt die Beschreibung des Elements.
	 * 
	 * @param description
	 *            Beschreibungstext der zugewiesen werden soll
	 */
	public void setDescription(StyledText description) {
		this.description = description;
	}

	/**
	 * Setzt das Erscheinungsbild (Skin) f�r das Element.
	 * 
	 * @param skin
	 *            Skin-Objekt, das das Erscheinungsbild bestimmt
	 */
	public void setSkin(ESkin skin) {
		this.skin = skin;
		skin.plug(this.getView());
	}

	/**
	 * Gibt eine Swing-Komponente des im Erscheinungsbild (Skin) gekapselten
	 * Elements f�r die Darstellung zur�ck.
	 * 
	 * @return Eine neue Swing-Komponente zur sp�teren Darstellung
	 */
	public JComponent getSkinnedView() {
		return skin;
	}

	/**
	 * Gibt eine Swing-Komponente zur sp�teren Darstellung zur�ck.
	 */
	public JComponent getView() {
		if (view == null)
			createView();
		return view;
	}

	/**
	 * Erzeugt die Komponenten zur Darstellung.
	 */
	abstract protected void createView();
}