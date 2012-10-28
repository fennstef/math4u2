package math4u2.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.print.PrinterJob;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import math4u2.application.resource.Folders;
import math4u2.application.resource.Images;
import math4u2.application.resource.Settings;
import math4u2.controller.Broker;
import math4u2.controller.ExtendedBroker;
import math4u2.exercises.EParser;
import math4u2.exercises.Math4U2Document;
import math4u2.exercises.scripting.EActionContainer;
import math4u2.exercises.ui.ETree;
import math4u2.exercises.ui.ExercisePanel;
import math4u2.exercises.ui.Math4u2TextPane;
import math4u2.util.swing.print.JPreview;
import math4u2.util.swing.print.PreviewPanel;
import math4u2.util.swing.print.PrintUtilities;
import math4u2.view.formula.FormulaRenderContext;

/**
 * 
 * @author Fenn Stefan
 */
public class HelpFrame extends JFrame {

	/** Referenz zum Broker, wenn es im helpFrame ist */
	private Broker helpBroker = new ExtendedBroker();

	private JSplitPane splitPane;
	private ExercisePanel exercises;
	private ETree tree;

	public HelpFrame() {
		super("Hilfe");
		init();
	} //Konstruktor

	public void init() {
		EParser parser = new EParser(helpBroker);
		XMLManager.analyseFolderRecursive(Folders.HELP_FOLDER, parser);
		exercises = XMLManager.createExercisePanel(parser, true);

		Math4U2Document doc = XMLManager.parseDocument(parser);
		tree = new ETree(doc.getIssues(), doc.getFolders(),
				ETree.COLLAPSE_TREE);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				exercises.setCurrentElement(e.getSource());
				EActionContainer.stopAllActions();
				exercises.organizeTreeMousePress();
			}//valueChanged
		});
		
		//printMenu
//		JMenuBar menuBar = new JMenuBar();
//		JMenu menuFile = new JMenu("Datei");
//        JMenuItem printMenu = new JMenuItem("Drucken ...");
//        printMenu.setToolTipText("Drucken der aktuellen Lektion");
//        printMenu.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//            	showPrintMenu();
//            } //actionPerformed
//        });
//        menuBar.add(menuFile);
//        menuFile.add(printMenu);
//        getContentPane().add(menuBar, BorderLayout.NORTH);
        

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, exercises);
		splitPane.setOneTouchExpandable(true);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane
				.setDividerLocation((int) ((Settings.SCREEN_SIZE.width) * 0.25));

		setIconImage(Images.LOGO_ICON.getImage());		
		getContentPane().add(splitPane);
	} //init
	
    /**
     * Zeigt die Druckvorschau an. Zuvor werden die einzelnen
     * Komponenten für das Drucken vorbereitet.
     */
	public void showPrintMenu(){
		//erzeuge aus Swing ein Printable		
		Math4u2TextPane[] views =exercises.getAllViewsToPrint();
		Component[] ca = new Component[views.length+1];
		for(int i=0; i<views.length; i++){
			ca[i+1]=views[i];
		}
		
		//Header-Komponente mit Logo und Titel
		JComponent header = new JComponent(){
			public void paintComponent(Graphics g){
				super.paintComponent(g);				
				g.setColor(Color.BLACK);
				g.drawRect(0,0,getWidth()-1,getHeight()-1);
				
				String title = tree.getJTree().getSelectionPath().toString();
				title = title.replace(",", "  / ");
				title = title.replace("[", "");
				title = title.replace("]", "");
				int fontSize = 8;
				Font font = FormulaRenderContext.getFont("Dialog", Font.BOLD, fontSize);
	            g.setFont(font);
	            int tWidth = g.getFontMetrics().stringWidth(title);
	            setSize(tWidth+10,fontSize+5);
	            
	            g.setColor(Color.BLACK);
	            g.drawString(title, 5, fontSize+1);
			}//paintComponent
		};
		
		header.setSize(600,32);

		ca[0]= header;
				
		PrintUtilities pa = new PrintUtilities(ca,true);
		
		//Druckvorschau aufbauen
		JFrame frame = new JFrame("Druckvorschau");
		frame.setIconImage(Images.PRINT_ICON.getImage());
		PrinterJob job = PrinterJob.getPrinterJob();
		JPreview preview = new JPreview(job, pa, PreviewPanel.getStandardPageFormatPortrait(job));
		frame.setContentPane(preview);
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}//showPrintMenu    	
	
    private boolean firstTime = true;
    
	public void setVisible(boolean b){
		if(firstTime && b){
			setBounds(Settings.computeBounds(Math4U2Win.getInstance(),0.8, 0.85));
			firstTime=false;
		}//if
		super.setVisible(b);
	}//setVisible	

} //class HelpFrame
