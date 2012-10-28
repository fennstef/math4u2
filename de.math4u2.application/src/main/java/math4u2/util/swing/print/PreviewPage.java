package math4u2.util.swing.print;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JPanel;

public class PreviewPage extends JPanel {
	protected PrinterJob job;

	protected Printable printable;

	protected int page;

	protected double scale;
	
	protected PageFormat format;

	public PreviewPage(PrinterJob job, Printable printable, int page, PageFormat pf, double scale)
			throws PrinterException {
		this.job = job;
		this.printable = printable;
		this.page = page;
		this.scale = scale;
		this.format = pf;
		setOpaque(false);
		setBackground(Color.WHITE);
		setBorder(new PreviewBorder());
		render(pf, scale);
	}

	public void render(PageFormat format, double scale) throws PrinterException {
		Graphics g = getGraphics();
		if(g==null){
			BufferedImage image = new BufferedImage((int)(format.getWidth()*scale),(int) (format.getHeight()*scale), BufferedImage.TYPE_INT_RGB);
			g = image.getGraphics();
			Graphics2D gr = (Graphics2D) g;
			gr.scale(1/scale, 1/scale);
			g.setColor(Color.BLACK);
		}
		if (printable.print(g, format, page) == Printable.NO_SUCH_PAGE){
			throw new PrinterException("No Such Page");
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int w = (int) format.getWidth();
		int h = (int) format.getHeight();
		int ww = (int) (format.getWidth() * scale);
		int hh = (int) (format.getHeight() * scale);

		Insets insets = getInsets();
		setPreferredSize(new Dimension(ww + insets.left + insets.right, hh
				+ insets.top + insets.bottom));		

		g.setColor(getBackground());
		g.fillRect(insets.left, insets.top, w - insets.left - insets.right, h
				- insets.top - insets.bottom);
		((Graphics2D) g).scale(scale, scale);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, ww, hh);
		try {
			printable.print(g, format, page);
		} catch (PrinterException e) {
			e.printStackTrace();
		}
		((Graphics2D) g).scale(1/scale, 1/scale);
	}//paintComponent
}