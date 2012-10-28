package math4u2.exercises;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.text.BadLocationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.exercises.scripting.EActionAnimation;
import math4u2.exercises.scripting.EActionCall;
import math4u2.exercises.scripting.EActionContainer;
import math4u2.exercises.scripting.EActionContainerPar;
import math4u2.exercises.scripting.EActionContainerSeq;
import math4u2.exercises.scripting.EActionParam;
import math4u2.exercises.scripting.ModuleRegistry;
import math4u2.exercises.ui.Math4u2TextPane;
import math4u2.exercises.ui.StyledText;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.UserFunction;
import math4u2.parser.formula.fsParser;
import math4u2.parser.script.KurzSchrift;
import math4u2.util.color.ColorUtil;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotImplementedException;
import math4u2.util.exception.parser.ParseException;
import math4u2.util.io.file.FileResolver;
import math4u2.util.io.file.VersionFileTester;
import math4u2.util.io.file.xml.DOMWriter;
import math4u2.util.io.file.xml.filestatus.FileStatusInterface;
import math4u2.util.io.file.xml.filestatus.Remote;
import math4u2.util.io.file.xml.sax.ServerContentsFile;
import math4u2.util.io.file.xml.sax.ServerContentsFileXMLHandler;
import math4u2.util.swing.font.Unicode;
import math4u2.view.formula.AtomicBox;
import math4u2.view.formula.BinBox;
import math4u2.view.formula.ContainerBox;
import math4u2.view.formula.FormulaRenderContext;
import math4u2.view.formula.ImageBox;
import math4u2.view.formula.TableBox;
import math4u2.view.gui.component.DefinitionField;
import math4u2.view.gui.component.MathObjectWrapper;
import math4u2.view.layout.FormulaLayout;
import math4u2.view.layout.PercentLayout;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xmlpull.v1.XmlPullParserException;


