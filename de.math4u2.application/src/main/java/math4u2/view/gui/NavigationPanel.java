
package math4u2.view.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.plaf.basic.BasicTreeUI;

import math4u2.application.resource.Images;
import math4u2.application.resource.Settings;
import math4u2.exercises.EIssue;
import math4u2.exercises.Exercise;
import math4u2.exercises.ui.ETree;
import math4u2.exercises.ui.ETreePopupMenuGlobal;
import math4u2.exercises.ui.ETreePopupMenuIssue;
import math4u2.exercises.ui.ETreeServerDialog;
import math4u2.view.layout.TableLayout;

public class NavigationPanel extends ETree{

    protected JPopupMenu popupMenuGlobal;
    protected ETreePopupMenuIssue popupMenuEIssue;

    protected JFrame serverDialog;

    private JSplitPane splitPane;

    private JTabbedPane tab;

    private JLabel fileStatusImageLabel;
    private JLabel statusLabel;
    private JLabel titleLabel;
    private JLabel authorLabel;
    private JLabel difficultyLabel;
    private JLabel keywordsLabel;
    private JLabel locationLabel;
    private JTextArea textAreaInformationTab;
    private JScrollPane scrollTextInformationTab;
    
    private JTextArea textAreaServernewsTab;
    private JScrollPane scrollTextServernewsTab;
    private JToolBar toolBar;
    
    /**
     * @param issues
     * @param folders
     * @param expandTree
     */
    public NavigationPanel(List issues, List folders, boolean expandTree) {
        super(issues, folders, expandTree);
        serverDialog = new ETreeServerDialog(this);
        this.refreshPopupMenuGlobal();
        popupMenuEIssue = new ETreePopupMenuIssue(this);
        fileStatusImageLabel = new JLabel(Images.LOCAL_FILE);
        statusLabel = new JLabel("");
        titleLabel = new JLabel("");
        authorLabel = new JLabel("");
        difficultyLabel = new JLabel("");
        keywordsLabel = new JLabel("");
        locationLabel = new JLabel("");
        textAreaInformationTab = new JTextArea(10, 10);
        textAreaInformationTab.setEditable(false);
        textAreaInformationTab.setWrapStyleWord(true);
        textAreaInformationTab.setLineWrap(true);
        textAreaInformationTab.setText("");
        scrollTextInformationTab = new JScrollPane(textAreaInformationTab);
        
        textAreaServernewsTab = new JTextArea(10, 10);
        textAreaServernewsTab.setEditable(false);
        textAreaServernewsTab.setWrapStyleWord(true);
        textAreaServernewsTab.setLineWrap(true);
        textAreaServernewsTab.setText("");
        scrollTextServernewsTab = new JScrollPane(textAreaServernewsTab);
        
        init();
        
        if (XMLManager.getServernews() != null){
            tab.setSelectedIndex(1);
            textAreaServernewsTab.setText(XMLManager.getServernews());
        }
        
    }//Konstruktor
    
    protected void init(){
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        tab = new JTabbedPane();
        tab.add(buildInformationTab(), "Lektion");
        tab.add(buildServerNewsTab(), "Server-News");
        splitPane.add(scrollPane, JSplitPane.LEFT);
        splitPane.add(tab, JSplitPane.RIGHT);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(Settings.computeBounds(null,0.5, 0.7).width / 2);
        add(splitPane, BorderLayout.CENTER);
        
        //setzen der Bilder für Ordner offen/zu
        ((BasicTreeUI) jTree.getUI()).setExpandedIcon(Images.COLLAPSED_ICON);
        ((BasicTreeUI) jTree.getUI()).setCollapsedIcon(Images.EXPAND_ICON);
        buildToolBar();
        add(toolBar, BorderLayout.NORTH);          
    }//init
    
