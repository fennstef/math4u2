package math4u2.application.resource;

import java.awt.*;

import javax.swing.JFrame;


/**
 * Verwaltung aller Farben, die eingesetzt werden.
 * 
 * @author Fenn Stefan
 */
public class Colors {
	public static final Color BACKGROUND = new JFrame().getBackground();
	public static final Color SELECTED = new Color(BACKGROUND.getRed()+15,BACKGROUND.getGreen()+15,BACKGROUND.getBlue()+15);
	public static final Color TEXT_FIELD = new Color(240, 240, 240);
	public static final Color COMBO_BOX_SELECTED_LINE = new Color(245,165,16);
	public static final Color EDIT = new Color(250, 250, 204);
	public static final Color TEXTPANE_HISTORY_COLOR = new Color(0, 0, 0,(int) (255 * 0.2));
}//class Colors
