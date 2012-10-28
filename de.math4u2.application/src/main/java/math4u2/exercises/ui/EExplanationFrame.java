// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaConstants;

/**
 * Dialogfeld zur Darstellung von Erkl�rungen
 * 
 * @version 0.1
 * @author Erich Seifert
 */
class EExplanationFrame extends JDialog implements DrawAreaConstants {
	/** Textbereich mit Antialias */
	private static Math4u2TextPane explPane = new Math4u2TextPane() {
		public void paintComponent(Graphics gr) {
			Graphics2D g = (Graphics2D) gr;
			g.setRenderingHints(antialiasOn); //AntiAliasing
														// aktivieren
			g.setColor(ESkin.normColM);
			Insets in = getInsets();
			g.fillRoundRect(5, in.top - 5, getWidth() - 10, getHeight()
					- in.bottom, 20, 20);
			super.paintComponent(g);
		}//paintComponent
	};

	/**
	 * Konstruktor, der ein neues EExplanationFrame-Objekt erzeugt.
	 * 
	 * @param owner
	 *            Fenster mit dem der Dialog verkn�pft werden soll
	 */
	public EExplanationFrame(JFrame owner) {
		super(owner, "Erkl�rung"); // Titel und f�r das f�r diesen Dialog
								   // verantwortliche Fenster festlegen

		ScrollablePanel panel = new ScrollablePanel(); // Rollbaren Beh�lter f�r
													   // den Text erzeugen
		JScrollPane sp = new JScrollPane(panel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		setBackground(ESkin.normColL);

		this.setSize(300, 200); // Initiale Gr��e festlegen
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE); // Fenster
																	  // soll
																	  // beim
																	  // Schlie�en
																	  // nu
																	  // versteckt
																	  // werden

		explPane.setEditable(false); // Der benutzer darf den Textbeh�lter nicht
									 // bearbeiten
		explPane.setMargin(new Insets(10, 15, 15, 15)); // Abstand gegen�ber dem
														// Dialogrand lassen

		explPane.setOpaque(false); // Textbereich soll durchsichtig (nicht opak)
								   // sein.
		panel.setOpaque(false); // Rollbarer Beh�lter soll durchsichtig sein
		sp.getViewport().setBackground(Color.white); // Hintergrundfarbe des
													 // rollbaren Bereichs
													 // setzen

		panel.setLayout(new BorderLayout()); // Neues Layout f�r Beh�lter
											 // festelgen
		panel.add(explPane, BorderLayout.NORTH); // Textbereich in den Beh�lter
												 // einf�gen

		getContentPane().add(sp, BorderLayout.CENTER); // Rollbereich in das
													   // Dialogfeld einf�gen
	}

	/**
	 * Legt aktuellen Text fest, der dargestellt werden soll.
	 * 
	 * @param Erkl�rungstext
	 *            der angezeigt werden soll
	 */
	public void setText(StyledText text) {
		Document doc = explPane.getDocument();
		try {
			doc.remove(0, doc.getLength()); // Alten Text l�schen
			if (text != null)
				text.insertText(doc); // Neuen Text einf�gen
		} catch (BadLocationException ble) {
			ExceptionManager.doError("Couldn't insert initial explanation frame text.",ble);
		}
	}//setText

}//EExplanationFrame
