package math4u2.util;

import math4u2.util.exception.IExceptionFrame;

public class SystemErrorExceptionFrame implements IExceptionFrame {
	public void setVisible(boolean visible) {
	}
	
	public void newException(String title, String message) {
		System.err.println(title+": "+message);
	}
}
