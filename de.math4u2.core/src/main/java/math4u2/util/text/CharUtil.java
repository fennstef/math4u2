package math4u2.util.text;

/**
 * @author Fenn Stefan
 */
public class CharUtil {

	/**
	 * Generiert einen Füllstring z.B. mit generateFillString("ab",3) entsethe
	 * "ababab"
	 */
	public static String generateFillString(String s, int n) {
		StringBuffer sb = new StringBuffer(s.length() * n);
		for (int i = 0; i < n; i++) {
			sb.append(s);
		}//for
		return sb.toString();
	}//generateFillString

	/**
	 * Zahlt die Häufigkeit des Buchstaben c im String search
	 */
	public static int count(char c, String search) {
		int count = 0;
		for (int i = 0; i < search.length(); i++)
			if (search.charAt(i) == c)
				count++;
		return count;
	}//count
}//class CharUtil
