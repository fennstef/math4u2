// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.scripting;

/**
 * abstrakte Klasse die zum Verwalten von zeitgesteuerten Plug-In-Aktionen
 * dient.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public abstract class EActionItem extends EBasicAction {
    protected Object obj;

    protected String objName;

    public EActionItem(String objName) {
        this.objName = objName;
        obj = ModuleRegistry.getInstance().lookup(objName);
    } //Konstruktor
    
	public void reinit() {		
		super.reinit();
		obj = ModuleRegistry.getInstance().lookup(objName);
	}//reinit
} //class EActionItem
