package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.gradient.GradientPainter;
import math4u2.view.graph.gradient.GradientSegment;
import math4u2.view.graph.util.IFunction2;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;
import math4u2.view.graph.util.SimpleScalarDoubleValueHolder;

/**
 * Graphische Darstellung einer Map (Karte = Hoehendarstellung einer Funktion
 * f(x,y).
 * 
 * 
 * @author Max Weiss
 * 
 */

public class MapGraph extends AbstractSimpleGraph {

	/** Breite des Abgespeicherten Bildes für den Farbverlauf */
	public static final int GRADIENT_WIDTH = 5000;

	/** Weiß Transparent für oberen Bereich */
	private final int WHITE_TRANSPARENT = new Color(255, 255, 255, 50).getRGB();

	/** Schwarz Transparent für unteren Bereich */
	private final int BLACK_TRANSPARENT = new Color(0, 0, 0, 100).getRGB();

	public String getKey() {
		return evalFunction.getKey();
	}

	public void detach() throws Exception {
	}

	/** Muß neu gerechnet werden ? */
	private boolean validate = false;

	public static int[][] gradientMaps;

	private static GradientPainter[] gradientArray;

	private double xMin, xMax, yMin, yMax, zMin, zMax, zdelta;

	private int height, width, oldWidth, oldHeight;

	private IScalarDoubleHolder xPos = new SimpleScalarDoubleValueHolder(Double.NaN);

	private IScalarDoubleHolder yPos = new SimpleScalarDoubleValueHolder(Double.NaN);

	private BufferedImage bi;

	private MapThread mapThread;

	private IVectorDoubleValueHolder bandVector;
	private IScalarDoubleHolder contourDelta;
	private IFunction2<IScalarDoubleHolder, IScalarDoubleHolder, IScalarDoubleHolder> evalFunction;

