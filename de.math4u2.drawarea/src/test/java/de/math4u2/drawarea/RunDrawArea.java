package de.math4u2.drawarea;

import java.awt.Color;

import javax.swing.JFrame;

import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.IExceptionFrame;
import math4u2.view.graph.AbstractSimpleGraph;
import math4u2.view.graph.AngleGraph;
import math4u2.view.graph.ArrowGraph;
import math4u2.view.graph.BarGraph;
import math4u2.view.graph.BezierGraph;
import math4u2.view.graph.CircleGraph;
import math4u2.view.graph.CurveGraph;
import math4u2.view.graph.DefaultAreaGraphSettings;
import math4u2.view.graph.DefaultGraphSettings;
import math4u2.view.graph.DiscreteGraph;
import math4u2.view.graph.DiscreteSequenceGraph;
import math4u2.view.graph.FieldVectorGraph;
import math4u2.view.graph.FunctionGraph;
import math4u2.view.graph.MapGraph;
import math4u2.view.graph.MarkerGraph;
import math4u2.view.graph.PointGraph;
import math4u2.view.graph.SimpleGraphInterface;
import math4u2.view.graph.StraightGraph;
import math4u2.view.graph.StretchGraph;
import math4u2.view.graph.VectorElementGraph;
import math4u2.view.graph.drawarea.DrawArea;
import math4u2.view.graph.drawarea.DrawAreaChangeListener;
import math4u2.view.graph.util.IFunction1;
import math4u2.view.graph.util.IFunction2;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;
import math4u2.view.graph.util.SimpleScalarDoubleValueHolder;
import math4u2.view.graph.util.SimpleScalarStringValueHolder;
import math4u2.view.graph.util.SimpleVectorDoubleValueHolder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class RunDrawArea {

	@Before
	public void setup() {
		ExceptionManager.initExceptionFrame(new IExceptionFrame() {
			public void setVisible(boolean visible) {
			}

			public void newException(String title, String message) {
				System.err.println(title + ": " + message);
			}
		});
	}

	@Ignore
	@Test
	public void testStart() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		showFrame(frame);
	}

	@Ignore
	@Test
	public void testSimpleGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		SimpleScalarDoubleValueHolder x = new SimpleScalarDoubleValueHolder(1);
		SimpleScalarDoubleValueHolder y = new SimpleScalarDoubleValueHolder(1);
		SimpleGraphInterface sgi = new SimpleGraphPoint(x, y, da);
		sgi.setColor(Color.red);
		da.addGraph(sgi);

		showFrame(frame);
	}

	@Ignore
	@Test
	public void testMapGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		SimpleScalarDoubleValueHolder contourDelta = new SimpleScalarDoubleValueHolder(
				0.001);
		SimpleVectorDoubleValueHolder bandVector = new SimpleVectorDoubleValueHolder(
				new double[] { -0.8, 0, 0.8 });
		MapGraph graph = new MapGraph(
				new DefaultGraphSettings(),
				da,
				bandVector,
				contourDelta,
				new IFunction2<IScalarDoubleHolder, IScalarDoubleHolder, IScalarDoubleHolder>() {
					public IScalarDoubleHolder eval(IScalarDoubleHolder p1,
							IScalarDoubleHolder p2) throws Exception {
						double d = Math.sin(p1.getScalarOrNan())
								* Math.cos(p2.getScalarOrNan());
						return new SimpleScalarDoubleValueHolder(d);
					}

					public String getKey() {
						return "test";
					}
				});
		graph.setColor(Color.red);
		da.addGraph(graph);

		showFrame(frame);
	}

	@Ignore
	@Test
	public void testAngleGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);

		IVectorDoubleValueHolder v1 = new SimpleVectorDoubleValueHolder(
				new double[] { 1, -1 });
		IVectorDoubleValueHolder v2 = new SimpleVectorDoubleValueHolder(
				new double[] { 1, 2 });
		IScalarDoubleHolder x = new SimpleScalarDoubleValueHolder(0);
		IScalarDoubleHolder y = new SimpleScalarDoubleValueHolder(0);
		IScalarDoubleHolder r = new SimpleScalarDoubleValueHolder(2);
		SimpleGraphInterface graph = new AngleGraph(da,
				new DefaultAreaGraphSettings(), "angle", v1, v2, x, y, r);
		graph.setColor(Color.red);
		da.addGraph(graph);

		showFrame(frame);
	}

	@Ignore
	@Test
	public void testArrowGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		SimpleScalarDoubleValueHolder x = new SimpleScalarDoubleValueHolder(1);
		SimpleScalarDoubleValueHolder y = new SimpleScalarDoubleValueHolder(1);
		IVectorDoubleValueHolder v = new SimpleVectorDoubleValueHolder(
				new double[] { 1, -1 });
		SimpleGraphInterface graph = new ArrowGraph(da,
				new DefaultGraphSettings(), "arrow", x, y, v);
		graph.setColor(Color.red);
		da.addGraph(graph);

		graph = new ArrowGraph(da, new DefaultGraphSettings(), "arrow",
				new SimpleScalarDoubleValueHolder(0),
				new SimpleScalarDoubleValueHolder(0), v);
		graph.setColor(Color.green);
		da.addGraph(graph);

		showFrame(frame);
	}

	@Ignore
	@Test
	public void testBarGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		SimpleScalarDoubleValueHolder t = new SimpleScalarDoubleValueHolder(0.9);
		IVectorDoubleValueHolder vx = new SimpleVectorDoubleValueHolder(
				new double[] { 0, 1, 2, 3, 4 });
		IVectorDoubleValueHolder vy = new SimpleVectorDoubleValueHolder(
				new double[] { 1, 4, -2, 3, -4 });
		BarGraph graph = new BarGraph(da, new DefaultAreaGraphSettings(),
				"bar", vx, vy, t);
		graph.setFillColor(Color.green);
		graph.setBorderColor(Color.blue);
		da.addGraph(graph);

		showFrame(frame);
	}

	@Ignore
	@Test
	public void testBezierGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		SimpleScalarDoubleValueHolder t = new SimpleScalarDoubleValueHolder(0.9);
		BezierGraph graph = new BezierGraph(da, new DefaultAreaGraphSettings(),
				"bezier", new SimpleScalarDoubleValueHolder(0),
				new SimpleScalarDoubleValueHolder(0),
				new SimpleScalarDoubleValueHolder(1),
				new SimpleScalarDoubleValueHolder(10),
				new SimpleScalarDoubleValueHolder(4),
				new SimpleScalarDoubleValueHolder(0),
				new SimpleScalarDoubleValueHolder(1),
				new SimpleScalarDoubleValueHolder(0));
		graph.setColor(Color.green);
		da.addGraph(graph);

		showFrame(frame);
	}

	@Ignore
	@Test
	public void testCircleGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		CircleGraph graph = new CircleGraph(da, new DefaultAreaGraphSettings(),
				"circle", new SimpleScalarDoubleValueHolder(0),
				new SimpleScalarDoubleValueHolder(1),
				new SimpleScalarDoubleValueHolder(2));
		graph.setFillColor(Color.green);
		graph.setBorderColor(Color.blue);
		da.addGraph(graph);

		showFrame(frame);
	}

	@Ignore
	@Test
	public void testCurveGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		IScalarDoubleHolder minT = new SimpleScalarDoubleValueHolder(-5);
		IScalarDoubleHolder maxT = new SimpleScalarDoubleValueHolder(20);
		IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> xFunction = new IFunction1<IScalarDoubleHolder, IScalarDoubleHolder>() {
			public String getKey() {
				return "x";
			}

			public IScalarDoubleHolder eval(IScalarDoubleHolder p1)
					throws Exception {
				double d = Math.cos(p1.getScalarOrNan())
						* Math.cos(p1.getScalarOrNan());
				return new SimpleScalarDoubleValueHolder(d);
			}
		};
		IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> yFunction = new IFunction1<IScalarDoubleHolder, IScalarDoubleHolder>() {
			public String getKey() {
				return "y";
			}

			public IScalarDoubleHolder eval(IScalarDoubleHolder p1)
					throws Exception {
				double d = p1.getScalarOrNan() * Math.cos(p1.getScalarOrNan());
				return new SimpleScalarDoubleValueHolder(d);
			}
		};
		CurveGraph graph = new CurveGraph(da, new DefaultGraphSettings(),
				"cure", minT, maxT, xFunction, yFunction);
		graph.setColor(Color.blue);
		da.addGraph(graph);

		showFrame(frame);
	}

	@Ignore
	@Test
	public void testDiscreteGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		IVectorDoubleValueHolder xVector = new SimpleVectorDoubleValueHolder(
				new double[] { 1, 1, 2, 3, 4 });
		IVectorDoubleValueHolder yVector = new SimpleVectorDoubleValueHolder(
				new double[] { 1, 2, 1, 4, 3 });
		IScalarDoubleHolder radius = new SimpleScalarDoubleValueHolder(4);
		DiscreteGraph graph = new DiscreteGraph(da,
				new DefaultAreaGraphSettings(), "discrete", xVector, yVector,
				radius);
		graph.setFillColor(Color.green);
		graph.setBorderColor(Color.blue);
		da.addGraph(graph);

		showFrame(frame);
	}

	@Ignore
	@Test
	public void testDiscreteSequenceGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);

		IScalarStringHolder layout = new SimpleScalarStringValueHolder("SP");
		IVectorDoubleValueHolder xVector = new SimpleVectorDoubleValueHolder(
				new double[] { 1, 1, 2, 3, 4 });
		IVectorDoubleValueHolder yVector = new SimpleVectorDoubleValueHolder(
				new double[] { 1, 2, 1, 4, 3 });
		IScalarDoubleHolder radius = new SimpleScalarDoubleValueHolder(6);
		DiscreteSequenceGraph graph = new DiscreteSequenceGraph(da,
				new DefaultAreaGraphSettings(), "discrete", layout, xVector,
				yVector, radius);
		graph.setFillColor(Color.green);
		graph.setBorderColor(Color.blue);
		da.addGraph(graph);

		showFrame(frame);
	}

	@Ignore
	@Test
	public void testFieldVectorGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		IScalarStringHolder layout = new SimpleScalarStringValueHolder("VN");
		IScalarDoubleHolder refX = new SimpleScalarDoubleValueHolder(0);
		IScalarDoubleHolder refY = new SimpleScalarDoubleValueHolder(0);
		IScalarDoubleHolder distX = new SimpleScalarDoubleValueHolder(0.5);
		IScalarDoubleHolder distY = new SimpleScalarDoubleValueHolder(0.5);
		IScalarDoubleHolder cutoff = new SimpleScalarDoubleValueHolder(5);
		IFunction2<IVectorDoubleValueHolder, IScalarDoubleHolder, IScalarDoubleHolder> fieldFunction = new IFunction2<IVectorDoubleValueHolder, IScalarDoubleHolder, IScalarDoubleHolder>() {

			public String getKey() {
				return "evalFunc";
			}

			public IVectorDoubleValueHolder eval(IScalarDoubleHolder p1,
					IScalarDoubleHolder p2) throws Exception {
				double[] r = new double[] { p1.getScalar() + p2.getScalar(),
						Math.sin(p1.getScalar() * 4) };
				return new SimpleVectorDoubleValueHolder(r);
			}
		};

		FieldVectorGraph graph = new FieldVectorGraph(da,
				new DefaultGraphSettings(), "fieldVector", layout, refX, refY,
				distX, distY, cutoff, fieldFunction);
		graph.setColor(Color.blue);
		da.addGraph(graph);

		showFrame(frame);
	}

	@Ignore
	@Test
	public void testFunctionGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> func = new IFunction1<IScalarDoubleHolder, IScalarDoubleHolder>() {
			public IScalarDoubleHolder eval(IScalarDoubleHolder p1)
					throws Exception {
				double d = Math.sin(p1.getScalarOrNan());
				return new SimpleScalarDoubleValueHolder(d);
			}

			public String getKey() {
				return "f";
			}
		};

		FunctionGraph graph = new FunctionGraph(da, new DefaultGraphSettings(),
				func);
		graph.setColor(Color.blue);
		da.addGraph(graph);

		showFrame(frame);
	}

	@Ignore
	@Test
	public void testMarkerGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		IScalarStringHolder layout = new SimpleScalarStringValueHolder(
				"circle_cross");
		IScalarDoubleHolder xPos = new SimpleScalarDoubleValueHolder(1);
		IScalarDoubleHolder yPos = new SimpleScalarDoubleValueHolder(1);
		MarkerGraph graph = new MarkerGraph(da, new DefaultGraphSettings(),
				"marker", layout, xPos, yPos);
		graph.setColor(Color.blue);
		da.addGraph(graph);

		showFrame(frame);
	}
	
	 @Ignore
	@Test
	public void testStraightGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		IVectorDoubleValueHolder vec = new SimpleVectorDoubleValueHolder(new double[]{-1,2});
		IVectorDoubleValueHolder point = new SimpleVectorDoubleValueHolder(new double[]{1,2});
		IScalarStringHolder name = new SimpleScalarStringValueHolder("straight");
		AbstractSimpleGraph graph = new StraightGraph(da, new DefaultGraphSettings(), name, point, vec);
		graph.setColor(Color.blue);
		da.addGraph(graph);
		showFrame(frame);
	}
	 
	 @Ignore
	@Test
	public void testStretchGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		IVectorDoubleValueHolder start = new SimpleVectorDoubleValueHolder(new double[]{-1,2});
		IVectorDoubleValueHolder end = new SimpleVectorDoubleValueHolder(new double[]{1,3});
		IScalarStringHolder name = new SimpleScalarStringValueHolder("stretch");
		AbstractSimpleGraph graph = new StretchGraph(da, new DefaultGraphSettings(), name, start, end);
		graph.setColor(Color.blue);
		da.addGraph(graph);
		showFrame(frame);
	}
	
	 @Ignore
	@Test
	public void testVectorGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		IVectorDoubleValueHolder vec = new SimpleVectorDoubleValueHolder(new double[]{-1,2,5,3});
		IScalarStringHolder name = new SimpleScalarStringValueHolder("vec");
		AbstractSimpleGraph graph = new VectorElementGraph(da, new DefaultGraphSettings(), name, vec);
		graph.setColor(Color.blue);
		da.addGraph(graph);
		showFrame(frame);
	}

