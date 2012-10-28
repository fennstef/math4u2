package math4u2.view.gui.component;

import java.awt.Graphics;
import java.awt.Insets;

import math4u2.application.resource.Images;

public class ListCheckBox extends TristateCheckBox {
	public ListCheckBox(String text, State initial) {
		super(text, initial);
	}

	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		Insets insets = getInsets();
		insets.left=0; insets.top=0;
		if (getState() == SELECTED && isEnabled())
			gr.drawImage(Images.EMPTY_LIST.getImage(), insets.left,
					insets.top, null);
		else if (getState() == SELECTED && !isEnabled())
			gr.drawImage(Images.EMPTY_LIST_DISABLED.getImage(), insets.left,
					insets.top, null);
		else if (getState() == DONT_CARE && isEnabled())
			gr.drawImage(Images.UP_ARROW.getImage(), insets.left,
					insets.top, null);
		else if (getState() == DONT_CARE && !isEnabled())
			gr.drawImage(Images.UP_DISABLED.getImage(),
					insets.left, insets.top, null);
		else if (getState() == NOT_SELECTED && isEnabled())
			gr.drawImage(Images.DOWN_ARROW.getImage(), insets.left,
					insets.top, null);
		else if (getState() == NOT_SELECTED && !isEnabled())
			gr.drawImage(Images.DOWN_DISABLED.getImage(), insets.left,
					insets.top, null);
	}//paintComponent
}//class ListCheckBox