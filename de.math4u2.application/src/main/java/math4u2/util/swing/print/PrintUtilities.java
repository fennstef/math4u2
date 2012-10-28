package math4u2.util.swing.print;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

import math4u2.exercises.ui.Math4u2TextPane;
import math4u2.util.exception.ExceptionManager;

/**
 * A simple utility class that lets you very simply print an arbitrary
 * component. Just pass the component to the PrintUtilities.printComponent. The
 * component you want to print doesn't need a print method and doesn't have to
 * implement any interface or do anything special at all.
 * <P>
 * If you are going to be printing many times, it is marginally more efficient
 * to first do the following:
 * 
 * <PRE>
 * 
 * PrintUtilities printHelper = new PrintUtilities(theComponent);
 * 
 * </PRE>
 * 
 * then later do printHelper.print(). But this is a very tiny difference, so in
 * most cases just do the simpler
 * PrintUtilities.printComponent(componentToBePrinted).
 * 
 * 7/99 Marty Hall, http://www.apl.jhu.edu/~hall/java/ May be freely used or
 * adapted.
 */

public class PrintUtilities implements Printable {
	private Component[] componentsToBePrinted;
	private boolean isHelpView;

	public PrintUtilities(Component[] componentsToBePrinted, boolean isHelpView) {
		this.componentsToBePrinted = componentsToBePrinted;
		this.isHelpView = isHelpView;
	}
	
	public void print() {
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this);
		if (printJob.printDialog())
			try {
				printJob.print();
			} catch (PrinterException pe) {
				ExceptionManager.doError("Error printing: ",pe);
			}
	}

	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		Graphics2D g2d = (Graphics2D) g;
		int page = 0;
		double curY = 0;
		double k=1.0;
		
		for (int i = 0; i < componentsToBePrinted.length; i++) {
			JComponent c = (JComponent) componentsToBePrinted[i];
			double pw = pageFormat.getImageableWidth();
			double cw = c.getSize().width;
			double ph = pageFormat.getImageableHeight();
			double ch = c.getSize().height;
			if (cw == 0) {
				cw = pw;
				ch = c.getPreferredSize().height;
				c.setSize((int) cw, (int) ch);
				c.setPreferredSize(new Dimension((int)cw,(int)ch));
			}

			double l = ph / ch;
			if(i==0 && !isHelpView){
				k=1.0;
			}else if(i==1){
				k = pw / cw;				
				k = Math.min(k, l);
			}
			
			if (curY + ch * k > pageFormat.getImageableHeight()) {
				curY = 0;
				page++;
			}
			
			boolean componentPainted=true;
			if (page == pageIndex && g2d!=null){
				g2d.translate((int) pageFormat.getImageableX(),
						(int) pageFormat.getImageableY() + curY);
				g2d.scale(k, k);
				disableDoubleBuffering(c);

				if (c instanceof Math4u2TextPane) {
					Math4u2TextPane tp = (Math4u2TextPane) c;
					boolean isHistory = tp.isHistory();
					if(tp.getDocument().getLength()!=0){
						tp.setHistory(false);
						c.paint(g2d);
						tp.setHistory(isHistory);						
					}else{
						componentPainted=false;
					}				
				} else {
					c.paint(g2d);
				}// else
				
				enableDoubleBuffering(c);
				g2d.scale(1 / k, 1 / k);
				g2d.translate((int) -pageFormat.getImageableX(),
						(int) -pageFormat.getImageableY() - curY);
			}// if
			
			if(componentPainted){
				curY += ch * k +10;
			}//if
		}// for i
		if (page < pageIndex)
			return NO_SUCH_PAGE;
		else
			return PAGE_EXISTS;
	}// print

	/**
	 * The speed and quality of printing suffers dramatically if any of the
	 * containers have double buffering turned on. So this turns if off
	 * globally.
	 * 
	 * @see enableDoubleBuffering
	 */
	public static void disableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}

	/** Re-enables double buffering globally. */

	public static void enableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}
}//PrintUtilities
