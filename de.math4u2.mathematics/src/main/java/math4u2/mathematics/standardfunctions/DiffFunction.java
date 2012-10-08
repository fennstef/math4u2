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
 * Differenz diff(x,y) = x - y
 * </p>
 * 
 * @version 1.0
 */
public class DiffFunction extends ScalarInfixStandardFunction {

	public DiffFunction() {
		name = "diff";
		operatorName = "-";
		operatorNiceName = "\u2004\u2212\u2004";
	}

	public Object eval(Object[] args) throws MathException {
		return new ScalarDoubleResult(((ScalarDoubleResult) args[0]).value
				- ((ScalarDoubleResult) args[1]).value);
	} //eval

	/**
	 * Kostruiert zur Differenz f(x) - g(x) aus den beiden Ableitungstermen
	 * f'(x) und g'(x) den Ableitungsterm f'(x) - g'(x).
	 * 
	 * @param arguementTerms
	 *            Array aus f(x) und g(x)
	 * @param derivedArgumentTerms
	 *            Array aus f'(x) und g'(x)
	 * @param broker
	 * @return f'(x) - g'(x)
	 */
	public TermNode buildDeriveTerm(TermNode[] arguementTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		try {
			return new TermNodeFunct((Function) (broker.getObject("diff")),
					new TermNode[] { derivedArgumentTerms[0],
							derivedArgumentTerms[1] }, broker);
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
				&& ((TermNodeFunct) arguments[1]).getName().equals("diff")
				&& ((TermNodeFunct) arguments[1]).getArgumentTerm(0) instanceof TermNodeNum) {
			return new TermNodeFunct(
					(Function) (broker.getObject("sum")),
					new TermNode[] {
							new TermNodeNum(arguments[0].evalScalar()
									- ((TermNodeFunct) arguments[1])
											.getArgumentTerm(0).evalScalar()),
							((TermNodeFunct) arguments[1]).getArgumentTerm(1) },
					broker);
		}

		if (arguments[1] instanceof TermNodeNum
				&& arguments[0] instanceof TermNodeFunct
				&& ((TermNodeFunct) arguments[0]).getName().equals("diff") ) {
			
			if ( ((TermNodeFunct) arguments[0]).getArgumentTerm(0) instanceof TermNodeNum) {
				return new TermNodeFunct(this, new TermNode[] {
						new TermNodeNum(((TermNodeFunct) arguments[0])
								.getArgumentTerm(0).evalScalar()
								- arguments[1].evalScalar()),
						((TermNodeFunct) arguments[0]).getArgumentTerm(1) }, broker);
			}
			if ( ((TermNodeFunct) arguments[0]).getArgumentTerm(1) instanceof TermNodeNum) {
				return new TermNodeFunct(this, new TermNode[] {
						((TermNodeFunct) arguments[0]).getArgumentTerm(0), 						
						new TermNodeNum(((TermNodeFunct) arguments[0])
								.getArgumentTerm(1).evalScalar()
								+ arguments[1].evalScalar()),
						 }, broker);
			}					
		}

		return oldTerm;
	}

	public TermNode simplify(TermNode oldTerm, TermNode[] arguments, Broker broker)
			throws MathException {
		// Der Subtrahend (arguments[1])ist = 0, also
		// Minuenden (arguemnts[0]) zurueckgeben.
		if (arguments[1].hasResult0())
			return arguments[0];

		if (arguments[0] instanceof TermNodeNum
			&& arguments[1] instanceof TermNodeNum) {
			// beide Konten sind Zahlen, Differenz berechnen, entsprechenden
			// Knoten
			// zurueckgeben
			if (arguments[1] instanceof TermNodeNum)
				return new TermNodeNum(arguments[0].evalScalar()
						- arguments[1].evalScalar());
		}	
		
		if (arguments[1] instanceof TermNodeFunct
				&& ((TermNodeFunct)arguments[1]).getFunction() instanceof MinusFunction ) {
			//System.out.println("Minus");
		    TermNode term1 = arguments[0];
		    TermNode term2 = ((TermNodeFunct)arguments[1]).getArguments()[0];
		    Function f;
			try {
				f = (Function)broker.getObject("sum");
			} catch (BrokerException e) {				
				e.printStackTrace();
				throw new MathException(e);
			}
			return new TermNodeFunct(f, new TermNode[] {term1,term2}, broker);
		}
		return oldTerm;
	}

	/*
	 * public Function getDerive1Function(Broker broker) { return new
	 * UserFunction("",new TermNodeNum(1.0), new Variable[] {}, broker); }
	 * 
	 * public Function getDerive2Function(Broker broker) { return new
	 * UserFunction("", new TermNodeFunct( (Function)(broker.getObject("-")),
	 * new TermNode[] { new TermNodeNum(1.0) }, broker ), new Variable[] {},
	 * broker); }
	 */

	/*
	 * public Function derive(String varName) throws MathException { return
	 * null; }
	 */

}