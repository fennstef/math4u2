package math4u2.view.formula;

/**
 * Klasse für binäre Formelelemente (z.B. +, -, *, etc.)
 * 
 * @author Erich Seifert
 * @version $Revision: 1.3 $
 */
public class BinBox extends StringBox {
	/**
	 * Konstruktor, der ein neues <code>BinBox</code>-Objekt
	 * erzeugt und initialisiert.
	 * @param frc Darstellungsumgebung
	 * @param content Inhalt für das Formelelement
	 */
    public BinBox(FormulaRenderContext frc, String content) {
        super(frc, content, false);
    }

}