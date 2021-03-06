package math4u2.mathematics.standardfunctions;

import math4u2.controller.*;
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
 * Arcus-Cosinus-Funktion
 * </p>
 * 
 * @version 1.0
 */
public class AcosFunction extends ScalarUnaryStandardFunction {

	public AcosFunction() {
		name = "arccos";
		summaryText = "Arcus-Cosinus";
	}//Konstruktor 1

	/**
	 * Gibt den errechneten Funktionswert zurueck
	 */
	public Object eval(Object[] args) throws MathException {
		return new ScalarDoubleResult(Math
				.acos(((ScalarDoubleResult) args[0]).value));
	} //eval

	/**
	 * Die Ableitung von acos(x) ist -1/sqrt(1-x^2).
	 * 
	 * @param broker
	 * @return -1/sqrt(1-x^2)
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		ScalarVariable x = new ScalarVariable("x");

		TermNode temp = new TermNodeFunct(
				(Function) (broker.getObject("prod")), new TermNode[] { x, x },
				broker);

		temp = new TermNodeFunct((Function) (broker.getObject("diff")),
				new TermNode[] { new TermNodeNum(1.0), temp, }, broker);

		temp = new TermNodeFunct((Function) (broker.getObject("sqrt")),
				new TermNode[] { temp }, broker);

		temp = new TermNodeFunct((Function) (broker.getObject("-")),
				new TermNode[] { temp }, broker);

		return new UserFunction("", new TermNodeFunct((Function) (broker
				.getObject("quot")), new TermNode[] { new TermNodeNum(1.0),
				temp }, broker), new ScalarVariable[] { x }, broker, getViewFactory());
	}

}//AcosFunction
