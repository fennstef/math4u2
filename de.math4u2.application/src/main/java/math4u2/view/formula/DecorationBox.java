package math4u2.view.formula;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;

public class DecorationBox extends StringBox {
    /**
     * Konstruktor der eine neue Box für <i>Verzierungen</i> erstellt und
     * initialisiert.
     * @param frc Darstellungsumgebung
     * @param content Inhalt
     */
    public DecorationBox(FormulaRenderContext frc, String content) {
        super(frc, content, true);
    }

    /**
     * Ueberschriebene Methode.
     * @see math4u2.view.formula.StringBox#rebuild()
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
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getMaximumSize().height));
    }
}
