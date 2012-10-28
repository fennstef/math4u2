package math4u2.view.gui.wizard.components;

import java.awt.Event;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class WizTextPane extends JEditorPane implements CanModifyText {

	public WizTextPane(){
		InputMap inputmap = new InputMap();
		inputmap.setParent(getInputMap(JComponent.WHEN_FOCUSED));
		inputmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Event.CTRL_MASK), inputmap.get(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0)));
		inputmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), new Object());
		setInputMap(JComponent.WHEN_FOCUSED, inputmap);		
		
		setBorder((new JTextField()).getBorder());
		setToolTipText("Zeilenumbruch mit Strg+ENTER");
		addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar()=='\t'){
					nextFocus();
					e.consume();
				}//if
			}//keyTyped
		});
	}//Konstruktor

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
