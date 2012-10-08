package math4u2.view.graph.util;

public interface IScalarBooleanHolder extends ICanBeFixed{
	boolean getScalar() throws Exception;
	void setScalar(boolean value, boolean propagateChange) throws Exception;
	boolean getScalarOrNan();	
}
