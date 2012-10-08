package math4u2.util.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class LabelAsButtonMouseListener implements MouseListener {
	
	private JLabel label;
	private Cursor prevCursor;
	private static Cursor devCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	
	private static Border pressedBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
	private static Border hoveredBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
	private static Border unselectedBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
	
	public LabelAsButtonMouseListener(JLabel label){
		this.label = label;
		label.setBorder(unselectedBorder);
	}//Konstruktor
	
	public void mouseClicked(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		if(label.isEnabled())
			label.setBorder(pressedBorder);
		else
			label.setBorder(unselectedBorder);
	}//mousePressed

	public void mouseReleased(MouseEvent arg0) {
		if(label.isEnabled())
			label.setBorder(hoveredBorder);
		else
			label.setBorder(unselectedBorder);
	}//mouseReleased

	public void mouseEntered(MouseEvent arg0) {
		if(label.isEnabled()){
			label.setBorder(hoveredBorder);
			prevCursor = label.getCursor();
			label.setCursor(devCursor);
		}else{
			label.setBorder(unselectedBorder);
		}
	}//mouseEntered

	public void mouseExited(MouseEvent arg0) {
		if(label.isEnabled()){
			label.setBorder(unselectedBorder);
			label.setCursor(prevCursor);
		}else{
			label.setBorder(unselectedBorder);
		}
	}//mouseExited
}//class LabelAsButtonMouseListener