package math4u2.view.gui.wizard.components;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

public class WizComboBox extends JComboBox implements CanModifyText{
	
	public WizComboBox(Object[] oa){
		super(oa);
	}//Konstruktor

	public void setText(String text) {
		for(int i=0; i<getItemCount(); i++){
			if(getItemAt(i).equals(text)){
				setSelectedIndex(i);
				return;
			}//if
		}//for i
	}//setText
	
	public boolean hasFocus(){
		if(!isEditable())
			return super.hasFocus();
		return ((JTextComponent)getEditor().getEditorComponent()).hasFocus();
	}//hasFocus

	public String getText() {
		Object obj = getSelectedItem();
		if(obj instanceof Entry)
			return ((Entry)obj).getValue();
		else
			return  ((JTextComponent)getEditor().getEditorComponent()).getText();
	}//getText
	
	public static class Entry{
		private String key;
		private String value;
		
		public Entry(String key, String value){
			this.key = key;
			this.value = value;
		}//Konstruktor
		
		public String toString(){
			return key;
		}//toString
		
		public String getValue(){
			return value;
		}//getvalue
		
		public String getKey(){
			return key;
		}
	
	}//class Entry

	public void addFinishListener(Object listener) {
		addKeyListener((KeyListener) listener);
	}//addFinishListener

	public void addChangeListener(Object listener) {
		addActionListener((ActionListener) listener);
		if(isEditable()){
			((JTextComponent)getEditor().getEditorComponent()).addKeyListener((KeyListener) listener);
			((JTextComponent)getEditor().getEditorComponent()).addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent event){
					System.out.print(event.getKeyChar());
				}
			});
		}//if
	}//addChangeListener
	
	public void addExitListener(Object listener) {
		addKeyListener((KeyListener) listener);
	}//addExitListener
}//class WizComboBox
