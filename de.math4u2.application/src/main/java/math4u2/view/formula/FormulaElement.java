package math4u2.view.formula;

/**
 * Schnittstelle für alle Formelelemente
 * @author Erich Seifert
 * @version $Revision: 1.4 $
 */
public interface FormulaElement {
	/**
	 * Gibt die Höhe der Grundlinie zurück.
	 * @return Höhe der Grundlinie gegenüber der <em>Oberkante</em> in Pixeln
	 */
    float getBaseline();

    /**
     * Gibt einen Typ für die Abstandsberechnung zurück.
     * @return Typ für die Abstandsberechnung
     */
    Class getSpacingClass();

    /**
     * Gibt die tatsächliche Höhe zurück in der Element dargestellt wird.
     * @return tatsächliche Darstellungshöhe in Pixeln
     */
    float getDisplayHeight();

    /**
     * Gibt die Höhe des "x"-Zeichens zurück. Dies entspricht in etwa der Minuskelhöhe.
     * @return Höhe des "x"-Zeichens in Pixeln
     */
    float getXHeight();

    /**
     * Gibt die Oberlänge zurück.
     * @return Oberlänge
     */
    float getRealAscend();

    /**
     * Gibt die Unterlänge zurück.
     * @return Unterlänge
     */
    float getRealDescend();
}
