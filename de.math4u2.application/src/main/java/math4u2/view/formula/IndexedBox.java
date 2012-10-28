package math4u2.view.formula;

import java.awt.Component;
import java.awt.Insets;

import math4u2.view.layout.IndexLayout;

/**
 * IndexedContainerBox
 * 
 * @author erich
 * @version $Revision: 1.6 $
 */
public class IndexedBox extends ContainerBox {
    private final IndexLayout layout;

    private Component center;

    /**
     * Konstruktor, der ein neues <code>IndexedBox</code>-Objekt
     * erzeugt und initialisiert.
     * @param frc Darstellungsumgebung
     */
    public IndexedBox(FormulaRenderContext frc) {
        this(frc, null);
    }

    /**
     * Konstruktor, der ein neues <code>IndexedBox</code>-Objekt
     * erzeugt und initialisiert.
     * @param frc Darstellungsumgebung
     * @param content
     */
    public IndexedBox(FormulaRenderContext frc, String content) {
        super(frc);
        int mu = Math.round(getDisplayHeight() * FormulaRenderContext.MU);
        layout = new IndexLayout(new Insets(mu, mu, mu, mu), 0);
        setLayout(layout);
        if (content != null) {
            center = new OrdBox(getRenderContext(), content);
            add(center, IndexLayout.CENTER);
        }
    }

    /**
     * Überschriebene Methode.
     * @see math4u2.view.formula.AtomicBox#rebuild()
     */
    protected void rebuild() {
        super.rebuild();

		if (layout!=null) {
	        int mu = Math.round(getDisplayHeight() * FormulaRenderContext.MU);
	        layout.setGaps(new Insets(mu, mu, mu, mu), 0);
		}
    }

    /**
     * Überschriebene Methode, die versucht dem Container ein
     * Component-Objekt hinzuzufügen.
     * @throws IllegalArgumentException falls das Component-Objekt nicht das Interface FormulaElement implmentiert
     * @see java.awt.Container#addImpl(java.awt.Component, java.lang.Object, int)
     */
    protected void addImpl(Component comp, Object constraints, int index) {
		super.addImpl(comp, constraints, index);
		if (constraints.equals(IndexLayout.CENTER)) {
		    center = comp;
		}
		else {
			AtomicBox box = (AtomicBox)comp;
		    box.shrinkVisually();
		}
    }

    /**
     * Überschriebene Methode.
     * @see math4u2.view.formula.ContainerBox#getMainComponent()
     */
    protected Component getMainComponent() {
        return center;
    }

    public float getRealAscend() {
        if ((getMainComponent()==null) || !(getMainComponent() instanceof FormulaElement))
            return super.getRealAscend();
        return ((FormulaElement)getMainComponent()).getRealAscend();
    }

    public float getRealDescend() {
        if ((getMainComponent()==null) || !(getMainComponent() instanceof FormulaElement))
            return super.getRealDescend();
        return ((FormulaElement)getMainComponent()).getRealDescend();
    }
}