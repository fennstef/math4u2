package math4u2.application.resource;

import java.io.File;

import math4u2.util.io.file.FileUtils;
import static math4u2.util.io.file.FileResolver.*;

/**
 * Verwaltung aller Pfade, die verwendet werden
 * 
 * @author Fenn Stefan
 */
public class Folders {

	/**
	 * Hauptpfad zur Anwendung
	 */
	public static final String MATH4U2_FOLDER = resolveToUri("", Folders.class);

	public static final File MATH4U2_FOLDER_FILE = resolve("", Folders.class);

	/**
	 * Pfad zu den XML-Übungen
	 */
	public static final String XML_FOLDER = resolveToUri("math4u2/xml/", Folders.class);

	/**
	 * Pfad zu den Hilfe-Datein
	 */
	public static final String HELP_FOLDER = resolveToUri("math4u2/doc/hilfe/", Folders.class);

	/**
	 * Pfad zu den temp-Dateien der Serververzeichnisse
	 */
	public static final File TEMP_FOLDER_SC = resolve("math4u2/temp/sc/", Folders.class);

	/**
	 * Pfad zu den temp-xml-Dateien
	 */
	public static final File TEMP_FOLDER_XML = resolve("math4u2/temp/xml/", Folders.class);

	/**
	 * Pfad zu Intialisierungs Xml-Datei
	 */
	public static final File INIT_XML_FILE = resolve("math4u2/conf/initialisierung.xml", Folders.class);

	/**
	 * Pfad zur Reinitialisierungs Xml-Datei
	 */
	public static final File REINIT_XML_FILE = resolve("math4u2/conf/reInit.xml", Folders.class);
}// class Folders
