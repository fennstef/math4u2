package math4u2.view.gui.listview;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import math4u2.controller.Broker;
import math4u2.mathematics.affine.AffPoint;
import math4u2.mathematics.affine.Angle;
import math4u2.mathematics.affine.Arrow;
import math4u2.mathematics.affine.Bar;
import math4u2.mathematics.affine.Bezier;
import math4u2.mathematics.affine.Circle;
import math4u2.mathematics.affine.Curve;
import math4u2.mathematics.affine.Discrete;
import math4u2.mathematics.affine.DiscreteSequence;
import math4u2.mathematics.affine.Field;
import math4u2.mathematics.affine.Map;
import math4u2.mathematics.affine.Marker;
import math4u2.mathematics.affine.Straight;
import math4u2.mathematics.affine.Stretch;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserDefinedFunction;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.graph.AngleGraph;
import math4u2.view.graph.AreaGraph;
import math4u2.view.graph.ArrowGraph;
import math4u2.view.graph.BarGraph;
import math4u2.view.graph.BezierGraph;
import math4u2.view.graph.CircleGraph;
import math4u2.view.graph.CurveGraph;
import math4u2.view.graph.DiscreteGraph;
import math4u2.view.graph.DiscreteSequenceGraph;
import math4u2.view.graph.FieldVectorGraph;
import math4u2.view.graph.FunctionGraph;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.GraphInterfaceFactory;
import math4u2.view.graph.ListGraph;
import math4u2.view.graph.MapGraph;
import math4u2.view.graph.MarkerGraph;
import math4u2.view.graph.PointGraph;
import math4u2.view.graph.SimpleGraph;
import math4u2.view.graph.SimpleGraphInterface;
import math4u2.view.graph.StraightGraph;
import math4u2.view.graph.StretchGraph;
import math4u2.view.graph.TextBubbleGraph;
import math4u2.view.graph.VectorElementGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.FixedScalarDoubleValueHolder;
import math4u2.view.graph.util.FixedScalarStringValueHolder;
import math4u2.view.graph.util.FixedVectorDoubleValueHolder;
import math4u2.view.graph.util.IFunction1;
import math4u2.view.graph.util.IFunction2;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;
import math4u2.view.graph.util.SimpleScalarStringValueHolder;

