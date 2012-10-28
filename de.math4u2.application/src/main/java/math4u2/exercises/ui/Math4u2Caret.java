package math4u2.exercises.ui;

import javax.swing.text.*;
import java.awt.*;
import javax.swing.event.*;

/**
 * Abgeleiteter und leer implementierter DefaultCaret. Dadurch wird die
 * JTextPane beim Einfügen von Text nicht gescrollt und es entstehen keine
 * Speicherleichen
 * 
 * @author Fenn Stefan
 */

public class Math4u2Caret extends DefaultCaret {

	protected void adjustVisibility(Rectangle nloc) {
		super.adjustVisibility(nloc);
	}

	public void install(JTextComponent c) {
		super.install(c);
	}

	public void deinstall(JTextComponent c) {
		super.install(c);
	}

	public void paint(Graphics g) {
		super.paint(g);
	}

	public void addChangeListener(ChangeListener l) {
		super.addChangeListener(l);
	}

	public void removeChangeListener(ChangeListener l) {
		super.removeChangeListener(l);
	}

	public boolean isVisible() {
		return super.isVisible();
	}

	public void setVisible(boolean v) {
		super.setVisible(v);
	}

	public boolean isSelectionVisible() {
		return super.isSelectionVisible();
	}

	public void setSelectionVisible(boolean v) {
		super.setSelectionVisible(v);
	}

	public void setMagicCaretPosition(Point p) {
		super.setMagicCaretPosition(p);
	}

	public Point getMagicCaretPosition() {
		return super.getMagicCaretPosition();
	}

	public void setBlinkRate(int rate) {
	}

	public int getBlinkRate() {
		return 3;
	}

	public int getDot() {
		return super.getDot();
	}

	public int getMark() {
		return super.getMark();
	}

	public void setDot(int dot) {
		super.setDot(dot);
	}

	public void moveDot(int dot) {
		super.moveDot(dot);
	}

}//class Math4u2Caret
