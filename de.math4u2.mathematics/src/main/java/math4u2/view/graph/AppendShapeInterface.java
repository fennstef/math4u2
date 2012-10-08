package math4u2.view.graph;

import java.awt.geom.GeneralPath;

import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.graph.drawarea.DrawAreaInterface;

public interface AppendShapeInterface {
	public void appendShape( GeneralPath gp, DrawAreaInterface da, UserFunction own)
	    throws MathException ;
}
