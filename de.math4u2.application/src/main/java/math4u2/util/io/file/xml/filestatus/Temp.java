package math4u2.util.io.file.xml.filestatus;

import javax.swing.ImageIcon;

import math4u2.application.resource.Images;
import math4u2.exercises.ui.ESkin;
import math4u2.exercises.ui.ETreeCellRenderer;

/**
 * @author Michael Lichtenstern
 */
public class Temp extends AbstractFileStatus {

	/**
	 * Setzt Temp-Icon und ContextStyle
	 * 
	 * @param etcl
	 *            ETreeCellRenderer
	 */
	public void showTreeIssue(ETreeCellRenderer etcl) {
		etcl.setIcon(ETreeCellRenderer.getIssueIconTemp());
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
	    return "temp";
	}
	
	public ImageIcon getIcon(){
	    return Images.TEMP_FILE;
	}
	
	public String getDescription(){
	    return "Temporär angelegte Datei";
	}
}