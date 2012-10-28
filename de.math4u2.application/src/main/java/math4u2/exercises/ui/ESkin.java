// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import math4u2.view.formula.FormulaRenderContext;

/**
 * Klasse für Erscheinungbilder von Übungselementen
 * 
 * @author Erich Seifert
 * @version 0.1
 * @see math4u2.exercises.EElement
 */
public class ESkin extends JPanel {

	/**
	 * Nomales Erscheinungsbild.
	 */
	public static final short NORMAL_SCHEME = 0;

	/**
	 * Erscheinungsbild für fehlerhafte Elemente.
	 */
	public static final short FAILURE_SCHEME = 1;

	/**
	 * Erscheinungsbild für korrekte Elemente.
	 */
	public static final short CORRECT_SCHEME = 2;

	/**
	 * Aktuelles Erscheinungsbild
	 */
	private short scheme = NORMAL_SCHEME;

	private static final RenderingHints antialiasOn = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	public static final Color normColD = new Color(180, 180, 255);

	public static final Color normColM = new Color(210, 210, 255);

	public static final Color normColL = new Color(240, 240, 255);

	public static final Color failColD = new Color(255, 180, 180);

	public static final Color failColM = new Color(255, 210, 210);

	public static final Color failColL = new Color(255, 240, 240);

	public static final Color correctColD = new Color(180, 255, 180);

	public static final Color correctColM = new Color(110, 230, 110);

	public static final Color correctColL = new Color(240, 255, 240);

	private Color colorD, colorM, colorL; // Aktuelle dunkle, mittlere und helle
										  // Farbe

	private StyledText explanation; // Erklärungstext

	static EExplanationFrame explFrame; // Global definiertes Dialogfeld zum
										// Anzeigen der Erklärung

	private final GridBagLayout gridbag = new GridBagLayout();

	/**
	 * Statische Variable für Textformatierungen.
	 */
	public static final StyleContext styleContext = new StyleContext(){
		public Font getFont(String family, int style, int size) {
			return FormulaRenderContext.getFont(family, style, size);
		}
		
	};

	/**
	 * Statische Methode zum erzeugen eines neuen Kontrollkästchens (CheckBox).
	 * 
	 * @return Ein neues ResizableCheckBox-Objekt
	 */
	public static final ResizableCheckBox getCheckBox() {
		ResizableCheckBox checkBox = new ResizableCheckBox(20, 20);
		checkBox.setOpaque(false);
		return checkBox;
	}

	static {
		init();
	}//statischer Konstruktor

	/**
	 * Konstruktor der ein neues ESkin-Objekt erzeugt.
	 */
	public ESkin() {
		setOpaque(false);
		setLayout(gridbag);
		setScheme(NORMAL_SCHEME); // Anfänglich normales Erscheinungsbild
	} //Konstruktor

	/**
	 * Legt das Dialogfeld für die Darstellung von Erklärungen fest.
	 * 
	 * @param explanationFrame
	 *            Fenster für das Darstellen der Erklärungen
	 */
	public static void setExplanationFrame(EExplanationFrame explanationFrame) {
		explFrame = explanationFrame;
	}

