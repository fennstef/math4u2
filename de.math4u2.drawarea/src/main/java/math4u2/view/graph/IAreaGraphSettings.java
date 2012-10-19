package math4u2.view.graph;

import java.awt.Color;

public interface IAreaGraphSettings extends IGraphSettings{

	Color getFillColor();

	Color getBorderColor();

	void setFillColor(Color fillColor);

	void setBorderColor(Color borderColor);
}
