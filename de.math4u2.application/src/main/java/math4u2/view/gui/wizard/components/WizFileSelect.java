package math4u2.view.gui.wizard.components;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import math4u2.util.swing.FrameFileChooser;
import math4u2.view.gui.wizard.WizardUtil;

public class WizFileSelect extends JPanel implements CanModifyText{	
	private JButton loadButton;
	private JLabel location;	
	
	public WizFileSelect(){
		super(new FlowLayout(FlowLayout.LEFT,3,0));
		location = new JLabel();
		loadButton = new JButton("Auswählen ...");
		loadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser("Importieren");
				fc.setDialogTitle("Import");

				FrameFileChooser frame = new FrameFileChooser(fc, new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						File file = fc.getSelectedFile();
						if(file!=null && file.exists())
							location.setText(file.toString().replaceAll("\\\\", "/"));
					}//actionPerformed
				});
				WizardUtil.layoutWindow(frame);
				frame.setVisible(true);				
			}//actionPerformed
		});
		add(loadButton);
		add(location);
	}//Konstruktor

	public void addChangeListener(Object listener) {
		loadButton.addActionListener((ActionListener) listener);
	}//addChangeListener

	public void addExitListener(Object listener) {
		loadButton.addKeyListener((KeyListener) listener);		
	}//addExitListener

	public void addFinishListener(Object listener) {
		loadButton.addKeyListener((KeyListener) listener);
	}//addFinishListener

	public void addFocusListener(FocusListener listener) {
		if(loadButton!=null) loadButton.addFocusListener(listener);
	}//addFocusListener

	public String getText() {
		if(location.getText().length()==0) return "finput";
		return "finput:"+location.getText();
	}//getText

	public void setText(String text) {		
		location.setText(text);
	}//setText

}//class WizFileSelect
