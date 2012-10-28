package math4u2.view.formula;

import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasse für abgeschlossene Teilformeln (z.B. 1, x, sin, etc.).
 * "Ord" steht für "ordinary" (gewöhnlich).
 * 
 * @author Erich Seifert
 * @version $Revision: 1.8 $
 */
public class OrdBox extends StringBox {
    private static final Pattern PATTERN_VAR = Pattern.compile("[\\p{Alnum}_\\-']+");
    private static final Pattern PATTERN_CONST = Pattern.compile("\\p{Digit}.*");

    /**
	 * Konstruktor, der ein neues <code>OrdBox</code>-Objekt
	 * erzeugt und initialisiert. Dabei wird automatisch überprüft,
	 * ob der übergebene Inhalt einen Variablennamen darstellt
     * @param frc Darstellungsumgebung
	 * @param content Inhalt, der dargestellt werden soll
	 */
    public OrdBox(FormulaRenderContext frc, String content) {
        super(frc, content, false);

        Matcher constMat = PATTERN_CONST.matcher(content);
        if (!constMat.matches()) {
            Matcher varMat = PATTERN_VAR.matcher(content);
            if (varMat.matches()) {
	            setFontStyle(Font.ITALIC);
	        }
        }
    }
}