package math4u2.mathematics.functions;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.reference.PathReference;
import math4u2.mathematics.affine.AffPoint;
import math4u2.mathematics.affine.AreaInterface;
import math4u2.mathematics.affine.HasCompleteView;
import math4u2.mathematics.collection.HasTypeString;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.standardfunctions.FixFunction;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.termnodes.TermNodeNum;
import math4u2.mathematics.termnodes.Variable;
import math4u2.mathematics.types.MatrixType;
import math4u2.mathematics.types.OperatorExpert;
import math4u2.mathematics.types.ResultType;
import math4u2.mathematics.types.ScalarType;
import math4u2.mathematics.types.VectorType;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

/**
 * Repraesentiert eine (selbstdefinierte) Funktion (0-te Ableitung) wie z.B.
 * f(x,y) = x^2 + y oder eine Ableitung einer solchen Funktion nach einer
 * bestimmten ScalarVariable (hier: x oder y) in einer bestimmten Ordnung
 * (1,2,3,...). Sie enthaelt den Namen der Funktion (hier: f), die Namen der
 * lokalen Variablen (hier: x und y), sowie den Funktionsterm (hier: x^2 + y).
 */

public class UserFunction extends Function implements HasCompleteView,
		HasTypeString, HasGraph {

	/***************************************************************************
	 * Farbe der Funktion
	 */
	protected Color color = Color.BLACK;

	/***************************************************************************
	 * Füll-Farbe der Funktion
	 */
	protected Color fillColor;

	/***************************************************************************
	 * Rand-Farbe der Funktion
	 */
	protected Color borderColor;

	/***************************************************************************
	 * Linienstil der Funktion
	 */
	protected int lineStyle = 0;

	/***************************************************************************
	 * Darstellungstil bei z.B. einen Punkt
	 */
	protected String style;

	/***************************************************************************
	 * Sichtbarkeit der Funktion
	 */
	protected boolean visible = true;

	/***************************************************************************
	 * Darf die Funktion vom Benutzer verändert werden?
	 */
	protected boolean freeze = false;

	/* Variablen der Funktion */
	Variable[] variables;

	/* wird bei Änderungen benachrichtigt. */
	Broker broker;

	/*
	 * Der beim letzten Aufruf von eval berechnete Wert. Bei einer nullstelligen
	 * Funktion ist dieser gültig, falls
	 */
	Object currentValue;

	/*
	 * Entscheidet bei nullstelligen Funktionen, ob der in currentValue
	 * gespeicherte Wert gueltig ist, dann wird er bei eval() ohne neue
	 * Berechnung zurueckgegeben, oder nicht, dann wird bei eval() der Wert neu
	 * berechnet. isValid muss auf false gesetzt werden bei renew() und bei
	 */
	boolean isValid = false;

	/*
	 * Der eigentliche Funktionsterm
	 */
	TermNode functionTerm;

	Set indexFunctionSet;

	/*
	 * höchste Ordnung, für die der Term der Ableitungsfunktion aktuell gueltig
	 * ist.
	 */
	int maxValidOrder = -1;

	/*
	 * Liste der Terme der Ableitungsfunktionen. Beim Index 0 steht der
	 * Funktionsterm selbst, allgemein beim Index i der Term der i-ten
	 * Ableitung.
	 */
	List deriveTerms;

	protected ViewFactoryInterface viewFactory;

	/**
	 * Initialisiert sämtliche Variablen und legt den Typ fest. Wird von den
	 * öffentlichen Konstruktoren verwendet.
	 */

	public UserFunction(String name, TermNode functionTerm,
			Variable[] variables, Broker broker,
			ViewFactoryInterface viewFactory) {
		this.name = name;
		this.functionTerm = functionTerm;
		this.variables = variables;
		this.broker = broker;
		isValid = false;
		this.viewFactory = viewFactory;
	}

	/***************************************************************************
	 * Konstruktor fuer einen Parameter mit einem Zahlenwert, also einer
	 * nullstelligen Funktion. Hier wird eine solche Funktion erzeugt, deren
	 * Funktionswert durch parameterValue gegeben ist.
	 * 
	 * @param parameterName
	 *            Name des Parameters
	 * @param parameterValue
	 *            Wert des Parameters
	 * @param broker
	 *            Referenz auf den Broker
	 */
	public UserFunction(String parameterName, double parameterValue,
			Broker broker, ViewFactoryInterface viewFactory) {

		this(parameterName, new TermNodeNum(parameterValue), new Variable[] {},
				broker, viewFactory);
	}

	/**
	 * Erzeugt eine Funktion ohne Namen
	 * 
	 * @param functionTerm
	 * @param variables
	 * @param broker
	 */

	public UserFunction(TermNode functionTerm, Variable[] variables,
			Broker broker, ViewFactoryInterface viewFactory) {

		this("", functionTerm, variables, broker, viewFactory);
		name = null;
	}

	/***************************************************************************
	 * Liefert den Funktionsnamen
	 */
	public String getName() {
		return name;
	}

	public AbstractSet getPartSet() {
		HashSet result = new HashSet();
		functionTerm.insertParts(result);
		return result;
	}

	public boolean isNotFixed() {
		if (functionTerm instanceof PathReference) {
			Object o = ((PathReference) functionTerm).getRootObject();
			if (getArity() != 0)
				return false;
			if (getResultType() != ScalarType.class)
				return false;

			return !(o instanceof FixFunction);
		}// if
		return true;
	}// isFixed

	public boolean isScalarParameter() {
		return getArity() == 0 && getResultType() == ScalarType.class;
	}// isScalarParameter

	public boolean isNumber() {
		return (functionTerm instanceof TermNodeNum);
	}// isNumber

	/**
	 * Ersetzt im aktuellen Objekt jede referenz auf oldObject durch eine
	 * Referenz auf newObject.
	 * 
	 * @param oldObject
	 *            zunaechst referenziertes Objekt
	 * @param newObject
	 *            statt oldObjekt zu referenzierendes Objekt
	 * @throws MathException
	 */
	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws MathException {
		functionTerm.swapLinks(oldObject, newObject);
		maxValidOrder = -1; // erzwingt, dass Ableitungen neu gerechnet werden
	}

	/***************************************************************************
	 * Falls eine Ableitung vorliegt ( type == partialDerive ), wird der Term
	 * der Ableitungsfunktion neu berechnet. Ansonsten geschieht nichts.
	 * 
	 * @param source
	 */
	public void renew(MathObject source) {
		isValid = false;
	}

	/***************************************************************************
	 * Kettenregel für Funtionen beliebiger Stelligkeit:
	 * d/dt(F(x1(t),x2(t),...,xn(t)) = (df/dx1)*(dx1/dt)+....+(df/dxn)*(dxn/dt).
	 * 
	 * @param argumentTerms
	 *            Die Argumente x1(t),...,xn(t)
	 * @param derivedArgumentTerms
	 *            Die Ableitungen dx1/dt,...,dxn/dt
	 * @param broker
	 * @return Ableitung dF/dt
	 * @throws Exception
	 */
	public TermNode buildDeriveTerm(TermNode[] argumentTerms,
			TermNode[] derivedArgumentTerms, Broker broker)
			throws MathException {

		Function f;
		if (getArity() == 0) {
			if (ScalarType.class.isAssignableFrom(functionTerm.getResultType()))
				return new TermNodeNum(0.0);

			else {
				// return functionTerm.getNullTerm();
				TermNode returnTerm;
				f = OperatorExpert.getFunctionForArguments(functionTerm,
						new TermNodeNum(0.0), "*", broker);
				returnTerm = new TermNodeFunct(f, new TermNode[] {
						functionTerm, new TermNodeNum(0.0) }, broker);
				return returnTerm;
			}
		}

		TermNode returnTerm;

		if (derivedArgumentTerms[0] instanceof TermNodeNum
				&& ((TermNodeNum) derivedArgumentTerms[0]).evalScalar() == 0.0) {
			returnTerm = derivedArgumentTerms[0];
		} else {
			f = OperatorExpert.getFunctionForArguments(functionTerm,
					derivedArgumentTerms[0], "*", broker);
			returnTerm = new TermNodeFunct(f, new TermNode[] {
					new TermNodeFunct(new UserFunction("",
							functionTerm.derive(variables[0]), variables,
							broker, viewFactory), argumentTerms, broker),
					derivedArgumentTerms[0] }, broker);
		}

		for (int i = 1; i < variables.length; i++) {
			if (!(derivedArgumentTerms[i] instanceof TermNodeNum && ((TermNodeNum) derivedArgumentTerms[i])
					.evalScalar() == 0.0)) {

				f = OperatorExpert.getFunctionForArguments(functionTerm,
						derivedArgumentTerms[i], "*", broker);
				TermNode tn = new TermNodeFunct(f, new TermNode[] {
						new TermNodeFunct(new UserFunction("",
								functionTerm.derive(variables[i]), variables,
								broker, viewFactory), argumentTerms, broker),
						derivedArgumentTerms[i] }, broker);

				f = OperatorExpert.getFunctionForArguments(returnTerm, tn, "+",
						broker);
				returnTerm = new TermNodeFunct(f, new TermNode[] { returnTerm,
						tn }, broker);
			}

		}
		return returnTerm;
	} // buildDeriveTerm

	public String buildTermString(String[] argStrings, String name) {

		if (this.getName().equals("aaa")) {
			return "($" + super.buildTermString(argStrings, name) + "::"
					+ functionTerm.getTermString() + "$)";
		} else
			return super.buildTermString(argStrings, name);
	} // buildTermString

	/**
	 * Fuegt alle Funktionen zu indexFunctions hinzu, die in einem Index (Liste,
	 * Matrix, Vektor) des aktuellen Knotens und seines Teilbaums verwendet
	 * werden. Muss von den abgeleiteten Klassen ueberschrieben werden, in denen
	 * tatsaechlich Indices verwendet werden.
	 * 
	 * @param indexFunctionSet
	 *            Menge, in der die in Indices verwendeten Funktionen gesammelt
	 *            werden.
	 */
	public void insertIndexFunctions(Set indexFunctionSet) {
		functionTerm.insertIndexFunctions(indexFunctionSet);
	}

	public void insertAllFunctions(Set indexFunctionSet) {
		functionTerm.insertAllFunctions(indexFunctionSet);
	}

	/***************************************************************************
	 * Die aktuelle Funktion kann die mit oldFunction gelieferte Funktion
	 * ersetzen, wenn - oldFunction keine Standard-Funktion ist und eines von
	 * folgendem gilt: - die Funktion oldFunction nicht verwendet wird, d.h. die
	 * Menge oldAggregateSet ist leer oder - die Stelligkeit der aktuellen
	 * Funktion stimmt mit der Stelligkeit von oldFunction ueberein.
	 * 
	 * @param oldObject
	 * @param oldAggregateSet
	 * @return true, falls Ersetzung moeglich, ansonsten false.
	 */

	public boolean testSubstitution(MathObject oldFunction, Set oldAggregateSet) {
		return (!(oldFunction instanceof StandardFunction) && (oldAggregateSet
				.isEmpty() || (this.getArity() == ((Function) oldFunction)
				.getArity())));
	}

	/***************************************************************************
	 * Gibt den Funktionsterm zurueck.
	 * 
	 * @return Funktionsterm
	 */
	public TermNode getFunction() {
		return functionTerm;
	}

	/***************************************************************************
	 * Macht functionTerm zur neuen Abbildungsvorschrift der Funktion
	 * 
	 * @param functionTerm
	 *            Die neue Abbildungsvorschrift
	 */

	public void setFunction(TermNode functionTerm) {
		this.functionTerm = functionTerm;
		isValid = false;
	}

	/***************************************************************************
	 * Trägt einen neuen Zahlenwert in einen Parameter ein
	 * 
	 * @param functionTerm
	 *            Die neue Abbildungsvorschrift
	 */
	public void setParameterValue(double value, boolean makeNoRenew)
			throws MathException, BrokerException {
		if (!(functionTerm instanceof TermNodeNum)) {
			// throw new MathException("Funktion muß Nullstellig sein.");
			functionTerm = new TermNodeNum(value);
		} else {
			((TermNodeNum) functionTerm).setValue(value);
		}
		isValid = false;
		if (!makeNoRenew)
			broker.propagateChange(this);
	}

	/***************************************************************************
	 * Gibt den zuständigen Graphen zurück.
	 * 
	 * @param da
	 *            Die Zeichenfläche
	 */
	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		if (!hasCurrentObjectGraph())
			throw new RuntimeException("Diese Funktion besitzt keinen Graphen");

		UserDefinedFunction userFunc = getUserDefinedFunction();
		if (userFunc != null && userFunc.hasCurrentObjectGraph()) {
			return userFunc.getGraph(da, f);
		}// if

		if (getArity() == 0) {
			Object o = null;
			try {
				o = eval();
			} catch (MathException e) {
				ExceptionManager
						.doError("Fehler beim ermitteln des Graphen", e);
				return null;
			}
			if (o instanceof HasGraph) {
				return ((HasGraph) o).getGraph(da, this);
			} else if (o instanceof VectorDoubleResult) {
				return getViewFactory().getGraphFactory().createVectorElementGraph(da, (UserFunction) this);
			}// else
		} else if (getArity() == 1) {
			return getViewFactory().createFunctionGraph(da, this);
		}
		return null;
	}// getGraph

	/**
	 * Falls diese Function eine benutzerdefinierte Funktion ist, wird diese
	 * zurück gegeben. Andernfalls wurd null zurück gegeben.
	 */
	public UserDefinedFunction getUserDefinedFunction() {
		if (functionTerm instanceof PathReference) {
			PathReference pr = (PathReference) functionTerm;
			MathObject rootMo = pr.getRootObject();
			if (rootMo instanceof UserDefinedFunction) {
				return (UserDefinedFunction) rootMo;
			}// if
		}// if
		return null;
	}// getUserDefinedFunction

	/***************************************************************************
	 * Abfrage, ob die UserFunction einen Graphen besitzt
	 * 
	 */
	public boolean hasCurrentObjectGraph() {
		UserDefinedFunction userFunc = getUserDefinedFunction();
		if (userFunc != null && userFunc.hasCurrentObjectGraph()) {
			return true;
		}// if

		Class returnType = getResultType();
		if (getArity() == 1) {
			// Parameter muß skalar sein
			Variable var = variables[0];
			if (!ScalarType.class.isAssignableFrom(var.getResultType()))
				return false;

			// Rückgabetyp muß skalar sein
			if (!ScalarType.class.isAssignableFrom(returnType))
				return false;

			return true;
		} else {
			if (getArity() == 0) {
				if (HasGraph.class.isAssignableFrom(returnType)
						|| VectorType.class.isAssignableFrom(returnType))
					return true;
			}// if
				// else {
			// if ( getArity() == 2) {
			//
			// return VectorType.class.isAssignableFrom(returnType) ||
			// ScalarType.class.isAssignableFrom(returnType) ;
			// }
			// }
			return false;
		}// else
	}// hasCurrentObjectGraph

	/***************************************************************************
	 * 
	 * Setzen der Farbe
	 */
	public void setColor(Color c) {
		UserDefinedFunction userFunc = getUserDefinedFunction();
		if (userFunc != null) {
			userFunc.setColor(c);
		} else if (isEncapsulated()) {
			try {
				Object obj = eval();
				if (obj instanceof HasGraph) {
					((HasGraph) obj).setColor(c);
				}// if
			} catch (MathException e) {
				ExceptionManager.doError("Fehler beim Setzen der Farbe", e);
			}
		}// if
		color = c;
	}// setColor

	/***************************************************************************
	 * 
	 * Setzen der Füllfarbe
	 */
	public void setFillColor(Color c) {
		if (isEncapsulated()) {
			try {
				Object obj = eval();
				if (obj instanceof HasGraph) {
					((AreaInterface) obj).setFillColor(c);
				}// if
			} catch (MathException e) {
				ExceptionManager.doError("Fehler beim Setzen der Füllfarbe", e);
			}
		}// if
		fillColor = c;
	}// setFillColor

	/***************************************************************************
	 * 
	 * Setzen der Randfarbe
	 */
	public void setBorderColor(Color c) {
		if (isEncapsulated()) {
			try {
				Object obj = eval();
				if (obj instanceof HasGraph) {
					((AreaInterface) obj).setBorderColor(c);
				}// if
			} catch (MathException e) {
				ExceptionManager.doError("Fehler beim Setzen der Randfarbe", e);
			}
		}// if
		borderColor = c;
	}// setFillColor

	/***************************************************************************
	 * Setzen des Linienstils
	 */
	public void setLineStyle(int style) {
		UserDefinedFunction userFunc = getUserDefinedFunction();
		if (userFunc != null) {
			userFunc.setLineStyle(style);
		} else if (isEncapsulated()) {
			try {
				Object obj = eval();
				if (obj instanceof HasGraph) {
					((HasGraph) obj).setLineStyle(style);
				}// if
			} catch (MathException e) {
				ExceptionManager.doError("Fehler beim Setzen des Linienstils",
						e);
			}
		}// if
		lineStyle = style;
	}// setLineStyle

	/***************************************************************************
	 * Setzen des Stils
	 */
	public void setStyle(String style) {
		if (isEncapsulated()) {
			try {
				Object obj = eval();
				if (obj instanceof AffPoint) {
					((AffPoint) obj).setStyle(style);
				}// if
			} catch (MathException e) {
				ExceptionManager.doError("Fehler beim Setzen der Stils", e);
			}
		}// if
		this.style = style;
	}// setLineStyle

	/***************************************************************************
	 * Setzen der Sichtbarkeit
	 */
	public void setVisible(boolean visible) {
		UserDefinedFunction userFunc = getUserDefinedFunction();
		if (userFunc != null) {
			userFunc.setVisible(visible);
		} else if (isEncapsulated()) {
			try {
				Object obj = eval();
				if (obj instanceof HasGraph) {
					((HasGraph) obj).setVisible(visible);
				}// if
			} catch (MathException e) {
				ExceptionManager.doError("Fehler beim Setzen der Sichtbarkeit",
						e);
			}
		}// if
		this.visible = visible;
	}// setVisible

	/***************************************************************************
	 * Setze das Flag, ob eine Funktion überschrieben werden darf
	 */
	public void setFreeze(boolean freeze) {
		this.freeze = freeze;
	}// setFreeze

	public Color getColor() {
		UserDefinedFunction userFunc = getUserDefinedFunction();
		if (userFunc != null) {
			return userFunc.getColor();
		}// if
		if (isEncapsulated()) {
			try {
				color = ((HasGraph) eval()).getColor();
			} catch (MathException e) {
				ExceptionManager.doError("Fehler beim Setzen von Freeze", e);
			}
		}
		return color;
	}// getColor

	public Color getFillColor() {
		if (isEncapsulated()) {
			try {
				fillColor = ((AreaInterface) eval()).getFillColor();
			} catch (MathException e) {
				ExceptionManager.doError("Fehler beim Holen der Füllfarbe", e);
			}
		}// if
		return fillColor;
	}// getFillColor

	public Color getBorderColor() {
		if (isEncapsulated()) {
			try {
				borderColor = ((AreaInterface) eval()).getBorderColor();
			} catch (MathException e) {
				ExceptionManager.doError("Fehler beim Holen der Randfarbe", e);
			}
		}// if
		return borderColor;
	}// getBorderColor

	public String getStyle() {
		if (isEncapsulated()) {
			try {
				this.style = ((AffPoint) eval()).getStyle();
			} catch (MathException e) {
				ExceptionManager.doError("Fehler beim Holen des Stils", e);
			}
		}// if
		return style;
	}// getStyle

	public int getLineStyle() {
		UserDefinedFunction userFunc = getUserDefinedFunction();
		if (userFunc != null) {
			return userFunc.getLineStyle();
		}// if
		if (isEncapsulated()) {
			try {
				lineStyle = ((HasGraph) eval()).getLineStyle();
			} catch (MathException e) {
				ExceptionManager
						.doError("Fehler beim Holen des Linienstils", e);
			}// catch
		}// if
		return lineStyle;
	}// getLineStyle

	public boolean isVisible() {
		return visible;
	}// isVisible

	public boolean isFreeze() {
		return freeze;
	}// isFreeze

	/***************************************************************************
	 * Setzen des Namens
	 */

	/***************************************************************************
	 * Transferiert in die Hülle von oldObject den Funktionsterm, die Variablen
	 * und alle anderen Bestandteile der aktuellen Funktion.
	 * 
	 * @param oldObject
	 *            Funktion, deren Funktionalität ersetzt werden soll
	 * @return oldObject, mit neuem Inhalt versehen.
	 */
	public MathObject constructSubstitution(MathObject oldObject) {
		((UserFunction) oldObject).setFunction(this.getFunction());
		((UserFunction) oldObject).setVariables(this.getVariables());
		try {
			broker.propagateChange(oldObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		isValid = false;
		return oldObject;
	}

	public Class getVariableType(int pos) throws MathException {
		if (0 <= pos && pos < variables.length)
			return variables[pos].getResultType();
		else
			throw new MathException("Funktion " + name
					+ " hat keine Variable an der Stelle " + pos);
	}

	public Class getResultType(Class[] argTypes) {
		return functionTerm.getResultType();
	}

	public Class getResultType() {
		return functionTerm.getResultType();
	}

	public boolean isEncapsulated() {
		if (functionTerm instanceof PathReference && getArity() == 0) {
			return !ResultType.class.isAssignableFrom(getResultType());
		} else
			return false;
	}// isEncapsulated

	public String getTypeString() {
		if (isEncapsulated()) {
			try {
				Object obj = eval();
				if (obj instanceof HasTypeString) {
					return ((HasTypeString) obj).getTypeString();
				}
			} catch (MathException e) {
				ExceptionManager.doError("Fehler beim Ermitteln des Typs", e);
			}
		}

		int arity = getArity();
		String typeString = "";
		String[] vars = getVariableNames();

		for (int i = 0; i < arity; i++) {
			try {
				Class type = getVariableType(i);
				Method m = type.getMethod("getTypeString", new Class[0]);
				typeString += (String) m.invoke(null, new Object[0]);
			} catch (Exception e) {
				ExceptionManager.doError("Fehler beim Ermitteln des Typs", e);
			}// catch

			if (i != 0)
				typeString += ",";
			typeString += vars[i];
		}// for i
		if (getArity() != 0)
			typeString = "(" + typeString + ")";
		return "funktion" + typeString;
	}// getTypeString

	/***************************************************************************
	 * Gibt die Variablennamen der Funktion zurueck.
	 * 
	 * @return Array mit Variablennamen
	 */

	public String[] getVariableNames() {
		if (getArity() > 0) {
			String[] variableNames = new String[variables.length];
			for (int i = 0; i < variables.length; i++)
				variableNames[i] = variables[i].getTermString();

			return variableNames;
		}
		return new String[] {};
	}

	/***************************************************************************
	 * Gibt die Variablen der Funktion zurueck.
	 * 
	 * @return Array der Variablen
	 */

	public Variable[] getVariables() {
		return variables;
	}

	/***************************************************************************
	 * Mache die in newVariables enthaltenen Variablen zu den neuen Variablen
	 * der Funktion.
	 * 
	 * @param newVariables
	 *            Array der neuen Variablen
	 */

	public void setVariables(Variable[] newVariables) {
		isValid = false;
		variables = newVariables;
	}

	/***************************************************************************
	 * Gibt die Stelligkeit der Funktion zurueck.
	 * 
	 * @return Stelligkeit als int, z.B. 1 bei f(x), 2 bei f(x,y)
	 */

	public int getArity() {
		if (variables == null)
			return 0;
		return variables.length;
	}

	/***************************************************************************
	 * Gibt den (vom Benutzer verwendeten) Namen des Arguments an der Stelle
	 * Position der Argumentliste zurück. Dies ist nur zur Darstellung des
	 * Funktionsterms am Bildschirm relevant.
	 * 
	 * @param position
	 *            die Position in der Parameterliste, z.B. ist x bei f(x,y) auf
	 *            Position 1
	 * @return der Name des Arguments
	 */

	public String getArgName(int position)
			throws ArrayIndexOutOfBoundsException {
		return variables[position].getTermString();
	}

	/***************************************************************************
	 * Berechnet den Funktionswert: -- zuerst werden die Variablen der Funktion
	 * der Reihe nach durch die Werte in argList substituiert -- dann wird der
	 * Funktionsterm ausgewertet
	 * 
	 * @param argList
	 *            Array mit Werten
	 * @return Ergebnis
	 */

	public Object eval(Object[] argList) throws MathException {

		// Spezielle Behandlung bei nullstelligen Funktionen
		if (argList.length == 0) {
			return eval();
		}

		if (argList.length != variables.length)
			throw new MathException(
					"Funktion "
							+ name
							+ ": Stelligkeit und Anzahl der Aufrufparameter stimmen nicht ueberein");
		else {
			for (int i = 0; i < argList.length; i++) {
				variables[i].setValue(argList[i]);
			}
		}
		return functionTerm.eval();
	} // eval

	public Object shallowEval(Object[] argList) throws MathException {

		// Spezielle Behandlung bei nullstelligen Funktionen
		if (argList.length == 0) {
			return eval();
		}

		if (argList.length != variables.length)
			throw new MathException(
					"Funktion "
							+ name
							+ ": Stelligkeit und Anzahl der Aufrufparameter stimmen nicht ueberein");
		else {
			for (int i = 0; i < argList.length; i++) {
				variables[i].setValue(argList[i]);
			}
		}
		return functionTerm.shallowEval();
	} // eval

	/***************************************************************************
	 * Berechnet den Funktionswert der Ableitung einer einstelligen Funktion
	 * 
	 * @param deriveOrder
	 *            Ordnung der Ableitung
	 * @param arg
	 *            Aktueller Aufrufparameter
	 * @return Ergebnis
	 */
	public Object shallowEval(int deriveOrder, Object arg) throws MathException {
		if (1 != variables.length)
			throw new MathException(
					"Funktion "
							+ name
							+ ": Stelligkeit und Anzahl der Aufrufparameter stimmen nicht ueberein");
		else
			variables[0].setValue(arg);

		if (deriveOrder > maxValidOrder) {
			// noetige Ableitungsterme berechnen
			if (maxValidOrder < 0) {
				deriveTerms = new ArrayList();
				deriveTerms.add(functionTerm);
				maxValidOrder = 0;
			}
			for (; maxValidOrder < deriveOrder; maxValidOrder++) {
				try {
					TermNode newTerm = ((TermNode) deriveTerms
							.get(maxValidOrder)).getClone(variables, variables);
					// System.out.println("abzuleiten: " +
					// newTerm.getTermString());
					newTerm = ((TermNode) deriveTerms.get(maxValidOrder))
							.derive(variables[0]);

					newTerm = newTerm.evalNum();
					newTerm = newTerm.expand();
					newTerm = newTerm.simplify();

					// newTerm =
					// ((TermNode)deriveTerms.get(maxValidOrder)).derive(variables[0]).evalNum().simplify();
					// // expand()??
					deriveTerms.add(newTerm);
					// System.out.println("sssssssss  derive: "+
					// newTerm.expand().getTermString()+ " m0" + maxValidOrder);

					// ((TermNode)deriveTerms.get(maxValidOrder)).derive(variables[0]).expand().evalNum().simplify();
				} catch (MathException e) {
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					e.printStackTrace();
					throw new MathException(e.getMessage());
				}
				// System.out.println("sssssssssssssssss  derive: mord " +
				// maxValidOrder);
			}
		}
		return ((TermNode) deriveTerms.get(deriveOrder)).shallowEval();
	} // eval

	/***************************************************************************
	 * Berechnet den Funktionswert einer einstelligen Funktion
	 * 
	 * @param arg
	 *            Aktueller Aufrufparameter
	 * @return Ergebnis
	 */
	public Object eval(Object arg) throws MathException {
		if (1 != variables.length)
			throw new MathException(
					"Funktion "
							+ name
							+ ": Stelligkeit und Anzahl der Aufrufparameter stimmen nicht ueberein");
		else
			variables[0].setValue(arg);
		return functionTerm.eval();
	} // eval

	/***************************************************************************
	 * Berechnet den Funktionswert der Ableitung einer einstelligen Funktion
	 * 
	 * @param deriveOrder
	 *            Ordnung der Ableitung
	 * @param arg
	 *            Aktueller Aufrufparameter
	 * @return Ergebnis
	 */
	public Object eval(int derOrder, Object arg) throws MathException {
		if (1 != variables.length)
			throw new MathException(
					"Funktion "
							+ name
							+ ": Stelligkeit und Anzahl der Aufrufparameter stimmen nicht ueberein");
		else
			variables[0].setValue(arg);

		if (derOrder > maxValidOrder) {
			// noetige Ableitungsterme berechnen
			if (maxValidOrder < 0) {
				deriveTerms = new ArrayList();
				deriveTerms.add(functionTerm);
				maxValidOrder = 0;
			}
			for (; maxValidOrder < derOrder; maxValidOrder++) {
				try {
					TermNode newTerm = ((TermNode) deriveTerms
							.get(maxValidOrder)).getClone(variables, variables);
					// System.out.println("abzuleiten: " +
					// newTerm.getTermString());
					newTerm = ((TermNode) deriveTerms.get(maxValidOrder))
							.derive(variables[0]);

					newTerm = newTerm.evalNum();
					newTerm = newTerm.expand();
					newTerm = newTerm.simplify();

					// System.out.println("&&&&&&&&&&&&  derive: "+
					// newTerm.expand().getTermString()+ " m0" + maxValidOrder);
					// newTerm =
					// ((TermNode)deriveTerms.get(maxValidOrder)).derive(variables[0]).evalNum().simplify();
					// // expand()??
					deriveTerms.add(newTerm);
					// System.out.println("&&&&&&&&&&&&&&&&  derive: "+
					// newTerm.expand().getTermString()+ " m0" + maxValidOrder);
				} catch (MathException e) {
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					e.printStackTrace();
					throw new MathException(e.getMessage());
				}
			}
		}
		System.out.println("&&&&&&&&&&&&&&&&  derive: mord " + maxValidOrder);
		return ((TermNode) deriveTerms.get(derOrder)).eval();
	} // eval

	/***************************************************************************
	 * Berechnet den Funktionswert einer nullstelligen Funktion.
	 * 
	 * @return Ergebnis
	 */
	public Object eval() throws MathException {

		if (isValid && currentValue != null) { // der zuletzt berechnete Wert
												// ist noch gültig
			return currentValue;
		}

		if (0 != variables.length) {
			throw new MathException(
					"Funktion "
							+ name
							+ ": Stelligkeit und Anzahl der Aufrufparameter stimmen nicht ueberein");
		}

		isValid = true;
		currentValue = functionTerm.eval();
		return currentValue;
	} // eval

	/***************************************************************************
	 * Liefert den Term der Funktion als String, z.B. x^2 bei f(x) = x^2
	 * 
	 * @return Funktionsterm
	 */
	public String getTermString() {
		return functionTerm.getTermString();
	}

	public String getTermString(MathObject mo) {
		return functionTerm.getTermString(mo);
	}

	public void invalidate() {
		isValid = false;
	}

	/***************************************************************************
	 * Erzeugt Definitions-String wie "f(x,x) := sin(x)+y^2" oder
	 * "derive(sin(x)*y,y,2)" .
	 * 
	 * @return Definitions-String
	 */

	public String toString() {
		return getDefinitionHeader() + " := " + functionTerm.getTermString();
	}

	/***************************************************************************
	 * Erzeugt eine Kopie der aktuellen Funktion. Die Variablen der Kopie haben
	 * die gleichen Namen wie die Variablen der aktuellen Funktion. Die Kopie
	 * erhält den gleichen Namen wie die aktuelle Funktion.
	 * 
	 * @return Kopie
	 * @throws Exception
	 */

	public UserFunction cloneFunction() throws Exception {
		// ScalarVariable klonen
		Variable[] newVars = new Variable[variables.length];
		for (int i = 0; i < variables.length; i++) {
			newVars[i] = variables[i].getClone();
		}
		TermNode newFunctionTerm = functionTerm.getClone(variables, newVars);
		return new UserFunction(this.getName(), newFunctionTerm, newVars,
				broker, viewFactory);
	} // cloneFunction

	/***************************************************************************
	 * Vereinfacht die Funktion durch Vereinfachung des Funktionsterms.
	 * 
	 * @throws Exception
	 */

	public void simplify() throws Exception {
		functionTerm = functionTerm.simplify();
	} // simplify

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		if (getArity() == 0) {
			try {
				Object obj = eval();
				if (obj instanceof HasCompleteView) {
					return ((HasCompleteView) obj).getCompleteView(this, alv,
							broker);
				}// if
			} catch (MathException e) {
			}
		}// if

		// Bei einer Matrix wird die Matrix-DetailSicht aufgebaut
		if (getResultType() == MatrixType.class) {
			return viewFactory.createCompleteMatrixViewItem(this, alv, broker);
		}// if

		return viewFactory.createCompleteFunctionViewItem(this, alv, broker);
	} // getCompleteView

	/**
	 * @param d
	 *            Neuer Wert der gesetzt werden soll
	 */
	public void setValue(double d) throws MathException {
		if (isFreeze())
			return;

		functionTerm = new TermNodeNum(d);
		isValid = false;
	}// setValue

	public Object computeDummyObject(Class resultType)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, MathException {
		if (isEncapsulated()) {
			return ((PathReference) functionTerm).getObject();
		} else {
			return super.computeDummyObject(resultType);
		}
	}// computeDummyObject

	public void applyProperties(HasGraph oldObject) {
		setColor(oldObject.getColor());
		setLineStyle(oldObject.getLineStyle());
		setVisible(oldObject.isVisible());
	}
} // UserFunction
