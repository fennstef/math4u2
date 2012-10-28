// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.scripting;

import java.util.LinkedList;
import java.util.List;

/**
 * abstrakte Klasse die zum Verwalten von zeitgesteuerten Plug-In-Aktionen
 * dient.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public abstract class EBasicAction implements EAction {
    protected float begin, end;
    protected long startTime;
    
    /** Es werden hier alle Parameter gespeichert, die animiert wurden */
    protected static List animationParameters = new LinkedList();

    private boolean isRunning = false;

    public float getBegin() {
        return begin;
    }//getBegin

    public void setBegin(float begin) {
        this.begin = begin;
    }//setBegin

    public void setEnd(float end) {
        this.end = end;
    }//setEnd

    public float getEnd() {
        return end;
    }//getEnd

    public void setDuration(float dur) {
        end = begin + dur;
    }//setDuration

    public float getDuration() {
        return getEnd() - getBegin();
    }//getDuration

    public boolean isRunning() {
        return isRunning;
    }//isRunning

    public void beforeFirstRun(long timeMillis) {
        isRunning = true;
        startTime=timeMillis;
    }//beforeFirstRun

    public void afterLastRun(long timeMillis) {
        isRunning = false;
    }//afterLastRun

    public void doRun(long timeMillis) {
        if (!isRunning)
            throw new IllegalStateException();
    }//doRun
    
    public long getStartTime() {
        return startTime;
    }//getStartTime();
    
    
	public void reinit() {
		isRunning = false;
	}
	
	public abstract boolean hasBreakAction();
}//class EBasicAction
