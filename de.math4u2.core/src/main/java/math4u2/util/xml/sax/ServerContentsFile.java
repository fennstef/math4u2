package math4u2.util.xml.sax;

import java.util.LinkedList;
import java.util.List;

/**
 * Bean fuer die Kurzbeschreibung einer Server-Datei.
 * 
 * @author Michael Lichtenstern
 * 
 * @see math4u2.util.xml.sax.ServerContentsFileXMLHandler
 */
public class ServerContentsFile {

	private String xmlVersion;

	private String title;

	private String[] author;

	private String summary;

	private String mathLocation;

	private String uri;

	private List keywords=new LinkedList();

	private String level;

	private String release;

	/**
	 * Gibt den Author der XML-Datei zurueck.
	 * 
	 * @return Author der XML-Datei
	 */
	public String[] getAuthor() {
		return author;
	}

	/**
	 * Gibt die Schluesselwoerter der XML-Datei zurueck.
	 * 
	 * @return Schluesselwoerter der XML-Datei
	 */
	public List getKeywords() {
		return keywords;
	}

	/**
	 * Gibt das Level der XML-Datei zurueck.
	 * 
	 * @return Level der XML-Datei
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * Gibt die MathLocation der XML-Datei zurueck.
	 * 
	 * @return MathLocation der XML-Datei
	 */
	public String getMathLocation() {
		return mathLocation;
	}

	/**
	 * Gibt die Release-Version der XML-Datei zurueck.
	 * 
	 * @return Release-Version der XML-Datei
	 */
	public String getRelease() {
		return release;
	}

	/**
	 * Gibt die Zusammenfassung der XML-Datei zurueck.
	 * 
	 * @return Zusammenfassung der XML-Datei
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * Gibt den Titel der XML-Datei zurueck.
	 * 
	 * @return Titel der XML-Datei
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gibt die URI der XML-Datei zurueck.
	 * 
	 * @return URI der XML-Datei
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Gibt die XML-Version der XML-Datei zurueck.
	 * 
	 * @return XML-Version
	 */
	public String getXmlVersion() {
		return xmlVersion;
	}

	/**
	 * Setzt den Author der XML-Datei.
	 * 
	 * @param strings
	 *            Author der XML-Datei
	 */
	public void setAuthor(String[] strings) {
		author = strings;
	}

	/**
	 * Setzt die Keywords der XML-Datei.
	 * 
	 * @param objects
	 *            Keywords der XML-Datei
	 */
	public void addKeyword(String keyword) {
		keywords.add(keyword);
	}

	/**
	 * Setzt das Level der XML-Datei.
	 * 
	 * @param string
	 *            Level der XML-Datei
	 */
	public void setLevel(String string) {
		level = string;
	}

	/**
	 * Setzt die MathLocation der XML-Datei.
	 * 
	 * @param string
	 *            MathLocation der XML-Datei
	 */
	public void setMathLocation(String string) {
		mathLocation = string;
	}

	/**
	 * Setzt die Release-Version der XML-Datei.
	 * 
	 * @param string
	 *            Release-Version der XML-Datei
	 */
	public void setRelease(String string) {
		release = string;
	}

	/**
	 * Setzt die Zusammenfassung der XML-Datei.
	 * 
	 * @param string
	 *            Zusammenfassung der XML-Datei
	 */
	public void setSummary(String string) {
		summary = string;
	}

	/**
	 * Setzt den Titel der XML-Datei.
	 * 
	 * @param string
	 *            Titel der XML-Datei
	 */
	public void setTitle(String string) {
		title = string;
	}

	/**
	 * Setzt die Uri der XML-Datei.
	 * 
	 * @param string
	 *            Uri der XML-Datei
	 */
	public void setUri(String string) {
		uri = string;
	}

	/**
	 * Setzt die XML-Version der XML-Datei.
	 * 
	 * @param string
	 *            XML-Version der XML-Datei
	 */
	public void setXmlVersion(String string) {
		xmlVersion = string;
	}
}