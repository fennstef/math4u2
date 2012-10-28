package math4u2.exercises.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import math4u2.application.resource.Images;
import math4u2.application.resource.Servers;
import math4u2.application.resource.Settings;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.gui.Math4U2Win;
import math4u2.view.gui.NavigationPanel;

/**
 * Fenster zum bearbeiten der verschieden Verbindungen zu Servern. <br>
 * Erstes Textfeld = Alias fuer die Verbindung. <br>
 * Zweites Textfeld = URL des Servers mit voll qualifizierten Namen (z.B.:
 * http://www.math4u2.de/getcontents.php) <br>
 * 
 * @author Michael Lichtenstern
 */
public class ETreeServerDialog extends JFrame {

	private NavigationPanel navigationPanel;

	private Servers serverProperties = new Servers();

	private int extraRows = 2;

	private JPanel[] jPanel;

	private String[] keys;
	
	private boolean firstTime = true;

	/**
	 * Initalisiert das Fenster.
	 * 
	 * @param eTree
	 *            ETree
	 */
	public ETreeServerDialog(NavigationPanel navi) {
		this.navigationPanel = navi;
		setTitle("Server bearbeiten");
		setResizable(false);		
		setIconImage(Images.LOGO_ICON.getImage());
		addWindowListener(new java.awt.event.WindowAdapter() {

			public void windowClosing(java.awt.event.WindowEvent evt) {
				setVisible(false);
			}
		});
		refreshView(serverProperties.initServerProps());
	}//Konstruktor
	
	public void setVisible(boolean b){
		if(firstTime && b){
			setBounds(Settings.computeBounds(Math4U2Win.getInstance(),700, 150));
			firstTime=false;
		}//if
		super.setVisible(b);
	}//setVisible
	

	/**
	 * Aktualisiert das Fenster.
	 * 
	 * @param serverProperties
	 *            Serverproperties die angezeigt werden
	 */
	public void refreshView(Properties serverProperties) {
		Enumeration serverNames = serverProperties.propertyNames();
		keys = new String[serverProperties.size()];
		int sizeOfRows = serverProperties.size() + extraRows;
		getContentPane().setLayout(new GridLayout(sizeOfRows, 0));
		getContentPane().removeAll();
		JPanel explanation = new JPanel();
		explanation.setLayout (new FlowLayout (FlowLayout.LEFT));
        JLabel name = new JLabel ("1. Feld: Bezeichung (z.B.: math4u2)  |"
                + "  2. Feld: URL (z.B.: http://www.math4u2.de/getcontents.php)");
        explanation.add (name);
//        JLabel url = new JLabel ("URL ");
//        explanation.add(url);
		getContentPane().add(explanation);
		jPanel = new JPanel[sizeOfRows];
		int i = 0;
		while (serverNames.hasMoreElements()) {
			String key = (String) serverNames.nextElement();
			keys[i] = key;
			jPanel[i] = new javax.swing.JPanel();
			jPanel[i].setLayout(new FlowLayout(FlowLayout.LEFT));
			JTextField jtextfield1 = new JTextField(key, 30);
			JTextField jtextfield2 = new JTextField(serverProperties
					.getProperty(key), 30);
			JButton jButtonDelete = new JButton("löschen");
			jButtonDelete.setName(String.valueOf(i));
			jButtonDelete.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt) {
					jButtonDeleteActionPerformed(evt);
				}
			});
			JButton jButtonStore = new JButton("speichern");
			jButtonStore.setName(String.valueOf(i));
			jButtonStore.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt) {
					jButtonStoreActionPerformed(evt);
				}
			});
			jPanel[i].add(jtextfield1);
			jPanel[i].add(jtextfield2);
			jPanel[i].add(jButtonDelete);
			jPanel[i].add(jButtonStore);
			super.getContentPane().add(jPanel[i]);
			i++;
		}
		// hinzufügen Textboxen
		jPanel[i] = new javax.swing.JPanel();
		jPanel[i].setLayout(new FlowLayout(FlowLayout.LEFT));
		JTextField jtextfield1 = new JTextField("", 30);
		JTextField jtextfield2 = new JTextField("", 30);
		jPanel[i].add(jtextfield1);
		jPanel[i].add(jtextfield2);
		// hinzufügen Button
		JButton add = new JButton("hinzufügen");
		add.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				jButtonAddActionPerformed(evt);
			}
		});
		jPanel[i].add(add);
		getContentPane().add(jPanel[i]);
		pack();
	}

	/**
	 * Berabeitet eine Loeschauftrag.
	 * 
	 * @param evt
	 *            ActionEvent
	 */
	private void jButtonDeleteActionPerformed(ActionEvent evt) {
		int index = getIndex(evt);
		Properties serverProps = serverProperties.initServerProps();
		serverProps.remove(((JTextField) jPanel[index].getComponent(0))
				.getText());
		serverProperties.storeServerProps(serverProps);
		this.refreshServerDialogAndPopUpMenue(serverProps);
	}

	/**
	 * Bearbeitet einen Speicherauftrag.
	 * 
	 * @param evt
	 *            ActionEvent
	 */
	private void jButtonStoreActionPerformed(ActionEvent evt) {
		int index = this.getIndex(evt);
		Properties serverProps = serverProperties.initServerProps();
		serverProps.remove(keys[index]);
		serverProps.setProperty(((JTextField) jPanel[index].getComponent(0))
				.getText(), ((JTextField) jPanel[index].getComponent(1))
				.getText());
		serverProperties.storeServerProps(serverProps);
		refreshServerDialogAndPopUpMenue(serverProps);
	}

	/**
	 * Fuegt eine neuen Eintrag hinzu und aktualisiert das Fenster.
	 * 
	 * @param evt
	 *            ActionEvent
	 */
	private void jButtonAddActionPerformed(ActionEvent evt) {
		Properties serverProps = serverProperties.initServerProps();
		String key = ((JTextField) jPanel[jPanel.length - 2].getComponent(0))
				.getText();
		String value = ((JTextField) jPanel[jPanel.length - 2].getComponent(1))
				.getText();
		if (key.compareTo("") != 0 && value.compareTo("") != 0) {
			if (serverProps.contains(key)) {
				ExceptionManager
						.doError("Einträge mit gleichen Namen sind nicht erlaubt!");
			} else {
				serverProps.setProperty(key, value);
			}
		} else {
			ExceptionManager.doError("Bitte Name und URL eintragen!");
		}
		serverProperties.storeServerProps(serverProps);
		refreshServerDialogAndPopUpMenue(serverProps);
	}

	/**
	 * Aktualisert das Fenster und das entsprechende Kontextmenü.
	 * 
	 * @param serverProperties
	 *            Serverproperties die angezeigt werden
	 */
	private void refreshServerDialogAndPopUpMenue(Properties serverProperties) {
		this.refreshView(serverProperties);
		super.invalidate();
		navigationPanel.refreshPopupMenuGlobal();
	}

	/**
	 * Gibt den Index eines JButtons zurueck.
	 * 
	 * @param evt
	 *            ActionEvent
	 * @return Index eines JButtons
	 */
	private int getIndex(ActionEvent evt) {
		return Integer.parseInt(((JButton) evt.getSource()).getName());
	}
}