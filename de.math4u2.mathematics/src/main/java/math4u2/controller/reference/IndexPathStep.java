package math4u2.controller.reference;

import java.util.Set;

import math4u2.mathematics.collection.Sequence;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.util.exception.ExceptionManager;

/**
 * Indexschritt wie bei Listen ll[2*i] oder Matrizen m[1,2]
 * 
 * @author Fenn Stefan
 */
public class IndexPathStep extends AbstractPathStep {
	
	public IndexPathStep(){
	}

	/**
	 * Methodenname weglassen
	 */
	protected String getBodyString() throws MathException {
		return "";
	} //getBodyString

	public void insertIndexFunctions(Set indexFunctionSet) {
		for (int i = 0; i < args.length; i++) {
			args[i].insertAllFunctions(indexFunctionSet);
		}//for i
	}//insertIndexFunctions
	
	protected Object getLocalDummyObject(CreatesPath obj) {
		try {
			if (obj instanceof Sequence) {
				if (result == null || result.length != args.length)
					result = new Object[args.length];

				for (int i = 0; i < args.length; i++) {
					result[i] = new ScalarDoubleResult(1.0);
				} // for i

				if (result2 == null || result2.length != 1)
					result2 = new Object[1];
				result2[0] = result;
				return stepMethod.invoke(obj, result2);
			}else{
				return super.getLocalDummyObject(obj);
			}
		} catch (Exception e) {
			ExceptionManager.doError(
					"Das Stellvertreter-Objekt wurde nicht gefunden (" + obj
							+ ").", e);
			throw new RuntimeException(e);
		} //catch
	}

	/**
	 * Eckige Klammern verwenden
	 */
	protected String bracketArguments(String argsStr){
	    return "[" + argsStr + "]";
	}//bracketArguments	
	
	public PathStep getEmptyClone() {
		return new IndexPathStep();
	}//getEmptyClone	

} //class SimplePathStep
