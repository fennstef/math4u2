package math4u2.util.reflect;

import java.util.*;
import java.lang.reflect.*;

/**
 * @author Fenn Stefan
 * 
 * Hilfsmethoden für die Reflection-API
 */
public class ReflectUtil {

	/**
	 * Erzeugt eine String-Representation in der Art: <br>
	 * <code>objName.methodName(param1, param2, ...)
	 * @param objName Objektname
	 * @param methodName Methodenname
	 * @param params Parameter
	 */
	public static String buildCallRepresentation(String objName,
			String methodName, List params) {
		String callStr = objName + "." + methodName + "(";
		Iterator iterator = params.iterator();
		while (iterator.hasNext()) {
			callStr += iterator.next();
			if (iterator.hasNext())
				callStr += ", ";
		} //while
		callStr += ")";
		return callStr;
	}//buildCallRepresentation

	/**
	 * Überprüft, ob diese Klasse eine bestimmte Methode enthält.
	 * 
	 * @param modClass
	 *            Klasse in der gesucht wird
	 * @param methodName
	 *            Methodenname, der gefunden werden soll
	 */
	public static boolean methodNameExists(Class modClass, String methodName) {
		//schauen, ob diese Methode überhaupt existiert
		Method[] ma = modClass.getMethods();
		boolean methodNameExists = false;
		for (int i = 0; i < ma.length; i++) {
			if (ma[i].getName().equals(methodName)) {
				methodNameExists = true;
				break;
			}//if
		}//for i
		return methodNameExists;
	}//methodNameExist

}//class ReflectUtil
