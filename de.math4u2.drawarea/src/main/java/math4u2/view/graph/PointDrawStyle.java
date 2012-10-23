package math4u2.view.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import math4u2.view.formula.FormulaRenderContext;
import math4u2.view.graph.util.IScalarStringHolder;

public enum PointDrawStyle {
	CIRCLE_CROSS {
		@Override
		public void paint(Color color, Graphics2D g, int x, int y, int size, IScalarStringHolder name, IScalarStringHolder index) {
			// Name schreiben
			g.setColor(color);
			paintName(g, 0, 18, x, y, size, name, index);

			// Äußerer Kreis
			int radius = (int) (size * 0.30f);
			g.setStroke(THICK_STROKE);
			g.drawOval(x + size / 2 - radius, y + size / 2 - radius, radius * 2,
					radius * 2);

			// Innerer Kreis
			float radius2 = size * 0.15f;
			g.setStroke(THICK_STROKE);
			g.fillOval((int) (x + size / 2 - radius2 + 1), (int) (y + size / 2
					- radius2 + 1), (int) (radius2 * 2), (int) (radius2 * 2));

			// Beide Striche
			g.setStroke(MEDIUM_STROKE);
			g.drawLine(x + size / 2, y, x + size / 2, y + size);
			g.drawLine(x, y + size / 2, x + size, y + size / 2);
		}
	}, CIRCLE_DOT {
		@Override
		public void  paint(Color color, Graphics2D g, int x, int y, int size, IScalarStringHolder name, IScalarStringHolder index) {
			// Name schreiben
			g.setColor(color);
			paintName(g, 0, 18, x, y, size, name, index);

			// Äußerer Kreis
			int radius = (int) (size * 0.30f);
			g.setStroke(THICK_STROKE);
			g.drawOval(x + size / 2 - radius, y + size / 2 - radius, radius * 2,
					radius * 2);

			// Innerer Kreis
			float radius2 = size * 0.15f;
			g.setStroke(THICK_STROKE);
			g.fillOval((int) (x + size / 2 - radius2 + 1), (int) (y + size / 2
					- radius2 + 1), (int) (radius2 * 2), (int) (radius2 * 2));
			
		}
	}, ARROW_H {
		@Override
		public void  paint(Color color, Graphics2D g, int x, int y, int size, IScalarStringHolder name, IScalarStringHolder index) {			
			//NameSchreiben
			g.setColor(color);
			paintName(g,0,25, x, y, size, name, index);
			
			g.setStroke(THICK_STROKE);
			//Querbalken
			g.drawLine(x + size/2, y, x + size/2, y + size);
			
			g.setStroke(MEDIUM_STROKE);
			//Pfeil rechts
			float sizeF = size;
			GeneralPath gp = getArrowPath(x,y,sizeF);
			gp.transform(AffineTransform.getTranslateInstance(-x-sizeF/2,-y-sizeF/2));
			gp.transform(AffineTransform.getRotateInstance(Math.PI/2));
			gp.transform(AffineTransform.getTranslateInstance(x+sizeF/2,y+sizeF/2));
			g.draw(gp);
			
			//Pfeil links
			gp.transform(AffineTransform.getTranslateInstance(-x-sizeF/2,-y-sizeF/2));
			gp.transform(AffineTransform.getRotateInstance(Math.PI));
			gp.transform(AffineTransform.getTranslateInstance(x+sizeF/2,y+sizeF/2));
			g.draw(gp);
		}
	}, ARROW_V {
		@Override
		public void  paint(Color color, Graphics2D g, int x, int y, int size, IScalarStringHolder name, IScalarStringHolder index) {
			// NameSchreiben
			g.setColor(color);
			paintName(g, 0, 18, x, y, size, name, index);

			g.setStroke(THICK_STROKE);
			// Querbalken
			g.drawLine(x, y + size / 2, x + size, y + size / 2);

			g.setStroke(MEDIUM_STROKE);
			// Pfeil oben
			float sizef = size;
			GeneralPath gp = getArrowPath(x,y,sizef);
			g.draw(gp);
			// Pfeil unten
			gp.transform(AffineTransform.getTranslateInstance(-x - sizef / 2, -y
					- sizef / 2));
			gp.transform(AffineTransform.getRotateInstance(Math.PI));
			gp.transform(AffineTransform.getTranslateInstance(x + sizef / 2, y
					+ sizef / 2));
			g.draw(gp);
		}
	};
	
	/** Strichstärke */
	private static final Stroke THICK_STROKE = new BasicStroke(2.5f);	
	private static final Stroke MEDIUM_STROKE = new BasicStroke(2);
	
	public abstract void  paint(Color color, Graphics2D g, int x, int y, int size, IScalarStringHolder name, IScalarStringHolder index);
	
	private static void paintName(Graphics2D g, int dx, int dy, int x, int y, int size, IScalarStringHolder name, IScalarStringHolder index) {
		Font oldFont = g.getFont();
		g.setFont(FormulaRenderContext.getFont("Serif", Font.ITALIC, 21));
		if (name.getScalarOrNull() != null) {
			g.drawString(name.getScalarOrNull(), x + size + dx, y + size - dy - 3);
			if (index.getScalarOrNull() != null) {
				FontMetrics fm = g.getFontMetrics();
				int width = fm.stringWidth(name.getScalarOrNull());

				g.setFont(FormulaRenderContext
						.getFont("Serif", Font.ITALIC, 15));
				fm = g.getFontMetrics();
				int height = fm.getHeight();
				g.drawString(index.getScalarOrNull(), x + size + width + dx, y + size - dy - 13
						+ height);

			}// if index
		}// if name
		g.setFont(oldFont);
	}// paintName
	
	private static GeneralPath getArrowPath(int x, int y, float size) {
		GeneralPath gp = new GeneralPath();
		gp.moveTo(x + size / 3, y + size / 2);
		gp.lineTo(x + size / 3, y);

		gp.moveTo(x + size * 2 / 3, y + size / 2);
		gp.lineTo(x + size * 2 / 3, y);

		gp.moveTo(x + size / 6, y + size / 4);
		gp.lineTo(x + size / 2, y - size / 3);

		gp.moveTo(x + size - size / 6, y + size / 4);
		gp.lineTo(x + size - size / 2, y - size / 3);
		return gp;
	}// getArrowPath
}
