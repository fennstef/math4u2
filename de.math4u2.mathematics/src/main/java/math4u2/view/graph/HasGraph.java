package math4u2.view.graph;

import java.awt.Color;

import math4u2.controller.MathObject;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.graph.drawarea.DrawAreaInterface;

/**
 * Interface HasGraph Diese Schnittstelle sichert, dass das Objekt einen Graphen
 * erzeugen kann und die funktionsspezifischen Daten (wie z.B. Farbe,
 * Strichstil) hält
 * 
 * @author Fenn Stefan
 */
public interface HasGraph extends MathObject {

	/** Durchgehende Linie */
	static final int SOLID_STROKE = 0;

	/** Gestrichelte Linie */
	static final int DASH_STROKE = 1;

	/** Gepunktete Linie */
	static final int DOT_STROKE = 2;

	/** Punkt-Strich Linie */
	static final int DOT_DASH_STROKE = 3;

	/** Array der Linestyles */
	static final String[] LINE_STYLES = new String[] { "solid", "dash", "dot",
			"dot-dash" };

	/** 
	 * Erzeugt oder holt einen Graphen.
	 * 
	 * @param da Zeichenfläche
	 * @param f UserFunction, die evaluiert, den HasGraph zurück gibt. 
	 */
	GraphInterface getGraph(DrawAreaInterface da, UserFunction f);

	/** Farben aller Graphen */
	void setColor(Color c);

	Color getColor();

	/** Strichstil aller Graphen */
	void setLineStyle(int lineStyle);

	int getLineStyle();

	/** Sichtbarkeit aller Graphen */

	boolean isVisible();

	void setVisible(boolean b);

	/** Modifizierbarkeit der Objekte */

	boolean isFreeze();

	void setFreeze(boolean b);
	
	/** Ist im aktuellen Zustand ein Graph erzeugbar? */

	boolean hasCurrentObjectGraph();
	
	void applyProperties(HasGraph oldObject);

} //HasGraph
