// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2;

import java.awt.BorderLayout;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.UIManager;

import math4u2.application.resource.Images;
import math4u2.application.resource.Settings;
import math4u2.application.resource.Versions;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.string.VersionChecker;
import math4u2.util.string.VersionChecker.VersionException;
import math4u2.view.gui.ExceptionFrame;
import math4u2.view.gui.Math4U2Win;

/**
 * Klasse für die Hauptanwendung.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public class Math4U2 {

    private static JWindow preloadFrame;

    public static void main(String[] args) {
    	//setSystemOutErr();
    	ExceptionManager.initExceptionFrame(ExceptionFrame.getInstance());
    	
        try {
            try {
                VersionChecker.checkJavaVersion(Versions.JAVA_VERSION);
            } catch (VersionException e) {
                String message = "Sie benutzen die Java-Version '"
                        + e.getVerstionReal()
                        + "'.\nSie brauchen jedoch mindestens die Java-Version '"
                        + e.getVersionMustBe()
                        + "'.\nMit dem Befehl: 'java -version' können Sie ihre Java-Version abfragen";
                ExceptionManager.doError(message, "Versionsfehler");
                return;
            } //catch

            try {
            	 UIManager.setLookAndFeel(
            	            UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
            	e.printStackTrace();
            }

            showInitialWindow();

            Math4U2Win w = Math4U2Win.getInstance();
            preloadFrame.dispose();
            w.setVisible(true);
        } catch (Throwable t) {
            ExceptionManager.doError("Unerwarteter Fehler",t);
        }//catch
    } //main

    private static void setSystemOutErr() {
    	FileOutputStream os;
		try {
			os = new FileOutputStream("output.log");
			PrintStream ps = new PrintStream(os);
	    	System.setOut(ps);
	    	System.setErr(ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
	}

	public static void showInitialWindow() {
        preloadFrame = new JWindow();
        JLabel label = new JLabel(Images.PRELOADER);
        preloadFrame.getContentPane().add(label, BorderLayout.CENTER);

        preloadFrame.setBounds(Settings.computeBounds(null,Images.PRELOADER
                .getIconWidth(), Images.PRELOADER.getIconHeight()));

        // Show the window
        preloadFrame.setVisible(true);
    } //showIntitialWindow

} //class Math4u2
