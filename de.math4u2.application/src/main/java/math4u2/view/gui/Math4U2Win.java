package math4u2.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.print.PrinterJob;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import math4u2.application.MainWindow;
import math4u2.application.resource.Folders;
import math4u2.application.resource.Images;
import math4u2.application.resource.Settings;
import math4u2.application.resource.Versions;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.ExtendedBroker;
import math4u2.controller.HasBroker;
import math4u2.exercises.EIssue;
import math4u2.exercises.EParser;
import math4u2.exercises.Exercise;
import math4u2.exercises.Math4U2Document;
import math4u2.exercises.scripting.EActionContainer;
import math4u2.exercises.ui.ETree;
import math4u2.exercises.ui.ETreeNodeInfo;
import math4u2.exercises.ui.ExercisePanel;
import math4u2.exercises.ui.Math4u2TextPane;
import math4u2.mathematics.affine.MousePosition;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.io.file.xml.filestatus.Local;
import math4u2.util.io.file.xml.filestatus.Remote;
import math4u2.util.swing.SaveImageUtil;
import math4u2.util.swing.key.GlobalHotkeyManager;
import math4u2.util.swing.print.JPreview;
import math4u2.util.swing.print.PrintUtilities;
import math4u2.view.ContentPane;
import math4u2.view.dragAndDrop.target.DropTargetAndDeny;
import math4u2.view.dragAndDrop.target.DropTargetOnTrash;
import math4u2.view.formula.FormulaRenderContext;
import math4u2.view.graph.drawarea.DrawArea;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.drawarea.DrawAreasManager;
import math4u2.view.gui.listview.ListBox;
import math4u2.view.gui.listview.ViewFactorySingleton;
import math4u2.view.gui.listview.complete.CompleteViewBox;
import math4u2.view.gui.save.SaveDialog;
import math4u2.view.gui.wizard.WizardUtil;
import math4u2.view.layout.PercentLayout;
import math4u2.view.layout.TableLayout;

/**
 * Klasse für das Hauptfenster.
 * 
 * @author Erich Seifert, Fenn Stefan
 */
public class Math4U2Win extends JFrame implements HasBroker {

    /** Singelton-Referenz */
    private static Math4U2Win instance;

    /** Hilfe-Fenster */
    HelpFrame helpFrame;

    /** Container aller Zeichenflächen */
    private JPanel drawAreaContainer;

    /** Alle Eingabeeinheiten */
    private JPanel topPanel;

    /** Trenner von DrawAreaManager und Excercise */
    private JSplitPane splitPane;

    /** Darstellung der Steps bzw. Übungen */
    ExercisePanel exercises;

    /** Darstellung des Themenbaumes */
    private NavigationPanel navigationPanel;

    /** Fenster in der die Themenübersicht dargestellt wird */
    private JFrame treeFrame;

    /** PopupMenü in der oberen Leiste */
    private JPopupMenu popupMenu = new JPopupMenu();

    /** ListBox, in der alle Funktionen eingetragen sind */
    private ListBox listBox;

    /** Gesamt-Sicht einer Funktion */
    private CompleteViewBox completeViewBox;

    /** Referenz zum Broker */
    private ExtendedBroker broker = new ExtendedBroker();

    public LinkedHashSet actualTreePaths = new LinkedHashSet();

    public String actualTreeSelection = "";
    
    private JMenu fontMenu;
    
    /**
     * Titel des Hauptfensters
     */
    private String title;

    /** Singleton-Methode */
    public static Math4U2Win getInstance() {
        if (instance == null) {
            instance = new Math4U2Win();            
            /*
             * darf nicht im Konstruktor aufgerufen werden, da sonst eine
             * Endlosscheife entstehen würde, wenn eine andere Klasse wieder auf
             * getInstance() zugreift.
             */
            instance.init();
            MainWindow.initMainWindow(instance);
        } //if
        return instance;
    } //getInstance

    /**
     * Verdeckter Konstruktor. Um eine Instanz zu bekommen wird die Methode
     * <code>getInstance()</code> aufgerufen.
     * 
     * @see math4u2.view.gui.Math4U2Win#getInstance()
     */
    private Math4U2Win() {
        super();
        setTitleText("");
    } //Konstruktor

