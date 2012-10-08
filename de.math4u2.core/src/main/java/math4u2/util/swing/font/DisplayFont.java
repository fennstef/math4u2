package math4u2.util.swing.font;

import java.awt.*;
import java.awt.font.*;

public class DisplayFont extends Font {
	private float ascent;

	private float descent;

	private float height;

	private float xGap;

	private float leading;

	private int baseline;

	public DisplayFont(String name, int style, int size) {
		super(name, style, size);
	}

	public void initMetrics(FontRenderContext frc, String s) {
		LineMetrics lm = this.getLineMetrics(s, frc);
		ascent = lm.getAscent();
		descent = lm.getDescent();
		height = ascent + descent;
		xGap = height / 7;
		leading = lm.getLeading();
		baseline = lm.getBaselineIndex();
	} //initMetrics

	public void initMetrics(FontRenderContext frc) {
		initMetrics(frc, "8");
	}

	public float getAscent() {
		return ascent;
	}

	public float getDescent() {
		return descent;
	}

	public float getHeight() {
		return height;
	}

	public float getXGap() {
		return xGap;
	}

	public float getLeading() {
		return leading;
	}

	public int getBaseline() {
		return baseline;
	}
}//DisplayFont
