package math4u2.view.formula;

/**
 * @author Fenn Stefan
 *
 * Hier können beliebige Komponenten vom Typ 
 * <code>JComponent</code> eingefügt werden.
 */
public class TableBox extends AtomicBox {

    public TableBox(FormulaRenderContext frc) {
        super(frc);
    }//Konstruktor

	/**
	 * Überschriebene Methode, um das Element vertikal zu zentrieren.
	 * @see math4u2.view.formula.FormulaElement#getBaseline()
	 */
	public float getBaseline() {
		return (float)super.getHeight()/2f + getRenderContext().getAxisHeight()*getDisplayHeight();
	}
	
}//class TableBox
