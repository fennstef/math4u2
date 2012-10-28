package math4u2.exercises.scripting;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import math4u2.view.gui.Math4U2Win;

/**
 * Klasse die zum Speichern von Zusatzmodul-Aktionen dient.
 * 
 * @version 0.1
 * @author Erich Seifert, Fenn Stefan
 */
public abstract class EActionContainer extends EBasicAction {

	/** Actionen, die der Container als Kinder enthält */
	protected List childItems;

	/** Referenz zum Elter. Der RootContainer besitzt keinen Elter */
	private EActionContainer parent;

	protected EActionContainer(EActionContainer parent, List children) {
		this.parent = parent;
		childItems = children;
	} //Konstruktor 1

	protected EActionContainer() {
		childItems = new LinkedList();
		this.parent = null;
	} //Konstruktor

	/**
	 * Besitzt der Container oder ein Untercontainer eine Animation zum
	 * Abspielen?
	 */
	public boolean hasAnimation() {
		//        Iterator iterator = childItems.iterator();
		//        while (iterator.hasNext()) {
		//            Object o = iterator.next();
		//            if (o instanceof EActionAnimation)
		//                return true;
		//            if (o instanceof EActionContainer) {
		//                return ((EActionContainer) o).hasAnimation();
		//            } //if EActionContainer
		//        } //while
		//        return false;
		return true;
	} //hasAnimation

	public void add(EBasicAction a) {
		childItems.add(a);
	} //add

	public void addAll(List list) {
		childItems.addAll(list);
	}//addAll

	/**
	 * Ist dieses Objekt der Hauptcontainer?
	 */
	public boolean isRootContainer() {
		return parent == null;
	}//isRootContainer

	/**
	 * Es wird solange gewartet, bis alle Animationen fertig sind. Danach wird
	 * die Methode m aufgerufen.
	 */
	public static void stopAllActions() {
		Ticker.getInstance().removeAction();
	} //stopAllActions

	/**
	 * Eintrittpunkt um alle Actions ablaufen zu lassen.
	 */
	public void startActions() {
		Ticker.getInstance().setAction(this);
	}//start

	/**
	 * Wird aufgerufen, bevor die Animation startet
	 */
	public void beforeFirstRun(long timeMillis) {
		super.beforeFirstRun(timeMillis);
	}//beforeFirstRun

	/**
	 * Ablauf eines Animationsschrittes
	 */
	public void doRun(long timeMillis) {
		super.doRun(timeMillis);
	}//doRun

	/**
	 * Wenn dies die Root-Animation ist und die Zeit zuende ist, wird die Action
	 * aus dem Ticker-Thread genommen
	 */
	protected void proveForAnimationEnd(long time) {
		if ((time > getEnd() * 1000) && isRootContainer()) {
			Ticker.getInstance().removeAction();
			Math4U2Win.getInstance().getExercisePanel().setAnimationIsPlaying(
					false);
			animationParameters.clear();
		}//if
	}//proveEnd
	
	/**
	 * Wird nach der Animation aufgerufen
	 */
	public void afterLastRun(long timeMillis) {
		super.afterLastRun(timeMillis);
	}//afterLastRun
	
	/**
	 * Reinit an Kinder weiterleiten
	 */
	public void reinit() {
		super.reinit();
		for (Iterator iter = childItems.iterator(); iter.hasNext();) {
			EAction action = (EAction) iter.next();
			action.reinit();
		}//for iter
	}//reinit
	
	/**
	 * Berechnet den Beginn der Container-Action, in Abhängigkeit der
	 * untergeordneten Container und Animationen.
	 */
	public float getBegin() {
		return super.getBegin();
	}//getBegin

	/**
	 * Berechnet das Ende der Container-Action, in Abhängigkeit der
	 * untergeordneten Container und Animationen.
	 */
	public float getEnd() {
		return super.getEnd();
	}//getEnd
	
	/**
	 * Sucht nach einer Break-Anweisung und gibt die Position zurück
	 */
	public boolean hasBreakAction(){
		for(Object childObj : childItems){
			EBasicAction eba = (EBasicAction) childObj;
			if(eba.hasBreakAction()) return true;
		}
		return false;
	}
	
} //EActionContainer
