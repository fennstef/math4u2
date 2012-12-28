package de.math4u2.mathematics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.parser.parser;
import math4u2.util.EmptyViewFactory;
import math4u2.util.SystemErrorExceptionFrame;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.ParseException;

import org.junit.Before;

public abstract class AbstractMathTest {
	protected Broker broker;
	protected static final double TOL = 1e-20;
	protected static final double FUNC_TOL = 1e-4;
	
	@Before
	public void setup(){
		ExceptionManager.initExceptionFrame(new SystemErrorExceptionFrame());
		broker = new Broker();
		EmptyViewFactory evf = new EmptyViewFactory();
		Function.setViewFactory(evf);
		parser.init(broker, evf);
	}

	protected UserFunction parseUserFunction(String def) throws ParseException, ObjectNotInRelationException, BrokerException {
		String key = parser.NEWParseDefinition(def, true, broker);
		MathObject mo = broker.getMathObject(key);
		assertNotNull(mo);
		assertTrue(mo instanceof UserFunction);
		UserFunction func = (UserFunction) mo;
		broker.publishObject(func, func.getKey());
		return func;
	}

	/**
	 * This assert test some evaluation points for equality.
	 */
	protected void assertFunc1DimEvalEquals(UserFunction a,UserFunction expected) throws MathException{
		double[] da = new double[]{-1e6, -100, -10, -5, -1, -0.5, -1e-5, 0, 1e-5, 0.5, 1, 5, 10, 100, 1e6};
		for(double d : da){
			ScalarDoubleResult sdr = new ScalarDoubleResult(d);
			ScalarDoubleResult resultA = (ScalarDoubleResult) a.eval(sdr);
			ScalarDoubleResult resultB = (ScalarDoubleResult) expected.eval(sdr);
			assertEquals(resultB.value, resultA.value, FUNC_TOL);
		}
	}
}
