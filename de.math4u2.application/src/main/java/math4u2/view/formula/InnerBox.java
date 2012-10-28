package math4u2.view.formula;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Box für Brüche
 * 
 * @author Erich Seifert
 * @version $Revision: 1.8 $
 */
public class InnerBox extends GridBox {
	/** Gibt an, ob der Bruch bereits verkleinert wurde. */
	private boolean shrunk;
	/** Abstand zwischen Bruchstrich und oberen/unteren Term */
    private static final float PADDING = 3f;

    /**
     * Konstruktor, der ein neues <code>InnerBox</code>-Objekt
     * erzeugt und initialisiert.
     * @param frc Darstellungsumgebung
     */
    public InnerBox(FormulaRenderContext frc) {
        super(frc, 1, 2, 0f, 2f * PADDING + frc.getLineWidth()/FormulaRenderContext.MU);
    }

    /**
     * Überschriebene Methode.
     * @see math4u2.view.formula.FormulaElement#getBaseline()
     */
    public float getBaseline() {
        float axisHeight = getAxisHeight();
        if (axisHeight >= 0f) {
            doLayout();
            float offset = getRenderContext().getAxisHeight() * getDisplayHeight();
            return axisHeight + offset;
        }
        return super.getBaseline();
    }

    /**
     * Überschriebene Methode.
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        super.paint(g);

        if (getComponentCount() == 2) {
            Graphics2D g2 = (Graphics2D) g;

            Stroke oldStroke = g2.getStroke();
            float strokeWidth = getRenderContext().getLineWidth()*getDisplayHeight();
            g2.setStroke(new BasicStroke(strokeWidth));

            float axisHeight = getAxisHeight();
            g2.draw(new Line2D.Float(0f, axisHeight, getWidth()-1, axisHeight));

            g2.setStroke(oldStroke);
        }
    }

    /**
     * Gibt die Lage des Bruchstriches gegenüber der Grundlinie zurück.
     * @return Lage des Bruchstriches
     */
    private float getAxisHeight() {
        if (getComponentCount() >= 1) {
            float axisHeight = getComponent(0).getPreferredSize().height;
            axisHeight += (PADDING*FormulaRenderContext.MU + getRenderContext().getLineWidth()/2f) * getDisplayHeight();
            return axisHeight;
        }
        return -1f;
    }

    /**
     * Überschriebene Methode.
     * @see java.awt.Container#addImpl(java.awt.Component, java.lang.Object, int)
     */
    protected void addImpl(Component comp, Object constraints, int index) {
        int numComps = getComponentCount();

        if (numComps >= 2)
            throw new IllegalArgumentException(
                    "This container can only store two components at a time.");

        if ((numComps < 2) && !shrunk) {
            Dimension prf = getPreferredSize();
            if ((float) prf.height / (float) prf.width >= 1.25f) {
                shrinkRelSize();
				shrunk = true;
            }
        }

        super.addImpl(comp, constraints, index);
	}

    /**
     * Überschriebene Methode.
     * @see math4u2.view.formula.AtomicBox#rebuild()
     */
    protected void rebuild() {
        super.rebuild();
        int sp = Math.round(FormulaRenderContext.MU * getDisplayHeight());
        setSpacing(0, sp, 0, sp);
    }

}