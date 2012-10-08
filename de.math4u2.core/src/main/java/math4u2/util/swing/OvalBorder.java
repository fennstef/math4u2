package math4u2.util.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.Border;

/**
 * Erzeugt einen Border mit runden Ecken
 * 
 * @author Fenn Stefan
 */
public class OvalBorder implements Border {
	public final static RenderingHints antialiasOn = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	public final static RenderingHints antialiasOff = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

	protected int ovalWidth = 10;

	protected int ovalHeight = 10;

	protected Color color;

	public OvalBorder() {
		this(10, 10);
	}// Konstruktor

	public OvalBorder(int w, int h) {
		this(w, h, Color.BLACK);
	}// Konstruktor

	public OvalBorder(int ovalWidth, int ovalHeight, Color color) {
		this.ovalWidth = ovalWidth;
		this.ovalHeight = ovalHeight;
		this.color = color;
	}// Konstruktor

	public Insets getBorderInsets(Component c) {
		return new Insets(ovalHeight, ovalWidth, ovalHeight, ovalWidth);
	}// getBorderInsets

	public boolean isBorderOpaque() {
		return true;
	}// isBorderOpaque

	public void paintBorder(Component c, Graphics gr, int x, int y, int width,
			int height) {
		width--;
		height--;

		Graphics2D g = (Graphics2D) gr;

		g.setColor(c.getBackground());

		g.fillRect(x, y, width, ovalHeight);
		g.fillRect(x, y, ovalWidth, height);
		g.fillRect(x + width - ovalWidth, y, ovalWidth, height);
		g.fillRect(x, y + height - ovalHeight, width, ovalHeight);

		g.setRenderingHints(antialiasOn);
		g.setColor(color);
		g.drawRoundRect(x, y, width, height, ovalWidth, ovalHeight);
		g.setRenderingHints(antialiasOff);
	}// paintBorder
}// class OvalBorder
