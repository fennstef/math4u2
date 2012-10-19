package math4u2.view.graph;

import java.awt.geom.GeneralPath;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.graph.drawarea.DrawAreaInterface;

public interface GraphInterfaceFactory {

	GraphInterface createBubbleGraph(DrawAreaInterface da, UserFunction f,
			Broker broker);

	GraphInterface createPointGraph(DrawAreaInterface da, UserFunction f,
			Broker broker);

	GraphInterface createAngleGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createAreaGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createArrowGraph(DrawAreaInterface da, UserFunction f,
			Broker broker);

	GraphInterface createBarGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createBezierGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createCircleGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createCurveGraph(DrawAreaInterface da, UserFunction f);

	void appendShape(GeneralPath gp, GraphInterface graph);

	GraphInterface createDiscreteGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createDiscreteSequenceGraph(DrawAreaInterface da,
			UserFunction f);

	GraphInterface createFieldVectorGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createMapGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createMarkerGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createStraightGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createStretchGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createListGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createVectorElementGraph(DrawAreaInterface da,
			UserFunction userFunction);
	
	GraphInterface createFunctionGraph(DrawAreaInterface da, UserFunction f);

	GraphInterface createSimpleGraph(DrawAreaInterface da, UserFunction f);
}
