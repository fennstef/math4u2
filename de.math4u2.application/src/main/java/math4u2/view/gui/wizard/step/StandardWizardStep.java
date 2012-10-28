package math4u2.view.gui.wizard.step;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.ParseException;
import math4u2.util.swing.OvalBorder;
import math4u2.util.swing.UnderlineHighlighter.UnderlineHighlightPainter;
import math4u2.view.gui.wizard.Math4u2WizardModel;
import math4u2.view.gui.wizard.components.CanModifyText;
import math4u2.view.gui.wizard.components.WizTextField;
import math4u2.view.gui.wizard.title.HasTitle;
import math4u2.view.gui.wizard.types.WizParamType;
import math4u2.view.layout.TableLayout;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardModel;

/**
 * Wizardstep für die Standard-Objekte wie Punkt, Pfeil etc.
 * 
 * @author Fenn Stefan
 */
public class StandardWizardStep extends PanelWizardStep {
	
	protected static final String MARK_START = "<font color=\"#6A3500\">";
	protected static final String MARK_END = "</font>";
	
	/** Hintergrundfarbe der Resultanzeige */
	protected static Color resultBackgroundColor = new Color(231,231,253);
	
	public static NextFocusOnCharacter nextFocusOnComma = new NextFocusOnCharacter();

	/** Referenz zum StepModel */
	protected Math4u2WizardModel model;

	/** Name des Objekts z.B. punkt, kreis ... */
	protected String name;
	
	/** Name des vorhergehenden Wizardtitel */
	protected HasTitle previousTitle;
	
	/** Output-Text */
	protected String output;

	/** Liste aller Parameter mit grafischer Anzeige */
	protected List params = new ArrayList();
	
	/** Typ des Endergebnisses (was in ResultLabel steht) */
	protected WizParamType resultType;

	/** Ergbnisanzeige */
	protected ResultTextField resultLabel;

	protected ChangedTextListener changeListener = new ChangedTextListener();
	
	protected FinishOrNextOnEnterListener finishOrNextListener = new FinishOrNextOnEnterListener();
	
	protected ExitListener exitListener = new ExitListener();	
	
	static{
		//Tooltips bleiben immer da
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
	}

	public StandardWizardStep(String fullname, HasTitle previousTitle, String name, WizParamType resultType) {
		this(fullname,previousTitle, name,"", resultType);
	}//Konstruktor
	
	public StandardWizardStep(String fullname, HasTitle previousTitle, String name, String description, WizParamType resultType) {
		super(" Details: "+fullname, description);
		this.previousTitle = previousTitle;
		this.name = name;
		this.resultType = resultType;
		
		if(ChooseObjectWizardStep.getType(fullname)!=null)
			setIcon((ImageIcon)ChooseObjectWizardStep.getType(fullname).image);
		
		resultLabel = new ResultTextField("text/html", "");
		resultLabel.setEditable(false);
		resultLabel.setFocusable(false);
		resultLabel.setOpaque(true);
		resultLabel.setBackground(resultBackgroundColor);
		resultLabel.setBorder(new OvalBorder());
	}//Konstruktor

	public void init(WizardModel model) {
		this.model = (Math4u2WizardModel) model;		
		setComplete(true);
		
		createComponents();

		final int ROW_COUNT = 2;
		double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;

		// Erzeuge x-Layout
		double[] xSize = { border, P, border, F, border };

		// Erzeuge y-Layout
		int rows = params.size() + 1; // +1 für das resultLabel
		double[] ySize = new double[2 * rows + 1];

		Arrays.fill(ySize, border);
		ySize[1] = 35;
		ySize[2] = 3*border;
		int i = 3;
		for (Iterator iter = params.iterator(); iter.hasNext() ; i+=2) {
			ParamEntry param = (ParamEntry) iter.next();
			ySize[i] = param.type.isFillComponent()?F:P;
		}// for i

		double size[][] = { xSize, ySize };

		setLayout(new TableLayout(size));

		//Ergebnisanzeige
		refreshResult();
		add(resultLabel, "1, 1, 3,1");

		i = 2;
		for (Iterator iter = params.iterator(); iter.hasNext(); i += 2) {
			ParamEntry param = (ParamEntry) iter.next();
			int y = (i / ROW_COUNT) * 2 + 1;
			
			param.titleComp = getTitle(param);
			add(param.titleComp, 			"1, " + y + ", L, T");
			add((JComponent) param.comp, 	"3, " + y + ", F, F");
		}// for i
	}//init
	
