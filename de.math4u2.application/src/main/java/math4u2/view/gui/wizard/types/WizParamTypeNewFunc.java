package math4u2.view.gui.wizard.types;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.InfixStandardFunction;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.StandardFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.gui.Math4U2Win;
import math4u2.view.gui.wizard.Math4u2WizardModel;
import math4u2.view.gui.wizard.components.CanModifyText;
import math4u2.view.gui.wizard.components.WizComboBox;
import math4u2.view.gui.wizard.step.StandardWizardStep.ParamEntry;

public class WizParamTypeNewFunc extends WizParamType {

	private Entry[] varTypes;
	private Object[] comboBoxList;
	
	public WizParamTypeNewFunc() {
		super("Neue Funktion");		
	}//Konstruktor
	
	public void createFunctionList(){
		//Funktionen holen
		List objs = Math4U2Win.getInstance().getBroker().getUnorderedContent();

		//Funktionen aussortieren ...
		LinkedList trash = new LinkedList();
		String[] ignoreList2 = { "-", "fft", "ft", "DualVectorMinus",
				"MatrixBrackets", "MatrixMinus", "VectorMinus", "brackets",
				"fouriersumme", "fouriersumme2", "not_defined_at", "polynom_derive", "diskret"};
		List ignoreList = Arrays.asList(ignoreList2);
		for (Iterator iter = objs.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof StandardFunction) {
				if (obj instanceof InfixStandardFunction)
					trash.add(obj);
				if (ignoreList.contains(((Function) obj).getKey()))
					trash.add(obj);
			} else {
				trash.add(obj);
			}
		}// for iter

		// ... und löschen und sortieren
		objs.removeAll(trash);
		
		LinkedList varTypesList = new LinkedList();
		int i = 0;
		for (Iterator iter = objs.iterator(); iter.hasNext(); i++) {
			StandardFunction func = (StandardFunction) iter.next();

			try {
				String typeStr = "";
				WizParamType type[] = new WizParamType[func.getArity()];
				for (int var = 0; var < func.getArity(); var++) {
					Class paramType = func.getVariableType(var);
					String varStr = paramType.getName();
					type[var] = WizParamType.ANY_INPUT;
					try {
						Method m = paramType.getDeclaredMethod("getTypeString",
								new Class[0]);
						varStr = (String) m.invoke(null, new Object[0]);
						if (varStr.length() == 0) {
							varStr += "   ";
							type[var] = WizParamType.SCALAR;
						} else if (varStr.equals("<vektor>")) {
							type[var] = WizParamType.VECTOR;
						} else if (varStr.equals("<dualvektor>")) {
							type[var] = WizParamType.DUALVEKTOR;
						} else if (varStr.equals("<matrix>")) {
							type[var] = WizParamType.MATRIX;
						}
					} catch (Exception e) {
					}

					if (var != 0)
						typeStr += ", " + varStr;
					else
						typeStr += varStr;

				}// for var
				
				Entry entry = new Entry(func.getSummaryText(), type, func
						.getArgumentStrings(), func.getArgumentTexts());							

				typeStr = typeStr.replaceAll("java.lang.Object", "<beliebig> ");

				String name = func.getKey().toString();				
				String nameAndParam = name + "(" + typeStr + ")";
				if(typeStr.length()==0)
					nameAndParam = name;

				entry.wizComboBox = new WizComboBox.Entry(nameAndParam, name);
				varTypesList.add(entry);
			} catch (MathException e) {
				ExceptionManager.doError(e);
			}
		}// for iter
		
		//Noch weitere Funktionen manuell hinzufügen
		
		//derive
		WizParamType[] types = new WizParamType[]{WizParamType.FUNCTION};
		Entry entry = new Entry("Berechnet die Ableitung einer Funktion", types, new String[]{"Term"}, new String[]{"Funktion die abgeleitet werden soll"});		
		entry.wizComboBox = new WizComboBox.Entry("derive()", "derive");
		varTypesList.add(entry);
		
		types = new WizParamType[]{WizParamType.FUNCTION, WizParamType.SCALAR};
		entry = new Entry("Berechnet die n.te Ableitung einer Funktion", types, new String[]{"Term","Ordnung"}, new String[]{"Funktion die abgeleitet werden soll","Ordnung der Ableitung"});		
		entry.wizComboBox = new WizComboBox.Entry("derive(  ,  )", "derive");
		varTypesList.add(entry);
		
		types = new WizParamType[]{WizParamType.FUNCTION};
		entry = new Entry("Berechnet die partielle Ableitung einer Funktion", types, new String[]{"Term"}, new String[]{"Funktion die abgeleitet werden soll"});		
		entry.wizComboBox = new WizComboBox.Entry("pderive()", "pderive");
		varTypesList.add(entry);
		
