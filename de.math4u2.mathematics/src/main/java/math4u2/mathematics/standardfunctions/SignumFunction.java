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
 * Signum-Funktion
 * </p>
 * Liefert fuer Wert a von args[0]:
 * 1, falls a>0
 * 0, falls a==0
 * -1, falls a<0
 * 
 * @version 1.0
 */
public class SignumFunction extends ScalarUnaryStandardFunction {

	public SignumFunction() {
		name = "signum";
		summaryText = "Berechnet zum Argument a:" +
				"<br>1, falls a>0 ," +
				"<br>0, falls a=0 und" +
				"<br>-1, falls a<0 .";
	}//Konstruktor 1

	/**
	 * Liefert fuer Wert a von args[0]:
	 * 1, falls a>0
	 * 0, falls a==0
	 * -1, falls a<0
	 */
	public Object eval(Object[] args) throws MathException {
		double result = 0;
		if( ((ScalarDoubleResult) args[0]).value > 0) result = 1;
		if( ((ScalarDoubleResult) args[0]).value == 0) result = 0;
		if( ((ScalarDoubleResult) args[0]).value < 0) result = -1;
		return new ScalarDoubleResult(result);
	} //eval

	
	public Function getDeriveFunction(Broker broker) throws Exception {
		ScalarVariable x = new ScalarVariable("x");
		return new UserFunction("", 
				                new TermNodeFunct((Function) (
				                		broker.getObject("not_defined_at")), 
										new TermNode[] { x, new TermNodeNum(0.0), new TermNodeNum(2.0) } ,
										broker),
				                new ScalarVariable[] { x }, 
								broker, getViewFactory());
	}
	
	

}//SignumFunction
