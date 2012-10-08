package math4u2.view.graph;

import java.awt.Color;

public interface IAreaGraphSettings extends IGraphSettings{

	boolean isFillArea();

	Color getFillColor();

	Color getBorderColor();

	void setFillArea(boolean fillArea);

	void setFillColor(Color fillColor);

	void setBorderColor(Color borderColor);
}
