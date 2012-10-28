package math4u2.view.gui.component;

import java.awt.Color;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.graph.HasGraph;

/**
 * Farbwähler für Ränder
 * 
 * @author Fenn Stefan
 */
public class ColorBorderButton extends ColorButton {

	public ColorBorderButton(HasGraph hasGraph, Broker broker) {
		super(hasGraph, broker);
	} //Konstruktor

	public void applyColor(Color c) {
		((UserFunction) hasGraph).setBorderColor(c);
		ColorBorderButton.this.setBackground(c);
	}//applyColor

	public void refresh() {
		setBackground(((UserFunction) hasGraph).getBorderColor());
	} //refresh

} //class ColorButton