    private void buildToolBar(){
        toolBar = new JToolBar();
        JButton refresh = new JButton(Images.REFRESH);
        refresh.setToolTipText("Alles neu laden");
        refresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                XMLManager.refreshFull(true);
            }//actionPerformed
        });        
        toolBar.add(refresh);
        
        final JButton earth = new JButton(Images.EARTH);
        earth.setToolTipText("Serverliste");
        earth.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                popupMenuGlobal.setLocation(earth.getLocationOnScreen());
                popupMenuGlobal.setVisible(true);
            }//actionPerformed
        });        
        toolBar.add(earth);      
    }//buildToolBar
    
    public void defaultUpdate(){
        statusLabel.setText("");
        titleLabel.setText("");
        authorLabel.setText("");
        //difficultyLabel.setText("");
        //keywordsLabel.setText("");
        textAreaInformationTab.setText("");
    }
    
    public void update() {
        tab.setSelectedIndex(0);
        if(actualIssueOrExercise instanceof Exercise) return;
        EIssue ie = (EIssue) actualIssueOrExercise;
        fileStatusImageLabel.setIcon(ie.getStatus().getIcon());
        statusLabel.setText(ie.getStatus().getDescription());
        titleLabel.setText(ie.getTitle());
        authorLabel.setText(ie.getAuthor());
        //difficultyLabel.setText(super.actualIssue.getTitle());
        List keyWords = ie.getStatus().getKeyWords();
        String keyStr = "";
        if (keyWords != null) {
			for (Iterator iter = keyWords.iterator(); iter.hasNext();) {
				String keyword = (String) iter.next();
				keyStr += keyword;
				if (iter.hasNext())
					keyStr += ", ";
			}//for iter
		}//if
        keywordsLabel.setText(keyStr);
        locationLabel.setText(new File(ie.getStatus().getFilename()).getName());
        textAreaInformationTab.setText(ie.getSummary());
    }
    
    /**
     * Erzeugt den Register für die Informationen
     */
    private Component buildInformationTab() {
        JPanel panel = new JPanel(null);

        

        //Layout
        double border = 10, gap = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;

        double size[][] = {
                /* Zeilen */// 
                { border, P, gap * 3, F, border },
                /* Spalten */// 
                { border, P, gap, P, gap, P, gap, P, gap, P, gap, P, gap * 3, P, gap,
                        P, border } };
        panel.setLayout(new TableLayout(size));
        panel.add(fileStatusImageLabel, "1, 1, L, C");
        panel.add(statusLabel, "3, 1, F, F");
        
        
        panel.add(new JLabel("Titel:"), "1, 3, L, C");
        panel.add(titleLabel, "3, 3, F, F");

        panel.add(new JLabel("Autor:"), "1, 5, L, C");
        panel.add(authorLabel, "3, 5, F, F");

        panel.add(new JLabel("Schwierigkeit:"), "1, 7, L, C");
        panel.add(difficultyLabel, "3, 7, F, F");

        panel.add(new JLabel("Begriffe:"), "1, 9, L, C");
        panel.add(keywordsLabel, "3, 9, L, C");

        panel.add(new JLabel("Datei:"), "1, 11, L, C");
        panel.add(locationLabel, "3, 11, L, C");
        
        panel.add(new JLabel("Zusammenfassung:"), "1, 13, 3, 13");
        panel.add(scrollTextInformationTab, "1, 15, 3, 15");

        return panel;
    }//buildInformationTab

    /**
     * Erzeugt den Register für die Informationen
     */
    private Component buildServerNewsTab() {
        JPanel panel = new JPanel(null);

        //Layout
        double border = 10, gap = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;

        double size[][] = {
        /* Zeilen */// 
                { border, F, border },
                /* Spalten */// 
                { border, P, gap * 3, P, border } };
        panel.setLayout(new TableLayout(size));

        panel.add(new JLabel("Neuigkeiten:"), "1, 1, L,C");
        panel.add(scrollTextServernewsTab, "1, 3, F, F");

        return panel;
    }//buildServerNewsTag    
    
    /**
     * Aktualisiert das globale Kontextmenü.
     */
    public void refreshPopupMenuGlobal() {
        popupMenuGlobal = new ETreePopupMenuGlobal(jTree, serverDialog);
    }

    /**
     * Zeigt das Kontextmenü für eine lokale Datei an.
     * 
     * @param evt
     *            MouseEvent
     */
    public void showLocalPopupMenue(MouseEvent evt) {
        popupMenuEIssue.setLocalConfiguration();
        popupMenuEIssue.show(jTree, evt.getX(), evt.getY());
    }

    /**
     * Zeigt das Kontextmenü für eine remote Datei an.
     * 
     * @param evt
     *            MouseEvent
     */
    public void showRemotePopupMenue(MouseEvent evt) {
        popupMenuEIssue.setRemoteConfiguration();
        popupMenuEIssue.show(jTree, evt.getX(), evt.getY());
    }

    /**
     * Zeigt das Kontextmenü für eine temp Datei an.
     * 
     * @param evt
     *            MouseEvent
     */
    public void showTempPopupMenue(MouseEvent evt) {
        popupMenuEIssue.setTempConfiguration();
        popupMenuEIssue.show(jTree, evt.getX(), evt.getY());
    }

    /**
     * @return Returns the popupMenuEIssue.
     */
    public ETreePopupMenuIssue getPopupMenuEIssue() {
        return popupMenuEIssue;
    }
    
    /**
     * @return Returns the popupMenuGlobal.
     */
    public JPopupMenu getPopupMenuGlobal() {
        return popupMenuGlobal;
    }    
    
}
