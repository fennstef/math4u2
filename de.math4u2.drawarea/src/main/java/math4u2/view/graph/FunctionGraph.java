package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.FixedScalarStringValueHolder;
import math4u2.view.graph.util.IFunction1;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.SimpleScalarDoubleValueHolder;

/**
 * Graphische Darstellung einer Funktion.
 * 
 *
 * @author Fenn Stefan
 *
 * Problem mit bad path rasterize : http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4755500
 */

public class FunctionGraph extends AbstractSimpleGraph{
	private static final int P0_X=0, P0_Y=1, P1_X=2, P1_Y=3, P2_X=4, P2_Y=5;

	private int detailOld;

	private boolean validate = false;

	private double[] d;

	private double xMin, xMax, yMin, yMax, width;

	private int[] history = new int[6];
	private int historySize = 0;

	private GeneralPath gp = new GeneralPath();
	
	private IScalarDoubleHolder sdr = new SimpleScalarDoubleValueHolder(Double.NaN);
	private IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> evalFunction;

	public FunctionGraph(DrawAreaInterface da, IGraphSettings settings, IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> evalFunction) {
		super(da, settings, createNameFromKey(evalFunction));
		this.evalFunction = evalFunction;
	} //Konstruktor

	private static IScalarStringHolder createNameFromKey(
			final  IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> evalFunction) {
		return new FixedScalarStringValueHolder() {
			@Override
			public String getScalarOrNull() {
				return evalFunction.getKey();
			}
		};
	}

	public void paintGraph(Graphics gr) {
		if (isVisible()) {
			Graphics2D g = (Graphics2D) gr;

			int detail = da.getDetail();
			if (detail != detailOld)
				validate = false;

			Color ca = g.getColor();
			g.setStroke(getStroke());
			g.setColor(getColor());

			int x1 = da.xCoordToPix(da.getXMin());
			int x2 = da.xCoordToPix(da.getXMax());

			if (!((da.getXMin() == xMin) && (da.getXMax() == xMax)
					&& (da.getYMin() == yMin) && (da.getYMax() == yMax) && (da
					.getWidth() == width)))
				validate = false;
			if (!validate) {
				width = da.getWidth();
				xMin = da.getXMin();
				xMax = da.getXMax();
				yMin = da.getYMin();
				yMax = da.getYMax();
				if(d==null || d.length!=x2 + detail + 1)
				d = new double[x2 + detail + 1];
				int x = x1;				
				
				int exceptionCounter = 0;				
				try {
					sdr.setScalar(da.xPixToCoord(x), false);
					d[0] = evalFunction.eval(sdr).getScalarOrNan();
				} catch (Throwable e) {
					ExceptionManager.doError("Fehler beim Zeichnen des Funktions-Graphen "+getIdentifier(),e);
					exceptionCounter++;
				}//catch
				x += detail;
				for (int i = 1; x <= x2 + detail; i++) {
					try {
						sdr.setScalar(da.xPixToCoord(x),false);
						d[i] = evalFunction.eval(sdr).getScalarOrNan(); 
					} catch (Throwable e) {
						d[i] = Double.NaN;
						if(exceptionCounter==0){
							exceptionCounter++;
							ExceptionManager.doError("Fehler beim Zeichnen des Funktions-Graphen "+getIdentifier(),e);
						}
					}//catch
					if (d[i] < yMin)
						d[i] = Double.NEGATIVE_INFINITY;
					if (d[i] > yMax)
						d[i] = Double.POSITIVE_INFINITY;
					x += detail;
				} //for x
				validate = true;
			} //not validate

			//Paint Function
			gp.reset();
			int x = x1;
			historySize=0;
			gp.moveTo(0, 0);
			boolean moveFirst = true;
			for (int i = 0; x <= x2 + detail; i++) {
				if (Double.isNaN(d[i])) {
					_paintRest(history, gp, moveFirst);
					moveFirst = true;
				} //if Not A Number
				else if (Double.isInfinite(d[i])) {
					if (d[i] > 0){
						addHistory(x, da.yCoordToPix(yMax));
					}if (d[i] < 0){
						addHistory(x, da.yCoordToPix(yMin));
					}
					_paintRest(history, gp, moveFirst);
					moveFirst = true;
				} else {
					int y2 = da.yCoordToPix(d[i]);
					if (i > 0) {
						if (Double.isInfinite(d[i - 1])) {
							if (d[i - 1] > 0)
								addHistory(x, da.yCoordToPix(yMax));
							else
								addHistory(x, da.yCoordToPix(yMin));
						} //if Infinite
					} //if i>0
					addHistory(x, y2);
				} //else

				if (historySize == 3) {
					int p1 = _paintQuadCurve(history, gp, moveFirst);
					addHistory(history[p1],history[p1+1]);
					moveFirst = false;
				} //if

				x += detail;
			} //for
			_paintRest(history, gp, moveFirst);
			g.draw(gp);
			g.setColor(ca);
			xMin = da.getXMin();
			xMax = da.getXMax();
			width = da.getWidth();
			detailOld = detail;
			// deaktivieren
		} //if visible
	} //paintGraph
	
	private void addHistory(int x, int y){
		history[historySize*2]=x;
		history[historySize*2+1]=limitRangeY(y);		
		historySize++;
	}//addHistory

	/** Zeichnet den Rest in History */
	private void _paintRest(int[] history, GeneralPath gp, boolean moveFirst) {
		if (historySize == 3)
			_paintQuadCurve(history, gp, moveFirst);
		else if (historySize == 2)
			_paintLine(history, gp, moveFirst);
		else if (historySize == 1) {
			if (moveFirst) {
				gp.moveTo(history[P0_X], history[P0_Y]);
			} //if
			historySize=0;
		} //else
	} //_paintRest

	/**
	 * Hilfsmethode um eine Quadratische Kurve zu zeichnen
	 * 
	 * @return letzter gezeichneter Punkt
	 */
	private int _paintQuadCurve(int[] history, GeneralPath gp,
			boolean moveFirst) {
		if (moveFirst)
			gp.moveTo(history[P0_X], history[P0_Y]);
		gp.quadTo(history[P1_X], history[P1_Y], history[P2_X], history[P2_Y]);
		historySize=0;
		return P2_X;
	}

	private int _paintLine(int[] history, GeneralPath gp, boolean moveFirst) {
		if (moveFirst)
			gp.moveTo(history[P0_X], history[P0_Y]);
		gp.lineTo(history[P1_X], history[P1_Y ]);
		historySize=0;
		return history[P1_X];
	}

	public void detach() throws Exception {
	}

	@Override
	public void renew() {
		validate=false;
	} 
} //FunctionGraph
