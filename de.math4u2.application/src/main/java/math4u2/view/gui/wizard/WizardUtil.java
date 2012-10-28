package math4u2.view.gui.wizard;

import java.awt.Container;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.TextAction;

import math4u2.application.resource.Images;
import math4u2.application.resource.Settings;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.swing.LabelAsButtonMouseListener;
import math4u2.view.gui.wizard.components.CanModifyText;
import math4u2.view.gui.wizard.step.ChooseObjectWizardStep;
import math4u2.view.gui.wizard.step.DefinitionWizardStep;
import math4u2.view.gui.wizard.step.FunctionWizardStep;
import math4u2.view.gui.wizard.step.ListInstantiateChoiceStep;
import math4u2.view.gui.wizard.step.ListWizardStep;
import math4u2.view.gui.wizard.step.MapWizardStep;
import math4u2.view.gui.wizard.step.MatrixInstantiateChoiceStep;
import math4u2.view.gui.wizard.step.MatrixWizardStep;
import math4u2.view.gui.wizard.step.SelectObjectWizardStep;
import math4u2.view.gui.wizard.step.StandardWizardStep;
import math4u2.view.gui.wizard.title.HasTitle;
import math4u2.view.gui.wizard.title.HasTitleWrapper;
import math4u2.view.gui.wizard.types.WizParamType;
import math4u2.view.layout.TableLayout;

import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardEvent;
import org.pietschy.wizard.WizardFrameCloser;
import org.pietschy.wizard.WizardListener;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

public class WizardUtil {
	
