package math4u2.view.gui.listview;

import math4u2.mathematics.functions.Function;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.view.graph.util.IFunction2;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.SimpleScalarDoubleValueHolder;

public abstract class FunctionFunctionSS2S implements IFunction2<IScalarDoubleHolder, IScalarDoubleHolder, IScalarDoubleHolder>{
	public abstract Function getFunction() throws Exception;
	
	public IScalarDoubleHolder eval(IScalarDoubleHolder p1)
			throws Exception {
		ScalarDoubleResult arg = new ScalarDoubleResult(p1.getScalar());
		ScalarDoubleResult sdr = (ScalarDoubleResult) getFunction().eval(arg);
		return new SimpleScalarDoubleValueHolder(sdr.getScalar());
	}
	
	public IScalarDoubleHolder eval(IScalarDoubleHolder p1,
			IScalarDoubleHolder p2) throws Exception {
		ScalarDoubleResult arg1 = new ScalarDoubleResult(p1.getScalar());
		ScalarDoubleResult arg2= new ScalarDoubleResult(p2.getScalar());
		ScalarDoubleResult[] args = new ScalarDoubleResult[]{arg1,arg2};
		ScalarDoubleResult sdr = (ScalarDoubleResult) getFunction().eval(args);
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
