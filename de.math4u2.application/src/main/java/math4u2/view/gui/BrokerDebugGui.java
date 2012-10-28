package math4u2.view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import math4u2.application.resource.Images;
import math4u2.controller.Broker;
import math4u2.controller.BrokerEvent;
import math4u2.controller.BrokerException;
import math4u2.controller.BrokerListener;
import math4u2.controller.MathObject;
import math4u2.controller.relation.RelationContainer;
import math4u2.mathematics.functions.UserFunction;
import math4u2.parser.parser;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.ParseException;
import math4u2.view.formula.FormulaRenderContext;

/**
 * Diese Gui erhält man mit STRG + D
 * 
 * Es werden die aktuellen benannten Beziehungen dargestellt. Zusätzlich kann
 * man noch Anweisungen wie z.B. löschen, renew, eval ausführen.
 * 
 * @author Fenn Stefan
 */
public class BrokerDebugGui extends JFrame {

	private Broker broker;

	final JComboBox comboBox;

	final JTextPane ausgabe = new JTextPane();

	final JTextField eingabe = new JTextField(40);

	public static final KeyStroke debugHotkey = KeyStroke
			.getKeyStroke("control D");

	public final Action debug = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			setEnabled(false); // stop any other events from interfering
			setVisible(!isVisible());
			manageAusgabe("Alle");
			eingabe.requestFocus();
			setEnabled(true);
		}
	};

	public BrokerDebugGui(Broker broker) {
		super("Broker Debug");
		setIconImage(Images.LOGO_ICON.getImage());
		this.broker = broker;
		comboBox = new JComboBox();
		comboBox.setEditable(true);
		refreshComboBox();

		broker.addBrokerListener(new BrokerListener() {
			public void newObjectPublished(BrokerEvent e) {
				refreshComboBox();
			}

			public void objectDeleted(BrokerEvent e) {
				refreshComboBox();
			}
		});
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manageAusgabe((String) comboBox.getSelectedItem());
			} //itemStateChanged
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				dispose();
			} //windowClosing
		});

		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton defineButton = new JButton("Definiere");
		defineButton.setMnemonic('d');
		JButton deleteButton = new JButton("Lösche");
		deleteButton.setMnemonic('c');
		JButton propagateChangeButton = new JButton("PropagateChange");
		propagateChangeButton.setMnemonic('p');
		JButton evalButton = new JButton("Eval");
		evalButton.setMnemonic('e');
		JButton renewButton = new JButton("Renew?");
		renewButton.setMnemonic('r');
		renewButton.setToolTipText("Wer würde ein Renew bekommen?");
		defineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					parser.NEWParseDefinition(eingabe.getText(), true, getBroker());
					manageAusgabe("Alle");
				} catch (ParseException e1) {
					ExceptionManager.doError(e1);
					ausgabe.setText(e1.getMessage());
				}
				eingabe.requestFocus();
			} //actionPerformed
		});
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String delStr = eingabe.getText();
					String[] delObjKeys = delStr.split(",");
					for (int i = 0; i < delObjKeys.length; i++) {
						delObjKeys[i] = delObjKeys[i].trim();
					} //for i
					getBroker().deleteObjects(Arrays.asList(delObjKeys));
					manageAusgabe("Alle");
				} catch (Exception e1) {
					ExceptionManager.doError(e1);
					ausgabe.setText(e1.getMessage());
				}
				eingabe.requestFocus();
			} //actionPerformed
		});
		eingabe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("".equals(eingabe.getText().trim())) {
					manageAusgabe("Alle");
					return;
				} else if (eingabe.getText().indexOf(":=") == -1) {
					//Objekt anzeigen
					manageAusgabe(eingabe.getText().trim());
					return;
				}
				try {
					parser.NEWParseDefinition(eingabe.getText(), true, getBroker());
					manageAusgabe("Alle");
				} catch (ParseException e1) {
					ExceptionManager.doError(e1);
					ausgabe.setText(e1.getMessage() + " at Line "
							+ e1.getErrorColumn());
				} //catch
			} //actionPerformed
		});
		propagateChangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					getBroker().propagateChange(
							getBroker().tryToGetObject(eingabe.getText()));
				} catch (BrokerException e1) {
					ExceptionManager.doError(e1);
					ausgabe.setText(e1.getMessage());
				} //catch
				eingabe.requestFocus();
			} //actionPerformed
		});
		evalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					parser.NEWParseDefinition("temp:=" + eingabe.getText(), true,
							getBroker());
					MathObject mo = getBroker().getObject("temp");
					UserFunction temp = (UserFunction) mo;
					String s = temp.getFunction().getTermString(temp) + "\n"
							+ temp.eval();
					getBroker().deleteObject(temp);
					ausgabe.setText(s);
				} catch (Exception e1) {
					ExceptionManager.doError(e1);
					ausgabe.setText(e1.getMessage());
				} //catch
				eingabe.requestFocus();
			} //actionPerformed
		});
		renewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MathObject changedObject = getBroker().getObject(eingabe.getText().trim());
					RelationContainer rc = changedObject.getRelationContainer();
			        Set objForRenew = new HashSet();
			        //alle zu benachrichtigenden Objekte einsammeln
			        rc.getAllMessageableObjects(changedObject, objForRenew);
			        //sich selber nicht benachrichtigen
			        objForRenew.remove(changedObject);
			        
			        Iterator iter = objForRenew.iterator();
			        String message="";
			        while(iter.hasNext()){
			            MathObject mo = (MathObject) iter.next();
			            message+= mo.getIdentifier()+" : "+mo.getClass()+" : "+mo.toString()+"\n\n";
			        }
			        ausgabe.setText(message);
				} catch (Exception e1) {
					ExceptionManager.doError(e1);
					ausgabe.setText(e1.getMessage());
				} //catch
				eingabe.requestFocus();
			} //actionPerformed
		});
		panel2.add(evalButton);
		panel2.add(propagateChangeButton);
		panel2.add(deleteButton);
		panel2.add(defineButton);
		panel2.add(renewButton);
		panel2.add(eingabe);

		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		ausgabe.setPreferredSize(new Dimension((int) (width * 0.99), 400));
		ausgabe.setFont(FormulaRenderContext.getFont("Dialoge", Font.PLAIN, 15));
		manageAusgabe("Alle");

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(comboBox, BorderLayout.NORTH);
		getContentPane().add(panel2, BorderLayout.SOUTH);
		getContentPane().add(panel, BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(ausgabe), BorderLayout.CENTER);
		pack();
	} //Konstruktor

	Broker getBroker() {
		return broker;
	} //getBroker

	void manageAusgabe(String inStr) {
		try {
			StringBuffer outStr = new StringBuffer();
			if ("Alle".equals(inStr)) {
				outStr.append(getBroker().toString());
				outStr.append("\nDefinitionen:\n");
				Set keySet = getBroker().getFirstLevelObjects();
				for (Iterator iter = keySet.iterator(); iter.hasNext();) {
					String key = (String) iter.next();
					outStr.append(getBroker().tryToGetObject(key));
					if (iter.hasNext())
						outStr.append("\n");
				} //for iter
			} else {
				MathObject o = getBroker().tryToGetObject(inStr);
				outStr.append(o.getRelationContainer().toString());
				outStr.append("\nKlasse: " + o.getClass());
				if(o instanceof UserFunction){
					outStr.append("\nTermnode-Klasse: " + ((UserFunction)o).getFunction().getClass());
				}
				outStr.append("\n\n" + getBroker().tryToGetObject(inStr));
			}
			ausgabe.setText(outStr.toString());
		} catch (BrokerException e) {
			ausgabe.setText(e.getMessage());
		} //catch
	} //manageAusgabe

	void refreshComboBox() {
		comboBox.removeAllItems();
		comboBox.addItem("Alle");
		Object[] obja = new LinkedList(broker.getFirstLevelObjects()).toArray();
		for (int i = 0; i < obja.length; i++) {
			comboBox.addItem(obja[i]);
		} //for i
		comboBox.setSelectedItem("Alle");
	} //refreshComboBox

} //BrokerDebugGui
