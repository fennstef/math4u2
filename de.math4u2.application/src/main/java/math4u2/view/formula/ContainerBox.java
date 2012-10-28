package math4u2.view.formula;

import java.awt.Component;

import math4u2.view.layout.FormulaLayout;

/**
 * Box, die wiederum andere Boxen speichern kann und anhand der Grundlinie
 * nebeneinander ausrichtet.
 * 
 * @author Erich Seifert
 * @version $Revision: 1.11 $
 */
public class ContainerBox extends AtomicBox {

	/**
	 * Konstruktor, der ein neues <code>ContainerBox</code> -Objekt erzeugt
	 * und initialisiert.
	 * 
	 * @param frc
	 */
	public ContainerBox(FormulaRenderContext frc) {
		super(frc);
		setLayout(new FormulaLayout());
	}

	/**
	 * Überschriebene Methode, die statt der Grundlinie des Behälters, die des
	 * eingeklammerten Inhalts zurückgibt.
	 * 
	 * @see math4u2.view.formula.FormulaElement#getBaseline()
	 */
	public float getBaseline() {
		doLayout();
		if (getMainComponent() != null) {
			float yPos = getMainComponent().getY();
			float baseline = ((FormulaElement) getMainComponent())
					.getBaseline();
			return yPos + baseline;
		}

		return super.getBaseline();
	}

	/**
	 * Überschriebene Methode, die versucht dem <code>Container</code> ein
	 * <code>Component</code> -Objekt hinzuzufügen. Es dürfen jedoch nur
	 * Komponenten vom Typ <code>AtomicBox</code> hinzugefügt werden.
	 * 
	 * @throws IllegalArgumentException
	 *             falls das Component-Objekt nicht das Interface FormulaElement
	 *             implmentiert
	 * @see java.awt.Container#addImpl(java.awt.Component, java.lang.Object,
	 *      int)
	 */
	protected void addImpl(Component comp, Object constraints, int index) {
		if (comp instanceof AtomicBox) {
			AtomicBox box = (AtomicBox) comp;
			box.setRenderContext(getRenderContext());
			box.setRelSize(getRelSize());
		}
		super.addImpl(comp, constraints, index);
	}

	/**
	 * Überschriebene Methode, die für den Formel-Layout-Prozess statt dem Typ
	 * der Behälterklasse den Typ des Inhalts zurückgibt.
	 * 
	 * @see math4u2.view.formula.FormulaElement#getSpacingClass()
	 */
	public Class getSpacingClass() {
		if (getMainComponent() != null)
			return ((FormulaElement) getMainComponent()).getSpacingClass();
		return super.getSpacingClass();
	}

	/**
	 * Überschriebene Methode, die neben dem Behälter auch die Größen aller
	 * Inhaltsobjekte verändert.
	 * 
	 * @see math4u2.view.formula.AtomicBox#shrinkRelSize()
	 */
	public void shrinkRelSize() {
		super.shrinkRelSize();

		int numComps = getComponentCount();

		for (int i = 0; i < numComps; i++) {
			Component c = getComponent(i);
			if (c instanceof AtomicBox) {
				AtomicBox box = (AtomicBox) c;
				box.shrinkRelSize();
			}
		}
	}

	/**
	 * Überschriebene Methode, die eine Darstellungsumgebung setzt und
	 * gleichzeitig an die Inhaltsobjekte dieses Behälters weitergibt.
	 * 
	 * @see math4u2.view.formula.AtomicBox#setRenderContext(math4u2.view.formula.FormulaRenderContext)
	 */
	public void setRenderContext(FormulaRenderContext frc) {
		super.setRenderContext(frc);

		int numComps = getComponentCount();

		for (int i = 0; i < numComps; i++) {
			Component c = getComponent(i);
			if (c instanceof AtomicBox) {
				AtomicBox box = (AtomicBox) c;
				box.setRenderContext(frc);
			}
		}
	}

	/**
	 * Gibt die Hauptkomponente dieses Behälters zurück.
	 * 
	 * @return Hauptkomponente des Behälters
	 */
	protected Component getMainComponent() {
		if (getComponentCount() > 0)
			return getComponent(0);
		return null;
	}

	public float getRealAscend() {
	    float maxAscend = 0f;

	    int numComps = getComponentCount();
		for (int i = 0; i < numComps; i++) {
			Component c = getComponent(i);
		    if (c instanceof FormulaElement) {
		    	FormulaElement e = (FormulaElement)c;
		    	float baselineDistCont = Math.abs(getBaseline()-c.getY());
		    	float baselineDistElem = Math.abs(baselineDistCont-e.getBaseline());
		    	float ascend = baselineDistElem+e.getRealAscend();
			    maxAscend = Math.max(maxAscend, ascend);
		    }
		}
        return maxAscend;
	}

	public float getRealDescend() {
	    float maxDescend = 0f;

	    int numComps = getComponentCount();
		for (int i = 0; i < numComps; i++) {
			Component c = getComponent(i);
		    if (c instanceof FormulaElement) {
		    	FormulaElement e = (FormulaElement)c;
		    	float baselineDistCont = Math.abs(getBaseline()-c.getY());
		    	float baselineDistElem = Math.abs(baselineDistCont-e.getBaseline());
		    	float descend = baselineDistElem+e.getRealDescend();
			    maxDescend = Math.max(maxDescend, descend);
		    }
		}
        return maxDescend;
	}

}