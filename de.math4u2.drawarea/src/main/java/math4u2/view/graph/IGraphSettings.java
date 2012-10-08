package math4u2.view.graph;

import java.awt.Color;

public interface IGraphSettings {

	void setColor(Color c);

	Color getColor();

	void setLineStyle(int style);

	int getLineStyle();

	void setVisible(boolean b);

	boolean isVisible();

}
