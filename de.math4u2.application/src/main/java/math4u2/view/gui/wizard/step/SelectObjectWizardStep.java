package math4u2.view.gui.wizard.step;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import math4u2.application.resource.Settings;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.reference.EvalPathStep;
import math4u2.controller.reference.MethodContext;
import math4u2.controller.reference.PathReference;
import math4u2.mathematics.collection.MathList;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.StandardFunction;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.DualVectorDoubleResult;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.results.VectorDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.parser.parser;
import math4u2.util.string.StringUtil;
import math4u2.util.swing.OvalBorder;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.Math4U2Win;
import math4u2.view.gui.listview.ListBox;
import math4u2.view.gui.listview.complete.CompleteViewBox;
import math4u2.view.gui.wizard.Math4u2WizardModel;
import math4u2.view.gui.wizard.components.WizTextField;
import math4u2.view.gui.wizard.title.SimpleHasTitle;
import math4u2.view.gui.wizard.types.WizParamType;
import math4u2.view.layout.TableLayout;

import org.pietschy.wizard.WizardModel;

/**
 * JFrame, in dem ein Objekt mit Methodenpfad selektiert werden kann.
 *  
 */
public class SelectObjectWizardStep extends StandardWizardStep {

	/**
	 * Komponenten für die Objektauswahl
	 */
	
	private DefaultListModel objListModel;

	private JList objList;
	
	private JScrollPane scrollObjekts;
	
	private WizTextField objField;

	/**
	 * Komponenten für die Methodenpfad-Auswahl
	 */

	private DefaultListModel methodListModel;

	private JList methodList;

	private JScrollPane scrollMethods;
	
	private WizTextField methodsField;
	
	/**
	 * Komponenten für das Ergebnis
	 */
	private JPanel resultPanel;
	
	private JLabel returnTypeLabel;

	public SelectObjectWizardStep() {
		super("Objekt auswählen", SimpleHasTitle.EMPTY_TITLE, "",
				"Objekt und Unterobjekte können hier ausgewählt werden.", WizParamType.OBJECT_PATH);
		
		setComplete(true);
		setIcon((ImageIcon)ChooseObjectWizardStep.getType("Objekt auswählen").image);
		resultLabel.setText(" ");
	}//Konstruktor

	public void init(WizardModel wModel) {
		model = (Math4u2WizardModel) wModel;
	}//init
	