	protected JLabel getTitle(ParamEntry param){
		JLabel label = new JLabel(param.title);		
		String description = "";
		if(param.description!=null)
			description += "<b>Beschreibung:</b> "+param.description+"<br><br>";
		
		description += "<b>Typ:</b> "+param.type;
		label.setToolTipText("<html>"+description+"</html>");
		return label;
	}//getTitle
	
	protected CanModifyText createComponent(ParamEntry param){
		CanModifyText comp = param.type.getComponent(previousTitle.getTitle(),param, model);
		comp.addChangeListener(changeListener);
		comp.addFinishListener(finishOrNextListener);		
		comp.addFocusListener(changeListener);
		comp.addExitListener(exitListener);		
		return comp;
	}//createComponent
	
	protected void createComponents(){
		for(Iterator iter = params.iterator(); iter.hasNext();){
			ParamEntry entry = (ParamEntry) iter.next();
			CanModifyText comp = createComponent(entry);
			entry.comp = comp;
		}//for iter
	}//createComponents

	public void prepare() {
		//Fokus auf erste Komponente legen
		if(params.isEmpty()) return; 
		
		JComponent comp = (JComponent) ((ParamEntry) params.get(0)).comp;
		setFirstTimeFocus(comp);
	}//prepare
	
	public static void setFirstTimeFocus(final JComponent comp){
		//Irgendwo danach wird der Fokus nocheinmal gesetzt, darum
		//wird ein neuer Thread gestartet und nach ein paar Millisekunden
		//der Fokus gesetzt - unsauber aber funktioniert.
		(new Thread() {
			public void run() {
				try {
					sleep(50);
				} catch (InterruptedException e) {
					ExceptionManager.doError(e);
				}
				comp.requestFocus();
			}//run
		}).start();		
	}//setFirstTimeFocus

	public void applyState() throws InvalidStateException {
		model.put("output", output);
	}//applyState

	protected void refreshResult() {
		output = _refreshResult(false);
		resultLabel.setText(_refreshResult(true));
		
		refreshErrorHighlights();
	}//refreshResult
	
	protected void refreshErrorHighlights(){
		//Highlights der einzelnen Komponenten berechnen
		for(Iterator iter = params.iterator(); iter.hasNext();) {
			ParamEntry param = (ParamEntry) iter.next();
			if(param==null || param.comp==null){
				continue;
			}//if
			if(!((JComponent)param.comp).hasFocus())
				continue;
			if(!(param.comp instanceof WizTextField))
				continue;
			WizTextField field = (WizTextField) param.comp;
			field.removeHighlights();
			try {
				param.type.validate(field.getText());
			} catch (ParseException e) {
				field.highlightError(e.getErrorColumn(), field.getText().length());
			}
		}//for iter
		//Highlight des ResultLabels neu berechnen
		resultLabel.removeHighlights();
		try {
			resultType.validate(output);
		} catch (ParseException e) {
			resultLabel.highlightError(e.getErrorColumn(), output.length());
		}
	}//refreshErrorHighlights
	
	protected String _refreshResult(boolean withHtml){
		StringBuffer text = new StringBuffer(name + "(");
		for (Iterator iter = params.iterator(); iter.hasNext();) {
			ParamEntry param = (ParamEntry) iter.next();

			if(param==null || param.comp==null){
				return "";
			}
			text.append(markIt(param.comp.getText(),withHtml, param.comp));

			if (iter.hasNext())
				text.append(", ");
		}//for iter
		text.append(")");
		return text.toString();
	}//_refreshResult
	
	protected String markIt(String text, boolean withHtml, Object comp){
		if(!withHtml) return text;
		text = text.replaceAll("\\<","&lt;").replaceAll("\\>","&gt;");
		if( ((JComponent)comp).hasFocus()){			
			return MARK_START+text+MARK_END;
		}else return text;
	}//markIt

	protected void addParam(String title, String description, String value, WizParamType type) {
		ParamEntry pe = new ParamEntry(title, description, value, type,
				null);
		params.add(pe);
	}//addParam

	public void addParam(String title, String description, WizParamType type) {
		addParam(title, description, "", type);
	}//addParam
	
	public void addParam(String title, WizParamType type) {
		addParam(title, null, "", type);
	}//addParam	

	public static class ParamEntry implements HasTitle{

