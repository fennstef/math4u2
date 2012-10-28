package math4u2.view.formula;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Klasse für Operatoren (z.B. Summe, Produkt, sin, lim, etc.)
 * 
 * @author Erich Seifert
 * @version $Revision: 1.4 $
 */
public class BigOpBox extends OpBox {

	/**
	 * Konstruktor, der ein neues <code>BigOpBox</code>-Objekt
	 * erzeugt und initialisiert.
     * @param frc Darstellungsumgebung
	 * @param content Inhalt, der dargestellt werden soll
	 */
    public BigOpBox(FormulaRenderContext frc, String content) {
        super(frc, content);
    }

    /**
     * Ueberschriebene Methode.
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(0.0, super.getBaseline()-getBaseline());
        super.paint(g2);
    }

	/**
	 * Überschriebene Methode, um das Element vertikal zu zentrieren.
	 * @see math4u2.view.formula.FormulaElement#getBaseline()
	 */
	public float getBaseline() {
		return (super.getHeight() + getRenderContext().getAxisHeight()*getDisplayHeight())/2f;
	}

	/**
	 * Überschriebene Methode, um die Standardsymbolgröße zu erhöhen.
	 * @see math4u2.view.formula.AtomicBox#setFontSize(float)
	 */
	public void setFontSize(float fontSize) {
		super.setFontSize(fontSize*getRenderContext().getBigOpScale());
	}

	/**
	 * Überschriebene Methode für eine korrekte Abstandsberechnung.
	 * @see math4u2.view.formula.FormulaElement#getSpacingClass()
	 */
	public Class getSpacingClass() {
		return OpBox.class;
	}

}