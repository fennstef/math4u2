package math4u2.exercises.scripting;

import java.util.List;

public class EActionBreakCall extends EActionCall {

	public EActionBreakCall(String objName, String methodName, List parameters) {
		super(objName, methodName, parameters);
	}

	@Override
	public boolean hasBreakAction() {
		return true;
	}
	
	/**
	 * Wird der die BreakAction erreicht, so wird in normaler
	 * Geschwindigkeit weiter gearbeitet.
	 */
	@Override public void doRun(long timeMillis) {
		BreakActionSingelton.getInstance().setBeforeBreak(false);
		System.out.println("Break doRun");
	}
	
	@Override public void doCall() {
		doRun(0);
	}

	@Override public void afterLastRun(long timeMillis) {
//		System.out.println("Break afterLastRun");
	}
	@Override public void beforeFirstRun(long timeMillis) {
//		System.out.println("Break beforeFirstRun");
	}
	
	@Override protected Object rebuild() {return null;}
}
