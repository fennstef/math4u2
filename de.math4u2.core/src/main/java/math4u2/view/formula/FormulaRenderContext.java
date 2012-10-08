package math4u2.view.formula;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import math4u2.util.exception.ExceptionManager;
import math4u2.util.formulaUtils.PropertyReader;

/**
 * Hilfsmittel f�r den Formelsatz.
 * 
 * @author Erich Seifert
 * @version $Revision: 1.11 $
 */
public class FormulaRenderContext {
    /** Formel-Stil <i>Display</i>. F�r normale Formeln. */
    public static final byte FONTSIZE_D = 0;
    /** Formel-Stil <i>Text</i>. Erste Verkleinerungsstufe. */
    public static final byte FONTSIZE_T = 1;
    /** Formel-Stil <i>Small</i>. Kleine Version der Formel,
     * z.B. f�r Inidices oder Exponenten. */
    public static final byte FONTSIZE_S = 2;
    /** Formel-Stil <i>Small Small</i>. Kleinste Ausf�hrung der Formel,
     * z.B. f�r Indices in Exponenten, usw. */
    public static final byte FONTSIZE_SS = 3;

    /** Faktor, mit dem die Schriftgr��e mutlipliziert werden muss,
     * um auf eine <code>Mathematical Unit</code> (Mathematische
     * Einheit) zu kommen. Dies ist die kleinste Einheit im Formelsatz
     * und wird beispielsweise f�r die Gr��e von Zwischenr�umen benutzt.
     */
    public static final float MU = 1f / 16f;

    /** Die NAmen aller integrierten mathemathischen Funktionen */
    private static final String[] BUILTIN_FUNCTIONS = new String[] {
    	"sin", "cos", "tan", "arcsin", "arccos", "arctan", "e", "log", "ln" };

    /** Name des Standardzeichensatzes */
    private static final String DEFAULT_FONT_NAME = "Serif";

    /** Standardgr��e des Zeichensatzes in Pixeln */
    private static final double DEFAULT_FONT_SIZE = 19.0;

    /** Standardbreite von Linien */
    private static final float LINE_WIDTH = 0.95f * MU;

    /** H�he der Mittelachse gegen�ber der Grundlinie,
     * z.B. f�r Bruchstriche */
    private static final float AXIS_HEIGHT = 5f * MU;
    
    private static final float SPACE_WIDTH = 4f* MU;

    /** Saklierungsfaktor f�r gro�e Operatoren, z.B. f�r Integral,
     * Summe, Produkt */
    private static final float BIGOP_SCALE = 1.75f;

    /** Standard-Umgebung zum Darstellen von Zeichens�tzen */
    private final FontRenderContext DEFAULT_FRC;

    /** Beh�lter f�r mathemathische Symbole und Sonderzeichen */
    private final static PropertyReader symbols = new PropertyReader();
    
    public static Font serif;
    public static Font serif_bold;
    public static Font serif_italic;
    public static Font serif_bold_italic;
    
    public static Font symbol;
    public static Font symbol_bold;
    public static Font symbol_italic;
    public static Font symbol_bold_italic;
    
    public static Font dialog;
    public static Font dialog_bold;
    public static Font dialog_italic;
    public static Font dialog_bold_italic;
    
    private static Map fontCache = new HashMap();
    
    static{
        try {
            symbols.read("math4u2/view/formula/unicodeSymbols.properties");
        } catch (IOException e) {
            ExceptionManager.doError(e);
        }
        String path="math4u2/view/formula/fonts/";        
        serif = loadSpecialFont(path+"serif.ttf");
        serif_bold = loadSpecialFont(path+"serif_b.ttf");
        serif_italic = loadSpecialFont(path+"serif_i.ttf");
        serif_bold_italic = loadSpecialFont(path+"serif_bi.ttf");
        
        dialog = loadSpecialFont(path+"dialog.ttf");
        dialog_bold = loadSpecialFont(path+"dialog_b.ttf");
        dialog_italic = loadSpecialFont(path+"dialog_i.ttf");
        dialog_bold_italic = loadSpecialFont(path+"dialog_bi.ttf");

        symbol = new Font("Lucida Sans", Font.PLAIN, 12);
        symbol_bold = new Font("Lucida Sans", Font.BOLD, 12);
        symbol_italic = new Font("Lucida Sans", Font.ITALIC, 12);
        symbol_bold_italic = new Font("Lucida Sans", Font.ITALIC + Font.BOLD, 12);
    }//static
    
