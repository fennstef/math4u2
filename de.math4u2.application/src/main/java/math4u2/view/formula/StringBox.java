package math4u2.view.formula;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

/**
 * Klasse für Box-Elemente, die Text darstellen können.
 * 
 * @author Erich Seifert
 * @version $Revision: 1.15 $
 */
public abstract class StringBox extends AtomicBox {
    /** Darstellungsoptionen für Textelemente */
    protected static final RenderingHints ANTIALIAS = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    /** Inhalt: Zeichenkette */
    private final String content;

    /** Vektorumriss der Zeichenkette */
    private Shape shape;

    /** Name des Zeichnesatzes mit dem der Inhalt dargestellt werden soll */
    private String fontName;

    /** Formattierungsoptionen für den Zeichensatz (fett, kursiv, ...) */
    private int fontStyle;

    /**
     * Legt fest, ob der Inhalt skaliert werden soll oder eine feste Größe
     * besitzt.
     */
    private boolean resizeContent;

    /**
     * Konstruktor, der ein neues <code>StringBox</code>-Objekt mit
     * definiertem Inhalt erzeugt und initialisiert.
     * 
     * @param frc
     *            Darstellungsumgebung
     * @param content
     *            textueller Inhalt
     * @param scaleContent
     *            <code>true</code> falls der Inhalt mitsakliert werden soll,
     *            anderenfalls <code>false</code>
     */
    public StringBox(FormulaRenderContext frc, String content,
            boolean scaleContent) {
        this(frc, content, scaleContent, frc.getDefaultFontName(), Font.PLAIN);
    }

    /**
     * Konstruktor, der ein neues <code>StringBox</code>-Objekt mit
     * definiertem Inhalt und Zeichensatz erzeugt und initialisiert.
     * 
     * @param frc
     *            Darstellungsumgebung
     * @param content
     *            textueller Inhalt
     * @param scaleContent
     *            <code>true</code> falls der Inhalt mitsakliert werden soll,
     *            anderenfalls <code>false</code>
     * @param fontName
     *            Name des zu verwendenden Zeichensatzes
     */
    public StringBox(FormulaRenderContext frc, String content,
            boolean scaleContent, String fontName) {
        this(frc, content, scaleContent, fontName, Font.PLAIN);
    }

    /**
     * Konstruktor, der ein neues <code>StringBox</code>-Objekt mit
     * definiertem Inhalt und Zeichensatz erzeugt und initialisiert. Zusaetzlich
     * koennen Formattierungsoptionen, wie fetter/kursiver Schriftschnitt,
     * uebergeben werden.
     * 
     * @param frc
     *            Darstellungsumgebung
     * @param content
     *            textueller Inhalt
     * @param scaleContent
     *            <code>true</code> falls der Inhalt mitsakliert werden soll,
     *            anderenfalls <code>false</code>
     * @param fontName
     *            Name des zu verwendenden Zeichensatzes
     * @param fontStyle
     *            Formattierungsoptionen fuer den Zeichensatz
     */
    public StringBox(FormulaRenderContext frc, String content,
            boolean scaleContent, String fontName, int fontStyle) {
        super(frc);
        this.content = FormulaRenderContext.getSymbol(content);
        this.resizeContent = scaleContent;
        if(getFontName()==null) setFontName(fontName);
        Font font = FormulaRenderContext.getFont(getFontName(),getFontStyle(),getDisplayHeight());
        
        if(font.canDisplayUpTo(this.content)==-1){
        	setFontName(fontName);	
        }else{
        	setFontName("Symbol");
        }
        
        setFontStyle(fontStyle);
    }

