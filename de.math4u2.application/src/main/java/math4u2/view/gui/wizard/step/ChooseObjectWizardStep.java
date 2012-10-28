package math4u2.view.gui.wizard.step;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import math4u2.application.resource.Images;
import math4u2.view.gui.wizard.Math4u2WizardModel;
import math4u2.view.gui.wizard.types.WizParamType;
import math4u2.view.layout.TableLayout;
import math4u2.view.layout.WrapLayout;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardModel;

public class ChooseObjectWizardStep extends PanelWizardStep {

	private Math4u2WizardModel model;
	
	public static final Entry OBJEKT_WAEHLEN = new Entry("Objekt auswählen", 'j');
	public static final Entry VEKTOR = new Entry("Vektor",'v');
	public static final Entry DUALVEKTOR = new Entry("Dualvektor", 'd');
	public static final Entry MATRIX = new Entry("Matrix", 'm');
	public static final Entry LISTE = new Entry("Liste", 'l');
	public static final Entry FOLGE = new Entry("Folge", 'o');
	public static final Entry FUNKTION = new Entry("Funktion", 'f');
	public static final Entry PUNKT = new Entry("Punkt", 'p');
	public static final Entry MARKIERUNG = new Entry("Markierung", 'a');
	public static final Entry STRECKE = new Entry("Strecke", 's');
	public static final Entry GERADE = new Entry("Gerade", 'g');
	public static final Entry KREIS = new Entry("Kreis", 'k');
	public static final Entry WINKEL = new Entry("Winkel", 'w');
	public static final Entry PARAM_KURVE = new Entry("Param. Kurve", 'r');
	public static final Entry BEZIER_KURVE = new Entry("Bezier-Kurve", 'b');
	public static final Entry KURVENZUG = new Entry("Kurvenzug", 'z');
	public static final Entry FLAECHE = new Entry("Fläche", 'ä');
	public static final Entry PFEIL = new Entry("Pfeil", 'i');
	public static final Entry PUNKTE = new Entry("Punkte", 'u');
	public static final Entry PUNKT_FOLGE = new Entry("Punktfolge(y)", 'y');
	public static final Entry BALKEN = new Entry("Balken", 'n');
	public static final Entry KARTE = new Entry("Karte", 't');
	public static final Entry VEKTORFELD = new Entry("Vektorfeld", 'e');
	public static final Entry TEXT = new Entry("Text", 'x');
	
	public static final Entry[] typeList = new Entry[]{
		OBJEKT_WAEHLEN, VEKTOR, DUALVEKTOR, MATRIX, LISTE, FOLGE,
		FUNKTION, PUNKT, MARKIERUNG, STRECKE, GERADE, KREIS, PFEIL,
		WINKEL, PARAM_KURVE, BEZIER_KURVE, KURVENZUG, FLAECHE,
		PUNKTE, PUNKT_FOLGE, BALKEN, KARTE, VEKTORFELD, TEXT
		
	};

	private JRadioButton[] radioButtons = new JRadioButton[typeList.length];

	private ButtonGroup radioButtonGroup;
	
	private int selectedIndex;
	
	private int firstSelectionIndex;
	
	private WizParamType type;
	
	public ChooseObjectWizardStep() {
		super("Objekt/Typ auswählen", "Wählen Sie aus der Liste aus");
		setComplete(true);		
	}// Konstruktor

