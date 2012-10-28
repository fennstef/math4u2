package math4u2.view.gui.listview;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import math4u2.application.resource.Colors;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.mathematics.functions.UserFunction;
import math4u2.parser.parser;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotFoundException;
import math4u2.util.exception.parser.ParseException;
import math4u2.view.gui.Math4U2Win;
import math4u2.view.gui.component.ListCheckBox;
import math4u2.view.gui.wizard.WizardUtil;
import math4u2.view.gui.wizard.title.SimpleHasTitle;
import math4u2.view.gui.wizard.types.WizParamType;
import math4u2.view.layout.TableLayout;

/**
 * Grafische Repräsentation aller definierten Objekte
 * 
 * @author Fenn Stefan
 */
public class ListBox extends AbstractListView {

	/** Eingabefeld für Formeln jeglicher Art */
	private JEditorPane inputField;
	
	/** Button der den Wizard öffnet */
	private JLabel addWizardButton;
	
	/** Container, der Inputfeld und Wizard-Button enthält */
	private JPanel inputPanel;
	
	/**
	 * ScorllPane für die Eingabe
	 */
	private JScrollPane scrollPaneInput;

	/** Liste der versteckten Funktionen */
	private Set hideSet = new HashSet();

	public ListBox(Broker broker) {
		super(broker);
	} //Konstruktor