/**
 * Eine Parser-Klasse, die math4u2-XML-Dateien mit Hilfe eines DOM-Dokuments
 * analysiert.
 * 
 * @author Erich Seifert
 * @author Michael Lichtenstern
 * @version 0.1
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class EParser {

	private static final String SHOW_BODY = "body";

	private static final String SHOW_SHORT = "short";

	private static final String SHOW_ACTUAL = "actual";

	private static final String SHOW_NORMAL = "";

	private static final String SHOW_DEFINITION = "definition";

	private static final String BR_CHAR = System.getProperty("line.separator");

	private static final String PARA_CHAR = BR_CHAR + BR_CHAR;

	private String fileName;

	/** Zählt wieviele <script>-Tags enthalten sind */
	private int blockNr;

	private Document doc;

	private Math4U2Document m4uDoc = new Math4U2Document();

	private DocumentBuilder builder;

	private Broker broker;

	private SAXParser saxParser;

	/**
	 * Standardkonstruktor der ein neues EParser-Objekt erzeugt.
	 */
	public EParser(Broker broker) {
		this.broker = broker;
	}

	/**
	 * Konstruktor, der ein neues EParser-Objekt erzeugt und gleich mit der
	 * Analyse der übergebenen Datei beginnt.
	 * 
	 * @param filename
	 *            Name der Datei die analysiert werden soll
	 */
	public EParser(String filename, Broker broker, FileStatusInterface fsi) {
		this(broker);
		analyse(filename, fsi);
	} //Konstruktor
	
    private static File getFile(String filePath){
    	return FileResolver.resolve(filePath, EParser.class);
    }

	/**
	 * Gibt einen Builder zurück, der XML-Dateien parsen kann.
	 */
	public DocumentBuilder getDocumentBuilder() {
		if (builder == null) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setValidating(false);
				factory.setNamespaceAware(true);
				factory.setXIncludeAware(true);
				builder = factory.newDocumentBuilder();
				builder.setEntityResolver(new EntityResolver() {
					public InputSource resolveEntity(String publicId,
							String systemId) throws SAXException, IOException {
						if ("-//FH Augsburg//DTD Math4u2//DE".equals(publicId)) {
							File file = getFile(
									"math4u2/xml/dtd/math4u2DTD.dtd");
							return new InputSource(new FileInputStream(file));
						} else if ("-//FH Augsburg//DTD Math4u2 V2.1//DE"
								.equals(publicId)) {
							File file = getFile(
									"math4u2/xml/dtd/math4u2_V2_1DTD.dtd");
							return new InputSource(new FileInputStream(file));
						} else if ("-//FH Augsburg//DTD Math4u2 V2.2//DE"
								.equals(publicId)) {
							File file = getFile(
									"math4u2/xml/dtd/math4u2_V2_2DTD.dtd");
							return new InputSource(new FileInputStream(file));							
						} else {
							return new InputSource(systemId);
						}//else
					}//resolveEntity
				});
			} catch (FactoryConfigurationError e) {
				// unable to get a document builder factory
				ExceptionManager.doError(
						"Parser-Fabrik ist nicht gültig konfiguriert.", e);
			} catch (ParserConfigurationException e) {
				// parser was unable to be configured
				ExceptionManager.doError(
						"Parser ist nicht gültig konfiguriert.", e);
			}
			return builder;
		} else
			return builder;
	}//getDocumentBuilder

	/**
	 * Analysiert eine Datei und speichert das Ergebnis.
	 * 
	 * @param filename
	 *            Name der Datei die analysiert werden soll
	 */
	public void analyseDOM(File file) throws ParseException {
		if (!file.exists())
			throw new ParseException("Die Datei " + file.getAbsolutePath()
					+ " wurde nicht gefunden");

		//Wichtige Attribute herauslesen
		SimpleTagParser stp = new SimpleTagParser(file.getAbsolutePath());
		String[][] saa = new String[][] { { "version" } };

		Map map = null;
		try {
			map = stp.parseTagText(saa);
		} catch (XmlPullParserException e) {
			ExceptionManager.doError("Fehler beim Parsen der Datei " + file.getAbsolutePath()
					+ ".", e);
		} catch (NoMath4u2DocumentException e) {
			return;
		}
		String currentVersion = (String) map.get("version");

		this.fileName = file.getAbsolutePath();
		InputStream in = null;
		try {
			DocumentBuilder builder = getDocumentBuilder();
			in = new BufferedInputStream(new FileInputStream(file));
			doc = builder.parse(in);
		} catch (SAXParseException e) {
			ExceptionManager.doError("Zeile: " + e.getLineNumber()
					+ " Spalte: " + e.getColumnNumber() + " in der Datei:"
					+ e.getSystemId(), e);
		} catch (SAXException e) {
			ExceptionManager.doError("Fehler beim Parsen der Datei " + file.getAbsolutePath()
					+ ".", e);
		} catch (IOException e) {
			ExceptionManager.doError("Fehler beim Lesen der Datei " + file.getAbsolutePath()
					+ ".", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					ExceptionManager.doError(e);
				}
			}//if
		}//finally
		doc.getDocumentElement().setAttribute("version", currentVersion);
	} //analyseDOM

	/**
	 * Analysiert die Baumstruktur der Themen
	 */
	// geaendert von Michael Lichtenstern	
	public void analyse(String filename, FileStatusInterface fsi) {
		this.fileName = filename;
		// Erzeugen Parser
		EIssue currentIssue = new EIssue(fsi);
		Folder folder = null;

		SimpleTagParser stp = new SimpleTagParser(filename);

		//Issue
		String[][] saa = new String[][] { { "version", "author" },
				{ "location", "title", "summary" } };
		Map map = null;
		
		try {
			map = stp.parseTagText(saa);
		} catch (XmlPullParserException e) {
			ExceptionManager.doError("Fehler beim Parsen der Datei " + filename
					+ ".", e);
		} catch (NoMath4u2DocumentException e) {
			return;
		}

		(new VersionFileTester()).test((String) map.get("version"), filename);

		currentIssue.setVersion((String) map.get("version"));
		if (map.get("author") == null) {
			//Autor neu einlesen, wegen der der neuen XML-Version 2.1
			String[][] saa2 = new String[][] { { "author" } };
			stp.restart();

			Map map2 = null;
			try {
				map2 = stp.parseTagText(saa2);
			} catch (XmlPullParserException e) {
				ExceptionManager.doError("Fehler beim Parsen der Datei "
						+ filename + ".", e);
			} catch (NoMath4u2DocumentException e) {
				return;
			}

			currentIssue.setAuthor((String) map2.get("author"));
		} else {
			currentIssue.setAuthor((String) map.get("author"));
		}//else
		currentIssue.setSummary((String) map.get("summary"));

		String title = (String) map.get("title");
		if (title == null || title.length() == 0) {
			ExceptionManager
					.doError("Tag <title> konnte nicht gelesen werden.("
							+ filename + ")");
			return;
		}
		String location = (String) map.get("location");
		if (location == null || location.length() == 0) {
			ExceptionManager
					.doError("Tag <location> konnte nicht gelesen werden.("
							+ filename + ")");
			return;
		}
		folder = parseLocation(location, m4uDoc.getFolders());

		fsi.setStatus(filename);

		if (">".equals(currentIssue.getSummary()))
			currentIssue.setSummary("");
		currentIssue.setTitle((String) map.get("title"));

		//Exercise
		stp = new SimpleTagParser(filename);
		List list = null;
		try {
			list = stp.parseForTagList(new String[] { "exercise", "title" });
		} catch (XmlPullParserException e) {
			ExceptionManager.doError("Fehler beim Parsen der Datei " + filename
					+ ".", e);
		}
		stp = null;
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			StyledText t = new StyledText();
			t.add((String) iter.next());
			Exercise e = new Exercise(t, null, this);
			currentIssue.addExercise(e);
		} //while

		//Keywords
		stp = new SimpleTagParser(filename);
		List keywords = null;
		try {
			keywords = stp.parseForTagList(new String[] { "keyword" });
		} catch (XmlPullParserException e) {
			ExceptionManager.doError("Fehler beim Parsen der Datei " + filename
					+ ".", e);
		}
		keywords.remove(">");
		stp = null;
		fsi.setKeyWords(keywords);

		folder.addIssue(currentIssue);
		m4uDoc.sortAll();
	} //analyse

	/**
	 * Erzeugt den Parser und gibt diesen jeweils gecacht wieder zurück
	 */
	private SAXParser getSAXParser() {
		if (saxParser != null)
			return saxParser;
		try {
			//Erzeugen Parser
			SAXParserFactory factory = SAXParserFactory.newInstance();
			saxParser = factory.newSAXParser();
		} catch (FactoryConfigurationError e) {
			ExceptionManager.doError("Parser-Fabrik ist ungültig konfiguriert",
					e);
		} catch (ParserConfigurationException e) {
			ExceptionManager.doError("Parser ist ungültig konfiguriert", e);
		} catch (SAXException e) {
			ExceptionManager.doError("Parser-Fehler", e);
		}
		return saxParser;
	}//getSAXParser

	/**
	 * Analysiert die Baumstruktur des Serverinhaltsverzeichnis
	 */
	// geaendert von Michael Lichtenstern
	public void analyseContents(List serverlist) {

		//Erzeugen Handler
		ServerContentsFileXMLHandler handler = null;
		Iterator i = serverlist.iterator();

		while (i.hasNext()) {
			handler = new ServerContentsFileXMLHandler();
			//Erzeugen Verbindung zur Eingabedatei
			File file = null;
			try {
				file = (File) i.next();
				getSAXParser().parse(file, handler);
			} catch (FileNotFoundException e) {
				ExceptionManager.doError("Datei " + file.getAbsolutePath()
						+ " wurde nicht gefunden", e);
			} catch (SAXException e) {
				ExceptionManager.doError("Parser-Fehler in der Datei "
						+ file.getAbsolutePath() + ".", e);
			} catch (IOException e) {
				ExceptionManager.doError("Lese-Fehler in der Datei "
						+ file.getAbsolutePath() + ".", e);
			}

			List xmlfiles = handler.getFiles();
			Iterator xi = xmlfiles.iterator();
			while (xi.hasNext()) {
				ServerContentsFile scf = (ServerContentsFile) xi.next();

				Folder folder = parseLocation(scf.getMathLocation(), m4uDoc
						.getFolders());
				FileStatusInterface fs = new Remote();
				fs.setStatus(scf.getUri());
				fs.setKeyWords(scf.getKeywords());

				EIssue issue = new EIssue(fs);
				issue.setVersion(scf.getXmlVersion());
				issue.setAuthor(scf.getAuthor()[0]);
				if (">".equals(scf.getSummary()))
					scf.setSummary("");
				issue.setSummary(scf.getSummary());
				issue.setTitle(scf.getTitle());
				folder.addIssue(issue);
			}//while
		}//while

		m4uDoc.sortAll();
	} //analyse

	/** Gibt die Resourcen frei */
	public void clear() {
		m4uDoc = new Math4U2Document();
		/*
		 * Es müssen Nodes des öfteren gelesen werden. Deshalb darf das Dokument
		 * nicht verworfen werden. doc = null;
		 */
	} //clear

	/**
	 * Entfernt ein Issue im Verzeichnis. Bislang nur im Root
	 */
	public void removeIssue(EIssue issue) {
		m4uDoc.getIssues().remove(issue);
	} //removeIssue

	/**
	 * Gibt eine Liste der verfügbaren Themen zurück
	 * 
	 * @return Einen ArrayList-Behälter der alle verfügbaren Themen speichert
	 */
	public Math4U2Document getDocument() throws ParseException {
		if (doc == null)
			return m4uDoc;
		Element root = doc.getDocumentElement();
		NodeList elements = root.getChildNodes();

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				if (n.getNodeName().equals("head"))
					parseHeader(m4uDoc, n);
				else if (n.getNodeName().equals("body"))
					parseBody(m4uDoc, n);
				else
					throw new ParseException(
							"Ungültiges Tag <"
									+ n.getNodeName()
									+ "> gefunden. Es wird <head> oder <body> erwartet.");
			} //if Node.ELEMENT_NODE
		} //for i
		return m4uDoc;
	} //getDocument

	public EIssue getIssue() throws ParseException {
		Element root = doc.getDocumentElement();
		NodeList elements = root.getChildNodes();
		blockNr = 0;

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				if (n.getNodeName().equals("head"))
					parseHeader(m4uDoc, n);
				else if (n.getNodeName().equals("body")) {
					return parseBody(m4uDoc, n);
				} //else
				else
					throw new ParseException(
							"Ungültiges Tag <"
									+ n.getNodeName()
									+ "> gefunden. Es wird <head> oder <body> erwartet.");
			} //if Node.ELEMENT_NODE
		} //for i
		return null;
	} //getIssue

	/**
	 * Analysiert ein <header>-Element.
	 * 
	 * @param m4uDoc
	 *            Das Math4U2-Dokument in das die Daten eingetragen werden
	 *            sollen.
	 * @param head
	 *            Das Node-Objekt das analysiert werden soll
	 */
	private void parseHeader(Math4U2Document m4uDoc, Node head)
			throws ParseException {
		final NodeList elements = head.getChildNodes();
		final Map metaTags = new HashMap();

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);

			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				if (name.equals("meta")) {
					NamedNodeMap attrs = n.getAttributes();
					if (attrs.getLength() > 0) {
						String key = null, val = null;
						Node nameAttr = attrs.getNamedItem("name");
						if (nameAttr != null)
							key = nameAttr.getNodeValue();
						Node contentAttr = attrs.getNamedItem("content");
						if (contentAttr != null)
							val = contentAttr.getNodeValue();

						if (key != null && val != null)
							metaTags.put(key, val);
					} //if
				} else if (name.equals("module")) {
					NamedNodeMap attrs = n.getAttributes();
					if (attrs.getLength() > 0) {
						String modId = null, modClass = null;
						Node idAttr = attrs.getNamedItem("id");
						if (idAttr != null)
							modId = idAttr.getNodeValue();
						Node classAttr = attrs.getNamedItem("class");
						if (classAttr != null)
							modClass = classAttr.getNodeValue();
						if (modId != null && modClass != null) {
							ModuleRegistry.getInstance().bind(modId, modClass);
						} //if
					} //if
				} else if (name.equals("author")) {
				} else if (name.equals("version")) {
				} else if (name
						.equals("Lektion_nur_in_math4u2_Version_2.1_ausfuehrbar")) {
				} else if (name
						.equals("Lektion_nur_in_math4u2_Version_2.2_ausfuehrbar")) {					
				} else if (name.equals("define")) {	
					parseDefineTag(n);
				} else
					throw new ParseException(
							"Ungültiges Tag <"
									+ n.getNodeName()
									+ "> gefunden. Es wird <meta>, <module>, <version> oder <author> erwartet.");
			} //if
		}

		if (!metaTags.isEmpty())
			m4uDoc.setMetaTags(metaTags);
	} //parseHead
	
	/**
	 * Analysiert ein <define>-Element. Hier können neue Funktionen 
	 * und Graphen definiert werden.
	 * @param n XML-Knoten
	 * @throws ParseException
	 */
	private void parseDefineTag(Node n) throws ParseException{
		final NodeList elements = n.getChildNodes();
		for (int i = 0; i < elements.getLength(); i++) {
			Node child = elements.item(i);
			if(child.getNodeType() != Node.ELEMENT_NODE) continue;
			String name = child.getNodeName();
			if("function".equals(name)){
				parseFunctionDefinition(child);
			}else if("graph".equals(name)){
				parseFunctionDefinition(child);
			}else{
				throw new ParseException(
						"Ungültiges Tag <"
								+ child.getNodeName()
								+ "> gefunden. Es wird <function> oder <graph> erwartet.");				
			}
		}//for i
	}//parseDefineTag
	
	private static int classCounter = 0;
	
	private void parseFunctionDefinition(Node n) throws ParseException{
		final NodeList elements = n.getChildNodes();
		
		CtClass functionClass=null;
		String funcName=null;		
		String methodStr=null;
		ClassPool.getDefault().clearImportedPackages();
		try{
			for (int i = 0; i < elements.getLength(); i++) {
				Node child = elements.item(i);
				if(child.getNodeType() != Node.ELEMENT_NODE) continue;
				String name = child.getNodeName();
				if("name".equals(name)){
					if(broker.getMathObject(name)!=null){
						ExceptionManager.doError("Funktion/Objekt mit dem Namen "+name+" exisitert bereits.");
						return;
					}
					try {
						ClassPool pool = ClassPool.getDefault();
						funcName = child.getFirstChild().getNodeValue();
						CtClass functionCl2 = pool.get("math4u2.mathematics.functions.UserDefinedFunction");
						pool.importPackage("math4u2.mathematics.functions");
						pool.importPackage("math4u2.mathematics.results");
						pool.importPackage("math4u2.mathematics.types");
						pool.importPackage("math4u2.mathematics.affine");
						pool.importPackage("math4u2.view.graph.drawarea");
						pool.importPackage("java.awt");						
						functionClass = pool.makeClass(funcName+(classCounter++),functionCl2);
						functionClass.detach();
					} catch (NotFoundException e) {
						ExceptionManager.doError(e);
						return;
					}
					methodStr = "public String getName() {return \""+funcName+"\";}";
					buildFunctionMethodDefinition(methodStr, functionClass);
				}else if("method".equals(name)){
					parseFunctionMethodDefinition(child, functionClass);
				}else if("arguments".equals(name)){
					NodeList arguments = child.getChildNodes();
					ArrayList argumentList = new ArrayList();
					for(int j=0; j<arguments.getLength(); j++){
						Node argument = arguments.item(j);		
						if(argument.getNodeType()!=Node.ELEMENT_NODE) continue;
						if(!"argument".equals(argument.getNodeName())){
							throw new ParseException("Ungültiges Tag <"
									+ argument.getNodeName() + "> gefunden. Es wird <argument> erwartet.");						
						}//if
						NamedNodeMap atts = argument.getAttributes();
						argumentList.add(new String[]{atts.getNamedItem("type").getNodeValue(), atts.getNamedItem("name").getNodeValue()});
					}//for j
					
					//Methode getArity
					methodStr = "public int getArity() { return "+argumentList.size()+";}";
					buildFunctionMethodDefinition(methodStr, functionClass);
					
					//Methode getVariableNames
					methodStr = "public String[] getVariableNames() { return new String[]{";
					for (Iterator iter = argumentList.iterator(); iter.hasNext();) {
						String[] varName = (String[]) iter.next();
						methodStr += "\""+varName[1]+"\"";
						if(iter.hasNext()) methodStr+=", ";
					}
					methodStr += "};}";
					if(argumentList.size()==0)
						methodStr = methodStr.replaceFirst("String\\[\\]\\{\\}", "String[0]");
					buildFunctionMethodDefinition(methodStr, functionClass);
					
					//Methode getVariableType
					methodStr = "public Class getVariableType(int pos) throws MathException { \nif";
					for (int j=0; j<argumentList.size(); j++) {
						String[] varName = (String[]) argumentList.get(j);
						String type = varName[0]+".class";
						
						methodStr+="(pos=="+j+"){ return "+type+";}";
						if(j!=argumentList.size()-1) methodStr+="\nelse if";
					}
					methodStr+="\nelse return null;\n}";
					if(argumentList.size()==0)
						methodStr="public Class getVariableType(int pos) throws MathException { return null;}";
					buildFunctionMethodDefinition(methodStr, functionClass);
				}else if("with-graph".equals(name)){
					methodStr = "public boolean hasCurrentObjectGraph() {return true;}";
					buildFunctionMethodDefinition(methodStr, functionClass);	
				}else if("result-type".equals(name)){
					String resultType = child.getFirstChild().getNodeValue();
					methodStr = "public Class getResultType(Class[] argTypes) {return "+resultType+".class;}";
					buildFunctionMethodDefinition(methodStr, functionClass);	
				}else if("import".equals(name)){
					ClassPool.getDefault().importPackage(child.getFirstChild().getNodeValue());		
				}else{
					throw new ParseException(
							"Ungültiges Tag <"
									+ child.getNodeName()
									+ "> gefunden. Es wird <name>, <import>, <arguments>, <result-type>, <with-graph> oder <method> erwartet.");				
				}
			}//for i
		} catch(CannotCompileException e){
			return;
		}
		try {
			Object functObj = functionClass.toClass().newInstance();
			if(broker.getMathObject(funcName)!=null)
				broker.deleteObjectByKey(funcName);
			broker.publishObject((Function)functObj,funcName);			
		} catch (InstantiationException e) {
			ExceptionManager.doError(e);
		} catch (IllegalAccessException e) {
			ExceptionManager.doError(e);
		} catch (CannotCompileException e) {
			ExceptionManager.doError(e);
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError(e);
		} catch (BrokerException e) {
			ExceptionManager.doError(e);
		}
	}//parseFunctionDefinition
	
	private void parseFunctionMethodDefinition(Node n, CtClass theClass) throws CannotCompileException{
		NodeList elements = n.getChildNodes();
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<elements.getLength(); i++){
			Node child = elements.item(i);
			if(child.getNodeType()==Node.TEXT_NODE || child.getNodeType()==Node.CDATA_SECTION_NODE){
				buffer.append(child.getNodeValue());
			}
		}//for i
		try{
			buildFunctionMethodDefinition(buffer.toString(), theClass);
		}catch(CannotCompileException e){
			ExceptionManager.doError("Methode:\n"+buffer.toString()+"\n"+e.getMessage(), "Fehler bei selbstdefinierten Funktion");
			throw e;
		}
	}//parseFunctionMethodDefinition
	
	public void buildFunctionMethodDefinition(String methodStr, CtClass theClass)
		throws CannotCompileException{

		CtMethod method = null;
		try{
			method = CtMethod.make(methodStr, theClass);
		}catch(CannotCompileException e){
			ExceptionManager.doError("Fehler bei der Methode:\n"+methodStr+"\n"+e.getMessage(),"Fehler in Funktionsdefinition");
			return;
		}
			
		CtMethod oldMethod = null;
		try {
			oldMethod = theClass.getDeclaredMethod(method.getName());
		} catch (NotFoundException e) {
			//Do nothing
		}
		
		if(oldMethod!=null){
			try {
				theClass.removeMethod(oldMethod);
			} catch (NotFoundException e) {
				ExceptionManager.doError(e);
			}
		}
		theClass.addMethod(method);	
	}//buildFunctionMethodDefinition

	/**
	 * Analysiert ein <body>-Element.(Hilfsmethode
	 * 
	 * @param m4uDoc
	 *            Das Math4U2-Dokument in das die Daten eingetragen werden
	 *            sollen.
	 * @param body
	 *            Das Node-Objekt das analysiert werden soll
	 */
	private EIssue parseBody(Math4U2Document m4uDoc, Node body)
			throws ParseException {
		List[] la = _parseBody(body);
		if (m4uDoc != null) {
			List i = m4uDoc.getIssues();
			List f = m4uDoc.getFolders();
			i.addAll(la[0]);
			f.addAll(la[1]);
			m4uDoc.setIssues(i);
			m4uDoc.setFolders(f);
		} //if
		return (EIssue) la[0].get(0);
	} //parseBody

	/**
	 * Analysiert ein <body>-Element.(Hilfsmethode)
	 * 
	 * @param body
	 *            Das Node-Objekt das analysiert werden soll
	 */
	private List[] _parseBody(Node body) throws ParseException {
		final NodeList elements = body.getChildNodes();
		List issues = new LinkedList();
		List folders = new LinkedList();
		List exercises = new LinkedList();
		List steps = new LinkedList();
		EIssue issue = null;
		Folder folder = null;
		String title = null;
		boolean firstStep = true;

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);

			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				if (name.equals("location")) {
					//					folder=parseLocation(n, m4uDoc.getFolders());
				} else if (name.equals("title")) {
					title = n.getFirstChild().getNodeValue();
				} else if (name.equals("step")) {
					steps.add(parseStep(n, firstStep));
					firstStep = false;
				} else if (name.equals("exercise")) {
					exercises.add(parseExercise(n));
				} else if (name.equals("summary")) {
				} else if (name.equals("keywords")) {
				} else if (name.equals("author")) {
				} else
					throw new ParseException(
							"Ungültiges Tag <"
									+ n.getNodeName()
									+ "> gefunden. Es wird <location>, <title>, <step>, <keywords> oder <exercise> erwartet.");
			} //if ELEMENT_NODE
		} //for i
		issue = new EIssue(steps, exercises);
		issue.setTitle(title);

		if (folder == null)
			issues.add(issue);
		else
			folder.getIssues().add(issue);
		List[] la = new List[2];
		la[0] = issues;
		la[1] = folders;

		return la;
	} //parseBody

	/**
	 * parst den Ablegeort return Folder Ordner des Ablegeorts
	 */
	public Folder parseLocation(String location, List folders) {
		if (location == null)
			return null;
		location = location.trim();
		//location formatieren
		location = location.replace('\\', '/');
		if (location.length() != 0 && location.charAt(0) == '/')
			location = location.substring(1);
		if (location.length() != 0
				&& location.charAt(location.length() - 1) == '/')
			location = location.substring(0, location.length() - 1);
		Folder f = createLocationFolder(location, folders);
		return f;
	} //parseLocation

	/**
	 * Analysiert ein <seq>-Node-Objekt.
	 * 
	 * @param n
	 *            Das Node-Objekt das analysiert werden soll
	 * @return Ein Behälter mit den gefundenen Aktions-Aufrufen
	 */
	private EActionContainer parseActions(Node node, EActionContainer parent)
			throws ParseException {
		NodeList elements = node.getChildNodes();

		EActionContainer actions = null;

		boolean isParallel = node.getNodeName().equals("par");
		if (isParallel) {
			actions = new EActionContainerPar(parent, new LinkedList());
		} else {
			actions = new EActionContainerSeq(parent, new LinkedList());
		}//else

		//NamedNodeMap attrs = node.getAttributes();
		//		if (attrs.getLength() > 0) {
		//			Node repeatAttr = attrs.getNamedItem("repeat");
		//			if (repeatAttr != null) {
		//				int repeatCount = 1;
		//				repeatCount = Integer.parseInt(repeatAttr.getNodeValue());
		//				actions.setRepeat(repeatCount);
		//			} //if
		//		} //if

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				if (name.equals("seq") || name.equals("par")) {
					// Rekursion!!!
					actions.add(parseActions(n, actions));
				} //if seq || par
				else if (name.equals("call")) {
					actions.add(parseActionCall(n));
				} //if call
				else if (name.equals("animate")) {
					actions.add(parseAnimateCall(n));
				} else if (name.equals("script")) {
					actions.addAll(parseScript(n));
				} //if script
				else
					throw new ParseException(
							"Ungültiges Tag <"
									+ n.getNodeName()
									+ "> gefunden. Es wird <par>, <seq>, <call> oder <animate> erwartet.");
			} //if
		} //for

		return actions;
	} //parseActions

	private List parseScript(Node n) {
		blockNr++;
		if (n.getChildNodes().item(0) == null)
			return new LinkedList();
		StringBuffer sb = new StringBuffer();
		readScriptChild(sb, n);
		String scriptStr = sb.toString().trim();

		scriptStr = DOMWriter.nodeToString(n);
		if (scriptStr.startsWith("<script>"))
			scriptStr = scriptStr.substring("<script>".length());
		if (scriptStr.endsWith("</script>"))
			scriptStr = scriptStr.substring(0, scriptStr.length()
					- "</script>".length());
		if (scriptStr.length() == 0)
			return new LinkedList();
		try {
			KurzSchrift scriptParser = new KurzSchrift(new StringReader(
					scriptStr));
			scriptParser.setBroker(broker);
			return scriptParser.parseScript();
		} catch (math4u2.util.exception.parser.script.ParseException e) {
			e.printStackTrace();

			String fileName2 = fileName
					.substring(fileName.lastIndexOf('\\') + 1);
			ExceptionManager.doError(e, scriptStr, fileName2, blockNr);
		} catch (Throwable t) {
			String fileName2 = fileName
					.substring(fileName.lastIndexOf('\\') + 1);
			ExceptionManager.doError(("Fehler im " + blockNr
					+ ". <script>-Tag in der Datei " + fileName2 + "."), t);
		}
		return new LinkedList();
	} //parseScript

	private void readScriptChild(StringBuffer sb, Node n) {
		NodeList nodeList = n.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				sb.append(child.toString());
			} else if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType()==Node.CDATA_SECTION_NODE) {
				sb.append(child.getNodeValue());
			} else {
				ExceptionManager.doError("Script-Inhalt nicht erkannt");
			}
		}//for i
	}//readScriptChild

	/**
	 * Analysiert ein <call>-Node-Objekt.
	 * 
	 * @param node
	 *            Das Node-Objekt das analysiert werden soll
	 * @return Ein neues EActionItem-Objekt, das die gefundenen Attribute
	 *         besitzt
	 */
	private EActionCall parseActionCall(Node node) throws ParseException {
		NodeList elements = node.getChildNodes();

		String obj = null, method = null;
		float begin = 0f, end = Float.NaN;
		final LinkedList params = new LinkedList();

		NamedNodeMap attrs = node.getAttributes();
		if (attrs.getLength() > 0) {
			Node objAttr = attrs.getNamedItem("object");
			if (objAttr != null)
				obj = objAttr.getNodeValue();
			Node methodAttr = attrs.getNamedItem("action");
			if (methodAttr != null)
				method = methodAttr.getNodeValue();
			Node begAttr = attrs.getNamedItem("begin");
			if (begAttr != null)
				begin = Float.parseFloat(begAttr.getNodeValue());
			Node endAttr = attrs.getNamedItem("end");
			if (endAttr != null)
				end = Float.parseFloat(endAttr.getNodeValue());
		} //if

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				if (name.equals("param")) {
					NamedNodeMap pattrs = n.getAttributes();
					if (pattrs.getLength() > 0) {
						String val = null, type = null;
						Node valAttr = pattrs.getNamedItem("value");
						if (valAttr == null)
							throw new ParseException(
									"Es fehlt das Attribut 'value' im Tag <param>");
						val = valAttr.getNodeValue();
						Node typeAttr = pattrs.getNamedItem("type");
						if (typeAttr == null)
							throw new ParseException(
									"Es fehlt das Attribut 'type' im Tag <param>");
						type = typeAttr.getNodeValue();
						if ((val != null) && (type != null))
							params.add(new EActionParam(val, type));
					} else
						throw new ParseException(
								"Es fehleen die Attribute 'type' und 'value' im Tag <param>");
				} else
					throw new ParseException("Ungültiges Tag <"
							+ n.getNodeName()
							+ "> gefunden. Es wird <param> erwartet.");
			} //if
		} //for i
		EActionCall actionCall = new EActionCall(obj, method, params);
		actionCall.setBegin(begin);
		actionCall.setEnd(end);
		return actionCall;
	} //parseActionCall

	/**
	 * Analysiert ein <animate>-Node-Objekt.
	 * 
	 * @param node
	 *            Das Node-Objekt das analysiert werden soll
	 * @return Ein neues EAnimateAction-Objekt, das die gefundenen Attribute
	 *         besitzt
	 */
	private EActionAnimation parseAnimateCall(Node node) {
		String obj = null, getMethod = "getParameter", setMethod = "setParameter", attribName = null, timeFunction = null;
		float begin = 0f, end = Float.NaN, from = Float.NaN, to = Float.NaN;

		NamedNodeMap attrs = node.getAttributes();
		if (attrs.getLength() > 0) {
			Node objAttr = attrs.getNamedItem("object");
			if (objAttr != null)
				obj = objAttr.getNodeValue();

			Node getMethodAttr = attrs.getNamedItem("getAction");
			if (getMethodAttr != null)
				getMethod = getMethodAttr.getNodeValue();

			Node setMethodAttr = attrs.getNamedItem("setAction");
			if (setMethodAttr != null)
				setMethod = setMethodAttr.getNodeValue();

			Node attribNameAttr = attrs.getNamedItem("parameterName");
			if (attribNameAttr != null)
				attribName = attribNameAttr.getNodeValue();

			Node tfAttr = attrs.getNamedItem("timeFunction");
			if (tfAttr != null)
				timeFunction = tfAttr.getNodeValue();

			Node fromAttr = attrs.getNamedItem("from");
			if (fromAttr != null)
				from = Float.parseFloat(fromAttr.getNodeValue());

			Node toAttr = attrs.getNamedItem("to");
			if (toAttr != null)
				to = Float.parseFloat(toAttr.getNodeValue());

			Node begAttr = attrs.getNamedItem("begin");
			if (begAttr != null)
				begin = Float.parseFloat(begAttr.getNodeValue());

			Node endAttr = attrs.getNamedItem("end");
			if (endAttr != null)
				end = Float.parseFloat(endAttr.getNodeValue());
		}
		EActionAnimation actionCall = new EActionAnimation(obj, getMethod,
				setMethod, attribName, timeFunction, broker);
		actionCall.setBegin(begin);
		actionCall.setEnd(end);
		actionCall.setFrom(from);
		actionCall.setTo(to);
		return actionCall;
	} //parseAnimateCall

	/**
	 * Erzeugt die Ordnerstruktur von Location, wenn nicht vorhanden und gibt
	 * den Ordner von Location zurück
	 * 
	 * @param location
	 *            Name des Pfads, wo der Ordner sich befinden soll
	 * @return Ordner, der auf Location zeigt
	 */
	private Folder createLocationFolder(String location, List folders) {
		Iterator iterator = folders.iterator();
		int index = location.indexOf('/');
		String firstFolderStr = location;
		String restStr = location;

		if (index != -1) {
			firstFolderStr = location.substring(0, index).trim();
			restStr = location.substring(index + 1, location.length()).trim();
		} //if index!=-1

		Folder f = null;
		while (iterator.hasNext()) {
			f = (Folder) iterator.next();
			if (f.getName().equals(firstFolderStr)) {
				//Ordner existiert bereits
				if (index == -1)
					return f;
				else
					return createLocationFolder(restStr, f.getFolders());
			} //if
		} //while hasNext
		f = new Folder(firstFolderStr);
		folders.add(f);
		if (index == -1)
			return f;
		else
			return createLocationFolder(restStr, f.getFolders());
	} //createLocationFolder

	/**
	 * Analysiert ein Node-Objekt und erzeugt daraus ein neues EIssue-Objekt.
	 * 
	 * @param issue
	 *            Das Node-Objekt das analysiert werden soll
	 * @return Ein neues EIssue-Objekt, das die gefundenen Attribute besitzt
	 */
	private Step parseStep(Node issue, boolean firstStep) throws ParseException {
		final NodeList elements = issue.getChildNodes();
		StyledText title = null;
		Node descriptionNode = null;
		StyledText summary = null;
		final EActionContainer actions = new EActionContainerSeq();

		PercentLayout layout = null;
		if (firstStep)
			layout = new PercentLayout(new double[][] { { 1.0 } });
		String[] drawAreaNames = { "da" };

		//zuerst calls
		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				if (name.equals("seq") || name.equals("par"))
					actions.add(parseActions(n, actions));
			} //if
		} //for i

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);

			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				if (name.equals("title")) {
					title = parseText(n);
				} else if (name.equals("description")) {
					descriptionNode = n;
				} else if (name.equals("layout")) {
					LayoutScheme ls = parseLayout(n);
					layout = ls.layout;
					drawAreaNames = ls.drawAreaNames;
				} else if (name.equals("seq") || name.equals("par")) {
				} else
					throw new ParseException(
							"Ungültiges Tag <"
									+ n.getNodeName()
									+ "> gefunden. Es wird <title>,<description> oder <layout> erwartet.");
			} //if ELEMETN_NODE
		} //for i

		Step step = new Step(title, descriptionNode, summary, layout,
				drawAreaNames, broker, this);
		step.setActions(actions);
		return step;
	} //parseStep

	/**
	 * Analysiert ein Node-Objekt und erzeugt daraus ein neues Exercise-Objekt.
	 * 
	 * @param exercise
	 *            Das Node-Objekt das analysiert werden soll
	 * @return Ein neues Exercise-Objekt, das die gefundenen Attribute besitzt
	 */
	private Exercise parseExercise(Node exercise) throws ParseException {
		final NodeList elements = exercise.getChildNodes();
		StyledText title = null;
		Node descriptionNode = null;
		StyledText summary = null;
		final List elems = new LinkedList();
		final EActionContainer actions = new EActionContainerSeq();

		PercentLayout layout = new PercentLayout(new double[][] { { 1.0 } });
		String[] drawAreaNames = { "da" };

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				if (name.equals("seq") || name.equals("par")) {
					actions.add(parseActions(n, actions));
				} // if seq || par
			} //if
		} //for

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				if (name.equals("title")) {
					title = parseText(n);
				} else if (name.equals("description")) {
					descriptionNode = n;
				} else if (name.equals("summary")) {
					summary = parseText(n);
				} else if (name.equals("choice")) {
					elems.add(parseChoice(n));
				} else if (name.equals("selection")) {
					elems.add(parseSelection(n));
				} else if (name.equals("seq") || name.equals("par")) {
				} else
					throw new ParseException("Ungültiges Tag <"
							+ n.getNodeName() + "> in <exercise> gefunden.");
			} //if
		} //for

		Exercise e = new Exercise(title, descriptionNode, summary, elems,
				layout, drawAreaNames, broker, this);
		e.setActions(actions);
		return e;
	} //parseExercise

	/**
	 * Analysiert ein Node-Objekt und erzeugt daraus ein neues Choice-Objekt.
	 * 
	 * @param choice
	 *            Das Node-Objekt das analysiert werden soll
	 * @return Ein neues Choice-Objekt, das die gefundenen Attribute besitzt
	 */
	private EChoice parseChoice(Node choice) throws ParseException {
		NodeList elements = choice.getChildNodes();
		Node description = null;
		Node explanation = null;
		boolean solution = false;

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				if (name.equals("statement")) {
					description = n;
				} else if (name.equals("explanation")) {
					explanation = n;
				} else if (name.equals("solution")) {
					solution = parseBoolean(n.getFirstChild().getNodeValue());
				} else
					throw new ParseException(
							"Ungültiges Tag <"
									+ n.getNodeName()
									+ "> in <choice> gefunden. Es wird <statement>, <explanation> oder <solution> erwartet.");
			} //if
		} //for
		return new EChoice(description, solution, explanation, this);
	} //parseChoice

	/**
	 * Analysiert ein Node-Objekt und erzeugt daraus ein neues
	 * EMultipleChoice-Objekt.
	 * 
	 * @param selection
	 *            Das Node-Objekt das analysiert werden soll
	 * @return Ein neues EMultipleChoice-Objekt, das die gefundenen Attribute
	 *         besitzt
	 */
	private EMultipleChoice parseSelection(Node selection)
			throws ParseException {
		NodeList elements = selection.getChildNodes();
		Node descriptionNode = null;
		boolean mutual = false;
		int rights = 0;
		List items = new LinkedList();

		NamedNodeMap attrs = selection.getAttributes();
		if (attrs.getLength() > 0) {
			Node mutualAttr = attrs.getNamedItem("mutual");
			if (mutualAttr != null)
				mutual = parseBoolean(mutualAttr.getNodeValue());
		}

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				if (name.equals("choice")) {
					EChoice choice = parseChoice(n);
					if (choice.getSolution())
						rights++;
					if (mutual && (rights > 1))
						throw new ParseException(
								"Das Tag wurde mutual erzeugt (d.h. nur eine Frage ist richtig)."
										+ "In \n"
										+ n
										+ "\nkam das zweite mal ein <solution>true</solution> vor");
					items.add(choice);
				} else if (name.equals("description")) {
					descriptionNode = n;
				} else
					throw new ParseException(
							"Ungültiges Tag <"
									+ n.getNodeName()
									+ "> in <selection> gefunden. Es wird <choice> oder <description> erwartet.");
			} //if
		} //for i
		if (mutual && (rights == 0))
			throw new IllegalArgumentException(
					"Das Tag solution wurde mutual erzeugt, d.h. es muß mindestens eine Frage richtig sein");
		return new EMultipleChoice(descriptionNode, items, mutual, this);
	} //parseSelection
	
	private void addChilds(Node n, StyledText paragraph, String type){
		NodeList list = n.getChildNodes();
		for(int i=0; i<list.getLength(); i++){
			Node child = list.item(i);
			if (child != null && child.getNodeValue()!=null)
				paragraph.add(child.getNodeValue(), type);
		}//for i
	}//addChilds

	/**
	 * Analysiert ein Node-Objekt und erzeugt daraus einen formatierten Text.
	 * 
	 * @param textNode
	 *            Das Node-Objekt das analysiert werden soll
	 * @param inParagraph
	 *            Zeigt an, ob die Analyse bereits in einem Absatz stattfindet
	 * @param oneStepActipe
	 *            bei true werden Attributfelder inaktiv geschalten sonst nicht
	 * @return Eine neue formattierte Zeichenkette
	 */
	private StyledText parseText2(Node textNode, boolean inParagraph,
			boolean oneStepActive) throws ParseException {
		StyledText text = new StyledText();
		StyledText paragraph = new StyledText();
		NodeList elements = textNode.getChildNodes();

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				if (name.equals("p")) {
					if (!paragraph.isEmpty()) {
						if (!text.isEmpty() && (!inParagraph))
							text.add(PARA_CHAR);
						text.add(paragraph);
					} //if
					paragraph = parseText2(n, true, oneStepActive);
				} else if (name.equals("br")) {
					//paragraph.add("_"+brChar+"_","par");
					paragraph.add(BR_CHAR);
				} else if (name.equals("tab")) {
					paragraph.add("\t");
				} else if (name.equals("em")) {
					addChilds(n, paragraph, "emphasized");
				} else if (name.equals("strong")) {
					addChilds(n, paragraph, "strong");
				} else if (name.equals("code")) {
					addChilds(n, paragraph, "code");
				} else if (name.equals("command")) {
					addChilds(n, paragraph, "command");
				} else if (name.equals("title2")) {
					if (n.getFirstChild() != null) {
						LinkedList list = paragraph.getComponents();
						if (list.size() != 0) {
							Object last = list.getLast();
							if (last != BR_CHAR && last != PARA_CHAR) {
								paragraph.add(BR_CHAR);
							}//if
						}//if
						addChilds(n, paragraph, "title2");
						paragraph.add(BR_CHAR);
					}//if
				} else if (name.equals("ul")) {
					parseUnorderedList(paragraph, n, oneStepActive);
				} else if (name.equals("img")) {
					AtomicBox ab = parseImage(n);
					paragraph.add(ab);
				} else if (name.equals("table")) {
					AtomicBox ab = parseTable(n, oneStepActive);
					paragraph.add(ab);
				} else if (name.equals("f")) {
					AtomicBox ab = parseMath4u2(n, SHOW_NORMAL, oneStepActive);
					if (ab != null)
						paragraph.add(ab);
				} else if (name.equals("fd")) {
					AtomicBox ab = parseMath4u2(n, SHOW_DEFINITION,
							oneStepActive);
					if (ab != null)
						paragraph.add(ab);
				} else if (name.equals("fb")) {
					AtomicBox ab = parseMath4u2(n, SHOW_BODY, oneStepActive);
					if (ab != null)
						paragraph.add(ab);
				} else if (name.equals("fh")) {
					AtomicBox ab = parseMath4u2(n, SHOW_SHORT, oneStepActive);
					if (ab != null)
						paragraph.add(ab);
				} else if (name.equals("fa")) {
					AtomicBox ab = parseMath4u2(n, SHOW_ACTUAL, oneStepActive);
					if (ab != null)
						paragraph.add(ab);
				} else if (name.equals("formula")) {
					NamedNodeMap attrs = n.getAttributes();
					if (attrs.getLength() > 0) {
						Node typeAttr = attrs.getNamedItem("type");
						if (typeAttr != null) {
							String type = typeAttr.getNodeValue().toLowerCase();
							if (type.equals("math4u2")) {
								//show : actual, formal, short
								String show = "";
								typeAttr = attrs.getNamedItem("show");
								if (typeAttr != null) {
									show = typeAttr.getNodeValue()
											.toLowerCase();
								}

								AtomicBox ab = parseMath4u2(n, show,
										oneStepActive);
								if (ab != null)
									paragraph.add(ab);
							} //math4u2
						} //typeAttr!=null
						else {
							//show : actual, formal, short
							String show = "";
							typeAttr = attrs.getNamedItem("show");
							if (typeAttr != null) {
								show = typeAttr.getNodeValue().toLowerCase();
							}

							AtomicBox ab = parseMath4u2(n, show, oneStepActive);
							if (ab != null)
								paragraph.add(ab);
						} //typeAttr==null
					} //attr.length!=0
					else {
						AtomicBox ab = parseMath4u2(n, SHOW_NORMAL,
								oneStepActive);
						if (ab != null)
							paragraph.add(ab);
					} //attr.length==0
				} else
					throw new ParseException("Ungültiges Tag <"
							+ n.getNodeName() + "> gefunden.");
			} else if (n.getNodeType() == Node.TEXT_NODE || n.getNodeType() == Node.CDATA_SECTION_NODE) {
				String trimmedText = n.getNodeValue().replaceAll("\\s+", " ");
				if (paragraph.isEmpty())
					trimmedText = trimmedText.replaceAll("^\\s+", "");
				paragraph.add(trimmedText);
			} else if (n.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
				throw new NotImplementedException();
			}
		}

		if (!paragraph.isEmpty()) {
			if (!text.isEmpty() && (!inParagraph))
				text.add(PARA_CHAR);
			text.add(paragraph);
		}
		return text;
	} //parseText

	/**
	 * Parst eine Formeldefinition, die Interaktiv verändert werden kann. z.B.
	 * <fd>f(x) </fd>
	 */
	private AtomicBox parseFormularDefinitionVersion2_0(Node n,
			boolean oneStepActive) {
		String def = n.getFirstChild().getNodeValue();
		String header = def;
		int pos = def.indexOf(":=");
		if (pos != -1) {
			header = def.substring(0, pos);
			def = header;
		}
		pos = def.indexOf("(");
		if (pos != -1)
			def = def.substring(0, pos);

		MathObject mo = null;
		try {
			mo = broker.getObject(def);
		} catch (BrokerException e) {
			ExceptionManager.doError("Objekt nicht gefunden aus "
					+ n.getFirstChild().getNodeValue() + "\n"
					+ DOMWriter.nodeToString(n));
			return null;
		}

		final DefinitionField df = new DefinitionField(((UserFunction) mo)
				.getDefinitionHeader(), mo, broker);
		FormulaRenderContext frc = new FormulaRenderContext();

		AtomicBox ab = parseFormulaVersion2_0(header, new HashMap(), broker,
				oneStepActive);
		BinBox bb = new BinBox(frc, ":=");
		ab.add(bb);

		JTextField tf = df.getInput();
		Node colNode = n.getAttributes().getNamedItem("width");

		int cols = 5;
		if (colNode != null) {
			String col = colNode.getNodeValue();
			cols = Integer.parseInt(col);
		}
		tf.setColumns(cols);
		tf.setFont(FormulaRenderContext.getFont("Dialog", Font.PLAIN, 16));

		MathObjectWrapper wrapper = new MathObjectWrapper(df);
		df.getInput().addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				df.getInput().postActionEvent();
			}
		});

		//Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[1];
		ra[0] = RelationFactory.getView_Function_Relation();
		List parts = new LinkedList();
		parts.add(wrapper);
		List relations = Arrays.asList(ra);

		try {
			//Definieren lassen
			broker.defineRelations(mo, parts, relations, Broker.SECOND_OBJECT);
		} catch (BrokerException e) {
			ExceptionManager.doError(
					"Fehler beim Erstellen der Beziehungen von " + mo.getIdentifier(),
					e);
		}
		Step.addParameterBoxesToDeleteAfterUse(wrapper);

		ab.add(tf);
		ab.setSize(ab.getPreferredSize());

		return ab;
	}//parseFormularDefinitonVersion2_0

	/**
	 * Analysiert ein Node-Objekt mit Tabellen-Inhalt (an HTML-Tabellen
	 * angelehnt) und erzeugt daraus ein neues AtomicBox-Objekt.
	 * 
	 * @param n
	 *            Das Node-Objekt das analysiert werden soll
	 * @return Ein neues AtomicBox-Objekt, das die gefundenen Attribute besitzt
	 * @see math4u2.view.textgraphics.AtomicBox
	 */
	private AtomicBox parseTable(Node node, boolean oneStepActive)
			throws ParseException {
		//Tabelle erzeugen
		FormulaRenderContext frc = new FormulaRenderContext();
		AtomicBox table = new TableBox(frc);
		table.setLayout(new GridBagLayout());
		//Hintergrundfarbe holen
		Color bgColor = null;
		Node typeAttr = node.getAttributes().getNamedItem("bgcolor");
		if (typeAttr != null) {
			//Tabellenfarbe
			String colorStr = typeAttr.getNodeValue();
			bgColor = ColorUtil.parseColor(colorStr);
		} //if
		if (bgColor == null)
			bgColor = java.awt.Color.white;
		Color borderColor = null;
		typeAttr = node.getAttributes().getNamedItem("bordercolor");
		if (typeAttr != null) { //Tabellenfarbe
			String colorStr = typeAttr.getNodeValue();
			borderColor = ColorUtil.parseColor(colorStr);
		} //if

		if (borderColor == null)
			borderColor = Color.BLACK;
		int borderSize = 1;
		typeAttr = node.getAttributes().getNamedItem("border");

		if (typeAttr != null) {
			//Rahmenstärke
			borderSize = Integer.parseInt(typeAttr.getNodeValue());
		} //if
		GridBagConstraints gridBagConstraints1;
		NodeList elements = node.getChildNodes();
		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (!"tr".equals(n.getNodeName()))
				continue;
			NodeList elements2 = n.getChildNodes();
			for (int k = 0; k < elements2.getLength(); k++) {
				Node n2 = elements2.item(k);
				if (!"td".equals(n2.getNodeName()))
					continue;
				//Inhalt
				StyledText st = parseText2(n2, true, oneStepActive);

				Math4u2TextPane tp = new Math4u2TextPane();
				tp.setOpaque(true);
				try {
					st.insertText(tp.getDocument());
				} catch (BadLocationException e) {
					ExceptionManager.doError("Fehler beim Einfügen\n"
							+ DOMWriter.nodeToString(n2), e);
				} //catch

				//Layout - Rahmen
				int borderTop = (i != 1) ? 0 : borderSize;
				int borderLeft = (k != 1) ? 0 : borderSize;
				int borderDown = borderSize;
				int borderRight = borderSize;
				Border b1 = new MatteBorder(borderTop, borderLeft, borderDown,
						borderRight, borderColor);
				Border b2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
				Border b3 = BorderFactory.createCompoundBorder(b1, b2);
				tp.setBorder(b3);
				tp.setEditable(false);

				//Layout
				gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.gridx = k;
				gridBagConstraints1.gridy = i;
				gridBagConstraints1.anchor = GridBagConstraints.WEST;
				gridBagConstraints1.fill = GridBagConstraints.BOTH;

				Color color = null;
				Node typeAttr2 = n2.getAttributes().getNamedItem("bgcolor");
				if (typeAttr2 != null) {
					//Tabellenfarbe
					String colorStr = typeAttr2.getNodeValue();
					color = ColorUtil.parseColor(colorStr);
				} //if
				if (color == null)
					color = bgColor;
				tp.setBackground(color);
				table.add(tp, gridBagConstraints1);
			} //for td
		} //for tr
		TableBox tb = new TableBox(frc);
		tb.add(table);
		tb.setLayout(new FormulaLayout());
		tb.setRenderContext(frc);
		return tb;
	} //parseTable

	/**
	 * Analysiert ein Node-Objekt mit Bild-Inhalt und erzeugt daraus ein neues
	 * AtomicBox-Objekt.
	 * 
	 * @param n
	 *            Das Node-Objekt das analysiert werden soll
	 * @return Ein neues AtomicBox-Objekt, das die gefundenen Attribute besitzt
	 * @see math4u2.view.textgraphics.AtomicBox
	 */
	private AtomicBox parseImage(Node n) throws ParseException {
		NamedNodeMap attrs = n.getAttributes();
		if (attrs.getLength() == 0)
			return null;
		//Source
		String sourceStr = "";
		Node typeAttr = attrs.getNamedItem("src");
		if (typeAttr == null)
			throw new ParseException(
					"Das Bild muß das Attribut \"src\" enthalten ("
							+ n.toString() + ")");
		sourceStr = typeAttr.getNodeValue();
		ImageBox view = null;
		try {
			view = new ImageBox(sourceStr);
		} catch (FileNotFoundException e) {
			ExceptionManager.doError("Datei wurde nicht gefunden(" + sourceStr
					+ ")\n" + DOMWriter.nodeToString(n), e);
		}
		view.setRenderContext(new FormulaRenderContext());
		TableBox tb = new TableBox(new FormulaRenderContext());
		tb.setLayout(new FormulaLayout());
		tb.add(view);
		return tb;
	} //parseImage

	private void parseUnorderedList(StyledText paragraph, Node node,
			boolean oneStepActive) throws ParseException {
		NodeList elements = node.getChildNodes();
		paragraph.add("\n", "p", 0);
		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (!"li".equals(n.getNodeName()))
				continue;
			paragraph.add(Unicode.AUFZAEHLUNG2[0] + " ", "strong", 20);
			paragraph.add(parseText2(n, true, oneStepActive), 30);
			paragraph.add("\n", "p", 30);
		} //for
	} //parseUnorderedList

	/**
	 * Analysiert ein Node-Objekt mit Math4u2-Inhalt und erzeugt daraus ein
	 * neues AtomicBox-Objekt.
	 * 
	 * @param n
	 *            Das Node-Objekt das analysiert werden soll
	 * 
	 * @return Ein neues AtomicBox-Objekt, das die gefundenen Attribute besitzt
	 * @see math4u2.view.textgraphics.AtomicBox
	 */
	private AtomicBox parseMath4u2(Node n, String show, boolean oneStepActive)
			throws ParseException {
		String currentVersion = n.getOwnerDocument().getDocumentElement()
				.getAttribute("version");
		AtomicBox ab;
		if (currentVersion.equals("2.0"))
			ab = parseMath4u2Version2_0(n, show, oneStepActive);
		else
			ab = parseMath4u2Version2_1(n, show, oneStepActive);
		if (ab == null) {
			ExceptionManager.doError(DOMWriter.nodeToString(n),
					"Fehler beim Erzeugen der Formel");
		}
		return ab;
	} //parseMath4u2

	private AtomicBox parseMath4u2Version2_1(Node n, String show,
			boolean oneStepActive) {
		Node child = n.getFirstChild();
		while (child.getNodeType() == Node.TEXT_NODE
				&& child.getNodeValue().trim().equals("")) {
			child = child.getNextSibling();
		}

		if (!child.getNodeName().equals("conf")
				&& !(child.getNodeType() == Node.TEXT_NODE)) {
			ExceptionManager.doError("Parse-Fehler", "Das Element "
					+ child.getNodeName() + " is ungültig.\n"
					+ DOMWriter.nodeToString(n));
			return null;
		}//if

		Node formulaNode = null;
		if (child.getNodeType() == Node.TEXT_NODE) {
			formulaNode = child;
		} else {
			formulaNode = child.getNextSibling();
		}//else

		List list = null;
		try {
			if (child.getNodeName().equals("conf")) {
				String nodeValue = child.getFirstChild().getNodeValue();
				KurzSchrift scriptParser = new KurzSchrift(new StringReader(
						nodeValue + ";"));
				scriptParser.setBroker(broker);
				list = scriptParser.parseScriptWithoutActionComponents();
			}//if
		} catch (DOMException e) {
			ExceptionManager.doError("Fehler beim Auswerten einer Formel.\n"
					+ DOMWriter.nodeToString(child), e);
			return null;
		} catch (math4u2.util.exception.parser.script.ParseException e) {
			ExceptionManager.doError("Fehler beim Lesen einer Formel.\n"
					+ DOMWriter.nodeToString(child), e);
			return null;
		}

		Map map = new HashMap();
		if (list != null) {
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Object[] oa = (Object[]) iter.next();
				//methodennamem, hashmap
				map.put(oa[1], oa[2]);
			}//for iter
		}//if

		//Hintergrundfarbe
		NamedNodeMap attrs = n.getAttributes();
		if (attrs.getLength() > 0) {
			//Hintergrundfarbe
			Node typeAttr = attrs.getNamedItem("bgcolor");
			if (typeAttr != null) {
				Color c = ColorUtil.parseColor(typeAttr.getNodeValue());
				map.put("bgColor", c);
			}
		} //attrs.length>0

		String valueStr = formulaNode.getNodeValue().trim();
		return parseFormulaVersion2_1(valueStr, map, broker, oneStepActive);
	}//parseMath4u2Version2_1

	private AtomicBox parseMath4u2Version2_0(Node n, String show,
			boolean oneStepActive) {
		if (show.equals(SHOW_DEFINITION)) {
			return parseFormularDefinitionVersion2_0(n, oneStepActive);
		}//if

		Node child = n.getFirstChild();

		while (child.getNodeType() == Node.TEXT_NODE
				&& child.getNodeValue().trim().equals("")) {
			child = child.getNextSibling();
		}//while

		if (!child.getNodeName().equals("conf")
				&& !(child.getNodeType() == Node.TEXT_NODE)) {
			ExceptionManager.doError("Parse-Fehler", "Das Element "
					+ child.getNodeName() + " is ungültig.\n"
					+ DOMWriter.nodeToString(n));
			return null;
		}//if

		Node formulaNode = null;
		if (child.getNodeType() == Node.TEXT_NODE) {
			formulaNode = child;
		} else {
			formulaNode = child.getNextSibling();
		}//else

		List list = null;
		try {
			if (child.getNodeName().equals("conf")) {
				String nodeValue = child.getFirstChild().getNodeValue();
				KurzSchrift scriptParser = new KurzSchrift(new StringReader(
						nodeValue + ";"));
				scriptParser.setBroker(broker);
				list = scriptParser.parseScriptWithoutActionComponents();
			}
		} catch (DOMException e) {
			ExceptionManager.doError("Fehler beim Auswerten einer Formel.\n"
					+ DOMWriter.nodeToString(child), e);
			return null;
		} catch (math4u2.util.exception.parser.script.ParseException e) {
			ExceptionManager.doError("Fehler beim Lesen einer Formel.\n"
					+ DOMWriter.nodeToString(child), e);
			return null;
		}

		Map map = new HashMap();
		if (list != null) {
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Object[] oa = (Object[]) iter.next();
				map.put(oa[1], oa[2]);
			}//for iter
		}//if

		//Hintergrundfarbe
		NamedNodeMap attrs = n.getAttributes();
		if (attrs.getLength() > 0) {
			//Hintergrundfarbe
			Node typeAttr = attrs.getNamedItem("bgcolor");
			if (typeAttr != null) {
				Color c = ColorUtil.parseColor(typeAttr.getNodeValue());
				map.put("bgColor", c);
			}
		} //attrs.length>0

		String fnName = null;
		String valueStr = formulaNode.getNodeValue().trim();

		int posAss = valueStr.indexOf(":=");
		if (posAss != -1) {
			if (show.equals(SHOW_NORMAL)) {
				return parseFormulaVersion2_0(valueStr, map, broker,
						oneStepActive);
			} else if (show.equals(SHOW_SHORT)) {
				return parseFormulaVersion2_0(valueStr.substring(0, posAss),
						map, broker, oneStepActive);
			} else if (show.equals(SHOW_BODY)) {
				return parseFormulaVersion2_0(valueStr.substring(posAss + 2),
						map, broker, oneStepActive);
			}
		} else if (valueStr.indexOf("(") != -1) {
			fnName = valueStr.substring(0, valueStr.indexOf("("));
			//nur Name herauslesen
		} else {
			fnName = valueStr;
		}

		MathObject mo;
		try {
			mo = broker.getObject(fnName);
		} catch (BrokerException e) {
			mo = null;
		}
		//Wenn das Objekt nicht gefunden werden konnte, dann ist
		//es eine Formel ohne Definition z.B. e^x
		if (mo == null) {
			return parseFormulaVersion2_0(valueStr, map, broker, oneStepActive);
		}//if

		if (show.equals(SHOW_NORMAL)) {
			if (mo instanceof UserFunction) {
				return parseFormulaVersion2_0(mo.toString(), map, broker,
						oneStepActive);
			} else {
				return parseFormulaVersion2_0(valueStr, map, broker,
						oneStepActive);
			}
		} else if (show.equals(SHOW_SHORT)) {
			if (mo instanceof UserFunction) {
				return parseFormulaVersion2_0(((Function) mo)
						.getDefinitionHeader(), map, broker, oneStepActive);
			} else {
				return parseFormulaVersion2_0(mo.getIdentifier().toString(), map,
						broker, oneStepActive);
			}
		} else if (show.equals(SHOW_BODY)) {
			if (mo instanceof UserFunction) {
				return parseFormulaVersion2_0(((UserFunction) mo)
						.getTermString(), map, broker, oneStepActive);
			} else {
				String def = mo.toString();
				int pos = def.indexOf(":=");
				if (pos == -1) {
					throw new RuntimeException(
							"Objekt kann in Body-Sicht nicht dargestellt werden. ("
									+ def + ")");
				} else {
					return parseFormulaVersion2_0(def.substring(pos), map,
							broker, oneStepActive);
				}
			}//else
		} else if (show.equals(SHOW_ACTUAL)) {
			List objs = broker.getUnorderedContent();
			for (Iterator iter = objs.iterator(); iter.hasNext();) {
				MathObject obj = (MathObject) iter.next();
				if (obj instanceof UserFunction
						&& ((UserFunction) obj).getArity() == 0) {
					map.put(obj.getIdentifier(), "text");
				}//if
			}//for iter
			return parseFormulaVersion2_0(mo.toString(), map, broker,
					oneStepActive);
		}//else if
		return null;
	}//parseMath4u2Version2_0

	public static AtomicBox parseFormulaVersion2_0(String formulaStr, Map map,
			Broker broker, boolean oneStepActive) {
		FormulaRenderContext frc = new FormulaRenderContext();
		AtomicBox testFormula = new math4u2.view.formula.ContainerBox(frc);
		math4u2.parser.formulaold.fsParser fsParserInst = new math4u2.parser.formulaold.fsParser(
				new StringReader(formulaStr));
		Map cloneMap = new HashMap(map);
		for (Iterator iter = cloneMap.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			Map secondMap = (Map) cloneMap.get(key);
			if (!secondMap.containsKey("oneStepActive")) {
				secondMap.put("oneStepActive", Boolean.toString(oneStepActive));
			}
		}//for iter
		fsParserInst.setModifierMap(cloneMap);

		math4u2.parser.formulaold.Node n;
		try {
			n = fsParserInst.parse(broker);
			n.bakeComponents(testFormula);

			Color bgColor = (Color) map.get("bgColor");
			if (bgColor != null) {
				testFormula.setOpaque(true);
				int alpha = bgColor.getAlpha();
				if (alpha == 255)
					alpha /= 2;
				testFormula.setBackground(new Color(bgColor.getRed(), bgColor
						.getGreen(), bgColor.getBlue(), alpha));
				testFormula.setBorder(BorderFactory.createEmptyBorder(5, 5, 5,
						5));
			}//if

			return testFormula;
		} catch (math4u2.parser.formulaold.ParseException e) {
			ExceptionManager.doError("Fehler in der Formel " + formulaStr, e);
		} catch (Throwable t) {
			ExceptionManager.doError("Fehler in der Formel " + formulaStr, t);
		}
		return null;
	}//parseFormulaVersion2_0

	public static AtomicBox parseFormulaVersion2_1(String formulaStr, Map map,
			Broker broker, boolean oneStepActive) {
		FormulaRenderContext frc = new FormulaRenderContext();
		AtomicBox testFormula = new ContainerBox(frc);
		int formulaSpacingH = Math.round(3f * FormulaRenderContext.MU
				* frc.getDefaultFontSize());
		int formulaSpacingV = Math.round(5f * FormulaRenderContext.MU
				* frc.getDefaultFontSize());
		testFormula.setSpacing(formulaSpacingV, formulaSpacingH,
				formulaSpacingV, formulaSpacingH);
		fsParser fsParserInst = new fsParser(new StringReader(formulaStr));

		Map cloneMap = new HashMap(map);
		for (Iterator iter = cloneMap.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			Object tmpObj = cloneMap.get(key);
			if (tmpObj instanceof Map) {
				Map secondMap = (Map) tmpObj;
				if (!secondMap.containsKey("oneStepActive")) {
					secondMap.put("oneStepActive", Boolean
							.toString(oneStepActive));
				}//if
			}//if is Map
		}//for iter
		fsParserInst.setModifierMap(cloneMap);

		math4u2.parser.formula.Node n;
		try {
			n = fsParser.parseFormula(formulaStr, cloneMap, broker);
			n.bakeComponents(testFormula);

			Color bgColor = (Color) map.get("bgColor");
			if (bgColor != null) {
				testFormula.setOpaque(true);
				int alpha = bgColor.getAlpha();
				if (alpha == 255)
					alpha /= 2;
				testFormula.setBackground(new Color(bgColor.getRed(), bgColor
						.getGreen(), bgColor.getBlue(), alpha));
				testFormula.setBorder(BorderFactory.createEmptyBorder(5, 5, 5,
						5));
			}//if

			return testFormula;
		} catch (math4u2.parser.formula.ParseException e) {
			ExceptionManager.doError("Fehler in der Formel " + formulaStr, e);
		} catch (Throwable t) {
			ExceptionManager.doError("Fehler in der Formel " + formulaStr, t);
		}
		return null;
	}//parseFormulaVersion2_1

	private LayoutScheme parseLayout(Node node) throws ParseException {
		NodeList elements = node.getChildNodes();
		double[][] layoutX;
		double[] layoutY;
		LinkedList names = new LinkedList();
		LinkedList rowPerc = new LinkedList();
		LinkedList rows = new LinkedList();

		for (int i = 0; i < elements.getLength(); i++) {
			Node n = elements.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String name = n.getNodeName();
				if (name.equals("row")) {
					rowPerc.add(n.getAttributes().getNamedItem("perc")
							.getNodeValue());
					LinkedList colPerc = new LinkedList();
					NodeList elementsRow = n.getChildNodes();
					for (int j = 0; j < elementsRow.getLength(); j++) {
						Node n2 = elementsRow.item(j);
						if (n2.getNodeType() != Node.ELEMENT_NODE)
							continue;
						if (n2.getNodeName().equals("col")) {

							colPerc.add(n2.getAttributes().getNamedItem("perc")
									.getNodeValue());
							names.add(n2.getAttributes().getNamedItem("name")
									.getNodeValue());
						} else
							throw new ParseException(
									"Ungültiges Tag <"
											+ n.getNodeName()
											+ "> in <row> gefunden. Es wird <col> erwartet.");
					} //for j
					rows.add(colPerc);
				} else
					throw new ParseException("Ungültiges Tag <"
							+ n.getNodeName()
							+ "> in <layout> gefunden. Es wird <row> erwartet.");
			} //if
		} //for i

		LayoutScheme ls = new LayoutScheme();
		ls.drawAreaNames = listToStringArray(names);

		//LayoutY
		layoutY = listToDoubleArray(rowPerc);

		//LayoutX
		layoutX = new double[rows.size()][];
		Object[] la = rows.toArray();

		for (int i = 0; i < la.length; i++) {
			layoutX[i] = listToDoubleArray((List) la[i]);
		} //for iter

		ls.layout = new PercentLayout(layoutX, layoutY);
		return ls;
	} //parseLayout

	private static double[] listToDoubleArray(List list) {
		double[] array = new double[list.size()];
		int i = 0;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			array[i] = Double.parseDouble(((String) iter.next())) / 100.0;
			i++;
		} //for iter
		return array;
	} //listToDoubleArray

	private static String[] listToStringArray(List list) {
		String[] array = new String[list.size()];
		int i = 0;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			array[i] = ((String) iter.next());
			i++;
		} //for iter
		return array;
	} //listToDoubleArray

	/**
	 * Analysiert ein Node-Objekt und erzeugt daraus einen formatierten Text.
	 * 
	 * @param textNode
	 *            Das Node-Objekt das analysiert werden soll
	 * @return Eine neue formattierte Zeichenkette
	 */
	public StyledText parseText(Node textNode) throws ParseException {
		return parseText2(textNode, false, true);
	} //parseText

	public StyledText parseText(Node textNode, boolean oneStepActive)
			throws ParseException {
		return parseText2(textNode, false, oneStepActive);
	} //parseText

	/**
	 * Überprüft eine Zeichenkette, ob der Inhalt boolschen Typs ist.
	 * 
	 * @param boolStr
	 *            Die Zeichenkette die analysiert werden soll
	 * @return Inhaltswert der Zeichenkette (<code>true</code> oder
	 *         <code>false</code>)
	 */
	private static final boolean parseBoolean(String boolStr) {
		String bStr = boolStr.toLowerCase();
		return (bStr.compareTo("true") == 0) || (bStr.compareTo("1") == 0);
	} //parseBoolean

	class LayoutScheme {

		public PercentLayout layout;

		public String[] drawAreaNames;
	}
} //class EParser
