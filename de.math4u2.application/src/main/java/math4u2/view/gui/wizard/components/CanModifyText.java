package math4u2.view.gui.wizard.components;

import java.awt.event.FocusListener;


/**
 * Jeder Komponente die diese Schnittstelle implementiert, kann
 * Text setzen und holen.
 * 
 * @author Fenn Stefan
 */
public interface CanModifyText {
	void setText(String text);
	String getText();
	void addChangeListener(Object listener);
	void addFinishListener(Object listener);
	void addFocusListener(FocusListener listener);
	void addExitListener(Object listener);
}
