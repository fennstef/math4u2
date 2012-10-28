package math4u2.view.gui.wizard.components;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import math4u2.application.resource.Settings;
import math4u2.view.layout.TableLayout;

public class WizList extends JScrollPane implements CanModifyText{
	
	private JList list;
	private DefaultListModel model;
	private List changeListeners = new LinkedList();
	
	public WizList(){
		super();
		model = new DefaultListModel();
		
		list = new JList(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		setViewportView(list);
		list.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_DELETE){
					deleteSelectedItem();
				}//if
			}//keyReleased
			
			public void keyTyped(KeyEvent e){
				if(e.getKeyChar()=='\n'){
					changeItem();
				}//if	
			}//keyTyped
		});
		list.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()!=2) return;
				changeItem();
			}//mouseClicked
		});
	}//Konstruktor
	
	public void changeItem(){
		final int index = list.getSelectedIndex();
		if(index==-1) return;
		
		Container window = getParent();
		while(!(window instanceof Frame)){
			window = window.getParent();
		}//while
		final JDialog dialog = new JDialog((Frame)window, true);
		dialog.setTitle("Element ändern");
		
		double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = { { border, P, border, F, border }, {
				border, P, border, P, border}
		};

		JPanel panel = (JPanel) dialog.getContentPane();
		panel.setLayout(new TableLayout(size));

		final JTextField field = new JTextField();
		String s = (String) model.getElementAt(index);
		field.setText(s);
		field.selectAll();
		
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
				model.removeElementAt(index);
				list.setSelectedIndex(Math.max(0,index-1));
				
				String[] parts = getElementParts(s);
				for(int i=0; i<parts.length; i++)
					_addItem(parts[i]);
				fireChangeAction();
				dialog.dispose();
			}//actionPerformed
			
		};
		fertig.addActionListener(listener);
		abbrechen.addActionListener(listener);
		field.addActionListener(listener);
		
		panel.add(new JLabel("Element:"), 			"1, 1, L, T");
		panel.add(field,							"3, 1, F, F");
		panel.add(buttonPanel,						"1, 3, 3, 3");
		
		dialog.setBounds(Settings.computeBounds(window, 300, 95));
		dialog.setVisible(true);		
	}//changeItem
	
	public String[] getElementParts(String text){
		if(text==null) return null;
		text = text.replaceAll(",+", ",");
		String[] parts = text.split(",");
		for(int i=0; i<parts.length; i++){
			parts[i] = parts[i].trim();
		}//for i
		return parts;
	}//getElementParts

	/**
	 * Bekommt eine kommagetrennte Liste und trägt diese in die Liste ein
	 */
	public void setText(String text) {
		String[] parts = getElementParts(text);
		model.removeAllElements();
		
		for(int i=0; i<parts.length; i++){
			model.addElement(parts[i].trim());
		}//for i
	}//setText

	public String getText() {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<model.getSize(); i++){
			sb.append(model.getElementAt(i));
			if(i!=model.getSize()-1)
				sb.append(", ");
		}//for i
		return sb.toString();
	}//getText
	
	private void _deleteSelectedItem(){
		int pos = list.getSelectedIndex();
		if(pos!=-1)
			model.remove(pos);
		list.setSelectedIndex(Math.min(pos,model.getSize()-1));
	}//_deleteSelectedItem
	
	public void deleteSelectedItem(){
		_deleteSelectedItem();
		fireChangeAction();
	}//deleteSelectedItem
	
	private void _addItem(String s){
		int pos = list.getSelectedIndex();
		if(pos==-1){
			model.addElement(s);
		}else{
			model.add(pos+1,s);
			list.setSelectedIndex(pos+1);
		}
	}//addItem
	
	public void addItem(String s){
		_addItem(s);
		fireChangeAction();
	}//addItem
	
	JList getJList(){
		return list;
	}//getJList
	
	public void fireChangeAction(){
		for(Iterator iter = changeListeners.iterator(); iter.hasNext();){
			ActionListener listener = (ActionListener) iter.next();
			ActionEvent ae = new ActionEvent(this, -1, null);
			listener.actionPerformed(ae);
		}//for iter
	}//fireChangeAction
	
	public void addChangeListener(Object listener){
		changeListeners.add(listener);
	}//addChangeListener

	public void addFinishListener(Object listener) {
	}//addFinishListener
	
	public void addExitListener(Object listener) {
		addKeyListener((KeyListener) listener);
	}//addExitListener
}//class WizList
