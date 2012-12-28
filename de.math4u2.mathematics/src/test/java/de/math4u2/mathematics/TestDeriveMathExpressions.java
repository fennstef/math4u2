package de.math4u2.mathematics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.parser.parser;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.IExceptionFrame;
import math4u2.util.exception.parser.ParseException;
import math4u2.view.graph.GraphInterfaceFactory;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("unused")
public class TestDeriveMathExpressions extends AbstractMathTest {
	@Test
	public void testDerive01() throws Exception {
		UserFunction a = parseUserFunction("a(x):=x*sin(x)");
		UserFunction b = parseUserFunction("b(x):=derive(a(x))");
		UserFunction c = parseUserFunction("c(x):=sin(x)+x*cos(x)");
		assertFunc1DimEvalEquals(b, c);
	}

	@Test
	public void testDerive02() throws Exception {
		UserFunction a = parseUserFunction("a(x):=x*sin(x)");
		UserFunction b = parseUserFunction("b(x):=derive(a(x),3)");
		UserFunction c = parseUserFunction("c(x):=-3*sin(x)-x*cos(x)");
		assertFunc1DimEvalEquals(b, c);
	}

	@Test
	public void testDerive03() throws Exception {
		UserFunction a = parseUserFunction("a(x,y):=pderive(x*y*sin(y), x, y, y)");
		UserFunction b = parseUserFunction("b(y):=a(0,y)");
		UserFunction c = parseUserFunction("c(y):=2*cos(y)-y*sin(y)");
		assertFunc1DimEvalEquals(b, c);
	}
	
	@Test
	public void testDerive04() throws Exception {
		UserFunction a = parseUserFunction("a(z):=z + pderive(vars(x,y), x*y*sin(y), x, y, y)(z,z)");
		UserFunction b = parseUserFunction("b(z):=z + 2*cos(z)-z*sin(z)");
		assertFunc1DimEvalEquals(a, b);
	}
}
