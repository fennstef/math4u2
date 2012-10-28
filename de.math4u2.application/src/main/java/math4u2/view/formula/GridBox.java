package math4u2.view.formula;

import math4u2.view.layout.CompactGridLayout;

/**
 * Behälter für tabellenförmige Formularelelemente (z.B. Matrizen, Vektoren,
 * usw.).
 * 
 * @author Erich Seifert
 * @version $Revision: 1.9 $
 */
public class GridBox extends ContainerBox {
    /** Layout-Objekt, dass die Anordnung der Elemente übernimmt */ 
    private final CompactGridLayout layout;

    /** Anzahl der Spalten */
    private final int cols;
    /** Anzahl der Zeilen */
    private final int rows;

    /** horizontaler Abstand zwischen den Spalten */
    private float padx;
    /** vertikaler Abstand zwischen den Zeilen */
    private float pady;

    /**
     * Konstruktor, der ein neues <code>GridBox</code>-Objekt
     * erzeugt und initialisiert.
     * @param frc Darstellungsumgebung
     * @param cols Anzahl der Spalten
     * @param rows Anzahl der Zeilen
     */
    public GridBox(FormulaRenderContext frc, int cols, int rows) {
        this(frc, cols, rows, 6f, 4f);
    }

    /**
     * Konstruktor, der ein neues <code>GridBox</code>-Objekt
     * erzeugt und initialisiert.
     * @param frc Darstellungsumgebung
     * @param rows Anzahl der Zeilen
     * @param cols Anzahl der Spalten
     * @param padx horizontaler Abstand zwischen den Spalten
     * @param pady vertikaler Abstand zwischen den Zeilen
     */
    public GridBox(FormulaRenderContext frc, int cols, int rows, float padx, float pady) {
        super(frc);

        this.cols = cols;
        this.rows = rows;
        this.padx = padx;
        this.pady = pady;

        int padX = Math.round(this.padx * FormulaRenderContext.MU*getDisplayHeight());
        int padY = Math.round(this.pady * FormulaRenderContext.MU*getDisplayHeight());
        layout = new CompactGridLayout(this.cols, this.rows, padX, padY);

        setLayout(layout);
    }

    /**
     * Überschriebene Methode.
     * @see math4u2.view.formula.FormulaElement#getBaseline()
     */    
    public float getBaseline() {
    	float h_2 = getHeight() / 2f;
    	float offset = getRenderContext().getAxisHeight() * getDisplayHeight();
        return h_2 + offset;
    }

    /**
     * Überschriebene Methode.
     * @see math4u2.view.formula.AtomicBox#rebuild()
     */
    protected void rebuild() {
        super.rebuild();

        int padX = Math.round(padx * getDisplayHeight() * FormulaRenderContext.MU);
        int padY = Math.round(pady * getDisplayHeight() * FormulaRenderContext.MU);
		if (layout!=null)
			layout.setGaps(padX, padY);
    }

}