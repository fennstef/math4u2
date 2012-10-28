package math4u2.view.gui.wizard.components;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import math4u2.view.gui.component.TextStyleCombo;

public class WizTextStyleCombo extends TextStyleCombo implements CanModifyText{

	public WizTextStyleCombo() {
		super(textStyles);
		setRenderer(new TextStyleRenderer());
	}
	
	public void setText(String text) {
		for(int i=0; i<getItemCount(); i++){
			if(getItemAt(i).equals(text)){
				setSelectedIndex(i);
			}//if
		}//for i
	}//setText

	public String getText() {
		return (String)getSelectedItem();
	}//getText	

	public void addChangeListener(Object listener) {
		addActionListener((ActionListener) listener);
	}//addChangeListener

	public void addFinishListener(Object listener) {
	}//addFinishListener
	
	public void addExitListener(Object listener) {
		addKeyListener((KeyListener) listener);
	}//addExitListener
}//class WizTextStyleCombo