	/**
	 * Fügt ein Element in das Erscheinungsbild ein.
	 * 
	 * @param contentView
	 *            Komponente des zu kapselnden Objekts
	 * @param ctrlElems
	 *            Steuerungselemente des zu kapselnden Objekts
	 */
	public void plug(JComponent contentView, JComponent[] ctrlElems) {
		JPanel content = new JPanel();
		content.setLayout(gridbag);
		content.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();

		// Alle vom Element übergebenen Steuerelemente einfügen
		Box controls = Box.createHorizontalBox();
		controls.setOpaque(false);
		for (int i = 0; i < ctrlElems.length; i++)
			controls.add(ctrlElems[i]);

		gbc.anchor = GridBagConstraints.NORTHWEST;

		// Inhalt in den Behälter content einfügen
		gbc.gridx = 0;
		gbc.insets = new Insets(2, 7, 3, 3);
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gridbag.setConstraints(contentView, gbc);
		content.add(contentView, gbc);

		// Antwortknopf in den Behälter content einfügen
		gbc.gridx = 1;
		gbc.insets = new Insets(2, 2, 15, 3);
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;

		if (explanation != null) {
			EExplanationButton explButton = new EExplanationButton(20, 20);
			setExplanationButtonScheme(explButton);
			explButton.addActionListener(new ExplanationListener(explanation));
			gridbag.setConstraints(explButton, gbc);
			content.add(explButton, gbc);
		} //if

		// Behälter für Steuerelemente einfügen
		gbc.gridx = 0;
		gbc.weightx = 0.0;
		gbc.insets = new Insets(2, 5, 15, 0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gridbag.setConstraints(controls, gbc);
		add(controls, gbc);

		// Behälter für Inhalt einfügen
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(0, 0, 5, 0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gridbag.setConstraints(content, gbc);
		add(content, gbc);
	}

	/**
	 * Fügt ein Element in das Erscheinungsbild ein.
	 * 
	 * @param contentView
	 *            Komponente des zu kapselnden Objekts
	 * @param ctrlElem
	 *            Einzelnes Steuerungselement des zu kapselnden Objekts
	 */
	public void plug(JComponent contentView, JComponent ctrlElem) {
		JComponent[] ctrlArray = new JComponent[1];
		ctrlArray[0] = ctrlElem;
		plug(contentView, ctrlArray);
	}

	/**
	 * Fügt ein Element in das Erscheinungsbild ein.
	 * 
	 * @param contentView
	 *            Komponente des zu kapselnden Objekts
	 */
	public void plug(JComponent contentView) {
		JPanel space = new JPanel();
		space.setOpaque(false);
		space.setMinimumSize(new Dimension(20, 20));
		space.setPreferredSize(new Dimension(20, 20));
		plug(contentView, space);
	}

	/**
	 * Legt den Erklärungstext fest.
	 * 
	 * @param explanationText
	 *            Neuer Erklärungstext der verwendet werden soll
	 */
	public void setExplanation(StyledText explanationText) {
		explanation = explanationText;
	} //setExplanation

	/**
	 * Legt die die Variation des Erscheinungsbildes fest.
	 * 
	 * @param scheme
	 *            Variation die verwendet werden soll
	 */
	public void setScheme(short scheme) {
		this.scheme = scheme;
		switch (scheme) {
		case FAILURE_SCHEME:
			colorD = failColD;
			colorM = failColM;
			colorL = failColL;
			break;
		case CORRECT_SCHEME:
			colorD = correctColD;
			colorM = correctColM;
			colorL = correctColL;
			break;
		default:
			colorD = normColD;
			colorM = normColM;
			colorL = normColL;
			break;
		}
		repaint();
	} //setScheme

	public void setExplanationButtonScheme(EExplanationButton explButton) {
		switch (scheme) {
		case FAILURE_SCHEME:
			explButton.setColors(Color.black, colorD, colorM);
			break;
		case CORRECT_SCHEME:
			explButton.setColors(Color.black, colorD, colorM);
			break;
		default:
			explButton.setColors(colorD, Color.black, colorM);
			break;
		}
	} //setScheme

	/**
	 * Zeichnet das Erscheinungsbild.
	 * 
	 * @param Graphics-Objekt
	 *            auf das gezeichnet werden soll
	 */
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		//RenderingHints oldRh = g2d.getRenderingHints();

		g2d.setRenderingHints(antialiasOn); // AntiAliasing aktivieren

		Dimension curSize = getSize();
		RoundedRect outerRect = new RoundedRect(0.0, 0.0, curSize.getWidth(),
				curSize.getHeight(), 7.0);

		// Äußeres, abgerundetes Rechteck malen
		g2d.setColor(colorM);
		g2d.fill(outerRect);

		if (getComponentCount() > 0) {
			double leftMargin = (getComponentCount() > 1) ? getComponent(1)
					.getX() : getComponent(0).getSize().getWidth();
			double bottomMargin = 7.0;
			RoundedRect innerRect = new RoundedRect(leftMargin, 0.0, curSize
					.getWidth()
					- leftMargin, curSize.getHeight() - bottomMargin, 0.0, 7.0,
					0.0, 7.0);

			// Inneres abegrundetes Rechteck malen
			g2d.setColor(colorL);
			g2d.fill(innerRect);
		}

		//g2d.setRenderingHints(oldRh); // Zeichen-Einstellungen zurücksetzen
	} //paintComponent

	/**
	 * Initialisiert alle statischen Attribute
	 */
	public static void init() {
		// Textformatierungen festlegen
		Style regular = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);
		SimpleAttributeSet defAttrs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(defAttrs, "SansSerif");
		StyleConstants.setFontSize(defAttrs, 16);

		regular.addAttributes(defAttrs);
		styleContext.addStyle(StyleContext.DEFAULT_STYLE, regular);

		Style em = styleContext.addStyle("emphasized", regular);
		StyleConstants.setItalic(em, true);

		Style strong = styleContext.addStyle("strong", regular);
		StyleConstants.setBold(strong, true);
		
		Style title2 = styleContext.addStyle("title2", regular);
		StyleConstants.setBold(title2, true);

		Style code = styleContext.addStyle("code", regular);
		StyleConstants.setBold(code, true);
		StyleConstants.setFontFamily(code, "Courier");
		StyleConstants.setForeground(code, new Color(0, 50, 120));

		Style command = styleContext.addStyle("command", regular);
		StyleConstants.setFontFamily(command, "Arial");
		StyleConstants.setForeground(command, new Color(120, 0, 0));

		Style heading = styleContext.addStyle("heading", regular);
		StyleConstants.setFontSize(heading, (int) (StyleConstants
				.getFontSize(regular) * 1.5));
		StyleConstants.setBold(heading, true);

		Style indent = styleContext.addStyle("indent", regular);
		StyleConstants.setLeftIndent(indent, 30);

		Style noindent = styleContext.addStyle("noindent", regular);
		StyleConstants.setLeftIndent(noindent, 0);

		Style par = styleContext.addStyle("par", regular);
		StyleConstants.setLineSpacing(par, -20f);
		StyleConstants.setFontSize(par, 5);
		StyleConstants.setSpaceAbove(par, -10f);
	} //init

	class ExplanationListener implements ActionListener {
		private StyledText explanation;

		public ExplanationListener(StyledText expl) {
			explanation = expl;
		} //Konstruktor

		public void actionPerformed(ActionEvent e) {
			if ((explanation != null) && (explFrame != null)) {
				explFrame.setText(explanation); // neuen Text setzen
				explFrame.setVisible(true); // Fenster einblenden
			} //if
		} //actionPerformed
	} //class ExplanationListener

} //class ESkin
