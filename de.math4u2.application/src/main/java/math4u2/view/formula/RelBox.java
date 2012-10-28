package math4u2.view.formula;

/**
 * Klasse für Realationsymbole (z.B. =, !=, <=, >=, etc.)
 * 
 * @author Erich Seifert
 * @version $Revision: 1.3 $
 */
public class RelBox extends StringBox {
	/**
	 * Konstruktor, der ein neues <code>RelBox</code>-Objekt
	 * erzeugt und initialisiert.
     * @param frc Darstellungsumgebung
	 * @param content Inhalt, der dargestellt werden soll
	 */
    public RelBox(FormulaRenderContext frc, String content) {
        super(frc, content, false);
    }
}