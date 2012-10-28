package math4u2.view;

import java.awt.Container;

/**
 * Hier k�nnen sich alle Container die absolute Fontgr��e und �hnliche
 * Referenzen holen.
 */

public class ContentPane {

	private static Container contentPane;

	private static int fontSize;

	public static void setContentPane(Container cp) {
		contentPane = cp;
	}//setContentPane

	public static Container getContentPane() {
		return contentPane;
	}//getContentPane

	public static void setAbsoluteFontSize(int i) {
		fontSize = i;
	} //setAbsoluteFontSize

	public static int getAbsoluteFontSize() {
		return fontSize;
	}//getAbsoluteFontSize
} //ContentPane
