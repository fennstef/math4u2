package math4u2.application.resource;

import java.io.File;

import math4u2.util.io.file.FileUtils;

/**
 * Verwaltung aller Pfade, die verwendet werden
 * 
 * @author Fenn Stefan
 */
public class Folders {

	/**
	 * Hauptpfad zur Anwendung
	 */
	public static final String MATH4U2_FOLDER = FileUtils.urlToUriPath(ClassLoader
			.getSystemResource(""));


	public static final File MATH4U2_FOLDER_FILE = new File(MATH4U2_FOLDER);

	/**
	 * Pfad zu den XML-Übungen
	 */
	public static final String XML_FOLDER = FileUtils.urlToUriPath(ClassLoader
			.getSystemResource("math4u2/xml/"));

	/**
	 * Pfad zu den Hilfe-Datein
	 */
	public static final String HELP_FOLDER = FileUtils.urlToUriPath(ClassLoader
			.getSystemResource("math4u2/doc/hilfe/"));

	/**
	 * Pfad zu den temp-Dateien der Serververzeichnisse
	 */
	public static final String TEMP_FOLDER_SC = "math4u2/temp/sc/";

	/**
	 * Pfad zu den temp-xml-Dateien
	 */
	public static final String TEMP_FOLDER_XML = "math4u2/temp/xml/";

	/**
	 * Pfad zu Intialisierungs Xml-Datei
	 */
	public static final File INIT_XML_FILE = new File(
			Folders.MATH4U2_FOLDER_FILE, "math4u2/conf/initialisierung.xml");

	/**
	 * Pfad zur Reinitialisierungs Xml-Datei
	 */
	public static final File REINIT_XML_FILE = new File(
			Folders.MATH4U2_FOLDER_FILE, "math4u2/conf/reInit.xml");
}// class Folders
