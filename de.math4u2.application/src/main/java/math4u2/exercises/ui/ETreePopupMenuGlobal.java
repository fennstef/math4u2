package math4u2.exercises.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;

import math4u2.application.resource.Servers;
import math4u2.view.gui.XMLManager;

/**
 * Globales PopupMenü für den Baum.
 * 
 * @author Michael Lichtenstern
 */
public class ETreePopupMenuGlobal extends JPopupMenu {

	private Servers serverProperties = new Servers();

	private JFrame serverDialog;

	/**
	 * Initalisiertz das globale PopupMenü.
	 * 
	 * @param jTree
	 *            JTree an welchen es gehängt wird.
	 * @param serverDialog
	 *            ServerDialog zum editieren.
	 */
	public ETreePopupMenuGlobal(JTree jTree, JFrame serverDialog) {
		this.serverDialog = serverDialog;
		Properties serverProps = serverProperties.initServerProps();
		Enumeration eServerProps = serverProps.propertyNames();
		while (eServerProps.hasMoreElements()) {
			String element = (String) eServerProps.nextElement();
			JMenuItem jmi = new JMenuItem(element);
			jmi.addActionListener(new ServerListener(serverProps
					.getProperty(element)));
			add(jmi);
		}
		JMenuItem serverDialogItem = new JMenuItem("Serverliste bearbeiten");
		serverDialogItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				getServerDialog().setVisible(true);
			}
		});
		add(new JSeparator());
		add(serverDialogItem);
		super.setInvoker(jTree);
		super.setLightWeightPopupEnabled(true);
	}

	/**
	 * Gibt den JFrame des Server-Dialogs zurueck
	 * 
	 * @return serverDialog.
	 */
	public JFrame getServerDialog() {
		return serverDialog;
	}

	/**
	 * Listener fuer die einzelnen Server-Verbindungen.
	 * 
	 * @author Michael Lichtenstern
	 */
	private class ServerListener implements ActionListener {

		private String url = "";

		public ServerListener(String url) {
			this.url = url;
		}

		public void actionPerformed(ActionEvent evt) {
			XMLManager.serverRefresh(url);
		}
	}
}