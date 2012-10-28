package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import math4u2.controller.MathObject;
import math4u2.controller.reference.EvalPathStep;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserDefinedFunction;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;

public class SimpleGraph extends AbstractGraph {
	private UserDefinedFunction f;
	private UserFunction uf;

	public SimpleGraph(DrawAreaInterface da, UserDefinedFunction f, UserFunction uf) {
		super(da);
		this.f = f;
		this.uf = uf;
	}

	public HasGraph getModel() {
		return f;
	}

	public void paintGraph(Graphics gr) {
		paintGraph(gr, da);
	}

	public void paintGraph(Graphics gr, DrawAreaInterface da) {
		if (!isVisible())
			return;
		Color ca = gr.getColor();
		Graphics2D g = (Graphics2D) gr;
		Stroke strokeOld = g.getStroke();
		g.setColor(getColor());
		g.setStroke(AbstractGraph.getStroke(da.getStroke(), getLineStyle()));
		
		
		paint(g, da);

		g.setColor(ca);
		g.setStroke(strokeOld);
	}

	public void paint(Graphics2D g, DrawAreaInterface da) {
		f.paint(g, da);
	}

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (f == oldObject)
			f = (UserDefinedFunction) newObject;
		f.evalFunction();
		if(uf==oldObject){
			uf = (UserFunction) newObject;
		}
	}

	@Override
	public void renew(MathObject source) {
		super.renew(source);
		f.evalFunction();
	}
}