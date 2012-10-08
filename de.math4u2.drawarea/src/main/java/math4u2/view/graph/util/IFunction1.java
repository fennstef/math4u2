package math4u2.view.graph.util;

public interface IFunction1<R,P1> {
	R eval(P1 p1) throws Exception;
	String getKey();
}
