package math4u2.util.swing.print;

import java.awt.*;
import javax.swing.*;

import math4u2.util.swing.*;
import math4u2.view.layout.*;

public class PreviewLayout extends AbstractLayout {
	protected int hgap, vgap;

	public PreviewLayout() {
		this(4, 4);
	}

	public PreviewLayout(int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
	}

	public Dimension minimumLayoutSize(Container parent) {
		int count = parent.getComponentCount();
		if (count < 1)
			return parent.getMinimumSize();
		Component child = parent.getComponent(0);
		int w = child.getMinimumSize().width + hgap;
		int h = child.getMinimumSize().height + vgap;
		int width = Math.min(count, Math.max(1, parent.getSize().width / w));
		if (parent instanceof PreviewPanel) {
			Dimension grid = ((PreviewPanel) parent).getGrid();
			if (grid != null)
				width = grid.width;
		}
		int height = (int) Math.ceil(count / width);
		if (count % width != 0)
			height++;
		int wide = width * w;
		if (parent.getParent() instanceof JViewport) {
			wide = ((JViewport) parent.getParent()).getExtentSize().width;
		}
		return new Dimension(wide, height * h + hgap);
	}

	public Dimension preferredLayoutSize(Container parent) {
		int count = parent.getComponentCount();
		if (count < 1)
			return parent.getPreferredSize();
		Component child = parent.getComponent(0);
		int w = child.getPreferredSize().width + hgap;
		int h = child.getPreferredSize().height + vgap;
		int width = Math.min(count, Math.max(1, parent.getSize().width / w));
		if (parent instanceof PreviewPanel) {
			Dimension grid = ((PreviewPanel) parent).getGrid();
			if (grid != null)
				width = grid.width;
		}
		int height = (int) Math.ceil(count / width);
		if (count % width != 0)
			height++;
		int wide = width * w;
		if (parent.getParent() instanceof JViewport) {
			wide = ((JViewport) parent.getParent()).getExtentSize().width;
		}
		return new Dimension(wide, height * h + hgap);
	}

	public void layoutContainer(Container parent) {
		int count = parent.getComponentCount();
		if (count < 1)
			return;
		Component child = parent.getComponent(0);
		int w = child.getPreferredSize().width;
		int h = child.getPreferredSize().height;
		int width = Math.max(1, parent.getSize().width / (w + hgap));
		ExtendedInsets parentInsets = new ExtendedInsets(parent.getInsets());
		int top = parentInsets.top;
		if (parent instanceof PreviewPanel) {
			Dimension grid = ((PreviewPanel) parent).getGrid();
			if (grid != null)
				width = grid.width;
		}
		for (int i = 0; i < count; i++) {
			child = parent.getComponent(i);
			int left = parentInsets.adjustX((parentInsets.adjustWidth(parent
					.getSize().width
					- (w + hgap) * Math.min(width, count))) / 2);
			int x = left + (w + hgap) * (i % width);
			int y = top + (h + vgap) * (i / width);
			child.setBounds(x, y, w, h);
		}
	}
}