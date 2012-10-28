package math4u2.exercises.scripting;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasse die zum Speichern von Zusatzmodul-Aktionen dient.
 * 
 * @version 0.1
 * @author Erich Seifert, Fenn Stefan
 */
public class EActionContainerPar extends EActionContainer {

    public EActionContainerPar(EActionContainer parent, List children) {
        super(parent, children);
    } //Konstruktor 1

    public EActionContainerPar() {
        super();
    } //Konstruktor

    /**
     * Wird aufgerufen, bevor die Animation startet
     */
    public void beforeFirstRun(long timeMillis) {
        super.beforeFirstRun(timeMillis);
    }//beforeFirstRun

    /**
     * Wird während der Animation aufgerufen.
     */
    public void doRun(long timeMillis) {
        super.doRun(timeMillis);
        long time = timeMillis - getStartTime();
        List trash = new LinkedList();

        //parallel
        for (Iterator iter = childItems.iterator(); iter.hasNext();) {
            EAction child = (EAction) iter.next();

            //erster Aufruf
            if (!child.isRunning()) {
                child.beforeFirstRun(timeMillis);
                child.doRun(timeMillis);
            }//if

            if (timeMillis - child.getStartTime() < child.getBegin() * 1000) {
                //vor der Animation
            } else if (timeMillis - child.getStartTime() > child.getEnd() * 1000) {
                //nach der Animation
                if (child.isRunning()) {
                    child.afterLastRun(timeMillis);
                    trash.add(child);
                }
            } else {
                //in der Animation
                child.doRun(timeMillis);
            }//else
        }//for iter

        childItems.removeAll(trash);
        proveForAnimationEnd(time);
    }//doRun

    /**
     * Wird nach der Animation aufgerufen.
     */
    public void afterLastRun(long timeMillis) {
        super.afterLastRun(timeMillis);
        for (Iterator iter = childItems.iterator(); iter.hasNext();) {
            EAction action = (EAction) iter.next();
            if (action.isRunning())
                action.afterLastRun(timeMillis);
        }//for
    }//afterLastRun

    /**
     * Berechnet den Beginn der Container-Action, in Abhängigkeit der
     * untergeordneten Container und Animationen.
     */
    public float getBegin() {
        float min = Float.MAX_VALUE;
        for (Iterator iter = childItems.iterator(); iter.hasNext();) {
            EAction action = (EAction) iter.next();
            if (action instanceof EActionCall)
                continue;
            min = Math.min(min, action.getBegin());
        }//for
        return super.getBegin() + min;
    }//getBegin

    /**
     * Berechnet das Ende der Container-Action, in Abhängigkeit der
     * untergeordneten Container und Animationen.
     */
    public float getEnd() {
        float max = 0;
        for (Iterator iter = childItems.iterator(); iter.hasNext();) {
            EAction action = (EAction) iter.next();
            if (action instanceof EActionCall)
                continue;
            max = Math.max(max, action.getEnd());
        }//for
        return super.getBegin() + max;
    }//getEnd
} //EActionContainer