	public void init(WizardModel wModel) {
		model = (Math4u2WizardModel) wModel;
		
		radioButtonGroup = new ButtonGroup();

		// Alle RadioButton tragen die aktuelle Auswahl ein
		ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				JRadioButton rButton = (JRadioButton) event.getSource();

				if (rButton.isSelected()) {
					setSelectedIndex(rButton);					
					model.put("model", rButton.getText());
				}
			}// itemStateChanged
		};

		// Per Tastendruck kann ebenfalls ein RadioButton ausgewählt werden.
		// Bei ENTER oder Space geht es zum nächsten Schritt.
		// Pfeiltasten können bei der Auswahl benutzt werden.
		// ESCAPE schließt das Fenster
		KeyListener keyListener = new KeyListener() {
			public void keyTyped(KeyEvent event) {
				char c = event.getKeyChar();
				int pos = -1;
				for (int i = 0; i < typeList.length; i++) {
					if(typeList[i].mnenomic == c){
						pos = i;
						break;
					}//if
				}//for i
				
				if (pos >= 0 && typeList[pos].comp.isVisible()) {
					setSelectedIndex(pos);
					return;
				}// if

				//Enter oder Space
				if (c == '\n' || c == ' ') {
					model.nextStep();
					return;
				}//if
			}// keyTyped

			public void keyPressed(KeyEvent event) {
			}//keyPressed

			public void keyReleased(KeyEvent event) {
				int size = radioButtons.length;
				if(event.getKeyCode()==KeyEvent.VK_LEFT){
					int index = getSelectedIndex();
					index = (index+size-1)%size;
					while( !typeList[index].comp.isVisible())
						index = (index+size-1)%size;
					setSelectedIndex(index);	
				}else if(event.getKeyCode()==KeyEvent.VK_RIGHT){
					int index = getSelectedIndex();
					index = (index+1)%size;
					while( !typeList[index].comp.isVisible())
						index = (index+1)%size;					
					setSelectedIndex(index);						
				}else if(event.getKeyCode()==KeyEvent.VK_ESCAPE){
					Wizard wizard = (Wizard) model.getObj("wizard");
					wizard.cancel();
				}
			}//keyReleased
		};	

		// Bei Doppelklick geht es zum nächsten Schritt
		final MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					model.nextStep();
				}// if
			}// mouseClicked
		};

		// Panel für Objekt auswählen
		JPanel selectPanel = new JPanel();
		
		// Neues Objekt
		JPanel panel = new JPanel(new WrapLayout(WrapLayout.LEFT,0,5));
		
		for (int i = 0; i < radioButtons.length; i++) {
			final JRadioButton button = new JRadioButton(typeList[i].description);
			button.setIcon(Images.TRANSPARENT_ICON);
			button.setMnemonic(typeList[i].mnenomic);			
			button.addItemListener(itemListener);
			button.addKeyListener(keyListener);
			button.addMouseListener(mouseListener);
			radioButtonGroup.add(button);
			radioButtons[i] = button;			
					
			JLabel iconLabel = new JLabel(typeList[i].image);
			iconLabel.setPreferredSize(new Dimension(30,30));
			
			MouseListener compMouseListener = new MouseAdapter(){
				public void mouseClicked(MouseEvent e) {
					button.setSelected(true);
					mouseListener.mouseClicked(e);
				}//mouseClicked
			};
			iconLabel.addMouseListener(compMouseListener);
			
			JPanel comp = new JPanel();
			comp.add(iconLabel);
			comp.add(button);
			comp.addMouseListener(compMouseListener);
			typeList[i].comp = comp;
			
			if(typeList[i].mnenomic==OBJEKT_WAEHLEN.mnenomic){
				setSelectedIndex(i);
				firstSelectionIndex = i;
				selectPanel.add(comp);				
			}else{
				button.setPreferredSize(new Dimension(75,30));
				panel.add(comp);
			}//else
		}// for i

		final JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		// TableLayout
		double P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = { { F }, {
				P, F}
		};

		setLayout(new TableLayout(size));

		add(selectPanel, 	"0, 0, L, C");
		add(scrollPane,		"0, 1, F, F");		
	}// init
	
	public void setType(WizParamType type){
		this.type = type;
		refreshTypeVisiblity();
	}//setType
	
	private void refreshTypeVisiblity(){
		for (int i = 0; i < typeList.length; i++) {
			if(type==null)
				return;
			typeList[i].comp.setVisible(type.getWizardEntries().contains(typeList[i]));			
		}//for i
	}//setType
	
	public void prepare() {
		model.put("model", radioButtons[getSelectedIndex()].getText());
		refreshTypeVisiblity();
		
		StandardWizardStep.setFirstTimeFocus(radioButtons[firstSelectionIndex]);
	}//prepare
	
	
	public int getSelectedIndex(){
		return selectedIndex;
	}//getSelectedIndex
	
	public void setSelectedIndex(int index){
		selectedIndex = index;
		radioButtons[index].setSelected(true);
		radioButtons[index].requestFocus();
	}//setSelectedIndex
	
	public void setSelectedIndex(JRadioButton rButton){
		for(int i=0; i<radioButtons.length; i++){
			if(rButton == radioButtons[i]){
				setSelectedIndex(i);
				return;
			}//i							
		}//for i	
	}//setSelectedIndex

	public void applyState() throws InvalidStateException {
	}// applyState
	
	public static Entry getType(String name){
		for (int i = 0; i < typeList.length; i++) {
			if(typeList[i].description.equals(name))
				return typeList[i];
		}
		return null;
	}//getType
	
	public static class Entry {
		public String description;
		public char mnenomic;
		public ImageIcon image;
		public Component comp;
		
		public Entry(String description, char mnenomic){
			this.description = description;
			this.mnenomic = mnenomic;
			String descriptionImage = description.replaceAll("ä", "ae").replaceAll(" ", "_");
					
			image = Images.load("math4u2/images/wizard/" + descriptionImage	+ ".gif");
		}//Konstruktor
		
	}//class Entry
}// class ChooseObjectWizardStep
