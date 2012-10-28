// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.scripting;

/**
 * Schnittstelle für automatisierbare Elemente.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public interface Scriptable {
    void setActions(EActionContainer actions);

    EActionContainer getActions();

    void animate();
}