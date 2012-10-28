package math4u2.view.formula;

import java.awt.*;
import java.awt.font.TextLayout;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import math4u2.controller.MathObject;
import math4u2.controller.relation.RelationContainer;
import math4u2.util.exception.NotImplementedException;

/**
 * Klasse für grundlegende Funktionalität von Formel-Boxen. Sie dient auch als
 * Behälter für Indizes und Exponenten, sowie Dekorationen (Vektorpfeile,
 * Über-/Unterstrich, usw.).
 * 
 * @author Erich Seifert
 * @version $Revision: 1.16 $
 */
public abstract class AtomicBox extends JPanel implements FormulaElement, MathObject {
	/** Höhe der Grundlinie */
    private float baseline;
	/** Größe des Zeichensatzes */
    private float fontSize;
	/** reltive Größe */
    private byte relSize;

	/** Pixelgröße in der das Element dargestellt wird. */
	private float displayHeight;
	/** Pixelgröße der Kleinbuchstaben */
	private float xHeight;
	/** Pixelgröße der Kleinbuchstaben */
	private float em;

	/** Darstellungsumgebung */
    private FormulaRenderContext renderContext;
    /** Name der AtomicBox */
    private String name;

    /**
     * Konstruktor, der ein neues <code>AtomicBox</code>-Objekt
     * erzeugt und initialisiert.
     * @param frc Standardumgebung zur Darstellung der Formel
     */
    public AtomicBox(FormulaRenderContext frc) {
        setLayout(null);
        setOpaque(false);
        setAlignmentX(CENTER_ALIGNMENT);

        setRenderContext(frc);
        setFontSize(renderContext.getDefaultFontSize());
        setRelSize(FormulaRenderContext.FONTSIZE_D);
    }

    // ------------------------------------------------------------
    // von MathObject implementierte Methoden
    
	public Object getIdentifier() {
		return name;
	}

    /*
     * (non-Javadoc)
     * @see math4u2.controller.MathObject#getRelationContainer()
     */
    public RelationContainer getRelationContainer() {
        throw new NotImplementedException();
    } //relationContainer

    /*
     * (non-Javadoc)
     * @see math4u2.controller.MathObject#prepareDelete()
     */
    public void prepareDelete() {        
    }

    /*
     * (non-Javadoc)
     * @see math4u2.controller.MathObject#renew(math4u2.controller.MathObject)
     */
    public void renew(MathObject source) {
        throw new NotImplementedException();
    }

    /*
     * (non-Javadoc)
     * @see math4u2.controller.MathObject#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }
    
	public Object getKey() {
		return name;
	}

    /*
     * (non-Javadoc)
     * @see math4u2.controller.MathObject#swapLinks(math4u2.controller.MathObject,
     *      math4u2.controller.MathObject)
     */
    public void swapLinks(MathObject oldObject, MathObject newObject)
            throws Exception {
        throw new NotImplementedException();
    } //swapLinks

    /*
     * (non-Javadoc)
     * @see math4u2.controller.MathObject#testDelete()
     */
    public boolean testDelete() {
        return true;
    }

    /*
     * (non-Javadoc)
     * @see math4u2.controller.MathObject#testSubstitution(math4u2.controller.MathObject,
     *      java.util.Set)
     */
    public boolean testSubstitution(MathObject oldObject, Set oldAggregateSet) {
        return true;
    }

    // -------------------------------------------------------------------------------
    // Ende der von MathObject implementierten Methoden

    /**
     * Gibt die typografische Grundlinie dieses Formelelements zurück.
     * @return Höhe der Grundlinie gegenüber dem oberen Elementrand
     */
    public float getBaseline() {
        return baseline;
    }

    /**
     * Legt die Höhe der Grundlinie für dieses Formelelement fest.
     * @param baseline Höhe der Grundlinie gegenüber dem oberen Elementrand
     */
    protected void setBaseline(float baseline) {
        this.baseline = baseline;
    }

