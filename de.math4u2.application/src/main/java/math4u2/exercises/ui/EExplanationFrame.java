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
 * Dialogfeld zur Darstellung von Erklärungen
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
	 *            Fenster mit dem der Dialog verknüpft werden soll
	 */
	public EExplanationFrame(JFrame owner) {
		super(owner, "Erklärung"); // Titel und für das für diesen Dialog
								   // verantwortliche Fenster festlegen

		ScrollablePanel panel = new ScrollablePanel(); // Rollbaren Behälter für
													   // den Text erzeugen
		JScrollPane sp = new JScrollPane(panel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		setBackground(ESkin.normColL);

		this.setSize(300, 200); // Initiale Größe festlegen
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE); // Fenster
																	  // soll
																	  // beim
																	  // Schließen
																	  // nu
																	  // versteckt
																	  // werden

		explPane.setEditable(false); // Der benutzer darf den Textbehälter nicht
									 // bearbeiten
		explPane.setMargin(new Insets(10, 15, 15, 15)); // Abstand gegenüber dem
														// Dialogrand lassen

		explPane.setOpaque(false); // Textbereich soll durchsichtig (nicht opak)
								   // sein.
		panel.setOpaque(false); // Rollbarer Behälter soll durchsichtig sein
		sp.getViewport().setBackground(Color.white); // Hintergrundfarbe des
													 // rollbaren Bereichs
													 // setzen

		panel.setLayout(new BorderLayout()); // Neues Layout für Behälter
											 // festelgen
		panel.add(explPane, BorderLayout.NORTH); // Textbereich in den Behälter
												 // einfügen

		getContentPane().add(sp, BorderLayout.CENTER); // Rollbereich in das
													   // Dialogfeld einfügen
	}

	/**
	 * Legt aktuellen Text fest, der dargestellt werden soll.
	 * 
	 * @param Erklärungstext
	 *            der angezeigt werden soll
	 */
	public void setText(StyledText text) {
		Document doc = explPane.getDocument();
		try {
			doc.remove(0, doc.getLength()); // Alten Text löschen
			if (text != null)
				text.insertText(doc); // Neuen Text einfügen
		} catch (BadLocationException ble) {
			ExceptionManager.doError("Couldn't insert initial explanation frame text.",ble);
		}
	}//setText

}//EExplanationFrame
