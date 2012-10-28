//Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import java.awt.*;
import java.awt.geom.GeneralPath;
import javax.swing.*;

/**
 * Eine Schaltfläche, die zum Abrufen von Erklärungen benutzt wird.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public class EExplanationButton extends JButton {
	/**
	 * Die Symbole für die verschiedenen Zustände der Schaltfläche: Normal,
	 * Pressed, Disabled.
	 */
	protected ResizableIcon nIcon, pIcon, dIcon;

	/**
	 * Konstruktor, der ein neues EExplanationButton-Objekt erzeugt.
	 * 
	 * @param width
	 *            Breite der neuen Schaltfläche
	 * @param height
	 *            Höhe der neuen Schaltfläche
	 */
	public EExplanationButton(int width, int height) {
		setSize(width, height);
		setMargin(new Insets(0, 0, 0, 0));
		setBorderPainted(false); // Standard Swing-Rahmen ausblenden

		GeneralPath symbol = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 32);
		GeneralPath circleSymbol = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 5);

		// Kreisumrisse innen und außen erzeugen
		circleSymbol.moveTo(0.50000f, 1.00000f);
		circleSymbol.curveTo(0.77538f, 1.00000f, 1.00000f, 0.77538f, 1.00000f,
				0.50000f);
		circleSymbol.curveTo(1.00000f, 0.22462f, 0.77538f, 0.00000f, 0.50000f,
				0.00000f);
		circleSymbol.curveTo(0.22462f, 0.00000f, 0.00000f, 0.22462f, 0.00000f,
				0.50000f);
		circleSymbol.curveTo(0.00000f, 0.77538f, 0.22462f, 1.00000f, 0.50000f,
				1.00000f);
		circleSymbol.closePath();

		circleSymbol.moveTo(0.50000f, 0.92500f);
		circleSymbol.curveTo(0.73408f, 0.92500f, 0.92500f, 0.73408f, 0.92500f,
				0.50000f);
		circleSymbol.curveTo(0.92500f, 0.26592f, 0.73408f, 0.07500f, 0.50000f,
				0.07500f);
		circleSymbol.curveTo(0.26592f, 0.07500f, 0.07500f, 0.26592f, 0.07500f,
				0.50000f);
		circleSymbol.curveTo(0.07500f, 0.73408f, 0.26592f, 0.92500f, 0.50000f,
				0.92500f);
		circleSymbol.closePath();

		// Fragezeichen erzeugen
		symbol.moveTo(0.69219f, 0.33754f);
		symbol.curveTo(0.69219f, 0.36622f, 0.68709f, 0.38935f, 0.67685f,
				0.41181f);
		symbol.curveTo(0.66661f, 0.43412f, 0.65313f, 0.44845f, 0.63652f,
				0.46549f);
		symbol.curveTo(0.62019f, 0.48225f, 0.60113f, 0.49789f, 0.57997f,
				0.51238f);
		symbol.curveTo(0.55853f, 0.52715f, 0.53594f, 0.54135f, 0.51179f,
				0.55499f);
		symbol.lineTo(0.51179f, 0.66165f);
		symbol.lineTo(0.43340f, 0.66165f);
		symbol.lineTo(0.43340f, 0.51989f);
		symbol.curveTo(0.45242f, 0.50924f, 0.47289f, 0.49716f, 0.49505f,
				0.48439f);
		symbol.curveTo(0.51705f, 0.47145f, 0.53497f, 0.45839f, 0.54914f,
				0.44532f);
		symbol.curveTo(0.56592f, 0.43000f, 0.57940f, 0.42005f, 0.58864f,
				0.40384f);
		symbol.curveTo(0.59803f, 0.38767f, 0.60271f, 0.36918f, 0.60271f,
				0.34405f);
		symbol.curveTo(0.60271f, 0.31110f, 0.58921f, 0.28526f, 0.56691f,
				0.26902f);
		symbol.curveTo(0.54447f, 0.25288f, 0.52472f, 0.24592f, 0.48934f,
				0.24592f);
		symbol.curveTo(0.45770f, 0.24592f, 0.44134f, 0.25316f, 0.41324f,
				0.26308f);
		symbol.curveTo(0.38497f, 0.27304f, 0.37061f, 0.28526f, 0.35430f,
				0.29549f);
		symbol.lineTo(0.34989f, 0.29549f);
		symbol.lineTo(0.34989f, 0.21280f);
		symbol.curveTo(0.37036f, 0.20500f, 0.38839f, 0.18881f, 0.41978f,
				0.18255f);
		symbol.curveTo(0.45116f, 0.17631f, 0.46735f, 0.17092f, 0.49546f,
				0.17092f);
		symbol.curveTo(0.55825f, 0.17092f, 0.59887f, 0.18500f, 0.63540f,
				0.21551f);
		symbol.curveTo(0.67175f, 0.24604f, 0.69219f, 0.28752f, 0.69219f,
				0.33754f);
		symbol.closePath();

		symbol.moveTo(0.51891f, 0.82909f);
		symbol.lineTo(0.42942f, 0.82909f);
		symbol.lineTo(0.42942f, 0.73665f);
		symbol.lineTo(0.51891f, 0.73665f);
		symbol.lineTo(0.51891f, 0.82909f);
		symbol.closePath();

		GeneralPath[] solutionSymbol = { circleSymbol, symbol };

		// Maximale Symbolgröße festlegen
		int curWidth = getSize().width;
		int curHeight = getSize().height;
		int symbolSize = (curWidth > curHeight) ? curHeight : curWidth;

		// Symbole erzeugen
		nIcon = new ResizableIcon(solutionSymbol, symbolSize, symbolSize);
		pIcon = new ResizableIcon(solutionSymbol, symbolSize, symbolSize);
		dIcon = new ResizableIcon(solutionSymbol, symbolSize, symbolSize);

		// Einstellungen festlegen
		setFocusPainted(false); // Keine Anzeige der Schaltfläche die den Fokus
								// hat
		setContentAreaFilled(false); // Druchsichtig (nicht setOpaque verwenden)

		// Symbole festlegen
		setIcon(nIcon);
		setPressedIcon(pIcon);
		setDisabledIcon(dIcon);
	}

	/**
	 * Standardkonstruktor, der ein neues EExplanationButton-Objekt erzeugt.
	 * 
	 * @param width
	 *            Breite der neuen Schaltfläche
	 * @param height
	 *            Höhe der neuen Schaltfläche
	 */
	public EExplanationButton() {
		this(15, 15);
	}

	/**
	 * Legt die Farben für die verschiedenen Zustände (normal, gedrückt,
	 * deaktiviert) fest.
	 * 
	 * @param normal
	 *            Farbe für den normalen Zustand
	 * @param pressed
	 *            Farbe für den gedrückten Zustand
	 * @param disabled
	 *            Farbe für den deaktivierten Zustand
	 */
	public void setColors(Color normal, Color pressed, Color disabled) {
		nIcon.setColor(normal);
		pIcon.setColor(pressed);
		dIcon.setColor(disabled);
	}
}