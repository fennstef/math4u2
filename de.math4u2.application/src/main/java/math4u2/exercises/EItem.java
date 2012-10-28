// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises;

import math4u2.exercises.ui.StyledText;

/**
 * Abstrakte Basisklasse für alle Übungselemente die in einem Behälter
 * gespeichert werden können.
 * 
 * @version 0.2
 * @author Erich Seifert
 */
public abstract class EItem extends EElement {
	private StyledText explanation; // Erklärung

	/**
	 * Konstruktor, der ein neues EElement-Objekt erzeugt.
	 * 
	 * @param description
	 *            Beschreibung des Elements
	 * @param explanation
	 *            Erklärung zum Elements
	 */
	public EItem(StyledText description, StyledText explanation) {
		super(description);
		this.explanation = explanation;
	}

	/**
	 * Gibt den Erklärungstext zurück.
	 * 
	 * @return Den aktuellen Erklärungstext
	 */
	public StyledText getExplanation() {
		return explanation;
	}

}