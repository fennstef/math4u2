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
 * Produkt prod(x,y) = x*y
 * </p>
 * 
 * @version 1.0
 */
public class ProdFunction extends ScalarInfixStandardFunction {

	public ProdFunction() {
		name = "prod";
		operatorName = "*";
		operatorNiceName = "\u200A\u00B7\u200A";

	}

	public Object eval(Object[] args) throws MathException {
			return new ScalarDoubleResult(((ScalarDoubleResult) args[0]).value
					* ((ScalarDoubleResult) args[1]).value);	
	} //eval

	/**
	 * Konstruiert den Ableitungsterm zur Produktregel: (f(x)*g(x))' =
	 * f'(x)*g(x) + f(x)*g(x) aus den Termen und den Ableitungstermen.
	 * 
	 * @param arguementTerms
	 *            Array aus f(x) und g(x)
	 * @param derivedArgumentTerms
	 *            Array aus f'(x) und g'(x)
	 * @param broker
	 * @return f'(x)*g(x) + f(x)*g(x)
	 */

	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		try {
			TermNode s1 = new TermNodeFunct((Function) (broker
					.getObject("prod")), new TermNode[] {
					derivedArgumentTerms[0], argumentTerms[1] }, broker);
			TermNode s2 = new TermNodeFunct((Function) (broker
					.getObject("prod")), new TermNode[] {
					derivedArgumentTerms[1], argumentTerms[0] }, broker);

			return new TermNodeFunct((Function) (broker.getObject("sum")),
					new TermNode[] { s1, s2 }, broker);
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
	}

	public TermNode evalNum(TermNode oldTerm, TermNode[] arguments, Broker broker)
			throws Exception {

		arguments[0] = arguments[0].evalNum();
		arguments[1] = arguments[1].evalNum();

		if (arguments[0] instanceof TermNodeNum
				&& arguments[1] instanceof TermNodeNum) {
			return new TermNodeNum(oldTerm.evalScalar());
		}

		if (arguments[0] instanceof TermNodeNum
				&& arguments[1] instanceof TermNodeFunct
				&& ((TermNodeFunct) arguments[1]).getName().equals("prod")
				&& ((TermNodeFunct) arguments[1]).getArgumentTerm(0) instanceof TermNodeNum) {
			return new TermNodeFunct(this, new TermNode[] {
					new TermNodeNum(arguments[0].evalScalar()
							* ((TermNodeFunct) arguments[1]).getArgumentTerm(0)
									.evalScalar()),
					((TermNodeFunct) arguments[1]).getArgumentTerm(1) }, broker);
		}

		if (arguments[1] instanceof TermNodeNum
				&& arguments[0] instanceof TermNodeFunct
				&& ((TermNodeFunct) arguments[0]).getName().equals("prod")
				&& ((TermNodeFunct) arguments[0]).getArgumentTerm(0) instanceof TermNodeNum) {
			return new TermNodeFunct(this, new TermNode[] {
					new TermNodeNum(arguments[1].evalScalar()
							* ((TermNodeFunct) arguments[0]).getArgumentTerm(0)
									.evalScalar()),
					((TermNodeFunct) arguments[0]).getArgumentTerm(1) }, broker);
		}

		if (arguments[0] instanceof TermNodeNum
				&& arguments[1] instanceof TermNodeFunct
				&& ((TermNodeFunct) arguments[1]).getName().equals("prod")
				&& ((TermNodeFunct) arguments[1]).getArgumentTerm(1) instanceof TermNodeNum) {
			return new TermNodeFunct(
					this,
					new TermNode[] {
							new TermNodeNum(
									((ScalarDoubleResult) arguments[0].eval()).value
											* ((ScalarDoubleResult) ((TermNodeFunct) arguments[1])
													.getArgumentTerm(1).eval()).value),
							((TermNodeFunct) arguments[1]).getArgumentTerm(0) },
					broker);
		}

		if (arguments[1] instanceof TermNodeNum) {
			TermNode temp = arguments[0];
			arguments[0] = arguments[1];
			arguments[1] = temp;

			return new TermNodeFunct(this, new TermNode[] { arguments[1],
					arguments[0] }, broker);
		}

		return oldTerm;
	}

	public TermNode simplify(TermNode oldTerm, TermNode[] arguments, Broker broker)
			throws MathException {
		Function f;

			if (arguments[0].hasResult1())
				return arguments[1];

			if (arguments[0].hasResult0())
				return new TermNodeNum(0.0);

			if (arguments[0].hasResultMinus1()
					&& (arguments[1] instanceof TermNodeFunct)
					&& (((TermNodeFunct) (arguments[1])).getFunction() instanceof MinusFunction)) {
				return ((TermNodeFunct) (arguments[1])).getArgumentTerm(0);
			}
			
			if ( arguments[0].hasResultMinus1() ){
				try {
					f = (Function)broker.getObject("-");
					return new TermNodeFunct(f, new TermNode[] {arguments[1]}, broker);
				} catch (BrokerException e) {				
					e.printStackTrace();
					throw new MathException(e);		
				}	
			}
			
			if (arguments[1].hasResult1())
				return arguments[0];

			if (arguments[1].hasResult0())
				return new TermNodeNum(0.0);

			if (arguments[1].hasResultMinus1()
					&& (arguments[0] instanceof TermNodeFunct)
					&& (((TermNodeFunct) (arguments[0])).getFunction() instanceof MinusFunction)) {
				return ((TermNodeFunct) (arguments[0])).getArgumentTerm(0);
			}
			
				
		if (arguments[0] instanceof TermNodeFunct
				&& ((TermNodeFunct)arguments[0]).getFunction() instanceof MinusFunction 
				&& ((TermNodeFunct)arguments[0]).getArguments()[0].hasResult1()) {
			//System.out.println("Minus einbauen");
			try {
				f = (Function)broker.getObject("-");
				return new TermNodeFunct(f, new TermNode[] {arguments[1]}, broker);
			} catch (BrokerException e) {				
				e.printStackTrace();
				throw new MathException(e);		
			}
			
		}

		return oldTerm;
	}

	/*
	 * 
	 * public Function derive(String varName) throws MathException { return
	 * null; }
	 */

}