public class GraphFactory implements GraphInterfaceFactory{
	public GraphInterface createCircleGraph(DrawAreaInterface da, UserFunction f) {
		final GraphWrapper<Circle> wrapper = new GraphWrapper<Circle>(f, da);
		IScalarDoubleHolder x = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getCenter().getX();
			}
		};
		IScalarDoubleHolder y = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getCenter().getY();
			}
		};
		IScalarDoubleHolder r = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getRadius();
			}
		};
		SimpleGraphInterface sGraph = new CircleGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), x, y, r);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}
	
	public GraphInterface createPointGraph(DrawAreaInterface da,
			UserFunction f, Broker broker) {
		final GraphWrapper<AffPoint> wrapper = new GraphWrapper<AffPoint>(f, da);
		IScalarDoubleHolder x = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getX();
			}
		};
		IScalarDoubleHolder y = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getY();
			}
		};
		IScalarStringHolder layout = new FixedScalarStringValueHolder() {
			@Override
			public String getScalarOrNull() {
				return wrapper.getAffineObject().getStyle();
			}

			@Override
			public void setScalar(String value, boolean propagateChange)
					throws Exception {
				wrapper.getAffineObject().setStyle(value);
			}
		};
		IScalarStringHolder index = new SimpleScalarStringValueHolder("");
		SimpleGraphInterface sGraph = new PointGraph(da, wrapper.getAreaSettings(), layout, x, y, wrapper.getNameHolder(), index);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createAngleGraph(DrawAreaInterface da, UserFunction f) {
		final GraphWrapper<Angle> wrapper = new GraphWrapper<Angle>(f, da);
		IScalarDoubleHolder x = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getApex().getX();
			}
		};
		IScalarDoubleHolder y = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getApex().getY();
			}
		};
		IScalarDoubleHolder radius = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getRadius();
			}
		};
		IVectorDoubleValueHolder vector1 = new FunctionVectorDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getVectors()[0];
			}
		};
		IVectorDoubleValueHolder vector2 = new FunctionVectorDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getVectors()[1];
			}
		};
		SimpleGraphInterface sGraph = new AngleGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), vector1, vector2, x, y, radius);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}
	
	public GraphInterface createArrowGraph(DrawAreaInterface da,
			UserFunction f, Broker broker) {
		final GraphWrapper<Arrow> wrapper = new GraphWrapper<Arrow>(f, da);
		IScalarDoubleHolder x = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getStart().getX();
			}
		};
		IScalarDoubleHolder y = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getStart().getY();
			}
		};		
		IVectorDoubleValueHolder vector = new FunctionVectorDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getVectorFunction();
			}
		};		
		SimpleGraphInterface sGraph = new ArrowGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), x, y, vector);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}
	
	public GraphInterface createAreaGraph(DrawAreaInterface da, UserFunction f) {
		return new AreaGraph(da, f);
	}

	public GraphInterface createBarGraph(DrawAreaInterface da, UserFunction f) {
		final GraphWrapper<Bar> wrapper = new GraphWrapper<Bar>(f, da);		
		IVectorDoubleValueHolder xVector = new FunctionVectorDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getXVector();
			}
		};
		IVectorDoubleValueHolder yVector = new FunctionVectorDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getYVector();
			}
		};
		IScalarDoubleHolder thickness = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getThickness();
			}
		};
		SimpleGraphInterface sGraph = new BarGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), xVector, yVector, thickness);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createBezierGraph(DrawAreaInterface da, UserFunction f) {
		final GraphWrapper<Bezier> wrapper = new GraphWrapper<Bezier>(f, da);
		IScalarDoubleHolder startPointX = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getStartPoint().getX();
			}
		};
		IScalarDoubleHolder startPointY = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getStartPoint().getY();
			}
		};
		IScalarDoubleHolder startDirX = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getStartDirPoint().getX();
			}
		};
		IScalarDoubleHolder startDirY = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getStartDirPoint().getY();
			}
		};
		IScalarDoubleHolder endPointX = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getEndPoint().getX();
			}
		};
		IScalarDoubleHolder endPointY = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getEndPoint().getY();
			}
		};
		IScalarDoubleHolder endDirX = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getEndDirPoint().getX();
			}
		};
		IScalarDoubleHolder endDirY = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getEndDirPoint().getY();
			}
		};
		SimpleGraphInterface sGraph = new BezierGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), startPointX, startPointY, startDirX, startDirY, endPointX, endPointY, endDirX, endDirY);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createCurveGraph(DrawAreaInterface da, UserFunction f) {
		final GraphWrapper<Curve> wrapper = new GraphWrapper<Curve>(f, da);
		IScalarDoubleHolder minT = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getMinFunction();
			}
		};
		IScalarDoubleHolder maxT = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getMaxFunction();
			}
		};
		IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> xFunction = new FunctionFunctionS2S(){
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getXFunction();
			}
		};
		IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> yFunction = new FunctionFunctionS2S(){
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getYFunction();
			}
		};
		SimpleGraphInterface sGraph = new CurveGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), minT, maxT, xFunction, yFunction);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createDiscreteGraph(DrawAreaInterface da,
			UserFunction f) {
		final GraphWrapper<Discrete> wrapper = new GraphWrapper<Discrete>(f, da);
		IScalarDoubleHolder radius = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getRadiusFunction();
			}
		};		
		IVectorDoubleValueHolder xVector = new FunctionVectorDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getXVector();
			}
		};
		IVectorDoubleValueHolder yVector = new FunctionVectorDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getYVector();
			}
		};
		SimpleGraphInterface sGraph = new DiscreteGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), xVector, yVector, radius);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createDiscreteSequenceGraph(DrawAreaInterface da,
			UserFunction f) {
		final GraphWrapper<DiscreteSequence> wrapper = new GraphWrapper<DiscreteSequence>(f, da);
		IScalarStringHolder layout = new FixedScalarStringValueHolder() {
			@Override
			public String getScalarOrNull() {
				return wrapper.getAffineObject().getLayout();
			}
		};
		IScalarDoubleHolder radius = new FunctionScalarDoubleHolder(){
			@Override
			public Function getFunction() throws Exception{
				return wrapper.getAffineObject().getRadiusFunction();
			}
		};		
		IVectorDoubleValueHolder xVector = new FunctionVectorDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getXVectorFunction();
			}
		};
		IVectorDoubleValueHolder yVector = new FunctionVectorDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getYVectorFunction();
			}
		};
		SimpleGraphInterface sGraph = new DiscreteSequenceGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), layout, xVector, yVector, radius);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createFieldVectorGraph(DrawAreaInterface da,
			UserFunction f) {
		final GraphWrapper<Field> wrapper = new GraphWrapper<Field>(f, da);
		IScalarStringHolder layout = new FixedScalarStringValueHolder() {
			@Override
			public String getScalarOrNull() {
				return wrapper.getAffineObject().getLayout();
			}
		};
		IScalarDoubleHolder refX = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				Function f = wrapper.getAffineObject().getRefPoint();
				AffPoint point = (AffPoint) f.eval();
				return point.getX();
			}
		};
		IScalarDoubleHolder refY = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				Function f = wrapper.getAffineObject().getRefPoint();
				AffPoint point = (AffPoint) f.eval();
				return point.getY();
			}
		};
		IScalarDoubleHolder distX = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getXDistFunc();
			}
		};
		IScalarDoubleHolder distY = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getYDistFunc();
			}
		};
		IScalarDoubleHolder cutoff = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getCutoffFunc();
			}
		};
		IFunction2<IVectorDoubleValueHolder, IScalarDoubleHolder, IScalarDoubleHolder> fieldFunction = new UserFunctionFunctionSS2V() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getFieldFunc();
			}
		};
		SimpleGraphInterface sGraph = new FieldVectorGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), layout, refX, refY, distX, distY, cutoff, fieldFunction);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createMapGraph(DrawAreaInterface da, UserFunction f) {
		final GraphWrapper<Map> wrapper = new GraphWrapper<Map>(f, da);
		IVectorDoubleValueHolder bandVector = new FunctionVectorDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getBandVecor();
			}
		};
		IScalarDoubleHolder contourDelta = new FixedScalarDoubleValueHolder() {
			@Override
			public double getScalar() throws Exception {
				return wrapper.getAffineObject().getContourDelta();
			}
		};
		IFunction2<IScalarDoubleHolder, IScalarDoubleHolder, IScalarDoubleHolder> evalFunction = new FunctionFunctionSS2S() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getMapFunc();
			}
		};
		SimpleGraphInterface sGraph = new MapGraph(wrapper.getAreaSettings(), da, bandVector, contourDelta, evalFunction);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createMarkerGraph(DrawAreaInterface da, UserFunction f) {
		final GraphWrapper<Marker> wrapper = new GraphWrapper<Marker>(f, da);
		IScalarStringHolder layout = new FixedScalarStringValueHolder() {
			@Override
			public String getScalarOrNull() {
				return wrapper.getAffineObject().getStyle();
			}
		};
		IScalarDoubleHolder xPos = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getX();
			}
		};
		IScalarDoubleHolder yPos = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getY();
			}
		};
		SimpleGraphInterface sGraph = new MarkerGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), layout, xPos, yPos);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createStraightGraph(DrawAreaInterface da,
			UserFunction f) {
		final GraphWrapper<Straight> wrapper = new GraphWrapper<Straight>(f, da);
		IVectorDoubleValueHolder point = new FixedVectorDoubleValueHolder() {
			@Override
			public double[] getVector() throws Exception {
				AffPoint p = wrapper.getAffineObject().getStartPoint();
				return new double[]{p.evalX(), p.evalY()};
			}
		};
		IVectorDoubleValueHolder vector = new FunctionVectorDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getAffineObject().getVector();
			}
		};
		SimpleGraphInterface sGraph = new StraightGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), point, vector);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createStretchGraph(DrawAreaInterface da,
			UserFunction f) {
		final GraphWrapper<Stretch> wrapper = new GraphWrapper<Stretch>(f, da);
		IVectorDoubleValueHolder start = new FixedVectorDoubleValueHolder() {
			@Override
			public double[] getVector() throws Exception {				
				AffPoint p = wrapper.getAffineObject().getStartPoint();
				return new double[]{p.evalX(), p.evalY()};
			}
		};
		IVectorDoubleValueHolder end = new FixedVectorDoubleValueHolder() {
			@Override
			public double[] getVector() throws Exception {				
				AffPoint p = wrapper.getAffineObject().getEndPoint();
				return new double[]{p.evalX(), p.evalY()};
			}
		};
		SimpleGraphInterface sGraph = new StretchGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), start, end);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createListGraph(DrawAreaInterface da, UserFunction f) {
		return new ListGraph(da, f);
	}

	public GraphInterface createVectorElementGraph(DrawAreaInterface da,
			UserFunction f) {
		final GraphWrapper<UserFunction> wrapper = new GraphWrapper<UserFunction>(f, da);
		IVectorDoubleValueHolder vector = new FunctionVectorDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getModel();
			}
		};
		SimpleGraphInterface sGraph = new VectorElementGraph(da, wrapper.getAreaSettings(), wrapper.getNameHolder(), vector);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createFunctionGraph(DrawAreaInterface da,
			UserFunction f) {
		final GraphWrapper<UserFunction> wrapper = new GraphWrapper<UserFunction>(f, da);
		IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> evalFunction = new FunctionFunctionS2S() {
			@Override
			public Function getFunction() throws Exception {
				return wrapper.getModel();
			}
		};
		SimpleGraphInterface sGraph = new FunctionGraph(da, wrapper.getAreaSettings(), evalFunction);
		wrapper.setSimpleGraph(sGraph);
		return wrapper;
	}

	public GraphInterface createSimpleGraph(DrawAreaInterface da, UserDefinedFunction f, UserFunction uf) {
		return new SimpleGraph(da, f, uf);
	}
	public GraphInterface createBubbleGraph(DrawAreaInterface da,
			UserFunction f, Broker broker) {
		return new TextBubbleGraph(da, f, broker);
	}
	
	public void appendShape(GeneralPath gp, GraphInterface graph) {
		Shape s;
		try {
			SimpleGraphInterface sGraph = ((GraphWrapper)graph).getInternal();
			s = ((CurveGraph)sGraph).computeShape();
			if (s == null)
				throw new MathException("Shape konnte nicht erstellt werden");
			gp.append(s, true);
		} catch (Exception e) {
			throw new RuntimeException("Shape konnte nicht erstellt werden",e);
		}
	}
}
