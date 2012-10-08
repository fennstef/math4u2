package math4u2.util.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;

import javax.swing.border.EmptyBorder;

/**
 * Erzeugt einen Rahmen mit abgerundeten Ecken
 * 
 * @author Fenn Stefan
 */
public class RoundedRectBorder extends EmptyBorder {

    public final static RenderingHints antialiasOn = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    public final static RenderingHints antialiasOff = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

    protected float thickness;

    protected Color lineColor;

    protected int radius, indent;

    /**
     * Erzeugt einen abgerundeten Rahmen
     * 
     * @param color
     *            Farbe des Rahmens
     * @param thickness
     *            Dicke des Rahmens
     * @param radius
     *            Radius der Abrundungen an den Ecken
     */
    public RoundedRectBorder(Color color, float thickness, int radius,
            int indent) {
        super(radius / 2 + indent * 2, radius / 2 + indent * 2, radius / 2
                + indent * 2, radius / 2 + indent * 2);
        lineColor = color;
        this.thickness = thickness;
        this.radius = radius;
        this.indent = indent;
    }//Konstrukotr
    
    public void paintBorderFromSuper(Component c, Graphics g, int x, int y, int width,
            int height){
    	super.paintBorder(c,g,x,y,width,height);
    }//paintBorderFromSuper

    public void paintBorder(Component c, Graphics g, int x, int y, int width,
            int height) {
        Insets insets = getBorderInsets(c);
        Graphics2D gr = (Graphics2D) g;

        gr.setRenderingHints(antialiasOff);
        Color oldColor = g.getColor();
        Stroke oldStroke = gr.getStroke();
        
        g.translate(x + indent, y + indent);
        
        
        //Leerraumfüllen
        if (indent > 0) {
        	if(c.getParent()!=null)
        		g.setColor(c.getParent().getBackground());
        	else g.setColor(Color.WHITE);
            g.fillRect(-indent, -indent, width, indent);
            g.fillRect(-indent, -indent, indent, height);
            g.fillRect(width - indent * 2, -indent, width, height);
            g.fillRect(-indent, height - indent * 2, width, height);
            g.setColor(Color.WHITE);
            g.drawRect(0,1,width-indent*2-radius/2+2,height-indent*2-radius/2+2);
        }

        //Ränder leer füllen
        GeneralPath gp;
        if(c.getParent()!=null)
        	g.setColor(c.getParent().getBackground());
        //links oben
        Arc2D arc1 = new Arc2D.Double(Arc2D.OPEN);
        arc1.setArc(0, 0, radius, radius, 90, 90, Arc2D.OPEN);
        gp = new GeneralPath(arc1);
        gp.lineTo(0, 0);
        gp.closePath();
        gr.fill(gp);

        //rechts oben
        Arc2D arc2 = new Arc2D.Double(Arc2D.OPEN);
        arc2.setArc(width - insets.right - radius / 2, 0, radius, radius, 0,
                90, Arc2D.OPEN);
        gp = new GeneralPath(arc2);
        gp.lineTo(width - 2 * indent, 0);
        gp.closePath();
        gr.fill(gp);

        //links unten
        Arc2D arc3 = new Arc2D.Double(Arc2D.OPEN);
        arc3.setArc(0, height - insets.top - radius / 2, radius, radius, 180,
                90, Arc2D.OPEN);
        gp = new GeneralPath(arc3);
        gp.lineTo(0, height - 2 * indent);
        gp.closePath();
        gr.fill(gp);

        //rechts unten
        Arc2D arc4 = new Arc2D.Double(Arc2D.OPEN);
        arc4.setArc(width - insets.right - radius / 2, height - insets.top
                - radius / 2, radius, radius, 270, 90, Arc2D.OPEN);
        gp = new GeneralPath(arc4);
        gp.lineTo(width - 2 * indent, height - 2 * indent);
        gp.closePath();
        gr.fill(gp);

        //Rand zeichnen
        gr.setRenderingHints(antialiasOn);
        gr.setStroke(new BasicStroke(thickness));
        g.setColor(lineColor);
        gp = new GeneralPath(arc1);
        gp.append(arc3, true);
        gp.append(arc4, true);
        gp.append(arc2, true);
        gp.closePath();
        gr.draw(gp);
        g.translate(-x, -y);

        g.setColor(oldColor);
        gr.setStroke(oldStroke);
        gr.setRenderingHints(antialiasOff);
    }//paintBorder

    /**
     * Returns the color of the border.
     */
    public Color getLineColor() {
        return lineColor;
    }//getLineColor

    /**
     * Returns the thickness of the border.
     */
    public double getThickness() {
        return thickness;
    }//getThickness

    public boolean isBorderOpaque() {
        return true;
    }//isBorderOpaque

}//class RoundedRectBorder
