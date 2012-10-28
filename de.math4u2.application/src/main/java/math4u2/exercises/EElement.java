// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises;

import javax.swing.*;

import math4u2.exercises.ui.*;

/**
 * Abstrakte Basisklasse für alle Übungen und Übungsbehälter.
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
	 * Das Erscheinungsbild das für dieses Element festegelgt wurde.
	 */
	protected ESkin skin;

	/**
	 * Cache für die Swing-Darstellung.
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
	 * Gibt den aktuellen Korrektheitszustand zurück: <code>true</code> falls
	 * die Lösung korrekt ist, <code>false</code> falls die Lösung falsch ist.
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
	 * Gibt die Beschreibung des Elements zurück.
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
	 * Setzt das Erscheinungsbild (Skin) für das Element.
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
	 * Elements für die Darstellung zurück.
	 * 
	 * @return Eine neue Swing-Komponente zur späteren Darstellung
	 */
	public JComponent getSkinnedView() {
		return skin;
	}

	/**
	 * Gibt eine Swing-Komponente zur späteren Darstellung zurück.
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