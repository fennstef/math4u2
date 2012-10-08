package math4u2.mathematics.affine;

import java.awt.Color;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

/**
 * Grundgerüst für ein grafisches Objekt mit Füllfläche.
 * 
 * @author Fenn Stefan
 */
public abstract class AbstractArea extends AbstractAffineObject implements
        AreaInterface {

    @Override
	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return null;
	}

	@Override
	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
	}

	@Override
	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return null;
	}

	@Override
	public String bodyToString() {
		return null;
	}

	@Override
	public void applyProperties(HasGraph oldObject) {
		super.applyProperties(oldObject);
		setFillColor(((AreaInterface) oldObject).getFillColor());
		setBorderColor(((AreaInterface) oldObject).getBorderColor());
	}

	/** Füllfarbe */
    protected Color fillColor;

    /** wird die Fläche angezeigt */
    private boolean fillArea;

    public AbstractArea(String type, String name, Color color, boolean fillArea,
            int lineStyle, boolean isVisible, Broker broker, ViewFactoryInterface viewFactory) {
        super(type, name, color, lineStyle, isVisible, broker, viewFactory);
        this.fillArea = fillArea;
        //Damit die Füllung berechnet wird
        setColor(color);
    }//Konstruktor

    public boolean isFillArea() {
        return fillArea;
    }//isFillArea

    public Color getFillColor() {
        return fillColor;
    }//getFillColor

    public void setFillColor(Color c) {
        fillColor = c;
    }//setFillColor

    /**
     * Die Füllfläche besitzt die gleiche Farbe aber mit 50% der ursprünglichen
     * Deckungskraft.
     */
    public void setColor(Color c) {
        setBorderColor(c);
        fillColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), c
                .getAlpha() / 2);
    }//setColor

    public Color getBorderColor() {
        return getColor();
    }//getBorderColor

    public void setBorderColor(Color c) {
        super.setColor(c);
    }//setBorderColor

}//class AbstractArea
