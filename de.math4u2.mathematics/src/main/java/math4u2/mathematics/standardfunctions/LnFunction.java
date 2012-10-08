package math4u2.mathematics.standardfunctions;

import math4u2.controller.*;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.ScalarUnaryStandardFunction;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.ScalarVariable;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.termnodes.TermNodeNum;

/**
 * <p>
 * Natuerlicher Logarithmus
 * </p>
 * 
 * @version 1.0
 */
public class LnFunction extends ScalarUnaryStandardFunction {

	public LnFunction() {
		name = "ln";
		summaryText = "Berechnet den natürlichen Logarithmus,<br>also den Logarithmus zur Basis e";
	}//Konstruktor 1

	/**
	 * Gibt den errechneten Funktionswert zurueck
	 * 
	 * @return Ergebnis als ScalarDoubleResult
	 */
	public Object eval(Object[] args) throws MathException {
		return new ScalarDoubleResult(Math
				.log(((ScalarDoubleResult) args[0]).value));
	} //eval

	/**
	 * Die Ableitung von ln(x) ist die Funktion 1/x.
	 * 
	 * @param broker
	 * @return 1/x
	 */
	public Function getDeriveFunction(Broker broker) throws Exception {
		ScalarVariable x = new ScalarVariable("x");
		return new UserFunction("", new TermNodeFunct((Function) (broker
				.getObject("pow")),
				new TermNode[] { x, new TermNodeNum(-1.0) }, broker),
				new ScalarVariable[] { x }, broker, getViewFactory());
	}


	
	

}//LnFunction
