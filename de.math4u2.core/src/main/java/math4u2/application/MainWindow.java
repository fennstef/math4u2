package math4u2.application;

import javax.swing.JFrame;

import math4u2.application.resource.Versions;

public class MainWindow {
	private static JFrame main;
	private static String title;
	
	public static void initMainWindow(JFrame mainWin){
		main = mainWin;
	}
	
	public static JFrame get(){
		return main;
	}
	
    /**
	 * Setzen des Titels im Hauptfenster
	 */
	public static void setTitleText(String title) {
		MainWindow.title = title;
	    if (title != null && title.length() != 0)
	        title = " - " + title;
	    main.setTitle("math4u2 " + Versions.APPLICATION_VERSION + title);
	}//setTitleText
}
