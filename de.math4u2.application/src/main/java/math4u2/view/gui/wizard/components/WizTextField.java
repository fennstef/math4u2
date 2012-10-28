package math4u2.view.gui.wizard.components;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import math4u2.util.swing.UnderlineHighlighter.UnderlineHighlightPainter;

public class WizTextField extends JTextField implements CanModifyText {

	private Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(
			Color.ORANGE);
	private Highlighter.HighlightPainter errorPainter = new UnderlineHighlightPainter(Color.RED);
	
	public WizTextField(){

	}//Konstruktor

	public void highlight(int start, int end) {
		// First remove all old highlights
		removeHighlights();

		try {
			Highlighter hilite = getHighlighter();
			hilite.addHighlight(start, end, painter);
		} catch (BadLocationException e) {
		}
	}//highlight
	
	public void highlightError(int start, int end) {
		// First remove all old highlights
		removeHighlights();

		try {
			Highlighter hilite = getHighlighter();
			hilite.addHighlight(start, end, errorPainter);
		} catch (BadLocationException e) {
		}
	}//highlight	

	// Removes only our private highlights
	public void removeHighlights() {
		Highlighter hilite = getHighlighter();
		Highlighter.Highlight[] hilites = hilite.getHighlights();

		for (int i = 0; i < hilites.length; i++) {
			if (hilites[i].getPainter() instanceof Highlighter.HighlightPainter) {
				hilite.removeHighlight(hilites[i]);
			}//if
		}//for i
	}//removeHighlights

	public void addChangeListener(Object listener) {
		addKeyListener((KeyListener) listener);
	}//addChangeListener

	public void addFinishListener(Object listener) {
		addActionListener((ActionListener) listener);
	}//addFinishListener

	public void addExitListener(Object listener) {
		addKeyListener((KeyListener) listener);
	}//addExitListener
	

}//class WizTextField
