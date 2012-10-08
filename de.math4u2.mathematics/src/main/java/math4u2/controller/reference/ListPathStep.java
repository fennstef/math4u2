package math4u2.controller.reference;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import math4u2.controller.MathObject;
import math4u2.mathematics.collection.MathList;
import math4u2.mathematics.functions.MathException;

/**
 * Methode, die ein Listenelement anspricht Z.B. liste[3].x : Methode "[]" mit
 * Parameter "3"
 * 
 * @author Fenn Stefan
 */
public class ListPathStep extends AbstractPathStep {

	protected String getBodyString() throws MathException {
		return "[" + args[0].getTermString() + "]";
	} //getBodyString

	/**
	 * Das Listen-Objekt stellt ein Dummy-Objekt zur Verfügung. Dieses wird
	 * zurück gegeben.
	 */
	protected Object getLocalDummyObject(CreatesPath obj) {
		return ((MathList) obj).getDummyObject();
	} //getDummyObject

	protected String getArgumentsAsString() {
		return "";
	} //getArgumentsAsString

	public PathStep getEmptyClone() {
		return new ListPathStep();
	}//getEmptyClone

	public void insertIndexFunctions(Set indexFunctionSet) {
		for (int i = 0; i < args.length; i++) {
			args[i].insertAllFunctions(indexFunctionSet);
		}
	}
	
	public Object getShallowObject(Object root)
		throws 	IllegalArgumentException, IllegalAccessException,
				InvocationTargetException, MathException {

		if (hasVarTerms) {
			cachedChild = getObjectFromMethod(root);
			if(nextStep==null)
				return cachedChild;
			else return nextStep.getObject(cachedChild);
		}
        return nextStep.getShallowObject(cachedChild);
	} //getShallowObject
	
	
    public void swapLinks(MathObject oldObj, MathObject newObj) {
        // TODO Auto-generated method stub
        super.swapLinks(oldObj, newObj);
    }
} //class ListPathStep
