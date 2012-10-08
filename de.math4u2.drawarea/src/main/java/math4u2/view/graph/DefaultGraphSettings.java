package math4u2.view.graph;

import java.awt.Color;

import math4u2.view.graph.util.LineStyleConstants;

public class DefaultGraphSettings implements IGraphSettings, LineStyleConstants {
	private boolean visible=true;
	private Color color = Color.BLACK;
	private int lineStyle = SOLID_STROKE;

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

	public void setLineStyle(int style) {
		this.lineStyle = style;
	}

	public int getLineStyle() {
		return lineStyle;
	}
}