	public void prepare() {		
		setComplete(true);
		
		//Result-Panel
		returnTypeLabel = new JLabel("");
		returnTypeLabel.setToolTipText("Rückgabetyp");

		
		JComponent gap = new JPanel();
		gap.setBackground(resultLabel.getBackground());
		
		resultPanel = new JPanel();
		returnTypeLabel.setBackground(resultLabel.getBackground());
		
		resultPanel.setBorder(new OvalBorder());		
		resultPanel.setBackground(resultLabel.getBackground());
		resultLabel.setBorder(null);

 		double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
 		double size[][] = {
 				{ P,F, P},
 				{ F} };

 		resultPanel.setLayout(new TableLayout(size));

 		resultPanel.add(resultLabel,		"0, 0, F, F");
 		resultPanel.add(gap,				"1, 0, F, F");
 		resultPanel.add(returnTypeLabel,	"2, 0, F, F");

		//Object-Liste
		List objects = Math4U2Win.getInstance().getBroker()
				.getUnorderedContent();
		
		Collections.sort(objects, new Comparator(){
			public int compare(Object first, Object second) {
				if( !(first instanceof MathObject) || !(second instanceof MathObject))
					return -1;
				String key1 = (String) ((MathObject) first).getIdentifier();
				String key2 = (String) ((MathObject) second).getIdentifier();
				return key1.compareTo(key2);
			}//compare
		});
		
		objListModel = new DefaultListModel();
		objList = new JList(objListModel);
		objList.setFocusable(false);
		for (Iterator iter = objects.iterator(); iter.hasNext();) {
			MathObject obj = (MathObject) iter.next();
			if (obj instanceof StandardFunction || obj instanceof ListBox
					|| obj instanceof CompleteViewBox)
				continue;
			objListModel.addElement(obj);
		}//for iter
		scrollObjekts = new JScrollPane(objList);

		//Objektfeld
		objField = new WizTextField();
		objField.addKeyListener(changeListener);
		objField.addKeyListener(finishOrNextListener);
		objField.addKeyListener(new NextFocusOnCharacter(new char[]{'.'}, new char[]{'(','['}){
			public JTextComponent getNextComponent(){
				return methodsField;
			}//getNextComponent
		});
		objField.addFocusListener(changeListener);
		objField.addExitListener(exitListener);

		//Methoden-Liste
		methodListModel = new DefaultListModel();
		methodList = new JList(methodListModel);
		methodList.setFocusable(false);
		methodList.setCellRenderer(new MyCellRenderer());
		scrollMethods = new JScrollPane(methodList);
		
		//Methoden-Feld
		methodsField = new WizTextField();
		methodsField.addKeyListener(finishOrNextListener);
		methodsField.addExitListener(exitListener);

		//Object-Liste Listener
		objList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				MathObject mo = (MathObject) objList.getSelectedValue();
				if(mo==null) return;
				objField.setText(mo.getIdentifier().toString());
				methodsField.setText("");
				refreshResult();
			}//valueChanged
		});

		//Objektfeld Listener
		DocumentListener changeListener = new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {doIt();}
			public void insertUpdate(DocumentEvent e) {doIt();}
			public void removeUpdate(DocumentEvent e) {doIt();}
			
			public void doIt(){
				updateAvailableMethods();
				refreshResult();
			}//doIt
		};
		objField.getDocument().addDocumentListener(changeListener);
		
		//Methoden-Liste Listener
		methodList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 2)
					return;
				if (methodList.getSelectedValue().equals("Zurück")) {
					String text = methodsField.getText();
					int pos = text.lastIndexOf(".");
					pos = Math.max(text.lastIndexOf("["), pos);
					if (pos != -1)
						methodsField.setText(text.substring(0, pos));
					else
						methodsField.setText("");
					return;
				}//if

				String m = methodsField.getText();
				String nextMethodStep = ((CollectMethodEntry)methodList.getSelectedValue()).methodName;
				
				if(m.length()!=0 && nextMethodStep.indexOf("[")==-1)
					m += ".";
				
				if(nextMethodStep.indexOf("[")==-1){					
					String text = m + nextMethodStep;
					methodsField.setText(text);
					refreshResult();
				}else{
					int[] indexRange = ((CollectMethodEntry)methodList.getSelectedValue()).indexRange;
					newIndexMethodStep(indexRange);
				}
			}//mouseClicked
		});
		
		//Methodenfeld Listener
		methodsField.getDocument().addDocumentListener(changeListener);


		double[][] size2 = {
				{ border, P, border, F, border },
				{ border, 33, 3*border, P, border, F, border*3, P, border, P, border, F, border} };

		setLayout(new TableLayout(size2));

		add(resultPanel,						"1, 1, 3, 1");
		add(new JLabel("Objektauswahl"), 		"1, 3, L, C");
		add(objField,							"3, 3, F, C");
		add(scrollObjekts, 						"1, 5, 3, 5");
		add(new JLabel("anwendbare Methoden"), 	"1, 7, L, C");
		add(methodsField,						"3, 7, F, C");
		add(new JLabel("Methodenname"),			"1, 9, L, C");
		add(new JLabel("Rückgabe"),				"3, 9, R, C");
		add(scrollMethods, 						"1, 11, 3, 11");		
		
		setFirstTimeFocus(objField);
	}//prepare

	public String getText(){
		return _refreshResult(false);
	}//getText
	
	protected String _refreshResult(boolean withHtml) {
		String m = methodsField.getText();
		String o = objField.getText();		
		
		String returnStr = "";
		if(m.length()==0){
			returnStr = markIt(o,withHtml, objField);
		}else{ 
			if(m.charAt(0)!='(' && m.charAt(0)!='[')
				returnStr = o+"."+markIt(m,withHtml, methodsField);
			else
				returnStr = o+markIt(m,withHtml, methodsField);
		}		
		return returnStr;
	}//_refreshResult
	
	/**
	 * Update des Rückgabetyps des Objekts
	 */
	private void updateReturnTypeLabel(Object mo){
		if(mo instanceof UserFunction){
			try {
				mo = ((UserFunction)mo).eval();
			} catch (MathException e) {
			}
		}//if
			
		String typeString = getResultTypeString(mo);
		if(mo==null){
			returnTypeLabel.setText( "");
		}else if(typeString!=null){
			returnTypeLabel.setText( typeString );
		}else{
			String type = mo.getClass().getName();
			int pos = type.lastIndexOf('.');
			if(pos!=-1) type = type.substring(pos);
			if(mo instanceof DrawAreaInterface)
				type = "Zeichenfläche";
			returnTypeLabel.setText( type );
		}
	}//updateReturnTypeLabel
	
	/**
	 * Überprüft, ob die Mehtode "getTypeString" vorhanden ist, und führt
	 * gegebenenfalls diese aus.
	 * 
	 * @param mo
	 *            Objekt das gefragt werden soll
	 * @return Rückgabewert der Methode und Null, falls die Methode nicht
	 *         vorhanden ist
	 */
	public static String getResultTypeString(Object mo) {
		if(mo==null) return null;
		try {
			Method method = mo.getClass().getMethod("getTypeString", new Class[0]);
			Object obj = method.invoke(mo, new Object[0]);
			if(obj instanceof String)
				return StringUtil.firstLetterToUpperCase((String) obj);
			else 
				return null;
		} catch (Exception e) {
			return null;
		}//catch
	}// getResultTypeString
	
	public void newIndexMethodStep(int[] indexRange){
		Container window = getParent();
		while(!(window instanceof Frame)){
			window = window.getParent();
		}//while
		final JDialog dialog = new JDialog((Frame)window, true);
		String title = "Index auswählen ["+indexRange[0]+" - "+indexRange[1];
		if(indexRange.length==4){
			title += ", " + indexRange[2]+" - "+indexRange[3];
		}//if
		dialog.setTitle(title+"]");
		
		double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = { { border, P, border, F, border }, {
				border, P, border, P, border}
		};

		JPanel panel = (JPanel) dialog.getContentPane();
		panel.setLayout(new TableLayout(size));

		final JTextField field = new JTextField();
		String s = indexRange[0]+"";
		field.setText(s);
		field.selectAll();
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3));
		final JButton fertig = new JButton("Fertig");
		fertig.setMnemonic('f');
		buttonPanel.add(fertig);
		final JButton abbrechen = new JButton("Abbrechen");
		abbrechen.setMnemonic('a');
		buttonPanel.add(abbrechen);
		
		ActionListener listener = new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				String text = methodsField.getText() + "["+field.getText()+"]";
				methodsField.setText(text);
				refreshResult();
				dialog.dispose();
			}//actionPerformed
			
		};
		fertig.addActionListener(listener);
		abbrechen.addActionListener(listener);
		field.addActionListener(listener);
		
		panel.add(new JLabel("Element:"), 			"1, 1, L, T");
		panel.add(field,							"3, 1, F, F");
		panel.add(buttonPanel,						"1, 3, 3, 3");
		
		dialog.setBounds(Settings.computeBounds(window, 300, 95));
		dialog.setVisible(true);		
	}//changeItem

	/**
	 * Sucht alle Methoden, die für das aktuelle Objekt aufgerufen
	 * werden können.
	 */
	public void updateAvailableMethods() {
		methodListModel.removeAllElements();
		if(methodsField.getText().length()!=0)
			methodListModel.addElement("Zurück");

		Object obj = null;
		Broker broker = Math4U2Win.getInstance().getBroker();
		String pathStr = getText();
		
		// Ist das Objekt als First-Level-Objekt bekannt?
		try {
			obj = broker.getObject(pathStr);
		} catch (BrokerException e2) {
		}
		
		// Wenn nicht, muß der Pfad geparst werden
		if (obj == null) {
			try {
				TermNode t = parser.parsePath(pathStr, broker);

				if (!(t instanceof PathReference))
					return;

				PathReference path = (PathReference) t;

				if (path.getLastStep() != null
						&& path.getLastStep() instanceof EvalPathStep)
					obj = path.getObjectWithoutLastStep();
				else
					obj = path.getObject();
			} catch (Throwable e1) {
				// ExceptionManager.doError(e1);
				updateReturnTypeLabel(null);
				return;
			}//catch
		}//if
		
		if (obj instanceof UserFunction) {
			if (((UserFunction) obj).isEncapsulated())
				try {
					obj = (MathObject) ((UserFunction) obj).eval();
				} catch (MathException e) {
					//ExceptionManager.doError(e);
				}//catch
		}//if

		SortedSet methodList = collectAllMethods(obj);

		for (Iterator iter = methodList.iterator(); iter.hasNext();) {
			methodListModel.addElement(iter.next());
		}//for iter
		
		updateReturnTypeLabel(obj);
	}//updateAvailableMethods

	private SortedSet collectAllMethods(Object mo) {
		SortedSet methodList = new TreeSet();

		collectOperators(mo, methodList);

		// getting result
		if (mo instanceof Function) {
			try {
				Object obj = ((Function) mo).eval();
				collectOperators(obj, methodList);
			} catch (Throwable e) {
				//ExceptionManager.doError(e);
			}
		}//if

		return methodList;
	}//collectAllMethods

	/**
	 * get methods beginning with 'operator_' but not with 'operator_set'
	 */
	private void collectOperators(Object obj, SortedSet set) {
		if(obj==null)
			return;
		Class theClass = obj.getClass();
		Method[] methods = theClass.getMethods();
		for (int i = 0; i < methods.length; i++) {
			String m = methods[i].getName();
			if (m.startsWith("operator_") && !m.startsWith("operator_set")) {
				String newM = m.substring("operator_".length());
				
				if(newM.equals("calceval") || newM.equals("eval") || newM.equals("derive"))
					continue;

				Class returnTypeClass = null;				
				try {
					Method retMeth = theClass.getMethod("returnType_" + newM,
							new Class[] { MethodContext.class });
					Object[] varsClass = new Object[] { null };
					
					Object theResult = retMeth.invoke(obj,
							varsClass);
					returnTypeClass = (Class) theResult;
				} catch (Throwable e) {
					//ExceptionManager.doError(e);
				}
				
				Object[] vars = new Object[0];
				int[] indexRange = null;
				if(newM.equals("index")){
					//Falls es ein Index-Step ist, weiß man auch die Argumente
					if(obj instanceof MathList){
						int firstElementIndex = ((MathList)obj).getFirstElementIndex();
						int endElementIndex = ((MathList)obj).getLastElementIndex();
						
						indexRange = new int[]{firstElementIndex, endElementIndex};
						vars = new Object[]{new ScalarDoubleResult(firstElementIndex)};
						newM = "[...]";
					}else if(obj instanceof VectorDoubleResult){
						VectorDoubleResult vdr = (VectorDoubleResult) obj;
						indexRange = new int[]{1, vdr.rowDim};
						
						ScalarDoubleResult sdr = new ScalarDoubleResult(1);
						vars = new Object[]{sdr};
						newM = "[...]";
					}else if(obj instanceof DualVectorDoubleResult){
						DualVectorDoubleResult vdr = (DualVectorDoubleResult) obj;
						indexRange = new int[]{1, vdr.colDim};
						
						ScalarDoubleResult sdr = new ScalarDoubleResult(1);
						vars = new Object[]{sdr};
						newM = "[...]";						
					}else if(obj instanceof MatrixDoubleResult){
						MatrixDoubleResult mdr = (MatrixDoubleResult) obj;
						indexRange = new int[]{1, mdr.rowDim, 1, mdr.colDim};
						
						ScalarDoubleResult sdr = new ScalarDoubleResult(1);
						vars = new Object[]{sdr, sdr};
						newM = "[... , ...]";
					}
				}//if index
				 
				String returnType;
				try {
					Object result = methods[i].invoke(obj, new Object[]{ vars});
					if(result instanceof UserFunction && returnTypeClass!=UserFunction.class){
						UserFunction func = (UserFunction) result;
						result = func.eval();						
					}//if
					
					returnType = getResultTypeString(result);
					if(returnType==null)
						returnType="";
				} catch (Exception e) {
					//ExceptionManager.doError(e);
					continue;
				}

				set.add(new CollectMethodEntry(newM, returnType, indexRange));
			}//if
		}//for i
	}//collectOperators

	private static class CollectMethodEntry implements Comparable {

		public String methodName;

		public String returnType;
		
		public int[] indexRange;
		
		/**
		 * @param indexRange
		 *            gültiges Intervall bei Index-Methode z.B. 1 bis 2 bei
		 *            2-dim Vektor oder 1 bis 2 und 1 bis 3 bei Matrix ({1,2}
		 *            bzw. {1,2,1,3})
		 */
		public CollectMethodEntry(String methodName, String returnType, int[] indexRange) {
			this.methodName = methodName;
			this.returnType = returnType;
			this.indexRange = indexRange;
		}// Konstruktor

		public boolean equals(Object o) {
			if (!(o instanceof CollectMethodEntry))
				return false;
			return methodName.equals(((CollectMethodEntry) o).methodName);
		}// equals

		public int compareTo(Object obj) {
			return methodName.compareTo(((CollectMethodEntry) obj).methodName);
		}// compareTo

	}// class CollectionMethodEntry

	private static class MyCellRenderer extends JPanel implements
			ListCellRenderer {

		private JLabel left = new JLabel();

		private JLabel right = new JLabel();

		public MyCellRenderer() {
			setOpaque(true);

			// TableLayout
			double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
			double size[][] = { { border, P, border, F, border }, { P } };

			setLayout(new TableLayout(size));

			add(left, "1, 0, L, T");
			add(right, "3, 0, R, T");
		}// MyCellRenderer

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			if ("Zurück".equals(value)) {
				left.setText("Zurück");
				right.setText("");
			} else {
				CollectMethodEntry cme = (CollectMethodEntry) value;
				left.setText(cme.methodName);

				right.setText("");
				if (cme.returnType != null)
					right.setText(cme.returnType.toString());
			}// else

			setBackground(isSelected ? list.getSelectionBackground() : list
					.getBackground());
			setForeground(isSelected ? list.getSelectionForeground() : list
					.getForeground());
			return this;
		}// getListCellRendererComponent
	}// class MyCellRenderer

}// class SelectObjectFrame
