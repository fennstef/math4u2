package math4u2.util.io.file;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.formula.FormulaRenderContext;

public class FileResolver {
    public static File resolve(String name, Class clazz) {
    	URL url = clazz.getClassLoader().getResource(name);
    	if(url!=null){
    		File file;
			try {
				file = new File(url.toURI());
				if(file.exists()) return file;
			} catch (Throwable e) {				
			}
    		
    	}
		try {			
			URL urli = clazz.getProtectionDomain().getCodeSource().getLocation();
			URI uri = urli.toURI();
			String uriRaw = uri.toString();
			String partWithoutJar = uriRaw.replaceAll("!.*", "").replaceAll("^jar:","");
			URI uri2 = new URI(partWithoutJar);
//			System.out.println("path: "+partWithoutJar);			
//			System.out.println(new File(uri2).getAbsolutePath());
//			System.out.println(new File(uri2).exists());
//			System.out.println(new File(new File(uri2).getParent(),name).getAbsolutePath());
			File file = new File(new File(uri2).getParent(),name);
			if(file.exists()) return file;
			throw new RuntimeException();
		} catch (Throwable t) {
			ExceptionManager.doError("Die Datei '" + name
					+ "' wurde nicht gefunden",t);
			throw new RuntimeException();
		}//catch
	}
    
    public static String resolveToUri(String name, Class clazz){
    	File file = resolve(name, clazz);
    	return file.toURI().getPath();
    }
}
