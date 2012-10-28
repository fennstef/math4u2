package math4u2.view.gui.wizard.components;

import java.awt.event.KeyListener;

import javax.swing.JCheckBox;

public class WizCheckBox extends JCheckBox implements CanModifyText {

	public WizCheckBox(String text) {
		super(text);
	}

	public void addChangeListener(Object listener) {
		addKeyListener((KeyListener) listener);
	}//addChangeListener

	public void addFinishListener(Object listener) {
		addKeyListener((KeyListener) listener);
	}//addFinishListener

	public void addExitListener(Object listener) {
		addKeyListener((KeyListener) listener);
	}//addExitListener
	

}//class WizTextField
