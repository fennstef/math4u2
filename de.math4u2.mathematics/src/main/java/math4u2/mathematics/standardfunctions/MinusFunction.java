package math4u2.mathematics.standardfunctions;

import math4u2.controller.Broker;
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
 * Unaere Minus-Funktion -x
 * </p>
 * 
 * @version 1.0
 */
public class MinusFunction extends ScalarUnaryStandardFunction {

	public MinusFunction() {
		name = "-";
	} //Konstruktor 1

	/**
	 * Gibt den errechneten Funktionswert zurueck
	 */
	public Object eval(Object[] args) throws MathException {
		return new ScalarDoubleResult(-(((ScalarDoubleResult) args[0]).value));
	} //eval

	/**
	 * Setzt vor argStrings[0] den Operator "-" .
	 */
	public String buildTermString(String[] argStrings, String name) {
		return "-" + argStrings[0];
	}

	/**
	 * Die Ableitung der Funktion -x ist -1.
	 * 
	 * @param broker
	 * @return Die Funktion -1
	 */
	public Function getDeriveFunction(Broker broker) {
		ScalarVariable x = new ScalarVariable("x");
		return new UserFunction("", new TermNodeNum(-1.0),
				new ScalarVariable[] { x }, broker, getViewFactory());
	}

	//public Function derive(String varName) throws MathException {
	/**
	 * Object[] derivedObjArray = getSubTerm(0).derive(varName); TermNode
	 * derivedTN = (TermNode) derivedObjArray[0]; Object[] retObjArray = new
	 * Object[2]; retObjArray[0] = new MinusFunction(derivedTN); retObjArray[1] =
	 * derivedObjArray[1]; return retObjArray;
	 */
	//return null;
	//}//derive
	
	public TermNode simplify(TermNode oldTerm, TermNode[] arguments,
			Broker broker) throws MathException {

		if (arguments[0] instanceof TermNodeFunct
				&& ((TermNodeFunct) arguments[0]).getFunction() instanceof MinusFunction) {
			return ((TermNodeFunct) arguments[0]).getArgumentTerm(0);
		}
		return oldTerm;
	}

} //MinusFunction
