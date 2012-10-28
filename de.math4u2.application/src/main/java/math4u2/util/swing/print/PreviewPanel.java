package math4u2.util.swing.print;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.border.EmptyBorder;

import math4u2.exercises.ui.ScrollablePanel;
import math4u2.util.swing.ExtendedInsets;

public class PreviewPanel extends ScrollablePanel{
	protected PreviewLayout layout;

	protected Dimension grid;

	protected double scale=1.0;
	
	protected PrinterJob job;
	
	protected Printable printable;
	
	public PreviewPanel(PrinterJob job, Printable printable, PageFormat pageFormat) {
		this.job = job;
		this.printable = printable;
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(layout = new PreviewLayout(8, 8));
		try {
			int page = 0;
			while (true) {
				add(new PreviewPage(job, printable, page, pageFormat, scale));
				page++;
			}
		} catch (PrinterException e) {
		}
		setBackground(Color.GRAY);		
	}
	
	public static PageFormat getStandardPageFormatLandscape(PrinterJob job){
		PageFormat pageFormat = job.defaultPage();
		pageFormat.setOrientation(PageFormat.LANDSCAPE);
		return pageFormat;
	}
	
	public static PageFormat getStandardPageFormatPortrait(PrinterJob job){
		PageFormat pageFormat = job.defaultPage();
		pageFormat.setOrientation(PageFormat.PORTRAIT);
		return pageFormat;
	}	

	public Dimension getGrid() {
		return grid;
	}

	public void setGrid(Dimension grid) {
		this.grid = grid;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
		grid = null;
	}

	public void render(PageFormat format) {
		if (grid != null) {
			ExtendedInsets insets = new ExtendedInsets(getInsets());
			double width = insets.adjustWidth(getPreferredSize().width);
			double height = insets.adjustHeight(getParent().getSize().height);
			double w = (format.getWidth() + 12) * grid.width;
			double h = (format.getHeight() + 12) * grid.height;
			scale = width / w;
			if (scale * h > height)
				scale = height / h;
		}
		
		removeAll();
		int pageCount = 0;
		try {			
			while (true) {
				PreviewPage page = new PreviewPage(job, printable, pageCount, format, scale);
				add(page);
				pageCount++;
			}
		} catch (PrinterException e) {
		}
		revalidate();
	}//render
}