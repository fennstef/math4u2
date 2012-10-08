package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.ScalarInfixStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;

/**
 * <p>
 * Quotient quot(x,y) = x/y
 * </p>
 */
public class QuotFunction extends ScalarInfixStandardFunction {

	public QuotFunction() {
		name = "quot";
		operatorName = "/";
	}

	public Object eval(Object[] args) throws MathException {
		return new ScalarDoubleResult(((ScalarDoubleResult) args[0]).value
				/ ((ScalarDoubleResult) args[1]).value);
	} //eval

	/**
	 * Konstruiert den Ableitungsterm zur Quotientenregel: (f(x)/g(x))' =
	 * (f'(x)*g(x) - f(x)*g(x))/(g(x)^2) aus den Termen und den
	 * Ableitungstermen.
	 * 
	 * @param arguementTerms
	 *            Array aus f(x) und g(x)
	 * @param derivedArgumentTerms
	 *            Array aus f'(x) und g'(x)
	 * @param broker
	 * @return (f'(x)*g(x) - f(x)*g(x))/(g(x)^2)
	 */
	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		try {
			// keine echte Quotientenregel
			if ( derivedArgumentTerms[1].hasResult0()) {
				return new TermNodeFunct((Function) (broker.getObject("quot")),
						new TermNode[] { derivedArgumentTerms[0], argumentTerms[1]}, broker);
			}
			// Quotientenregel	
			TermNode s1 = new TermNodeFunct((Function) (broker
					.getObject("prod")), new TermNode[] {
					derivedArgumentTerms[0], argumentTerms[1] }, broker);
			TermNode s2 = new TermNodeFunct((Function) (broker
					.getObject("prod")), new TermNode[] {
					derivedArgumentTerms[1], argumentTerms[0] }, broker);
			TermNode num = new TermNodeFunct((Function) (broker
					.getObject("diff")), new TermNode[] { s1, s2 }, broker);
			TermNode denom = new TermNodeFunct((Function) (broker
					.getObject("prod")), new TermNode[] { argumentTerms[1],
					argumentTerms[1] }, broker);
			return new TermNodeFunct((Function) (broker.getObject("quot")),
					new TermNode[] { num, denom }, broker);
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
	}
}//class QuotFunction
