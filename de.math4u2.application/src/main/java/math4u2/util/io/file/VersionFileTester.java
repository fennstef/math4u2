package math4u2.util.io.file;

import java.io.File;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import math4u2.exercises.NoMath4u2DocumentException;
import math4u2.exercises.SimpleTagParser;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.string.VersionChecker;
import math4u2.util.string.VersionChecker.VersionException;
import math4u2.application.resource.Versions;

/**
 * Untersucht, ob die Datei eine XML-Endung besitzt ist und ob die Version
 * gültig ist
 * 
 * @author Fenn Stefan
 */
public class VersionFileTester implements FileTester {

	public boolean test(File file) {
		GenericFileFilter gff = new GenericFileFilter("*.xml");
		if (!gff.accept(file))
			return true;

		SimpleTagParser stp = new SimpleTagParser(file.toString());
		Map map=null;
		try {
			map = stp.parseTagText(new String[][] { { "version" } });
		} catch (XmlPullParserException e) {
			ExceptionManager.doError("Fehler beim Parsen von der Datei "+file.getAbsolutePath());
		} catch (NoMath4u2DocumentException e) {
			return true;
		}
		return test((String) map.get("version"), file.getName());
	} //test

	public boolean test(String version, String fileStr) {
		boolean valid = versionValidation(version);
		if (!valid) {
			ExceptionManager.doError("Die Datei\n" + fileStr
					+ "\nenthält eine falsche Version ( Ver: " + version
					+ " ).\nAktuelle unterstützte Version: "
					+ Versions.MIN_XML_VERSION +" bis "+Versions.MAX_XML_VERSION, "Versionsfehler");
		} //if
		return valid;
	} //test

	private boolean versionValidation(String version) {
		try {
			VersionChecker.checkVersion(version, Versions.MIN_XML_VERSION);
			VersionChecker.checkVersion(Versions.MAX_XML_VERSION, version);
			return true;
		} catch (VersionException e) {
			return false;
		}
	} //versionValidation

} //class VersionFileTester
