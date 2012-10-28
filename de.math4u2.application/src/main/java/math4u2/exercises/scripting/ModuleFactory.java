// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.scripting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


import math4u2.util.exception.ExceptionManager;
import math4u2.util.io.file.FileResolver;

/**
 * Klasse die zum Erzeugen von Plug-In-Objekten dient.
 * 
 * @version 0.1
 * @author Erich Seifert, Fenn Stefan
 */
public class ModuleFactory {
    private final Properties modsAvailable;

    private transient final Map classCache = new HashMap();

    public ModuleFactory() {
        FileInputStream fis = null;
        modsAvailable = new Properties();

        try {
            fis = new FileInputStream(getFile("math4u2/conf/plugins.properties"));
            modsAvailable.load(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            ExceptionManager.doError("Die Datei math4u2/conf/plugins.properties wurde nicht gefunden.",e);
        } catch (IOException e) {
            ExceptionManager.doError("Fehler beim Leser der Datei math4u2/conf/plugins.properties.", e);
        } 
        finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    ExceptionManager.doError(e);
                }
            }
        }
    }
    
    private static File getFile(String filePath){
    	return FileResolver.resolve(filePath, ModuleFactory.class);
    }

    public Object getModule(String name) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Class modKlasse = null;

        String klassenName = (String) modsAvailable.get(name);

        if (classCache.containsKey(name)) {
            modKlasse = (Class) classCache.get(name);
        } else {
            modKlasse = Class.forName(klassenName);
        }//else

        return (Object) modKlasse.newInstance();
    }//getModule
}//class ModuleFactory
