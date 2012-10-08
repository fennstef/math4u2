package math4u2.view.graph.util;

public interface IVectorDoubleValueHolder extends ICanBeFixed {
	double[] getVector() throws Exception;
	void setVector(double value[], boolean propagateChange) throws Exception;
	double[] getVectorOrNull();
}