    /**
     * Einmalige Initialisierung
     */
    private void init() {
    	addWindowFocusListener(new WindowFocusListener(){
			public void windowGainedFocus(WindowEvent arg0) {
				if(ExceptionFrame.getInstance().isShowing())
					ExceptionFrame.getInstance().toFront();
			}
			public void windowLostFocus(WindowEvent arg0) {
			}
    	});
    	
        //Inhalte aus Temp-Datei löschen
        XMLManager.deleteTempContents();

        //Debugger
        initDebugger();

        //Layout
        getContentPane().setLayout(new BorderLayout(0, 0));
        ContentPane.setAbsoluteFontSize(3);

        //Hilfe-Frame vorbereiten
        helpFrame = new HelpFrame();

        //ToolPanel
        initTopPanel();

        //DrawAreaContainer
        initDrawAreaContainer();

        //Exercises
        EParser parser = initExercises();

        //TreeView
        initTreeView(parser);

        //SplitPane
        initSplitPane();

        //Frame
        initFrame();

        //ObjektListe gleich öffnen
        try {
            ((ListBox) broker.getObject("ListBox")).setExpandList(true);
        } catch (BrokerException e) {
            ExceptionManager.doError("Fehler beim Holen des Objekts ListBox",e);
        } //catch
        
        try {
			((CompleteViewBox) broker.getObject("CompleteView"))
					.setExpandList(true);
		} catch (BrokerException e) {
			ExceptionManager.doError(
					"Fehler beim Holen des Objekts CompleteView", e);
		}//catch

    } //init

