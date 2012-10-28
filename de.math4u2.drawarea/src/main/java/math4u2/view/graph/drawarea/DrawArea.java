package math4u2.view.graph.drawarea;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseMotionListener;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.Border;

import math4u2.application.resource.Colors;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.swing.RoundedRectBorder;
import math4u2.view.Formatierer;
import math4u2.view.formula.FormulaRenderContext;
import math4u2.view.graph.SimpleGraphInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.SimpleScalarDoubleValueHolder;

public class DrawArea extends JPanel implements DrawAreaInterface,
		DrawAreaConstants {

	protected static int preferredDetail = 1;

	protected String name = "da";

	protected String title = "";

	protected Color gridColor = Color.lightGray, axisColor = Color.black;

	protected Stroke gridStroke = new BasicStroke(1),
			axesStroke = new BasicStroke(2);

	// Faktoren fuer den Abstand der Gitterlinien
	protected double xFak = 1.0f, yFak = 1.0f;

	// Nullstellige Funktionen, liefern die aktuellen Grenzen des
	// Koordinatensystems
	protected IScalarDoubleHolder xMinFunct, xMaxFunct, yMinFunct, yMaxFunct,
			xDistFunct, yDistFunct;

	// Anzahl Pixel pro Einheit in beiden Achsenrichtung
	// diese beiden Werte werden aus Performance-Gruenden gespeicher
	protected double xPixPerUnit, yPixPerUnit;

	protected MouseMotionListener mouseDisplayListener;

	// Popup Menu
	protected DrawAreaPopupMenu drawAreaPopupMenu;

	protected boolean makeNoRenew = false;

	protected Border roundRect;

	// Hoehe und Breite, die beim Erzeugen gesetzt wird
	protected int myHeight, myWidth;

	// Hoehe und Breite in Pixel, die beim Zeichnen verwendet wird
	protected int height, width;

	// Graphen, die gezeichnet werden
	protected List<SimpleGraphInterface> graphs = new LinkedList<SimpleGraphInterface>();

	protected boolean fastZoom = false;

	protected int stroke = 2;

	static int detail = 1, detailA = 1;

	protected static int borderThickness = 6;

	int rx1 = 0, rx2 = 0, ry1 = 0, ry2 = 0;

	protected boolean aktion = false, drawRec = false, graphChanged = false,
			activate1To1Zoom = false;

	protected List<DrawAreaChangeListener> listeners = new LinkedList<DrawAreaChangeListener>();

	public DrawArea(String name, int width, int height) {
		this.name = name;
		initValueHolder();

		myWidth = width;
		myHeight = height;
		Border b2 = new RoundedRectBorder(Color.BLACK, 1.2f, 7, 5);
		// setBorder(b1);
		roundRect = b2;

		setPreferredSize(new Dimension(10000, 10000)); // auf maximale Größe
		// setzten
		setLayout(null);
		setOpaque(true);
		setDoubleBuffered(false);
		setBackground(Color.WHITE);
		drawAreaPopupMenu = new DrawAreaPopupMenu(this);
		setDetail(preferredDetail);
		addMouseMotionListener(mouseDisplayListener);
	} 
	
	private void initValueHolder() {
		xMinFunct = new SimpleScalarDoubleValueHolder(0);
		xMaxFunct = new SimpleScalarDoubleValueHolder(0);
		yMinFunct = new SimpleScalarDoubleValueHolder(0);
		yMaxFunct = new SimpleScalarDoubleValueHolder(0);
		xDistFunct = new SimpleScalarDoubleValueHolder(0);
		yDistFunct = new SimpleScalarDoubleValueHolder(0);
		setXMin(-5);
		setXMax(5);
		setYMin(-5);
		setYMax(5);
	}

	public DrawArea(String name) {
		this(name, 100, 100);
	} // Konstruktor 2

	public String getName() {
		return name;
	}

	public Component add(Component b) {
		b.addMouseMotionListener(mouseDisplayListener);
		return super.add(b);
	}

	public boolean isFastZoom() {
		return fastZoom;
	}

	public void setFastZoom(boolean b) {
		fastZoom = b;
	}

	public double getGridMeshX() {
		return 1.0 / xFak;
	}

	public double getGridMeshY() {
		return 1.0 / yFak;
	}

	public Color getAxisColor() {
		return axisColor;
	}

	public void setAxisColor(Color c) {
		axisColor = c;
	}

	public void setGridColor(Color c) {
		gridColor = c;
	}

	public Color getGridColor() {
		return gridColor;
	}

	public int getStroke() {
		return stroke;
	}

	public void setStroke(int i) {
		stroke = i;
	}

	public double getXMin() {
		return xMinFunct.getScalarOrNan();
	}

	public double getXMax() {
		return xMaxFunct.getScalarOrNan();
	}

	public double getYMin() {
		return yMinFunct.getScalarOrNan();
	}

	public double getYMax() {
		return yMaxFunct.getScalarOrNan();
	}

	public int getWidth() {
		return width;
	}

	public void setDrawareaSize(int width, int height) {
		this.width = width;
		this.height = height;
		graphChanged = true;
	}

	public int getHeight() {
		return height;
	}

	public int xCoordToPix(double xCoord) {
		return (int) ((xCoord - getXMin()) * xPixPerUnit) + borderThickness;
	}

	public int yCoordToPix(double yCoord) {
		return (int) (height - (yCoord - getYMin()) * yPixPerUnit)
				- borderThickness;
	}

	public double xPixToCoord(int xPix) {
		return ((xPix - borderThickness) / xPixPerUnit + getXMin());
	}

	public double yPixToCoord(int yPix) {
		return (-(yPix + borderThickness - height) / yPixPerUnit + getYMin());
	}

	public double xPerPix() {
		return (1 / xPixPerUnit);
	}

	public double yPerPix() {
		return (1 / yPixPerUnit);
	}

	public void setGridMesh(double xDiv, double yDiv) {
		try {
			xFak = (double) 1. / xDiv;
			xDistFunct.setScalar(getGridPointDist(getXMin(), getXMax(), xFak),
					makeNoRenew);
			yFak = (double) 1. / yDiv;
			yDistFunct.setScalar(getGridPointDist(getYMin(), getYMax(), yFak),
					makeNoRenew);
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Setzen der Gitterdichte", e);
		}// catch
	} // setGridMesh

	public void setDetail(int i) {
		detail = i;
	} // setDetail

	public int getDetail() {
		return detail;
	} // getDetail

	public boolean inMouseAction() {
		return aktion;
	}

	/**
	 * Überprüft, ob das Model eines Graphen mit dem Schlüssel <code>key</code>
	 * in der Zeichenfläche vorhanden ist.
	 */
	public boolean containsGraph(Object key) {
		for (Iterator iter = graphs.iterator(); iter.hasNext();) {
			SimpleGraphInterface gi = (SimpleGraphInterface) iter.next();
			if (gi == null)
				return false;
			if (gi.getKey() == null)
				return false;
			if (gi.getKey().equals(key))
				return true;
		} // for iter
		return false;
	}// containsGraph

	/**
	 * Ein Graph kann nur einmal eingefügt werden, ansonsten wird eine
	 * <code>RuntimeException</code> geworfen. (Emulation einer geordneten
	 * Mengen)
	 */
	public void addGraph(SimpleGraphInterface graph) {
		if (graphs.contains(graph)) {
			graphs.remove(graph);
			throw new RuntimeException(
					"Graph schon vorhanden in Zeichenfläche " + getKey());
		} // if
		graphs.add(graph);
		graphHasChanged();
	} // addGraph

	public SimpleGraphInterface getGraph(String name) {
		for (Iterator iter = graphs.iterator(); iter.hasNext();) {
			SimpleGraphInterface gi = (SimpleGraphInterface) iter.next();
			if (name.equals(gi.getKey()))
				return gi;
		}// for iter
		return null;
	}// getGraph

	public List<SimpleGraphInterface> getGraphList() {
		return graphs;
	}// getGraphList

	public void deleteGraph(String graphName) {
		// suche den Graphen in der Menge
		SimpleGraphInterface graph = null;
		for (Iterator iter = graphs.iterator(); iter.hasNext();) {
			if (graphName.equals((graph = (SimpleGraphInterface) iter.next())
					.getKey()))
				break;
		} // for iter
		if (graph == null)
			throw new NullPointerException("Der Graph " + graphName
					+ " wurde nicht in der Zeichenfläche " + getName()
					+ " gefunden.\n" + toString());

		// lösche den Graphen
		removeGraph(graph);
	}

	private void _setXMin(double val) {
		try {
			xMinFunct.setScalar(val, makeNoRenew);
			xDistFunct.setScalar(getGridPointDist(getXMin(), getXMax(), xFak),
					makeNoRenew);
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Setzen von xMin", e);
		}
	}

	/** setzt den Wert nur dann, wenn er aus einen Konstanten Wert besteht */
	public void setXMin(double val) {
		if (xMinFunct.isFixed())
			return;
		_setXMin(val);
	} // setXMin

	private void _setYMin(double val) {
		try {
			yMinFunct.setScalar(val, makeNoRenew);
			yDistFunct.setScalar(getGridPointDist(getYMin(), getYMax(), yFak),
					makeNoRenew);
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Setzen von yMin", e);
		}
	}

	/** setzt den Wert nur dann, wenn er aus einen Konstanten Wert besteht */
	public void setYMin(double val) {
		if (yMinFunct.isFixed())
			return;
		_setYMin(val);
	} // setYMin

	private void _setXMax(double val) {
		try {
			xMaxFunct.setScalar(val, makeNoRenew);
			xDistFunct.setScalar(getGridPointDist(getXMin(), getXMax(), xFak),
					makeNoRenew);
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Setzen von xMax", e);
		}
	}

	/** setzt den Wert nur dann, wenn er aus einen Konstanten Wert besteht */
	public void setXMax(double val) {
		if (xMaxFunct.isFixed())
			return;
		_setXMax(val);
	}

	public void _setYMax(double val) {
		try {
			yMaxFunct.setScalar(val, makeNoRenew);
			yDistFunct.setScalar(getGridPointDist(getYMin(), getYMax(), yFak),
					makeNoRenew);
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Setzen von yMax", e);
		}
	}

	/** setzt den Wert nur dann, wenn er aus einen Konstanten Wert besteht */
	public void setYMax(double val) {
		if (yMaxFunct.isFixed())
			return;
		_setYMax(val);
	}

	public void removeGraph(SimpleGraphInterface graph) {
		if (!graphs.contains(graph)) {
			return;
		}
		graphs.remove(graph); // Zeichenfläche benachrichtigt nicht mehr den
		try {
			graph.detach();
		} catch (Exception e) {
			ExceptionManager.doError("Graph " + graph.getKey()
					+ " kann nicht gelöscht werden.");
		}
		// Graphen
		graphHasChanged();
	}

	public void removeAllGraphs() {
		graphs.clear();
	}

	private void _redefine(double xNewMin, double xNewMax, double yNewMin,
			double yNewMax) {
		boolean temp = makeNoRenew;
		makeNoRenew = true;
		setXMin(xNewMin);
		setXMax(xNewMax);
		setYMin(yNewMin);
		setYMax(yNewMax);
		makeNoRenew = temp;

		double xDiff = getXMax() - getXMin();
		xPixPerUnit = (width - 2 * borderThickness) / xDiff;
		double yDiff = getYMax() - getYMin();
		yPixPerUnit = (height - 2 * borderThickness) / yDiff;
	} // _redefine

	public void coordinateSystem(double xNewMin, double xNewMax,
			double yNewMin, double yNewMax) {
		if (!activate1To1Zoom) { // normaler Zoom
			_redefine(xNewMin, xNewMax, yNewMin, yNewMax);
		} else { // 1:1 Zoom
			if (getWidth() == 0)
				doLayout();
			double yDiffNeu = ((double) getHeight()) / ((double) getWidth())
					* (xNewMax - xNewMin);
			double dy = yNewMax - yNewMin;
			_redefine(xNewMin, xNewMax, yNewMin - (yDiffNeu - dy) / 2, yNewMax
					+ (yDiffNeu - dy) / 2);

		} // else
	} // coordinateSystem

	public Object getKey() {
		return name;
	}// getKey

	public void graphHasChanged() {
		if (makeNoRenew)
			return;
		boolean temp = makeNoRenew;
		makeNoRenew = true;
		if (activate1To1Zoom) {
			coordinateSystem(getXMin(), getXMax(), getYMin(), getYMax());
		} // if
		makeNoRenew = temp;
		graphChanged = true;
		invalidate();
		// Es wird nur die Zeichenfläche erneuert und nicht der Rand.
		// Diese Methode erspart viel Rechenzeit.
		repaint(borderThickness, borderThickness, width - 2 * borderThickness,
				height - 2 * borderThickness);		
	} // graphHasChanged

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// nur zeichnen wenn eine Änderung vorgenommen wird
		if (!graphChanged) {
			if (!isValid()) {
				// da von Handle paintComponent aufgerufen wird
				return;
			}// if
		}// if

		graphChanged = true;

		Stroke oldS = ((Graphics2D) g).getStroke();

		// draw the net
		g.translate(borderThickness, -borderThickness);
		createGrid((Graphics2D) g);
		g.translate(-borderThickness, borderThickness);

		// draw graphs
		Iterator gI = graphs.iterator();
		((Graphics2D) g).setRenderingHints(antialiasOn);
		while (gI.hasNext()) {
			try {
				SimpleGraphInterface f = (SimpleGraphInterface) gI.next();
				f.paintGraph(g);
			} catch (ConcurrentModificationException cme) {
			} // kann bei mehreren Threads passieren
		} // while
		g.setColor(Color.black);
		if (drawRec)
			g.drawRect(rx1, ry1, rx2 - rx1, ry2 - ry1); // für Box-Zoom
		((Graphics2D) g).setStroke(oldS);

		super.paintChildren(g);
	} // paintComponent

	protected void paintChildren(Graphics arg0) {
	}// paintChildren

	protected void paintBorder(Graphics g) {
		drawTitle(g);
		roundRect.paintBorder(this, g, 0, 0, width, height);
	}// paintBorder

	private void drawTitle(Graphics g) {
		if (title != null && title.length() > 0) {
			((Graphics2D) g).setRenderingHints(antialiasOn);
			g.setColor(Colors.SELECTED);
			Font font = FormulaRenderContext.getFont("Serif", Font.PLAIN, 18);
			g.setFont(font);

			int tWidth = g.getFontMetrics().stringWidth(title) + 18;
			int xPos = width - tWidth;
			int tHeight = 30;
			// Hintergrund
			g.fillRect(xPos, 0, tWidth + 10, tHeight);
			g.fillArc(xPos - tHeight, 0 - tHeight, tHeight * 2, tHeight * 2,
					-90, -90);

			// Text
			g.setColor(Color.BLACK);
			g.drawString(title, xPos, 24);
			// Rahmen
			g.setColor(new Color(130, 130, 130));
			g.drawLine(xPos, tHeight, width, tHeight);
			g.drawArc(xPos - tHeight, 0 - tHeight, tHeight * 2, tHeight * 2,
					-90, -90);
			((Graphics2D) g).setRenderingHints(antialiasOff);
		}// if
	}// drawTitle

	public boolean getActivate1To1Zoom() {
		return activate1To1Zoom;
	} // getActivate1To1Zoom

	public void activate1To1Zoom(boolean b) {
		drawAreaPopupMenu.activate1To1Zoom(b);
		graphHasChanged();
	} // setactivate1To1Zoom

	public void doLayout() {
		super.doLayout();
		Dimension d = getSize();
		width = d.width;
		height = d.height;
		if (width < 10)
			width = myWidth;
		if (height < 10)
			height = myHeight;
		graphHasChanged();
	} // doLayout

	public String toString() {
		return "DrawArea " + name + "(" + graphs + ")";
	} // toString

	/**
	 * Zeichnet auf g das Koordinatensystem
	 */
	public void createGrid(Graphics2D g) {
		double xMin = getXMin();
		double xMax = getXMax();
		double yMin = getYMin();
		double yMax = getYMax();

		double xDiff = xMax - xMin;
		xPixPerUnit = (width - 2 * borderThickness) / xDiff;
		double yDiff = yMax - yMin;
		yPixPerUnit = (height - 2 * borderThickness) / yDiff;

		// mathematicher Abstand der Gitterlinien in x-Richtung
		double xF = getXGridDist();
		// mathematischer Abstand der Gitterlinien in y-Richtung
		double yF = getYGridDist();

		double startValue;
		int number, position, axisPosition;

		// senkrechte Linien Zeichnen

		// startValue = initialisieren mit mathematischer x-Position
		// der am weitesten links
		// gelegenen vertikalen Koordinatenlinie
		startValue = Math.ceil(xMin / xF) * xF;
		// number = Anzahl der vertikalen Koordinatenlinien
		number = (int) Math.round(Math.floor(xDiff / xF)) + 1;
		// Pixel-Position fuer Beschriftung berechnen
		if ((yMin < 0) && (yMax > 0)) {
			axisPosition = (int) (height + yMin * yPixPerUnit) + 12;
		} else {
			axisPosition = height - 12;
		}
		// zeichnen
		g.setColor(gridColor);
		g.setStroke(gridStroke);
		for (int i = 0; i < number; i++) {
			// feststellen, ob aktuelle x-Position der die Position der
			// vertikalen y-Achse ist, wenn ja, Farbe wechseln
			if (Math.abs(startValue) < xF * 0.5) {
				g.setColor(axisColor);
				g.setStroke(axesStroke);
			} // if
			position = (int) (Math.round((startValue - xMin) * xPixPerUnit));
			g.drawLine(position, 0, position, height);
			g.setColor(axisColor);
			g.setStroke(gridStroke);
			g.drawString(Formatierer.value2String(startValue), position + 2,
					axisPosition);
			g.setColor(gridColor);
			g.setStroke(gridStroke);
			startValue += xF;
		} // for, senkrechte Linien zeichnen

		// analog: waagrechte Linien zeichnen
		startValue = Math.ceil(yMin / yF) * yF;
		number = (int) Math.round(Math.floor(yDiff / yF)) + 1;
		if ((xMin < 0) && (xMax > 0)) {
			axisPosition = (int) (-xMin * xPixPerUnit) + 5;
		} else {
			axisPosition = 5;
		}
		g.setColor(gridColor);
		g.setStroke(gridStroke);
		for (int i = 0; i < number; i++) {
			if (Math.abs(startValue) < yF * 0.5) {
				g.setColor(axisColor);
				g.setStroke(axesStroke);
			} // if
			position = (int) height
					- (int) (Math.round((startValue - yMin) * yPixPerUnit));
			g.drawLine(0, position, width, position);
			g.setColor(axisColor);
			g.setStroke(gridStroke);
			g.drawString(Formatierer.value2String(startValue), axisPosition,
					position - 2);
			g.setColor(gridColor);
			g.setStroke(gridStroke);
			startValue += yF;
		} // for, waagrechte Linien zeichnen
	} // createGridGraphics

	/**
	 * Berechnet den mathematischen Abstand zweier Gitterpunkte, wenn sich das
	 * Gitter von min bis max erstreckt. Für factor == 1 wird ein
	 * Standardabstand berechnet und zurückgegeben, ansonsten Standardabstand *
	 * factor
	 * 
	 * @param min
	 *            linke Begrenzung des Gitters
	 * @param max
	 *            rechte Begrenzung des Gitters
	 * @param factor
	 *            Vergrößerungsfaktor im Vergleich zum Standardabstand
	 * @return Abstand zweier Gitterpunkte
	 */
	protected double getGridPointDist(double min, double max, double factor) {
		double diff = max - min;
		double result = Math.pow(10.0,
				Math.ceil(Math.log(diff) / Math.log(10.0)) - 1);
		switch ((int) (diff / result)) {
		case 7:
		case 6:
		case 5:
		case 4:
			result /= 2;
			break;
		case 3:
			result /= 4;
			break;
		case 2:
			result /= 5;
			break;
		case 1:
			result /= 10;
			break;
		}
		result *= factor;
		return result;
	}

	/**
	 * @return mathemaischer Abstand der Gitterpunkte in x-Richtung
	 */
	public double getXGridDist() {
		return getGridPointDist(getXMin(), getXMax(), xFak);
	}

	/**
	 * @return mathemaischer Abstand der Gitterpunkte in y-Richtung
	 */
	public double getYGridDist() {
		return getGridPointDist(getYMin(), getYMax(), yFak);
	}

	/**
	 * @return Alle Gitterpunkte in Array result vom Typ double[2][Anzahl],
	 *         dabei steht in result[0][...] die mathematische x-Koordinate, in
	 *         result[1][...] die mathematische y-Koordinate eines
	 *         Gitterpunktes.
	 */
	public double[][] getGridPoints() {
		double xDiff = getXMax() - getXMin();
		xPixPerUnit = (width - 2 * borderThickness) / xDiff;
		double yDiff = getYMax() - getYMin();
		yPixPerUnit = (height - 2 * borderThickness) / yDiff;

		// mathematicher Abstand der Gitterlinien in x-Richtung
		double xF = getXGridDist();
		// mathematischer Abstand der Gitterlinien in y-Richtung
		double yF = getYGridDist();

		double xMin = getXMin();
		double xStartValue = Math.ceil(xMin / xF) * xF;
		int xNumber = (int) Math.round(Math.floor(xDiff / xF)) + 1;
		double yMin = getYMin();
		double yStartValue;
		int yNumber = (int) Math.round(Math.floor(yDiff / yF)) + 1;
		double[][] result = new double[2][xNumber * yNumber];
		int resultPos = 0;
		for (int ix = 0; ix < xNumber; ix++) {
			yStartValue = Math.ceil(yMin / yF) * yF;
			for (int iy = 0; iy < yNumber; iy++) {
				result[0][resultPos] = xStartValue;
				result[1][resultPos] = yStartValue;
				resultPos++;
				yStartValue += yF;
			}
			xStartValue += xF;
		} // for
		return result;
	} // getGridPoints

	public void setCursorPosition(Point point) {
		drawAreaPopupMenu.setCursorPosition(point);
	}

	public boolean isMakeNoRenew() {
		return makeNoRenew;
	}// isMakeNoRenew

	public void setMakeNoRenew(boolean makeNoRenew) {
		this.makeNoRenew = makeNoRenew;
	}// setMakeNoRenew

	public void setTitle(String title) {
		this.title = title;
		graphHasChanged();
	}// setTitle

	public String getTitle() {
		return title;
	}// getTitle

	public boolean isOptimizedDrawingEnabled() {
		return true;
	}

	public void addChangeListener(DrawAreaChangeListener listener) {
		listeners.add(listener);
	}

	public void removeChangeListener(DrawAreaChangeListener listener) {
		listeners.remove(listener);
	}

	void fireChangeEvent() {
		for (DrawAreaChangeListener listener : listeners) {
			listener.drawAreaChanged();
		}
	}
} // DrawArea