	static {
		// Farbverläufe erzeugen
		List<GradientPainter> gradients = new LinkedList<GradientPainter>();
		GradientPainter gp = new GradientPainter();

		gp = new GradientPainter();
		gp.addSegment(new GradientSegment(new Color(51, 59, 126), new Color(45,
				74, 138), 0.0, 0.05, false));
		gp.addSegment(new GradientSegment(new Color(45, 74, 138), new Color(38,
				89, 149), 0.05, 0.1, false));
		gp.addSegment(new GradientSegment(new Color(38, 89, 149), new Color(28,
				116, 174), 0.1, 0.15, false));
		gp.addSegment(new GradientSegment(new Color(28, 116, 174), new Color(
				18, 142, 186), 0.15, 0.2, false));
		gp.addSegment(new GradientSegment(new Color(18, 142, 186), new Color(
				19, 158, 198), 0.2, 0.25, false));
		gp.addSegment(new GradientSegment(new Color(19, 158, 198), new Color(
				22, 161, 191), 0.25, 0.3, false));
		gp.addSegment(new GradientSegment(new Color(22, 161, 191), new Color(
				28, 160, 163), 0.3, 0.35, false));
		gp.addSegment(new GradientSegment(new Color(28, 160, 163), new Color(
				47, 157, 119), 0.35, 0.4, false));
		gp.addSegment(new GradientSegment(new Color(47, 157, 119), new Color(
				86, 156, 66), 0.4, 0.45, false));
		gp.addSegment(new GradientSegment(new Color(86, 156, 66), new Color(
				121, 162, 43), 0.45, 0.5, false));
		gp.addSegment(new GradientSegment(new Color(121, 162, 43), new Color(
				162, 172, 29), 0.5, 0.55, false));
		gp.addSegment(new GradientSegment(new Color(162, 172, 29), new Color(
				194, 184, 22), 0.55, 0.6, false));
		gp.addSegment(new GradientSegment(new Color(194, 184, 22), new Color(
				216, 194, 17), 0.6, 0.65, false));
		gp.addSegment(new GradientSegment(new Color(216, 194, 17), new Color(
				223, 204, 15), 0.65, 0.7, false));
		gp.addSegment(new GradientSegment(new Color(223, 204, 15), new Color(
				238, 201, 15), 0.7, 0.75, false));
		gp.addSegment(new GradientSegment(new Color(238, 201, 15), new Color(
				237, 180, 17), 0.75, 0.8, false));
		gp.addSegment(new GradientSegment(new Color(237, 180, 17), new Color(
				236, 144, 19), 0.8, 0.85, false));
		gp.addSegment(new GradientSegment(new Color(236, 144, 19), new Color(
				228, 96, 25), 0.85, 0.90, false));
		gp.addSegment(new GradientSegment(new Color(228, 96, 25), new Color(
				221, 60, 30), 0.9, 0.95, false));
		gp.addSegment(new GradientSegment(new Color(221, 60, 30), new Color(
				221, 49, 33), 0.95, 1.0, false));
		gradients.add(gp);

		gp = new GradientPainter();
		gp.addSegment(new GradientSegment(new Color(255 - 51, 255 - 59,
				255 - 126), new Color(255 - 45, 255 - 74, 255 - 138), 0.0,
				0.05, false));
		gp.addSegment(new GradientSegment(new Color(255 - 45, 255 - 74,
				255 - 138), new Color(255 - 38, 255 - 89, 255 - 149), 0.05,
				0.1, false));
		gp.addSegment(new GradientSegment(new Color(255 - 38, 255 - 89,
				255 - 149), new Color(255 - 28, 255 - 116, 255 - 174), 0.1,
				0.15, false));
		gp.addSegment(new GradientSegment(new Color(255 - 28, 255 - 116,
				255 - 174), new Color(255 - 18, 255 - 142, 255 - 186), 0.15,
				0.2, false));
		gp.addSegment(new GradientSegment(new Color(255 - 18, 255 - 142,
				255 - 186), new Color(255 - 19, 255 - 158, 255 - 198), 0.2,
				0.25, false));
		gp.addSegment(new GradientSegment(new Color(255 - 19, 255 - 158,
				255 - 198), new Color(255 - 22, 255 - 161, 255 - 191), 0.25,
				0.3, false));
		gp.addSegment(new GradientSegment(new Color(255 - 22, 255 - 161,
				255 - 191), new Color(255 - 28, 255 - 160, 255 - 163), 0.3,
				0.35, false));
		gp.addSegment(new GradientSegment(new Color(255 - 28, 255 - 160,
				255 - 163), new Color(255 - 47, 255 - 157, 255 - 119), 0.35,
				0.4, false));
		gp.addSegment(new GradientSegment(new Color(255 - 47, 255 - 157,
				255 - 119), new Color(255 - 86, 255 - 156, 255 - 66), 0.4,
				0.45, false));
		gp.addSegment(new GradientSegment(new Color(255 - 86, 255 - 156,
				255 - 66), new Color(255 - 121, 255 - 162, 255 - 43), 0.45,
				0.5, false));
		gp.addSegment(new GradientSegment(new Color(255 - 121, 255 - 162,
				255 - 43), new Color(255 - 162, 255 - 172, 255 - 29), 0.5,
				0.55, false));
		gp.addSegment(new GradientSegment(new Color(255 - 162, 255 - 172,
				255 - 29), new Color(255 - 194, 255 - 184, 255 - 22), 0.55,
				0.6, false));
		gp.addSegment(new GradientSegment(new Color(255 - 194, 255 - 184,
				255 - 22), new Color(255 - 216, 255 - 194, 255 - 17), 0.6,
				0.65, false));
		gp.addSegment(new GradientSegment(new Color(255 - 216, 255 - 194,
				255 - 17), new Color(255 - 223, 255 - 204, 255 - 15), 0.65,
				0.7, false));
		gp.addSegment(new GradientSegment(new Color(255 - 223, 255 - 204,
				255 - 15), new Color(255 - 238, 255 - 201, 255 - 15), 0.7,
				0.75, false));
		gp.addSegment(new GradientSegment(new Color(255 - 238, 255 - 201,
				255 - 15), new Color(255 - 237, 255 - 180, 255 - 17), 0.75,
				0.8, false));
		gp.addSegment(new GradientSegment(new Color(255 - 237, 255 - 180,
				255 - 17), new Color(255 - 236, 255 - 144, 255 - 19), 0.8,
				0.85, false));
		gp.addSegment(new GradientSegment(new Color(255 - 236, 255 - 144,
				255 - 19), new Color(255 - 228, 255 - 96, 255 - 25), 0.85,
				0.90, false));
		gp.addSegment(new GradientSegment(new Color(255 - 228, 255 - 96,
				255 - 25), new Color(255 - 221, 255 - 60, 255 - 30), 0.9, 0.95,
				false));
		gp.addSegment(new GradientSegment(new Color(255 - 221, 255 - 60,
				255 - 30), new Color(255 - 221, 255 - 49, 255 - 33), 0.95, 1.0,
				false));
		gradients.add(gp);

		gp = new GradientPainter();
		gp.addSegment(new GradientSegment(new Color(0, 0, 0), new Color(255,
				255, 255), 0.0, 1.0, false));
		gradients.add(gp);

		gp = new GradientPainter();
		gp.addSegment(new GradientSegment(new Color(51, 0, 102), new Color(0,
				0, 128), 0.0, 0.5, false));
		gp.addSegment(new GradientSegment(new Color(0, 0, 128), new Color(204,
				255, 255), 0.5, 1.0, false));
		gradients.add(gp);

		gp = new GradientPainter();
		gp.addSegment(new GradientSegment(new Color(102, 0, 0), new Color(102,
				0, 0), 0.0, 0.5, false));
		gp.addSegment(new GradientSegment(new Color(255, 102, 0), new Color(
				255, 102, 0), 0.5, 1.0, false));
		gradients.add(gp);

		gp = new GradientPainter();
		gp.addSegment(new GradientSegment(new Color(0, 0, 0),
				new Color(0, 0, 0), 0.0, 0.05, false));
		gp.addSegment(new GradientSegment(new Color(255, 255, 255), new Color(
				255, 255, 255), 0.05, 0.1, false));
		gp.addSegment(new GradientSegment(new Color(0, 0, 0),
				new Color(0, 0, 0), 0.1, 0.15, false));
		gp.addSegment(new GradientSegment(new Color(255, 255, 255), new Color(
				255, 255, 255), 0.15, 0.20000048, false));
		gp.addSegment(new GradientSegment(new Color(0, 0, 0),
				new Color(0, 0, 0), 0.20000048, 0.25000027, false));
		gp.addSegment(new GradientSegment(new Color(255, 255, 255), new Color(
				255, 255, 255), 0.25000027, 0.3, false));
		gp.addSegment(new GradientSegment(new Color(0, 0, 0),
				new Color(0, 0, 0), 0.3, 0.35, false));
		gp.addSegment(new GradientSegment(new Color(255, 255, 255), new Color(
				255, 255, 255), 0.35, 0.40001193, false));
		gp.addSegment(new GradientSegment(new Color(0, 0, 0),
				new Color(0, 0, 0), 0.40001193, 0.45, false));
		gp.addSegment(new GradientSegment(new Color(255, 255, 255), new Color(
				255, 255, 255), 0.45, 0.5, false));
		gp.addSegment(new GradientSegment(new Color(0, 0, 0),
				new Color(0, 0, 0), 0.5, 0.55, false));
		gp.addSegment(new GradientSegment(new Color(255, 255, 255), new Color(
				255, 255, 255), 0.55, 0.6, false));
		gp.addSegment(new GradientSegment(new Color(0, 0, 0),
				new Color(0, 0, 0), 0.6, 0.65, false));
		gp.addSegment(new GradientSegment(new Color(255, 255, 255), new Color(
				255, 255, 255), 0.65, 0.7, false));
		gp.addSegment(new GradientSegment(new Color(0, 0, 0),
				new Color(0, 0, 0), 0.7, 0.75, false));
		gp.addSegment(new GradientSegment(new Color(255, 255, 255), new Color(
				255, 255, 255), 0.75, 0.8, false));
		gp.addSegment(new GradientSegment(new Color(0, 0, 0),
				new Color(0, 0, 0), 0.8, 0.85, false));
		gp.addSegment(new GradientSegment(new Color(255, 255, 255), new Color(
				255, 255, 255), 0.85, 0.9, false));
		gp.addSegment(new GradientSegment(new Color(0, 0, 0),
				new Color(0, 0, 0), 0.9, 0.95, false));
		gp.addSegment(new GradientSegment(new Color(255, 255, 255), new Color(
				255, 255, 255), 0.95, 1.0, false));
		gradients.add(gp);

		gp = new GradientPainter();
		gp.addSegment(new GradientSegment(new Color(120, 28, 23), new Color(
				224, 137, 0), 0.0, 0.5, false));
		gp.addSegment(new GradientSegment(new Color(224, 137, 0), new Color(
				224, 206, 0), 0.5, 1.0, false));
		gradients.add(gp);

		gp = new GradientPainter();
		gp.addSegment(new GradientSegment(new Color(55, 14, 77), new Color(128,
				9, 14), 0.0, 0.5, false));
		gp.addSegment(new GradientSegment(new Color(128, 9, 14), new Color(115,
				143, 79), 0.5, 1.0, false));
		gradients.add(gp);

		gp = new GradientPainter();
		gp.addSegment(new GradientSegment(new Color(109, 79, 143), new Color(
				143, 79, 79), 0.0, 0.33, false));
		gp.addSegment(new GradientSegment(new Color(143, 79, 79), new Color(
				122, 76, 47), 0.33, 0.66499996, false));
		gp.addSegment(new GradientSegment(new Color(122, 76, 47), new Color(
				142, 143, 79), 0.66499996, 1.0, false));
		gradients.add(gp);

		gradientArray = (GradientPainter[]) gradients
				.toArray(new GradientPainter[0]);

		gradientMaps = new int[gradientArray.length][];
		for (int i = 0; i < gradientArray.length; i++) {
			Image image = gradientArray[i].computeGradientImage(Toolkit
					.getDefaultToolkit(), new Rectangle(GRADIENT_WIDTH, 1),
					new Point(GRADIENT_WIDTH / 2, 0), GRADIENT_WIDTH, 0,
					GradientPainter.LINEAR_GRADIENT, Color.WHITE.getRGB());

			int[] gradientMap = new int[GRADIENT_WIDTH];
			PixelGrabber pg = new PixelGrabber(image, 0, 0, GRADIENT_WIDTH, 1,
					gradientMap, 0, 1);
			gradientMaps[i] = gradientMap;
			try {
				pg.grabPixels();
			} catch (InterruptedException e) {
				ExceptionManager.doError(
						"Fehler beim Erstellen der Gradientenkarten", e);
			}

		}// for
	}// statischer Konstruktor

