package math4u2.view.graph.drawarea;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.SimpleGraphInterface;

/**
 * Popupmenü für die Zeichenfläche
 * 
 * @author Fenn Stefan
 * @see DrawArea
 */
public class DrawAreaPopupMenu implements DrawAreaConstants {

	/** Referenz zur Zeichenfläche */
	private DrawArea drawArea;

	/** Steuerung der Maus */
	private Robot robot;

	/** Das Popupmenü */
	private JPopupMenu popupMenu = new JPopupMenu();

	/** Checkbox für den 1:1 Zoom */
	private JCheckBoxMenuItem oneToOneZoomCheckBox;

	/** Untermenü, für die zu löschenden Graphen */
	private JMenu deleteGraph = new JMenu();

	/** Menüpunkte */
	private JMenuItem zoom = new JMenuItem(), xZoom = new JMenuItem(),
			yZoom = new JMenuItem();

	private JMenuItem boxZoom = new JMenuItem(), translate = new JMenuItem(),
			none = new JMenuItem(), properties1 = new JMenuItem(),
			oneZoom = new JMenuItem();

	/** Listener, wenn sie die Maus bewegt */
	MouseMotionListener currentMotionListener, zoomMotionListener,
			xZoomMotionListener, yZoomMotionListener, boxZoomMotionListener,
			translateMotionListener, oneMotionListener;

	/** Listener, wenn die Maustaste gedrückt wird */
	MouseListener currentMouseListener, zoomMouseListener, xZoomMouseListener,
			yZoomMouseListener, boxZoomMouseListener, translateMouseListener,
			oneMouseListener;

	int ax = 0, ay = 0;

	/**
	 * Initialisiert Popup
	 * 
	 * @param drawArea
	 */
	public DrawAreaPopupMenu(DrawArea drawArea) {
		this.drawArea = drawArea;
		init();
	} // Konstruktor

