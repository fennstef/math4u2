package math4u2.view.gui.save;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import math4u2.application.resource.Images;
import math4u2.application.resource.Settings;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.io.file.FileUtils;
import math4u2.util.io.file.GenericFileFilter;
import math4u2.view.gui.Math4U2Win;
import math4u2.view.layout.TableLayout;

public class SaveDialog extends JFrame {

    private static final String TITLE = "Speichern";

    /** Zieldatei wo abgespeichert werden soll */
    private File file = null;

    /** Titel des Dokuments */
    private JTextField title;

    /** Autor des Dokuments */
    private JTextField author;

    /** Pfad zum Dokument */
    private JTextField pfad;

    /** Beschreibung */
    private JTextArea text;

    /** Controll Speichern */
    private JButton save = new JButton("Speichern");

    /** Controll Abbrechen */
    private JButton cancel = new JButton("Abbrechen");

    private File pathFile = FileUtils.urlToFile(ClassLoader.getSystemResource(
            "math4u2/xml/"));

    private JFileChooser fc = new JFileChooser(pathFile);

    
    public SaveDialog() {
        super(TITLE);
        init();
    }//Konstruktor

    private void init() {
        setIconImage(Images.LOGO_ICON.getImage());

        fc.setApproveButtonText("Speichern");
        fc.setDialogTitle(TITLE);

        JPanel main = new JPanel();

        //Titel
        title = new JTextField("Math4u2-Dokument", 40);
        title.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel titleP = new JPanel();
        buildTitle("Titel des Dokuments", titleP);

        //Author
        author = new JTextField("unbekannt", 40);
        author.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel authorP = new JPanel();
        buildTitle("Autor des Dokuments", authorP);

        //Pfad
        pfad = new JTextField("/Ablage/", 40);
        pfad.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel pathP = new JPanel();
        buildTitle("Pfad (z.B. /Geometrie/Dreieck/)", pathP);

        //Beschreibung
        text = new JTextArea(("Dokument vom " + java.text.DateFormat
                .getDateInstance().format(new Date())), 10, 40);
        JScrollPane scrollDescription = new JScrollPane(text);
        scrollDescription
                .setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel descriptionP = new JPanel();
        buildTitle("Beschreibung", descriptionP);

        //Kontrollfelder
        JPanel controllP = new JPanel();
        controllP.add(save);
        controllP.add(cancel);
        final JFrame frame = this;
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                frame.dispose();
            }//actionPerformed
        });
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                openDialog();
            }//actionPerformed
        });

        //Layout
        double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
        double t = 15, eborder = 10;
        double size[][] = {
                /* Zeilen */
                { eborder, F, eborder },
                /* Spalten */
                { eborder, t, border, P, eborder, t, border, P, eborder, t,
                        border, P, eborder, t, border, F, eborder, P, eborder } };
        main.setLayout(new TableLayout(size));

        main.add(titleP, "1, 1, F,F");
        main.add(title, "1, 3, F,F");
        main.add(authorP, "1, 5, F,F");
        main.add(author, "1, 7, F,F");
        main.add(pathP, "1, 9, F,F");
        main.add(pfad, "1, 11, F,F");
        main.add(descriptionP, "1, 13, F,F");
        main.add(scrollDescription, "1, 15, F,F");
        main.add(controllP, "1, 17, F,F");

        getContentPane().add(main);
    }//init
    
    private boolean firstTime = true;
    
	public void setVisible(boolean b){
		if(firstTime && b){
			setBounds(Settings.computeBounds(Math4U2Win.getInstance(),0.4, 0.6));
			firstTime=false;
		}//if
		super.setVisible(b);
	}//setVisible
	
	
    /**
     * Erzeugt einen Titel
     * 
     * @param title
     *            Titel-Nachricht
     * @param panel
     *            wo soll der Titel eingebaut werden
     */
    private void buildTitle(String title, JPanel panel) {
        panel
                .setBorder(BorderFactory.createTitledBorder(BorderFactory
                        .createMatteBorder(1, 0, 0, 0, Color.BLACK), " "
                        + title + " "));
    } //buildTitle

    private void openDialog() {
        fc.setFileFilter(new GenericFileFilter("*.xml", "XML-Dateien", true));
        int fd = fc.showOpenDialog(this);
        if (fd == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();

            String fileStr = file.toString();
            if(!fileStr.endsWith(".xml")) fileStr += ".xml";
            
            int pos = fileStr.lastIndexOf('.');
            pos = (pos == -1) ? fileStr.length() : pos;
            //File ohne Extension
            fileStr = fileStr.substring(0, pos);
            //File mit Extension
            fileStr += ".xml";
            file = new File(fileStr);

            //Falls die Datei nich im Hauptverzeichnis liegt
            if (!isSameDirectory(file.getParent(), pathFile)) {
                Object[] options = { "OK" };
                int i = JOptionPane
                        .showOptionDialog(
                                null,
                                "Die Datei kann nur im XML-Hauptverzeichnis abgespeichert werden !",
                                "Ungültiger Speicherort", JOptionPane.DEFAULT_OPTION,
                                JOptionPane.WARNING_MESSAGE, null, options,
                                options[0]);
                if (i != 1) {
                    fc.setCurrentDirectory(pathFile);
                    return;
                }//if
            }// if file exists            
            
            //Falls die Datei schon existiert
            if (file.exists()) {
                Object[] options = { "OK", "Abbrechen" };
                int i = JOptionPane.showOptionDialog(null,
                        "Soll die Datei überschrieben werden?",
                        "Die Datei existiert bereits",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (i != 0) {
                    return;
                }
            }// if file exists

            XMLStoreManager store = new XMLStoreManager();
            try {
				store.saveAll(file, pfad.getText(), title.getText(), author
				        .getText(), text.getText());
			} catch (Exception e) {
				ExceptionManager.doError("Speicherfehler",e);
			}
            dispose();
        }//if OK
    }//openDialog

    private boolean isSameDirectory(String s, File g) {
        return s.equals(g.toString());
    }//samedirectory

}//SaveDialog
