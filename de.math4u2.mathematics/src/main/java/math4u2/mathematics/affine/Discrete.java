package math4u2.mathematics.affine;

import java.awt.Color;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class Discrete extends AbstractDiscrete {

	public Discrete(String name, MathObject xVector, MathObject yVector,
			MathObject rFunc, Color color, int lineStyle, boolean isVisible,
			Broker broker, ViewFactoryInterface viewFactory) {

		super("punkte", name, xVector, yVector, rFunc, color, lineStyle,
				isVisible, broker, viewFactory);
	} // Konstruktor

	public Discrete(MathObject xVector, MathObject yVector, MathObject rFunc,
			Broker broker, ViewFactoryInterface viewFactory) {
		this(null, xVector, yVector, rFunc, Color.black, HasGraph.SOLID_STROKE,
				true, broker, viewFactory);
	}

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createDiscreteGraph(da, f);
	} // getGraph

} // class Discrete
