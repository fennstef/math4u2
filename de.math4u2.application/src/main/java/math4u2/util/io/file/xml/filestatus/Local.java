package math4u2.util.io.file.xml.filestatus;

import javax.swing.ImageIcon;

import math4u2.application.resource.Images;
import math4u2.exercises.ui.ESkin;
import math4u2.exercises.ui.ETreeCellRenderer;

/**
 * Die Datei liegt im xml-Verzeichnis und wird beim naechsten Starten der
 * Applikation initalisiert.
 * 
 * @author Michael Lichtenstern
 */
public class Local extends AbstractFileStatus {

	/**
	 * Setzt Local-Icon und ContextStyle.
	 * 
	 * @param etcl
	 *            ETreeCellRenderer
	 */
	public void showTreeIssue(ETreeCellRenderer etcl) {
		etcl.setIcon(ETreeCellRenderer.getIssueIconLocal());
		this.showTreeIssueSetFont(etcl, ESkin.styleContext.getStyle("strong"));
	}

	/**
	 * Setze Namen und Status der Datei auf Local.
	 * 
	 * @param filename
	 *            Dateiname
	 */
	public void setStatus(String filename) {
		this.setFilename(filename);
		this.setLocal(true);
	}
	
	public String toString(){
	    return "local";
	}
	
	public ImageIcon getIcon(){
	    return Images.LOCAL_FILE;
	}
	
	public String getDescription(){
	    return "Lokale Datei";
	}
}