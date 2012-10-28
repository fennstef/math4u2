package math4u2.util.swing.print;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import math4u2.view.layout.TableLayout;

public class JPreview extends JPanel {
	protected PreviewPanel panel;

	public JPreview(PrinterJob job, Printable printable, PageFormat pageFormat) {
		panel = new PreviewPanel(job, printable, pageFormat);
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
		
		setBackground(Color.GRAY);
		scrollPane.getViewport().setBackground(Color.GRAY);
		
		//TableLayout
		double P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = { {F}, {
			P, F}
		};

		setLayout(new TableLayout(size));

		add(new PreviewToolbar(job, printable, panel), 	"0, 0, F, C");
		add(scrollPane,									"0, 1, F, F");
	}
	
	public JPreview(PrinterJob job, Printable printable) {
		this(job,printable,PreviewPanel.getStandardPageFormatLandscape(job));
	}

	public Dimension getGrid() {
		return panel.getGrid();
	}

	public void setGrid(Dimension grid) {
		panel.setGrid(grid);
	}

	public double getScale() {
		return panel.getScale();
	}

	public void setScale(double scale) {
		panel.setScale(scale);
	}
}