	public MapGraph(
			IGraphSettings settings,
			DrawAreaInterface da,
			IVectorDoubleValueHolder bandVector,
			IScalarDoubleHolder contourDelta,
			IFunction2<IScalarDoubleHolder, IScalarDoubleHolder, IScalarDoubleHolder> evalFunction) {
		super(da,settings);
		this.bandVector = bandVector;
		this.contourDelta = contourDelta;
		this.evalFunction = evalFunction;
	} // Konstruktor

	public void paintGraph(Graphics gr) {
		final Graphics2D g = (Graphics2D) gr;
		if (!isVisible()) {
			if (mapThread != null)
				mapThread.makeStop();
		} else {
			if (!((da.getXMin() == xMin) && (da.getXMax() == xMax)
					&& (da.getYMin() == yMin) && (da.getYMax() == yMax)
					&& (da.getWidth() == width) && (da.getHeight() == height))) {
				validate = false;
			}// if

			// BandVector überprüfen
			double[] valArr = null;
			try {
				valArr = bandVector.getVector();
			} catch (Exception e) {
				ExceptionManager
						.doError(
								"Fehler beim Zeichnen der Farbkarte. Höhenlinien können nicht evaluiert werden.",
								e);
			}

			if (!validate) {
				// System.out.println("neu rechnen");
				xMin = da.getXMin();
				yMin = da.getYMin();
				xMax = da.getXMax();
				yMax = da.getYMax();
				height = da.getHeight();
				width = da.getWidth();
				double cx, cy, result = 0;

				// Neues Bild zum Cachen erzeugen
				if (bi == null || oldWidth != width || oldHeight != height) {
					bi = new BufferedImage(width, height,
							BufferedImage.TYPE_INT_ARGB);
					oldWidth = width;
					oldHeight = height;
				}// if

				if (mapThread != null) {
					try {
						mapThread.makeStop();
						mapThread.join();
					} catch (Exception e) {

					}
				}// if

				zMin = valArr[0];
				zMax = valArr[valArr.length - 1];
				zdelta = zMax - zMin;

				// Grobberechnung
				for (int x = 10; x < width; x += 20) {
					cx = MapGraph.this.da.xPixToCoord(x);
					for (int y = 10; y < height; y += 20) {
						cy = da.yPixToCoord(y);
						try {
							xPos.setScalar(cx, false);
							yPos.setScalar(cy, false);
						} catch (Exception e) {
							ExceptionManager.doError(e);
						}
						

						try {
							result = evalFunction.eval(xPos, yPos)
									.getScalarOrNan();
							int color = zCoordToColor(result, valArr);

							for (int xr = x - 10; xr < x + 10 && xr < width; xr++) {
								for (int yr = y - 10; yr < y + 10
										&& yr < height; yr++) {
									try {
										bi.setRGB(xr, yr, color);
									} catch (ArrayIndexOutOfBoundsException e) {
										// Bild-Dimension hat sich geändert
										return;
									}
								}// for yr
							}// for xr

							// Falls letzte Runde, negierten Streifen zeichnen
							if (y + 20 < width) {
								int c = ((~color) & 0xffffff) + 0xff000000;
								for (int xr = x - 10; xr < x + 10; xr++) {
									for (int yr = 0; yr < 5; yr++) {
										try {
											bi.setRGB(xr, height - 10 + yr, c);
										} catch (ArrayIndexOutOfBoundsException a) {
										}
									}
								}
							}// if
						} catch (Exception e) {
							ExceptionManager.doError(
									"Fehler beim Zeichnen der Farbkarte", e);
						}// catch
					} // for y
				} // for x

				g.drawImage(bi, null, 0, 0);

				if (da.inMouseAction()) {
					return;
				}

				mapThread = new MapThread(valArr, g, da);
				mapThread.setPriority(Thread.MIN_PRIORITY);
				mapThread.start();
				validate = true;
			} else {
				g.drawImage(bi, null, 0, 0);
			}// validate
		} // if visible
	} // paintGraph

