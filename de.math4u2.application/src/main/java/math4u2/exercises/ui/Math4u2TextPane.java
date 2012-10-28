package math4u2.exercises.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import math4u2.application.resource.Colors;
import math4u2.util.exception.ExceptionManager;

/**
 * Diese Klasse schaltet in erster Linie die automatischen Caret-Scrolls aus.
 * Des weiteren kann diese Komponente sich beim Wiederaufbau in das Sichtbare
 * scrollen.
 * 
 * @author Fenn Stefan
 */
public class Math4u2TextPane extends JTextPane {

	private static final Map hints = new HashMap();

	/** Flag um das automatische scrollen bei Veränderung zu steuern */
	private boolean scrollOnUpdate = false;

	/** Aktuelle Höhe zum vorhergehenden Container (für scrollOnUpdate) */
	private int height = -1;

	/** Flag, ob die Komponente im Histroy-Stil eingefärbt werden soll */
	private boolean isHistory;

	public Math4u2TextPane() {
		super();
		init();
	}//Konstruktor

	public boolean getScrollOnUpdate() {
		return scrollOnUpdate;
	}

	public void setScrollOnUpdate(boolean val) {
		scrollOnUpdate = val;
	}

	public boolean isHistory() {
		return isHistory;
	}

	public void setHistory(boolean val) {
		isHistory = val;
	}

	public void init() {
		setEditable(false); // Kopftext ist nicht vom Benutzer bearbeitbar
		setOpaque(false); // Kopftext ist durchsichtig (nicht opak)
		//setCaret(m4uCaret);
		
		
		hints.put(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		//hints.put(RenderingHints.KEY_FRACTIONALMETRICS,
		// RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	}//init

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.addRenderingHints(hints);
		super.paintComponent(g);
	}//paintComponent

	/** Wird benutzt um über alle Komponenten zu zeichnen */
	public void paint(Graphics g) {
		try{
			super.paint(g);
		}catch(IllegalStateException e){
			ExceptionManager.doError("Fehler beim Drucken","Dieser Fehler ist in der Java JVM 1.5 behoben");
		}
		if (!isHistory)
			return;
		Graphics2D g2 = (Graphics2D) g;
		g2.addRenderingHints(hints);
		Color ca = g.getColor();
		g.setColor(Colors.TEXTPANE_HISTORY_COLOR);
		g.fillRoundRect(0, 0, getSize().width, getSize().height, 15, 15);
		g.setColor(ca);
	}//paint

	/**
	 * wenn invalidate aufgerufen wird, könnte es sein, dass man diese TextPane
	 * wieder in das Sichtbare gescrollt werden muß.
	 */
	public void invalidate() {
		super.invalidate();

		//soll neu gescrollt werden ?
		if (!scrollOnUpdate)
			return;

		if (height == (int) getLocation().getY())
			return;
		height = (int) getLocation().getY();

		//Komponenten holen
		JComponent scrollContent = (JComponent) getParent();
		if (scrollContent.getParent() == null)
			return;
		JScrollPane scroller = (JScrollPane) scrollContent.getParent()
				.getParent();

		//Sicherheitshalber nach unten Scrollen bei Höhe 0
		if (height == 0) {
			scrollContent.scrollRectToVisible(new Rectangle(0, scrollContent
					.getSize().height, 0, 0));
			return;
		}//if height==0

		//es wird neu gescrollt
		scrollContent.scrollRectToVisible(new Rectangle(0, height
				+ scroller.getSize().height - getInsets().top, 0, 0));
	}//invalidate

	/** Erzwingt, dass beim nächsten invalidate wieder gescrollt wird */
	public void prepareForScroll() {
		height = -1;
	}//prepareForScroll

}//class Math4u2TextPane
