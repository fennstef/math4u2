package math4u2.util.string;

import java.util.*;

/**
 * @author Fenn Stefan
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class VersionChecker {

	/**
	 * Überprüft, ob die eine Version in Ordnung ist.
	 * 
	 * @param versionReal
	 *            die echte Versionsnummer z.B. "1.4"
	 * @param versionMustBe
	 *            die Versionsnummer die mindestens enthalten sein muß z.B.
	 *            "1.2"
	 * @return <code>true</code> falls die Version in Ordnung ist.
	 */
	public static void checkVersion(String versionReal, String versionMustBe)
			throws VersionException {
		boolean isValid = true;
		
		if(versionReal==null || versionMustBe==null)
			throw new  RuntimeException("Es konnte die Version nicht gelesen werden.");
		StringTokenizer stR = new StringTokenizer(versionReal, "._-br");
		StringTokenizer stM = new StringTokenizer(versionMustBe, "._-br");

		while (stR.hasMoreTokens()) {
			String strR = stR.nextToken();
			if (!stM.hasMoreTokens())
				break;
			String strM = stM.nextToken();
			//Nullen anfügen 1.4 u. 1.12 wird zu 1.40 u. 1.12
			while (strR.length() != strM.length()) {
				if (strR.length() < strM.length())
					strR += "0";
				else
					strM += "0";
			}//while
			int r = Integer.parseInt(strR);
			int m = Integer.parseInt(strM);
			if (r < m) {
				isValid = false;
				break;
			} else if (r > m)
				break;
		}//while

		if (!isValid)
			throw new VersionChecker.VersionException(versionMustBe,
					versionReal);
	} //checkVersion

	/**
	 * Überprüft, ob die Java-Version in Ordnung ist.
	 * 
	 * @param versionStr
	 *            Versionsnummer als String z.B. "1.4"
	 * @return <code>true</code> falls die Version in Ordnung ist.
	 */
	public static void checkJavaVersion(String versionMustBe)
			throws VersionException {
		String versionReal = System.getProperty("java.runtime.version");
		checkVersion(versionReal, versionMustBe);
	} //checkJavaVersion

	public static class VersionException extends Exception {
		private String versionMustBe, versionReal;

		public VersionException(String versionMustBe, String versionReal) {
			super();
			this.versionMustBe = versionMustBe;
			this.versionReal = versionReal;
		} //Konstruktor

		public String getVersionMustBe() {
			return versionMustBe;
		} //getVersionMustBe

		public String getVerstionReal() {
			return versionReal;
		} //getVersionReal

	} //inner class VersionException

} //JavaVersionChecker