    /**
     * Ueberschriebene Methode.
     * 
     * @see math4u2.view.formula.AtomicBox#rebuild()
     */
    protected void rebuild() {
        super.rebuild();

        Dimension dim;
        if (content != null) {
            Insets insets = getInsets();
            if (content.equals(" ")) {
                dim = new Dimension(insets.left
                        + Math.round(getRenderContext().getSpaceWidth()
                                * getDisplayHeight()) + insets.right,
                        insets.top
                                + Math.round(getRenderContext().getSpaceWidth()
                                        * getDisplayHeight()) + insets.bottom);
            } else {
            	Font font = FormulaRenderContext.getFont(getFontName(), getFontStyle(), getDisplayHeight());
                TextLayout layout = new TextLayout(content, font,
                        getRenderContext().getDefaultFontRenderContext());
                setShape(layout.getOutline(null));

                Rectangle2D bounds = getShape().getBounds2D();

                float ascent = Math.max(-(float) bounds.getMinY(), getEm());
                float descent = Math.max((float) bounds.getMaxY(), 0f);

                dim = new Dimension(insets.left
                        + (int) Math.ceil(bounds.getWidth()) + insets.right,
                        insets.top + (int) Math.ceil(ascent + descent)
                                + insets.bottom);

                setBaseline(ascent);
            }
        } else {
            dim = new Dimension();
            setShape(null);
            setBaseline(0f);
        }

        setMinimumSize(dim);
        setPreferredSize(dim);
        setMaximumSize(dim);
    }

    /**
     * Ueberschriebene Methode.
     * 
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        super.paint(g);

        /*
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);
        g.setColor(Color.GRAY);
        g.drawLine(0, Math.round(getBaseline()), getWidth()-1, Math.round(getBaseline()));
        g.setColor(Color.BLACK);
        //*/

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(ANTIALIAS);

        Insets insets = getInsets();

        if (getShape() == null)
            rebuild();

        if (getShape() != null) {
        	Rectangle2D bounds = getShape().getBounds2D();
            if (resizeContent) {
                g2.scale((double)getWidth()  / bounds.getWidth(),
                         (double)getHeight() / bounds.getHeight());
            }
            g2.translate(insets.left - bounds.getMinX(),
                         insets.top  + getBaseline());
            g2.fill(getShape());
        }
    }

    /**
     * Gibt den Inhalt dieses Textelements zurueck.
     * 
     * @return textueller Inhalt
     */
    public String getContent() {
        return content;
    }

    /**
     * Gibt die verwendeten Formattierungsoptionen fuer den Zeichensatz zurueck.
     * 
     * @return Formattierungsoptionen fuer den Zeichensatz
     */
    protected int getFontStyle() {
        return fontStyle;
    }

    /**
     * Legt die Formattierungsoptionen fuer den Zeichensatz fest.
     * 
     * @param style
     *            Neue Formattierungsoptionen fuer den Zeichensatz
     */
    protected void setFontStyle(int style) {
        if (style != fontStyle) {
            fontStyle = style;
            rebuild();
        }
    }

    /**
     * Gibt den Namen des verwendeten Zeichensatz zurueck.
     * 
     * @return NAme des verwendeten Zeichensatzes
     */
    protected String getFontName() {
        return fontName;
    }

    /**
     * Legt den Namen des Zeichensatzes fest, der zur Darstellung des Inhalts
     * verwendet werden soll.
     * 
     * @param fontName
     *            Name des neuen Zeichensatzes
     */
    protected void setFontName(String fontName) {
        if (fontName != this.fontName) {
            this.fontName = fontName;
            rebuild();
        }
    }

    /**
     * Gibt den Vektorumriss des Texts zurück.
     * 
     * @return Vektorumriss des Texts
     */
    protected Shape getShape() {
        return shape;
    }

    /**
     * Legt den Vektorumriss des Texts fest.
     * 
     * @param shape
     *            Vektorumriss des Texts
     */
    private void setShape(Shape shape) {
        this.shape = shape;
    }

    protected boolean isResizeContent() {
        return resizeContent;
    }

    protected void setResizeContent(boolean resizeContent) {
        this.resizeContent = resizeContent;
    }

}