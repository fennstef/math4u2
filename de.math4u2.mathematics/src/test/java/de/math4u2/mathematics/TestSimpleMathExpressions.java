package de.math4u2.mathematics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.parser.parser;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.IExceptionFrame;
import math4u2.util.exception.parser.ParseException;
import math4u2.view.graph.GraphInterfaceFactory;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

import org.junit.Before;
import org.junit.Test;

public class TestSimpleMathExpressions {
	private Broker broker;
	
	@Before
	public void setup(){
		ExceptionManager.initExceptionFrame(new IExceptionFrame() {
			public void setVisible(boolean visible) {
			}
			
			public void newException(String title, String message) {
				System.err.println(title+": "+message);
			}
		});
		broker = new Broker();
		parser.init(broker, new EmptyViewFactory());
		
	}

	@Test
	public void testSmokeTest() throws ParseException, MathException {
		UserFunction a = parseUserFunction("a:=1/2");
		assertEquals(0.5,a.evalScalar(), 1e-20);
	}
	
	@Test
	public void testParameterUpdate() throws ParseException, MathException {
		UserFunction a = parseUserFunction("a:=1/2");
		UserFunction b = parseUserFunction("b:=a*2");
		assertEquals(1,b.evalScalar(), 1e-20);
		parseUserFunction("a:=1");
		assertEquals(2,b.evalScalar(), 1e-20);
	}

	private UserFunction parseUserFunction(String def) throws ParseException {
		String key = parser.NEWParseDefinition(def, true, broker);
		MathObject mo = broker.getMathObject(key);
		assertNotNull(mo);
		assertTrue(mo instanceof UserFunction);
		UserFunction func = (UserFunction) mo;
		return func;
	}

}
