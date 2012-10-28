package math4u2.view.gui.component;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.HasGraph;

/**
 * Farbwähler für Flächen
 * 
 * @author Fenn Stefan
 */
public class ColorAreaButton extends ColorButton {
	private JSlider slider;

	public ColorAreaButton(HasGraph hasGraph, Broker broker) {
		super(hasGraph, broker);
	} //Konstruktor

	public void applyColor(Color c) {
		Color c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), slider
				.getValue());
		((UserFunction) hasGraph).setFillColor(c2);
		setBackground(((UserFunction) hasGraph).getFillColor());
	}//applyColor

	public void refresh() {
		setBackground(((UserFunction) hasGraph).getFillColor());
		invalidate();
	} //refresh

	public JSlider getAlphaSlider() {
		slider = new JSlider(0, 255, 255 / 2);
		slider.setValue(((UserFunction)hasGraph).getFillColor().getAlpha());
		Dimension dim =new Dimension(130, (int)slider.getPreferredSize()
				.getHeight());
		slider.setPreferredSize(dim);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Color c = ((UserFunction) hasGraph).getFillColor();
				applyColor(c);
				try {
					broker.propagateChange(hasGraph);
				} catch (BrokerException e) {
					ExceptionManager.doError("Fehler beim Erneuern des Objekts "+hasGraph,e);
				}//catch
			}//stateChanged
		});
		slider.setOpaque(false);
		return slider;
	}//getAlphaSlider

} //class ColorButton