    /**
     * Gibt die Höhe von Kleinbuchstaben des Standardzeichensatzes
     * dieser Box in der eingestellten Schriftgröße zurück.
     * @return Minuskelhöhe in Pixel
     */
    public float getXHeight() {
		return xHeight;
    }

    /**
     * Gibt die Höhe von Großbuchstaben des Standardzeichensatzes
     * dieser Box in der eingestellten Schriftgröße zurück.
     * @return Majuskelhöhe in Pixel
     */
    public float getEm() {
		return em;
    }

	/**
     * Gibt die Größe des Zeichensatzes dieses Formelelements zurück.
     * @return Größe des Zeichensatzes
     */
    public float getFontSize() {
        return fontSize;
    }

    /**
     * Legt die Größe des Zeichensatzes dieses Formelelements fest.
     * @param fontSize Größe des Zeichensatzes
     */
    public void setFontSize(float fontSize) {
		if (fontSize!=this.fontSize) {
	        this.fontSize = fontSize;
	        rebuild();
		}
    }

    /* DEBUGGING:
    public void paint(Graphics g) {
        super.paint(g);
		
        //g.setColor(Color.LIGHT_GRAY);
        //g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		//g.setColor(Color.RED);
		//int ly1 = Math.round(getBaseline());
        //g.drawLine(0, ly1, getWidth() - 1, ly1);

		//g.setColor(Color.GREEN);
		//int ly2 = Math.round(getSpacing().top);
        //g.drawLine(0, ly2, getWidth() - 1, ly2);

		//g.setColor(Color.BLACK);
    }
    //*/

	public Insets getSpacing() {
		return getInsets();
	}

    /**
     * Legt die Abstände (in Pixeln) zu umgebenden Elementen fest.
     * @param top oberer Abstand
     * @param left linker Abstand
     * @param bottom unterer Abstand
     * @param right rechter Abstand
     */
    public void setSpacing(int top, int left, int bottom, int right) {
		EmptyBorder insetBorder = new EmptyBorder(top, left, bottom, right);
        setBorder(insetBorder);
    }

    /**
     * Erweitert/verringert die Abstände (in Pixeln) zu umgebenden Elementen.
     * @param top Erweiterung gegenüber dem oberen Abstand
     * @param left Erweiterung gegenüber dem linken Abstand
     * @param bottom Erweiterung gegenüber dem unteren Abstand
     * @param right Erweiterung gegenüber dem rechten Abstand
     */
    public void addSpacing(int top, int left, int bottom, int right) {
		if (top==0 && left==0 && bottom==0 && right==0)
			return;

		Insets insets = getSpacing();
        setSpacing(insets.top + top, insets.left + left, insets.bottom + bottom, insets.right + right);
    }

    /**
     * Überschriebene Methode, die die vertikale Ausrichtung des
     * Formelelements zurückgibt. Dies wird hauptsächlich zur Darstellung
     * von Formeln innerhalb anderer Swing-Oberflächenkomponenten, wie
     * beispielswweise Textbereichen, benötigt.
     * @see java.awt.Component#getAlignmentY()
     */
    public float getAlignmentY() {
        double alignment = Math.round(getBaseline())/(float)getHeight();
        if (alignment < 0.0)
            return 0f;
        if (alignment > 1.0)
            return 1f;
        return (float)alignment;
    }

    /**
     * Gibt die relative Größenstufe zurück.
     * @return relative Größenstufe
     * @see FormulaRenderContext
     */
    protected byte getRelSize() {
        return relSize;
    }

    /**
     * Legt die relative Größenstufe fest. Es sollte jedoch
     * vermieden werden, die relative Größenstufe direkt zu
     * setzen. Stattdessen wird die Verwendung der Methoden
     * <code>shrinkRelSize()</code> oder <code>shrinkVisually()</code>
     * empfohlen.
     * @param relFontSize relative Größenstufe
     * @see #shrinkRelSize()
     * @see #shrinkVisually()
     */
    public void setRelSize(byte relFontSize) {
		if (relFontSize!=relSize) {
	        relSize = relFontSize;
	        rebuild();
		}
    }