//	 @Ignore
	@Test
	public void testPointGraph() throws InterruptedException {
		JFrame frame = createFrame();
		final DrawArea da = createDrawArea();
		frame.getContentPane().add(da);
		IScalarStringHolder layout = new SimpleScalarStringValueHolder(
				"CIRCLE_DOT");
		IScalarDoubleHolder xPos = new SimpleScalarDoubleValueHolder(1);
		IScalarDoubleHolder yPos = new SimpleScalarDoubleValueHolder(1);
		IScalarStringHolder name = new SimpleScalarStringValueHolder("P");
		IScalarStringHolder index = new SimpleScalarStringValueHolder("12");
		AbstractSimpleGraph graph = new PointGraph(da, new DefaultGraphSettings(), layout, xPos, yPos, name, index);
		graph.setColor(Color.blue);
		da.addGraph(graph);
		
		showFrame(frame);
		da.graphHasChanged();
	}

	private JFrame createFrame() {
		JFrame frame = new JFrame("Zeichenfläche");
		frame.setSize(800, 600);
		return frame;
	}

	private DrawArea createDrawArea() {
		final DrawArea da = new DrawArea("test");
		da.addChangeListener(new DrawAreaChangeListener() {
			public void drawAreaChanged() {
				da.graphHasChanged();
			}
		});
		return da;
	}

	private void showFrame(JFrame frame) throws InterruptedException {
		frame.setVisible(true);
		Thread.sleep(20000);
	}

}
