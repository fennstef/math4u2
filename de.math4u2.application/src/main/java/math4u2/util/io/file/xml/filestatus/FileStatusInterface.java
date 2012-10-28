package math4u2.util.io.file.xml.filestatus;

import java.util.List;

import javax.swing.ImageIcon;

import math4u2.exercises.ui.ETreeCellRenderer;
import math4u2.exercises.ui.ExercisePanel;

/**
 * Aktueller Status der Datei. <br>
 * <br>
 * Eine Datei kann lokal, remote oder temporaer vorhanden sein. <br>
 * Local = die Datei liegt im xml-Verzeichnis und wird beim naechsten Starten
 * der Applikation initalisiert. <br>
 * Remote = die Datei liegt auf einem Server und es ist nur eine
 * Kurzbeschreibung im Speicher vorhanden. <br>
 * Temp = die Datei ist vom Server geholt und liegt in einem tempoaeren
 * Verzeichnis, welches beim Herunterfahren bzw. Starten der Applikation
 * bereinigt wird.
 * 
 * @author Michael Lichtenstern
 */
public interface FileStatusInterface {

	/**
	 * Prueft ob aktuelles Element bereits gecachet ist. <br>
	 * Enfernt alle Zeichenflächen und gibt aktuelles Element zurueck.
	 * 
	 * @param ep
	 *            ExercisePanel
	 * @param curElement
	 *            aktuelles Element
	 * @return aktuelles Element
	 */
	Object manageAction(ExercisePanel ep, Object curElement);

	/**
	 * Setzt entsprechendes Icon und ContextStyle.
	 * 
	 * @param etcl
	 *            ETreeCellRenderer
	 */
	void showTreeIssue(ETreeCellRenderer etcl);

	/**
	 * Setze Namen und Status der Datei.
	 * 
	 * @param filename
	 *            Dateiname
	 */
	void setStatus(String filename);

	/**
	 * Gibt den Namen der Datei zurueck.
	 * 
	 * @return Name der Datei
	 */
	String getFilename();

	/**
	 * Setzt den Namen der Datei.
	 * 
	 * @param string
	 *            Name der Datei
	 */
	void setFilename(String string);
	
	/**
	 * Gibt ein Icon für diesen Status zurück 
	 *
	 */
	ImageIcon getIcon();
	
	/**
	 * Gibt eine textuelle Beschreibung zurück
	 */
	String getDescription();
	
	/**
	 * Setzen der Schlagwörter
	 * 
	 * @author Fenn Stefan
	 */
	void setKeyWords(List keywords);
	
	/**
	 * Holen der Schlagwörter
	 * 
	 * @author Fenn Stefan
	 */
	List getKeyWords();
	
}