    /**
     * Verkleinert die relative Größe dieses Elements um eine
     * Stufe. Dies muss noch keine optische Änderung bewirken.
     * Um garantiert eine optische Änderung zu bewirken kann die
     * Methode <code>shrinkVisually()</code> verwendet werden.
     * @see #shrinkVisually()
     */
    public void shrinkRelSize() {
        byte newRelFontSize = renderContext.shrinkRelSize(getRelSize());
        setRelSize(newRelFontSize);
    }

    /**
     * Verkleinert dne Formelstil einer gegebene Formelbox so lange,
     * bis die Auswirkungen auch optisch wahrnehmbar sind.
     * @return Anzahl der Verkleinerungsstufen die durchlaufen wurden
     * @see #shrinkRelSize()
     */
    public final int shrinkVisually() {
        float origFontSize = getRenderContext().getRelFontSize(getRelSize());

        int shrinkages = 0;
        while (shrinkages++ < 20) {
            shrinkRelSize();
            if (Math.abs(origFontSize - getRenderContext().getRelFontSize(getRelSize())) > 1e-4)
            	break;
        }

        return shrinkages;
    }

    /**
     * Methode, die die tatsächliche Darstellungsgröße in Pixeln
     * zurückgibt.
     * @return tatsächliche Darstellungsgröße in Pixeln
     * @see math4u2.view.formula.FormulaElement#getDisplayHeight()
     */
    public float getDisplayHeight() {
        return displayHeight;
    }

    /**
     * Gibt die Oberlänge zurück.
     * @return Oberlänge
     */
    public float getRealAscend() {
        return getBaseline();
    }

    /**
     * Gibt die Unterlänge zurück.
     * @return Unterlänge
     */
    public float getRealDescend() {
        return getPreferredSize().height-getBaseline();
    }

    /**
     * Erstellt interne Strukturen für die Darstellung neu.
     * Diese Methode sollte bei allen Größen oder Schriftänderungen
     * aufgerufen werden. 
     */
    protected void rebuild() {
		invalidate();

		// Darstellungsgröße neu berechnen
		displayHeight = getFontSize() * getRenderContext().getRelFontSize(getRelSize());

		// Minuskel- und Majuskelhöhe neu berechnen
    	String fontName = getRenderContext().getDefaultFontName();
        Font font = FormulaRenderContext.getFont(fontName, Font.PLAIN, getFontSize());

		TextLayout xhLayout = new TextLayout("x", font,
                getRenderContext().getDefaultFontRenderContext());
        Shape xhShape = xhLayout.getOutline(null);
        xHeight = (float)xhShape.getBounds2D().getHeight();

		TextLayout emLayout = new TextLayout("M", font,
                getRenderContext().getDefaultFontRenderContext());
        Shape emShape = emLayout.getOutline(null);
        em = (float)emShape.getBounds2D().getHeight();
	}

    /**
     * Gibt die Art des Formelelements für die Abstandsbestimmung
     * zwischen den Formelelementen zurück.
     * @return Art des Formelelements für die Abstandsbestimmung
     * @see math4u2.view.formula.FormulaElement#getSpacingClass()
     */
    public Class getSpacingClass() {
        return getClass();
    }

    /**
     * Gibt die Darstellungsumgebung dieses Formelelements zurück.
     * @return Darstellungsumgebung
     */
    public FormulaRenderContext getRenderContext() {
    	return renderContext;
    }

    /**
     * Legt die Darstellungsumgebung dieses Formelelements fest.
     * @param frc neue Darstellungsumgebung
     */
    public void setRenderContext(FormulaRenderContext frc) {
		if (frc!=renderContext) {
	    	renderContext = frc;
	    	rebuild();
		}
    }

}