	private int zCoordToColor(double z, double[] valueArray) {
		int i = (int) Math
				.round(((z - zMin) * (gradientMaps[getLineStyle()].length - 1.0))
						/ zdelta);

		if (zdelta <= 0)
			i = GRADIENT_WIDTH + 1;

		int v = 0;
		if (i >= 0 && i < gradientMaps[getLineStyle()].length) {
			v = gradientMaps[getLineStyle()][i];
		}// if

		int vr = (0xFF0000 & v) >> 16;
		int vg = (0x00FF00 & v) >> 8;
		int vb = 0x0000FF & v;

		for (int j = 1; j < valueArray.length - 1; j++) {
			double err = Math.abs(z - valueArray[j]);
			if (err < contourDelta.getScalarOrNan() * Math.abs(zdelta)) {
				// Falls genau die Höhenlinie getroffen wird, ist c=0
				// Im schlechtesten Fall ist c=1
				double c = (1.0 / (contourDelta.getScalarOrNan() * Math
						.abs(zdelta)) * err);
				int c2 = ((int) (c * vr) << 16) + ((int) (c * vg) << 8)
						+ (int) (c * vb);
				return c2 + (getColor().getAlpha() << 24);
			}// if
		}// for j

		if (i < 0)
			return BLACK_TRANSPARENT;
		if (i >= gradientMaps[getLineStyle()].length)
			return WHITE_TRANSPARENT;
		return gradientMaps[getLineStyle()][i] + (getColor().getAlpha() << 24);
	} // zCoordToColor

