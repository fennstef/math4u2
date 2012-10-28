package math4u2.exercises.scripting;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import math4u2.view.gui.Math4U2Win;

/**
 * Es werden sequentiel die Actions ausgeführt.
 * 
 * @author Fenn Stefan
 */
public class EActionContainerSeq extends EActionContainer {

	private List startCalls;

	/**
	 * Aktueller Zeiger der Liste, welche Animation als nächstes abgespielt
	 * werden soll.
	 */
	private Iterator seqElem;

	/**
	 * Aktuelles Action-Element
	 */
	private EAction seqChild;

	public EActionContainerSeq(EActionContainer parent, List children) {
		super(parent, children);
	} //Konstruktor 1

	public EActionContainerSeq() {
		super();
	} //Konstruktor

	/**
	 * Holt das nächste Element, dass abgespielt werden soll, wenn vorhanden.
	 * Andernfalls wird null zurück gegeben.
	 *  
	 */
	public EAction getNextSeqChild(long timeMillis) {
		if (seqElem.hasNext()) {
			EAction action = (EAction) seqElem.next();
			return (EAction) action;
		} else {
			return null;
		}//else
	}//getNextSeqChild

	/**
	 * Es werden die ersten Calls sofort ausgeführt.
	 */
	public void startActions() {
		seqChild = null;
		seqElem = null;
		if (childItems.size() > 0) {			
			if (startCalls == null) {
				EActionContainer eac = (EActionContainer) childItems.get(0);
				startCalls = new LinkedList();
				Iterator iter = eac.childItems.iterator();
				while (iter.hasNext()) {
					EAction action = (EAction) iter.next();
					if (action instanceof EActionCall) {
						((EActionCall) action).doCall();
						startCalls.add(action);
					} else {
						break;
					}
				}//while
				eac.childItems.removeAll(startCalls);
			} else {
				for (Iterator iter = startCalls.iterator(); iter.hasNext();) {
					EActionCall call = (EActionCall) iter.next();
					call.doCall();
				}//for
			}//else
		}//if
		super.startActions();
	}//startActions

	/**
	 * Initialisierung vor Animationsstart
	 */
	public void beforeFirstRun(long timeMillis) {
		super.beforeFirstRun(timeMillis);
		seqElem = childItems.iterator();
		seqChild = getNextSeqChild(timeMillis);
	}//beforeFirstRun

	/**
	 * Wird immer wieder während der Animation aufgerufen
	 * 
	 * @param timeMillis
	 *            aktuelle Zeit
	 */
	public void doRun(long timeMillis) {
		super.doRun(timeMillis);

		// Alle calls durchführen
		makeSequenceCalls(timeMillis);

		//die Zeit könnte sich durch die Calls verschoben haben
		timeMillis = System.currentTimeMillis();
		long time = timeMillis - getStartTime();

		if (seqChild == null) {
			//keine weiteren Elemente werden animiert
			proveForAnimationEnd(time);
			return;
		}//if
		
		if (!seqChild.isRunning()) {
			//erster Lauf der Animation
			seqChild.beforeFirstRun(timeMillis);
			seqChild.doRun(timeMillis);
		}//if
		
		BreakActionSingelton bas = BreakActionSingelton.getInstance();
		while(bas.isBeforeBreak()){			
			seqChild.afterLastRun(timeMillis);
			seqChild = getNextSeqChild(timeMillis);
			if (seqChild == null) {
				Ticker.getInstance().removeAction();
				Math4U2Win.getInstance().getExercisePanel().setAnimationIsPlaying(
						false);
				animationParameters.clear();
				return;
			}//if
			seqChild.beforeFirstRun(timeMillis);
			seqChild.doRun(timeMillis);
		} 		
	
		if (timeMillis - seqChild.getStartTime() > seqChild.getEnd() * 1000) {
			//nach der Animation
			if (seqChild.isRunning()) {
				seqChild.afterLastRun(timeMillis);
				seqChild = getNextSeqChild(timeMillis);
				if (seqChild == null) {
					proveForAnimationEnd(time);
					return;
				}//if
				seqChild.beforeFirstRun(timeMillis);
				seqChild.doRun(timeMillis);				
			}//if
		}else if(timeMillis - seqChild.getStartTime() < seqChild.getBegin() * 1000) {
			//vor der Animation
		}else{
			// in der Animation
			seqChild.doRun(timeMillis);
		}//else

		//Ist die gesamte Animation fertig?
		proveForAnimationEnd(time);
	}//doRun

	/**
	 * Durchführung aller Aufrufe in GUI-Thread
	 */
	private void makeSequenceCalls(long timeMillis) {
		while (seqChild instanceof EActionCall) {
			((EActionCall) seqChild).doCall();
			seqChild = getNextSeqChild(timeMillis);
			if (seqChild == null)
				break;
		}//while
	}//_makeSequenceCalls

	/**
	 * Aktionen, die nach der Animation durchgeführt werden.
	 */
	public void afterLastRun(long timeMillis) {
		super.afterLastRun(timeMillis);
		if (seqChild != null && seqChild.isRunning()) {
			seqChild.afterLastRun(timeMillis);
		}//if
		seqChild = null;
		seqElem = null;
	}//afterLastRun
	
	public void reinit() {
		super.reinit();
		seqChild = null;
		seqElem = null;
		if(isRootContainer()){
			animationParameters.clear();
		}
	}//reinit
	
	/**
	 * Berechnet den Beginn der Container-Action, in Abhängigkeit der
	 * untergeordneten Container und Animationen.
	 */
	public float getBegin() {
		for (Iterator iter = childItems.iterator(); iter.hasNext();) {
			EAction action = (EAction) iter.next();
			if (action instanceof EActionCall)
				continue;
			return super.getBegin() + action.getBegin();
		}//for
		return 0;
	}//getBegin

	/**
	 * Berechnet das Ende der Container-Action, in Abhängigkeit der
	 * untergeordneten Container und Animationen.
	 */
	public float getEnd() {
		float sum = 0;
		for (Iterator iter = childItems.iterator(); iter.hasNext();) {
			EAction action = (EAction) iter.next();
			if (action instanceof EActionCall)
				continue;
			sum += action.getEnd();
		}
		if (seqChild != null && seqChild.isRunning()
				|| (seqElem != null && seqElem.hasNext())) {
			//solange die Animation läuft, darf der Container
			//nicht vorzeitig stoppen.
			return Float.MAX_VALUE;
		} else {
			return super.getBegin() + sum;
		}//else
	}//getEnd
} //EActionContainer
