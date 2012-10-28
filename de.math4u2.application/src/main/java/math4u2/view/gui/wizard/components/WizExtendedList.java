package math4u2.view.gui.wizard.components;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import math4u2.application.resource.Images;
import math4u2.application.resource.Settings;
import math4u2.util.swing.LabelAsButtonMouseListener;
import math4u2.view.gui.wizard.WizardUtil;
import math4u2.view.gui.wizard.title.HasTitle;
import math4u2.view.gui.wizard.types.WizParamType;
import math4u2.view.layout.TableLayout;

public class WizExtendedList extends JPanel implements CanModifyText {

	/** Textkomponente */
	private WizTextField textField;

	/** Listenkomponente */
	private WizList listBox;

	private JLabel addButton;

	private JLabel removeButton;

	/** Liste aller ActionListener */
	private LinkedList actionListeners = new LinkedList();

	/** Bezeichner für den Namen */
	private String name;

	public WizExtendedList(String name,final WizParamType type, HasTitle description, String previousTitle) {
		this.name = name;

		// Textfield
		textField = new WizTextField();
		WizardUtil.addKeyAssignment(textField, textField, type, description, previousTitle, false);

		textField.getDocument().addDocumentListener(new DocumentListener(){

			public void changedUpdate(DocumentEvent arg0) {
				doIt();
			}

			public void insertUpdate(DocumentEvent arg0) {
				doIt();
			}

			public void removeUpdate(DocumentEvent arg0) {
				doIt();
			}
			
			public void doIt(){
				listBox.setText(textField.getText());
				fireActionEvent();
			}
		});
		
		textField.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e) {
				textField.removeHighlights();
			}//focusGained
		});

		// List
		listBox = new WizList();
		listBox.getJList().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						int pos = listBox.getJList().getSelectedIndex();
						highlightItem(pos);
					}
				});
		listBox.addChangeListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				textField.setText(listBox.getText());
				int pos = listBox.getJList().getSelectedIndex();
				highlightItem(pos);
				fireActionEvent();
			}//actionPerformed
		});
		listBox.getJList().setFocusable(false);

		// AddButton
		addButton = new JLabel(Images.EXPAND_ICON);
		addButton.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		addButton.addMouseListener(new LabelAsButtonMouseListener(addButton));
		addButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				Container window = getParent();
				while(!(window instanceof Frame)){
					window = window.getParent();
				}//while				
				final JDialog dialog = new JDialog((Frame)window, true);
				dialog.setTitle("Element hinzufügen");
				
				double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
				double size[][] = { { border, P, border, F, border }, {
						border, P, border, P, border}
				};

				JPanel panel = (JPanel) dialog.getContentPane();
				panel.setLayout(new TableLayout(size));

				final JTextField field = new JTextField();
				
				JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3));
				final JButton fertig = new JButton("Fertig");
				fertig.setMnemonic('f');
				buttonPanel.add(fertig);
				final JButton abbrechen = new JButton("Abbrechen");
				abbrechen.setMnemonic('a');
				buttonPanel.add(abbrechen);
				
				ActionListener listener = new ActionListener(){
					public void actionPerformed(ActionEvent event) {
						if(event.getSource()==abbrechen){
							dialog.dispose();
							return;
						}//if
						String s = field.getText();
						if(s==null || s.length()==0) return;
						String[] parts = s.split(",");
						for(int i=0; i<parts.length; i++){
							listBox.addItem(parts[i]);
						}//for i
						dialog.dispose();
					}//actionPerformed
					
				};
				fertig.addActionListener(listener);
				abbrechen.addActionListener(listener);
				field.addActionListener(listener);
				
				panel.add(new JLabel("Neues Element:"), 	"1, 1, L, T");
				panel.add(field,							"3, 1, F, F");
				panel.add(buttonPanel,						"1, 3, 3, 3");
				
				dialog.setBounds(Settings.computeBounds(window, 300, 95));
				dialog.setVisible(true);
			}//mouseClicked
		});

		// RemoveButton
		removeButton = new JLabel(Images.COLLAPSED_ICON);
		removeButton.addMouseListener(new LabelAsButtonMouseListener(removeButton));
		removeButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				listBox.deleteSelectedItem();
			}
		});
		
		double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = { { F, border/2 , P }, { P, border, P, border/2 , P, F } };

		setLayout(new TableLayout(size));

		add(textField, 		"0, 0, 2, 0");
		add(listBox, 		"0, 2, 0, 5");
		add(addButton, 		"2, 2, F, F");
		add(removeButton, 	"2, 4, F, F");
	}// Konstruktor

	/**
	 * Bekommt eine kommagetrennte Liste und trägt diese in die Liste ein
	 */
	public void setText(String text) {
		textField.setText(text);
		listBox.setText(text);
	}// setText

	public String getText() {
		if(name == null)
			return "{" + textField.getText() + "}";
		return name + "({" + textField.getText() + "})";
	}// getText

	private void highlightItem(int pos) {
		if (pos == -1)
			return;
		String text = textField.getText();
		String[] parts = text.split(",");
		int start = 0;
		for (int i = 0; i < pos; i++)
			start += parts[i].length() + 1;
		if(pos>=parts.length) return;
		int end = start + parts[pos].length();
		textField.highlight(start, end);

		int width = textField.getFontMetrics(textField.getFont()).stringWidth(
				text.substring(0, start));
		textField.getHorizontalVisibility().setValue(width);
	}// highlightItem
	
	public void requestFocus() {
		textField.requestFocus();
	}//requestFocus
	
	public boolean hasFocus(){
		return textField.hasFocus() || listBox.hasFocus();
	}//hasFocus

	private void fireActionEvent() {
		for (Iterator iter = actionListeners.iterator(); iter.hasNext();) {
			ActionListener listener = (ActionListener) iter.next();
			ActionEvent ae = new ActionEvent(this, -1, null);
			listener.actionPerformed(ae);
		}// for iter
	}// fireActionEvent

	public void addFinishListener(Object listener) {
		textField.addActionListener((ActionListener) listener);
	}//addFinishListener
	
	/**
	 * Listener wird benachrichtigt, wenn der Textinhalt sich geändert hat
	 */
	public void addChangeListener(Object listener) {
		actionListeners.add(listener);
	}// addTextChangeListener
	
	public void addExitListener(Object listener) {
		textField.addKeyListener((KeyListener) listener);
		listBox.addKeyListener((KeyListener) listener);
	}//addExitListener
}// class WizExtendedList