		types = new WizParamType[]{WizParamType.FUNCTION, WizParamType.VAR_NAME_LIST};
		entry = new Entry("Berechnet die partielle Ableitung einer Funktion", types, new String[]{"Term","Reihenfolge"}, new String[]{"Funktion die abgeleitet werden soll", "Reihenfolge der Ableitungen z.B. x,y,y"});		
		entry.wizComboBox = new WizComboBox.Entry("pderive(  ,  )", "pderive");
		varTypesList.add(entry);
		
		types = new WizParamType[]{WizParamType.VAR_NAME, WizParamType.SCALAR, WizParamType.SCALAR, WizParamType.FUNCTION};
		entry = new Entry("Summe", types, 
				new String[]{"Indexname","Startindex","Endindex","Elementterm"}, 
				new String[]{"Hier wird der Name für den Elementindex vereinbart.<br>Dieser wird im Elementterm benutzt.", 
							 "Index ab dem summiert werden soll",
							 "Index bis zu welchen Index summiert werden soll.",
							 "Beschreibt, wie die Elemente vom Index abhängen."});
		entry.wizComboBox = new WizComboBox.Entry("sum(  ,  ,  , )", "sum");
		varTypesList.add(entry);
		
		types = new WizParamType[]{WizParamType.VAR_NAME, WizParamType.SCALAR, WizParamType.SCALAR, WizParamType.FUNCTION};
		entry = new Entry("Produkt", types, 
				new String[]{"Indexname","Startindex","Endindex","Elementterm"}, 
				new String[]{"Hier wird der Name für den Elementindex vereinbart.<br>Dieser wird im Elementterm benutzt.", 
							 "Index ab dem multipliziert werden soll.",
							 "Index bis zu welchen Index multipliziert werden soll.",
							 "Beschreibt, wie die Elemente vom Index abhängen."});	
		entry.wizComboBox = new WizComboBox.Entry("prod(  ,  ,  , )", "prod");
		varTypesList.add(entry);
		
		Collections.sort(varTypesList);		
		varTypes = new Entry[varTypesList.size()];
		varTypes = (Entry[]) varTypesList.toArray(varTypes);
		
		LinkedList comboBoxLinkedList = new LinkedList();
		for (int j = 0; j < varTypes.length; j++) {
			comboBoxLinkedList.add(varTypes[j].wizComboBox);
		}
		comboBoxList = comboBoxLinkedList.toArray();
	}//createFunctionList

	public Entry getFunctionEntry(int selectedIndex){
		if(varTypes==null){
			createFunctionList();	
		}//if
		return varTypes[selectedIndex];
	}//getFunctionEntry
	
	public CanModifyText getComponent(String previousTitle, ParamEntry param,
			Math4u2WizardModel model) {
		if(comboBoxList==null){
			createFunctionList();
		}//if
		ListCellRenderer renderer = new ListRenderer();
		WizComboBox combo = new WizComboBox(comboBoxList);
		combo.setRenderer(renderer);
		return combo;
	}// getComponent
	
	class ListRenderer extends JLabel implements ListCellRenderer {
		public ListRenderer() {
			setOpaque(true);
		}//Konstruktor
		
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			setText(value.toString());
			if(index!=-1){
				if(varTypes[index].functionText.equals("Beschreibung nicht vorhanden"))
					setToolTipText(null);
				else
					setToolTipText("<html>"+varTypes[index].functionText+"</html>");
			}
			setBackground(isSelected ? list.getSelectionBackground() : list
					.getBackground());
			setForeground(isSelected ? list.getSelectionForeground() : list
					.getForeground());
			return this;
		}//getListCellRendererComponent
	}//class ListRenderer
	
	public static class Entry implements Comparable{
		
		public String functionText;

		public WizParamType[] argumentTypes;

		public String[] argumentStrings;

		public String[] argumentTexts;
		
		public WizComboBox.Entry wizComboBox;

		public Entry(String functionText, WizParamType[] argumentTypes,
				String[] argumentStrings, String[] argumentTexts) {
			this.functionText = functionText;
			this.argumentTypes = argumentTypes;
			this.argumentStrings = argumentStrings;
			this.argumentTexts = argumentTexts;
		}//Konstruktor

		public int compareTo(Object o) {
			Entry entry = (Entry) o;
			int test = wizComboBox.getKey().compareTo(entry.wizComboBox.getKey());
			if(test!=0) return test;
			for(int i=0; i<Math.min(argumentStrings.length, entry.argumentStrings.length); i++){
				test = argumentStrings[i].compareTo(entry.argumentStrings[i]);
				if(test!=0) return test;
			}//for i
			return 0;
		}
	}//class Entry

}//class WizParamTypeNewFunc
