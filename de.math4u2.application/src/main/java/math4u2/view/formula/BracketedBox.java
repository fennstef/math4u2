package math4u2.view.formula;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import math4u2.view.layout.BracketLayout;

/**
 * Klasse für eingeklammerte Teilformeln
 * 
 * @author Erich Seifert
 * @version $Revision: 1.10 $
 */
public class BracketedBox extends ContainerBox {
    public static final byte TYPE_PARENTHESE = 0;
    public static final byte TYPE_BRACKET = 1;
    public static final byte TYPE_BRACE = 2;
    public static final byte TYPE_ANGLE = 3;

    public static final byte TYPE_ABS = 4;
    public static final byte TYPE_CEIL = 5;
    public static final byte TYPE_FLOOR = 6;

    private static final Map openBrackets;
    private static final Map closeBrackets;

    private final AtomicBox open;
    private final AtomicBox close;
    private final ContainerBox content;

    static {
        openBrackets = new HashMap();
        closeBrackets = new HashMap();

        // "Normale Klammern"
        openBrackets.put(new Byte(TYPE_PARENTHESE),
        		new BracketBox.Bracket("(", "(", "(", "(", "("));
        closeBrackets.put(new Byte(TYPE_PARENTHESE),
        		new BracketBox.Bracket(")", ")", ")", ")", ")"));

        // "Eckige Klammern"
        openBrackets.put(new Byte(TYPE_BRACKET),
        		new BracketBox.Bracket("[", "[", "[", "[", "["));
        closeBrackets.put(new Byte(TYPE_BRACKET),
        		new BracketBox.Bracket("]", "]", "]", "]", "]"));

        // "Geschweifte Klammern"
        openBrackets.put(new Byte(TYPE_BRACE),
        		new BracketBox.Bracket("{", "{", "{", "{", "{"));
        closeBrackets.put(new Byte(TYPE_BRACE),
        		new BracketBox.Bracket("}", "}", "}", "}", "}"));

        // "Spitze Klammern"
        openBrackets.put(new Byte(TYPE_ANGLE),
        		new BracketBox.Bracket("<", "<", "<", "<", "<"));
        closeBrackets.put(new Byte(TYPE_ANGLE),
        		new BracketBox.Bracket(">", ">", ">", ">", ">"));
        
        // "Betrag Klammern"
        openBrackets.put(new Byte(TYPE_ABS),
        		new BracketBox.Bracket("|", "|", "|", "|", "|"));
        closeBrackets.put(new Byte(TYPE_ABS),
        		new BracketBox.Bracket("|", "|", "|", "|", "|"));
    }

    /**
     * Konstruktor, der ein neues <code>BracketedBox</code>-Objekt
     * mit dem Standard-Klammertyp erzeugt und initialisert.
     * @param frc Darstellungsoptionen
     */
    public BracketedBox(FormulaRenderContext frc) {
        this(frc, TYPE_PARENTHESE);
    }

    /**
     * Konstruktor, der ein neues <code>BracketedBox</code>-Objekt
     * eines bestimmten Klammertyps erzeugt und initialisert.
     * @param frc Darstellungsoptionen
     * @param type Typ für Anfangs- und Endklammer
     */
    public BracketedBox(FormulaRenderContext frc, byte type) {
        this(frc, type, type);
    }

    /**
     * Konstruktor, der ein neues <code>BracketedBox</code>-Objekt
     * mit unterschiedlichen Anfangs- und Endklammern erzeugt
     * und initialisert.
     * @param frc Darstellungsoptionen
     * @param openType Typ der Anfangsklammer
     * @param closeType Typ der Endklammer
     */
    public BracketedBox(FormulaRenderContext frc, byte openType, byte closeType) {
        super(frc);
        
        int bracketSp = Math.round(2f*getDisplayHeight()*FormulaRenderContext.MU);
        addSpacing(0, bracketSp, 0, bracketSp);

        int spV = Math.round(1f*getDisplayHeight()*FormulaRenderContext.MU);
        setLayout(new BracketLayout(0, spV));

		// "Klammer auf"
		open = getOpenBox(openType);
		super.add(open, BracketLayout.LEFT_BRACKET);
		
		// Inhalt
		content = new ContainerBox(getRenderContext());
		if (openType==TYPE_ABS)
			content.addSpacing(0, bracketSp, 0, 0);
		if (closeType==TYPE_ABS)
			content.addSpacing(0, 0, 0, bracketSp);
		super.add(content, BracketLayout.CENTER);

		// Klammer zu
		close = getCloseBox(closeType);
		super.add(close, BracketLayout.RIGHT_BRACKET);
    }

    /**
     * Überschriebene Methode, die neue Unterobjekte nicht dem Behälter
     * hinzufügt sondern dem Hauptinhalt.
     * @see java.awt.Container#add(java.awt.Component)
     */
    public Component add(Component comp) {
        return content.add(comp);
    }

    /**
     * Gibt das Objekt für eine Anfangsklammer betimmten Typs zurück.
     * @param type Typ der Klammer.
     * @return Anfangsklammer des Typs <code>type</code>
     */
    private BracketBox getOpenBox(byte type) {
        BracketBox.Bracket symbol = (BracketBox.Bracket) openBrackets
                .get(new Byte(type));

        if (symbol == null)
            symbol = (BracketBox.Bracket) openBrackets.get(new Byte(
                    TYPE_PARENTHESE));

        BracketBox bracket = new BracketBox(getRenderContext(), symbol);
        return bracket;
    }

    /**
     * Gibt das Objekt für eine Endklammer betimmten Typs zurück.
     * @param type Typ der Klammer.
     * @return Endklammer des Typs <code>type</code>
     */
    private BracketBox getCloseBox(byte type) {
        BracketBox.Bracket symbol = (BracketBox.Bracket) closeBrackets
                .get(new Byte(type));

        if (symbol == null)
            symbol = (BracketBox.Bracket) closeBrackets.get(new Byte(
                    TYPE_PARENTHESE));

        BracketBox bracket = new BracketBox(getRenderContext(), symbol);
        return bracket;
    }

    /**
     * Gibt den Typ für die Abstandsberechnungen im Formellayout zurück.
     * @see math4u2.view.formula.FormulaElement#getSpacingClass()
     */
    public Class getSpacingClass() {
        return getClass();
    }

    /**
     * Gibt die Hauptkompontente, also den Inhalt, dieses
     * Klammerbehälters zurück.
     * @see math4u2.view.formula.ContainerBox#getMainComponent()
     */
    protected Component getMainComponent() {
        return content;
    }

    /**
     * Gibt den Inhalt dieser <code>MatrixBox</code> zurück.
     * @return Inhalt dieser <code>MatrixBox</code>
     */
    protected ContainerBox getContent() {
    	return content;
    }

    public float getRealAscend() {
    	return super.getRealAscend();
    	/*FormulaElement elem = (open!=null) ? open : close;
        if (elem==null) return super.getRealAscend();
        return elem.getRealAscend();*/
    }

    public float getRealDescend() {
    	return super.getRealDescend();
    	/*FormulaElement elem = (open!=null) ? open : close;
        if (elem==null) return super.getRealDescend();
        return elem.getRealDescend();*/
    }
}