package math4u2.util.swing.print;

import java.awt.*;
import javax.swing.border.*;

public class PreviewBorder implements Border {
	protected static final Insets borderInsets = new Insets(1, 1, 4, 4);

	public boolean isBorderOpaque() {
		return false;
	}

	public Insets getBorderInsets(Component component) {
		return borderInsets;
	}

	public void paintBorder(Component component, Graphics g, int x, int y,
			int w, int h) {
		g.setColor(Color.black);
		g.drawRect(0, 0, w - 4, h - 4);
		g.setColor(Color.black);
		g.fillRect(3, h - 3, w - 3, 3);
		g.fillRect(w - 3, 3, 3, h - 3);
		g.setColor(Color.GRAY);
		g.fillRect(w-3,0, w, 3);
		g.fillRect(0,h-3, 3, h);
	}
}