	/** hier werden alle Popup-Elemente initialisiert */
	public void init() {
		// Robot wird benötigt, um die Maus zu bewegen
		try {
			robot = new Robot();
		} catch (AWTException e) {
			ExceptionManager
					.doError(
							"Wahrscheinlich falsche Java-Version (AWT-Robot) nicht gefunden",
							e);
		} // catch

		popupMenu.setInvoker(drawArea);
		popupMenu.setLightWeightPopupEnabled(true);

		// Zoom
		zoomMotionListener = new ZoomMotionListener();
		zoomMouseListener = new ZoomMouseListener();
		zoom.setToolTipText("linke Maustaste dr\u00fccken und Maus nach oben bzw. unten ziehen");
		zoom.setText("Zoom");
		zoom.setAccelerator(KeyStroke.getKeyStroke("typed Z"));
		zoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				manageListener(zoomMouseListener, zoomMotionListener,
						zoomCursor);
			} // actionPerformed
		});
		popupMenu.add(zoom);

		// Zoom in X-Richtung
		xZoomMotionListener = new XZoomMotionListener();
		xZoomMouseListener = new XZoomMouseListener();
		xZoom.setToolTipText("linke Maustaste dr\u00fccken und Maus nach oben bzw. unten ziehen");
		xZoom.setText("Zoom in X-Richtung");
		xZoom.setAccelerator(KeyStroke.getKeyStroke("typed X"));
		xZoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				manageListener(xZoomMouseListener, xZoomMotionListener,
						xZoomCursor);
			} // actionPerformed
		});
		popupMenu.add(xZoom);

		// Zoom in Y-Richtung
		yZoomMotionListener = new YZoomMotionListener();
		yZoomMouseListener = new YZoomMouseListener();
		yZoom.setToolTipText("linke Maustaste dr\u00fccken und Maus nach oben bzw. unten ziehen");
		yZoom.setText("Zoom in Y-Richtung");
		yZoom.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Y, 0));
		yZoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				manageListener(yZoomMouseListener, yZoomMotionListener,
						yZoomCursor);
			}
		});
		popupMenu.add(yZoom);

		// BoxZoom
		boxZoomMotionListener = new BoxZoomMotionListener();
		boxZoomMouseListener = new BoxZoomMouseListener();
		boxZoom.setToolTipText("linke Maustaste gedr\u00fcckt halten und Rechteck aufspannen");
		boxZoom.setText("Box-Zoom");
		boxZoom.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_B, 0));
		boxZoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				manageListener(boxZoomMouseListener, boxZoomMotionListener,
						boxZoomCursor);
			} // actionPerformed
		});
		popupMenu.add(boxZoom);

		// Zoom 1:1
		oneMotionListener = new OneMotionListener();
		oneMouseListener = new OneMouseListener();
		oneZoom.setToolTipText("linke Maustaste dr\u00fccken und Maus nach oben bzw. unten ziehen");
		oneZoom.setText("Zoom 1:1");
		oneZoom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0));
		oneZoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				manageListener(oneMouseListener, zoomMotionListener, zoomCursor);
			} // actionPerformed
		});
		popupMenu.add(oneZoom);

		oneToOneZoomCheckBox = new JCheckBoxMenuItem("ständiger 1:1 Zoom");
		oneToOneZoomCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				drawArea.activate1To1Zoom(((JCheckBoxMenuItem) evt.getSource())
						.getState());
			} // actionPerformed
		});
		popupMenu.add(oneToOneZoomCheckBox);

		// Seperator
		popupMenu.add(new JSeparator());

		// Verschieben
		translateMotionListener = new TranslateMotionListener();
		translateMouseListener = new TranslateMouseListener();
		translate
				.setToolTipText("linke Maustaste dr\u00fccken und Maus in beliebige Richtung ziehen");
		translate.setText("Verschieben");
		translate.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_V, 0));
		translate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				manageListener(translateMouseListener, translateMotionListener,
						moveCursor);
			} // actionPerformed
		});
		popupMenu.add(translate);

		// None
		none.setToolTipText("normaler Modus");
		none.setText("normaler Modus");
		none.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_SPACE, 0));
		none.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				manageListener(null, null, defaultCursor);
			} // actionPerformed
		});
		popupMenu.add(none);

		// Seperator
		popupMenu.add(new JSeparator());

		// Graphen löschen
		deleteGraph.setText("Lösche Graph");
		popupMenu.add(deleteGraph);

		// Seperator
		popupMenu.add(new JSeparator());

		// Einstellungen
		properties1.setToolTipText("weitere Einstellungen");
		properties1.setText("Einstellungen...");
		properties1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				new PropertyFrame(drawArea,
						(int) drawArea.getLocation().getX() + 50,
						(int) drawArea.getLocation().getY() + 50);
			} // actionPerformed
		});
		popupMenu.add(properties1);
		// DrawArea
		drawArea.addMouseListener(new PopupMouseListener());
		// Shortcuts für alle Zooms
		drawArea.addKeyListener(new DrawAreaKeyListener());
	} // init

	public void activate1To1Zoom(boolean b) {
		if (drawArea.activate1To1Zoom == b)
			return;
		drawArea.activate1To1Zoom = b;
		xZoom.setEnabled(!b);
		yZoom.setEnabled(!b);
		oneZoom.setEnabled(!b);
		oneToOneZoomCheckBox.setState(b);
		drawArea.coordinateSystem(drawArea.getXMin(), drawArea.getXMax(),
				drawArea.getYMin(), drawArea.getYMax());
	} // setactivate1To1Zoom

	/**
	 * diese Methode sorgt dafür, dass der richtige MouseListener und
	 * MouseMotionListener aktiv ist. Außerdem wird der aktuelle Cursor c
	 * gesetzt
	 */
	void manageListener(MouseListener ml, MouseMotionListener mml, Cursor c) {
		if (currentMotionListener != null)
			drawArea.removeMouseMotionListener(currentMotionListener);
		if (currentMouseListener != null)
			drawArea.removeMouseListener(currentMouseListener);
		currentMotionListener = mml;
		currentMouseListener = ml;
		drawArea.addMouseMotionListener(mml);
		drawArea.addMouseListener(ml);
		drawArea.setCursor(c);
	} // manageListener

	void standardPressAction(MouseEvent evt) {
		if (evt.getModifiers() == MouseEvent.BUTTON1_MASK)
			drawArea.aktion = true;
		else
			drawArea.aktion = false;
		ay = evt.getY();
		ax = evt.getX();
		drawArea.setCursor(invisibleCursor);
		if (drawArea.isFastZoom())
			DrawArea.detailA = drawArea.getDetail();
	}// standardPressAction

	// Listenerklassen
	// -------------------------------------------------------------------
	// -------------------------------------------------------------------

	private final class DrawAreaKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent evt) {
			switch (evt.getKeyCode()) {
			case (KeyEvent.VK_Z): {
				manageListener(zoomMouseListener, zoomMotionListener,
						zoomCursor);
				break;
			} // Z
			case (KeyEvent.VK_X): {
				manageListener(xZoomMouseListener, xZoomMotionListener,
						xZoomCursor);
				break;
			} // X
			case (KeyEvent.VK_Y): {
				manageListener(yZoomMouseListener, yZoomMotionListener,
						yZoomCursor);
				break;
			} // Y
			case (KeyEvent.VK_1): {
				manageListener(oneMouseListener, zoomMotionListener, zoomCursor);
				break;
			} // 1
			case (KeyEvent.VK_B): {
				manageListener(boxZoomMouseListener, boxZoomMotionListener,
						boxZoomCursor);
				break;
			} // B
			case (KeyEvent.VK_V): {
				manageListener(translateMouseListener, translateMotionListener,
						moveCursor);
				break;
			} // V
			case (KeyEvent.VK_SPACE): {
				manageListener(null, null, defaultCursor);
				break;
			} // SPACE
			default:
				break;
			} // switch
		} // keyPressed
	} // class DrawAreaKeyListener

	private final class PopupMouseListener extends MouseAdapter {
		public void doPopup(MouseEvent evt) {
			try {
				drawArea.requestFocus();
				if (evt.isPopupTrigger()) {
					deleteGraph.removeAll();

					Iterator iter = drawArea.getGraphList().iterator();
					deleteGraph.setVisible(iter.hasNext());

					while (iter.hasNext()) {
						final SimpleGraphInterface gi = (SimpleGraphInterface) iter
								.next();
						if (gi.getIdentifier() == null)
							continue;
						JMenuItem item = new JMenuItem(gi.getIdentifier() + "");
						item.addActionListener(new DeleteGraphAction(gi));
						deleteGraph.add(item);
					}
					deleteGraph.doLayout();
					popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
				} // if
			} catch (Throwable t) {
				ExceptionManager.doError(t);
			}
		} // doPopup

		public void mousePressed(MouseEvent evt) {
			doPopup(evt);
		} // mousePressed

		public void mouseReleased(MouseEvent evt) {
			doPopup(evt);
		} // mouseReleased
	} // class PopupMouseListener

	private final class DeleteGraphAction implements ActionListener {
		private final SimpleGraphInterface gi;

		private DeleteGraphAction(SimpleGraphInterface gi) {
			super();
			this.gi = gi;
		}

		public void actionPerformed(ActionEvent e) {
			try {
				drawArea.removeGraph(gi);
				gi.detach();
			} catch (Exception e1) {
				ExceptionManager.doError(
						"Fehler beim Löschvorgang von " + gi.getIdentifier(),
						e1);
			}
		}
	} // class DeleteGraphAction

	private final class TranslateMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent evt) {
			standardPressAction(evt);
		} // mousePressed

		public void mouseReleased(MouseEvent evt) {
			if (drawArea.aktion) {
				Point p = drawArea.getLocationOnScreen();
				robot.mouseMove((int) (drawArea.getWidth() / 2 + p.getX()),
						(int) (p.getY() + drawArea.getHeight() / 2));
				drawArea.aktion = false;
			} // if aktion
			drawArea.setCursor(moveCursor);
			if (drawArea.isFastZoom()) {
				drawArea.setDetail(DrawArea.detailA);
			} // if fastZoom
			drawArea.graphHasChanged();
		} // mouseReleased
	} // class TranslateMouseListener

	private final class TranslateMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent evt) {
			if (drawArea.aktion) {
				if (drawArea.isFastZoom())
					drawArea.setDetail(FAST_DETAIL);
				Point p = drawArea.getLocationOnScreen();
				double valX = drawArea.xPerPix() * (evt.getX() - ax);
				double valY = drawArea.yPerPix() * (evt.getY() - ay);
				drawArea.coordinateSystem(drawArea.getXMin() - valX,
						drawArea.getXMax() - valX, drawArea.getYMin() + valY,
						drawArea.getYMax() + valY);
				robot.mouseMove((int) (drawArea.getWidth() / 2 + p.getX()),
						(int) (drawArea.getHeight() / 2 + p.getY()));
				ay = (int) (drawArea.getHeight() / 2);
				ax = (int) (drawArea.getWidth() / 2);
				drawArea.graphHasChanged();
			} // if aktion
		} // mouseDragged
	} // class TranslateMotionListener

	private final class OneMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent evt) {
			drawArea.activate1To1Zoom(true);
			standardPressAction(evt);
		} // mousePressed

		public void mouseReleased(MouseEvent evt) {
			if (drawArea.aktion) {
				Point p = drawArea.getLocationOnScreen();
				robot.mouseMove((int) (drawArea.getWidth() / 2 + p.getX()),
						(int) (p.getY() + drawArea.getHeight() / 2));
				drawArea.aktion = false;
			} // if aktion
			drawArea.setCursor(zoomCursor);
			if (drawArea.isFastZoom()) {
				drawArea.setDetail(DrawArea.detailA);
			} // if fastZoom
			drawArea.graphHasChanged();
			drawArea.activate1To1Zoom(false);
		} // mouseReleased
	} // class OneMouseListener

	private final class OneMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent evt) {
			if (drawArea.aktion) {
				if (drawArea.isFastZoom())
					drawArea.setDetail(FAST_DETAIL);
				Point p = drawArea.getLocationOnScreen();
				double k = (drawArea.getYMax() - drawArea.getYMin())
						/ drawArea.getHeight() * drawArea.getWidth() / 2;
				double m = (drawArea.getXMax() + drawArea.getXMin()) / 2;
				double xMa = m + k;
				double xMi = m - k;

				double valueY = (evt.getY() - ay)
						* (drawArea.getYMax() - drawArea.getYMin())
						/ ((double) drawArea.getHeight());
				drawArea.coordinateSystem(xMi, xMa,
						drawArea.getYMin() - valueY, drawArea.getYMax()
								+ valueY);
				robot.mouseMove((int) (drawArea.getWidth() / 2 + p.getX()),
						(int) (drawArea.getHeight() / 2 + p.getY()));
				ay = (int) (drawArea.getHeight() / 2);
				drawArea.graphHasChanged();
			} // if aktion
		} // mouseDragged
	} // class OneMotionListener

	private final class BoxZoomMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent evt) {
			standardPressAction(evt);
			drawArea.rx1 = ax;
			drawArea.ry1 = ay;
			drawArea.drawRec = true;
		} // mousePressed

		public void mouseReleased(MouseEvent evt) {
			if (drawArea.aktion) {
				if ((Math.abs(evt.getX() - ax) > 5)
						&& (Math.abs(evt.getY() - ay) > 5)) {
					double x1 = drawArea.xPixToCoord(evt.getX());
					double y1 = drawArea.yPixToCoord(evt.getY());
					double x2 = drawArea.xPixToCoord(ax);
					double y2 = drawArea.yPixToCoord(ay);
					drawArea.coordinateSystem(Math.min(x1, x2),
							Math.max(x1, x2), Math.min(y1, y2),
							Math.max(y1, y2));
				} // if
				drawAreaChanged();
				drawArea.aktion = false;
			} // if aktion
			drawArea.drawRec = false;
			drawArea.setCursor(boxZoomCursor);
			if (drawArea.isFastZoom()) {
				drawArea.setDetail(DrawArea.detailA);
				drawAreaChanged();
			} // if fastZoom
		} // mouseReleased
	} // class BoxZoomMouseListener

	private final class BoxZoomMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent evt) {
			if (drawArea.aktion) {
				if (drawArea.isFastZoom())
					drawArea.setDetail(FAST_DETAIL);
				drawArea.rx2 = evt.getX();
				drawArea.ry2 = evt.getY();
				drawAreaChanged();
			} // if aktion
		} // mouseDragged
	} // class BoxZoomMotionListener

	private final class YZoomMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent evt) {
			standardPressAction(evt);
		} // mousePressed

		public void mouseReleased(MouseEvent evt) {
			if (drawArea.aktion) {
				Point p = drawArea.getLocationOnScreen();
				robot.mouseMove((int) (drawArea.getWidth() / 2 + p.getX()),
						(int) (p.getY() + drawArea.getHeight() / 2));
				drawArea.aktion = false;
			} // if aktion
			drawArea.setCursor(yZoomCursor);
			if (drawArea.isFastZoom()) {
				drawArea.setDetail(DrawArea.detailA);
			} // if fastZoom
			drawAreaChanged();
		} // mouseReleased
	} // class YZoomMouseListener

	private final class YZoomMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent evt) {
			if (drawArea.aktion) {
				if (drawArea.isFastZoom())
					drawArea.setDetail(FAST_DETAIL);
				Point p = drawArea.getLocationOnScreen();
				double value = (evt.getY() - ay)
						* (drawArea.getYMax() - drawArea.getYMin())
						/ ((double) drawArea.getHeight());
				drawArea.coordinateSystem(drawArea.getXMin(),
						drawArea.getXMax(), drawArea.getYMin() - value,
						drawArea.getYMax() + value);
				robot.mouseMove((int) (drawArea.getWidth() / 2 + p.getX()),
						(int) (drawArea.getHeight() / 2 + p.getY()));
				ay = (int) (drawArea.getHeight() / 2);
				drawAreaChanged();
			} // if aktion
		} // mouseDragged
	} // class YZoomMotionListener

	private final class XZoomMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent evt) {
			standardPressAction(evt);
		} // mousePressed

		public void mouseReleased(MouseEvent evt) {
			if (drawArea.aktion) {
				Point p = drawArea.getLocationOnScreen();
				robot.mouseMove((int) (drawArea.getWidth() / 2 + p.getX()),
						(int) (p.getY() + drawArea.getHeight() / 2));
				drawArea.aktion = false;
			} // if aktion
			drawArea.setCursor(xZoomCursor);
			if (drawArea.isFastZoom()) {
				drawArea.setDetail(DrawArea.detailA);
			} // if fastZoom
			drawAreaChanged();
		} // mouseReleased
	} // class XZoomMouseListener

	private final class XZoomMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent evt) {
			if (drawArea.aktion) {
				if (drawArea.isFastZoom())
					drawArea.setDetail(FAST_DETAIL);
				Point p = drawArea.getLocationOnScreen();
				double value = (evt.getY() - ay)
						* (drawArea.getXMax() - drawArea.getXMin())
						/ ((double) drawArea.getWidth());
				drawArea.coordinateSystem(drawArea.getXMin() - value,
						drawArea.getXMax() + value, drawArea.getYMin(),
						drawArea.getYMax());
				robot.mouseMove((int) (drawArea.getWidth() / 2 + p.getX()),
						(int) (drawArea.getHeight() / 2 + p.getY()));
				ay = (int) (drawArea.getHeight() / 2);
				drawAreaChanged();
			} // if aktion
		} // mouseDragged
	} // class XZoomMotionListener

	private final class ZoomMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent evt) {
			standardPressAction(evt);
		} // mousePressed

		public void mouseReleased(MouseEvent evt) {
			if (drawArea.aktion) {
				Point p = drawArea.getLocationOnScreen();
				robot.mouseMove((int) (drawArea.getWidth() / 2 + p.getX()),
						(int) (p.getY() + drawArea.getHeight() / 2));
				if (drawArea.isFastZoom()) {
					drawArea.setDetail(DrawArea.detailA);
				} // if fastZoom
				drawArea.aktion = false;
				drawAreaChanged();
			} // if aktion
			drawArea.setCursor(zoomCursor);
		} // mouseReleased
	} // class ZoomMouseListener

	private final class ZoomMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent evt) {
			if (drawArea.aktion) {
				if (drawArea.isFastZoom())
					drawArea.setDetail(FAST_DETAIL);
				Point p = drawArea.getLocationOnScreen();
				double valueY = (evt.getY() - ay)
						* (drawArea.getYMax() - drawArea.getYMin())
						/ ((double) drawArea.getHeight());
				double valueX = (evt.getY() - ay)
						* (drawArea.getXMax() - drawArea.getYMin())
						/ ((double) drawArea.getWidth());
				valueX = (valueY / (drawArea.getYMax() - drawArea.getYMin()) * (drawArea
						.getXMax() - drawArea.getXMin()));
				drawArea.coordinateSystem(drawArea.getXMin() - valueX,
						drawArea.getXMax() + valueX, drawArea.getYMin()
								- valueY, drawArea.getYMax() + valueY);
				robot.mouseMove((int) (drawArea.getWidth() / 2 + p.getX()),
						(int) (drawArea.getHeight() / 2 + p.getY()));
				ay = (int) (drawArea.getHeight() / 2);
				drawAreaChanged();
			} // if aktion
		} // mouseDragged
	} // class ZoomMotionListener

	public void setCursorPosition(Point point) {
		robot.mouseMove(point.x, point.y);
	}

	public void drawAreaChanged() {
		drawArea.fireChangeEvent();
	}

} // class DrawAreaPopupMenu
