package math4u2.view.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;

import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.LineStyleConstants;
import math4u2.view.graph.util.SimpleScalarStringValueHolder;

/**
 * Abstrakte Graphen-Klasse
 * 
 * Diese Klasse organisiert übergeordnete Aufgaben wie Sichtbarkeit Strichdicke
 * und MathObjekt implementierung
 * 
 * @author Fenn Stefan
 * @version 1.0
 */
public abstract class AbstractSimpleGraph implements SimpleGraphInterface,
		LineStyleConstants {

	protected DrawAreaInterface da;
	protected IGraphSettings settings;
	protected IScalarStringHolder name;
	protected IScalarStringHolder index;

	public AbstractSimpleGraph(DrawAreaInterface da, IGraphSettings settings, IScalarStringHolder name) {
		this.da = da;
		this.settings = settings;
		this.name = name;
	}

	abstract public void paintGraph(Graphics gr);

	public void setColor(Color c) {
		settings.setColor(c);
	} // setColor

	public Color getColor() {
		return settings.getColor();
	}

	public void setLineStyle(int style) {
		settings.setLineStyle(style);
		da.graphHasChanged();
	} // setLineStyle

	public int getLineStyle() {
		return settings.getLineStyle();
	}

	public void setVisible(boolean b) {
		settings.setVisible(b);
		da.graphHasChanged();
	} // setVisible

	public boolean isVisible() {
		return settings.isVisible();
	}

	public IScalarStringHolder getName() {
		if(name==null){
			name = new SimpleScalarStringValueHolder("");
		}
		return name;
	}

	public void setName(IScalarStringHolder name) {
		this.name = name;
	}

	public IScalarStringHolder getIndex() {
		if(index==null){
			index = new SimpleScalarStringValueHolder("");
		}
		return index;
	}

	public void setIndex(IScalarStringHolder index) {
		this.index = index;
	}

	/**
	 * Beschränkt die X-Koordinate auf die Größe der Zeichenfläche
	 * 
	 * @param x
	 *            Zu zeichnende Breite
	 */
	protected int limitRangeX(int x) {
		x = Math.max(x, -20);
		return Math.min(x, da.getWidth() + 20);
	}// limitRangeX

	/**
	 * Beschränkt die Y-Koordinate auf die Größe der Zeichenfläche
	 * 
	 * @param y
	 *            Zu zeichnende Höhe
	 */
	public int limitRangeY(int y) {
		y = Math.max(y, -20);
		return Math.min(y, da.getHeight() + 20);
	}// limitRangeY

	public static Stroke getStroke(int thick, int lineStyle) {
		if (lineStyle == SOLID_STROKE)
			return new BasicStroke(thick, BasicStroke.JOIN_ROUND,
					BasicStroke.JOIN_ROUND);
		else if (lineStyle == DASH_STROKE) {
			float strokeThickness = thick;
			float miterLimit = 10f;
			float[] dashPattern = { 10f };
			float dashPhase = 0f;
			return new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase);
		} // else if DASH_STROKE
		else if (lineStyle == DOT_STROKE) {
			float strokeThickness = thick;
			float miterLimit = 10f;
			float[] dashPattern = { 3f, 5f };
			float dashPhase = 0f;
			return new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase);
		} // else if DOT_STROKE
		else if (lineStyle == DOT_DASH_STROKE) {
			float strokeThickness = thick;
			float miterLimit = 10f;
			float[] dashPattern = { 10f, 5f, 2f, 5f };
			float dashPhase = 0f;
			return new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase);
		} // else if DOT_DASH_STROKE
		else
			throw new IllegalArgumentException();
	}

	/** Erstellt ein Strickmuster-Objekt */
	public Stroke getStroke() {
		return getStroke(da.getStroke(), getLineStyle());
	}
	
	public String getIdentifier() {
		return name.getScalarOrNull();
	}
	
	public void detach() throws Exception {
	}

	public void renew() {
	}
}

