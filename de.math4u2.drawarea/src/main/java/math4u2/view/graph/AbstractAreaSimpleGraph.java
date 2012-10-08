package math4u2.view.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;

import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.LineStyleConstants;

public abstract class AbstractAreaSimpleGraph extends AbstractSimpleGraph{

	public AbstractAreaSimpleGraph(DrawAreaInterface da, IGraphSettings settings) {
		super(da,settings);
	}

	public boolean isFillArea() {
		return ((IAreaGraphSettings)settings).isFillArea();
	}

	public Color getFillColor() {
		return ((IAreaGraphSettings)settings).getFillColor();
	}

	public Color getBorderColor() {
		return ((IAreaGraphSettings)settings).getBorderColor();
	}

	public void setFillArea(boolean fillArea) {
		((IAreaGraphSettings)settings).setFillArea(fillArea);
	}

	public void setFillColor(Color fillColor) {
		((IAreaGraphSettings)settings).setFillColor(fillColor);
	}

	public void setBorderColor(Color borderColor) {
		((IAreaGraphSettings)settings).setBorderColor(borderColor);
	}
}
