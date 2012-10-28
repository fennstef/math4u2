package math4u2.view.gui.wizard.types;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.mathematics.affine.AffPoint;
import math4u2.mathematics.affine.Angle;
import math4u2.mathematics.affine.Area;
import math4u2.mathematics.affine.Arrow;
import math4u2.mathematics.affine.Bar;
import math4u2.mathematics.affine.Bezier;
import math4u2.mathematics.affine.Circle;
import math4u2.mathematics.affine.Curve;
import math4u2.mathematics.affine.Discrete;
import math4u2.mathematics.affine.DiscreteSequence;
import math4u2.mathematics.affine.Field;
import math4u2.mathematics.affine.Map;
import math4u2.mathematics.affine.Marker;
import math4u2.mathematics.affine.Straight;
import math4u2.mathematics.affine.Stretch;
import math4u2.mathematics.affine.TextBubble;
import math4u2.mathematics.collection.Sequence;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.DualVectorDoubleResult;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.parser.parser;
import math4u2.util.exception.parser.ParseException;
import math4u2.view.gui.Math4U2Win;
import math4u2.view.gui.listview.ViewFactorySingleton;
import math4u2.view.gui.wizard.Math4u2WizardModel;
import math4u2.view.gui.wizard.WizardUtil;
import math4u2.view.gui.wizard.components.CanModifyText;
import math4u2.view.gui.wizard.components.WizCheckBox;
import math4u2.view.gui.wizard.components.WizComboBox;
import math4u2.view.gui.wizard.components.WizExtendedList;
import math4u2.view.gui.wizard.components.WizFileSelect;
import math4u2.view.gui.wizard.components.WizTextField;
import math4u2.view.gui.wizard.components.WizTextPane;
import math4u2.view.gui.wizard.components.WizTextStyleCombo;
import math4u2.view.gui.wizard.step.ChooseObjectWizardStep;
import math4u2.view.gui.wizard.step.StandardWizardStep;
import math4u2.view.gui.wizard.step.StandardWizardStep.ParamEntry;
import math4u2.view.layout.TableLayout;

public class WizParamType {
	
