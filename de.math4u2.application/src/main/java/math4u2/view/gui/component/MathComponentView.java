package math4u2.view.gui.component;

import math4u2.controller.MathObject;

/**
 * Diese Schnittstelle, zeigt an, das MathObject´s angezeigt werden. Diese
 * können neu gesetzt werden und sich neu zeichen.
 */
public interface MathComponentView {

	/**
	 * Setzt z.B. das Textfeld neu
	 */
	void refresh();

	/**
	 * Das Model wir neu gesetzt
	 */
	void setMathModel(MathObject mo);

	/**
	 * Dadruch kann die Sicht nicht mehr
	 * verändert werden.
	 */
	void deactivate();
}