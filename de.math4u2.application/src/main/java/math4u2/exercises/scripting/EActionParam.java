// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.scripting;

import java.awt.Color;
import java.util.HashMap;

import math4u2.util.color.ColorUtil;

/**
 * Klasse die zum Verwalten der Parametern von Plug-In-Aktionen dient.
 * 
 * @version 0.1
 * @author Erich Seifert, Fenn Stefan
 */
public class EActionParam {
    private Object value;

    private Class type;

    private final static Class ColorClass = Color.class;

    private final static Class HashMapClass = HashMap.class;

    public EActionParam(java.util.HashMap params) {
        type = HashMapClass;
        value = params;
    }//Konstruktor

    public EActionParam(String paramValue, String paramType) {
        if (paramType.equalsIgnoreCase("boolean")
                || paramType.equalsIgnoreCase("bool")) {
            type = Boolean.TYPE;
        } else if (paramType.equalsIgnoreCase("string")) {
            type = String.class;
        } else if (paramType.equalsIgnoreCase("long")) {
            type = Long.TYPE;
        } else if (paramType.equalsIgnoreCase("int")
                || paramType.equalsIgnoreCase("integer")) {
            type = Integer.TYPE;
        } else if (paramType.equalsIgnoreCase("double")) {
            type = Double.TYPE;
        } else if (paramType.equalsIgnoreCase("float")) {
            type = Float.TYPE;
        } else if (paramType.equalsIgnoreCase("color")) {
            type = ColorClass;
        }

        if (paramValue != null)
            setValue(paramValue);
    } //Konstuktor

    public Class getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String paramValue) {
        if (type == Boolean.TYPE) {
            value = Boolean.valueOf(paramValue);
        } else if (type == String.class) {
            value = paramValue;
        } else if (type == Long.TYPE) {
            value = Long.valueOf(paramValue);
        } else if (type == Integer.TYPE) {
            value = Integer.valueOf(paramValue);
        } else if (type == Double.TYPE) {
            value = Double.valueOf(paramValue);
        } else if (type == Float.TYPE) {
            value = Float.valueOf(paramValue);
        } else if (type.equals(ColorClass)) {
            value = ColorUtil.parseColor(paramValue);
        }
    } //setValue

    public String toString() {
        return value.toString();
    } //toString

} //EActionParam
