package de.math4u2.drawarea;

import java.awt.Color;
import java.awt.Graphics;

import math4u2.view.graph.SimpleGraphInterface;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;

public class SimpleGraphPoint implements SimpleGraphInterface{
	private IScalarDoubleHolder x;
	private IScalarDoubleHolder y;
	private DrawAreaInterface da;
	private boolean visible=true;
	private Color color = Color.BLACK;
	
	public SimpleGraphPoint(IScalarDoubleHolder x, IScalarDoubleHolder y, DrawAreaInterface da){
		this.x = x;
		this.y = y;
		this.da = da;
	}
	
	public void paintGraph(Graphics g) {
		if(!visible) return;
		int xC = da.xCoordToPix(x.getScalarOrNan());
		int yC = da.yCoordToPix(y.getScalarOrNan());
		g.setColor(color);
		g.fillOval(xC-2, yC-2, 5, 5);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public String getIdentifier() {
		return "p";
	}

	public void detach() throws Exception {
	}

	public void renew() {
	}

}