	public void setLineStyle(int style) {
		super.setLineStyle(style);
		validate = false;
	}// setLineStyle

	public void setColor(Color c) {
		super.setColor(c);
		validate = false;
	}// setColor

	class MapThread extends Thread {
		private double[] valArr;
		private Graphics2D g;
		private boolean makeSuicide = false;
		private DrawAreaInterface da;

		public MapThread(double[] valArr, Graphics2D g,
				DrawAreaInterface da) {
			super("MapGradiation");
			this.valArr = valArr;
			this.g = g;
			this.da = da;
		}// Konstruktor

		public void makeStop() {
			makeSuicide = true;
		}// makeStop

		public void run() {
			// Schlechte Auflösung zeichnen
			((JComponent) MapGraph.this.da).repaint();

			// Test: Soll gestoppt werden
			if (makeSuicide) {
				validate = false;
				return;
			}// if

			double cx, cy, result;
			for (int x = 0; x < width; x++) {
				cx = da.xPixToCoord(x);
				for (int y = 0; y < height; y++) {
					// Test: Soll gestoppt werden
					if (makeSuicide) {
						validate = false;
						return;
					}// if
					cy = da.yPixToCoord(y);
					try {
						xPos.setScalar(cx, false);
						yPos.setScalar(cy, false);
					} catch (Exception e) {
						ExceptionManager.doError(e);
					}

					try {
						result = evalFunction.eval(xPos, yPos).getScalarOrNan();
						int color = zCoordToColor(result, valArr);

						try {
							bi.setRGB(x, y, color);
						} catch (ArrayIndexOutOfBoundsException e) {
						}// catch
					} catch (Exception e) {
						ExceptionManager.doError(e);
					}// catch
				} // for y
				if (x % 30 == 0)
					((JComponent) MapGraph.this.da).repaint(x - 30, 0, x,
							height - 1);
			} // for x
				// Test: Soll gestoppt werden
			if (makeSuicide) {
				validate = false;
				return;
			}
			g.drawImage(bi, null, 0, 0);
			((JComponent) MapGraph.this.da).repaint();
		}// run
	}// class MapThread

} // MapGraph
