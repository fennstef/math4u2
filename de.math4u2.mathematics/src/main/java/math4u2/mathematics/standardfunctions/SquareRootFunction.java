package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.ScalarUnaryStandardFunction;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.ScalarVariable;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.termnodes.TermNodeNum;

/**
 * <p>
 * Quadratwurzel
 * </p>
 * 
 * @version 1.0
 */
public class SquareRootFunction extends ScalarUnaryStandardFunction {

	public SquareRootFunction() {
		name = "sqrt";
		summaryText = "Quadratwurzel";
	} //Konstruktor 1

	/**
	 * Gibt den errechneten Funktionswert zurueck
	 */
	public Object eval(Object[] args) throws MathException {
//		if (((ScalarDoubleResult) args[0]).value >= 0)
//			return new ScalarDoubleResult(Math
//					.sqrt(((ScalarDoubleResult) args[0]).value));
//		else
//			throw new MathException("The value: "
//					+ ((ScalarDoubleResult) args[0]).value
//					+ " must be positiv for Square Root");
	    return new ScalarDoubleResult(Math
				.sqrt(((ScalarDoubleResult) args[0]).value));
	} //eval

	/**
	 * Die Ableitung von sqrt(x) ist 1/(2*sqrt(x)).
	 * 
	 * @param broker
	 * @return 1/(2*sqrt(x))
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		ScalarVariable x = new ScalarVariable("x");

		TermNode temp = new TermNodeFunct(
				(Function) (broker.getObject("prod")), new TermNode[] {
						new TermNodeNum(2.0),
						new TermNodeFunct(this, new ScalarVariable[] { x },
								broker) }, broker);

		return new UserFunction("", new TermNodeFunct((Function) (broker
				.getObject("quot")), new TermNode[] { new TermNodeNum(1.0),
				temp }, broker), new ScalarVariable[] { x }, broker, getViewFactory());
	}

} //SquareRootFunction
