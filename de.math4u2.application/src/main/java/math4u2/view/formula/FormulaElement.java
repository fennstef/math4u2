package math4u2.view.formula;

/**
 * Schnittstelle f�r alle Formelelemente
 * @author Erich Seifert
 * @version $Revision: 1.4 $
 */
public interface FormulaElement {
	/**
	 * Gibt die H�he der Grundlinie zur�ck.
	 * @return H�he der Grundlinie gegen�ber der <em>Oberkante</em> in Pixeln
	 */
    float getBaseline();

    /**
     * Gibt einen Typ f�r die Abstandsberechnung zur�ck.
     * @return Typ f�r die Abstandsberechnung
     */
    Class getSpacingClass();

    /**
     * Gibt die tats�chliche H�he zur�ck in der Element dargestellt wird.
     * @return tats�chliche Darstellungsh�he in Pixeln
     */
    float getDisplayHeight();

    /**
     * Gibt die H�he des "x"-Zeichens zur�ck. Dies entspricht in etwa der Minuskelh�he.
     * @return H�he des "x"-Zeichens in Pixeln
     */
    float getXHeight();

    /**
     * Gibt die Oberl�nge zur�ck.
     * @return Oberl�nge
     */
    float getRealAscend();

    /**
     * Gibt die Unterl�nge zur�ck.
     * @return Unterl�nge
     */
    float getRealDescend();
}
