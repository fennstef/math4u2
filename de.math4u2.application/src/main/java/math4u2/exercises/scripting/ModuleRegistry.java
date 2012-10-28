// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.scripting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import math4u2.util.exception.ExceptionManager;

/**
 * Klasse zum Registrieren von Zusatzmodulen.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public class ModuleRegistry {
    private static ModuleRegistry instanz;

    private final Map modules = new HashMap();

    public void bind(String name, Object obj) {
        modules.put(name, obj);
    }

    public void bind(String name, String className) {
        ModuleFactory modFactory = new ModuleFactory();
        Object modObj = null;

        if (modules.containsKey(name)) {
            return;
        }
        try {
            modObj = modFactory.getModule(className);
        } catch (ClassNotFoundException ex) {
            ExceptionManager.doError("Die Klasse "+className+" wurde nicht gefunden.",ex);
        } catch (InstantiationException ex) {
            ExceptionManager.doError("Die Klasse "+className+" konnte nicht instatiiert werden.",ex);
        } catch (IllegalAccessException ex) {
            ExceptionManager.doError("Unerlaubter Zugriff auf die Klasse "+className+".",ex);
        }

        bind(name, modObj);
    }

    public void unbind(String name) {
        modules.remove(name);
    }

    public Object lookup(String name) {
        return modules.get(name);
    }//lookup

    public Iterator nameIterator() {
        return modules.keySet().iterator();
    }//nameIterator

    /** Gibt die aktuelle Instanz des Zusatzmodul-Verzeichnisses zurück. */
    public static ModuleRegistry getInstance() {
        if (instanz == null)
            instanz = new ModuleRegistry();
        return instanz;
    }

    public String toString() {
        return "ModuleRegistry: " + modules;
    }//toString
}