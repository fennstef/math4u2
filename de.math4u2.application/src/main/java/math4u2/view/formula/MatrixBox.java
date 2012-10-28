package math4u2.view.formula;



/**
 * Klasse für eingeklammerte Matrizen und Vektoren
 * 
 * @author Erich Seifert
 * @version $Revision: 1.5 $
 */
public class MatrixBox extends BracketedBox {

    /**
     * Konstruktor, der ein neues <code>MatrixBox</code>-Objekt
     * mit dem Standard-Klammertyp erzeugt und initialisert.
     * @param frc Darstellungsoptionen
     */
    public MatrixBox(FormulaRenderContext frc) {
        this(frc, TYPE_PARENTHESE);
    }

    /**
     * Konstruktor, der ein neues <code>MatrixBox</code>-Objekt
     * eines bestimmten Klammertyps erzeugt und initialisert.
     * @param frc Darstellungsoptionen
     * @param type Typ für Anfangs- und Endklammer
     */
    public MatrixBox(FormulaRenderContext frc, byte type) {
        this(frc, type, type);
    }

    /**
     * Konstruktor, der ein neues <code>MatrixBox</code>-Objekt
     * mit unterschiedlichen Anfangs- und Endklammern erzeugt
     * und initialisert.
     * @param frc Darstellungsoptionen
     * @param openType Typ der Anfangsklammer
     * @param closeType Typ der Endklammer
     */
    public MatrixBox(FormulaRenderContext frc, byte openType, byte closeType) {
        super(frc, openType, closeType);
        //setLayout(new FormulaLayout());

        // Abstand von Klammern zum Matrixinhalt einfügen
        int csp = (int)Math.round(3f*getContent().getDisplayHeight()*FormulaRenderContext.MU);
		getContent().addSpacing(0, csp, 0, csp);
    }

	/**
	 * Überschriebene Methode für eine korrekte Abstandsberechnung.
	 * @see math4u2.view.formula.FormulaElement#getSpacingClass()
	 */
	public Class getSpacingClass() {
		return BracketedBox.class;
	}

}