	public static final KeyStroke CTRL_E = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK);
	public static final KeyStroke CTRL_S = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK);
	public static final KeyStroke CTRL_SPACE = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK);	

	private static final int SIZE_X = 530;
	private static final int SIZE_Y = 480;
	
	private static LinkedList wizardsStack = new LinkedList();
	
	private static Wizard definitionWizard;
	
	public static Wizard getDefinitionWizard(){
		if(definitionWizard==null){
			definitionWizard = createDefinitionWizard();			
		}//if
		definitionWizard.reset();
		WizardFrameCloser.bind(definitionWizard, getWizardFrame(definitionWizard));
		definitionWizard.getModel().getActiveStep().prepare();
		return definitionWizard;		
	}//getDefinitionWizard
	
	private static Wizard createDefinitionWizard(){
		Math4u2WizardModel model = new Math4u2WizardModel();
		
		StandardWizardStep step;
		
		//Definition
		step = new DefinitionWizardStep();
		step.addParam("Definitionskopf", "Hier wird der Name des Objekts vereinbart, bei Funktionen zusätzlich der Variablenname, z.B " +
				"<ul><li>a&nbsp;&nbsp;&nbsp;Kann der Name eines Parameters sein</li>" +
				"<li>f(x)&nbsp;&nbsp;&nbsp;Funktion mit Variable x</li>" +
				"<li>f(u,v)&nbsp;&nbsp;&nbsp;zweistellige Funktion mit Variablen u, v</li>" +
				"<li>mehrstellige Funktionen entsprechend</li>" +
				"<li>pkt1&nbsp;&nbsp;&nbsp;Kann der Name eines Punktes sein</li></ul>", WizParamType.DEFINITION_HEAD);
		step.addParam("Definitionsrumpf", "Geben Sie eine Definition ein, z.B. sin(3), punkt(1,2)",WizParamType.DEFINITION_BODY);
		step.addParam("", WizParamType.CLEAR_FORMULAR);
		step.addParam("", WizParamType.EXECUTE_DEFINITION);
		model.add(step);		
					
		final Wizard wizard = new Wizard(model);
		model.put("wizard", wizard);
		wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
		String frameTitle = "Definition";
		JFrame wizardFrame = new JFrame(frameTitle);
		model.put("frame", wizardFrame);
		layoutWindow(wizardFrame);
		wizardFrame.setIconImage(Images.LOGO_ICON.getImage());

		wizardFrame.getContentPane().add(wizard);
		wizardFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				wizard.cancel();				
			}
		});		

		definitionWizard = wizard;
		return wizard;		
	}//createDefinitionWizard
	
	public static Wizard wizardCache = _getNewObjectWizard();
	
	public static Wizard getNewObjectWizard(WizParamType type, HasTitle titleGiver, String previousTitle){
		Wizard wizard = wizardCache;
		wizardCache = null;
		(new Thread(){
			public void run(){
				wizardCache = _getNewObjectWizard();
			}
		}).start();
		
		Math4u2WizardModel model = (Math4u2WizardModel) wizard.getModel();
		
		Object obj = model.getObj("ChooseObjectWizardStep");
		((ChooseObjectWizardStep)obj).setType(type);
		
		obj = model.getObj("HasTitleWrapper");
		((HasTitleWrapper)obj).setTitle(titleGiver);
		
		//Fenstertitel setzen
		String frameTitle = previousTitle;
		if(titleGiver.getTitle().length()!=0 && previousTitle.length()!=0){
			frameTitle += " | ";			
		}//if
		frameTitle += titleGiver.getTitle();
		getWizardFrame(wizard).setTitle(frameTitle);
		return wizard;
	}//getNewObjectWizard
	
	public static Wizard _getNewObjectWizard(){
		Math4u2WizardModel model = new Math4u2WizardModel();
		
		HasTitle titleGiver = new HasTitleWrapper();
		model.put("HasTitleWrapper", titleGiver);
		
		ChooseObjectWizardStep chooseStep = new ChooseObjectWizardStep();
		model.put("ChooseObjectWizardStep", chooseStep);
		model.add(chooseStep);		
		
		StandardWizardStep step;

		//Objekt auswählen
		step = new SelectObjectWizardStep();
		model.add(step, new EquCondition("Objekt auswählen"));
		
		//Vektor auswahl
		step = new MatrixInstantiateChoiceStep("Vektor", titleGiver, "vektor", "Wie soll der Vektor erzeugt werden?");
		step.addParam("Platzhalter", WizParamType.SCALAR);
		model.add(step, new EquCondition("Vektor"));
		
		//Vektor explizit
		step = new StandardWizardStep("Vektor", titleGiver, "vektor", WizParamType.VECTOR);
		step.addParam("Vektor", WizParamType.ANONYM_SCALAR_LIST);
		model.add(step, new EquCondition("Vektor explizit"));
		
		//Vektor implizit
		step = new StandardWizardStep("Vektor", titleGiver, "vektor", WizParamType.VECTOR);
		step.addParam("Indexname", "Hier wird der Name für den Elementindex vereinbart.<br>Dieser wird im Elementterm benutzt.", WizParamType.VAR_NAME);
		step.addParam("Anzahl der Elemente", WizParamType.SCALAR);
		step.addParam("Elementterm", "Beschreibt, wie die Elemente vom Index abhängen<br>z.B. vektor(i,3,2*i) ergibt {2,4,6}", WizParamType.FUNC_1_SCALAR);
		model.add(step, new EquCondition("Vektor implizit"));		
		
		//Vektor aus Datei laden
		step = new StandardWizardStep("Vektor", titleGiver, "vektor", WizParamType.VECTOR);
		step.addParam("Dateipfad", "Datei, in der die Elementwerte durch Kommata getrennt aufgeführt sind.<br>z.B. 1.0, 2, 3E0, 7.0e-3, sin(3)", WizParamType.FILE);		
		model.add(step, new EquCondition("Vektor aus Datei laden"));	
		
		//Dualvektor auswahl
		step = new MatrixInstantiateChoiceStep("Dualvektor", titleGiver, "dualvektor", "Wie soll der Dualvektor erzeugt werden?");
		step.addParam("Platzhalter", WizParamType.SCALAR);
		model.add(step, new EquCondition("Dualvektor"));
		
		//Dualvektor explizit
		step = new StandardWizardStep("Dualvektor", titleGiver, "dualvektor", WizParamType.DUALVEKTOR);
		step.addParam("Dualvektor", WizParamType.ANONYM_SCALAR_LIST);
		model.add(step, new EquCondition("Dualvektor explizit"));
		
		//Dualvektor implizit
		step = new StandardWizardStep("Dualvektor", titleGiver, "dualvektor", WizParamType.DUALVEKTOR);
		step.addParam("Indexname", "Hier wird der Name für den Elementindex vereinbart.<br>Dieser wird im Elementterm benutzt.", WizParamType.VAR_NAME);
		step.addParam("Anzahl der Elemente", WizParamType.SCALAR);
		step.addParam("Elementterm", "Beschreibt, wie die Elemente vom Index abhängen<br>z.B. dualvektor(i,3,2*i) ergibt {2,4,6}", WizParamType.FUNC_1_SCALAR);
		model.add(step, new EquCondition("Dualvektor implizit"));		
		
		//Dualvektor aus Datei laden
		step = new StandardWizardStep("Dualvektor", titleGiver, "dualvektor", WizParamType.DUALVEKTOR);
		step.addParam("Datei", "Datei, in der die Elementwerte durch Kommata getrennt aufgeführt sind.<br>z.B. 1.0, 2, 3E0, 7.0e-3" , WizParamType.FILE);		
		model.add(step, new EquCondition("Dualvektor aus Datei laden"));		
		
		//Matrix auswahl
		step = new MatrixInstantiateChoiceStep("Matrix", titleGiver, "matrix", "Wie soll die Matrix erzeugt werden?");
		step.addParam("Platzhalter", WizParamType.SCALAR);
		model.add(step, new EquCondition("Matrix"));
		
		//Matrix explizit - Dimension
		step = new StandardWizardStep("Matrix", titleGiver, "matrix", WizParamType.MATRIX){
			public void prepare() {
				super.prepare();				
				setComplete(false);
			}//prepare

			protected String _refreshResult(boolean withHtml) {
				CanModifyText cols = ((ParamEntry)params.get(0)).comp;
				CanModifyText rows = ((ParamEntry)params.get(1)).comp;
				model.put("cols", cols.getText());
				model.put("rows", rows.getText());
				
				String s = "Matrix-Dimension: ";
				String colsStr = cols.getText();
				s += markIt(colsStr, withHtml, cols);
				if(colsStr!= null && colsStr.length()!=0)
					s += " x ";
				String rowsStr = rows.getText();
				s += markIt(rowsStr, withHtml, rows);
				model.put("model", "Matrix explizit2");
				try{
					Integer.parseInt(rowsStr); Integer.parseInt(colsStr);
					setComplete(true);
				}catch(NumberFormatException e){};
				return s;
			}//_refreshResult
			
			protected void refreshErrorHighlights(){}
		};
		step.addParam("Anzahl Reihen", WizParamType.SCALAR);
		step.addParam("Anzahl Spalten", WizParamType.SCALAR);
		model.add(step, new EquCondition("Matrix explizit"));
		
		step = new MatrixWizardStep("Matrix", titleGiver, "matrix");
		step.addParam("", WizParamType.ANONYM_MATRIX);
		model.add(step, new EquCondition("Matrix explizit2"));
		
		//Matrix implizit
		step = new StandardWizardStep("Matrix", titleGiver, "matrix", WizParamType.MATRIX);
		step.addParam("Spalen-Indexname", "Hier wird der Spalten-Indexname für den Elementindex vereinbart.<br>Dieser wird im Elementterm benutzt.", WizParamType.VAR_NAME);
		step.addParam("Anzahl der Spalte", WizParamType.SCALAR);
		step.addParam("Zeilen-Indexname", "Hier wird der Zeilen-Indexname für den Elementindex vereinbart.<br>Dieser wird im Elementterm benutzt.", WizParamType.VAR_NAME);
		step.addParam("Anzahl der Zeilen", WizParamType.SCALAR);		
		step.addParam("Elementterm", "Beschreibt, wie die Elemente vom Spaltenindex und Zeilenindex abhängen<br>z.B. matrix(i,3,j,2,j*i) ergibt {{1,2},{2,4},{3,6}}", WizParamType.FUNC_1_SCALAR);
		model.add(step, new EquCondition("Matrix implizit"));		
		
		//Matrix aus Datei laden
		step = new StandardWizardStep("Matrix", titleGiver, "matrix", WizParamType.MATRIX);
		step.addParam("Datei", "Datei, in der die Elementwerte spaltenweise durch Kommata<br>und zeilenweise durch Zeilen (newline) getrennt aufgeführt sind.<br>z.B.<br>1.0, 2, 3E0, 7.0e-3<br>3.0, sin(2), 0", WizParamType.FILE);		
		model.add(step, new EquCondition("Matrix aus Datei laden"));	
				
		//Liste Auswahl
		step = new ListInstantiateChoiceStep("Liste", titleGiver, "liste", "Wie soll die Liste erzeugt werden?");
		step.addParam("Platzhalter", WizParamType.SCALAR);
		model.add(step, new EquCondition("Liste"));
		
		//Liste explizit
		step = new ListWizardStep("Liste", titleGiver, "liste");
		step.addParam("Typ", WizParamType.LIST_TYPE);
		step.addParam("Liste", WizParamType.ANONYM_LIST);
		model.add(step, new EquCondition("Liste explizit"));	
		
		//Liste implizit
		step = new ListWizardStep("Liste", titleGiver, "liste");
		step.addParam("Typ", WizParamType.LIST_TYPE);
		step.addParam("Indexname", "Hier wird der Name für den Elementindex vereinbart.<br>Dieser wird in der Erzeugungsvorschrift benutzt.",WizParamType.VAR_NAME);
		step.addParam("Anzahl der Elemente", WizParamType.SCALAR);
		step.addParam("Erzeugungsvorschrift", "Erzeugungsvorschrift des Elements<br>z.B. &lt;punkt> liste(i,2,punkt(i,i)) ergibt {punkt(1,1), punkt(2,2)}", WizParamType.ANY_INPUT);
		model.add(step, new EquCondition("Liste implizit"));
		
		//Folge
		step = new StandardWizardStep("Folge", titleGiver, "folge",
				"Aus einer Anzahl von Startwerten werden weitere Folgenglieder berechnet.", WizParamType.FOLGE){
			protected String _refreshResult(boolean withHtml) {
				StringBuffer text = new StringBuffer();
				
				Iterator iter = params.iterator();
				
				ParamEntry param = (ParamEntry) iter.next();
				text.append(markIt(param.comp.getText(),withHtml, param.comp));
				
				text.append(" ");
				params.remove(0);
				text.append(super._refreshResult(withHtml));
				params.add(0,param);
				return text.toString();
			}//_refreshResult			
		};
		step.addParam("Typ", WizParamType.SEQUENCE_TYPE);
		step.addParam("Indexname", "Hier wird der Name für den Elementindex vereinbart.<br>Dieser wird im Elementterm benutzt.", WizParamType.VAR_NAME);
		step.addParam("Startwerte", "Diese Werte können später in der Erzeugungsvorschrift benutzt werden.", WizParamType.ANONYM_SCALAR_LIST);
		step.addParam("Erzeugungsvorschrift", "Erzeugungsvorschrift des nächsten Elements<br>z.B. fib := folge(i, {1, 1}, fib[i-1] + fib[i-2])", WizParamType.ANY_INPUT);
		model.add(step, new EquCondition("Folge"));
		
		//Funktion
		step = new FunctionWizardStep("Funktion", titleGiver);
		step.addParam("Funktion", WizParamType.NEW_FUNC);
		step.addParam("Argument 1", WizParamType.ANY_INPUT);
		step.addParam("Argument 2", WizParamType.ANY_INPUT);
		step.addParam("Argument 3", WizParamType.ANY_INPUT);
		step.addParam("Argument 4", WizParamType.ANY_INPUT);
		step.addParam("Argument 5", WizParamType.ANY_INPUT);
		step.addParam("Argument 6", WizParamType.ANY_INPUT);
		model.add(step, new EquCondition("Funktion"));			
		
		//Punkt
		step = new StandardWizardStep("Punkt", titleGiver, "punkt",
				"Der Punkt kann mit der Maus bewegt werden, dadurch "
						+ "ändern sich seine Koordinaten.", WizParamType.POINT);
		step.addParam("x-Koordinate", WizParamType.SCALAR);
		step.addParam("y-Koordinate", WizParamType.SCALAR);
		model.add(step, new EquCondition("Punkt"));
		
		//Markierung
		step = new StandardWizardStep(
				"Markierung",
				titleGiver,
				"marker",
				"Die Markierung ist eine optisch und funktional reduzierte Variante des Punktes.<br>Eine Markierung kann nicht mit der Maus bewegt werden.", WizParamType.MARKER);
		step.addParam("x-Koordinate", WizParamType.SCALAR);
		step.addParam("y-Koordinate", WizParamType.SCALAR);
		model.add(step, new EquCondition("Markierung"));
		
		// Strecke
		step = new StandardWizardStep("Strecke", titleGiver, "strecke", "Verbindungsstrecke von zwei Punkten.", WizParamType.STRETCH);
		step.addParam("Startpunkt", WizParamType.POINT);
		step.addParam("Endpunkt", WizParamType.POINT);
		model.add(step, new EquCondition("Strecke"));
		
		//Gerade
		step = new StandardWizardStep("Gerade", titleGiver, "gerade", "Die Gerade wird durch einen Punkt und einen Richtungsvektor definiert.", WizParamType.STRAIGHT);
		step.addParam("Punkt", "Ein Punkt, durch den die Gerade verlaufen soll.", WizParamType.POINT);
		step.addParam("Richtungsvektor", "Die Gerade wird in Richtung dieses Vektors gezeichnet", WizParamType.VECTOR_2D);
		model.add(step, new EquCondition("Gerade"));

		//Kreis
		step = new StandardWizardStep("Kreis", titleGiver, "kreis", "Der Kreis wurd durch Mittelpunkt und Radius definiert.", WizParamType.CIRCLE);
		step.addParam("Mittelpunkt", WizParamType.POINT);
		step.addParam("Radius", WizParamType.SCALAR);
		model.add(step, new EquCondition("Kreis"));
		
		//Pfeil
		step = new StandardWizardStep("Pfeil", titleGiver, "pfeil", "Ein Pfeil ist ein Repräsentant eines zweidimensionalen Vektors", WizParamType.ARROW);
		step.addParam("Aufpunkt", WizParamType.POINT);
		step.addParam("Vektor", "Vektor, den der Pfeil repräsentiert", WizParamType.VECTOR_2D);
		model.add(step, new EquCondition("Pfeil"));			
		
		//Winkel
		step = new StandardWizardStep("Winkel", titleGiver, "winkel", "Darstellung eines Winkels im Scheitelpunkt mit Drehrichtung vom ersten zum zweiten Schenkel.", WizParamType.ANGLE);
		step.addParam("Scheitelpunkt", "Scheitelpunkt des Winkels", WizParamType.POINT);
		step.addParam("ersten Richtungsvektor", "Richtungvektor des ersten Schenkels",WizParamType.VECTOR_2D);
		step.addParam("zweiter Richtungsvektor", "Richtungvektor des zweiten Schenkels", WizParamType.VECTOR_2D);
		step.addParam("Radius", "Abstand des Winkelpfeils vom Scheitelpunkt", WizParamType.SCALAR);
		model.add(step, new EquCondition("Winkel"));		
		
		//Param. Kurve
		step = new StandardWizardStep("Param. Kurve", titleGiver, "kurve", "Kurve die durch einen Parameter und zwei Koordinaten-Funktionen definiert ist.", WizParamType.CURVE);
		step.addParam("Kurvenparameter", WizParamType.VAR_NAME);
		step.addParam("untere Grenze", "untere Grenze des Parameterintervalls, z.B. 0", WizParamType.SCALAR);
		step.addParam("obere Grenze", "obere Grenze des Parameterintervalls, z.B. 1", WizParamType.SCALAR);
		step.addParam("x-Funktion", "Koordinatenfunktion der x-Koordinate, z.B. t^2+1, falls t der Kurvenparameter ist", WizParamType.FUNC_1_SCALAR);
		step.addParam("y-Funktion", "Koordinatenfunktion der y-Koordinate, z.B. sint(t), falls t der Kurvenparameter ist", WizParamType.FUNC_1_SCALAR);
		model.add(step, new EquCondition("Param. Kurve"));				
		
		//Bezier-Kurve
		step = new StandardWizardStep("Bezier-Kurve", titleGiver, "bezier", "Eine Bezier-Kurve ist ein Kurvenstück, dessen Verlauf durch vier Steuerpunkte festgelegt wird.", WizParamType.BEZIER);
		step.addParam("Punkt 1", "Startpunkt", WizParamType.POINT);
		step.addParam("Punkt 2", "Kurve verläuft von Punkt 1 (Startpunkt) aus zunächst in Richtung dieses Punktes", WizParamType.POINT);
		step.addParam("Punkt 3", "Kurve kommt aus der Richtung dieses Punktes am Punkt 4 (Endpunkt) an",WizParamType.POINT);
		step.addParam("Punkt 4", "Endpunkt", WizParamType.POINT);
		model.add(step, new EquCondition("Bezier-Kurve"));	
		
		//Kurvenzug
		step = new StandardWizardStep("Kurvenzug", titleGiver, "kurvenzug", 
				"Ein Kurvenzug enthält mehrere Elemente, die sich selbst zeichnen können." +
				"<br>Diese Elemente werden der Reihe nach durch Strecken verbunden.",
				WizParamType.CURVES);
		step.addParam("Elemente", WizParamType.ANONYM_AREA_LIST);
		model.add(step, new EquCondition("Kurvenzug"));			

		//Fläche
		step = new StandardWizardStep("Fläche", titleGiver, "flaeche", 
				"Eine Fläche enthält mehrere Elemente, die sich selbst zeichnen können." +
				" Diese Elemente werden der Reihe nach durch Strecken zu einer geschlossenen Fläche verbunden.",
				WizParamType.AREA);
		step.addParam("Elemente", WizParamType.ANONYM_AREA_LIST);
		model.add(step, new EquCondition("Fläche"));		
		
		//Punkte
		step = new StandardWizardStep("Punkte", titleGiver, "punkte", "Diskretes Punkt-Diagramm.", WizParamType.DISCRETE);
		step.addParam("x-Werte", "Die Elemente des Vektors geben die x-Koordinaten der Punkte an", WizParamType.VECTOR);
		step.addParam("y-Werte", "Die Elemente des Vektors geben die y-Koordinaten der Punkte an", WizParamType.VECTOR);
		step.addParam("Symbolgröße", "Symbolgröße in Pixel", WizParamType.SCALAR);
		model.add(step, new EquCondition("Punkte"));
		
		//Punktfolge
		step = new StandardWizardStep("Punktfolge", titleGiver, "punktFolge", "Punkt-Diagramm einer Folge.", WizParamType.DISCRETE_SEQUENCE);
		step.addParam("x-Folge", "Die Elemente der Folge geben die x-Koordinaten der Punkte an", WizParamType.SEQUENCE_TYPE);
		step.addParam("y-Folge", "Die Elemente der Folge geben die y-Koordinaten der Punkte an", WizParamType.SEQUENCE_TYPE);
		step.addParam("Anzahl", "Gibt an, bis zu welchem Folgenglied die Darstellung erfolgen soll.", WizParamType.SCALAR);
		step.addParam("Symbolgröße", "Symbolgröße in Pixel der Marker", WizParamType.SCALAR);
		step.addParam("Darstellung", WizParamType.DISCRETE_SEQUENCE_LAYOUT);
		model.add(step, new EquCondition(ChooseObjectWizardStep.PUNKT_FOLGE.description));	
		
		//Balken
		step = new StandardWizardStep("Balken", titleGiver, "balken", "Erstellt ein Balkendiagramm auf der x-Achse.", WizParamType.BAR);
		step.addParam("x-Koordinaten", "Die Elemente des Vektors geben die Positionen der Balken an", WizParamType.VECTOR);
		step.addParam("Balkenhöhen", "Die Elemente des Vektors bestimmen die Höhe der Balken", WizParamType.VECTOR);
		step.addParam("Breite", "Die Funktion breite regelt die Breite der Balken.<br>Mit -1 wird die Breite automatisch angepasst", WizParamType.SCALAR);
		model.add(step, new EquCondition("Balken"));		

		//Karte
		step = new MapWizardStep("Karte", titleGiver, "karte", 
				"Eine Karte stellt die Werte einer zweistelligen Funktion f(x,y) farblich dar. Wie bei einer Landkarte" +
				" werden den Positionen (x,y) durch die Funktion f Farbwerte zugeordnet.");
		step.addParam("Zweistelligen Funktion", "Name der Funktion, z.B. f<br>Diese zweistellige Funktion f muß vorher definiert sein",  WizParamType.FUNC_2_SCALAR);
		step.addParam("kleinster Funktionswert", "Dieser Wert legt den kleinsten Funktionswert fest, der farbig dargestellt wird", WizParamType.SCALAR);
		step.addParam("größter Funktionswert", "Dieser Wert legt den größten Funktionswert fest, der farbig dargestellt wird", WizParamType.SCALAR);
		step.addParam("Höhenlinien", "Werte an denen Höhenlinien gezeichnet werden, z.B. 1, 2, 3<br>" +
				"Es können auch Höhenlinien dargestellt werden, die außerhalb des Bereichs<br>[kleinster Funktionswert, größter Funktionswert] liegen.",WizParamType.ANONYM_SCALAR_LIST);
		model.add(step, new EquCondition("Karte"));			
	
		//Vektorfeld
		step = new StandardWizardStep("Vektorfeld", titleGiver, "feld", "Stellt eine zweistellige vektorwertige Funktion v(x,y) (Vektorfeld) graphisch dar.", WizParamType.FIELD_VECTOR);
		step.addParam("Funktion", "Name der Funktion, z.B. v<br>Diese zweistellige vektorwertige Funktion v muß vorher definiert sein", WizParamType.FUNC_2_VECTOR);
		step.addParam("Referenzpunkt", "Referenzpunkt für die graphische Darstellung.<br>Dient zur Positionierung des Darstellungsgitters", WizParamType.POINT);
		step.addParam("x-Distanz", "Abstand der Gitterpunkte in x-Richtung", WizParamType.SCALAR);
		step.addParam("y-Distanz", "Abstand der Gitterpunkte in y-Richtung", WizParamType.SCALAR);
		step.addParam("Cutoff", "Größter darzustellenden Betrag.<br>Vektoren, deren Betrag größer ist, werden nicht dargestellt.", WizParamType.SCALAR);
		step.addParam("Darstellung", WizParamType.VECTORFIELD_LAYOUT);
		model.add(step, new EquCondition("Vektorfeld"));				
		
		//Text
		step = new StandardWizardStep("Text", titleGiver, "text","Textblase, die auf der Zeichenfläche dargestellt werden kann.", WizParamType.TEXT);
		step.addParam("Aufpunkt", "Die Textblase zeigt auf diesen Punkt", WizParamType.POINT);
		step.addParam("Textinhalt", "Der Text kann auch Formeln enthalten z.B. &quot;&lt;f>x^2&lt;/f> ist eine Formel&quot;", WizParamType.TEXT);
		step.addParam("Layout-Konstante", WizParamType.TEXT_STYLE);
		model.add(step, new EquCondition("Text"));			
		
		final Wizard wizard = new Wizard(model);
		model.put("wizard", wizard);
		wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);		

		JFrame wizardFrame = new JFrame("");
		model.put("frame", wizardFrame);		
		wizardFrame.setIconImage(Images.LOGO_ICON.getImage());
		wizardFrame.getContentPane().add(wizard);
		wizardFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				wizard.cancel();
			}
		});
		WizardFrameCloser.bind(wizard, wizardFrame);
		
		return wizard;
	}//getNewObjectWizard
	
	public static void layoutWindow(Window window){
		window.setBounds(Settings.computeBounds(null, SIZE_X, SIZE_Y));
		Point point = window.getLocation();
		int count = wizardsStack.size()-2;
		point.translate(count *20,count*30);
		window.setLocation(point);
		window.doLayout();
		
		// Handling für Zähler
		window.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e) {				
				LinkedList stack = WizardUtil.wizardsStack;
				if(!stack.contains(e.getWindow()))
					return;
				
				Window window;
				while(!stack.isEmpty() && !(window = (Window)stack.getLast()).equals(e.getWindow())){
					window.dispose();
					stack.remove(window);
				}//while
				
				//Jetzt ist das aktuelle Window an der Stackspitze
				stack.removeLast();
				
				//Alle Windows in der Reihenfolge der Öffnung nach vorne bringen
				for(Iterator iter = stack.iterator(); iter.hasNext();){
					window = (Window) iter.next();
					window.toFront();
				}//for iter
			}//windowClosed
			
		});

		wizardsStack.add(window);
	}//layoutWindow
	
	/**
	 * Setzt die Textkomponente mit Wizard fest.
	 * @param textComp Textkomponente
	 * @param type
	 */
	public static void addKeyAssignment(final JTextComponent textComp, Container container, WizParamType type, HasTitle titleGiver, String previousTitle, boolean isListBox){
	    Keymap parent = textComp.getKeymap();
	    
	    Keymap newmap = JTextComponent.addKeymap("ListBoxInputFieldKeyMap", parent);

	    
	    
	    final Action actionE = new WizardUtil.InsertOrEditMathObjectAction(type, titleGiver, previousTitle, isListBox);
	    newmap.addActionForKeyStroke(CTRL_E, actionE);	    
	    newmap.addActionForKeyStroke(CTRL_SPACE, actionE);
	    Action actionS = new WizardUtil.SelectMathObjectAction(type, titleGiver);
	    newmap.addActionForKeyStroke(CTRL_S, actionS);	    
	    

	    // get all the actions JTextComponent provides for us
	    Action actionList[] = textComp.getActions();
	    // put them in a Hashtable so we can retreive them by Action.NAME
	    Hashtable lookup = new Hashtable();
	    for (int j=0; j < actionList.length; j+=1)
	      lookup.put(actionList[j].getValue(Action.NAME), actionList[j]);

	    // set the JTextArea's Keymap to be our new map
	    textComp.setKeymap(newmap);		    
	    
	    //Button hinzufügen
		JLabel addButton = getAddButton(textComp);
		
		if(container!=null){
			double P = TableLayout.PREFERRED, F = TableLayout.FILL;
			double size[][] = { { F, P }, { F, P, F} };
			container.setLayout(new TableLayout(size));
			container.add(addButton, "1, 1, R, C");
		}//if
	}//addKeyAssignment
	
	public static JLabel getAddButton(final JTextComponent textComp){
		JLabel addButton = new JLabel(Images.ADD_BUTTON_ICON);
		addButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ActionEvent event = new ActionEvent(textComp, ActionEvent.ACTION_PERFORMED,null);
				textComp.getKeymap().getAction(CTRL_E).actionPerformed(event);
			}
		});
		addButton.addMouseListener(new LabelAsButtonMouseListener(addButton));
		addButton.setToolTipText("<html><b>Objekt erzeugen/auswhälen</b>  Strg+E oder Strg+LEERTASTE<br>(für Objekt auswählen  Strg+S)</html>");
		return addButton;
	}//getAddButton
	
	private static JFrame getWizardFrame(Wizard wizard){
		return (JFrame)((Math4u2WizardModel)wizard.getModel()).getObj("frame");
	}//showWizard
		
	public static class InsertOrEditMathObjectAction extends TextAction {
		
		private WizParamType type;
		private HasTitle titleGiver;
		private String previousTitle;
		private boolean isListBox;
		
		public InsertOrEditMathObjectAction(WizParamType type, HasTitle titleGiver, String previousTitle, boolean isListBox) {
			super("insert-or-edit-mathobject-action");
			this.type = type;
			this.titleGiver = titleGiver;
			this.previousTitle = previousTitle;
			this.isListBox = isListBox;
		}//Konstruktor
		
		public void actionPerformed(ActionEvent e) {
			final JTextComponent comp = (JTextComponent) e.getSource();
			if (comp == null)
				return;			
			int start = comp.getSelectionStart();
			int end = comp.getSelectionEnd();
			
			//Falls keine Selektion vorhanden ist, Wizard für "neues Objekt" öffnen
			if (start == end) {				
				Wizard wizard = null;
				if(isListBox && comp.getText().trim().length()==0){					
					wizard = getDefinitionWizard();
				}else{
					wizard = getNewObjectWizard(type,titleGiver, previousTitle);	
				}
								
				wizard.addWizardListener(new WizardListener(){
					public void wizardClosed(WizardEvent we) {
						Math4u2WizardModel model = (Math4u2WizardModel) we.getWizard().getModel();
						String text = model.get("output");
						try {
							comp.getDocument().insertString(comp.getCaretPosition(),text,null);
						} catch (BadLocationException e) {
							ExceptionManager.doError(e);
						}
						we.getWizard().removeWizardListener(this);
						
						if(model.getActiveStep() instanceof DefinitionWizardStep){
							((DefinitionWizardStep)model.getActiveStep()).endAction(text);
						}//if						
					}//wizardClosed

					public void wizardCancelled(WizardEvent arg0) {
					}
				});
				layoutWindow(getWizardFrame(wizard));
				getWizardFrame(wizard).setVisible(true);
			}//if
		}//actionPerformed
	}//class InsertOrEditMathObjectAction	
	
	public static class SelectMathObjectAction extends TextAction {
		
		private WizParamType type;
		private HasTitle titleGiver;
		
		public SelectMathObjectAction(WizParamType type, HasTitle titleGiver) {
			super("select-mathobject-action");
			this.type = type;
			this.titleGiver = titleGiver;
		}//Konstruktor

		public void actionPerformed(ActionEvent e) {
			final JTextComponent comp = (JTextComponent) e.getSource();
			if (comp == null)
				return;
			final Document doc = comp.getDocument();
			int start = comp.getSelectionStart();
			int end = comp.getSelectionEnd();
			
			if (start == end) {
				Wizard wizard = getNewObjectWizard(type,titleGiver, "");
				((Math4u2WizardModel)wizard.getModel()).put("model", "Objekt auswählen");
				wizard.getModel().nextStep();
				wizard.addWizardListener(new WizardListener(){

					public void wizardClosed(WizardEvent we) {
						Math4u2WizardModel model = (Math4u2WizardModel) we.getWizard().getModel();
						String text = model.get("output");
						try {
							doc.insertString(comp.getCaretPosition(),text,null);
						} catch (BadLocationException e) {
							ExceptionManager.doError(e);
						}
						we.getWizard().removeWizardListener(this);
					}//wizardClosed

					public void wizardCancelled(WizardEvent arg0) {
					}
				});
				layoutWindow(getWizardFrame(wizard));
				getWizardFrame(wizard).setVisible(true);
			}//if
		}//actionPerformed
	}//class SelectMathObjectAction
	
	static class EquCondition implements Condition{
		
		private String test;
		
		public EquCondition(String test){
			this.test = test;
		}//EquCondition
		
		public boolean evaluate(WizardModel model) {
			return test.equals(((Math4u2WizardModel) model)
					.get("model"));
		}// evaluate
	}// class Condition
	
}//class WizardUtil
