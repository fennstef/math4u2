package math4u2.view.formula;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;

/**
 * Klasse für Klammern
 * 
 * @author Erich Seifert
 * @version $Revision: 1.4 $
 */
public class BracketBox extends StringBox {
	/** interne Klasse für Klammertypen */
    public static final class Bracket {
        public final String standard;

        public final String top;

        public final String middle;

        public final String extender;

        public final String bottom;

        public Bracket(String standard, String top, String middle,
                String extender, String bottom) {
            this.standard = standard;
            this.top = top;
            this.middle = middle;
            this.extender = extender;
            this.bottom = bottom;
        }
    }

    /**
     * Konstruktor, der ein neues <code>BracketBox</code>-Objekt
     * erzeugt und initialisiert.
	 * @param frc Darstellungsumgebung
	 * @param symbol Klammersymbol, das dargestellt werden soll
     */
    public BracketBox(FormulaRenderContext frc, Bracket symbol) {
        super(frc, symbol.standard, true);
    }

    /**
     * Überschriebene Methode.
     * @see math4u2.view.formula.AtomicBox#rebuild()
     */
    protected void rebuild() {
        super.rebuild();
		if (getShape()!=null) {
	        Rectangle2D bounds = getShape().getBounds2D();
			Insets insets = getInsets();

			Dimension dim = new Dimension(
					insets.left + (int)Math.ceil(bounds.getWidth()) + insets.right,
					insets.top + (int)Math.ceil(bounds.getHeight()) + insets.bottom
					);
			setMinimumSize(dim);
			setPreferredSize(dim);

			setBaseline(-(float)bounds.getMinY());
		}
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

}