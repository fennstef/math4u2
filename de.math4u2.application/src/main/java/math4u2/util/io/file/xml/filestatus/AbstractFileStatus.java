package math4u2.util.io.file.xml.filestatus;

import java.util.List;

import javax.swing.text.Style;

import math4u2.exercises.EIssue;
import math4u2.exercises.ui.ESkin;
import math4u2.exercises.ui.ETreeCellRenderer;
import math4u2.exercises.ui.ExercisePanel;
import math4u2.view.graph.drawarea.DrawAreasManager;

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
abstract class AbstractFileStatus implements FileStatusInterface {

	protected String filename;

	protected boolean local;
	
	protected List keywords;

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
	public Object manageAction(ExercisePanel ep, Object curElement) {
		ep.checkCurrentIssue((EIssue) curElement);
		DrawAreasManager.unregisterAllDrawAreas();
		return ExercisePanel.getIssue().getStep(0);
	}

	/**
	 * Gibt den Namen der Datei zurueck.
	 * 
	 * @return Name der Datei
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Setzt den Namen der Datei.
	 * 
	 * @param string
	 *            Name der Datei
	 */
	public void setFilename(String string) {
		filename = string;
	}

	/**
	 * Setzt den Status (Local oder Remote) der Datei.
	 * 
	 * @param status
	 *            Status der Datei
	 */
	public void setLocal(boolean status) {
		local = status;
	}

	/**
	 * Setzt ContextStyle fuer die Darstellung.
	 * 
	 * @param etcl
	 *            ETreeCellRenderer
	 * @param style
	 *            ContextStyle
	 */
	protected void showTreeIssueSetFont(ETreeCellRenderer etcl, Style style) {
		etcl.setFont(ESkin.styleContext.getFont(style));
		etcl.setTextNonSelectionColor(ESkin.styleContext.getForeground(style));
	}//showTreeIssueSetFont
	
	
	public void setKeyWords(List keywords) {
		this.keywords=keywords;
	}//setKeyWords
	
	public List getKeyWords() {
		return keywords;
	}//getKeyWords
}