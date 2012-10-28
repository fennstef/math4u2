// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.scripting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.ParseException;
import math4u2.util.reflect.ReflectUtil;

/**
 * Klasse die zum Verwalten von Plug-In-Aktionen dient.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public class EActionCall extends EActionItem {
    private String methodName;

    private Method method;

    private final List params;

    private Class[] paramClasses;

    private Object[] paramValues;

    public EActionCall(String objName, String methodName, List parameters) {
        super(objName);
        this.objName = objName;
        this.methodName = methodName;

        params = parameters;
        createParamLists();

        rebuild();
    }//Konstruktor

    /** baut neues Objekt und neue Methode auf */
    protected Object rebuild() {
        Object obj = ModuleRegistry.getInstance().lookup(objName);

        if (obj == null)
            return obj;
        Class modClass = obj.getClass();

        try {
            method = modClass.getMethod(methodName, paramClasses);
        } catch (NoSuchMethodException ex) {
            buildParseException(modClass, ex);
        } //catch

        return obj;
    }//rebuild

    /**
     * Es wurde eine NoSuchMethodException geworfen. Nun muﬂ eine geeignete
     * Ausgabe erfolgen.
     * 
     * @param modClass
     *            Klasse der Methode
     * @param ex
     *            Die geworfene Exception
     */
    private void buildParseException(Class modClass, NoSuchMethodException ex) {
        String message;
        if (ReflectUtil.methodNameExists(modClass, methodName))
            message = "Parameterliste nicht gefunden: ";
        else
            message = "Methode nicht gefunden: ";
        message += ReflectUtil.buildCallRepresentation(objName, methodName,
                params);
        ExceptionManager.doError("Aufruffehler bei "+toString()+" gefunden",new ParseException(message));
    } //buildParseException

//    static long sum=0;
    
    /**
     * Ruft die Action-Methode auf.
     */
    public void doCall() {
        try {
            invoke();
        } catch (IllegalAccessException ex) {
            ExceptionManager.doError("Aufruffehler bei "+toString(),ex);
        } catch (InvocationTargetException ex) {
            ExceptionManager.doError("Aufruffehler bei "+toString(),ex);
        }//catch
    }//doCall

    public String getObjectName() {
        return objName;
    }//getObjectName

    public String getMethodName() {
        return methodName;
    }//getMethodName

    protected String getCallName() {
        return objName + "." + methodName + " call";
    }//getCallName

    private void invoke() throws IllegalAccessException,
            InvocationTargetException {
        obj = rebuild();

        if (obj != null) {
            method.invoke(obj, paramValues);
        } else {
            ExceptionManager.doError("Objekt nicht gefunden: " + objName
                    + "\nAufruf:" + objName + "." + toString() + "\n"
                    + ModuleRegistry.getInstance().toString());
        }//else
    }//invoke

    private void createParamLists() {
        if (params != null && !params.isEmpty()) {
            paramClasses = new Class[params.size()];
            paramValues = new Object[params.size()];
        } //if

        Iterator it = params.iterator();
        int i = 0;
        while (it.hasNext()) {
            EActionParam param = (EActionParam) it.next();
            paramClasses[i] = param.getType();
            paramValues[i] = param.getValue();
            i++;
        } //while
    } //createParmLists

    public String toString() {
        String s = null;
        if (paramValues != null)
            s = java.util.Arrays.asList(paramValues).toString();
        return methodName + " " + s;
    } //toString

    public void beforeFirstRun(long timeMillis) {
        doCall();
    }//beforeFirstRun

    public void doRun(long timeMillis) {
    }//doRun

    public void afterLastRun(long timeMillis) {
    }//afterLastRun

	@Override public boolean hasBreakAction() {
		return false;
	}

} //class EActionCall
