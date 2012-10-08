package math4u2.util.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * JFileChooser wird in einem Frame dargestellt.
 * 
 * @author Fenn Stefan
 *
 */
public class FrameFileChooser extends JFrame {

	private String actionCommand;

	public FrameFileChooser(JFileChooser fileChooser,final ActionListener listener) {
		super(fileChooser.getDialogTitle());
		add(fileChooser);
		
		fileChooser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				actionCommand = event.getActionCommand();
				if(actionCommand.equals(JFileChooser.APPROVE_SELECTION))
					dispose();
				if(actionCommand.equals(JFileChooser.CANCEL_SELECTION))
					dispose();
				listener.actionPerformed(event);
			}//actionPerformed				
		});
	}//Konstruktor

}//classFramFileChooser
