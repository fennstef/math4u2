package math4u2.exercises.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import math4u2.application.resource.Folders;
import math4u2.exercises.EIssue;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.io.file.FileCopy;
import math4u2.util.io.file.xml.filestatus.Remote;
import math4u2.view.gui.XMLManager;

/**
 * PopupMenü für Issues des Baumes.
 * 
 * @author Michael Lichtenstern
 */
public class ETreePopupMenuIssue extends JPopupMenu {

	private JMenuItem store;

	private JMenuItem delete;

	private JMenuItem download;

	private ETree eTree;

	private JOptionPane chechStoreOptionPane = new JOptionPane(
			"Die Datei ist bereits im xml-Verzeichnis vorhanden.\n"
					+ "Wenn Sie die Datei speichern, wird die alte Version überschrieben.\n"
					+ "Wollen Sie speichern?", JOptionPane.WARNING_MESSAGE,
			JOptionPane.YES_NO_OPTION);

	private JDialog checkStoreDialog;

	/**
	 * Initalisiert das PopupMenü fuer Issues des Baumes.
	 * 
	 * @param eTree
	 *            Baum
	 */
	public ETreePopupMenuIssue(ETree eTree) {
		this.eTree = eTree;
		checkStoreDialog = chechStoreOptionPane.createDialog(this, "Speichern");
		store = new JMenuItem("speichern");
		store.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object obj = getETree().getActualIssueOrExcercise();
				if(!(obj instanceof EIssue)) return;
				EIssue ie = (EIssue) obj;
				String filename = ie.getStatus().getFilename();
				File source = new File(filename);
				File destination = new File(Folders.XML_FOLDER
						+ source.getName());
				if (destination.exists()) {
					checkStoreDialog.setVisible(true);
					int value = ((Integer) chechStoreOptionPane.getValue())
							.intValue();
					if (value == 0) {
						this.storeFile(source, destination);
					}
				} else {
					this.storeFile(source, destination);
				}
				XMLManager.showRefreshFull();
			}

			public void storeFile(File source, File destination) {
				try {
					FileCopy.copyFile(source, destination);
					System.gc();
					if (!source.delete()) {
						ExceptionManager
								.doError("Die Datei konnte nicht gelöscht werden");
					}
				} catch (IOException e) {
					ExceptionManager
							.doError("Die Datei konnte nicht gespeichert werden", e);
				}
			}
		});
		delete = new JMenuItem("löschen");
		delete.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object obj = getETree().getActualIssueOrExcercise();
				if(!(obj instanceof EIssue)) return;
				EIssue ie = (EIssue) obj;
				String filename = ie.getStatus().getFilename();
				File source = new File(filename);
				if (!source.delete()) {
					ExceptionManager
							.doError("Die Datei konnte nicht gelöscht werden!");
				}
				XMLManager.showRefreshFull();
			}
		});
		download = new JMenuItem("herunterladen");
		download.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				downloadRemoteFile();
			}
		});
		this.add(store);
		this.add(delete);
		this.add(download);
		this.setInvoker(eTree);
		this.setLightWeightPopupEnabled(true);
	}

	/**
	 * Leadt die Remote-Datei vom Server herunter und aktualisiert den Baum.
	 */
	public void downloadRemoteFile() {
		Object obj = getETree().getActualIssueOrExcercise();
		if(!(obj instanceof EIssue)) return;
		EIssue ie = (EIssue) obj;
		Remote remote = (Remote) ie.getStatus();
		remote.getRemoteFile();
		XMLManager.showRefreshFull();
	}

	/**
	 * Setzt im Kontextmenü die aktivierten Menüpunkte fuer ein Local-Issue.
	 */
	public void setLocalConfiguration() {
		store.setEnabled(false);
		delete.setEnabled(true);
		download.setEnabled(false);
	}

	/**
	 * Setzt im Kontextmenü die aktivierten Menüpunkte fuer ein Remote-Issue.
	 */
	public void setRemoteConfiguration() {
		store.setEnabled(false);
		delete.setEnabled(false);
		download.setEnabled(true);
	}

	/**
	 * Setzt im Kontextmenü die aktivierten Menüpunkte fuer ein Temp-Issue.
	 */
	public void setTempConfiguration() {
		store.setEnabled(true);
		delete.setEnabled(true);
		download.setEnabled(false);
	}

	/**
	 * Gibt den ETree zurueck.
	 * 
	 * @return eTree.
	 */
	public ETree getETree() {
		return eTree;
	}
}