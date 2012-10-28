package math4u2.view.gui;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import math4u2.application.resource.Folders;
import math4u2.exercises.EParser;
import math4u2.exercises.Math4U2Document;
import math4u2.exercises.ui.ExercisePanel;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.ParseException;
import math4u2.util.io.file.FileUtils;
import math4u2.util.io.file.GenericFileFilter;
import math4u2.util.io.file.xml.filestatus.FileStatusInterface;
import math4u2.util.io.file.xml.filestatus.Local;
import math4u2.util.io.file.xml.filestatus.Temp;
import math4u2.util.net.HTTP;

/**
 * Hilfsklasse die beim XML-parsen unterstützt
 * 
 * @author Fenn Stefan
 * @author Michael Lichtenstern
 */
public class XMLManager {
    
    private static String servernews = null;

    /**
     * Parsen einer Datei
     * 
     * @param fileName
     *            Dateinamen mit Pfad
     * @param parser
     */
    private static void parseFile(String fileName, EParser parser,
            FileStatusInterface fsi) {
        parser.analyse(fileName, fsi);
    } //importFile

    /**
     * Erzeugt einen neuen ExcercisePanel
     * 
     * @param parser
     * @return ExercisePanel
     */
    public static ExercisePanel createExercisePanel(EParser parser,
            boolean isHelpFrame) {
        ExercisePanel exercises = new ExercisePanel(Math4U2Win.getInstance(),
                isHelpFrame, parser);
        return exercises;
    } //createExercisePanel

    /**
     * Parst das aktuelle Dokument
     * 
     * @param parser
     * @return Math4U2Document
     */
    public static Math4U2Document parseDocument(EParser parser) {
        Math4U2Document doc = null;
        try {
            doc = parser.getDocument();
        } catch (ParseException e) {
            ExceptionManager.doError("Fehler beim Parsen",e);
        } //catch
        return doc;
    } //parseDocument

    /**
     * Refresh ueber alle lokalen Dateien (xml- und temp-Verzeichnis), der
     * ServerContents-Dateien, sowie anschliessendem anzeigen
     */
    public static void showRefreshFull() {
        refreshFull(true);
    }

    public static void refreshFull(boolean showFrame) {
        EParser parser = new EParser(Math4U2Win.getInstance().getBroker());
        refreshLocal(parser);
        refreshTemp(parser);
        refreshServer(parser);
        showRefresh(parser, showFrame);
    }//refreshFull

    /**
     * Refresh des Server XML-Ordner
     */
    public static void serverRefresh(String url) {
        String content = HTTP.getHTTPContent(url);
        XMLManager.setServernews(HTTP.getHTTPContent(HTTP.parseURL(url)+"/getservernews.php"));
        if (!content.equalsIgnoreCase("")) {
            FileUtils.writeTempFile(new File(Folders.TEMP_FOLDER_SC
                    + HTTP.parseNameFromURL(url) + "_sc.xml"), content);
        }
        showRefreshFull();
    }

    /**
     * Refresh der lokalen Dateien im xml-Verzeichnis.
     * 
     * @param parser
     *            Eparser
     */
    private static void refreshLocal(EParser parser) {
        analyseFolderRecursive(Folders.XML_FOLDER, parser);
    }

    /**
     * Refresh der ServerContents-Dateien.
     * 
     * @param parser
     */
    private static void refreshServer(EParser parser) {
        List server = new LinkedList();
        //Trägt alle Dateien in eine Liste ein
        server = _analyseFolder(Folders.TEMP_FOLDER_SC, server);
        parser.analyseContents(server);
    }

    /**
     * Refresh der temporaeren Dateien im temp-Verzeichnis.
     * 
     * @param parser
     */
    private static void refreshTemp(EParser parser) {
        List temp = new LinkedList();
        temp = _analyseFolder(Folders.TEMP_FOLDER_XML, temp);
        analyseTemp(parser, temp);
    }

    /**
     * Analysiert das temp-Verzeichnis.
     * 
     * @param parser
     *            Eparser
     * @param files
     *            Dateiliste
     */
    private static void analyseTemp(EParser parser, List files) {
        Iterator iterator = files.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next().toString();
            parseFile(name, parser, new Temp());
        }
    }

    /**
     * Erneuert die Ansicht.
     * 
     * @param parser
     *            Eparser
     */
    private static void showRefresh(EParser parser, boolean showFrame) {
        ExercisePanel exercises = createExercisePanel(parser, false);
        Math4U2Win.getInstance().layoutAfterRefresh(exercises, parser,
                showFrame);
    }

    /**
     * Analysieren eines bestimmten Ordners mit Unterordnern
     * 
     * @param rootStr
     *            Ordnername mit Pfad
     * @param parser
     */
    public static void analyseFolderRecursive(String rootStr, EParser parser) {
        File root = new File(rootStr);
        if (!root.exists())
            return;
        List list = new LinkedList();
        //Trägt alle Dateien in eine Liste ein
        list = _analyseFolder(root, list);
        //Liste traversieren
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next().toString();
            parseFile(name, parser, new Local());
        } //while
    } //analyseFolder

    /**
     * Analysieren eines bestimmten Ordners ohne Unterordner
     * 
     * @param parentFile
     *            Fileobjekt eines Ordners
     * @param list
     *            Liste der schon gefundenen XML-Dateien
     * @return
     */
    private static List _analyseFolder(File parentFile, List list) {
        File[] subDir = parentFile.listFiles(new GenericFileFilter("*.xml"));
        for (int i = 0; i < subDir.length; i++) {
            File file = subDir[i];
            if (file.isDirectory()) {
                _analyseFolder(file, list);
            } else
                list.add(file);
        } //for
        return list;
    } //_analyseFolder

    /**
     * Loescht alle Inhalte des temp-Verzeichnis.
     */
    public static void deleteTempContents() {
        try{
            deleteFolderContent(Folders.TEMP_FOLDER_SC);
            deleteFolderContent(Folders.TEMP_FOLDER_XML);            
        }catch(Throwable t){};
    }

    /**
     * Loescht den Inhalt eines Verzeichnis.
     * 
     * @param folder
     *            File-Objekt des Verzeichnis
     */
    private static void deleteFolderContent(File folder) {
        if (!folder.exists())
            return;
        List l = new LinkedList();
        l = _analyseFolder(folder, l);
        Iterator i = l.iterator();
        while (i.hasNext()) {
            ((File) i.next()).delete();
        }
    }
    /**
     * @return Returns the servernews.
     */
    public static String getServernews() {
        return servernews;
    }
    /**
     * @param servernews The servernews to set.
     */
    public static void setServernews(String servernews) {
        XMLManager.servernews = servernews;
    }
}