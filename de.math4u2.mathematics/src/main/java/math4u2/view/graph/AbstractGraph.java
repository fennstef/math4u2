package math4u2.view.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import math4u2.controller.MathObject;
import math4u2.controller.reference.PathStep;
import math4u2.controller.relation.RelationContainer;
import math4u2.util.exception.NotImplementedException;
import math4u2.view.graph.drawarea.DrawAreaInterface;

/**
 * Abstrakte Graphen-Klasse
 * 
 * Diese Klasse organisiert übergeordnete Aufgaben wie Sichtbarkeit Strichdicke
 * und MathObjekt implementierung
 * 
 * @author Fenn Stefan
 * @version 1.0
 */
public abstract class AbstractGraph implements GraphInterface {

	protected DrawAreaInterface da;

	protected RelationContainer relationContainer;

	public AbstractGraph(DrawAreaInterface da) {
		this.relationContainer = new RelationContainer(this);
		this.da = da;
	} //Konstruktor

	abstract public void paintGraph(Graphics gr);

	abstract public HasGraph getModel();

	abstract public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception;

	public void setColor(Color c) {
		getModel().setColor(c);
	} //setColor

	public Color getColor() {
		return getModel().getColor();
	}

	public void setLineStyle(int style) {
		getModel().setLineStyle(style);
		da.graphHasChanged();
	} //setLineStyle

	public int getLineStyle() {
		return getModel().getLineStyle();
	}

	public void setVisible(boolean b) {
		getModel().setVisible(b);
		da.graphHasChanged();
	} //setVisible

	public boolean isVisible() {
		return getModel().isVisible();
	}
	
	/**
	 * Beschränkt die X-Koordinate auf die Größe der Zeichenfläche
	 * @param x Zu zeichnende Breite
	 */
	protected int limitRangeX(int x){
	    x = Math.max(x,-20);
	    return Math.min(x,da.getWidth()+20);   
	}//limitRangeX
	
	/**
	 * Beschränkt die Y-Koordinate auf die Größe der Zeichenfläche
	 * @param y Zu zeichnende Höhe
	 */
	public int limitRangeY(int y){
	    y = Math.max(y,-20);
	    return Math.min(y,da.getHeight()+20);	   
	}//limitRangeY	

	public static Stroke getStroke(int thick, int lineStyle) {
		if (lineStyle == HasGraph.SOLID_STROKE)
			return new BasicStroke(thick,BasicStroke.JOIN_ROUND,BasicStroke.JOIN_ROUND);
		else if (lineStyle == HasGraph.DASH_STROKE) {
			float strokeThickness = thick;
			float miterLimit = 10f;
			float[] dashPattern = { 10f };
			float dashPhase = 0f;
			return new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase);
		} //else if DASH_STROKE
		else if (lineStyle == HasGraph.DOT_STROKE) {
			float strokeThickness = thick;
			float miterLimit = 10f;
			float[] dashPattern = { 3f, 5f };
			float dashPhase = 0f;
			return new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase);
		} //else if DOT_STROKE
		else if (lineStyle == HasGraph.DOT_DASH_STROKE) {
			float strokeThickness = thick;
			float miterLimit = 10f;
			float[] dashPattern = { 10f, 5f, 2f, 5f };
			float dashPhase = 0f;
			return new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase);
		} //else if DOT_DASH_STROKE
		else
			throw new IllegalArgumentException();
	}//getStroke

	/** Erstellt ein Strickmuster-Objekt */
	public Stroke getStroke() {
		return getStroke(da.getStroke(), getLineStyle());
	} //getStroke

	public String toString() {
		return getModel().toString();
	}

	//MathObject Implementierungen
	public void renew(MathObject source) {
	}

	public boolean testSubstitution(MathObject a, Set oldAggregateSet) {
		return true;
	} //testSubstitiution

	public boolean testDelete() {
		return true;
	} //testDelete

	public void prepareDelete() {
		SimpleGraphInterface g = this;
		da.removeGraph(g);
	}//prepareDelete

	public Object getIdentifier() {
		Object id = getModel().getIdentifier();
		if(id==null) return null;
		return id.toString();
	} //getKey
	
	public String getKey() {
		if(getKey()==null) return null;
		return getKey().toString();
	} //getKey

	public void setName(String name) {
		//		throw new NullPointerException("Die Methode ist noch nicht
		// implementiert");
	} //setName

	public void setName(String name, String index) {
		//		throw new NullPointerException("Die Methode ist noch nicht
		// implementiert");
	} //setName

	public RelationContainer getRelationContainer() {
		return relationContainer;
	} //getRelationContainer

	public PathStep createPathStep(List methods) {
		throw new NotImplementedException();
	}//createPathStep

	public Class getReturnType(PathStep nextStep) {
		throw new NotImplementedException();
	}//getReturnType

	public void detach() throws Exception {
		prepareDelete();
	}

	public void renew() {
		renew(this);
	}
} //AbstractGraph
