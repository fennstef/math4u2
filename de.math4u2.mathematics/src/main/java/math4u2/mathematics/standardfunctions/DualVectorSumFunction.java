package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.DualVectorDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;

public class DualVectorSumFunction extends MatrixSumFunction {

	public DualVectorSumFunction() {
		name = "DualVectorSum";
		operatorName = "+";
		operatorNiceName = " + ";
	}

	public Object eval(Object[] args) throws MathException {		
		return new DualVectorDoubleResult(evalArray((DualVectorDoubleResult)args[0], (DualVectorDoubleResult)args[1]));
	} //eval

	public TermNode buildDeriveTerm(TermNode[] arguementTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {
		try {
			return new TermNodeFunct((Function) (broker
					.getObject("DualVectorSum")), new TermNode[] {
					derivedArgumentTerms[0], derivedArgumentTerms[1] }, broker);
		} catch (BrokerException e) {
			throw new MathException(e.getMessage());
		}
	} // buildDeriveTerm

} //DualVectorSumFunction
