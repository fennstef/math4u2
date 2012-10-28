// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises;

import math4u2.exercises.ui.StyledText;

/**
 * Abstrakte Basisklasse f�r alle �bungselemente die in einem Beh�lter
 * gespeichert werden k�nnen.
 * 
 * @version 0.2
 * @author Erich Seifert
 */
public abstract class EItem extends EElement {
	private StyledText explanation; // Erkl�rung

	/**
	 * Konstruktor, der ein neues EElement-Objekt erzeugt.
	 * 
	 * @param description
	 *            Beschreibung des Elements
	 * @param explanation
	 *            Erkl�rung zum Elements
	 */
	public EItem(StyledText description, StyledText explanation) {
		super(description);
		this.explanation = explanation;
	}

	/**
	 * Gibt den Erkl�rungstext zur�ck.
	 * 
	 * @return Den aktuellen Erkl�rungstext
	 */
	public StyledText getExplanation() {
		return explanation;
	}

}