    /**
     * Initialisiert den Themenbaum
     */
    private void initTreeView(EParser parser) {
        Math4U2Document doc = XMLManager.parseDocument(parser);
        navigationPanel = new NavigationPanel(doc.getIssues(),
                doc.getFolders(), ETree.COLLAPSE_TREE);
        
        navigationPanel.getJTree().addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent arg0) {
                navigationPanel.defaultUpdate();
                TreePath path = arg0.getPath();
                if (path != null) {
                    DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) path
                            .getLastPathComponent();
                    ETreeNodeInfo nodeinfo = (ETreeNodeInfo) dmtn
                            .getUserObject();
                    EActionContainer.stopAllActions();
                    if (nodeinfo.getObject() instanceof EIssue || nodeinfo.getObject() instanceof Exercise) {
                        navigationPanel.setActualIssueOrExercise(nodeinfo.getObject());
                        navigationPanel.update();
                    }else{
                    	navigationPanel.setActualIssueOrExercise(null);
                    }                   
                }//if else
                navigationPanel.getJTree().requestFocus();                    
			}}
        );
        
        navigationPanel.getJTree().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					Object ie = navigationPanel.getActualIssueOrExcercise();
					if (ie == null)
						return;
					treeFrame.setVisible(false);
					if (ie instanceof EIssue && ((EIssue)ie).getStatus() instanceof Remote) {
						navigationPanel.getPopupMenuEIssue()
								.downloadRemoteFile();
					}//if
					exercises.setCurrentElement(ie);
							
					(new Thread() {
						public void run() {							
							try {
								SwingUtilities.invokeAndWait(new Runnable(){
									public void run() {
										treeFrame.setVisible(false);
										exercises.showLoading();
									}//run
								});
							} catch (InterruptedException e) {
								ExceptionManager.doError(e);
							} catch (InvocationTargetException e) {
								ExceptionManager.doError(e);
							}
							try {
								SwingUtilities.invokeAndWait(new Runnable(){
									public void run() {
										exercises.organizeTreeMousePress();
									}//run
								});
							} catch (InterruptedException e) {
								ExceptionManager.doError(e);
							} catch (InvocationTargetException e) {
								ExceptionManager.doError(e);
							}
							
							try {
								SwingUtilities.invokeAndWait(new Runnable(){
									public void run() {
										exercises.hideLoading();
									}//run
								});
							} catch (InterruptedException e) {
								ExceptionManager.doError(e);
							} catch (InvocationTargetException e) {
								ExceptionManager.doError(e);
							}
						}//run
					}).start();
				}//if doppelklick
			}//mouseClicked

			public void doPopup(MouseEvent evt) {
				if (evt.isPopupTrigger()) //rechte Maustaste
				{
					TreePath path = navigationPanel.getJTree()
							.getPathForLocation(evt.getX(), evt.getY());
					if (path != null) {
						DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) path
								.getLastPathComponent();
						ETreeNodeInfo nodeinfo = (ETreeNodeInfo) dmtn
								.getUserObject();
						if (nodeinfo.getObject() instanceof EIssue) {
							EIssue ie = (EIssue) nodeinfo.getObject();
							navigationPanel.setActualIssueOrExercise(ie);
							if (ie.getStatus() instanceof Local) {
								navigationPanel.showLocalPopupMenue(evt);
							} else if (ie.getStatus() instanceof Remote) {
								navigationPanel.showRemotePopupMenue(evt);
							} else {
								navigationPanel.showTempPopupMenue(evt);
							}
						} else {
							navigationPanel.getPopupMenuGlobal().show(
									navigationPanel.getJTree(), evt.getX(),
									evt.getY());
						}
					} else {
						navigationPanel.getPopupMenuGlobal().show(
								navigationPanel.getJTree(), evt.getX(),
								evt.getY());
					}
				} //if
			} //mousePressed

			public void mousePressed(MouseEvent evt) {
				doPopup(evt);
			}

			public void mouseReleased(MouseEvent evt) {
				doPopup(evt);
			}
		});
        treeFrame = new JFrame("Themen-Navigator"){
        	private boolean firstTime=true;
        	
        	public void setVisible(boolean b){
        		if(firstTime && b){
        			setBounds(Settings.computeBounds(Math4U2Win.getInstance(),0.5, 0.7));
        			firstTime=false;
        		}//if
        		super.setVisible(b);
        	}//setVisible
        };
        treeFrame.getContentPane().add(navigationPanel);
        treeFrame.setIconImage(Images.LOGO_ICON.getImage());
    } //initTreeView

    /**
	 * Initialisiert das Themenblatt
	 */
    private EParser initExercises() {
        EParser parser = new EParser(broker);
        XMLManager.analyseFolderRecursive(Folders.XML_FOLDER, parser);
        exercises = XMLManager.createExercisePanel(parser, false);

        return parser;
    } //initExercises

    /**
     * Importieren eines Ordners
     * 
     * @param rootStr
     *            Ordner als String
     */
    public void analyseFolder(String rootStr) {
        EParser parser = new EParser(broker);
        XMLManager.analyseFolderRecursive(rootStr, parser);
    }//analyseFolder

    /**
     * Initialisiert das Hauptfenster
     */
    private void initFrame() {
        setIconImage(Images.LOGO_ICON.getImage());
        setBounds(Settings.computeBounds(null,0.9, 0.9));
        setExtendedState(MAXIMIZED_BOTH);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                listBox.setExpandList();
                listBox.scrollPaneHasResized();
                completeViewBox.setExpandList();
                completeViewBox.scrollPaneHasResized();
            }//compoentResized
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            } //windowClosing
        });
    } //initFrame

    /**
     * Initialisiert den Trenner zwischen Zeichenfläche und Themensicht
     */
    private void initSplitPane() {
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                drawAreaContainer, exercises);
        splitPane.setOneTouchExpandable(true);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        splitPane
                .setDividerLocation((int) ((Settings.SCREEN_SIZE.height - 80) * 0.7));
        splitPane.setBorder(null);
    } //initSplitPane

    /**
     * Initialisiert den Container aller Zeichenflächen
     */
    private void initDrawAreaContainer() {
        drawAreaContainer = new JPanel();
        double[][] lx = new double[][] { { 1.0 } };
        double[] ly = new double[] { 1.0 };
        drawAreaContainer.setLayout(new PercentLayout(lx, ly));
        drawAreaContainer.setBorder(BorderFactory.createLineBorder(
                getBackground(), 5));
        drawAreaContainer.add((JComponent) DrawAreasManager.get(broker, "da"));
        getContentPane().add(drawAreaContainer, BorderLayout.CENTER);
    } //initDrawAreaContainer

    /**
     * Initialisiert die obere Leiste
     */
    private void initTopPanel() {
        //TopPanel
        topPanel = new JPanel(null);
        popupMenu.setInvoker(topPanel);
        popupMenu.setLightWeightPopupEnabled(true);

        // deleteAllMenu
        JMenuItem deleteAllMenu = new JMenuItem();
        deleteAllMenu.setToolTipText("löscht alle Objekte");
        deleteAllMenu.setText("Alles löschen");
        deleteAllMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	if (Versions.WITH_RADICAL_DELETE) {
					broker.deleteAllObjects();					
					DrawAreasManager.get(broker, "da");
				}//if

           		exercises.startElement(ExercisePanel.REINIT_STEP);
                exercises.startElement(ExercisePanel.INIT_STEP);
                Math4U2Win.getInstance().setTitleText("");
            } //actionPerformed
        });
        popupMenu.add(deleteAllMenu);

        // saveMenu
        JMenuItem saveMenu = new JMenuItem("Speichern unter ...");
        saveMenu.setToolTipText("Speichert alle Objekte");
        saveMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                SaveDialog sd = new SaveDialog();
                sd.setVisible(true);
            } //actionPerformed
        });
        popupMenu.add(saveMenu);
        
        //printMenu
        JMenuItem printMenu = new JMenuItem("Drucken ...");
        printMenu.setToolTipText("Drucken der aktuellen Lektion");
        printMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	showPrintMenu();
            } //actionPerformed
        });
        popupMenu.add(printMenu);
        
        //Seperator
        popupMenu.add(new JSeparator());

        // Schrifttyp
        ActionListener fontListener = new FontActionListener();
        fontMenu = new JMenu("Schrifttyp");  
        
        int fontSize = (new JTextField()).getFont().getSize();
        String fontName = (new JTextField()).getFont().getName();
        JCheckBoxMenuItem font1Menu = new JCheckBoxMenuItem("Schrift: Variante 1");
        font1Menu.setFont(FormulaRenderContext.getFont(fontName,Font.PLAIN, fontSize));
        font1Menu.addActionListener(fontListener);
        font1Menu.setSelected(true);
        fontMenu.add(font1Menu);
        
        JCheckBoxMenuItem font2Menu = new JCheckBoxMenuItem("Schrift: Variante 2");
        font2Menu.setFont(FormulaRenderContext.getFont(fontName,Font.BOLD, fontSize));
        font2Menu.addActionListener(fontListener);
        fontMenu.add(font2Menu);
        
        JCheckBoxMenuItem font3Menu = new JCheckBoxMenuItem("Schrift: Variante 3");
        font3Menu.setFont(FormulaRenderContext.getFont(fontName,Font.PLAIN, fontSize+6));
        font3Menu.addActionListener(fontListener);
        fontMenu.add(font3Menu);
        
        JCheckBoxMenuItem font4Menu = new JCheckBoxMenuItem("Schrift: Variante 4");
        font4Menu.setFont(FormulaRenderContext.getFont(fontName,Font.BOLD, fontSize+6));
        font4Menu.addActionListener(fontListener);
        fontMenu.add(font4Menu);
        
        JCheckBoxMenuItem font5Menu = new JCheckBoxMenuItem("Schrift: Variante 5");
        font5Menu.setFont(FormulaRenderContext.getFont(fontName,Font.PLAIN, fontSize+12));
        font5Menu.addActionListener(fontListener);
        fontMenu.add(font5Menu);
        
        JCheckBoxMenuItem font6Menu = new JCheckBoxMenuItem("Schrift: Variante 6");
        font6Menu.setFont(FormulaRenderContext.getFont(fontName,Font.BOLD, fontSize+12));
        font6Menu.addActionListener(fontListener);
        fontMenu.add(font6Menu);        

        popupMenu.add(fontMenu);        

        //Seperator
        popupMenu.add(new JSeparator());

        // mouseMenu
        JMenuItem mouseMenu = new JMenuItem("Mauskoordinaten anzeigen");
        mouseMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                MousePosition.setBroker(broker);
                CompleteViewBox.register(MousePosition.getInstance(ViewFactorySingleton.getInstance())
                        .getMouseFunction(), completeViewBox, broker);
            } //actionPerformed
        });
        popupMenu.add(mouseMenu);

        topPanel.addMouseListener(new PopupMouseListener());

        //Trash
        JLabel trashLabel = new JLabel(Images.TRASH);
        trashLabel.setPreferredSize(new Dimension(30, 30));
        trashLabel.setToolTipText("Drag & Drop Löschfeld");
        trashLabel.setFocusable(false);
        new DropTargetOnTrash(trashLabel);

        //TreeView
        AbstractAction treeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	  treeFrame.setVisible(!treeFrame.isVisible());
            	  ExceptionFrame.getInstance().toFront();
            } //actionPerformed
        };
        treeAction.putValue(AbstractAction.SMALL_ICON, Images.TREE);
        treeAction.putValue(AbstractAction.SHORT_DESCRIPTION,
                "Themen-Navigator ( STRG + Y )");
        JButton treeButton = new JButton(treeAction);
        treeButton.setPreferredSize(new Dimension(28, 28));
        treeButton.setFocusable(false);
        new DropTargetAndDeny(treeButton);

        GlobalHotkeyManager hotkeyManager = GlobalHotkeyManager.getInstance();
        hotkeyManager.getInputMap().put(KeyStroke.getKeyStroke("control Y"),
                "Tree");
        hotkeyManager.getActionMap().put("Tree", treeAction);

        //Help
        AbstractAction helpAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!helpFrame.isVisible()) {
                    helpFrame.setVisible(true);
                    helpFrame.setVisible(false);
                    helpFrame.setExtendedState(MAXIMIZED_BOTH);
                    helpFrame.setVisible(true);
                } else
                    helpFrame.dispose();
            } //actionPerformed
        };
        helpAction.putValue(AbstractAction.SMALL_ICON, Images.HELP);
        helpAction.putValue(AbstractAction.SHORT_DESCRIPTION, "Hilfe ( F1 )");

        JButton helpButton = new JButton(helpAction);
        helpButton.setPreferredSize(new Dimension(28, 28));
        helpButton.setAction(helpAction);
        helpButton.setFocusable(false);
        new DropTargetAndDeny(helpButton);

        hotkeyManager.getInputMap().put(KeyStroke.getKeyStroke("F1"), "Help");
        hotkeyManager.getActionMap().put("Help", helpAction);

        //Button weitere Aktionen
        final JButton furtherActions = new JButton("...");
        furtherActions.setPreferredSize(new Dimension(30, 30));
        furtherActions.setToolTipText("weitere Aktionen");
        furtherActions.setFocusable(false);
        furtherActions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                popupMenu.setLocation(furtherActions.getLocationOnScreen());
                popupMenu.setVisible(true);
            }
        });
        
        //Save Image Aktion
        AbstractAction saveImageAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	System.out.println("Save Image");
            	
            	final JFrame frame = new JFrame("Bild speichern");
            	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            	frame.setSize(250,200);
            	List das = DrawAreasManager.getAllDrawAreas();
            	String[] names = new String[das.size()];
            	final HashMap das2 = new HashMap();
            	for(int i=0; i<names.length; i++){
            		names[i] = ((DrawAreaInterface)das.get(i)).getName();
            		das2.put(names[i],das.get(i));
            	}            	
            	JPanel panel = new JPanel();
            	final JTextField widthField = new JTextField(10);
            	final JTextField heightField = new JTextField(10);
            	final JTextField scaleField = new JTextField("1.0");
            	final JTextField waitField = new JTextField("0");
            	final JComboBox box = new JComboBox(names);
            	box.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						String name = (String) box.getSelectedItem();
						DrawArea da = (DrawArea) das2.get(name);
						widthField.setText(da.getWidth()+"");
						heightField.setText(da.getHeight()+"");
					}
            	});
            	String name = (String) box.getSelectedItem();
				DrawArea da = (DrawArea) das2.get(name);
				widthField.setText(da.getWidth()+"");
				heightField.setText(da.getHeight()+"");
            	
            	JButton okButton = new JButton("OK");
            	okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						String name = (String) box.getSelectedItem();
						DrawArea da = (DrawArea) das2.get(name);
						int oldWidth = da.getWidth();
						int oldHeight = da.getHeight();
						int width = Integer.parseInt(widthField.getText());
						int height = Integer.parseInt(heightField.getText());
						double scale = Double.parseDouble(scaleField.getText());
						int wait = Integer.parseInt(waitField.getText());;
		            	da.setDrawareaSize(width, height);
		            	
		            	JFileChooser fc = new JFileChooser();
		            	fc.showSaveDialog(frame);
		            	
		            	SaveImageUtil.saveComponentAsJPEG(da, fc.getSelectedFile(),scale,wait);
		            	da.setDrawareaSize(oldWidth, oldHeight);		            	
		            	
		            	frame.dispose();
					}
            	});            	
            	JButton cancelButton = new JButton("Abbrechen");
            	cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						frame.dispose();
					}
            	});
            	
            	//Layout
                double border = 10, gap = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
            	double size[][] = {
                        { border, P, 3*gap, F, border }, 
                        { border, P, gap, P, gap, P, gap, P, gap, P, gap, F,border} };
                panel.setLayout(new TableLayout(size));
            	frame.getContentPane().add(panel);
            	panel.add(new JLabel("Zeichenfläche: ")	,"1,1,L,C");
            	panel.add(box 							,"3,1,F,F");
            	panel.add(new JLabel("Breite: ")		,"1,3,L,C");
            	panel.add(widthField					,"3,3,F,F");
            	panel.add(new JLabel("Höhe: ")			,"1,5,L,C");
            	panel.add(heightField					,"3,5,F,F");
            	panel.add(new JLabel("Skalierung: ")	,"1,7,L,C");
            	panel.add(scaleField					,"3,7,F,F");
            	panel.add(new JLabel("Wartezeit (sek): ")		,"1,9,L,C");
            	panel.add(waitField						,"3,9,F,F");            	
            	panel.add(okButton						,"1,11,L,B");
            	panel.add(cancelButton					,"3,11,L,B");
            	
            	frame.setVisible(true);
            } //actionPerformed
        };
        saveImageAction.putValue(AbstractAction.SMALL_ICON, Images.HELP);
        saveImageAction.putValue(AbstractAction.SHORT_DESCRIPTION, "Speichere Bild");

        hotkeyManager.getInputMap().put(KeyStroke.getKeyStroke("control S"), "Speichere Bild");
        hotkeyManager.getActionMap().put("Speichere Bild", saveImageAction);

        listBox = new ListBox(broker);
        completeViewBox = new CompleteViewBox(broker);
        WizardUtil.getDefinitionWizard();

        //Layout
        double border = 10, gap = 10, P = TableLayout.PREFERRED, F = TableLayout.FILL;
        double size[][] = {
                /* Zeilen */// 
                { border, P, gap * 2, P, gap / 2, P, gap * 2, P, gap, F, gap,
                        F, border },
                /* Spalten */// 
                { 5, P, 0 } };

        topPanel.setLayout(new TableLayout(size));

        topPanel.add(helpButton, "1, 1, L, C");
        topPanel.add(treeButton, "3, 1, L, C");
        topPanel.add(furtherActions, "5, 1, L, C");
        topPanel.add(trashLabel, "7, 1, L, C");
        topPanel.add(listBox, "9, 1, F, F");
        topPanel.add(completeViewBox, "11, 1, F, F");

        getContentPane().add(topPanel, BorderLayout.NORTH);
    } //initTopPanel

    /**
     * Initialisiert das Debug-Fenster Öffnen mit STRG + D
     */
    private void initDebugger() {
        BrokerDebugGui bdg = new BrokerDebugGui(broker);
        GlobalHotkeyManager hotkeyManager = GlobalHotkeyManager.getInstance();
        hotkeyManager.getInputMap().put(BrokerDebugGui.debugHotkey, "Debug");
        hotkeyManager.getActionMap().put("Debug", bdg.debug);
    } //initDebugger
    
    
    /**
     * Zeigt die Druckvorschau an. Zuvor werden die einzelnen
     * Komponenten für das Drucken vorbereitet.
     */
	public void showPrintMenu(){
		//erzeuge aus Swing ein Printable		
		Math4u2TextPane[] views =exercises.getAllViewsToPrint();
		Component[] ca = new Component[views.length+2];
		for(int i=0; i<views.length; i++){
			ca[i+2]=views[i];
		}
		
		//Header-Komponente mit Logo und Titel
		JComponent header = new JComponent(){
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				
//				g.setColor(Color.BLACK);
//				g.drawRect(0,0,getWidth()-1,getHeight()-1);
				
				String title = getTitleText();
				Font font = FormulaRenderContext.getFont("Dialog", Font.BOLD, 16);
	            g.setFont(font);
	            int tWidth = g.getFontMetrics().stringWidth(title) + 150;
	            setSize(tWidth,32);
	            
	            g.setColor(Color.BLACK);
	            g.drawString(title, 110, 26);
				
				
				g2.scale(100.0/Images.LOGO_BIG.getIconWidth(),32.0/Images.LOGO_BIG.getIconHeight());
				g.drawImage(Images.LOGO_BIG.getImage(),0,0,Color.WHITE,null);
			}//paintComponent
		};
		
		header.setSize(getDrawAreaContainer().getWidth(),32);

		ca[0]= header;
		ca[1]= getDrawAreaContainer();
			
		PrintUtilities pa = new PrintUtilities(ca,false);
		
		//Druckvorschau aufbauen
		JFrame frame = new JFrame("Druckvorschau");
		frame.setIconImage(Images.PRINT_ICON.getImage());
		frame.setContentPane(new JPreview(PrinterJob.getPrinterJob(), pa));
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);	
	}//showPrintMenu    

    /**
     * Gibt den Container aller Zeichenflächen zurück
     */
    public JPanel getDrawAreaContainer() {
        return drawAreaContainer;
    } //getDrawAreacontainer
    
    /**
     * Gibt die Definitionseingabe (und Zubehör) zurück
     */
    public ListBox getListBox(){
    	return listBox;
    }//getListBox
    
    /**
     * Was muß erneuert werden, wenn der XML-Baum erneuert wird?
     * 
     * @see math4u2.view.gui.XMLManager#refresh(EParser parser)
     */
    public void layoutAfterRefresh(final ExercisePanel exercises,
            EParser parser, boolean show) {
        this.exercises = exercises;
        int divLoc = splitPane.getDividerLocation();
        splitPane.setBottomComponent(exercises);
        splitPane.setDividerLocation(divLoc);
        Rectangle rec = treeFrame.getBounds();
        JFrame oldFrame = treeFrame;
        initTreeView(parser);
        treeFrame.setBounds(rec);
        if (show) {
            treeFrame.setVisible(true);
        }
        oldFrame.dispose();
        ExceptionFrame.getInstance().toFront();
    } //layoutAfterRefresh

    /**
     * Gibt den aktuellen Broker zurück
     */
    public Broker getBroker() {
        return broker;
    } //getBroker

    private final class PopupMouseListener extends MouseAdapter {
        public void doPopup(MouseEvent evt) {
            if (evt.isPopupTrigger()) {
                popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            } //if
        } //doPopup

        public void mousePressed(MouseEvent evt) {
            doPopup(evt);
        } //mousePressed

        public void mouseReleased(MouseEvent evt) {
            doPopup(evt);
        } //mouseReleased
    } //class PopupMouseListener

    
    private final class FontActionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			JCheckBoxMenuItem m = (JCheckBoxMenuItem) arg0.getSource();
			listBox.getInputField().setFont(m.getFont());
			
			Component[] ca = fontMenu.getMenuComponents();
			for(int i=0; i<ca.length; i++){
				JCheckBoxMenuItem mi = (JCheckBoxMenuItem) ca[i];
				mi.setSelected(mi.getFont().equals(listBox.getInputField().getFont()));
			}//for i
			
		}//actionPerformed
    }//class FontActionListener
    
    public ExercisePanel getExercisePanel() {
        return exercises;
    }//getExercisePanel
    
    /**
	 * Setzen des Titels im Hauptfenster
	 */
	public void setTitleText(String title) {
		this.title = title;
	    if (title != null && title.length() != 0)
	        title = " - " + title;
	    setTitle("math4u2 " + Versions.APPLICATION_VERSION + title);
	}//setTitleText

	/**
     * Gibt den Titel ohne math4u2 V... zurück
     */
    public String getTitleText(){
    	return title;
    }//getTitleText
    
} //class Math4u2Win
