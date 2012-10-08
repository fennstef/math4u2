package math4u2.view.graph.drawarea;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.view.graph.GraphInterface;

public interface DrawAreaRegistryInterface {
	void register(MathObject mo, GraphInterface gr,
            DrawAreaInterface da, Broker broker);
}
