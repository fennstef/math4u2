package math4u2.view.formula;

/**
 * Klasse für Operatoren (z.B. Summe, Produkt, sin, lim, etc.)
 * 
 * @author Erich Seifert
 * @version $Revision: 1.3 $
 */
public class OpBox extends StringBox {
	/**
	 * Konstruktor, der ein neues <code>OpBox</code>-Objekt
	 * erzeugt und initialisiert.
     * @param frc Darstellungsumgebung
	 * @param content Inhalt der dargestellt werden soll
	 */
    public OpBox(FormulaRenderContext frc, String content) {
        super(frc, content, false);
		//return (float)super.getHeight()/2f + getRenderContext().getAxisHeight()*getDisplayHeight();
    }
}