    private static Font loadSpecialFont(String filePath){
		try {
			FileInputStream fis;
			URI uri = FormulaRenderContext.class.getClassLoader().getResource(filePath).toURI();
			File file = new File(uri);
			fis = new FileInputStream(file);
			Font font = Font.createFont(Font.TRUETYPE_FONT, fis);
			fis.close();
			return font;
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Laden der Schriftart");
			return null;
		}
    }//loadSpecialFont
    
    public static Font getFont(String name, int style, float size){
    	FontEntry fe = new FontEntry(name,style,size);
    	Object obj = fontCache.get(fe); 
    	if(obj!=null) return (Font) obj;
    	
    	Font font = null;
    	if(name==null) name="Serif";
    	if(name.equals("Serif")){
    		if(style==Font.PLAIN){
    			font = FormulaRenderContext.serif.deriveFont(size);
    		}else if(style==Font.BOLD){
    			font = FormulaRenderContext.serif_bold.deriveFont(size);
    		}else if(style==Font.ITALIC){
    			font = FormulaRenderContext.serif_italic.deriveFont(size);
    		}else if(style==Font.BOLD+Font.ITALIC){
    			font = FormulaRenderContext.serif_bold_italic.deriveFont(size);
    		}else{
    			ExceptionManager.doError("Fehler beim Finder der Schriftart");
    		}
    	}else if(name.equals("Symbol")){
    		if(style==Font.PLAIN){
    			font = FormulaRenderContext.symbol.deriveFont(size);
    		}else if(style==Font.BOLD){
    			font = FormulaRenderContext.symbol_bold.deriveFont(size);
    		}else if(style==Font.ITALIC){
    			font = FormulaRenderContext.symbol_italic.deriveFont(size);
    		}else if(style==Font.BOLD+Font.ITALIC){
    			font = FormulaRenderContext.symbol_bold_italic.deriveFont(size);
    		}else{
    			ExceptionManager.doError("Fehler beim Finder der Schriftart");
    		}
    	}else if(name.equals("Dialog")){
    		if(style==Font.PLAIN){
    			font = FormulaRenderContext.dialog.deriveFont(size);
    		}else if(style==Font.BOLD){
    			font = FormulaRenderContext.dialog_bold.deriveFont(size);
    		}else if(style==Font.ITALIC){
    			font = FormulaRenderContext.dialog_italic.deriveFont(size);
    		}else if(style==Font.BOLD+Font.ITALIC){
    			font = FormulaRenderContext.dialog_bold_italic.deriveFont(size);
    		}else{
    			ExceptionManager.doError("Fehler beim Finder der Schriftart");
    		}    		
    	}else{        	
    		font = new Font(name, style, Math.round(size));
    	}   
    	fontCache.put(fe, font);
    	return font;
    }//getFont

    /**
     * Konstruktor, der ein neues <code>FormulaRenderContext</code>-Objekt
     * erzeugt und initialisiert.
     */
    public FormulaRenderContext() {
    	DEFAULT_FRC = new FontRenderContext(null, true, true);
    }

    /**
     * Gibt den standardm�igen <code>FontRenderContext</code> zur�ck.
     * Dieser wird beispielsweise ben�tigt, um die Vektor-Umrisse einer
     * Schrift zu bekommen oder die Dimensionen zu berechnen, die eine
     * bestimmte Zeichenkette in einer bestimmten Schrift auf dem
     * Bildschirm einnehmen wird. Au�erdem gibt der <code>FontRenderContext</code>
     * an, ob die Darstellung mit Kantengl�ttung und/oder auf
     * Subpixel-Basis erfolgt.
     * @return <code>FontRenderContext</code>-Objekt.
     */
    public final FontRenderContext getDefaultFontRenderContext() {
        return DEFAULT_FRC;
    }

    /**
     * Gibt die Standard-Schriftgr��e f�r Formeln zur�ck.
     * @return Schriftgr��e in Pixeln.
     */
    public final int getDefaultFontSize() {
//        int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
//        int screenSize = (int) Math.round(DEFAULT_FONT_SIZE * screenRes / 72.0);
//        return screenSize;
    	  return (int) DEFAULT_FONT_SIZE;
    }

    /**
     * Gibt den Namen der Standardschrift f�r Formeln zur�ck.
     * @return Namen des Schriftsatzes
     */
    public final String getDefaultFontName() {
        return DEFAULT_FONT_NAME;
    }

