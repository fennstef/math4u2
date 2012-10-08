package math4u2.util.xml.sax;

import java.util.LinkedList;

import org.xml.sax.Attributes;

/**
 * Handler fuer die Inhaltsangabe eines Servers (servercontents-Datei).
 * 
 * @author Michael Lichtenstern, Fenn Stefan
 * 
 * @see math4u2.util.xml.sax.ServerContentsFile
 */
public class ServerContentsFileXMLHandler extends XMLHandler {

	/**
	 * LinkedList fuer alle ServerContentsFile-Objekte
	 */
	private LinkedList xmlfiles = new LinkedList();
	
	private ServerContentsFile scf;
	
	/** Cache für Author-String */
	private String cacheAuthor="";
	
	/** Cache für Keyword-String */
	private String cacheKeyword="";
	
	/** Cache für XmlVersion-String */
	private String cacheXmlVersion="";

	/** Cache für Title-String */
	private String cacheTitle="";
	
	/** Cache für Summary-String */
	private String cacheSummary="";	
	
	/** Cache für MathLocation-String */
	private String cacheMathLocation="";	
	
	/** Cache für Uri-Strin */
	private String cacheUri="";

	/**
	 * Wird am Anfang eines Elements aufgerufen. <br>
	 * <br>
	 * Legt den Namen des Tags auf dem Stack ab. <br>
	 * Erzeugt bei file-Tag ein neues ServerContentsFile-Objekt.
	 */
	public void startElement(String uri, String name, String qName,
			Attributes attributes) {
		s.push(qName);
		if (qName.equalsIgnoreCase("file")) {
			scf = new ServerContentsFile();
		}
	}

	/**
	 * Wird aufgerufen wenn Daten in einem Tag vorhanden sind. <br>
	 * <br>
	 * Holt sich das aktuelle Element vom Stack und setzt dann den
	 * dementsprechende Klassenmember im ServerContentsFile.
	 */
	public void characters(char[] ch, int start, int length) {
		String content = this.getContent(ch, start, length);
		String element = (String) s.peek();
		if (element.equalsIgnoreCase("xml-version")) {
			cacheXmlVersion += content;
		} else if (element.equalsIgnoreCase("title")) {
			cacheTitle += content;
		} else if (element.equalsIgnoreCase("author")) {
			cacheAuthor += content;
		} else if (element.equalsIgnoreCase("summary")) {
			cacheSummary += content;
		} else if (element.equalsIgnoreCase("math-location")) {
			cacheMathLocation += content;
		} else if (element.equalsIgnoreCase("uri")) {
			cacheUri += content;
		} else if (element.equalsIgnoreCase("keyword")) {
			cacheKeyword += content;
		}
	}//characters

	/**
	 * Wird am Ende eines Elements aufgerufen. <br>
	 * <br>
	 * Entfernt das Element wieder vom Stack. <br>
	 * Fuegt bei file-Tag das ServerContentsFile-Objekt der LinkedList hinzu.
	 */
	public void endElement(String uri, String name, String qName) {
		s.pop();
		if (qName.equalsIgnoreCase("file")) {
			xmlfiles.add(scf);
		}else if(qName.equalsIgnoreCase("author")){
			scf.setAuthor(cacheAuthor.split(","));
			cacheAuthor = "";
		}else if(qName.equalsIgnoreCase("keyword")){
			scf.addKeyword(cacheKeyword);
			cacheKeyword = "";
		}else if(qName.equalsIgnoreCase("xml-version")){
			scf.setXmlVersion(cacheXmlVersion);
			cacheXmlVersion = "";
		}else if(qName.equalsIgnoreCase("title")){
			scf.setTitle(cacheTitle);
			cacheTitle = "";
		}else if(qName.equalsIgnoreCase("summary")){
			scf.setSummary(cacheSummary);
			cacheSummary="";
		}else if(qName.equalsIgnoreCase("math-location")){
			scf.setMathLocation(cacheMathLocation);
			cacheMathLocation = "";
		}else if(qName.equalsIgnoreCase("uri")){
			scf.setUri(cacheUri);
			cacheUri = "";
		}
			
	}//endElement

	/**
	 * Gibt eine LinkedList aller ServerContentsFile-Objekt zurueck.
	 * 
	 * @return LinkedList aller ServerContentsFile-Objekte
	 */
	public LinkedList getFiles() {
		return xmlfiles;
	}
}