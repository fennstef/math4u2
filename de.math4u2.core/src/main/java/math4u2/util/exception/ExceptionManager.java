package math4u2.util.exception;

import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.text.JTextComponent;

import math4u2.util.text.CharUtil;
import math4u2.util.xml.DOMWriter;

import org.w3c.dom.Node;

/**
 * @author Fenn Stefan
 * 
 * Manager der alle Exceptions aufnimmt. Er garantiert, dass die Ausnahme
 * angezeigt wird.
 */
public class ExceptionManager {
	public static IExceptionFrame exceptionFrame;
	
	public static void initExceptionFrame(IExceptionFrame frame){
		exceptionFrame = frame;
	}

    /**
     * Gibt eine Ausnahme aus. Sucht nach einen Ausgangskanal z.B.
     * Fehlertextfeld oder ein Messagefenster.
     */
    public static void doError(String errorMessage, String errorTitle,
            boolean withConsoleOutput) {
        String keyword = "Exception:";
        int pos = -1;
        if (errorMessage != null)
            errorMessage.lastIndexOf(keyword);
        if (pos != -1)
            errorMessage = errorMessage.substring(pos + keyword.length());

        if (withConsoleOutput)
            System.err.println(errorTitle + ": " + errorMessage);
        Toolkit.getDefaultToolkit().beep();
        exceptionFrame.newException(errorTitle, errorMessage);
    } //doError

    public static void doError(String errorMessage, String errorTitle) {
        doError(errorMessage, errorTitle, true);
    } //doError

    public static void doError(String errorMessage) {
        doError(errorMessage, "Fehler", true);
    } //doError

    public static void doError(Throwable e) {
        e.printStackTrace();
        String message = getMessage(e);
        e.printStackTrace();
        doError(message, "Fehler", false);
    } //doError

    public static void doError(String message, Throwable e) {
        e.printStackTrace();
        doError(e.getMessage(), message, false);
    } //doError

    public static void doError(math4u2.util.exception.parser.ParseException e, Node node){
        e.printStackTrace();
        String message = getMessage(e)+"\n"+DOMWriter.nodeToString(node);
        doError(message, "Fehler", false);    	
    }//doError
    
    public static void doError(String message, math4u2.util.exception.parser.ParseException e, Node node) {
        e.printStackTrace();
        doError(e.getMessage()+"\n"+DOMWriter.nodeToString(node), message, false);
    } //doError
    
    
    public static void doError(InvocationTargetException e) {
        e.printStackTrace();
        doError(e.getCause().getMessage(), "Aufruffehler", false);
    }

    public static void doError(math4u2.util.exception.parser.ParseException e,
            JTextComponent inputField, String definition) {
        e.printStackTrace();
        inputField.setSelectionStart(e.getErrorColumn() - 1);
        inputField.setSelectionEnd(inputField.getText().length());
        doError(e.getMessage(), "Fehler in der Definition '" + definition
                + "' spalte:" + e.getErrorColumn() + " ", false);
    } //doError
    
    
    private static String getMessage(Throwable e) {
        String message = e.getMessage();
        if(message==null) message = "";
        if(message.indexOf("Exception")!=-1) message="";
        message = message.trim();
        
        if(e.getCause()==null)
            return message;
        else if(message.equals("")){            
            return message+getMessage(e.getCause());
        }else{
            return message+"\n\n...\n\n"+getMessage(e.getCause());
        }
    }//getMessage

    /**
     * Erzeugt eine Fehler-Ausgabe, die beim parsen eines <script>-Tags
     * entstanden ist.
     * 
     * Dies beinhaltet auch eine Fehleranzeige in ASCII-Format
     */
    public static void doError(math4u2.util.exception.parser.script.ParseException e, String parentXml,
            String fileName, int blockNr) {
        //Zeile berechnen, wo der Fehler entstanden ist
        int line = e.currentToken.next.beginLine;
        String[] sa = parentXml.split("\n");
        LinkedList sl = new LinkedList(Arrays.asList(sa));
        // generiert den ASCII-Pfeil, der auf die Fehlerstelle zeigt.
        sl.add(line, CharUtil.generateFillString(" ",
                e.currentToken.next.beginColumn - 1)
                + "^");        
        //Alles wieder zusammensetzten
        StringBuffer sb = new StringBuffer();
        String title = "Fehler in der Datei " + fileName + " im " + blockNr
                + ". <script>-Tag. \n";
        sb.append(title);
        for (Iterator iter = sl.iterator(); iter.hasNext();) {
            String s = (String) iter.next();
            sb.append(s);
            sb.append("\n");
        } //for
        System.err.println(sb.toString());
        e.printStackTrace();
        doError(title + e.getMessage()+"\n"+sb.toString(), "Fehler im <script>-Tag", false);
    } //doError 5
} //class Exception
