package math4u2.view.gui.save;

import java.awt.Color;
import java.awt.Component;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.affine.AffPoint;
import math4u2.mathematics.affine.AreaInterface;
import math4u2.mathematics.affine.Map;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.StandardFunction;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotImplementedException;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.drawarea.DrawAreasManager;
import math4u2.view.gui.Math4U2Win;
import math4u2.view.gui.XMLManager;
import math4u2.view.gui.listview.ListBox;
import math4u2.view.gui.listview.complete.CompleteViewBox;
import math4u2.view.layout.PercentLayout;

/**
 * Dient zum Speichern eines Zustandes aller Zeichenflächen. Diese werden wie
 * alle anderen Lektionen als XML-Datei gespeichert.
 * 
 * @author Fenn Stefan
 */
public class XMLStoreManager {
	
	/**
	 * Anzahl der maximalen Zeilen für den Output.
	 * Dient als Umbruchgröße
	 */
	public static int MAX_TEXT_WIDTH=80;

    /**
     * Speichert einen Zustand aller Zeichenflächen ab
     * 
     * @param file
     *            XML-Datei, in der der Zustand gespeichert werden soll
     * @param pfad
     *            Pfad zur XML-Datei
     * @param title
     *            Titel der Lektion
     * @param author
     *            Autor der Datei
     * @param text
     *            Beschreibungstext
     */
    public void saveAll(File file, String location, String title,
            String author, String text) throws Exception{
        FileInputStream fis=null;
        DataInputStream in = null;
        String template = null;
        String rawFileName = "math4u2/conf/lectureTemplate.xml";
        URL filenameUrl = XMLStoreManager.class.getClassLoader().getResource(rawFileName);
        File fileObj = new File(filenameUrl.toURI());
        try {
            fis = new FileInputStream(fileObj);
            in = new DataInputStream(fis);
            byte[] b = new byte[in.available()];
            in.readFully(b);
            template = new String(b, 0, b.length);
        } catch (FileNotFoundException e) {
            ExceptionManager.doError("Die Datei "+rawFileName+" wurde nich gefunden.",e);
        } catch (IOException e) {
            ExceptionManager.doError("Fehler beim Lesen der Datei "+rawFileName,e);
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e1) {
                    ExceptionManager.doError(e1);
                }//catch
            }//if
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e1) {
                    ExceptionManager.doError(e1);
                }//catch
            }//if           
        }//finally

        String layout = generateLayoutText();
        String script = generateScriptText();

        template = template.replaceAll("_author_", author);
        template = template.replaceAll("_title_", title);
        template = template.replaceAll("_location_", location);
        template = template.replaceAll("_summary_", "");
        template = template.replaceAll("_description_", text);
        template = template.replaceAll("_layout_", layout);
        template = template.replaceAll("_script_", script);

        //Schreibe in die Datei
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(template.getBytes());
        } catch (FileNotFoundException e) {
            ExceptionManager.doError("Fehler beim Schreiben der Datei "+file.getAbsolutePath()+".\nDie Datei wurde nicht gefunden.",e);
        } catch (IOException e) {
        	ExceptionManager.doError("Fehler beim Schreiben der Datei "+file.getAbsolutePath()+".",e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    ExceptionManager.doError(e);
                }
            }//if
        }//finally
        XMLManager.refreshFull(false);
    }//saveAll

    /**
     * Erzeugt alles was im Tag <layout> steht.
     */
    private String generateLayoutText() {
        String result = "";

        //Es wird nicht DrawAreasManager.getAllDrawAreas() verwendet, 
        //da dort nicht die Reihenfolge eingehalten wird !!!
        List das = new LinkedList();
        JPanel container = Math4U2Win.getInstance().getDrawAreaContainer();
        Component[] comps = container.getComponents();
        for (int i = 0; i < comps.length; i++) {
            DrawAreaInterface da = (DrawAreaInterface) comps[i];
            das.add(da);
        } //for i        
                
        PercentLayout pl = (PercentLayout) Math4U2Win.getInstance()
                .getDrawAreaContainer().getLayout();
        double[][] layoutX = pl.getLayoutSchemeX();
        double[] layoutY = pl.getLayoutSchemeY();

        Iterator iter = das.iterator();
        for (int y = 0; y < layoutY.length; y++) {
            result += "<row perc=\"" + layoutY[y] * 100 + "\">\n";
            for (int x = 0; x < layoutX[y].length; x++) {
                result += "\t<col perc=\"" + layoutX[y][x] * 100 + "\" name=\""
                        + ((DrawAreaInterface) iter.next()).getName()
                        + "\"/>\n";
            }//for
            result += "</row>\n";
        }//for y
        return result;
    }//generateLayoutText

    /**
     * Erzeugt alles was im Tag <script>steht
     * @throws Exception 
     */
    private String generateScriptText() throws Exception {
        StringBuffer result = new StringBuffer();
        result.append("deleteAll();\n\n");

        //Listbox und Broker holen
        Broker broker = Math4U2Win.getInstance().getBroker();
        ListBox listBox = null;
        CompleteViewBox completeView = null;
        try {
			listBox = (ListBox) broker.getObject("ListBox");
		} catch (BrokerException e) {
			ExceptionManager
					.doError("Fehler beim Holen des Objekts ListBox", e);
		}//catch

		try {
			completeView = (CompleteViewBox) broker.getObject("CompleteView");
		} catch (BrokerException e) {
			ExceptionManager.doError(
					"Fehler beim Holen des Objekts CompleteView", e);
		}//catch

		result.append("expand(left=" + listBox.getExpandState() + ";right="
				+ completeView.getExpandState() + ");\n");

        //DrawArea´s in das Koordinatensystem richtig setzen
        List dais = DrawAreasManager.getAllDrawAreas();
        for (Iterator iter = dais.iterator(); iter.hasNext();) {
            DrawAreaInterface dai = (DrawAreaInterface) iter.next();
            result.append(dai.getName()+".coordinateSystem(");
            result.append("xMin=" + dai.getXMin() + ";");
            result.append("xMax=" + dai.getXMax() + ";");
            result.append("yMin=" + dai.getYMin() + ";");
            result.append("yMax=" + dai.getYMax() + ";");
            result.append(");\n");
            if (dai.getTitle() != null && dai.getTitle().length() != 0) {
                result.append(dai.getName() + ".setTitle(title=\"" + dai.getTitle()
                        + "\");\n");
            }//if
        }//for iter
        result.append("\n");

        // Alle Elemente zusammensuchen
        List elementList = new LinkedList();
        elementList.addAll(dais);
        Set elementNames = listBox.getRelationContainer()
                .getAllNamedRelationNames();
        for (Iterator iter = elementNames.iterator(); iter.hasNext();) {
            String eName = (String) iter.next();
            try {
                MathObject view = listBox.getRelationContainer()
                        .getObjectByName(eName);
                Iterator iter2 = view.getRelationContainer()
                        .getSpecificRelationsIterator(
                                RelationFactory.getFunction_ListView_Relation()
                                        .getName());
                RelationInterface ri = (RelationInterface) iter2.next();
                elementList.add(ri.getPartner(view));
            } catch (ObjectNotInRelationException e) {
                ExceptionManager.doError("Fehler beim Suchen der Verbindungen ("+eName+")",e);
            }//catch
        }//for iter
        
        //Sorierung nach Klasse und Keynamen 
        Collections.sort(elementList,new Comparator(){
			public int compare(Object o1, Object o2) {
				MathObject m1 = (MathObject) o1;
				MathObject m2 = (MathObject) o2;
				String m1S = m1.getClass().toString()+m1.getIdentifier();
				String m2S = m2.getClass().toString()+m2.getIdentifier();
				return (m1S.compareTo(m2S));
			}
        });

        // Erzeugungsreihenfolge festlegen
        List creationList = new LinkedList();
        int proveSize = -1;
        while (creationList.size() != elementList.size()) {
            if(proveSize==creationList.size()){
            	//kein Element konnte in die Liste hinzugefügt werden
            	//d.h. es würde eine unendliche Schleife geben
            	throw new Exception("Es konnte keine gültige Erzeugungsreihenfolge gefunden werden.");
            }//if
            proveSize = creationList.size();
            for (Iterator iter = elementList.iterator(); iter.hasNext();) {
                MathObject mo = (MathObject) iter.next();
                Set deps = new HashSet();
                createCreationList(mo, creationList, deps);
                if (deps.size() == 0 && !creationList.contains(mo)) {
                    creationList.add(mo);
                }//if
            }//for iter            
        }//while

        //Objektdefinitionen schreiben
        Object lastObject = creationList.get(0);
        for (Iterator iter = creationList.iterator(); iter.hasNext();) {
            MathObject mo = (MathObject) iter.next();
            
            //DrawAreas nicht definieren
            if(mo instanceof DrawAreaInterface)
                continue;
            
            List params = new LinkedList();
            params.add("def=" + mo.toString().replaceAll("<", "&lt;"));
            
            MathObject encapsulatedObject = mo;
        	if (mo instanceof UserFunction && ((UserFunction) mo).isEncapsulated()) {
				try {
					Object o = ((UserFunction) mo).eval();
					if (o instanceof MathObject) {
						encapsulatedObject = (MathObject) o;
					}
				} catch (MathException e) {
					ExceptionManager.doError("Fehler beim Entkapseln des Objekts "+mo.getIdentifier(),e);
				}//catch
			}//if
            
            if (encapsulatedObject instanceof HasGraph) {
                HasGraph hg = (HasGraph) encapsulatedObject;
                
            	//Style
            	if(hg instanceof AffPoint){
            		AffPoint ap = (AffPoint) hg;
            		params.add("style="+ap.getStyle());
            	}//if

                //Color
                if (hg instanceof AreaInterface) {
                    AreaInterface ai = (AreaInterface) hg;
                    Color fillColor = ai.getFillColor();
                    Color borderColor = ai.getBorderColor();
                    params.add("fillcolor=" + convertColor(fillColor));
                    params.add("bordercolor=" + convertColor(borderColor));
                } else {
                    Color c = hg.getColor();
                    if (c != Color.BLACK) {
                        params.add("color=" + convertColor(c));
                    }//if
                }//else

                //Linestyle
                int lineStyle = hg.getLineStyle();
                if (lineStyle != HasGraph.SOLID_STROKE) {
                	if(hg instanceof Map){
                		params.add("linestyle=" + lineStyle);
                	}else {
                		params.add("linestyle=" + HasGraph.LINE_STYLES[lineStyle]);
                	}
                }//if

                //Graphs
                List targets = new LinkedList();
                boolean allVisible = true;
                boolean allInvisible = true;
                dais = DrawAreasManager.getAllDrawAreas();
                for (Iterator iter2 = dais.iterator(); iter2.hasNext();) {
                    DrawAreaInterface dai = (DrawAreaInterface) iter2.next();
                    GraphInterface gi = (GraphInterface) dai.getGraph((String) mo.getIdentifier());
                    if (gi != null) {
                        targets.add(dai);
                        if (gi.isVisible()) {
                            allInvisible = false;
                        } else {
                            allVisible = false;
                        }//else
                    }//if
                }//for iter2

                //Sind Graphen vorhanden?
                if (targets.size() != 0) {
                    String das = "target=";
                    for (Iterator iter3 = targets.iterator(); iter3.hasNext();) {
                        DrawAreaInterface dai = (DrawAreaInterface) iter3
                                .next();
                        das += dai.getName() + ((iter3.hasNext()) ? "," : "");
                    }//for iter
                    params.add(das);
                    if (allInvisible) {
                        params.add("visible=false");
                    } else if (!allVisible) {
                        //Graphen einzeln sichtbar schalten
                        throw new NotImplementedException();
                    }//else
                }//if

                //Ist das Objekt in der CompleteView?
                String cName = "CompleteView.$" + mo.getIdentifier();
                try {                	
                    Object obj = broker.getObject(cName);
                    if (obj != null) {
                        params.add("select=true");
                    }//if
                } catch (BrokerException e) {
                    ExceptionManager.doError("Fehler beim Holen des Objekts "+cName,e);
                }//catch
            }//if
            
            //Berechnung, wann ein Newline kommt            
            UserFunction f = (UserFunction) mo;
            Object curObject =f;
            if(f.isEncapsulated()){
	            try {
					curObject = f.eval();
				} catch (MathException e1) {}
            }//if
	        
            if (!curObject.getClass().equals(lastObject.getClass())){            
	            result.append("\n");
	        }else if(curObject instanceof UserFunction && lastObject instanceof UserFunction){
	        	curObject = f;
	        	if(f.getArity()!=((UserFunction)lastObject).getArity())
	        		result.append("\n");
	        }
            lastObject=curObject;
            

            result.append("newObject(");            
            
            int chars=0;
            for (Iterator iter2 = params.iterator(); iter2.hasNext();) {
                String s = (String) iter2.next();
                chars += s.length();
            }//for iter2
            
            int i = 0;
            for (Iterator iter2 = params.iterator(); iter2.hasNext(); i++) {
                if (chars>MAX_TEXT_WIDTH) {
                    if (i == 0)
                        result.append("\n");
                    result.append("\t");
                }
                String s = (String) iter2.next();
                result.append(s + ";");
                if(i!=params.size()-1) result.append(" ");
                
                if (chars>MAX_TEXT_WIDTH) {
                    result.append("\n");
                }
            }//for iter2            
            result.append(");\n");
            
        }//for

        return result.toString();
    }//generateScriptText

    /**
     * Erzeugt eine Liste der zu erzeugenden Objekte mit einer gültigen
     * Reihenfolge.
     * 
     * @param creationList
     *            Erzeugungsliste in einer gültigen Reihenfolge
     * @param deps
     *            Abhängigkeiten, die nicht in der creationList stehen.
     */
    private void createCreationList(MathObject mo, List creationList, Set deps) {
        // Abhängigkeiten hinzufügen
        Iterator iter = mo.getRelationContainer().getSpecificRelationsIterator(
                RelationFactory.getFunction_SubFunction_Relation().getName());
        while (iter.hasNext()) {
            RelationInterface ri = (RelationInterface) iter.next();
            try {
                MathObject partner = ri.getPartner(mo);
                              
                if (creationList.contains(partner)
                        || partner instanceof StandardFunction || "mouse".equals(partner.getIdentifier())) {
                    continue;
                } else if (ri.getPositionFromObject(mo) == RelationInterface.FIRST) {
                    deps.add(partner);
                }//else if
            } catch (ObjectNotInRelationException e) {
                ExceptionManager.doError("Fehler beim Erstellen der Erzeugungsliste aller Objekte",e);
            }//catch
        }//while

        // Abhängigkeiten hinzufügen
        iter = mo.getRelationContainer().getSpecificRelationsIterator(
                RelationFactory.getPart_Of_Relation().getName());
        while (iter.hasNext()) {
            RelationInterface ri = (RelationInterface) iter.next();
            try {
                if (ri.getPositionFromObject(mo) == RelationInterface.FIRST)
                    createCreationList(ri.getPartner(mo), creationList, deps);
            } catch (ObjectNotInRelationException e) {
                ExceptionManager.doError("Fehler beim Erstellen der Erzeugungsliste aller Objekte",e);
            }//catch
        }//while
        
        //z.B. bei Folge kann eine eigene Abhängigkeit auftauchen
        //Diese Abhängigkeit wird wieder gelöscht
        deps.remove(mo);
    }//createCreationList

    /**
     * Erzeugt einen String mit Farbdefinition
     */
    private String convertColor(Color c) {
        String rgb = c.getRed() + "." + c.getGreen() + "." + c.getBlue();
        if (c.getAlpha() != 255)
            rgb += "." + c.getAlpha();
        return rgb;
    }//convertColor

}//class XMLStoreManger