	public void init() {
		//InputField
		inputField = new JEditorPane();
		inputField.setBackground(Colors.TEXT_FIELD);
		InputMap inputmap = new InputMap();
		inputmap.setParent(inputField.getInputMap(JComponent.WHEN_FOCUSED));
		inputmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Event.CTRL_MASK), inputmap.get(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0)));
		inputmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), new Object());
		inputField.setInputMap(JComponent.WHEN_FOCUSED, inputmap);
		Border b1 = BorderFactory.createEmptyBorder(7, 3, 3, 3);
		inputField.setBorder(b1);
		inputField.addKeyListener(new ListBoxInputListener());
		WizardUtil.addKeyAssignment(inputField, null, WizParamType.ANY_INPUT, new SimpleHasTitle("Neues Object erzeugen"), "", true);
		addWizardButton = WizardUtil.getAddButton(inputField);
		
		scrollPaneInput = new JScrollPane(inputField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneInput.setBorder(null);
		scrollPaneInput.getViewport().setBorder(null);
		scrollPaneInput.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		
		inputPanel = new JPanel(null);
		inputPanel.setBackground(inputField.getBackground());
		double P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = {
				/* Zeilen */// 
				{ F, P, 5 },
				/* Spalten */// 
				{ F } };
		inputPanel.setLayout(new TableLayout(size));
		inputPanel.add(addWizardButton, "1, 0, R, C");
		inputPanel.add(scrollPaneInput, "0, 0, F, F");
		inputPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			
		super.init();
		add(inputPanel, "0, 0, 1,0");
	} //init

	public void setExpandList(boolean expand) {		
		ListCheckBox.State state = ((ListCheckBox)checkListExpand).getState();
		boolean b = objectList.getComponentCount() > hideSet.size();
		checkListExpand.setEnabled(b);
		scrollPane.setVisible(b && state!=ListCheckBox.NOT_SELECTED);

		Point p2 = SwingUtilities.convertPoint(inputPanel, 0, 0, Math4U2Win
				.getInstance().getContentPane());
		int width = inputPanel.getWidth();
		int height = MAX_VIEWPORT_HEIGHT - inputPanel.getHeight();
		int heightTemp = 0;
		Component[] ca = objectList.getComponents();
		
		for (int i = 0; i < ca.length; i++) {
			ListBoxItem lbi = (ListBoxItem) ca[i];
			if (hideSet.contains(lbi.getModelKey())) {
				lbi.setVisible(false);
				continue;
			} else {
				lbi.setVisible(true);
			}//else
			
			if (state==ListCheckBox.DONT_CARE) {
				boolean visible = selectedItem==lbi; 
				lbi.setVisible(visible);
				if(!visible) continue;
			}//if
			JComponent c = (JComponent) ca[i];
			Insets insets = c.getInsets();
			heightTemp += insets.top + insets.bottom - 1;
			heightTemp += c.getPreferredSize().height;
		
		}//for i
		height = Math.min(height, heightTemp + 1) + 1;
		scrollPane.setBounds(p2.x, p2.y + inputPanel.getHeight() - 1, width,
				height);
		scrollPane.doLayout();
		objectList.revalidate();
		scrollPane.setVisible(expand && checkListExpand.isEnabled());
	}//setExpandList
	
	/**
	 * Fügt eine neue Komponente hinzu
	 */
	@Override
	public void addItem(ListViewItemInterface c) {
		super.addItem(c);
	}

	public boolean isEmpty() {
		Component[] ca = objectList.getComponents();
		for (int i = 0; i < ca.length; i++) {
			ListBoxItem lbi = (ListBoxItem) ca[i];
			if (hideSet.contains(lbi.getModel().getIdentifier()))
				continue;
			return false;
		}//for i
		return true;
	}//isEmpty

	public Object getKey() {
		return "ListBox";
	} //getKey
	
	public Object getIdentifier(){
		return getKey();
	}

	public void clearInputField() {
		inputField.setText("");
	}//clearInputField

	/**
	 * Verarbeitet die Selektion eines Elements
	 * 
	 * @param item
	 *            selektiertes Element
	 */
	@Override
	public void selectItem(ListViewItemInterface item) {
		super.selectItem(item);
		if (selectedItem != null)
			inputField.setText(selectedItem.toString());
	}//manageSelection

	public String manageInput(String inputStr) {
		inputStr = inputStr.replace('\n', ' ');
		if ((inputStr == null) || "".equals(inputStr))
			return "";
		String name = null;
		try {
			//ListBoxItem, nur bei der ersten Definition
			int pos = inputStr.indexOf(":=");
			if (pos == -1)
				throw new NotFoundException("Zuweisungsoperator ':=' nicht in '"
						+ inputStr + "' gefunden.");
			String oldN = inputStr.substring(0, inputStr.indexOf(":=")).trim();
			if (oldN.indexOf("(") != -1)
				oldN = oldN.substring(0, oldN.indexOf("(")).trim();
//			MathObject moOld = broker.getObject(oldN);

			name = parser.NEWParseDefinition(inputStr, true, broker);

			//Funtioniert nicht, falls eine Folge definiert wurde
//			if (moOld != null)
//				return name;

			try {
				if (broker.getObject("ListBox.$" + name) != null)
					return name;
			} catch (BrokerException e) {
				//Wird geworfen, wenn auf Zeichenflächen-Parameter zugegriffen
				// wird
				return name;
			}

			MathObject mo = broker.getObject(name);
			if(mo instanceof UserFunction){
				UserFunction f = (UserFunction) mo;
				ListBoxItem lbi = new ListBoxItem(f, this, broker);
				ListBoxItem.register(lbi, mo, broker);
				addItem(lbi);
				super.selectItem(lbi);
				inputField.requestFocus();
			}else{
				ExceptionManager.doError("UserFunction erwartet (ListBox)");
			}
		} catch (ParseException e) {
			ExceptionManager.doError(e, inputField, inputStr);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler in der Definition", e);
		} catch (NotFoundException e) {
			ExceptionManager.doError("Fehler in der Definition", e);			
		} catch (Throwable e) {
			ExceptionManager.doError("Unerwarteter Fehler", e);
		}//catch
		setExpandList();
		return name;
	} //manageInput

	/**
	 * Versteckt die Funktion für den Anwender
	 */
	public void hideObject(String name, boolean hide) {
		if (hide) {
			hideSet.add(name);
		} else {
			hideSet.remove(name);
		}
		setExpandList();
	}//hideObject
	
	public boolean isHideObject(String name){
		return hideSet.contains(name);
	}//isHideObject
	
	public void clearHideList(){
		hideSet.clear();
	}
	
	public void fitToScrollPane(JComponent c) {
		c.setPreferredSize(new Dimension(scrollPaneInput.getBounds().width,c.getPreferredSize().height));
	}//fitToScrollPane
	

	public JTextComponent getInputField(){
		return inputField;
	}//getInputField
	
	
	/**
	 * Verwaltet die Eingabe im Eingabefeld
	 * 
	 */
	class ListBoxInputListener implements ActionListener, KeyListener {

		public void doIt(){
			String inputStr = inputField.getText().trim();
			manageInput(inputStr);
		}//doIt
		
		public void actionPerformed(ActionEvent e) {
			doIt();
		}//actionPerformed

		public void keyTyped(KeyEvent event) {
			if(event.getKeyChar()=='\n' && event.getModifiers()!=KeyEvent.CTRL_MASK){
				doIt();
				event.consume();
			}
		}

		public void keyPressed(KeyEvent arg0) {}
		public void keyReleased(KeyEvent arg0) {}
	}//class ListBoxInputListener
} //class ListBox
