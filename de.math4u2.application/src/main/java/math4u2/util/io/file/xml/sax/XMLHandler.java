package math4u2.util.io.file.xml.sax;

import java.util.Stack;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Abstrakter XMLHandler
 * 
 * @author Michael Lichtenstern
 */
abstract class XMLHandler extends DefaultHandler {

	/**
	 * Stack fuer die Ablage der Elemente.
	 */
	protected Stack s = new Stack();

	/**
	 * Schneidet ein Character-Array zu und gibt einen String zurueck. <br>
	 * 
	 * @param characterArray
	 *            Array aus Charactern
	 * @param start
	 *            Position zum beginnen
	 * @param length
	 *            Laenge des Zuschnitts
	 * @return Zuschnitt als String
	 */
	protected String getContent(char[] characterArray, int start, int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = start; i < start + length; i++) {
			sb.append(characterArray[i]);
		}
		if(sb==null) return "";
		return sb.toString();
	}
}