package math4u2.util.swing;

import java.awt.*;

public class ExtendedInsets extends Insets {
	public ExtendedInsets(Insets insets) {
		super(insets.top, insets.left, insets.bottom, insets.right);
	}

	public ExtendedInsets(int top, int left, int bottom, int right) {
		super(top, left, bottom, right);
	}

	public int getWidth() {
		return left + right;
	}

	public int getHeight() {
		return top + bottom;
	}

	public int adjustX(int x) {
		return x + left;
	}

	public int adjustY(int y) {
		return y + top;
	}

	public int adjustWidth(int width) {
		return width - getWidth();
	}

	public int adjustHeight(int height) {
		return height - getHeight();
	}

	public Rectangle adjustBounds(Rectangle bounds) {
		return new Rectangle(adjustX(bounds.x), adjustY(bounds.y),
				adjustWidth(bounds.width), adjustHeight(bounds.height));
	}
}