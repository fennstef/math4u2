package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.ScalarInfixStandardFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.termnodes.TermNodeNum;

/**
 * <p>
 * Allgemeine Exponentialfunktion pow(x,y) = x^y
 * </p>
 * 
 * @version 1.0
 */
public class PowFunction extends ScalarInfixStandardFunction {

	public PowFunction() {
		name = "pow";
		operatorName = "^";
	}

	public Object eval(Object[] args) throws MathException {
		if (((ScalarDoubleResult) args[0]).value == 0.0) {
			if (((ScalarDoubleResult) args[1]).value > 0.0) {
				return new ScalarDoubleResult(0.0);
			}
			if (((ScalarDoubleResult) args[1]).value == 0.0) {
				return new ScalarDoubleResult(1.0);
			}
		}

		return new ScalarDoubleResult(Math.pow(
				((ScalarDoubleResult) args[0]).value,
				((ScalarDoubleResult) args[1]).value));
	} // eval

	/**
	 * Konstruiert den Ableitungsterm zu f(x)^g(x): (f(x)^g(x))' =
	 * g(x)*f(x)^(g(x)-1)*f'(x) + f(x)^g(x)*ln(f(x))*g'(x) aus den Termen und
	 * den Ableitungstermen.
	 * 
	 * @param arguementTerms
	 *            Array aus f(x) und g(x)
	 * @param derivedArgumentTerms
	 *            Array aus f'(x) und g'(x)
	 * @param broker
	 * @return g(x)*f(x)^(g(x)-1)*f'(x) + f(x)^g(x)*ln(f(x))*g'(x)
	 */
	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		Function diff;
		Function expo;
		Function prod;
		Function ln;
		Function sum;
		try {
			sum = (Function) (broker.getObject("sum"));
			prod = (Function) (broker.getObject("prod"));
			expo = this;
			diff = (Function) (broker.getObject("diff"));
			ln = (Function) (broker.getObject("ln"));
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
		TermNode x = argumentTerms[0];
		TermNode y = argumentTerms[1];
		TermNode dx = derivedArgumentTerms[0];
		TermNode dy = derivedArgumentTerms[1];

		TermNode temp = new TermNodeFunct(diff, new TermNode[] { y,
				new TermNodeNum(1.0) }, broker);
		temp = new TermNodeFunct(expo, new TermNode[] { x, temp }, broker);
		temp = new TermNodeFunct(prod, new TermNode[] { y, temp }, broker);

		temp = new TermNodeFunct(prod, new TermNode[] { temp, dx }, broker);
		if ((dy instanceof TermNodeNum) && (dy.evalScalar() == 0))
			return temp;

		TermNode temp1 = new TermNodeFunct(expo, new TermNode[] { x, y },
				broker);
		TermNode temp2 = new TermNodeFunct(ln, new TermNode[] { x }, broker);
		temp2 = new TermNodeFunct(prod, new TermNode[] { temp1, temp2 }, broker);
		temp2 = new TermNodeFunct(prod, new TermNode[] { temp2, dy }, broker);
		return new TermNodeFunct(sum, new TermNode[] { temp, temp2 }, broker);
	} // buildDeriveTerm

	/**
	 * Konstruiert aus den beiden Argumenten in argStrings[0] und argStrings[1]
	 * die Infix-Darstellung des Gesamtterms.
	 * 
	 * @param argStrings
	 *            Argumente
	 * @return Infix-Darstellung des Terms
	 */
	/*
	 * wird von InfixStandardFunction implementiert public String
	 * buildTermString(String[] argStrings) { return argStrings[0] +
	 * operatorName + argStrings[1] ; }
	 */

	public TermNode evalNum(TermNode oldTerm, TermNode[] arguments)
			throws MathException {
		if (arguments[1] instanceof TermNodeNum
				&& arguments[1].evalScalar() == 0.0) {
			return new TermNodeNum(1.0);
		}

		if (arguments[1] instanceof TermNodeNum
				&& arguments[1].evalScalar() == 1.0) {
			return arguments[0];
		}

		return oldTerm;
	}

	public TermNode simplify(TermNode oldTerm, TermNode[] arguments,
			Broker broker) throws MathException {

		if (arguments[1].hasResult1())
			return arguments[0];

		if (arguments[1].hasResult0())
			return new TermNodeNum(1.0);

		return oldTerm;
	}

} // pow
