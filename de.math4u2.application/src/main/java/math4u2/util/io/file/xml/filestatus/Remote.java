package math4u2.util.io.file.xml.filestatus;

import java.io.File;

import javax.swing.ImageIcon;

import math4u2.application.resource.Folders;
import math4u2.application.resource.Images;
import math4u2.exercises.ui.ESkin;
import math4u2.exercises.ui.ETreeCellRenderer;
import math4u2.util.io.file.FileUtils;
import math4u2.util.net.HTTP;

/**
 * Die Datei liegt auf einem Server und es ist nur eine Kurzbeschreibung im
 * Speicher vorhanden.
 * 
 * @author Michael Lichtenstern
 */
public class Remote extends AbstractFileStatus {

	/**
	 * Name des Servers
	 */
	private String serverName = "";

	/**
	 * Setzt Remote-Icon und ContextStyle
	 * 
	 * @param etcl
	 *            ETreeCellRenderer
	 */
	public void showTreeIssue(ETreeCellRenderer etcl) {
		etcl.setIcon(ETreeCellRenderer.getIssueIconRemote());
		this.showTreeIssueSetFont(etcl, ESkin.styleContext.getStyle("strong"));
	}

	/**
	 * Setze Namen und Status der Datei auf Remote.
	 * 
	 * @param filename
	 *            Dateiname
	 */
	public void setStatus(String filename) {
		this.setFilename(filename);
		this.setLocal(false);
	}

	/**
	 * Gibt den Namen des Servers zurueck.
	 * 
	 * @return Name des Servers
	 */
	public String getServer() {
		return serverName;
	}

	/**
	 * Setzt den Namen des Servers.
	 * 
	 * @param server
	 *            Name des Servers
	 */
	public void setServer(String server) {
		this.serverName = server;
	}

	/**
	 * Holt die Datei vom Server und legt diese temporaer ab.
	 */
	public void getRemoteFile() {
		String remoteURI = this.getFilename();
		this.setFilename(Folders.TEMP_FOLDER_XML
				+ HTTP.removeProtocolFromURL(this.getFilename()));
		FileUtils.writeTempFile(new File(this.getFilename()), HTTP
				.getHTTPContent(remoteURI));
	}
	
	public String toString(){
	    return "server";
	}
	
	public ImageIcon getIcon(){
	    return Images.REMOTE_FILE;
	}
	
	public String getDescription(){
	    return "Datei im Internet";
	}
}