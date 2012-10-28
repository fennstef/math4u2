package math4u2.view.gui.wizard.components;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;

public class WizRadioButtonGroup extends JPanel implements CanModifyText{
	
	private Entry[] entries;
	private ButtonGroup group;
	private JRadioButton[] buttons;
	
	private KeyListener keyListener = new KeyAdapter(){
		public void keyTyped(KeyEvent e) {		
			for (int i = 0; i < entries.length; i++) {
				if(e.getKeyChar()!=entries[i].mnenomic)
					continue;
				buttons[i].setSelected(true);
				buttons[i].requestFocus();
				return;
			}//for i
		}//keyTyped
	};
	
	public WizRadioButtonGroup(Entry[] entries){
		super(null);
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.entries = entries;
		
		group = new ButtonGroup();
		buttons = new JRadioButton[entries.length];
		
		for (int i = 0; i < entries.length; i++) {
			Entry entry = entries[i];
			buttons[i] = new JRadioButton(entry.key);
			buttons[i].setMnemonic(entries[i].mnenomic);
			buttons[i].addKeyListener(keyListener);
			group.add(buttons[i]);
			add(buttons[i]);
		}//for i
		buttons[0].setSelected(true);
	}//Konstruktor

	public void setText(String text) {
		for(int i=0; i<entries.length; i++){
			if(entries[i].value.equals(text)){
				buttons[i].setSelected(true);
			}//if
		}//for i
	}//setText
	
	private int getSelectedIndex(){
		for (int i = 0; i < buttons.length; i++) {
			if(buttons[i].isSelected())
				return i;
		}//for i
		return -1;
	}//getSelectedIndex

	public String getText() {
		if(getSelectedIndex()==-1) return null;
		return entries[getSelectedIndex()].getValue();
	}//getText

	public void addFinishListener(Object listener) {
		for(int i=0; i<buttons.length; i++){
			buttons[i].addKeyListener((KeyListener) listener);
			if(listener instanceof MouseListener)
				buttons[i].addMouseListener((MouseListener) listener);
		}//for i
	}//addFinishListener

	public void addChangeListener(Object listener) {
		for(int i=0; i<buttons.length; i++){
			buttons[i].addActionListener((ActionListener) listener);
			buttons[i].addChangeListener((ChangeListener) listener);
		}
	}//addChangeListener
	
	public void addExitListener(Object listener) {
		for(int i=0; i<buttons.length; i++)
			buttons[i].addKeyListener((KeyListener) listener);
	}//addExitListener
	
	public void requestFocus() {
		buttons[0].requestFocus();
	}//requestFocus
	
	public static class Entry{
		private String key;
		private String value;
		private char mnenomic;
		
		public Entry(String key, String value, char mnenomic){
			this.key = key;
			this.value = value;
			this.mnenomic = mnenomic;
		}//Konstruktor
		
		public String toString(){
			return key;
		}//toString
		
		public String getValue(){
			return value;
		}//getvalue
	
	}//class Entry
}//class WizComboBox
