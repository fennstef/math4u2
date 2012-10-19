package math4u2.view.graph;

import java.awt.Color;

import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarStringHolder;

public abstract class AbstractAreaSimpleGraph extends AbstractSimpleGraph{

	public AbstractAreaSimpleGraph(DrawAreaInterface da, IGraphSettings settings, IScalarStringHolder name) {
		super(da,settings,name);
	}

	public Color getFillColor() {
		return ((IAreaGraphSettings)settings).getFillColor();
	}

	public Color getBorderColor() {
		return ((IAreaGraphSettings)settings).getBorderColor();
	}

	public void setFillColor(Color fillColor) {
		((IAreaGraphSettings)settings).setFillColor(fillColor);
	}

	public void setBorderColor(Color borderColor) {
		((IAreaGraphSettings)settings).setBorderColor(borderColor);
	}
}
