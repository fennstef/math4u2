package math4u2.view.gui.listview;

import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.graph.util.IScalarDoubleHolder;

public abstract class FunctionScalarDoubleHolder implements IScalarDoubleHolder{
	public abstract Function getFunction() throws Exception;
	
	public boolean isFixed() {
		try {
			if(!(getFunction() instanceof UserFunction)) return true;
			return !((UserFunction)getFunction()).isNotFixed();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setFixed(boolean fixed) {
		throw new RuntimeException();
	}

	public double getScalar() throws Exception {
		return getFunction().evalScalar();
	}

	public void setScalar(double value, boolean propagateChange)
			throws Exception {
		if(getFunction() instanceof UserFunction){
			UserFunction uf = (UserFunction) getFunction();
			uf.setValue(value);
			if(propagateChange){
				uf.getBroker().propagateChange(uf);
			}
		}else{
			throw new Exception();
		}
	}

	public double getScalarOrNan() {
		try {
			return getScalar();
		} catch (Exception e) {
			return Double.NaN;
		}
	}

}
