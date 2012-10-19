package math4u2.view.graph;


import java.awt.Color;
import java.awt.Graphics;

import math4u2.controller.MathObject;

public interface GraphInterface extends MathObject, SimpleGraphInterface {
	void paintGraph(Graphics g);

	HasGraph getModel();

	void setVisible(boolean b);

	boolean isVisible();

	public void setColor(Color c);

	public Color getColor();
}//GraphInterface
