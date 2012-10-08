package math4u2.view.graph.util;

public interface IScalarStringHolder extends ICanBeFixed{
	String getScalar() throws Exception;
	void setScalar(String value, boolean propagateChange) throws Exception;
	String getScalarOrNull();	
}
