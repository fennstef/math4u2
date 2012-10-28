package math4u2.view.gui.listview;

import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.view.graph.util.IFunction1;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.SimpleScalarDoubleValueHolder;

public abstract class FunctionFunctionS2S implements IFunction1<IScalarDoubleHolder, IScalarDoubleHolder>{
	public abstract Function getFunction() throws Exception;
	
	public IScalarDoubleHolder eval(IScalarDoubleHolder p1)
			throws Exception {
		ScalarDoubleResult arg = new ScalarDoubleResult(p1.getScalar());
		ScalarDoubleResult sdr = (ScalarDoubleResult) getFunction().eval(arg);
		return new SimpleScalarDoubleValueHolder(sdr.getScalar());
	}

	public String getKey() {
		try {
			return getFunction().getKey()+"";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