	public static final WizParamType CLEAR_FORMULAR = new WizParamType(""){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			WizCheckBox comp = new WizCheckBox("Leere Textfelder bei der nächsten Eingabe");			
			return comp;
		}//getComponent
	};	

	public static final WizParamType EXECUTE_DEFINITION = new WizParamType(""){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			WizCheckBox comp = new WizCheckBox("Defintion beim Beenden erstellen");			
			return comp;
		}//getComponent
	};	
	
	public static final WizParamType SCALAR = new WizParamType("Term mit skalarem Rückgabewert"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			WizTextField comp = (WizTextField) super.getComponent(previousTitle, param, model);
			return comp;
		}//getComponent

		public List getWizardEntries() {
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN,					
					ChooseObjectWizardStep.FUNKTION
			});
		}//getWizardEntries
		
		protected Class getReturnTypeClass(){
			return ScalarDoubleResult.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			TermNode t = parser.sum(new LinkedList(), new LinkedList(), new HashSet(), regList);
			try{
				Object obj = t.eval();
				if(!(obj instanceof ScalarDoubleResult))
					throw new Throwable();
			}catch(Throwable e){
				throw new ParseException(0,"");
			}			
		}//validateFromParser		
	};
	
	public static final WizParamType VAR_NAME = new WizParamType("Variablenname z.B. i, t, ..."){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			WizTextField comp = new WizTextField();
			((WizTextField)comp).addKeyListener(StandardWizardStep.nextFocusOnComma);
			return comp;
		}//getComponent
	};
	
	public static final WizParamType VAR_NAME_LIST = new WizParamType("Liste von Variablennamen z.B. i, t, ..."){

	};
	
	public static final WizParamType TEXT = new WizParamType("Text"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			WizTextPane comp = new WizTextPane(){
				public String getText(){
					return '"'+super.getText()+'"';
				}//getText
			};
			return comp;
		}//getComponent	
		
		public boolean isFillComponent(){
			return true;
		}//isFillComponent
		
		protected Class getReturnTypeClass(){
			return TextBubble.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newTextObject(regList);
		}//validateFromParser
	};

	public static final WizParamType TEXT_STYLE = new WizParamType("Text-Stil"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			return new WizTextStyleCombo();
		}//getComponent				
	};	
	
	public static final WizParamType VECTORFIELD_LAYOUT = new WizParamType("Vektorfeld Konstante"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			String [] types = {"V", "VP", "NV", "NVP", "T", "TP", "NT", "NTP"};
			String [] descriptions = {
					"Betragstreue Pfeile",
					"Betragstreue Pfeile mit Aufpunkt",
					"Normierte Pfeile",
					"Normierte Pfeile mit Aufpunkt",
					"Betragstreue Tangenten",
					"Betragstreue Tangenten mit Aufpunkt",
					"Normierte Tangenten",
					"Normierte Tangenten mit Aufpunkt"					
			};
			Object[] list = new Object[types.length];
			for(int i=0; i<types.length; i++){
				list[i] = new WizComboBox.Entry(descriptions[i],types[i]);
			}
			return new WizComboBox(list);
		}//getComponent				

	};
	
	public static final WizParamType DISCRETE_SEQUENCE_LAYOUT = new WizParamType("Folge Layout-Konstante"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			String [] types = {"P", "S", "PS"};
			String [] descriptions = {
					"Nur die Punkte zeichnen.",
					"Die Punkte werden durch einen Streckenzug verbunden, dieser wird gezeichnet.",
					"Die Punkte und der zugehörige Streckenzug werden gezeichnet."					
			};
			Object[] list = new Object[types.length];
			for(int i=0; i<types.length; i++){
				list[i] = new WizComboBox.Entry(descriptions[i],types[i]);
			}
			return new WizComboBox(list);
		}//getComponent				

	};
	
	public static final WizParamType NEW_FUNC = new WizParamTypeNewFunc();
	
	public static final WizParamType ANONYM_LIST = new WizParamType("Liste von Elementen wie: <br>elem1, elem2, ... "){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			return new WizExtendedList(null, this, param, previousTitle);
		}//getComponent
		
		public boolean isFillComponent(){
			return true;
		}//isFillComponent
	};
	
	public static final WizParamType ANONYM_SCALAR_LIST = new WizParamType("Liste von Elementen wie: <br>elem1, elem2, ... <br>Die Elemente sind Terme mit skalarem Rückgabewert"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			return new WizExtendedList(null, this, param, previousTitle);
		}//getComponent
		
		public List getWizardEntries() {
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN,
					ChooseObjectWizardStep.FUNKTION
			});
		}//getWizardEntries		
	};
	
	public static final WizParamType ANONYM_AREA_LIST = new WizParamType("Liste von Punkten, Strecken und Kurven (parametrisierte Kurven und Bezier-Kurven)"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			CanModifyText comp = new WizExtendedList(null, this, param, previousTitle);
			return comp;
		}//getComponent
		
		public List getWizardEntries() {
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN,
					ChooseObjectWizardStep.PUNKT,
					ChooseObjectWizardStep.STRECKE,
					ChooseObjectWizardStep.PARAM_KURVE,
					ChooseObjectWizardStep.BEZIER_KURVE
			});
		}//getWizardEntries		
	};		
	
	public static final WizParamType ANONYM_VECTOR = new WizParamType("anonymer Vektor, z.B. vektor({1,2})"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			return new WizExtendedList("vektor", this, param, previousTitle);
		}//getComponent
		
		public boolean isFillComponent(){
			return true;
		}//isFillComponent
	};
	
	public static final WizParamType ANONYM_MATRIX = new WizParamTypeMatrix(){
		public boolean isFillComponent(){
			return false;
		}//isFillComponent		
	};
	
	public static final WizParamType FILE = new WizParamType("Dateipfad"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			return new WizFileSelect();
		}//getComponent			
	};
	
	public static final WizParamType FUNCTION = new WizParamType("allgemeine Funktion"){
		public List getWizardEntries() {
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN,
					ChooseObjectWizardStep.FUNKTION
			});
		}//getWizardEntries
	};
	
	public static final WizParamType FUNC_1_SCALAR = new WizParamType("Einstellige Funktion mit skalarer Rückgabe"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			WizTextField comp = (WizTextField) super.getComponent(previousTitle, param, model);
			((WizTextField)comp).addKeyListener(StandardWizardStep.nextFocusOnComma);
			return comp;
		}//getComponent
		
		public List getWizardEntries() {
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN,
					ChooseObjectWizardStep.FUNKTION
			});
		}//getWizardEntries
	};
	
	public static final WizParamType FUNC_2_SCALAR = new WizParamType("Zweistellige Funktion mit skalarer Rückgabe"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			WizTextField comp = (WizTextField) super.getComponent(previousTitle, param, model);
			((WizTextField)comp).addKeyListener(StandardWizardStep.nextFocusOnComma);
			return comp;
		}//getComponent
		
		public List getWizardEntries() {
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN
			});
		}//getWizardEntries		
	};
	
	public static final WizParamType POINT = new WizParamType("Punkt"){
		
		protected Class getReturnTypeClass(){
			return AffPoint.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newPointObject(regList);
		}//validateFromParser
		
		public List getWizardEntries(){			
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN,
					ChooseObjectWizardStep.FUNKTION,
					ChooseObjectWizardStep.PUNKT
			});
		}//getWizardEntries				
	};
	
	public static final WizParamType FOLGE = new WizParamType("Folge"){
		
		protected Class getReturnTypeClass(){
			return Sequence.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			return;
		}//validateFromParser
		
		public List getWizardEntries(){			
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN,
					ChooseObjectWizardStep.FUNKTION
			});
		}//getWizardEntries				
	};		
	
	public static final WizParamType VECTOR = new WizParamType("Vektor"){
		public List getWizardEntries() {
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN,
					ChooseObjectWizardStep.FUNKTION,					
					ChooseObjectWizardStep.VEKTOR,
			});
		}//getWizardEntries
		
		protected Class getReturnTypeClass(){
			return VectorDoubleResult.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.vectorFunctionReference(regList);
		}//validateFromParser
	};
	
	public static final WizParamType VECTOR_2D = new WizParamType("zweidimensionaler Vektor"){
		public List getWizardEntries() {
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN,
					ChooseObjectWizardStep.FUNKTION,					
					ChooseObjectWizardStep.VEKTOR,
			});
		}//getWizardEntries
		
		protected Class getReturnTypeClass(){
			return VectorDoubleResult.class;
		}//getReturnTypeClass
		
		protected boolean isObjectIsValid(Object obj) {
			VectorDoubleResult vdr = (VectorDoubleResult) obj;
			if(vdr.colDim==1 && vdr.rowDim==2)
				return true;
			return false;
		}//isObjectIsValid

		protected void validateFromParser(List regList) throws Throwable{
			UserFunction func = parser.vectorFunctionReference(regList);
			isObjectIsValid(func.eval());
		}//validateFromParser
	};	
	
	public static final WizParamType DUALVEKTOR = new WizParamType("Dualvektor"){
		public List getWizardEntries() {
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN,
					ChooseObjectWizardStep.FUNKTION,					
					ChooseObjectWizardStep.DUALVEKTOR,
			});
		}//getWizardEntries
		
		protected Class getReturnTypeClass(){
			return DualVectorDoubleResult.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.dualVectorFunctionReference(regList);
		}//validateFromParser		
	};		
	
	public static final WizParamType MATRIX = new WizParamType("Matrix"){
		public List getWizardEntries() {
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN,
					ChooseObjectWizardStep.FUNKTION,					
					ChooseObjectWizardStep.MATRIX,
			});
		}//getWizardEntries
		
		protected Class getReturnTypeClass(){
			return MatrixDoubleResult.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.matrixFunctionReference(regList);
		}//validateFromParser		
	};	
	
	public static final WizParamType MAP = new WizParamType("Farbkarte"){
		protected Class getReturnTypeClass(){
			return Map.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newMapObject(regList);
		}//validateFromParser		
	};
	
	public static final WizParamType MARKER = new WizParamType("Markierung"){
		protected Class getReturnTypeClass(){
			return Marker.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newMarkerObject(regList);
		}//validateFromParser			
	};
	
	public static final WizParamType STRETCH = new WizParamType("Strecke"){
		protected Class getReturnTypeClass(){
			return Stretch.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newStretchObject(regList);
		}//validateFromParser		
	};
	
	public static final WizParamType STRAIGHT = new WizParamType("Gerade"){
		protected Class getReturnTypeClass(){
			return Straight.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newStraightObject(regList);
		}//validateFromParser			
	};
	
	public static final WizParamType CIRCLE = new WizParamType("Kreis"){
		protected Class getReturnTypeClass(){
			return Circle.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newCircleObject(regList);
		}//validateFromParser		
	};
	
	public static final WizParamType ARROW = new WizParamType("Pfeil"){
		protected Class getReturnTypeClass(){
			return Arrow.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newArrowObject(regList);
		}//validateFromParser		
	};
	
	public static final WizParamType ANGLE = new WizParamType("Winkel"){
		protected Class getReturnTypeClass(){
			return Angle.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newAngleObject(regList);
		}//validateFromParser
	};
	
	public static final WizParamType CURVE = new WizParamType("Param. Kurve"){
		protected Class getReturnTypeClass(){
			return Curve.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newCurveObject(regList);
		}//validateFromParser		
	};
	
	public static final WizParamType BEZIER = new WizParamType("Bezier-Kurve"){
		protected Class getReturnTypeClass(){
			return Bezier.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newBezierObject(regList);
		}//validateFromParser
	};
	
	public static final WizParamType CURVES = new WizParamType("Kurvenzug"){
		protected Class getReturnTypeClass(){
			return Area.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newAreaObject(regList);
		}//validateFromParser
	};
	
	public static final WizParamType AREA = new WizParamType("Fläche"){
		protected Class getReturnTypeClass(){
			return Area.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newAreaObject(regList);
		}//validateFromParser		
	};
	
	public static final WizParamType DISCRETE = new WizParamType("Punkte"){
		protected Class getReturnTypeClass(){
			return Discrete.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newDiscreteObject(regList);
		}//validateFromParser				
	};
	
	public static final WizParamType DISCRETE_SEQUENCE = new WizParamType("Punktefolge"){
		protected Class getReturnTypeClass(){
			return DiscreteSequence.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newDiscreteSequenceObject(regList);
		}//validateFromParser				
	};
	
	public static final WizParamType BAR = new WizParamType("Balken-Diagramm"){
		protected Class getReturnTypeClass(){
			return Bar.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newBarObject(regList);
		}//validateFromParser		
	};
	
	public static final WizParamType FIELD_VECTOR = new WizParamType("Vektorfeld"){
		protected Class getReturnTypeClass(){
			return Field.class;
		}//getReturnTypeClass
		
		protected void validateFromParser(List regList) throws Throwable{
			parser.newFieldObject(regList);
		}//validateFromParser		
	};
	
	public static final WizParamType OBJECT_PATH = new WizParamType("Objektname und gegebenenfalls Methodenaufrufen z.B. mouse.x"){
		public void validate(String text) throws ParseException {
			Broker broker = Math4U2Win.getInstance().getBroker();
			try {
				MathObject mo = broker.getObject(text);
				if(mo==null)
					throw new BrokerException();
			} catch (BrokerException e) {
				throw new ParseException(0,"");
			}
		}//validate
	};
	
	public static final WizParamType FUNC_2_VECTOR = new WizParamType("Zweistellige Funktion mit vektorieller Rückgabe"){
		public List getWizardEntries() {
			return Arrays.asList(new ChooseObjectWizardStep.Entry[]{
					ChooseObjectWizardStep.OBJEKT_WAEHLEN
			});
		}//getWizardEntries			
	};		
	
	public static final WizParamType DEFINITION = new WizParamType("Definition"){
		public void validate(String text) throws ParseException {
			parser.NEWParseDefinition(text.trim(), false, Math4U2Win.getInstance().getBroker());
		}
	};
	
	public static final WizParamType DEFINITION_HEAD = new WizParamType("Objektname"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			WizTextField comp = new WizTextField();
			KeyListener keyListener = new StandardWizardStep.NextFocusOnCharacter(new char[]{':', '='}, new char[0]);
			((WizTextField)comp).addKeyListener(keyListener);
			return comp;
		}//getComponent
	};
	
	public static final WizParamType DEFINITION_BODY = new WizParamType("beliebiger Defintionsrumpf"){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			final WizTextPane comp = new WizTextPane();
			comp.addFocusListener(new FocusAdapter(){
				public void focusGained(FocusEvent e) {					
					comp.selectAll();
				}
			});
			WizardUtil.addKeyAssignment(comp, null, this, param, previousTitle, false);
			JLabel addButton = WizardUtil.getAddButton(comp);
			double P = TableLayout.PREFERRED, F = TableLayout.FILL;
			double size[][] = { { F, P }, { P, F} };
			comp.setLayout(new TableLayout(size));
			comp.add(addButton, "1, 0, R, T");
			return comp;
		}//getComponent
		
		public boolean isFillComponent(){
			return true;
		}//isFillComponent
	};
	
	public static final WizParamType ANY_INPUT = new WizParamType("Irgend ein Inhalt");
	
	public static final WizParamType LIST_TYPE = new WizParamType(
			"Folgende Typen sind möglich: <br><ul>" +
			"<li>Liste aus Termen mit Parametern. Typ bleibt leer oder &quot;&lt;funktion()>&quot;.</li>" +
			"<li>Liste aus einstelligen Funktionen. Typ: &quot;&lt;funktion(x)>&quot;, dabei ist x Funktionsvariable." +
			"<li>Liste aus zweistelligen Funktionen. Typ: &quot;&lt;funktion(x,y)>&quot;, dabei sind x, y Funktionsvariablen." +
			"<li>Entsprechend können mehrstellige Funktionen definiert werden.</li>" +
			"<li>Liste aus Punkten.Typ: &quot;&lt;punkt>&quot;.</li>" +
			"</ul>"
			){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			String [] types = {"", "<funktion(x)>", "<funktion(x,y)>", "<punkt>"};			
			WizComboBox comboBox = new WizComboBox(types);
			comboBox.setEditable(true);
			return comboBox;
		}//getComponent
	};	
	
	public static final WizParamType SEQUENCE_TYPE = new WizParamType("Der Rückgabetyp wird hier festgelegt."){
		public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
			String [] types = {"", "<skalar>", "<vektor>", "<matrix>"};			
			WizComboBox comboBox = new WizComboBox(types);
			comboBox.setEditable(true);
			return comboBox;
		}//getComponent
	};
	
	private String name;
	
	protected WizParamType(String name){
		this.name = name;
	}//Konstrktor
	
	public String toString(){
		return name;
	}//toString
	
	public List getWizardEntries(){
		return Arrays.asList(ChooseObjectWizardStep.typeList);
	}//getWizardEntries
	
	public CanModifyText getComponent(String previousTitle, ParamEntry param, Math4u2WizardModel model){
		WizTextField comp = new WizTextField();
		WizardUtil.addKeyAssignment(comp, comp, this, param, previousTitle, false);
		return comp;
	}//getComponent
	
	public boolean isFillComponent(){
		return false;
	}//isFillComponent
	
	public void validate(String text) throws ParseException {
		Class returnType = getReturnTypeClass();
		if (returnType == null)
			return;

		boolean isDefined = false;
		Broker broker = Math4U2Win.getInstance().getBroker();
		isDefined = tryToFindObject(text, returnType, broker);
		
		List regList=null;
		try {
			if (!isDefined) {
				regList = parser.startValidate(text, broker, ViewFactorySingleton.getInstance());
				validateFromParser(regList);
				testDefinitionIsValid(text);
			}// if
		} catch (Throwable t) {
			if(regList!=null)
				parser.handleValidateError(t, regList, broker);
		}
	}// validate
	
	protected boolean tryToFindObject(String text, Class returnType, Broker broker)
			throws ParseException{
		try {
			MathObject mo = broker.getObject(text); 
			if(mo==null)
				return false;
			if(!(mo instanceof Function))
				throw new ParseException(0, "Typproblem");
			Object obj = ((Function)mo).eval();
			if(!returnType.isAssignableFrom(obj.getClass()))
				throw new ParseException(0, "Typproblem");
			if(!isObjectIsValid(obj))
				throw new ParseException(0, "nicht gültig");
		} catch (ParseException e) {
			throw e;
		} catch (Exception e){
			return false;
		}
		return true;
	}//tryToFindObject
	
	protected boolean isObjectIsValid(Object obj){
		return true;
	}//isObjectIsValid
	
	protected void testDefinitionIsValid(String text) throws ParseException{
	}//isDefinitionIsValid
	
	protected Class getReturnTypeClass(){
		return null;
	}//getReturnTypeClass
	
	protected void validateFromParser(List regList) throws Throwable {
	}//validateFromParser
	
}//class WizParamType
