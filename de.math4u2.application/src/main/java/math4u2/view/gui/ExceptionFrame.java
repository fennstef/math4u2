package math4u2.view.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import math4u2.application.resource.Images;
import math4u2.application.resource.Settings;
import math4u2.util.exception.IExceptionFrame;
import math4u2.view.formula.FormulaRenderContext;
import math4u2.view.layout.TableLayout;

/**
 * Frame, das die Fehlermeldungen anzeigt
 * 
 * @author Fenn Stefan
 */
public class ExceptionFrame extends JDialog implements IExceptionFrame {
	/** Singelton-Instanz */
	private static ExceptionFrame instance;

	/** Anzeige des Fehlertitels */
	private JLabel errorTitle;

	/** Übersicht aller Fehler seit dem letzem Aufruf */
	private JList errorList;

	/** ScrollPane für errorList */
	private JScrollPane listScrollPane;

	/** Feld in dem die Fehler angezeigt werden */
	private JTextArea messageArea;

	/** ScrollPane für messageArea */
	private JScrollPane messageScrollPane;

	/** OK-Button */
	private JButton okButton;
	
	private Thread dialogThread;
	
	/** zuletzt geworfene Exception */
	private ErrorData lastErrorData;

	/** Singelton-Pattern */
	public static ExceptionFrame getInstance() {
		if (instance == null){
		    /**
		     * Workaround um dem ExceptionFrame ein eigenes 
		     * Icon mitzugeben
		     */
		    JFrame frame = new JFrame();
		    frame.setIconImage(Images.ERROR_ICON.getImage());
			instance = new ExceptionFrame(frame);
		}
		return instance;
	}//getInstance

	private ExceptionFrame(JFrame frame) {
	    super(frame);
		setModal(true);
		initFrame();
	}//Konstruktor
	
	private void initFrame() {
		((JPanel) getContentPane()).setBorder(BorderFactory
				.createLineBorder(Color.BLACK));
		setTitle("Fehler");
		addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                exit();
            }//windowClosing
        });

		//errorList
		errorList = new JList(new DefaultListModel());
		errorList.addKeyListener(new ExitKeyListener());
		errorList.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		errorList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				ErrorData ed = (ErrorData) errorList.getSelectedValue();
				if(ed!=null)
					showError(ed);				
			}
		});
		
		listScrollPane = new JScrollPane(errorList,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0,
				Color.BLACK));		

		//errorTitle
		errorTitle = new JLabel();
		errorTitle.setIcon(Images.ERROR_ICON);

		//messageArea
		messageArea = new JTextArea();
		messageArea.setWrapStyleWord(true);
		messageArea.setLineWrap(true);
		messageArea.setEditable(false);
		messageArea.setFont(FormulaRenderContext.getFont("Courier",Font.PLAIN,12));
		messageArea.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		messageArea.addKeyListener(new ExitKeyListener());

		messageScrollPane = new JScrollPane(messageArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		messageScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0,
				Color.BLACK));

		//okButton
		okButton = new JButton("  OK  ");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    dispose();
				exit();
			}
		});
		okButton.addKeyListener(new ExitKeyListener());
		okButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		JPanel p = (JPanel) getContentPane();

		//Layout
		double border = 10, gap = 10, P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = {
		/* Zeilen */// 
				{ border, F, border },
				/* Spalten */// 
				{ border, P, gap/2, 0.3, gap, P, gap/2, F, gap, P, border } };

		p.setLayout(new TableLayout(size));
		p.add(new JLabel("Überblick:"), "1,1, L,C");
		p.add(listScrollPane, "1, 3, F, F");
		p.add(errorTitle, "1, 5, F, F");
		p.add(messageScrollPane, "1, 7, F, F");
		p.add(okButton, "1, 9, L, C");
	}//init
	
    private boolean firstTime = true;
    
	public void setVisible(boolean b){
		if(firstTime && b){
			setBounds(Settings.computeBounds(Math4U2Win.getInstance(),0.4, 0.4));
			firstTime=false;
		}//if
		super.setVisible(b);
	}//setVisible	

	public void newException(String title, String message) {
		ErrorData ed = new ErrorData(title,message);
		DefaultListModel dlm = (DefaultListModel) errorList.getModel();
		if(!ed.equals(lastErrorData))
		        dlm.addElement(ed);
		lastErrorData = ed;
		
		if(!dlm.isEmpty())
			errorList.setSelectedIndex(0);
		doLayout();
		if (dialogThread==null || !dialogThread.isAlive()){
		    //Neuen Thread erzeugen, damit weiter Fehler
		    //in der Anzeige hinzugefügt werden können.
		    dialogThread = new Thread(){
		        public void run(){
		            setVisible(true);
		        }
		    }; 
		    dialogThread.start();			
		}//if
	}//newException
	
	private void showError(ErrorData ed){
		errorTitle.setText(ed.title);
		messageArea.setText(ed.message);
		messageArea.setCaretPosition(0);
	}//showError
	
	private void exit(){
		((DefaultListModel)errorList.getModel()).removeAllElements();
		lastErrorData = null;
		dispose();
	}//exit

	/** 
	 * Kapselt eine Fehlermeldung für die ListBox 
	 */
	class ErrorData {
		private String title, message;

		public ErrorData(String title, String message) {
			this.title = title;
			this.message = message;
		}//Konstruktor

		public String toString() {
			return title;
		}//toString
		
		public boolean equals(Object obj){
		    if(obj==null) return false;
		    if(obj instanceof ErrorData){
		        ErrorData ed = (ErrorData) obj;
		        if(ed.title == null) ed.title="";
		        if(ed.message==null) ed.message="";
		        return ed.title.equals(title) && ed.message.equals(message);
		    }
		    return false;
		}//equals
	}//class ErrorData
	
	/**
	 * Wenn Enter gedrückt wird, schließt sich das Fenster
	 */
	class ExitKeyListener extends KeyAdapter{
		public void keyTyped(KeyEvent e) {
			if ((int) e.getKeyChar() == 10)
				exit();
		}//keyTyped
	}//class ExitKeyListner	

}//class ExceptionFrame
