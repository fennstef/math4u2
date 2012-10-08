package math4u2.view.graph.util;

public interface IScalarDoubleHolder extends ICanBeFixed{
	double getScalar() throws Exception;
	void setScalar(double value, boolean propagateChange) throws Exception;
	double getScalarOrNan();
}
