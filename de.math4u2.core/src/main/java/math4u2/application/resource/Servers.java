package math4u2.application.resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import math4u2.util.exception.ExceptionManager;
import math4u2.util.io.file.FileUtils;

/**
 * ToolKlasse fuer die Konfiguratins-Datei der Remote-Server. <br>
 * Laedt und Speichert die Server-Properties.
 * 
 * @author Michael Lichtenstern
 */
public class Servers {

	/**
	 * Realtive Pfad zur Server-Konfigurations-Datei server.properties.
	 */
	private String pathname = Folders.MATH4U2_FOLDER
			+ "math4u2/conf/servers.properties";

	/**
	 * Initalisiert die Server-Properties und gibt diese als Properties Objekt
	 * zurueck.
	 * 
	 * @return Server-Properties
	 */
	public Properties initServerProps() {
		FileInputStream fis=null;
		Properties serverProps = new Properties();
		try {
			fis = new FileInputStream(pathname);
			serverProps.load(fis);
		} catch (FileNotFoundException e) {
			this.createServerPropertiesFile(pathname);
		} catch (IOException e) {
			ExceptionManager.doError("Fehler beim lesen der Server-Einstellungen aus der Datei "+pathname+".", e);
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					ExceptionManager.doError(e);
				}//catch
			}//if
		}//finally
		return serverProps;
	}

	/**
	 * Speichert die Server-Properties.
	 * 
	 * @param serverProps
	 *            Properties Objekt
	 */
	public void storeServerProps(Properties serverProps) {
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(pathname);
			serverProps.store(fos, "");
		} catch (FileNotFoundException e) {
			this.createServerPropertiesFile(pathname);
		} catch (IOException e) {
			ExceptionManager
					.doError("Servers.storeServerProps IOException!", e);
		} finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					ExceptionManager.doError(e);
				}//catch
			}//if
		}//finally
	}//storeServerProps

	/**
	 * Legt die Datei server.properties an. Falls dies nicht moeglich ist, wird
	 * ein Fehler an den ExceptionManager weitergereicht.
	 * 
	 * @param pathname
	 *            Pfad der server.properties Datei
	 */
	private void createServerPropertiesFile(String pathname) {
		if (!FileUtils.createFile(pathname)) {
			ExceptionManager
					.doError("Die Datei servers.properties konnte nicht gefunden und nicht angelegt werden!");
		}
	}
}