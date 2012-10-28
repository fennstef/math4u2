package math4u2.view.formula;

/**
 * @author Fenn Stefan
 *
 * Hier k�nnen beliebige Komponenten vom Typ 
 * <code>JComponent</code> eingef�gt werden.
 */
public class TableBox extends AtomicBox {

    public TableBox(FormulaRenderContext frc) {
        super(frc);
    }//Konstruktor

	/**
	 * �berschriebene Methode, um das Element vertikal zu zentrieren.
	 * @see math4u2.view.formula.FormulaElement#getBaseline()
	 */
	public float getBaseline() {
		return (float)super.getHeight()/2f + getRenderContext().getAxisHeight()*getDisplayHeight();
	}
	
}//class TableBox