    /**
     * Gibt ein Feld mit den implementierten Standardfunktionen zur�ck.
     * Dies ist beispielsweise f�r die Darstellung wichtig, um entscheiden
     * zu k�nnen, wie bestimmte Schl�sselw�rter dargestellt werden sollen.
     * @return Feld mit allen implementierten Standardfunktionen
     */
    public final String[] getBuiltinFunctions() {
        return BUILTIN_FUNCTIONS;
    }

    /**
     * Gibt die Standard-Linienst�rke in <i>MU</i> zur�ck.
     * @return Standard-Linienst�rke in <i>MU</i>
     * @see #MU
     */
    public final float getLineWidth() {
        return LINE_WIDTH;
    }

    /**
     * Gibt die H�he der Bruchstrichlinie gegen�ber der Grundlinie
     * in <i>MU</i> zur�ck.
     * @return H�he der Bruchstrichlinie in <i>MU</i>
     * @see #MU
     */
    public final float getAxisHeight() {
        return AXIS_HEIGHT;
    }
    
    /**
     * Gibt die Breite eines Leerzeichens in <i>MU</i> zur�ck.
     * @return
     */
    public final float getSpaceWidth(){
        return SPACE_WIDTH;
    }

    /**
     * Gibt den Faktor zur�ck, mit dem gro�e Operatoren, (z.B.
     * Integral, Summe, Produkt) skaliert werden sollen.
     * @return Skalierungsfaktor f�r gro�e Operatoren
     */
    public final float getBigOpScale() {
    	return BIGOP_SCALE;
    }
    
    /**
     * Gibt den Faktor f�r einen bestimmten Formelstil
     * (<code>FONTSIZE_D</code>, <code>FONTSIZE_T</code>,
     * <code>FONTSIZE_S</code>, <code>FONTSIZE_SS</code>)
     * zur�ck, mit dem die Schriftgr��e multipliziert werden muss,
     * um die reale Schriftgr��e zu erhalten.
     * @param relSize Formelstil
     * @return Mit Formelstil verbundener Saklierungsfaktor
     * @see #FONTSIZE_D
     * @see #FONTSIZE_T
     * @see #FONTSIZE_S
     * @see #FONTSIZE_SS
     */
    public final float getRelFontSize(byte relSize) {
        switch (relSize) {
        case FONTSIZE_D:
            return 1f;
        case FONTSIZE_T:
            return 1f;
        case FONTSIZE_S:
            return 0.75f;
        case FONTSIZE_SS:
            return 0.6f;
        default:
            return 1f;
        }
    }

    /**
     * Verkleinert einen gegebenen Formelstil um eine Stufe.
     * Die Zahl der Stufen ist begrenzt, so dass die Verkleinerung
     * nur bis zu einer gewissen Stufe stattfindet.
     * @param relSize Formelstil
     * @return n�chstkleinerer Formelstil
     */
    public final byte shrinkRelSize(byte relSize) {
        ++relSize;
        if (relSize > FONTSIZE_SS)
            relSize = FONTSIZE_SS;
        return relSize;
    }

    /**
     * Gibt das Symbol f�r einen bestimmtes K�rzel (<code>token</code>)
     * zur�ck. Damit k�nnen einfach Sonderzeichen �ber deren entsprechende
     * K�rzel angesprochen werden.
     * @param token K�rzel f�r das das Symbol zur�ckgegeben werden soll
     * @return Symbol f�r das das �bergebene K�rzel
     */
    public static final String getSymbol(String token) {
        String s = symbols.getProperty(token);
        if (s == null)
            return token;
        return s;
    }
    
    /**
     * Klasse, die Font-Name, Stil und Gr��e speichert. Dies w�rd f�r den
     * Font-Cache gebraucht.
     */
    public static class FontEntry{
    	public String name;
    	public int style;
    	public float size;
    	
    	public FontEntry(String name, int style, float size){
    		this.name = name;
    		this.style = style;
    		this.size = size;
    	}//Konstruktor

		public boolean equals(Object obj) {
			if(!(obj instanceof FontEntry)) return false;
			FontEntry fe = (FontEntry) obj;
			return name.equals(fe.name) && style==fe.style && size==size;
		}

		public int hashCode() {
			return name.hashCode()+style+Float.toString(size).hashCode();
		}
    	
    }//class FontEntry
}