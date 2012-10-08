package math4u2.view.graph;

import java.awt.Color;

public class DefaultAreaGraphSettings extends DefaultGraphSettings implements IAreaGraphSettings{
	private boolean fillArea;
	private Color fillColor;
	private Color borderColor;
	
	public boolean isFillArea() {
		return fillArea;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setFillArea(boolean fillArea) {
		this.fillArea = fillArea;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	
}
