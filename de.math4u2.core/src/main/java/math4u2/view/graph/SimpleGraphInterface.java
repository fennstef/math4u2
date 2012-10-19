package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;

public interface SimpleGraphInterface {
	void paintGraph(Graphics g);

	void setVisible(boolean b);

	boolean isVisible();

	public void setColor(Color c);

	public Color getColor();
	
	public String getIdentifier();
	
	public void detach() throws Exception;
	
	public void renew();
}//GraphInterface

