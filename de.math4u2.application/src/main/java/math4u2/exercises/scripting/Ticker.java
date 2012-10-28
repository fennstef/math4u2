package math4u2.exercises.scripting;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.drawarea.DrawAreasManager;

/**
 * Dieser Thread läuft die ganze Zeit durch. Wenn Actions eingehängt werden, so
 * werden sie mit den Methoden in EAction versorgt.
 * 
 * @see math4u2.exercises.scripting.EAction
 * @author Fenn Stefan
 */
public class Ticker extends Thread {

	private static Ticker instance;
	private TickerRunner tickerRunner;

	private EAction action;

	private boolean stop = false;

	public synchronized static Ticker getInstance() {
		if (instance == null) {
			instance = new Ticker();
//			instance.setPriority(9);
			instance.start();
		}//if
		return instance;
	}//getInstance

	private Ticker() {
		super("Ticker-Thread");
		tickerRunner = new TickerRunner();
	}//Konstruktor

	public void run() {
		while (!stop) {
			long time = System.currentTimeMillis();
			if (action != null) {
				try {
					SwingUtilities.invokeAndWait(tickerRunner);
				} catch (InterruptedException e1) {
					ExceptionManager.doError("Animationsfehler",e1);
				} catch (InvocationTargetException e1) {
					ExceptionManager.doError("Aufruffehler",e1);
				}
			}//if
			long delta = System.currentTimeMillis()-time;
			//System.out.println(delta +" "+action);
			try {
				if(BreakActionSingelton.getInstance().isBeforeBreak())continue;
				if(delta<40)
					Thread.sleep(40-delta);
				else 
					Thread.sleep(10);
			} catch (InterruptedException e) {
				ExceptionManager.doError("Aufruffehler",e);
			}
		}//while

		if (action != null)
			action.afterLastRun(System.currentTimeMillis());	
		
		action = null;
		stop = false;
		instance = null;
	}//run

	public synchronized void setAction(EAction action) {
		action.reinit();
		stop = false;
		this.action = action;
	}//setAction

	public void removeAction() {
		stop = true;
	}//removeAction

	public EAction getAction() {
		return action;
	}//getAction
	
	class TickerRunner implements Runnable{
		public void run() {
			List dai = DrawAreasManager.getAllDrawAreas();
			
			for (Iterator iter = dai.iterator(); iter.hasNext();) {
				DrawAreaInterface da = (DrawAreaInterface) iter
						.next();
				da.setMakeNoRenew(true);
			}//for iter
			
			if (!action.isRunning()) {
				action.beforeFirstRun(System
						.currentTimeMillis());
			}//if							
			
			action.doRun(System.currentTimeMillis());

			for (Iterator iter = dai.iterator(); iter.hasNext();) {
				DrawAreaInterface da = (DrawAreaInterface) iter
						.next();
				da.setMakeNoRenew(false);
				da.graphHasChanged();
			}//for iter			
		}//run		
	}//class TickerRunner

}//class Ticker