		/** Titel des Parameter */
		public String title;

		/** Genauere Beschreibung des Parameters */
		public String description;

		/** Aktueller Wert des Parameters */
		public String value;

		/** Typ des Parameters */
		public WizParamType type;

		/** Repräsentant des Parameters */
		public CanModifyText comp;
		
		/** Repräsentant des Titels */
		public JLabel titleComp;

		public ParamEntry(String title, String description, String value,
				WizParamType type, CanModifyText comp) {
			this.title = title;
			this.description = description;
			this.value = value;
			this.type = type;
			this.comp = comp;
		}//Konstruktor
		
		public String getTitle(){
			return title;
		}//getTitle
	}//class ParamEntry

	
	private class FinishOrNextOnEnterListener extends KeyAdapter implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			Wizard wizard = (Wizard) model.getObj("wizard");
			if(model.isNextAvailable())
				model.nextStep();
			else{
				wizard.getFinishAction().actionPerformed(null);
			}
		}//actionPerformed

		public void keyTyped(KeyEvent e) {
			if(e.getKeyChar()=='\n' && e.getModifiersEx()!=KeyEvent.CTRL_DOWN_MASK){
				actionPerformed(null);
			}			
		}//keyTyped
	}//class FinishOrNextOnEnterListener
	
	private class ChangedTextListener extends KeyAdapter implements ActionListener, FocusListener {
		public void keyReleased(KeyEvent e) {
			refreshResult();
		}//keyTyped

		public void actionPerformed(ActionEvent e) {
			refreshResult();
		}//actionPerformed

		public void focusGained(FocusEvent e) {
			refreshResult();
		}//focusGained

		public void focusLost(FocusEvent e) {
			refreshResult();
		}//focusLost
	}//class ChangedTextListener
	
	public static class NextFocusOnCharacter extends KeyAdapter {

		private char[] charsWithConsume = new char[]{','};
		private char[] charsWithoutConsume = new char[]{};
		
		public NextFocusOnCharacter(){}
		
		public NextFocusOnCharacter(char[] charsWithConsume, char[] charsWithoutConsume){
			this.charsWithConsume = charsWithConsume;
			this.charsWithoutConsume = charsWithoutConsume;
		}//Konstruktor
		
		public void keyTyped(KeyEvent e) {
			for (int i = 0; i < charsWithConsume.length; i++) {
				if(e.getKeyChar()==charsWithConsume[i]){
					JTextComponent comp = (JTextComponent) e.getSource();
					e.consume();
					comp.transferFocus();
					return;
				}//if
			}//for i
			for (int i = 0; i < charsWithoutConsume.length; i++) {
				if(e.getKeyChar()==charsWithoutConsume[i]){
					JTextComponent comp = (JTextComponent) e.getSource();
					JTextComponent next = getNextComponent();
					e.consume();
					next.setText(e.getKeyChar()+"");
					comp.transferFocus();
					return;
				}//if
			}//for i
		}//keyTyped
		
		public JTextComponent getNextComponent(){
			return null;
		}//getNextComponent
	}//class NextFocusOnCharacter
	
	protected class ExitListener extends KeyAdapter{
		public void keyReleased(KeyEvent event){
			if(event.getKeyCode()==KeyEvent.VK_ESCAPE){
				Wizard wizard = (Wizard) model.getObj("wizard");
				wizard.cancel();
			}//if
		}//keyRelaeased
	}//ExitListener
	
	class ResultTextField extends JEditorPane{
		private Highlighter.HighlightPainter painter = new UnderlineHighlightPainter(Color.RED);
		
		public ResultTextField(String type, String text) {
			super(type, text);
		}//Konstruktor
		
		public void highlightError(int start, int end) {
			// First remove all old highlights
			removeHighlights();

			try {
				Highlighter hilite = getHighlighter();
				hilite.addHighlight(start, end, painter);
			} catch (BadLocationException e) {
			}
		}//highlight

		// Removes only our private highlights
		public void removeHighlights() {
			Highlighter hilite = getHighlighter();
			Highlighter.Highlight[] hilites = hilite.getHighlights();

			for (int i = 0; i < hilites.length; i++) {
				if (hilites[i].getPainter() instanceof Highlighter.HighlightPainter) {
					hilite.removeHighlight(hilites[i]);
				}//if
			}//for i
		}//removeHighlights
	}//class ResultTextField

}//class StandardWizardStep
