package math4u2.util.swing.print;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PreviewSelector extends JMenuItem implements MouseListener,
		MouseMotionListener {
	protected Dimension size;

	protected int width, height;

	protected int ww = 12;

	protected int hh = 16;

	protected int selectedX = 0;

	protected int selectedY = 0;

	public PreviewSelector(int width, int height) {
		this.width = width;
		this.height = height;
		size = new Dimension((width * ww) + 3, (height * hh) + 3);
		addMouseListener(this);
		addMouseMotionListener(this);
		setBorder(BorderFactory.createLineBorder(getBackground()));
	}

	public Dimension getPreferredSize() {
		return size;
	}

	public Dimension getGridSize() {
		return new Dimension(selectedX + 1, selectedY + 1);
	}

	public void paintComponent(Graphics g) {
		int w = getSize().width - 1;
		int h = getSize().height - 1;
		g.setColor(getBackground());
		g.fillRect(0, 0, w + 1, h + 1);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				drawPage(g, x <= selectedX & y <= selectedY, x * ww + 2, y * hh
						+ 2, ww - 2, hh - 2);
			}
		}
	}

	protected void drawPage(Graphics g, boolean selected, int x, int y, int w,
			int h) {
		g.setColor(selected ? Color.blue : Color.white);
		g.fillRect(x, y, w, h);
		g.setColor(selected ? Color.white : Color.black);
		for (int i = 0; i < h; i += 2) {
			g.drawLine(x + 2, y + i, x + w - 2, y + i);
		}
		g.setColor(Color.black);
		g.drawRect(x, y, w, h);
	}

	public void mousePressed(MouseEvent event) {
		Point point = event.getPoint();
		selectedX = point.x / ww;
		selectedY = point.y / hh;
		repaint();
	}

	public void mouseReleased(MouseEvent event) {
		MenuSelectionManager.defaultManager().clearSelectedPath();
		fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
				getActionCommand()));
	}

	public void mouseDragged(MouseEvent event) {
		Point point = event.getPoint();
		selectedX = point.x / ww;
		selectedY = point.y / hh;
		repaint();
	}

	public void mouseMoved(MouseEvent event) {
		mouseDragged(event);
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mouseClicked(MouseEvent event) {
	}

}