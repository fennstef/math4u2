package math4u2.view.graph.util;

public interface IFunction2<R,P1,P2> {
	R eval(P1 p1, P2 p2) throws Exception;
	String getKey();
}
