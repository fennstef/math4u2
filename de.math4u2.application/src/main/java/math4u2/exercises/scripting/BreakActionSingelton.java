package math4u2.exercises.scripting;

public class BreakActionSingelton {
	private static BreakActionSingelton instance;
	private boolean beforeBreak = false;
	
	private BreakActionSingelton(){}
	
	public static BreakActionSingelton getInstance(){
		if(instance==null) instance = new BreakActionSingelton();
		return instance;		
	}

	public boolean isBeforeBreak() {
		return beforeBreak;
	}

	public void setBeforeBreak(boolean beforeBreak) {
		this.beforeBreak = beforeBreak;
	}
	
	

}
